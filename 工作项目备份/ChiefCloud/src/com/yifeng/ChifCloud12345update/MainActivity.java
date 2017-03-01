package com.yifeng.ChifCloud12345update;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.yifeng.util.AutoUpdate;
import com.yifeng.util.CommonUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.HttpDownloader;

public class MainActivity extends Activity {
	ProgressBar progressBar;
	private boolean ifc = false;// 是否联网
	private AutoUpdate autoupdate;
	private CommonUtil commonUtil;
	private String versionfile;
	public static boolean iflogin = false;// 是否登录

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (iflogin) {
			Intent main = new Intent(MainActivity.this, MainPageActivity.class);
			startActivity(main);
			this.finish();
		} else {
			progressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
			autoupdate = new AutoUpdate(this);
			commonUtil = new CommonUtil(this);
			// progressBar.setBackgroundColor( 0x176EC8);
			ifc = ifConnect();
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			new Thread(myRunnable).start();
		}
	}

	private Runnable myRunnable = new Runnable() {
		public void run() {
			try {

				progressBar.setMax(100);
				for (int i = 1; i < 101; i++) {
					progressBar.setProgress(i);
					Thread.sleep(10);
				}
				handler.sendMessage(handler.obtainMessage());
			} catch (InterruptedException e) {
			}
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (ifc) {
				doCheckSystem();
			} else {
				commonUtil.showToast("手机未联网,确定联网后重新开启程序。");
			}
		}
	};

	boolean ifConnect() {
		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWorkInfo = cwjManager.getActiveNetworkInfo();
		if (netWorkInfo != null)

			flag = netWorkInfo.isAvailable();
		return flag;
	}

	/***
	 * 检测试版本
	 */
	private void doCheckSystem() {
		if (autoupdate.ifReset()) {

			// autoupdate.delAPK();
			//
			// return;
			ifshow = true;
		}
		HttpDownloader loader = new HttpDownloader();
		// 检测是否有网络
		if (autoupdate.isNetworkAvailable(this) == false) {

			return;
		} else {
			versionfile = getString(R.string.ipconfig) + "android/version/version.txt";
			String json = loader.download(versionfile);
			if (json.equals("error")) {
				this.commonUtil.shortToast("检查更新失败。");
				goLogin();
			} else if (!json.equals(null) || !json.equals("")) {
				Map<String, String> map = DataConvert.toMap(json);
				float webcode = Float.parseFloat(map.get("CODE"));
				float lochostcode = Float.parseFloat(autoupdate.versionName);
				if (webcode > lochostcode) {
					autoupdate.setMsg(map.get("MSG"));

					autoupdate.check();// 下载远程apk文件
				} else {
					goLogin();
				}
			} else {

				goLogin();
			}
		}
	}

	private boolean ifshow = false;

	private void goLogin() {

		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.putExtra("ifshow", ifshow);
		startActivity(intent);
		MainActivity.this.finish();
	}

	/**
	 * 退出
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			commonUtil.doExit();
			return false;

		}
		return false;

	}
}