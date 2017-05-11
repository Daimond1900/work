package com.yifeng.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.LoginActivity;
import com.yifeng.ChifCloud12345update.R;

/**
 * 
 * Android AutoUpdate.
 * 
 * 吴家宏/2011.04.11
 * 
 * 1.Set apkUrl.
 * 
 * 2.check().
 * 
 * 3.add delFile() method in resume()onPause().
 */

public class AutoUpdate {

	public Activity activity = null;

	public int versionCode = 0;

	public String versionName = "";

	private static final String TAG = "AutoUpdate";

	private String currentFilePath = "";

	private String currentTempFilePath = "";

	private String fileEx = "";

	private String fileNa = "";

	private String strURL = "";

	private ProgressDialog dialog;
	private String Msg = "";

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public AutoUpdate(Activity activity) {
		this.activity = activity;
		strURL = activity.getString(R.string.ipconfig)
				+ "android/version/YZRXY.apk";
		getCurrentVersion();

	}

	public void check() {
		if (isNetworkAvailable(this.activity) == false) {

			return;

		}

		if (true) {// Check version.

			showUpdateDialog();

		}

	}

	public static boolean isNetworkAvailable(Context ctx) {

		try {

			ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = cm.getActiveNetworkInfo();

			return (info != null && info.isConnected());

		} catch (Exception e) {

			e.printStackTrace();

			return false;

		}

	}

	public void showUpdateDialog() {
		final Dialog builder = new Dialog(this.activity, R.style.dialog);
		builder.setContentView(R.layout.confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText("版本更新");
		pMsg.setText(Html.fromHtml(getMsg()));
		Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
		confirm_btn.setText("立即更新");
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadTheFile(strURL);
				showWaitDialog();
				builder.dismiss();
			}
		});

		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				activity.finish();
			}
		});
		builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();

		// @SuppressWarnings("unused")
		/*
		 * AlertDialog alert = new AlertDialog.Builder(this.activity).setTitle(
		 * "版本更新").setMessage("发现新版本是否更新?") .setPositiveButton("更新", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * public void onClick(DialogInterface dialog, int which) {
		 * 
		 * downloadTheFile(strURL);
		 * 
		 * showWaitDialog();
		 * 
		 * }
		 * 
		 * }).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
		 * 
		 * public void onClick(DialogInterface dialog, int which) {
		 * dialog.cancel();
		 * 
		 * }
		 * 
		 * }).show();
		 */

	}

	public void showWaitDialog() {

		dialog = new ProgressDialog(activity);

		dialog.setMessage("正在下载请稍后....");

		dialog.setIndeterminate(true);

		dialog.setCancelable(true);

		dialog.show();

	}

	public void getCurrentVersion() {

		try {

			PackageInfo info = activity.getPackageManager().getPackageInfo(

			activity.getPackageName(), 0);

			this.versionCode = info.versionCode;

			this.versionName = info.versionName;

		} catch (NameNotFoundException e) {

			e.printStackTrace();

		}

	}
	public int FileLength=0,DownedFileLength=0;
	private void downloadTheFile(final String strPath) {
		DownedFileLength=0;
		
		fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
				.toLowerCase();

		fileNa = strURL.substring(strURL.lastIndexOf("/") + 1, strURL
				.lastIndexOf("."));

		try {

			/*if (strPath.equals(currentFilePath)) {

				doDownloadTheFile(strPath);

			}

			currentFilePath = strPath;

			Runnable r = new Runnable() {

				public void run() {

					try {

						doDownloadTheFile(strPath);

					} catch (Exception e) {

						Log.e(TAG, e.getMessage(), e);

					}

				}

			};

			new Thread(r).start();*/
			
			currentFilePath = strPath;
			
			Thread thread=new Thread(){  
			        public void run(){  
			            try {  
			            	doDownloadTheFile(strPath);
			             } catch (Exception e) { 
			            	e.printStackTrace();
			            	dialog.dismiss();
			            	Intent intent = new Intent(activity, LoginActivity.class);
							activity.startActivity(intent);
							activity.finish();
			            	System.out.println("系统未知错误!");    
			             }  
			          }  
			   };  
			  thread.start();  
			

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

//	private void doDownloadTheFile(String strPath) throws Exception {
//
//		Log.i(TAG, "getDataSource()");
//
//		if (!URLUtil.isNetworkUrl(strPath)) {
//
//			Log.i(TAG, "getDataSource() It's a wrong URL!");
//
//		}
//
//		else {
//
//			URL myURL = new URL(strPath);
//
//			URLConnection conn = myURL.openConnection();
//
//			conn.connect();
//
//			InputStream is = conn.getInputStream();
//
//			if (is == null) {
//
//				throw new RuntimeException("stream is null");
//
//			}
//
//			// File myTempFile = File.createTempFile(fileNa, "." +
//			// fileEx);//下载到sdcard
//			File myTempFile = new File(
//					"data/data/com.yifeng.ChifCloud12345update", fileNa + "."
//							+ fileEx);// 下载到内存
//			// String
//			// path=Environment.getExternalStorageDirectory()+"/"+fileNa+"." +
//			// fileEx;
//			// File myTempFile = new File(path);
//
//			currentTempFilePath = myTempFile.getAbsolutePath();
//
//			FileOutputStream fos = new FileOutputStream(myTempFile);
//
//			byte buf[] = new byte[128];
//
//			do {
//
//				int numread = is.read(buf);
//
//				if (numread <= 0) {
//
//					break;
//
//				}
//
//				fos.write(buf, 0, numread);
//
//			} while (true);
//
//			Log.i(TAG, "getDataSource() Download ok...");
//
//			/** 下载到内存加权限 **/
//			String command = "chmod 777 " + currentTempFilePath;
//			Log.i("zyl", "command = " + command);
//			Runtime runtime = Runtime.getRuntime();
//			runtime.exec(command);
//
//			dialog.cancel();
//
//			dialog.dismiss();
//			openFile(myTempFile);
//			try {
//				is.close();
//			} catch (Exception ex) {
//
//				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
//
//			}
//
//		}
//
//	}
	  private Handler handler=new Handler()  
	     {  
	        public void handleMessage(Message msg)  
	       {  
	        	if (!Thread.currentThread().isInterrupted()) 
	            { 
	        		  switch (msg.what) {  
	                    case 0:  
	                    	//dialog.setMax(FileLength);  
	                    	dialog.setMessage(Html.fromHtml("准备下载  <font color='red' size='30'>" +DownedFileLength+"</font> 请稍后..."));
	                    	Log.i("文件长度----------->", dialog.getMax()+"");    
	                	  break;
	                    case 1:  
	                    	//dialog.setProgress(DownedFileLength);  
	                    	int x=DownedFileLength*100/FileLength;  
	                        dialog.setMessage(Html.fromHtml("正在下载 <font color='red' size='30'>"+x+"%</font>请稍后..."));
	                  	  break;
	                    case 2:  
	                    	//下载成功
	                  	  break;
	                  }
	            } 
	      } 
	     };
	private void doDownloadTheFile(String strPath) throws Exception {

		Log.i(TAG, "getDataSource()");

		if (!URLUtil.isNetworkUrl(strPath)) {

			Log.i(TAG, "getDataSource() It's a wrong URL!");

		}

		else {
			Message message=new Message();  
			URL myURL = new URL(strPath);

			URLConnection conn = myURL.openConnection();

			conn.connect();

			InputStream is = conn.getInputStream();

			if (is == null) {

				throw new RuntimeException("stream is null");

			}

			//File myTempFile = File.createTempFile(fileNa, "." + fileEx);//下载到sdcard
			File myTempFile = new File("data/data/com.yifeng.ChifCloud12345update",fileNa+"."+fileEx);//下载到内存
			//String path=Environment.getExternalStorageDirectory()+"/"+fileNa+"." + fileEx;
			//File myTempFile = new File(path); 

			currentTempFilePath = myTempFile.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(myTempFile);
			
			//下载进度 开始下载
			FileLength=conn.getContentLength();
			message.what=0;  
            handler.sendMessage(message); 
			//////////////////////
            
			byte buf[] = new byte[4 * 1024];

			do {

				int numread = is.read(buf);
				
				//下载进度 正在下载
				DownedFileLength+=numread;
				Message message1=new Message();  
                message1.what=1;  
                handler.sendMessage(message1);  
                /////////////
                
				if (numread <= 0) {

					break;

				}

				fos.write(buf, 0, numread);

			} while (true);
			
			//下载进度结束
			Message message2=new Message();  
            message2.what=2;  
            handler.sendMessage(message2);
            //////////////////////

			Log.i(TAG, "getDataSource() Download ok...");
			
			/**下载到内存加权限**/
			String command = "chmod 777 " + currentTempFilePath;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
            
			dialog.cancel();

			dialog.dismiss();

			openFile(myTempFile);

			try {

				is.close();

			} catch (Exception ex) {

				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);

			}

		}

	}
	public boolean ifReset() {
		PackageManager manager = activity.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(
					"com.yifeng.ChifCloud12345", 0);
		} catch (NameNotFoundException e) {
			return false;
		}

		return true;
	}

	public void delAPK() {
		final Dialog builder = new Dialog(this.activity, R.style.dialog);
		builder.setContentView(R.layout.confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText("升级须知");
		pMsg.setText("检测到旧版热线云，\n您只需点击卸载即可进行卸载,\n此次卸载不会影响热线云后续使用。\n(卸载完成下次登陆将不再提示)");
		Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
		confirm_btn.setText("立即卸载");
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);

		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				delShortcut();
//				addShortcut();
				
				// 卸载旧版apk
				 Uri packageURI =
				 Uri.parse("package:com.yifeng.ChifCloud12345");
				 Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
				 packageURI);
				 activity.startActivity(uninstallIntent);
				
//				 String command ="pm uninstall com.yifeng.ChifCloud12345";
//				 Runtime runtime = Runtime.getRuntime();
//				 try {
//				 runtime.exec(command);
//				 } catch (IOException e) {
//				 e.printStackTrace();
//				 }
//				execCommand("pm", "uninstall", "com.yifeng.ChifCloud12345");
				
				builder.dismiss();
			}
		});
		cancel_btn.setText("下次再说");
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				File file =new File("/mnt/sdcard/"+
//".android_secure/com.yifeng.ChifCloud12345-2.asec");
////				if(file.exists())
//					Toast.makeText(activity, file.exists()+"", Toast.LENGTH_SHORT).show();
				builder.dismiss();
				
			}
		});
		// cancel_btn.setEnabled(false);
		// builder.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
		builder.show();

	}
	   private void addShortcut(){  

	       //实例化具有安装快捷方式动作的Intent对象   

	       Intent shortcut = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT");  
	        //快捷方式的名称  
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,activity. getString(R.string.app_name)); 
	        shortcut.putExtra("duplicate", false); //不允许重复创建  

	        //指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer   
	       //注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序  
	        ComponentName comp = new ComponentName(activity.getPackageName(), ".MainActivity");  
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  
	        //快捷方式的图标  
	        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, R.drawable.icon);  
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  
	       //发送创建快捷方式的广播   
	        activity.sendBroadcast(shortcut);  
	    }  
	   
	   /* 
    * 删除程序的快捷方式   
     */      
   private void delShortcut(){       
      Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");       

       //快捷方式的名称      
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));      

        //指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer       
        //注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式       
        String appClass = "com.yifeng.ChifCloud12345" + ".MainActivity" ;     
        ComponentName comp = new ComponentName("com.yifeng.ChifCloud12345", appClass);     
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));      
        //发送卸载快捷方式的图标           
        activity.sendBroadcast(shortcut);      

  }     
	   
	public static void execCommand(String... command) {

		Process process = null;

		try {

			process = new ProcessBuilder().command(command).start();

			// 对于命令的执行结果我们可以通过流来读取

			 InputStream in = process.getInputStream();
			 OutputStream out = process.getOutputStream();
			 InputStream err = process.getErrorStream();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (process != null)

				process.destroy();
		}

	}

	private void openFile(File f) {

		Intent intent = new Intent();

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = getMIMEType(f);

		intent.setDataAndType(Uri.fromFile(f), type);

		activity.startActivity(intent);

	}

	public void delFile() {

		Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");

		File myFile = new File(currentTempFilePath);

		if (myFile.exists()) {

			myFile.delete();

		}

	}

	private String getMIMEType(File f) {

		String type = "";

		String fName = f.getName();

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {

			type = "audio";

		}

		else if (end.equals("3gp") || end.equals("mp4")) {

			type = "video";

		}

		else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {

			type = "image";

		}

		else if (end.equals("apk")) {

			type = "application/vnd.android.package-archive";

		}

		else {

			type = "*";

		}

		if (end.equals("apk")) {

		}

		else {

			type += "/*";

		}

		return type;

	}

}
