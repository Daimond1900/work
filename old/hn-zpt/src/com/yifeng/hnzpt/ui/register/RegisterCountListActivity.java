package com.yifeng.hnzpt.ui.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.CommonAdapter;
import com.yifeng.hnzpt.data.RegisterDAL;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.ListViewUtil;

/**
 * ClassName:RegisterCountListActivity 
 * Description：报名信息-统计页面
 * @author Administrator 
 * Date：2012-10-19
 */
public class RegisterCountListActivity extends BaseActivity implements OnScrollListener {
	private Button backBtn, refreshBtn;
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private RegisterDAL registerDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private boolean isLoading = true;// 标志正在加载数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_count_list);

		registerDAL = new RegisterDAL(this);

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
					String postId = list.get(arg2).get("acb210").toString();
					Intent intent = new Intent(RegisterCountListActivity.this, RegisterPostListActivity.class);
					intent.putExtra("post_id", postId);// 岗位编号
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
			util.addFootBar();
			adapter = new CommonAdapter(this, list, R.layout.register_count_list_item, 
					new String[] { "itemPost", "itemNumber", }, 
					new int[] { R.id.item_postTxt, R.id.item_numberTxt }, getResources());
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
				param.add(new BasicNameValuePair("aab001", user.getUserId()));// 公司Id
				param.add(new BasicNameValuePair("state", ""));// 公司Id
				pageNum++;
				returnList = registerDAL.doPostQuery(param, "android/sending/groupSendingByJob","jobs");

				Message msg = recHandler.obtainMessage();
				msg.what = 0;
				recHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.v("数据加载线程出错:", e.getMessage());
				e.printStackTrace();
			}
		}
	};

	Handler recHandler = new Handler() {
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
				map.put("itemPost", map.get("acb216"));// 岗位名称
				map.put("itemNumber", map.get("num"));// 人数
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
				RegisterCountListActivity.this.finish();
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
