package com.winksoft.android.yzjycy;
import android.content.Context;
import android.content.SharedPreferences;

import com.winksoft.android.yzjycy.entity.User;

public class CacheData {
	public  final static String SETTINGS_USER="SETTINGS_USER";
	/***
	 * 设置登录信息
	 * @param mContext
	 * @param user
	 */
    public static void setUser(Context mContext,User user){
    	SharedPreferences m= mContext.getSharedPreferences(SETTINGS_USER, 0);
    	m.edit()
    	.putString("userId", user.getUserId())
    	.putString("userName", user.getUserName())
    	.putString("userPwd", user.getUserPwd())
    	.putString("real_name", user.getReal_name())
    	.putString("create_time", user.getCreate_time())
    	.putString("email", user.getEmail())
    	.putString("department_id", user.getDepartment_id())
    	.putString("department_name", user.getDepartmentName())
    	.putString("role_name", user.getRoleName())
    	.putString("role_id", user.getRoleId())
    	.putString("unit_id", user.getUnitId())
    	.commit();
    }
    /***
     * 获取用户信息
     * @param mContext
     * @return
     */
    public static User getUser(Context mContext){
    	User u=new User();
    	SharedPreferences m= mContext.getSharedPreferences(SETTINGS_USER, 0);
    	u.setUserId(m.getString("userId", ""));
    	u.setUserName(m.getString("userName", ""));
    	u.setUserPwd(m.getString("userPwd", ""));
    	u.setReal_name(m.getString("real_name", ""));
    	u.setCreate_time(m.getString("create_time", ""));
    	u.setEmail(m.getString("email", ""));
    	u.setDepartment_id(m.getString("department_id", ""));
    	u.setDepartmentName(m.getString("department_name", ""));
    	u.setRoleName(m.getString("role_name", ""));
    	u.setRoleId(m.getString("role_id", ""));
    	u.setUnitId(m.getString("unit_id", ""));
    	return u;
    }

}
