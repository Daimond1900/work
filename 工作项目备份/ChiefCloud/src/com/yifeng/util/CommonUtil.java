package com.yifeng.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.ChifCloud12345.service.SendNotificationService;
import com.yifeng.ChifCloud12345update.MainActivity;
import com.yifeng.ChifCloud12345update.R;

/**
 * activity常用功能类
 * 
 * @author 吴家宏 2011-4-27
 */
public class CommonUtil{
	private Activity activity;
	private Calendar c;

	public CommonUtil(Activity activity) {
		this.activity = activity;
		c = Calendar.getInstance();
	}

	/**
	 * 判断手机是否能联网
	 * 
	 * @return
	 */
	public boolean checkNetWork() {
		ConnectivityManager cManager = (ConnectivityManager) this.activity
				.getSystemService(this.activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			// do something
			// 能联网
			return true;
		} else {
			// do something
			// 不能联网
			return false;
		}

	}
	/**
	 * -1没网络,1:wifi网 2:3G网
	 * @return
	 */
    public int isWifi(){
    	int ret=0;
    	ConnectivityManager cManager = (ConnectivityManager) this.activity
		.getSystemService(this.activity.CONNECTIVITY_SERVICE);
    	
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info == null || !cManager.getBackgroundDataSetting()) {  
			    return ret=-1;  
	    } 

		//判断网络连接类型，只有在3G或wifi里进行一些数据更新。    
		int netType = info.getType();  
		int netSubtype = info.getSubtype(); 
		
		if (netType == ConnectivityManager.TYPE_WIFI) {//wifi  
			    ret=1; 
		}
		//3g
		else if (netType == ConnectivityManager.TYPE_MOBILE  
				           && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS  ) {  
			 ret=2; 
		}
	  return ret;
		

    }
	/**
	 * 退出程序
	 * 
	 * @param context
	 * @return
	 */
	public void doExit() {
		final Dialog builder = new Dialog(this.activity, R.style.dialog);
		builder.setContentView(R.layout.confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(activity.getString(R.string.alert_dialog_finish_title));
		pMsg.setText(activity.getString(R.string.alert_dialog_finish));
		Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.iflogin=false;
				builder.dismiss();
				Intent startTongZi = new Intent(activity,
						SendNotificationService.class);
				activity.stopService(startTongZi);

				// 取消通知栏
				String service = Context.NOTIFICATION_SERVICE;
				NotificationManager nm = (NotificationManager) activity
						.getSystemService(service);
				nm.cancel(1);
				nm.cancel(2);
				dosetUser();
				ActivityStackControlUtil.finishProgram();// 退出
			}
		});

		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();

	}
	
	/**
	 * 临时保存用户名,判断接受到消息返回登陆首页
	 */
	private void dosetUser(){
		SharedPreferences rmd = activity.getSharedPreferences("loginstate", 0);
		rmd.edit().putString("loginName", "").commit();
	}

	/**
	 * 获取时间控件 2011-02-10格式
	 * 
	 * @return
	 */
	public void getTime(Button btn) {
		final Button btn1 = btn;
		String time = btn1.getText().toString();
		if (time.equals("")) {
			new DatePickerDialog(this.activity, new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String month = String.valueOf(monthOfYear + 1);
					String day = String.valueOf(dayOfMonth);
					if ((monthOfYear + 1) < 10) {
						month = "0" + month;
					}
					if (dayOfMonth < 10) {
						day = "0" + day;
					}
					btn1.setText(String.valueOf(year) + "-" + month + "-" + day);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
					.get(Calendar.DATE)).show();
		} else {
			int cyear = c.get(Calendar.YEAR);
			int cmoth = c.get(Calendar.MONTH);
			int cdate = c.get(Calendar.DATE);
			try {
				if (time.length() >= 10) {
					cyear = Integer.parseInt(time.substring(0, 4));
					cmoth = Integer.parseInt(time.substring(5, 7)) - 1;
					cdate = Integer.parseInt(time.substring(8, 10));
				}
			} catch (Exception e) {
			}

			final int year = cyear;
			final int moth = cmoth;
			final int date = cdate;

			new DatePickerDialog(this.activity, new OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String month = String.valueOf(monthOfYear + 1);
					String day = String.valueOf(dayOfMonth);
					if ((monthOfYear + 1) < 10) {
						month = "0" + month;
					}
					if (dayOfMonth < 10) {
						day = "0" + day;
					}
					btn1.setText(String.valueOf(year) + "-" + month + "-" + day);
				}
			}, year, moth, date).show();
		}

	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @return
	 */
	public void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(this.activity, R.style.dialog);
		builder.setContentView(R.layout.dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}

	/**
	 * 结束程序
	 */
	public void exit() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.activity.startActivity(startMain);
		System.exit(0);
	}

	/**
	 * 
	 * @param 信息提示
	 */
	public void showToast(String context) {
		Toast.makeText(this.activity, context, Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 * @param 信息提示短时间显示
	 */
	public void shortToast(String context) {
		Toast.makeText(this.activity, context, Toast.LENGTH_SHORT).show();
	}

	/***
	 * SDK号
	 * 
	 * @return
	 */
	public String getSDK() {
		String sdk = android.os.Build.VERSION.SDK; // SDK号
		return sdk;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public String getModel() {
		String model = android.os.Build.MODEL; // 手机型号
		return model;
	}

	/**
	 * android系统版本号
	 * 
	 * @return
	 */
	public String getVersion() {
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		return release;
	}

	/**
	 * 去掉带有控格的EditText
	 * 
	 * @param str
	 * @return
	 */
	public String doConvertEmpty(String str) {
		String nstr = "";
		try {
			if (!str.equals("") || str != null) {
				nstr = str.replaceAll(" ", "");
			}
		} catch (Exception e) {
			return nstr;
		}
		return nstr;
	}

	/**
	 * 根据HTTP路径获取远程图片即时展现
	 * 
	 * @param uriPic
	 * @return
	 */

	public static Bitmap getURLBitmap(String uriPic) {
		URL imageUrl = null;
		Bitmap bitmap = null;
		InputStream is=null;
		HttpURLConnection conn=null;
		try {
			/* new URL对象将网址传入 */
			imageUrl = new URL(uriPic);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			/* 取得联机 */
			  conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.connect();
			/* 取得回传的InputStream */
			is = conn.getInputStream();
			/* 将InputStream变成Bitmap */
			bitmap = BitmapFactory.decodeStream(is);
			/* 关闭InputStream */
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			if(conn!=null){
				conn.disconnect();
			}
		}
		return bitmap;
	}
}
