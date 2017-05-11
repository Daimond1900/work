package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yifeng.adapter.Assigndapter;
import com.yifeng.data.FormDAL;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 领导批示
 * 
 * @author Administrator
 * 
 *         10031109341234120160 咨询 11052520593959390002 建议 10082511353735370003
 *         投诉 11030818023423400050 求助 11101309094894800003 举报
 */
public class ReadyToDoListActivity extends BaseActivity implements
		OnScrollListener {
	ListView listview;
	Assigndapter adapter;
	Button back;
	String name, ORG_ID;
	FormDAL dal;
	ListViewUtil util;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbsx_list);
		listview = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ReadyToDoListActivity.this.finish();
			}
		});
		dal = new FormDAL(this);
		Intent intent = getIntent();
		ORG_ID = intent.getStringExtra("ORG_ID");
		listview.setOnScrollListener(this);
		util = new ListViewUtil(this, listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Message msg;
				String form_id = (String) list.get(position).get("FORM_ID");
				msg = new Message();
				Bundle data = new Bundle();
				data.putString("form_id", form_id);
				msg.setData(data);
				viewHanler.sendMessage(msg);

			}
		});

		doSetListView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
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
	public static String form_id = "";
	Handler jbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent serverIntent = new Intent(ReadyToDoListActivity.this,
					UserSelect1Activity.class);

			serverIntent.putExtra("org_id", ORG_ID);
			serverIntent.putExtra("form_id", form_id);
			startActivityForResult(serverIntent, 1);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK)
			Toast.makeText(this, "交办成功", Toast.LENGTH_SHORT).show();
		else if (resultCode != Activity.DEFAULT_KEYS_DIALER)
			Toast.makeText(this, "网络异常，未成功交办。", Toast.LENGTH_SHORT).show();
	}

	void addData() {
		List<Map<String, String>> returnList = dal
				.doQueryAssignedFormList(LoginBiz.loadUser(this).getUserId());
		String state = returnList.get(0).get("state");

		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				tmap.put("ADDTIME",
						DateUtil.getDate((String) tmap.get("ASSIGN_TIME")));
				tmap.put(
						"CONTENT_TEXT",
						"["
								+ tmap.get("USERNAME")
								+ "]:"
								+ (tmap.get("ASSIGN_COMMENT") == null ? ""
										: tmap.get("ASSIGN_COMMENT")));

				list.add(tmap);
			}
		} else {
			util.showListAddDataState(state);
		}
		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
			adapter = new Assigndapter(this, list, R.layout.ldps_item,
					new String[] { "TITLE", "CONTENT_TEXT", "ADDTIME" },
					new int[] { R.id.title, R.id.content, R.id.time },
					getResources());
			adapter.appleHandler = appleHandler;
			adapter.replayHandler = replayHandler;
			adapter.viewHanler = viewHanler;
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();
	}

	int pageNum = 0;
	ProgressBar progressBar;
	LinearLayout loadingLayout;

	Handler replayHandler = new Handler() {

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			final String assign_id = msg.getData().getString("assign_id");
			final EditText inputServer = new EditText(
					ReadyToDoListActivity.this);
			inputServer.setText("已完成");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ReadyToDoListActivity.this);
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

							String reply_comment = inputServer.getText()
									.toString();
							 
							String state = dal.doReplyAssign(reply_comment,
									assign_id);
							if (state.equals("SUCCESS"))
								ReadyToDoListActivity.this.commonUtil
										.shortToast("回复成功");
							else
								ReadyToDoListActivity.this.commonUtil
										.shortToast("回复失败");

						}
					});
			builder.show();

		}

	};// 回复handler
	Handler appleHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String assign_id = msg.getData().getString("assign_id");
			String state = dal.doReceiveAssign(assign_id);
			if (state.equals("SUCCESS"))
				ReadyToDoListActivity.this.commonUtil.shortToast("签收成功");
			else
				ReadyToDoListActivity.this.commonUtil.shortToast("签收失败");
		}

	}; // 签收handler
	Handler viewHanler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String form_id = (String) msg.getData().get("form_id");

			Intent intent = new Intent(ReadyToDoListActivity.this,
					WebViewForMyWorkActivty.class);
			intent.putExtra("title_name", "领导批示工单详情");
			intent.putExtra("url",
					"android/form/doQueryFormDetail?key=" + user.getKey()
							+ "&form_id=" + form_id);
			startActivity(intent);
		}

	};// 详情handler

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

}