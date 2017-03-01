package com.yifeng.hnqzt.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/***
 * webViewJs工具类解决代码混淆后
 * @author WuJiaHong
 *
 */
public class WebViewJsUtil {
	public Handler jsHandler;
    public WebViewJsUtil(){
    	jsHandler=new Handler();
    }
    
   /***
    * 到公司详细
    * @param companyId
    * @param companyName
    * @param companyAddress
    * @param telNo
    * @param longitude
    * @param latitude
    * @param method 方法名
    */
   public  void  goToMap(String companyId,String companyName,String companyAddress,String telNo,String longitude,String latitude){
	   	Message msg=new Message();
	   	Bundle b=msg.getData();
	   	b.putString("companyId", companyId);
	   	b.putString("companyName", companyName);
	   	b.putString("companyAddress", companyAddress);
	   	b.putString("telNo", telNo);
	   	b.putString("longitude", longitude);
	   	b.putString("latitude", latitude);
	   	b.putString("method", "goToMap");
	   	msg.setData(b);
	   	jsHandler.sendMessage(msg);
   }
   

     
}
