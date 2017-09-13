# JetLiveSource
JDBC Code Generator - implementation of Java DAO pattern   

## Java JDBC Code Generation 
* Stub Class with 1-1 relationship with a Database Record having Getter/Setter methods to handle private attributes/record fields
* Stub Class for DAO implementing methods, search/retrieve/retrieve array/edit/update/delete
* Stub Class for Functione/Procedure call with methods signature 1-1 mapping of arguments in procedure/functions
* Auto implementation - no need to provide mapping of types.

## Compatible Databases
* Oracle Server
* Oracle MySQL
* MariaDB 
* PostGre SQL Server

See the example configuration

## Usage
**Step 1: Configure the database source in the XML configuration file:**
```xml
<configuration>
  Project specific info
	<project package="gr.illumine.docreview.jdbc" 
	         sources="C:\test" 
	         author="Michael Mountrakis mountrakis@illumine.gr"
	         version="1.0.0"
	         comments="Example of Live Source configuration, Enjoy...."/>

  The database to connect and the objects to generate JDBC stub code are
	<database type="MariaDB" connstring="jdbc:mariadb://localhost:3306/information_schema"
		user="root" pass="qweasd" />

		 
  Create DAO/Object stub for that table
	<table name="companies" />
		<table name="author" />
		<table name="document" />
		<table name="review" />
		<table name="reviewer" />

  Create DAO/Object stub for those routines:
  
  <procedure  name="proc_insert_document"/>
	<procedure  name="proc_insert_review"/>  
	<procedure  name="prc_check_selt_test_availability"  /> 
	<procedure  name="prc_find_dslam_info" />            
	<procedure  name="prc_k3_create_request_and_cases"/>
	<procedure  name="prc_monitor_create_request_and_cases"/>
	<function   name="fun_test" /> 
	<function   name="calculate_sign_integral" /> 
	<function   name="fun_test"  class="MyClass2"/> 
	<function   name="fun_test"  class="MyClass3"/> 
	<procedure package="pck_tst" name="MyFunction" class="MyClass"/> 

</configuration>
```

`project` _Generated Java Code Settings_   
`package`: the Java package the generated source will belong. Your project's packaging    
`sources`: the directory to put the generated sources     
`author`: self explanatory :-)    
`version`: the version to appear on the sources as a comment.     
`comments`: some comments regarding this configuration. Will appear as comments on the sources.    


`database` _Target Database details -  Connection Settings_    
`type` : MariDB database parser to be used for DB Object translation.   
`connstring`: how to connect to the MariaDB      
`procedure` : The Oracle Package and Store Procedure that will be called.    

_Target Database details - Database Objects to generate code for _       
`table` : generate DAO/Adapter Java Class for this table or view    
`procedure`  : generate Adapter Java Class for this procedure    
`function`  : generate Adapter Java Class for this database function     


This program also uses log4j. To configure the Log4J please edit the Log4J Properties file ./cls/log4j.properties


**Step 2: Run JetLiveSource against the target DB:**
<pre>
src> java gr.illumine.jetlivesource.LiveSource config/mariadb_config.xml

DEBUG -LiveSource Argument (0) is config/mariadb_config.xml
INFO  -SAXParserHelper SYS ID: file:/C:/workspaces/workspace-zk/JetLiveSource/config/mariadb_config.xml
MARIADB
DEBUG -MariaDBEngine Connected to DB jdbc:mariadb://localhost:3306/information_schema
DEBUG -MariaDBEngine TABLE : 0 is author
DEBUG -MariaDBEngine Got table description for AUTHOR
DEBUG -MariaDBEngine DBTable.debug(): TABLE author
DBTableField [name=id, dbType=int, outType=, isKey=true]
DBTableField [name=name, dbType=varchar, outType=, isKey=false]
DBTableField [name=surname, dbType=varchar, outType=, isKey=false]
DBTableField [name=email, dbType=varchar, outType=, isKey=false]
DBTableField [name=created, dbType=timestamp, outType=, isKey=false]
DBTableField [name=updated, dbType=timestamp, outType=, isKey=false]
....
</pre>

**Step 3: See what is generated and use it**

An example of a Record Java Object Stub
<pre>
//-----------------------------------------------
//  Generated by Jet LiveSource (TM)
//  v2.0
//  on Wed Sep 13 09:46:46 CEST 2017
//  Author  :Michael Mountrakis mountrakis@illumine.gr
//  Version :1.0.0
//  Comments:
//  Example of Live Source configuration, Enjoy....
//-----------------------------------------------



package gr.illumine.docreview.jdbc;
import java.util.*;
import java.sql.*;

public class Author{



   public Author(){
      super();
}

   private Integer id;
   private String name;
   private String surname;
   private String email;
   private java.sql.Timestamp created;
   private java.sql.Timestamp updated;


//  Getters
   public Integer  getId(){
      return this.id;
</pre>

While this is the _AuthorDAO_ to use the _Author_:
<pre>
//-----------------------------------------------
//  Generated by Jet LiveSource (TM)
//  v2.0
//  on Wed Sep 13 09:46:46 CEST 2017
//  Author  :Michael Mountrakis mountrakis@illumine.gr
//  Version :1.0.0
//  Comments:
//  Example of Live Source configuration, Enjoy....
//-----------------------------------------------
package gr.illumine.docreview.jdbc;
import java.util.*;
import java.sql.*;

public class AuthorDAO{

  public AuthorDAO(){
  ...
  public static void insert( Vector<Author> dataVector, Connection conn ) throws SQLException{
  ...
  public static void insertSingle( Author data, Connection conn) throws SQLException{
  ...   
  public static void update( Author data, Connection  conn ) throws SQLException{
  ... 
  public static Author [] resultSetToArray( ResultSet rs ) throws SQLException{
  ..
  public static List<Author> resultSetToList( ResultSet rs ) throws SQLException{
  ..
  public static PreparedStatement fillPreparedStatement( Author data, PreparedStatement ps ) throws SQLException {
  ..
  public static Object [][] convertTo2DObject( Author [] data ) throws SQLException{
  ..
}//class
</pre>

This is an example of Database routine (procedure) stub code:
<pre>
//-----------------------------------------------
//  Generated by Jet LiveSource (TM)
//  v2.0
//  on Wed Sep 13 09:46:46 CEST 2017
//  Author  :Michael Mountrakis mountrakis@illumine.gr
//  Version :1.0.0
//  Comments:
//  Example of Live Source configuration, Enjoy....
//-----------------------------------------------



package gr.illumine.docreview.jdbc;
import java.sql.*;



public class ProcInsertDocumentProc{
   private Integer documentId;
   private String errmsg;
   private Integer errcode;
      
   public ProcInsertDocumentProc(){
   ...

   public Integer  getDocumentId(){
   ..
      
   public void  setDocumentId( Integer documentId )
   public String  getErrmsg(){
   public void  setErrmsg( String errmsg )
   public Integer  getErrcode()
   public void  setErrcode( Integer errcode )

public static  ProcInsertDocumentProc   callProcInsertDocument(
	Connection connection,
	Integer authorid, 
	String doctitle, 
	String docbody, 
	Integer document_id, 
	String errmsg, 
	Integer errcode
	) throws SQLException{
  ....  
}
</pre>



## Author

Jet Live Source  (TM) Illumine Consulting www.illumine.gr   
JDBC Code Generator   
Author Michael Mountrakis    
https://www.linkedin.com/profile/view?illumine    


Main Class : gr.illumine.jetlivesource.LiveSource.java


Provide a simple configuration file like those in ./config
