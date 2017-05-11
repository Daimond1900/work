package com.yifeng.hnjcy.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.qzt.widget.AuthLogin;

/***
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button loginBtn, exit_btn;
	private EditText userName, passWord;
	private String name = "", pwd = "";
	private TextView loadtext,jzmm;
	public AuthLogin authLogin;
	private CheckBox checkbox;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		authLogin = new AuthLogin(this);
		authLogin.handler = this.mHandler;

		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		userName = (EditText) findViewById(R.id.user_name);
		passWord = (EditText) findViewById(R.id.user_pass);
		loadtext = (TextView) findViewById(R.id.loadtext);
		jzmm= (TextView) findViewById(R.id.jzmm);
		exit_btn = (Button) findViewById(R.id.login_back);
		exit_btn.setOnClickListener(this);
		SharedPreferences userinfo = getSharedPreferences(ConstantUtil.PWD, 0);
		userName.setText(userinfo.getString("loginName", ""));
		passWord.setText(userinfo.getString("loginPwd", ""));
	}

	/**
	 * 启动云端登陆线程
	 * 
	 * @return
	 */
	private void doCheckLogin() {
		name = commonUtil.doConvertEmpty(userName.getText().toString());
		pwd = commonUtil.doConvertEmpty(passWord.getText().toString());
		if (name.equals("") || pwd.equals("")) {
			commonUtil.showMsg("错误", "用户名及密码不能为空请重新输入!");
		} else if (name.length() > 20) {
			commonUtil.showMsg("错误", "用户名不能大于20位!");
		} else if (pwd.length() > 20) {
			commonUtil.showMsg("错误", "密码不能大于20位!");
		} else {
			loginBtn.setEnabled(false);
			loadtext.setText("正在登陆认证,请稍后...");

			// Intent bl=new Intent(LoginActivity.this,MainActivity.class);
			// startActivity(bl);
			// LoginActivity.this.finish();
			// 开始登陆认证
			authLogin.doAuthUser(name, pwd, AuthLogin.LOGINAUTH);

		}

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == AuthLogin.HANDLER_MSG) {
				authLogin.goToSystem();
				if (checkbox.isChecked())
					authLogin.doRememberPwd();
				else
					authLogin.doRemovePwd();
				// startActivity(new
				// Intent(LoginActivity.this,MainActivity.class));
				// authLogin.loginActivity();//登陆返回状态
				loadtext.setText("");
			} else if (msg.what == ConstantUtil.LOGIN_FAIL) {
				loadtext.setText(authLogin.user.getMsg());
				loginBtn.setEnabled(true);
				if (authLogin.user == null) {
					commonUtil.showToast("系统认证失败!");
					return;
				} else if (authLogin.user.getState() == ConstantUtil.SERVER_ERROR) {
					commonUtil.showToast("服务器连接超时请重试!或服务器己关闭!");
					return;

				} else if (authLogin.user.getState() == ConstantUtil.INNER_ERROR) {
					commonUtil.showToast("对不起，系统数据解析异常，请重试!");
					return;

				}
				commonUtil.shortToast("登陆失败!");
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			doCheckLogin();
			break;
		case R.id.login_back:
			this.commonUtil.doExit();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ConstantUtil.ISEJOB) {// 如果是管理版进来，直接返回;
				this.finish();
			} else {
				this.commonUtil.doExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
