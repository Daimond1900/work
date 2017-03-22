package com.winksoft.android.yzjycy.data;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.BaseDAL;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.util.StringUtil;

public class LoginDal extends BaseDAL{
	public LoginDal(Context context) {
		super(context);
	}
	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @param callBack
	 */
	public void login(String username, String password,AjaxCallBack<?>  callBack){
		Map<String , String > map=new HashMap<String, String>();
		map.put("userName", username);
		map.put("userPwd",password);
		post(Constants.IP+"wap/wapuser/doLogin", callBack, map);
//		post(Constants.IP+"android/system/login4Qzy", callBack, map);
	}
	
	/**
	 * 注册
	 * @param userName
	 * @param real_name
	 * @param email
	 * @param password
	 * @param callBack
	 */
	public void createUser(String userName,String real_name,String email,String password,AjaxCallBack<?>  callBack){
		Map<String , String > map=new HashMap<String, String>();
		map.put("username", userName);//登陆用户名
		map.put("real_name", real_name);//真实名称
		map.put("email", email);//邮件地址
		map.put("pwd", StringUtil.getMd5(password));//
		post(Constants.IP+"android/androiduser/createUser", callBack, map);
	}
	
	/***
	 * 密码修改
	 * @param callBack
	 * @param newPwd
	 * @param userId
	 */
	public void updatePassWord(AjaxCallBack<?>  callBack,String newPwd,String oldPwd,String userId,String name){
		Map<String , String > map=new HashMap<String, String>();
		map.put("newPwd", newPwd);
		map.put("oldPwd", oldPwd);
		map.put("id", userId);
		map.put("name", name);
		post(Constants.IP+"android/system/changePwd", callBack, map);
	}
	
	//==========================
	/**
	 * 修改个人信息
	 * @param map
	 * @param callBack
	 */
	public void doUpdate(Map<String , String > map,AjaxCallBack<?>  callBack){
		post(Constants.IP+"android/myinfo/modifyMyInfoDetail", callBack, map);
	}
	
	public void getUserInfo(String card_id,AjaxCallBack<?>  callBack,String key){
		Map<String , String > map=new HashMap<String, String>();
		map.put("card_id", card_id);
		map.put("key", key);
		get(Constants.IP+"android/myinfo/queryMyInfoDetail", callBack, map);
	}
	
	/**
	 * 获取验证码
	 * @param phone
	 * @param uuid
	 * @param callBack
	 */
	public void getCode(String phone ,String uuid, AjaxCallBack<?>  callBack){
		Map<String , String > map=new HashMap<String, String>();
		map.put("phone", phone);
		map.put("uuid", uuid);
		post(Constants.IP+"wap/wapuser/getSmsCode", callBack, map);
	}
	
	
	/**
	 * 注册
	 * @param phone
	 * @param uuid
	 * @param code
	 * @param pwd
	 * @param callBack
	 */
	public void registerUser(String phone ,String code,String pwd, String loginType ,String uuid, AjaxCallBack<?>  callBack){
		Map<String , String > map=new HashMap<String, String>();
		map.put("phone", phone);
		map.put("verifycode", code);
		map.put("pwd", pwd);
		map.put("uuid", uuid);
		map.put("logintype", loginType);
		post(Constants.IP+"wap/wapuser/registerUser", callBack, map);
	}
}
