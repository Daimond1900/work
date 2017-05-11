package com.yifeng.hngly.data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.os.Handler;
import com.yifeng.hngly.util.ConstantUtil;
import com.yifeng.hngly.util.HttpPostGetUtil;
import com.yifeng.hngly.util.UserSession;

/**
 * 读取远程数据操作父类
 * @author 吴家宏
 * 2011-04-19
 */
public   class BaseDAL {
	private String ipconfig;
	public Context context;
	protected Handler handler;
	private String url;
	public BaseDAL(Context context, Handler handler){
		this.context=context;
		HttpPostGetUtil.getHttpClient();
		ipconfig=ConstantUtil.ip;
		this.handler=handler;
	}
	
	public String getIpconfig() {
		return ipconfig;
	}

	public void setIpconfig(String ipconfig) {
		this.ipconfig = ipconfig;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String doGetLogin(Map<String,String> params){
		if(params==null){
			params=new HashMap<String,String>();
		}
		UserSession session=new UserSession(this.context);
//		params.put("key", session.getUser().getKey());
		String data=HttpPostGetUtil.doGetLogin(this.getUrl(), params);
//		if(data!=null){
//			data=StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
//	    }
		return data;
	}
	
	/**
	 * get请求
	 * @param params
	 * @return
	 */
	public String doGet(Map<String,String> params){
		if(params==null){
			params=new HashMap<String,String>();
		}
		UserSession session=new UserSession(this.context);
		//params.put("key", session.getUser().getKey());
		String data=HttpPostGetUtil.doGet(this.getUrl(), params);
//		if(data!=null)
//		{
//			data=StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
//	    }
		return data;
	}
	/***
	 * post请求
	 * @param pos_params
	 * @return
	 */
	public String doPost(List<NameValuePair> pos_params){
		 if(pos_params==null){
			 pos_params=new ArrayList<NameValuePair>();
		 }
		 UserSession session=new UserSession(this.context);
		 pos_params.add(new BasicNameValuePair("key",session.getUser().getKey())); 
		 String data=HttpPostGetUtil.doPost(this.getUrl(), pos_params);
//		 if(data!=null){
//				data=StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
//		  }
		 return data;
	}
	public String doPost1(List<NameValuePair> pos_params){
		 if(pos_params==null){
			 pos_params=new ArrayList<NameValuePair>();
		 }
		 UserSession session=new UserSession(this.context);
		 pos_params.add(new BasicNameValuePair("key",session.getUser().getKey())); 
		 String data=HttpPostGetUtil.doPost1(this.getUrl(), pos_params);
//		 if(data!=null){
//				data=StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
//		  }
		 return data;
	}
}
