/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package real.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import real.item.ItemOptionShop;
import server.DBService;

/**
 *
 * @author phong
 */
public class Mob_ItemDAO {

    public static int[][] itemList_RoiDo_idItem = new int[3][88];
    public static int[][] itemList_RoiDo_Optione = new int[3][150];
    public static int[][] itemList_RoiDo_maxValue = new int[3][160];
    public static void getItemRoi() {
        getItemDB(0);
        getItemDB(1);
        getItemDB(2);
        System.out.println("Load itemRoi done");

    }
    public static void getItemDB(int gender) {
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `item_option_shop`.* FROM `item_option_shop` WHERE (`item_option_shop`.`npc_id` = ?) AND (`tab` = 0 OR `tab` = 1);");
            ps.setInt(1, (gender+7));
            ResultSet rs = ps.executeQuery();
            int loop = 0;
            while (rs.next()) {
                int idItem = rs.getInt(2);
                int Options = rs.getInt(3);
                int maxValue = rs.getInt(4);
                itemList_RoiDo_idItem[gender][loop] = idItem;
                itemList_RoiDo_Optione[gender][loop] = Options;
                itemList_RoiDo_maxValue[gender][loop] = maxValue;
//                System.out.println("itemList_RoiDo_idItem["+gender+"]["+loop+"] = "+ itemList_RoiDo_idItem[gender][loop]);
                loop++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
