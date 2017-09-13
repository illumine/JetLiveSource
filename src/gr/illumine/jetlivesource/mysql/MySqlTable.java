/**
 * @(#)LSOracleTable.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource.mysql;

import gr.illumine.jetlivesource.Table;
import gr.illumine.jetlivesource.TableField;
import gr.illumine.jetlivesource.TableRealizer;

import java.sql.*;


public class MySqlTable extends Table implements TableRealizer{


	public MySqlTable( Table t ){
		super( t.getSchema(), t.getName(), t.getUseKey(), t.getKey());
	}
	

	public MySqlTable() {
		super("","", "", "");
	}


	public void createTable(final ResultSet rs) throws SQLException {
		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			TableField newField = new TableField();
			newField.name = rs.getString("COLUMN_NAME");
			newField.dbType = rs.getString("DATA_TYPE");
			
			String isKey = rs.getString("COLUMN_KEY");
			if( isKey.equalsIgnoreCase("PRI")){
				newField.isKey = true;
			}
			
			addField(newField);
			i++;
		}
	}

	@Override
	public void findKey(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		
	}




}