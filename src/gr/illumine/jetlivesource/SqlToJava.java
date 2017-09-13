package gr.illumine.jetlivesource;


public interface SqlToJava {

	public static final String IN = "IN";
	public static final String OUT = "OUT";
	public static final String RETURN_NAME = "RETURNS";
	
	public boolean isKnownType(String token);
	public String mapType(String dbType);
	
	
}
