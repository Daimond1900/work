package com.yifeng.hnjcy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;

import com.yifeng.hnjcy.util.DataConvert;

/**
 * comment:招聘信息
 * @author:ZhangYan
 * Date:2012-8-30
 */
public class EnterpriseDal extends BaseDAL {

	public EnterpriseDal(Context context, Handler handler) {
		super(context, handler);
	}
	/***
	 * 查询企业信息列表
	 * @param pageNum
	 * @return
	 */
	public  List<Map<String,Object>> doPostQuery(String pageNum,String aab004,String aae017){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", pageNum));
		params.add(new BasicNameValuePair("aab004",aab004));//企业名称
		params.add(new BasicNameValuePair("aae017", aae017));//县市编号
		this.setUrl(this.getIpconfig() +"android/corecruitment/listCompany");
		String json = this.doPost(params);
		return DataConvert.toConvertObjectList(json, "companies");
	}
	/***
	 * 查询公司所有职位
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> doQueryPosition(String companyId,int pageNum){
		this.setUrl(this.getIpconfig()+"android/corecruitment/listRecruitInfo");
		Map<String, String> params = new HashMap<String, String>();
		params.put("aab001",companyId);
		params.put("page",pageNum+"");
		String json=this.doGet(params);
		return DataConvert.toConvertObjectList(json,"recruitments");
	}
	
	public List<Map<String, Object>> doQuery(){
		/*this.setUrl(this.getIpconfig()+url);
		String json=this.doGet(params);
		return DataConvert.toArrayList(json);*/
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
			map.put("state", "1");
			map.put("id", "1");
			map.put("companyName", "美工(2)");
			map.put("qz_count", "2");//岗位数
			map.put("gw_count", "3000");//求职数
			map.put("gz_count", "1");//关注数
			list.add(map);
			return list;
		
		
	}
	
	/***
	 * 公司详细
	 * @param positionId
	 * @return
	 */
	public Map<String,String> getEnterPrise(String companyId){
		Map<String,String> param=new HashMap<String,String>();
		param.put("aab001", companyId);
		this.setUrl(getIpconfig()+"android/corecruitment/queryCompanyDetail");
		String json=this.doGet(param);
		return DataConvert.toConvertStringMap(json, "company");
	}
	
	/****
	 * 获取周边招聘信息
	 * @param myLat 我的纬度
	 * @param myLng 我的经度
	 * @param limit 距离
	 * @param positionName 职位
	 * @param price 薪资
	 * 
	 * @return
	 */
	public List<Map<String,String>> loadMaps(int myLat,int myLng,int limit,String positionName,String price){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("myLat", myLat+""));
		params.add(new BasicNameValuePair("myLng",myLng+""));
		params.add(new BasicNameValuePair("limit",limit+""));
		params.add(new BasicNameValuePair("positionName",positionName));
		params.add(new BasicNameValuePair("price",price));
		this.setUrl(this.getIpconfig()+"android/compnaycloud/queryCompnayAround");
		String json=this.doPost(params);
		
		List<Map<String,String>> list=DataConvert.toConvertStringList(json, "data");
		/*String state=list.get(0).get("state");
		if(state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
			 for (int i = 0; i < list.size(); i++) {
				 Map<String,String> map=list.get(i);
				 map.put("companyId", map.get("aab001"));
				 map.put("companyName", map.get("aab004"));
			     map.put("telNo", map.get("aae005"));
			     map.put("linkMan", map.get("aab013"));
			     map.put("address", map.get("aae006"));
			     //map.put("lat", "3238801132515847");//纬度
			     //map.put("lng", "119457947015380");//经度
			     list.add(map);
			 }
			 
		}*/
		return list;
		
		
		/*
		 List<Map<String,String>> companys=new ArrayList<Map<String,String>>();
		
		 Map<String,String> map=new HashMap<String,String>();
		 map.put("state", "1");
	     map.put("companyName", "扬州电信扬州装维");
	     map.put("telNo", "0514-89989");
	     map.put("linkMan", "王先生");
	     map.put("address", "扬州五指山中路130号");
	     map.put("lat", "3238801132515847");//纬度
	     map.put("lng", "119457947015380");//经度
	     companys.add(map);
	     
	     map=new HashMap<String,String>();
	     map.put("state", "1");
	     map.put("companyName", "扬州新世纪酒店");
	     map.put("telNo", "0514-89989");
	     map.put("linkMan", "林先生");
	     map.put("address", "江阳中路240号");
	     map.put("lat", "323880733");
	     map.put("lng", "119443645");
	     companys.add(map);
	     
	     map=new HashMap<String,String>();
	     map.put("state", "1");
	     map.put("companyName", "扬州西园大酒店");
	     map.put("telNo", "0514-89989");
	     map.put("linkMan", "吴先生");
	     map.put("address", "扬州运河西路150号");
	     map.put("lat", "323860735322");
	     map.put("lng", "119434645121");
	     companys.add(map);
		
		 return companys;*/
		
	}
	
	
}
