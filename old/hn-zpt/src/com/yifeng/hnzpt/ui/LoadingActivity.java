package com.yifeng.hnzpt.ui;

import java.util.Map;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.util.AutoUpdate;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;
import com.yifeng.hnzpt.util.HttpDownloader;
import com.yifeng.hnzpt.util.StringHelper;

/**
 * comment:闪屏界面
 * @author:ZhangYan 
 * Date:2012-8-25
 */
public class LoadingActivity extends BaseActivity
{
	private ImageView imageView;
	private AnimationDrawable animDrawable;
	private AutoUpdate autoupdate;
	private HttpDownloader loader = null;
	private TextView load_text;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		// 判断是否从管理版进来的
		Intent bl = this.getIntent();
		if (bl.getStringExtra("Auth") != null) {
			if (StringHelper.handlePwd(ConstantUtil.AUTH_KEY).equals(
					bl.getStringExtra("Auth"))) {
				ConstantUtil.ISEJOB = true;
			} 
				
		}else ConstantUtil.ISEJOB = false;
		
		//如果已登录直接到主界面
		if(ConstantUtil.IFLOGIN){
			Intent mian=new Intent(LoadingActivity.this,MainMenuActivity.class);
			mian.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mian);
			this.finish();
			return;
			
		}
				
		autoupdate = new AutoUpdate(this);
		loader = new HttpDownloader();

		imageView = (ImageView) findViewById(R.id.frameview);
		// 取得帧动画
		animDrawable = (AnimationDrawable) imageView.getBackground();

		load_text = (TextView) findViewById(R.id.load_text);
		new Thread(checkRunnable).start();

	}
	


	
	

	private Runnable checkRunnable = new Runnable()
	{
		@Override
		public void run()
		{

			try
			{

				handler.sendEmptyMessage(100);
				Thread.sleep(500);

				String json = "";
				if (autoupdate.isNetworkAvailable(LoadingActivity.this))
				{
					json = loader.download(ConstantUtil.downtxt);
				}

				Message msg = new Message();
				msg.what = 1;
				Bundle data = msg.getData();
				data.putString("json", json);
				msg.setData(data);
				handler.sendMessage(msg);

			} catch (InterruptedException e)
			{
				e.printStackTrace();
				handler.sendEmptyMessage(-1);
			}

		}
	};

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			if (msg.what == 100)
			{
				animDrawable.start();
				load_text.setText("正在启动中...");
			}
			if (msg.what == 1)
			{
				String json = msg.getData().getString("json");
				if (json.equals("error"))
				{
					load_text.setText("版本检测失败");
					goPageHome();
				} else
				{
					doCheckSystem(json);// 检测新版本
				}
			}
			if (msg.what == -1)
			{
				goPageHome();
			}

		}
	};

	/***
	 * 检测试版本
	 */
	private void doCheckSystem(String json)
	{
		if (!json.equals(null) || !json.equals(""))
		{
			try
			{
				Map<String, String> map = DataConvert.toMap(json);
				float webcode = Float.parseFloat(map.get("CODE"));
				float lochostcode = Float.parseFloat(autoupdate.versionName);
				if (webcode > lochostcode)
				{
					autoupdate.setMsg(map.get("MSG"));
					autoupdate.check();// 下载远程apk文件
				} else
				{
					goPageHome();
				}
			} catch (Exception e)
			{
				Log.i("检测试版本", "检测试版本出错!" + e);
				goPageHome();
			}
		}
	}

	/**
	 * 跳转登录
	 */
	private void goPageHome()
	{
		//Intent login = new Intent(LoadingActivity.this, LoginActivity.class);
		Intent login = new Intent(LoadingActivity.this, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(login);

		load_text.setText("");
		animDrawable.stop();

		this.finish();
	}

	/**
	 * 返回
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}