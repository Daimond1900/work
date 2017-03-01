package com.yifeng.hnqzt.data;

import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnqzt.util.DataConvert;

import android.content.Context;

/**
 * comments:创业项目
 * @author Administrator
 * Date: 2012-9-1
 */
public class VentureDAL extends BaseDAL
{
	public VentureDAL(Context context)
	{
		super(context);
	}
	/***
	 * 创业平台
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> doQuery(int pageNum,String keyWords)
	{   
		this.setUrl(this.getIpconfig()+"android/trainingplatform/businessPlatform");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", pageNum+""));
		params.add(new BasicNameValuePair("title", keyWords));
		String json=this.doPost(params);
		return DataConvert.toConvertObjectList(json, "business");
	}


}
