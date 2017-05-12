package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;

import android.content.Context;
import android.os.Handler;

public class ResumeDAL extends BaseDAL {

	public ResumeDAL(Context context, Handler handler) {
		super(context, handler);
	}


	/**
	 * 简历投递
	 * 
	 * @param ids
	 * @param names
	 * @param cid
	 * @param pid
	 * @return
	 */
	public String sendResume(String ids, String names, String cid, String pid) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("id", ids));
		pos_params.add(new BasicNameValuePair("xm", names));
		pos_params.add(new BasicNameValuePair("aab001", cid));
		pos_params.add(new BasicNameValuePair("acb210", pid));
		this.setUrl(Constants.IP + "android/root/sendResumes");
		return this.doPost(pos_params);
	}

	/**
	 * 投递列表
	 * 
	 * @param page
	 * @param user_id
	 * @return
	 */
	public List<Map<String, String>> doQuerySendList(int page, String user_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("create_by", user_id);
		param.put("page", page + "");
		this.setUrl(Constants.IP + "android/root/listSendings");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("sendings"));
		else
			return null;
	}

	/**
	 * 投递详情
	 * 
	 * @param id
	 * @return
	 */
	public String doQuerySendDetail(String id) {
		return Constants.IP
				+ "android/sending/querySendingDetail?sending_id=" + id;
	}
	
	/**
	 * 简历新增
	 * @param params
	 * @return
	 */
	public String doSendResume(List<NameValuePair> params) {
		this.setUrl(Constants.IP + "android/person/createResume");
		return this.doPost(params);
	}
	
	/**
	 * 简历修改
	 * @param params
	 * @return
	 */
	public String doModifyResume(List<NameValuePair> params) {
		this.setUrl(Constants.IP + "android/person/modifyResume");
		return this.doPost(params);
	}
	
	/**
	 * 修改状态
	 * @param params
	 * @return
	 */
	public String doModifyResumeStatus(String id,String is_open) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		param.put("is_open", is_open);
		this.setUrl(Constants.IP + "android/person/changeResumeStatus");
		return this.doGet(param);
	}
	
	/**
	 * 获取简历详情
	 * @param id
	 * @return
	 */
	public Map<String, String> getResumeDetail(String id,String flag) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		param.put("flag", flag);
		this.setUrl(Constants.IP + "android/person/queryResumeDetail");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
}
