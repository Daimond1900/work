package com.winksoft.android.yzjycy.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.StringHelper;

import android.content.Context;
import android.util.Log;

/**
 * comments:我的求职
 * 
 * @author WuJiaHong Date: 2012-9-21
 */
public class JobDAL extends BaseDAL {
	
	private final String TAG = "JobDAL";
	
	/**
	 * @param context
	 */
	public JobDAL(Context context) {
		super(context);
	}

	/***
	 * 获取统计数据
	 * @return
	 */
	public Map<String, String> doGetCount(String userId) {
		this.setUrl(Constants.IP + "android/sending/countSending");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userId);
		String json = this.doGet(params);
		return DataConvert.toConvertStringMap(json, "sending");
	}

	/***
	 * 简历投递等待和失败列表
	 * @param userId
	 * @param type
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> doQuery(String userId, String type, int pageNum) {

		this.setUrl(Constants.IP + "android/myjobcloud/waitingConfirmation");
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", pageNum + "");
		params.put("YHM", userId);
		params.put("STATE", type);
		String json = this.doGet(params);
		return DataConvert.toConvertObjectList(json, "waitingConfirmation");
	}

	/***
	 * 试用通知
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> doNrialNotice(String userId, int pageNum, String status) {

		this.setUrl(Constants.IP + "android/sending/listSending");
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", pageNum + "");
		params.put("id", userId);
		params.put("state", status);
		String json = this.doGet(params);
		return DataConvert.toConvertObjectList(json, "sendings");

	}

	/***
	 * 面试通知
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> dointerviewInvitation(String userId, int pageNum) {

		this.setUrl(Constants.IP + "android/myjobcloud/interviewInvitation");
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", pageNum + "");
		params.put("YHM", userId);
		String json = this.doGet(params);
		return DataConvert.toConvertObjectList(json, "interviewInvitation");

	}

	/***
	 * 取消投递
	 * @param sending_id
	 * @return
	 */
	public Map<String, String> doRevokeResume(String sending_id) {
		this.setUrl(Constants.IP + "android/sending/revokeSending");
		Map<String, String> params = new HashMap<String, String>();
		params.put("sending_id", sending_id);
		String json = this.doGet(params);
		return DataConvert.toMap(json);
	}

}
