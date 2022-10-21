package real.lucky;

import real.func.UseItem;
import real.player.Player;
import server.io.Message;

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
}
