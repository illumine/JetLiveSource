package gr.illumine.jetlivesource;

public interface CodeGenerationEngine {
	public void createTablesViews() throws Exception ;
	public void createFunctions() throws Exception ;
	public void createProcedures() throws Exception;
}


