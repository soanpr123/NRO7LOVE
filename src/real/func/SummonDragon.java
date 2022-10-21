package real.func;

import java.util.HashMap;
import java.util.Map;
import real.item.Item;
import real.item.ItemDAO;
import real.item.ItemData;
import real.item.ItemOption;
import real.npc.Npc;
import real.npc.NpcFactory;
import real.player.Inventory;
import real.player.Player;
import real.player.PlayerDAO;
import server.MenuController;
import server.Service;
import server.Util;
import server.io.Message;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SummonDragon {

    public static final byte WISHED = 0;
    public static final byte TIME_UP = 1;

    public static final byte DRAGON_SHENRON = 0;
    public static final byte DRAGON_PORUNGA = 1;

    public static final short NGOC_RONG_1_SAO = 14;
    public static final short NGOC_RONG_2_SAO = 15;
    public static final short NGOC_RONG_3_SAO = 16;
    public static final short NGOC_RONG_4_SAO = 17;
    public static final short NGOC_RONG_5_SAO = 18;
    public static final short NGOC_RONG_6_SAO = 19;
    public static final short NGOC_RONG_7_SAO = 20;

    public static final String SUMMON_SHENRON_TUTORIAL
            = "Có 3 cách gọi rồng thần. Gọi từ ngọc 1 sao, gọi từ ngọc 2 sao, hoặc gọi từ ngọc 3 sao\n"
            + "Các ngọc 4 sao đến 7 sao không thể gọi rồng thần được\n"
            + "Để gọi rồng 1 sao cần ngọc từ 1 sao đến 7 sao\n"
            + "Để gọi rồng 2 sao cần ngọc từ 2 sao đến 7 sao\n"
            + "Để gọi rồng 3 sao cần ngọc từ 3 sao đến 7sao\n"
            + "Điều ước rồng 3 sao: Capsule 3 sao, hoặc 2 triệu sức mạnh, hoặc 200k vàng\n"
            + "Điều ước rồng 2 sao: Capsule 2 sao, hoặc 20 triệu sức mạnh, hoặc 2 triệu vàng\n"
            + "Điều ước rồng 1 sao: Capsule 1 sao, hoặc 200 triệu sức mạnh, hoặc 20 triệu vàng, hoặc đẹp trai, hoặc....\n"
            + "Ngọc rồng sẽ mất ngay khi gọi rồng dù bạn có ước hay không\n"
            + "Quá 5 phút nếu không ước rồng thần sẽ bay mất";
    public static final String SHENRON_SAY
            = "Ta sẽ ban cho người 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định";

    public static final String[] SHENRON_1_STAR_WISHES_1
            = new String[]{"Giàu có\n+2 Tỏi\nVàng", "Găng tay\nđang mang\nlên 1 cấp", "Chí mạng\nGốc +2%",
                "Thay\nChiêu 2-3\nĐệ tử", "Điều ước\nkhác"};
    public static final String[] SHENRON_1_STAR_WISHES_2
            = new String[]{"Đẹp trai\nnhất\nVũ trụ", "Giàu có\n+10K\nNgọc", "+200 Tr\nSức mạnh\nvà tiềm\nnăng",
                "Điều ước\nkhác"};
    public static final String[] SHENRON_2_STARS_WHISHES
            = new String[]{"Giàu có\n+2000\nNgọc", "+20 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+200 Tr\nVàng"};
    public static final String[] SHENRON_3_STARS_WHISHES
            = new String[]{"Giàu có\n+200\nNgọc", "+2 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+20 Tr\nVàng"};
    //--------------------------------------------------------------------------
    private static SummonDragon instance;
    private final Map pl_dragonStar;
    private long lastTimeShenronAppeared;
    private long lastTimeShenronWait;
    private final int timeResummonShenron = 0;
    private boolean isShenronAppear;
    private final int timeShenronWait = (1000 * 5 * 60);

    private final Thread update;
    private boolean active;

    private Player playerSummonShenron;
    private int menuShenron;
    private byte select;

    private SummonDragon() {
        this.pl_dragonStar = new HashMap<>();
        this.update = new Thread(() -> {
            while (active) {
                try {
                    if (isShenronAppear) {
                        if (Util.canDoWithTime(lastTimeShenronWait, timeShenronWait)) {
                            shenronLeave(playerSummonShenron, TIME_UP);
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        });
        this.active();
    }

    private void active() {
        if (!active) {
            active = true;
            this.update.start();
        }
    }

    public static SummonDragon gI() {
        if (instance == null) {
            instance = new SummonDragon();
        }
        return instance;
    }

    public void openMenuSummonShenron(Player pl, byte dragonBallStar) {
        this.pl_dragonStar.put(pl, dragonBallStar);
        Npc.createMenuConMeo(pl, NpcFactory.SUMMON_SHENRON, -1, "Bạn muốn gọi rồng thần ?",
                "Hướng\ndẫn thêm\n(mới)", "Gọi\nRồng Thần\n" + dragonBallStar + " Sao");
    }

    public synchronized void summonShenron(Player pl) {
        if (pl.map.id == 0 || pl.map.id == 7 || pl.map.id == 14) {
            if (checkShenronBall(pl)) {
                if (Util.canDoWithTime(lastTimeShenronAppeared, timeResummonShenron)) {
                    //gọi rồng
                    playerSummonShenron = pl;
                    byte dragonStar = (byte) pl_dragonStar.get(playerSummonShenron);
                    int begin = NGOC_RONG_1_SAO;
                    switch (dragonStar) {
                        case 2:
                            begin = NGOC_RONG_2_SAO;
                            break;
                        case 3:
                            begin = NGOC_RONG_3_SAO;
                            break;
                    }
                    for (int i = begin; i <= NGOC_RONG_7_SAO; i++) {
                        pl.inventory.subQuantityItemsBag(pl.inventory.findItemBagByTemp(i), 1);
                    }
                    pl.inventory.sendItemBags();
                    activeShenron(pl, true);
                    sendWhishesShenron(pl);
                } else {
                    int timeLeft = (int) ((timeResummonShenron - (System.currentTimeMillis() - lastTimeShenronAppeared)) / 1000);
                    Service.getInstance().sendThongBao(pl, "Vui lòng đợi " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa");
                }
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Chỉ được gọi rồng thần ở ngôi làng trước nhà");
        }
    }

    private void sendWhishesShenron(Player pl) {
        byte dragonStar = (byte) pl_dragonStar.get(pl);
        switch (dragonStar) {
            case 1:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case 2:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case 3:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
        }
    }

    private void activeShenron(Player pl, boolean appear) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(appear ? 0 : (byte) 1);
            if (appear) {
                msg.writer().writeShort(pl.map.id);
                msg.writer().writeShort(pl.map.bgId);
                msg.writer().writeByte(pl.map.zoneId);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeUTF("");
                msg.writer().writeShort(pl.x);
                msg.writer().writeShort(pl.y);
                msg.writer().writeByte(DRAGON_SHENRON);
                sendNotifyShenronAppear(pl);
                lastTimeShenronWait = System.currentTimeMillis();
                isShenronAppear = true;
            }
            Service.getInstance().sendMessAllPlayer(msg);
        } catch (Exception e) {
        }
    }

    private boolean checkShenronBall(Player pl) {
        byte dragonStar = (byte) this.pl_dragonStar.get(pl);
        if (dragonStar == 1) {
            if (!pl.inventory.existItemBag(NGOC_RONG_2_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 2 sao");
                return false;
            }
            if (!pl.inventory.existItemBag(NGOC_RONG_3_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                return false;
            }
        } else if (dragonStar == 2) {
            if (!pl.inventory.existItemBag(NGOC_RONG_3_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                return false;
            }
        }
        if (!pl.inventory.existItemBag(NGOC_RONG_4_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 4 sao");
            return false;
        }
        if (!pl.inventory.existItemBag(NGOC_RONG_5_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 5 sao");
            return false;
        }
        if (!pl.inventory.existItemBag(NGOC_RONG_6_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 6 sao");
            return false;
        }
        if (!pl.inventory.existItemBag(NGOC_RONG_7_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 7 sao");
            return false;
        }
        return true;
    }

    private void sendNotifyShenronAppear(Player pl) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(pl.name + " vừa gọi rồng thần tại " + pl.map.name + " khu vực " + pl.map.zoneId);
            Service.getInstance().sendMessAllPlayerIgnoreMe(pl, msg);
            msg.cleanup();
            System.out.println("send thong bao cho toan server");
        } catch (Exception e) {
        }
    }

    public void confirmWish() {
        switch (this.menuShenron) {
            case NpcFactory.SHENRON_1_1:
                switch (this.select) {
                    case 0: //2 tỉ vàng
                        this.playerSummonShenron.inventory.gold = 2000000000;
//                        if (this.playerSummonShenron.inventory.gold > Inventory.LIMIT_GOLD) {
//                            this.playerSummonShenron.inventory.gold = Inventory.LIMIT_GOLD;
//                        }
                        this.playerSummonShenron.sendInfo();
                        break;
                    case 1: //găng tay đang đeo lên 1 cấp

                        break;
                    case 2: //chí mạng +2%
                        if (this.playerSummonShenron.point.critGoc < 9) {
                            this.playerSummonShenron.point.critGoc += 2;
                        } else {
                            for (int it = 14; it < 21; it++) {
                                PlayerDAO.AddItem_Bag_Default(this.playerSummonShenron, (short) it);
                            }
                        }
                        Service.getInstance().point(this.playerSummonShenron);
                        break;
                    case 3: //thay chiêu 2-3 đệ tử

                        break;
                }
                break;
            case NpcFactory.SHENRON_1_2:
                switch (this.select) {
                    case 0: //đẹp trai nhất vũ trụ
                        Item item = new Item();
                        item.template = ItemData.getTemplate((short) (227 + this.playerSummonShenron.gender));
                        item.content = item.getContent();
                        item.itemOptions.add(new ItemOption(77, (short) Util.nextInt(5, 25)));
                        item.itemOptions.add(new ItemOption(97, (short) Util.nextInt(1, 10)));
                        item.quantity = 1;
                        item.id = ItemDAO.create(item.template.id, item.itemOptions);
                        this.playerSummonShenron.inventory.addItemBag(item);
                        this.playerSummonShenron.inventory.sendItemBags();
                        break;
                    case 1: //+1,5 ngọc
                        this.playerSummonShenron.inventory.gem += 10000;
                        this.playerSummonShenron.sendInfo();
                        break;
                    case 2: //+200 tr smtn
                        Service.getInstance().congTiemNang(this.playerSummonShenron, (byte) 2, 200000000);
                        break;
                }
                break;
            case NpcFactory.SHENRON_2:
                switch (this.select) {
                    case 0: //+150 ngọc
                        this.playerSummonShenron.inventory.gem += 2000;
                        this.playerSummonShenron.sendInfo();
                        break;
                    case 1: //+20 tr smtn
                        Service.getInstance().congTiemNang(this.playerSummonShenron, (byte) 2, 20000000);
                        break;
                    case 2: //200 tr vàng
                        if (this.playerSummonShenron.inventory.gold > 1800000000) {
                            this.playerSummonShenron.inventory.gold = Inventory.LIMIT_GOLD;
                        } else {
                            this.playerSummonShenron.inventory.gold += 200000000;
                        }
                        this.playerSummonShenron.sendInfo();
                        break;
                }
                break;
            case NpcFactory.SHENRON_3:
                switch (this.select) {
                    case 0: //+15 ngọc
                        this.playerSummonShenron.inventory.gem += 200;
                        this.playerSummonShenron.sendInfo();
                        break;
                    case 1: //+2 tr smtn
                        Service.getInstance().congTiemNang(this.playerSummonShenron, (byte) 2, 2000000);
                        break;
                    case 2: //20tr vàng
                        if (this.playerSummonShenron.inventory.gold > (2000000000 - 20000000)) {
                            this.playerSummonShenron.inventory.gold = Inventory.LIMIT_GOLD;
                        } else {
                            this.playerSummonShenron.inventory.gold += 20000000;
                        }
                        this.playerSummonShenron.sendInfo();
                        break;
                }
                break;
        }
        shenronLeave(this.playerSummonShenron, WISHED);
    }

    public void showConfirmShenron(Player pl, int menu, byte select) {
        this.menuShenron = menu;
        this.select = select;
        String wish = null;
        switch (menu) {
            case NpcFactory.SHENRON_1_1:
                wish = SHENRON_1_STAR_WISHES_1[select];
                break;
            case NpcFactory.SHENRON_1_2:
                wish = SHENRON_1_STAR_WISHES_2[select];
                break;
            case NpcFactory.SHENRON_2:
                wish = SHENRON_2_STARS_WHISHES[select];
                break;
            case NpcFactory.SHENRON_3:
                wish = SHENRON_3_STARS_WHISHES[select];
                break;
        }
        Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_CONFIRM, "Ngươi có chắc muốn ước?", wish, "Từ chối");
    }

    public void reOpenShenronWishes(Player pl) {
        switch (menuShenron) {
            case NpcFactory.SHENRON_1_1:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case NpcFactory.SHENRON_1_2:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                break;
            case NpcFactory.SHENRON_2:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case NpcFactory.SHENRON_3:
                Npc.createMenuRongThieng(pl, NpcFactory.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
        }
    }

    public void shenronLeave(Player pl, byte type) {
        if (type == WISHED) {
            Npc.createTutorial(pl, -1, "Điều ước của ngươi đã trở thành sự thật\nHẹn gặp ngươi lần sau, ta đi nha, bái bai");
        } else {
            Npc.createMenuRongThieng(pl, NpcFactory.IGNORE_MENU, "Ta buồn ngủ quá rồi\nHẹn gặp ngươi lần sau, ta đi đây, bái bai");
        }
        activeShenron(pl, false);
        this.isShenronAppear = false;
        this.menuShenron = -1;
        this.select = -1;
        this.playerSummonShenron = null;
        lastTimeShenronAppeared = System.currentTimeMillis();
    }

    //--------------------------------------------------------------------------
}
