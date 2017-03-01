package com.winksoft.yzsmk.ftp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class CZFilesInfo {
	
	private static String tag = "CZFilesInfo";
	private List<Map<String, Object>> mapList1;
	public CZFilesInfo(List<Map<String, Object>> mapList) 
	{
		mapList1 = mapList;
	}
	 
	//1.总帐文件(AL)
    //1.1.File Description Area
	public String getAL_DA()
	{
	    String ret = "";	
		String Version = "00";//	N2	版本号	00
		String FileType	= "1000";//N4	文件类型	1000
		String RecNum = padLeft(mapList1.size()+"",8);//	N8	记录总数	
		String RecLength = padLeft(205+"",8);//N8	记录长度	
		//String RtnSign = "";//	回车符	’\n’
		ret = Version + FileType + RecNum + RecLength ;
	    return ret;
	}
	
	//1.总帐文件(AL)
	//1.2.File Record Area（N）
	public String getAL_RA()
	{
		String ret = "";	
	    String CorpId = "";//	N11	充值受理方代码	
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");  
        String SettDate = tempDate.format(new java.util.Date()).toString();  //	YYYYMMDD	结算日期	
		String FhCnfmNum = padLeft(mapList1.size()+"",6);//	N6	合计充值笔数	
		String FhcnfmAmt = padLeft(getAmount()+"",14);//	N14	合计充值金额(单位：分)	
		//String RtnSign = "";//	S1	回车符	’\n’
		ret = CorpId + SettDate + FhCnfmNum + FhcnfmAmt ;
	    return ret;
	}
	
	public String getDT_DA()
	{
		String ret = "";	
		String Version = "00";	//N2	版本号	00
		String FileType	= "1002"; //N4	文件类型	1002
		String RecNum = padLeft(mapList1.size()+"",8);	//N8	记录总数	
		String RecLength = padLeft(205+"",8);//N8	记录长度
		//String RtnSign = "";//	S1	回车符	’\n’
		ret = Version + FileType + RecNum + RecLength ;
	    return ret;
	}
	
	public String getDT_RA(int index,String fileNameAL)
	{
		String ret = "";
		if(mapList1!=null)
		{
			try
			{
		        Map map = mapList1.get(index);
		        String CorpId = padLeft(map.get("CorpId").toString(),11);//	N11	充值受理方代码	
				String TxnFileName = padRight(fileNameAL,27);//ANS27	总帐文件名称	
				SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");  
		        String SettDate = tempDate.format(new java.util.Date()).toString();  //	YYYYMMDD	结算日期	
				String CitizenCardNo = padLeft(map.get("CitizenCardNo").toString(),20);//	N20	市民卡号	
				String CrdBalBef = padLeft(map.get("CrdBalBef").toString(),14);//	N14	充值前金额	
				String CrdBalAft = padLeft(map.get("CrdBalAft").toString(),14);//	N14	充值后金额	
				String TxnAmt = padLeft(map.get("TxnAmt").toString(),14);//	N14	充值金额	
				String TxnDt = padRight(map.get("TxnDt").toString(),14);//	yyyyMMddHHmmss	充值时间	
				String SamNo = padLeft(map.get("SamNo").toString(),12);//	N12	SAM卡号	
				String AccpetCusNo =  padLeft(map.get("AccpetCusNo").toString(),20);//	N20	网点编号	
				String OprNo = padLeft(map.get("OprNo").toString(),24); //N24	操作员编号	
				String TAC = padRight(map.get("TAC").toString(),8); //	H8	交易认证码	ASCII码的十六进制数
				String BusinessNo = padLeft(map.get("BusinessNo").toString(),10);//	N10	业务流水号	
				String CurrCount = padLeft(map.get("CurrCount").toString(),8);//	N8	卡计数器					
				//String RtnSign = "";//	S1	回车符	’\n’
		        ret = CorpId + TxnFileName + SettDate + CitizenCardNo + CrdBalBef + CrdBalAft + TxnAmt + TxnDt + SamNo + AccpetCusNo + OprNo +  TAC +   BusinessNo +  CurrCount  ;
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

