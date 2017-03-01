package com.yifeng.hnzpt.ui.mapabc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TestActivty extends Activity{
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent bl = this.getIntent();
		String longitude = bl.getStringExtra("longitude") == "" ? "1192679470" : bl
				.getStringExtra("longitude");// 经度
		String latitude = bl.getStringExtra("latitude") == "" ? "32248012" : bl
				.getStringExtra("latitude");// 纬度
		
		System.out.println(longitude+"=============="+latitude);
	}
}
