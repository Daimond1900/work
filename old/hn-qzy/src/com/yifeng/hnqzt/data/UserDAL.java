package com.yifeng.hnqzt.data;

import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import com.yifeng.hnqzt.entity.User;
import com.yifeng.hnqzt.ui.UserSession;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.DataConvert;
import com.yifeng.hnqzt.util.FileUtils;
import com.yifeng.hnqzt.util.FormFile;

/***
 * 用户操作工具类
 * 
 * @author wujiahong 2012-10-19
 */
public class UserDAL extends BaseDAL {
	private Context context;
	UserSession session;
	User user;
	public UserDAL(Context context) {
		super(context);
		this.context = context;
		session = new UserSession(context);
		user = session.getUser();
	}

	/***
	 * 登录方法 获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @param pwd
	 *            密码
	 * @param md5Pwd
	 *            Md5加密码
	 * @return 
	 *         {"success":true,"msg":"登陆成功！","user":{"yhm":"ls_test","sj":"15105111112"
	 *         ,"id":"2012101513310336","mm":"123321","xm":"李小小"}}
	 */
	public User loadUser(String userId, String pwd) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("name", userId);
		param.put("password", pwd);

		this.setUrl(this.getIpconfig() + "android/system/login4Qzy");
		String json = this.doGetLogin(param);
		User user = new User();
		if (json.equals("error")) {
			user.setState(ConstantUtil.SERVER_ERROR);
		} else if (json.equals("FAIL")) {
			user.setState(ConstantUtil.LOGIN_FAIL);
		} else {
			try {
				Map<String, String> map = DataConvert.toMap(json);

				if (map.get("success").equals("false")) {// 账号不存在或用户名密码错误
					user.setState(ConstantUtil.WRONG_ID_OR_PWD);
					user.setServiceMsg(map.get("msg"));
				} else {
					user.setState(ConstantUtil.LOGIN_SUCCESS);
					user.setTitle(map.get("title"));// 用户名
					user.setUserName(userId); // 登录ID
					user.setUserPwd(pwd);// 密码
					user.setUserId(map.get("id")); // 用户id
					user.setDeptId(map.get("department_id")); // 部门ID
					user.setDeptName(map.get("department_name")); // 部门名称
					user.setAreas(map.get("cities")); // 区域
					user.setUserPic(map.get("pic_addr"));
					user.setMobileNo(map.get("sfzh"));//身份证号码
					user.setZbdw(map.get("zbdw")); //主办单位
				}
			} catch (Exception e) {
				user.setState(ConstantUtil.INNER_ERROR);// 解析异常
			}
		}
		return user;
	}

	/***
	 * 用户注册
	 * 
	 * @param account
	 *            账号
	 * @param passwd
	 *            密码
	 * @param repasswd
	 *            确认密码
	 * @return
	 */
	public Map<String, String> doRegedit(String account, String passwd,
			String areaId,String name) {
		this.setUrl(this.getIpconfig() + "android/person/register");
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("name", account));
		pos_params.add(new BasicNameValuePair("password", passwd));
		pos_params.add(new BasicNameValuePair("department_id", areaId));
		pos_params.add(new BasicNameValuePair("title", name));
		String json = this.doPost(pos_params);
		Map<String, String> map = DataConvert.toMap(json);
		return map;
	}

	/***
	 * 获取简历详细
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, String> queryResumeDetail(String userId,String flag) {
		this.setUrl(this.getIpconfig()
				+ "android/person/queryResumeDetail");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userId);
		params.put("flag", flag);
		String json = this.doGet(params);
		return DataConvert.toConvertStringMap(json, "resume");
	}

	/***
	 * 修改密码
	 * 
	 * @param account
	 * @param oldPwd
	 * @param newPwd
	 * @param confirmPwd
	 * @return
	 */
	public Map<String, String> doUpdatePwd(String id, String name,
			String old_pwd, String new_pwd) {
		this.setUrl(this.getIpconfig() + "android/system/changePwd");
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("id", id));
		pos_params.add(new BasicNameValuePair("name", name));
		pos_params.add(new BasicNameValuePair("old_pwd", old_pwd));
		pos_params.add(new BasicNameValuePair("new_pwd", new_pwd));
		String json = this.doPost(pos_params);
		Map<String, String> map = DataConvert.toMap(json);
		return map;

	}

	/***
	 * 添加和修改简历
	 * 
	 * @param isAdd
	 * @param params
	 * @param files
	 * @return
	 */
	public Map<String, String> doPostResume(boolean isAdd,
			Map<String, String> params, FormFile[] files) {
		String postUrl = this.getIpconfig()
				+ "android/person/modifyResume";// 新增提交路径
		Map<String, String> returnMap = new HashMap<String, String>();
		if (!isAdd) {
			postUrl = this.getIpconfig()
					+ "android/person/modifyResume";// 修改改提交路径
		}
		try {
			String json = FileUtils.post(postUrl, params, files);
			returnMap = DataConvert.toMap(json);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("state", String.valueOf(ConstantUtil.LOGIN_FAIL));
			returnMap.put("success", "false");
			returnMap.put("msg", "信息提交失败!");
		}
		return returnMap;
	}

	/***
	 * 获取婚姻状况
	 * 
	 * @return
	 */
	public List<Map<String, String>> getMarriages() {
		List<Map<String, String>> marriages = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "");
		map.put("value", "选择");
		marriages.add(map);

		map = new HashMap<String, String>();
		map.put("id", "未婚");
		map.put("value", "未婚");
		marriages.add(map);

		map = new HashMap<String, String>();
		map.put("id", "已婚");
		map.put("value", "已婚");
		marriages.add(map);
		return marriages;
	}

	/***
	 * 获取政治面貌
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPolitics() {
		List<Map<String, String>> politics = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "");
		map.put("value", "选择");
		politics.add(map);

		map = new HashMap<String, String>();
		map.put("id", "党员");
		map.put("value", "党员");
		politics.add(map);

		map = new HashMap<String, String>();
		map.put("id", "团员");
		map.put("value", "团员");
		politics.add(map);

		map = new HashMap<String, String>();
		map.put("id", "无");
		map.put("value", "无");
		politics.add(map);

		return politics;

	}

	/***
	 * 获取学历水平
	 * 
	 * @return
	 */
	public List<Map<String, String>> getEducationals() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "");
		map.put("value", "选择");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "博士");
		map.put("value", "博士");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "硕士");
		map.put("value", "硕士");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "大学");
		map.put("value", "大学");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "大专");
		map.put("value", "大专");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "中专中技");
		map.put("value", "中专中技");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "技校");
		map.put("value", "技校");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "高中");
		map.put("value", "高中");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "职高");
		map.put("value", "职高");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "初中");
		map.put("value", "初中");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "小学");
		map.put("value", "小学");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "文盲或半文盲");
		map.put("value", "文盲或半文盲");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "不限");
		map.put("value", "不限");
		list.add(map);

		return list;
	}
	
	
	/**
	 * 动态获取区域
	 * @return
	 */
	public List<Map<String, String>> doGetAreas() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		this.setUrl(this.getIpconfig()+"android/person/listCity");
		String json=this.doPost(params);
		Map<String,String> m = DataConvert.toMap(json);
		if(m!=null){
			if(m.get("success")!=null && m.get("success").equals("true")){
				return DataConvert.toConvertStringList(json, "cities");
			}
		}
		return null;
	}
	
	/**
	 * 首页获取区域列表
	 * @return
	 */
	public List<Map<String, Object>> doGetAreas1() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		this.setUrl(this.getIpconfig()+"android/person/listCity");
		String json=this.doPost(params);
		Map<String,String> m = DataConvert.toMap(json);
		if(m!=null){
			if(m.get("success")!=null && m.get("success").equals("true")){
				return DataConvert.toConvertObjectList(json, "cities");
			}
		}
		return null;
	}
	
	/**
	 * 招聘信息、招聘企业未登录加载地点
	 * @return
	 */
	public List<Map<String, String>> doUnLoginAreas() {
		List<Map<String, String>> maps;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "");
		map.put("name", "全部");
		list.add(map);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		this.setUrl(this.getIpconfig()+"android/person/listCity");
		String json=this.doPost(params);
		Map<String,String> m = DataConvert.toMap(json);
		if(m!=null){
			if(m.get("success")!=null && m.get("success").equals("true")){
				maps = DataConvert.toConvertStringList(json, "cities");
				if(maps.get(0).get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
					for (int i = 0; i < maps.size(); i++) {
						Map<String, String> m1 = maps.get(i);
						list.add(m1);
					}
				}
			}
		}
		return list;
	}

	/***
	 * 获取区域
	 * @return
	 */
	public List<Map<String, String>> getAreas(boolean flag) {
		List<Map<String, String>> maps = DataConvert.toAreaList(user.getAreas());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(flag){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "");
			map.put("name", "选择");
			list.add(map);
		}
		if(maps.size()>0){
			for (int i = 0; i < maps.size(); i++) {
				Map<String, String> m = maps.get(i);
				list.add(m);
			}
		}
		return list;
	}
	
	
	/***
	 * 获取区域
	 * @return
	 */
	public String getPersonalAreas(String id) {
		List<Map<String, String>> maps = DataConvert.toAreaList(user.getAreas());
		if(maps.size()>0){
			for (int i = 0; i < maps.size(); i++) {
				Map<String, String> m = maps.get(i);
				if(m.get("id").equals(id)){
					return m.get("name");
				}
			}
		}
		return "";
	}

	/***
	 * 获取工作性质
	 * 
	 * @return
	 */
	public List<Map<String, String>> getJobs() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "");
		map.put("value", "选择");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "全职");
		map.put("value", "全职");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "兼职");
		map.put("value", "兼职");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "实习");
		map.put("value", "实习");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("id", "不限");
		map.put("value", "不限");
		list.add(map);

		return list;
	}
}
