package com.yifeng.hnqzt.ui;
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.data.UserDAL;
import com.yifeng.hnqzt.entity.User;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.StringHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.*;

/**
 * comment:注册页面
 * @author:吴家宏 
 * Date:2012-10-19
 */
public class RegeditActivity extends BaseActivity implements OnClickListener
{
	private EditText login_id,login_name;
	private Button regedit_btn, backBtn;
	private String userId,userName;
	private UserDAL userDal;
	private Map<String,String> returnMap;
	private ProgressDialog progressDialog ;
	private Spinner spn_area;
    private List<Map<String,String>> areas;
    private String area_id = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regedit);
		spn_area = (Spinner)findViewById(R.id.spn_area);//工作地区
		login_id = (EditText) findViewById(R.id.login_id);
		login_name = (EditText) findViewById(R.id.login_name);
		regedit_btn = (Button) findViewById(R.id.regedit_btn);
		regedit_btn.setOnClickListener(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		userDal=new UserDAL(this);
		showDialog("正在加载所在地点，请稍候...");
		new Thread(runnable).start();
	}
	
	private void initArea(){
		SimpleAdapter adapter=new SimpleAdapter(this, areas,  android.R.layout.simple_spinner_item, new String[]{"name"}, new int[]{android.R.id.text1});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_area.setAdapter(adapter);
		spn_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				area_id = areas.get(arg2).get("id");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.regedit_btn:
			checkReGedit();
			break;
		case R.id.backBtn:
			RegeditActivity.this.finish();
			break;
		default:
			break;
		}
	}
	/***
	 * 验证注册信息
	 */
	private void checkReGedit(){
		userId=login_id.getText().toString().trim();
		userName = login_name.getText().toString().trim();
		if(userId.equals("")){
			commonUtil.shortToast("登录账号不能为空!");
			return;
		}
		if(StringHelper.doConvertEmpty(userId).length()>10){
			commonUtil.shortToast("登录账号过长,请重新输入!");
			return;
		}
		if(userName.equals("")){
			commonUtil.shortToast("请输入姓名!");
			return;
		}
		if(area_id.equals("")){
			commonUtil.shortToast("请选择所在地点！");
			return;
		}
	   showDialog("正在提交注册信息...");
	   new Thread(regeditRun).start();
		
	}
	
	private void showDialog(String msg){
		progressDialog = ProgressDialog.show(this, null,msg, true);
		progressDialog.setIndeterminate(true);//设置进度条是否为不明确
	    progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消 
	}
	
	Runnable regeditRun=new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				returnMap=userDal.doRegedit(userId,"",area_id,userName);
				regeditHandler.sendEmptyMessage(1);
				
			} catch (Exception e) {
				e.printStackTrace();
				regeditHandler.sendEmptyMessage(-1);
			}
		}
	};
	
	
	Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				areas=userDal.doGetAreas();
				regeditHandler.sendEmptyMessage(5);
				
			} catch (Exception e) {
				e.printStackTrace();
				regeditHandler.sendEmptyMessage(-1);
			}
		}
	};
	
	Handler regeditHandler=new Handler(){
	   public void handleMessage(Message msg) {
		   super.handleMessage(msg);
		   if(msg.what==1){
			   showMsg();
		   }
		   if(msg.what==2){
			   //跳到添加简历界面
			   Intent bl=new Intent(RegeditActivity.this,AddResumeActivity.class);
			   bl.putExtra("isAdd", true);
			   bl.putExtra("name", userName);
			   startActivity(bl);
			   RegeditActivity.this.finish();
		   }
		   if(msg.what == 5){
			   if(areas!=null && areas.size()>0){
				   initArea();
			   }
		   }
		   if(msg.what==100){
				  doLogin();
		   }
		   if(progressDialog!=null && progressDialog.isShowing()){
			   progressDialog.dismiss();
			   progressDialog = null;
		   }
	   };	
	};
	
	//显示返回信息
	private void showMsg(){
		   if(returnMap.get("state").equals(String.valueOf(ConstantUtil.SERVER_ERROR))){
			   commonUtil.shortToast("服务器连接超时请重试!或服务器己关闭!");
		   }else if(returnMap.get("state").equals(String.valueOf(ConstantUtil.INNER_ERROR))){
			   commonUtil.shortToast("对不起，系统数据解析异常，请重试!");
		   }else if(returnMap.get("state").equals(String.valueOf(ConstantUtil.LOGIN_FAIL))){
			   commonUtil.shortToast("注册失败!");
		   }else{
			   String flag=returnMap.get("success")==null?"false":returnMap.get("success");
			   if(flag.equals("true")){
				   if(progressDialog!=null && progressDialog.isShowing()){
					   progressDialog.dismiss();
					   progressDialog = null;
				   }
				   //注册成功后执行自动登录
				   commonUtil.shortToast(returnMap.get("msg"));
				   showDialog("正在自动登录验证，请稍候...");
				   new Thread(mRunnable).start();
			   }else{
				   dialogUtil.showMsg("系统提示", returnMap.get("msg"));
			   }
		    }
	}
	
	private User loginUser;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);	    
				loginUser=userDal.loadUser(userId,"123456");
				regeditHandler.sendEmptyMessage(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
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
			ConstantUtil.isLogin=true;//登录状态
			//保存到全局Session
			UserSession session=new UserSession(this);
			session.setUser(loginUser);
			regeditHandler.sendEmptyMessage(2);
	    }else{
	    	dialogUtil.showToast("系统忙,请稍后再试!");
	    }
		
   }

}
