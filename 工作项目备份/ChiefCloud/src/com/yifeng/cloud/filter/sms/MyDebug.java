package com.yifeng.cloud.filter.sms;

import java.util.Calendar;
import android.util.Log;

public class MyDebug {

	private final static String TAG = "MyDebug";
	
	public static void displaySmsInfo(SmsInfo smsInfo)
	{
		if (smsInfo != null)
		{
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("_id = " + smsInfo._id + "\n");
			sBuffer.append("phoneNum = " + smsInfo.address + "\n");
			sBuffer.append("content = " + smsInfo.body + "\n");
			
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(smsInfo.date));
			
			String date = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + 
					calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + 
					calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
			/*String date=DateUtil.getCurrentYear()+"/"+
			DateUtil.getCurrentMonth()+"/"+DateUtil.getCurrentDay()+" "
			+DateUtil.getCurrentHour()+":"+DateUtil.getCurrentMinute()+":"+
			DateUtil.getCurrentSecond();*/
			sBuffer.append("date = " + date + "\n");
			
			Log.d(TAG, sBuffer.toString());
		}
	}
	
	public static void displayDebug(String info)
	{
		Log.d(TAG, info);
	}
	
	public static void displayError(String info)
	{
		Log.e(TAG, info);
	}
}
