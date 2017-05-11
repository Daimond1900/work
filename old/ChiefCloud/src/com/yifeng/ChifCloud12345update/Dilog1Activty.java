package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.manager.LoginBiz;

public class Dilog1Activty extends BaseActivity{
	Button cancel, submit;
	ListView listview;
	HomeAdapter adapter;
	String org_id, form_id, target_user_id,transaction_id,dispatch_id,flag;
	EditText editText1;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private Map<String, String> map = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_gzjb);
		editText1 = (EditText) findViewById(R.id.editText1);
		cancel = (Button) findViewById(R.id.cancle);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (addAssign()) {
						Intent intent = new Intent();
						setResult(Activity.RESULT_OK, intent);
						finish();
					}

				} catch (Exception e) {
					Intent intent = new Intent();
					setResult(3, intent);
					finish();
				} finally {

				}

			}
		});

		listview = (ListView) findViewById(R.id.listview);

		adapter = new HomeAdapter(this, list, R.layout.dilog_item,
				new String[] { "content" }, new int[] { R.id.content },
				getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		transaction_id= getIntent().getStringExtra("transaction_id");
		dispatch_id= getIntent().getStringExtra("dispatch_id");
		form_id = getIntent().getStringExtra("form_id");
		flag  = getIntent().getStringExtra("flag");
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectedPosition = position;
				selectItem = position;
				Toast.makeText(Dilog1Activty.this,
						"选择了" + list.get(position).get("content"),
						Toast.LENGTH_SHORT).show();
				target_user_id = (String) list.get(position).get("userid");
				adapter.notifyDataSetChanged();
			}
		});
		dal = new FormDAL(this);
		new Thread(mRunnable).start();
	}

	int selectItem = -1;
	/**
	 * 加载数据线程
	 */
	private Runnable mRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				mHandler.sendMessage(mHandler.obtainMessage());
			} catch (InterruptedException e) {
			}
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			addData();
		}
	};

	boolean addAssign() {
		if (target_user_id != null) {
			map.put("form_id", form_id);
			map.put("user_id", user.getUserId());
			map.put("transaction_id", transaction_id);
			map.put("dispatch_id", dispatch_id);
			map.put("target_user_id", target_user_id);
			map.put("dispatch_comment", editText1.getText().toString());
			map.put("flag", flag);
			
			String state =  dal.doAddDispatch(map,"android/dispatch/doAddDispatch");
			if (state.equals("SUCCESS")){
				return true;
			}
			else{
				return false;
			}
		} else
			Toast.makeText(this, "先选择处理人。", Toast.LENGTH_SHORT).show();
		return false;
	}

	FormDAL dal;

	void addData() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("role_type", LoginBiz.loadUser(this).getRole_type());
		map1.put("departmentid", LoginBiz.loadUser(this).getOrg_id());
		List<Map<String, String>> list1 = dal.doQueryUserList(map1,"android/dispatch/doQueryTargetList");
		if(!list1.get(0).get("state").equals("0")){
		for (Map<String, String> map : list1) {

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("content", map.get("USERNAME"));
			m.put("userid", map.get("USERID"));
			list.add(m);
		}
		}
		adapter.notifyDataSetChanged();

	}

}
