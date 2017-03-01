package com.yifeng.ChifCloud12345update;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.yifeng.ChifCloud12345.service.SendNotificationService;
import com.yifeng.ChifCloud12345.service.Utils;
import com.yifeng.entity.User;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ActivityStackControlUtil;
import com.yifeng.util.AutoUpdate;
import com.yifeng.util.ConstantUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
	public static final String PWD = "PWD";
	private Button login, logout;
	private EditText login_name, login_pwd;
	private CheckBox reg_check;
	private TextView loadtext;
	private String name, pwd;
	private LoginBiz userBiz;
	private Thread loginThread;
	private String versionfile;
	private Boolean ifshow=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(getIntent().getExtras()!=null)
		ifshow=getIntent().getExtras().getBoolean("ifshow");
		
		if(ifshow!=null&&ifshow){
			new AutoUpdate(this).delAPK();
		}
		
		userBiz = new LoginBiz(this);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);

		logout = (Button) findViewById(R.id.logout);
		logout.setOnClickListener(this);

		loadtext = (TextView) findViewById(R.id.loadtext);
		login_name = (EditText) findViewById(R.id.user_name);
		login_pwd = (EditText) findViewById(R.id.user_pass);
		reg_check = (CheckBox) findViewById(R.id.checkbox);

		SharedPreferences user_n_p = getSharedPreferences(PWD, 0);
		if (user_n_p.getString("loginName", "").equals(""))
			reg_check.setChecked(false);
		else
			reg_check.setChecked(true);
		login_name.setText(user_n_p.getString("loginName", ""));
		login_pwd.setText(user_n_p.getString("loginPwd", ""));
		
		/**
		 * 
		 */
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(LoginActivity.this, "api_key"));
		
	}

	/**
	 * 锟斤拷陆锟斤拷锟�
	 * 
	 * @return
	 */
	private void doCheckLogin() {
		name = commonUtil.doConvertEmpty(login_name.getText().toString());
		pwd = commonUtil.doConvertEmpty(login_pwd.getText().toString());
		if (name.equals("") || pwd.equals("")) {
			commonUtil.showMsg("为空提示", "用户和密码不能为空!");
		} else if (name.length() > 20) {
			commonUtil.showMsg("长度提示", "名称不能大于20位!");
		} else if (pwd.length() > 20) {
			commonUtil.showMsg("长度提示", "密码不能大于20位!");
		} else {
			// doLogin();
			loadtext.setText("正在登陆中...");
			loginThread = new Thread(mRunnable);
			loginThread.start();
		}
	}

	/**
	 * 锟斤拷锟矫碉拷陆
	 */
	private Runnable mRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				mHandler.sendMessage(mHandler.obtainMessage());
			} catch (InterruptedException e) {
				// System.out.println("Error-"+e.getMessage());
			}
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			doLogin();
			//loginThread.stop();
		}
	};

	private void doLogin() {
		User user = new User();
		user.setUserName(name);
		user.setUserPwd(pwd);
		user.setImsi(this.getImsi());
		int ret = userBiz.doLogin(user);
		if (ret == ConstantUtil.SERVER_ERROR) {
			commonUtil.showToast("服务器异常！");
			loadtext.setText("");
		} else if (ret == ConstantUtil.LOGIN_FAIL) {
			commonUtil.showToast("key找不到");
			loadtext.setText("");
		} else if (ret == ConstantUtil.LOGIN_FAIL1) {
			commonUtil.showToast("key异常");
			loadtext.setText("");
		} else if (ret == ConstantUtil.INNER_ERROR) {
			commonUtil.showToast("密钥错误！");
			loadtext.setText("");
		} else {
			if (reg_check.isChecked()) {
				doRememberPwd();// 记住密码
			} else {
				doRemovePwd();
			}
			// 锟斤拷锟斤拷service
//			statrMyService();
			loadtext.setText("");
			Intent main = new Intent(LoginActivity.this, MainPageActivity.class);
			MainActivity.iflogin=true;
			startActivity(main);
			dosetUser();
			LoginActivity.this.finish();
		}
	}

	/**
	 * 记住密码do
	 * 
	 */
	private void doRememberPwd() {
		SharedPreferences rmd = getSharedPreferences(PWD, 0);
		rmd.edit().putString("loginName", login_name.getText().toString())
				.putString("loginPwd", login_pwd.getText().toString()).commit();
	}
	
	/**
	 * 记录用户的登陆状态
	 */
	private void dosetUser(){
		SharedPreferences rmd = getSharedPreferences("loginstate", 0);
		rmd.edit().putString("loginName", login_name.getText().toString()).commit();
	}

	/**
	 * 锟斤拷锟斤拷住锟斤拷锟斤拷
	 * 
	 */
	private void doRemovePwd() {
		SharedPreferences rmd = getSharedPreferences(PWD, 0);
		rmd.edit().putString("loginName", "").putString("loginPwd", "")
				.commit();
	}

//	/***
//	 * 锟斤拷锟斤拷Service statrMyService()
//	 */
//	private void statrMyService() {
//		// 锟斤拷锟斤拷通知
//		Intent startTongZi = new Intent(LoginActivity.this,
//				SendNotificationService.class);
//		startService(startTongZi);
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			doCheckLogin();
			break;
		case R.id.logout:
			Intent startTongZi=new Intent(LoginActivity.this,SendNotificationService.class);
			LoginActivity.this.stopService(startTongZi);
		    
		    //取锟斤拷通知锟斤拷
		    String service = Context.NOTIFICATION_SERVICE;
		    NotificationManager nm=(NotificationManager) LoginActivity.this.getSystemService(service); 
		    nm.cancel(1);
		    nm.cancel(2);
			ActivityStackControlUtil.finishProgram();//锟剿筹拷
			LoginActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
