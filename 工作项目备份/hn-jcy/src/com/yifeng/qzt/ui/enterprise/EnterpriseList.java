package com.yifeng.qzt.ui.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.yifeng.hnjcy.adapter.CommonAdapter;
import com.yifeng.hnjcy.data.EnterpriseDal;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.DataConvert;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
/***
 * 招聘企业
 * @author wujiahong
 * 2012-10-18
 */
public class EnterpriseList extends BaseActivity implements OnScrollListener
{
	private ListView listview;
	private EnterpriseDal enterpriseDal;
	private int pageNum = 0,lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
	private CommonAdapter adapter;
	private Button back_btn, refresh_btn, qbqy_btn, djqy_btn, zmqy_btn,search_btn;
	private Spinner spn_area;
	private EditText edtTxt_name;
	private String c_name="",area_id ="";
	private boolean isLoading=true;//标志正在加载数据
	ArrayAdapter<String> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enterprise_list);
		
		enterpriseDal = new EnterpriseDal(this,new Handler());
		
		MyOnclick onclick = new MyOnclick();
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);

		refresh_btn = (Button) findViewById(R.id.refresh_btn);
		refresh_btn.setOnClickListener(onclick);

		qbqy_btn = (Button) findViewById(R.id.qbqy_btn);
		qbqy_btn.setOnClickListener(onclick);
		
		djqy_btn = (Button) findViewById(R.id.djqy_btn);
		djqy_btn.setOnClickListener(onclick);
		
		zmqy_btn = (Button) findViewById(R.id.zmqy_btn);
		zmqy_btn.setOnClickListener(onclick);
		
		search_btn=(Button)findViewById(R.id.search_btn);//搜索
		search_btn.setOnClickListener(onclick);
		
		edtTxt_name=(EditText)findViewById(R.id.edtTxt_name);
				
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				try{
					String companyId = list.get(arg2).get("aab001").toString();
					Intent intent = new Intent(EnterpriseList.this, EnterpriseView.class);
					intent.putExtra("companyId",companyId);
					intent.putExtra("title","公司详情");
					startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
					commonUtil.shortToast("未响应!");
				}
			}
		});

		initSpinner();
		// 数据加载
		doLoadData();
		
	}
	
	private List<Map<String, String>> maps; 
	private void initSpinner(){
		spn_area = (Spinner)findViewById(R.id.spn_area);
		maps = DataConvert.toAreaList(user.getGroup());
		if(maps.size()>0){
			String[] items = new String[maps.size()];
			for (int i = 0; i < maps.size(); i++) {
				items[i] = maps.get(i).get("name");
			}
			arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items);
			arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_area.setAdapter(arrayAdapter);
		}
		spn_area.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				area_id = maps.get(arg2).get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	private void doLoadData()
	{
		if (pageNum == 0)
		{
			list.clear();
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list,R.layout.enterprise_list_item, 
					new String[]{ "companyName", "address","tel","logo" }, 
					new int[]{ R.id.companyName,R.id.address,R.id.tel, R.id.companyLogo },listview);

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
				returnList = enterpriseDal.doPostQuery(pageNum+"",c_name,area_id);
				recHandler.sendEmptyMessage(0);
				
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
			if (msg.what == 0)
			{
				addData();
			}
			isLoading=true;
		}
	};
	
	/**
	 * 更新列表
ACB200	招聘编号
AAB004	单位名称
AAB022	行业类型  枚举
AAB019	单位类型  枚举
AAE006	单位地址
AAE004	单位联系人
AAE005	单位联系人电话
	 */
	private void addData()
	{
		
		pageNum++;
		
		String state = (String) returnList.get(0).get("state");
		
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS)))
			{
				if (returnList.size() < 10)
					listview.removeFooterView(commonUtil.loadingLayout);
				for (int i = 0; i < returnList.size(); i++)
				{

					Map<String,Object> map = returnList.get(i);
					map.put("companyName", map.get("aab004"));
					map.put("address", "地址:"+map.get("aae006"));
					map.put("tel", "电话:"+map.get("aae005"));
					String logo=map.get("logo_url")==null?"":map.get("logo_url").toString();
					map.put("logo", ConstantUtil.ip+ "images/" +logo.replace("\\", "/"));
				    //map.put("logo", R.drawable.logo1);
					
					list.add(map);
				}
			}else
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
			case R.id.back_btn:
				EnterpriseList.this.finish();
				break;
			case R.id.search_btn:
				c_name=edtTxt_name.getText().toString();
				pageNum = 0;
				doLoadData();
				break;
			case R.id.refresh_btn:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.djqy_btn:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.zmqy_btn:
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
	public void onScrollStateChanged(AbsListView view, int state)
	{
		if (lastItem == adapter.count
				&& state == OnScrollListener.SCROLL_STATE_IDLE)
		{
			doLoadData();
		}

	}
}
