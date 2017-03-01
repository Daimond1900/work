package com.yifeng.hngly.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;

import com.yifeng.cloud.entity.SetUp;
import com.yifeng.cloud.entity.User;
import com.yifeng.hngly.ui.MainActivity;
import com.yifeng.hngly.ui.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.sax.StartElementListener;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 常用功能类
 * 
 * @author 吴家宏 2011-09-25
 */
public class CommonUtil {
	private Activity context;
	private Calendar c;
	public Handler tohandler;
	public LinearLayout loadingLayout;

	public CommonUtil(Activity context) {
		this.context = context;
		c = Calendar.getInstance();
		tohandler = new Handler();
	}

	/**
	 * 判断手机是否能联网
	 * 
	 * @return
	 */
	public boolean checkNetWork() {
		ConnectivityManager cManager = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected()) {
			// do something
			// 能联网
			return true;
		} else {
			// do something
			// 不能联网
			return false;
		}

	}

	/***
	 * 显示提求框
	 * 
	 * @param title
	 * @param msg
	 * @param handler
	 * @param message
	 * @param context
	 */
	public static void dialog(String title, String msg, final Handler handler,
			final Message message, Context context) {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		final Button confirm_btn = (Button) builder
				.findViewById(R.id.confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirm_btn.setEnabled(false);

				handler.sendMessage(message);
				builder.dismiss();

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
	 * 退出程序
	 * 
	 * @param context
	 * @return
	 */
	public void doExit() {
		final Dialog builder = new Dialog(this.context, R.style.dialog);
		builder.setContentView(R.layout.confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(context.getString(R.string.alert_dialog_finish_title));
		pMsg.setText(context.getString(R.string.alert_dialog_finish));
		final Button confirm_btn = (Button) builder
				.findViewById(R.id.confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doLogOut();

				// ActivityStackControlUtil.finishProgram();//退出
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

	/***
	 * Bitmap文件转成字节
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/***
	 * 读取本地文件转成字节
	 * 
	 * @param f
	 * @return
	 */
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

	/**
	 * 获取时间控件 2011-02-10格式
	 * 
	 * @return
	 */
	public void getTime(Button btn) {
		final Button btn1 = btn;
		String time = btn1.getText().toString();
		if (time.equals("")) {
			new DatePickerDialog(this.context, new OnDateSetListener() {
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

			new DatePickerDialog(this.context, new OnDateSetListener() {
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
	 * 获取时间控件 2011-02-10格式
	 * 
	 * @return
	 */
	public void doGetTime(TextView btn) {
		final TextView btn1 = btn;
		String time = btn1.getText().toString();
		if (time.equals("")) {
			new DatePickerDialog(this.context, new OnDateSetListener() {
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

					String ndate = String.valueOf(year) + "-" + month + "-"
							+ day;
					btn1.setText(ndate);

					Message msg = new Message();
					Bundle data = msg.getData();
					data.putString("data", ndate);
					tohandler.sendMessage(msg);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE)).show();
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

			new DatePickerDialog(this.context, new OnDateSetListener() {
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
					String ndate = String.valueOf(year) + "-" + month + "-"
							+ day;
					btn1.setText(ndate);

					Message msg = new Message();
					Bundle data = msg.getData();
					data.putString("data", ndate);
					tohandler.sendMessage(msg);
				}
			}, year, moth, date).show();
		}

	}

	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @return
	 */
	public void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(this.context, R.style.dialog);
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
		this.context.startActivity(startMain);
		ConstantUtil.IFLOGIN=false;
		System.exit(0);
	}

	/**
	 * 
	 * @param 信息提示
	 */
	public void showToast(String context) {
		// showComToast(context,Toast.LENGTH_SHORT);
		Toast.makeText(this.context, context, Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 * @param 信息提示短时间显示
	 */
	public void shortToast(String context) {
		// showComToast(context,Toast.LENGTH_SHORT);
		Toast.makeText(this.context, context, Toast.LENGTH_SHORT).show();
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

	/***
	 * 注销用户
	 */
	private void doLogOut() {
		// 删除所有正在运行的activity
		ActivityStackControlUtil.finishProgram();// 退出
		exit();
	}

	/**
	 * 分页listview 脚部 加载
	 * 
	 * @return
	 */
	public LinearLayout addFootBar() {
		LayoutInflater inflater = LayoutInflater.from(this.context);
		View searchLayout = inflater.inflate(R.layout.publicloadingprogress,
				null);
		loadingLayout = new LinearLayout(this.context);

		loadingLayout.addView(searchLayout);
		loadingLayout.setGravity(Gravity.CENTER);
		return loadingLayout;
	}

	/***
	 * 列表页面loading
	 * 
	 * @param listv
	 * @param state
	 */
	public void showListAddDataState(ListView listv, String state) {
		if (state.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			shortToast("服务器未响应");
			if (loadingLayout != null)
				listv.removeFooterView(this.loadingLayout);
		}

		else if (state.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			shortToast("数据解析失败");
			if (loadingLayout != null)
				listv.removeFooterView(this.loadingLayout);

		} else if (state.equals(String.valueOf(ConstantUtil.IS_EMPTY))) {
			if (loadingLayout != null)
				listv.removeFooterView(this.loadingLayout);
			shortToast("无新数据");
		} else if (state.equals(String.valueOf(ConstantUtil.DATA_NULL))) {
			if (loadingLayout != null)
				listv.removeFooterView(this.loadingLayout);
			shortToast("服务端放回null");
		} else if (state.equals(String.valueOf(ConstantUtil.KEY_ERROR))) {
			if (loadingLayout != null)
				listv.removeFooterView(this.loadingLayout);
			shortToast("重复登录");

		}
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
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			/* new URL对象将网址传入 */
			imageUrl = new URL(uriPic);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			/* 取得联机 */
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.connect();
			/* 取得回传的InputStream */
			is = conn.getInputStream();
			/* 将InputStream变成Bitmap */
			bitmap = BitmapFactory.decodeStream(is);
			/* 关闭InputStream */

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return bitmap;
	}

	/**
	 * 根据给定本地路径读取本地资源图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			System.out.println("加载图片资源出错!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取文件后缀名
	 * 
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

	/***
	 * 在路径中获取文件名
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
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 * @author SHANHY
	 */
	public String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/***
	 * 显示系统进度条框
	 * 
	 * @param title
	 * @param msg
	 * @return
	 */
	public ProgressDialog showProg(String title, String msg) {
		ProgressDialog progressDialog = ProgressDialog.show(this.context,
				title, msg, true);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		return progressDialog;
	}

	/***
	 * 判断一个apk是否在本机安装过了
	 * 
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public boolean isPkgInstalled(String packageName) {
		PackageManager pm = this.context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}

	/***
	 * 获取系统配置信息
	 * 
	 * @param context
	 * @return
	 */
//	public static SetUp loadSetUp(Context context) {
//		SetUp setup = new SetUp();
//		SharedPreferences info = context.getSharedPreferences(
//				ConstantUtil.SETUP, 0);
//		setup.setFirstUse(info.getBoolean("isFirstUse", true));// 是否第一次使用系统,如果是第一次使用需自动发送短信网关去验证并实现自动登陆,自动验证或登陆成功后把
//																// isFirstUse设为false
//		setup.setUse(info.getBoolean("isUse", true));// 是否使用短信换醒程序功能
//		setup.setIsSatart(info.getString("isStart", "1"));// 直接启动还是弹窗确认
//		setup.setSmsContent(info.getString("smsContent", ConstantUtil.YFCLOUD));// 启动短信过滤提醒内容
//		setup.setOpenMeeting(info.getBoolean("isOpenMeeting", true));// 是否开通会议提醒,通知公告提醒
//		setup.setOpenMeetingAlarm(info.getBoolean("isOpenMeetingAlarm", false));// 是否开启默认会前30分钟闹钟提醒
//		setup.setStartActivity(info.getString("startActivity",
//				ConstantUtil.MAIN_ACTIVITY));// 设置登录成功后启动项 默认通讯录
//		setup.setEdu(info.getBoolean("isEdu", true));
//		setup.setDept(info.getBoolean("isDept", true));
//		setup.setTea(info.getBoolean("isTea", true));
//		setup.setStu(info.getBoolean("isStu", true));
//		return setup;
//	}

	/***
	 * 启动项Activity
	 */
	public void doStartActivity() {
//		String activityName = loadSetUp(this.context).getStartActivity();
//		if (activityName.equals(ConstantUtil.MAIN_ACTIVITY)) {
			Intent main = new Intent(this.context, MainActivity.class);
			main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			context.startActivity(main);
//		}
	}

	/***
	 * 正在运行程序
	 */
	public void systemDoing() {
		UserSession session = new UserSession(this.context);
		User user = session.getUser();
		String appName = this.context.getString(R.string.app_name);
		String service = Context.NOTIFICATION_SERVICE;
		NotificationManager main = (NotificationManager) this.context
				.getSystemService(service);
		Notification n = new Notification();
		// // n.icon=R.drawable.icon;//通知图标
		// n.tickerText = appName;//状态栏显示的通知文本提示
		// n.when = System.currentTimeMillis();// 通知产生的时间，会在通知信息里显示
		// //如果想在Ongoing列表项中显示通知，加上这一句就OK ，如果在通知栏里就把下面一句话去掉
		// n.flags=Notification.FLAG_ONGOING_EVENT;
		// 添加声音
		// n.defaults |=Notification.DEFAULT_SOUND;

//		Intent intent = new Intent(this.context, MainActivity.class);
//
//		PendingIntent pi = PendingIntent
//				.getActivity(this.context, 0, intent, 0);
//		n.setLatestEventInfo(this.context, appName, user.getUserName() + "欢迎使用"
//				+ appName, pi);
//		main.notify(ConstantUtil.MAIN_NOTIFICATIONID, n);// 1代表排在第一个当服务停掉可以清
	}

	/***
	 * 获取启动项Activity
	 */
//	public Intent getStartActivity() {
//		Intent intent = null;
//		String activityName = loadSetUp(this.context).getStartActivity();
//		if (activityName.equals(ConstantUtil.MAIN_ACTIVITY)) {
//			intent = new Intent(this.context, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//		}
//		return intent;
//
//	}

	public   boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/***
	 * 系统进度条
	 * @param msg
	 * @return
	 */
	public ProgressDialog showProgressDialog(String msg){
		ProgressDialog progressDialog = ProgressDialog.show(this.context, null,msg, true);
		progressDialog.setIndeterminate(true);//设置进度条是否为不明确
	    progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消 
	    return progressDialog;
	}
	/**
	 * 网络连接异常
	 * @param context
	 */
	public void alertNetError() {
		final Dialog dialog = new Dialog(context,R.style.dialog);
		dialog.setContentView(R.layout.network_set);
		final Button update = (Button)dialog.findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));//网络设置
			}
		});
		final Button close = (Button)dialog.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//context.finish();//退出
			}
		});
		dialog.show();
	}
}
