package gr.illumine.jetlivesource;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TableRealizer {
	public void createTable(final ResultSet rs) throws SQLException;
	
	public void findKey(ResultSet rs) throws SQLException;
	
}
