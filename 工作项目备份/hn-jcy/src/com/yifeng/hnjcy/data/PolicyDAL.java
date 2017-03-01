package com.yifeng.hnjcy.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yifeng.hnjcy.util.DataConvert;

import android.content.Context;
import android.os.Handler;

/**
 * @author ZK
 */
public class PolicyDAL extends BaseDAL {
	
	public PolicyDAL(Context context, Handler handler) {
		super(context, handler);
	}

	/**
	 * 政策法规列表
	 */
	public List<Map<String, String>> doQueryPolicy(int page,String state) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("state",state);
		param.put("page", page + "");
		this.setUrl(this.getIpconfig() + "android/managementcloud/listNews");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("news"));
		else
			return null;
	}
	
	/**
	 * 政策法规详细
	 */
	public String doGetPolicyDetail(String id) {
		
		return this.getIpconfig()
				+ "android/managementcloud/queryNewsDetail?id="
				+ id;
	}
	/**
	 * 每日一新列表
	 */
	public List<Map<String, String>> doQueryNew(int page) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("state","4");
		param.put("page", page + "");
		this.setUrl(this.getIpconfig() + "android/managementcloud/listNews");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("news"));
		else
			return null;
	}
	
	/**
	 * 每日一新详细
	 */
	public List<Map<String, String>> doQueryNewDetail(int id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id+"");
		this.setUrl(this.getIpconfig() + "android/managementcloud/queryNewsDetail");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("Labours"));
		else
			return null;
	}
}
