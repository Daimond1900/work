package com.yifeng.hnzpt.util;

import android.os.Environment;

/**
 * comment:常量操作类
 * 
 * @author:ZhangYan Date:2012-7-24
 */
public class ConstantUtil {
	// 查询状态
	// :SERVER_ERROR=-2秘钥错误SERVER_ERROR=-1服务器异常，LOGIN_FAIL=0找不到，LOGIN_SUCCESS=1加载成功，INNER_ERROR=2数据解析异常
	public static final int KEY_ERROR = -2;
	public static final int SERVER_ERROR = -1;
	public static final int LOGIN_FAIL = 0;
	public static final int LOGIN_FAIL1 = -3;
	public static final int LOGIN_SUCCESS = 1;
	public static final int INNER_ERROR = 2;
	public static final int IS_EMPTY = 0;// 空值
	public static final int DATA_NULL = 4;// 返回null

	/**
	 * 项目所有图片
	 */
	public static final String IMAGE_CAPTURE = Environment
			.getExternalStorageDirectory() + "/hnzpt";
	/**
	 * 热门事件图片
	 */
	public static final String HOT_EVENT = IMAGE_CAPTURE + "/zpt";
	public static final String Hot_EVENT_DIR = "/hnzpt/zpt/";
	
//    public static final String ip = "http://192.168.66.40:8080/hnjy/";
	public static final String ip = "http://124.225.113.8:8421/hnjyy/";

    /**自动更新放到内存*/
	public static final String SYS_PACK_NAME = "com.yifeng.hnzpt"; 
	public static final String downapk = ip + "download/hnzpt.apk";
	public static final String downtxt = ip + "download/hnzpt.txt";

	// 程序互调密钥
	public static final String AUTH_KEY = "EJOB2012";
	// 标志是否是管理版进来的 默认为false
	public static boolean ISEJOB = false;
	// 是否登陆
	public static boolean IFLOGIN = false;

}
