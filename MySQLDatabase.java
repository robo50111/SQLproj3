import java.sql.*;
import java.util.*;


public class MySQLDatabase
{
   private String dbURL, driver, user, pass;
   private Connection conn = null;
   private Statement stmt;
   private ResultSet rs;
   private ResultSetMetaData rsmd;
   private ArrayList<ArrayList<String>> table;

   public MySQLDatabase(String _dbURL, String _driver, String _user, String _pass)
   {
      dbURL = _dbURL;
      driver = _driver;
      user = _user;
      pass = _pass;
      // Load The Driver
      try
      {
         Class.forName(driver);
      }
      catch (Exception e)
      {
         new DLException(e, "Could Not find/load the driver");
      }  
   }
   public boolean connect()
   {
      //open the database
      try
      {
         conn = DriverManager.getConnection(dbURL, user, pass);
         return true;
      }
      catch(SQLException sqle)
      {
         new DLException(sqle, "Failed to open the database. Check the username, password, or path to the database");
         return false;
      }
   }
   public boolean close()
   {
      try
      {
         conn.close();
         return true;
      }
      catch(SQLException sqle)
      {
         return false;
      }
      catch (NullPointerException npe)
      {
         return false;
      }
   }
   /**
   *@param query The query that the user wants to execute
   *This method is the get data method that takes in a query and returns a 2d arraylist with the results
   **/
   public ArrayList<ArrayList<String>> getData(String query) throws DLException
   {
      table = new ArrayList<ArrayList<String>>();
      try
      {
         //create statment object
         stmt = conn.createStatement();
         //execute statment, return a result set
         rs = stmt.executeQuery(query);
         rsmd = rs.getMetaData();
         int fields = rsmd.getColumnCount();
         while (rs.next())
         {
            //create a new arraylist for each result
            ArrayList<String> val = new ArrayList<String>();
            for(int i = 0; i<fields; i++)
            {
               // go through the row, add the values to the new arrayList
               val.add(rs.getString(i+1));
            }//end for
            //add the row to the table
            table.add(val);
         }//end while
      }//end try
      catch(Exception e)
      {
         throw new DLException(e, "Check your query or number of fields. Query: " + query);
      }
      //return the table
      return table;      
   }
  /**
   *@param query The query that the user wants to execute
   *@param getColumnName A boolean that determines if the user wants the result to contain the column names
   *This method is the get data method that takes in a query and a boolean getColumnName and returns a 2d arraylist with the results
   **/
   public ArrayList<ArrayList<String>> getData(String query, boolean getColumnName) throws DLException
   {
      table = new ArrayList<ArrayList<String>>();
      if(getColumnName)
      {
         try
         {
            //create statment object
            stmt = conn.createStatement();
            //execute statment, return a result set
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            int fields = rsmd.getColumnCount();
            //get the column names
            ArrayList<String> col = new ArrayList<String>();
            for(int i = 0; i<fields; i++)
            {
               col.add(rsmd.getColumnName(i+1));
            }
            table = getData(query);//use the getData method to execute the query and then set it equal to the table arraylist in this method
            table.add(0,col);//add the column headers to the beginning of the table array
         }//end try
         catch(Exception e)
         {
            throw new DLException(e, "Check your query or number of fields. Query: " + query);
         }     
      }//end if
      //if the boolean is false
      else
      {
         return getData(query);
      }
      //return the table
      return table;             
   }
 /**
   *@param query The query that the user wants to execute
   *This method is the get data method that takes in a query and an arraylist and returns a 2d arraylist with the results
   **/
   public ArrayList<ArrayList<String>> getData(String query, ArrayList<String> vals) throws DLException
   {
      table = new ArrayList<ArrayList<String>>();
      try
      {
         //call prepare function and execute the query
         PreparedStatement pstmt = prepare(query, vals);
         //execute statment, return a result set
         rs = pstmt.executeQuery();
         rsmd = rs.getMetaData();
         int fields = rsmd.getColumnCount();
         //get the column names
         ArrayList<String> col = new ArrayList<String>();
         for(int i = 0; i<fields; i++)
         {
            col.add(rsmd.getColumnName(i+1));
         }
         //go through the result set and get the data
         while (rs.next())
         {
            //create a new arraylist for each result
            ArrayList<String> val = new ArrayList<String>();
            for(int i = 0; i<fields; i++)
            {
               // go through the row, add the values to the new arrayList
               val.add(rs.getString(i+1));
            }//end for
            //add the row to the table
            table.add(val);
         }//end while
         table.add(0,col);//add the column headers to the beginning of the table array
         return table;
      }//end try
      catch(Exception e)
      {
         throw new DLException(e, "Check your query or number of fields. Query: " + query);
      }                  
   }
 /**
   *@params String Query the query to set the data in the database
   *@return true if the statement is executed properly 
   *@return false if the statement does not execute
   **/
   public boolean setData(String query)
   {
      try
      {
         Statement stmt = conn.createStatement();
         if(stmt.executeUpdate(query) > 0)
         {
            return true;
         }
         else
         {
            return false;     
         }
      }
      catch(Exception e)
      {
         new DLException(e, "Check your query. Query: " + query);
         return false;
      }
   }
 /**
   *@params String Query the query to set the data in the database
   *@ArrayList params the parameters for the query
   *@return true if the statement is executed properly 
   *@return false if the statement does not execute
   **/
   public boolean setData(String query, ArrayList<String> params)
   {
      try
      {
         if(executeStmt(query, params) > 0)
         {
            return true;
         }
         else
         {
            return false;     
         }
      }
      catch(Exception e)
      {
         new DLException(e, "Check your query. Query: " + query);
         return false;
      }
   }
   
   public PreparedStatement prepare(String query, ArrayList<String> vals)
   {
      try
      {
         PreparedStatement stmt = conn.prepareStatement(query);
         for(int i = 0; i<vals.size(); i++)
         {
            stmt.setInt(i+1,Integer.parseInt(vals.get(i))); 
         }
         return stmt;
      }
      catch(Exception e)
      {
         new DLException(e, "Could not prepare statement. Query: " + query);
         return null;
      }
   }
   
   private int executeStmt(String query, ArrayList<String> vals)
   {
      try
      {
         //call prepare function and execute the query
         PreparedStatement pstmt = prepare(query, vals);
         //execute statment, return a result set
         return pstmt.executeUpdate();
         
      }
      catch(Exception e)
      {
         new DLException(e, "Could not execute statement. Query: " + query);
         return 0;
      }
   }
   
   public void startTrans()
   {
      try
      {
         conn.setAutoCommit(false);
      }
      catch(Exception e)
      {
         new DLException(e, "Could not start the transaction");
      }
   }
   
   public void endTrans()
   {
      try
      { 
         conn.commit();//added 4/11
         conn.setAutoCommit(true);
      }
      catch(Exception e)
      {
         new DLException(e, "Could not end the transaction");
      }
   }
   
   public void rollback()
   {
      try
      {
         conn.rollback();
      }
      catch(Exception e)
      {
         new DLException(e, "Could not roll back the transaction");
      }
   }
   
   public void descTable(String query)
   {
      try
      {
         //create statment object
         Statement stmt = conn.createStatement();
         //execute statment, return a result set
         ResultSet rs = stmt.executeQuery(query);
         ResultSetMetaData rsmd = rs.getMetaData();
         int fields = rsmd.getColumnCount();
         System.out.println("Number of fields: " + fields);
         // loop through the to print out the metadata
         for(int i = 0; i<fields; i++)
         {
            System.out.printf("Column Name: %20s || Column Type: %20s\n", rsmd.getColumnName(i+1), rsmd.getColumnTypeName(i+1)); 
         }
         
         int len = 0;//length of column string
         //print out the columns
         System.out.printf("\n");
         for(int j=0;j<fields;j++)
         {
            int width = rsmd.getColumnDisplaySize(j+1);
            len = len + rsmd.getColumnLabel(j+1).length();
            System.out.printf("|%" + width + "s", rsmd.getColumnLabel(j+1));
         }
         //this is to print our dashed lines under the columns
         String lines ="";
         for(int k = 0; k<len + 20;k++)
         {
            lines = lines + "-";
         }
         System.out.printf("\n"+lines + "\n");
         //get the rows
         while (rs.next())
         {
            //print out the rows
            for(int i = 0; i<fields; i++)
            {
               int width = rsmd.getColumnDisplaySize(i+1);
               // go through the row, get the values
               System.out.printf(" %" + width + "s|", rs.getString(i+1));
               if(i == fields -1)
               {
                  System.out.printf("\n");
               }
            }//end for
         }//end while
      }
      catch(Exception e)
      {  
         new DLException(e, "Check your Query. Query: " + query);
      }
   }
}