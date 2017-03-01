package com.yifeng.data;

import java.util.List;
import java.util.Map;

import com.yifeng.util.DataConvert;

import android.content.Context;
/**
 * 市长寄语
 * @author WuJiaHong
 */
public class MayorDal extends BaseDAL{

	public MayorDal(Context context) {
		super(context);
	}
	/**
	 * 获取寄语市长的 list info
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> doQury(Map<String,String> map){
		this.setUrl(this.getIpconfig()+"android/jysz/doGetList");
		String json=this.doGet(map);
		return DataConvert.toArrayList(json);
	}	
}
