package real.lucky;

import real.func.UseItem;
import real.item.CaiTrangData;
import real.item.Item;

import real.player.Player;
import server.io.Message;

import java.util.ArrayList;
import java.util.List;

public class Lucky {
     public    boolean isDone=true;

    private static Lucky instance;

    private Lucky() {

    }

    public static Lucky gI() {
        if (instance == null) {
            instance = new Lucky();
        }
        return instance;
    }
    public void luckyRound(Player pl){
//        isDone=false;
        Message msg;
        try {
            msg = new Message(129);
            msg.writer().writeByte(0);
            msg.writer().writeByte(7);
            msg.writer().writeShort(419);
            msg.writer().writeShort(420);
            msg.writer().writeShort(421);
            msg.writer().writeShort(422);
            msg.writer().writeShort(423);
            msg.writer().writeShort(424);
            msg.writer().writeShort(425);

            msg.writer().writeByte(1);
            msg.writer().writeInt(20);
            msg.writer().writeShort(0);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public  void openLuckyBox(Player pl){
        Message msg;
        try {
            List<ItemLucky> list = pl.inventory.itemsLuckyBox;

            List<ItemLucky> list2 = new ArrayList<>();

            for (int j = 0; j < list.size(); j++) {
                ItemLucky it= list.get(j);
                if (it.id != -1) {
                    list2.add(it);
                }}
            msg = new Message(-44);

            msg.writer().writeByte(4);

            msg.writer().writeByte(1);

            msg.writer().writeUTF("Rương\nPhụ");
            msg.writer().writeByte(list2.size());

//                                            msg.writer().writeShort(1);
            for (int j = 0; j < list2.size(); j++) {
                ItemLucky it= list2.get(j);
                System.out.println("itemmmm==========>" + it.itemTemplate.id);
                msg.writer().writeShort(list2.size()==0?-1:it.itemTemplate.id);
                int options = it.options.size();
                msg.writer().writeUTF("Tăng 10%sd");
                msg.writer().writeByte(options);
                if (options != 0) {
                    for (int k = 0; k < options; k++) {
                        msg.writer().writeByte(it.options.get(k).optionTemplate.id);
                        msg.writer().writeShort(it.options.get(k).param);
                    }
                }
                msg.writer().writeByte( 0);
                int isCaiTrang = it.isCaitrang?1:0;
                msg.writer().writeByte(isCaiTrang);
                if (isCaiTrang == 1) {
                    CaiTrangData.CaiTrang ct = CaiTrangData.getCaiTrangByTempID(it.itemTemplate.id);
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);

                }
            }


            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
