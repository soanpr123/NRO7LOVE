package real.npc;

import real.func.*;
import real.item.CaiTrangData;
import real.item.Item;
import real.lucky.Lucky;
import real.magictree.MagicTree;
import real.map.MapManager;
import real.player.Player;
import server.DBService;
import server.Service;
import server.Util;
import server.io.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static real.func.SummonDragon.*;

/**
 * @author ๐ Trแบงn Lแบกi ๐
 * @copyright ๐ GirlkuN ๐
 */
public class NpcFactory {

    //npcid
    public static final byte ONG_GOHAN = 0;
    public static final byte ONG_PARAGUS = 1;
    public static final byte ONG_MOORI = 2;
    public static final byte RUONG_DO = 3;
    public static final byte DAU_THAN = 4;
    public static final byte CON_MEO = 5;
    public static final byte KHU_VUC = 6;
    public static final byte BUNMA = 7; //562
    public static final byte DENDE = 8; //350
    public static final byte APPULE = 9; //565
    public static final byte DR_DRIEF = 10;
    public static final byte CARGO = 11;
    public static final byte CUI = 12;
    public static final byte QUY_LAO_KAME = 13;
    public static final byte TRUONG_LAO_GURU = 14;
    public static final byte VUA_VEGETA = 15;
    public static final byte URON = 16;
    public static final byte BO_MONG = 17;
    public static final byte THAN_MEO_KARIN = 18;
    public static final byte THUONG_DE = 19;
    public static final byte THAN_VU_TRU = 20;
    public static final byte BA_HAT_MIT = 21; //1410
    public static final byte TRONG_TAI = 22;
    public static final byte GHI_DANH = 23;
    public static final byte RONG_THIENG = 24;
    public static final byte LINH_CANH = 25;
    public static final byte DOC_NHAN = 26;
    public static final byte RONG_THIENG_NAMEC = 27;
    public static final byte CUA_HANG_KY_GUI = 28;
    public static final byte RONG_OMEGA = 29;
    public static final byte RONG_2S = 30;
    public static final byte RONG_3S = 31;
    public static final byte RONG_4S = 32;
    public static final byte RONG_5S = 33;
    public static final byte RONG_6S = 34;
    public static final byte RONG_7S = 35;
    public static final byte RONG_1S = 36;
    public static final byte BUNMA_ = 37;
    public static final byte CALICK = 38;
    public static final byte SANTA = 39;
    public static final byte MABU_MAP = 40;
    public static final byte TRUNG_THU = 41;
    public static final byte QUOC_VUONG = 42;
    public static final byte TO_SU_KAIO = 43;
    public static final byte OSIN = 44;
    public static final byte KIBIT = 45;
    public static final byte BABIDAY = 46;
    public static final byte GIUMA_DAU_BO = 47;
    public static final byte NGO_KHONG = 48;
    public static final byte DUONG_TANG = 49;
    public static final byte QUA_TRUNG = 50;
    public static final byte DUA_HAU = 51;
    public static final byte HUNG_VUONG = 52;
    public static final byte TAPION = 53;
    public static final byte LY_TIEU_NUONG = 54;
    public static final byte BILL = 55;
    public static final byte WHIS = 56;
    public static final byte CHAMPA = 57;
    public static final byte VADOS = 58;
    public static final byte TRONG_TAI_ = 59;
    public static final byte GOKU_SSJ = 60;
    public static final byte GOKU_SSJ_ = 61;
    public static final byte POTAGE = 62;
    public static final byte JACO = 63;
    public static final byte YARIROBE = 65;
    public static final byte NOI_BANH = 66;
    public static final byte MR_POPO = 67;
    public static final byte PANCHY = 68;
    public static final byte THO_DAI_CA = 69;

    //index menu con meo
    public static final int IGNORE_MENU = 500;
    public static final int START_COMBINE = 501;
    public static final int MAKE_MATCH_PVP = 502;
    public static final int MAKE_FRIEND = 503;
    public static final int REVENGE = 504;
    public static final int TUTORIAL_SUMMON_DRAGON = 505;
    public static final int SUMMON_SHENRON = 506;

    //index menu rong thieng
    public static final int SHENRON_CONFIRM = 501;
    public static final int SHENRON_1_1 = 502;
    public static final int SHENRON_1_2 = 503;
    public static final int SHENRON_2 = 504;
    public static final int SHENRON_3 = 505;

    public static void main(String[] args) throws Exception {
        PreparedStatement ps = DBService.gI().getConnection().prepareStatement("select * from map_npc where temp_id = ?");
        ps.setInt(1, RONG_THIENG);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt(6));
        }
        rs.close();

    }

    private NpcFactory() {

    }

    public static void createNPC(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        try {
            Npc npc;
            switch (tempId) {
                case ONG_PARAGUS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 23) {
                                this.createOtherMenu(pl, 0, "Nhแบญn quร? tรขn thแปง tแบกi ฤรขy", "Nhแบญn 100K hแบฟn ", "Nhแบญn 2 Tแปi Vร?ng", "Nhแบญn ฤแป tแปญ");
                            }
                        }

                        @Override

                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 23) {
                                switch (select) {
                                    case 0:
                                        player.inventory.gem = 100000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 100K hแบฟn.");
                                        break;
                                    case 1:
                                        player.inventory.gold = 2000000000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 2 tแปi vร?ng");
                                        break;
                                    case 2:
                                        if (player.pet != null) {
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ cรณ ฤแป tแปญ rแปi!");
                                        } else {
                                            player.createPET();
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ nhแบญn ฤฦฐแปฃc ฤแป tแปญ!");

                                        }
                                }
                            }
                        }
                    };
                    break;
                case ONG_GOHAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 21) {
                                this.createOtherMenu(pl, 0, "Nhแบญn quร? tรขn thแปง tแบกi ฤรขy", "Nhแบญn 100K hแบฟn", "Nhแบญn 2 Tแปi Vร?ng", "Nhแบญn ฤแป tแปญ");
                            }
                        }

                        @Override

                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 21) {
                                switch (select) {
                                    case 0:
                                        player.inventory.gem = 100000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 100K hแบฟn.");
                                        break;
                                    case 1:
                                        player.inventory.gold = 2000000000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 2 tแปi vร?ng");
                                        break;
                                    case 2:
                                        if (player.pet != null) {
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ cรณ ฤแป tแปญ rแปi!");
                                        } else {
                                            player.createPET();
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ nhแบญn ฤฦฐแปฃc ฤแป tแปญ!");
                                        }
                                }
                            }
                        }
                    };
                    break;
                case ONG_MOORI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 22) {
                                this.createOtherMenu(pl, 0, "Nhแบญn quร? tรขn thแปง tแบกi ฤรขy", "Nhแบญn 100K hแบฟn", "Nhแบญn 2 Tแปi Vร?ng", "Nhแบญn ฤแป tแปญ");
                            }
                        }

                        @Override

                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 22) {
                                switch (select) {
                                    case 0:
                                        player.inventory.gem = 100000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 100K hแบฟn.");
                                        break;
                                    case 1:
                                        player.inventory.gold = 2000000000;
                                        Service.getInstance().sendMoney(player);
                                        Service.getInstance().sendThongBao(player, "Bแบกn vแปซa nhแบญn ฤฦฐแปฃc 2 tแปi vร?ng");
                                        break;
                                    case 2:
                                        if (player.pet != null) {
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ cรณ ฤแป tแปญ rแปi!");
                                        } else {
                                            player.createPET();
                                            Service.getInstance().sendThongBao(player, "Bแบกn ฤรฃ nhแบญn ฤฦฐแปฃc ฤแป tแปญ!");
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case RUONG_DO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player player) {
                            if (tempId == 3) {
                                try {
                                    Message m;
                                    m = new Message(-35);
                                    m.writer().writeByte(1);
                                    player.sendMessage(m);
                                    m.cleanup();
                                } catch (Exception e) {
                                }
                            } else {
                                super.openMenu(player);
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {

                        }

                    };

                    break;
                case DAU_THAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == (21 + pl.gender)) {
                                MagicTree dauThan = pl.magicTree;
                                if (dauThan.isUpdate) {
                                    int gemUp = dauThan.gemUpPea;
                                    int phantram = ((dauThan.timeUpdate - ((int) (System.currentTimeMillis() / 1000))) * 100 / dauThan.timeUpPea);
                                    if (phantram <= 80) {
                                        gemUp = (dauThan.gemUpPea * phantram / 100);
                                    }

                                    createMenuMagicTree(pl, 0, "Nรขng cแบฅp nhanh\n" + Util.powerToString(gemUp).replace(" ", "") + " ngแปc", "Hแปงy nรขng ฤแบญu\n" + Util.powerToString(dauThan.goldUpPea / 2).replace(" ", "") + " vร?ng");

                                } else {
                                    if (dauThan.currentPea == dauThan.maxPea && dauThan.level < 10) {
                                        createMenuMagicTree(pl, 0, "Thu hoแบกch", "Nรขng cแบฅp\n" + Util.powerToString(dauThan.goldUpPea).replace(" ", "") + " vร?ng");
                                    } else if (dauThan.level == 10 && dauThan.currentPea == dauThan.maxPea) {
                                        createMenuMagicTree(pl, 0, "Thu hoแบกch");
                                    } else if (dauThan.level == 10 && dauThan.currentPea != dauThan.maxPea) {
                                        createMenuMagicTree(pl, 0, "Thu hoแบกch", "Kแบฟt hแบกt nhanh\n" + dauThan.gemOnPea + " ngแปc");
                                    } else {
                                        createMenuMagicTree(pl, 0, "Thu hoแบกch", "Nรขng cแบฅp\n" + Util.powerToString(dauThan.goldUpPea).replace(" ", "") + " vร?ng", "Kแบฟt hแบกt nhanh\n" + dauThan.gemOnPea + " ngแปc");
                                    }
                                }

                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            MagicTree dauThan = player.magicTree;
                            if (player.getIndexMenu() == 0) {

                            }
                        }
                    };
                    break;
                case BUNMA_:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 102) {
                                this.createOtherMenu(pl, 0, "Cแบญu cแบงn trang bแป gรฌ cแปฉ ฤแบฟn chแป tรดi nhรฉ", "Cแปญa hร?ng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player.getIndexMenu() == 0) {
                                if (select == 0) {//Shop
                                    Shop.gI().openShop(player, this.tempId);
                                }
                            }
                        }
                    };
                    break;
                case BUNMA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 0 || this.mapId == 84) {
                                this.createOtherMenu(pl, 0, "Cแบญu cแบงn trang bแป gรฌ cแปฉ ฤแบฟn chแป tรดi nhรฉ", "Cแปญa hร?ng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player.getIndexMenu() == 0) {
                                if (select == 0) {//Shop
                                    if (player.gender == 0) {
                                        Shop.gI().openShop(player, this.tempId);
                                    } else {
                                        Npc.createMenuConMeo(player, IGNORE_MENU, 562, "Xin lแปi cฦฐng, chแป chแป bรกn ฤแป cho ngฦฐแปi Trรกi ฤแบฅt", "ฤรณng");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case THUONG_DE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                                int num=0;
                                for(int i=0;i<pl.inventory.itemsLuckyBox.size();i++){
                                    if(pl.inventory.itemsLuckyBox.get(i).id!=-1){
                                        num++;
                                    }
                                }
                            if (this.mapId == 45) {
                                this.createOtherMenu(pl, 0, "Mร?y muแปn gรฌ ", "Vรฒng quay", "Rฦฐฦกng phแปฅ \n ฤang cรณ "+num +" mรณn", "ฤรณng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {


                            System.out.println(select);
                            switch (select) {
                                case 0://Shop
                                    Lucky.gI().luckyRound(player);
                                    break;
                                case 1:
                                    Lucky.gI().openLuckyBox(player);
                                    break;
                            }
                        }

                    };
                    break;
                case DENDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 7 || this.mapId == 84) {
                                this.createOtherMenu(pl, 0, "Cแบญu cแบงn trang bแป gรฌ cแปฉ ฤแบฟn chแป tรดi nhรฉ", "Cแปญa hร?ng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player.getIndexMenu() == 0) {
                                if (select == 0) {//Shop
                                    if (player.gender == 1) {
                                        Shop.gI().openShop(player, this.tempId);
                                    } else {
                                        Npc.createMenuConMeo(player, IGNORE_MENU, 350, "Xin lแปi anh แบก, em chแป bรกn ฤแป cho dรขn tแปc Namแบฟc", "ฤรณng");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case APPULE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 14 || this.mapId == 84) {
                                this.createOtherMenu(pl, 0, "Cแบญu cแบงn trang bแป gรฌ cแปฉ ฤแบฟn chแป tรดi nhรฉ", "Cแปญa hร?ng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player.getIndexMenu() == 0) {
                                if (select == 0) {//Shop
                                    if (player.gender == 2) {
                                        Shop.gI().openShop(player, this.tempId);
                                    } else {
                                        Npc.createMenuConMeo(player, IGNORE_MENU, 565, "Vแป hร?nh tinh hแบก ฤแบณng cแปงa ngฦฐฦกi mร? mua ฤแป cรนi,rแบป rรกch nhรฉ. Tแบกi ฤรขy ta chแป bรกn ฤแป cho ngฦฐแปi Xayda thรดi", "ฤรณng");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case DR_DRIEF:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 24) {
                                this.createOtherMenu(pl, 0, "Tร?u Vลฉ Trแปฅ cแปงa ta cรณ thแป ฤฦฐa cแบญu ฤแบฟn hร?nh tinh khรกc chแป trong 3 giรขy. Cแบญu muแปn ฤi ฤรขu?", "ฤแบฟn\nNamแบฟc", "ฤแบฟn\nXayda", "Siรชu thแป");
                            } else if (this.mapId == 84) {
                                this.createOtherMenu(pl, 0, "Tร?u Vลฉ Trแปฅ cแปงa ta cรณ thแป ฤฦฐa cแบญu ฤแบฟn hร?nh tinh khรกc chแป trong 3 giรขy. Cแบญu muแปn ฤi ฤรขu?",
                                        pl.gender == 0 ? "ฤแบฟn\nTrรกi ฤแบฅt" : pl.gender == 1 ? "ฤแบฟn\nNamแบฟc" : "ฤแบฟn\nXayda");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 84) {
                                ChangeMap.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                            } else {
                                switch (select) {
                                    case 0:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 25, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                    case 1:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 26, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);

                                        break;
                                    case 2:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 84, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case CARGO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 25) {
                                this.createOtherMenu(pl, 0, "Tร?u Vลฉ Trแปฅ cแปงa ta cรณ thแป ฤฦฐa cแบญu ฤแบฟn hร?nh tinh khรกc chแป trong 3 giรขy. Cแบญu muแปn ฤi ฤรขu?", "ฤแบฟn\nTrรกi ฤแบฅt", "ฤแบฟn\nXayda", "Siรชu thแป");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            switch (select) {
                                case 0:
                                    ChangeMap.gI().changeMapBySpaceShip(player, 24, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                    break;
                                case 1:
                                    ChangeMap.gI().changeMapBySpaceShip(player, 26, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                    break;
                                case 2:
                                    ChangeMap.gI().changeMapBySpaceShip(player, 84, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                    break;
                            }
                        }
                    };
                    break;
                case CUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 19) {
                                this.createOtherMenu(pl, 0, "Cแบญu muแปn tแปi ฤรขu?", "ฤแบฟn\nCold", "ฤแบฟn\nNappa");
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, 0, "Sแปฃ chแบกy rแปi sao?", "Vแป thร?nh phแป\nVegeta");
                            } else if (this.mapId == 26) {
                                this.createOtherMenu(pl, 0, "Tร?u Vลฉ Trแปฅ Xayda Sแบฝ ฤฦฐa Ngฦฐฦกi ฤแบฟn Bแบฅt Kแปณ ฤรขu Ngฦฐฦกi Muแปn Trong 3 Giรขy!", "ฤแบฟn\nTrรกi ฤแบฅt", "ฤแบฟn\nNamec", "ฤแบฟn\nSiรชu Thแป");

                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override

                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 19) {
                                switch (select) {
                                    case 0:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 109, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                    case 1:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 68, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                }

                            }
                            if (this.mapId == 26) {
                                switch (select) {
                                    case 0:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 24, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                    case 1:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 25, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;
                                    case 2:
                                        ChangeMap.gI().changeMapBySpaceShip(player, 84, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                        break;

                                }
                            } else if (this.mapId == 68) {

                                ChangeMap.gI().changeMapBySpaceShip(player, 19, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                            } else if (this.mapId == 26) {

                            }

                        }
                    };
                    break;
                case CALICK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 28) {
                                this.createOtherMenu(pl, 0, "Ngฦฐฦกi cรณ muแปn tแปi tฦฐฦกng lai khรดng?", "Cรณ", "Khรดng");
                            } else if (this.mapId == 102) {
                                this.createOtherMenu(pl, 0, "Muแปn quay vแป hแบฃ?", "Vแป lแบกi chแป cลฉ", "Khรดng");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override

                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 28) {
                                if (select == 0) {
                                    ChangeMap.gI().changeMapBySpaceShip(player, 102, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                }

                            }
                            if (this.mapId == 102) {
                                if (select == 0) {
                                    ChangeMap.gI().changeMapBySpaceShip(player, 28, -1, -1, ChangeMap.DEFAULT_SPACE_SHIP);
                                }

                            }

                        }
                    };
                    break;
                case SANTA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                                this.createOtherMenu(pl, 0, "Xin chร?o, ta cรณ mแปt sแป vแบญt phแบฉm ฤแบทt biแปt cแบญu cรณ muแปn xem khรดng?", "Cแปญa hร?ng", "Tiแปm cแบฏt tรณc");
                            } else {
                                super.openMenu(pl);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                                switch (select) {
                                    case 0://shop
                                        Shop.gI().openShop(player, this.tempId);
                                        break;
                                    case 1:

                                        break;
                                }
                            }
                        }
                    };
                    break;
                case URON:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openMenu(Player pl) {
                            Shop.gI().openShop(pl, this.tempId);
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                        }
                    };
                    break;
                case BA_HAT_MIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openMenu(Player pl) {
                            if (this.mapId == 5) {
                                this.createOtherMenu(pl, 0, "Ngฦฐฦกi tรฌm ta cรณ viแปc gรฌ ?", "รp sao\nTrang bแป", "Pha lรช hรณa\nTrang bแป", "Chuyแปn hรณa\nTrang bแป", "Vรต ฤร?i\nSinh Tแปญ");
                            } else if (this.mapId == 112) {
                                this.createOtherMenu(pl, 0, "Ngฦฐฦกi muแปn ฤฤng kรฝ thi ฤแบฅu vรต ฤร?i? Nhiแปu phแบงn thฦฐแปng giรก trแป ฤang ฤแปฃi ngฦฐฦกi ฤรณ", "BXH", "ฤแปng รฝ \n 0 ngแปc", "Tแปซ chแปi", "Vแป ฤแบฃo rรนa");
                            } else if (this.mapId == 84 || this.mapId == 42 || this.mapId == 43) {
                                this.createOtherMenu(pl, 0, "Ngฦฐฦกi tรฌm ta cรณ viแปc gรฌ ?", "Cแปญa hร?ng\nBรนa", "Nรขng cแบฅp\nVแบญt phแบฉm", "Nรขng cแบฅp\nBรดng tai\nPorata", "Lร?m phรฉp\nNhแบญp ฤรก", "Nhแบญp\nNgแปc Rแปng");
                            } else {
                                super.openMenu(pl);
                            }
                        }


                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 5) {
                                if (player.getIndexMenu() == 0) {
                                    switch (select) {
                                        case 0:
                                            player.combine.openTabCombine(Combine.EP_SAO_TRANG_BI);
                                            break;
                                        case 1:
                                            player.combine.openTabCombine(Combine.PHA_LE_HOA_TRANG_BI);
                                            break;
                                        case 2:
                                            player.combine.openTabCombine(Combine.CHUYEN_HOA_TRANG_BI);
                                            break;
                                        case 3:
                                            ChangeMap.gI().changeMap(player, 112, -1, 50, 408, ChangeMap.NON_SPACE_SHIP);
                                            break;
                                    }
                                }
                            } else if (this.mapId == 112) {
                                if (select == 3) {
                                    ChangeMap.gI().changeMapBySpaceShip(player, 5, Util.nextInt(0, (MapManager.gI().getMapById(5).size() - 1)), 1156, ChangeMap.DEFAULT_SPACE_SHIP);
                                }
                            }
                        }
                    };
                    break;
                default:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void confirmMenu(Player player, int select) {
                            Shop.gI().openShop(player, this.tempId);
                        }
                    };
            }
        } catch (Exception e) {
        }
    }

    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                System.out.println("= = =menu: " + player.getIndexMenu() + " select: " + select);
                switch (player.getIndexMenu()) {
                    case IGNORE_MENU:

                        break;
                    case SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case SHENRON_1_1:
                        if (player.getIndexMenu() == SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            Npc.createMenuRongThieng(player, NpcFactory.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case SHENRON_1_2:
                        if (player.getIndexMenu() == SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            Npc.createMenuRongThieng(player, NpcFactory.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.getIndexMenu()) {
                    case IGNORE_MENU:

                        break;
                    case START_COMBINE:
                        if (select == 0) {
                            player.combine.startCombine();
                        }
                        break;
                    case MAKE_MATCH_PVP:
                        PVP.gI().sendInvitePVP(player, (byte) select);
                        break;
                    case MAKE_FRIEND:
                        if (select == 0) {
                            player.listPlayer.acceptMakeFriend();
                        }
                        break;
                    case REVENGE:
                        PVP.gI().acceptRevenge(player);
                        break;
                    case TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            Npc.createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case SUMMON_SHENRON:
                        if (select == 0) {
                            Npc.createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                }
            }
        };
    }

}
