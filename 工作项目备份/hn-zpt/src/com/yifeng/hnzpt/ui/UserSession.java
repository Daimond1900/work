package com.yifeng.hnzpt.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.yifeng.hnzpt.entity.User;

/**
 * 用户信息
 * @author WuJiaHong
 * 
 */
public class UserSession {
	private Context context;
	public static final String USERINFO = "USERINFO";

	public UserSession(Context context) {
		this.context = context;
	}
	
	public void setMaps(String companyName,String companyAddress,String telNo){
		SharedPreferences settings = this.context.getSharedPreferences("maps", 0);
		settings.edit().putString("companyName",companyName)
		.putString("companyAddress", companyAddress).putString("telNo", telNo).commit();
	}
	
	
	public String[] getMaps(){
		SharedPreferences settings = this.context.getSharedPreferences("maps", 0);
		return new String[]{settings.getString("companyName", ""),
				settings.getString("companyAddress", "")
				,settings.getString("telNo", "")};
	}

	/**
	 * 昨时存用户
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		SharedPreferences settings = this.context.getSharedPreferences(USERINFO, 0);
		settings.edit().putInt("state", user.getState()).
		putString("imsi", user.getImsi()).putString("key", user.getKey()).
		putString("mobileNo", user.getMobileNo()).
		putString("publicKey", user.getPublicKey()).
		putString("userId", user.getUserId()).
		putString("userName", user.getUserName()).
		putString("userPwd", user.getUserPwd()).
		putBoolean("rememberPwd", user.isRememberPwd()).
		putString("companyId", user.getCompanyId()).
		putString("companyName", user.getCompanyName()).
		putString("companyManager", user.getcompanyManager()).
		putString("longitude", user.getLongitude()).
		putString("latitude", user.getLatitude()).
		putString("linkPhone", user.getLinkPhone()).
		putString("linkAddress", user.getLinkAddress()).
		putString("title", user.getTitle()).
		putString("areas", user.getAreas()).
		putString("deptId", user.getDeptId()).
		putString("deptName", user.getDeptName()).
		putString("zbdw", user.getZbdw()).
		commit();
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public User getUser() {
		User user = new User();
		SharedPreferences userinfo = context.getSharedPreferences(USERINFO, 0);
		user.setState(userinfo.getInt("state", 0));
		user.setPublicKey(userinfo.getString("imsi", ""));
		user.setKey(userinfo.getString("key", ""));
		user.setMobileNo(userinfo.getString("mobileNo", ""));
		user.setPublicKey(userinfo.getString("publicKey", ""));
		user.setUserId(userinfo.getString("userId", ""));
		user.setUserName(userinfo.getString("userName", ""));
		user.setUserPwd(userinfo.getString("userPwd", ""));
		user.setRememberPwd(userinfo.getBoolean("rememberPwd", false));
		user.setCompanyId(userinfo.getString("companyId", ""));
		user.setCompanyName(userinfo.getString("companyName", ""));
		user.setcompanyManager(userinfo.getString("companyManager", ""));
		user.setLongitude(userinfo.getString("longitude", ""));
		user.setLatitude(userinfo.getString("latitude", ""));
		user.setLinkPhone(userinfo.getString("linkPhone", ""));
		user.setLinkAddress(userinfo.getString("linkAddress", ""));
		user.setTitle(userinfo.getString("title", ""));
		user.setAreas(userinfo.getString("areas", ""));
	    user.setDeptId(userinfo.getString("deptId", ""));
	    user.setDeptName(userinfo.getString("deptName", ""));
	    user.setZbdw(userinfo.getString("zbdw", ""));
		return user;
	}
}
