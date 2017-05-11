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
import com.yifeng.hngly.util.ReJson;

/**
 * 培训意愿
 * 
 * @author user_ygl
 * 
 */
public class PxyyDetail extends BaseActivity {
	TextView sfz, name, sfcjgpx, jntc, gjzyzg, pxyy, djsj;
	private ManpowerDetailDAL dal;
	private String intentCode, intentName;
	private String finalString = "";
	private Button save, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldl_pxyy_detail);
		intentCode = getIntent().getStringExtra("code");
		intentName = getIntent().getStringExtra("name");
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PxyyDetail.this.finish();
			}
		});
		dal = new ManpowerDetailDAL(this, new Handler());
		initView();
	}

	private void initView() {
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
			ReJson json=	dal.modifyTrainingAspiration(sfz.getText().toString(),
						ParseData.getKeyByValue(ParseData.TYSF, sfcjgpx
								.getText().toString()), jntc.getText()
								.toString(), ParseData.getKeyByValue(
								ParseData.GJZYZG, gjzyzg.getText().toString()),
						ParseData.getKeyByValue(ParseData.PXYY, pxyy.getText()
								.toString()), PxyyDetail.this.user.getUserid(),
						PxyyDetail.this.user.getOperatorname(),
						PxyyDetail.this.user.getOrg(), PxyyDetail.this.user
								.getZzs051());
			return json.isSuccess();
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						progressDialog.dismiss();
						save.setEnabled(true);
						if (result) {
//							Toast.makeText(PxyyDetail.this, "修改成功。",
//									Toast.LENGTH_SHORT).show();
							PxyyDetail.this.finish();
						}else {
//							Toast.makeText(PxyyDetail.this, "修改失败。",
//									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						save.setEnabled(false);
						progressDialog=PxyyDetail.this.commonUtil.	showProgressDialog(save.getText().toString()+"培训意愿中...");

					}
					ProgressDialog progressDialog;

				}.execute();
			}
		});
		sfz = (TextView) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name);
		sfcjgpx = (TextView) findViewById(R.id.sfcjgpx);
		jntc = (TextView) findViewById(R.id.jntc);
		gjzyzg = (TextView) findViewById(R.id.gjzyzg);
		pxyy = (TextView) findViewById(R.id.pxyy);
		djsj = (TextView) findViewById(R.id.djsj);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		name.setFocusable(false);
		name.setBackgroundColor(0x00000000);
		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				return dal.queryTrainingAspiration(intentCode);

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
					// jsyy,bzsm
					save.setText("修改");
					sfz.setText(result.get("aac002"));
					name.setText(result.get("aac003"));
					// sfcjgpx,jntc,gjzyzg,pxyy,djsj;
					sfcjgpx.setText(ParseData.TYSF.get(result.get("acc907")));
					jntc.setText(result.get("acc908"));
					gjzyzg.setText(ParseData.GJZYZG.get(result.get("acc904")));
					pxyy.setText(result.get("acc909"));
					djsj.setText(result.get("aae036"));
				}
				finalString = getAllString();
				//setTextView(sfz, name, sfcjgpx, jntc, gjzyzg, pxyy, djsj);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog=PxyyDetail.this.commonUtil.	showProgressDialog("数据加载中...");

			}
			ProgressDialog progressDialog;

		}.execute();
		initAllTextView();
	}

	private void initAllTextView() {

//		setButtonClisk(sfcjgpx, ParseData.getMapAllValue(ParseData.TYSF),
//				"是否参加过培训");
//		setButtonClisk(gjzyzg, ParseData.getMapAllValue(ParseData.GJZYZG),
//				"选择国家职业资格");
//		setButtonClisk(pxyy, ParseData.getMapAllValue(ParseData.PXYY),
//				"选择培训意愿");
//		 setButtonClisk(pxyy, whcdvalue, "选择培训意愿");

	}

	private String getAllString() {
		return sfz.getText().toString() + "," + name.getText().toString() + ","
				+ sfcjgpx.getText().toString() + ","
				+ jntc.getText().toString() + "," + gjzyzg.getText().toString()
				+ "," + pxyy.getText().toString() + ","
				+ djsj.getText().toString();
	}

	private void setButtonClisk(final TextView b, final String[] whcdvalue,
			final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(PxyyDetail.this)
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
