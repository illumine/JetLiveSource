/**
 * @(#)LSOracleFunction.java
 *
 *
 * @author 
 * @version 1.00 2007/8/3
 */
package gr.illumine.jetlivesource.mysql;


import gr.illumine.jetlivesource.LiveSourceException;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.ProcedureArgument;
import gr.illumine.jetlivesource.utils.StringUtils;

import java.sql.*;


public class MySqlProcedure extends Procedure{

	String returnString;

	MySqlToJava mapper = new MySqlToJava();
	
	
	public MySqlProcedure() {
		super();
	}
	
	public MySqlProcedure( Procedure proc ) {
		this();
		this.setClassName( proc.getClassName() );
		this.setName(proc.getName());
		this.setPackageName(proc.getPackageName() );
	}
	


	public void parseDeclaration(ResultSet rs) throws LiveSourceException,
			SQLException {
		boolean found = false;
		while (rs.next()) {
			declarationText = rs.getString("param_list");
			returnString = rs.getString("returns");
			found = true;
			//System.out.println("RETURN TYPE[" + returnString + "]" );
			break;
		}
		if (!found)
			throw new LiveSourceException("Could not find declaration for "
					+ getRefName() );
	}


/*
IN  v_dslam_name     VARCHAR  (  32 ),
IN  v_card_id        INT,
IN  v_port_id        INT,
OUT o_is_valid       INT,
OUT o_error          VARCHAR(256 )
 */
	public void ProcessDeclarationArguments() {

		declarationText = declarationText.replace('`', ' ').replace('\n', ' ');
		declarationText = StringUtils.normalizeWhiteSpaces(declarationText);
		String[] tokens = declarationText.split(" ");
		
		ProcedureArgument argument = new ProcedureArgument();
		String name = "", dir = "";
		for (int i = 0; i < tokens.length; i++) {
			if (   StringUtils.emptyOrNull( tokens[i] ) || StringUtils.isWhiteSpace(tokens[i]) )
				continue;
			else if (tokens[i].equalsIgnoreCase(MySqlToJava.IN) || tokens[i].equalsIgnoreCase(MySqlToJava.OUT)  ) {
				dir =  tokens[i].toUpperCase();
				//System.out.println("Direction : " + dir );
				continue;
			}else if (mapper.isKnownType(tokens[i])) {
				//System.out.println("Type : " + tokens[i] );
				argument.name = name;
				argument.direction = dir;
				argument.dbType = parseArgumentType( tokens[i] );
				arguments.add(argument);
			} else {
				name = tokens[i];
				//System.out.println("Name : " + tokens[i] );
				argument = new ProcedureArgument();
			}
		}
	}

	public void ProcessDeclarationReturnType() {
		if( !StringUtils.emptyOrNull(returnString) ){
			returnType.name = MySqlToJava.RETURN_NAME;
			returnType.dbType = parseArgumentType(returnString);
			returnType.direction = MySqlToJava.OUT;
		}else{
			returnType = null;
			System.out.println("Procedure does not return anything.....");
		}
	}
	
	private String parseArgumentType( String argType ){
		int idx = argType.indexOf(",");
		if (idx != -1)
			argType = argType.trim().substring(0, idx);

		//case argument of type VARCHAR(xxx)
		idx = argType.indexOf("(");
		if (idx != -1)
			argType = argType.trim().substring(0, idx);
		return argType.toUpperCase();
	}
	
}
