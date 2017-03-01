package com.yifeng.data;

import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.yifeng.entity.User;
import com.yifeng.util.*;
import android.content.Context;

public class UserDAL extends BaseDAL {
	public UserDAL(Context context) {
		super(context);
	}

	/**
	 * 查询用户列表返回数据
	 * 
	 * @return
	 */
	public List<Map<String, String>> userList() {
		this.setUrl(this.getIpconfig() + "/getUser.jsp");
		HttpPostGetUtil.doGet(this.getUrl(), null);
		return null;
	}

	/***
	 * 登陆查询用户
	 * 
	 * @return
	 */
	public User doLogin(User us) {
		this.setUrl(this.getIpconfig() + "login/doLogin");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", us.getUserName());
		param.put("userPwd", StringHelper.handlePwd(us.getUserPwd()));
		param.put("imsi", us.getImsi());
		String data = this.doGet(param);
		User user = new User();
		if (data.equals("error")) {
			user.setState(ConstantUtil.SERVER_ERROR);
		} else if (data.equals("FAIL")) {
			user.setState(ConstantUtil.LOGIN_FAIL);
		} else if (data.equals("FAIL1")) {
			user.setState(ConstantUtil.LOGIN_FAIL1);
		} else {
			try {
				JSONObject vo = new JSONObject(data);
				user.setState(ConstantUtil.LOGIN_SUCCESS);
				user.setUserId(vo.getString("USERID"));
				user.setUserName(vo.getString("USERNAME") == null ? "" : vo
						.getString("USERNAME"));
				user.setUserPwd(vo.getString("PWD"));
				// user.setRoleId(vo.getString("ROLEID"));
				// user.setDepartment(vo.getString("DEPARTMENTID"));
				user.setOrg_id(vo.getString("ORG_ID"));
				user.setImsi(vo.getString("IMSI"));
				user.setKey(vo.getString("key"));
				user.setJob(vo.getString("JOB"));
				user.setRole_type(vo.getString("ROLE_TYPE"));
				user.setMobile_no(vo.getString("MOBILE_NO"));
			} catch (JSONException e) {
				user.setState(ConstantUtil.INNER_ERROR);// 解析异常
			}
		}
		return user;
	}

	/**
	 * 修改密码
	 * 
	 * @param userid
	 * @param old_pwd
	 * @param new_pwd
	 * @return
	 */
	public int doUpdatePwd(String userid, String old_pwd, String new_pwd) {
		this.setUrl(this.getIpconfig() + "login/doUpdatePwd");
		Map<String, String> pos_params = new HashMap<String, String>();
		pos_params.put("userid", userid);
		pos_params.put("old_pwd", old_pwd);
		pos_params.put("new_pwd", new_pwd);
		String data = this.doGet(pos_params);
		if (data != null) {
			data = StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data);
		}
		if (data.equals("error")) {
			return ConstantUtil.SERVER_ERROR;
		} else if (data.equals("SUCCESS")) {
			return 1;
		} else {
			return 0;
		}
	}

}
