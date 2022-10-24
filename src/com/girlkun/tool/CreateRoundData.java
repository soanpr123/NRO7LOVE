package com.girlkun.tool;

import real.item.*;
import real.map.Map;
import real.npc.Npc;
import server.DBService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateRoundData {
   ;
    public static boolean  writeDb( List<Item> items){
      Connection con=DBService.gI().getConnection();
       try {
           PreparedStatement ps = con.prepareStatement("delete from item_lucky_round");
           ps.executeUpdate();
           PreparedStatement ps3 = con.prepareStatement("delete from item_option_lucky");
           ps3.executeUpdate();
           con.setAutoCommit(false);
           PreparedStatement ps1 = con.prepareStatement("insert into item_lucky_round(item_template_id,isItemNew,quantity,reasion,power_require) value (?,?,?,?,?)");
           PreparedStatement ps2 = con.prepareStatement("insert into item_option_lucky(item_template_id,option_id,param) value (?,?,?)");

           for (int j = 0; j < items.size(); j++) {

               ps1.setInt(1, items.get(j).template.id);
               ps1.setInt(2, 0);
               ps1.setInt(3, 0);
               ps1.setString(4,"");
               ps1.setInt(5,items.get(j).template.strRequire);
               ps1.addBatch();
               for( ItemOption iot:items.get(j).itemOptions){


                   System.out.println("optionId: " + iot.optionTemplate.id);
                   System.out.println("param: " + iot.param);

                   ps2.setInt(1, items.get(j).template.id);
                   ps2.setInt(2, iot.optionTemplate.id);
                   ps2.setInt(3, iot.param);
//                   ps2.setInt(5, tab);
                   ps2.addBatch();
               }
           }

           ps1.executeBatch();
           ps2.executeBatch();
           con.commit();
           ps.close();
           ps3.close();
           con.close();
           System.out.println("done");
           return  true;
       } catch (Exception e) {
        e.printStackTrace();
        return  false;
    }
    }
    public static boolean  removeFromDB( List<Item> items,int index){
        Connection con=DBService.gI().getConnection();
        try {

            con.setAutoCommit(false);
            PreparedStatement ps1 = con.prepareStatement("delete from item_lucky_round where item_template_id=?" );
            PreparedStatement ps2 = con.prepareStatement("delete from item_option_lucky where item_template_id=? ");



                ps1.setInt(1, items.get(index).template.id);
                ps1.addBatch();
                for( ItemOption iot:items.get(index).itemOptions){


                    System.out.println("optionId: " + iot.optionTemplate.id);
                    System.out.println("param: " + iot.param);

                    ps2.setInt(1, items.get(index).template.id);

//
                    ps2.addBatch();
                }


            ps1.executeBatch();
            ps2.executeBatch();
            con.commit();

            con.close();
            System.out.println("done");
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

}
