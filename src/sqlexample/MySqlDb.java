/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlexample;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lucifer
 */
public class MySqlDb {
    
    private Connection conn;
    
    public void openConnection(String driverClass, String url, String userName, String password) throws Exception
    {
        Class.forName (driverClass);
			  conn = DriverManager.getConnection(url, userName, password);
    }
    
    public void closeConnection() throws Exception
    {
        conn.close();
    }
    
    public List<Map<String,Object>> findAllRecords(String tableName) throws Exception
    {
        String sql = "SELECT * FROM " + tableName;
        List<Map<String,Object>> records = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();  // can capture columncount
        int columnCount = metaData.getColumnCount();  // this is max number of coulmns
        
        while (rs.next())
        {
            Map<String,Object> record = new HashMap<>();
            for(int i=1; i<=columnCount; i++)
            {
                record.put(metaData.getColumnName(i), rs.getObject(i));  // places values into a map column name first, then the data for the column(record)
            }
            records.add(record);
        }
        return records;
    }
    
    
    
    
    public static void main(String[] args) throws Exception {
        MySqlDb db = new MySqlDb();
        db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/book", "root", "admin");
        
        List<Map<String,Object>> records = db.findAllRecords("author");
        
        for(Map record : records){
            System.out.println(record);
        }
        
        db.closeConnection();
    }
}
