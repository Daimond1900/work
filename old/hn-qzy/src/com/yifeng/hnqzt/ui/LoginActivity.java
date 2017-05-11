package com.yifeng.hnqzt.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.data.UserDAL;
import com.yifeng.hnqzt.entity.User;
import com.yifeng.hnqzt.util.ConstantUtil;

/**
 * comment:登录界面
 * @author:吴家宏
 * Date:2012-8-27
 */
public class LoginActivity extends BaseActivity{
	private TextView loadtext;
	private EditText user_name,user_pass;
	private Button login_btn,login_back,backBtn;
	private String name,pwd;
	private UserDAL userDAL;
	private CheckBox remeberPwd,autoLogin;//记住密码，自动登录
	private User loginUser;
	private final int LOGINSTATE=100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userDAL = new UserDAL(this);
		initPage();
	}
	
	private void initPage(){
		
		user_name = (EditText)findViewById(R.id.user_name);
		user_name.setText(user.getUserName());
		user_pass = (EditText)findViewById(R.id.user_pass);
		if(user.isRemeberPwd()){
			user_pass.setText(user.getUserPwd());
		}
		
		loadtext = (TextView)findViewById(R.id.loadtext);
		
		remeberPwd=(CheckBox)findViewById(R.id.remeberPwd);
		remeberPwd.setChecked(user.isRemeberPwd());
		
		autoLogin=(CheckBox)findViewById(R.id.autoLogin);
		autoLogin.setChecked(user.isAutoLogin());
		
		autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					remeberPwd.setChecked(isChecked);
			}
			}
		});
		
		ImageButtonClickListener click = new ImageButtonClickListener();
		login_btn = (Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(click);
		login_back = (Button)findViewById(R.id.login_back);
		login_back.setOnClickListener(click);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(click);
		
		
		
	}
	
	
	
	private class ImageButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn:
				doCheckLogin();
				
				break;
			case R.id.login_back:
				Intent regedit = new Intent(LoginActivity.this,RegeditActivity.class);
				regedit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(regedit);
				LoginActivity.this.finish();
				break;
			case R.id.backBtn:
				LoginActivity.this.finish();
				break;
			default:
				break;
			}
		}
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
		} else if (pwd.length() > 20) {
			dialogUtil.showMsg("错误", "密码不能大于20位!");
		} else {
			
			login_btn.setEnabled(false);
			loadtext.setText("正在验证...");
			new Thread(mRunnable).start();
		}
	}
	
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);	    
				loginUser=userDAL.loadUser(name,pwd);
				mHandler.sendEmptyMessage(LOGINSTATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	};
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==LOGINSTATE){
			  doLogin();
			}
			loadtext.setText("");
			
		}
	};
	
	private void doLogin(){
	    
		if (loginUser.getState() == ConstantUtil.SERVER_ERROR) {
			dialogUtil.showToast("服务器连接超时请重试!或服务器己关闭!");
		}else if (loginUser.getState() == ConstantUtil.INNER_ERROR) {
			dialogUtil.showToast("对不起，系统数据解析异常，请重试!");
		}else if (loginUser.getState() == ConstantUtil.LOGIN_FAIL) {
			dialogUtil.showToast("登录失败，用户名或密码有误请重新输入!");
		}else if(loginUser.getState() == ConstantUtil.WRONG_ID_OR_PWD){
			dialogUtil.showToast(loginUser.getServiceMsg());
		}else if(loginUser.getState()==ConstantUtil.LOGIN_SUCCESS){
			
	    	loginUser.setAutoLogin(autoLogin.isChecked());
			loginUser.setRemeberPwd(remeberPwd.isChecked());
			
			ConstantUtil.isLogin=true;//登录状态
			
			//保存到全局Session
			UserSession session=new UserSession(this);
			session.setUser(loginUser);
			this.finish();
			
	    }else{
	    	dialogUtil.showToast("系统忙,请稍后再试!");
	    }
		
		login_btn.setEnabled(true);
   }
    
	@Override
    protected void onResume() {
    	super.onResume();
    	if(ConstantUtil.isLogin){//如果是登录过的本界面不可访问
    		this.finish();
    	}
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			this.finish();
            //dialogUtil.doAdvanceExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
