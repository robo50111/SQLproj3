import java.util.*;
public class DataConnections
{
   public static void main(String [] args)
   {
      //A
      DLEquipment equip1 = new DLEquipment(568);
      DLUser user = new DLUser("admin","password");
      BLEquipment blequip = new BLEquipment();
      user.login(); 
      //b
      equip1.fetch();
      System.out.println("Equipment ID " + equip1.getEquipID() + "\n" + 
                         "Equipment Name: " + equip1.getEquipName() + "\n" + 
                         "Equipment Description: " + equip1.getEquipDesc() + "\n" + 
                         "Equipment Capacity: " + equip1.getEquipCap()+ "\n");
      //c
      if(blequip.save())
      {
        equip1.swap(894);
      }
      else
      {
        System.out.println("User does not have proper access to the database\n");
      }
      //d
      equip1.fetch();
      System.out.println("Equipment ID " + equip1.getEquipID() + "\n" + 
                         "Equipment Name: " + equip1.getEquipName() + "\n" + 
                         "Equipment Description: " + equip1.getEquipDesc() + "\n" + 
                         "Equipment Capacity: " + equip1.getEquipCap()+ "\n");
      //e
      DLEquipment equip2 = new DLEquipment(894);
      equip2.fetch();
      System.out.println("Equipment ID " + equip2.getEquipID() + "\n" + 
                         "Equipment Name: " + equip2.getEquipName() + "\n" + 
                         "Equipment Description: " + equip2.getEquipDesc() + "\n" + 
                         "Equipment Capacity: " + equip2.getEquipCap()+ "\n");
                         
   }
}