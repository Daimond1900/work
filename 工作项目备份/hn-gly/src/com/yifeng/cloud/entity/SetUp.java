package com.yifeng.cloud.entity;
/***
 * 系统配置信息
 * @author WuJiaHong
 *
 */
public class SetUp {
	private boolean isUse;//是否使用短信换醒程序功能
	private String isSatart;//直接启动还是弹窗确认
	private String smsContent;//短信过滤提醒内容
	private boolean isOpenMeeting;//是否开通会议提醒
	private boolean isOpenMeetingAlarm;//是否开启默认会议闹钟提醒
	private String startActivity;//设置登录成功后启动项
	private boolean isFirstUse;//是否第一次使用系统,如果是第一次使用需自动发送短信网关去验证
	private boolean isEdu;//是否开启校园新闻提醒
	private boolean isDept;//是否开启院系公告提醒
	private boolean isTea;//是否开启教师公告提醒
	private boolean isStu;//是否开启班级公告提醒
	public boolean isEdu() {
		return isEdu;
	}

	public void setEdu(boolean isEdu) {
		this.isEdu = isEdu;
	}

	public boolean isDept() {
		return isDept;
	}

	public void setDept(boolean isDept) {
		this.isDept = isDept;
	}

	public boolean isTea() {
		return isTea;
	}

	public void setTea(boolean isTea) {
		this.isTea = isTea;
	}

	public boolean isStu() {
		return isStu;
	}

	public void setStu(boolean isStu) {
		this.isStu = isStu;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}

	public boolean isFirstUse() {
		return isFirstUse;
	}

	public void setFirstUse(boolean isFirstUse) {
		this.isFirstUse = isFirstUse;
	}

	public boolean isOpenMeeting() {
		return isOpenMeeting;
	}
	
	public void setOpenMeeting(boolean isOpenMeeting) {
		this.isOpenMeeting = isOpenMeeting;
	}
	
	public String getIsSatart() {
		return isSatart;
	}
	public void setIsSatart(String isSatart) {
		this.isSatart = isSatart;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public boolean isOpenMeetingAlarm() {
		return isOpenMeetingAlarm;
	}
	public void setOpenMeetingAlarm(boolean isOpenMeetingAlarm) {
		this.isOpenMeetingAlarm = isOpenMeetingAlarm;
	}

	public String getStartActivity() {
		return startActivity;
	}

	public void setStartActivity(String startActivity) {
		this.startActivity = startActivity;
	}

}
