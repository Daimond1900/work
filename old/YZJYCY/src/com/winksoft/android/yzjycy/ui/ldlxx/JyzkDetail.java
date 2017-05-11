package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;
import com.winksoft.android.yzjycy.data.ManpowerDetailDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.ReJson;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 就业状况
 * 
 * @author user_ygl
 * 
 */
public class JyzkDetail extends BaseActivity {
	private TextView sfz, name, jbqc, jyzt, mqjycy, cyhy, jyxs, sffxcyry, jydqlx, jydq, jyfs, wgnx, jnwgsj, wgygz,
			sfqdldht, sfjgbm, swzyryjyyx;
	private String intentCode, intentName;
	private ManpowerDetailDAL dal;
	private Button save, back;
	private String finalString;
	private boolean cansave = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcy_ldl_jyzk_detail);
		intentCode = getIntent().getStringExtra("code");
		intentName = getIntent().getStringExtra("name");
		dal = new ManpowerDetailDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				JyzkDetail.this.finish();
			}
		});
		initView();
		BASEHANDLER = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (finalString != null && !finalString.equals(getAllString())) {
					new AlertDialog.Builder(JyzkDetail.this).setTitle("提示").setMessage("信息有变更，是否保存？")
							.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									System.gc(); // 提醒系统及时回收
									JyzkDetail.this.finish();
									ActivityStackControlUtil.remove(JyzkDetail.this);
								}
							}).setNegativeButton("保存", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							}).show();
				} else {
					System.gc(); // 提醒系统及时回收
					JyzkDetail.this.finish();
					ActivityStackControlUtil.remove(JyzkDetail.this);
				}
			}
		};
	}

	private void initView() {
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cansave)
					new AsyncTask<Void, Void, Boolean>() {

						@Override
						protected Boolean doInBackground(Void... params) {
							// sfz, name, jbqc, jyzt, mqjycy, cyhy, jyxs,
							// sffxcyry,
							// jydqlx, jydq, jyfs, wgnx, jnwgsj, wgygz,
							// sfqdldht, sfjgbm,
							// swzyryjyyx
							ReJson json = dal.modifyEmploymentStatus(sfz.getText().toString(),
									ParseData.getKeyByValue(ParseData.NCJBQK, jbqc.getText().toString()),
									ParseData.getKeyByValue(ParseData.JYZT1, jyzt.getText().toString()),
									ParseData.getKeyByValue(ParseData.MQJYCY, mqjycy.getText().toString()),
									ParseData.getKeyByValue(ParseData.CYHY, cyhy.getText().toString()),
									ParseData.getKeyByValue(ParseData.JYXS, jyxs.getText().toString()),
									ParseData.getKeyByValue(ParseData.TYSF, sffxcyry.getText().toString()),
									ParseData.getKeyByValue(ParseData.JYDQXS, jydqlx.getText().toString()),
									ParseData.getKeyByValue(ParseData.JYDQ, jydq.getText().toString()),
									ParseData.getKeyByValue(ParseData.JYFS, jyfs.getText().toString()),
									wgnx.getText().toString(), jnwgsj.getText().toString(), wgygz.getText().toString(),
									ParseData.getKeyByValue(ParseData.TYSF, sfqdldht.getText().toString()),
									ParseData.getKeyByValue(ParseData.SWZYRYJYYX, swzyryjyyx.getText().toString()),
									ParseData.getKeyByValue(ParseData.TYSF, sfjgbm.getText().toString()),

									JyzkDetail.this.user.getUserId(), "", // JyzkDetail.this.user.getOperatorname()
									JyzkDetail.this.user.getOrgid(), "");// JyzkDetail.this.user.getZzs051()
																			// /*需要修改*/
							return json.isSuccess();

						}

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							progressDialog.dismiss();
							save.setEnabled(true);
							if (result) {
								// Toast.makeText(JyzkDetail.this,
								// save.getText().toString()+"成功。",
								// Toast.LENGTH_SHORT).show();
								JyzkDetail.this.finish();
							} else {
								// Toast.makeText(JyzkDetail.this,
								// save.getText().toString()+"失败。",
								// Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							save.setEnabled(false);
							progressDialog = JyzkDetail.this.commonUtil
									.showProgressDialog(save.getText().toString() + "就业状态中...");

						}

						ProgressDialog progressDialog;
					}.execute();
			}
		});
		sfz = (TextView) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name);
		jbqc = (TextView) findViewById(R.id.jbqc);
		jyzt = (TextView) findViewById(R.id.jyzt);
		mqjycy = (TextView) findViewById(R.id.mqjycy);
		cyhy = (TextView) findViewById(R.id.cyhy);
		jyxs = (TextView) findViewById(R.id.jyxs);
		sffxcyry = (TextView) findViewById(R.id.sffxcyry);
		jydqlx = (TextView) findViewById(R.id.jydqlx);
		jydq = (TextView) findViewById(R.id.jydq);
		jyfs = (TextView) findViewById(R.id.jyfs);
		wgnx = (TextView) findViewById(R.id.wgnx);
		jnwgsj = (TextView) findViewById(R.id.jnwgsj);
		wgygz = (TextView) findViewById(R.id.wgygz);
		sfqdldht = (TextView) findViewById(R.id.sfqdldht);
		sfjgbm = (TextView) findViewById(R.id.sfjgbm);
		swzyryjyyx = (TextView) findViewById(R.id.swzyryjyyx);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		name.setFocusable(false);
		name.setBackgroundColor(0x00000000);
		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				return dal.queryEmploymentStatus(intentCode);

			}

			@Override
			protected void onPostExecute(Map<String, String> result) {
				super.onPostExecute(result);
				progressDialog.dismiss();
				// System.out.println(result);
				if (result == null) {
					sfz.setText(intentCode);
					name.setText(intentName);
					save.setText("添加");

				} else {
					save.setText("修改");
					sfz.setText(result.get("aac002"));
					name.setText(result.get("aac003"));
					// , , ,
					// , , , , , , , ,
					// ;
					jbqc.setText(ParseData.NCJBQK.get(result.get("acc100")));
					jyzt.setText(ParseData.JYZT1.get(result.get("acc926")));
					mqjycy.setText(ParseData.MQJYCY.get(result.get("aab054")));
					cyhy.setText(ParseData.CYHY.get(result.get("aab022")));
					jyxs.setText(ParseData.JYXS.get(result.get("acc90b")));
					sffxcyry.setText(ParseData.TYSF.get(result.get("acc90h")));
					jydqlx.setText(ParseData.JYDQXS.get(result.get("acc901")));
					jydq.setText(ParseData.JYDQ.get(result.get("acc902")));
					jyfs.setText(ParseData.JYFS.get(result.get("acc423")));
					wgnx.setText(result.get("acc903"));
					jnwgsj.setText(result.get("acb214"));
					wgygz.setText(result.get("acc034"));
					sfqdldht.setText(ParseData.TYSF.get(result.get("acc22b")));
					sfjgbm.setText(ParseData.TYSF.get(result.get("acc906")));

					swzyryjyyx.setText(ParseData.SWZYRYJYYX.get(result.get("acc905")));

				}

				finalString = getAllString();
				// setTextView(sfz, name, jbqc, jyzt, mqjycy, cyhy, jyxs,
				// sffxcyry, jydqlx, jydq, jyfs, wgnx, jnwgsj, wgygz,
				// sfqdldht, sfjgbm, swzyryjyyx);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = JyzkDetail.this.commonUtil.showProgressDialog("数据加载中...");

			}

			ProgressDialog progressDialog;

		}.execute();
		// initAllTextView();
	}

	private String getAllString() {
		return sfz.getText().toString() + "," + name.getText().toString() + "," + jbqc.getText().toString() + ","
				+ jyzt.getText().toString() + "," + mqjycy.getText().toString() + "," + cyhy.getText().toString() + ","
				+ jyxs.getText().toString() + "," + sffxcyry.getText().toString() + "," + jydqlx.getText().toString()
				+ "," + jydq.getText().toString() + "," + jyfs.getText().toString() + "," + jnwgsj.getText().toString()
				+ "," + wgygz.getText().toString() + "," + sfqdldht.getText().toString() + ","
				+ sfjgbm.getText().toString() + "," + swzyryjyyx.getText().toString();

	}

	// private void initAllTextView() {
	// setButtonClisk(jbqc, ParseData.getMapAllValue(ParseData.NCJBQK),
	// "选择基本情况");
	// setButtonClisk(jyzt, ParseData.getMapAllValue(ParseData.JYZT1),
	// "选择就业状态");
	// setButtonClisk(mqjycy, ParseData.getMapAllValue(ParseData.MQJYCY),
	// "选择就业产业");
	// setButtonClisk(cyhy, ParseData.getMapAllValue(ParseData.CYHY), "选择从业产业");
	// setButtonClisk(jyxs, ParseData.getMapAllValue(ParseData.JYXS), "选择就业形势");
	// setButtonClisk(jydqlx, ParseData.getMapAllValue(ParseData.JYDQXS),
	// "选择就业地区类型");
	// setButtonClisk(jydq, ParseData.getMapAllValue(ParseData.JYDQ), "选择就业地区");
	// setButtonClisk(jyfs, ParseData.getMapAllValue(ParseData.JYFS), "选择就业方式");
	// setButtonClisk(swzyryjyyx,
	// ParseData.getMapAllValue(ParseData.SWZYRYJYYX), "选择尚未转移人员就业意向");
	// setButtonClisk(sffxcyry, ParseData.getMapAllValue(ParseData.TYSF),
	// "是否返乡创业人员");
	// setButtonClisk(sfqdldht, ParseData.getMapAllValue(ParseData.TYSF),
	// "是否签订劳动合同");
	// setButtonClisk(sfjgbm, ParseData.getMapAllValue(ParseData.TYSF),
	// "是否建工部门成建制输出");
	//
	// }

	private void setButtonClisk(final TextView b, final String[] whcdvalue, final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// new AlertDialog.Builder(JyzkDetail.this)
				// .setTitle(title)
				// .setItems(whcdvalue,
				// new DialogInterface.OnClickListener() {
				//
				// public void onClick(DialogInterface dialog,
				// int which) {
				// b.setText(whcdvalue[which]);
				// }
				// })
				// .setNeutralButton("取消",
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// b.setText("");
				// dialog.cancel();
				// }
				// }).show();
			}
		});
	}

	private void setTextView(TextView... TextView) {
		for (TextView et : TextView) {
			et.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (!finalString.equals(getAllString()))
						save.setVisibility(View.VISIBLE);
					else
						save.setVisibility(View.GONE);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
		}
	}

}
