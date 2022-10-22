package real.item;

import server.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class ItemLuckyDAO {

    public static  List<ItemLucky> loadItemLucky() {
        List<ItemLucky> lists = new ArrayList<>();
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from item_lucky order by npc_id asc, tab asc, create_time desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemLucky it = new ItemLucky();
                it.npcId = rs.getInt(1);
                it.itemTemplate = ItemData.getTemplate(rs.getShort(2));
                it.typeShop = rs.getInt(3);
                it.tab = rs.getInt(4);
                it.tabName = rs.getString(5);
                it.itemNew = rs.getInt(6) == 1;
                it.isCaiTrang = CaiTrangData.getCaiTrangByTempID(rs.getShort(2)) != null;
                it.sell = rs.getInt(7) == 1;
                it.gold = rs.getInt(8);
                it.gem = rs.getInt(9);
                it.quantity = rs.getInt(10);
                it.itemOptions = ItemOptionShopDAO.getOption(it.npcId, it.itemTemplate.id);

                lists.add(it);

            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    public static ItemShop getByTemp(int tempId) {
        ItemShop it = new ItemShop();
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from item_shop where item_template_id = ?");
            ps.setInt(1, tempId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                it.npcId = rs.getInt(1);
                it.itemTemplate = ItemData.getTemplate(rs.getShort(2));
                it.typeShop = rs.getInt(3);
                it.tab = rs.getInt(4);
                it.tabName = rs.getString(5);
                it.itemNew = rs.getInt(6) == 1;
                it.gold = rs.getInt(8);
                it.gem = rs.getInt(9);
                it.quantity = rs.getInt(10);
                it.itemOptions = ItemOptionShopDAO.getOption(it.npcId, it.itemTemplate.id);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return it;
    }

}
