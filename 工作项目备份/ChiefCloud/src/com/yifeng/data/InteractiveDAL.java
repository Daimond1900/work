package com.yifeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;

import com.yifeng.util.CommonUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.FormFile;
import com.yifeng.util.FormFile1;
import com.yifeng.util.SocketHttpRequester;
import com.yifeng.util.UploadImage;

public class InteractiveDAL extends BaseDAL {

	public InteractiveDAL(Context context) {
		super(context);
	}
/**
 * 得到微博列表
 * @param map
 * @return
 */
	public List<Map<String, String>> doQury(Map<String, String> map) {
		this.setUrl(this.getIpconfig() + "android/nywy/doInit");
		String jstring = this.doGet(map);
		return DataConvert.toArrayList(jstring);
	}

	public void sendMessage(Map<String, String> map) {
		this.setUrl(this.getIpconfig() + "android/nywy/doRelease");
		this.doGet(map);
	}
/**
 * 回复微博
 * @param map
 * @param picUrl
 * @param bm
 */
	public void doReply(Map<String, String> map, String picUrl,Bitmap bm) {
		
		if (picUrl != null) {
			
			byte[] b = CommonUtil.Bitmap2Bytes( bm);
			FormFile1 formFile = new FormFile1(picUrl, b, "uploadFile",
					null);
			FormFile1[] files = new FormFile1[] { formFile };
			try {
				SocketHttpRequester.post(this.getIpconfig()
						+ "upload/doReply", map, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			FormFile1[] files = new FormFile1[] {   };
			try {
				SocketHttpRequester.post(this.getIpconfig()
						+ "upload/doReply", map, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回复列表
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> getDetail(Map<String, String> map) {
		this.setUrl(this.getIpconfig() + "android/nywy/doDetails");
		String jstring = this.doGet(map);
		Map<String, String> map1 = DataConvert.toMap(jstring);
		String row = map1.get("row");
		String table = map1.get("table");
		List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();

		list1.add(DataConvert.toMap(row));

		List<Map<String, String>> table_list = DataConvert.toArrayList(table);
		if (table_list.get(0).get("state").equals("1")) {

			for (Map m : table_list) {
				m.put("SUBJECT", "回复");
				list1.add(m);
			}

		}
		return list1;
	}

	public void sendMessage1(Map<String, String> map, String picUrl ) {
		if (picUrl != null) {
			byte[] b = CommonUtil.getBytesFromFile(new File(picUrl));
			FormFile formFile = new FormFile(picUrl, b, "uploadFile",
					"image/jpg");
			FormFile[] files = new FormFile[] { formFile };
			new UploadImage().uploadFile(this.getIpconfig()
					+ "upload/uploadingPic", map, files);
		} else {
			new UploadImage().post(this.getIpconfig()
					+ "android/nywy/uploadingPic", map, null);
		}
	}
	/**
	 * 发布微博
	 * @param map
	 * @param picUrl
	 */
	public void sendMessage2(Map<String, String> map, String picUrl,Bitmap bm ) {
		if (picUrl != null) {
			
			byte[] b = CommonUtil.Bitmap2Bytes( bm);
			FormFile1 formFile = new FormFile1(picUrl, b, "uploadFile",
					null);
			FormFile1[] files = new FormFile1[] { formFile };
			try {
				SocketHttpRequester.post(this.getIpconfig()
						+ "upload/doRelease", map, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			FormFile1[] files = new FormFile1[] {   };
			try {
				SocketHttpRequester.post(this.getIpconfig()
						+ "upload/doRelease", map, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 删除帖子
	 * @param map
	 * @param picUrl
	 */
	public void deletePost(String mainId ) {
		this.setUrl(this.getIpconfig() + "android/nywy/deletePost");
		Map<String,String> map=new HashMap<String, String>();
		map.put("mainId", mainId);
		  this.doGet(map);
		
	}
	/**
	 * 删除回帖
	 * @param map
	 * @param picUrl
	 */
	public void deleteReply(String returnId ) {
		this.setUrl(this.getIpconfig() + "android/nywy/deleteReply");
		Map<String,String> map=new HashMap<String, String>();
		map.put("returnId", returnId);
		  this.doGet(map);
		
	}
	
	
}
