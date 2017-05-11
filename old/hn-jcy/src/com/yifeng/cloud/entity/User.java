package com.yifeng.cloud.entity;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 存放用户信息
 * 
 * @author WuJiaHong 2012-03-09
 */
public class User {
	private int state = 0;// 查询状态 -1服务器异常，0找不到，1加载成功，2数据解析异常
	private String userId;
	private String userName;
	private String userPwd;
	private String pwd;// 明码
	private String userTel;
	private String mobileNo;
	private String pic_path;
	private String duty;
	private String userEmail;
	private String userGrade;
	private String userRole;
	private String departmentid;
	private String companyName;
	private String key;
	private String cardId;
	private String address;
	private String ehome;
	private String card_style;
	private String cookie;

	private String group;
	private String group_id;
	private String group_name;
	private String logo_path;
	public static Bitmap userHead = null;
	private String oaGroup;
	private boolean hasOa;
	private String oaIp;
	private String oaToken;
	private int isLocked = 0;
	private int group_locked = 0;
	private int card_locked = 0;
	private boolean success = true;
	private String msg;
	// :{"aaa021":"江苏省", 区域名称
	// "aaa020":"320000000000", 区域代码
	// "zzs051":"江苏省劳动就业管理中心 ", 经办机构名称
	// "operatorname":"管理员", 用户姓名
	// "org":"3200000000000000", 经办机构代码
	// "userid":"admin"} 用户id
	private String aaa021;
	private String aaa020;
	private String zzs051;
	private String org;
	private String operatorname;
	private String userid;
	
	private String zbdw; //主办单位

	public String getZbdw() {
		return zbdw;
	}

	public void setZbdw(String zbdw) {
		this.zbdw = zbdw;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getAddress() {
		return address;
	}

	public static Bitmap getUserHead() {
		return userHead;
	}

	public static void setUserHead(Bitmap userHead) {
		User.userHead = userHead;
	}

	public String getAaa021() {
		return aaa021;
	}

	public void setAaa021(String aaa021) {
		this.aaa021 = aaa021;
	}

	public String getAaa020() {
		return aaa020;
	}

	public void setAaa020(String aaa020) {
		this.aaa020 = aaa020;
	}

	public String getZzs051() {
		return zzs051;
	}

	public void setZzs051(String zzs051) {
		this.zzs051 = zzs051;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String picPath) {
		pic_path = picPath;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
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

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEhome() {
		return ehome;
	}

	public void setEhome(String ehome) {
		this.ehome = ehome;
	}

	public String getCard_style() {
		return card_style;
	}

	public void setCard_style(String cardStyle) {
		card_style = cardStyle;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getGroup() {
		return group;
	}

	public String getOaIp() {
		return oaIp;
	}

	public void setOaIp(String oaIp) {
		this.oaIp = oaIp;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String groupId) {
		group_id = groupId;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String groupName) {
		group_name = groupName;
	}

	public String getLogo_path() {
		return logo_path;
	}

	public void setLogo_path(String logoPath) {
		logo_path = logoPath;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public static Bitmap getUserHead(Context context) {
		return userHead;
	}

	public boolean isHasOa() {
		return hasOa;
	}

	public void setHasOa(boolean hasOa) {
		this.hasOa = hasOa;
	}

	public String getOaToken() {
		return oaToken;
	}

	public void setOaToken(String oaToken) {
		this.oaToken = oaToken;
	}

	public String getOaGroup() {
		return oaGroup;
	}

	public void setOaGroup(String oaGroup) {
		this.oaGroup = oaGroup;
	}

	public int getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(int isLocked) {
		this.isLocked = isLocked;
	}

	public int getGroup_locked() {
		return group_locked;
	}

	public void setGroup_locked(int groupLocked) {
		group_locked = groupLocked;
	}

	public int getCard_locked() {
		return card_locked;
	}

	public void setCard_locked(int cardLocked) {
		card_locked = cardLocked;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
