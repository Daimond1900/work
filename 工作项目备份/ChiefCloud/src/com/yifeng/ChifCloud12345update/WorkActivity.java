package com.yifeng.ChifCloud12345update;

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

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.ReadyToDoDal;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 
 * 
 * @author Administrator 已办事宜
 */

public class WorkActivity extends BaseActivity implements OnScrollListener {
	private ListView listview;
	private HomeAdapter adapter;
	private LinearLayout top;
	private Button back;
	private ProgressBar progressBar;
	private LinearLayout loadingLayout;
	private int lastItem = 0;
	private String type_id = "";
	private int pageNum = 0;
	private ReadyToDoDal readyDal;
	private TextView title;
	String name, ORG_ID;
	private ListViewUtil util;
	/***
	 * 
	 10031109341234120160 咨询 11052520593959390002 建议 10082511353735370003 投诉
	 * 11030818023423400050 求助 11101309094894800003 举报
	 */
	private Map<String, String> param;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csgd);
		listview = (ListView) findViewById(R.id.listview);
		param = new HashMap<String, String>();
		readyDal = new ReadyToDoDal(this);
		util = new ListViewUtil(this, listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WorkActivity.this.finish();
			}
		});
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();

		title = (TextView) findViewById(R.id.title);
		title.setText("已办事宜");

		listview.setOnScrollListener(this);
		Intent intent = getIntent();
		ORG_ID = intent.getStringExtra("ORG_ID");
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(WorkActivity.this,
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

		// 加载信息
		doSetListView();

		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

	private void doSetListView() {
		if(total_pages==pageNum){
			return;
		}
		/** 参数 **/
		param.put("u_id", user.getUserId());
		param.put("org_id", ORG_ID);
		param.put("form_type", type_id);
		param.put("page", pageNum + "");
		param.put("role_id", user.getRole_type());
		//
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
			adapter = new HomeAdapter(this, list, R.layout.ybsy_item,
					new String[] { "content_text", "title", "addtime" },
					new int[] { R.id.content, R.id.title, R.id.time },
					getResources());

			adapter.setViewBinder();
			listview.setAdapter(adapter);
		}
		new Thread(gorupRunnable).start();

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
	
	
	private int total_pages = -1;
	void addData() {
		try {
			String json = readyDal
					.doQueryUndealedForms1(param);
			Map<String, String> m = DataConvert.toMap(json);
			if(m.get("success") !=null && m.get("success").equals("false")){
				commonUtil.shortToast(m.get("msg"));
				util.removeFootBar();
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
				util.removeFootBar();
			}
			else if (m.get("state")
					.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
				total_pages = Integer.parseInt(m.get("total-pages"));
				List<Map<String, String>> returnList = DataConvert.toArrayList(m.get("forms"));
				if(returnList!=null && returnList.size()>0){
					if (returnList.size() < 10 || total_pages==pageNum)
						util.removeFootBar();

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
					util.removeFootBar();
				}
			}

		} catch (Exception e) {
			this.commonUtil.showToast(getString(R.string.fail_msg));
			util.removeFootBar();
		}

	}

}