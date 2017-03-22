package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;

import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;

import android.content.Context;
import android.util.Log;

/***
 * 用户操作工具类
 * 
 * @author wujiahong 2012-10-19
 */
public class UserDAL extends BaseDAL {
	private Context context;
	UserSession session;
	User user;
	private final String TAG = "UserDAL";
	
	public UserDAL(Context context) {
		super(context);
		this.context = context;
		session = new UserSession(context);
		user = session.getUser();
	}

	
//	/***
//	 * 用户需要考勤的课数
//	 * @param userId
//	 * @param flag
//	 * @return
//	 */
//	public Map<String, String> queryKqCount() {
//		Map<String, String> params = new HashMap<String, String>();
////		this.setUrl(Constants.IP+"android/person/queryResumeDetail");
//		this.setUrl("http://192.168.66.244:8080/yzjycy/android/person/queryResumeDetail");
//		
//		params.put("id", "");
//		params.put("flag", "");
//		String json = this.doGet(params);
//		return DataConvert.toConvertStringMap(json, "");
//	}
//	
	/***
	 * 获取简历详细
	 *
	 * @param userId
	 * @return
	 */
	public Map<String, String> queryResumeDetail(String userId,String flag) {
		this.setUrl(Constants.IP
				+ "android/person/queryResumeDetail");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userId);
		params.put("flag", flag);
		String json = this.doGet(params);
		Log.d(TAG+"获取简历详细--返回值:", (String) json);
		return DataConvert.toConvertStringMap(json, "resume");
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
		String postUrl = "";
		Map<String, String> returnMap = new HashMap<String, String>();
//		if (!isAdd) {
//			postUrl = Constants.IP
//					+ "android/person/modifyResume";// 修改改提交路径
//		}else{
//			postUrl = Constants.IP
//			+ "android/person/createResume";// 新增提交路径
//		}
		postUrl = Constants.IP
				+ "android/person/modifyResume";// 修改改提交路径
		
		try {
			String json = FileUtils.post(postUrl, params, files);
			returnMap = DataConvert.toMap(json);
		} catch (Exception e) {
			e.printStackTrace();
//			returnMap.put("state", String.valueOf(0));
//			returnMap.put("success", "false");
//			returnMap.put("msg", "信息提交失败!");
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
		this.setUrl(Constants.IP+"android/person/listCity");
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
		this.setUrl(Constants.IP+"android/person/listCity");
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
		this.setUrl(Constants.IP+"android/person/listCity");
		String json=this.doPost(params);
		Map<String,String> m = DataConvert.toMap(json);
		if(m!=null){
			if(m.get("success")!=null && m.get("success").equals("true")){
				maps = DataConvert.toConvertStringList(json, "cities");
				if(maps.get(0).get("state").equals(String.valueOf(1))){
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
		List<Map<String, String>> maps = DataConvert.toAreaList(user.getCities());
//		List<Map<String, String>> maps = DataConvert.toAreaList("扬州");
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
		List<Map<String, String>> maps = DataConvert.toAreaList(user.getCities());
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
	
	/***
	 * 修改密码
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
		Log.d(TAG+"修改密码--返回值:", json);					// 日志
		Map<String, String> map = DataConvert.toMap(json);
		return map;
	}
}
