package com.yifeng.hnjcy.util;
import android.os.Environment;

/**
* 常量操作类
* @author 吴家宏
*  2011-4-27
*/
public class ConstantUtil {
	//查询状态   SERVER_ERROR=-1服务器异常，LOGIN_FAIL=0找不到，LOGIN_SUCCESS=1加载成功，INNER_ERROR=2数据解析异常
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
	/**没有头像字符串	 */
	public static final String NO_USER_HEAD_PIC="NO_USER_HEAD_PIC";
	/**没有图片	 */
	public static final String NO_PIC="NO_PIC";
	/**有图片	 */
	public static final String HAVE_PIC="HAVE_PIC";
	
	
	public static final String ip = "http://124.225.113.8:8421/hnjyy/";
//	public static final String ip = "http://192.168.66.40:8080/hnjy/";
	
 	/**是否登陆*/
    public static boolean IFLOGIN=false;
	/**自动更新放到内存*/
	public static final String SYS_PACK_NAME = "com.yifeng.hnjcy.ui"; 
	public static final String downapk=ip+"download/hnjcy.apk";
	public static final String downtxt=ip+"download/hnjcy.txt";
	//标志是否是管理版进来的 默认为false
	public static boolean ISEJOB=false;
	//程序互调密钥
	public static final String AUTH_KEY="EJOB2012";
	public static final String IMAGE_CAPTURE = Environment.getExternalStorageDirectory()+"/hnjcy";
	public static final String DAY_NEW = IMAGE_CAPTURE+"/dayNew";
	public static final String DAY_NEW_DIR = IMAGE_CAPTURE+"/dayNew/";
	 
}
