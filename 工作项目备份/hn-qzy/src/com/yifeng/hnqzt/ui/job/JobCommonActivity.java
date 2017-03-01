package com.yifeng.hnqzt.ui.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.data.JobDAL;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.ConstantUtil;

/**
 * comments:我的求职-公共(1.等待公司确认；2.简历投递失败；3.收藏的职位)
 * @author Administrator
 * Date: 2012-9-4
 */
public class JobCommonActivity extends BaseActivity implements OnClickListener,OnScrollListener
{
	private TextView titleTxt;
	private Button backBtn, refreshBtn;
	private ListView listview;
	private JobDAL jobDAL;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private CommonAdapter adapter;
	private int pageNum = 0, lastItem = 0;
	private String type="0";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_common_list);

		jobDAL = new JobDAL(this);

		titleTxt = (TextView) findViewById(R.id.titleTxt);
		
		Intent qz = this.getIntent();
		titleTxt.setText(qz.getStringExtra("title"));
		type=qz.getStringExtra("type")==null?"0":qz.getStringExtra("type");//等待，失败
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		refreshBtn = (Button) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(this);

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				try{
				String id=list.get(arg2).get("sending_id").toString();
				Intent intent = new Intent(JobCommonActivity.this,JobDetailActivity.class);
				intent.putExtra("url", ConstantUtil.ip+"android/sending/querySendingDetail?sending_id="+id);
				intent.putExtra("title", "详细信息");
				intent.putExtra("type", type);
				intent.putExtra("sending_id", id);
				startActivityForResult(intent,2);
				
				//startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
					commonUtil.shortToast("信息加载失败!");
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
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list, R.layout.job_common_list_item, 
					new String[]{ "companyName", "positionName", "address", "price", "createDate" }, 
					new int[]{ R.id.companyName, R.id.positionName, R.id.address, R.id.price, R.id.createDate }, listview);
			listview.setAdapter(adapter);
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
				Thread.sleep(200);
				returnList = jobDAL.doNrialNotice(user.getUserId(),pageNum,type);
				recHandler.sendEmptyMessage(1);

			} catch (Exception e)
			{
				Log.v("数据加载线出错:", e.getMessage());
				e.printStackTrace();
				recHandler.sendEmptyMessage(-1);
			}
		}
	};
	Handler recHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 1)
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
		pageNum ++;
		
		String state = (String) returnList.get(0).get("state");

		if (returnList.get(0).get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS)))
		{
			if (returnList.size() < 10)
				listview.removeFooterView(commonUtil.loadingLayout);
			for (int i = 0; i < returnList.size(); i++)
			{
				    Map<String, Object> map = returnList.get(i);
                   
				    String price=map.get("acc034")==null?"面议":map.get("acc034").toString();
				    if (price.equals(""))
					{
						price = "面议";
					}
					map.put("companyName", map.get("aab004"));//公司
					map.put("positionName", "岗位：" + map.get("acb216"));//求职岗位
					map.put("price", "薪资：" + price);//薪资
					map.put("address", "地址:"+map.get("aae006"));//公司地址
					map.put("createDate", map.get("send_time"));//投递日期
					list.add(returnList.get(i));
				
			}
		} else
		{
			this.commonUtil.showListAddDataState(listview, state);
		}

		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.backBtn:
			JobCommonActivity.this.finish();
			break;
		case R.id.refreshBtn:
			pageNum = 0;
			doLoadData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount)
	{
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (lastItem == adapter.count
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			doLoadData();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==0)return;
		if(requestCode==2){
			pageNum = 0;
			doLoadData();
		}
	}

}
