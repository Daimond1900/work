package com.yifeng.hnzpt.ui.recruitment;

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
import com.yifeng.hnzpt.data.RecruitmentDAL;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.util.ActivityStackControlUtil;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.ListViewUtil;

/**
 * ClassName:ChooseJobsActivity1 
 * Description：招聘登记-选择招聘岗位（一级列表）
 * @author Administrator 
 * Date：2012-10-19
 */
public class ChooseJobsActivity1 extends BaseActivity implements OnScrollListener {
	private Button backBtn;
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private RecruitmentDAL recruitmentDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private boolean isLoading = true;// 标志正在加载数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recruitment_choose_jobs_list);

		recruitmentDAL = new RecruitmentDAL(this);

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChooseJobsActivity1.this.finish();
			}
		});

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					if(list.size() > 0){
						String postId = list.get(arg2).get("aca111").toString();
						String post_id = postId.substring(0, 1);
						Intent intent = new Intent(ChooseJobsActivity1.this, ChooseJobsActivity2.class);
						intent.putExtra("jobsId", post_id);
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
					dialogUtil.shortToast("未响应!");
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
			adapter = new CommonAdapter(this, list, R.layout.recruitment_choose_jobs_list_item1_1, 
					new String[] { "itemPost" }, new int[] { R.id.item_postTxt }, getResources());
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
				param.add(new BasicNameValuePair("aca11a", "1" + ""));
				param.add(new BasicNameValuePair("aca111", ""));
				pageNum++;
				returnList = recruitmentDAL.doPostQuery(param, "android/system/listJob");
				/*
				 * Message msg = myHandler.obtainMessage(); msg.what = 0;
				 * myHandler.sendMessage(msg);
				 */
				/*
				 * Message msg = new Message(); msg.what = 0;
				 * myHandler.sendMessage(msg);
				 */
				myHandler.sendEmptyMessage(1);

			} catch (Exception e) {
				Log.v("数据加载线程出错:", e.getMessage());
				e.printStackTrace();
				myHandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
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
			if (returnList.size() < 15)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				map.put("itemPost", map.get("aca112"));
				list.add(map);
			}
		} else {
			util.showListAddDataState(state);
		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
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
