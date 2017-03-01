package com.yifeng.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.yifeng.util.DataConvert;

/**
 * 表单处理
 * 
 * @author Administrator
 * 
 */
public class FormDAL extends BaseDAL {

	public FormDAL(Context context) {
		super(context);
	}

	/**
	 * 待办工单列表数据
	 * 
	 * @param org_id
	 *            所选部门id
	 * @param page
	 *            当前页 从0开始
	 * @param form_type
	 *            工单类型
	 * @param user_id
	 *            用户id
	 * @return 数据结果集
	 */
	public String doQueryUndealedForms(String org_id,
			String page, String form_type, String user_id,String role_id ) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("org_id", org_id);
		map.put("page", page);
		map.put("role_id", role_id);
		
		map.put("form_type", form_type);
		map.put("user_id", user_id);

		this.setUrl(this.getIpconfig() + "android/wsform/doQueryUndealedForms");
		String jstring = this.doGet(map);
		return jstring;
	}

	/**
	 * 新增关注
	 * 
	 * @param form_id
	 *            工单id
	 * @param role_type
	 *            人物角色
	 * @param user_id
	 *            用户id
	 * @return 返回 ALREADYCONCERNED 已存在 SUCCESS 成功 FAIL 失败
	 */
	public String doAddConcern(String form_id, String role_type, String user_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("form_id", form_id);
		map.put("role_type", role_type);
		map.put("user_id", user_id);
		this.setUrl(this.getIpconfig() + "android/concern/doAddConcern");
		String jstring = this.doGet(map);

		return jstring;
	}

	/**
	 * 取消关注
	 * 
	 * @param form_id
	 *            工单id
	 * @param user_id
	 *            用户id
	 * @return 数据结果集
	 * @return 已存在 SUCCESS 成功 FAIL 失败
	 */
	public String doCancelConcern(String form_id, String user_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("form_id", form_id);
		map.put("user_id", user_id);
		this.setUrl(this.getIpconfig() + "android/concern/doCancelConcern");
		String jstring = this.doGet(map);
		return jstring;
	}

	/**
	 * 获取我的关注
	 * 
	 * @param user_id
	 *            用户id
	 * @param form_type
	 *            工单类型
	 * @return list
	 */
	public List<Map<String, String>> doQueryMyConcern(String user_id,
			String form_type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("form_type", form_type);

		this.setUrl(this.getIpconfig() + "android/concern/doQueryMyConcern");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	/**
	 * 根据org_id获取人员信息
	 * 
	 * @param org_id
	 * @return 人员名单
	 * 
	 */

	public List<Map<String, String>> doQueryTargetList(String org_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("org_id", org_id);
		this.setUrl(this.getIpconfig() + "android/assign/doQueryTargetList");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}
	
	/**
	 * 获取人员信息(工单转派)
	 * 
	 * @param org_id
	 * @return 人员名单
	 * 
	 */

	public List<Map<String, String>> doQueryUserList(Map<String, String> map,String url) {
		
		this.setUrl(this.getIpconfig() + url);
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	/**
	 * 交办给某个人
	 * 
	 * @param form_id
	 *            工单id
	 * @param user_id
	 * @param assign_comment
	 *            交办内容
	 * @param target_user_id
	 *            交办给谁
	 * @param role_type
	 *            登陆人
	 */
	public String doAddAssign(String form_id, String user_id,
			String assign_comment, String target_user_id, String role_type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("form_id", form_id);
		map.put("user_id", user_id);
		map.put("assign_comment", assign_comment);
		map.put("target_user_id", target_user_id);
		map.put("role_type", role_type);
		this.setUrl(this.getIpconfig() + "android/assign/doAddAssign");
		String jstring = this.doGet(map);
		return jstring;
	}
	
	/**
	 * 工单转派
	 */
	public String doAddDispatch(Map<String, String> map,String url) {
		this.setUrl(this.getIpconfig() + url);
		String jstring = this.doGet(map);
		return jstring;
	}

	/**
	 * 获取领导批示工单
	 * 
	 * @param user_id
	 * @return 人员名单
	 * 
	 */

	public List<Map<String, String>> doQueryAssignedFormList(String userid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", userid);
		this.setUrl(this.getIpconfig() + "android/assign/doQueryAssignList");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	/**
	 * 超时工单列表数据
	 * 
	 * @param org_id
	 *            所选部门id
	 * @param page
	 *            当前页 从0开始
	 * @param form_type
	 *            工单类型
	 * @param user_id
	 *            用户id
	 * @return 数据结果集
	 */
	public String doQueryOvertimeForms(String org_id,
			String page, String form_type, String user_id,String role_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("org_id", org_id);
		map.put("page", page);
		map.put("form_type", form_type);
		map.put("user_id", user_id);
		map.put("role_id", role_id);
		this.setUrl(this.getIpconfig() + "android/wsform/doQueryOvertimeForms");
		return this.doGet(map);
	}

	/**
	 * 查询所有超时工单列表数据
	 * 
	 * @param org_id
	 *            所选部门id
	 * @param page
	 *            当前页 从0开始
	 * @param form_type
	 *            工单类型
	 * @param user_id
	 *            用户id
	 * @return 数据结果集
	 */
	public List<Map<String, String>> doQueryOvertime(String page, String user_id,String role_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", page);
		map.put("user_id", user_id);
		map.put("role_id", role_id);
		this.setUrl(this.getIpconfig()
				+ "android/form/doQueryOvertimeFormsForHomepage");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	/**
	 * 领导批示签收
	 * 
	 * @param assign_id
	 *            批示工单id
	 * @return 数据结果集
	 */
	public String doReceiveAssign(String assign_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("assign_id", assign_id);
		this.setUrl(this.getIpconfig() + "android/assign/doReceiveAssign");
		String jstring = this.doGet(map);
		return jstring;
	}

	/**
	 * 领导批示回复
	 * 
	 * @param reply_comment
	 *            回复内容
	 * @param assign_id
	 *            回复工单id
	 * @return 数据结果集
	 */
	public String doReplyAssign(String reply_comment, String assign_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("reply_comment", reply_comment);
		map.put("assign_id", assign_id);

		this.setUrl(this.getIpconfig() + "android/assign/doReplyAssign");
		String jstring = this.doGet(map);
		return jstring;
	}
	
	/**
	 * 领导批示回复(工单派发)
	 * 
	 * @param reply_comment
	 *            回复内容
	 * @param assign_id
	 *            回复工单id
	 * @return 数据结果集
	 */
	public String doReplyDispatch(String reply_comment, String dispatch_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("reply_comment", reply_comment);
		map.put("dispatch_id", dispatch_id);

		this.setUrl(this.getIpconfig() + "android/dispatch/doReplyDispatch");
		String jstring = this.doGet(map);
		return jstring;
	}
	
	/**
	 * 获取已回复工单的回复详情
	 * @param user_id 用户id
	 * @param form_id  工单id
	 * @return
	 */
	public List<Map<String, String>> doQueryMyAssignByForm(String user_id,String form_id){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("form_id", form_id);

		this.setUrl(this.getIpconfig()
				+ "android/assign/doQueryMyAssignByForm");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
		
	}
	/**
	 * 获取政策法规条目
	 * @return
	 */
	public List<Map<String, String>> doGetPolicyList( ){
		
		Map<String, String> map = new HashMap<String, String>();
		 

		this.setUrl(this.getIpconfig()
				+ "android/policy/doGetPolicyList");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
		
	}
	/**
	 * 获取政策法规详情
	 * @return
	 */
	public List<Map<String, String>> doGetPolicyDetail(String address){
		
		Map<String, String> map = new HashMap<String, String>();
		 
		map.put("address", address);
		this.setUrl(this.getIpconfig()
				+ "android/policy/doGetPolicyDetail");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
		
	}
	/**
	 * 查询热线待办的各项条数
	 * @param user_id
	 * @param role_id    角色类型 
	 * @param org_id  部门编号
	 * @return
	 */
	public Map<String, String> doCountMass(String user_id ,String role_id,String org_id){
		
		Map<String, String> map = new HashMap<String, String>();
		 
		map.put("user_id", user_id);
		map.put("role_id", role_id);
		map.put("org_id", org_id);
		this.setUrl(this.getIpconfig()
				+ "android/wsform/doCountMass");
		String jstring = this.doGet(map);
		return DataConvert.toMap(jstring);
		
	}
	
	/**
	 * 工单转派，代办事宜（已派,未派）
	 * 
	 */
	public List<Map<String, String>> doQueryForms(Map<String,String> map,String url) {
		this.setUrl(this.getIpconfig() + url);
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}
	
	
	/**
	 * 新增关注
	 * 获取 已派，未派 （条数）
	 */
	public Map<String, String> doGetCount(String user_id,String dept_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", user_id);
		map.put("org_id", dept_id);
		this.setUrl(this.getIpconfig() + "android/dispatch/doGetCounts");
		String json = this.doGet(map);
		return DataConvert.toMap(json);
	}
	
	
	/**
	 * 回访不满意工单
	 */
	public String doQueryNot(String page, String org_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", page);
		map.put("org_id", org_id);
		this.setUrl(this.getIpconfig() + "android/wsform/doQueryNotSatisfyFrom");
		return this.doGet(map);
	}
}
