package com.yifeng.data;

import java.util.List;
import java.util.Map;

import com.yifeng.util.DataConvert;

import android.content.Context;
/**
 * 经验交流
 * @author WuJiaHong
 *
 */
public class TalkDAL extends BaseDAL{

	public TalkDAL(Context context) {
		super(context);
	}
	
	public List<Map<String,String>> doQury(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/jyjl/doInit");
		String jstring=this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}	
}
