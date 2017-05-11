package com.winksoft.yzsmk.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FTP {
	/**
	 * 服务器名.
	 */
	private String FhostName;

	/**
	 * 端口号
	 */
	private int FserverPort;

	/**
	 * 用户名.
	 */
	private String FuserName;

	/**
	 * 密码.
	 */
	private String Fpassword;

	/**
	 * FTP连接.
	 */
	private FTPClient ftpClient;
	/**
	 * 静态对象.
	 */
	private static FTP staticFtp; 
	
	/**
	 * 远端路径.
	 */
	private String FremotePath = "/XF"; //消费业务为:/XF/,充值业务为:/CZ/
	
	private String tag = "FTP";
	private static Handler msgHandler;
	public static FTP get()
	{
		return staticFtp;
	}

	public static FTP get(String host,int port,String userName,String password ,String remotePath,Handler handler) {

		if( (host==null) || host.equals("")||(userName==null) || userName.equals("")||(password==null) || password.equals(""))
		{
			return null;
		}
		if(staticFtp==null)
		{
		   staticFtp = new FTP(host,port,userName,password);
		}
		if(handler!=null)
		{
			msgHandler = handler;
		}
		return staticFtp;
	}

	private void sendMsg(String msg)
	{
		if(msgHandler!=null)
		{
		   Message message = new Message();  
		   message.what = 1;  
		   Bundle bundle = new Bundle();
		   bundle.putString("MSG",msg);   
		   message.setData(bundle); 
		   msgHandler.sendMessage(message);  
		}
		else
		{
			Log.d(tag,"sendMsg msgHandler is null:" +  msg);
		}
	}
	
	private FTP(String host,int port,String userName,String password) {
		if(ftpClient!=null)
		{
			try
			{
				ftpClient.disconnect();
				ftpClient = null;
			}
			catch(Exception e)
			{
				
			}
		}
		try
		{
		  this.FhostName = host;
		  this.FserverPort = port;
		  this.FuserName = userName;
		  this.Fpassword = password;

		  this.ftpClient = new org.apache.commons.net.ftp.FTPClient();
		}
		catch(Exception e)
		{
		   Log.d(tag,"FTPClient create err:"+ e.getMessage() + "," + e.getStackTrace());	
		}
	}

	// -------------------------------------------------------文件上传方法------------------------------------------------

	/**
	 * 上传单个文件.
	 * 
	 * @param localFile
	 *            本地文件
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void uploadSingleFile(File singleFile, String remotePath,
			UploadProgressListener listener) throws IOException {

		// 上传之前初始化
		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;
		flag = uploadingSingle(singleFile, listener);
		if (flag) {
			listener.onUploadProgress(MainActivity.FTP_UPLOAD_SUCCESS, 0,
					singleFile);
		} else {
			listener.onUploadProgress(MainActivity.FTP_UPLOAD_FAIL, 0,
					singleFile);
		}

		// 上传完成之后关闭连接
		this.uploadAfterOperate(listener);
	}
	
	/*
	 * 上传指定目录的文件(上传成功后会自动将文件移到上传成功目录LocalPath_SUCCESS,过长时间上传失败的会移到上传失败目录LocalPath_FAIL)
	 * */	
	public boolean uploadFiles(String path)
	{
	  boolean ret = false;
	  sendMsg("uploadFiles start");
	  LinkedList<File> fileList = new LinkedList();
	  File dir = new File(path);  
	  File[] files = dir.listFiles();  
	  int iFileCount = 0;
	  if (files != null)  
	  {
		 for (int i = 0; i < files.length; i++) 
		 {  
		    if (!files[i].isDirectory()) 
		    {  
		       fileList.add(files[i]);
		       iFileCount ++;
		    }
		 }
	  }
		  
	  try
	  {
		  ret = uploadMultiFile(fileList,FremotePath,null);
      }
	  catch(Exception e)
	  {
		Log.d(tag,"uploadFiles:" + e.getMessage() + "," + e.getStackTrace());
	  }
	 
	  SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm:ss");  
      String strDt = tempDate.format(new java.util.Date()).toString();  
	  sendMsg("uploadFiles finished,count=" + iFileCount + ",ret[" + ret +"]," + strDt);
	  return ret;
	}
	
	/**
	 * 上传多个文件.
	 * 
	 * @param localFile
	 *            本地文件
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public boolean uploadMultiFile(LinkedList<File> fileList, String remotePath,
			UploadProgressListener listener) throws IOException {
		boolean ret = true;
		try
		{
		   // 上传之前初始化
		   this.uploadBeforeOperate(remotePath, listener);
		   boolean flag;
		   for (File singleFile : fileList) 
		   {
			  flag = uploadingSingle(singleFile, listener);
			  if (flag) 
			  {
				  //上传成功后会自动将文件移到上传成功目录LocalPath_SUCCESS
				  boolean ret1 = FilesUtil.MoveFileToDesc(singleFile,BusinessMode.LocalPath_SUCCESS);
				  if(listener!=null)
				  {
				     listener.onUploadProgress(MainActivity.FTP_UPLOAD_SUCCESS, 0,singleFile);
				  }
			  } 
			  else 
			  {
				  //过长时间上传失败的会移到上传失败目录LocalPath_FAIL
				  boolean bTooOld = FilesUtil.isTooOld(singleFile);
				  if(bTooOld)
				  {
					  boolean ret1 = FilesUtil.MoveFileToDesc(singleFile,BusinessMode.LocalPath_FAIL);
					  if(!ret1)
					  {
						 try{ singleFile.delete();}catch(Exception e)
						 {Log.d(tag,"uploadMultiFile singleFile.delete[" + singleFile.getName() +"],err:" +  e.getMessage() + "," +  e.getStackTrace());}  
					  }
				  }
				  ret = false;
				  if(listener!=null)
				  {
				     listener.onUploadProgress(MainActivity.FTP_UPLOAD_FAIL, 0,singleFile);
				  }
			  }
		   }
		   // 上传完成之后关闭连接
		   this.uploadAfterOperate(listener);
		}
		catch(Exception e)
		{
		   ret = false;
		   this.uploadAfterOperate(listener);
		   Log.d(tag,"uploadMultiFile err:" + e.getMessage() + "," + e.getStackTrace());
		}
		return ret;
	}

	/**
	 * 上传单个文件.
	 * 
	 * @param localFile
	 *            本地文件
	 * @return true上传成功, false上传失败
	 * @throws IOException
	 */
	private boolean uploadingSingle(File localFile,
			UploadProgressListener listener) throws IOException {
		boolean flag = true;
		// 不带进度的方式
		// // 创建输入流
		// InputStream inputStream = new FileInputStream(localFile);
		// // 上传单个文件
		// flag = ftpClient.storeFile(localFile.getName(), inputStream);
		// // 关闭文件流
		// inputStream.close();

		// 带有进度的方式
		BufferedInputStream buffIn = new BufferedInputStream(
				new FileInputStream(localFile));
		ProgressInputStream progressInput = new ProgressInputStream(buffIn,
				listener, localFile);
		flag = ftpClient.storeFile(localFile.getName(), progressInput);
		buffIn.close();

		return flag;
	}

	/**
	 * 上传文件之前初始化相关参数
	 * 
	 * @param remotePath
	 *            FTP目录
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	private void uploadBeforeOperate(String remotePath,
			UploadProgressListener listener) throws IOException {

		// 打开FTP服务
		try {
			this.openConnect();
			if(listener!=null)
			{
			   listener.onUploadProgress(MainActivity.FTP_CONNECT_SUCCESSS, 0,null);
			}
		} catch (IOException e1) 
		{
			e1.printStackTrace();
			if(listener!=null)
			{
			   listener.onUploadProgress(MainActivity.FTP_CONNECT_FAIL, 0, null);
			}
			return;
		}

		// 设置模式
		ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
		// FTP下创建文件夹
		ftpClient.makeDirectory(remotePath);
		// 改变FTP目录
		ftpClient.changeWorkingDirectory(remotePath);
		// 上传单个文件

	}

	/**
	 * 上传完成之后关闭连接
	 * 
	 * @param listener
	 * @throws IOException
	 */
	private void uploadAfterOperate(UploadProgressListener listener)
			throws IOException {
		this.closeConnect();
		if(listener!=null)
		{
		   listener.onUploadProgress(MainActivity.FTP_DISCONNECT_SUCCESS, 0, null);
		}
	}

	// -------------------------------------------------------文件下载方法------------------------------------------------

	/**
	 * 下载单个文件，可实现断点下载.
	 * 
	 * @param serverPath
	 *            Ftp目录及文件路径
	 * @param localPath
	 *            本地目录
	 * @param fileName       
	 *            下载之后的文件名称
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
			throws Exception {

		// 打开FTP服务
		try {
			this.openConnect();
			if(listener!=null)
			{
			   listener.onDownLoadProgress(MainActivity.FTP_CONNECT_SUCCESSS, 0, null);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			if(listener!=null)
			{
			   listener.onDownLoadProgress(MainActivity.FTP_CONNECT_FAIL, 0, null);
			}
			return;
		}

		// 先判断服务器文件是否存在
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			if(listener!=null)
			{
			   listener.onDownLoadProgress(MainActivity.FTP_FILE_NOTEXISTS, 0, null);
			}
			return;
		}

		//创建本地文件夹
		File mkFile = new File(localPath);
		if (!mkFile.exists()) {
			mkFile.mkdirs();
		}

		localPath = localPath + fileName;
		// 接着判断下载的文件是否能断点下载
		long serverSize = files[0].getSize(); // 获取远程文件的长度
		File localFile = new File(localPath);
		long localSize = 0;
		if (localFile.exists()) {
			localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
			if (localSize >= serverSize) {
				File file = new File(localPath);
				file.delete();
			}
		}
		
		// 进度
		long step = serverSize / 100;
		long process = 0;
		long currentSize = 0;
		// 开始准备下载文件
		OutputStream out = new FileOutputStream(localFile, true);
		ftpClient.setRestartOffset(localSize);
		InputStream input = ftpClient.retrieveFileStream(serverPath);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = input.read(b)) != -1) {
			out.write(b, 0, length);
			currentSize = currentSize + length;
			if (currentSize / step != process) {
				process = currentSize / step;
				if (process % 5 == 0) {  //每隔%5的进度返回一次
					if(listener!=null)
					{
					   listener.onDownLoadProgress(MainActivity.FTP_DOWN_LOADING, process, null);
					}
				}
			}
		}
		out.flush();
		out.close();
		input.close();
		
		// 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
		if (ftpClient.completePendingCommand()) {
			if(listener!=null)
			{
			   listener.onDownLoadProgress(MainActivity.FTP_DOWN_SUCCESS, 0, new File(localPath));
			}
		} else {
			if(listener!=null)
			{
			   listener.onDownLoadProgress(MainActivity.FTP_DOWN_FAIL, 0, null);
			}
		}

		// 下载完成之后关闭连接
		this.closeConnect();
		if(listener!=null)
		{
		   listener.onDownLoadProgress(MainActivity.FTP_DISCONNECT_SUCCESS, 0, null);
		}

		return;
	}

	// -------------------------------------------------------文件删除方法------------------------------------------------

	/**
	 * 删除Ftp下的文件.
	 * 
	 * @param serverPath
	 *            Ftp目录及文件路径
	 * @param listener
	 *            监听器
	 * @throws IOException
	 */
	public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener)
			throws Exception {

		// 打开FTP服务
		try {
			this.openConnect();
			if(listener!=null)
			{
			  listener.onDeleteProgress(MainActivity.FTP_CONNECT_SUCCESSS);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			if(listener!=null)
			{
			   listener.onDeleteProgress(MainActivity.FTP_CONNECT_FAIL);
			}
			return;
		}

		// 先判断服务器文件是否存在
		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			if(listener!=null)
			{
			  listener.onDeleteProgress(MainActivity.FTP_FILE_NOTEXISTS);
			}
			return;
		}
		
		//进行删除操作
		boolean flag = true;
		flag = ftpClient.deleteFile(serverPath);
		if (flag) {
			if(listener!=null)
			{
			   listener.onDeleteProgress(MainActivity.FTP_DELETEFILE_SUCCESS);
			}
		} else {
			if(listener!=null)
			{
			   listener.onDeleteProgress(MainActivity.FTP_DELETEFILE_FAIL);
			}
		}
		
		// 删除完成之后关闭连接
		this.closeConnect();
		if(listener!=null)
		{
		   listener.onDeleteProgress(MainActivity.FTP_DISCONNECT_SUCCESS);
		}
		
		return;
	}

	// -------------------------------------------------------打开关闭连接------------------------------------------------

	/**
	 * 打开FTP服务.
	 * 
	 * @throws IOException
	 */
	public void openConnect() throws IOException {
		// 中文转码
		ftpClient.setControlEncoding("UTF-8");
		int reply; // 服务器响应值
		// 连接至服务器
		ftpClient.connect(FhostName, FserverPort);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		}
		// 登录到服务器
		ftpClient.login(FuserName, Fpassword);
		// 获取响应值
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// 断开连接
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		} else {
			// 获取登录信息
			FTPClientConfig config = new FTPClientConfig(ftpClient
					.getSystemType().split(" ")[0]);
			config.setServerLanguageCode("zh");
			ftpClient.configure(config);
			// 使用被动模式设为默认
			ftpClient.enterLocalPassiveMode();
			// 二进制文件支持
			ftpClient
					.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
	}

	/**
	 * 关闭FTP服务.
	 * 
	 * @throws IOException
	 */
	public void closeConnect() throws IOException {
		if (ftpClient != null) {
			// 退出FTP
			ftpClient.logout();
			// 断开连接
			ftpClient.disconnect();
		}
	}
	
	/**
	 * 生成文件.
	 * 
	 * @throws IOException
	 */
	public void genFile()
	{
		
	}

	// ---------------------------------------------------上传、下载、删除监听---------------------------------------------
	
	/*
	 * 上传进度监听
	 */
	public interface UploadProgressListener {
		public void onUploadProgress(String currentStep, long uploadSize, File file);
	}

	/*
	 * 下载进度监听
	 */
	public interface DownLoadProgressListener {
		public void onDownLoadProgress(String currentStep, long downProcess, File file);
	}

	/*
	 * 文件删除监听
	 */
	public interface DeleteFileProgressListener {
		public void onDeleteProgress(String currentStep);
	}

	
	public void setFhostName(String hostName)
	{
		FhostName = hostName;
	}
	
	public void setFserverPort(int serverPort)
	{
		FserverPort = serverPort;
	}
	
	public void setFuserName(String userName)
	{
		FuserName = userName;
	}
	
	public void setFpassword(String password)
	{
		Fpassword = password;
	}
	
	public void setFremotePath(String remotePath)
	{
		FremotePath = remotePath;
	}
}

