package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yifeng.ChifCloud12345.video.VideoList;
import com.yifeng.adapter.HomeAdapter;

/**
 * 热线点评
 * @author Administrator
 * 
 */
public class HomeActivity extends BaseActivity {
	private ListView listview;
	private HomeAdapter adapter;
	private LinearLayout top;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		listview = (ListView) findViewById(R.id.listview);
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		adapter = new HomeAdapter(this, list, R.layout.home_item, new String[] { "vimg", "row_name" },
				new int[] { R.id.vimg, R.id.row_name }, getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemid = Integer.parseInt(list.get(position).get("id").toString());
				switch (itemid) {
				case 0:
					Intent intent = new Intent(HomeActivity.this, ForWebViewActivty.class);
					intent.putExtra("title_name", "当日点评");
					intent.putExtra("url", "android/drdp/doLastRecord" + "?key=" + user.getKey() + "&type=2");
					startActivity(intent);

					break;

				/**
				 * 2016/11/23 删除 ---热点问题
				 */
				/*
				 * case 1: intent = new Intent(HomeActivity.this,
				 * ForWebViewActivty.class); intent.putExtra("title_name",
				 * "热点问题");
				 * intent.putExtra("url","android/rdwt/doLastRecord"+"?key="+
				 * user.getKey()+"&type=5"); startActivity(intent);
				 * 
				 * break;
				 */

				case 2:
					// 经验交流
					intent = new Intent(HomeActivity.this, TalkActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(HomeActivity.this, ForWebViewActivty.class);
					intent.putExtra("title_name", "办结排行");
					intent.putExtra("url", "android/bjph/doLastRecord" + "?key=" + user.getKey() + "&type=7");
					startActivity(intent);

					break;

				case 4:
					// intent = new Intent(HomeActivity.this,
					// ForWebViewActivty.class);
					// intent.putExtra("title_name", "每月简报");
					// intent.putExtra("url","android/myjb/doLastRecord"+"?key="+user.getKey()+"&type=3");
					// startActivity(intent);
					intent = new Intent(HomeActivity.this, EveryMonthActivity.class);
					// intent.putExtra("title_name", "每月简报");
					// intent.putExtra("url","android/myjb/doLastRecord"+"?key="+user.getKey()+"&type=3");
					startActivity(intent);

					break;
				case 5:
					// intent = new Intent(HomeActivity.this,
					// ForWebViewActivty.class);
					// intent.putExtra("title_name", "年度总结");
					// intent.putExtra("url","android/ndzj/doLastRecord"+"?key="+user.getKey()+"&type=4");
					// startActivity(intent);

					intent = new Intent(HomeActivity.this, EveryYearActivity.class);
					startActivity(intent);
					break;
				case 6:
					// 视频新闻
					intent = new Intent(HomeActivity.this, VideoList.class);
					intent.putExtra("title", "扬州视频新闻");
					intent.putExtra("type", "0");
					startActivity(intent);

					break;
				case 7:
					// 高清实况
//					intent = new Intent(HomeActivity.this, VideoList.class);
//					intent.putExtra("title", "高清实况");
//					intent.putExtra("type", "1");
//					startActivity(intent);

					// 高清实况
					/*
					 * String video_url="rtsp://vscm1.kuaiyibo.com/ntjx/mbc1";
					 * if(commonUtil.isWifi()!=1){ startActivity(new
					 * Intent(Intent.ACTION_VIEW, Uri.parse(video_url))); }else{
					 * commonUtil.shortToast("必须是3G网才可以播放!"); }
					 */

					break;

				/**
				 * 2016/11/23 删除 ---部门接听
				 */
				/*
				 * case 8: intent = new Intent(HomeActivity.this,
				 * DeptAnswerActivity.class); startActivity(intent);
				 * 
				 * break;
				 */
				default:
					break;

				}

			}
		});

		addData();
		adapter.notifyDataSetChanged();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu1, R.drawable.bottom_menu1_);

	}

	void addData() {
		Resources res = getResources();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.drdp));
		map.put("row_name", "当日点评");
		map.put("id", "0");
		list.add(map);

		/**
		 * 2016/11/23 删除
		 */
		/*
		 * map = new HashMap<String, Object>(); map.put("vimg",
		 * BitmapFactory.decodeResource(res, R.drawable.rdwt));
		 * map.put("row_name", "热点问题"); map.put("id", "1"); list.add(map);
		 */

		/**
		 * 2016/11/23 删除
		 */
		/*
		 * map = new HashMap<String, Object>(); map.put("vimg",
		 * BitmapFactory.decodeResource(res, R.drawable.bmjt));
		 * map.put("row_name", "部门接听"); map.put("id", "8"); list.add(map);
		 */

		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.bjph));
		map.put("row_name", "办结排行");
		map.put("id", "3");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.myjb));
		map.put("row_name", "每月简报");
		map.put("id", "4");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.ndzj));
		map.put("row_name", "年度总结");
		map.put("id", "5");
		list.add(map);

		/*
		 * map = new HashMap<String, Object>(); map.put("vimg",
		 * BitmapFactory.decodeResource(res, R.drawable.dbsy));
		 * map.put("row_name", "政策动态"); list.add(map);
		 */

		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.zxsp));
		map.put("row_name", "扬州视频新闻");
		map.put("id", "6");
		list.add(map);

//		map = new HashMap<String, Object>();
//		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.gqsp));
//		map.put("row_name", "高清实况");
//		map.put("id", "7");
//		list.add(map);

		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.jyjl));
		map.put("row_name", "经验交流");
		map.put("id", "2");
		list.add(map);

	}

}