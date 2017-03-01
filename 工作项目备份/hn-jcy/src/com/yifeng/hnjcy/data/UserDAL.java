package com.yifeng.hnjcy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.yifeng.cloud.entity.User;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.DataConvert;
import com.yifeng.hnjcy.util.HttpPostGetUtil;
import com.yifeng.hnjcy.util.UserSession;

import android.content.Context;
import android.os.Handler;

public class UserDAL extends BaseDAL {

	public UserDAL(Context context, Handler handler) {
		super(context, handler);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param param
	 * @return
	 */
	public User loadUser(Map<String, String> param) {
		String account = "", pwd = "";
		if (param != null) {
			pwd = param.get("password");
			account = param.get("name");
		}
		this.setUrl(this.getIpconfig() + "android/system/login4Jcy");
		String json = this.doGetLogin(param);
		User user = new User();
		user.setSuccess(false);
		if (json.equals(HttpPostGetUtil.NONETWORKERROR)) {
			user.setState(ConstantUtil.SERVER_ERROR);
		} else if (json.equals("FAIL")) {
			user.setState(ConstantUtil.LOGIN_FAIL);
		} else if (json.equals("FAIL1")) {
			user.setState(ConstantUtil.LOGIN_FAIL1);
		} else {
			try {
				Map<String, String> map = DataConvert.toMap(json);
				if (map.get("success").equals("false")) {// 账号不存在或用户名密码错误
					user.setSuccess(false);
					user.setMsg(map.get("msg"));
					return user;
				} else {
					user.setMsg(map.get("msg"));
					user.setOperatorname(map.get("title"));//用户名
					user.setUserName(account); //登录ID
					user.setPwd(pwd);//密码
					user.setUserId(map.get("id")); //用户id
					user.setDepartmentid(map.get("department_id"));  //部门ID
					user.setCompanyName(map.get("department_name")); //部门名称
					user.setZbdw(map.get("zbdw")); //主办单位
					user.setGroup(map.get("cities")); //区域
					
					user.setSuccess(true);
				}

			} catch (Exception e) {
				e.printStackTrace();
				user.setState(ConstantUtil.INNER_ERROR);// 解析异常
			}
		}
		return user;
	}

	/**
	 * 根据token去获取用户名及密码
	 * 
	 * @param token
	 * @return
	 */
	public Map<String, String> doGetAuth(String token) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("token", token);
		this.setUrl(this.getIpconfig() + "login/login/getAuth");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}

	/**
	 * OA登陆验证
	 * 
	 * @param pos_params
	 * @return
	 */
	public Map<String, String> loadOaUser(Map<String, String> params) {

		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();

		pos_params.add(new BasicNameValuePair("CheckCode", "sd"));// 可为空
		pos_params
				.add(new BasicNameValuePair("Username", params.get("userid")));
		pos_params.add(new BasicNameValuePair("Password", params.get("mpwd")));
		pos_params.add(new BasicNameValuePair("_spring_security_remember_me",
				"on"));// 永远为ON

		this.setUrl(params.get("OA_IP") + "loginMobile.do");

		String json = this.doPost(pos_params);

		return DataConvert.toMap(json);
	}

	/***
	 * 用户注销
	 * 
	 * @return
	 */
	public String doLogOut() {
		this.setUrl(this.getIpconfig() + "login/doLogout");
		Map<String, String> params = new HashMap<String, String>();
		UserSession session = new UserSession(this.context);
		params.put("userName", session.getUser().getUserId());
		String state = this.doGet(params);
		return state;
	}

	/**
	 * 用户修改密码
	 * 
	 * @param user_id
	 * @param new_pwd
	 * @return
	 */
	public int doUpdatePwd(String user_id, String new_pwd) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("user_id", user_id));
		pos_params.add(new BasicNameValuePair("new_pwd", new_pwd));
		this.setUrl(this.getIpconfig() + "login/login/UpdatePassword");
		String json = this.doPost(pos_params);
		if (json.equals("error")) {
			return -1;
		}
		if (json.equals("fail")) {
			return 0;
		}
		if (json.equals("success")) {
			return 1;
		}
		return 0;
	}

	/**
	 * 修改密码
	 * 
	 * @param account
	 * @param oldPwd
	 * @param newPwd
	 * @param confirmPwd
	 * @return
	 */
	public Map<String, String> doUpdatePwd(String oldpasswd, String newpasswd,
			String userid) {
		this.setUrl(this.getIpconfig() + "android/rootscloud/updatePasswd");

		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("oldpasswd", oldpasswd));
		pos_params.add(new BasicNameValuePair("newpasswd", newpasswd));
		pos_params.add(new BasicNameValuePair("userid", userid));
		String json = this.doPost(pos_params);
		Map<String, String> map = DataConvert.toMap(json);
		return map;

	}
}
