package com.yifeng.data;

import java.util.*;
import java.util.List;
import java.util.Map;

import com.yifeng.util.DataConvert;
import com.yifeng.util.DateUtil;

import android.content.Context;
/**
 * 在线视频
 * @author WuJiaHong
 *
 */
public class VideoDal extends BaseDAL{

	public VideoDal(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public List<Map<String,String>> doQury(int pageNum,String type){
		
		Map<String,String> param=new HashMap<String,String>();
		param.put("page", pageNum + "");
		param.put("type", type);
		this.setUrl(this.getIpconfig()+"android/video/doQueryVideoList");
		String json=this.doGet(param);
		return DataConvert.toArrayList(json);
		
		/*List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for (int i = 0; i < 10; i++) {
			Map<String,String> map=new HashMap<String,String>();
			map.put("video_type", "高清视频");
			map.put("video_title", "第二次全市大会视频");
			map.put("time", "2012-04-12 15:24:30");
			map.put("content", "第二次全市大会视频");
			map.put("long_time", "2");
			map.put("video_url", "http://221.229.34.136:8080/yzvod/get_yzvod.php?vid=live&tag=live");
			map.put("state", "1");
			list.add(map);
		}
		return list;*/
		
	}	
	public Map<String,String> doGetInfo(String id){
		Map<String,String> param=new HashMap<String,String>();
		param.put("video_id", id);
		this.setUrl(this.getIpconfig()+"android/video/doQueryDetail");
		String json=this.doGet(param);
		return DataConvert.toMap(json);
		/*
		Map<String,String> map=new HashMap<String,String>();
		map.put("state", "1");
		map.put("video_title", "测试一个视频标题看看");
		map.put("video_type", "高清视频");
		map.put("video_date", "2012-04-12");
		map.put("video_long", "02:23");
		map.put("video_content", "测试一个视频标题看看测试一个视频标题看看测试一个视频标题看看测试一个视频标题看看");
		map.put("video_url", "http://221.229.34.136:8080/yzvod/get_yzvod.php?vid=live&tag=live");
		return map;*/
	}

}
