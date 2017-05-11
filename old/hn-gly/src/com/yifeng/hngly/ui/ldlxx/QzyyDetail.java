package com.yifeng.hngly.ui.ldlxx;
 
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hngly.data.ManpowerDetailDAL;
import com.yifeng.hngly.ui.BaseActivity;
import com.yifeng.hngly.ui.R;
import com.yifeng.hngly.util.DateUtil;
import com.yifeng.hngly.util.ReJson;

/**
 * 求职意愿
 * 
 * @author user_ygl
 * 
 */
public class QzyyDetail extends BaseActivity {
	TextView sfz, name, qzgz, qzdq, yxgzjw, djsj;
	private String intentCode, intentName;
	private ManpowerDetailDAL dal;
	private String finalString;
	Button save, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldl_qzyy_detail);
		intentCode = getIntent().getStringExtra("code");
		intentName = getIntent().getStringExtra("name");
		dal = new ManpowerDetailDAL(this, new Handler());
		initView();
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QzyyDetail.this.finish();
			}
		});
	}

	private void initView() {
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
						ReJson json = dal.modifyJobAspiration(sfz.getText()
								.toString(), qzgz.getText().toString(),
						ParseData.getKeyByValue(ParseData.JYDQXS, qzdq
								.getText().toString()), yxgzjw.getText()
								.toString(), QzyyDetail.this.user.getUserid(),
								QzyyDetail.this.user.getOperatorname(),
								QzyyDetail.this.user.getOrg(),
								QzyyDetail.this.user.getZzs051());
						return json.isSuccess();
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						save.setEnabled(true);
						progressDialog.dismiss();
						if (result) {
//							Toast.makeText(QzyyDetail.this, "修改成功。",
//									Toast.LENGTH_SHORT).show();
							QzyyDetail.this.finish();
						} else {
//							Toast.makeText(QzyyDetail.this, "修改失败。",
//									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						save.setEnabled(false);
						progressDialog=QzyyDetail.this.commonUtil.	showProgressDialog(save.getText()+"求职意愿中...");

					}
					ProgressDialog progressDialog;
				}.execute();
			}
		});
		sfz = (TextView) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name);
		qzgz = (TextView) findViewById(R.id.qzgz);
		qzdq = (TextView) findViewById(R.id.qzdq);
		yxgzjw = (TextView) findViewById(R.id.yxgzjw);
		djsj = (TextView) findViewById(R.id.djsj);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		name.setFocusable(false);
		name.setBackgroundColor(0x00000000);
		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				return dal.queryJobAspiration(intentCode);

			}

			@Override
			protected void onPostExecute(Map<String, String> result) {
				super.onPostExecute(result);
				progressDialog.dismiss();
				// System.out.println(result);
				if (result == null) {
					sfz.setText(intentCode);
					name.setText(intentName);
					djsj.setText(DateUtil.getStrCurrentDate());
					save.setText("添加");

				} else {
					save.setText("修改");
					sfz.setText(result.get("aac002"));
					name.setText(result.get("aac003"));
					qzgz.setText(result.get("aca112"));
					qzdq.setText(ParseData.JYDQXS.get(result.get("acc901")));
					yxgzjw.setText(result.get("acc034"));
					djsj.setText(result.get("aae036"));
				}
				finalString = getAllString();
				//setTextView(sfz, name, qzgz, qzdq, yxgzjw, djsj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog=QzyyDetail.this.commonUtil.	showProgressDialog("数据加载中...");

			}
			ProgressDialog progressDialog;
		}.execute();
		initAllTextView();
	}

	private String getAllString() {
		return sfz.getText().toString() + "," + name.getText().toString() + ","
				+ qzgz.getText().toString() + "," + qzdq.getText().toString()
				+ "," + yxgzjw.getText().toString() + ","
				+ djsj.getText().toString();

	}

	private void initAllTextView() {
//		setButtonClisk(qzdq, ParseData.getMapAllValue(ParseData.JYDQXS),
//				"选择求职地区");

	}

	private void setButtonClisk(final TextView b, final String[] whcdvalue,
			final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(QzyyDetail.this)
						.setTitle(title)
						.setItems(whcdvalue,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										b.setText(whcdvalue[which]);
									}
								})
						.setNeutralButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										b.setText("");
										dialog.cancel();
									}
								}).show();
			}
		});
	}

	private void setTextView(TextView... TextView) {
		for (TextView et : TextView) {
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
		}
	}
}
