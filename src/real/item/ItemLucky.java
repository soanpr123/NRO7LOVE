package real.item;

import java.util.ArrayList;
import java.util.List;

public class ItemLucky {



    public ItemTemplate itemTemplate;



    public boolean itemNew;
    public boolean isCaiTrang;

    public int tiemnang;
    public int quantity;

    public List<ItemOptionLucky> itemOptions=new ArrayList<>();
    public ItemLucky() {
    }

    public ItemLucky( ItemTemplate itemTemplate, boolean itemNew, boolean isCaiTrang,int tiemnang,int quantity,List<ItemOptionLucky>itemOptions) {

        this.itemTemplate = itemTemplate;
        this.itemNew = itemNew;
        this.isCaiTrang = isCaiTrang;
        this.tiemnang=tiemnang;
        this.quantity=quantity;
        this.itemOptions=itemOptions;
    }
}
