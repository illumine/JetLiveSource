//-----------------------------------------------
//  Generated by Jet LiveSource (TM)
//  v2.0
//  on Thu Jun 21 17:42:18 EEST 2012
//  Author  :Michael Mountrakis mountrakis@uit.gr
//  Version :1.0.0
//  Comments:
//  Example of Live Source configuration, Enjoy....
//-----------------------------------------------



package gr.uit.jetexamples;
import java.sql.*;



public class FunAddNumbersProc{

      private Double returns;

      public Double  getReturns(){
      return this.returns;
   }
      public void  setReturns( Double returns ){
      this.returns = returns;
   }
   public FunAddNumbersProc(){
   }

public static  FunAddNumbersProc   callFunAddNumbers(
	Connection connection,
	Integer a, 
	Integer b, 
	Double x, 
	Double y
	) throws SQLException{
   String sql_cmd = "";
   CallableStatement cs = null;

   sql_cmd = "{ call ? = fun_add_numbers(?,?,?,?)}";  //Callable Statement

   cs = connection.prepareCall(sql_cmd);

   cs.registerOutParameter(1,Types.DOUBLE);
   cs.setInt( 2,a);
   cs.setInt( 3,b);
   cs.setDouble( 4,x);
   cs.setDouble( 5,y);


   cs.execute();

   FunAddNumbersProc   results =
	 new FunAddNumbersProc();
   results.setReturns(cs.getDouble(1));
   return results;
}

}
