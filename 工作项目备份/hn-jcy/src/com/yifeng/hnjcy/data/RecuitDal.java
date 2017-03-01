package com.yifeng.hnjcy.data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.DataConvert;

import android.content.Context;
import android.os.Handler;

/**
 * comment:招聘信息
 * @author:吴家宏
 * Date:2012-9-17
 */
public class RecuitDal extends BaseDAL {

	public RecuitDal(Context context,Handler handler) {
		super(context,handler);
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
		this.setUrl(this.getIpconfig()+ "android/corecruitment/listRecruitInfo");
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "recruitments");
	}
	
	public String doQuery(Map<String, String> map, String url){
		this.setUrl(ConstantUtil.ip + url);
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
		this.setUrl(getIpconfig()+"android/corecruitment/queryRecruitmentDetail");
		String json=this.doGet(param);
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
	public Map<String,String> doPostDelivery(String positionId,String userid,String userName,String companyId,String remark){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ACB210", positionId));//岗位编号
		params.add(new BasicNameValuePair("YHM", userid));//用户名
		params.add(new BasicNameValuePair("XM", userName));//姓名
		params.add(new BasicNameValuePair("AAB001", companyId));//公司编号
		params.add(new BasicNameValuePair("REMARK", remark));//描述
		this.setUrl(getIpconfig()+"android/searchingcloud/submitResume");
		String json = this.doPost(params);
		return DataConvert.toMap(json);
	}
	
}
