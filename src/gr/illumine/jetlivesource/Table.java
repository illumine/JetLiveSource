package gr.illumine.jetlivesource;


import java.util.Vector;

public  class Table {
	protected String schema;
	protected String name;
	protected String useKey;
	protected String key;
	protected Vector<TableField> fields;
	
	public Table(){
		fields = new Vector<TableField>();
	}
	
	
	public Table(String schema, String name, String useKey, String key) {
		this();
		this.schema = schema;
		this.name = name;
		this.useKey = useKey;
		this.key = key;
	}


	public void addField(TableField field) {
		fields.add((TableField) field);
	}
	
	
	public String getKey() {
		if (key !=null && !key.equals("")) {
			return key;
		} else if (!useKey.equals("")) {
			return useKey;
		}

		// If no useKey is specified and key is not exists
		// Set the first field of the table to be the key.
		if (fields.size() > 0) {
			TableField field = (TableField) fields.get(0);
			return field.name;
		} else
			return "";

	}	
	

	public final String getSchema() {
		return schema;
	}


	public final void setSchema(String schema) {
		this.schema = schema;
	}


	public final String getName() {
		return name;
	}


	public final void setName(String name) {
		this.name = name;
	}


	public final String getUseKey() {
		return useKey;
	}


	public final void setUseKey(String useKey) {
		this.useKey = useKey;
	}


	public final Vector<TableField> getFields() {
		return fields;
	}


	public final void setFields(Vector<TableField> fields) {
		this.fields = fields;
	}


	public String debug() {
		String s = "DBTable.debug(): TABLE " + name + "\n";
		for (int i = 0; i < fields.size(); i++) {
			TableField newField = (TableField) fields.get(i);
			s += newField.debug();
		}
		return s;
	}	
}
