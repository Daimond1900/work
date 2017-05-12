package com.winksoft.android.yzjycy.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.entity.MenuItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil implements IMainMenu{
	private static DBUtil mInstance;
	private Context mContext;
	private SQLHelper mSQLHelp;
	private SQLiteDatabase mSQLiteDatabase;

	private DBUtil(Context context) {
		mContext = context;
		mSQLHelp = new SQLHelper(context);
		mSQLiteDatabase = mSQLHelp.getWritableDatabase();
	}
	/**
	 * 初始化数据库操作DBUtil类
	 */
	public static DBUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBUtil(context);
		}
		return mInstance;
	}
	/**
	 * 关闭数据库
	 */
	public void close() {	
		if(mSQLiteDatabase!=null && mSQLiteDatabase.isOpen()){
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}
	}

	/**
	 * 添加数据
	 */
	public void insertData(ContentValues values) {
		mSQLiteDatabase.insert(SQLHelper.TABLE_MENUS, null, values);
	}

	/**
	 * 更新数据
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void updateData(ContentValues values, String whereClause,
			String[] whereArgs) {
		mSQLiteDatabase.update(SQLHelper.TABLE_MENUS, values, whereClause,
				whereArgs);
	}

	/**
	 * 删除数据
	 * 
	 * @param whereClause
	 * @param whereArgs
	 */
	public void deleteData(String whereClause, String[] whereArgs) {
		mSQLiteDatabase
				.delete(SQLHelper.TABLE_MENUS, whereClause, whereArgs);
	}

	/**
	 * 查询数据
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor selectData(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = mSQLiteDatabase.query(SQLHelper.TABLE_MENUS,columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	
	@Override
	public boolean addCache(MenuItem item) {
		boolean flag = false;
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(SQLHelper.NAME, item.getName());
			values.put(SQLHelper.ID, item.getId());
			values.put(SQLHelper.ORDERID, item.getOrderId());
			values.put(SQLHelper.SELECTED, item.getSelected());
			values.put(SQLHelper.RESNAME, item.getResName());
			values.put(SQLHelper.GROUPID, item.getGroupId());
			id = mSQLiteDatabase.insert(SQLHelper.TABLE_MENUS, null, values);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {
		} finally {
		}
		return flag;
	}

	@Override
	public boolean deleteCache(String whereClause, String[] whereArgs) {
		boolean flag = false;
		int count = 0;
		try {
			count = mSQLiteDatabase.delete(SQLHelper.TABLE_MENUS, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
		} finally {
		}
		return flag;
	}

	@Override
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		boolean flag = false;
		int count = 0;
		try {
			count = mSQLiteDatabase.update(SQLHelper.TABLE_MENUS, values, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			
		} finally {
		}
		return flag;
	}

	@Override
	public Map<String, String> viewCache(String selection,
			String[] selectionArgs) {
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			cursor = mSQLiteDatabase.query(true, SQLHelper.TABLE_MENUS, null, selection,
					selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
			}
		} catch (Exception e) {

		} finally {
			if(cursor != null){
		        cursor.close();
		    }
		}
		return map;
	}

	@Override
	public List<Map<String, String>> listCache(String selection,String[] selectionArgs,String orderBy, String limit) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(false, SQLHelper.TABLE_MENUS, null, selection,selectionArgs, null, null, orderBy, limit);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {

					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
				list.add(map);
			}
		} catch (Exception e) {
			
		} finally {
			if(cursor != null){
		        cursor.close();
		    }
		}
		return list;
	}

	public void clearFeedTable() {
		String sql = "DELETE FROM " + SQLHelper.TABLE_MENUS + ";";
		mSQLiteDatabase.execSQL(sql);
		revertSeq();
	}

	private void revertSeq() {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ SQLHelper.TABLE_MENUS + "'";
		mSQLiteDatabase.execSQL(sql);
	}
}