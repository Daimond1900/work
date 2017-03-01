package com.yifeng.hnzpt.ui.employ;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.CommonAdapter;
import com.yifeng.hnzpt.data.ManageDAL;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.ui.register.PersonalResumeActivity;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.ListViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ClassName:ManageListActivity 
 * Description：录用查询
 * @author Administrator 
 * Date：2012-10-23
 */
public class EmployListActivity extends BaseActivity implements OnScrollListener {
	private Button backBtn, refreshBtn;
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private ManageDAL manageDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private boolean isLoading = true;// 标志正在加载数据
	private Intent qz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.employ_list);

		manageDAL = new ManageDAL(this);

		qz = this.getIntent();
		
		MyClick myClick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myClick);
		refreshBtn = (Button) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(myClick);

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Intent intent = new Intent(EmployListActivity.this, PersonalResumeActivity.class);
					intent.putExtra("c_yhm", list.get(arg2).get("itemName").toString());
					intent.putExtra("c_id", list.get(arg2).get("sending_id").toString());
					intent.putExtra("id", list.get(arg2).get("id").toString());
					intent.putExtra("c_tag", "3");// 录用
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// 数据加载
		doLoadData();
	}

	private void doLoadData() {
		if (pageNum == 0) {
			list.clear();
			util.removeFootBar();
			util.addFootBar();
			adapter = new CommonAdapter(this, list, R.layout.employ_list_item, 
					new String[] { "itemName", "itemSex", "itemContact", "itemDate" }, 
					new int[] { R.id.item_nameTxt, R.id.item_sexTxt, R.id.item_contactTxt, R.id.item_dateTxt }, getResources());
			listview.setAdapter(adapter);
			adapter.setViewBinder();
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
				param.add(new BasicNameValuePair("aab001", user.getUserId()));
				param.add(new BasicNameValuePair("acb210", qz.getStringExtra("post_id")));
				param.add(new BasicNameValuePair("state", "3"));
				pageNum++;
				returnList = manageDAL.doPostQuery(param, "android/sending/listSendingByJob");
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
		if (returnList.get(0).get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				map.put("itemName", map.get("xm"));// 姓名
				map.put("itemSex", map.get("xb"));// 性别
				map.put("itemContact", map.get("lxdh"));// 联系电话
				String date = map.get("send_time").toString();
				String date1 = date.substring(0, 10);
				map.put("itemDate", date1); // 报名时间
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
				EmployListActivity.this.finish();
				break;
			case R.id.refreshBtn:
				lastItem = 0;
				pageNum = 0;
				list.clear();
				util.removeFootBar();
				adapter.notifyDataSetChanged();
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
