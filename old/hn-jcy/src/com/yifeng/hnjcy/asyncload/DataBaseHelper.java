package com.yifeng.hnjcy.asyncload;

import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * DataBaseHelper作为访问SQLite助手类提供两个全面的功能 第一个:
 * getReadableDatabase(),getWritableDatabase()可获得SQLiteDatabase对象
 * 第二个：onCreate和onUpgrade两个因调函数可以创建和更新数据库的版本
 * 
 * @author WuJiaHong
 *  2012-03-13
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "yfcloud_db";// 创建数据库名
	private static final int VERSION = 9;// 数据库版本 ,如果修改表结构版本要比原来高

	// 即时聊天短信表 T_SEND_MSG
	// id 自增长 主键 ,context=短信内容 ;receive_user=接收人 ;send_type 接收 还是发送
	// ;send_date=发送时间;msg_type=消息类型;video_url=视频
	private static final String T_SEND_MSG = " CREATE TABLE  IF NOT EXISTS  T_SEND_MSG (id integer PRIMARY KEY autoincrement, "
			+ "context varchar(500), receive_user varchar(50),user varchar(50)," +
					"send_type varchar(2),send_date varchar(20)," +
					"msg_type varchar(2),video_url varchar(100));";
	//文件共享下载管理:T_SHARE
	//id:自增长 主键,context=短信内容,title=标题,share_user=共享人,share_date=共享时间,down_date=下载时间,share_id=共享编号,file_size=文件大小,down_path=下载路径
	private static final String T_SHARE = " CREATE TABLE  IF NOT EXISTS  T_SHARE (id integer PRIMARY KEY autoincrement, "
		+ "context varchar(5000), title varchar(500),share_user varchar(50)," +
				"share_date varchar(30),down_date varchar(30)," +
				"share_id varchar(100),file_size varchar(50),down_path varchar(100));";
	//用户头像下载
	private static final String USER_HEAD_IMG = " CREATE TABLE  IF NOT EXISTS  USER_HEAD_IMG (id integer PRIMARY KEY autoincrement, "
		+ "NET_URL varchar(50), LOCAL_URL varchar(50));";
	
	//会议通知闹铃设置
	private static final String MEETING_DB = " CREATE TABLE  IF NOT EXISTS  MEETING_DB (id integer PRIMARY KEY autoincrement, "
		+ "meetingid varchar(20), title varchar(100),address varchar(100),meetingTime varchar(50),alarmTime varchar(50),content varchar(500),isShake char(1),isRing char(1));";

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DataBaseHelper(Context context) {
		this(context, DATABASE_NAME, VERSION);
		// TODO Auto-generated constructor stub
	}

	public DataBaseHelper(Context context, String name) {
		this(context, name, VERSION);
		// TODO Auto-generated constructor stub
	}

	public DataBaseHelper(Context context, String name, int version) {
		this(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	// 在第一次创建数库时执行
	@Override
	public void onCreate(SQLiteDatabase db) {

		System.out.println("创建数据库:" + T_SEND_MSG);
		// 即时聊天短信表 T_SEND_MSG
		db.execSQL(T_SEND_MSG);
		
		//文件共享下载管理
		db.execSQL(T_SHARE);
	 
		db.execSQL(USER_HEAD_IMG);
		
		//会议通知闹铃设置
		db.execSQL(MEETING_DB);
	}

	// 在更新数据版本或升级时执行
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// 修改数据库版本
		System.out.println("修改数据库 T_SEND_MSG ,T_SHARE 版本：" + VERSION);
		db.execSQL("DROP TABLE IF EXISTS T_SEND_MSG " );
		db.execSQL("DROP TABLE IF EXISTS T_SHARE " );
		db.execSQL("DROP TABLE IF EXISTS USER_HEAD_IMG " );
		db.execSQL("DROP TABLE IF EXISTS MEETING_DB " );
		onCreate(db);
	}

}
