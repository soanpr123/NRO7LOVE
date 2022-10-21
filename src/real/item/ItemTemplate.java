package real.item;
//share by chibikun

public class ItemTemplate {

    public short id;

    public byte type;

    public byte gender;

    public String name;

    public String description;

    public byte level;

    public short iconID;

    public short part;

    public boolean isUpToUp;

    public int strRequire;

    public ItemTemplate() {
    }

    public ItemTemplate(short id, byte type, byte gender, String name, String description, short iconID, short part, boolean isUpToUp, int strRequire) {
        this.id = id;
        this.type = type;
        this.gender = gender;
        this.name = name;
        this.description = description;
        this.iconID = iconID;
        this.part = part;
        this.isUpToUp = isUpToUp;
        this.strRequire = strRequire;
    }

    
}
