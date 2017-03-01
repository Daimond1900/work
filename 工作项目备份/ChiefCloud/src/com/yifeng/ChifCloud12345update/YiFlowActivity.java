package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yifeng.data.FormDAL;
import com.yifeng.util.ConstantUtil;

public class YiFlowActivity extends BaseActivity {
	ListView listview;
	Button back;
	SimpleAdapter adapter;
	private Thread loadThread;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private Map<String, String> param = new HashMap<String, String>();
	FormDAL dal;
	private String transaction_id,flag="",sin,url,form_id="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dal = new FormDAL(this);
		setContentView(R.layout.wdjb_info_list);
		listview = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				YiFlowActivity.this.finish();
			}
		});

		Intent intent = getIntent();
		transaction_id = intent.getStringExtra("transaction_id");
		sin = intent.getStringExtra("sin");
		if(intent.getStringExtra("flag")!=null){
			flag = intent.getStringExtra("flag");
		}
		if(intent.getStringExtra("form_id")!=null){
			form_id = intent.getStringExtra("form_id");
		}
		loadThread = new Thread(loadRunnable);
		loadThread.start();

		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

	private Runnable loadRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				loadHandler.sendMessage(loadHandler.obtainMessage());
			} catch (InterruptedException e) {
				// System.out.println("Error-"+e.getMessage());
			}
		}
	};

	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			loadData();
			//loadThread.stop();

		}
	};

	private void loadData() {
		param.put("transaction_id", transaction_id);
		param.put("flag", flag);
		param.put("user_id", user.getUserId());
		param.put("form_id", form_id);
		if(sin.equals("y")){
			url = "android/dispatch/doAlreadyDispatchTrace";
		}else if(sin.equals("w")){
			url = "android/dispatch/doNotDispatchTrace";
		}else if(sin.equals("banjie")){
			url = "android/dispatch/doOverDispatchTrace";
		}
		List<Map<String, String>> returnList = dal.doQueryForms(param,url);
		if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.server_error_msg));
		} else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.inner_error_msg));

		} else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_FAIL))) {
			this.commonUtil.shortToast("没有更多数据。");
		} else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				tmap.put(
						"CONTENT_TEXT",
						"["
								+ tmap.get("POST_NAME")
								+ "]于["
								+ tmap.get("DISPATCH_TIME")
								+ "]发送给["
								+ tmap.get("REPLY_NAME")
								+ "]:"
								+ (tmap.get("DISPATCH_COMMENT") == null ? ""
										: tmap.get("DISPATCH_COMMENT")));
				if (tmap.get("DISPATCH_STATUS").toString().equals("2")) {
					tmap.put(
							"CONTENT_TEXT1",
							"["
									+ tmap.get("REPLY_NAME")
									+ "]于["
									+ tmap.get("UPDATE_TIME")
									+ "]回复["
									+ tmap.get("POST_NAME")
									+ "]:"
									+ (tmap.get("REPLY_COMMENT") == null ? ""
											: tmap.get("REPLY_COMMENT")));
				} else {
					tmap.put("CONTENT_TEXT1", "");
				}
				list.add(tmap);
			}
		}
		adapter = new SimpleAdapter(this, list, R.layout.wdjb_info_list_item,
				new String[] { "CONTENT_TEXT", "CONTENT_TEXT1" }, new int[] {
						R.id.content, R.id.content1 });
		listview.setAdapter(adapter);
	}
}
