package com.winksoft.android.yzjycy.ui.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyCheckBoxAdapter;
import com.winksoft.android.yzjycy.adapter.MyCheckBoxAdapter.ViewHolder;
import com.winksoft.android.yzjycy.data.RecruitmentDAL;
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
 * ClassName:ChooseJobsActivity3
 * Description：招聘登记-选择招聘岗位（三级列表，带CheckBox多选框）
 * @author Administrator
 * Date：2012-10-20
 */
public class Zpt_ChooseJobsActivity3 extends BaseActivity implements OnScrollListener
{
	private Button backBtn;
	private ListView listview;
	private ListViewUtil util;
	private MyCheckBoxAdapter adapter;
	private RecruitmentDAL recruitmentDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private Intent qz;
	private ArrayList<String> listStr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_recruitment_choose_jobs_list);

		recruitmentDAL = new RecruitmentDAL(this);

		qz = this.getIntent();

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Zpt_ChooseJobsActivity3.this.finish();
			}
		});

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listStr = new ArrayList<String>();
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				ViewHolder holder = (ViewHolder) arg1.getTag();
				holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
				MyCheckBoxAdapter.isSelected.put(arg2, holder.cb.isChecked()); // 同时修改map的值保存状态
				if (holder.cb.isChecked() == true)
				{
					listStr.add((String)list.get(arg2).get("aca112"));
				} else
				{
					listStr.remove((String)list.get(arg2).get("aca112"));
				}
				dialogUtil.shortToast(listStr.toString());
				try
				{
					String post = list.get(arg2).get("aca112").toString();
					Intent intent = new Intent(Zpt_ChooseJobsActivity3.this, Zpt_RecruitmentActivity.class);
					intent.putExtra("post", post);
					startActivity(intent);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		// 数据加载
		doLoadData();

	}

	private void doLoadData()
	{
		if (pageNum == 0)
		{
			list.clear();
			util.removeFootBar();
			util.addFootBar();
			
			adapter = new MyCheckBoxAdapter(this, list, R.layout.zpt_recruitment_choose_jobs_list_item2, 
					new String[]{ "itemPost", "itemCheckBox" }, 
					new int[]{ R.id.item_postTxt, R.id.item_checkbox });
			listview.setAdapter(adapter);
			//adapter.setViewBinder();
		}
		new Thread(myRunnable).start();
	}

	/**
	 * 加载信息
	 */
	private Runnable myRunnable = new Runnable()
	{
		public void run()
		{
			try
			{

				Thread.sleep(300);

				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("page", pageNum + ""));
				param.add(new BasicNameValuePair("grade", "3" + ""));
				param.add(new BasicNameValuePair("pomark", qz.getStringExtra("jobsId")));
				pageNum++;
				returnList = recruitmentDAL.doPostQuery(param, "android/recruitmentcloud/listPosition");
				Message msg = myHandler.obtainMessage();
				msg.what = 0;
				myHandler.sendMessage(msg);

			} catch (Exception e)
			{
				Log.v("数据加载线程出错:", e.getMessage());
				e.printStackTrace();
			}
		}
	};

	Handler myHandler = new Handler()
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
		//adapter.count += 1;
		if (pageNum == 0)
		{
			list.clear();
		}

		String state = (String) returnList.get(0).get("state");

		if (returnList.get(0).get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS)))
		{
			if (returnList.size() < 10)
				// listview.removeFooterView(commonUtil.loadingLayout);
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++)
			{

				Map<String, Object> map = returnList.get(i);
				map.put("itemPost", map.get("aca112"));
				map.put("itemCheckBox", false);
				list.add(map);
			}
		} else
		{
			util.showListAddDataState(state);
		}

		//adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		lastItem = firstVisibleItem + visibleItemCount - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int state)
	{
		if (lastItem == adapter.getCount() && state == OnScrollListener.SCROLL_STATE_IDLE)
		{
			doLoadData();
		}

	}

}
