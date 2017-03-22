package com.winksoft.nox.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.winksoft.android.yzjycy.R;
import com.winksoft.nox.android.ui.util.ListViewUtil;
import com.winksoft.nox.android.view.YFListView;
import com.winksoft.nox.android.view.YFListView.OnRefreshListener;
import com.yifeng.nox.android.ui.BaseActivity;

/**
 * 列表基础类
 * 
 * @author user_ygl 初始化所有父类的抽象方法 可用的父类变量 SUPERPAGENUM 页数 从0开始 SURPERDATA
 *         listview dataset formatDate 需要将 STRINGLIST 转化为 SURPERDATA
 */
public abstract class BaseListActivity extends BaseActivity {
	protected YFListView listview;
	int lastItem = 0;
	protected String EMTYTEXT = "";
	ListViewUtil footbar;
	protected int SUPERPAGENUM = 0;
	private int pageSize = 1;
	protected List<Map<String, Object>> SURPERDATA = new ArrayList<Map<String, Object>>();
	protected List<Map<String, String>> STRINGLIST;
	protected boolean loading = false;
	private BaseAdapter super_adapter;
	public boolean isloading = true;
	public Button bt_bottom_menu1, bt_bottom_menu2, bt_bottom_menu3,
			bt_bottom_menu4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 初始化
	 * 
	 * @param load
	 *            是否显示底部进度条
	 * @param showHead
	 *            是否下拉刷新
	 */
	protected void intiListview(boolean load, boolean showHead) {
		this.listview = (YFListView) findViewById(R.id.listview);
		listview.load = load;
		listview.showHead = showHead;
		listview.init(this);

		if (footbar == null) {
			footbar = new ListViewUtil(this, listview, getResources());
		}
		
		if (isloading) {
			loadDate();
		}
		
		if(!showHead){
			return;
		}
		listview.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if (loading)
					return;
				loading = true;
				SUPERPAGENUM = 0;
				loadDate();
				// new AsyncTask<Void, Void, Void>() {
				// @Override
				// protected Void doInBackground(Void... params) {
				// SUPERPAGENUM = 0;
				// STRINGLIST = setDataMethod();
				// return null;
				// }
				//
				// @Override
				// protected void onPostExecute(Void result) {
				// addData();
				// listview.onRefreshComplete();
				// }
				//
				// @Override
				// protected void onPreExecute() {
				// if (listview.load) {
				// footbar.removeFootBar();
				// footbar.addFootBar();
				// }
				//
				// }
				// }.execute();
			}

			@Override
			public void addDate() {
				if (loading)
					return;
				loading = true;
				// doSetListView();
				loadDate();

			}

		});
	}

	/**
	 * 为listview 设置adapter
	 */
	protected abstract BaseAdapter setAdapter();

	/**
	 * 格式化返回数据 将
	 */
	protected void formatData() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {
				otm.put(ts,
						tm.get(ts).replaceAll("\r\t", "")
								.replaceAll("\r\n", "").trim());
			}
			SURPERDATA.add(otm);
		}
	};

	/**
	 * listview 刷新
	 */
	protected void myNotifyDataSetChanged() {
	};

	protected List<Map<String, String>> setDataMethod() {
		return STRINGLIST;
	};

	protected abstract void loadDate();

	public void onListviewStart() {
		if (SUPERPAGENUM == 0) {
			SURPERDATA.clear();
			if (listview.load) {
				footbar.removeFootBar();
				footbar.addFootBar();
			}
			super_adapter = setAdapter();
			listview.setAdapter(super_adapter);
		}

	}

	public void onListviewonFailure() {
		SUPERPAGENUM = 0;
		footbar.removeFootBar();
		listview.onRefreshComplete();
	}

	public void onListviewNoResult() {
		footbar.removeFootBar();
	}

	public void onListviewSuccess() {
		loading = false;
		if (SUPERPAGENUM == 0)
			SURPERDATA.clear();
		if (STRINGLIST == null) {
			footbar.ALERTTEXT = EMTYTEXT;
			footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
			EMTYTEXT = "";
			myNotifyDataSetChanged();
			super_adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
			return;
		}
		if (STRINGLIST.size() == 0) {
			footbar.ALERTTEXT = EMTYTEXT;
			footbar.showListAddDataState(ListViewUtil.IS_EMPTY + "");
			EMTYTEXT = "";
			myNotifyDataSetChanged();
			super_adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
			return;
		}

		SUPERPAGENUM++;
		// String state = STRINGLIST.get(0).get("state");
		// String ifSuccess = STRINGLIST.get(0).get("success");

		// if (ifSuccess != null && ifSuccess.equals("true")) {
		if (STRINGLIST.size() < 10 && listview.load)
			footbar.removeFootBar();
		formatData();
		EMTYTEXT = "";
		// } else if (ifSuccess != null&& ifSuccess.equals("false")) {
		// footbar.ALERTTEXT = STRINGLIST.get(0).get("msg");
		// footbar.showListAddDataState(ListViewUtil.ERROR+"");
		// EMTYTEXT = "";
		// }

		myNotifyDataSetChanged();
		super_adapter.notifyDataSetChanged();
		listview.onRefreshComplete();
	}
}
