package com.yifeng.data;
import java.util.*;
import com.yifeng.util.DataConvert;

import android.content.Context;
/**
 * 手机重要数据提醒
 * @author WuJiaHong
 *
 */
public class AlertDAL extends BaseDAL{

	public AlertDAL(Context context) {
		super(context);
	}
	/**
	 * 重要数据提醒
	 * @param map
	 * @return
	 */
	public Map<String,String> doAlert(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/wsform/doGetCount");
		String jstring=this.doGet(map);
		return DataConvert.toMap(jstring);
	}	
}
