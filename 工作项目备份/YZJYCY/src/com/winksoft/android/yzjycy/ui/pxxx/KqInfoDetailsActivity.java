package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.BaseListActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.yifeng.nox.android.json.DataConvert;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class KqInfoDetailsActivity extends BaseListActivity implements OnClickListener {
	private final String TAG = "KqInfoDetailsActivity";
	private Button back, bt_kq, bt_qt;
	pxDAL dal;
	private MyBaseAdapter adapter;
	private String class_id = "";
	private String detailStr = "";
	private ProgressDialog progressDialog;
	private int postId;
	private TextView bjmc, pxjg, pxgz, pxlx, pxdj;
	private Map<String,String> isKq;
	private String isKqStr = "0";
	private String isCheck = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmkqinfo);
		initView();
		
		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					// Intent intent = new Intent(KqInfoDetailsActivity.this,
					// PxDetailsActivity.class); //跳转至详情界面
					// String opening_time = SURPERDATA.get(arg2 -
					// 1).get("opening_time").toString();
					// intent.putExtra("opening_time", opening_time);
					// startActivity(intent);
				} catch (Exception e) {
					commonUtil.shortToast("未响应!");
				}
			}
		});
	}
	
	

	private void initView() {
		bjmc = (TextView) findViewById(R.id.bjmc);
		pxjg = (TextView) findViewById(R.id.pxjg);
		pxgz = (TextView) findViewById(R.id.pxgz);
		pxlx = (TextView) findViewById(R.id.pxlx);
		pxdj = (TextView) findViewById(R.id.pxdj);
		dal = new pxDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(this);
		bt_kq = (Button) findViewById(R.id.bt_kq);
		bt_kq.setOnClickListener(this);
		bt_qt = (Button) findViewById(R.id.bt_qt);
		bt_qt.setOnClickListener(this);
		class_id = this.getIntent().getStringExtra("class_id");
		isKq = new HashMap<String, String>();
	}


	Runnable loadRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				if (postId == 1) {
//					showProg("正在加载数据...");
					detailStr = dal.doKqInfoDetails(user.getUserId(), class_id);
					loadHandler.sendEmptyMessage(1);
				}
				if (postId == 2) {
					if("1".equals(isCheck)){	//签到时间
						isKq = dal.isKqInfo(user.getUserId(), class_id);
						loadHandler.sendEmptyMessage(2);
					}else if("2".equals(isCheck)){
						isKq = dal.isQtInfo(user.getUserId(), class_id);
						loadHandler.sendEmptyMessage(2);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}
		}
	};
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				formatDetailsData(detailStr);
			}
			if (msg.what == 2) {
				formatDetailsData(detailStr);
				if(isKq!=null && "true".equals(isKq.get("success"))){
					Intent intent = new Intent(KqInfoDetailsActivity.this, KqInfoSureActivity.class);
					intent.putExtra("class_id", class_id);
					intent.putExtra("ischeck", isCheck);
					startActivity(intent);
				}else {
					commonUtil.shortToast("考勤失败，稍后再试！");
				}
			}
			
			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};
	
	private void formatDetailsData(String json){
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (map.get("success").equals("false")) {
			} else if (map.get("success").equals("true")) {
				Map<String, String> baseInfo = DataConvert.toConvertStringMap(json, "baseInfo");
				if (baseInfo != null) {
					bjmc.setText(getMapValue(baseInfo, "class_name"));
					pxjg.setText(getMapValue(baseInfo, "training_agent_name"));
					pxlx.setText(getMapValue(baseInfo, "training_type"));
					pxgz.setText(getMapValue(baseInfo, "training_job_type"));
					pxdj.setText(getMapValue(baseInfo, "training_level"));
					isKqStr = getMapValue(baseInfo, "ischeck"); // 0:未考勤  1:考完勤
					if("1".equals(isKqStr)){
						bt_kq.setText("签退");
					}
				}else {
					commonUtil.shortToast("数据加载失败!");
				}
				// 课表
//				List<Map<String, String>> listCourseInfo = DataConvert.toConvertStringList(json, "courseInfo");
//				SimpleAdapter adapter = new SimpleAdapter(this, listCourseInfo, R.layout.listcourseinfo_item,
//						new String[] { "class_name", "lesson_content", "lesson_teacher", "room_loc" },
//						new int[] { R.id.bjmc, R.id.kcnr, R.id.skr, R.id.dz });
//				kblistview.setAdapter(adapter);
			}
		}
	}
//	private void showProg(String Msg) {
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		// progressDialog.setTitle("请稍等...");
//		progressDialog.setMessage(Msg);
//		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
//		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
//		progressDialog.show();
//	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			KqInfoDetailsActivity.this.finish();
			break;
		case R.id.bt_kq: // 考勤
			//判断是否可以考勤
			if("0".equals(isKqStr)){ // "0" --> kq
				isCheck = "1";
			}else if("1".equals(isKqStr)){	// "1" --> qt
				isCheck = "2";
			}
			postId = 2;
			new Thread(loadRunnable).start();
			break;
		case R.id.bt_qt: // 签退
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA, R.layout.kqxq_list_item,
				new String[] {"check_time"},
				new int[] {R.id.time}) {
			@SuppressLint("ResourceAsColor")
			@Override
			protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
				Map<String, ?> map = data.get(position);
				if("1".equals(map.get("check_type") == null ? "" : map.get("check_type"))){	//签到 
					return;
				}else if("2".equals(map.get("check_type") == null ? "" : map.get("check_type"))){ //签退
					TextView ztinfo = (TextView) view.findViewById(R.id.ztinfo);
					TextView time = (TextView) view.findViewById(R.id.time);
					ImageView img = (ImageView) view.findViewById(R.id.img);
					ztinfo.setText("签退");
					img.setImageResource(R.drawable.ic_dz_t);
					ztinfo.setTextColor(getResources().getColor(R.color.red));
					time.setTextColor(getResources().getColor(R.color.red));
					
					
				}
//				/** 图片加载，截至日期的格式化 */
//				ImageView rs_imag = (ImageView) view.findViewById(R.id.rs_imag);
//				TextView rs = (TextView) view.findViewById(R.id.rs);
//				Map<String, ?> map = data.get(position);
//				if ("0".equals(map.get("enrolled_cnt").toString().trim())) {
//					rs.setVisibility(View.GONE);
//					rs_imag.setVisibility(View.GONE);
//				} else {
//					rs.setVisibility(View.VISIBLE);
//					rs_imag.setVisibility(View.VISIBLE);
//				}
				// ((TextView)view.findViewById(R.id.jzrq)).setText(Html.fromHtml("<font>"
				// + "2019-2-22" + "</font>"));
			}
		};
		adapter.setViewBinder();
		listview.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		postId = 1;
		new Thread(loadRunnable).start();
	}
	
	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {
				otm.put(ts, tm.get(ts));
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
		return dal.doKqRecordQuery(user.getUserId(), class_id);
	}

	private String getMapValue(Map<String, String> map, String key) {
		return map.get(key) == null ? "" : map.get(key);
	}
}
