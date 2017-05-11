package com.yifeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.yifeng.ChifCloud12345update.MainActivity;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.CommonUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.FormFile;
import com.yifeng.util.UploadImage;

public class ImportantEventDAL extends BaseDAL {

	public ImportantEventDAL(Context context) {
		super(context);
	}

	public List<Map<String, String>> doQury1(Map<String, String> map) {
		this.setUrl(this.getIpconfig() + "android/zytx/doGetList");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	/**
	 * 重要提醒未读查询
	 * @param page 当前页
	 * @param department_id =orle_id 
	 * @param state 重要提醒0
	 * @param user_id 用户id
	 * @return
	 */
	
	public List<Map<String, String>>  doQueryNotRead(String page,String department_id,String state,String  user_id) {
		this.setUrl(this.getIpconfig() + "android/zytx/doQueryNotRead");
		Map<String, String> map = new HashMap<String, String>();
		map.put("page",page);
//		map.put("state", state);
		map.put("user_id",  user_id);
//		map.put("department_id",department_id);
		
		
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	
	/**
	 * 重要提醒已读未读查询
	 * @param page 当前页
	 * @param department_id =orle_id 
	 * @param state 重要提醒0
	 * @param user_id 用户id
	 * @return
	 */
	
	public List<Map<String, String>>   doQueryListAll(String page,String department_id,String state,String  user_id) {
		this.setUrl(this.getIpconfig() + "android/zytx/doQueryListAll");
		Map<String, String> map = new HashMap<String, String>();
		map.put("page",page);
//		map.put("state", state);
		map.put("user_id",  user_id);
		map.put("department_id",department_id);
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}
	
	
	
	/**
	 * 重要提醒阅读确认
	 * @param im_id 提醒条目id
	 
	 * @param user_id 用户id
	 * @return
	 */
	
	public void doIsRead(String im_id,String  user_id) {
		this.setUrl(this.getIpconfig() + "android/zytx/doIsRead");
		Map<String, String> map = new HashMap<String, String>();
		map.put("im_id",im_id);
//		map.put("state", state);
		map.put("user_id",  user_id);
//		map.put("department_id",department_id);
		
		
		  this.doGet(map);
	}
	
	/**
	 * 每月简报
	 * @param page 当前页
 
	 * @return
	 */
	
	public List<Map<String, String>>   doQueryMonthAll(String page,String url ) {
		this.setUrl(this.getIpconfig() + url);
		Map<String, String> map = new HashMap<String, String>();
		map.put("page",page);
//		map.put("state", state);
		 
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}
	
	/**
	 * 年度总结
	 * @param page 当前页
 
	 * @return
	 */
	
	public List<Map<String, String>>   doQueryYearAll(String page ) {
		this.setUrl(this.getIpconfig() + "android/ndzj/doInit");
		Map<String, String> map = new HashMap<String, String>();
		map.put("page",page);
//		map.put("state", state);
		 
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}
	
	
}
