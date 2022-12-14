package real.func;

import java.util.ArrayList;
import java.util.List;
import real.item.Item;
import real.item.ItemData;
import real.item.ItemOption;
import real.npc.Npc;
import real.npc.NpcFactory;
import real.player.Player;
import server.Service;
import static server.Service.numberToMoney;
import server.Util;
import server.io.Message;

/**
 *
 * @author ๐ Trแบงn Lแบกi ๐
 * @copyright ๐ GirlkuN ๐
 *
 */
public final class Combine {

    public static final byte EP_SAO_TRANG_BI = 10;
    public static final byte PHA_LE_HOA_TRANG_BI = 11;
    public static final byte CHUYEN_HOA_TRANG_BI = 12;

    public static final byte OPEN_TAB_COMBINE = 0;
    public static final byte READD_ITEM_TAB = 1;
    public static final byte COMBINE_SUCCESS = 2;
    public static final byte COMBINE_FAIL = 3;
    public static final byte COMBINE_UPGRADE = 4;
    public static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    private Player player;

    private byte typeCombine;
    private byte tiLeCombine;
    private int giaGoldCombine;
    private int giaGemCombine;
    private final List<Item> itemCombine;

    public Combine(Player player) {
        this.player = player;
        itemCombine = new ArrayList<>();
        setUncombine();
    }

    public void setUncombine() {
        itemCombine.clear();
        typeCombine = -1;
        tiLeCombine = 0;
        giaGoldCombine = 0;
        giaGemCombine = 0;
    }

    public byte getTypeCombine() {
        return this.typeCombine;
    }

    public void setTypeCombine(byte type) {
        this.typeCombine = type;
    }

    public byte getTileCombine() {
        return this.tiLeCombine;
    }

    public void setTiLeCombine(byte tiLe) {
        this.tiLeCombine = tiLe;
    }

    public int getGoldCombine() {
        return this.giaGoldCombine;
    }

    public void setGoldCombine(int gia) {
        this.giaGoldCombine = gia;
    }

    public int getGemCombine() {
        return this.giaGemCombine;
    }

    public void setGemCombine(int gia) {
        this.giaGemCombine = gia;
    }

    public List<Item> getItemCombine() {
        return this.itemCombine;
    }

    public void openTabCombine(byte type) {
        setTypeCombine(type);
        switch (type) {
            case EP_SAO_TRANG_BI:
                doCombine(OPEN_TAB_COMBINE, "Vร?o hร?nh trang\n"
                        + "Chแปn trang bแป\n"
                        + "(รo, quแบงn, gฤng, giร?y hoแบทc rada) cรณ รด ฤแบทt sao pha lรช\n"
                        + "Chแปn loแบกi sao pha lรช\n"
                        + "Sau ฤรณ chแปn 'Nรขng cแบฅp'", "Ta sแบฝ phรน phรฉp\n"
                        + "cho trang bแป cแปงa ngฦฐฦกi\n"
                        + "trแป nรชn mแบกnh mแบฝ");
                break;
            case PHA_LE_HOA_TRANG_BI:
                doCombine(OPEN_TAB_COMBINE, "Vร?o hร?nh trang\n"
                        + "Chแปn trang bแป\n"
                        + "(รo, quแบงn, gฤng, giร?y hoแบทc rada)\n"
                        + "Sau ฤรณ chแปn 'Nรขng cแบฅp'", "Ta sแบฝ phรน phรฉp\n"
                        + "cho trang bแป cแปงa ngฦฐฦกi\n"
                        + "trแป thร?nh trang bแป pha lรช");
                break;
            case CHUYEN_HOA_TRANG_BI:
                doCombine(OPEN_TAB_COMBINE, "Vร?o hร?nh trang\n"
                        + "Chแปn trang bแป gแปc\n"
                        + "(รo,quแบงn,gฤng,giร?y hoแบทc rada)\n"
                        + "tแปซ cแบฅp [+4] trแป lรชn\n"
                        + "Chแปn tiแบฟp trang bแป mแปi\n"
                        + "chฦฐa nรขng cแบฅp cแบงn nhแบญp thแป\n"
                        + "sau ฤรณ chแปn 'Nรขng cแบฅp'", "Lฦฐu รฝ trang bแป mแปi\n"
                        + "phแบฃi hฦกn trang bแป gแปc\n"
                        + "1 bแบญc");
                break;
        }
    }

    public void startCombine() {
        switch (typeCombine) {
            case EP_SAO_TRANG_BI:
                doCombine(isSuccessCombine(), null, null);
                break;
            case PHA_LE_HOA_TRANG_BI:
                byte result = player.combine.isSuccessCombine();
                doCombine(result, null, null);
                break;
        }
        player.inventory.sendItemBags();
        doCombine(READD_ITEM_TAB, null, null); //load lแบกi info item trong tab nรขng cแบฅp
    }

    public void doCombine(int type, String info, String infoTop, short... iconId) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(type);
            switch (type) {
                case OPEN_TAB_COMBINE:
                    msg.writer().writeUTF(info);
                    msg.writer().writeUTF(infoTop);
                    break;
                case READD_ITEM_TAB:
                    msg.writer().writeByte(getItemCombine().size());
                    for (Item it : getItemCombine()) {
                        for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                            if (it == player.inventory.itemsBag.get(j)) {
                                msg.writer().writeByte(j);
                            }
                        }
                    }
                    break;
                case COMBINE_SUCCESS:
                    //thร?nh cรดng
                    break;
                case COMBINE_FAIL:
                    //xแปt
                    break;
                case COMBINE_UPGRADE:
                    msg.writer().writeShort(iconId[0]);
                    break;
                case COMBINE_DRAGON_BALL:
                    msg.writer().writeShort(iconId[0]);
                    break;
                case OPEN_ITEM:
                    msg.writer().writeShort(iconId[0]);
                    msg.writer().writeShort(iconId[1]);
                    break;
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private byte isSuccessCombine() {
        byte success = COMBINE_FAIL;
        boolean isHaveStar = false;

        int tiLe = getTileCombine();
        int giaDap = getGoldCombine();
        player.inventory.gold -= giaDap;
        Service.getInstance().sendMoney(player);
        System.out.println("type combine: " + this.getTypeCombine());
        switch (getTypeCombine()) {
            case EP_SAO_TRANG_BI:
                Item trangBi = null,
                 daPhaLe = null;
                for (Item item : getItemCombine()) {
                    if (isItemCanCombine(item)) {
                        trangBi = item;
                    }
                    if (isDaPhaLe(item)) {
                        daPhaLe = item;
                    }
                }
                addParam(trangBi, getOptionDaPhaLe(daPhaLe), getParamDaPhaLe(daPhaLe));
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        io.param++;
                        isHaveStar = true;
                        break;
                    }
                }
                if (!isHaveStar) {
                    trangBi.itemOptions.add(new ItemOption(102, (short) 1));
                }
                player.inventory.subQuantityItemsBag(daPhaLe, 1);
                if (daPhaLe.quantity == 0) {
                    getItemCombine().remove(daPhaLe);
                }
                success = COMBINE_SUCCESS;
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (Util.isTrue(tiLe)) {
                    Item item = player.combine.getItemCombine().get(0);
                    for (ItemOption itemOption : item.itemOptions) {
                        if (itemOption.optionTemplate.id == 107) {
                            itemOption.param++;
                            isHaveStar = true;
                            break;
                        }
                    }
                    if (!isHaveStar) {
                        item.itemOptions.add(new ItemOption(107, (short) 1));
                    }
                    success = COMBINE_SUCCESS;
                } else {
                    success = COMBINE_FAIL;
                }
                break;
        }
        return success;
    }

    public void showInfoCombine(int... index) {
        if ((player.timeLimit[0] + 1000) > System.currentTimeMillis()) {
            Service.getInstance().sendThongBao(player, "Bแบกn thao tรกc quรก nhanh");
            return;
        }
        player.timeLimit[0] = System.currentTimeMillis();
        itemCombine.clear();
        if (index.length > 0) {
            try {
                Item[] items = new Item[index.length];
                for (int i = 0; i < items.length; i++) {
                    items[i] = player.inventory.itemsBag.get(index[i]);
                    player.combine.getItemCombine().add(items[i]);
                }
                switch (player.combine.getTypeCombine()) {
                    case EP_SAO_TRANG_BI:
                        if (items.length != 2) {
                            Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, "Cแบงn 1 trang bแป cรณ lแป sao pha lรช vร? 1 loแบกi ngแปc ฤแป รฉp vร?o.", "ฤรณng");
                        } else {
                            Item trangBi = null, daPhaLe = null;
                            for (Item item : items) {
                                if (isItemCanCombine(item)) {
                                    trangBi = item;
                                }
                                if (isDaPhaLe(item)) {
                                    daPhaLe = item;
                                }
                            }
                            if (trangBi == null || daPhaLe == null || starSlot(trangBi) == maxStarSlot(trangBi)) {
                                Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, "Cแบงn 1 trang bแป cรณ lแป sao pha lรช vร? 1 loแบกi ngแปc ฤแป รฉp vร?o.", "ฤรณng");
                            } else {
                                String npcSay = trangBi.template.name + "\n|0|";
                                for (ItemOption io : trangBi.itemOptions) {
                                    npcSay += io.optionTemplate.name.replaceAll("#", io.param + "") + "\n";
                                }
                                if (daPhaLe.template.type == 30) {
                                    for (ItemOption io : daPhaLe.itemOptions) {
                                        npcSay += io.optionTemplate.name.replaceAll("#", io.param + "") + "\n";
                                    }
                                } else {
                                    npcSay += ItemData.getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                                }
                                this.setGemCombine(10);
                                npcSay += "|2|Cแบงn 10 ngแปc";
                                if (player.inventory.gold >= this.getGemCombine()) {

                                    Npc.createMenuConMeo(player, NpcFactory.START_COMBINE, 1410, npcSay, "Nรขng cแบฅp\n10 ngแปc", "Tแปซ chแปi");
                                } else {
                                    Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, npcSay, "Cรฒn thiแบฟu\n" + (getGemCombine() - player.inventory.gem) + " ngแปc", "ฤรณng");
                                }

                            }
                        }
                        break;
                    case PHA_LE_HOA_TRANG_BI:
                        if (items.length > 1) {
                            Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, "Chแปn 1 vแบญt phแบฉm thรดi anh bแบกn!", "ฤรณng");
                        } else {
                            if (isItemCanCombine(items[0])) {
                                int star = 0;
                                for (ItemOption itemOption : items[0].itemOptions) {
                                    if (itemOption.optionTemplate.id == 107) {
                                        star += itemOption.param;
                                    }
                                }
                                if (star == 7) {
                                    Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, "Vแบญt phแบฉm ฤรฃ tแปi ฤa sao pha lรช", "ฤรณng");
                                } else {
                                    setGoldCombine(getGiaDapDo(star));
                                    setTiLeCombine((byte) getTiLeDapDo(star));

                                    String npcSay = items[0].template.name + "\n|0|";
                                    for (ItemOption io : items[0].itemOptions) {
                                        npcSay += io.optionTemplate.name.replaceAll("#", io.param + "") + "\n";
                                    }
                                    if (player.inventory.gold < this.giaGoldCombine) {
                                        npcSay += "|2|Tแป lแป thร?nh cรดng: " + this.tiLeCombine + "%\n|7|Cแบงn " + numberToMoney(this.giaGoldCombine) + " vร?ng";

                                        Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, npcSay, "Cรฒn thiแบฟu\n" + numberToMoney(this.giaGoldCombine - player.inventory.gold) + "\nvร?ng");

                                    } else {
                                        npcSay += "|2|Tแป lแป thร?nh cรดng: " + this.tiLeCombine + "%\nCแบงn " + numberToMoney(this.giaGoldCombine) + " vร?ng";
                                        Npc.createMenuConMeo(player, NpcFactory.START_COMBINE, 1410, npcSay, "ฤแบญp");
                                    }
                                }
                            } else {
                                Npc.createMenuConMeo(player, NpcFactory.IGNORE_MENU, 1410, "Vui lรฒng chแปn ฤรบng trang bแป.", "ฤรณng");
                            }
                        }
                        break;
                    case CHUYEN_HOA_TRANG_BI:
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getGiaDapDo(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 10000000;
            case 2:
                return 20000000;
            case 3:
                return 40000000;
            case 4:
                return 60000000;
            case 5:
                return 120000000;
            case 6:
                return 240000000;
        }
        return 0;
    }

    private int getTiLeDapDo(int star) {
        switch (star) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 5;
            case 5:
                return 2;
            case 6:
                return 1;
        }
        return 0;
    }

    private byte starSlot(Item item) {
        byte star = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 102) {
                star = (byte) io.param;
            }
        }
        return star;
    }

    private byte maxStarSlot(Item item) {
        byte star = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 107) {
                star = (byte) io.param;
            }
        }
        return star;
    }

    private boolean isItemCanCombine(Item item) {
        return item.template.type == 0 || item.template.type == 1 || item.template.type == 2
                || item.template.type == 3 || item.template.type == 4 || item.template.type == 6;
    }

    private boolean isDaPhaLe(Item item) {
        return item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20);
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5;
            case 19:
                return 5;
            case 18:
                return 5;
            case 17:
                return 5;
            case 16:
                return 3;
            case 15:
                return 2;
            case 14:
                return 2;
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 47;
            case 14:
                return 108;
            default:
                return -1;
        }
    }

    private void addParam(Item item, int optionId, int param) {
        boolean flag = false;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == optionId) {
                io.param += param;
                flag = true;
                break;
            }
        }
        if (!flag) {
            item.itemOptions.add(new ItemOption(optionId, (short) param));
        }
    }
}
