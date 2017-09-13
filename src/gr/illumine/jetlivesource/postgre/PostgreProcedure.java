/**
 * @(#)LSOracleFunction.java
 *
 *
 * @author 
 * @version 1.00 2007/8/3
 */
package gr.illumine.jetlivesource.postgre;

import gr.illumine.jetlivesource.LiveSourceException;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.ProcedureArgument;


import java.sql.*;

public class PostgreProcedure extends Procedure {

	String retType, argTypes, allArgTypes, argModes, argNames;
	String returnString = "";
	PostgreToJava mapper = new PostgreToJava();

	public PostgreProcedure() {
		super();
	}

	public PostgreProcedure(Procedure proc) {
		this();
		this.setClassName(proc.getClassName());
		this.setName(proc.getName());
		this.setPackageName(proc.getPackageName());
	}

	public void parseDeclaration(ResultSet rs) throws LiveSourceException,
			SQLException {
		boolean found = false;
		while (rs.next()) {
			retType = rs.getString("prorettype");
			argTypes = rs.getString("proargtypes");
			allArgTypes = rs.getString("proallargtypes");
			argModes = rs.getString("proargmodes");
			argNames = rs.getString("proargnames");
			found = true;
			break;
		}
		if (!found)
			throw new LiveSourceException("Could not find declaration for "
					+ getRefName());

		if (allArgTypes != null) {
			allArgTypes = allArgTypes.replace('{', ' ').replace('}', ' ')
					.trim();
		}
		if (argNames != null) {
			argNames = argNames.replace('{', ' ').replace('}', ' ').trim();
		}
		if (argModes != null) {
			argModes = argModes.replace('{', ' ').replace('}', ' ').trim();
		}
		// --debug--
		//if (retType != null)
		//	System.out.println("retType " + retType.toString());
		//if (argTypes != null)
		//	System.out.println("argTypes " + argTypes.toString());
		//if (allArgTypes != null)
		//	System.out.println("allArgTypes " + allArgTypes.toString());
		//if (argModes != null)
		//	System.out.println("argModes " + argModes.toString());
		//if (argNames != null)
		//	System.out.println("argNames " + argNames.toString());
		// --end debug --

		// parse return type
		if (retType != null) {
			returnType.name = PostgreToJava.RETURN_NAME;
			returnType.dbType = retType;
		}

		// parse argument names
		if (argNames != null) {
			String[] names = argNames.split(",");
			for (int i = 0; i < names.length; i++) {
				ProcedureArgument arg = new ProcedureArgument();
				arg.name = names[i];
				arguments.add(arg);
			}
		}

		// parse arguments direction
		if (argModes != null) {
			String[] modes = argModes.split(",");
			for (int i = 0; i < modes.length; i++) {
				ProcedureArgument arg = arguments.get(i);
				if (modes[i].equals("i")) {
					arg.direction = PostgreToJava.IN;
				} else {
					arg.direction = PostgreToJava.OUT;
				}
			}
		} else {
			for (int i = 0; i < arguments.size(); i++) {
				ProcedureArgument arg = arguments.get(i);
				arg.direction = PostgreToJava.IN;
			}
		}
		// parse argument SQL types
		if (allArgTypes != null) {
			String[] sqlTypes = allArgTypes.split(",");
			for (int i = 0; i < sqlTypes.length; i++) {
				ProcedureArgument arg = arguments.get(i);
				arg.dbType = sqlTypes[i];
			}
		} else {
			String[] sqlTypes = argTypes.split(" ");
			for (int i = 0; i < arguments.size(); i++) {
				ProcedureArgument arg = arguments.get(i);
				arg.dbType = sqlTypes[i];
			}
		}
	}

	/**
	 * This transforms Postgre Procedure Types to simple Postgre Types
	 */
	public void processDeclarationArguments() {
		for (ProcedureArgument parg : arguments) {
			parg.dbType = mapper.mapType_func(parg.dbType);
		}
		returnType.dbType = mapper.mapType_func(returnType.dbType);
	}

}
