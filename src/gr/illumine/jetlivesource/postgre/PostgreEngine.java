/**
 * @(#)LSOracleEngine.java
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 * @version 2.00 2012/3/21
 */
package gr.illumine.jetlivesource.postgre;

import org.apache.log4j.Logger;

import gr.illumine.jetlivesource.CodeGenerationEngine;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.LiveSourceConstants;
import gr.illumine.jetlivesource.ProcedureArgument;
import gr.illumine.jetlivesource.generators.FunctionWithOutParamsToJava;
import gr.illumine.jetlivesource.generators.ProcedureToJava;
import gr.illumine.jetlivesource.generators.TableToJava;
import gr.illumine.jetlivesource.jdbc.DatabaseProxy;
import gr.illumine.jetlivesource.utils.FileUtils;
import gr.illumine.jetlivesource.utils.StringUtils;

import java.sql.*;

public class PostgreEngine implements CodeGenerationEngine {
	Logger logger = Logger.getLogger(PostgreEngine.class.getName());

	LiveSourceConfiguration config;
	DatabaseProxy dbProxy;

	public PostgreEngine(LiveSourceConfiguration config) throws Exception {
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
			PostgreTable table = null;
			for (int i = 0; i < config.tables.size(); i++) {
				table = new PostgreTable(config.tables.get(i));
				logger.debug("TABLE : " + i + " is " + table.getName());

				// Q1
				String sql_cmd = "select column_name, data_type from INFORMATION_SCHEMA.COLUMNS  where "
						+ " table_name='"
						+ table.getName()
						+ "' "
						+ getSchema(table) + " order by ordinal_position";
				statement = connection.createStatement();
				rsColums = statement.executeQuery(sql_cmd);

				table.createTable(rsColums);
				logger.debug("Got table description for "
						+ table.getName().toUpperCase());

				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rsColums);

				// Q2
				sql_cmd = "select column_name from information_schema.key_column_usage where "
						+ " table_name='"
						+ table.getName()
						+ "' "
						+ getSchema(table) + " order by ordinal_position";
				statement = connection.createStatement();
				rsColums = statement.executeQuery(sql_cmd);

				table.findKey(rsColums);
				logger.debug("Got table description for "
						+ table.getName().toUpperCase());
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rsColums);

				String dbg = table.debug();
				logger.debug(dbg);

				TableToJava JavaConverter = new TableToJava(config, table,
						new PostgreToJava());

				String jtext = JavaConverter.createTable();

				FileUtils.createAsciiFile(config.sources,
						JavaConverter.getJavaClassName()
								+ LiveSourceConstants.JAVA_SUFFIX, jtext);
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

			// ----
			PostgreProcedure procedure = null;
			for (int i = 0; i < config.procedures.size(); i++) {
				procedure = new PostgreProcedure(config.procedures.get(i));
				procedure.setSchema(config.procedures.get(i).getSchema());
				logger.debug("PostgreSQL Procedure : " + i + " is "
						+ procedure.getName());

				// Q1
				String sql_cmd = "select prorettype, proargtypes, proallargtypes, proargmodes, proargnames "
						+ " from pg_proc pr, pg_type tp "
						+ " where proname='"
						+ procedure.getName()
						+ "'"
						+ " and tp.oid = pr.prorettype "
						+ " and pr.proisagg=FALSE "
						+ " and tp.typname <> 'trigger'"
						+ " and pr.pronamespace in ("
						+ " select oid from pg_namespace where nspname not like 'pg_%' "
						+ " and nspname != 'information_schema' )";

				statement = connection.createStatement();
				rs1 = statement.executeQuery(sql_cmd);
				//creates -> oids
				procedure.parseDeclaration(rs1);

				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);

				//oids -> pgsql types
				procedure = parseSqlDataTypes(procedure);
				
				//pgsql types -> PG_TYPES
				procedure.processDeclarationArguments();
				logger.debug(procedure.debug());

				ProcedureToJava JavaConverter = new ProcedureToJava(config,
						procedure, new PostgreToJava());

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

			// ----
			PostgreProcedure function = null;
			for (int i = 0; i < config.functions.size(); i++) {
				function = new PostgreProcedure(config.functions.get(i));
				function.setSchema(config.functions.get(i).getSchema());
				logger.debug("PostgreSQL Procedure : " + i + " is "
						+ function.getName());

				// Q1
				String sql_cmd = "select prorettype, proargtypes, proallargtypes, proargmodes, proargnames "
						+ " from pg_proc pr, pg_type tp "
						+ " where proname='"
						+ function.getName()
						+ "'"
						+ " and tp.oid = pr.prorettype "
						+ " and pr.proisagg=FALSE "
						+ " and tp.typname <> 'trigger'"
						+ " and pr.pronamespace in ("
						+ " select oid from pg_namespace where nspname not like 'pg_%' "
						+ " and nspname != 'information_schema' )";

				statement = connection.createStatement();
				rs1 = statement.executeQuery(sql_cmd);
				//creates -> oids
				function.parseDeclaration(rs1);
                //System.out.println(function.debug()); 
				
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);

				//oids -> pgsql types
				function = parseSqlDataTypes(function);
				//System.out.println(function.debug()); 
				
				//pgsql types -> PG_TYPES
				function.processDeclarationArguments();
				//System.out.println(function.debug());

				FunctionWithOutParamsToJava JavaConverter = new FunctionWithOutParamsToJava(config,
						function, new PostgreToJava());

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

	// ----------------------------------
	// Private Routines
	// ----------------------------------
/**
 * Populates retArg.dbType with  a PL/pgSQL type instead of OID
 */
	private PostgreProcedure parseSqlDataTypes(PostgreProcedure proc)
			throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs1 = null;

		try {
			connection = dbProxy.getConnection();
			for (ProcedureArgument parg : proc.getArguments()) {
				parg.dbType = queryPostgreType(connection, parg.dbType);
			}
			ProcedureArgument retArg = proc.getReturnType();
			retArg.name = PostgreToJava.RETURN_NAME;
			retArg.direction = PostgreToJava.OUT;
			retArg.dbType = queryPostgreType(connection, retArg.dbType);
		} catch (Exception ex) {
			throw ex;
		} finally {
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
			DatabaseProxy.closeConnection(connection);
		}
		return proc;
	}

	private String queryPostgreType(Connection connection, String oid)
			throws SQLException {
		Statement statement = null;
		ResultSet rs1 = null;
		String postgreFunctionArgumentType = null;
		try {
			String sql_cmd = "select typname from  pg_type   where oid=" + oid;
			statement = connection.createStatement();
			rs1 = statement.executeQuery(sql_cmd);
			while (rs1.next()) {
				postgreFunctionArgumentType = rs1.getString("typname");
				break;
			}
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
		} finally {
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
		}

		return postgreFunctionArgumentType;
	}

	private String getSchema(PostgreTable table) {
		String clause = "";
		if (!StringUtils.emptyOrNull(table.getSchema())) {
			clause = " and table_schema=''" + table.getSchema() + "' ";
		}

		return clause;
	}

}