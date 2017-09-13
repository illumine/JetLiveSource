//-----------------------------------------------
//  Generated by Jet LiveSource (TM)
//  v2.0
//  on Thu Jun 21 17:42:17 EEST 2012
//  Author  :Michael Mountrakis mountrakis@uit.gr
//  Version :1.0.0
//  Comments:
//  Example of Live Source configuration, Enjoy....
//-----------------------------------------------



package gr.uit.jetexamples;
import java.sql.*;



public class FunCheckWeather1Proc{

      private Integer pTempH;
      public Integer  getPTempH(){
      return this.pTempH;
   }
      public void  setPTempH( Integer pTempH ){
      this.pTempH = pTempH;
   }
      private Integer pTempL;
      public Integer  getPTempL(){
      return this.pTempL;
   }
      public void  setPTempL( Integer pTempL ){
      this.pTempL = pTempL;
   }
      private Integer pId;
      public Integer  getPId(){
      return this.pId;
   }
      public void  setPId( Integer pId ){
      this.pId = pId;
   }
      private String returns;

      public String  getReturns(){
      return this.returns;
   }
      public void  setReturns( String returns ){
      this.returns = returns;
   }
   public FunCheckWeather1Proc(){
   }

public static  FunCheckWeather1Proc   callFunCheckWeather1(
	Connection connection,
	String p_city, 
	java.sql.Date p_created, 
	Integer p_temp_h, 
	Integer p_temp_l, 
	Integer p_id
	) throws SQLException{
   String sql_cmd = "";
   CallableStatement cs = null;

   sql_cmd = "{ call ? = fun_check_weather1(?,?,?,?,?)}";  //Callable Statement

   cs = connection.prepareCall(sql_cmd);

   cs.registerOutParameter(1,Types.VARCHAR);
   cs.setString( 2,p_city);
   cs.setDate( 3,p_created);
   cs.registerOutParameter(4,Types.INTEGER);
   cs.registerOutParameter(5,Types.INTEGER);
   cs.registerOutParameter(6,Types.INTEGER);


   cs.execute();

   FunCheckWeather1Proc   results =
	 new FunCheckWeather1Proc();
   results.setReturns(cs.getString(1));
   results.setPTempH(cs.getInt(4));
   results.setPTempL(cs.getInt(5));
   results.setPId(cs.getInt(6));
   return results;
}

}
