package com.yifeng.cloud.filter.sms;
import com.yifeng.util.ConstantUtil;

public class SmsFilter {
   
	// 根据号码过滤
	public static boolean isFilter(SmsInfo smsInfo)		
	{   
		if (smsInfo.address.equals(ConstantUtil.SEND_SMS_NUM))
		{   
			   return true;
		}		
		
		return false;
	}
	
	
}
