package com.yifeng.ChifCloud12345update;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.android.common.logging.Log;
import com.yifeng.ChifCloud12345.video.VideoList;
import com.yifeng.entity.User;
import com.yifeng.manager.LoginBiz;
import com.yifeng.microalloy.InteractiveActivty;
import com.yifeng.util.ActivityStackControlUtil;
import com.yifeng.util.CommonUtil;
import com.yifeng.util.ConstantUtil;

public class BaseActivity extends Activity {
	public Button bt_bottom_menu1, bt_bottom_menu2, bt_bottom_menu3,
			bt_bottom_menu4, bt_bottom_menu5,tabs_menu1,tabs_menu2,tabs_menu3;
	public LinearLayout top;
	public CommonUtil commonUtil;
	private TelephonyManager telMgr;
	public User user;
	private String imsi = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示 使应用程序全屏运行，不使用title bar */
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		commonUtil = new CommonUtil(this);
		user = LoginBiz.loadUser(this);
		telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (telMgr.getSimState() == telMgr.SIM_STATE_READY) {
			if (telMgr.getSubscriberId() != null) {
				imsi = telMgr.getSubscriberId();
			}
		}
		ActivityStackControlUtil.add(this);

		if (!commonUtil.checkNetWork()) {
			// 您手机未能联网或数开关己关闭,跳回登陆界面
			Intent error = new Intent(BaseActivity.this, ErrorActivity.class);
			startActivity(error);
		}
	}

	public String getImsi() {
		return imsi;
	}
	
	public void initBottom() {
		bt_bottom_menu1 = (Button) findViewById(R.id.bt_bottom_menu1);
		bt_bottom_menu2 = (Button) findViewById(R.id.bt_bottom_menu2);
		bt_bottom_menu3 = (Button) findViewById(R.id.bt_bottom_menu3);
		bt_bottom_menu4 = (Button) findViewById(R.id.bt_bottom_menu4);
		bt_bottom_menu5 = (Button) findViewById(R.id.bt_bottom_menu5);
	}
	
	public void initTabs(){
		tabs_menu1 = (Button) findViewById(R.id.tabs_menu1);
		tabs_menu2  = (Button) findViewById(R.id.tabs_menu2);
		tabs_menu3  = (Button) findViewById(R.id.tabs_menu3);
	}
	
	public void setTabsFocus(Button menu, int drawable) {
		menu.setEnabled(false);
		menu.setBackgroundResource(drawable);
		// 未派
		tabs_menu1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						WeiPaiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 已派
		tabs_menu2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						YiPaiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 已办结
				tabs_menu3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(BaseActivity.this,
								AlreadyActivity.class);
						startActivity(intent);
						finish();
					}
				});
	}

	public void setFocus(Button menu, int drawable) {
		menu.setEnabled(false);
		menu.setBackgroundResource(drawable);
		// 热线点评
		bt_bottom_menu1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 热线待办
		bt_bottom_menu2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						MyWorkActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 网上互动
		bt_bottom_menu3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						InteractiveActivty.class);
				startActivity(intent);
				finish();
			}
		});
		// 首页
		bt_bottom_menu4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						MainPageActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 寄语市长
		bt_bottom_menu5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this,
						MayorActivity.class);
				startActivity(intent);
				finish();
			}
		});
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
		case R.id.go_login:
			// 取消通知栏
			String service = Context.NOTIFICATION_SERVICE;
			NotificationManager nm = (NotificationManager) BaseActivity.this
					.getSystemService(service);
			nm.cancel(1);
			nm.cancel(2);
			Intent bl = new Intent(BaseActivity.this, LoginActivity.class);
			startActivity(bl);
			this.finish();
			break;
		case R.id.systemExit:
			commonUtil.doExit();
			break;
		case R.id.editpwd:
			editPwd();
			break;

		}
		return false;
	}
	
	
	private void editPwd() {

		final Dialog dialoghelp = new Dialog(this, R.style.dialog);

		dialoghelp.setContentView(R.layout.editpwd);
		final EditText old_pwd = (EditText) dialoghelp
				.findViewById(R.id.old_pwd);
		final EditText new_pwd1 = (EditText) dialoghelp
				.findViewById(R.id.new_pwd1);
		final EditText new_pwd2 = (EditText) dialoghelp
				.findViewById(R.id.new_pwd2);
		old_pwd.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		Editable etable = old_pwd.getText();
		Selection.setSelection(etable, etable.length());
		new_pwd1.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etable = new_pwd1.getText();
		Selection.setSelection(etable, etable.length());
		new_pwd2.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etable = new_pwd2.getText();
		Selection.setSelection(etable, etable.length());
		Button btnCancel = (Button) dialoghelp.findViewById(R.id.submit);
		final CheckBox checkbox = (CheckBox) dialoghelp
				.findViewById(R.id.checkbox);

		dialoghelp.setCancelable(true);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (checkbox.isChecked()) {
					old_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					Editable etable = old_pwd.getText();
					Selection.setSelection(etable, etable.length());
					new_pwd1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					etable = new_pwd1.getText();
					Selection.setSelection(etable, etable.length());
					new_pwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					etable = new_pwd2.getText();
					Selection.setSelection(etable, etable.length());

				} else {
					old_pwd.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					Editable etable = old_pwd.getText();
					Selection.setSelection(etable, etable.length());
					new_pwd1.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					etable = new_pwd1.getText();
					Selection.setSelection(etable, etable.length());
					new_pwd2.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					etable = new_pwd2.getText();
					Selection.setSelection(etable, etable.length());
				}
			}
		});

		// TextView textView01 = (TextView)
		// dialoghelp.findViewById(R.id.TextView01);

		// textView01.setText(content + content + content);

		btnCancel.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View view) {
				dialoghelp.cancel();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(old_pwd.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(new_pwd1.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(new_pwd2.getWindowToken(), 0);
				doCheckLogin(new_pwd1.getText().toString(), new_pwd2.getText().toString(), old_pwd.getText().toString());
			}
		});
		dialoghelp.show();
	}

	/**
	 * 登陆检测
	 * 
	 * @return
	 */
	String new_pwd1, new_pwd2, old_pwd;

	private void doCheckLogin(String new_pwd1, String new_pwd2, String old_pwd) {
		if (new_pwd1.equals("") || new_pwd1.equals("") || old_pwd.equals("")) {
			commonUtil.showMsg("错误", "密码不能为空!");
		} else if (!new_pwd2.equals(new_pwd1)) {
			commonUtil.showMsg("错误", "两次输入的密码不一致");
		} else if (new_pwd2.length() > 20) {
			commonUtil.showMsg("错误", "密码不能大于20位!");
		}else if (new_pwd2.equals(old_pwd)) {
			commonUtil.showMsg("错误", "修改密码与原始密码一样。");
		}  else {
			this.new_pwd1 = new_pwd1;
			this.new_pwd2 = new_pwd2;
			this.old_pwd = old_pwd;
			new Thread(mRunnable).start();
		}
	}

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
			sendEdit();
		}
	};

	private void sendEdit() {
		int ret = new LoginBiz(this).editPwd(this.user.getUserId(), old_pwd ,
				new_pwd1);
		if (ret == ConstantUtil.SERVER_ERROR) {
			commonUtil.showToast("服务器连接超时请重试!或服务器己关闭!");

		} else if (ret == 1) {
			commonUtil.showToast("密码修改成功!");

		} else if (ret == 0) {
			commonUtil.showToast("旧密码错误，修改失败!");
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityStackControlUtil.remove(this);

	}

}
