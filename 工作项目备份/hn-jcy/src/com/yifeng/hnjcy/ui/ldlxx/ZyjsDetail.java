package com.yifeng.hnjcy.ui.ldlxx;
 
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
import com.yifeng.hnjcy.data.ManpowerDetailDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.ReJson;

/**
 * 资源减少
 * 
 * @author user_ygl
 * 
 */
public class ZyjsDetail extends BaseActivity {

	TextView sfz, name, jsyy, bzsm;
	private ManpowerDetailDAL dal;
	private String intentCode, intentName;
	private String finalString = "";
	private Button save, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldl_zyjs_detail);
		intentCode = getIntent().getStringExtra("code");
		intentName = getIntent().getStringExtra("name");
		dal = new ManpowerDetailDAL(this, new Handler());
		initView();
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ZyjsDetail.this.finish();
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
						ReJson json = dal.modifyResourceReduction(sfz.getText()
								.toString(), ParseData.getKeyByValue(
								ParseData.JSYY, jsyy.getText().toString()),
								bzsm.getText().toString(), ZyjsDetail.this.user
										.getUserid(), ZyjsDetail.this.user
										.getOperatorname(),
								ZyjsDetail.this.user.getOrg(),
								ZyjsDetail.this.user.getZzs051());

						return json.isSuccess();
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						save.setEnabled(true);
						progressDialog.dismiss();
						if (result) {
//							Toast.makeText(ZyjsDetail.this, "修改成功。",
//									Toast.LENGTH_SHORT).show();
							ZyjsDetail.this.finish();
						} else {
//							Toast.makeText(ZyjsDetail.this, "修改失败。",
//									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						save.setEnabled(false);
						progressDialog=ZyjsDetail.this.commonUtil.	showProgressDialog(save.getText()+"资源减少中...");

					}
					ProgressDialog progressDialog;

				}.execute();
			}
		});
		sfz = (TextView) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name);
		jsyy = (TextView) findViewById(R.id.jsyy);
		bzsm = (TextView) findViewById(R.id.bzsm);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		name.setFocusable(false);
		name.setBackgroundColor(0x00000000);
		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				return dal.queryResourceReduction(intentCode);

			}

			@Override
			protected void onPostExecute(Map<String, String> result) {
				super.onPostExecute(result);
				// System.out.println(result);
				progressDialog.dismiss();
				if (result == null) {
					sfz.setText(intentCode);
					name.setText(intentName);

					save.setText("添加");

				} else {
					// jsyy,bzsm
					save.setText("修改");
					sfz.setText(result.get("aac002"));
					name.setText(result.get("aac003"));
					jsyy.setText(ParseData.JSYY.get(result.get("acc912")));
					bzsm.setText(result.get("aae013"));
				}
				finalString = getAllString();
				//setTextView(sfz, name, jsyy, bzsm);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog=ZyjsDetail.this.commonUtil.	showProgressDialog("数据加载中...");

			}
			ProgressDialog progressDialog;

		}.execute();
		initAllTextView();
	}

	private void initAllTextView() {
		//setButtonClisk(jsyy, ParseData.getMapAllValue(ParseData.JSYY), "选择减少原因");
	}

	private String getAllString() {
		return sfz.getText().toString() + "," + name.getText().toString() + ","
				+ jsyy.getText().toString() + "," + bzsm.getText().toString();
	}

	private void setButtonClisk(final TextView b, final String[] whcdvalue,
			final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ZyjsDetail.this)
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
