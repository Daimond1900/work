package com.winksoft.android.yzjycy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "yzjycy.db"; //数据库名称
	private static final int DATABASE_VERSION = 1;  
	
	public static final String TABLE_MENUS = "menus";//数据表 

	public static final String ID = "id";  
	public static final String NAME = "name";   // 栏目名称
	public static final String ORDERID = "orderId";  // 排序
	public static final String SELECTED = "selected"; // 是否选中我的应用
	public static final String RESNAME = "resName";  // 资源图标名称
	public static final String GROUPID = "groupId";  //分组id 1 、就业，2 创业，3 资讯
	public static final String COUNT = "count";
	private Context context;
	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists "+TABLE_MENUS +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ID + " INTEGER UNIQUE, " +
				NAME + " VARCHAR2 , " +
				ORDERID + " INTEGER , " +
				SELECTED + " SELECTED ," +
				GROUPID + " INTEGER , " + 
				RESNAME + " VARCHAR2)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
