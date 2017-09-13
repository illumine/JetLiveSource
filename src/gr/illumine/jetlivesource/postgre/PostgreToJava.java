/**
 * @(#)LSOracleToJava.java
 *
 *
 * @author MIke Mountrakis
 * @version 1.00 2007/8/4
 */

package gr.illumine.jetlivesource.postgre;

import gr.illumine.jetlivesource.JavaTypes;
import gr.illumine.jetlivesource.SqlToJava;

final class PostgreToJava implements SqlToJava{
	// see
	// Numeric
	public static final String PG_SMALLINT = "SMALLINT"; // 2b
	public static final String PG_INTEGER = "INTEGER"; // 4b
	public static final String PG_BIGINT = "BIGINT"; // 8b
	public static final String PG_DECIMAL = "DECIMAL"; // variable
	public static final String PG_NUMERIC = "NUMERIC"; // variable
	public static final String PG_REAL = "REAL"; // 4b
	public static final String PG_DOUBLE_PRECISION = "DOUBLE PRECISION"; // 8b
	public static final String PG_SERIAL = "SERIAL"; // 4b
	public static final String PG_BIGSERIAL = "BIGSERIAL"; // 8b

	// Monetary
	public static final String PG_MONEY = "MONEY"; // 4b

	// Character
	public static final String PG_CHAR_V = "CHARACTER VARYING";
	public static final String PG_VARCHAR = "VARCHAR";
	public static final String PG_CHAR = "CHAR";
	public static final String PG_TEXT = "TEXT";

	// Byte
	public static final String PG_BYTEA = "BYTEA";

	// DATE and TIME
	public static final String PG_TIMESTAMP_NTZ = "TIMESTAMP WITHOUT TIME ZONE"; // 8 bytes
	public static final String PG_TIMESTAMP_TZ = "TIMESTAMP WITH TIME ZONE"; // 12 bytes
	public static final String PG_TIMESTAMP = "TIMESTAMP"; // 8 bytes
	
	public static final String PG_INTERVAL = "INTERVAL"; // 12 bytes
	public static final String PG_DATE = "DATE"; // 4 bytes
	public static final String PG_TIME_NTZ = "TIME WITHOUT TIME ZONE"; // 8 bytes
	public static final String PG_TIME_TZ = "TIME WITH TIME ZONE"; //  12 bytes
	public static final String PG_TIME = "TIME"; // 8 or 12 bytes
	// Boolean
	public static final String PG_BOOLEAN = "BOOLEAN";

	// BIT
	public static final String PG_BIT = "BIT";
	public static final String PG_BIT_VAR = "BIT VARYING";

	public static final String[][] TYPE_MAP = {
			{ PG_SMALLINT, JavaTypes.JAVA_Integer },
			{ PG_INTEGER, JavaTypes.JAVA_Integer },
			{ PG_BIGINT, JavaTypes.JAVA_Long },
			{ PG_DECIMAL, JavaTypes.JAVA_Long },
			{ PG_NUMERIC, JavaTypes.JAVA_Long },
			{ PG_REAL, JavaTypes.JAVA_Double },
			{ PG_DOUBLE_PRECISION, JavaTypes.JAVA_Double },
			{ PG_SERIAL, JavaTypes.JAVA_Integer },
			{ PG_BIGSERIAL, JavaTypes.JAVA_Long },

			// Monetary
			{ PG_MONEY, JavaTypes.JAVA_Integer },

			// Character
			{ PG_CHAR_V, JavaTypes.JAVA_String },
			{ PG_VARCHAR, JavaTypes.JAVA_String },
			{ PG_CHAR, JavaTypes.JAVA_String },
			{ PG_TEXT, JavaTypes.JAVA_String },

			// Byte
			{ PG_BYTEA, JavaTypes.JAVA_BYTE_AR },

			// DATE and TIME
			{ PG_TIMESTAMP, JavaTypes.JAVA_Timestamp },
			{ PG_TIMESTAMP_NTZ, JavaTypes.JAVA_Timestamp },
			{ PG_TIMESTAMP_TZ, JavaTypes.JAVA_Timestamp },
			{ PG_INTERVAL, JavaTypes.JAVA_Long },
			{ PG_TIME, JavaTypes.JAVA_Time },
			{ PG_TIME_NTZ, JavaTypes.JAVA_Time },
			{ PG_TIME_TZ, JavaTypes.JAVA_Time },
			{ PG_DATE, JavaTypes.JAVA_Date },

			// Boolean
			{ PG_BOOLEAN, JavaTypes.JAVA_String },

			// BIT
			{ PG_BIT, JavaTypes.JAVA_String },
			{ PG_BIT_VAR, JavaTypes.JAVA_String } };
	
	public static final String IN = "IN";
	public static final String OUT = "OUT";

	public static final String [][] PRC_TYPES_MAP = {
		{ "bool" , PG_BOOLEAN },
		{ "bytea" ,PG_BYTEA },
		{ "char" , PG_CHAR },
		{ "name" , PG_VARCHAR },
		{ "int8" , PG_BIGSERIAL },
		{ "int2" , PG_INTEGER },
		{ "int2vector" , PG_VARCHAR },
		{ "int4" , PG_INTEGER },
		{ "text" , PG_VARCHAR },
		{ "oid" , PG_VARCHAR },
		{ "tid" , PG_VARCHAR },
		{ "xid" , PG_VARCHAR },
		{ "cid" , PG_VARCHAR },
		{ "oidvector" , PG_VARCHAR },
		{ "pg_type" , PG_VARCHAR },
		{ "pg_attribute" , PG_VARCHAR },
		{ "pg_proc" , PG_VARCHAR },
		{ "pg_class" , PG_VARCHAR },
		{ "xml" , PG_VARCHAR },
		{ "pg_node_tree" , PG_VARCHAR },
		{ "smgr" , PG_VARCHAR },
		{ "point" , PG_VARCHAR },
		{ "lseg" , PG_VARCHAR },
		{ "path" , PG_VARCHAR },
		{ "box" , PG_VARCHAR },
		{ "polygon" , PG_VARCHAR },
		{ "line" , PG_VARCHAR },
		{ "float4" , PG_DOUBLE_PRECISION },
		{ "float8" , PG_DOUBLE_PRECISION },
		{ "abstime" , PG_BIGSERIAL },
		{ "reltime" , PG_BIGSERIAL },
		{ "tinterval" , PG_BIGSERIAL },
		{ "unknown" , PG_VARCHAR },
		{ "circle" , PG_VARCHAR },
		{ "money" , PG_MONEY },
		{ "macaddr" , PG_VARCHAR },
		{ "inet" , PG_VARCHAR },
		{ "bpchar" , PG_VARCHAR },
		{ "varchar" , PG_VARCHAR },
		{ "date" , PG_DATE },
		{ "time" , PG_TIME },
		{ "timestamp" , PG_TIMESTAMP_NTZ },
		{ "timestamptz" , PG_TIMESTAMP_TZ },
		{ "interval" , PG_BIGSERIAL },
		{ "timetz" , PG_TIMESTAMP_TZ },
		{ "bit" , PG_INTEGER },
		{ "varbit" , PG_BIT_VAR },
		{ "numeric" , PG_INTEGER },
		{ "refcursor" , PG_VARCHAR },
		{ "regprocedure" , PG_VARCHAR },
		{ "regoper" , PG_VARCHAR },
		{ "regoperator" , PG_VARCHAR },
		{ "regclass" , PG_VARCHAR },
		{ "regtype" , PG_VARCHAR },
		{ "uuid" , PG_VARCHAR },
		{ "tsvector" , PG_VARCHAR },
		{ "gtsvector" , PG_VARCHAR },
		{ "tsquery" , PG_VARCHAR },
		{ "regconfig" , PG_VARCHAR },
		{ "regdictionary" , PG_VARCHAR },
		{ "record" , PG_VARCHAR },
		{ "cstring" , PG_VARCHAR },
		{ "any" , PG_VARCHAR },
		{ "anyarray" , PG_VARCHAR },
		{ "void" , PG_VARCHAR },
		{ "trigger" , PG_VARCHAR },
		{ "internal" , PG_BIGSERIAL },
		{ "opaque" , PG_BIGSERIAL },
		{ "anyelement" , PG_BIGSERIAL },
		{ "anynonarray" , PG_BIGSERIAL },
		{ "anyenum" , PG_BIGSERIAL }
	};
	
	
	public String mapType_func(String dbType) {
		for (int j = 0; j < PRC_TYPES_MAP.length; j++){
			if (dbType.equalsIgnoreCase(PRC_TYPES_MAP[j][0])){
				return PRC_TYPES_MAP[j][1];
			}
		}
		return null;

	}

	public  boolean isKnownType_func(String token) {
		for (int i = 0; i < PRC_TYPES_MAP.length; i++) {
			if (token.startsWith(PRC_TYPES_MAP[i][0])){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	public String mapType(String dbType) {
		for (int j = 0; j < TYPE_MAP.length; j++){
			if (dbType.equalsIgnoreCase(TYPE_MAP[j][0])){
				return PostgreToJava.TYPE_MAP[j][1];
			}
		}
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
