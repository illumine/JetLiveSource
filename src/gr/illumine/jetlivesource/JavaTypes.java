package gr.illumine.jetlivesource;

public final class JavaTypes {

	public static final String JAVA_Array = "Array";
	public static final String JAVA_InputStream = "InputStream"; 
	public static final String JAVA_BigDecimal = "BigDecimal";
	public static final String JAVA_Blob = "Blob";
	public static final String JAVA_boolean = "boolean";
	public static final String JAVA_Boolean = "Boolean";
	public static final String JAVA_BYTE_AR = "byte []";
	public static final String JAVA_Clob = "Clob";
	public static final String JAVA_Date = "java.sql.Date";
	public static final String JAVA_double = "double";
	public static final String JAVA_Double = "Double";
	public static final String JAVA_float = "float";
	public static final String JAVA_Float = "Float";
	public static final String JAVA_int = "int";
	public static final String JAVA_Integer = "Integer";
	public static final String JAVA_long = "long";
	public static final String JAVA_Long = "Long";
	public static final String JAVA_String = "String";
	public static final String JAVA_Timestamp = "java.sql.Timestamp";
	public static final String JAVA_Time = "java.sql.Time";

	public static final String[][] GET_MAP = { { JAVA_Array, "getArray" },
			{ JAVA_InputStream, "getBinaryStream" },
			{ JAVA_BigDecimal, "getBigDecimal" }, { JAVA_Blob, "getBlob" },
			{ JAVA_Boolean, "getBoolean" }, { JAVA_boolean, "getBoolean" },
			{ JAVA_BYTE_AR, "getBytes" }, { JAVA_Clob, "getClob" },
			{ JAVA_Date, "getDate" }, { JAVA_double, "getDouble" },
			{ JAVA_Double, "getDouble" }, { JAVA_Float, "getFloat" },
			{ JAVA_float, "getFloat" }, { JAVA_int, "getInt" },
			{ JAVA_Integer, "getInt" }, { JAVA_long, "getLong" },
			{ JAVA_Long, "getLong" }, { JAVA_String, "getString" },
			{ JAVA_Timestamp, "getTimestamp" } , { JAVA_Time, "getTime" }};

	public static final String[][] SET_MAP = { { JAVA_Array, "setArray" },
			{ JAVA_InputStream, "setAsciiStream" },
			{ JAVA_BigDecimal, "setBigDecimal" }, { JAVA_Blob, "setBlob" },
			{ JAVA_Boolean, "setBoolean" }, { JAVA_boolean, "setBoolean" },
			{ JAVA_BYTE_AR, "setBytes" }, { JAVA_Clob, "setClob" },
			{ JAVA_Date, "setDate" }, { JAVA_double, "setDouble" },
			{ JAVA_Double, "setDouble" }, { JAVA_Float, "setFloat" },
			{ JAVA_float, "setFloat" }, { JAVA_int, "setInt" },
			{ JAVA_Integer, "setInt" }, { JAVA_long, "setLong" },
			{ JAVA_Long, "setLong" }, { JAVA_String, "setString" },
			{ JAVA_Timestamp, "setTimestamp" }, { JAVA_Time, "setTime" }  };
	
	
   public static final String [][] JAVA_TO_SQL = { { JAVA_Array, "ARRAY" },
		{ JAVA_InputStream, "VARCHAR" },
		{ JAVA_BigDecimal, "DECIMAL" }, { JAVA_Blob, "BLOB" },
		{ JAVA_Boolean, "BOOLEAN" }, { JAVA_boolean, "BOOLEAN" },
		{ JAVA_BYTE_AR, "BLOB" }, { JAVA_Clob, "CLOB" },
		{ JAVA_Date, "DATE" }, { JAVA_double, "DOUBLE" },
		{ JAVA_Double, "DOUBLE" }, { JAVA_Float, "FLOAT" },
		{ JAVA_float, "FLOAT" }, { JAVA_int, "INTEGER" },
		{ JAVA_Integer, "INTEGER" }, { JAVA_long, "LONG" },
		{ JAVA_Long, "LONG" }, { JAVA_String, "VARCHAR" },
		{ JAVA_Timestamp, "TIMESTAMP" }, { JAVA_Time, "TIME" }  };
	
	
   
   public static String getSqlType( String javaType){
		String s_sqlType = "Types.";

		for (int j = 0; j < JAVA_TO_SQL.length; j++){
			if (javaType.equalsIgnoreCase(JAVA_TO_SQL[j][0])){
				s_sqlType += JAVA_TO_SQL[j][1];
				break;
			}
		}
		return s_sqlType;	   
   }

   private static String findGetter(String fieldOutType) {
		String s_get = "";

		for (int j = 0; j < GET_MAP.length; j++){
			if (fieldOutType.equalsIgnoreCase(GET_MAP[j][0])){
				s_get = GET_MAP[j][1];
				break;
			}
		}
		return s_get;	   
   }
   
   
   public static String getGetterFunction(int fieldIdx, TableField field) {
		return "." + findGetter(field.javaType) + "(" + fieldIdx + ")";
	}
   
	public static String getGetterFunction(TableField field) {
		return "." + findGetter(field.javaType)+ "(\"" + field.name + "\")";
	}
   
   
	private static String findSetter(String fieldOutType){
		String s_get = "";

		for (int j = 0; j < SET_MAP.length; j++){
			if (fieldOutType.equalsIgnoreCase(SET_MAP[j][0])){
				s_get = SET_MAP[j][1];
				break;
			}
		}
		return s_get;			
	}
 
	
 	
	public static String getSetterFunction( int fieldIdx, TableField field ){
		return "." + findSetter(field.javaType)+ "( " + fieldIdx + "," + field.name + ")";
	}

	public static String getSetterFunction(TableField field) {
		return "." +  findSetter(field.javaType)+ "(";		
	}
	

}
