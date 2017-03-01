package com.winksoft.yzsmk.desfire;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.winksoft.yzsmk.ftp.CommonUtil;

public class DisfireSqlite {
	
	private static String tag = "DisfireSqlite";
	private List<Map<String, Object>> mapList1;
	public DisfireSqlite(List<Map<String, Object>> mapList) 
	{
		mapList1 = mapList;
	}
	 
	public String getString(int index)
	{
		String ret = "";
		if(mapList1!=null)
		{
			try
			{
		        Map map = mapList1.get(index);
		        String crade_num = padLeft(map.get("crade_num").toString(),8);
				String city_code = padLeft(map.get("city_code").toString(),2);				
				String passenger_num = padLeft(map.get("passenger_num").toString(),8);				
				String care_class = padLeft(map.get("care_class").toString(),2);					
				String care_type = padLeft(map.get("care_type").toString(),2);					
				String crade_class = padRight(map.get("crade_class").toString(),2);					
				String crade_date = padLeft(map.get("crade_date").toString(),14);				
				String consume_money =  padLeft(map.get("consume_money").toString(),6);					
				String consume_after_balance = padLeft(map.get("consume_after_balance").toString(),6); 				
				String authorization_num = padRight(map.get("authorization_num").toString(),14); 				
				String recharge_money = padLeft(map.get("recharge_money").toString(),6);				
				String recharge_date = padLeft(map.get("recharge_date").toString(),14);	
				String crade_auth = padLeft(map.get("crade_auth").toString(),8);
				String care_num = padLeft(map.get("care_num").toString(),16);
				String consume_crade_val = padLeft(map.get("consume_crade_val").toString(),6);		
				//String RtnSign = "";//	S1	回车符	’\n’
				ret = crade_num + " " + city_code + " " + passenger_num + " "
						+ care_class + " " + care_type + " " + crade_class
						+ " " + crade_date + " " + consume_money + " "
						+ consume_after_balance + " "+ authorization_num + " "+ recharge_money + " "+ recharge_date + " "
						+ crade_auth + " " + care_num + " " + consume_crade_val;
			}
			catch(Exception e)
			{
				Log.d(tag,"getDT_RA err:" +  e.getMessage() + "," + e.getStackTrace());
				ret = "getDT_RA err:" +  e.getMessage() + "," + e.getStackTrace();
			}
		}
		return ret;
	}
	
	public int getAmount()
	{
	   int amount = 0 ;
	   for(int i=0;i<mapList1.size();i++)
	   {
		   amount = amount + Integer.parseInt(mapList1.get(i).get("TxnAmt").toString());
	   }
	   return amount;
	}
	
	public String padRight(String src, int len)
	{
		return CommonUtil.padRight(src, len);
	}
	
	public String padLeft(String src, int len)
	{
		return CommonUtil.padLeft(src, len);
	}
}
