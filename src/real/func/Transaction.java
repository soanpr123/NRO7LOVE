package real.func;

import java.util.LinkedList;
import java.util.List;
import real.item.Item;
import real.item.ItemOption;
import real.player.Inventory;
import real.player.Player;
import server.Service;
import server.io.Message;

/**
 *
 * @author ๐ Trแบงn Lแบกi ๐
 * @copyright ๐ GirlkuN ๐
 *
 */
public class Transaction {

    private static final List<Transaction> TRANSACTIONS = new LinkedList<>();

    private static Transaction instance;

    private static final byte SEND_INVITE_TRADE = 0;
    private static final byte ACCEPT_TRADE = 1;
    private static final byte ADD_ITEM_TRADE = 2;
    private static final byte CANCEL_TRADE = 3;
    private static final byte LOCK_TRADE = 5;
    private static final byte ACCEPT = 7;

    private long tranId;
    private Player player1;
    private Player player2;
    private byte accept;

    private Transaction(Player pl1, Player pl2) {
        this.player1 = pl1;
        this.player2 = pl2;
        this.tranId = System.currentTimeMillis();
    }

    public static Transaction gI() {
        if (instance == null) {
            instance = new Transaction(null, null);
        }
        return instance;
    }

    public Transaction findTran(Player pl) {
        for (Transaction tran : TRANSACTIONS) {
            if (tran.player1.id == pl.id || tran.player2.id == pl.id) {
                return tran;
            }
        }
        return null;
    }

    public void controller(Player pl, Message message) {
        try {
            byte action = message.reader().readByte();
            int playerId = -1;
            Player plMap = null;
            Transaction tran = null;
            System.out.println("action giao dich: " + action);
            switch (action) {
                case SEND_INVITE_TRADE:
                case ACCEPT_TRADE:
                    playerId = message.reader().readInt();
                    plMap = pl.map.getPlayerInMap(playerId);
                    if (plMap != null) {
                        tran = findTran(pl);
                        if (tran == null) {
                            tran = findTran(plMap);
                        }
                        if (tran == null) {
                            if (action == SEND_INVITE_TRADE) {
                                sendInviteTrade(pl, plMap);
                            } else {
                                tran = new Transaction(pl, plMap);
                                TRANSACTIONS.add(tran);
                                tran.openTabTrade();
                            }
                        } else {
                            Service.getInstance().sendThongBao(pl, "Khรดng thแป thแปฑc hiแปn");
                        }
                    }
                    break;
                case ADD_ITEM_TRADE:
                    byte index = message.reader().readByte();
                    int quantity = message.reader().readInt();
                    addItemTrade(pl, index, quantity);
                    break;
                case CANCEL_TRADE:
                    tran = findTran(pl);
                    if (tran != null) {
                        tran.cancelTrade();
                        tran.dispose();
                        tran = null;
                    }
                    break;
                case LOCK_TRADE:
                    tran = findTran(pl);
                    if (tran != null) {
                        tran.lockTran(pl);
                    }
                    break;
                case ACCEPT:
                    tran = findTran(pl);
                    if (tran != null) {
                        tran.acceptTrade();
                        if (tran.accept == 2) {
                            tran.dispose();
                            tran = null;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addItemTrade(Player pl, byte index, int quantity) {
        if (index == -1) {
            pl.inventory.goldTrade = quantity;
        } else {
            Item item = new Item(pl.inventory.itemsBag.get(index));
            if (item.template.type == 5) {
                removeItemTrade(pl, index);
            } else {
                item.quantity = quantity != 0 ? quantity : 1;
                pl.inventory.itemsTrade.add(item);
            }
        }
    }

    private void removeItemTrade(Player pl, byte index) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(2);
            msg.writer().write(index);
            pl.sendMessage(msg);
            msg.cleanup();
            Service.getInstance().sendThongBao(pl, "Khรดng thแป giao dแปch vแบญt phแบฉm nร?y");
        } catch (Exception e) {
        }
    }

    private void lockTran(Player pl) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(6);
            if (pl.equals(player1)) {
                msg.writer().writeInt(player1.inventory.goldTrade);
                msg.writer().writeByte(player1.inventory.itemsTrade.size());
                for (Item item : player1.inventory.itemsTrade) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeByte(item.quantity);
                    msg.writer().writeByte(item.itemOptions.size());
                    for (ItemOption io : item.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
                player2.sendMessage(msg);
            } else {
                System.out.println("name trade: " + player2.name);
                msg.writer().writeInt(player2.inventory.goldTrade);
                msg.writer().writeByte(player2.inventory.itemsTrade.size());
                for (Item item : player2.inventory.itemsTrade) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeByte(item.quantity);
                    msg.writer().writeByte(item.itemOptions.size());
                    for (ItemOption io : item.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
                player1.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendInviteTrade(Player plInvite, Player plReceive) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plInvite.id);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void openTabTrade() {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player1.id);
            player2.sendMessage(msg);
            msg.cleanup();

            msg = new Message(-86);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player2.id);
            player1.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void cancelTrade() {
        String notifiText = "Giao dแปch " + this.tranId + " bแป hแปงy bแป";
        Service.getInstance().sendThongBao(player1, notifiText);
        Service.getInstance().sendThongBao(player2, notifiText);
        closeTab();
    }

    private void closeTab() {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(7);
            player1.sendMessage(msg);
            player2.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void acceptTrade() {
        this.accept++;
        if (this.accept == 2) {
            this.trade();
        }
    }

    private void trade() {
        byte tradeStatus = 0;
        List<Item> itemsBag1 = player1.inventory.copyItemsBag();
        List<Item> itemsBag2 = player2.inventory.copyItemsBag();

        List<Item> itemsTrade1 = player1.inventory.itemsTrade;
        List<Item> itemsTrade2 = player2.inventory.itemsTrade;
        int goldTrade1 = player1.inventory.goldTrade;
        int goldTrade2 = player2.inventory.goldTrade;

        for (Item itemBag : itemsBag1) {
            for (Item itemTrade : itemsTrade1) {
                if (itemBag.id == itemTrade.id) {
                    player1.inventory.subQuantityItem(itemsBag1, itemBag, itemTrade.quantity);
                    break;
                }
            }
        }

        for (Item itemBag : itemsBag2) {
            for (Item itemTrade : itemsTrade2) {
                if (itemBag.id == itemTrade.id) {
                    player2.inventory.subQuantityItem(itemsBag2, itemBag, itemTrade.quantity);
                    break;
                }
            }
        }

        if (player1.inventory.gold + goldTrade2 > Inventory.LIMIT_GOLD) {
            tradeStatus = 1;
        } else if (player2.inventory.gold + goldTrade1 > Inventory.LIMIT_GOLD) {
            tradeStatus = 2;
        }
        if (tradeStatus != 0) {
            sendNotifyTrade(tradeStatus);
        } else {
            for (Item item : itemsTrade2) {
                if (!player1.inventory.addItemList(itemsBag1, item)) {
                    tradeStatus = 3;
                    break;
                }
            }
            if (tradeStatus != 0) {
                sendNotifyTrade(tradeStatus);
            } else {
                for (Item item : itemsTrade1) {
                    if (!player2.inventory.addItemList(itemsBag2, item)) {
                        tradeStatus = 4;
                        break;
                    }
                }
                if (tradeStatus == 0) {
                    player1.inventory.gold += goldTrade2;
                    player2.inventory.gold += goldTrade1;
                    player1.inventory.gold -= goldTrade1;
                    player2.inventory.gold -= goldTrade2;
                    player1.inventory.itemsBag = itemsBag1;
                    player2.inventory.itemsBag = itemsBag2;
                    player1.inventory.sendItemBags();
                    player2.inventory.sendItemBags();
                    player1.sendInfo();
                    player2.sendInfo();
                }
                sendNotifyTrade(tradeStatus);
            }
        }

    }

    private void sendNotifyTrade(byte status) {
        switch (status) {
            case 0:
                Service.getInstance().sendThongBao(player1, "Giao dแปch thร?nh cรดng");
                Service.getInstance().sendThongBao(player2, "Giao dแปch thร?nh cรดng");
                break;
            case 1:
                Service.getInstance().sendThongBao(player1, "Giao dแปch thแบฅt bแบกi do sแป lฦฐแปฃng vร?ng sau giao dแปch vฦฐแปฃt tแปi ฤa");
                Service.getInstance().sendThongBao(player2, "Giao dแปch thแบฅt bแบกi do sแป lฦฐแปฃng vร?ng " + player1.name + " sau giao dแปch vฦฐแปฃt tแปi ฤa");
                break;
            case 2:
                Service.getInstance().sendThongBao(player2, "Giao dแปch thแบฅt bแบกi do bแบกn ฤรฃ lฦฐแปฃng vร?ng ฤรฃ tแปi giแปi hแบกn");
                Service.getInstance().sendThongBao(player1, "Giao dแปch thแบฅt bแบกi do sแป lฦฐแปฃng vร?ng " + player2.name + " sau giao dแปch vฦฐแปฃt tแปi ฤa");
                break;
            case 3:
                Service.getInstance().sendThongBao(player1, "Giao dแปch thแบฅt bแบกi do khรดng ฤแปง รด trแปng hร?nh trang");
                Service.getInstance().sendThongBao(player2, "Giao dแปch thแบฅt bแบกi do " + player1.name + " khรดng ฤแปง chแป รด hร?nh trang");
                break;
            case 4:
                Service.getInstance().sendThongBao(player2, "Giao dแปch thแบฅt bแบกi do khรดng ฤแปง รด trแปng hร?nh trang");
                Service.getInstance().sendThongBao(player1, "Giao dแปch thแบฅt bแบกi do " + player2.name + " khรดng ฤแปง chแป รด hร?nh trang");
                break;
        }
    }

    private void dispose() {
        this.player1.inventory.itemsTrade.clear();
        this.player2.inventory.itemsTrade.clear();
        this.player1.inventory.goldTrade = 0;
        this.player2.inventory.goldTrade = 0;
        this.player1 = null;
        this.player2 = null;
        TRANSACTIONS.remove(this);
    }

}
