package gr.illumine.jetlivesource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;


public class OracleJdbcConnectionManager extends ConnectionManager {

	protected OracleJdbcConnectionManager() {
		super();
	}

	public static Connection getConnection(String oracleConnectionString,
			String user, String pass) throws NamingException, SQLException {

		Connection conn = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(oracleConnectionString, user,
					pass); 
		} catch (SQLException e) {
			log.severe("getConnection() Cannot acquire JDBC connection."
					+ e.getMessage());
			throw e;
		}
		return conn;
	}
}
