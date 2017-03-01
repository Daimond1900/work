package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.yifeng.ChifCloud12345.service.PushMessageReceiver;
import com.yifeng.ChifCloud12345.video.VideoList;
import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.AlertDAL;
import com.yifeng.entity.User;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ConstantUtil;

/**
 * 首页
 */
public class MainPageActivity extends BaseActivity {
	ListView listview;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	HomeAdapter adapter;
	WebView webview;
	private TextView main_msg;
	private AlertDAL alertDal;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sy);
		ConstantUtil.STATE = true;
		user = LoginBiz.loadUser(this);
		alertDal = new AlertDAL(this);

		this.initBottom();
		this.setFocus(this.bt_bottom_menu4, R.drawable.bottom_menu4_);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new HomeAdapter(this, list, R.layout.sy_item1, new String[] { "icon1", "content", "count" },
				new int[] { R.id.icon1, R.id.content, R.id.count }, getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				int itemid = Integer.parseInt(list.get(arg2).get("id").toString());
				switch (itemid) {
				case 1:
					// 视频新闻
					Intent intent = new Intent(MainPageActivity.this, VideoList.class);
					intent.putExtra("title", "扬州视频新闻");
					intent.putExtra("type", "0");
					startActivity(intent);
					break;
				case 2:
//					// 高清实况
//					intent = new Intent(MainPageActivity.this, VideoList.class);
//					intent.putExtra("title", "高清实况");
//					intent.putExtra("type", "1");
//					startActivity(intent);
//					break;
				case 3:
					// 重要提醒
					intent = new Intent(MainPageActivity.this, ImportantEventActivity.class);
					startActivity(intent);
					break;
				case 4:
					// 超时工单
					intent = new Intent(MainPageActivity.this, OrverTimeDeptActivity.class);
					startActivity(intent);
					break;
				case 5:
					// 领导批示
					intent = new Intent(MainPageActivity.this, ReadyToDoListActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}

			}

		});

		addData();
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl(getString(R.string.ipconfig) + "/android/maqueen/doGetLatestMsg?key=" + user.getKey());
		webview.setBackgroundColor(0);
		main_msg = (TextView) findViewById(R.id.main_msg);
		main_msg.setText("欢迎" + user.getUserName() + user.getJob());
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Thread(runnable).start();
	}

	private Map<String, String> map;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				Map<String, String> param = new HashMap<String, String>();
				param.put("user_id", user.getUserId());
				param.put("department_id", user.getOrg_id());
				param.put("role_id", user.getRole_type());
				map = alertDal.doAlert(param);
				handler.sendEmptyMessage(10001);
			} catch (InterruptedException e) {

			}
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 10001) {
				if (map != null) {
					if (map.get("state") != null && map.get("state").equals("1")) {
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).get("id").equals("1")) {
								list.get(i).put("count", map.get("spxw") == null ? "0" : map.get("spxw"));
							} else if (list.get(i).get("id").equals("2")) {
								list.get(i).put("count", map.get("gqsp") == null ? "0" : map.get("gqsp"));
							} else if (list.get(i).get("id").equals("3")) {
								list.get(i).put("count", map.get("zytx") == null ? "0" : map.get("zytx"));
							} else if (list.get(i).get("id").equals("4")) {
								list.get(i).put("count", map.get("overtime") == null ? "0" : map.get("overtime"));
							}
						}
						adapter.notifyDataSetChanged();
					}
				}
			}
		};
	};

	void addData() {
		Resources res = getResources();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.qpxw));
		map.put("count", "0");
		map.put("content", "视频新闻");
		map.put("id", "1");
		list.add(map);

//		map = new HashMap<String, Object>();
//		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.gqsp));
//		map.put("count", "0");
//		map.put("content", "高清实况");
//		map.put("id", "2");
//		list.add(map);

		map = new HashMap<String, Object>();
		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.sy_zdsj));
		map.put("count", "0");
		map.put("content", "重要提醒");
		map.put("id", "3");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("icon1", BitmapFactory.decodeResource(res, R.drawable.csgd));
		map.put("count", "0");
		map.put("content", "超时工单");
		map.put("id", "4");
		list.add(map);
	}

}
