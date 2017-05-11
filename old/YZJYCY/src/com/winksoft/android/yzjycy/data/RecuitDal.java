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
import com.winksoft.android.yzjycy.util.StringHelper;

import android.content.Context;
import android.util.Log;

/**
 * comment:招聘信息
 * @author:吴家宏
 * Date:2012-9-17
 */
public class RecuitDal extends BaseDAL {
	private final String TAG = "zpxx";
	public RecuitDal(Context context) {
		super(context);
	}
	
	/****
	 *  查询招聘信息
	 * @param pageNum
	 * @param keyWords
	 * @return
	 */
	public  List<Map<String,Object>> doPostQuery(String pageNum,String aab004,String acb202,String acb216 ){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", pageNum + ""));
		params.add(new BasicNameValuePair("aab004", aab004)); //单位名称
		params.add(new BasicNameValuePair("acb202", acb202)); //县市编号
		params.add(new BasicNameValuePair("acb216", acb216)); //工种说明
		this.setUrl(Constants.IP+ "android/corecruitment/listRecruitInfo");
		String json = this.doPost(params);
		Log.d(TAG, "查询全部： "+"接口： android/corecruitment/listRecruitInfo");
		Log.d(TAG, "查询全部： "+"参数： "+params.toString());
		Log.d(TAG, "查询全部： "+"返回值： "+json);
		return DataConvert.toConvertObjectList(json, "recruitments");
	}
	
	public String doQuery(Map<String, String> map, String url){
		this.setUrl(Constants.IP + url);
		String json=this.doGet(map);
		return json;
		
	}
	
	/***
	 * 职位详细
	 * @param positionId
	 * @return
	 */
	public Map<String,String> getPosition(String positionId,String companyId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("acb210", positionId);//职位编号
		this.setUrl(Constants.IP+"android/corecruitment/queryRecruitmentDetail");
		String json=this.doGet(param);
		Log.d(TAG, "职位详细： "+"参数： "+param.toString());
		Log.d(TAG, "职位详细： "+"返回值： "+json);
		return DataConvert.toConvertStringMap(json, "recruitment");
	}
	
	/***
	 * 投递简历
	 * @param positionId
	 * @param userid
	 * @param userName
	 * @param companyId
	 * @param remark
	 * @return
	 */
	public Map<String,String> doPostDelivery(String positionId,String userid,String companyId,String remark){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("acb210", positionId));//岗位编号
		params.add(new BasicNameValuePair("id", userid));//用户名
		params.add(new BasicNameValuePair("aab001", companyId));//公司编号
		params.add(new BasicNameValuePair("remark", remark));//描述
		this.setUrl(Constants.IP+"android/sending/sendResume");
		String json = this.doPost(params);
		Log.d(TAG, "投递简历： "+"参数： "+params.toString());
		Log.d(TAG, "投递简历： "+"返回值： "+json);
		return DataConvert.toMap(json);
	}
	
}
