/**
 * @(#)LSOracleToJava.java
 *
 *
 * @author MIke Mountrakis
 * @version 1.00 2007/8/4
 */

package gr.illumine.jetlivesource.oracle;

import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.SqlToJava;


final class OracleToJava implements SqlToJava{
	// see
	// http://www.stanford.edu/dept/itss/docs/oracle/10g/java.101/b10983/datamap.htm
	/*
	 * ORACLE TYPES
	 */

	// String
	public static final String ORA_VARCHAR2 = "VARCHAR2";
	public static final String ORA_CHAR = "CHAR";
	public static final String ORA_CHARACTER = "CHARACTER";
	public static final String ORA_VARCHAR = "VARCHAR";

	// oracle.sql.NString
	public static final String ORA_NCHAR = "NCHAR";
	public static final String ORA_NVARCHAR2 = "NVARCHAR2";

	// byte[]
	public static final String ORA_RAW = "RAW";
	public static final String ORA_LONG = "LONG";

	// int
	public static final String ORA_BINARY_INTEGER = "BINARY_INTEGER";
	public static final String ORA_NATURAL = "NATURAL";
	public static final String ORA_NATURALN = "NATURALN";
	public static final String ORA_PLS_INTEGER = "PLS_INTEGER";
	public static final String ORA_POSITIVE = "POSITIVE";
	public static final String ORA_POSITIVEN = "POSITIVEN";
	public static final String ORA_SIGNTYPE = "POSITIVEN";
	public static final String ORA_INT = "INT";
	public static final String ORA_INTEGER = "INTEGER";
	public static final String ORA_SMALLINT = "SMALLINT";

	// java.math.BigDecimal
	public static final String ORA_DEC = "DEC";
	public static final String ORA_DECIMAL = "DECIMAL";
	public static final String ORA_NUMBER = "NUMBER";
	public static final String ORA_NUMERIC = "NUMERIC";

	// double
	public static final String ORA_DOUBLE_PRECISION = "DOUBLE PRECISION";
	public static final String ORA_FLOAT = "FLOAT";
	public static final String ORA_REAL = "REAL";

	// java.sql.Timestamp
	public static final String ORA_DATE = "DATE";
	public static final String ORA_TIMESTAMP = "TIMESTAMP";

	// oracle.sql.ROWID
	public static final String ORA_ROWID = "ROWID";
	public static final String ORA_UROWID = "UROWID";
	public static final String ORA_BOOLEAN = "BOOLEAN";

	//BLOB and CLOB
	public static final String ORA_BLOB = "BLOB";
	public static final String ORA_CLOB = "CLOB";
	
	
	public static final String IN = "IN";
	public static final String OUT = "OUT";


	public static final String[][] TYPE_MAP = { { ORA_VARCHAR2, JavaTypes.JAVA_String},
			{ ORA_CHAR, JavaTypes.JAVA_String }, { ORA_CHARACTER, JavaTypes.JAVA_String },
			{ ORA_VARCHAR, JavaTypes.JAVA_String }, { ORA_NCHAR, JavaTypes.JAVA_String },
			{ ORA_NVARCHAR2, JavaTypes.JAVA_String }, { ORA_RAW, JavaTypes.JAVA_BYTE_AR },
			{ ORA_LONG, JavaTypes.JAVA_Long }, { ORA_BINARY_INTEGER, JavaTypes.JAVA_Integer },
			{ ORA_NATURAL, JavaTypes.JAVA_Integer }, { ORA_NATURALN, JavaTypes.JAVA_Integer },
			{ ORA_PLS_INTEGER, JavaTypes.JAVA_Integer }, { ORA_POSITIVE, JavaTypes.JAVA_Integer },
			{ ORA_POSITIVEN, JavaTypes.JAVA_Integer }, { ORA_SIGNTYPE, JavaTypes.JAVA_Integer },
			{ ORA_INT, JavaTypes.JAVA_Integer }, { ORA_INTEGER, JavaTypes.JAVA_Integer },
			{ ORA_SMALLINT, JavaTypes.JAVA_Integer }, { ORA_DOUBLE_PRECISION, JavaTypes.JAVA_Double },
			{ ORA_FLOAT, JavaTypes.JAVA_Double }, { ORA_REAL, JavaTypes.JAVA_Double },
			{ ORA_DATE, JavaTypes.JAVA_Timestamp }, { ORA_TIMESTAMP, JavaTypes.JAVA_Timestamp },
			{ ORA_DEC, JavaTypes.JAVA_Integer }, { ORA_DECIMAL, JavaTypes.JAVA_Integer },
			{ ORA_NUMBER, JavaTypes.JAVA_Integer }, { ORA_NUMERIC, JavaTypes.JAVA_Integer },
			{ ORA_BLOB, JavaTypes.JAVA_Blob }, { ORA_CLOB, JavaTypes.JAVA_Clob }

	};

	public  boolean isKnownType(String token) {
		for (int i = 0; i < TYPE_MAP.length; i++) {
			if (token.startsWith(TYPE_MAP[i][0]))
				return true;
		}

		return false;
	}

	public  String mapType(String dbType) {
		for (int j = 0; j < TYPE_MAP.length; j++)
			if (dbType.equals(TYPE_MAP[j][0]))
				return OracleToJava.TYPE_MAP[j][1];
		return null;

	}

}
