/**
 * @(#)LSOracleToJava.java
 *
 *
 * @author MIke Mountrakis
 * @version 1.00 2007/8/4
 */

package gr.illumine.jetlivesource.mysql;

import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.SqlToJava;

final class MySqlToJava implements SqlToJava{
	// see
	public static final String MYSQL_BIT = "BIT";
	public static final String MYSQL_TINYINT = "TINYINT";
	public static final String MYSQL_BOOL = "BOOL";
	public static final String MYSQL_BOOLEAN = "BOOLEAN";
	public static final String MYSQL_SMALLINT = "SMALLINT";
	public static final String MYSQL_MEDIUMINT = "MEDIUMINT";
	public static final String MYSQL_INT = "INT";
	public static final String MYSQL_INTEGER = "INTEGER";
	public static final String MYSQL_BIGINT = "BIGINT";
	public static final String MYSQL_FLOAT = "FLOAT";
	public static final String MYSQL_DOUBLE = "DOUBLE";
	public static final String MYSQL_DECIMAL = "DECIMAL";
	public static final String MYSQL_DATE = "DATE";
	public static final String MYSQL_DATETIME = "DATETIME";
	public static final String MYSQL_TIMESTAMP = "TIMESTAMP";
	public static final String MYSQL_TIME = "TIME";
	public static final String MYSQL_YEAR = "YEAR";
	public static final String MYSQL_CHAR = "CHAR";
	public static final String MYSQL_VARCHAR = "VARCHAR";
	public static final String MYSQL_BINARY = "BINARY";
	public static final String MYSQL_VARBINARY = "VARBINARY";
	public static final String MYSQL_TINYBLOB = "TINYBLOB";
	public static final String MYSQL_TEXT = "TEXT";
	public static final String MYSQL_MEDIUMBLOB = "MEDIUMBLOB";
	public static final String MYSQL_MEDIUMTEXT = "MEDIUMTEXT";
	public static final String MYSQL_LONGBLOB = "LONGBLOB";
	public static final String MYSQL_LONGTEXT = "LONGTEXT";
	public static final String MYSQL_ENUM = "ENUM";
	public static final String MYSQL_SET  = "SET";
	
	
	public static final String[][] TYPE_MAP = { 
    { MYSQL_BIT , JavaTypes.JAVA_BYTE_AR },
    { MYSQL_TINYINT,  JavaTypes.JAVA_Integer },
    { MYSQL_BOOL,  JavaTypes.JAVA_Integer },
    { MYSQL_BOOLEAN,JavaTypes.JAVA_Integer }, 
    { MYSQL_SMALLINT,   JavaTypes.JAVA_Integer }, 
    { MYSQL_MEDIUMINT,  JavaTypes.JAVA_Integer }, 
    { MYSQL_INT,   JavaTypes.JAVA_Integer }, 
    { MYSQL_INTEGER,  JavaTypes.JAVA_Integer },
    { MYSQL_BIGINT,  JavaTypes.JAVA_Long }, 
    { MYSQL_FLOAT,   JavaTypes.JAVA_Float }, 
    { MYSQL_DOUBLE,  JavaTypes.JAVA_Double },
    { MYSQL_DECIMAL,   JavaTypes.JAVA_Integer },  //check this 
    { MYSQL_DATE,   JavaTypes.JAVA_Date }, 
    { MYSQL_DATETIME,   JavaTypes.JAVA_Timestamp },
    { MYSQL_TIMESTAMP,  JavaTypes.JAVA_Timestamp },
    { MYSQL_TIME, JavaTypes.JAVA_Time}, 
    { MYSQL_YEAR,  JavaTypes.JAVA_Integer },
    { MYSQL_CHAR,  JavaTypes.JAVA_String }, 
    { MYSQL_VARCHAR, JavaTypes.JAVA_String },  
    { MYSQL_BINARY, JavaTypes.JAVA_BYTE_AR}, 
    { MYSQL_VARBINARY,  JavaTypes.JAVA_BYTE_AR}, 
    { MYSQL_TINYBLOB,  JavaTypes.JAVA_BYTE_AR}, 
    { MYSQL_TEXT, JavaTypes.JAVA_String }, 
    { MYSQL_MEDIUMBLOB,  JavaTypes.JAVA_BYTE_AR}, 
    { MYSQL_MEDIUMTEXT,  JavaTypes.JAVA_String }, 
    { MYSQL_LONGBLOB,    JavaTypes.JAVA_BYTE_AR}, 
    { MYSQL_LONGTEXT,  JavaTypes.JAVA_String }, 
    { MYSQL_ENUM, JavaTypes.JAVA_String }, 
    { MYSQL_SET, JavaTypes.JAVA_String }  
    
	};
	public static final String IN = "IN";
	public static final String OUT = "OUT";



	public  String mapType(String dbType) {
		for (int j = 0; j < TYPE_MAP.length; j++)
			if (dbType.equalsIgnoreCase(TYPE_MAP[j][0]))
				return MySqlToJava.TYPE_MAP[j][1];
		return null;

	}

	public  boolean isKnownType(String token) {
		token = token.toUpperCase();
		for (int i = 0; i < TYPE_MAP.length; i++) {
			if (token.startsWith(TYPE_MAP[i][0]))
				return true;
		}

		return false;
	}
}
