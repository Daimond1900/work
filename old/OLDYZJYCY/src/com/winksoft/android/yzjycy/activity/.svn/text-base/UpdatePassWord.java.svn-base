package com.winksoft.android.yzjycy.activity;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.winksoft.android.yzjycy.CacheData;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.LoginDal;
import com.winksoft.android.yzjycy.entity.User;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

public class UpdatePassWord extends BaseActivity{
	@SetView(id=R.id.topTitle) TextView topTitle;
	@SetView(id=R.id.backBtn,click="onViewClick")Button backBtn;
	@SetView(id=R.id.resetBtn,click="onViewClick")Button resetBtn;
	@SetView(id=R.id.submitBtn,click="onViewClick")Button submitBtn;
	@SetView(id=R.id.oldPwd) EditText oldPwd;
	@SetView(id=R.id.newPwd) EditText newPwd;
	@SetView(id=R.id.newPwd1) EditText newPwd1;
	User user;
	LoginDal loginDal;
	Dialog proDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_password);
		topTitle.setText("密码修改");
		user=CacheData.getUser(this);
		loginDal=new LoginDal(this);
		
	}
	
	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
		if(v.getId()==R.id.backBtn){
			finish();
		}
		if(v.getId()==R.id.submitBtn){
			if(oldPwd.getText().toString().equals("")){
				showDoigMsg("系统提示", "旧密码不能为空,请认真输入!");
				return;
			}
			if(!oldPwd.getText().toString().equals(user.getUserPwd())){
				showDoigMsg("系统提示", "旧密码不正确,请重新输入!");
				return;
			}
			if(newPwd.getText().toString().equals("")){
				showDoigMsg("系统提示", "请输入新密码!");
				return;
			}
			if(newPwd.getText().toString().length()<6){
				showDoigMsg("系统提示", "必须输入6到15位包含字母数字的新密码!");
				return;
			}
			if(newPwd.getText().toString().equals(oldPwd.getText().toString())){
				showDoigMsg("系统提示", "新密码不能与旧密码一样!");
				return;
			}
			
			if(newPwd1.getText().toString().equals("")){
				showDoigMsg("系统提示", "请输入确认密码!");
				return;
			}
			
			if(!newPwd.getText().toString().equals(newPwd1.getText().toString())){
				showDoigMsg("系统提示", "两次密码不一致,请重新输入!");
				return;
			}
			
			proDialog=CustomeProgressDialog.createLoadingDialog(this, "正在处理,请稍后...");
			proDialog.show();
			
			updatePassWord();
		}
		if(v.getId()==R.id.resetBtn){
			oldPwd.setText("");
			newPwd.setText("");
			newPwd1.setText("");
		}
	}
	
	private void updatePassWord(){
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(context,false) {
			
		   @Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				if(proDialog!=null)
					proDialog.dismiss();
			}
		   
		   @Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				boolean isSuccess=false;
				Map<String,String> m=DataConvert.toMap(t+"");
				
				if(m!=null){
					if(m.get("success").equals("true")){
    					isSuccess=true;
    				}
				}
				
				if(proDialog!=null)
					proDialog.dismiss();
				
				if(isSuccess){
					showDoigMsg("系统提示", "密码修改成功,下次登录请使用新密码!");
					//修改本地保存密码
					user.setUserPwd(newPwd.getText().toString());
					CacheData.setUser(UpdatePassWord.this, user);
					
					oldPwd.setText("");
					newPwd.setText("");
					newPwd1.setText("");
					
				}else{
					showDoigMsg("系统提示", "密码修改失败,请重试!");
				}
			}
		};
		loginDal.updatePassWord(callBack, newPwd.getText().toString(),oldPwd.getText().toString(),user.getUserId(),user.getUserName());
	}
}
