package com.yifeng.hnqzt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.LoginActivity;
import com.yifeng.hnqzt.ui.MainActivity;

/**
 * activity常用功能类
 * @author 吴家宏 
 * 2011-4-27
 */
public class CommonUtil {
	private static Activity activity;
	public LinearLayout loadingLayout;
	
	private Calendar c;

	public CommonUtil(Activity activitys) {
		activity = activitys;
		c = Calendar.getInstance();
	}

	/**
	 * 判断手机是否能联网
	 * 
	 * @return
	 */
	public boolean checkNetWork() {
		ConnectivityManager cManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
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
	 * 返回道页
	 */
	public void goHome() {
		Intent home = new Intent(activity, MainActivity.class);
		home.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		activity.startActivity(home);
		activity.finish();
	}

	/**
	 * 跳回闪屏界面
	 */
	public void goLogin() {
		Intent login = new Intent(activity, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		activity.startActivity(login);
		activity.finish();
	}

	/***
	 * 注销用户
	 */
/*	public static void doLogOut() {
		UserDAL dal = new UserDAL(context);
		dal.doLogOut();
		ActivityStackControlUtil.finishProgram();// 退出
	}*/

	/**
	 * 结束程序
	 */
	public void exit() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(startMain);
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
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
			new DatePickerDialog(activity, new OnDateSetListener() {
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

			new DatePickerDialog(activity, new OnDateSetListener() {
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
	
	
	/**
	 * 获取时间控件 2012-08格式
	 * 
	 * @return
	 */
	public void getMonth(Button btn) {
		final Button btn1 = btn;
		String time = btn1.getText().toString();
		if (time.equals("")) {
			new DatePickerDialog(activity, new OnDateSetListener() {
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
					btn1.setText(String.valueOf(year) + "-" + month);
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

			new DatePickerDialog(activity, new OnDateSetListener() {
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
					btn1.setText(String.valueOf(year) + "-" + month);
				}
			}, year, moth, date).show();
		}

	}

	/**
	 * 获取时间控件 20110210格式
	 * 
	 * @return
	 */
	public void getTime1(Button btn) {
		final Button btn1 = btn;
		String time = btn1.getText().toString();
		if (time.equals("")) {
			new DatePickerDialog(activity, new OnDateSetListener() {
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
					btn1.setText(String.valueOf(year) + month + day);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE)).show();

		} else {
			int cyear = c.get(Calendar.YEAR);
			int cmoth = c.get(Calendar.MONTH);
			int cdate = c.get(Calendar.DATE);
			try {
				if (time.length() >= 8) {
					cyear = Integer.parseInt(time.substring(0, 4));
					cmoth = Integer.parseInt(time.substring(4, 6)) - 1;
					cdate = Integer.parseInt(time.substring(6, 8));
				}
			} catch (Exception e) {
			}

			final int year = cyear;
			final int moth = cmoth;
			final int date = cdate;

			new DatePickerDialog(activity, new OnDateSetListener() {
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
					btn1.setText(String.valueOf(year) + month + day);
				}
			}, year, moth, date).show();
		}

	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public String getCurDate() {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		String months = String.valueOf(month);
		String days = String.valueOf(day);
		if ((month) < 10) {
			months = "0" + month;
		}
		if (day < 10) {
			days = "0" + day;
		}
		String cuDate = year + months + days;
		return cuDate;
	}

	/**
	 * 获当月第一天 如:20110101
	 * 
	 * @return
	 */
	public String getFirstDate() {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
//		int day = c.get(Calendar.DATE);
		String months = String.valueOf(month);
		if ((month) < 10) {
			months = "0" + month;
		}

		String cuDate = year + months + "01";
		return cuDate;
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

	/**
	 * 根据HTTP路径获取远程图片加头信息
	 * @param uriPic
	 * @return
	 */
	public Map<String, Object> getURLCode(String uriPic) throws Exception{
		URL imageUrl = null;
		Bitmap bitmap = null;
		Map<String, Object> map = new HashMap<String, Object>();

		/* new URL对象将网址传入 */
		imageUrl = new URL(uriPic);
		/* 取得联机 */
		HttpURLConnection conn = (HttpURLConnection) imageUrl
				.openConnection();
		conn.connect();
		Map<String, List<String>> hf = conn.getHeaderFields();
		List<String> set_cookie = hf.get("Set-Cookie");
		String setCookie = set_cookie.get(0);
		String[] cookie = setCookie.split(";");
		System.out.println("cookie=" + cookie[0]);

		/* 取得回传的InputStream */
		InputStream is = conn.getInputStream();
		/* 将InputStream变成Bitmap */
		bitmap = BitmapFactory.decodeStream(is);
		/* 关闭InputStream */
		is.close();
		/* 将图片与cookie插入map中 */
		map.put("cookie", cookie[0]);
		map.put("bitmap", bitmap);
		return map;
	}
	
	/**
	 * 获得图片的字节数组
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	public String getFix(String fileName) {
		String ext = "temp";
		if (fileName != null) {
			int mid = fileName.lastIndexOf(".");
			ext = fileName.substring(mid + 1, fileName.length());

		}
		return ext;
	}

	/****
	 * 在路径中获取文件名
	 * @param path
	 * @return
	 */
	public String getFixName(String path) {
		String newFileName = "temp";
		if (path != null) {
			int mid = path.lastIndexOf("/");
			newFileName = path.substring(mid + 1, path.length());
		}
		return newFileName;

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
	
	public void showListAddDataState(ListView listv,String state){
		if (state
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			shortToast("服务器未响应");
			if(loadingLayout!=null)
			listv.removeFooterView(this.loadingLayout);
		}

		else if (state
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			shortToast("数据解析失败");
			if(loadingLayout!=null)
			listv.removeFooterView(this.loadingLayout);

		} else if (state
				.equals(String.valueOf(ConstantUtil.IS_EMPTY))) {
			if(loadingLayout!=null)
			 listv.removeFooterView(this.loadingLayout);
			 shortToast("无新数据");
		} 
		else if(state
				.equals(String.valueOf(ConstantUtil.DATA_NULL))){
			if(loadingLayout!=null)
			 listv.removeFooterView(this.loadingLayout);
			 shortToast("服务端放回null");
		}else if(state
				.equals(String.valueOf(ConstantUtil.KEY_ERROR))){
			if(loadingLayout!=null)
			 listv.removeFooterView(this.loadingLayout);
			 shortToast("重复登录");
			 
		}
	}
	/**
	 * 分页listview 脚部 加载
	 * @return
	 */
	public LinearLayout  addFootBar() {
		LayoutInflater inflater = LayoutInflater.from(this.activity);
		View searchLayout = inflater.inflate(R.layout.publicloadingprogress, null);
			
			loadingLayout = new LinearLayout(this.activity);
			loadingLayout.addView(searchLayout);
			loadingLayout.setGravity(Gravity.CENTER);
			return loadingLayout;
		}
	
	/**
	 * 
	 * @param 信息提示短时间显示
	 */
	public void shortToast(String context){
		//showComToast(context,Toast.LENGTH_SHORT);
		Toast.makeText(this.activity, context, Toast.LENGTH_SHORT).show();
	}
	
	/***
	 * 系统进度条
	 * @param msg
	 * @return
	 */
	public ProgressDialog showProgressDialog(String msg){
		ProgressDialog progressDialog = ProgressDialog.show(this.activity, null,msg, true);
		progressDialog.setIndeterminate(true);//设置进度条是否为不明确
	    progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消 
	    return progressDialog;
	}
	
}
