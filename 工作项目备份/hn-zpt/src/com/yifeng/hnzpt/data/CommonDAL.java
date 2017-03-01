package com.yifeng.hnzpt.data;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;

import android.content.Context;

/**
 * ClassName:CommonDAL 
 * Description：公共数据操作类（列表查询）
 * @author Administrator 
 * Date：2012-10-15
 */
public class CommonDAL extends BaseDAL {
	/**
	 * @param context
	 */
	public CommonDAL(Context context) {
		super(context);
	}

	/**
	 * (get请求方式)
	 * 
	 * @param map
	 * @param url
	 * @return
	 */
	public List<Map<String, String>> doQuery(Map<String, String> map, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doGet(map);
		return DataConvert.toArrayList(json);
	}

	/**
	 * (post请求方式)
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public List<Map<String, Object>> doPostQuery(List<NameValuePair> params, String url) {
		this.setUrl(ConstantUtil.ip + url);
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "list");
	}

}
