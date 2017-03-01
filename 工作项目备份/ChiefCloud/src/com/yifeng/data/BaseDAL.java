package com.yifeng.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.yifeng.ChifCloud12345update.MainActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.HttpPostGetUtil;
import com.yifeng.util.StringHelper;

/**
 * 读取远程数据操作父类
 * @author 吴家宏 2011-04-19
 */
public class BaseDAL {
	private String ipconfig;
	public Context context;
	private String url;

	public BaseDAL(Context context) {
		this.context = context;
		HttpPostGetUtil.getHttpClient();
		ipconfig = this.context.getString(R.string.ipconfig);
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

	/**
	 * get请求
	 * 
	 * @param params
	 * @return
	 */
	public String doGet(Map<String, String> params) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put("key", LoginBiz.loadUser(this.context).getKey());
		String data = HttpPostGetUtil.doGet(this.getUrl(), params);
		if (data != null) {

			data = StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
			if (data.equals("FAIL")) {
				MainActivity.iflogin = false;
			}
		}
		return data;
	}

	/***
	 * post请求
	 * 
	 * @param pos_params
	 * @return
	 */
	public String doPost(List<NameValuePair> pos_params) {
		if (pos_params == null) {
			pos_params = new ArrayList<NameValuePair>();
		}
		pos_params.add(new BasicNameValuePair("key", LoginBiz.loadUser(this.context).getKey()));
		String data = HttpPostGetUtil.doPost(this.getUrl(), pos_params);
		if (data != null) {
			data = StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", data).trim();
		}
		return data;
	}

}
