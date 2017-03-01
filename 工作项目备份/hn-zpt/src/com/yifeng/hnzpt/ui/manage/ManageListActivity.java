package com.yifeng.hnzpt.ui.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.CommonAdapter;
import com.yifeng.hnzpt.data.ManageDAL;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.ListViewUtil;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ClassName:ManageListActivity 
 * Description：招聘管理-列表
 * @author Administrator 
 * Date：2012-10-23
 */
public class ManageListActivity extends BaseActivity implements OnScrollListener {
	private EditText searchEdt;
	private Button backBtn, refreshBtn, searchBtn;
	private Button ysh_tabBtn,wsh_tabBtn;
	private ListView listview;
	private ListViewUtil util;
	private CommonAdapter adapter;
	private ManageDAL manageDAL;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private boolean isLoading = true;// 标志正在加载数据
	private int flag=0;//0默认已审核, 1未审核

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_list);

		manageDAL = new ManageDAL(this);

		searchEdt = (EditText) findViewById(R.id.searchEdt);

		MyClick myClick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myClick);
		refreshBtn = (Button) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(myClick);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(myClick);
		ysh_tabBtn=(Button) findViewById(R.id.ysh_tabBtn);
		ysh_tabBtn.setOnClickListener(myClick);
		
		wsh_tabBtn=(Button) findViewById(R.id.wsh_tabBtn);
		wsh_tabBtn.setOnClickListener(myClick);
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Map<String, Object> map = list.get(arg2);
					Intent intent = new Intent();
					intent.setClass(ManageListActivity.this, PostDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("s_post", map.get("aca112").toString());// 岗位
					bundle.putString("s_gwsm", map.get("acb216").toString());	//岗位说明
					bundle.putString("s_zpnx", map.get("acb211").toString());	//招聘男性
					bundle.putString("s_zpvx", map.get("acb212").toString());	//招聘女性
					bundle.putString("s_jz", map.get("acb213").toString());//兼招
					bundle.putString("s_ygz", map.get("acc034").toString());	//月工资
					bundle.putString("s_wage", map.get("acc214")==null?"":map.get("acc214").toString());// 待遇
					bundle.putString("s_require", map.get("aae013")==null?"":map.get("aae013").toString());// 要求
					bundle.putString("s_start_date", map.get("aae030").toString());// 起始时间
					bundle.putString("s_end_date", map.get("aae031").toString());// 有效期
					bundle.putString("s_id", map.get("acb210").toString());// 招聘岗位的id
					bundle.putString("s_name", map.get("name").toString());// 用工区域
					if(flag==0){//已发布不可修改
						 bundle.putString("s_sh", "0");
					}else{//未发布
						 bundle.putString("s_sh", map.get("acb208").toString());// 是否审核
					}
					intent.putExtras(bundle);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// 数据加载
		doLoadData();
	}
	
	/**
	 * 按钮切换
	 * @param btn
	 */
	private void switchMenu(Button btn) {
		if(btn.getId() == R.id.ysh_tabBtn){
			ysh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_tab_button));
			wsh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_tab_button_));
			flag=0;//已审核
			refreshData();
		}else if(btn.getId() == R.id.wsh_tabBtn){
			ysh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_tab_button_));
			wsh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_tab_button));
			flag=1;//未审核
			refreshData();
		}
	}

	private void doLoadData() {
		if (pageNum == 0) {
			list.clear();
			util.removeFootBar();
			util.addFootBar();
			
			if(flag==1){//未审核显示图标
				adapter = new CommonAdapter(this, list, R.layout.manage_list_item, 
						new String[] { "itemJobs", "itemAudit", "itemWage", "itemDate" }, 
						new int[] { R.id.item_jobsTxt, R.id.item_auditImg, R.id.item_wageTxt, R.id.item_dateTxt }, getResources());
			}else{//已审核不显示图标
				adapter = new CommonAdapter(this, list, R.layout.manage_list_item, 
						new String[] { "itemJobs", "itemWage", "itemDate" }, 
						new int[] { R.id.item_jobsTxt, R.id.item_wageTxt, R.id.item_dateTxt }, getResources());
			}
			
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
				param.add(new BasicNameValuePair("acb216", commonUtil.doConvertEmpty(searchEdt.getText().toString())));
				param.add(new BasicNameValuePair("aab001", user.getUserId()));
				param.add(new BasicNameValuePair("acb208", flag+""));
				pageNum++;
				returnList = manageDAL.doPostQuery1(param, "android/corecruitment/listRecruitInfo");
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
		Resources res = getResources();
		String state = (String) returnList.get(0).get("state");
		if (returnList.get(0).get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);
				//map.put("itemJobs", map.get("zpgw") + "(" + map.get("rs") + ")");// 岗位
				map.put("itemJobs", map.get("acb216"));// 岗位
				
				String auditTag = map.get("acb208").toString();// 是否审核：0.未审核；1.已审核
				Bitmap auditStr = BitmapFactory.decodeResource(res, R.drawable.wsh);
				if ("1".equals(auditTag)) {
					auditStr = BitmapFactory.decodeResource(res, R.drawable.wsh);
				} else {
					auditStr = BitmapFactory.decodeResource(res, R.drawable.ysh);
				}
				map.put("itemAudit", auditStr);
				map.put("itemWage", "待遇：" + map.get("acc034")==null?"":map.get("acc034"));// 待遇
				map.put("itemDate", map.get("aae030")); // 发布时间
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
				ManageListActivity.this.finish();
				break;
			case R.id.refreshBtn:
				refreshData();
				break;
			case R.id.searchBtn:
				refreshData();
				break;
			case R.id.ysh_tabBtn:
				switchMenu(ysh_tabBtn);
				break;
			case R.id.wsh_tabBtn:
				switchMenu(wsh_tabBtn);
				break;
			default:
				break;
			}
		}

	}
	private void refreshData(){
		lastItem = 0;
		pageNum = 0;
		list.clear();
		util.removeFootBar();
		adapter.notifyDataSetChanged();
		doLoadData();
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
