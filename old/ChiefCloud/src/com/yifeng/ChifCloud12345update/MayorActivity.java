package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.MayorDal;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 寄语市长
 * 
 * @author Administrator
 */
public class MayorActivity extends BaseActivity implements OnScrollListener {
	private MayorDal mayorDal;
	private ListView listview;
	private HomeAdapter adapter;
	private LinearLayout top;
	private ProgressBar progressBar;
	private LinearLayout loadingLayout;
	private int lastItem = 0;
	private int pageNum = 0;
	private Map<String, String> param;
	ListViewUtil util;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jysz);
		mayorDal = new MayorDal(this);
		param = new HashMap<String, String>();
		listview = (ListView) findViewById(R.id.listview);
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(MayorActivity.this,
						MayorInfoActivity.class);
				intent.putExtra("title_name", "寄语市长详细");
				intent.putExtra("url",
						"android/jysz/pagequery?infoid="
								+ list.get(position).get("INFOID") + "&key="
								+ user.getKey());
				startActivity(intent);
			}
		});

		// 加载信息
		doSetListView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu5, R.drawable.bottom_menu5_);
	}

	@Override
	public void onScroll(AbsListView v, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView v, int state) {

		if (lastItem == adapter.count
				&& state == OnScrollListener.SCROLL_STATE_IDLE) {
			doSetListView();
		}
	}

	/**
	 * 加载信息详细
	 */
	private Runnable gorupRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				gorupHandler.sendMessage(gorupHandler.obtainMessage());
			} catch (InterruptedException e) {
			}
		}
	};
	Handler gorupHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			addData();
		}
	};

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
//			adapter = new HomeAdapter(this, list, R.layout.jysz_item, new String[] {
//					"CONTENT", "NAME", "INFOTYPE", "DEPARTMENT", "replay",
//					"COMMITTIME" }, new int[] { R.id.content, R.id.fbr, R.id.xxlx,
//					R.id.bm, R.id.replay, R.id.time }, getResources());
			adapter = new HomeAdapter(this, list, R.layout.jysz_item, new String[] {
					"CONTENT", "NAME", "INFOTYPE",  
					"COMMITTIME","DEPARTMENT" }, new int[] { R.id.content, R.id.fbr, R.id.xxlx,
					   R.id.time,R.id.bm}, getResources());

			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();
	}

	void addData() {
		adapter.count+=1;
		if (pageNum == 0) {
			list.clear();
		}
		/** 参数 **/
		param.put("page", pageNum + "");
		Resources res = getResources();
		pageNum++;
		/*
		 *寄语市长的数据 
		 */
		List<Map<String, String>> returnList = mayorDal.doQury(param);
		
		String state = returnList.get(0).get("state");
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				tmap.put("CONTENT", ((String) tmap.get("CONTENT")).replaceAll(
						"\t|\r|\n", ""));
				tmap.put("ADDTIME",
						DateUtil.getDate((String) tmap.get("CREATE_DATE")));
				list.add(tmap);
			}
		} else {
			util.showListAddDataState(state);

		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

}