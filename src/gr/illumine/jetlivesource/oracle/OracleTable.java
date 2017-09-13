/**
 * @(#)LSOracleTable.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource.oracle;

import gr.illumine.jetlivesource.Table;
import gr.illumine.jetlivesource.TableField;
import gr.illumine.jetlivesource.TableRealizer;

import java.sql.*;


public class OracleTable extends Table implements TableRealizer{

	public OracleTable( Table t ){
		super( t.getSchema(), t.getName(), t.getUseKey(), t.getKey());
	}

	public OracleTable() {
		super("", "", "", "");
	}


	public void createTable(final ResultSet rs) throws SQLException {
		if (rs == null) {
			System.out.println("RS IS NULL!");
			return;
		}

		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			TableField newField = new TableField();
			newField.name = rs.getString("COLUMN_NAME");
			newField.dbType = rs.getString("DATA_TYPE");
			addField(newField);
			i++;
		}

		// System.out.println("Set total Fields : " + i );
	}

	public void findKey(ResultSet rs) throws SQLException {
		while (rs.next()) {
			key = rs.getString("COLUMN_NAME");
		}

		if (key != null) {
			for (int i = 0; i < fields.size(); i++) {
				TableField field = (TableField) fields.get(i);
				if (key.equals(field.name)) {
					field.isKey = true;
				}
			}
		}
	}


}