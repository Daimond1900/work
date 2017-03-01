package com.yifeng.data;
import java.util.List;
import java.util.Map;
import com.yifeng.util.DataConvert;
import android.content.Context;
/***
 * 政策动态 及待办事宜数据列表
 * @author WuJiaHong
 *
 */
public class ReadyToDoDal extends BaseDAL{

	public ReadyToDoDal(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 动态区域列表
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> doArea(){
		this.setUrl(this.getIpconfig()+"android/zcdt/doGetList");
		String json=this.doGet(null);
		return DataConvert.toArrayList(json);
	}
	
	/**
	 * 动态区域详情列表
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> doQuery(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/zcdt/doGetTypeList");
		String json=this.doGet(map);
		return DataConvert.toArrayList(json);
	}
	
	/**
	 * 当前自己管辖部门
	 * type 1：待办；2：超时；3：已办结
	 *  role_id
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> doMyDept(Map<String,String> map,String type,String role_id){
		this.setUrl(this.getIpconfig()+"android/form/doGetOrgList");
		String json=this.doGet(map);
		return DataConvert.toArrayList(json);
	}
	
	public String doMyDept(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/wsform/doGetOrgList");
		String json=this.doGet(map);
		return json;
	}
	
	
	/**
	 * 待办事宜
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> doQueryUndealedForms(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/wsform/doQueryUndealedForms");
		String json=this.doGet(map);
		return DataConvert.toArrayList(json);
	}
	
	/**
	 * 已办事宜
	 * @param map
	 * @return
	 */
	public String doQueryUndealedForms1(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/wsform/doQueryDealedForms");
		return this.doGet(map);
	}
 
}
