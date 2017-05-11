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
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;
import com.winksoft.android.yzjycy.util.ReJson;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * 培训 
 * 考勤
 * @author ZK 
 */
public class pxDAL extends BaseDAL {

	public pxDAL(Context context, Handler handler) {
		super(context, handler);
	}
	
	/**
	 * 培训信息列表
	 */
	public List<Map<String, String>> doPxInfoQuery(String selectStr) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("class_name", selectStr);
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/listTrainingCourse.do");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json, "attendance").getList();
	}
	
	/**
	 * 培训信息详情
	 */
	public String doDetailsInfoQuery(String class_id,String userid) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("class_id", class_id);
		param.put("userid", userid);
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/viewTrainingCourse");//?class_id=100002&userid=p003
		String json = this.doGet(param);
		return json!=null && !"".equals(json) ? json:"";
	}
	
	/**
	 * 验证个人信息
	 */
	public Map<String, String> doVerifyUserinfor(String userid) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/verifyPersonalInfo.do");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
	
	/**
	 * 报名
	 */
	public Map<String, String> doBm(String userid,String class_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);
		param.put("class_id", class_id);
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/enrollTrainingCourse.do");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
	/**
	 * 报名信息确认
	 */
	public Map<String, String> doCompletePersonalInfo(String userid,String name,String idcard,int sex) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);
		param.put("name", name);
		param.put("idcard", idcard);
		param.put("sex", sex+"");
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/completePersonalInfo.do");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
	
	/**
	 *  考勤信息列表
	 */
	public List<Map<String, String>> doKqInfoQuery(String keyword,String userid) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("keyword", keyword);		
		param.put("userid", userid);		
//		this.setUrl(this.getIpconfig() + "/android/listTrainingCourse.do");
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/enrolledTrainingList.do");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json, "enrolledTraningList").getList();
	}
	
	/**
	 *  考勤信息详情
	 */
	public String doKqInfoDetails(String userid,String class_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);		// key 未定
		param.put("class_id", class_id);		// key 未定
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/viewCheckOutIn.do");
		String json = this.doGet(param);
//		return new ReJson(context, handler).setValue(json, "enrolledTraningList").getMap();
		return json!=null && !"".equals(json) ? json:"";
	}
	
	/**
	 * 考勤统计列表
	 */
	public List<Map<String, String>> doKqRecordQuery(String userid,String class_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);		// key 未定
		param.put("class_id", class_id);		// key 未定
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/hisCheckOutInInfo.do");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json, "checkOutInInfo").getList();
	}
	
	/**
	 * 是否可以考勤
	 */
	public Map<String, String> isKqInfo(String userid,String class_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);		// key 未定
		param.put("class_id", class_id);		// key 未定
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/verifyCheckInTime.do");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
	
	/**
	 * 是否可以签退
	 */
	public Map<String, String> isQtInfo(String userid,String class_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userid);		// key 未定
		param.put("class_id", class_id);		// key 未定
		this.setUrl("http://192.168.66.244:8080/yzjycy/android/attendance/verifyCheckOutTime.do");
		String json = this.doGet(param);
		return DataConvert.toMap(json);
	}
	
	/**
	 * 考勤
	 */
	public Map<String, String> doPostKq(Map<String, String> params, FormFile[] files) {
		String postUrl = "";
		Map<String, String> returnMap = new HashMap<String, String>();
//		postUrl = Constants.IP+ "android/person/modifyResume";
		postUrl = "http://192.168.66.244:8080/yzjycy/android/attendance/doCheckOutIn.do";
		
		try {
			String json = FileUtils.post(postUrl, params, files);
			returnMap = DataConvert.toMap(json);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("state", String.valueOf(0));
			returnMap.put("success", "false");
			returnMap.put("msg", "信息提交失败!");
		}
		return returnMap;
	}
}
