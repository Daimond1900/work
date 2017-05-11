package com.yifeng.ChifCloud12345.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.adapter.HomeAdapter;

/**
 * 在线视频
 * 
 * @author wujiahong
 * 
 */
public class VideoMenu extends BaseActivity {
	ListView listview;
	HomeAdapter adapter;
	LinearLayout top;
	Button back;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_menu);
		
	    listview = (ListView) findViewById(R.id.listview);
	    
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
		adapter = new HomeAdapter(this, list, R.layout.home_item1,
				new String[] { "vimg", "row_name", "counts" }, new int[] {
						R.id.vimg, R.id.row_name, R.id.counts }, getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemid = Integer.parseInt(list.get(position).get("id")
						.toString());
				switch (itemid) {

				case 1:
					// 切片新闻
					Intent intent = new Intent(VideoMenu.this,
							VideoList.class);
					intent.putExtra("title",(String)list.get(position).get("row_name"));
					startActivity(intent);

					break;

				case 2:
					// 高清视频

					intent = new Intent(VideoMenu.this,VideoList.class);
							intent.putExtra("title",(String)list.get(position).get("row_name"));
					startActivity(intent);
					break;
				
				default:
					break;

				}
			}
		});

		addData();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

	void addData() {
		Resources res = getResources();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.qpxw));
		map.put("id", "1");
		map.put("row_name", "切片新闻");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.gqsp));
		map.put("id", "2");
		map.put("row_name", "高清视频");
		list.add(map);
	}
}