package com.winksoft.android.yzjycy.ui.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.QztEnterpriseDal;
import com.winksoft.android.yzjycy.util.Constants;


import android.content.Intent;
import android.content.res.Resources;
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
 * comments:我的求职-关注的公司
 * @author Administrator 
 * Date: 2012-9-4
 */
public class QztAttentionActivity extends BaseActivity implements OnScrollListener
{
	private TextView titleTxt;
	private Button backBtn, refreshBtn;
	private ListView listview;
	private QztEnterpriseDal enterpriseDal;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private CommonAdapter adapter;
	private int pageNum = 0, page = 0, lastItem = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_job_common_list);
		
		enterpriseDal = new QztEnterpriseDal(this);
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(this.getIntent().getStringExtra("title"));
		
		MyOnclick onclick = new MyOnclick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(onclick);
		refreshBtn = (Button) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(onclick);
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				Intent intent = new Intent(QztAttentionActivity.this,QztJobDetailActivity.class);
				intent.putExtra("title", "详细信息");
				startActivity(intent);
			}
		});

		doLoadData();
	}
	
	private void doLoadData()
	{
		if (pageNum == 0)
		{
			list.clear();
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list, R.layout.qzt_attention_list_item, 
					new String[]{ "companyName", "count_lab", "logo" }, 
					new int[]{ R.id.companyName, R.id.count, R.id.companyLogo },listview);

			listview.setAdapter(adapter);
			adapter.setViewBinder();
		}
		new Thread(recRunnable).start();
	}

	/**
	 * 加载信息
	 */
	private Runnable recRunnable = new Runnable()
	{
		public void run()
		{
			try
			{
				Thread.sleep(500);
				// 线程加载
				returnList = enterpriseDal.doQuery();
				Message m = new Message();
				m.what = 0;
				recHandler.sendMessage(m);

			} catch (Exception e)
			{
				Log.v("数据加载线出错:", e.getMessage());
				e.printStackTrace();
			}
		}
	};
	
	Handler recHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 0)
			{
				addData();
			}
		}
	};

	/**
	 * 更新列表
	 */
	private void addData()
	{
		pageNum += 10;
		page += 1;

		Resources res = getResources();

		String state = (String) returnList.get(0).get("state");

		if (returnList.get(0).get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS)))
		{
			if (returnList.size() < 10)
				listview.removeFooterView(commonUtil.loadingLayout);
			for (int i = 0; i < returnList.size(); i++)
			{
				Map<String, Object> map = returnList.get(i);
				map.put("count_lab", "岗位:（" + map.get("gw_count") + "）,求职数:（"
						+ map.get("qz_count") + "）,关注数：（" + map.get("gz_count") + "）");
			
				list.add(map);
			}
		} else
		{
			this.commonUtil.showListAddDataState(listview, state);
		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	private class MyOnclick implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.backBtn:
				QztAttentionActivity.this.finish();
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

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
		lastItem = firstVisibleItem + visibleItemCount - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int state)
	{
		if (lastItem == adapter.count
				&& state == OnScrollListener.SCROLL_STATE_IDLE)
		{
			doLoadData();
		}

	}

}
