package com.winksoft.android.yzjycy.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.widget.MyGridView;
import com.winksoft.android.yzjycy.MyListView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.RecuitDal;
import com.winksoft.android.yzjycy.db.MainManage;
import com.winksoft.android.yzjycy.fragment.HomeFragment.MyReceiver;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztPositionView;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztRecuitInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.banner.Banner;
import com.winksoft.banner.BannerConfig;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.winksoft.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import android.widget.ScrollView;

public class JobFragment extends Fragment {
	private CommonUtil commonUtil;
	private RecuitDal reciotDal;
	private String c_name = "", area_id = "", c_remark = "";
	private View layout;
	private Banner banner;
	private Object[] images = new Object[] { R.drawable.main_banner1, R.drawable.main_banner2 };
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> zpList;
	private MyGridView mainGv;
	SimpleAdapter adapter;
	private ListView job_zp_list;
	private List<Map<String, Object>> zpReturnlist = new ArrayList<Map<String, Object>>();
	private CommonAdapter commAdapter;
	private int pageNum = 0;
	private boolean isLoading = true;// 标志正在加载数据
	private PtrClassicFrameLayout mPtrFrame;
	private ScrollView mScrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (layout != null) {
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_job, container, false);
		
		initView();
		return layout;
	}

	private void initView() {
		zpList = new ArrayList<Map<String,Object>>();
		job_zp_list = (ListView) layout.findViewById(R.id.job_zp_list);
		job_zp_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String positionId = zpReturnlist.get(arg2).get("acb210").toString();// 职位编号
					Intent intent = new Intent(getActivity(), QztPositionView.class);
					intent.putExtra("title", "职位详情");
					intent.putExtra("positionId", positionId);
					intent.putExtra("companyId", zpReturnlist.get(arg2).get("aab001").toString());
					startActivity(intent);
				} catch (Exception e) {
					commonUtil.shortToast("未响应!");
				}
			}
		});
		
		reciotDal = new RecuitDal(getActivity());
		initBanner();
		initGrid();
//		doZpListData();		
	}

	private void initBanner() {
		banner = (Banner) layout.findViewById(R.id.jobbanner);
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		banner.setDelayTime(4000);
		banner.setImages(images);
	}

	private void initGrid() {
		loadGridData();
		mainGv = (MyGridView) layout.findViewById(R.id.jobMainGridView);
		adapter = new SimpleAdapter(getContext(), list, R.layout.mygridview_item1,
				new String[] { "itemImage", "itemText" }, new int[] { R.id.img, R.id.text_item });
		mainGv.setAdapter(adapter);
		mainGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
		});
	}

	private void loadGridData() {
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("itemText", "我的简历");
		map.put("itemImage", R.drawable.jy_wdjl);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", 2);
		map.put("itemText", "我的求职");
		map.put("itemImage", R.drawable.jy_wdqz);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", 3);
		map.put("itemText", "求职信息");
		map.put("itemImage", R.drawable.jy_qzxx);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", -1);
		map.put("itemText", "全部");
		map.put("itemImage", R.drawable.more);
		list.add(map);
	}
	
	private void doZpListData(){
		if(pageNum == 0){
			zpList.clear();
			commAdapter = new CommonAdapter(getActivity(), zpReturnlist, R.layout.qzt_recruitment_list_item,
					new String[] { "work", "money", "company", "date" },
					new int[] { R.id.workTxt, R.id.moneyTxt, R.id.companyTxt, R.id.dateTxt }, job_zp_list);
			job_zp_list.setAdapter(commAdapter);
		}
		if(isLoading){
			isLoading = false;
			new Thread(recRunnable).start();
		}
	}
	
	/**
	 * 加载信息
	 */
	private Runnable recRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(500);
				zpList = reciotDal.doPostQuery(pageNum + "", c_name, area_id, c_remark);
				recHandler.sendEmptyMessage(100);

			} catch (Exception e) {
				Log.v("数据加载线出错:", e.getMessage());
				e.printStackTrace();
				recHandler.sendEmptyMessage(-1);
			}
		}
	};
	Handler recHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 100) {
				addData();
			}
			isLoading = true;
		}
	};
	
	
	private void addData() {

		pageNum++;

		String state = (String) zpList.get(0).get("state");

		if (state.equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			if (zpList.size() < 10)
//				job_zp_list.removeFooterView(commonUtil.loadingLayout);
			for (int i = 0; i < zpList.size(); i++) {
				Map<String, Object> map = zpList.get(i);

				// 招聘总人数
				// int
				// peopleCount=Integer.parseInt(map.get("acb211").toString())+Integer.parseInt(map.get("acb212").toString())+Integer.parseInt(map.get("acb213").toString());
				map.put("work", map.get("acb216"));

				// 月工资
				String money = map.get("acc034") == null ? "" : map.get("acc034").toString();

				if (money.equals("")) {
					money = "面议";
				}
				map.put("money", "月薪:" + money);

				// 公司名称
				map.put("company", map.get("aab004") == null ? "" : map.get("aab004"));

				// 起始日期
				map.put("date", map.get("aae030") == null ? "" : map.get("aae030"));

				zpReturnlist.add(map);
			}
		} else {
			this.commonUtil.showListAddDataState(job_zp_list, state);
		}
//		commAdapter.count = zpReturnlist.size();
		commAdapter.notifyDataSetChanged();
	}
	
	private void doQzListData(){
		
	}
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}
}
