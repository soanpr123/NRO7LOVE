package tool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Random;
import real.item.ItemData;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class WriteData {

    public static void main(String[] args) throws Exception {
        writeMenuNpc(21, 5, "Đập đá... à nhầm, đập đồ đeeeeee...!", "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Chuyển\nhóa\ntrang bị");
        writeMenuNpc(39, 5, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(16, 24, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(16, 25, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(16, 26, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(16, 84, "Anh bạn muốn gì à?", "Cửa hàng");

        writeMenuNpc(7, 0, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(7, 84, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(8, 7, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(8, 84, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(9, 14, "Anh bạn muốn gì à?", "Cửa hàng");
        writeMenuNpc(9, 84, "Anh bạn muốn gì à?", "Cửa hàng");

        writeMenuNpc(21, 42, "Anh bạn muốn gì à?", "Ghép đồ");
        writeMenuNpc(21, 44, "Anh bạn muốn gì à?", "Ghép đồ");
        writeMenuNpc(21, 43, "Anh bạn muốn gì à?", "Ghép đồ");

        writeMenuNpc(10, 24, "Anh bạn muốn gì à?", "Đến\nNamếc", "Đến\nXayda", "Đến\nsiêu thị");
        writeMenuNpc(11, 25, "Anh bạn muốn gì à?", "Đến\nTrái đất", "Đến\nXayda", "Đến\nsiêu thị");
        writeMenuNpc(12, 26, "Anh bạn muốn gì à?", "Đến\nTrái đất", "Đến\nNamếc", "Đến\nsiêu thị");
        writeMenuNpc(10, 84, "Anh bạn muốn gì à?", "Quay về\ntrạm tàu\nvũ trụ");
    }

    private static void writeMenuNpc(int npcId, int mapId, String npcSay, String... menu) throws Exception {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/npc_menu/" + npcId + "m" + mapId, false));
        dos.writeShort(npcId);
        dos.writeUTF(npcSay);
        dos.writeByte(menu.length);
        for (int i = 0; i < menu.length; i++) {
            dos.writeUTF(menu[i]);
        }
        dos.flush();
        dos.close();
    }

    private static void createMenuNPC(int npcID, int map, String npcChat, String... menu) throws Exception {
        String filePath = "C:\\Users\\adm\\Desktop\\ServerNro\\data\\npc_menu\\" + npcID + "m" + map;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        dos.writeShort(npcID);
        dos.writeUTF(npcChat);
        dos.writeByte(menu.length);
        for (int i = 0; i < menu.length; i++) {
            dos.writeUTF(menu[i]);
        }
        dos.flush();
        dos.close();
    }

    private static Random rd = new Random();

    public static void createShop(int npcID) throws Exception {
        ItemData.loadDataItems();
        String filePath = "C:\\Users\\adm\\Desktop\\ServerNro\\data\\shop\\16";
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        int typeShop = 0;
        dos.writeByte(typeShop); //type shop

        int slTab = 2;
        dos.writeByte(slTab); //số lượng tab
        for (int i = 0; i < slTab; i++) {
            dos.writeUTF("Tab " + (i + 1)); //tên tab
            if (typeShop == 2) { //shop ký gửi
                dos.writeByte(100); //số lượng trang tab
            }

            int slItem = 200;
            dos.writeByte(slItem); //số lượng item tab
            for (int j = 0; j < slItem; j++) {
                int idItem = rd.nextInt(800);
                dos.writeShort(idItem); //item id
                switch (typeShop) {
                    case 4: //không rõ type
                        dos.writeUTF("Reason gì đó");
                        break;
                    case 0: //shop bình thường
//                        dos.writeInt(ItemData.getTemplate((short) idItem).gold); //giá = ngọc xanh
//                        dos.writeInt(ItemData.getTemplate((short) idItem).gem); //giá = vàng
                        break;
                    case 1: //shop yêu cầu sm
                        dos.writeLong(1000); //sm yêu cầu
                        break;
                    case 2: //shop ký gửi
                        dos.writeShort(j); //item id
                        dos.writeInt(1000); //giá = vàng
                        dos.writeInt(1000); //giá = ngọc xanh
                        dos.writeByte(1); //buy type
                        dos.writeByte(99); //số lượng
                        dos.writeByte(0); //is item me
                        break;
                    case 3:
                        dos.writeShort(1); //icon special
                        dos.writeInt(1); //buy special
                        break;
                    default:
                        break;
                }
                int optionItem = 5;
                dos.writeByte(optionItem); //số lựogn option item
                if (optionItem != 0) {
                    for (int k = 0; k < optionItem; k++) {
                        int idOption = rd.nextInt(24); //id option
                        int param = rd.nextInt(100); //param option
                        dos.writeByte(idOption);
                        dos.writeShort(param);
                    }
                }

                dos.writeByte(1); //(1: item mới, 0: item cũ)
                int isCaiTrang = 0;
                dos.writeByte(isCaiTrang); //(1: cải trang, 0: item thường)
                if (isCaiTrang == 1) {
                    dos.writeShort(4); //đầu cải trang
                    dos.writeShort(4); //thân cải trang
                    dos.writeShort(4); //chân cải trang
                    dos.writeShort(4); //balo cải trang
                }
            }
        }
    }

}
