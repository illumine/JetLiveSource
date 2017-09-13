/**
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource;

import gr.illumine.jetlivesource.generators.GeneratorUtil;

public class TableField {
	public static final String TB = GeneratorUtil.TB;
	public String name;
	public String dbType;
	public String javaType;
	public boolean isKey;

	public TableField(String name, String dbType, String outType) {
		this.name = name;
		this.dbType = dbType;
		this.javaType = outType;
		isKey = false;
	}

	public TableField(String name) {
		this(name, "", "");
	}

	public TableField() {
		this("", "", "");
	}

	  
	public String javaFieldName() {
		return GeneratorUtil.javaNameFromSql(name);
	}

	private String javaMethodFieldName(){
		return GeneratorUtil.javaNameFromSqlCapitalFirst(name);
	}
	
	public String javaFieldDeclaration(){
		return TB + "private " + javaType + " " + javaFieldName() + ";\n";
	}
	

	public String javaSetterMethodDeclaration(){
		return TB + "public void  set" + javaMethodFieldName() + "( " + javaType + " " + javaFieldName() + " ){\n"
		       + TB + TB+ "this." +  javaFieldName() + " = " + javaFieldName() + ";\n" 
		       + TB + "}\n";
	}
	
	public String javaSetterMethod(String argumentName ){
		return "set" + javaMethodFieldName() + "(" + argumentName + ")";
	}
	
	public String javaGetterMethodDeclaration(){
		return TB +"public " + javaType + "  get" + javaMethodFieldName() + "(){\n"
		       + TB + TB+ "return this." +  javaFieldName() + ";\n" 
		       + TB + "}\n";
	}
	
	public String javaGetterMethod(){
		return "get" + javaMethodFieldName() + "()";
	}	

	
	public String debug() {
		return toString();
	}

	@Override
	public String toString() {
		return "DBTableField [name=" + name + ", dbType=" + dbType
				+ ", outType=" + javaType + ", isKey=" + isKey + "]\n";
	}

}