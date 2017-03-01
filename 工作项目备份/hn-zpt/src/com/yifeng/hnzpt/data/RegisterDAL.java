package com.yifeng.hnzpt.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;

import android.content.Context;

/**
 * ClassName:RegisterDAL 
 * Description：报名信息数据操作类
 * @author Administrator 
 * Date：2012-10-20
 */
public class RegisterDAL extends BaseDAL {
	public RegisterDAL(Context context) {
		super(context);
	}

	/**
	 * (post请求方式)
	 * @param params
	 * @param url
	 * @return
	 */
	public List<Map<String, Object>> doPostQuery(List<NameValuePair> params, String url,String key) {
		this.setUrl(ConstantUtil.ip + url);
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, key);
	}

	/***
	 * 获取简历详细
	 * @param userId
	 * @return
	 */
	public Map<String, String> queryResumeDetail(String userId,String flag) {
		this.setUrl(this.getIpconfig() + "android/person/queryResumeDetail");
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("id", userId));
		param.add(new BasicNameValuePair("flag", flag));
		String json = this.doPost(param);
		return DataConvert.toConvertStringMap(json, "resume");
	}

	/**
	 * 拒绝
	 * @param params
	 * @param url
	 * @return
	 */
	public String doUpRight(List<NameValuePair> rightParams, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doPost(rightParams);
		return json.toString();
	}

	/**
	 * 同意
	 * @param leftParams
	 * @param url
	 * @return
	 */
	public String doUpLeft(List<NameValuePair> leftParams, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doPost(leftParams);
		return json.toString();
	}
}
