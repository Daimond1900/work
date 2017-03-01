package com.yifeng.hnqzt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.HomeAdapter;
import com.yifeng.hnqzt.data.BrowseDAL;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.ListViewUtil;

/**
 * comment:浏览企业招聘信息
 * 
 * @author:ZhangYan Date:2012-8-28
 */
public class BrowseInfoActivity extends BaseActivity implements
		OnScrollListener {
	private EditText position_request;
	private Button search_btn;
	private ListView listview;
	private BrowseDAL browseDal;
	private ListViewUtil util;
	private int lastItem = 0;
	private int pageNum = 0;
	private HomeAdapter adapter;
	private List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_org_info);
		browseDal = new BrowseDAL(this);

		position_request = (EditText) findViewById(R.id.position_request);
		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lastItem = 0;
				pageNum = 0;
				doSetListView();
			}
		});

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = list.get(arg2).get("id").toString();
				if(id != null && !id.equals("")){
					Intent intent = new Intent(BrowseInfoActivity.this,CompanyDetailActivity.class);
					intent.putExtra("id", id);
					startActivity(intent);
				}
			}
		});
		doSetListView();
	}

	@Override
	public void onScroll(AbsListView v, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView v, int state) {
		if (lastItem == adapter.count
				&& state == OnScrollListener.SCROLL_STATE_IDLE) {
			doSetListView();
		}
	}

	/**
	 * 加载信息详细
	 */
	private Runnable gorupRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				/** 参数 **/
				Map<String, String> param = new HashMap<String, String>();
				param = new HashMap<String, String>();
				param.put("position_request", position_request.getText()
						.toString());
				param.put("page", pageNum + "");
				pageNum++;
				returnList = browseDal.doQuery(param,
						"android/ssnotice/doSslist");

				Message msg = new Message();
				msg.what = 0;
				gorupHandler.sendMessage(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	Handler gorupHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				addData();
			}
		}
	};

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
			adapter = new HomeAdapter(this, list,
					R.layout.browse_org_info_item, new String[] { "id",
							"companyName" }, new int[] { R.id.order_num,
							R.id.company_name }, getResources());
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();
	}

	private void addData() {
		adapter.count += 1;
		if (pageNum == 0) {
			list.clear();
		}

		String state = returnList.get(0).get("state");
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				list.add(tmap);
			}
		} else {
			util.showListAddDataState(state);

		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
	}
}
