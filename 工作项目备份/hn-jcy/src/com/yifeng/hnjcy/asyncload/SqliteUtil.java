package com.yifeng.hnjcy.asyncload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/***
 * android数据库封装类
 * 提供公用增删改查操作
 * @author WuJiaHong 2012-03-13
 */
public class SqliteUtil {
	private final Context mCtx;
	private DataBaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	public SqliteUtil(Context context) {
		this.mCtx = context;
	}

	/**
	 * 打开数据库连接
	 * @param isWrite 是否写入
	 * @throws SQLException
	 */
	private void open(boolean isWrite) throws SQLException {
		mDbHelper = new DataBaseHelper(this.mCtx);
		if(isWrite)
		mDb = mDbHelper.getWritableDatabase();
		else
		mDb = mDbHelper.getReadableDatabase();	
	}

	/**
	 * 删除数据库
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName) {
		this.open(true);
		mDb.execSQL("drop table if exists " + tableName);
		this.closeDataBase();
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeDataBase() {
		mDb.close();
		mDbHelper.close();
	}

	/***
	 * 插入数据
	 * 
	 * @param tableName
	 *            表名
	 * @param columnNameValue
	 *            列名=值
	 * @return
	 */
	public synchronized long   insert(String tableName, Map<String, String> columnNameValue) {
		this.open(true);
		ContentValues initialValues = new ContentValues();
		for (Iterator keys = columnNameValue.keySet().iterator(); keys
				.hasNext();) {
			String key = (String) keys.next();
			initialValues.put(key, columnNameValue.get(key));
		}
		long flag = mDb.insert(tableName, null, initialValues)  ;

		this.closeDataBase();

		return flag;
	}

	/***
	 * 集合查询
	 * 
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @param selection
	 *            筛选条件
	 * @param selectArray
	 *            条件参数
	 * @param groupBy
	 *            分组
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public List<Map<String, Object>> doQuery(String tableName,
			String[] columnName, String selection, String[] selectArray,
			String groupBy, String having, String orderBy) {
		this.open(false);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = mDb.query(tableName, columnName, selection,
				selectArray, groupBy, having, orderBy);
		 
			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnName.length; i++) {
					map.put(columnName[i], cursor.getString(cursor
							.getColumnIndex(columnName[i])));
				}
				list.add(map);
			}
	 
		cursor.close();
		this.closeDataBase();

		return list;
	}

	/**
	 * 单条查询方法
	 * 
	 * @param tableName
	 * 表名
	 * @param columnName
	 * 列名
	 * @param selection
	 * 筛选条件
	 * @param selectArray
	 * 条件参数
	 * @param groupBy
	 * 分组
	 * @param having
	 * 聚合
	 * @param orderBy
	 * 排序
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> findById(String tableName, String[] columnName,
			String selection, String[] selectArray, String groupBy,
			String having, String orderBy) throws SQLException {
		this.open(false);

		Map<String, Object> map = new HashMap<String, Object>();
		Cursor mCursor = mDb.query(tableName, columnName, selection,
				selectArray, groupBy, having, orderBy);
		if (mCursor.moveToFirst()) {
			mCursor.moveToFirst();
			for (int i = 0; i < columnName.length; i++) {
				map.put(columnName[i], mCursor.getString(mCursor
						.getColumnIndex(columnName[i])));
			}
		}
		mCursor.close();
		this.closeDataBase();

		return map;

	}

	/***
	 * 删除数据方法
	 * 
	 * @param tableName
	 * @param selection
	 *            条件
	 * @param selectionArray
	 *            参数
	 * @return
	 */
	public boolean doDelete(String tableName, String selection,
			String[] selectionArray) {
		this.open(true);
		boolean flag = mDb.delete(tableName, selection, selectionArray) > 0;

		this.closeDataBase();
		return flag;
	}

	/**
	 * 更新数据方法
	 * 
	 * @param tableName
	 * 表名
	 * @param columnNameValue
	 * 列名及值
	 * @param whereCase
	 * 筛选条件
	 * @param whereArray
	 * 条件参数
	 * @return
	 */
	public boolean doUpdate(String tableName,
			Map<String, String> columnNameValue, String whereCase,
			String[] whereArray) {
		this.open(true);

		ContentValues initialValues = new ContentValues();
		for (Iterator keys = columnNameValue.keySet().iterator(); keys
				.hasNext();) {
			String key = (String) keys.next();
			initialValues.put(key, columnNameValue.get(key));
		}
		boolean flag = mDb.update(tableName, initialValues, whereCase,
				whereArray) > 0;

		this.closeDataBase();
		return flag;
	}
}
