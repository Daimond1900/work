package com.winksoft.android.yzjycy.util;

public class Constants {

//        public static final String IP = "http://222.189.216.110:8889/yzjycy/"; // 公司外网

//    public static final String IP = "http://139.196.124.232:8080/yzjycy/"; // 正式地址
    public static final String IP = "http://app.yzcyjy.com:8080/yzcyjy/"; // 正式地址

//    public static final String IP = "http://192.168.66.244:8080/yzjycy/"; //王梅君本地

    public static final String downapk = IP + "down/yzcyjy.apk";
    public static final String downtxt = IP + "down/version.txt";

    public static boolean iflogin = false;

    public static final class BROADCAST_FILTER {
        public static final String REFRESH_MENUS = "refresh_menus";
    }

    /**
     * 重新整理后添加的
     */
    public static final int KEY_ERROR = -2;
    public static final int SERVER_ERROR = -1;
    public static final int LOGIN_FAIL = 0;
    public static final int LOGIN_FAIL1 = -3;
    public static final int LOGIN_SUCCESS = 1;
    public static final int INNER_ERROR = 2;
    public static final int IS_EMPTY = 0;// 空值
    public static final int DATA_NULL = 4;// 返回null
    public static final int EXISTS = 4;// 存在
    public static final int LOCKED = 1;// 锁定
    public static final int GROUP_LOCKED = 5;// 群组平台锁定
    public static final int SYS_LOCKED = 6;// 系统锁定
    public static final int WRONG_ID_OR_PWD = 7;// 用户名密码错误
    public static final int OA_LOGIN_FAIL = 8;// OA登陆失败
    public static final String PWD = "PWD";// 记住密码
    public static String masterPassword = "1234567890agdfsr";

}
