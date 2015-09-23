/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlexample;

import java.sql.Connection;

/**
 *
 * @author Mark
 */
public class DeleteRecordStartup {
   

	public static void main(String[] args) {

                DeleteRecordService delete = new DeleteRecordService();
                try{
                delete.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://bit.glassfish.wctc.edu:3306/sakila", "advjava", "advjava");
                
                
                    delete.deleteARecordByKey("12345","Id", "Actor");
                    
                   delete.closeConnection();
                   
                }catch(Exception ex){
                     System.out.println(ex.getMessage());
                }
                
        }  
                
}
