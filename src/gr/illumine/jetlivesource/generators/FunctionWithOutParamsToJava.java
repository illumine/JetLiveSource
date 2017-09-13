/**
 * @author Michael Mountrakis
 * @version 1.00 2007/8/2
 */

package gr.illumine.jetlivesource.generators;

import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.ProcedureArgument;
import gr.illumine.jetlivesource.SqlToJava;

import com.mysql.jdbc.StringUtils;

public class FunctionWithOutParamsToJava {

	public static final String TB = GeneratorUtil.TB;
	public static final String NAME_PREFIX = "call_";
	public static final String CLASS_SUFFIX = "_Proc";

	LiveSourceConfiguration config;
	Procedure procedure;
	SqlToJava mapper;

	public FunctionWithOutParamsToJava(LiveSourceConfiguration config,
			Procedure procedure, SqlToJava mapper) {
		this.config = config;
		this.procedure = procedure;
		this.mapper = mapper;
	}

	public String createProcedure() {
		String jtext = "";

		mapTypes();

		System.out.println(procedure.debug());
		GeneratorUtil util = new GeneratorUtil(config);
		jtext += util.getFileHeader();
		jtext += createJavaMethodResultsClass();
		
		procedure.setClassName(getJavaClassName());

		return jtext;
	}

	// ----------------------------------
	// Private Routines
	// ----------------------------------
	private void mapTypes() {
		for (int i = 0; i < procedure.getArguments().size(); i++) {
			ProcedureArgument arg = (ProcedureArgument) procedure
					.getArguments().get(i);
			arg.javaType = mapper.mapType(arg.dbType);
		}

		if (procedure.getReturnType() != null) {
			procedure.getReturnType().javaType = mapper.mapType(procedure
					.getReturnType().dbType);
		}
	}

	private String createJavaMethodResultsClass() {
		String jtext = "";
		// Marshal the OUT argument's returned values
		jtext += "\npackage " + config.packageName + ";";
		jtext += "\nimport java.sql.*;";
		jtext += "\n\n\n\n";

		jtext += "public class " + getJavaClassName() + "{\n\n";
		// Marshal the OUT argument's returned values, declare them as public members
		for (ProcedureArgument arg : procedure.getArguments()) {
			if (arg.direction.equals(SqlToJava.OUT)) {
				jtext += TB + arg.javaFieldDeclaration();
				jtext += TB + arg.javaGetterMethodDeclaration();
				jtext += TB + arg.javaSetterMethodDeclaration();
			}
		}
		// also add the function's return type
		if (procedure.getReturnType() != null) {
			jtext += TB + procedure.getReturnType().javaFieldDeclaration() + "\n";
			jtext += TB + procedure.getReturnType().javaGetterMethodDeclaration();
			jtext += TB + procedure.getReturnType().javaSetterMethodDeclaration();
		}

		//class constructor
		jtext += TB + "public " + getJavaClassName() + "(){\n" + TB + "}\n\n";

		//java method to call the function
		jtext += createJavaMethod();

		jtext += "\n\n}\n";

		return jtext;
	}
//-----------------------------------
// Private methods
//-----------------------------------	
	private String createJavaMethod() {

		String jtext = "public static " + getJavaMethodReturnType() + "  "
				+ getJavaMethodName() + "("
				+ getJavaMathodArgunentsDeclarations()
				+ ") throws SQLException{\n" + TB + "String sql_cmd = \"\";\n"
				+ TB + "CallableStatement cs = null;\n\n" + TB
				+ "sql_cmd = \"{ call ? = " + procedure.getRefName() + "(";

		for (int i = 0; i < procedure.getArguments().size(); i++) {
			if (i < procedure.getArguments().size() - 1) {
				jtext += "?,";
			} else {
				jtext += "?";
			}
		}

		jtext += ")}\";  //Callable Statement\n\n";
		jtext += TB + "cs = connection.prepareCall(sql_cmd);\n\n";

		// Marshal the Return type parameters ONLY in case of a function
		jtext += TB + "cs.registerOutParameter(1,"
				+ JavaTypes.getSqlType(procedure.getReturnType().javaType)
				+ ");\n";

		// Marshal the Input /Output parameters
		int i = 2;
		for (ProcedureArgument arg : procedure.getArguments()) {
			if (arg.direction.equals(SqlToJava.IN) || arg.direction.equals("")) {
				jtext += TB + "cs" +  JavaTypes.getSetterFunction(i, arg) + ";\n";
			} else if (arg.direction.equals(SqlToJava.OUT)) {
				jtext += TB + "cs.registerOutParameter(" + i + ","
						+ JavaTypes.getSqlType(arg.javaType) + ");\n";
			} else
				;
			i++;
		}

		jtext += "\n\n" + TB + "cs.execute();\n\n";

		jtext += TB + getJavaClassName() + TB + "results =\n\t new "
				+ getJavaClassName() + "();\n";

		// Marshal the returned value
		jtext += TB
				+ "results."
				+ procedure.getReturnType().javaSetterMethod( "cs"
				+ JavaTypes.getGetterFunction(1,
						procedure.getReturnType()) ) + ";\n";

		// Marshal the OUT argument's returned values
		i=2;
		for (ProcedureArgument arg : procedure.getArguments()) {
			if (arg.direction.equals(SqlToJava.OUT)) {
				jtext += TB + "results." + arg.javaSetterMethod("cs"
						+ JavaTypes.getGetterFunction( i, arg))
						+ ";\n";
			}
			i++;
		}

		jtext += TB + "return results;";

		jtext += "\n}";

		return jtext;
	}

	private String getJavaMethodReturnType() {
		if (!procedureHasOutParameters()) {
			return " void ";
		} else {
			return " " + getJavaClassName() + " ";
		}
	}

	private String getJavaMathodArgunentsDeclarations() {
		String jtext = "\n\t";

		if (procedure.getArguments().size() == 0) {
			jtext += "Connection connection\n\t";
		} else {
			jtext += "Connection connection,\n\t";
		}

		int i = 0;
		for (ProcedureArgument arg : procedure.getArguments()) {
			jtext += arg.javaType + " " + arg.name;
			if (i < procedure.getArguments().size() - 1)
				jtext += ", ";
			jtext += "\n\t";
			i++;
		}

		return jtext;
	}

	private boolean procedureHasOutParameters() {
		boolean hasOutParameters = false;
		for (ProcedureArgument arg : procedure.getArguments()) {
			if (arg.direction.equals(SqlToJava.OUT)) {
				hasOutParameters = true;
				break;
			}
		}
		
		if( procedure.getReturnType() != null ){
			hasOutParameters = true;
		}
		
		return hasOutParameters;
	}

	private String getJavaMethodName() {
		return GeneratorUtil.javaNameFromSql(NAME_PREFIX +  procedure.getName() );
	}
	
	public String getJavaClassName() {		
		if( StringUtils.isEmptyOrWhitespaceOnly( procedure.getClassName()) ){
			return GeneratorUtil.javaNameFromSqlCapitalFirst(  procedure.getName()+ CLASS_SUFFIX );
		}else{
			return  procedure.getClassName();
		}
	}
}