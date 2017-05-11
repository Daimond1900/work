package com.yifeng.hngly.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.yifeng.hngly.ui.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Android AutoUpdate.
 * 吴家宏/2011.04.11
 * 1.Set apkUrl.
 * 2.check().
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
	/* 更新进度条 */
	private ProgressBar mProgress;
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 记录进度条数量 */
	private int progress=0;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	/* 更新消息 */
	private TextView pMsg;
	private Dialog mDownloadDialog;
	private File myTempFile;
	private int length = -1;// 下载文件大小
	private int count = 0;// 已下载大小
	private String Msg = "";
	private  CommonUtil dialogUtil;
	
	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public AutoUpdate(Activity activity) {
		this.activity = activity;
		strURL = ConstantUtil.downapk;
		dialogUtil=new CommonUtil(activity);
		getCurrentVersion();

	}

	public void check() {
		if (isNetworkAvailable(this.activity) == false) {

			return;

		}else{
			showUpdateDialog();
			

		}

	}

	public boolean isNetworkAvailable(Context ctx) {

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
				/*
				 * downloadTheFile(strURL); showWaitDialog();
				 */
				builder.dismiss();
				
				// 显示下载对话框
				showDownloadDialog();
				//showWaitDialog();
				downloadTheFile(strURL);
				
			}
		});

		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
				
				//强制更新,如果不更新直接退出
				activity.finish();
				dialogUtil.exit();
			}
		});
		builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();

	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		final Dialog mDownloadDialog = new Dialog(this.activity, R.style.dialog);
		mDownloadDialog.setContentView(R.layout.down_dialog);
		
		pMsg = (TextView) mDownloadDialog.findViewById(R.id.pMsg);// 更新消息
		mProgress = (ProgressBar) mDownloadDialog
				.findViewById(R.id.update_progress);
		
		
		Button cancel_btn = (Button) mDownloadDialog.findViewById(R.id.cancel_btn);//取消更新
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDownloadDialog.dismiss();
				
				// 设置取消状态
				cancelUpdate = true;
				
				//强制更新,如果不更新直接退出
				activity.finish();
				dialogUtil.exit();
				
			}
		});
		
		mDownloadDialog.show();
		
		
	}

	/**
	 * 更新进度条
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
			    mProgress.setProgress(progress);
				pMsg.setText(Html.fromHtml("已下载 <font color='red'>" + progress + "%</font>,("+count + "b/" + length + "b)请稍等..."));
				//dialog.setMessage(Html.fromHtml("准备下载  <font color='red' size='30'>" +progress+"%</font> 请稍后..."));
				break;
			case DOWNLOAD_FINISH:
				
				// 打开文件
				openFile(myTempFile);
				activity.finish();
				
				break;
			default:
				break;
			}
		};
	};

	public void showWaitDialog() {

		dialog = new ProgressDialog(activity);
		//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平进度条
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度
		//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平进度条
		//dialog.setSecondaryProgress(50);  

		dialog.setMessage(Html.fromHtml("准备下载  <font color='red' size='30'>" +progress+"%</font> 请稍后..."));

		dialog.setIndeterminate(true);//设置进度条是否为不明确

		dialog.setCancelable(true);//设置进度条是否可以按退回键取消  
		
		dialog.setButton("取消", new DialogInterface.OnClickListener(){  
			  
             @Override  
             public void onClick(DialogInterface dialog, int which) {  
                 dialog.cancel();  
                 // 设置取消状态
 				cancelUpdate = true;
 				
 				//强制更新,如果不更新直接退出
 				activity.finish();
 				dialogUtil.exit();
             }  
               
         });  

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

	private void downloadTheFile(final String strPath) {

		fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
				.toLowerCase();

		fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
				strURL.lastIndexOf("."));

		try {

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
			

			new Thread(r).start();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private void doDownloadTheFile(String strPath) throws Exception {

		Log.i(TAG, "getDataSource()");

		if (!URLUtil.isNetworkUrl(strPath)) {

			Log.i(TAG, "getDataSource() It's a wrong URL!");

		}

		else {

			URL myURL = new URL(strPath);

			URLConnection conn = myURL.openConnection();

			conn.connect();

			// 获取文件大小
			length = conn.getContentLength();

			InputStream is = conn.getInputStream();

			if (is == null) {

				throw new RuntimeException("stream is null");

			}

			// File myTempFile = File.createTempFile(fileNa, "." +
			// fileEx);//下载到sdcard
			myTempFile = new File("data/data/"+ConstantUtil.SYS_PACK_NAME, fileNa + "."
					+ fileEx);// 下载到内存
			// String
			// path=Environment.getExternalStorageDirectory()+"/"+fileNa+"." +
			// fileEx;
			// File myTempFile = new File(path);

			currentTempFilePath = myTempFile.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(myTempFile);

			// byte buf[] = new byte[128];
			// 缓存
			byte buf[] = new byte[1024];
			// 写入到文件中
			do {
				int numread = is.read(buf);
				count += numread;
				// 计算进度条位置
				progress = (int) (((float) count / length) * 100);

				// 更新进度
				mHandler.sendEmptyMessage(DOWNLOAD);
				
				if (numread <= 0) {
					// 下载完成
					mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
					break;
				}
				// 写入文件
				fos.write(buf, 0, numread);

			} while (!cancelUpdate);// 点击取消就停止下载.

			Log.i(TAG, "getDataSource() Download ok...");

			/** 下载到内存加权限 **/
			String command = "chmod 777 " + currentTempFilePath;
			Log.i("zyl", "command = " + command);
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);

			/*
			 * dialog.cancel(); dialog.dismiss();
			 */

			/* openFile(myTempFile); */
			try {

				is.close();

			} catch (Exception ex) {

				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);

			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
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
