package com.yifeng.hngly.data;

import java.util.HashMap;
import java.util.Map;

import com.yifeng.hngly.util.DataConvert;
import com.yifeng.hngly.util.ReJson;

import android.content.Context;
import android.os.Handler;

public class LoginuserDAL extends BaseDAL{

	public LoginuserDAL(Context context, Handler handler) 
	{
		super(context, handler);
	}
	/**
	 * 
	 * @param page 
	 * @param id  
	 * @return
	 */
	public ReJson getListById(String page,String id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("account", page);
		param.put("passwd", id);
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/login");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json,"homes");
	}
	/**
	 * 获取通讯录信息
	 * @return
	 */
	public String doQuery_Add() {
		
		Map<String, String> param = new HashMap<String, String>();
	 
		this.setUrl(this.getIpconfig()
				+ "android/managementcloud/listContact");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return  map.get("contactList");
		else
			return null;
	}
	/**
	 * 初始化所有的下拉列表
	 * @return
	 */
	public Map<String, String> initAllOptions(){
		Map<String, String> param = new HashMap<String, String>();
	 
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/initAllOptions");
		String json = this.doGet(param);
		return  DataConvert.toMap1(json);
	}
	

}
