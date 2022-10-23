package real.lucky;

import server.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class luckyItemDAO {
    public static int[] itemList_RoiDo_idItem = new int[88];
    public static int[] itemList_RoiDo_Optione = new int[150];
    public static int[] itemList_RoiDo_maxValue = new int[160];
    public static void getItemRoi() {
        getItemDB();

        System.out.println("Load item thuong de done");

    }
    public static void getItemDB() {
        try {
            Connection con = DBService.gI().getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `item_option_shop`.* FROM `item_option_shop`;");

            ResultSet rs = ps.executeQuery();
            int loop = 0;
            while (rs.next()) {
                int idItem = rs.getInt(2);
                int Options = rs.getInt(3);
                int maxValue = rs.getInt(4);
                itemList_RoiDo_idItem[loop] = idItem;
                itemList_RoiDo_Optione[loop] = Options;
                itemList_RoiDo_maxValue[loop] = maxValue;
//                System.out.println("itemList_RoiDo_idItem["+gender+"]["+loop+"] = "+ itemList_RoiDo_idItem[gender][loop]);
                loop++;
                if(loop==88){
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
