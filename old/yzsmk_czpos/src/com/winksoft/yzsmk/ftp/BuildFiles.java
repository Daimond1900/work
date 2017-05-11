package com.winksoft.yzsmk.ftp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;

import android.content.Context;
import android.util.Log;

public class BuildFiles {
	
	private static String tag = "BuildFiles";
	private static Context cxt1;
	public static BuildFiles bfinstance = null;
	
	public static BuildFiles getInstance(Context cxt) 
	{  
	    if (bfinstance == null) 
	    {  
	    	cxt1 = cxt;
	    	bfinstance = new BuildFiles();  
	    }  
	    return bfinstance;  
	}  
		
	private BuildFiles() 
	{
	}
	
	/*
	 * 生成消费文件
	 * */
	public void createXFFile()
	{
		String directoryName = BusinessMode.LocalPath_ORI;
		//从数据库取出要处理的数据(充值、消费)
		List<Map<String, Object>> maplist = SqliteUtil.getInstance(null).doQuery("xfinfos", 
				new String[] {"body_0","body_12","body_32","body_51","body_67","body_70","body_71","body_79","body_87","body_95","body_105","body_117","body_129","body_138","body_139","body_147","body_155","body_161","body_169","body_171","body_173","body_177","body_193","body_197","body_201","body_229","body_259","id","state","insertdate" }, 
				"state=?", new String[] { "0" },
				null, null, "id");
		
		//根据协议生成指定格式的文件
		if(!maplist.isEmpty())
		{
			boolean bsuccess = true; //生成消费文件状态标志位
			String lastId = maplist.get(maplist.size()-1).get("id").toString();//取出的记录中最大的Id
			//文件名
			XFFilesInfo xffiles = new XFFilesInfo(maplist);
			SimpleDateFormat tempYear = new SimpleDateFormat("yyMMddHHmmss");
			String sDay = tempYear.format(new java.util.Date()).toString();//日期 	n 	12 	年用后2位，YYMMDDhhmmss 
			String yydwcode = xffiles.getyydwcode();//运营单位代码	n 	12	商户代码
			String seq = "yf00000001";//序列号 	ans 	10 	入网机构自定义
			String fileName = "CD" + sDay + yydwcode + seq + "A"; //H-手工帐，A-自动帐

			File d = new File(directoryName);
			if (!d.exists()) 
			{  
			   try{d.mkdirs();} catch (Exception e) 
			   {bsuccess = false;
			    saveLog("生成消费文件异常1,[" + e.getMessage() + "," + e.getStackTrace() + "]","2");}
			}
			
			File file = new File(directoryName  + fileName);
		    if (!file.exists())
		    {  
		    	try {file.createNewFile(); }catch (Exception e) 
		        {bsuccess = false;
		         saveLog("生成消费文件异常2,[" + e.getMessage() + "," + e.getStackTrace() + "]","2");}  
		    }

			
			String fileHead = xffiles.getHead();
			//String fileBody = xffiles.getBody();
		    
		    //生成文件
		    try 
	        {
		       FilesUtil.writeFile(directoryName  + fileName,fileHead);
		       String bodyi = "";
		       for (int i=0;i<maplist.size();i++) 
			   {
		    	   bodyi = xffiles.getBody(i);
		    	   if(!bodyi.contains("getBody err"))
		    	   {
		    		   FilesUtil.writeFile(directoryName  + fileName,bodyi);
		    	   }
		    	   else
		    	   {
		    		   bsuccess = false;
		    		   saveLog("生成消费文件异常4,[" + bodyi + "]","2");
		    		   break;
		    	   }
			   }	
	        }
		    catch (Exception e) 
	        {
	        	bsuccess = false;
	            saveLog("生成消费文件异常3,[" + e.getMessage() + "," + e.getStackTrace() + "]","2");
	            if (file.exists())
			    { 
	            	file.delete();	
			    }
	        }  
		    
		    //如果生成成功，则更新数据库标志位
		    if(bsuccess)
		    {
		    	Map<String, String> params = new HashMap<String, String>();
				params.put("state", "1");
				int ret = BusinessMode.sqlInstance.doUpdate1("xfinfos", params, "id<=? and state='0' ", new String[] { lastId });
				saveLog("正常生成消费文件[" +fileName  +"],ret[" + ret + "],maplist[" + maplist.size() + "]","7");
		    }
		}
		else
		{
			Log.d(tag,"没有需要生成的消费记录");
		}
		
		//删除90天之前且已生成消费文件的交易记录
		Date date=new Date(new java.util.Date().getTime() - - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 10*24*60*60*1000);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss");  
        String strDt = tempDate.format(date).toString();  
		int ret = BusinessMode.sqlInstance.doDelete1("xfinfos", "insertdate<=? and state='1' ", new String[] { strDt });
		saveLog("删除90天之前且已生成消费文件的交易记录,count[" + ret + "]","A");
	}
	
	/*
	 * 生成充值文件
	 * */
	public void createCZFile()
	{
		String directoryName = BusinessMode.LocalPath_ORI;
		//从数据库取出要处理的数据(充值、消费)
		List<Map<String, Object>> maplist = SqliteUtil.getInstance(null).doQuery("czinfos", 
				new String[] {"CorpId","TxnFileName","SettDate","CitizenCardNo","CrdBalBef","CrdBalAft","TxnAmt","TxnDt","SamNo","AccpetCusNo","OprNo","TAC","BusinessNo","CurrCount","id","state","insertdate"}, 
				"state=?", new String[] { "0" },
				null, null, "id");

		//根据协议生成指定格式的文件
		if(!maplist.isEmpty())
		{
			boolean bsuccess = true; //生成消费文件状态标志位
			String lastId = maplist.get(maplist.size()-1).get("id").toString();
			//文件名
			SimpleDateFormat tempYear = new SimpleDateFormat("yyyy");
			String syear = tempYear.format(new java.util.Date()).toString();
			String code = "12345678900"; //充值受理方代码(11位)
			
			SimpleDateFormat tempDate = new SimpleDateFormat("MMddHHmmss");  
	        String seq = tempDate.format(new java.util.Date()).toString();//序列号 	ans 	10 	入网机构自定义
			String fileNameAL ="AL" + syear + code + seq ; //文件标识（2位）+充值受理方代码(11位)+日期（8位yyyyMMdd）+序列号（6位）
			String fileNameDT ="DT" + syear + code + seq ; //文件标识（2位）+充值受理方代码(11位)+日期（8位yyyyMMdd）+序列号（6位）

			File d = new File(directoryName);
			if (!d.exists()) 
			{  
			   try{d.mkdirs();} catch (Exception e) 
			   {bsuccess = false;
			    saveLog("生成充值总帐文件异常1,[" + e.getMessage() + "," + e.getStackTrace() + "]","4");}
			}	

			//1.写总帐文件(AL)
			File fileAL = new File(directoryName  + fileNameAL);
		    if (!fileAL.exists())
		    {  
		    	try {fileAL.createNewFile(); }catch (Exception e) 
		        {bsuccess = false;
		         saveLog("生成充值总帐文件异常2,[" + e.getMessage() + "," + e.getStackTrace() + "]","4");}  
		    }
			CZFilesInfo czfiles = new CZFilesInfo(maplist);
			String AL_DA = czfiles.getAL_DA();
			FilesUtil.writeFile(directoryName  + fileNameAL,AL_DA);
			String AL_RA = czfiles.getAL_RA();
			FilesUtil.writeFile(directoryName  + fileNameAL,AL_RA);

            //2.写充值明细文件(DT)
			File fileDT = new File(directoryName  + fileNameDT);
		    if (!fileDT.exists())
		    {  
		    	try {fileDT.createNewFile(); }catch (Exception e) 
		        {bsuccess = false;
		         saveLog("生成充值明细文件异常2,[" + e.getMessage() + "," + e.getStackTrace() + "]","4");}  
		    }
			String DT_DA = czfiles.getDT_DA();
			FilesUtil.writeFile(directoryName  + fileNameDT,DT_DA);
			
		    //写充值明细文件(DA)
		    try 
	        {
		       String DT_RA = "";
		       for (int i=0;i<maplist.size();i++) 
			   {
		    	   DT_RA = czfiles.getDT_RA(i,fileNameAL);
		    	   if(!DT_RA.contains("getDT_RA err"))
		    	   {
		    		   FilesUtil.writeFile(directoryName  + fileNameDT,DT_RA);
		    	   }
		    	   else
		    	   {
		    		   bsuccess = false;
		    		   saveLog("生成充值文件异常4,[" + DT_RA + "]","4");
		    		   break;
		    	   }
			   }	
	        }
		    catch (Exception e) 
	        {
	        	bsuccess = false;
	            saveLog("生成充值文件异常4,[" + e.getMessage() + "," + e.getStackTrace() + "]","4");
	            if (fileDT.exists())
			    { 
	            	fileDT.delete();	
			    }
	            if (fileAL.exists())
			    { 
	            	fileAL.delete();	
			    }
	        }  
		    
		    //如果生成成功，则更新数据库标志位
		    if(bsuccess)
		    {
		    	Map<String, String> params = new HashMap<String, String>();
				params.put("state", "1");
				int ret = BusinessMode.sqlInstance.doUpdate1("czinfos", params, "id<=? and state='0' ", new String[] { lastId });
				saveLog("正常生成充值文件[" +fileNameAL +"," + fileNameDT  +"],ret[" + ret + "],maplist[" + maplist.size() + "]","8");
		    }
		}
		else
		{
			Log.d(tag,"没有需要生成的充值记录");
		}
		
		//删除90天之前且已生成消费文件的交易记录
		Date date=new Date(new java.util.Date().getTime() - - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 10*24*60*60*1000);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss");  
        String strDt = tempDate.format(date).toString();  
		int ret = BusinessMode.sqlInstance.doDelete1("czinfos", "insertdate<=? and state='1' ", new String[] { strDt });
		saveLog("删除90天之前且已生成充值文件的交易记录,count[" + ret + "]","B");
	}
	        
    /*
	 * 保存日志数据
	 * */
	public void saveLog(String content,String type)
	{
		Log.d(tag,content + ",type=" + type);
		try
		{
			if(content.length()>200)
			{
				content = content.substring(0,199);
			}
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss.SSS");  
	        String strDt = tempDate.format(new java.util.Date()).toString();  
						
			Map<String, String> map = new HashMap<String, String>(); 
			map.put("content", content);
			map.put("type", type);
			map.put("insertdate", strDt);
			BusinessMode.sqlInstance.insert("logs",map);
		}
		catch(Exception e)
		{
			Log.d(tag,"saveLog err:" + e.getMessage() + "," + e.getStackTrace());
		}
	}
    
    
}

