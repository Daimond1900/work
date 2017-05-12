package com.winksoft.android.yzjycy.ui.bminfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.RegisterDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.ListViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * ClassName:RegisterPostListActivity Description：报名信息-岗位的报名情况
 * 
 * @author Administrator Date：2012-10-22
 */
public class Zpt_RegisterPostListActivity extends BaseActivity implements OnScrollListener {
	private Button backBtn, qbBtn, ytzBtn, yjjBtn;
	private String flag = "";// Tab标志位：0.全部；1.已拒绝；4.已通知
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private RegisterDAL registerDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private Intent qz;
	private boolean isLoading = true;// 标志正在加载数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_register_post_list);

		registerDAL = new RegisterDAL(this);

		qz = this.getIntent();

		MyClick myClick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myClick);
		qbBtn = (Button) findViewById(R.id.qbBtn);
		qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
		qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
		qbBtn.setOnClickListener(myClick);
		ytzBtn = (Button) findViewById(R.id.ytzBtn);
		ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
		ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
		ytzBtn.setOnClickListener(myClick);
		yjjBtn = (Button) findViewById(R.id.yjjBtn);
		yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
		yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
		yjjBtn.setOnClickListener(myClick);

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Intent intent = new Intent(Zpt_RegisterPostListActivity.this, ZptPersonalResumeActivity.class);
					intent.putExtra("c_yhm", list.get(arg2).get("itemName").toString());
					intent.putExtra("id", list.get(arg2).get("id").toString());
					intent.putExtra("c_id", list.get(arg2).get("sending_id").toString());
					intent.putExtra("c_tag", list.get(arg2).get("sta").toString());
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		pageNum = 0;
		lastItem = 0;
		doLoadData();
	}

	private void doLoadData() {
		if (pageNum == 0) {
			list.clear();
			util.removeFootBar();
			util.addFootBar();
			adapter = new CommonAdapter(this, list, R.layout.zpt_register_post_list_item1,
					new String[] { "itemName", "itemSex", "itemTag", "itemContact", "itemDate" },
					new int[] { R.id.item_nameTxt, R.id.item_sexTxt, R.id.item_tagTxt, R.id.item_contactTxt,
							R.id.item_dateTxt },
					getResources());
			if (flag.equals("")) {
				adapter.isHtml = true; // 状态的显示，在适配器中改样式。
			}
			adapter.setViewBinder();
			listview.setAdapter(adapter);
		}
		if (isLoading) {
			isLoading = false;
			new Thread(myRunnable).start();
		}
	}

	/**
	 * 加载信息
	 */
	private Runnable myRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("page", pageNum + ""));
				param.add(new BasicNameValuePair("aab001", user.getUserId()));// 公司id
				param.add(new BasicNameValuePair("acb210", qz.getStringExtra("post_id") + ""));// 岗位编号
				param.add(new BasicNameValuePair("state", flag));// 三个按钮的标志位
				pageNum++;
				returnList = registerDAL.doPostQuery(param, "android/sending/listSendingByJob", "sendings");
				Message msg = myHandler.obtainMessage();
				msg.what = 0;
				myHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.v("数据加载线程出错:", e.getMessage());
				e.printStackTrace();
			}
		}
	};

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				addData();
			}
			isLoading = true;
		}
	};

	/**
	 * 更新列表
	 */
	private void addData() {
		adapter.count += 1;
		if (pageNum == 0) {
			list.clear();
		}
		String state = (String) returnList.get(0).get("state");
		if (returnList.get(0).get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				map.put("itemName", map.get("xm") == null ? "" : map.get("xm"));// 姓名
				map.put("itemSex", map.get("xb") == null ? "" : map.get("xb"));// 性别
				map.put("itemContact", map.get("lxdh") == null ? "" : map.get("lxdh"));// 联系电话
				String date = map.get("send_time").toString();
				String date1 = date.substring(0, 16);
				map.put("itemDate", date1);// 日期
				list.add(map);
			}
		} else {
			util.showListAddDataState(state);
		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
	}

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				Zpt_RegisterPostListActivity.this.finish();
				break;
			case R.id.qbBtn:
				flag = "";
				qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
				qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
				ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				lastItem = 0;
				pageNum = 0;
				doLoadData();
				break;
			case R.id.ytzBtn:
				flag = "2";
				qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
				ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
				yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				lastItem = 0;
				pageNum = 0;
				doLoadData();
				break;
			case R.id.yjjBtn:
				flag = "1";
				qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
				ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
				yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
				yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
				lastItem = 0;
				pageNum = 0;
				doLoadData();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int state) {
		if (lastItem == adapter.count && state == OnScrollListener.SCROLL_STATE_IDLE) {
			doLoadData();
		}
	}

}
