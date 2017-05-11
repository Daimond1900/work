package com.yifeng.hnjcy.set;

import java.util.HashMap;
import java.util.Map;

import com.yifeng.hnjcy.data.SetDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.DataConvert;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 修改我的密码
 * 
 */
public class MyPassword extends BaseActivity implements OnClickListener {

	private EditText old_password,new_password,affirm_password;
	private String old,new_pw,affirm;
	private Button save_password,reset_password,pwd_back;
	private SetDAL data;
	SharedPreferences userinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password);
        data = new SetDAL(this,new Handler());
        userinfo = getSharedPreferences(ConstantUtil.PWD, 0);
     	old_password=(EditText)findViewById(R.id.old_password);
		new_password=(EditText)findViewById(R.id.new_password);
		affirm_password=(EditText)findViewById(R.id.affirm_password);
    	save_password=(Button)findViewById(R.id.save_password);
    	save_password.setOnClickListener(this);
    	reset_password=(Button)findViewById(R.id.reset_password);
    	reset_password.setOnClickListener(this);
         pwd_back=(Button)findViewById(R.id.back);
         pwd_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyPassword.this.finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.save_password:
			if(checkPassword()){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", user.getUserId());
				map.put("name", user.getUserName());
				map.put("old_pwd", old);
				map.put("new_pwd", new_pw);
				String json=data.doUpdatePwd(map);
				Map<String, String> m = DataConvert.toMap(json);
				if(m != null){
					if(m.get("success")!=null){
						commonUtil.shortToast(m.get("msg"));
						if(m.get("success").equals("true")){
							userinfo.edit().putString("loginPwd", new_pw).commit();
						}
					}else{
						commonUtil.shortToast("网络或服务器异常,修改失败!");
					}
				}else{
					commonUtil.shortToast("网络或服务器异常,修改失败!");
				}
			}
			
			break;
		case R.id.reset_password:
			old_password.setText("");
			new_password.setText("");
			affirm_password.setText("");
			break;
		default:
			break;
		}
	}
	
	
	public boolean checkPassword(){
		old=old_password.getText().toString();
		new_pw=new_password.getText().toString();
		affirm=affirm_password.getText().toString();
		String user_pw = userinfo.getString("loginPwd", "");
		if (old.equals("") ) {
			commonUtil.showMsg("错误", "旧密码不能为空!");
			return false;
		} else if (!old.equals(user_pw)) {
			commonUtil.showMsg("错误", "旧密码输入错误!");
			return false;
		}
		
		if (new_pw.equals("") ) {
			commonUtil.showMsg("错误", "新密码不能为空!");
			return false;
		} else if (new_pw.length() > 20) {
			commonUtil.showMsg("错误", "新密码长度不能大于20!");
			return false;
		}
		
		if (affirm.equals("") ) {
			commonUtil.showMsg("错误", "确认密码不能为空!");
			return false;
		} else if (!affirm.equals(new_pw)) {
			commonUtil.showMsg("错误", "二次密码输入不一致!");
			return false;
		}
		
		return true;
	}
	

}
