package gr.illumine.jetlivesource.jdbc;

import gr.illumine.jetlivesource.LiveSourceConfiguration;

import java.text.*;
import java.sql.*;

import javax.naming.NamingException;


/*
 * ASYNCHRONOUS : Creates Connection on the fly excecutes the report store procedure
 * and terminates the connection within the same method.
 */
/**
 * TODO Mike change this class
 */
public class DatabaseProxy  extends ConnectionManager{
	
	public enum DatabaseType{
		ORACLE,
		MYSQL
	}
	
	LiveSourceConfiguration config;

	//TODO Change me!!!!!!!!!!!!!
	public DatabaseProxy(LiveSourceConfiguration config) throws Exception {
		this.config = config;
	}

	public Connection getConnection() throws SQLException, NamingException{
		Connection connection = null;
		if( config.getType() == LiveSourceConfiguration.DatabaseType.ORACLE ){
			connection = OracleJdbcConnectionManager.getConnection(config.connectionString, config.user, config.pass);
		}else if(config.getType() == LiveSourceConfiguration.DatabaseType.MYSQL){
			connection = MySqlJdbcConnectionManager.getConnection(config.connectionString, config.user, config.pass);
		}else if(config.getType() == LiveSourceConfiguration.DatabaseType.POSTGRESQL){
			connection = PostgreConnectionManager.getConnection(config.connectionString, config.user, config.pass);
		}else if(config.getType() == LiveSourceConfiguration.DatabaseType.MARIADB){
			connection = MariaDBJdbcConnectionManager.getConnection(config.connectionString, config.user, config.pass);
		}
		
		else{
			throw new SQLException("Unknown database type " + config.databaseType);
		}		
		
		return connection;
	}
	

	public static java.sql.Date javaDate2SqlDate(java.util.Date jd) {
		if (jd == null)
			return null;
		else
			return new java.sql.Date(jd.getTime());
	}

	public static java.util.Date SqlDate2JavaDate(java.sql.Date sqld) {
		if (sqld == null)
			return null;
		else
			return new java.util.Date(sqld.getTime());
	}

	public static java.sql.Timestamp javaDate2SqlTimestamp(java.util.Date jd) {
		if (jd == null)
			return null;
		else
			return new java.sql.Timestamp(jd.getTime());
	}

	public static java.util.Date SqlTimestamp2JavaDate(java.sql.Timestamp sqlTS) {
		if (sqlTS == null)
			return null;
		else
			return new java.util.Date(sqlTS.getTime());
	}

	// looses the TIME part of the timestamp. Conversion cannot be done.
	// See page
	// http://www.thunderguy.com/semicolon/2003/08/14/java-sql-date-is-not-a-real-date/
	public static java.sql.Date SqlTimestamp2SqlDate(java.sql.Timestamp sqlTS) {
		if (sqlTS == null)
			return null;
		else
			return new java.sql.Date(sqlTS.getTime());
	}

	public static java.sql.Timestamp SqlDate2SqlTimestamp(java.sql.Date sqlD) {
		if (sqlD == null)
			return null;
		else
			return new java.sql.Timestamp(sqlD.getTime());
	}

	// example
	public static java.util.Date dateFromString(String s_date)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		java.util.Date D1 = formatter.parse(s_date);
		return D1;
	}

}// class

