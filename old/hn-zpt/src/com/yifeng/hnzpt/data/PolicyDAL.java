package com.yifeng.hnzpt.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yifeng.hnzpt.util.DataConvert;

import android.content.Context;

/**
 * ClassName:PolicyDAL 
 * Description：政策法规数据操作类
 * @author Administrator 
 * Date：2012-9-2
 */
public class PolicyDAL extends BaseDAL {

	/**
	 * @param context
	 */
	public PolicyDAL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 政策法规列表
	 */
	public List<Map<String, String>> doQueryPolicy(int page, String state) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("page", page + "");
		this.setUrl(this.getIpconfig() + "android/managementcloud/listNews");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true")) {
			return DataConvert.toArrayList(map.get("news"));
		} else {
			return null;
		}
	}

	/**
	 * 政策法规详细
	 */
	public String doGetPolicyDetail(String id) {
		return this.getIpconfig() + "android/managementcloud/queryNewsDetail?id=" + id;
	}

}
