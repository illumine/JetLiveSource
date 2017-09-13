package gr.illumine.jetlivesource.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;


public abstract class ConnectionManager{

	public static Logger log = Logger.getLogger(ConnectionManager.class
			.getName());

	protected ConnectionManager() {
	}
	
	
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				if (!conn.isClosed())
					conn.close();
			}
		} catch (SQLException e) {
			// Error during closing database connection. Log error.
			log.warning("releaseConnection() Database release connection error : "
					+ e.toString());
		}
	}

	public static void closePreparedStatement(PreparedStatement ps) {
		try {
			if (ps != null) {
				if (!ps.isClosed())
					ps.close();
			}
		} catch (SQLException e) {
			;
		}
	}

	public static void closeCallableStatement(CallableStatement ps) {
		try {
			if (ps != null) {
				if (!ps.isClosed())
					ps.close();
			}
		} catch (SQLException e) {
			;
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				if (!rs.isClosed())
					rs.close();
			}
		} catch (SQLException e) {
			;
		}
	}

	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				if (!stmt.isClosed())
					stmt.close();
			}
		} catch (SQLException e) {
			;
		}
	}

}
