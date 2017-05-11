package com.yifeng.ChifCloud12345update;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.microalloy.DeclearActivity;

public class HotTalkActivity extends Activity {
	ListView listview;
	HomeAdapter adapter;
	LinearLayout top;
	Button back;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rdwt);
		listview = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HotTalkActivity.this.finish();
			}
		});
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		adapter = new HomeAdapter(this, list, R.layout.dbsx_list_item,
				new String[] { "bm_name", "content", "bm_logo", "title","icon1","icon2","icon3" },
				new int[] { R.id.bm_name, R.id.content, R.id.bm_logo,
						R.id.title,R.id.icon1,R.id.icon2,R.id.icon3 },getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				 Intent intent = new Intent(HotTalkActivity.this,
						 DeclearActivity.class);
				 startActivity(intent);
//				switch (position) {
//				case 0:
//					// Intent intent = new Intent(ReadyToDoActivity.this,
//					// HotLinkActivity.class);
//					// startActivity(intent);
//
//					break;
//
//				default:
//					break;
//				}

			}
		});

		addData();
		adapter.notifyDataSetChanged();
	}

	void addData() {
		Resources res = getResources();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bm_name", "工商局");
		map.put("bm_logo",
				BitmapFactory.decodeResource(res, R.drawable.icon_gs));

		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.icon1));
		map.put("icon2", BitmapFactory.decodeResource(res, R.drawable.icon2));
		map.put("icon3", BitmapFactory.decodeResource(res, R.drawable.icon3));
		map.put("title", "举办个体工商户培训");
		map.put("content",
				"为了认真贯彻落实国务院《个体工商户条例》（以下简称《条例》）和国家工商总局《个体工商户登记管理办法》（以下简称《办法》），2011年11月10日至11日，江苏省工商局在南京举办了全系统《条例》及《办法》培训。省各直属局分管注册登记的副局长，注册处长、个企处处长、区县工商局或工商分局基层业务骨干，以及省局领导和相关处室人员，共95人参加了培训。");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("bm_name", "地税局");
		map.put("bm_logo",
				BitmapFactory.decodeResource(res, R.drawable.icon_ds));

		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.icon1));
		map.put("icon2", null);
		map.put("icon3", null);
		map.put("title", "扬州地税“纳税服务开放日”活动通知");
		map.put("content",
				"时间：11月4日（星期五）上午。\n     参与对象：行会（商会）组织成员、部分企业法定代表人、税收志愿者以及其他关心地税事业发展的人士。"
						+ "\n　   主要内容：体验“12366在线咨询”和“江苏地税网上办税服务厅”，听取活动参与对象对江苏地税纳税服务工作的意见和建议。"
						+ "+\n　欢迎参与。");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("bm_name", "地税局");
		map.put("bm_logo",
				BitmapFactory.decodeResource(res, R.drawable.icon_ds));

		map.put("icon1", null);
		map.put("icon2", null);
		map.put("icon3", null);
		map.put("title", "扬州地税“纳税服务开放日”活动通知");
		map.put("content",
				"时间：11月4日（星期五）上午。\n     参与对象：行会（商会）组织成员、部分企业法定代表人、税收志愿者以及其他关心地税事业发展的人士。"
						+ "\n　   主要内容：体验“12366在线咨询”和“江苏地税网上办税服务厅”，听取活动参与对象对江苏地税纳税服务工作的意见和建议。"
						+ "\n　欢迎参与。");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("bm_name", "地税局");
		map.put("bm_logo",
				BitmapFactory.decodeResource(res, R.drawable.icon_ds));
		map.put("icon1", null);
		map.put("icon2", BitmapFactory.decodeResource(res, R.drawable.icon2));
		map.put("icon3", null);
		map.put("title", "扬州地税“纳税服务开放日”活动通知");
		map.put("content",
				"时间：11月4日（星期五）上午。\n     参与对象：行会（商会）组织成员、部分企业法定代表人、税收志愿者以及其他关心地税事业发展的人士。"
						+ "\n　   主要内容：体验“12366在线咨询”和“江苏地税网上办税服务厅”，听取活动参与对象对江苏地税纳税服务工作的意见和建议。"
						+ "\n　欢迎参与。");
		list.add(map);

	}

}