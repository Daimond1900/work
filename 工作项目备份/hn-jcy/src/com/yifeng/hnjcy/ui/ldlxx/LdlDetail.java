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
import android.widget.EditText;
import android.widget.TextView;

import com.yifeng.hnjcy.data.ManpowerDetailDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;

/**
 * 基本信息
 * 
 * @author user_ygl
 * 
 */
public class LdlDetail extends BaseActivity {
	private Button back;
	private EditText sfz, xb, csrq, whcd, hkxz, mz, zzmm, jyzt;
	private TextView name, lxdh, jtzz;
	private ManpowerDetailDAL dal;
	private String code;
	private String finalString = "";
	private Button save;
	private boolean cansave = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldl_main_detail);
		dal = new ManpowerDetailDAL(this, new Handler());
		code = getIntent().getStringExtra("code");
		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		sfz = (EditText) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name); // 姓名
		xb = (EditText) findViewById(R.id.xb);
		csrq = (EditText) findViewById(R.id.csrq);
		lxdh = (TextView) findViewById(R.id.lxdh);
		whcd = (EditText) findViewById(R.id.whcd);
		hkxz = (EditText) findViewById(R.id.hkxz);
		mz = (EditText) findViewById(R.id.mz);
		zzmm = (EditText) findViewById(R.id.zzmm);
		jtzz = (TextView) findViewById(R.id.jtzz);
		jyzt = (EditText) findViewById(R.id.jyzt);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LdlDetail.this.finish();
			}
		});
		if (code != null && !code.equals(""))
			initView();
		initAllEdittext();
	}

	private void initView() {

		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {

				return dal.getDetailByCard(code);

			}

			@Override
			protected void onPostExecute(Map<String, String> result) {
				super.onPostExecute(result);
				progressDialog.dismiss();
				// System.out.println(result);
				if (result == null) {
					sfz.setText(code);

				} else {
					save.setText("修改");
					// csrq, lxdh, whcd, hkxz, mz, zzmm, jtzz
					sfz.setText(result.get("aac002"));
					// String sbString= result.get("aac002").substring(16, 17);

					name.setText(result.get("aac003"));
					xb.setText(ParseData.NANVN.get(result.get("aac004")));
					csrq.setText(result.get("aac006"));
					lxdh.setText(result.get("aae005"));
					whcd.setText(ParseData.WHCD.get(result.get("aac011")));
					hkxz.setText(ParseData.HKZT.get(result.get("aac009")));
					mz.setText(ParseData.MAPMZ.get(result.get("aac005")));
					zzmm.setText(ParseData.ZZMM.get(result.get("aac024")));
					jtzz.setText(result.get("aac026"));
					jyzt.setText(ParseData.JYZT.get(result.get("aac016")));
				}
				finalString = getAllString();
				setEdittext(sfz, xb, csrq, whcd, hkxz, mz, zzmm);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = LdlDetail.this.commonUtil
						.showProgressDialog("数据加载中...");

			}

			ProgressDialog progressDialog;
		}.execute();

	}

	private void initAllEdittext() {
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cansave)
					new AsyncTask<Void, Void, Boolean>() {

						@Override
						protected Boolean doInBackground(Void... params) {
							// sfz, name, xb, csrq, lxdh, whcd, hkxz, mz, zzmm,
							// jtzz,
							// jyzt;
							return dal.modifyLabourInfo(
									sfz.getText().toString(),
									name.getText().toString(),
									lxdh.getText().toString(),
									ParseData.getKeyByValue(ParseData.MAPMZ, mz
											.getText().toString()),
									ParseData.getKeyByValue(ParseData.ZZMM,
											zzmm.getText().toString()),
									ParseData.getKeyByValue(ParseData.HKZT,
											hkxz.getText().toString()),
									ParseData.getKeyByValue(ParseData.WHCD,
											whcd.getText().toString()),
									jtzz.getText().toString(),
									user.getUserid(), user.getOperatorname(),
									user.getOrg(), user.getZzs051(),
									user.getAaa020(), user.getAaa021())
									.isSuccess();

						}

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							save.setEnabled(true);
							progressDialog.dismiss();
							if (result) {
								// Toast.makeText(LdlDetail.this, "修改成功。",
								// Toast.LENGTH_SHORT).show();
								LdlDetail.this.finish();
							} else {
								// Toast.makeText(LdlDetail.this, "修改失败。",
								// Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							save.setEnabled(false);
							progressDialog = LdlDetail.this.commonUtil
									.showProgressDialog(save.getText()
											+ "基本信息中...");

						}

						ProgressDialog progressDialog;

					}.execute();
			}
		});
	}

	private String getAllString() {
		// sfz, name, xb, csrq, lxdh, whcd, hkxz, mz, zzmm, jtzz,jyzt;
		return sfz.getText().toString() + "," + name.getText().toString() + ","
				+ xb.getText().toString() + "," + csrq.getText().toString()
				+ "," + lxdh.getText().toString() + ","
				+ whcd.getText().toString() + "," + hkxz.getText().toString()
				+ "," + mz.getText().toString() + ","
				+ zzmm.getText().toString() + "," + jtzz.getText().toString()
				+ "," + jyzt.getText().toString();
	}

	private void setButtonClisk(final EditText b, final String[] whcdvalue,
			final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(LdlDetail.this)
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

	private void setEdittext(EditText... edittext) {
		for (EditText et : edittext) {
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
