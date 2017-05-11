package com.yifeng.hnqzt.ui;

import java.util.Map;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.recruit.RecuitInfoActivity;
import com.yifeng.hnqzt.util.ActivityStackControlUtil;
import com.yifeng.hnqzt.util.AutoUpdate;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.DataConvert;
import com.yifeng.hnqzt.util.DialogUtil;
import com.yifeng.hnqzt.util.HttpDownloader;
import com.yifeng.hnqzt.util.StringHelper;

/**
 * comment:闪屏界面
 * 
 * @author:ZhangYan Date:2012-8-25
 */
public class LoadingActivity extends BaseActivity {
	private ImageView imageView;
	private AnimationDrawable animDrawable;
	private AutoUpdate autoupdate;
	private HttpDownloader loader = null;
	private TextView load_text;
	private DialogUtil dialogUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading);

		autoupdate = new AutoUpdate(this);
		loader = new HttpDownloader();
		// 无状态栏
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		dialogUtil = new DialogUtil(this);

		imageView = (ImageView) findViewById(R.id.frameview);

		// 取得帧动画
		animDrawable = (AnimationDrawable) imageView.getBackground();
		load_text = (TextView) findViewById(R.id.load_text);
		load_text.setText("正在启动中...");

		// 判断是否从管理版进来的
		Intent bl = this.getIntent();
		if (bl.getStringExtra("Auth") != null) {
			if (StringHelper.handlePwd(ConstantUtil.AUTH_KEY).equals(
					bl.getStringExtra("Auth"))) {
				ConstantUtil.ISEJOB = true;
			}
		} else {
			ConstantUtil.ISEJOB = false;
		}

		new Thread(checkRunnable).start();

		// 如果用户勾选自动登录则执行
		if (user.isAutoLogin()) {
			// 执行AutoLoginService 实现自动登录
			startService(new Intent(LoadingActivity.this,
					AutoLoginService.class));
		}

		// goPageHome();

		// 获取手机屏幕分辨率
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// Log.v("屏幕分辨率为","屏幕分辨率为:宽"+dm.widthPixels+" * 高"+dm.heightPixels);

	}

	private boolean checkMobile() {
		TelephonyManager telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (telMgr.getSimState() == telMgr.SIM_STATE_READY) {
			String imsi = telMgr.getSubscriberId();
			if (!imsi.equals("")) {
				// if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				// 中国移动
				// } else if (imsi.startsWith("46001")) {
				// 中国联通
				// }
				if (imsi.startsWith("46003")) {
					// 中国电信
					// checkLogin();
					return true;
				} else {
					dialogUtil.showMsgs("友情提醒", "对不起!非电信用户不能使用该系统!");
					return false;
				}
			} else {
				// checkLogin();
				dialogUtil.showMsg("友情提醒", "imsi获取失败!不能正常使用该系统!");
				return true;
			}
		} else if (telMgr.getSimState() == telMgr.SIM_STATE_ABSENT) {
			dialogUtil.showMsg("友情提醒", "无SIM卡");
			return true;
		} else if (telMgr.getSimState() == telMgr.SIM_STATE_NETWORK_LOCKED) {
			dialogUtil.showMsg("友情提醒", "需要NetWork PIN 解锁");
			return true;
		} else if (telMgr.getSimState() == telMgr.SIM_STATE_PIN_REQUIRED) {
			dialogUtil.showMsg("友情提醒", "需要SIM卡的PIN解锁");
			return true;
		} else if (telMgr.getSimState() == telMgr.SIM_STATE_PUK_REQUIRED) {
			dialogUtil.showMsg("友情提醒", "需要SIM卡的PUK解锁");
			return true;
		} else if (telMgr.getSimState() == telMgr.SIM_STATE_UNKNOWN) {
			dialogUtil.showMsg("友情提醒", "SIM卡状态未知");
			return true;
		}
		return false;
	}

	// 退出
	public void sysExit() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

		ActivityStackControlUtil.finishProgram();// 清除所有activity

		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	private Runnable checkRunnable = new Runnable() {

		@Override
		public void run() {

			try {

				handler.sendEmptyMessage(100);

				Thread.sleep(200);

				String json = "";
				if (autoupdate.isNetworkAvailable(LoadingActivity.this)) {
					json = loader.download(ConstantUtil.downtxt);
				}

				Message msg = new Message();
				msg.what = 1;
				Bundle data = msg.getData();
				data.putString("json", json);
				msg.setData(data);
				handler.sendMessage(msg);

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = -1;
				handler.sendMessage(msg);
			}

		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 100) {
				animDrawable.start();
			}
			if (msg.what == 1) {
				String json = msg.getData().getString("json");
				if (json.equals("error")) {
					load_text.setText("版本检测失败");
					goPageHome();
				} else {
					doCheckSystem(json);// 检测新版本
				}
			}
			if (msg.what == -1) {
				goPageHome();
			}

		}
	};

	/***
	 * 检测试版本
	 */
	private void doCheckSystem(String json) {
		if (!json.equals(null) || !json.equals("")) {
			try {
				Map<String, String> map = DataConvert.toMap(json);
				float webcode = Float.parseFloat(map.get("CODE"));
				float lochostcode = Float.parseFloat(autoupdate.versionName);
				if (webcode > lochostcode) {
					autoupdate.setMsg(map.get("MSG"));
					autoupdate.check();// 下载远程apk文件
				} else {
					goPageHome();
				}
			} catch (Exception e) {
				Log.i("检测试版本", "检测试版本出错!" + e);
				goPageHome();
			}
		}
	}

	/**
	 * 跳转登录
	 */
	private void goPageHome() {
		
//		if(!checkMobile()){
//			
//		}else{
			Intent login = new Intent(LoadingActivity.this, MainActivity.class);
			// login.putExtra("tag","zpxx");
			login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(login);
			load_text.setText("");
			animDrawable.stop();

			this.finish();
//		}
	}

	/**
	 * 返回
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			animDrawable.stop();
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}