package com.yifeng.hngly.ui;

import java.util.Map;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.yifeng.hngly.data.LoginuserDAL;
import com.yifeng.hngly.ui.ldlxx.ParseData;
import com.yifeng.hngly.util.AutoUpdate;
import com.yifeng.hngly.util.ConstantUtil;
import com.yifeng.hngly.util.DataConvert;
import com.yifeng.hngly.util.HttpDownloader;
import com.yifeng.hngly.util.StringHelper;

/**
 * 闪屏界面
 * 
 * @author wujiahong 2012-10-11
 */
public class LoadingActivity extends BaseActivity {
	private AutoUpdate autoupdate;
	ImageView frameview;
	AnimationDrawable animDrawable;
	private HttpDownloader loader = null;
	Handler h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		// 判断是否从管理版进来的
		Intent bl = this.getIntent();
		if (bl.getStringExtra("Auth") != null) {
			if (StringHelper.handlePwd(ConstantUtil.AUTH_KEY).equals(
					bl.getStringExtra("Auth"))) {
				ConstantUtil.ISEJOB = true;
			}

		} else
			ConstantUtil.ISEJOB = false;
		if (ConstantUtil.IFLOGIN) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
			return;
		}
		h = new Handler();
		frameview = (ImageView) findViewById(R.id.frameview);
		frameview.setBackgroundResource(R.anim.frameview);
		autoupdate = new AutoUpdate(this);
		loader = new HttpDownloader();
//		new loadSelect().execute();

		animDrawable = (AnimationDrawable) frameview.getBackground();
		frameview.post(new Runnable() {
			@Override
			public void run() {
				animDrawable.start();
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(100);
					Message msg = new Message();
					
					String json = "";
					if (autoupdate.isNetworkAvailable(LoadingActivity.this)) {
						json = loader.download(ConstantUtil.downtxt);
					
					}  
					msg.what = 1;
					Bundle data = msg.getData();
					data.putString("json", json);
					msg.setData(data);
					mhander.sendMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
					mhander.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	Handler mhander = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (msg.what == 1) {
					String json = msg.getData().getString("json");
					if (json.equals("error")) {
						goPageHome();
					} else {
						doCheckSystem(json);// 检测新版本
					}
				}
				if (msg.what == -1) {
					goPageHome();
				}

			}
//			else if (msg.what == 2) {
//				Builder b = new AlertDialog.Builder(LoadingActivity.this)
//						.setTitle("系统提示").setMessage("没有可用网络，是否对网络进行设置？");
//				b.setPositiveButton("设置",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								Intent mIntent = new Intent("/");
//								ComponentName comp = new ComponentName(
//										"com.android.settings",
//										"com.android.settings.WirelessSettings");
//								mIntent.setComponent(comp);
//								mIntent.setAction("android.intent.action.VIEW");
//								startActivityForResult(mIntent, 11); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
//								// LoadingActivity.this.finish();
//							}
//						})
//						.setNeutralButton("取消",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										dialog.cancel();
//										LoadingActivity.this.finish();
//									}
//								}).show();
//			}

		};
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
		Intent login = new Intent(LoadingActivity.this, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(login);
		this.finish();
	}

	void initAllselect() {
		if (ParseData.IFINIT)
			return;
		ParseData.IFINIT = true;
		LoginuserDAL addressDAL = new LoginuserDAL(AppContext.get(), h);
		Map<String, String> rejson = addressDAL.initAllOptions();
		ParseData.initDate(rejson);
	}

	class loadSelect extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			initAllselect();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 11) {
//			if (autoupdate.isNetworkAvailable(LoadingActivity.this)) {
				goPageHome();
//			} else {
//				LoadingActivity.this.finish();
//			}
		}

	}
}
