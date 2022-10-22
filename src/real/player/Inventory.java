package real.player;

import java.util.ArrayList;
import java.util.List;
import real.item.Item;
import real.item.ItemOption;
import real.map.ItemMap;
import real.pet.Pet;
import server.Service;
import server.Util;
import server.io.Message;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Inventory {

    public static final int LIMIT_GOLD = 2000000000;

    private final Player player;

    public List<Item> itemsBody;
    public List<Item> itemsBag;
    public List<Item> itemsBox;

    public List<Item> itemsTrade;
    public int goldTrade;

    public int gold;
    public int gem;
    public int ruby;

    public Inventory(Player player) {
        this.player = player;
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsTrade = new ArrayList<>();
    }

    public int getGemAndRuby() {
        return this.gem + this.ruby;
    }

    public void subGemAndRuby(int num) {
        this.ruby -= num;
        if (this.ruby < 0) {
            this.gem += this.ruby;
            this.ruby = 0;
        }
    }

    public List<Item> copyItemsBag() {
        return copyList(itemsBag);
    }

    public boolean getFullBag(){
        int  maxBag = this.itemsBag.size();
        int curentBag = 0;
        for(Item it : this.itemsBag){
            if(it.id != -1){
                curentBag += 1;
            }
        }
        if(curentBag == maxBag){
            return true;
        }
        return false;
    }
    public boolean getFullBox(){
        int  maxBox = this.itemsBox.size();
        int curentBox = 0;
        for(Item it : this.itemsBox){
            if(it.id != -1){
                curentBox += 1;
            }
        }
        if(curentBox == maxBox){
            return true;
        }
        return false;
    }
    private List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(new Item(item));
        }
        return list;
    }

    public void removeNgocRong() {
        for (int i = 0; i < itemsBag.size(); i++) {
            if (itemsBag.get(i).id != -1 && itemsBag.get(i).template.id >= 14 && itemsBag.get(i).template.id <= 20) {
                if (itemsBag.get(i).quantity == 1) {
                    Item sItem = new Item();
                    sItem.id = -1;
                    itemsBag.set(i, sItem);
                } else {
                    itemsBag.get(i).quantity -= 1;
                }
            }
        }
        sortItemBag();
        sendItemBags();

    }

    public boolean existItemBag(int id) {
        for (int i = 0; i < itemsBag.size(); i++) {
            if (itemsBag.get(i).id != -1 && itemsBag.get(i).template.id == id) {
                return true;
            }
        }
        return false;
    }

    public void setItemBag(Item item) {
        for (int i = 0; i < itemsBag.size(); i++) {
            if (itemsBag.get(i).id == item.id) {
                itemsBag.set(i, item);
                break;
            }
        }
    }

    public boolean addItemBag(Item item) {
        if(getFullBag() == true){
            Service.getInstance().sendThongBao(this.player, "Hành trang đầy");
            return false;
        }else{
                return addItemList(itemsBag, item);
        }
    }
    
    public Item eatMagicTree(){
        int index = 0;
        for (Item it : itemsBag) {
            
                if (it.id != -1 && it.template.type == 6) {
                    it.quantity -= 1;
                    if(it.quantity <= 0){
                        removeItemBag(index);
                        this.sortItemBag();
                    }
                    this.sendItemBags();
                    return it;
                }
                index++;
                }
        return null;
    }
    public Item banThoiVang(Item item){
        int index = 0;
        for (Item it : itemsBag) {
                if (it.id != -1 && it.template.id == 457 && it.id == item.id) {
                    it.quantity -= 1;
                    if(it.quantity <= 0){
                        removeItemBag(index);
                        this.sortItemBag();
                    }
                    this.sendItemBags();
                    return it;
                }
                index++;
                }
        return null;
    }
    
    public Item getGiapLuyenTap(){
        Item it = null;
        if(this.itemsBody.get(6).id != -1){
            return this.itemsBody.get(6);
        }
        return it;
    }
    
    public Item eatMagicTree(Item item){
        int index = 0;
        for (Item it : itemsBag) {
                if (it.id != -1 && it.template.type == 6 && it.id == item.id) {
                    it.quantity -= 1;
                    if(it.quantity <= 0){
                        removeItemBag(index);
                        this.sortItemBag();
                    }
                    this.sendItemBags();
                    return it;
                }
                index++;
                }
        return null;
    }
    public Item getDauThan(int id){
        try{
        for (Item it : itemsBag) {
                if (it.id != -1 && it.template.id == id) {
                    return it;
                }
            }
        }catch(Exception e){
            
        }
        return null;
    }
    
    public boolean addItemList(List<Item> items, Item item) {
        if (isItemIncremental(item)) {
            for (Item it : items) {
                if (it.id != -1 && it.template.id == item.template.id) {
                        it.quantity += item.quantity;
                    return true;
                }
            }
        }
        
        int optionId = isItemIncrementalOption(item);
        if (optionId != -1) {
            int param = 0;
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == optionId) {
                    param = io.param;
                }
            }
            for (Item it : items) {
                if (it.id != -1 && it.template.id == item.template.id) {
                    for (ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            io.param += param;
                        }
                    }
                    return true;
                }
            }
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == -1) {
                items.set(i, item);
                return true;
            }
        }
        return false;
    }

    public void setItemBody(Item item) {
        int index = -1;
        if (item.id != -1) {
            if (item.template.gender == player.gender || item.template.gender == 3) {
                if (item.template.strRequire <= player.point.getPower()) {
                    if (item.template.type >= 0 && item.template.type <= 5) {
                        index = item.template.type;
                    }
                    if (item.template.type == 32) {
                        index = 6;
                    }
                } else {
                    addItemBag(item);
                    Service.getInstance().sendThongBaoOK(player, "Sức mạnh không đủ yêu cầu");
                }
            } else {
                addItemBag(item);
                Service.getInstance().sendThongBaoOK(player, "Sai hành tinh");
            }
        }
        if (index != -1) {
            if (this.itemsBody.get(index).id != -1) {
                addItemBag(this.itemsBody.get(index));
            }
            this.itemsBody.set(index, item);
        }
    }

    private boolean isItemIncremental(Item item) { //item cộng dồn số lượng
        if (item.template.type == 12 || item.template.type == 30 || item.template.type == 27) {
            return true;
        }
        int temp = item.template.id;
        if (temp == 193 || temp == 402 || temp == 403 || temp == 404 || temp == 759 || temp == 361||temp==13) {
            return true;
        }
        return false;
    }

    private byte isItemIncrementalOption(Item item) { //trả về optionid
        int temp = item.template.id;
        if (temp == 521) {
            return 1;
        } else {
            return -1;
        }
    }

    public void throwItem(int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = itemsBody.get(index);
            removeItemBody(index);
            sortItemBag();
            sendItemBody();
        } else if (where == 1) {
            itemThrow = itemsBag.get(index);
            removeItemBag(index);
            sortItemBag();
            sendItemBags();
        }
        if (itemThrow == null) {
            return;
        }

        //ItemMap itemMap = new ItemMap(player.map, itemThrow.template, itemThrow.quantity, player.x, player.y, player.id);
        //itemMap.options = itemThrow.itemOptions;
        //Service.getInstance().roiItem(player, itemMap);
        Service.getInstance().Send_Caitrang(player);
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void arrangeItems(List<Item> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).id == -1) {
                int indexSwap = -1;
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(j).id != -1) {
                        indexSwap = j;
                        break;
                    }
                }
                if (indexSwap != -1) {
                    Item sItem = new Item();
                    sItem.id = -1;
                    list.set(i, list.get(indexSwap));
                    list.set(indexSwap, sItem);
                } else {
                    break;
                }
            }
        }
    }

    public Item putItemBag(Item item) {
        for (int i = 0; i < itemsBag.size(); i++) {
            if (itemsBag.get(i).id == -1) {
                itemsBag.set(i, item);
                Item sItem = new Item();
                sItem.id = -1;
                return sItem;
            }
        }
        return item;
    }
    
    public Item putItemBox(Item item) {
        for (int i = 0; i < itemsBox.size(); i++) {
            if (itemsBox.get(i).id == -1) {
                itemsBox.set(i, item);
                Item sItem = new Item();
                sItem.id = -1;
                return sItem;
            }
        }
        return item;
    }

    public Item searchItem(Item it){
        Item sItem = null;
       for (int i = 0; i < itemsBag.size(); i++) {
           if(itemsBag.get(i).id != -1){
          if(itemsBag.get(i).template.id == it.template.id){
              return itemsBag.get(i);
          }
          }
       }
        return sItem;
    }
    
    public Item putItemBody(Item item) {
        Item sItem = item;
        if (item.id != -1) {
            if (item.template.type == 0 || item.template.type == 1
                    || item.template.type == 2 || item.template.type == 3 || item.template.type == 4
                    || item.template.type == 5 || item.template.type == 32) {
                if (item.template.gender == player.gender || item.template.gender == 3) {
                    if (item.template.strRequire <= player.point.getPower()) {
                        sItem = itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                        itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                    } else {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                }
            } else {
                Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            }
        }
        return sItem;
    }

    public void itemBagToBody(int index) {
        if(this.player.isOpenShop ==  true){
            Service.getInstance().sendThongBao(this.player, "Không thể thực hiện khi đang đứng gần Shop hãy chạy ra vài cây số rồi hẵng sử dụng");
        }else{
        Item item = itemsBag.get(index);
        if (item.id != -1) {
            itemsBag.set(index, putItemBody(item));
            Service.getInstance().point(player);
            sendItemBags();
            sendItemBody();
            Service.getInstance().Send_Caitrang(player);
        }
        }
    }

    public void itemBodyToBag(int index) {
        if(this.player.isOpenShop ==  true){
            Service.getInstance().sendThongBao(this.player, "Không thể thực hiện khi đang đứng gần Shop hãy chạy ra vài cây số rồi hẵng sử dụng");
        }else{
        Item item = itemsBody.get(index);
        if (item.id != -1) {

            itemsBody.set(index, putItemBag(item));
            Service.getInstance().point(player);
            sendItemBags();
            sendItemBody();
            Service.getInstance().Send_Caitrang(player);
        }
        }
    }
    
    
    public void itemBagToPetBody(int index) {
        Item item = itemsBag.get(index);
        if (item.id != -1) {
            Item itemSwap = player.pet.inventory.putItemBody(item);
            itemsBag.set(index, itemSwap);
            Service.getInstance().point(player);
            sendItemBags();
            sendItemBody();
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().Send_Caitrang(player);
            if (!itemSwap.equals(item)) {
                Service.getInstance().showInfoPet(player);
            }
        }
    }

    public void itemPetBodyToBag(int index) {
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.id != -1) {
            player.pet.inventory.itemsBody.set(index, putItemBag(item));
            sendItemBags();
            sendItemBody();
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().showInfoPet(player);
        }
    }
    public byte getIndexBoxid(final int id) {
        for (byte i = 0; i < this.itemsBox.size(); ++i) {
            final Item item = this.itemsBox.get(i);
            if (item.id != -1 && item.template.id == id) {
                System.out.println(item.template.id);
                return i;
            }
        }
        return -1;
    }
    public byte getBoxNull() {
        byte num = 0;
        for (byte i = 0; i < this.itemsBox.size(); ++i) {
            if (this.itemsBox.get(i) == null) {
                num++;
            }
        }
        return num;
    }

    protected byte getIndexBoxNotItem() {
        for (byte i = 0; i < itemsBox.size(); ++i) {
            final Item item = itemsBox.get(i);
            if (item.id == -1) {
                System.out.println(i);
                return i;
            }
        }
        return -1;
    }

    public byte getIndexBagNotItem() {
        byte i;
        Item item;
        for (i = 0; i < itemsBag.size(); ++i) {
            item = itemsBag.get(i);
            if (item.id == -1) {
                return i;
            }
        }

        return -1;
    }
    public void itemBagToBox(int index) {
        Item item = itemsBag.get(index);
//        System.out.println(item.template.id);
//        System.out.println("item trong bag" + itemsBag.get(index).id);
//        for (byte i = 0; i < itemsBox.size(); ++i) {
//            final Item item = itemsBox.get(i);
//            System.out.println("item trong ruong =>>"+item.id);
//
//            if (item.id !=-1 && item.template.id == itemsBag.get(index).template.id) {
//                System.out.println(item.template.id);
//                return ;
//            }
//        }
//        return ;
        if (item != null) {
            byte indexBox = getIndexBoxid(item.template.id);
            System.out.println("indexBox====>" + indexBox);
            System.out.println(isItemIncremental(item));
            if (indexBox!=-1&&isItemIncremental(item)) {
                removeItemBag(index);
                Item item2 = itemsBox.get(indexBox);
                item2.quantity += item.quantity;
                System.out.println(item2.quantity);
                this.itemsBox.set(indexBox, item2);

            } else {
                if (getFullBox()==true) {
                    Service.getInstance().sendThongBao(player,"Rương đồ không đủ chỗ trống");
                    return;
                }
                indexBox = getIndexBoxNotItem();
                removeItemBag(index);
                itemsBox.set(indexBox, item);
//                item.quantity = item.quantitytemp;
            }
            sortItemBox();
           sendItemBox();
           sendItemBags();

        }
//
        }
    public byte getIndexBagid(int id) {
        byte i;
        Item item;
        for (i = 0; i < itemsBox.size(); ++i) {
            item = itemsBox.get(i);
            if (item.id != -1 && item.template.id == id) {
                return i;
            }
        }
        return -1;
    }

    public void itemBoxToBag(int index) {
        Item item = itemsBox.get(index);
        if (item != null) {
            byte indexBag = getIndexBoxid(item.id);
            if (indexBag != -1 &&isItemIncremental(item)) {
                removeItemBox(index);
                Item item2 = itemsBag.get(indexBag);
                item2.quantity += item.quantity;
                itemsBag.set(indexBag, item2);
            } else {
                if (getFullBag()) {
                    Service.getInstance().sendThongBao(player,"Hành trang không đủ chỗ trống");
                    return;
                }
                indexBag = getIndexBagNotItem();
                removeItemBox(index);
                itemsBag.set(indexBag, item);
            }
            sortItemBox();
           sendItemBags();
            sendItemBox();
        }
    }
    public void itemBoxToBag() {

    }

    public void itemBodyToBox() {

    }

    //--------------------------------------------------------------------------
    public void subQuantityItemsBag(Item item, int quantity) {
        subQuantityItem(itemsBag, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        for (Item it : items) {
            if (it.id == item.id) {
                it.quantity -= quantity;
                if (it.quantity <= 0) {
                    removeItem(items, item);
                }
                break;
            }
        }
    }

    public void sortItemBag() {
        sortItem(itemsBag);
    }
    public void sortItemBox() {
        sortItem(itemsBox);
    }

    public void sortItem(List<Item> items) {
        int index = 0;
        for (Item item : items) {
            if (item.id != -1) {
                items.set(index, item);
                index++;
            }
        }
        for (int i = index; i < items.size(); i++) {
            Item item = new Item();
            item.id = -1;
            items.set(i, item);
        }
    }

    //--------------------------------------------------------------------------
    public void removeItem(List<Item> items, int index) {
        Item item = new Item();
        item.id = -1;
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        Item it = new Item();
        it.id = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                break;
            }
        }
    }

    public void removeItemBag(int index) {
        removeItem(itemsBag, index);
    }

    public void removeItemBag(Item item) {
        removeItem(itemsBag, item);
    }

    public void removeItemBody(int index) {
        removeItem(itemsBody, index);
    }

    public void removeItemPetBody(int index) {
        this.player.pet.inventory.removeItemBody(index);
    }

    public void removeItemBox(int index) {
        removeItem(itemsBox, index);
    }

    //--------------------------------------------------------------------------
    public Item findItem(List<Item> list, int tempId) {
        for (Item item : list) {
            if (item.id != -1 && item.template.id == tempId) {
                return item;
            }
        }
        return null;
    }

    public Item findItemBagByTemp(int tempId) {
        return findItem(itemsBag, tempId);
    }

    //--------------------------------------------------------------------------
public void sendItemBags() {
        arrangeItems(itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (item.id == -1) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); //options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody() {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(this.player.getHead());
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (item.id == -1) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBox() {
        arrangeItems(itemsBox);
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (item.id == -1) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); //options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
