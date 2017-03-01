package com.yifeng.hnqzt.ui.policy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.data.PolicyDAL;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.StringHelper;

/**
 * comments:政策法规
 * @author WuJiaHong
 * Date: 2012-9-20
 */
public class PolicyActivity extends BaseActivity implements OnScrollListener
{
	private TextView titleTxt;
	private Button cityBtn,provinceBtn,chinaBtn,backBtn,refresh_btn;
	private ListView listview;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
	private CommonAdapter adapter;
	private int pageNum = 0, lastItem = 0,type=7;//type:7是市内，6是省内5是国内
	private PolicyDAL policyDAL;
	private String keyWord="";
	private boolean isLoading=true;//标志正在加载数据
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_list);
		
		policyDAL = new PolicyDAL(this);
		
		initView();
		
		doLoadData();
	}
	
	/**
	 * 初始化界面
	 */
	private void initView()
	{
			
		MyOnClick btnClick = new MyOnClick();
		
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(this.getIntent().getStringExtra("title"));
		
		cityBtn = (Button) findViewById(R.id.cityBtn);
		cityBtn.setOnClickListener(btnClick);
		cityBtn.setEnabled(false);//默认选中
		
		provinceBtn = (Button) findViewById(R.id.provinceBtn);
		provinceBtn.setOnClickListener(btnClick);
		
		chinaBtn = (Button) findViewById(R.id.chinaBtn);
		chinaBtn.setOnClickListener(btnClick);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(btnClick);
		refresh_btn = (Button) findViewById(R.id.refresh_btn);
		refresh_btn.setOnClickListener(btnClick);
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				try
				{
					Intent intent = new Intent(PolicyActivity.this,PolicyDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "政策法规详情");
					bundle.putString("p_title", list.get(arg2).get("BT").toString());
					bundle.putString("p_date", list.get(arg2).get("FBRQ").toString());
					bundle.putString("url", ConstantUtil.ip+"android/managementcloud/queryNewsDetail?id="+list.get(arg2).get("ID"));
					intent.putExtras(bundle); 
					startActivity(intent);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	private void setOnClickTag(View btn){
		if(btn.getId()==R.id.cityBtn){
			type=7;//市内
			cityBtn.setEnabled(false);
			cityBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_left_normal)));
			provinceBtn.setEnabled(true);
			provinceBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_mid_btns)));
			chinaBtn.setEnabled(true);
			chinaBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_right_btns)));
		}
		if(btn.getId()==R.id.provinceBtn){
			type=6;//省内
			cityBtn.setEnabled(true);
			cityBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_left_btns)));
			provinceBtn.setEnabled(false);
			provinceBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_mid_normal)));
			chinaBtn.setEnabled(true);
			chinaBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_right_btns)));
		}
		if(btn.getId()==R.id.chinaBtn){
			type=5;//国内
			cityBtn.setEnabled(true);
			cityBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_left_btns)));
			provinceBtn.setEnabled(true);
			provinceBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_mid_btns)));
			chinaBtn.setEnabled(false);
			chinaBtn.setBackgroundDrawable(getResources().getDrawable((R.drawable.tag_right_normal)));
		}
		
		pageNum = 0;
		doLoadData();
	}
	
	/**   */
	private void doLoadData()
	{
		if (pageNum == 0)
		{
			list.clear();
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list, R.layout.policy_list_item, 
					new String[]{ "p_title", "content", "p_date","image_url" }, 
					new int[] { R.id.policy_title, R.id.content, R.id.policy_date,R.id.img }, listview);
			adapter.isHtml=true;
			listview.setAdapter(adapter);
		}
		if(isLoading){
		 isLoading=false;
		 new Thread(policyRunnable).start();
		}
		
	}
	private Runnable policyRunnable = new Runnable()
	{
		
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(500);
				returnList = policyDAL.doQuery(pageNum,keyWord,type);
				Message msg = new Message();
				msg.what = 0;
				policyHandler.sendMessage(msg);
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	};
	
	Handler policyHandler = new Handler()
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
				
				String now=map.get("FBRQ")==null?"":map.get("FBRQ").toString();
				try {
					Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(now);
					now = new SimpleDateFormat("MM-dd HH:mm").format(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				map.put("p_title", map.get("BT"));
				map.put("content", StringHelper.removeHtml(map.get("NR")==null?"":map.get("NR").toString()));
				map.put("p_date", now);
				map.put("image_url", map.get("imageurl"));

				list.add(returnList.get(i));
			}
		} else
		{
			this.commonUtil.showListAddDataState(listview, state);
		}

		adapter.count = list.size();
		adapter.setViewBinder();
		adapter.notifyDataSetChanged();
		

	}
	
	private class MyOnClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.cityBtn://市内
				setOnClickTag(cityBtn);
				break;
			case R.id.provinceBtn://省内
				setOnClickTag(provinceBtn);		
				break;
			case R.id.chinaBtn://国内
				setOnClickTag(chinaBtn);		
				break;
			case R.id.backBtn:
				PolicyActivity.this.finish();
				break;
			case R.id.refresh_btn:
				pageNum = 0;
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
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (lastItem == adapter.count
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			doLoadData();
		}
		
	}
	
}
