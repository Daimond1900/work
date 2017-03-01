package com.yifeng.hnzpt.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;

import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;

import android.content.Context;

/**
 * ClassName:ManageDAL 
 * Description：招聘管理
 * @author Administrator 
 * Date：2012-10-20
 */

public class ManageDAL extends BaseDAL {
	/**
	 * @param context
	 */
	public ManageDAL(Context context) {
		super(context);
	}

	/**
	 * (post请求方式)
	 * @param params
	 * @param url
	 * @return
	 */
	public List<Map<String, Object>> doPostQuery(List<NameValuePair> params, String url) {
		this.setUrl(ConstantUtil.ip + url);
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "sendings");
	}
	
	/**
	 * 招聘管理
	 * @param params
	 * @param url
	 * @return
	 */
	public List<Map<String, Object>> doPostQuery1(List<NameValuePair> params, String url) {
		this.setUrl(ConstantUtil.ip + url);
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "recruitments");
	}

	/**
	 * 新增招聘信息
	 * @param params
	 * @return
	 */
	public String doAdd(List<NameValuePair> params, String url) {
		this.setUrl(this.getIpconfig() + url);
		String json = this.doPost(params);
		return json.toString();
	}

	/***
	 * 职位详细
	 * @param positionId
	 * @return
	 */
	public Map<String, String> getPosition(String positionId, String companyId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("ACB210", positionId);// 职位编号
		param.put("AAB001", companyId);// 公司编号
		this.setUrl(getIpconfig() + "android/searchingcloud/queryRecuitmentDetail");
		String json = this.doGet(param);
		return DataConvert.toConvertStringMap(json, "recruitment");
	}

}
