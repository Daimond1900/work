package com.winksoft.android.yzjycy.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;

import android.content.Context;

/**
 * ClassName:EnterpriseDAL 
 * Description：企业信息数据操作类
 * @author Administrator 
 * Date：2012-10-22
 */
public class EnterpriseDAL extends BaseDAL {
	/**
	 * @param context
	 */
	public EnterpriseDAL(Context context) {
		super(context);
	}

	/**
	 * (get请求方式)
	 * @param map
	 * @param url
	 * @return
	 */
	public List<Map<String, String>> doQuery(Map<String, String> map, String url) {
		this.setUrl(Constants.IP + url);
		String json = this.doGet(map);
		return DataConvert.toArrayList(json);
	}

	/**
	 * (post请求方式)
	 * @param params
	 * @param url
	 * @return
	 */
	public Map<String, String> doPostQuery(List<NameValuePair> params, String url) {
		this.setUrl(Constants.IP + url);
		String json = this.doPost(params);
		return DataConvert.toConvertStringMap(json, "company");
	}

	/***和修改简历
	 * 添加
	 * @param params
	 * @param files
	 * @return
	 */
	public Map<String, String> doChange(Map<String, String> params, FormFile[] files) {
		String postUrl = Constants.IP + "android/corecruitment/modifyCompanyInfo";
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			String json = FileUtils.post(postUrl, params, files);
			returnMap = DataConvert.toMap(json);

		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("state", String.valueOf(Constants.LOGIN_FAIL));
			returnMap.put("success", "false");
			returnMap.put("msg", "信息提交失败!");
		}
		return returnMap;
	}

}
