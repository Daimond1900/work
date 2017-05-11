package com.yifeng.hnqzt.util;

import android.os.Environment;

/**
 * comment:常量操作类
 * @author:ZhangYan
 * Date:2012-7-24
 */
public class ConstantUtil {
	public static final int KEY_ERROR=-2;
	public static final int SERVER_ERROR=-1;
	public static final int LOGIN_FAIL=0;
	public static final int LOGIN_FAIL1=-3;
	public static final int LOGIN_SUCCESS=1;
	public static final int INNER_ERROR=2;
	public static final int IS_EMPTY=0;//空值
	public static final int DATA_NULL=4;//返回null
	public static final int EXISTS=4;//存在
	public static final int LOCKED=1;//锁定
	public static final int GROUP_LOCKED=5;//群组平台锁定
	public static final int SYS_LOCKED=6;//系统锁定
	public static final int WRONG_ID_OR_PWD=7;//用户名密码错误
	
	public static final int OA_LOGIN_FAIL=8;//OA登陆失败
	
	public static final String PWD = "PWD";//记住密码
	
	public static String masterPassword = "1234567890agdfsr";
	/**
	 * 项目所有图片
	 */
	public static final String IMAGE_CAPTURE = Environment.getExternalStorageDirectory()+"/hnqzt";
	/**
	 *热门事件图片
	 */
	public static final String HOT_EVENT = IMAGE_CAPTURE+"/qzt";
	public static final String Hot_EVENT_DIR = "/hnqzt/qzt/";
	
	public static final String ip = "http://124.225.113.8:8421/hnjyy/";
//	public static final String ip = "http://192.168.66.40:8080/hnjy/";
	
	/**自动更新放到内存*/
	public static final String SYS_PACK_NAME = "com.yifeng.hnqzt"; 
	public static final String downapk=ip+"download/hnqzt.apk";
	public static final String downtxt=ip+"download/hnqzt.txt";
	
	
	//标志是否是管理版进来的 默认为false
	public static boolean ISEJOB=false;
	//程序互调密钥
	public static final String AUTH_KEY="EJOB2012";
	
	//登录状态
	public static boolean isLogin=false;
	
}
