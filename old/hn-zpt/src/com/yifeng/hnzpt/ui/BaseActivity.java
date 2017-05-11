package com.yifeng.hnzpt.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Window;

import com.yifeng.hnzpt.entity.User;
import com.yifeng.hnzpt.util.ActivityStackControlUtil;
import com.yifeng.hnzpt.util.CommonUtil;
import com.yifeng.hnzpt.util.DialogUtil;

import dalvik.system.VMRuntime;
/**
 * comment:Actvity父类
 * @author:ZhangYan
 * Date:2012-8-17
 */
public class BaseActivity extends Activity {
	public CommonUtil commonUtil;
	public DialogUtil dialogUtil;
	private TelephonyManager telMgr;
	public Handler BASEHANDLER;
	private String imsi = "";
	private final static float TARGET_HEAP_UTILIZATION = 0.75f;
	public User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 全屏显示 使应用程序全屏运行，不使用title bar **/
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);//优化内存管理
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		commonUtil = new CommonUtil(this,this);
		dialogUtil = new DialogUtil(this);
		
		setUser();
		
		telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
			if (telMgr.getSubscriberId() != null) {
				imsi = telMgr.getSubscriberId();
				user.setImsi(imsi);
			}
		}
		ActivityStackControlUtil.add(this);
		if(!commonUtil.checkNetWork()){
			/*dialogUtil.shortToast("请设置网络连接!");
			Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			startActivity(intent);*/
			dialogUtil.alertNetError();
			return;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(BASEHANDLER!=null){
				BASEHANDLER.sendMessage(BASEHANDLER.obtainMessage());
			}else {
				System.gc();
				this.finish();
				ActivityStackControlUtil.remove(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public String getImsi() {
		return imsi;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStackControlUtil.remove(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUser();
	}
	
	private void setUser(){
		UserSession session = new UserSession(this);
		user = session.getUser();
	}
}
