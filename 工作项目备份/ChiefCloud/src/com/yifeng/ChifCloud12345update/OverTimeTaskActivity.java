package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.DateUtil;

/**
 * 超时工单
 * 
 * @author Administrator
 * 
 *         10031109341234120160 咨询 11052520593959390002 建议 10082511353735370003
 *         投诉 11030818023423400050 求助 11101309094894800003 举报
 */
public class OverTimeTaskActivity extends BaseActivity implements
		OnScrollListener {
	ListView listview;
	HomeAdapter adapter;
	Button back;
	String name, ORG_ID;
	FormDAL dal;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csgd);
		listview = (ListView) findViewById(R.id.listview);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OverTimeTaskActivity.this.finish();
			}
		});
		dal = new FormDAL(this);
		Intent intent = getIntent();
		ORG_ID = intent.getStringExtra("ORG_ID");
		listview.setOnScrollListener(this);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(OverTimeTaskActivity.this,
						WebViewForMyWorkActivty.class);
				intent.putExtra("title_name", "工单详情");
				intent.putExtra(
						"url",
						"android/wsform/doQueryFormDetail?key=" + user.getKey()
								+ "&form_id="
								+ list.get(position).get("form_id"));
				startActivity(intent);
			}
		});

		doSetListView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

	/**
	 * 加载信息详细
	 */
	private Runnable gorupRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				gorupHandler.sendMessage(gorupHandler.obtainMessage());
			} catch (InterruptedException e) {
			}
		}
	};
	Handler gorupHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			addData();

		}
	};
	public String form_id = "";
	
	private int total_pages = -1;
	void addData() {
		try {
			String json = dal.doQueryOvertimeForms(ORG_ID,
					pageNum + "", type_id, LoginBiz.loadUser(this).getUserId(),
					LoginBiz.loadUser(this).getRole_type());
			Map<String, String> m = DataConvert.toMap(json);
			if(m.get("success") !=null && m.get("success").equals("false")){
				commonUtil.shortToast(m.get("msg"));
				removeFootBar();
				return;
			}
			pageNum++;
			if (m.get("state")
					.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
				this.commonUtil.showToast(getString(R.string.server_error_msg));
			}
			else if (m.get("state")
					.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
				this.commonUtil.showToast(getString(R.string.inner_error_msg));

			} else if (m.get("state")
					.equals(String.valueOf(ConstantUtil.LOGIN_FAIL))) {
				this.commonUtil.shortToast("没有更多数据。");
				removeFootBar();
			}
			else if (m.get("state")
					.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
				total_pages = Integer.parseInt(m.get("total-pages"));
				List<Map<String, String>> returnList = DataConvert.toArrayList(m.get("forms"));
				if(returnList!=null && returnList.size()>0){
					if (returnList.size() < 10 || total_pages==pageNum)
						removeFootBar();

					for (int i = 0; i < returnList.size(); i++) {
						Map tmap = returnList.get(i);
						tmap.put("addtime",
								DateUtil.getDate1((String) tmap.get("create_date")));

						list.add(tmap);
					}
					adapter.count = list.size();
					adapter.notifyDataSetChanged();
					
				}else{
					this.commonUtil.shortToast("没有更多数据。");
					removeFootBar();
				}
			}

		} catch (Exception e) {
			this.commonUtil.showToast(getString(R.string.fail_msg));
			removeFootBar();
		}

	}

	private void doSetListView() {
		if(total_pages==pageNum){
			return;
		}
		if (pageNum == 0) {
			list.clear();
			addFootBar();
			adapter = new HomeAdapter(this, list, R.layout.ldps_gdlb_item,
					new String[] { "content_text", "addtime", "title"}, new int[] {
							R.id.content, R.id.time, R.id.title },
					getResources());
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();

	}

	private String type_id = "";
	int pageNum = 0;
	ProgressBar progressBar;
	LinearLayout loadingLayout;

	private void removeFootBar() {
		listview.removeFooterView(loadingLayout);
	}

	private void addFootBar() {

		if (loadingLayout == null) {
			LinearLayout searchLayout = new LinearLayout(this);
			searchLayout.setOrientation(LinearLayout.HORIZONTAL);
			progressBar = new ProgressBar(this);
			progressBar.setPadding(0, 0, 15, 0);
			searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			TextView textView = new TextView(this);
			textView.setText("加载中...");
			textView.setGravity(Gravity.CENTER_VERTICAL);
			searchLayout.addView(textView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			searchLayout.setGravity(Gravity.CENTER);
			loadingLayout = new LinearLayout(this);
			loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			loadingLayout.setGravity(Gravity.CENTER);
		}
		listview.addFooterView(loadingLayout);
	}

	int lastItem = 0;

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

}