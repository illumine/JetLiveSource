/**
 * @(#)LiveSource.java
 *
 *
 * @author Mike Mountrakis
 * @version 1.00 2007/6/24
 */
package gr.illumine.jetlivesource;

import gr.illumine.jetlivesource.jdbc.DatabaseProxy;
import gr.illumine.jetlivesource.mariadb.MariaDBEngine;
import gr.illumine.jetlivesource.mysql.MySqlEngine;
import gr.illumine.jetlivesource.oracle.OracleEngine;
import gr.illumine.jetlivesource.postgre.PostgreEngine;

import java.sql.SQLException;

import org.apache.log4j.Logger;


public class LiveSource {

	LiveSourceConfiguration config;
	Logger logger;
	DatabaseProxy dbProxy;

	public LiveSource() {
		logger = Logger.getLogger(LiveSource.class.getName());
	}

	public void init(String[] args) {
		try {
			config = new LiveSourceConfiguration(args[0]);

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	}

	public void process() {
		try {
			System.out.println(config.getType() );
			if( config.getType() == LiveSourceConfiguration.DatabaseType.ORACLE ){
				OracleEngine oracleEngine = new OracleEngine(config);
				oracleEngine.createTablesViews();
				oracleEngine.createProcedures();
				oracleEngine.createFunctions();
				
			}else if(config.getType() == LiveSourceConfiguration.DatabaseType.MYSQL){
				MySqlEngine mySqlEngine = new MySqlEngine(config);
				mySqlEngine.createTablesViews();
				mySqlEngine.createProcedures();
				mySqlEngine.createFunctions();
			
			}else if(config.getType() == LiveSourceConfiguration.DatabaseType.MARIADB){
				MariaDBEngine mariadbEngine = new MariaDBEngine(config);
				mariadbEngine.createTablesViews();
				mariadbEngine.createProcedures();
				mariadbEngine.createFunctions();
			}			
			else if(config.getType() == LiveSourceConfiguration.DatabaseType.POSTGRESQL){
				PostgreEngine postgreEngine = new PostgreEngine(config);
				postgreEngine.createTablesViews();
				postgreEngine.createProcedures();
				postgreEngine.createFunctions();
			}else{
				throw new SQLException("Unknown database type " + config.databaseType);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void cleanup() {

	}

	public static void main(String[] args) {
		LiveSource LS = new LiveSource();
		LS.argumentListError(args);
		LS.init(args);
		LS.process();
		LS.cleanup();

	}

	void argumentListError(String[] args) {
		for (int i = 0; i < args.length; i++)
			logger.debug("Argument (" + i + ") is " + args[i]);
		if (args.length < 1) {
			logger.error("Missing configuration file");
			System.exit(1);
		}
	}
}
