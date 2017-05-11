package com.yifeng.microalloy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.adapter.AsyncAdapter;
import com.yifeng.data.InteractiveDAL;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 网上互动
 * 
 * @author Administrator
 * 
 */
public class InteractiveActivty extends BaseActivity implements
		OnScrollListener {
	ListView listview;
	AsyncAdapter adapter;
	InteractiveDAL dal;
	Button edit, reflash;
	ListViewUtil footbar;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home1);
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		footbar = new ListViewUtil(this, listview);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(InteractiveActivty.this,
						DetailActivty.class);

				intent.putExtra("id", (String) list.get(position)
						.get("MAIN_ID"));
				startActivity(intent);
			}
		});
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				if(!((String) list.get(arg2).get("USER_ID")).equals(InteractiveActivty.this.user.getUserId())){
					InteractiveActivty.this.commonUtil.showMsg("提醒", "只能删除自己发布的帖子。");
					return false;
				}
				
				final Dialog builder = new Dialog(InteractiveActivty.this,
						R.style.dialog);
				builder.setContentView(R.layout.confirm_dialog);
				TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
				TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
				ptitle.setText("确定删除");
				pMsg.setText("是否确定删除？");
				Button confirm_btn = (Button) builder
						.findViewById(R.id.confirm_btn);
				Button cancel_btn = (Button) builder
						.findViewById(R.id.cancel_btn);
				confirm_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
						dal.deletePost((String) list.get(arg2).get("MAIN_ID"));
						list.remove(arg2);
						adapter.notifyDataSetChanged();
					}
				});

				cancel_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
					}
				});
				builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
				builder.show();

				return false;
			}
		});

		dal = new InteractiveDAL(this);
		edit = (Button) findViewById(R.id.edit);
		reflash = (Button) findViewById(R.id.reflash);
		reflash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pageNum = 0;
				footbar.removeFootBar();
				doSetListView();
				reflash.setEnabled(false);
				reflash.setBackgroundResource(R.drawable.title_reload_selected);
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InteractiveActivty.this,
						DeclearActivity.class);
				startActivity(intent);
			}
		});
		doSetListView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu3, R.drawable.bottom_menu3_);
	}

	int pageNum = 0;

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			footbar.addFootBar();
			adapter = new AsyncAdapter(this, list, R.layout.item, new String[] {
					"bm_logo", "SUBJECT", "CONTENT", "USERNAME", "READ_CONT",
					"ADDTIME", "pic" }, new int[] { R.id.bm_logo, R.id.title,
					R.id.content, R.id.bm_name, R.id.rcount, R.id.time,
					R.id.pic }, getResources(),listview);
			adapter.setViewBinder();
			adapter.need1 = true;
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();
	}

	void addData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageNum + "");
		pageNum++;
		List<Map<String, String>> returnList = dal.doQury(map);
		String state = returnList.get(0).get("state");

		if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			Resources res = getResources();
			if (returnList.size() < 10)
				footbar.removeFootBar();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				String user_url = (String) tmap.get("PIC_PATH");
				String picUrl = (String) tmap.get("IMG_ADD");
				if (picUrl == null) {
					tmap.put("pic",
							BitmapFactory.decodeResource(res, R.drawable.none));
				} else {
					tmap.put("pic",
							BitmapFactory.decodeResource(res, R.drawable.pic));
				}
				tmap.put("ADDTIME",
						DateUtil.getDate((String) tmap.get("ADDTIME")));

				if (user_url == null)
					tmap.put("bm_logo", BitmapFactory.decodeResource(res,
							R.drawable.user_head));
				else{
					tmap.put("bm_logo", getString(R.string.ipconfig)+user_url);
					 
				}
				list.add(tmap);
			}

		} else {
			footbar.showListAddDataState(state);
		}
		reflash.setEnabled(true);
		reflash.setBackgroundResource(R.drawable.reflash);
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
	}

	ProgressBar progressBar;
	LinearLayout loadingLayout;

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
}