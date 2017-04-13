import java.sql.*;
import java.util.*;

public class DLUser
{
   private String userName, passWord, name, access;
   private MySQLDatabase mySQL = new MySQLDatabase("jdbc:mysql://127.0.0.1/travel?autoReconnect=true&useSSL=false", 
                                "com.mysql.jdbc.Driver",
                                "root",
                                "Coolerthenice2");
   
   //constructors
   public DLUser()
   { 
   }
   //this is the constructor that is used to populate the rest of the objects attributes
   public DLUser(String _userName, String _passWord)
   {
      userName = _userName;
      passWord = _passWord;
   }
   //This construstor is used to make a new user
   public DLUser(String _userName, String _passWord, String _name, String _access)
   {
      userName = _userName;
      passWord = _passWord;
      name = _name;
      access = _access;
   }
   //fetch, post, put, delete methods
   public boolean fetch()
   {
      if(mySQL.connect())
      {
         ArrayList<ArrayList<String>> data;
         //query
         String query = "Select * from users where UserName  = ?;";
         //make array to hold values for the prepared statement
         ArrayList<String> params = new ArrayList<String>();
         params.add(getUserName());
         try
         {
            //get the data and add it to the array list
            data = mySQL.getData(query, params);
            passWord = data.get(1).get(1);
            name = data.get(1).get(2);
            access = data.get(1).get(3);
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
         String query = "Update users " + 
                        "set " + 
                        "UserName = ?, " + 
                        "Password = ?, " + 
                        "Name = ?" + 
                        "Access = ?"+
                        "Where UserName = ?;";
         ArrayList<String> params = new ArrayList<String>();
         params.add(userName);
         params.add(passWord);
         params.add(name);
         params.add(access);
         params.add(userName);
         
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
         String query = "Insert into users VALUES(?, ?, ?, ?);";
         ArrayList<String> params = new ArrayList<String>();
         params.add(userName);
         params.add(passWord);
         params.add(name);
         params.add(access);
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
         String query = "Delete from users where userName = ?;";
         ArrayList<String> params = new ArrayList<String>();
         params.add(userName);
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
   /**
   * @param username
   * @param password
   * a method that verfies that the username and password is correct and then populates the rest of the objects attributes
   **/
   public boolean login()
   {
      if(mySQL.connect())
      {
         ArrayList<ArrayList<String>> data;
         //query
         String query = "Select PassWord from users where UserName = '" + getUserName() + "';";
         try
         {
            //get the data and add it to the array list
            data = mySQL.getData(query, true);
            if(getPassword() == data.get(1).get(0))
            {
              //new query
              query = "Select Name, Access from users where UserName = '" + getUserName() + "';";
              data = mySQL.getData(query, true);
              name = data.get(1).get(0);
              access = data.get(1).get(1);
              return true;
            }
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
   
   //set methods
   public void setUserName(String _userName)
   {
      userName = _userName;
   }
   public void setPassword(String _passWord)
   {
      passWord = _passWord;
   }
   public void setName(String _name)
   {
      name = _name;
   }
   public void setAccess(String _access)
   {
      access = _access;
   }
   //get methods
   public String getUserName()
   {
      return userName;
   }
   public String getPassword()
   {
      return passWord;
   }
   public String getName()
   {
      return name;
   }
   public String getAccess()
   {
      return access;
   }
}