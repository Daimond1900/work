package com.winksoft.yzsmk.db;

import java.text.SimpleDateFormat;
import java.util.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/***
 * android数据库封装类 提供公用增删改查操作
 * @author WuJiaHong 2012-03-13
 */
public class SqliteUtil {
	private static String tag = "SqliteUtil";
	private static DbContext dbContext;
	private DataBaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static SqliteUtil sqlInstance = null;

	// public SqliteUtil(Context context) {
	// this.mCtx = context;
	//
	// }

	public static SqliteUtil getInstance(Context cxt) {
		if (cxt != null) {
			if (sqlInstance == null) {
				dbContext = new DbContext(cxt);
				sqlInstance = new SqliteUtil();
			}
		}
		return sqlInstance;
	}

	private SqliteUtil() {
		
	}

	/**
	 * 打开数据库连接
	 * @param isWrite 是否写入
	 * @throws SQLException
	 */
	private void open(boolean isWrite) throws SQLException {
		mDbHelper = new DataBaseHelper(this.dbContext);
		if (isWrite)
			mDb = mDbHelper.getWritableDatabase();
		else
			mDb = mDbHelper.getReadableDatabase();
	}

	/**
	 * 删除数据库
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
	 * @param tableName 表名
	 * @param columnNameValue  列名=值
	 * @return
	 */
	public long insert(String tableName, Map<String, String> columnNameValue) {
		long flag = -1;
		try {
			this.open(true);
			ContentValues initialValues = new ContentValues();
			for (Iterator keys = columnNameValue.keySet().iterator(); keys.hasNext();) {
				String key = (String) keys.next();
				initialValues.put(key, columnNameValue.get(key));
			}
			flag = mDb.insert(tableName, null, initialValues);

		} catch (Exception e) {
			Log.d(tag, "insert err:" + e.getMessage() + "," + e.getStackTrace());
		}finally{
			this.closeDataBase();
		}
		return flag;
	}

	/***
	 * 集合查询
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param selection 筛选条件
	 * @param selectArray 条件参数
	 * @param groupBy 分组
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public List<Map<String, Object>> doQuery(String tableName, String[] columnName, String selection,
			String[] selectArray, String groupBy, String having, String orderBy) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		try {
			this.open(false);

			cursor = mDb.query(tableName, columnName, selection, selectArray, groupBy, having, orderBy);

			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnName.length; i++) {
					map.put(columnName[i], cursor.getString(cursor.getColumnIndex(columnName[i])));
				}
				list.add(map);
			}
		} catch (Exception e) {
			Log.d(tag, "doQuery err:" + e.getMessage() + "," + e.getStackTrace());
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			this.closeDataBase();
		}
		return list;
	}

	/**
	 * 分页查询数据
	 * @param start 分页开始记录数
	 * @param end 分页结束记录数
	 * @return 查询结果集
	 */
	@SuppressLint("NewApi")
	public List<Map<String, Object>> doQuery(String strSql, String[] columnName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		try {
			this.open(false);
			cursor = mDb.rawQuery(strSql, null, null);
			while (cursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnName.length; i++) {
					map.put(columnName[i], cursor.getString(cursor.getColumnIndex(columnName[i])));
				}
				list.add(map);
			}
			cursor.close();
			this.closeDataBase();
		} catch (Exception e) {
			Log.d(tag, "doQuery1 err:" + e.getMessage() + "," + e.getStackTrace());
		}finally{
			if(cursor!=null){
				cursor.close();
				this.closeDataBase();
			}
			}
		return list;
	}

	/**
	 * 
	 * @param sql
	 * @return 
	 */
	public int doQuery(String sql) {
		Cursor cursor = null;
		try {
			this.open(false);
			cursor = mDb.rawQuery(sql, null);

			if (cursor.moveToNext()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		return 0;
	}
	//下于修改
	public String doQueryJobNumber() {
		Cursor cursor = null;
		this.open(false);
		try {
			cursor = mDb.rawQuery("select sign_no from signin where state = '0'", null);
			if (cursor.moveToNext()) {
				return cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		return "";
	}
	//2016/12/10改
	public String daQueryPSAMNo(String name,String cName){
		Cursor cursor = null;
		String str = "";
		try {
			this.open(false);
			cursor = mDb.rawQuery("select distinct "+cName+" from "+name+" where state = '0'", null);
			while (cursor.moveToNext()) {
				str += cursor.getString(cursor.getColumnIndex(cName))+" 、";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}	
			this.closeDataBase();
		}
		return str.endsWith("、") ? str.substring(0, str.length()-1) : str;
	}

	/**
	 * 单条查询方法
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param selection 筛选条件
	 * @param selectArray 条件参数
	 * @param groupBy 分组
	 * @param having 聚合
	 * @param orderBy 排序
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> findById(String tableName, String[] columnName, String selection, String[] selectArray,
			String groupBy, String having, String orderBy) throws SQLException {
		this.open(false);

		Map<String, Object> map = new HashMap<String, Object>();
		Cursor mCursor = mDb.query(tableName, columnName, selection, selectArray, groupBy, having, orderBy);
		if (mCursor.moveToFirst()) {
			mCursor.moveToFirst();
			for (int i = 0; i < columnName.length; i++) {
				map.put(columnName[i], mCursor.getString(mCursor.getColumnIndex(columnName[i])));
			}
		}
		mCursor.close();
		this.closeDataBase();

		return map;

	}

	/***
	 * 删除数据方法
	 * @param tableName
	 * @param selection 条件
	 * @param selectionArray 参数
	 * @return
	 */
	public boolean doDelete(String tableName, String selection, String[] selectionArray) {
		this.open(true);
		// db.delete("person", "personid<?", new String[]{"2"});
		boolean flag = mDb.delete(tableName, selection, selectionArray) > 0;
		this.closeDataBase();
		return flag;
	}

	public int doDelete1(String tableName, String selection, String[] selectionArray) {
		this.open(true);
		// db.delete("person", "personid<?", new String[]{"2"});
		int flag = mDb.delete(tableName, selection, selectionArray);
		this.closeDataBase();
		return flag;
	}

	/**
	 * 更新数据方法
	 * @param tableName 表名
	 * @param columnNameValue 列名及值
	 * @param whereCase 筛选条件
	 * @param whereArray 条件参数
	 * @return
	 */
	public boolean doUpdate(String tableName, Map<String, String> columnNameValue, String whereCase,
			String[] whereArray) {
		this.open(true);

		ContentValues initialValues = new ContentValues();
		for (Iterator keys = columnNameValue.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
			initialValues.put(key, columnNameValue.get(key));
		}
		boolean flag = mDb.update(tableName, initialValues, whereCase, whereArray) > 0;

		this.closeDataBase();
		return flag;
	}

	public int doUpdate1(String tableName, Map<String, String> columnNameValue, String whereCase, String[] whereArray) {
		this.open(true);

		ContentValues initialValues = new ContentValues();
		for (Iterator keys = columnNameValue.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
			initialValues.put(key, columnNameValue.get(key));
		}
		int flag = mDb.update(tableName, initialValues, whereCase, whereArray);

		this.closeDataBase();
		return flag;
	}
	
	public int doUpdate(String sql,String[] values) {
		this.open(true);
		try {
			mDb.execSQL(sql,values);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeDataBase();
		}
		return 0;
	}
	
	/**
	 * 获取指定键的值
	 * @param key
	 */
	public String getKeyValue(String key) {
		String ret = "";
		List<Map<String, Object>> maplist = doQuery("params", new String[] { "value" }, "type=?", new String[] { key },
				null, null, null);
		if (maplist.size() > 0) {
			for (Map<String, Object> ml : maplist) {
				ret = (String) ml.get("value");
				if (ret != "") {
					break;
				}
			}
		}
		return ret;
	}
	
	/**
	 * 设置指定键的值   
	 * @param key,value
	 */
	public void setKeyValue(String key, String value) {
		if (getKeyValue(key) != "") {
			Map<String, String> param = new HashMap<String, String>();
			param.put("value", value);
			param.put("type", key);
			doUpdate("params", param, "type=?", new String[] { key });
			Log.d("setKeyValue", "update[" + key + "],value[" + value + "]");
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("value", value);
			param.put("type", key);
			insert("params", param);
			Log.d("setKeyValue", "insert[" + key + "],value[" + value + "]");
		}
	}
	
	/*---------------------------------------------------------------------------------------------------------------------------*/
	/**
	 * @param map
	 *            封装的数据 其中 insertdate 不需要
	 * @return long -1 添加失败
	 */
	public long insertDesfire(Map<String, String> map) {
		long ret = -1;
		try {
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			// String strDt = tempDate.format(new java.util.Date()).toString();
			String strDt = tempDate.format(new Date());
			map.put("insertdate", strDt);
			ret = insert("disfire", map);
		} catch (Exception e) {
			Log.d(tag, "saveToxfDb err:" + e.getMessage() + "," + e.getStackTrace());
		} finally {
			this.closeDataBase();
		}
		return ret;
	}

	/**
	 * 查询消费总笔数、总金额
	 * 
	 * @return Map<String, Object> key:sumcount(总笔数)、summoney(总金额)
	 */
	public Map<String, Object> querySumMoneyAndSumCount() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String sumcount = "";
		String summoney = "";
		Cursor cursor = null;
		try {
			this.open(false);
			cursor = mDb.rawQuery("select count(*) , sum(consume_money) from disfire", null);
			while (cursor.moveToNext()) {
				sumcount = cursor.getString(0);
				summoney = cursor.getString(1);
			}
		} catch (Exception e) {
			Log.d(tag, "doQuery err:" + e.getMessage() + "," + e.getStackTrace());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		resultMap.put("sumcount", sumcount);
		resultMap.put("summoney", summoney);
		return resultMap;
	}

	/**
	 * 读取指定的数据
	 * 
	 * @param state
	 *            状态
	 * @return
	 */
	public List<Map<String, Object>> queryDesfirebyState(int state, int count) {		//待测试
		 List<Map<String, Object>> resultList = doQuery("select * from disfire where state='" + state + "' order by insertdate limit "+ count,
		 new String[] { "id","crade_num", "city_code", "passenger_num",
		 "care_class", "care_type", "crade_class",
		 "crade_date", "consume_money", "consume_after_balance",
		 "authorization_num", "recharge_money",
		 "recharge_date", "crade_auth", "care_num", "consume_crade_val",
		 "state", "insertdate" });
		 return resultList;
//		String sql = "select * from disfire where state=? order by insertdate limit ?";
//		Cursor cursor = null;
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		try {
//			this.open(false);
//			cursor = mDb.rawQuery(sql, new String[] { state + "", count + "" });
//			while (cursor.moveToNext()) {
//				for (int i = 0; i < cursor.getColumnCount(); i++) {
//					map.put(cursor.getColumnName(i), cursor.getColumnIndex(cursor.getColumnName(i)));
//				}
//				list.add(map);
//			}
//		} catch (Exception e) {
//		}finally{
//			if (cursor != null) {
//				cursor.close();
//			}
//			this.closeDataBase();
//		}
//		return list;
	}

	/**
	 * 读取指定的数据的数量 记录状态state 1离线结算完成 2批上送完成
	 * 
	 * @param state
	 *            状态
	 * @return 数据的数量
	 */
	public int queryCountByState(int state) {
		Cursor cursor = null;
		int count = 0;
		try {
			this.open(false);
			cursor = mDb.rawQuery("select count(*) from disfire where state='" + state + "'", null);
			while (cursor.moveToNext()) {
				count = Integer.parseInt(cursor.getString(0));
			}
		} catch (Exception e) {
			Log.d(tag, "doQuery err:" + e.getMessage() + "," + e.getStackTrace());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		return count;
	}

	/**
	 * 更改状态(单一修改)
	 * 
	 * @param id
	 *            所需修改的id
	 * @param state
	 *            状态
	 */
	private boolean updateState(int id, int state) {
		ContentValues values = new ContentValues();
		values.put("state", state + "");
		int flage = 0;
		try {
			flage = mDb.update("disfire", values, "id = ?", new String[] { id + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flage > 0 ? true : false;
	}

	/**
	 * 更改状态(批量修改)
	 * 
	 * @param listId
	 *            多个id
	 * @param state
	 *            状态
	 */
	public boolean updateState(int[] listId, int state) {
		boolean flage = true;
		this.open(false);
		mDb.beginTransaction();
		try {
			for (Integer id : listId) {
				if (!updateState(id, state)) {
					return false;
				}
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mDb.endTransaction();
			this.closeDataBase();
		}
		return flage;
	}
	
	/****************************************************/
	public int getCount(String table_name) {
		String sql = "select count(*) from " + table_name;
		this.open(false);
		Cursor cursor = null;
		try {
			cursor = mDb.rawQuery(sql, null);
			if (cursor.moveToNext())// 判断Cursor中是否有数据
			{
				return cursor.getInt(0);// 返回总记录数
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		return 0;// 如果没有数据，则返回0
	}
	//获得签到最早的id
	public String getSignEarliestId(){
		Cursor cursor = null;
		try {
			this.open(false);
			cursor = mDb.rawQuery("select id from signin order by signin_date limit 1", null);
			if (cursor.moveToNext()) {
				return cursor.getString(0);
			}
		} catch (Exception e) {
			Log.d(tag, "doQuery err:" + e.getMessage() + "," + e.getStackTrace());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.closeDataBase();
		}
		return "";
	}
	
	public void deleteSignExceed(String id){
		this.open(false);
		try {
			if(!"".equals(id)){
					mDb.delete("signin", "id=?", new String[]{id});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.closeDataBase();
		}
	}
	
	//
}
