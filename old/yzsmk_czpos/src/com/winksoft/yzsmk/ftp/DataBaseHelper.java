package com.winksoft.yzsmk.ftp;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * DataBaseHelper作为访问SQLite助手类提供两个全面的功能 第一个:
 * getReadableDatabase(),getWritableDatabase()可获得SQLiteDatabase对象
 * 第二个：onCreate和onUpgrade两个因调函数可以创建和更新数据库的版本
 * 
 * 2012-03-13
 */
@SuppressLint("NewApi")
public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_PATH = Environment.getExternalStorageDirectory() + "/ylk/";
	private static final String DATABASE_NAME = "ftpfile.db";// 创建数据库名
	private static final int VERSION = 2;// 数据库版本 ,如果修改表结构版本要比原来高

	private static final String logs = "CREATE TABLE  IF NOT EXISTS logs(id integer PRIMARY KEY autoincrement, "
			+ "content varchar(200)," //日志主要信息
			+ "type varchar(2)," //日志类型   1：保存消费数据异常，2：生成消费文件异常，3：保存充值数据异常，4：生成充值文件异常，5：FTP上传消费数据异常，6：FTP上传充值数据异常， 
			//7： 正常生成消费文件，8： 正常生成充值文件，9：正常FTP上传消费数据，0：正常FTP上传充值数据，A：删除N天前且已生成消费文件的记录，B： 删除N天前且已生成充值文件的记录
    		+ "insertdate varchar(25)"//记录插入时间  YYYY-MM-DD HH:mm:ss.SSS  
    		+");";	
	
	private static final String xfinfos = "CREATE TABLE  IF NOT EXISTS xfinfos(id integer PRIMARY KEY autoincrement, "
			+ "body_0 varchar(12),"
			+ "body_12 varchar(20),"//12	20	an	卡片序列号	卡主账号，不足位数以空格填充	M
			+ "body_32 varchar(19),"//32	19	n	主账号	向左对齐，不足19位时不足部分补空格（右补空格）。-乘客卡号	M
    		+ "body_51 varchar(16),"//51	16	H	卡内号	卡主账号后16位	M
    		+ "body_67 varchar(3),"//67	3	n	卡种	01 普通卡；02 学生卡；03老年卡；05  拥军卡；06 离休卡；07 优抚卡；08 残疾人卡；09  敬老卡；10  异形卡；11 纪念卡	M
    		+ "body_70 varchar(1),"//70	1	n	记录类型	0：正常交易 1：黑卡交易	M
    		+ "body_71 varchar(8),"//71	8	H	交易前余额	消费前卡余额	M
    		+ "body_79 varchar(8),"//79	8	H	*交易金额	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
    		+ "body_87 varchar(8),"//87	8	H	交易余额	用8个可见的16进制字符（0～9，A～F）表示；电子钱包的消费，本域后补两个F。  若无法填写用缺省值空格填充。  此字段表示卡片消费后余额	M
    		+ "body_95 varchar(2),"//95	2	n	*交易类型标识	交易性质  06-表示电子钱包脱机消费；09-复合消费的类型。	M
    		+ "body_105 varchar(12),"//105	12	n	终端机编号	POS机编号	M
    		+ "body_117 varchar(12),"//117	12	n	*PSAM卡号	PSAM卡终端机编号	M
    		+ "body_129 varchar(9),"//129	9	n	*PSAM卡流水号	PSAM卡交易唯一流水号	M
    		+ "body_138 varchar(1),"//138	1	n	锁卡交易标志	0为正常交易；1为锁卡交易。统一为0，但发现黑名单卡要进行锁卡。	M
    		+ "body_139 varchar(8),"//139	8	H	终端交易序号	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指：POS交易流水号	M
    		+ "body_147 varchar(8),"//147	8	n	*终端交易日期	格式为YYYYMMDD	M
    		+ "body_155 varchar(6),"//155	6	n	*终端交易时间	格式为hhmmss	M
    		+ "body_161 varchar(8),"//161	8	H	*交易验证代码（TAC）	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
    		+ "body_169 varchar(2),"//169	2	H	消费密钥版本号	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
    		+ "body_171 varchar(2),"//171	2	H	消费密钥索引	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
    		+ "body_173 varchar(4),"//173	4	H	卡片脱机交易序列号	用4个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指卡消费计数器。	M
    		+ "body_177 varchar(16),"//177	16	n	发卡机构标识	卡主账号前八位	M
    		+ "body_193 varchar(4),"//193	4	n	*城市代码（交易地）	扬州城市代码：3120	M
    		+ "body_197 varchar(4),"//197	4	n	*城市代码（卡属地）	卡片读取	M
    		+ "body_201 varchar(8),"//201	8	n	运营单位代码	商户编号	M
    		+ "body_229 varchar(8),"//229	8	n	交易柜台编号	公交为：公交车辆编号	M
    		+ "body_259 varchar(2),"//259	2	  n	行业代码	01-公交，02-出租，03-地铁，04-有轨电车，05-公共自行车，06-轮渡，07-小额消费，08-停车场	M
    		+ "state varchar(1),"//记录状态   0:未生成文件,1:已生成文件
    		+ "insertdate varchar(25)"//记录插入时间  YYYY-MM-DD HH:mm:ss.SSS ,用以定期清除过期记录
    		+");";
	
	private static final String czinfos = "CREATE TABLE  IF NOT EXISTS czinfos(id integer PRIMARY KEY autoincrement, "
			+ "CorpId varchar(11),"
			//+ "TxnFileName varchar(27),"
			+ "SettDate varchar(8),"
			+ "CitizenCardNo varchar(20),"
			+ "CrdBalBef varchar(14),"
			+ "CrdBalAft varchar(14),"
			+ "TxnAmt  varchar(14),"
			+ "TxnDt varchar(14),"
			+ "SamNo varchar(12),"
			+ "AccpetCusNo 	varchar(20),"
			+ "OprNo varchar(24),"
			+ "TAC varchar(8),"
			+ "BusinessNo 	 varchar(10),"
			+ "CurrCount	varchar(8),"
			+ "state varchar(1),"//记录状态   0:未生成文件,1:已生成文件
    		+ "insertdate varchar(25)"//记录插入时间  YYYY-MM-DD HH:mm:ss.SSS 
    		+");";	
	
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DataBaseHelper(Context context) {
		this(context, DATABASE_PATH + DATABASE_NAME, VERSION);
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
		System.out.println("创建数据库,版本：" + VERSION);
		db.execSQL(xfinfos);
		db.execSQL(logs);
		db.execSQL(czinfos);
		db.execSQL(desfire);
	}

	// 在更新数据版本或升级时执行
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// 修改数据库版本
		System.out.println("onUpgrade,版本：" + VERSION);
		//db.execSQL("DROP TABLE IF EXISTS xfinfos ");
		//db.execSQL("DROP TABLE IF EXISTS logs ");
		//db.execSQL("DROP TABLE IF EXISTS czinfos ");
		onCreate(db);
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase()
	{
		SQLiteDatabase sdb = null;
		File dbp=new File(DATABASE_PATH);
		File dbf=new File(DATABASE_PATH + DATABASE_NAME);
		
		if(!dbp.exists())
		{
			dbp.mkdir();
		}
					                     
		//数据库文件是否创建成功
		boolean isFileCreateSuccess=false; 
					                     
		if(!dbf.exists())
		{
			try
			{                 
				 isFileCreateSuccess=dbf.createNewFile();
			}
			catch(IOException ioex)
			{
					                      
			}
		}
		else
		{
			isFileCreateSuccess=true;
		}
		if(isFileCreateSuccess)
			sdb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
		return sdb;
	}

	//@Override
	public SQLiteDatabase getReadableDatabase1()
	{
		SQLiteDatabase sdb = null;
		File dbp=new File(DATABASE_PATH);
		File dbf=new File(DATABASE_PATH + DATABASE_NAME);
		
		if(!dbp.exists())
		{
			dbp.mkdir();
		}
					                     
		//数据库文件是否创建成功
		boolean isFileCreateSuccess=false; 
					                     
		if(!dbf.exists())
		{
			try
			{                 
				 isFileCreateSuccess=dbf.createNewFile();
			}
			catch(IOException ioex)
			{
					                      
			}
		}
		else
		{
			isFileCreateSuccess=true;
		}
		if(isFileCreateSuccess)
			sdb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null,
	                  SQLiteDatabase.OPEN_READONLY, null);
		return sdb;
	}


	
	
	
	
	
	
	
	
	//**************************************************
	private static final String desfire = "CREATE TABLE  IF NOT EXISTS disfire(id integer PRIMARY KEY autoincrement, "
			+ "crade_num varchar(8),"		// [4];//POS 交易流水号 4
			+ "city_code varchar(2),"		// [2];//城市代码
			+ "passenger_num varchar(8),"	// [8];//乘客卡号 8	
			+ "care_class varchar(2),"		// 卡类型 1
			+ "care_type varchar(2),"		// [1]; //卡种 2
			+ "crade_class varchar(2),"	// 交易类型 1
			+ "crade_date varchar(14),"		// [7];//交易日期时间 7
			+ "consume_money varchar(6),"	// [3];//消费金额 3
			+ "consume_after_balance varchar(6),"// [3];//消费后卡内余额 3
			+ "authorization_num varchar(14),"// [7];//充值授权号 7
			+ "recharge_money varchar(6),"	// [3];//充值金额 3
			+ "recharge_date varchar(14),"	// [7];//充值日期 7
			+ "crade_auth varchar(8),"		// [8];//交易认证码 4
			+ "care_num varchar(16),"		// [8];//卡面号 8
			+ "consume_crade_val varchar(6),"// [3];//消费后交易计数器 3
			
    		+ "state varchar(1),"//记录状态   0:未生成文件,1:已生成文件
    		+ "insertdate varchar(25)"//记录插入时间  YYYY-MM-DD HH:mm:ss.SSS ,用以定期清除过期记录
    		+");";
}
