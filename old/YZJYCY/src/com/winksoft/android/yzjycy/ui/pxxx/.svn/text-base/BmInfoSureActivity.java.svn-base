package com.winksoft.android.yzjycy.ui.pxxx;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.util.IDCard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BmInfoSureActivity extends BaseActivity implements OnClickListener {
//	public static final String EXTRA_Infor_TRUE = "grxxsfwz";
	private Button back_btn, btn_baom;
	private EditText et_zsxm, et_sfz;
	private RadioGroup xbRadioGroup;
	private int xb = 0; // 性别
	pxDAL dal;
	private Map<String, String> bmResult;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bminfo_activity);
		dal = new pxDAL(this, new Handler());
		initView();
	}

	private void initView() {
		bmResult = new HashMap<String, String>();
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);
		btn_baom = (Button) findViewById(R.id.btn_baom);
		btn_baom.setOnClickListener(this);
		xbRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		xbRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.man) {
					xb = 0;
				} else {
					xb = 1;
				}
			}
		});
		et_zsxm = (EditText) findViewById(R.id.et_zsxm);
		et_sfz = (EditText) findViewById(R.id.et_sfz);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.btn_baom:
			if ("".equals(et_zsxm.getText().toString().trim())) {
				commonUtil.shortToast("真实姓名不能为空！");
				return;
			}
			if ("".equals(et_sfz.getText().toString().trim())) {
				commonUtil.shortToast("身份证不能为空！");
				return;
			}else {
				try {
					if(!"".equals(IDCard.IDCardValidate(et_sfz.getText().toString().trim()))){
						commonUtil.shortToast("身份证输入有误！");
						return;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			new Thread(loadRunnanle).start();
			break;

		default:
			break;
		}
	}

	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				// 报名
				String zsxm = et_zsxm.getText().toString().trim();
				String sfz = et_sfz.getText().toString().trim();
				bmResult = dal.doCompletePersonalInfo(user.getUserId(),zsxm,sfz,xb);
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
				if (bmResult != null && "true".equals(bmResult.get("success"))) {
					commonUtil.shortToast("报名信息编辑成功!");
					BmInfoSureActivity.this.finish();
				} else {
					commonUtil.shortToast("报名信息编辑失败!");
				}
			}
			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};
	private void showProg(String Msg) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// progressDialog.setTitle("请稍等...");
		progressDialog.setMessage(Msg);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		progressDialog.show();
	}
}
