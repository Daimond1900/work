package com.winksoft.android.yzjycy.ui.recruitmanage;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.BaseActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * ClassName:PostDetailActivity 
 * Description：招聘管理-岗位详情
 * @author Administrator 
 * Date：2012-10-24
 */
public class Zpt_PostDetailActivity extends BaseActivity {
	private TextView postTxt, fldyTxt, end_dateTxt, start_dateTxt, requireTxt,zpnxTxt, zpvxTxt, jzTxt,ygzTxt,gwsmTxt,txt_ygqy;

	private Button backBtn, editBtn;
	private ProgressDialog progressDialog;
	private Bundle qz;
	private String days = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_manage_post_detail);

		qz = this.getIntent().getExtras();
		days = qz.getString("s_end_date");
//		if ("1".equals(daysStr)) {
//			days = "半个月";
//		} else if ("0".equals(daysStr)) {
//			days = "一个月";
//		} else {
//			days = "两个月";
//		}
		initView();
		// 加载远程数据
		showProg("正在加载数据....");
		new Thread(loadRunnanle).start();
	}

	private void initView() {
		postTxt = (TextView) findViewById(R.id.postTxt); // 职位
		start_dateTxt = (TextView) findViewById(R.id.start_dateTxt); // 起始时间
		end_dateTxt = (TextView) findViewById(R.id.end_dateTxt); // 截止时间
		fldyTxt = (TextView) findViewById(R.id.fldyTxt); // 福利待遇
		requireTxt = (TextView) findViewById(R.id.requireTxt); // 具体要求
		txt_ygqy = (TextView) findViewById(R.id.txt_ygqy); // 用工区域
		backBtn = (Button) findViewById(R.id.backBtn);
		
		gwsmTxt = (TextView)findViewById(R.id.gwsm);
		zpnxTxt = (TextView)findViewById(R.id.zpnx);
		zpvxTxt = (TextView)findViewById(R.id.zpvx);
		jzTxt = (TextView)findViewById(R.id.jz);
		ygzTxt = (TextView)findViewById(R.id.ygz);
		
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Zpt_PostDetailActivity.this.finish();
			}
		});

		editBtn = (Button) findViewById(R.id.editBtn);
		// 是否审核：0.未审核；1.已审核。已审核的招聘岗位无法再进行编辑
		String shStr = qz.getString("s_sh");
		if ("0".equals(shStr)) {
			editBtn.setVisibility(View.GONE);
		}
		editBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Zpt_PostDetailActivity.this, Zpt_ManageEditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("s_post", qz.getString("s_post"));// 岗位
				bundle.putString("s_gwsm", qz.get("s_gwsm").toString());	//岗位说明
				bundle.putString("s_zpnx", qz.get("s_zpnx").toString());	//招聘男性
				bundle.putString("s_zpvx", qz.get("s_zpvx").toString());	//招聘女性
				bundle.putString("s_jz", qz.get("s_jz").toString());//兼招
				bundle.putString("s_ygz", qz.get("s_ygz").toString());	//月工资
				bundle.putString("s_wage", qz.getString("s_wage"));// 待遇
				bundle.putString("s_require", qz.getString("s_require"));// 要求
				bundle.putString("s_start_date", qz.getString("s_start_date"));// 起始时间
				bundle.putString("s_days", days);// 有效期
				bundle.putString("id", qz.getString("s_id"));// 有效期
				bundle.putString("s_name", qz.getString("s_name"));// 用工区域
				intent.putExtras(bundle);
				startActivity(intent);
				Zpt_PostDetailActivity.this.finish();
			}
		});
	}

	private void showProg(String Msg) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(Msg);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		progressDialog.show();
	}

	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				loadHandler.sendEmptyMessage(1);

			} catch (Exception e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}

		}
	};

	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				setData();
			}
			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

	protected void setData() {
		postTxt.setText(qz.getString("s_post"));
		start_dateTxt.setText(qz.getString("s_start_date"));
		end_dateTxt.setText(days);
		fldyTxt.setText(qz.getString("s_wage"));
		requireTxt.setText(qz.getString("s_require"));
		
		gwsmTxt.setText(qz.getString("s_gwsm"));
		zpnxTxt.setText(qz.getString("s_zpnx"));
		zpvxTxt.setText(qz.getString("s_zpvx"));
		jzTxt.setText(qz.getString("s_jz"));
		ygzTxt.setText(qz.getString("s_ygz"));
		txt_ygqy.setText(qz.getString("s_name"));
		
	}
}
