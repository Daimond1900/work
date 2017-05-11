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
	 * 
	 * @param userId
	 *            resumefailure 投递简历失败 nrialnotice 面试通知 waitingconfirmation
	 *            等待公司确认 interviewinvitation 面试邀请函 sum 投递总和
	 *            {"success":true,"msg":"查询成功!","myJob":{"resumefailure":0,"nrialnotice":0,"sum":0,"waitingconfirmation":0,"interviewinvitation":0}}
	 * 
	 * @return
	 */
	public Map<String, String> doGetCount(String userId) {
		this.setUrl(Constants.IP + "android/sending/countSending");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", userId);
		String json = this.doGet(params);
		Log.d(TAG, "获取统计数： "+"接口： "+"android/sending/countSending");
		Log.d(TAG, "获取统计数： "+"参数： "+params.toString());
		Log.d(TAG, "获取统计数： "+"返回值： "+json);
		return DataConvert.toConvertStringMap(json, "sending");
	}

	/***
	 * 简历投递等待和失败列表
	 * 
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
		Log.d(TAG, "简历投递等待和失败列表： "+"接口： "+"android/myjobcloud/waitingConfirmation");
		Log.d(TAG, "简历投递等待和失败列表： "+"参数： "+params.toString());
		Log.d(TAG, "简历投递等待和失败列表： "+"返回值： "+json);
		return DataConvert.toConvertObjectList(json, "waitingConfirmation");

		/*
		 * List<Map<String, Object>> list = new ArrayList<Map<String,
		 * Object>>(); Map<String, Object> map = new HashMap<String, Object>();
		 * map.put("state", "1"); map.put("id", "1"); map.put("jCompany",
		 * "江苏怡丰通信设备有限公司");//公司名称 map.put("jAddress", "扬州市广陵区元辰路1号");//公司地址
		 * map.put("jWage", "2000");//薪资 map.put("jPost", "开发工程师");//岗位
		 * map.put("jDate", "2012-08-31");//日期 map.put("jTime",
		 * "2012-08-31 9:30");//时间 map.put("jProbation", "3个月");//试用期
		 * map.put("jNumber", "3");//招聘人数 map.put("jFlag",
		 * "待确认");//状态（待确认，已确认，投递失败） list.add(map);
		 * 
		 * map = new HashMap<String, Object>(); map.put("state", "1");
		 * map.put("id", "2"); map.put("jCompany", "天诚公司招工启示");
		 * map.put("jAddress", "广陵区沙头镇施沙路2号"); map.put("jWage", "2500");//薪资
		 * map.put("jPost", "测试工程师");//岗位 map.put("jDate", "2012-08-25");//日期
		 * map.put("jTime", "2012-08-30 12:30");//时间 map.put("jProbation",
		 * "3个月");//试用期 map.put("jNumber", "5");//招聘人数 map.put("jFlag",
		 * "已确认");//状态（待确认，已确认，投递失败） list.add(map);
		 * 
		 * map = new HashMap<String, Object>(); map.put("state", "1");
		 * map.put("id", "3"); map.put("jCompany", "金泉网招聘信息");
		 * map.put("jAddress", "广陵信息产业基地12幢C座1层"); map.put("jWage", "3000");//薪资
		 * map.put("jPost", "销售人员");//岗位 map.put("jDate", "2012-08-15");//日期
		 * map.put("jTime", "2012-08-25 9:30");//时间 map.put("jProbation",
		 * "2个月");//试用期 map.put("jNumber", "10");//招聘人数 map.put("jFlag",
		 * "投递失败");//状态（待确认，已确认，投递失败） list.add(map); return list;
		 */
	}

	/***
	 * 试用通知
	 * 
	 * @param userId
	 * @param type
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
		Log.d(TAG, "试用通知： "+"接口： "+"android/sending/listSending");
		Log.d(TAG, "试用通知： "+"参数： "+params.toString());
		Log.d(TAG, "试用通知： "+"返回值： "+json);
		return DataConvert.toConvertObjectList(json, "sendings");

	}

	/***
	 * 面试通知
	 * @param userId
	 * @param type
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> dointerviewInvitation(String userId, int pageNum) {

		this.setUrl(Constants.IP + "android/myjobcloud/interviewInvitation");
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", pageNum + "");
		params.put("YHM", userId);
		String json = this.doGet(params);
		Log.d(TAG, "面试通知： "+"接口： "+"android/myjobcloud/interviewInvitation");
		Log.d(TAG, "面试通知： "+"参数： "+params.toString());
		Log.d(TAG, "面试通知： "+"返回值： "+json);
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
		Log.d(TAG, "取消投递： "+"接口： "+"android/sending/revokeSending");
		Log.d(TAG, "取消投递： "+"参数： "+params.toString());
		Log.d(TAG, "取消投递： "+"返回值： "+json);
		return DataConvert.toMap(json);
	}

}
