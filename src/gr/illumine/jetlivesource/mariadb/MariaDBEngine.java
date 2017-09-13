/**
 * @(#)LSOracleEngine.java
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 * @version 2.00 2012/3/21
 */
package gr.illumine.jetlivesource.mariadb;

import org.apache.log4j.Logger;


import gr.illumine.jetlivesource.CodeGenerationEngine;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.LiveSourceConstants;
import gr.illumine.jetlivesource.Procedure;
import gr.illumine.jetlivesource.generators.FunctionToJava;
import gr.illumine.jetlivesource.generators.ProcedureToJava;
import gr.illumine.jetlivesource.generators.TableToJava;
import gr.illumine.jetlivesource.jdbc.DatabaseProxy;
import gr.illumine.jetlivesource.utils.FileUtils;
import gr.illumine.jetlivesource.utils.StringUtils;

import java.sql.*;

public class MariaDBEngine implements CodeGenerationEngine {
	Logger logger = Logger.getLogger(MariaDBEngine.class.getName());

	LiveSourceConfiguration config;
	DatabaseProxy dbProxy;

	public MariaDBEngine(LiveSourceConfiguration config) throws Exception {
		this.config = config;

		dbProxy = new DatabaseProxy(config);
		logger.debug("Connected to DB " + config.connectionString);
	}

	@Override
	public void createTablesViews() throws Exception {

		Connection connection = null;
		Statement statement = null;
		ResultSet rsColums = null, rsKey = null;

		try {
			connection = dbProxy.getConnection();
			MariaDBTable table = null;
			for (int i = 0; i < config.tables.size(); i++) {
				table = new MariaDBTable(config.tables.get(i));
				logger.debug("TABLE : " + i + " is " + table.getName());

				// Q1
				String sql_cmd = "SELECT table_schema, table_name, column_name, data_type, column_key "
						+ " FROM COLUMNS WHERE table_name = '"
						+ table.getName().toUpperCase()
						+ "'  "
						+ getSchema(table)
						+ " ORDER BY ordinal_position";

				statement = connection.createStatement();
				rsColums = statement.executeQuery(sql_cmd);

				table.createTable(rsColums);
				logger.debug("Got table description for "
						+ table.getName().toUpperCase());
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rsColums);

				String dbg = table.debug();
				logger.debug(dbg);

				TableToJava JavaConverter = new TableToJava(config,
						table , new MariaDBToJava());
				
				String jtext = JavaConverter.createTable();

				FileUtils.createAsciiFile( config.sources , JavaConverter.getJavaClassName()+ LiveSourceConstants.JAVA_SUFFIX, jtext);
				
				jtext = JavaConverter.createTableDAO();
				
				FileUtils.createAsciiFile( config.sources , JavaConverter.getJavaClassNameDao()+ LiveSourceConstants.JAVA_SUFFIX, jtext);
				
			}

		} catch (Exception e) {
			throw e;
		} finally {
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rsKey);
			DatabaseProxy.closeResultSet(rsColums);
			DatabaseProxy.closeConnection(connection);
		}
	}

	@Override
	public void createProcedures() throws Exception {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs1 = null;

		try {
			connection = dbProxy.getConnection();
			statement = connection.createStatement();

			statement.execute("use mysql");
			DatabaseProxy.closeStatement(statement);

			// ----
			MariaDBProcedure procedure = null;
			for (int i = 0; i < config.procedures.size(); i++) {
				procedure = new MariaDBProcedure(config.procedures.get(i));
				procedure.setSchema(config.procedures.get(i).getSchema());
				logger.debug("MySql Procedure : " + i + " is " + procedure.getName());

				// Q1
				String sql_cmd = "SELECT param_list , returns FROM proc "
						+ " where name= '" + procedure.getName() + "'"
						+ getSchema(procedure);

				statement = connection.createStatement();
				rs1 = statement.executeQuery(sql_cmd);
				procedure.parseDeclaration(rs1);

				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);

				procedure.ProcessDeclarationArguments();
				procedure.ProcessDeclarationReturnType();
				// logger.debug(procedure.debug());

				ProcedureToJava JavaConverter = new ProcedureToJava(
						config, procedure, new MariaDBToJava());

				String jtext = JavaConverter.createProcedure();

				FileUtils.createAsciiFile(config.sources,
						JavaConverter.getJavaClassName()
								+ LiveSourceConstants.JAVA_SUFFIX, jtext);
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
			DatabaseProxy.closeConnection(connection);
		}
	}

	@Override
	public void createFunctions() throws Exception {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs1 = null;

		try {
			connection = dbProxy.getConnection();
			statement = connection.createStatement();
			statement.execute("use mysql");
			DatabaseProxy.closeStatement(statement);

			// ----
			MariaDBProcedure function = null;
			for (int i = 0; i < config.functions.size(); i++) {
				function = new MariaDBProcedure(config.functions.get(i));
				function.setSchema(config.functions.get(i).getSchema() );
				logger.debug("MySql Function : " + i + " is " + function.getName());

				// Q1
				String sql_cmd = "SELECT param_list , returns FROM proc "
						+ " where name= '" + function.getName() + "'"
						+ getSchema(function);

				statement = connection.createStatement();
				rs1 = statement.executeQuery(sql_cmd);
				function.parseDeclaration(rs1);

				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);

				function.ProcessDeclarationArguments();
				function.ProcessDeclarationReturnType();
				// logger.debug(function.debug());

				FunctionToJava JavaConverter = new FunctionToJava(
						config, function, new MariaDBToJava());

				String jtext = JavaConverter.createFunction();

				FileUtils.createAsciiFile(config.sources,
						JavaConverter.getJavaClassName()
								+ LiveSourceConstants.JAVA_SUFFIX, jtext);

			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
			DatabaseProxy.closeConnection(connection);
		}
	}

//----------------------------------
// Private Routines
//----------------------------------	
	private String getSchema(MariaDBTable table) {
		String clause = "";
		if (!StringUtils.emptyOrNull(table.getSchema())) {
			clause = " and TABLE_SCHEMA='" + table.getSchema() + "' ";
		}

		return clause;
	}

	private String getSchema(Procedure proc) {
		String clause = "";
		if (!StringUtils.emptyOrNull(proc.getSchema())) {
			clause = " and db='" + proc.getSchema() + "' ";
		}
		return clause;
	}

}