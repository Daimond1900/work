package com.winksoft.yzsmk.xfpos.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.R.id;
import com.yifengcom.yfpos.print.Print;
import com.yifengcom.yfpos.print.PrintPackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
	}
	/**
	 * 设置标题文本
	 * @param headText
	 */
	public void setTitle(String headText){
		TextView head = (TextView) findViewById(R.id.tvHead);
		head.setText(headText);
	}
	
	/**
	 * Activity 之间跳转
	 * @param c 目标Activity.class
	 */
	protected void startActivity(Class<?> c) {
		Intent intent = new Intent(this, c);
		startActivity(intent);
	}
	
	/**
	 * Toast
	 * @param msg 信息内容
	 */
	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 获取当前时间
	 * @return	String Date
	 */
	public String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public String getTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	
	/**
	 * 设置签退是打印内容
	 * @param map 
	 * @return
	 */
	protected byte[] getPrintBody(Map<String, String> map) {
		byte[] sendbuf = new byte[1024];
		try {
			Print printinfor = new Print();
			printinfor.PRINT_clear();

			String print = getString(map, "title");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_48X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));
			
			print = "--------------------------------";
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易日期:" + getString(map, "body_1");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "日结日期:" + getString(map, "body_2");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "工号:" + getString(map, "body_3");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "PSAM1:" + getString(map, "body_4");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "PSAM2:" + getString(map, "body_5");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易金额:" + getString(map, "body_6");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_48X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易笔数:" + getString(map, "body_7");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "打印时间:" + getCurrentTime();
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24,
					print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			printinfor.PRINT_Add_setp((short) (100));

			short sendLen = printinfor.PRINT_packages(sendbuf);
			byte[] sendbuf1 = new byte[sendLen];

			System.arraycopy(sendbuf, 0, sendbuf1, 0, sendLen);

			PrintPackage package1 = new PrintPackage(sendbuf1);
			if (package1 != null) {
				return package1.getPackData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据 map中的key获得value
	 * @param map 
	 * @param key
	 * @return key对应的value,map为空或key不存在，返回 ""
	 */
	protected String getString(Map<String, String> map,String key){
		if(map == null)
			return "";
		return map.get(key) == null ? "": map.get(key);
	}

}
