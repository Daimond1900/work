package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;

import android.content.Context;

/**
 * ClassName:RecruitmentDAL 
 * Description：招聘登记数据操作类
 * @author Administrator 
 * Date：2012-10-18
 */

public class RecruitmentDAL extends BaseDAL {
	UserSession session;
	User user;
	/**
	 * @param context
	 */
	public RecruitmentDAL(Context context) {
		super(context);
		session = new UserSession(context);
		user = session.getUser();
	}

	/**
	 * (get请求方式)
	 * @param map
	 * @param url
	 * @return
	 */
	public List<Map<String, String>> doQuery(Map<String, String> map, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doGet(map);
		return DataConvert.toArrayList(json);
	}

	/**
	 * (post请求方式)
	 * @param params
	 * @param url
	 * @return
	 */
	public List<Map<String, Object>> doPostQuery(List<NameValuePair> params, String url) {
		this.setUrl(Constants.IP + url);
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "jobs");
	}

	/**
	 * 新增招聘信息
	 * @param params
	 * @return
	 */
	public String doAdd(List<NameValuePair> params, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doPost(params);
		return json.toString();
	}
	
	/**
	 * 获取区域
	 * @param flag
	 * @param title 
	 * @return
	 */
	public List<Map<String, String>> getAreas(boolean flag,String title) {
		List<Map<String, String>> maps = DataConvert.toArrayList(user.getCities());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(flag){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "");
			map.put("name", title);
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

}
