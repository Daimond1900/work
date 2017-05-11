package com.winksoft.yzsmk.ftp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.xfpos.queryvalue.QueryValues;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BusinessMode {
	private String tag = "BusinessMode";
	/**
	 * 本地路径.
	 */
	public static String LocalPath_ORI =  Environment.getExternalStorageDirectory() + "/XF/filePath_ORI/"; //消费业务为:/XF/,充值业务为:/CZ/
	public static String LocalPath_SUCCESS =  Environment.getExternalStorageDirectory() + "/XF/filePath_SUCCESS/";
	public static String LocalPath_FAIL =  Environment.getExternalStorageDirectory() + "/XF/filePath_FAIL/";
	public FTP ftp;
	public static BusinessMode bminstance = null;
	public static SqliteUtil sqlInstance = null;
	
	private static BuildFiles bfInstance = null;
	public boolean bRunning = false;
	/**
	 * 运行模式.
	 */
	private boolean Fmode = true; 
	/**
	 * 运行时间.
	 */
	private String Fruntime = "01:00";
	private Handler parentHandler;
	private Context cxt1;
	/*
	 * 定时任务
	 * */
	private int iRunHour = 0;
	private int iRunMin = 0;
	
	//生成文件及上传任务
	private Timer timer;  
	private TimerTask task;  
	private Handler handler = null;
	
	//定期删除过期文件
	private Timer timer1;  
	private TimerTask task1;  
	private Handler handler1 = null;
	
	
	private  BusinessMode()
	{
	}
	
	public static BusinessMode getInstance() 
	{  
	    if (bminstance == null) 
	    {  
	       bminstance = new BusinessMode();  
	    }  
	    return bminstance;  
	}  
		 
	public boolean setParams(String host,int port,String userName,String password,String runtime ,String remotePath,Handler handler,Context cxt)
	{
		boolean bret = true;
		if( (host==null) || host.equals("")||(userName==null) || userName.equals("")||(password==null) || password.equals(""))
		{
			bret = false;
		}
		if(handler!=null)
		{
			parentHandler = handler;
		}
		if(cxt!=null)
		{
			cxt1 = cxt;
			if(sqlInstance==null)
			{
				sqlInstance = SqliteUtil.getInstance(cxt);
			}
			if(bfInstance==null)
			{
				bfInstance = BuildFiles.getInstance(cxt);
			}
		}	
		
		if(ftp==null)
		{
			ftp = FTP.get(host, port, userName, password,remotePath,handler);
		}
		
		if(ftp!=null)
		{
			ftp.setFhostName(host);
			ftp.setFserverPort(port);
			ftp.setFuserName(userName);
			ftp.setFpassword(password);
			ftp.setFremotePath(remotePath);
			setRunTime(runtime);
		}
		Fmode = true;
		return bret;
	}
	
	public boolean start()
	{
		boolean bret = true;
		if(handler==null)
		{
			  handler = new Handler(cxt1.getMainLooper()) {  
			        @Override  
			        public void handleMessage(Message msg) {  
			         // 要做的事情 
			        //buildFile();
			        new Thread(new Runnable() {			
						@Override
						public void run() {	
							buildFile();
						}
					}).start();
			        super.handleMessage(msg);  
			        }  
			  };  
		}
			
		if(handler1==null)
		{
			  handler1 = new Handler(cxt1.getMainLooper()) {  
			        @Override  
			        public void handleMessage(Message msg) {  
			         // 要做的事情 清除过期文件 LocalPath_SUCCESS 
			        FilesUtil.deleteOldFiles(LocalPath_SUCCESS);
			        FilesUtil.deleteOldFiles(LocalPath_FAIL);
			        super.handleMessage(msg);  
			        }  
			  };  
		}
		
		dostart();
		sendMsg("start");
		return bret;
	}
	
	public void stop()
	{
		dostop();
		sendMsg("stop");
	}
	
	public void dostart()
	{
		if(task==null)
		{
		  task = new TimerTask() {  
		 	 @Override  
			 public void run() {  
				// TODO Auto-generated method stub  
				Message message = new Message();  
				message.what = 1;  
				handler.sendMessage(message);  
			 } 
		   }; 
		}
		
		if(task1==null)
		{
		   task1 = new TimerTask() {  
			 @Override  
			 public void run() {  
				// TODO Auto-generated method stub  
				Message message = new Message();  
				message.what = 1;  
				handler1.sendMessage(message);  
			 } 
		   }; 
		}
		 
		Fmode = true;
		timer = new Timer();
		timer.schedule(task, 2000, 1000*30);   
		timer1 = new Timer();
		timer1.schedule(task1, 2000, 1000*60*60);   
		bRunning = true;
	}
	
	public void dostop()
	{
		if(task!=null)
		{
			task.cancel();
			task = null;
			timer.cancel(); 
			timer = null;
		}
		if(task1!=null)
		{
			task1.cancel();
			task1 = null;
			timer1.cancel(); 
			timer1 = null;
		}
	}
	
	public boolean setMode(boolean b)
	{
		boolean ret = true;
		if(!bRunning)
		{
			return false;
		}
		if(b!=Fmode)
		{	
			if(!b)
			{
				dostop();
			}
			else
			{
				dostart();
			}
			Fmode = b;
		}
		return ret;
	}
	
	public boolean getMode(boolean b)
	{
		return Fmode ;
	}
	
	public void createTestFiles()
	{
		sendMsg("createTestFiles start");
		if(ftp!=null)
		{
			FilesUtil.createTestFile();
		}
		sendMsg("createTestFiles end");
	}
	
	public void uploadFiles()
	{
		if(ftp!=null)
		{
			ftp.uploadFiles(LocalPath_ORI);
		}
	}
	
	public boolean getState()
	{
		return bRunning;
	}
		
	public void setRunTime(String runtime)
	{
		if(runtime!=null&&(!runtime.equals("")))
			Fruntime =runtime;
		
		try
		{
		   iRunHour = Integer.parseInt(Fruntime.split(":")[0]);
		   iRunMin = Integer.parseInt(Fruntime.split(":")[1]);
		}
		catch(Exception e)
		{
			Log.d(tag,"setRunTime err:" + Fruntime + "," + e.getMessage() + "," + e.getStackTrace());
		}
	}
	
	/*
	 * 手工上传,必须切换成手动模式才能执行
	 * 
	 * */
	public boolean manualOp()
	{
		boolean ret = true;
		if(Fmode)
		{
			return false;
		}
		//1.生成文件-测试时使用
		//FilesUtil.createTestFile();
		//1.生成消费文件-正式运行时采用
		bfInstance.createXFFile();
		//1.生成充值文件-正式运行时采用
		//bfInstance.createCZFile();
		
		//2.上传(上传成功后会自动将文件移到上传成功目录LocalPath_SUCCESS,过长时间上传失败的会移到上传失败目录LocalPath_FAIL)
		if(FilesUtil.hasUploadFiles(LocalPath_ORI)>0)
		{
		  ret = ftp.uploadFiles(LocalPath_ORI);
		  if(ret)
		  {
			saveLog("文件手动上传成功","9"); //9：正常FTP上传消费数据，0：正常FTP上传充值数据
		  }
		  else
		  {
			saveLog("文件手动上传失败","5"); //5：FTP上传消费数据异常，6：FTP上传充值数据异常
		  }
		}
		else
		{
			saveLog("没有需要手动上传的文件。","D");  
		}
		return ret;
	}
	
	public void buildFile()
	{
		SimpleDateFormat tempHour = new SimpleDateFormat("HH"); 
		SimpleDateFormat tempMin = new SimpleDateFormat("mm");
		SimpleDateFormat tempss = new SimpleDateFormat("ss");
		int icurHour = Integer.parseInt(tempHour.format(new java.util.Date()).toString());
		int icurMin = Integer.parseInt(tempMin.format(new java.util.Date()).toString());
		int icurSS = Integer.parseInt(tempss.format(new java.util.Date()).toString());
		
		//sendMsg("buildFile cur[" + icurHour + ":" + icurMin + ":" + icurSS + "],run[" + iRunHour + ":" + iRunMin +"]");
		boolean exec = false;
		if( (!exec) &&((icurHour==iRunHour)&&(icurMin>=iRunMin)&&(icurMin<iRunMin + 30)) ||
				((icurHour==iRunHour+1)&&(icurMin+iRunHour<=30)) ||
				(icurHour==0)&&(iRunHour==23)&&(icurMin+iRunHour<=30)
		  )
		{
			exec = true;
			//1.生成文件-测试时使用
			//FilesUtil.createTestFile();
			//1.生成消费文件-正式运行时采用
			bfInstance.createXFFile();
			//1.生成充值文件-正式运行时采用
			//bfInstance.createCZFile();
			
			//2.上传(上传成功后会自动将文件移到上传成功目录LocalPath_SUCCESS,过长时间上传失败的会移到上传失败目录LocalPath_FAIL)
			if(FilesUtil.hasUploadFiles(LocalPath_ORI)>0)
			{
			  boolean ret = ftp.uploadFiles(LocalPath_ORI);
			  if(ret)
			  {
				saveLog("文件上传成功","9"); //9：正常FTP上传消费数据，0：正常FTP上传充值数据
			  }
			  else
			  {
				saveLog("文件上传失败","5"); //5：FTP上传消费数据异常，6：FTP上传充值数据异常
			  }
			}
			else
			{
				saveLog("没有需要上传的文件。","D");  
			}
		}
		sendMsg("Finished buildFile[" + exec + "], cur[" + icurHour + ":" + icurMin + ":" + icurSS + "],run[" + iRunHour + ":" + iRunMin +"]");
	}
	
	/*
	 * 向调用类发送消息以反馈程序执行情况
	 * */
	private void sendMsg(String msg)
	{
		saveLog(msg, "C");
		if(parentHandler!=null)
		{
		   Message message = new Message();  
		   message.what = 1;  
		   Bundle bundle = new Bundle();
		   bundle.putString("MSG",msg);   
		   message.setData(bundle); 
		   parentHandler.sendMessage(message);  
		}
	}
	
	/*
	 * 保存消费数据到数据库
	 * */
	public long saveToxfDb(Map<String, String> map,Context context)
	{
		long ret = -1;
		try
		{
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss.SSS");  
	        String strDt = tempDate.format(new java.util.Date()).toString();  
    		map.put("state", "0");
			map.put("insertdate", strDt);
			if((sqlInstance==null)&&(context!=null))
			{
				sqlInstance = SqliteUtil.getInstance(context);
			}
			map.put("jobNum",sqlInstance.doQueryJobNumber());
			ret = sqlInstance.insert("xfinfos",map);
		}
		catch(Exception e)
		{
			Log.d(tag,"saveToxfDb err:" + e.getMessage() + "," + e.getStackTrace());
			saveLog("saveToxfDb err:" + e.getMessage() + "," + e.getStackTrace(),"1");
		}
		return ret;
	}
	
	/*
	 * 保存充值数据到数据库
	 * */
	public long saveToczDb(Map<String, String> map,Context context)
	{
		long ret = -1;
		try
		{
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss.SSS");  
	        String strDt = tempDate.format(new java.util.Date()).toString();  
    		map.put("state", "0");
			map.put("insertdate", strDt);
			if((sqlInstance==null)&&(context!=null))
			{
				sqlInstance = SqliteUtil.getInstance(context);
			}
			ret = sqlInstance.insert("czinfos",map);
		}
		catch(Exception e)
		{
			Log.d(tag,"saveToczDb err:" + e.getMessage() + "," + e.getStackTrace());
			saveLog("saveToczDb err:" + e.getMessage() + "," + e.getStackTrace(),"3");
		}
		return ret;
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
			sqlInstance.insert("logs",map);
		}
		catch(Exception e)
		{
			Log.d(tag,"saveLog err:" + e.getMessage() + "," + e.getStackTrace());
		}
	}
	
	/*
	 * 获取消费数据
	 * */
	public List<Map<String, Object>> getXFinfos(String selection,String [] selectArray)
	{
		List<Map<String, Object>> list = sqlInstance.doQuery("xfinfos", 
				new String[] {"body_0","body_12","body_32","body_51","body_67","body_70","body_71","body_79","body_87","body_95","body_105","body_117","body_129","body_138","body_139","body_147","body_155","body_161","body_169","body_171","body_173","body_177","body_193","body_197","body_201","body_229","body_259","id","state","insertdate" }, 
				selection, selectArray,
				null, null, "id");
		return list;
	}
	
	/*
	 * 获取充值数据
	 * */
	public List<Map<String, Object>> getCZinfos(String selection,String [] selectArray)
	{
		List<Map<String, Object>> list = SqliteUtil.getInstance(null).doQuery("czinfos", 
				new String[] {"CorpId","TxnFileName","SettDate","CitizenCardNo","CrdBalBef","CrdBalAft","TxnAmt","TxnDt","SamNo","AccpetCusNo","OprNo","TAC","BusinessNo","CurrCount","id","state","insertdate"}, 
				selection, selectArray,
				null, null, "id");
		return list;
	}
    
	/*
	 * 获取程序运行日志
	 * */
    public List<Map<String, Object>> getLogs(int start,int end)
    {
    	//List<Map<String, Object>> list = sqlInstance.doQuery("logs", 
		//		new String[] {"content","type","insertdate","id"}, 
		//		"ROWID<=500", null,//" ROWID<200" new String[] { "200" }
		//		null, null, "insertdate desc"); 
    	if(end==0)
    	{
    		start = 0;
    		end = 200;
    	}
    	String strSql = "select content,type,insertdate,id from logs  order by insertdate desc limit " + start + "," + end ;
    	List<Map<String, Object>> list = sqlInstance.doQuery(strSql,new String[] {"content","type","insertdate","id"}); 

    	return list;
    }
    
    /*
     * 批量插入数据库用于生成测试
     * */
    public long insertLogs_Test(Context cxt)
    {
    	Map map = new HashMap<String, Object>();
		map.put("content","content-1");
		map.put("type","1");
		map.put("insertdate","2016-09-27 11:31:24");	
		
		Map map1 = new HashMap<String, Object>();
		map1.put("content","content-2");
		map1.put("type","2");
		map1.put("insertdate","2016-09-26 11:31:24");	
		
		Map map2 = new HashMap<String, Object>();
		map2.put("content","content-5");
		map2.put("type","5");
		map2.put("insertdate","2016-09-28 10:21:24");	
		
		Map map3 = new HashMap<String, Object>();
		map3.put("content","content-B");
		map3.put("type","B");
		map3.put("insertdate","2016-09-29 05:32:12");	
		
		Map map4 = new HashMap<String, Object>();
		map4.put("content","content-A");
		map4.put("type","A");
		map4.put("insertdate","2016-09-17 08:27:24");	
		
		Map map5 = new HashMap<String, Object>();
		map5.put("content","content-A");
		map5.put("type","A");
		map5.put("insertdate","2016-09-26 08:31:34");	
		
		Map map6 = new HashMap<String, Object>();
		map6.put("content","content-9");
		map6.put("type","9");
		map6.put("insertdate","2016-09-27 21:41:56");	
		
		Map map7 = new HashMap<String, Object>();
		map7.put("content","content-8");
		map7.put("type","8");
		map7.put("insertdate","2016-09-29 17:32:24");	
		
		Map map8 = new HashMap<String, Object>();
		map8.put("content","content-6");
		map8.put("type","6");
		map8.put("insertdate","2016-09-27 11:31:24");	
		
		Map map9 = new HashMap<String, Object>();
		map9.put("content","content-6");
		map9.put("type","6");
		map9.put("insertdate","2016-09-23 11:33:45");	
		
		Map map10 = new HashMap<String, Object>();
		map10.put("content","content-5");
		map10.put("type","5");
		map10.put("insertdate","2016-09-27 11:31:24");	
		
		Map map11 = new HashMap<String, Object>();
		map11.put("content","content-3");
		map11.put("type","3");
		map11.put("insertdate","2016-09-27 11:31:24");	

    	long ret = -1;
		try
		{
			sqlInstance.insert("logs",map);
			sqlInstance.insert("logs",map1);
			sqlInstance.insert("logs",map2);
			sqlInstance.insert("logs",map3);
			sqlInstance.insert("logs",map4);
			sqlInstance.insert("logs",map5);
			sqlInstance.insert("logs",map6);
			sqlInstance.insert("logs",map7);
			sqlInstance.insert("logs",map8);
			sqlInstance.insert("logs",map9);
			sqlInstance.insert("logs",map10);
			sqlInstance.insert("logs",map11);
		}
		catch(Exception e)
		{
			Log.d(tag,"insertLogs err:" + e.getMessage() + "," + e.getStackTrace());
			saveLog("insertLogs err:" + e.getMessage() + "," + e.getStackTrace(),"3");
		}
		return ret;
    }
    
    
    /*
	 * 保存消费数据到数据库
	 * */
	public long saveToxfDb_desfire(Map<String, String> map,Context context)
	{
		long ret = -1;
		try
		{
//			QueryValues qv = new QueryValues();
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss.SSS");  
	        String strDt = tempDate.format(new java.util.Date()).toString();  
    		map.put("state", "0");
			map.put("insertdate", strDt);
			//工号
			if((sqlInstance==null)&&(context!=null))
			{
				sqlInstance = SqliteUtil.getInstance(context);
			}
			map.put("jobNum",sqlInstance.doQueryJobNumber());
			ret = sqlInstance.insert("disfire",map);
		}
		catch(Exception e)
		{
			Log.d(tag,"saveToxfDb err:" + e.getMessage() + "," + e.getStackTrace());
			saveLog("saveToxfDb err:" + e.getMessage() + "," + e.getStackTrace(),"1");
		}
		return ret;
	}
	
	
	/*
	 * 获取desfire消费数据
	 * */
	public List<Map<String, Object>> getXFinfos_desfire(String selection,String [] selectArray)
	{
		List<Map<String, Object>> list = sqlInstance.doQuery("desfire", 
				new String[] {"crade_num","city_code","passenger_num","care_type","crade_class","crade_date","consume_money",	"consume_after_balance","authorization_num","recharge_money","recharge_date","crade_auth","care_num","consume_crade_val","state","insertdate" }, 
				selection, selectArray,
				null, null, "id");
		return list;
	}
	/////////////////////////////////////////////////////////////////////////
}