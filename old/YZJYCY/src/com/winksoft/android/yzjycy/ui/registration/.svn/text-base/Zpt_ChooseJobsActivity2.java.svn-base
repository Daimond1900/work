package com.winksoft.android.yzjycy.ui.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.data.RecruitmentDAL;
import com.winksoft.android.yzjycy.BaseActivity;
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
 * ClassName:ChooseJobsActivity2 
 * Description：招聘登记-选择招聘岗位（二级列表）
 * @author Administrator 
 * Date：2012-10-19
 */
public class Zpt_ChooseJobsActivity2 extends BaseActivity implements OnScrollListener {
	private Button backBtn;
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private RecruitmentDAL recruitmentDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private Intent qz;
	private boolean isLoading = true;// 标志正在加载数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_recruitment_choose_jobs_list);

		recruitmentDAL = new RecruitmentDAL(this);

		qz = this.getIntent();

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Zpt_ChooseJobsActivity2.this.finish();
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
						String post_id = postId.substring(0, 3);
						Intent intent = new Intent(Zpt_ChooseJobsActivity2.this, Zpt_ChooseJobsActivity4.class);
						intent.putExtra("jobsId", post_id);
						startActivity(intent);
					}
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
			adapter = new CommonAdapter(this, list, R.layout.zpt_recruitment_choose_jobs_list_item1_2, 
					new String[] { "itemPost" }, new int[] { R.id.item_postTxt }, getResources());
			listview.setAdapter(adapter);
			// adapter.setViewBinder();
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
				param.add(new BasicNameValuePair("aca11a", "2" + ""));
				String postId = qz.getStringExtra("jobsId");
				param.add(new BasicNameValuePair("aca111", postId));
				pageNum++;
				returnList = recruitmentDAL.doPostQuery(param, "android/system/listJob");
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
