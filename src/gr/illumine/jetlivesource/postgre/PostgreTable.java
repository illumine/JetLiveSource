/**
 * @(#)LSOracleTable.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource.postgre;

import gr.illumine.jetlivesource.Table;
import gr.illumine.jetlivesource.TableField;
import gr.illumine.jetlivesource.TableRealizer;

import java.sql.*;

public class PostgreTable extends Table implements TableRealizer {

	public PostgreTable(Table t) {
		super(t.getSchema(), t.getName(), t.getUseKey(), t.getKey());
	}

	public PostgreTable() {
		super("", "", "", "");
	}

	public void createTable(final ResultSet rs) throws SQLException {
		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			TableField newField = new TableField();
			newField.name = rs.getString("column_name");
			newField.dbType = rs.getString("data_type");

			addField(newField);
			i++;
		}
	}

	@Override
	public void findKey(ResultSet rs) throws SQLException {
		String key = null;
		while (rs.next()) {
			key = rs.getString("column_name");
			System.out.println("Key Field : " + key );
			break;
		}

		if (key != null) {
			for (TableField field : fields) {
				if (field.name.equalsIgnoreCase(key)){
					field.isKey = true;
				}
			}
		}
	}

}