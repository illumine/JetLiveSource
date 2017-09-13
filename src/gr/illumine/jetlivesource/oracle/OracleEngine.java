/**
 * @(#)LSOracleEngine.java
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 * @version 2.00 2012/3/21
 */
package gr.illumine.jetlivesource.oracle;

import org.apache.log4j.Logger;




import gr.illumine.jetlivesource.CodeGenerationEngine;
import gr.illumine.jetlivesource.LiveSourceConfiguration;
import gr.illumine.jetlivesource.LiveSourceConstants;
import gr.illumine.jetlivesource.generators.FunctionToJava;
import gr.illumine.jetlivesource.generators.ProcedureToJava;
import gr.illumine.jetlivesource.generators.TableToJava;
import gr.illumine.jetlivesource.jdbc.DatabaseProxy;
import gr.illumine.jetlivesource.utils.FileUtils;

import java.sql.*;

public class OracleEngine implements CodeGenerationEngine {
	Logger logger 	=  Logger.getLogger(OracleEngine.class.getName());
	
	LiveSourceConfiguration config;
	DatabaseProxy dbProxy;

	public OracleEngine(LiveSourceConfiguration config)
			throws Exception {
		this.config = config;
		
		dbProxy = new DatabaseProxy(config);
		logger.debug("Connected to DB " + config.connectionString);
	}
	
	
	@Override
	public void createTablesViews() throws Exception {

		
		Connection connection = null;
		Statement  statement = null;
		ResultSet  rsColums = null, rsKey = null;
		
		try{
			connection = dbProxy.getConnection();
			statement = connection.createStatement();
			
			OracleTable table = null;
			for (int i = 0; i < config.tables.size(); i++) {
				table = new OracleTable( config.tables.get(i) );
				logger.debug("TABLE : " + i + " is " + table.getName());			
			
				// Q1
				String sql_cmd = "select column_name, data_type "
						+ "from USER_TAB_COLUMNS " + "where table_name='"
						+ table.getName().toUpperCase() + "' "
						+ "order by column_id asc";
				
				
				rsColums = statement.executeQuery(sql_cmd);
				table.createTable(rsColums);
				logger.debug("Got table description for "
						+ table.getName().toUpperCase());
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rsColums);
				
				// Q2
				sql_cmd = "select column_name from USER_CONS_COLUMNS "
						+ "where table_name='" + table.getName().toUpperCase()
						+ "' and constraint_name like 'PK_%'";

				rsKey = statement.executeQuery(sql_cmd);
				table.findKey(rsKey);
				logger.debug("Got table' key for "
						+ table.getName().toUpperCase() + " Key is "
						+ table.getKey());

				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rsKey);
				
				String dbg = table.debug();
				logger.debug(dbg);

				TableToJava JavaConverter = new TableToJava(
						config, table, new OracleToJava());
				
				
				String jtext = JavaConverter.createTable();

				FileUtils.createAsciiFile( config.sources , JavaConverter.getJavaClassName()+ LiveSourceConstants.JAVA_SUFFIX, jtext);
	
				jtext = JavaConverter.createTableDAO();
				
				FileUtils.createAsciiFile( config.sources , JavaConverter.getJavaClassNameDao()+ LiveSourceConstants.JAVA_SUFFIX, jtext);
			}
			
		}catch(Exception e ){
			throw e;
		}finally{
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rsKey);
			DatabaseProxy.closeResultSet(rsColums);
			DatabaseProxy.closeConnection(connection);
		}
		
	}
		
		


	/*
	 * --Start of declaration : 13 select * from user_source where NAME =
	 * 'PCK_IMS_BUSINESS' and type = 'PACKAGE' and upper(text) like upper(
	 * '%function%fun_ins_appointments%') --End of declaration : 15 select *
	 * from user_source where NAME = 'PCK_IMS_BUSINESS' and type = 'PACKAGE' and
	 * text like '%;%' and line >= 13 and rownum<2
	 * 
	 * --Get Declaration : Text select * from user_source where NAME =
	 * 'PCK_IMS_BUSINESS' and type = 'PACKAGE' and line >= 13 and line <= 15
	 */
	@Override
	public void createFunctions() throws Exception {

		Connection connection = null;
		Statement  statement = null;
		ResultSet  rs1 = null, rs2 = null,rs3 = null;
		
		try{
			connection = dbProxy.getConnection();
			statement = connection.createStatement();
		
			OracleProcedure function = null;	
			for (int i = 0; i < config.procedures.size(); i++) {
				function =  new OracleProcedure( config.procedures.get(i) );
				logger.debug("Oracle Function : " + i + " is "
					+ function.getName());

				// Q1
				String sql_cmd = "select line from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and upper(text) like upper( '%"
						+ function.getName() + "%')";

				rs1 = statement.executeQuery(sql_cmd);
				function.parseStartLine(rs1);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);
				
				logger.debug("Got function start of declaration for "
						+ function.getRefName());

				// Q2
				sql_cmd = "select line from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and upper(text) like '%;%' and line >= "
						+ function.getStartLine() + " and rownum<2";

				rs2 = statement.executeQuery(sql_cmd);
				function.parseEndLine(rs2);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs2);
				logger.debug("Got function end of declaration for "
						+ function.getRefName());

				// Q3
				sql_cmd = "select text from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and line >= "
						+ function.getStartLine() + " and line <= "
						+ function.getEndLine();

				rs3 = statement.executeQuery(sql_cmd);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs3);
				logger.debug("Got function declaration for "
						+ function.getRefName());

				function.ProcessDeclarationArguments();
				function.ProcessDeclarationReturnType();
				logger.debug(function.debug());

				FunctionToJava JavaConverter = new FunctionToJava(
						config, function, new OracleToJava());
				
				String jtext = JavaConverter.createFunction();

				FileUtils.createAsciiFile(config.sources,
						JavaConverter.getJavaClassName()
								+ LiveSourceConstants.JAVA_SUFFIX, jtext);
			}
		} catch (Exception ex) {
				throw ex;
		}finally{
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
			DatabaseProxy.closeResultSet(rs2);
			DatabaseProxy.closeResultSet(rs3);
			DatabaseProxy.closeConnection(connection);			
		}
	}

	@Override
	public void createProcedures() throws Exception {

		Connection connection = null;
		Statement  statement = null;
		ResultSet  rs1 = null, rs2 = null,rs3 = null;
		
		try{
			connection = dbProxy.getConnection();
			statement = connection.createStatement();
		
			OracleProcedure function = null;	
			for (int i = 0; i < config.procedures.size(); i++) {
				function =  new OracleProcedure( config.procedures.get(i) );
				logger.debug("Oracle Function : " + i + " is "
					+ function.getName());

				// Q1
				String sql_cmd = "select line from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and upper(text) like upper( '%"
						+ function.getName() + "%')";

				rs1 = statement.executeQuery(sql_cmd);
				function.parseStartLine(rs1);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs1);
				
				logger.debug("Got function start of declaration for "
						+ function.getRefName());

				// Q2
				sql_cmd = "select line from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and upper(text) like '%;%' and line >= "
						+ function.getStartLine() + " and rownum<2";

				rs2 = statement.executeQuery(sql_cmd);
				function.parseEndLine(rs2);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs2);
				logger.debug("Got function end of declaration for "
						+ function.getRefName());

				// Q3
				sql_cmd = "select text from user_source where NAME = upper('"
						+ function.getPackageName()
						+ "') and type = 'PACKAGE' and line >= "
						+ function.getStartLine() + " and line <= "
						+ function.getEndLine();

				rs3 = statement.executeQuery(sql_cmd);
				DatabaseProxy.closeStatement(statement);
				DatabaseProxy.closeResultSet(rs3);
				logger.debug("Got function declaration for "
						+ function.getRefName());

				function.ProcessDeclarationArguments();
				function.ProcessDeclarationReturnType();
				logger.debug(function.debug());

				ProcedureToJava JavaConverter = new ProcedureToJava(
						config, function, new OracleToJava() );
				
				String jtext = JavaConverter.createProcedure();

				FileUtils.createAsciiFile(config.sources,
						JavaConverter.getJavaClassName()
								+ LiveSourceConstants.JAVA_SUFFIX, jtext);
			}
		} catch (Exception ex) {
				throw ex;
		}finally{
			DatabaseProxy.closeStatement(statement);
			DatabaseProxy.closeResultSet(rs1);
			DatabaseProxy.closeResultSet(rs2);
			DatabaseProxy.closeResultSet(rs3);
			DatabaseProxy.closeConnection(connection);			
		}

		
	}

}