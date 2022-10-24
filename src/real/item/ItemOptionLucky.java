package real.item;

public class ItemOptionLucky {


    public int itemTemplateId;
    public int optionId;
    public int param;

    public ItemOptionLucky() {
    }

    public ItemOptionLucky( int itemTemplateId, int optionId, int param) {

        this.itemTemplateId = itemTemplateId;
        this.optionId = optionId;
        this.param = param;
    }
}
