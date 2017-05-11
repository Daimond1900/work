package com.winksoft.android.yzjycy.ui.system;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.*;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.LoginDal;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;

/**
 * comments:系统设置-帐号管理
 * 
 * @author WuJiaHong Date: 2012-9-24
 */
public class AccountActivity extends BaseActivity {
	private final String TAG = "AccountActivity";
	private EditText old_passwordEdt, new_passwordEdt, confirm_passwordEdt;
	private Button backBtn, submitBtn;
	private CheckBox checkbox;
	private String oldPasswd = "", newPasswd = "", confirmPasswd = "";
	private LoginDal loginDal;
	private UserDAL userDAL;
	private Map<String, String> returnMap;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_system_account);
		loginDal = new LoginDal(this);
		userDAL = new UserDAL(this);

		old_passwordEdt = (EditText) findViewById(R.id.old_passwordEdt);
		new_passwordEdt = (EditText) findViewById(R.id.new_passwordEdt);
		confirm_passwordEdt = (EditText) findViewById(R.id.confirm_passwordEdt);

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AccountActivity.this.finish();
			}
		});

		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkPwd();
			}
		});
	}

	/***
	 * 验证修改密码
	 */
	private void checkPwd() {

		oldPasswd = old_passwordEdt.getText().toString();
		newPasswd = new_passwordEdt.getText().toString();
		confirmPasswd = confirm_passwordEdt.getText().toString();
		if ("".equals(oldPasswd)) {
			commonUtil.shortToast("旧密码不能为空!");
		} else if (!user.getPassWord().equals(oldPasswd)) {
			commonUtil.shortToast("旧密码不正确!");
		} else if (newPasswd.equals("")) {
			commonUtil.shortToast("新密码不能为空!");
		} else if (newPasswd.length() < 6) {
			commonUtil.shortToast("新密码不能小于6位!");
		} else if (!newPasswd.equals(confirmPasswd)) {
			commonUtil.shortToast("新密码与确认密码不一致!");
		} else {

			progressDialog = commonUtil.showProgressDialog("正在处理,请稍等...");
			new Thread(postRunnable).start();
		}
	}

	Runnable postRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				returnMap = userDAL.doUpdatePwd(user.getUserId(), user.getLoginName(), oldPasswd, confirmPasswd);
//				loginDal.updatePassWord(, confirmPasswd, oldPasswd, user.getUserId(), user.getLoginName());
				mhandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
				mhandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (returnMap.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
					if (returnMap.get("success").equals("true")) {// 修改成功设置用户信息

						user.setPassWord(newPasswd);
						// user.setRemeberPwd(checkbox.isChecked());//是否记住密码
						// user.setAutoLogin(checkbox.isChecked());//自动登录

						UserSession session = new UserSession(AccountActivity.this);// 更新本地用户信息
						session.setUser(user);

						// 修改成功后把文本框清掉
						oldPasswd = "";
						newPasswd = "";
						confirmPasswd = "";
						old_passwordEdt.setText("");
						new_passwordEdt.setText("");
						confirm_passwordEdt.setText("");

					}
					commonUtil.shortToast(returnMap.get("msg"));
				} else {
					commonUtil.shortToast("系统忙,请稍后再试!");
				}
			}

			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

}
