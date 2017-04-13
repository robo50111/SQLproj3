import java.io.*;
import java .util.Date;
import java.sql.*;

public class DLException extends Exception
{
   private String errMsg;
   private Date date = new Date();
   private Exception e;
   
   public DLException(Exception _e)
   {
      e = _e;
      errMsg = "";
      log();
   }
   public DLException(Exception _e, String _errMsg)
   {
      e = _e;
      errMsg = _errMsg;
      log();
   }
   public void log()
   {
     Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      try
      {
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                              new FileOutputStream(new File("log.txt"), true)));
                          
         writer.write(timestamp +": "+ e.getMessage());
         writer.newLine();
         writer.write(timestamp +": "+ errMsg);
         writer.newLine();
         writer.close();
      }
      catch(Exception e)
      {
         new DLException(e, "Failed to write to the log file");
      }
   }   
}