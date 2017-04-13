import java.util.*;

public class DLEquipment
{
   private int equipID, equipCap;
   private String equipName, equipDesc;
   private MySQLDatabase mySQL = new MySQLDatabase("jdbc:mysql://127.0.0.1/travel?autoReconnect=true&useSSL=false", 
                                "com.mysql.jdbc.Driver",
                                "root",
                                "Coolerthenice2");
   
   //constructors
   public DLEquipment()
   { 
   }
   public DLEquipment(int _equipID)
   {
      equipID = _equipID;
   }
   public DLEquipment(int _equipID, String _equipName, String _equipDesc, int _equipCap)
   {
      equipID = _equipID;
      equipName = _equipName;
      equipDesc = _equipDesc;
      equipCap = _equipCap;
   }
   //fetch, post, put, delete methods
   public boolean fetch()
   {
      if(mySQL.connect())
      {
         ArrayList<ArrayList<String>> data;
         //query
         String query = "Select * from equipment where EquipID = ?;";
         //make array to hold values for the prepared statement
         ArrayList<String> params = new ArrayList<String>();
         params.add(String.valueOf(equipID));
         try
         {
            //get the data and add it to the array list
            data = mySQL.getData(query, params);
            equipName = data.get(1).get(1);
            equipDesc = data.get(1).get(2);
            equipCap = Integer.parseInt(data.get(1).get(3));
         }
         catch(Exception e)
         {
            new DLException(e, "No records exist for " + query);
            return false;
         }
      }
      else
      {
         return false;       
      }
      mySQL.close();
      return true;
   }
   public boolean put()
   {
      if(mySQL.connect())
      {
         String query = "Update equipment " + 
                        "set " + 
                        "EquipmentName = ?, " + 
                        "EquipmentDescription = ?, " + 
                        "EquipmentCapacity = ?" + 
                        "Where EquipID = ?;";
         ArrayList<String> params = new ArrayList<String>();
         params.add(equipName);
         params.add(equipDesc);
         params.add(String.valueOf(equipCap));
         params.add(String.valueOf(equipID));
         
         if(!mySQL.setData(query, params))
         {
            return false;
         }
      }
      else
      {
         return false;
      }
      mySQL.close();
      return true;
   }  
   public boolean post()
   {
      if(mySQL.connect())
      {
         String query = "Insert into equipment VALUES(?, ?, ?, ?);";
         ArrayList<String> params = new ArrayList<String>();
         params.add(String.valueOf(equipID));
         params.add(equipName);
         params.add(equipDesc);
         params.add(String.valueOf(equipCap));
         if(!mySQL.setData(query, params))
         {
            return false;
         }
      }
      else
      {
         return false;
      }
      mySQL.close();
      return true;
   }
   public boolean delete()
   {
      if(mySQL.connect())
      {
         String query = "Delete from equipment where EquipID = ?;";
         ArrayList<String> params = new ArrayList<String>();
         params.add(String.valueOf(equipID));
         if(!mySQL.setData(query, params))
         {
            return false;
         }
      }
      else
      {
         return false;
      }
      mySQL.close();
      return true;
   }
      
   //swap method
   public void swap(int _equipID)
   {
      if(mySQL.connect())
      {
         ArrayList<ArrayList<String>> data;
         //get the current objects name
         String eName = getEquipName();
         
         //get the equipment name from the other equipment
         String query = "Select EquipmentName from Equipment where EquipID = ?;";
         ArrayList<String> params = new ArrayList<String>();
         params.add(String.valueOf(_equipID));
         try
         {
            //set the equipment name to the name of the other equipment
            data = mySQL.getData(query, params);
            equipName =  data.get(1).get(0);
            //reset the params array
            params.remove(0);
            
            //start the transaction to update the current equipment name 
            mySQL.startTrans();
            query = "Update equipment set EquipmentName = '"+getEquipName()+ "' where EquipID = ?;";
            params.add(String.valueOf(getEquipID()));
            mySQL.setData(query, params);
            mySQL.endTrans();
            
            //start the transaction to update the second equipments name to this equipments name;
            mySQL.startTrans();
            query =  "Update equipment set EquipmentName = '"+ eName + "' where EquipID = ?;";
            //reset params array
            params.remove(0);
            params.add(String.valueOf(_equipID));
            mySQL.setData( query, params);
            mySQL.endTrans();          
         }
         catch(Exception e)
         {
            new DLException(e, "Could not Swap Names");
            mySQL.rollback();
         }
                 
      }
   }
   
   //set methods
   public void setEquipName(String _equipName)
   {
      equipName = _equipName;
   }
   public void setEquipDesc(String _equipDesc)
   {
      equipDesc = _equipDesc;
   }
   public void setEquipCap(int _equipCap)
   {
      equipCap = _equipCap;
   }
   //get methods
   public int getEquipID()
   {
      return equipID;
   }
   public String getEquipName()
   {
      return equipName;
   }
   public String getEquipDesc()
   {
      return equipDesc;
   }
   public int getEquipCap()
   {
      return equipCap;
   }
}