package com.yifeng.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.util.DataConvert;

import android.content.Context;

public class OAfindUserData extends BaseDAL{

	public OAfindUserData(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public List<Map<String, String>> selectAppUser(String dept,String userid){
		
		List<NameValuePair> pos_params=new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("departmentid", dept));
		pos_params.add(new BasicNameValuePair("user_id", userid));
		this.setUrl(this.getIpconfig() + "android/dispatch/doQueryTargetPerson");
		String json=this.doPost(pos_params);
	    return DataConvert.toArrayList(json);
	}
	public String listDepartment(String dept){
		List<NameValuePair> pos_params=new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("departmentid", dept));
		this.setUrl(this.getIpconfig() + "android/dispatch/doQueryTargetList");
		String json=this.doPost(pos_params);
		return json;
	}
	
}
