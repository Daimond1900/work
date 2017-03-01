package com.yifeng.hnjcy.ui.recruit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.yifeng.cloud.entity.User;
import com.yifeng.hnjcy.adapter.SendResumeAdapter;
import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseListActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.DataConvert;
import com.yifeng.hnjcy.util.UserSession;

public class SendResumeActivity extends BaseListActivity {
	ResumeDAL dal;
	SendResumeAdapter adapter;
	String s_top_title;
	private Button back, send_btn;
	User user;
	ProgressDialog dialog;
	private String positionId, companyId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_resume);
		positionId = this.getIntent().getStringExtra("positionId") == null ? ""
				: this.getIntent().getStringExtra("positionId");
		companyId = this.getIntent().getStringExtra("companyId") == null ? ""
				: this.getIntent().getStringExtra("companyId");

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SendResumeActivity.this.finish();
			}
		});
		send_btn = (Button) findViewById(R.id.send_btn);
		send_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkParameters();
			}
		});
		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		UserSession session = new UserSession(this);
		user = session.getUser();
		dal = new ResumeDAL(this, new Handler());
	}

	@Override
	protected void setAdapter() {
		adapter = new SendResumeAdapter(this, SURPERDATA);
		listview.setAdapter(adapter);
	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {
				otm.put(ts,
						tm.get(ts).replaceAll("\r\t", "")
								.replaceAll("\r\n", ""));
			}
			SURPERDATA.add(otm);
		}

	}

	@Override
	protected void myNotifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	@Override
	protected List<Map<String, String>> setDataMethod() {
		return dal.doQueryResumeList(SUPERPAGENUM, user.getUserId(),"Y");
	}

	private String ids, names;
	private void checkParameters() {
		ids = "";
		names = "";
		for (Iterator<Integer> iter = SendResumeAdapter.state.keySet()
				.iterator(); iter.hasNext();) {
			int key = (Integer) iter.next();
			ids += SURPERDATA.get(key).get("id") + ",";
			names += SURPERDATA.get(key).get("xm") + ",";
		}
		if (!ids.equals("")) {
			ids = ids.substring(0, ids.length() - 1);
			names = names.substring(0, names.length() - 1);
		} else {
			commonUtil.shortToast("未选中简历，请先选择!");
			return;
		}
		dialog = commonUtil.showProgressDialog("简历投递中，请稍候...");
		new Thread(runnable).start();
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendResume();
		}
	};
	
	private void sendResume() {
		try {
			String json = dal.sendResume(ids, names, companyId, positionId);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Map<String, String> map = DataConvert.toMap(json);
			if (map.get("success") != null && map.get("success").equals("true")) {
				showToast(map.get("msg").toString());
				send_btn.setEnabled(false);
			}else{
				showToast("简历投递失败！");
			}
		} catch (Exception e) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			showToast("简历投递失败！");
		}
	}
	
	private void showToast(String msg){
		Looper.prepare();  
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		Looper.loop();
	}
}