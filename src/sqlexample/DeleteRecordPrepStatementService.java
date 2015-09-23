/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author Mark
 */
public class DeleteRecordPrepStatementService {
      private Connection conn;
    private final String deleteString =
    "DELETE FROM ? WHERE ? = ?";
    private PreparedStatement delete;
    private final int tableIndex = 1;
    private final int keyNameIndex = 2;
    private final int keyValIndex = 3;
    
    
    public void openConnection(String driverClass, String url, String userName, String password) throws Exception
    {
        Class.forName (driverClass);
			  conn = DriverManager.getConnection(url, userName, password);
    }
    
    public void closeConnection() throws Exception
    {
        conn.close();
    }
    public void deleteARecordByKey(String keyName, String keyVal, String tableName)throws Exception{
        boolean success = false;
        
       this.delete = conn.prepareStatement(this.deleteString);
       //this won't work cant pass in tbale name
       
        delete.setObject(this.tableIndex, tableName);
           delete.setObject(this.keyNameIndex, keyName);

          delete.setObject(this.keyValIndex, keyVal);

         delete.execute();
         conn.commit();
        
       
    }
}
