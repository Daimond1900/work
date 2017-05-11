package com.yifeng.cloud.filter.sms;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySmsManager {

	
	
	private final static String TAG = "SMSManager";
	
	private Handler mHandler;
	private Context mContext;
	private SmsHandler mSmsHandler;							// 短信处理者
	private SmsObserver mSmsObserver;						// 短信数据库观察者

	private WorkThread mWorkThread;							// 工作线程（用于在数据库发生变化时扫描短信）
	private boolean mThreadExitFlag;						// 线程是否要退出
	
	private SMSReceive mSmsReceive;							// 短信拦截者
	private boolean register=false;
	
	public MySmsManager(Handler handler,Context context)
	{
		mHandler = handler;	
		mContext = context;

		mSmsHandler = new SmsHandler(context);
		mSmsObserver = new SmsObserver(handler);
		
		mThreadExitFlag = false;
		mWorkThread = new WorkThread();
		mWorkThread.start();
		
		mSmsReceive = new SMSReceive();
	}
	
	
	// 监听注册
	public void register(Handler handler, IProcessObser processObser)
	{  
		register=true;
		
		mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mSmsObserver);
		
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		mContext.registerReceiver(mSmsReceive, filter);
		
		mSmsHandler.addProcessObser(handler, processObser);
	}
	
	// 反注册
	public void unRegister()
	{  
	   if(mSmsReceive!=null&&register){
			mContext.getContentResolver().unregisterContentObserver(mSmsObserver);
			mContext.unregisterReceiver(mSmsReceive);
			mSmsHandler.removeProcessObser();
		}
	}
	
	// 销毁工作线程
	public void destory()
	{
		mThreadExitFlag = true;
		synchronized (mWorkThread) {
			mWorkThread.notify();
		}
	}
	

	class SmsObserver extends ContentObserver
    {

		public SmsObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub		
		}
	
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);				
			
			// onChange的执行是在UI线程里做的，所以不能直接去做遍历数据库的耗时操作
			synchronized (mWorkThread) {
				mWorkThread.notify();				// 唤醒工作线程
			}
			
			
		}
	}
	
	class WorkThread extends Thread
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while(true)
			{
				synchronized (this) {
					
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (mThreadExitFlag == true)
					{
						break;
					}
		
				}
				
				try {
					mSmsHandler.doWork();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
			
		}
	
		
	}
	
	
	public class SMSReceive extends BroadcastReceiver {

		private final static String TAG = "SMSReceive";

		
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			Bundle bundle = intent.getExtras();
			Object messages[] = (Object[]) bundle.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];

			SmsInfo smsInfo = new SmsInfo();
			
			StringBuffer sBuffer = new StringBuffer();
			for(int n = 0; n < messages.length; n++)
			{
				smsMessage[n]= SmsMessage.createFromPdu((byte[]) messages[n]); 
				//sBuffer.append(smsMessage[n].getMessageBody());
				
				sBuffer.append(smsMessage[n].getDisplayMessageBody());
				
				if(smsInfo.address.equals("")){
				 smsInfo.address = smsMessage[n].getOriginatingAddress();
				}
			}
			
			smsInfo.body = sBuffer.toString();
			smsInfo.date = String.valueOf(System.currentTimeMillis());
			
		
			if (SmsFilter.isFilter(smsInfo))				// 过滤短信并处理
			{
				abortBroadcast();	
				
				mSmsHandler.processSMSInfo(smsInfo);
			}
			

			
		}

		
			
	}
}
