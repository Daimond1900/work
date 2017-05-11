package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.json.DataConvert;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PxDetailsActivity extends BaseActivity implements OnClickListener {

	private Button back_btn, bm, ybm;
	private TextView bjmc, pxjg, pxlx, pxgz, pxdj, pxks, kbrq, jsrq, dz;
	pxDAL dal;
	private Map<String, String> verifyInfo, bmIsTrueFalse;
	private ProgressDialog progressDialog;
	private String class_id, is_enroll = "";
	private int postid;
	private int verifyFlag, mflag = 0;
	private String longitude = "";// 经度
	private String latitude = "";// 纬度
	private String doDetailsResult = "";
	private ListView kblistview;
	private boolean mIsWs = false;
	// private String isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pxxq_activity);
		initView();
		dal = new pxDAL(this, new Handler());
		class_id = this.getIntent().getStringExtra("class_id");
		// 加载数据
//		isEnroll(is_enroll);
//		postid = 1;
//		showProg("正在加载数据....");
//		new Thread(loadRunnanle).start();
	}

	private void isEnroll(String is_enroll2) {
		if ("1".equals(is_enroll2)) { // 已经报过名了
			bm.setVisibility(View.GONE);
			ybm.setVisibility(View.VISIBLE);
		} else {
			bm.setVisibility(View.VISIBLE);
			ybm.setVisibility(View.GONE);
		}

	}

	private void showProg(String Msg) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// progressDialog.setTitle("请稍等...");
		progressDialog.setMessage(Msg);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		progressDialog.show();
	}

	// 初始化
	private void initView() {
		// isLogin = user.getUserId();
		verifyInfo = new HashMap<String, String>(); // 验证信息的返回结果
		bmIsTrueFalse = new HashMap<String, String>();// 报名结果
		back_btn = (Button) findViewById(R.id.back_btn); // 返回键
		back_btn.setOnClickListener(this);
		bm = (Button) findViewById(R.id.bm); // 报名
		bm.setOnClickListener(this);
		ybm = (Button) findViewById(R.id.ybm); // 已报名
		ybm.setOnClickListener(this);
		bjmc = (TextView) findViewById(R.id.bjmc);
		pxjg = (TextView) findViewById(R.id.pxjg);
		pxlx = (TextView) findViewById(R.id.pxlx);
		pxgz = (TextView) findViewById(R.id.pxgz);
		pxdj = (TextView) findViewById(R.id.pxdj);
		pxks = (TextView) findViewById(R.id.pxks);
		kbrq = (TextView) findViewById(R.id.kbrq);
		jsrq = (TextView) findViewById(R.id.jsrq);
		kblistview = (ListView) findViewById(R.id.listview);
	}

	private void setPageValue(String json) {
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (map.get("success").equals("false")) {
				commonUtil.shortToast(map.get("msg"));
			} else if (map.get("success").equals("true")) {
				Map<String, String> mapClassInfo = DataConvert.toConvertStringMap(json, "classInfo");
				if (mapClassInfo != null) {
					pxks.setText(getMapValue(mapClassInfo, "training_course_hour"));// 课时
					jsrq.setText(getMapValue(mapClassInfo, "ending_time"));
					kbrq.setText(getMapValue(mapClassInfo, "opening_time"));// 开班时间
					is_enroll = getMapValue(mapClassInfo, "is_enroll"); // 是否投过简历
					latitude = getMapValue(mapClassInfo, "loc_lat"); // 纬度
					longitude = getMapValue(mapClassInfo, "loc_lng"); // 经度
				}
				// 课表
				List<Map<String, String>> listCourseInfo = DataConvert.toConvertStringList(json, "courseInfo");
				SimpleAdapter adapter = new SimpleAdapter(this, listCourseInfo, R.layout.listcourseinfo_item,
						new String[] { "class_name", "lesson_content", "lesson_teacher", "room_loc" },
						new int[] { R.id.bjmc, R.id.kcnr, R.id.skr, R.id.dz });
				kblistview.setAdapter(adapter);
			}
		}

		// 给组件赋值
		// 传过来的
		bjmc.setText(this.getIntent().getStringExtra("class_name")); // 课程名
		pxjg.setText(this.getIntent().getStringExtra("training_agent_name")); // 培训机构
		pxlx.setText(this.getIntent().getStringExtra("training_type")); // 课程名
		pxgz.setText(this.getIntent().getStringExtra("training_job_type")); // 培训工种
		pxdj.setText(this.getIntent().getStringExtra("training_level")); // 培训等级

		isEnroll(is_enroll);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn: // 返回
			this.finish();
			break;
		case R.id.bm: // 报名
			if (!Constants.iflogin) {
				Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
			} else {
				if (verifyFlag == 1 && mIsWs) {
					postid = 3;
					showProg("正在处理中....");
					new Thread(loadRunnanle).start();
				} else if (verifyFlag == 2) {
					commonUtil.shortToast("请完善个人信息!");
					// 跳转到 完整个人信息的界面
					intent = new Intent(this, BmInfoSureActivity.class);
//					startActivityForResult(intent, 0);
					startActivity(intent);
				}
			}
			//
			break;
		case R.id.ybm: // 已报名
			commonUtil.shortToast("您已经报过名了!");
			break;
		// case R.id.dz: // 地图
		// if (!longitude.equals("") && !latitude.equals("")) {
		// Intent map = new Intent(PxDetailsActivity.this,
		// ZptMapInfoActivity.class);
		// map.putExtra("companyName", "");
		// map.putExtra("companyAddress", dz.getText().toString());
		// map.putExtra("telNo", "");
		// map.putExtra("longitude", longitude);// 经度
		// map.putExtra("latitude", latitude);// 纬度
		// startActivity(map);
		// } else {
		// dialogUtil.shortToast("还没标注地理位置.");
		// }
		// break;
		default:
			break;
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (data == null) {
//			return;
//		}
//		mIsWs = data.getBooleanExtra(BmInfoSureActivity.EXTRA_Infor_TRUE, false);
//		verifyFlag = mIsWs ? 1 : 2;
//	}

	@Override
	protected void onResume() {
		super.onResume();
		postid = 1;
		showProg("正在加载数据....");
		new Thread(loadRunnanle).start();
	}

	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				if (postid == 1) { // 详情查询
					doDetailsResult = dal.doDetailsInfoQuery(class_id, !Constants.iflogin ? "":user.getUserId());
					verifyInfo = dal.doVerifyUserinfor(!Constants.iflogin ? "":user.getUserId());
					loadHandler.sendEmptyMessage(1);
				}
//				if (postid == 2) { // 验证用户信息
//					verifyInfo = dal.doVerifyUserinfor(!Constants.iflogin ? "":user.getUserId());
//					loadHandler.sendEmptyMessage(2);
//				}
				if (postid == 3) { // 报名
					bmIsTrueFalse = dal.doBm(!Constants.iflogin ? "":user.getUserId(), class_id);
					loadHandler.sendEmptyMessage(3);
				}

			} catch (Exception e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}

		}
	};
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) { // 详情
				setPageValue(doDetailsResult);
				if (verifyInfo != null && "true".equals(verifyInfo.get("success"))) {
					verifyFlag = 1; // 信息完整
					mIsWs = true;
				} else {
					verifyFlag = 2; // 信息不完整
				}
			}
			// if (msg.what == 2) { //
			// if (verifyInfo != null &&
			// "true".equals(verifyInfo.get("success"))) {
			// verifyFlag = 1; // 信息完整
			// } else {
			// verifyFlag = 2; // 信息不完整
			// }
			// }
			if (msg.what == 3) {
				if (bmIsTrueFalse != null && "true".equals(bmIsTrueFalse.get("success"))) {
					commonUtil.shortToast("报名成功!");
					isEnroll("1");
				} else {
					commonUtil.shortToast("报名失败!");
				}
			}
			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

	private String getMapValue(Map<String, String> map, String key) {
		return map.get(key) == null ? "" : map.get(key);
	}
}
