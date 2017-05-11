package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.adapter.WeiAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;

public class WeiPaiActivity extends BaseActivity implements
OnScrollListener{
	ListView listview;
	WeiAdapter adapter;
	FormDAL dal;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private Map<String, String> param = new HashMap<String, String>();
	Button   back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weipai);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WeiPaiActivity.this.finish();
			}
		});
		dal = new FormDAL(this);
		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(WeiPaiActivity.this,
						WebViewForMyWorkActivty.class);
				intent.putExtra("title_name", "工单详情");
				intent.putExtra(
						"url",
						"android/form/doQueryFormDetail?key=" + user.getKey()
								+ "&form_id="
								+ list.get(position).get("FORM_ID"));
				startActivity(intent);
			}
		});
		
		doSetListView();
		this.initTabs();
		this.setTabsFocus(this.tabs_menu1, R.drawable.menubg);
		initCount();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}
	
	public void initCount(){
		Map<String, String> map= new HashMap<String, String>();
		map = dal.doGetCount(user.getUserId(), user.getOrg_id());
		if (map.get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			this.tabs_menu1.setText("未派("+map.get("dipached").toString()+")");
			this.tabs_menu2.setText("已派("+map.get("already").toString()+")");
			this.tabs_menu3.setText("已办结("+map.get("over").toString()+")");
		}
	}
	
	public void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			addFootBar();
			adapter = new WeiAdapter(this, list, R.layout.gdzp_item,
					new String[] { "CONTENT_TEXT", "ADDTIME", "TITLE" }, new int[] {
							R.id.content, R.id.time, R.id.title},
					getResources());
			adapter.setViewBinder();
			adapter.replayHandler = replayHandler;
			adapter.jobHandler = jobHandler;
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK){
			Toast.makeText(this, "交办成功", Toast.LENGTH_SHORT).show();
			pageNum = 0;
			lastItem =0;
			list.clear();
			adapter.notifyDataSetChanged();
			doSetListView();
			initCount();
		}
		else if (resultCode == 3)
			Toast.makeText(this, "网络异常，未成功交办。", Toast.LENGTH_SHORT).show();
	}
	
	Handler jobHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String form_id = msg.getData().getString("form_id");
			String transaction_id = msg.getData().getString("transaction_id");
			String dispatch_id = msg.getData().getString("dispatch_id");
			String flag = msg.getData().getString("flag");
			Intent serverIntent = new Intent(WeiPaiActivity.this,
					UserSelectActivity.class);
			serverIntent.putExtra("form_id", form_id);
			serverIntent.putExtra("transaction_id", transaction_id);
			serverIntent.putExtra("dispatch_id", dispatch_id);
			serverIntent.putExtra("flag", flag);
			startActivityForResult(serverIntent, 1);
		}
	};
	
	Handler replayHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			final String assign_id = msg.getData().getString("assign_id");
			final EditText inputServer = new EditText(
					WeiPaiActivity.this);
			inputServer.setText("已完成");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					WeiPaiActivity.this);
			builder.setTitle("回复领导批示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(inputServer);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							String reply_comment =commonUtil.doConvertEmpty(inputServer.getText().toString());
							new HashMap<String, String>();
							
							String state = dal.doReplyDispatch(reply_comment,
									assign_id);
							if (state.equals("SUCCESS")){
								WeiPaiActivity.this.commonUtil
										.shortToast("回复成功");
								pageNum = 0;
								lastItem =0;
								list.clear();
								adapter.notifyDataSetChanged();
								doSetListView();
								initCount();
							}
							else{
								WeiPaiActivity.this.commonUtil
										.shortToast("回复失败");
							}

						}
					});
			builder.show();

		}

	};// 回复handler
	
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
	
	void addData() {
		param.put("page", pageNum + "");
		param.put("org_id", user.getOrg_id());
		param.put("user_id", user.getUserId());
		List<Map<String, String>> returnList = dal.doQueryForms(param,"android/dispatch/doNotDispatchList");
		pageNum++;
		if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.server_error_msg));
		}

		else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.inner_error_msg));

		} else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_FAIL))) {
			this.commonUtil.shortToast("没有更多数据。");
			removeFootBar();
		}

		else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			// Resources res = getResources();
			if (returnList.size() < 10)
				removeFootBar();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				tmap.put("ADDTIME",
						DateUtil.getDate1((String) tmap.get("CREATE_DATE")));
				list.add(tmap);
			}

		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();

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
	
	
	int pageNum = 0;
	ProgressBar progressBar;
	LinearLayout loadingLayout;

	private void removeFootBar() {
		listview.removeFooterView(loadingLayout);
	}
	
	private void addFootBar() {

		if (loadingLayout == null) {
			LinearLayout searchLayout = new LinearLayout(this);
			searchLayout.setOrientation(LinearLayout.HORIZONTAL);
			progressBar = new ProgressBar(this);
			progressBar.setPadding(0, 0, 15, 0);
			searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			TextView textView = new TextView(this);
			textView.setText("加载中...");
			textView.setGravity(Gravity.CENTER_VERTICAL);
			searchLayout.addView(textView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));
			searchLayout.setGravity(Gravity.CENTER);
			loadingLayout = new LinearLayout(this);
			loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			loadingLayout.setGravity(Gravity.CENTER);
		}
		listview.addFooterView(loadingLayout);
	}
}
