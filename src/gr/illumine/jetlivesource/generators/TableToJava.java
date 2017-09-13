/**
 * @(#)LSOracleTableToJava.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource.generators;


import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.SqlToJava;
import gr.illumine.jetlivesource.Table;
import gr.illumine.jetlivesource.TableField;


public class TableToJava {
	public static final String TB = GeneratorUtil.TB;
    public static final String DAO_SUFFIX = "DAO";
	
    public String tableNameSuffix = "";

	LiveSourceConfiguration config;
	Table table;
	SqlToJava mapper;

	public TableToJava(LiveSourceConfiguration config, Table table, SqlToJava mapper ) {
		this.config = config;
		this.table = table;
		this.mapper = mapper;
	}

	
	public String createTable() {
		try {
			mapTypes();
			
			String className = getJavaClassName();			
			String s_overall = createClassDeclaration( className);

			s_overall += createFieldDeclarations();
			s_overall += "\n\n//  Getters\n";
			s_overall += createGetters();
			s_overall += "\n\n//  Setters\n";
			s_overall += createSetters();
			s_overall += GeneratorUtil.createToString(table.getFields());
			s_overall += "\n}//class\n";
			return s_overall;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}	
		
	
	public String createTableDAO() {
		try {
			String daoClassName = getJavaClassNameDao();			
			String s_overall = createClassDeclaration( daoClassName);
			s_overall += createInsertStatement();
			s_overall += createInsertSingle();
			s_overall += createUpdate();
			s_overall += createResultSetToArray();
			s_overall += this.createResultSetToList();
			s_overall += createFillPreparedStatement();
			s_overall += createConvertTo2DObject();

			s_overall += "\n}//class\n";
			return s_overall;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}	
	
	
	public String getJavaClassName() {
		if (!config.objectSuffix.equals("")){
			tableNameSuffix = config.objectSuffix;
		}
		String tmp =  table.getName().toUpperCase() + tableNameSuffix;
		
		return GeneratorUtil.javaNameFromSqlCapitalFirst(tmp);
	}

	
	public String getJavaClassNameDao(){
		return getJavaClassName() + DAO_SUFFIX;
	}	
	
//---------------------------------------------------------------	
	private void mapTypes() {
		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			field.javaType = mapper.mapType(field.dbType);
		}
	}

	private String createClassDeclaration( String className){
		String javaText = "";
		
		GeneratorUtil util = new GeneratorUtil(config);
		javaText += util.getFileHeader();
		javaText += "\npackage " + config.packageName + ";";
		javaText += "\nimport java.util.*;";
		javaText += "\nimport java.sql.*;";
		javaText += "\n\n";
		javaText += "public class " + className + "{\n";
		javaText += "\n\n";	
		javaText += "\n" + TB + "public " + className + "(){\n" + TB + TB + "super();\n}\n\n";
		return javaText;
	}
		
	private String createFieldDeclarations() {
		String javaFieldDeclarations = "";

		for (TableField field : table.getFields()) {
			javaFieldDeclarations += field.javaFieldDeclaration();
		}
		return javaFieldDeclarations;
	}

	private String createGetters(){
		String javaGetterMethods = "";
		for (TableField field : table.getFields()) {
			javaGetterMethods += field.javaGetterMethodDeclaration();
		}
		return javaGetterMethods;
	}
	
	private String createSetters(){
		String javaSetterMethods = "";
		for (TableField field : table.getFields()) {
			javaSetterMethods += field.javaSetterMethodDeclaration();
		}
		return javaSetterMethods;
	}
	
	private String createInsertStatement() {
		String tmpStr = "";
		String s_insertStmt = "";

		s_insertStmt = "\n\n"
				+ "public static void insert( Vector<" + getJavaClassName() +
						"> dataVector, Connection conn ) throws SQLException{\n"
				+ TB + "String sql_cmd = \"insert into "
				+ table.getName()+ "( ";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt += field.name;
			tmpStr += "?";
			if (i == table.getFields().size() - 1)
				;
			else {
				s_insertStmt += ",";
				tmpStr += ",";
			}
		}
		s_insertStmt += ") values (" + tmpStr + ")\";\n";
		s_insertStmt += "  PreparedStatement ps = conn.prepareStatement(sql_cmd);\n"
				+ TB + "for(int i=0; i<dataVector.size(); i++ ){\n" + TB
				+ getJavaClassName()
				+ " data = "
				+ "("
				+ getJavaClassName()
				+ ")dataVector.get(i);\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt += TB +TB +"ps" + JavaTypes.getSetterFunction(field)
					+ (i + 1) + " , data." + field.javaGetterMethod() + ");\n";
		}
		s_insertStmt += "\n";

		s_insertStmt += TB + TB +"ps.executeUpdate();\n" 
		             + TB + "}\n"
				     + TB + "ps.close();\n" + "}\n\n";
		return s_insertStmt;
	}
	
	private String createInsertSingle() {
		String tmpStr = "";
		String s_insertStmt = "";

		s_insertStmt = "\n\n" + "public static void insertSingle( "
				+ getJavaClassName()
				+ " data, Connection conn) throws SQLException\n" + "{\n";

		s_insertStmt += TB + "String sql_cmd = \"insert into "
				+ table.getName() + "( ";
		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt += field.name;
			tmpStr += "?";
			if (i == table.getFields().size() - 1)
				;
			else {
				s_insertStmt += ",";
				tmpStr += ",";
			}
		}
		s_insertStmt += ") values (" + tmpStr + ")\";\n\n";

		s_insertStmt += TB + "PreparedStatement ps = conn.prepareStatement(sql_cmd);\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt +=  TB + "ps" + JavaTypes.getSetterFunction(field)
					+ (i + 1) + " , data." + field.javaGetterMethod() + ");\n";
		}
		// s_insertStmt += "\n";

		s_insertStmt += TB +  "ps.executeUpdate();\n" 
		             + TB + "ps.close();\n"
				+ "}\n\n";
		return s_insertStmt;

	}

	private String createUpdate() {
		String s_insertStmt = "";

		s_insertStmt = "\n\n" + "public static void update( " + getJavaClassName()
				+ " data, Connection  conn ) throws SQLException{\n";

		s_insertStmt += TB +"String sql_cmd = \"update "
				+ table.getName().toUpperCase() + " set ";
		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);

			if (!field.name.equals(table.getKey())) {
				s_insertStmt += field.name + " = ?";

				if (i == table.getFields().size() - 1)
					;
				else
					s_insertStmt += ",";
			} else
				;
		}
		s_insertStmt += " where " + table.getKey() + " = ?\";\n\n";

		s_insertStmt += TB + "PreparedStatement ps = conn.prepareStatement(sql_cmd);\n";

		TableField keyField = null;
		int i, j;
		for (i = 0, j = 0; j < table.getFields().size(); j++) {
			TableField field = (TableField) table.getFields().get(j);
			if (!field.name.equals(table.getKey())) {
				s_insertStmt += TB + "ps" + JavaTypes.getSetterFunction(field)
						+ (i + 1) + " , data." + field.javaGetterMethod() + ");\n";
				i++;
			} else {
				keyField = field;
			}

		}
		s_insertStmt += "\n/* Setting the Key */\n";
		s_insertStmt += TB + "ps" + JavaTypes.getSetterFunction(keyField)
				+ (i + 1) + " , data." + keyField.javaGetterMethod() + ");\n";

		s_insertStmt += TB + "ps.executeUpdate();\n" + "   ps.close();\n"
				+ "}\n\n";
		return s_insertStmt;
	}

	private String createFillPreparedStatement() {
		String s_insertStmt = "";

		s_insertStmt = "\n\n"
				+ "public static PreparedStatement fillPreparedStatement( "
				+ getJavaClassName()
				+ " data, PreparedStatement ps ) throws SQLException {\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt += "\n" + TB + "ps" + JavaTypes.getSetterFunction(field)
					+ (i + 1) + " , data." + field.javaGetterMethod() + ");";
		}

		s_insertStmt += "\n" + TB +"return ps;\n}\n\n";
		return s_insertStmt;

	}
	
	private String createConvertTo2DObject() {
		String s_insertStmt = "";

		s_insertStmt = "\n\n" + "public static Object [][] convertTo2DObject( "
				+ getJavaClassName() + " [] data ) throws SQLException{\n\n"
				+ TB  +  "Object [][] oData = new Object[data.length]["
				+ table.getFields().size() + "];\n"
				+ TB + "for(int i=0; i < data.length ; i++ ){\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_insertStmt += "\n" + TB + TB + "oData[i][" + i + "] = (Object) data[i]."
					+ field.javaGetterMethod()+ ";";
		}
		s_insertStmt += "\n" + TB + "}"
				      + "\n" + TB + "return oData;" 
				      + "\n}\n\n";

		return s_insertStmt;
	}	
	
	private String createResultSetToArray() {
		String s_resultSetToArray;

		s_resultSetToArray = "\n\n" + "public static " + getJavaClassName()
				+ " [] resultSetToArray( ResultSet rs ) throws SQLException{\n"
				+ TB +  "ArrayList<"+ getJavaClassName()  + "> resultList = new ArrayList<"+ getJavaClassName() +"> ();\n"
				+ TB +  "while (rs.next()){\n" 
				+ TB + TB + getJavaClassName() + " newItem = new " + getJavaClassName() + "();\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_resultSetToArray += TB + TB +"newItem." + field.javaSetterMethod( "rs"
					+ JavaTypes.getGetterFunction(field)) + ";\n";
		}
		s_resultSetToArray += TB +TB +"resultList.add(newItem);\n";
		s_resultSetToArray += TB +"}\n";
		s_resultSetToArray += TB +  getJavaClassName() + " ret [] = new "
				+ getJavaClassName() + "[resultList.size() ];\n";

		s_resultSetToArray += TB + "resultList.toArray(ret);\n" +
		                      TB + "return ret;\n" 
		                      + "}\n";

		return s_resultSetToArray;
	}

	private String createResultSetToList() {
		String s_resultSetToArray;

		s_resultSetToArray = "\n\n" + "public static List<" + getJavaClassName()
				+ "> resultSetToList( ResultSet rs ) throws SQLException{\n"
				+ TB +"ArrayList<" + getJavaClassName() + "> resultList = new ArrayList<"+ getJavaClassName() +">();\n"
				+ TB +"while (rs.next()){\n" + 
				TB + TB + getJavaClassName() + " newItem = new " + getJavaClassName() + "();\n";

		for (int i = 0; i < table.getFields().size(); i++) {
			TableField field = (TableField) table.getFields().get(i);
			s_resultSetToArray += TB +TB +"newItem." + field.javaSetterMethod("rs"
					+ JavaTypes.getGetterFunction(field)) + ";\n";
		}
		s_resultSetToArray += TB +TB +"resultList.add(newItem);\n";
		s_resultSetToArray += TB +"}\n";
		s_resultSetToArray += TB + "return resultList;\n" 
		                   + "}\n";

		return s_resultSetToArray;
	}


}