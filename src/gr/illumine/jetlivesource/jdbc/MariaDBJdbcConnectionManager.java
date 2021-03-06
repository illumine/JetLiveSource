package gr.illumine.jetlivesource.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;


public class MariaDBJdbcConnectionManager extends ConnectionManager {

	public MariaDBJdbcConnectionManager() {
		super();
	}

	public static Connection getConnection(String mySqlConnectionString,
			String user, String pass) throws NamingException, SQLException {

		Connection conn = null;
		try {
			DriverManager.registerDriver(new org.mariadb.jdbc.Driver());
			conn = DriverManager.getConnection(mySqlConnectionString, user,
					pass); // "jdbc:mysql://localhost/isblt", "root", "");
		} catch (SQLException e) {
			log.severe("getConnection() Cannot acquire JDBC connection."
					+ e.getMessage());
			throw e;
		}
		return conn;
	}

}
