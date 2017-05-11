package com.yifeng.ChifCloud12345.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 监听开机自动运行广播提醒
 * @author 吴家宏
 *
 */
public class SmsServiceBootReceiver extends BroadcastReceiver  { 
		  @Override
		public void onReceive(Context context, Intent intent) {
			  //监听开机时自动启动Service
			  Intent myIntent = new Intent();
			  myIntent.setAction("com.yifeng.ChifCloud12345.service.SendNotificationService");
			  context.startService(myIntent);
		}
}
