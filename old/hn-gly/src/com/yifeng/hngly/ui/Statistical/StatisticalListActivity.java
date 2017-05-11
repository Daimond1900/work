package com.yifeng.hngly.ui.Statistical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.yifeng.hngly.adapter.CommonAdapter;
import com.yifeng.hngly.ui.BaseActivity;
import com.yifeng.hngly.ui.R;
import com.yifeng.hngly.util.ConstantUtil;

public class StatisticalListActivity extends BaseActivity implements OnClickListener{
	private CommonAdapter adapter;
	private ListView listView;
	private List<Map<String, Object>> list;
	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistical_list);
		
		backBtn = (Button) findViewById(R.id.back);
		backBtn.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					Intent intent = new Intent(StatisticalListActivity.this,StatisticalActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title",list.get(arg2).get("title").toString());
					bundle.putString("url",ConstantUtil.ip+ list.get(arg2).get("url"));
					intent.putExtras(bundle);
					startActivity(intent);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		loadMenu();
	}

	private void loadMenu() {
		list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "1");
		map.put("title", "注册人数统计");
		map.put("url", "android/report/countRegistration");
		map.put("icon", R.drawable.new_mainpage_jyxx);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "2");
		map.put("title", "招聘发布统计");
		map.put("url", "android/report/countRecruitment");
		map.put("icon", R.drawable.new_mainpage_jyxx);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "3");
		map.put("title", "简历投递统计");
		map.put("url", "android/report/countSending");
		map.put("icon", R.drawable.new_mainpage_jyxx);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "4");
		map.put("title", "基层信息统计");
		map.put("url", "android/report/countLabours");
		map.put("icon", R.drawable.new_mainpage_jyxx);
		list.add(map);
		
		adapter=new CommonAdapter(this, list, R.layout.statistical_list_item, 
				new String[]{"title","icon"}, 
				new int[]{R.id.title,R.id.icon}, listView);
		listView.setAdapter(adapter);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		default:
			break;
		}
	}
}
