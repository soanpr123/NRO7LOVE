package real.lucky;

import real.item.ItemOption;
import real.item.ItemTemplate;
import real.map.ItemMap;
import real.map.Map;
import real.map.MapManager;
import server.Service;
import server.io.Message;

import java.util.ArrayList;
import java.util.List;

public class ItemLucky {
    private static int countItemAppeaerd = 0;

    public int id;
    public boolean isCaitrang;
    public ItemTemplate itemTemplate;
    public int quantity;
    public long playerId;
    public ArrayList<ItemOption> options=new ArrayList<>();

    private long createTime;
    public ItemLucky() {

    }

    public ItemLucky(ItemLucky itemLucky) {

        this.id = itemLucky.id;
        this.itemTemplate = itemLucky.itemTemplate;
        this.quantity = itemLucky.quantity;
        this.playerId = itemLucky.playerId;
        this.isCaitrang=itemLucky.isCaitrang;
        this.createTime = System.currentTimeMillis();
        for (ItemOption io : itemLucky.options) {
            this.options.add(new ItemOption(io));
        }
    }

    public void update() {
        if ((System.currentTimeMillis() - createTime) >= 60000) {

        }
        if ((System.currentTimeMillis() - createTime) >= 45000) {
            playerId = -1;
        }
    }

    public static void removeItemMap(ItemMap itemMap) {
//        System.out.println("remove item map: " + itemMap.itemTemplate.name);
        Message msg;
        try {
            msg = new Message(-21);
            msg.writer().writeShort(itemMap.itemMapId);
            Service.getInstance().sendMessAllPlayerInMap(MapManager.gI().getMap(itemMap.map.id, itemMap.map.zoneId), msg);
            msg.cleanup();
            MapManager.gI().getMap(itemMap.map.id, itemMap.map.zoneId).items.remove(itemMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
