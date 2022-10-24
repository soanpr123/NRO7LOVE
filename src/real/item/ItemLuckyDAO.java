package real.item;

import server.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemLuckyDAO {

    public static List<ItemLucky> loadItemShop() {
        List<ItemLucky> map = new ArrayList<>();
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from item_lucky_round");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemLucky it = new ItemLucky();
                it.itemTemplate = ItemData.getTemplate(rs.getShort(2));
                it.itemNew = rs.getInt(3) == 1;
                it.isCaiTrang = CaiTrangData.getCaiTrangByTempID(rs.getShort(2)) != null;

                it.quantity = rs.getInt(4);
                it.itemOptions = ItemOptionLuckyDAO.getOption(rs.getShort(2));

                map.add(it);

            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
