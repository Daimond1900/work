package com.yifeng.hngly.ui.zerotransfer;

import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.hngly.data.ZeroTransferDAL;
import com.yifeng.hngly.ui.BaseActivity;
import com.yifeng.hngly.ui.R;
import com.yifeng.hngly.ui.ldlxx.JyzkDetail;
import com.yifeng.hngly.ui.ldlxx.ParseData;
import com.yifeng.hngly.ui.ldlxx.PxyyDetail;
import com.yifeng.hngly.util.ActivityStackControlUtil;
import com.yifeng.hngly.util.ReJson;

/**
 * 新增零转移
 */
public class ViewDetail extends BaseActivity {

	private EditText sfz, hzxm, ldls, lxdh, bz, jbrq, jbr, jbjg, zt;
	private Button back;
	private TextView top_title;
	private Button save;
	private String finalString;
	private ZeroTransferDAL dal;
	private String intentCode;
	private boolean ifshow = true;
	private TableLayout showtable;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lzy_main_detail);
		showtable = (TableLayout) findViewById(R.id.showtable);
		intentCode = getIntent().getStringExtra("intentCode");
		initView();
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("添加零转移家庭");
		dal = new ZeroTransferDAL(this, new Handler());

	}

	private void initView() {
		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, ReJson>() {

					@Override
					protected ReJson doInBackground(Void... params) {
						ReJson json = dal.addZeroTransferDetail(sfz.getText()
								.toString(), hzxm.getText().toString(), ldls
								.getText().toString(), lxdh.getText()
								.toString(), bz.getText().toString(),
								ViewDetail.this.user.getUserid(),
								ViewDetail.this.user.getOperatorname(),
								ViewDetail.this.user.getOrg(),
								ViewDetail.this.user.getZzs051());
						return json;
					}

					@Override
					protected void onPostExecute(ReJson result) {
						super.onPostExecute(result);
						save.setEnabled(true);
						progressDialog.dismiss();
						if (result.isSuccess()) {
//							Toast.makeText(ViewDetail.this, "添加成功。",
//									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(ViewDetail.this,TransferList.class));
							ViewDetail.this.finish();
							
						} else {
//							Toast.makeText(ViewDetail.this,
//									"添加失败：" + result.getMsg(),
//									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						save.setEnabled(false);
						progressDialog = ViewDetail.this.commonUtil
								.showProgressDialog("新增零转移信息中...");

					}

					ProgressDialog progressDialog;

				}.execute();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewDetail.this.finish();

			}
		});

		sfz = (EditText) findViewById(R.id.sfz);
		hzxm = (EditText) findViewById(R.id.hzxm);
		ldls = (EditText) findViewById(R.id.ldls);
		lxdh = (EditText) findViewById(R.id.lxdh);
		bz = (EditText) findViewById(R.id.bz);
		jbrq = (EditText) findViewById(R.id.jbrq);
		jbr = (EditText) findViewById(R.id.jbr);
		jbjg = (EditText) findViewById(R.id.jbjg);
		zt = (EditText) findViewById(R.id.zt);
		sfz.setText(intentCode);
		id = getIntent().getStringExtra("hzsfz");
		sfz.setText(getIntent().getStringExtra("hzsfz"));
		hzxm.setText(getIntent().getStringExtra("hzxm"));
		ldls.setText(getIntent().getStringExtra("ldls"));
		lxdh.setText(getIntent().getStringExtra("lxdh"));
		bz.setText(getIntent().getStringExtra("bz"));
		jbrq.setText(getIntent().getStringExtra("aae036"));
		jbr.setText(getIntent().getStringExtra("aae019"));
		jbjg.setText(getIntent().getStringExtra("aae020"));
		if (getIntent().getStringExtra("state1") != null) {
			this.commonUtil.shortToast("该家庭已被转移");
			ifshow = false;
			save.setText("修改");
			if (getIntent().getStringExtra("state1").equals("0")) {
				zt.setText("未审核");
			} else if (getIntent().getStringExtra("state1").equals("1")) {
				zt.setText("已审核");

			} else if (getIntent().getStringExtra("state1").equals("2")) {
				zt.setText("已注销");
			}
		} else {
			showtable.setVisibility(View.GONE);
			save.setText("添加");
		}

		finalString = getAllString();
		setEdittext(sfz, hzxm, ldls, lxdh, bz);
		if (ifshow) {

			BASEHANDLER = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					if (finalString != null
							&& !finalString.equals(getAllString())) {
						new AlertDialog.Builder(ViewDetail.this)
								.setTitle("提示")
								.setMessage("信息有变更，是否保存？")
								.setPositiveButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												System.gc(); // 提醒系统及时回收
												ViewDetail.this.finish();
												ActivityStackControlUtil
														.remove(ViewDetail.this);
											}
										})
								.setNegativeButton("保存",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();
					} else {
						System.gc(); // 提醒系统及时回收
						ViewDetail.this.finish();
						ActivityStackControlUtil.remove(ViewDetail.this);
					}
				}
			};
		}
		// }
		//
		// @Override
		// protected void onPreExecute() {
		// super.onPreExecute();
		// }
		//
		// }.execute();

	}

	private String getAllString() {
		return sfz.getText().toString() + "," + hzxm.getText().toString() + ","
				+ ldls.getText().toString() + "," + lxdh.getText().toString()
				+ "," + bz.getText().toString();
	}

	private void setEdittext(EditText... edittext) {
		for (EditText et : edittext) {
			if (ifshow)

				et.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (!finalString.equals(getAllString()))
							save.setVisibility(View.VISIBLE);
						else
							save.setVisibility(View.GONE);
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
			else {
				et.setEnabled(false);
			}
		}
	}

}
