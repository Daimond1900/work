package com.yifeng.util;
/**
 * 常量操作类
 * @author 吴家宏
 *  2011-4-27
 */
public class ConstantUtil {
	//查询状态 SERVER_ERROR=-2秘钥错误   SERVER_ERROR=-1服务器异常，LOGIN_FAIL=0找不到，LOGIN_SUCCESS=1加载成功，INNER_ERROR=2数据解析异常，DATA_NULL=4返回null
	public static final int KEY_ERROR=-2;
	public static final int SERVER_ERROR=-1;
	public static final int LOGIN_FAIL=0;
	public static final int LOGIN_FAIL1=-3;
	public static final int LOGIN_SUCCESS=1;
	public static final int INNER_ERROR=2;
	public static final int IS_EMPTY=0;//空值
	public static final int DATA_NULL=4;//返回null'
	public static boolean STATE = true;
	public static boolean iflogin=false;
	
	/**短信换醒应用程序**/
	public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
	public static final String SEND_SMS_NUM = "051489712345";
	/**开机广播路径*/
	public static final String BOOT_COMPLETED="android.intent.action.BOOT_COMPLETED";
	/**短信过滤路径*/
	public static final String SMS_FILTER_ACTION="com.yifeng.12345Cloud.filterSms";
	public static boolean YFCloudSmsServiceDoing=false;//YFCloudSmsService是否正在启动 ，启动为true
}
