package com.winksoft.android.yzjycy.ui.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.JobDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.BaseActivity;

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
import android.widget.TextView;

/**
 * comments:我的求职-试用通知
 * 
 * @author Administrator Date: 2012-9-3
 */
public class QztNoticeActivity extends BaseActivity implements OnClickListener, OnScrollListener {
	private TextView titleTxt;
	private Button backBtn, refreshBtn;
	private ListView listview;
	private JobDAL jobDAL;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private CommonAdapter adapter;
	private int pageNum = 0, lastItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_job_common_list);

		jobDAL = new JobDAL(this);

		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(this.getIntent().getStringExtra("title"));

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		refreshBtn = (Button) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(this);

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String id = list.get(arg2).get("sending_id").toString();
					Intent intent = new Intent(QztNoticeActivity.this, QztJobDetailActivity.class);
					intent.putExtra("url", Constants.IP + "android/sending/querySendingDetail?sending_id=" + id);
					intent.putExtra("title", "面试通知详情");
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					commonUtil.shortToast("信息加载失败!");
				}
			}
		});
		// 数据加载
		doLoadData();
	}

	private void doLoadData() {
		if (pageNum == 0) {
			list.clear();
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list, R.layout.qzt_job_common_list_item,
					new String[] { "companyName", "positionName", "address", "price", "createDate" },
					new int[] { R.id.companyName, R.id.positionName, R.id.address, R.id.price, R.id.createDate },
					listview);
			listview.setAdapter(adapter);
		}
		new Thread(recRunnable).start();
	}

	/**
	 * 加载信息
	 */
	private Runnable recRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(500);
				returnList = jobDAL.doNrialNotice(user.getUserId(), pageNum, "3");
				Message msg = new Message();
				msg.what = 0;
				recHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.v("数据加载线出错:", e.getMessage());
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
		}
	};

	/**
	 * 更新列表
	 */
	private void addData() {
		pageNum++;

		String state = (String) returnList.get(0).get("state");

		if (state.equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				listview.removeFooterView(commonUtil.loadingLayout);
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				String price = map.get("acc034") == null ? "面议" : map.get("acc034").toString();
				if (price.equals("")) {
					price = "面议";
				}
				map.put("companyName", map.get("aab004"));// 公司
				map.put("positionName", "岗位：" + map.get("acb216"));// 求职岗位
				map.put("price", "薪资：" + price);// 薪资
				map.put("address", "地址:" + map.get("aae006"));// 公司地址
				map.put("createDate", map.get("send_time"));// 投递日期
				list.add(returnList.get(i));
			}
		} else {
			this.commonUtil.showListAddDataState(listview, state);
		}

		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			QztNoticeActivity.this.finish();
			break;
		case R.id.refreshBtn:
			lastItem = 0;
			pageNum = 0;
			listview.removeFooterView(commonUtil.loadingLayout);
			doLoadData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastItem == adapter.count && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			doLoadData();
		}
	}

}
