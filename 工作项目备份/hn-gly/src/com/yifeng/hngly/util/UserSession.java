package com.yifeng.hngly.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yifeng.cloud.entity.User;

/**
 * 用户信息
 * 
 * @author WuJiaHong
 * 
 */
public class UserSession {
	private Context context;
	public static final String USERINFO = "USERINFO";

	public UserSession(Context context) {
		this.context = context;
	}

	/**
	 * 昨时存用户
	 * 
	 * @param user
	 */
	public void setUser(User user) {

		SharedPreferences settings = this.context.getSharedPreferences(
				USERINFO, 0);
		settings.edit().putString("aaa021", user.getAaa021())
				.putString("aaa020", user.getAaa020())
				.putString("zzs051", user.getZzs051())
				.putString("org", user.getOrg())
				.putString("operatorname", user.getOperatorname())
				.putString("userid", user.getUserid())
				.putString("userId", user.getUserId())
				.putString("userName", user.getUserName())
				.putString("userPwd", user.getUserPwd())
				.putString("departmentid", user.getDepartmentid())
				.putString("userTel", user.getUserTel())
				.putString("userGrade", user.getUserGrade())
				.putString("cardId", user.getCardId())
				.putInt("state", user.getState())
				.putString("key", user.getKey())
				.putString("userRole", user.getUserRole())
				.putString("MOBLIE_NO", user.getMobileNo())
				.putString("COMPANY_NAME", user.getCompanyName())
				.putString("PIC_PATH", user.getPic_path())
				.putString("EMAIL", user.getUserEmail())
				.putString("ADDRESS", user.getAddress())
				.putString("DUTY", user.getDuty())
				.putString("pwd", user.getPwd())
				.putString("ehome", user.getEhome())
				.putString("card_style", user.getCard_style())
				.putString("card_style", user.getCard_style())
				.putString("oaIp", user.getOaIp() + "/")
				.putString("group", user.getGroup())
				.putString("group_id", user.getGroup_id())
				.putString("group_name", user.getGroup_name())
				.putString("logo_path", user.getLogo_path())
				.putBoolean("hasOa", user.isHasOa())
				.putInt("isLocked", user.getIsLocked())
				.putInt("group_locked", user.getGroup_locked())
				.putInt("card_locked", user.getCard_locked())
				.putBoolean("success", user.isSuccess())
				.putString("msg", user.getMsg())
				.putString("zbdw", user.getZbdw()).commit();
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public User getUser() {
		User user = new User();
		SharedPreferences userinfo = context.getSharedPreferences(USERINFO, 0);
		user.setUserId(userinfo.getString("userId", ""));
		user.setUserName(userinfo.getString("userName", ""));
		user.setUserPwd(userinfo.getString("userPwd", ""));
		user.setDepartmentid(userinfo.getString("departmentid", ""));
		user.setUserGrade(userinfo.getString("userGrade", ""));
		user.setUserRole(userinfo.getString("userRole", ""));
		user.setUserTel(userinfo.getString("userTel", ""));
		user.setState(userinfo.getInt("state", 0));
		user.setCardId(userinfo.getString("cardId", ""));
		user.setKey(userinfo.getString("key", ""));
		user.setMobileNo(userinfo.getString("MOBLIE_NO", ""));
		user.setCompanyName(userinfo.getString("COMPANY_NAME", ""));
		user.setAddress(userinfo.getString("ADDRESS", ""));
		user.setPic_path(userinfo.getString("PIC_PATH", ""));
		user.setUserEmail(userinfo.getString("EMAIL", ""));
		user.setDuty(userinfo.getString("DUTY", ""));
		user.setPwd(userinfo.getString("pwd", ""));
		user.setEhome(userinfo.getString("ehome", ""));
		user.setCard_style(userinfo.getString("card_style", ""));
		user.setOaIp(userinfo.getString("oaIp", ""));
		user.setGroup(userinfo.getString("group", ""));
		user.setGroup_id(userinfo.getString("group_id", ""));
		user.setGroup_name(userinfo.getString("group_name", ""));
		user.setLogo_path(userinfo.getString("logo_path", ""));
		user.setHasOa(userinfo.getBoolean("hasOa", false));
		user.setIsLocked(userinfo.getInt("isLocked", 0));
		user.setGroup_locked(userinfo.getInt("group_locked", 0));
		user.setCard_locked(userinfo.getInt("card_locked", 0));
		user.setSuccess(userinfo.getBoolean("success", false));
		user.setMsg(userinfo.getString("msg", ""));
		// private String aaa021;
		// private String aaa020;
		// private String zzs051;
		// private String org;
		// private String operatorname;
		// private String userid;
		user.setAaa020(userinfo.getString("aaa020", ""));
		user.setAaa021(userinfo.getString("aaa021", ""));
		user.setZzs051(userinfo.getString("zzs051", ""));
		user.setOrg(userinfo.getString("org", ""));
		user.setOperatorname(userinfo.getString("operatorname", ""));
		user.setUserid(userinfo.getString("userid", ""));
		user.setZbdw(userinfo.getString("zbdw", ""));
		return user;
	}
}
