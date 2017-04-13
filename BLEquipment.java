import java.sql.*;
import java.util.*;

public class BLEquipment
{
  public BLEquipment()
  {
  }
  
  public boolean save()
  {
    BLUser user = new BLUser();
    if (user.getAccess() == "Editor" || user.getAccess() == "Admin")
    {
      return true;
    } 
    else
    {
      return false;
    }
  }
}