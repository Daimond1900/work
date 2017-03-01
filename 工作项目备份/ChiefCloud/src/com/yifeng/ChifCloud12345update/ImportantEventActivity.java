package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.ImportantEventDAL;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 重要事件
 * @author Administrator
 */
public class ImportantEventActivity extends BaseActivity implements
		OnScrollListener {
	int clickItem = -1;// 点击item
	String itemString = "";// 上次点击item内容

	String intentId = "";
	Button back;
	ListView listview;
	HomeAdapter adapter;
	ImportantEventDAL dal;
	ListViewUtil util;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zytx);
		dal = new ImportantEventDAL(this);
		listview = (ListView) findViewById(R.id.listview);
		util = new ListViewUtil(this, listview);
		listview.setOnScrollListener(this);

		intentId = getIntent().getStringExtra("ID");

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});


		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);

		doSetListView();
	}

	/**
	 * 加载信息详细
	 */
	private Runnable gorupRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
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

	int pageNum = 0;
	ProgressBar progressBar;
	LinearLayout loadingLayout;

	void addData() {
		int setSelectionID = -1;
		adapter.count +=1;
		List<Map<String, String>> returnList = dal.doQueryListAll(pageNum + "",
				LoginBiz.loadUser(this).getOrg_id(), "0",
				LoginBiz.loadUser(this).getUserId());
		pageNum++;
		String state = returnList.get(0).get("state");
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
//			Resources res = getResources();

			if (returnList.size() < 10)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				String img_url = (String) tmap.get("IMG_ADD");
				String user_url = (String) tmap.get("user_head");
				String tString = (String) tmap.get("CONTENT");
				tmap.put("title", tmap.get("TITLE"));

				tmap.put("ADDTIME",
						DateUtil.getDate((String) tmap.get("REMIND_DATE")));

				tmap.put("info", tString);
				if (tString.length() < 30) {
					tmap.put("CONTENT", tString);
				} else
					tmap.put("CONTENT", tString.substring(0, 30) + "...");
				list.add(tmap);
				if (intentId != null) {

					if (tmap.get("ID").equals(intentId))
						setSelectionID = i;
				}

			}

		} else
			util.showListAddDataState(state);
		adapter.count = list.size();
		if (setSelectionID == -1)
			adapter.notifyDataSetChanged();
		else {
			UpdateList(setSelectionID);
			listview.setSelection(setSelectionID);

		}
	}

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
			adapter = new HomeAdapter(this, list, R.layout.zytx_item,
					new String[] { "title", "CONTENT", "ADDTIME" }, new int[] {
							R.id.title, R.id.content, R.id.time },
					getResources());
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					UpdateList(arg2);

				}
			});
		}
		new Thread(gorupRunnable).start();

	}

	int lastItem = 0;

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

	public void UpdateList(int selectedItem) {
		if (clickItem == selectedItem) {

			Map<String, Object> map = list.get(clickItem);
			map.put("CONTENT", itemString);
			clickItem = -1;

		} else if (selectedItem != list.size()) {
			if (clickItem != -1) {
				Map<String, Object> map = list.get(clickItem);
				map.put("CONTENT", itemString);
			}
			Map<String, Object> map = list.get(selectedItem);
			clickItem = selectedItem;
			itemString = (String) map.get("CONTENT");
			map.put("CONTENT", map.get("info"));
			dal.doIsRead((String) list.get(selectedItem).get("ID"), LoginBiz
					.loadUser(this).getUserId());
		}
		adapter.notifyDataSetChanged();
	}

}
