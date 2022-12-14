package real.func;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import real.npc.Npc;
import real.npc.NpcFactory;
import real.player.Player;
import server.Service;
import server.Util;
import server.io.Message;

/**
 *
 * @author ๐ Trแบงn Lแบกi ๐
 * @copyright ๐ GirlkuN ๐
 *
 */
public class PVP {

    public static final byte TYPE_DIE = 0;
    public static final byte TYPE_LEAVE_MAP = 1;

    private static final byte OPEN_GOLD_SELECT = 0;
    private static final byte ACCEPT_PVP = 1;

    private static final List<PVP> PVPS = new LinkedList<>();
    private static final Map COUPLE_PVP = new HashMap();
    private static final Map PL_GOLD = new HashMap();

    private static PVP instance;

    private long pvpId;
    public Player player1;
    public Player player2;
    private int totalGold;

    private PVP() {

    }

    private PVP(Player pl1, Player pl2) {
        this.player1 = pl1;
        this.player2 = pl2;
        this.pvpId = System.currentTimeMillis();
        this.totalGold = (int) PL_GOLD.get(pl1) * 2 * 99 / 100;
    }

    public static PVP gI() {
        if (instance == null) {
            instance = new PVP();
        }
        return instance;
    }

    public void controller(Player pl, Message message) {
        try {
            byte action = message.reader().readByte();
            byte type = message.reader().readByte();
            int playerId = message.reader().readInt();
            Player plMap = pl.map.getPlayerInMap(playerId);
            COUPLE_PVP.put(pl, plMap);
            COUPLE_PVP.put(plMap, pl);
            switch (action) {
                case OPEN_GOLD_SELECT:
                    openSelectGold(pl, plMap);
                    break;
                case ACCEPT_PVP:
                    acceptPVP(pl);
                    break;
            }
        } catch (IOException ex) {

        }
    }

    public PVP findPvp(Player player) {
        for (PVP pvp : PVPS) {
            if (pvp.player1.id == player.id || pvp.player2.id == player.id) {
                return pvp;
            }
        }
        return null;
    }

    public void finishPVP(Player plLose, byte typeWin) {
        if (plLose.typePk != 0) {
            Player plWin = (Player) COUPLE_PVP.get(plLose);
            COUPLE_PVP.put(plLose, null);
            if (plWin != null) {
                COUPLE_PVP.put(plWin, null);
            }
            PVP pvp = findPvp(plLose);
            if (pvp != null) {
                Service.getInstance().changeTypePK(pvp.player1, 0);
                Service.getInstance().changeTypePK(pvp.player2, 0);
                if (pvp.player1.equals(plLose)) {
                    pvp.sendResultMatch(pvp.player2, pvp.player1, typeWin);
                } else {
                    pvp.sendResultMatch(pvp.player1, pvp.player2, typeWin);
                }
                pvp.dispose();
                PVPS.remove(pvp);
                pvp = null;
            }
        }
    }

    private void acceptPVP(Player pl) {
        Player pl2 = (Player) COUPLE_PVP.get(pl);
        if (pl2 != null) {
            if (pl.map.equals(pl2.map)) {
                PVP pvp = new PVP(pl, pl2);
                PVPS.add(pvp);
                pvp.start();
            } else {
                Service.getInstance().sendThongBao(pl, "ฤแปi thแปง ฤรฃ rแปi khแปi map");
            }
        }
    }

    private void start() {
        int gold = (int) PL_GOLD.get(this.player1);
        this.player1.inventory.gold -= gold;
        this.player2.inventory.gold -= gold;
        this.player1.sendInfo();
        this.player2.sendInfo();
        Service.getInstance().changeTypePK(this.player1, 3);
        Service.getInstance().changeTypePK(this.player2, 3);
    }

    private void openSelectGold(Player pl, Player plMap) {
        PVP pvp1 = findPvp(pl);
        PVP pvp2 = findPvp(plMap);
        if (pvp1 == null && pvp2 == null) {
            Npc.createMenuConMeo(pl, NpcFactory.MAKE_MATCH_PVP, -1, plMap.name + " (sแปฉc mแบกnh " + Service.numberToMoney(plMap.point.power) + ")\nBแบกn muแปn cฦฐแปฃc bao nhiรชu vร?ng?",
                    "1000\nvร?ng", "10000\nvร?ng", "100000\nvร?ng");
        } else {
            Service.getInstance().hideInfoDlg(pl);
            Service.getInstance().sendThongBao(pl, "Khรดng thแป thแปฑc hiแปn");
        }
    }

    public void openSelectRevenge(Player pl, Player enemy) {
        COUPLE_PVP.put(pl, enemy);
        COUPLE_PVP.put(enemy, pl);
        PL_GOLD.put(pl, 0);
        PL_GOLD.put(enemy, 0);
        PVP pvp1 = findPvp(pl);
        PVP pvp2 = findPvp(enemy);
        if (pvp1 == null && pvp2 == null) {
            Npc.createMenuConMeo(pl, NpcFactory.REVENGE, -1, "Bแบกn muแปn ฤแบฟn ngay chแป hแบฏn, phรญ lร? 1 ngแปc vร? ฤฦฐแปฃc tรฌm thoแบฃi mรกi trong 5 phรบt nhรฉ", "Ok", "Tแปซ chแปi");
        } else {
            Service.getInstance().hideInfoDlg(pl);
            Service.getInstance().sendThongBao(pl, "Khรดng thแป thแปฑc hiแปn");
        }
    }

    public void acceptRevenge(Player pl) {
        if (pl.inventory.getGemAndRuby() > 0) {
            pl.inventory.subGemAndRuby(1);
            pl.sendInfo();
            Player enemy = (Player) COUPLE_PVP.get(pl);
            if (enemy != null) {
                PVP pvp = new PVP(pl, enemy);
                PVPS.add(pvp);
                ChangeMap.gI().changeMap(pl, enemy.map.id, enemy.map.zoneId, enemy.x + Util.nextInt(-5, 5), enemy.y, ChangeMap.NON_SPACE_SHIP);
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }
                    Service.getInstance().chat(pl, "Mau ฤแปn tแปi");
                    pvp.start();
                }).start();
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Bแบกn khรดng ฤแปง ngแปc, cรฒn thiแบฟu 1 ngแปc nแปฏa");
        }
    }

    public void sendInvitePVP(Player pl, byte selectGold) {
        Message msg;
        try {
            Player plReceive = (Player) COUPLE_PVP.get(pl);
            if (plReceive != null) {
                int gold = selectGold == 0 ? 1000 : selectGold == 1 ? 10000 : 100000;
                if (pl.inventory.gold >= gold) {
                    if (plReceive.inventory.gold < gold) {
                        Service.getInstance().sendThongBao(pl, "ฤแปi thแปง chแป cรณ " + plReceive.inventory.gold + " vร?ng, khรดng ฤแปง tiแปn cฦฐแปฃc");
                    } else {
                        PL_GOLD.put(pl, gold);
                        PL_GOLD.put(plReceive, gold);
                        msg = new Message(-59);
                        msg.writer().writeByte(3);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeInt(gold);
                        msg.writer().writeUTF(pl.name + " (sแปฉc mแบกnh " + Service.numberToMoney(pl.point.power) + ") muแปn thรกch ฤแบฅu bแบกn vแปi mแปฉc cฦฐแปฃc " + gold);
                        plReceive.sendMessage(msg);
                        msg.cleanup();
                    }
                } else {
                    Service.getInstance().sendThongBao(pl, "Bแบกn chแป cรณ " + pl.inventory.gold + " vร?ng, khรดng ฤแปง tiแปn cฦฐแปฃc");
                }
            }
        } catch (Exception e) {
        }
    }

    private void sendResultMatch(Player winer, Player loser, byte typeWin) {
        winer.inventory.gold += this.totalGold;
        winer.sendInfo();
        switch (typeWin) {
            case TYPE_DIE:
                Service.getInstance().sendThongBao(winer, "ฤแปi thแปญ ฤรฃ kiแปt sแปฉc, bแบกn thแบฏng ฤฦฐแปฃc " + this.totalGold + " vร?ng");
                Service.getInstance().sendThongBao(loser, "Bแบกn ฤรฃ thua vรฌ ฤรฃ kiแปt sแปฉc");
                break;
            case TYPE_LEAVE_MAP:
                Service.getInstance().sendThongBao(winer, "ฤแปi thแปง sแปฃ quรก bแป chแบกy, bแบกn thแบฏng ฤฦฐแปฃc " + this.totalGold + " vร?ng");
                Service.getInstance().sendThongBao(loser, "ฤแบกn bแป xแปญ thua vรฌ ฤรฃ bแป chแบกy");
                break;
        }
    }

    private void dispose() {
        this.player1 = null;
        this.player2 = null;
    }
}
