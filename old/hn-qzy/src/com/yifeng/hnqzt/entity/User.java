package com.yifeng.hnqzt.entity;

/**
 * comment:存放用户信息
 * @author:吴家宏
 * Date:2012-8-17
 */
public class User {
	private int state;// 查询状态 -1服务器异常，0找不到，1加载成功，2数据解析异常
	private String mobileNo;// 手机号码
	private String key;
	private String imsi;
	private String publicKey;// 公钥
	private String userName="";//用户名
    private String userId="";//登录名
	private String userPwd="";//密码
	private boolean isAutoLogin=false;//是否设置自动登录
	private boolean remeberPwd=false;//是否记住密码
    private String serviceMsg="";
    private String userPic="";//头像路径
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

	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
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

	private String title; //首页显示文字
	private String areas; //区域
    private String deptId; //部门id
    private String deptName; //部门名称
	
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

	public String getImsi() {
		return imsi;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	

	public boolean isAutoLogin() {
		return isAutoLogin;
	}

	public void setAutoLogin(boolean isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public boolean isRemeberPwd() {
		return remeberPwd;
	}

	public void setRemeberPwd(boolean remeberPwd) {
		this.remeberPwd = remeberPwd;
	}

	public String getServiceMsg() {
		return serviceMsg;
	}

	public void setServiceMsg(String serviceMsg) {
		this.serviceMsg = serviceMsg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
}
