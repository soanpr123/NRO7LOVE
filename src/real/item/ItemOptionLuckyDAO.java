package real.item;

import server.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemOptionLuckyDAO {

    public static List<ItemOptionLucky> getOption(int itemTemplate) {
        List<ItemOptionLucky> list = new ArrayList<>();
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from item_option_lucky where item_template_id = ?");

            ps.setInt(1, itemTemplate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ItemOptionLucky( itemTemplate, rs.getInt(2), rs.getInt(3)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<ItemOption> getOptions( int itemTemplate) {
        List<ItemOption> list = new ArrayList<>();
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from item_option_lucky where  item_template_id = ?");

            ps.setInt(1, itemTemplate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ItemOption(rs.getInt(2), rs.getShort(3)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ItemOption>) list;
    }
}
