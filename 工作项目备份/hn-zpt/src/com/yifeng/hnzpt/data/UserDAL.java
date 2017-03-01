package com.yifeng.hnzpt.data;

import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import android.content.Context;

import com.yifeng.hnzpt.entity.User;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;

/**
 * ClassName:UserDAL 
 * Description：用户登录数据操作类
 * @author Administrator 
 * Date：2012-8-17
 */
public class UserDAL extends BaseDAL {
	public UserDAL(Context context) {
		super(context);
	}

	/**
	 * 获取用户信息
	 * @param param
	 * @return
	 */
	public User loadUser(Map<String, String> param) {
		this.setUrl(this.getIpconfig() + "android/system/login4Zpy");

		String json = this.doGetLogin(param);

		User user = new User();
		if (json.equals("error")) {
			user.setState(ConstantUtil.SERVER_ERROR);
		} else if (json.equals("FAIL")) {
			user.setState(ConstantUtil.LOGIN_FAIL);
		} else {
			try {
				Map<String,String> map = DataConvert.toMap(json);
//				Map<String, String> map = DataConvert.toConvertStringMap(json, "user");
				if (map.get("success").equals("true")) {
					user.setState(ConstantUtil.LOGIN_SUCCESS);
					user.setUserId(map.get("id")); //用户唯一编号
					user.setUserPwd(param.get("password"));// 登录密码
					user.setUserName(param.get("name"));// 登录帐号
					user.setTitle(map.get("title"));// 用户名
					user.setDeptId(map.get("department_id")); // 部门ID
					user.setDeptName(map.get("department_name")); // 部门名称
					user.setAreas(map.get("cities")); // 区域
					user.setcompanyManager(map.get("aae004")); //联系人
					user.setLinkAddress(map.get("aae006")); //联系地址
					user.setLinkPhone(map.get("aae005")); //联系电话
					user.setLatitude(map.get("lat"));
					user.setLongitude(map.get("lng"));
					user.setZbdw(map.get("zbdw")); //主办单位
				} else {
					user.setState(ConstantUtil.LOGIN_FAIL);
					user.setCompanyName(map.get("msg"));
				}

			} catch (Exception e) {
				user.setState(ConstantUtil.INNER_ERROR);// 解析异常
			}
		}
		return user;
	}

	/**
	 * 密码修改
	 * @param params
	 * @return
	 */
	public String doUpdatePwd(List<NameValuePair> params, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doPost(params);
		return json.toString();
	}

}
