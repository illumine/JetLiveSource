package gr.illumine.jetlivesource.generators;

import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.ProcedureArgument;
import gr.illumine.jetlivesource.SqlToJava;

import com.mysql.jdbc.StringUtils;

public class FunctionToJava{ 
	public static final String TB = GeneratorUtil.TB;
	public static final String NAME_PREFIX = "call_";
	public static final String CLASS_SUFFIX = "_Func";

	LiveSourceConfiguration config;
	Procedure function;
	SqlToJava mapper;


	public FunctionToJava(LiveSourceConfiguration config,
			Procedure procedure, SqlToJava mapper) {
		this.config = config;
		this.function = procedure;
		this.mapper  = mapper;
	}


	public String createFunction() {
		String jtext = "";

		mapTypes();
		//System.out.println("Types Mapped.");
		GeneratorUtil util = new GeneratorUtil(config);
		jtext += util.getFileHeader();
		jtext += createJavaClassForMethod();
		System.out.println(jtext);
		function.setClassName( getJavaClassName() );

		return jtext;
	}	
	
	//----------------------------------
	// Private Routines
	//----------------------------------	

	private void mapTypes() {
		for (int i = 0; i < function.getArguments().size(); i++) {
			ProcedureArgument arg = (ProcedureArgument) function.getArguments().get(i);
			arg.javaType = mapper.mapType(arg.dbType);
		}

		if( function.getReturnType() != null ){
			function.getReturnType().javaType = mapper.mapType(function.getReturnType().dbType);
		}
	}

	
	private String createJavaClassForMethod() {
		String jtext = "";
		jtext += "\npackage " + config.packageName + ";";
		jtext += "\nimport java.sql.*;";
		jtext += "\n\n\n";

		jtext += "public class " + getJavaClassName()
				+ "{\n";


		jtext += TB + "public " + getJavaClassName()
				+ "(){\n" + TB + "}\n\n";

		jtext += createJavaMethod();

		jtext += "\n\n}\n";

		return jtext;
	}
	
	

	private String createJavaMethod() {
		
		
		String jtext = "public static " + getJavaMethodReturnType()   
		        + "  " + getJavaMethodName() + "("
				+ getJavaMathodArgunentsDeclarations() 
				+ ") throws SQLException{\n" 
				+ TB + "String sql_cmd = \"\";\n"
				+ TB + "CallableStatement cs = null;\n\n"
				+ TB + "sql_cmd = \"{ ? = call " + function.getRefName() + "(";
		for (int i = 0; i < function.getArguments().size(); i++) {
			if (i < function.getArguments().size() - 1)
				jtext += "?,";
			else
				jtext += "?";
		}

		jtext += ")}\";  //Callable Statement\n\n";
		jtext += TB + "cs = connection.prepareCall(sql_cmd);\n";

		//Register Out Parameter for the returned Argument
		int i=1;
		ProcedureArgument retArg = function.getReturnType();
		if (retArg != null) {
			jtext += TB + "cs.registerOutParameter(1,"
			        + JavaTypes.getSqlType(retArg.javaType) +  ");\n";       
		}
		
		//Marshal the IN Arguments
		i=2;
		for (ProcedureArgument arg : function.getArguments()) {
			if (arg.direction.equals(SqlToJava.IN) || arg.direction.equals("")){
				jtext += TB + "cs"
						+ JavaTypes.getSetterFunction( i, arg) + ";\n";
				i++;
			}
		}		
		
		jtext += TB + "cs.execute();\n";
		
		
		// Marshal the Return type parameters ONLY in case of a function
		if (retArg != null) {
			jtext += TB 
					+ retArg.javaType
					+ "  retValue = cs"
					+ JavaTypes.getGetterFunction(1,retArg) + ";\n";
			jtext += " cs.close();\n";
			jtext += " return retValue;\n\n";
		}
		
		
		jtext += "\n}";
		
		return jtext;
	}


	private String getJavaMethodReturnType(){
		String jtext = "";
		if (function.getReturnType() != null) {	
		 jtext +=  function.getReturnType().javaType;
		}
		return jtext;
	}
	
	private String getJavaMathodArgunentsDeclarations() {
		String jtext = "\n\t";
		
		if( function.getArguments().size() == 0 ){
			jtext += "Connection connection\n\t";
		}else{
			jtext += "Connection connection,\n\t";
		}
		
		int i =0;
		for (ProcedureArgument arg : function.getArguments()) {
			jtext += arg.javaType + " " + arg.name;
			if (i < function.getArguments().size() - 1)
				jtext += ", ";
			jtext += "\n\t";
			i++;
		}

		return jtext;
	}	
	
	
	
	private String getJavaMethodName() {
		return GeneratorUtil.javaNameFromSql(NAME_PREFIX + function.getName() );
	}
	
	public String getJavaClassName() {		
		if( StringUtils.isEmptyOrWhitespaceOnly(function.getClassName()) ){
			return GeneratorUtil.javaNameFromSqlCapitalFirst( function.getName()+ CLASS_SUFFIX );
		}else{
			return function.getClassName();
		}
	}

		
}