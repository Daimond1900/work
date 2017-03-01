package com.yifeng.entity;
/**
 * 用户实体类 用户Id,用户名称，用户密码，所属部门，所属权限
 * 
 * @author 吴家宏
 * 
 * 2011年4月20日
 */
public class User {
	private  int state;//查询状态   -1服务器异常，0找不到，1加载成功，2数据解析异常
	private  String userId;
	private  String userName;
	private  String userPwd;
	private  String department;
	private  String department_lab;
	private  String roleId;
	private  String roleId_lab;
	private  String key;
	private  String imsi;
	private String job;//职务
	private String role_type;//角色类型
	private String   mobile_no;
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getRole_type() {
		return role_type;
	}
	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String orgId) {
		org_id = orgId;
	}
	private  String org_id;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public User(){
	}
	public User(User user){
		this.userId=user.userId;
		this.userName=user.userName;
		this.userPwd=user.userPwd;
		this.department=user.department;
		this.roleId=user.roleId;
		this.department_lab=user.department_lab;
		this.roleId_lab=user.roleId_lab;
	}

	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public String getDepartment() {
		return department;
	}
	public String getRoleId() {
		return roleId;
	}
	public String getRoleId_lab() {
		return roleId_lab;
	}
	public String getDepartment_lab() {
		return department_lab;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setDepartment_lab(String departmentLab) {
		department_lab = departmentLab;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setRoleId_lab(String roleIdLab) {
		roleId_lab = roleIdLab;
	}
	/**
	*  -1服务器异常，0找不到，1加载成功,2数据解析异常
	* @return
	 */
	public int getState() {
		return state;
	}
   /**
	*  -1服务器异常，0找不到，1加载成功,2数据解析异常
	* @return
	*/
	public void setState(int state) {
		this.state = state;
	}
	
}
