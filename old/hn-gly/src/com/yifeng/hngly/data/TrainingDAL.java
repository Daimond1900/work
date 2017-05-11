package com.yifeng.hngly.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.os.Handler;
import com.yifeng.hngly.util.DataConvert;

/**
 * comments:培训信息
 * @author Administrator 
 * Date: 2012-9-1
 */
public class TrainingDAL extends BaseDAL
{
	 
	
    public TrainingDAL(Context context, Handler handler) {
		super(context, handler);
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
	
	public List<Map<String, String>> doQuery_ZCFG(int pageNum) {

		this.setUrl(this.getIpconfig()+"android/news/listNews");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", pageNum+""));
		params.add(new BasicNameValuePair("type", "2"));
		String json=this.doPost(params);
		return DataConvert.toConvertStringList(json, "news");
	}
	
}
