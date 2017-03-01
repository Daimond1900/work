package com.yifeng.hnqzt.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.yifeng.hnqzt.entity.User;
/**
 * 用户信息
 * @author WuJiaHong
 *
 */
public class UserSession {
	private Context context;
	public static final String USERINFO = "USERINFO";
	public UserSession(Context context){
		this.context=context;
	}
	
	/**
	 * 昨时存用户
	 * @param user
	 */
	public void setUser(User user){
		SharedPreferences settings = this.context.getSharedPreferences(USERINFO, 0);
		 settings.edit()
				 .putInt("state", user.getState())
				 .putString("key", user.getKey())
				 .putString("mobileNo", user.getMobileNo())
				 .putString("publicKey", user.getPublicKey())
				 .putString("imsi", user.getImsi())
				 .putString("userName", user.getUserName())
				 .putString("userId", user.getUserId())
				 .putString("userPic", user.getUserPic())
				 .putString("userPwd", user.getUserPwd())
				 .putBoolean("isAutoLogin", user.isAutoLogin())
				 .putBoolean("remeberPwd", user.isRemeberPwd())
				 .putString("userPic", user.getUserPic())
				 .putString("title", user.getTitle())
				 .putString("areas", user.getAreas())
				 .putString("deptId", user.getDeptId())
				 .putString("deptName", user.getDeptName())
				 .putString("zbdw", user.getZbdw())
		.commit(); 
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public User getUser(){
		User user=new User();
		SharedPreferences userinfo =context.getSharedPreferences(USERINFO, 0);
	    user.setState(userinfo.getInt("state", 0));
	    user.setKey(userinfo.getString("key", ""));
	    user.setMobileNo(userinfo.getString("mobileNo", ""));
	    user.setPublicKey(userinfo.getString("publicKey", ""));
	    user.setImsi(userinfo.getString("imsi", ""));
	    user.setUserName(userinfo.getString("userName", ""));
	    user.setUserId(userinfo.getString("userId", ""));
	    user.setUserPic(userinfo.getString("userPic", ""));
	    user.setUserPwd(userinfo.getString("userPwd", ""));
	    user.setAutoLogin(userinfo.getBoolean("isAutoLogin", false));
	    user.setRemeberPwd(userinfo.getBoolean("remeberPwd", false));
	    user.setUserPic(userinfo.getString("userPic", ""));
	    user.setTitle(userinfo.getString("title", ""));
	    user.setAreas(userinfo.getString("areas", ""));
	    user.setDeptId(userinfo.getString("deptId", ""));
	    user.setDeptName(userinfo.getString("deptName", ""));
	    user.setZbdw(userinfo.getString("zbdw", ""));
		return user;
	}
}
