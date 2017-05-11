package com.yifeng.cloud.filter.sms;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsHandler {

	private Context mContext;
	
	private Handler mExtenHandler;						// 外部传递进来的UIHANDLER
	private IProcessObser mProcessObser;				// 短信观察者
	private long mRegisterTimeMils = 0;					// 监听时间
	
	public SmsHandler(Context context)
	{
		mContext = context;
		
	}
	
	
	// 添加短信观察者
	public void addProcessObser(Handler handler, IProcessObser processObser)
	{
		mExtenHandler = handler;
		mProcessObser = processObser;
		mRegisterTimeMils = System.currentTimeMillis();
	}
	
	// 移除观察者
	public void removeProcessObser()
	{
		mExtenHandler = null;
		mProcessObser = null;
		mRegisterTimeMils = 0;
	}
	
	
	public void doWork()
	{
		MyDebug.displayDebug("doWork...");
		
		
		boolean flag = false;
		
		// 查询收件箱数据库
		Cursor cursor = mContext.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);    
		
		if (cursor != null)
		{
			Map<String, SmsInfo> smsMap = new HashMap<String, SmsInfo>();
			SmsInfo smsInfo = null;
			
			while(cursor.moveToNext())
			{
				String _id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
				String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
				String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
				String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
				
				smsInfo = new SmsInfo();
				smsInfo._id = _id;
				smsInfo.address = address;
				smsInfo.date = date;
				smsInfo.body = body;
				
				
				// 过滤得到想要的短信
				if (SmsFilter.isFilter(smsInfo))
				{
					smsMap.put(_id, smsInfo);
				}
			}
			
			cursor.close();
			
			// 处理过滤得到的短信
			handler(smsMap);
		}
		
	}
	
	
	private void handler(Map<String, SmsInfo> smsMap)
	{
		MyDebug.displayDebug("handler...");
		
		int size = smsMap.size();
		if (size == 0)
		{
			return ;
		}	
		
		Set keysSet = smsMap.keySet();
		Iterator iterator = keysSet.iterator();
		
		while(iterator.hasNext()) {
			
			Object key = iterator.next();
			
			SmsInfo value = smsMap.get(key);
			
			deleteSMSById(value._id);			//  删除该类短信在数据库中的存储

		} 
		
		SmsInfo smsInfo = null;
		iterator = smsMap.keySet().iterator();
		
		long timeMillis = mRegisterTimeMils;
		
		// 得到最新的那条短信
		while(iterator.hasNext()) {
			
			Object key = iterator.next();
			
			SmsInfo value = smsMap.get(key);
		
			long time = Long.valueOf(value.date);
			if (time > timeMillis)
			{
				smsInfo = value;
				timeMillis = time;
			}	
			
		} 
		
		smsMap.clear();
		
		// 处理得到的那条短信
		processSMSInfo(smsInfo);
		
	}
	
	
	public void processSMSInfo(SmsInfo info)
	{
		if (info != null)
		{
			MyDebug.displaySmsInfo(info);
			
			if (mExtenHandler != null)
			{
				mExtenHandler.post(new ProcessRunnable(info));		// 在UI线程里调用runnable任务
			}
		}
	}
	
	
	class ProcessRunnable implements Runnable
	{
		private SmsInfo msInfo;
		
		public ProcessRunnable(SmsInfo smsInfo)
		{
			msInfo = smsInfo;
		}
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mProcessObser != null)
			{
				mProcessObser.onProcessSms(msInfo);					// ui线程处理该短信
			}
		}
		
	}
	
	// 删除指定ID在短信收件箱数据库的存储
	private int deleteSMSById(String id)
	{
		 return mContext.getContentResolver().delete(Uri.parse("content://sms"), "_id=?", new String[]{id});
	}
	
}
