package com.yifeng.hnzpt.ui.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.data.UserDAL;
import com.yifeng.hnzpt.entity.User;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.ui.MainMenuActivity;
import com.yifeng.hnzpt.ui.UserSession;
import com.yifeng.hnzpt.util.DataConvert;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ClassName:AccountActivity 
 * Description：系统设置-帐号管理
 * @author Administrator 
 * Date：2012-10-16
 */
public class AccountActivity extends BaseActivity {
	private EditText old_passwordEdt, new_passwordEdt, confirm_passwordEdt;
	private Button backBtn, submitBtn, resetBtn;
	private ProgressDialog progressDialog;
	private String oldPwd = "", newPwd = "", confirmPwd = "";
	private UserDAL userDAL;
	private String returnJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_account);

		userDAL = new UserDAL(this);

		old_passwordEdt = (EditText) findViewById(R.id.old_passwordEdt);
		new_passwordEdt = (EditText) findViewById(R.id.new_passwordEdt);
		confirm_passwordEdt = (EditText) findViewById(R.id.confirm_passwordEdt);

		MyClick myClick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myClick);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(myClick);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(myClick);

	}

	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				AccountActivity.this.finish();
				break;
			case R.id.submitBtn:
				oldPwd = commonUtil.doConvertEmpty(old_passwordEdt.getText().toString().trim());
				newPwd = commonUtil.doConvertEmpty(new_passwordEdt.getText().toString().trim());
				confirmPwd = commonUtil.doConvertEmpty(confirm_passwordEdt.getText().toString().trim());
				String truePwd = "[a-zA-Z0-9]{1,15}";// 密码正确格式：1-15位英文大小写字母和数字。
				if (oldPwd.equals("")) {
					dialogUtil.shortToast("原密码不能为空!");
				}else if(oldPwd.length() > 6){
					dialogUtil.shortToast("原密码不能大于6位!");
				}else if (newPwd.equals("")) {
					dialogUtil.shortToast("新密码不能为空!");
				}else if(newPwd.length() > 6){
					dialogUtil.shortToast("新密码大于6位!");
				}else if (confirmPwd.equals("")) {
					dialogUtil.shortToast("确认密码不能为空!");
				} else if(confirmPwd.length() > 6){
					dialogUtil.shortToast("确认密码大于6位!");
				}else if (!newPwd.matches(truePwd)) {
					dialogUtil.shortToast("输入的新密码格式不正确！");
				} else if (!confirmPwd.matches(truePwd)) {
					dialogUtil.shortToast("输入的确认密码格式不正确！");
				} else if (!newPwd.equals(confirmPwd)) {
					dialogUtil.shortToast("新密码和确认密码不一致!");
				} else {
					showProg();
					new Thread(uploadRunnable).start();
				}
				break;
			case R.id.resetBtn:
				old_passwordEdt.setText("");
				new_passwordEdt.setText("");
				confirm_passwordEdt.setText("");
				break;
			default:
				break;
			}
		}
	}

	private void showProg() {
		progressDialog = ProgressDialog.show(this, "请稍等...", "正在处理...", true);
	}

	private Runnable uploadRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				/** 上传的参数 */
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", user.getUserId()));
				params.add(new BasicNameValuePair("name", user.getUserName()));
				params.add(new BasicNameValuePair("old_pwd", oldPwd));
				params.add(new BasicNameValuePair("new_pwd", confirmPwd));

				returnJson = userDAL.doUpdatePwd(params, "android/system/changePwd");
				myHandler.sendEmptyMessage(1);
			} catch (InterruptedException e) {
				System.out.println("Error-" + e.getMessage());
			}
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				doEditPwd();
			}
			progressDialog.dismiss();
		}
	};

	private void doEditPwd() {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = DataConvert.toMap(returnJson);
		Boolean isSuccess = Boolean.parseBoolean(returnMap.get("success"));
		String showStr = returnMap.get("msg").toString();

		if (isSuccess == true) {
			setPwd();
			showMsg("提示", showStr);
		} else {
			dialogUtil.showMsg("提示", showStr);
		}
	}
	
	/**
	 * 修改密码成功之后，存入本地缓存，下次登录就不需要手动在去输入新密码
	 */
	private void setPwd(){
		UserSession session = new UserSession(this);
		User user = session.getUser();
		user.setUserPwd(newPwd);
		session.setUser(user);
	}

	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @return
	 */
	private void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(AccountActivity.this, R.style.dialog);
		builder.setContentView(R.layout.dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
				Intent intent = new Intent(AccountActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});
		builder.show();
	}

}
