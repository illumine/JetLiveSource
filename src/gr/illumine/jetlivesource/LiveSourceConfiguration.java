package gr.illumine.jetlivesource;

import java.io.*;

import java.util.*;
import org.xml.sax.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

public class LiveSourceConfiguration extends SAXParserHelper {
	
	public enum DatabaseType{
		UNKNOWN,
		ORACLE,
		MYSQL,
		POSTGRESQL,
		MARIADB
	}
	
	
	// XML Syntax
	public static final String VERSION = "v2.0";
	public static final String ELEM_PROJECT = "project";
	public static final String ATTR_PACKAGE = "package";
	public static final String ATTR_SOURCES = "sources";
	public static final String ATTR_AUTHOR  = "author";
	public static final String ATTR_VERSION = "version";
	public static final String ATTR_COMMENT = "comments";
	public static final String ATTR_OBJSUFF = "objectSuffix";

	public static final String ELEM_CONFIG = "configuration";
	public static final String ELEM_DB = "database";
	public static final String ATTR_DB_TYPE = "type";
	public static final String ATTR_DB_USER = "user";
	public static final String ATTR_DB_PASS = "pass";
	public static final String ATTR_DB_CONNECTION_STRING = "connstring";

	public static final String ELEM_TABLE = "table";
	public static final String ATTR_TABLE_NAME = "name";
	public static final String ATTR_TABLE_KEY = "key";
	public static final String ATTR_TABLE_SCHEMA = "schema";

	public static final String ELEM_FUNCTION = "function";
	public static final String ELEM_PROCEDURE = "procedure";
	public static final String ATTR_P_SCHEMA  = "schema";
	public static final String ATTR_P_PACKAGE = "package";
	public static final String ATTR_P_NAME = "name";
	public static final String ATTR_P_CLASS = "class";
	
	public String author = "";
	public String comments = "";
	public String version = "";
	public String packageName = "";
	public String sources = "";
	public String objectSuffix = "";

	public String user = "";
	public String pass = "";
	public String connectionString = "";
	public String databaseType = "";

	public Vector<Table> tables;
	public Vector<Procedure> procedures;
	public Vector<Procedure> functions;

	public LiveSourceConfiguration(String file) throws SAXException,
			ParserConfigurationException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		tables = new Vector<Table>();
		procedures = new Vector<Procedure>();
		functions = new Vector<Procedure>();
		saxParser.parse(new File(file), this);
	}

	public void startElement(String namespaceURI, String lName, String qName,
			Attributes attrs) throws SAXException {
		String eName = lName; // element name

		if (eName.equals(""))
			eName = qName; // namespaceAware = false

		if (eName.equals(ELEM_CONFIG)) {

		}

		else if (eName.equals(ELEM_DB)) {

			String tmp = this.get_attributes_value(attrs, ATTR_DB_USER);

			if (tmp != null)
				user = tmp;
			else
				syntax_error_for_element(ELEM_DB, ATTR_DB_USER);

			tmp = this.get_attributes_value(attrs, ATTR_DB_PASS);
			if (tmp != null)
				pass = tmp;
			else
				syntax_error_for_element(ELEM_DB, ATTR_DB_PASS);

			tmp = this.get_attributes_value(attrs, ATTR_DB_CONNECTION_STRING);
			if (tmp != null)
				connectionString = tmp;
			else
				syntax_error_for_element(ELEM_DB, ATTR_DB_CONNECTION_STRING);

			tmp = this.get_attributes_value(attrs, ATTR_DB_TYPE);
			if (tmp != null)
				databaseType = tmp;
			else
				syntax_error_for_element(ELEM_DB, ATTR_DB_TYPE);
		}

		else if (eName.equals(ELEM_TABLE)) {
			Table newTable = new Table();

			String tmp = this.get_attributes_value(attrs, ATTR_TABLE_NAME);

			if (tmp != null)
				newTable.name = tmp;
			else
				syntax_error_for_element(ELEM_TABLE, ATTR_TABLE_NAME);

			tmp = this.get_attributes_value(attrs, ATTR_TABLE_KEY);
			if (tmp != null)
				newTable.useKey = tmp;
			else
				newTable.useKey = "";

			tmp = this.get_attributes_value(attrs, ATTR_TABLE_SCHEMA);
			if (tmp != null)
				newTable.schema = tmp;
			else
				newTable.schema = "";
		
			tables.add(newTable);
			
		} else if (eName.equals(ELEM_PROJECT)) {
			String tmp = this.get_attributes_value(attrs, ATTR_PACKAGE);
			if (tmp != null)
				packageName = tmp;
			else
				syntax_error_for_element(ELEM_PROJECT, ATTR_PACKAGE);

			tmp = this.get_attributes_value(attrs, ATTR_SOURCES);
			if (tmp != null)
				sources = tmp;
			else
				syntax_error_for_element(ELEM_PROJECT, ATTR_SOURCES);

			tmp = this.get_attributes_value(attrs, ATTR_OBJSUFF);
			if (tmp != null)
				objectSuffix = tmp;
			
			tmp = this.get_attributes_value(attrs, ATTR_AUTHOR);
			if (tmp != null)
				author = tmp;
			
			tmp = this.get_attributes_value(attrs, ATTR_VERSION);
			if (tmp != null)
				version = tmp;
			
			tmp = this.get_attributes_value(attrs, ATTR_COMMENT);
			if (tmp != null)
				comments = tmp;			
		} else if (eName.equals(ELEM_PROCEDURE)) {
			Procedure procedure = new Procedure();

			String tmp = this.get_attributes_value(attrs, ATTR_P_PACKAGE);
			if (tmp != null)
				procedure.setPackageName(tmp);

			tmp = this.get_attributes_value(attrs, ATTR_P_SCHEMA);
			if (tmp != null)
				procedure.setSchema(tmp);
			
			
			tmp = this.get_attributes_value(attrs, ATTR_P_NAME);
			if (tmp != null)
				procedure.setName(tmp);
			else
				syntax_error_for_element(ELEM_PROCEDURE, ATTR_P_NAME);

			tmp = this.get_attributes_value(attrs, ATTR_P_CLASS);
			if (tmp != null)
				procedure.setClassName(tmp);

			procedures.add(procedure);

		} else if (eName.equals(ELEM_FUNCTION)) {
			Procedure function = new Procedure();

			String tmp = this.get_attributes_value(attrs, ATTR_P_PACKAGE);
			if (tmp != null)
				function.setPackageName(tmp);

			tmp = this.get_attributes_value(attrs, ATTR_P_SCHEMA);
			if (tmp != null)
				function.setSchema(tmp);
			
			
			tmp = this.get_attributes_value(attrs, ATTR_P_NAME);
			if (tmp != null)
				function.setName(tmp);
			else
				syntax_error_for_element(ELEM_PROCEDURE, ATTR_P_NAME);

			tmp = this.get_attributes_value(attrs, ATTR_P_CLASS);
			if (tmp != null)
				function.setClassName( tmp);

			functions.add(function);

		}else
			;

	}// startElement

	
	public DatabaseType getType(){
		if( databaseType.equalsIgnoreCase(DatabaseType.ORACLE.toString())){
			return DatabaseType.ORACLE;
		}else if(databaseType.equalsIgnoreCase(DatabaseType.MYSQL.toString())){
			return DatabaseType.MYSQL;
		}else if(databaseType.equalsIgnoreCase(DatabaseType.POSTGRESQL.toString())){
			return DatabaseType.POSTGRESQL;
		}else if(databaseType.equalsIgnoreCase(DatabaseType.MARIADB.toString())){
			return DatabaseType.MARIADB;
		}
		else{
			return DatabaseType.UNKNOWN;
		}	
	}
	
	
	
}
