package com.yifeng.qzt.ui.system;

import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.yifeng.hnjcy.data.UserDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.MD5;
import com.yifeng.hnjcy.util.UserSession;

/**
 * comments:系统设置-帐号管理
 * 
 * @author WuJiaHong Date: 2012-9-24
 */
public class AccountActivity extends BaseActivity {
	private EditText old_passwordEdt, new_passwordEdt, confirm_passwordEdt;
	private Button backBtn, submitBtn, resetBtn;
	private CheckBox checkbox;
	private String oldPasswd = "", newPasswd = "", confirmPasswd = "";
	private UserDAL userDal;
	private Map<String, String> returnMap;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_account);
		userDal = new UserDAL(this, new Handler());

		old_passwordEdt = (EditText) findViewById(R.id.old_passwordEdt);
		new_passwordEdt = (EditText) findViewById(R.id.new_passwordEdt);
		confirm_passwordEdt = (EditText) findViewById(R.id.confirm_passwordEdt);

		// checkbox=(CheckBox)findViewById(R.id.checkbox);

		backBtn = (Button) findViewById(R.id.backBtn);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				old_passwordEdt.setText("");
				new_passwordEdt.setText("");
				confirm_passwordEdt.setText("");
				old_passwordEdt.requestFocus();
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		} else if (!user.getPwd().equals(oldPasswd)) {
			commonUtil.shortToast("旧密码不正确!");
		} else if (newPasswd.equals("")) {
			commonUtil.shortToast("新密码不能为空!");
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
				returnMap = userDal.doUpdatePwd(
						MD5.getMD5(old_passwordEdt.getText().toString()),
						MD5.getMD5(new_passwordEdt.getText().toString()),
						AccountActivity.this.user.getUserId());
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
				if (returnMap.get("state").equals(
						String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
					if (returnMap.get("success").equals("true")) {// 修改成功设置用户信息
						old_passwordEdt.setText("");
						new_passwordEdt.setText("");
						confirm_passwordEdt.setText("");

						user.setUserPwd(newPasswd);
						// user.setRemeberPwd(checkbox.isChecked());//是否记住密码
						// user.setAutoLogin(checkbox.isChecked());//自动登录

						UserSession session = new UserSession(
								AccountActivity.this);// 更新本地用户信息
						session.setUser(user);
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
