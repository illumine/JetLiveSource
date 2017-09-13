package gr.illumine.jetlivesource;

import gr.illumine.jetlivesource.utils.StringUtils;

import java.util.Vector;

public class Procedure {
	
	protected String name = "";
	protected String packageName = "";
	protected String schema = "";
	protected String className = "";
	protected int startLine = 0;
	protected int endLine = 0;
	protected String declarationText = "";
	protected Vector<ProcedureArgument> arguments;
	protected ProcedureArgument returnType;
	

	public Procedure(){
		arguments = new Vector<ProcedureArgument>();
		returnType = new ProcedureArgument();
	}


	public final String getName() {
		return name;
	}


	public final void setName(String name) {
		this.name = name;
	}


	public final String getPackageName() {
		return packageName;
	}


	public final void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public final String getSchema() {
		return schema;
	}


	public final void setSchema(String schema) {
		this.schema = schema;
	}


	public final String getClassName() {
		return className;
	}


	public final void setClassName(String className) {
		this.className = className;
	}


	public final int getStartLine() {
		return startLine;
	}


	public final void setStartLine(int startLine) {
		this.startLine = startLine;
	}


	public final int getEndLine() {
		return endLine;
	}


	public final void setEndLine(int endLine) {
		this.endLine = endLine;
	}


	public final String getDeclarationText() {
		return declarationText;
	}


	public final void setDeclarationText(String declarationText) {
		this.declarationText = declarationText;
	}


	public final Vector<ProcedureArgument> getArguments() {
		return arguments;
	}


	public final void setArguments(Vector<ProcedureArgument> arguments) {
		this.arguments = arguments;
	}


	public final ProcedureArgument getReturnType() {
		return returnType;
	}


	public final void setReturnType(ProcedureArgument returnType) {
		this.returnType = returnType;
	}


	public String debug() {
		String txt = "Procedure.debug() for " + getRefName() + "\n"
				+ "Start Line : " + startLine + " End Line " + endLine
				+ "\n" + declarationText + "\n";

		for (int i = 0; i < arguments.size(); i++) {
			ProcedureArgument argument = (ProcedureArgument) arguments.get(i);
			txt += argument.debug();
		}

		if( returnType!= null ){
			txt += returnType.debug();
		}
		
		return txt;
	}
	
	
	public String getRefName() {
		if( StringUtils.emptyOrNull(packageName)){
			return name;
		}else
			return packageName + "." + name;
	}
	
	
}
