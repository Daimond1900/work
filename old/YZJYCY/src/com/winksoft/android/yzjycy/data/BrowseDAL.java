package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.StringHelper;

import android.content.Context;

/**
 * comment:浏览企业招聘信息
 * @author:ZhangYan
 * Date:2012-8-30
 */
public class BrowseDAL extends BaseDAL {

	public BrowseDAL(Context context) {
		super(context);
	}
	
	public List<Map<String, String>> doQuery(Map<String, String> params,String url){
		/*this.setUrl(this.getIpconfig()+url);
		String json=this.doGet(params);
		return DataConvert.toArrayList(json);*/
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		if(!params.get("position_request").equals("") && params.get("position_request").equals("软件")){
			map.put("state", "1");
			map.put("id", "1.");
			map.put("companyName", "扬州恒春电子有限公司 [软件设计师]");
			list.add(map);
			map = new HashMap<String, String>();
			map.put("state", "1");
			map.put("id", "2.");
			map.put("companyName", "扬州中科半导体照明有限公司 [软件工程师]");
			list.add(map);
			map = new HashMap<String, String>();
			map.put("state", "1");
			map.put("id", "3.");
			map.put("companyName", "江苏亚威机床股份有限公司 [计算机软件及应用、数控系统二次开发]");
			list.add(map);
			return list;
		}else {
			return DataConvert.toArrayList("");
		}
		
	}
	
}
