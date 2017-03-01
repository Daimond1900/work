package com.yifeng.hngly.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import com.yifeng.cloud.entity.User;
import com.yifeng.hngly.ui.set.MyPassword;
import com.yifeng.hngly.util.ActivityStackControlUtil;
import com.yifeng.hngly.util.CommonUtil;
import com.yifeng.hngly.util.ConstantUtil;
import com.yifeng.hngly.util.UserSession;

/***
 * 所有管理类
 * 
 * @author wujiahong 2012-10-11
 */
public class BaseActivity extends Activity {
	public CommonUtil commonUtil;
	public Resources res;
	public Handler BASEHANDLER;
	public User user;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 全屏显示 使应用程序全屏运行，不使用title bar **/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		commonUtil = new CommonUtil(this);
		 UserSession session=new UserSession(this);
		 user=session.getUser();
		
		ActivityStackControlUtil.add(this);
		res = getResources();
		if(!commonUtil.checkNetWork()){
			/*dialogUtil.shortToast("请设置网络连接!");
			Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			startActivity(intent);*/
			commonUtil.alertNetError();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(BASEHANDLER!=null) BASEHANDLER.sendMessage(BASEHANDLER.obtainMessage());
			else{
			 System.gc();  //提醒系统及时回收

			this.finish();
			ActivityStackControlUtil.remove(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.recomd:
			Uri smsToUri = Uri.parse("smsto:");
			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
					smsToUri);
			mIntent.putExtra("sms_body", getString(R.string.app_name) + "下载地址:"
					+ ConstantUtil.downapk);
			startActivity(mIntent);
			break;
		case R.id.editPwd:
			Intent intent = new Intent(this,MyPassword.class);
			startActivity(intent);
			break;
		case R.id.exit:
			this.commonUtil.doExit();
			break;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStackControlUtil.remove(this);
	}
}
