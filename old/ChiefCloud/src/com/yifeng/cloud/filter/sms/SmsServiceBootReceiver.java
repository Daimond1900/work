package com.yifeng.cloud.filter.sms;
import com.yifeng.util.ConstantUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 短信过滤
 * 短信换醒自动运行广播
 * @author 吴家宏
 * 2012-06-20
 */
public class SmsServiceBootReceiver extends BroadcastReceiver  { 
		  @Override
		public void onReceive(Context context, Intent intent) {
			
			//监听开机时自动启动Service
			//if(intent.getAction().equals(ConstantUtil.BOOT_COMPLETED)||intent.getAction().equals(ConstantUtil.SMS_ACTION)){
			System.out.println("短信来了。。。。");
			//只要短信来就开启监听 不需要开机启动
			if(intent.getAction().equals(ConstantUtil.SMS_ACTION)){
				
			   //YFCloudSmsService如果还没启动执行启动
			   if(!ConstantUtil.YFCloudSmsServiceDoing){
				   Intent myIntent = new Intent(context,YFCloudSmsService.class);
				   context.startService(myIntent);//启动监听
			   }
			   
			}
			//来自YFCloudSmsService发送的广播
			if(intent.getAction().equals(ConstantUtil.SMS_FILTER_ACTION)){
				
				   if(intent!=null){
				    	
				 	String sender=intent.getStringExtra("sender");
			    	String body=intent.getStringExtra("body");
			    	String date=intent.getStringExtra("date");
			    	
			    	//进入提醒界面
			    	 Intent alert=new Intent(context,AlertActivity.class);
					    alert.putExtra("sender", sender);
						alert.putExtra("body",body);
						alert.putExtra("date",date);
						alert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						alert.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);  
						context.startActivity(alert);
					
				  }
				 
			 }
		}
}
