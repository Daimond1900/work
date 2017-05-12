package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.DataConvert;

import android.content.Context;
import android.util.Log;

/**
 * comments:培训信息
 * @author Administrator 
 * Date: 2012-9-1
 */
public class TrainingDAL extends BaseDAL
{
	public TrainingDAL(Context context)
	{
		super(context);
	}
	
	/**
     * 新闻列表
     * @param keyWord
     * @param pageNum
     * @return
     */
 	public List<Map<String, String>> doQuery(String keyWord,int pageNum)
 	{
 		
 		this.setUrl(this.getIpconfig()+"android/news/listNews");
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("page", pageNum+""));
 		params.add(new BasicNameValuePair("title",keyWord));//关键字
 		params.add(new BasicNameValuePair("type", "1"));
 		String json=this.doPost(params);
 		return DataConvert.toConvertStringList(json, "news");
 		
 		
 	}
 	
 	/**
 	 * 政策法规列表
 	 * @param pageNum
 	 * @return
 	 */
 	public List<Map<String, String>> doQuery_ZCFG(int pageNum) {

 		this.setUrl(this.getIpconfig()+"android/news/listNews");
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("page", pageNum+""));
 		params.add(new BasicNameValuePair("type", "2"));
 		String json=this.doPost(params);
 		Log.d("1 + == ", json);
 		return DataConvert.toConvertStringList(json, "news");
 	}

}
