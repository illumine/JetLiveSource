
package gr.illumine.jetlivesource.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class JndiConnectionManager extends ConnectionManager {
	
	protected JndiConnectionManager(){
		super();
	}

    public static Connection getConnection( String JNDIname ) throws NamingException, SQLException
	{
    	DataSource dataSource = null;
     	Connection connection = null;
     	
        try{
	        InitialContext ctx = new InitialContext();
	        dataSource = (DataSource) ctx.lookup(JNDIname);
	        if( dataSource != null ){
	        	connection = dataSource.getConnection();
	        }
        }
        catch( NamingException ne ){
        	log.severe("getConnection() Naming Exception for " + JNDIname + " reason : " + ne.toString() );
        	throw ne;
        }catch( SQLException se ){
       	 	log.severe("getConnection() SQL Exception for " + JNDIname + " reason : " + se.toString() );
       	 	throw se;
        }
        
        return connection;
     }
	
	
}
