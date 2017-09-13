package gr.illumine.jetlivesource.utils;

public class StringUtils {

	public static boolean emptyOrNull(String s){
		return ( s == null || s.equals("") ? true : false);
	}
	
	public static boolean isWhiteSpace(String s){
		if( !emptyOrNull(s) ){
			return ( s.equals("\n") || s.equals("\t") ? true: false);
		}else{
			return true;
		}
	}
	
	public static String normalizeWhiteSpaces( String s){
		String out = "";
		boolean previousWasSpace = false;
		for(int i=0; i<s.length(); i++ ){
			if( s.charAt(i) == ' ' ||  s.charAt(i) == '\t' ){
				if( !previousWasSpace ){
					out += s.charAt(i);
				}
				previousWasSpace = true;
			}else{
				out += s.charAt(i);
				previousWasSpace = false;
			}	
		}
		return out.trim();
	}
	
	
}
