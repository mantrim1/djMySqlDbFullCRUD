/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlexample;

import static com.sun.org.apache.bcel.internal.Repository.instanceOf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author Mark
 */
public class DeleteRecordService {
    private Connection conn;
    private final String deleteSqlStart = "DELETE FROM ";
    private final String deleteSqlMiddle = "WHERE ";
    private final String deleteSqEquals = " = ";
    private final String singleQuote = "'";
    
    
    public void openConnection(String driverClass, String url, String userName, String password) throws Exception
    {
        Class.forName (driverClass);
			  conn = DriverManager.getConnection(url, userName, password);
    }
    
    public void closeConnection() throws Exception
    {
        conn.close();
    }
    public boolean deleteARecordByKey(String keyName, String keyVal, String tableName)throws Exception{
        boolean success = false;
       String sqlCommand;
       Statement stmt = conn.createStatement();
        if(keyVal instanceof String){
            sqlCommand = this.deleteSqlStart + tableName + this.deleteSqlMiddle +
                    keyName + this.deleteSqEquals + this.singleQuote + keyVal +
                    this.singleQuote;
            stmt.execute(sqlCommand);
            success = true;
        
    }else{
                       sqlCommand = this.deleteSqlStart + tableName + this.deleteSqlMiddle +
                    keyName + this.deleteSqEquals +  keyVal; 
                       stmt.execute(sqlCommand);
                       success = true;
        }
         
        
        return success;
    }
}
