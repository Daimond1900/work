package com.yifeng.hngly.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hngly.util.DataConvert;

import android.content.Context;
import android.os.Handler;

/**
 * @author ZK
 * 通讯录
 */
public class SetDAL extends BaseDAL {

	public SetDAL(Context context, Handler handler) {
		super(context, handler);
	}

	
	/**
	 * 通讯录人员列表
	 */
	public List<Map<String, String>> doQuery() {
		Map<String, String> param = new HashMap<String, String>();
		this.setUrl(this.getIpconfig() + "android/managementcloud/listContact");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("news"));
		else
			return null;
	}
	
	  /**
		 * 用户修改密码
		 * 
		 * @param user_id
		 * @param new_pwd
		 * @return
		 */
		public String doUpdatePwd(Map<String, String> map) {
			List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
			pos_params.add(new BasicNameValuePair("id", map.get("id")));
			pos_params.add(new BasicNameValuePair("name", map.get("name")));
			pos_params.add(new BasicNameValuePair("old_pwd", map.get("old_pwd")));
			pos_params.add(new BasicNameValuePair("new_pwd", map.get("new_pwd")));
			this.setUrl(this.getIpconfig() + "android/system/changePwd");
			return this.doPost(pos_params);
		}
}
