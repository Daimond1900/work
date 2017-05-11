package com.yifeng.hnqzt.ui.venture;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import android.widget.EditText;
import android.widget.ListView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.data.VentureDAL;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.ConstantUtil;

/**
 * comments:创业平台列表显示
 * @author WuJiaHong
 * Date: 2012-9-20
 */
public class VentureListActivity extends BaseActivity implements OnClickListener,
		OnScrollListener
{
	private Button backBtn, picBtn;
	private ListView listview;
	private VentureDAL ventureDal;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private CommonAdapter adapter;
	private int pageNum = 0, lastItem = 0;
	private boolean isLoading=true;//标志正在加载数据
	private Button search_btn;
	private EditText keyWord;
	private String  keyWords="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venture_list);

		ventureDal = new VentureDAL(this);

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		picBtn = (Button) findViewById(R.id.picBtn);
		picBtn.setOnClickListener(this);
		
		search_btn=(Button)findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		
		keyWord=(EditText)findViewById(R.id.keyWord);
	
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				try{
					Intent bl=new Intent(VentureListActivity.this,VentureDetailActivity.class);
					bl.putExtra("title", list.get(arg2).get("title")+"");
					bl.putExtra("fromSource", list.get(arg2).get("fromSource")+"");
					bl.putExtra("pubshTime", list.get(arg2).get("pubshTime")+"");
					bl.putExtra("content", list.get(arg2).get("content")+"");
					bl.putExtra("pic_url", list.get(arg2).get("pic_url")+"");
					startActivity(bl);
				}catch(Exception e){
					e.printStackTrace();
					commonUtil.shortToast("未响应!");
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
			adapter = new CommonAdapter(this, list, R.layout.venture_list_item, 
					new String[]{ "pic_url","title", "fromSource", "pubshTime"}, 
					new int[]{ R.id.img,R.id.title, R.id.fromSource, R.id.pushDate }, listview);

			listview.setAdapter(adapter);
			adapter.setViewBinder();
		}
		if(isLoading){
		 isLoading=false;	
		 new Thread(recRunnable).start();
		}
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
				returnList = ventureDal.doQuery(pageNum,keyWords);
				Message msg = new Message();
				msg.what = 0;
				recHandler.sendMessage(msg);

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
			isLoading=true;
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
				map.put("fromSource", "来源:"+map.get("author"));
				map.put("pubshTime", map.get("pubtime"));
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
			VentureListActivity.this.finish();
			break;
		case R.id.picBtn:
			Intent bl=new Intent(VentureListActivity.this,VentureGalleryActivity.class);
			bl.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(bl);
			break;
		case R.id.search_btn:
			keyWords=keyWord.getText().toString();
			pageNum=0;
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

}
