/**
 * @(#)LSOracleArgument.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/8/4
 */

package gr.illumine.jetlivesource;

public class ProcedureArgument extends TableField{

	//public String name = "";
	//public String dbType = "";
	public String direction = "";
	//public String outType = "";

	public ProcedureArgument() {

	}
	
	

	@Override
	public String toString() {
		return "DBProcedureArgument [name=" + name + ", dbType=" + dbType
				+ ", direction=" + direction + ", outType=" + javaType + "]\n";
	}



	public String debug() {
		return toString();
	}

}