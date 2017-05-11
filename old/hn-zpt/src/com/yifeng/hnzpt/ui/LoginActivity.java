package com.yifeng.hnzpt.ui;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.data.UserDAL;
import com.yifeng.hnzpt.entity.User;
import com.yifeng.hnzpt.util.ActivityStackControlUtil;
import com.yifeng.hnzpt.util.ConstantUtil;

/**
 * ClassName:LoginActivity Description：用户登录界面
 * 
 * @author Administrator Date：2012-10-22
 */
public class LoginActivity extends BaseActivity {
	private TextView loadtext;
	private EditText user_name, user_pass;
	private Button login_btn, login_back;
	private CheckBox checkbox;// 记住密码
	private String name, pwd;
	private UserDAL userDAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		userDAL = new UserDAL(this);

		initPage();
	}

	private void initPage() {
		user_name = (EditText) findViewById(R.id.user_name);
		user_pass = (EditText) findViewById(R.id.user_pass);
		if (user.isRememberPwd()) {
			user_pass.setText(user.getUserPwd());
			user_name.setText(user.getUserName());
		}
		loadtext = (TextView) findViewById(R.id.loadtext);
		MyClick myclick = new MyClick();
		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(myclick);
		login_back = (Button) findViewById(R.id.login_back);
		login_back.setOnClickListener(myclick);

		checkbox = (CheckBox) findViewById(R.id.checkbox);
		checkbox.setChecked(user.isRememberPwd());
	}

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:
				doCheckLogin();
				break;
			case R.id.login_back:
				dialogUtil.doAdvanceExit();
				break;
			default:
				break;
			}
		}
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
					dialogUtil.showMsg("友情提醒", "对不起!非电信用户不能使用该系统!");
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

	/**
	 * 登陆检测
	 * 
	 * @return
	 */
	private void doCheckLogin() {
		name = commonUtil.doConvertEmpty(user_name.getText().toString());
		pwd = commonUtil.doConvertEmpty(user_pass.getText().toString());
		if (name.equals("") || pwd.equals("")) {
			dialogUtil.showMsg("错误", "账号及密码不能为空请重新输入!");
		} else if (name.length() > 20) {
			dialogUtil.showMsg("错误", "帐号不能大于20位!");
		} else if (pwd.length() > 15) {
			dialogUtil.showMsg("错误", "密码不能大于15位!");
		} else {
			loadtext.setText("正在验证...");
			new Thread(mRunnable).start();
		}
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(300);
				mHandler.sendMessage(mHandler.obtainMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			doLogin();
		}
	};

	private void doLogin() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", name);
		// param.put("pwd", StringHelper.handlePwd(pwd));
		param.put("password", pwd);
		User user = userDAL.loadUser(param);
		if (user.getState() == ConstantUtil.SERVER_ERROR) {
			dialogUtil.showToast("服务器连接超时请重试!或服务器己关闭!");
			loadtext.setText("");
		} else if (user.getState() == ConstantUtil.INNER_ERROR) {
			dialogUtil.showToast("对不起，系统数据解析异常，请重试!");
			loadtext.setText("");
		} else if (user.getState() == ConstantUtil.LOGIN_FAIL) {
			dialogUtil.showToast(user.getCompanyName());
			loadtext.setText("");
		} else {
			loadtext.setText("");
			user.setRememberPwd(checkbox.isChecked());
			// ConstantUtil.isLogin=true;//登录状态
			// 保存到全局Session
			UserSession session = new UserSession(this);
			session.setUser(user);

			loadtext.setText("");

			ConstantUtil.IFLOGIN = true;

			Intent main = new Intent(LoginActivity.this, MainMenuActivity.class);
			main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(main);
			LoginActivity.this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ConstantUtil.ISEJOB) {
				// 如果是管理版进来，直接返回;
				this.finish();
			} else {
				// 直接退出
				dialogUtil.doAdvanceExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
