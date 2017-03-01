package com.winksoft.yzsmk.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class FilesUtil {
	
	private static String tag = "FilesUtil";
		
	/*
	 * 批量创建文件用于上传测试
	 * */
	public static void createTestFile()
	{
	   	String directoryName = BusinessMode.LocalPath_ORI;
	    File d = new File(directoryName);
	    if (!d.exists()) 
	    {  
	       try
	       {  
	         //按照指定的路径创建文件夹  
	         d.mkdirs();  
	       } 
	       catch (Exception e) 
	       {  
	    	   Log.d(tag,"createTestFile err1:" +  e.getMessage() + "," + e.getStackTrace());
	       }
	    }
	    else
	    {
	    	File[] files = d.listFiles();  
	    	if (files != null)  
	    	{
	    		for (int i = 0; i < files.length; i++) 
	    		{  
	    			    if (!files[i].isDirectory()) 
	    			    {  
	    			    	files[i].delete();
	    			    }
	    		}
	    	}
	    }
	    
	    String fName = "";
	    for(int i=0;i<2;i++)
	    { 
	      SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd" + "_"  + "HHmmss");  
	      fName = tempDate.format(new java.util.Date()).toString() + "_" + i + ".txt";
	      File file = new File(directoryName  + fName);
	      if (!file.exists())
	      {  
	        try 
	        {  
	           //在指定的文件夹中创建文件  
	           file.createNewFile();  
	           writeFile(directoryName  + fName,directoryName  + fName);
	        } 
	        catch (Exception e) 
	        {  
	        	Log.d(tag,"createTestFile err2:" +  e.getMessage() + "," + e.getStackTrace());
	        }  
	      }
	    }
	}
	
	//向已创建的文件中写入数据  
	public static void writeFile(String fileName,String str) 
	{  
	    FileWriter fw = null;  
	    BufferedWriter bw = null;  
	    try 
	    {  
	    	//String datetime = "";  
	        // SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  + "HH:mm:ss");  
	        // datetime = tempDate.format(new java.util.Date()).toString();  
	           
	    	fw = new FileWriter(fileName, true);//  
	        // 创建FileWriter对象，用来写入字符流  
	        bw = new BufferedWriter(fw); // 将缓冲对文件的输出  
	        String myreadline = str;  
	             
	        bw.write(myreadline ); // 写入文件 //+ "\n" 
	        bw.newLine();  
	        bw.flush(); // 刷新该流的缓冲  
	        bw.close();  
	        fw.close();  
        } 
	    catch (Exception  e) 
	    {  
	    	Log.d(tag,"writeFile err1:" +  e.getMessage() + "," + e.getStackTrace());
	        try 
	        {  
	          bw.close();  
	          fw.close();  
	        } 
	        catch (IOException e1) 
	        {  
	        	Log.d(tag,"writeFile err2:" +  e.getMessage() + "," + e.getStackTrace());
	        }  
	    }  
	}
	
    /*
     * 删除过期的文件
     * 
     * */
    public static void deleteOldFiles(String path)
    {
    	File dir = new File(path);  
  	    File[] files = dir.listFiles();  
  	    int iFileCount = 0;
  	    if (files != null)  
  	    {
  		  for (int i = 0; i < files.length; i++) 
  		  {  
  		    if (!files[i].isDirectory()) 
  		    {  
  		      if(isTooOld(files[i]))
  		      {
  		    	files[i].delete();  
  		    	iFileCount++;
  		      }
  		    }
  		  }
  	    }
  	    Log.d(tag,"deleteOldFiles,path[" + path + "],count[" + iFileCount +"]");
    }
    
    /*
	 *判断文件日期是否过久(上传失败的情况下会执行) 
	 * */
	public static boolean isTooOld(File file)
	{
		 boolean bret = false;
		 String fileName = file.getName();
	     if(fileName.lastIndexOf("/")>0)
	     {
	    	  fileName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());  
	     }
	     
	     Date date=new Date(new java.util.Date().getTime() - - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 20*24*60*60*1000 - 10*24*60*60*1000);
		 SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");  
	     String curDt = tempDate.format(date).toString();  
	     
	     String fileDt = curDt;
	     if((fileName.indexOf("AL")==0)||(fileName.indexOf("DT")==0)) //AL1234567890020160929001122
	     {
	    	 fileDt = fileName.substring(13,13+8); 
	     }
	     else if(fileName.indexOf("CD")==0) //CD16093012131400112233445566AABBCCDDEEA
	     {
	    	 fileDt = "20" + fileName.substring(2,2+6); 
	     }
	     
	     if(Integer.parseInt(fileDt)<Integer.parseInt(curDt))
	     {
	    	 bret = true; 
	     }
		 return bret;
	}
	
	/**  
     * 移动单个文件  
     * @param oldPath String 原文件路径    
     * @param newPath String 复制后路径  
     * @return boolean  
     */  
    public static boolean MoveFileToDesc(File oriFile, String newPath) 
    {   
       boolean bret =  true;
       try 
       {  
    	  File d = new File(newPath);
   	      if (!d.exists()) 
   	      {  
   	    	d.mkdirs();  
   	      }
   	    
	      int bytesum = 0;   
	      int byteread = 0;  
	      
	      String fileName = oriFile.getName();
	      if(fileName.lastIndexOf("/")>0)
	      {
	    	  fileName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());  
	      }
	      String newPathFull = newPath + fileName;
	      if (oriFile.exists()) 
	      { 
	    	  //文件不存在时   
	          InputStream inStream = new FileInputStream(oriFile); //读入原文件  
	          
	          File tempFile1 = new File(newPathFull);
		      if (!tempFile1.exists())
		      {  
		        try 
		        {  
		           tempFile1.createNewFile();  
		        }
		        catch (Exception e) 
		        {}
		      }
		           
	          FileOutputStream fs = new FileOutputStream(newPathFull);   
	          byte[] buffer = new byte[1444];   
	          int length;   
	          while ( (byteread = inStream.read(buffer)) != -1) 
	          {   
	             bytesum += byteread; //字节数 文件大小   
	             fs.write(buffer, 0, byteread);   
	          }   
	          inStream.close();    
	          fs.close();
	          
	          //最后如果确认目标文件夹中已有相应文件,则删除原文件
	          File tempFile = new File(newPathFull);
	          if(tempFile.exists())
	          {
	             oriFile.delete();
	          }
	      }   
	   }   
	   catch (Exception e) 
       {   
		   bret = false;
		   Log.d(tag,"MoveFileToDesc[" + oriFile.getName() +"],newPath[" + newPath + "],err:" +  e.getMessage() + "," +  e.getStackTrace());
	   }
       return bret;
    }   
    
    public static int hasUploadFiles(String path)
	{
		  int ret = 0;
		  String fileName = "";
		  File dir = new File(path);  
		  File[] files = dir.listFiles();  
		  if (files != null)  
		  {
			 for (int i = 0; i < files.length; i++) 
			 {  
			    if (!files[i].isDirectory()) 
			    {  
			    	fileName = files[i].getName();
			    	//AL1234567890020160929001122 CD16093012131400112233445566AABBCCDDEEA
			    	if((fileName.indexOf("AL")==0)||(fileName.indexOf("DT")==0)||(fileName.indexOf("CD")==0)) 
				    {
			    		ret ++;
			    		break;
				    }
			    }
			 }
		  }
		  return ret;
	}
}

