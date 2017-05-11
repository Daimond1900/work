package com.yifeng.hnzpt.entity;

/**
 * comment:存放用户信息
 * @author:ZhangYan 
 * Date:2012-8-17
 */
public class User {
	private int state;// 查询状态 -1服务器异常，0找不到，1加载成功，2数据解析异常
	private String imsi;
	private String key;
	private String mobileNo;// 手机号码
	private String publicKey;// 公钥
	private String userId;// 用户Id（登录名）
	private String userName;// 用户名
	private String userPwd;// 密码
	private boolean rememberPwd = false;// 是否记住密码
	private String companyId;// 公司Id
	private String companyName;// 公司名称
	private String companyManager;// 公司法人
	private String longitude;// 经度
	private String latitude; // 纬度
	private String linkAddress;//联系地址
	private String linkPhone;//联系电话
	private String zbdw; //主办单位
	
    public String getZbdw() {
		return zbdw;
	}

	public void setZbdw(String zbdw) {
		this.zbdw = zbdw;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}
	
	private String title; //首页显示文字
	private String areas; //区域
	private String deptId; //部门id
    private String deptName; //部门名称
	

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public boolean isRememberPwd() {
		return rememberPwd;
	}

	public void setRememberPwd(boolean rememberPwd) {
		this.rememberPwd = rememberPwd;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getcompanyManager() {
		return companyManager;
	}

	public void setcompanyManager(String companyManager) {
		this.companyManager = companyManager;
	}

	/* ===================== 经度 ===================== */
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/* ===================== 纬度 ===================== */
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
