/**
 * @(#)LSOracleFunction.java
 *
 *
 * @author 
 * @version 1.00 2007/8/3
 */
package gr.illumine.jetlivesource.oracle;


import gr.illumine.jetlivesource.LiveSourceException;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.ProcedureArgument;
import gr.illumine.jetlivesource.oracle.OracleToJava;


import java.sql.*;


public class OracleProcedure extends Procedure{

	OracleToJava mapper = new OracleToJava();
	
	public OracleProcedure() {
		super();
	}
	
	public OracleProcedure( Procedure proc ) {
		this();
		this.className = proc.getClassName();
		this.name = proc.getName();
		this.packageName = proc.getPackageName();
	}
	



	public void parseStartLine(ResultSet rs) throws LiveSourceException,
			SQLException {
		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			startLine = rs.getInt("LINE");
			i++;
		}
		if (i == 0)
			throw new LiveSourceException("Could not find start line for "
					+ getRefName());
	}

	public void parseEndLine(ResultSet rs) throws LiveSourceException,
			SQLException {
		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			endLine = rs.getInt("LINE");
			i++;
		}
		if (i == 0)
			throw new LiveSourceException("Could not find end start line for "
					+ getRefName());
	}

	public void parseDeclaration(ResultSet rs) throws LiveSourceException,
			SQLException {
		int i = 0;
		while (rs.next()) {
			// System.out.println("Field : " + i );
			declarationText += rs.getString("TEXT");
			i++;
		}
		if (i == 0)
			throw new LiveSourceException("Could not find declaration for "
					+ getRefName());
	}



	public void ProcessDeclarationArguments() {
		int idxL = declarationText.indexOf('(') + 1;
		int idxR = declarationText.indexOf(')');

		String coreDecl = declarationText.substring(idxL, idxR).toUpperCase();

		String[] tokens = coreDecl.split(" ");
		ProcedureArgument argument = new ProcedureArgument();
		String name = "";
		String dir = "";
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals("") || tokens[i].equals("\n")
					|| tokens[i].equals(","))
				continue;
			else if (tokens[i].equals(OracleToJava.IN)) {
				// System.out.println("in");
				dir = OracleToJava.IN;
			} else if (tokens[i].equals(OracleToJava.OUT)) {
				dir = OracleToJava.OUT;
				// System.out.println("out");
			} else if (mapper.isKnownType(tokens[i])) {
				int idx = tokens[i].indexOf(",");
				if (idx != -1)
					tokens[i] = tokens[i].substring(0, idx - 1);
				// System.out.println("Type " + tokens[i]);

				argument.name = name;
				argument.direction = dir;
				argument.dbType = tokens[i];

				arguments.add(argument);
			} else {
				// System.out.println("Name (" + tokens[i] + ")" );
				name = tokens[i];
				argument = new ProcedureArgument();
			}
		}
	}

	public void ProcessDeclarationReturnType() {
		String returnDecl = declarationText.toUpperCase();
		int idxR = declarationText.indexOf("RETURN") + 7;
		returnDecl = declarationText.substring(idxR).toUpperCase();

		String[] tokens = returnDecl.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			// System.out.println("Return Token is : [" + tokens[i] + "]");

			if (tokens[i].equals("") || tokens[i].equals("\n"))
				continue;
			else {
				returnType.name = OracleToJava.RETURN_NAME;
				returnType.dbType = tokens[i];
				returnType.dbType = returnType.dbType.replaceAll(";",
						"");
				returnType.dbType = returnType.dbType.replaceAll("\n",
						"");
				returnType.direction = OracleToJava.OUT;
			}
		}
	}
}
