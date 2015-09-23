package sqlexample;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;

/**
 *
 * @author MAntrim
 */
public class MySqlDbStrategy implements DBStrategy {
    private Connection conn;
    private final String deleteSqlStart = "DELETE FROM ";
    private final String where = " WHERE ";
    private final String deleteSqEquals = " = ";
    private final String singleQuote = "'";
    private PreparedStatement psUpdate;
    private PreparedStatement psInsert;

    @Override
    public void openConnection(String driverClass, String url,
            String userName, String password) throws Exception {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, userName, password);
    }
    
    @Override
    public void closeConnection() throws SQLException {
        conn.close();
    }
    
    @Override
    public List<Map<String,Object>> findAllRecords(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        List<Map<String,Object>> recordList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        //get count of columns
        int columnCount = metaData.getColumnCount();
        
        while(rs.next()) {
            Map<String,Object> record = new HashMap<>();
            for(int i=1; i <= columnCount; i++) {
                record.put(metaData.getColumnName(i),rs.getObject(i));
            }
            recordList.add(record);
        }
        
        return recordList;
    }
    
    @Override
    public void deleteById(String tableName, String primaryKeyFieldName, Object primaryKeyValue) throws SQLException {
       
 
       String sqlCommand;
       Statement stmt = conn.createStatement();
        if(primaryKeyValue instanceof String){
            sqlCommand = this.deleteSqlStart + tableName + " " + this.where+
                    primaryKeyFieldName+ this.deleteSqEquals + this.singleQuote + primaryKeyValue +
                    this.singleQuote;
            //this returns the number of records altered
            stmt.executeUpdate(sqlCommand);
            System.out.println("Delete SQL: " + sqlCommand);
        
    }else{
                       sqlCommand = this.deleteSqlStart + tableName + " " + this.where +
                   primaryKeyFieldName + this.deleteSqEquals +  primaryKeyValue; 
                       System.out.println("Delete SQL: " + sqlCommand);
                       stmt.executeUpdate(sqlCommand);
                       
        }
         
        
       
    
    }
    @Override
    public void insertRecords(String tableName, List columNames, List columnValues) throws SQLException {
        this.psInsert = this.buildInsertStatement(conn, tableName, columNames, columnValues);
        final Iterator i = columnValues.iterator();
        int index = 0;
        while(i.hasNext()){
             if(! isNull(i.next())){
            Object obj = columnValues.get(index);
            psInsert.setObject(index+1, obj);
            index++;
             }
        }
       
        psInsert.executeUpdate();
    }
     @Override
    public void updateRecords(String tableName, String fieldName, Object fieldValue, List colNames, List values) throws SQLException {
        this.psUpdate = this.buildUpdateStatement(conn, tableName, colNames, fieldName);
        final Iterator i = values.iterator();
        int index = 0;
        while(i.hasNext()){
           
            if(! isNull(i.next())){
                 Object obj = values.get(index);
                psUpdate.setObject(index+1, obj);
            index++;
            }
            
        }
        psUpdate.executeUpdate();
    }
    private PreparedStatement buildInsertStatement(Connection conn, String tableName,
            List colNames, List colValues)throws SQLException{
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        (sql.append(tableName)).append(" ( ");
        final Iterator i = colNames.iterator();
        StringBuffer parameters = new StringBuffer();
        int count = 0;
        while(i.hasNext()){
            (sql.append( (String)i.next() )).append(", ");
            parameters.append("?, ");
        }
        sql = new StringBuffer( (sql.toString()).substring( 0,(sql.toString()).lastIndexOf(", ") ) );
        parameters = new StringBuffer( (parameters.toString()).substring( 0,(parameters.toString()).lastIndexOf(", ") ) );
        sql.append(") VALUES ( ");
        sql.append(parameters.toString());
        
        sql.append(" )");
        final String finalSQL=sql.toString();
        System.out.println(sql);
       return conn.prepareStatement(finalSQL); 
    }
    private PreparedStatement buildUpdateStatement(Connection conn, String tableName, List colNames, String whereField)
	throws SQLException {
		StringBuffer sql = new StringBuffer("UPDATE ");
                
		(sql.append(tableName)).append(" SET ");
                //update table name set
		final Iterator i=colNames.iterator();
		while( i.hasNext() ) {
			(sql.append( (String)i.next() )).append(" = ?, ");
                        //update table name set col name = ?,
		}
		sql = new StringBuffer( (sql.toString()).substring( 0,(sql.toString()).lastIndexOf(", ") ) );
                //update table name set col name = ?
		((sql.append(" WHERE ")).append(whereField)).append(" = ?");
                //update table name set col name = ? where whereField = ?
		final String finalSQL=sql.toString();
                System.out.println(finalSQL);
		return conn.prepareStatement(finalSQL);
	}
    public static void main(String[] args) throws Exception {
        MySqlDbStrategy db = new MySqlDbStrategy();
        
        // ALWAYS OPEN THE CONNECTION BEFORE YOU RUN YOUR QUERY
        db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/books", "root", "admin");
        
        System.out.println("All author records before delete:");
        List<Map<String,Object>> records =
                db.findAllRecords("author");
        for(Map record : records) {
            System.out.println(record);
        }
        
        List<String> colNames;
        colNames = new ArrayList<> ();
        String author_name = "author_name";
        colNames.add(author_name);
        List<String> colValue;
        colValue = new ArrayList<> ();
        colValue.add("Update Succeded2");
        
        //db.updateRecords("author", "author_id", 3, colNames, colValue);
        //db.deleteById("author", "author_id", 2);
        
        List<String> columnName;
        columnName = new ArrayList<> ();
        columnName.add("author_name");
        columnName.add("create_date");
        List<String> columnVal;
        columnVal = new ArrayList<> ();
        columnVal.add("New Record");
        columnVal.add("2015-9-22");
        
        
        db.insertRecords("author", columnName, columnVal);
        
        System.out.println("\nAll author records before delete:");
        records =
                db.findAllRecords("author");
        for(Map record : records) {
            System.out.println(record);
        }
        
        // DON'T FORGET TO CLOSE THE CONNECTION WHEN YOU ARE DONE!!!
        db.closeConnection();
    }



  
   
}
