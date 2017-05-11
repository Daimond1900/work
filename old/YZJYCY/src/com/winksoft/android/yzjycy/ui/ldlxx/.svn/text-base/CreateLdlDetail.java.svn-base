package com.winksoft.android.yzjycy.ui.ldlxx;

import java.text.ParseException;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.ManpowerDetailDAL;
import com.winksoft.android.yzjycy.util.IDCard;

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
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
 
/**
 * 基本信息
 * 
 * @author user_ygl
 * 
 */
public class CreateLdlDetail extends BaseActivity {
	private Button back;
	private EditText sfz, name, xb, csrq, lxdh, whcd, hkxz, mz, zzmm, jtzz,
			jyzt;
	private ManpowerDetailDAL dal;
	private String finalString = "";
	private Button save;
private LinearLayout l_jyzt;
	private boolean cansave = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcy_ldl_main_detail);
		dal = new ManpowerDetailDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		sfz = (EditText) findViewById(R.id.sfz);
		if(getIntent().getStringExtra("sfz")!=null){
			sfz.setText(getIntent().getStringExtra("sfz"));
		}
		l_jyzt=(LinearLayout) findViewById(R.id.l_jyzt);
		l_jyzt.setVisibility(View.GONE);
		name = (EditText) findViewById(R.id.name);
		xb = (EditText) findViewById(R.id.xb);
		csrq = (EditText) findViewById(R.id.csrq);
		lxdh = (EditText) findViewById(R.id.lxdh);
		whcd = (EditText) findViewById(R.id.whcd);
		hkxz = (EditText) findViewById(R.id.hkxz);
		mz = (EditText) findViewById(R.id.mz);
		zzmm = (EditText) findViewById(R.id.zzmm);
		jtzz = (EditText) findViewById(R.id.jtzz);
		jyzt = (EditText) findViewById(R.id.jyzt);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateLdlDetail.this.finish();
			}
		});
		initAllEdittext();
	}

	private void initAllEdittext() {
		save.setVisibility(View.VISIBLE);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cansave)
					if(check())
					new AsyncTask<Void, Void, Boolean>() {
					ProgressDialog progressDialog;
						@Override
						protected Boolean doInBackground(Void... params) {
							// sfz, name, xb, csrq, lxdh, whcd, hkxz, mz, zzmm,
							// jtzz,
							// jyzt;
							return dal.inseartNewLdl(
									sfz.getText().toString(),
									name.getText().toString(),
									lxdh.getText().toString(),
									ParseData.getKeyByValue(ParseData.MAPMZ,mz.getText().toString()),
									ParseData.getKeyByValue(ParseData.ZZMM,zzmm.getText().toString()),
									ParseData.getKeyByValue(ParseData.HKZT,
											hkxz.getText().toString()),
									ParseData.getKeyByValue(ParseData.WHCD,
											whcd.getText().toString()),
									jtzz.getText().toString(),
									 user.getUserId(),
									user.getDepartment_name(),
									user.getOrgid(),
				/*需要修改*/
									"",	//user.getZzs051()
									"",	//	user.getAaa020()
									""//user.getAaa021()
									).isSuccess();

						}

						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
							progressDialog.dismiss();
							save.setEnabled(true);
							if (result) {
//								Toast.makeText(CreateLdlDetail.this, "新增成功。",
//										Toast.LENGTH_SHORT).show();
								CreateLdlDetail.this.finish();
							}
						}

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							save.setEnabled(false);
							progressDialog=CreateLdlDetail.this.commonUtil.	showProgressDialog("新增劳动力中...");
						}

					}.execute();
			}
		});
		// sfz, name, xb, csrq, lxdh, whcd, hkxz, mz, zzmm, jtzz,jyzt
		String[] whcdvalue = ParseData.getMapAllValue(ParseData.WHCD);
		setButtonClisk(whcd, whcdvalue, "选择文化程度");
		String[] hkxzvalue = ParseData.getMapAllValue(ParseData.HKZT);
		setButtonClisk(hkxz, hkxzvalue, "选择户口性质");
		String[] mzvalue = ParseData.getMapAllValue(ParseData.MAPMZ);
		setButtonClisk(mz, mzvalue, "选择民族");
		String[] zzmmvalue = ParseData.getMapAllValue(ParseData.ZZMM);
		setButtonClisk(zzmm, zzmmvalue, "选择政治面貌");
		sfz.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					try {
						String parseString = IDCard.IDCardValidate(sfz
								.getText().toString());
						if (parseString.equals("")) {
							cansave = true;
						} else {
							cansave = false;
							sfz.setError(parseString);
//							Toast.makeText(CreateLdlDetail.this, parseString,
//									Toast.LENGTH_SHORT).show();

						}
					} catch (ParseException e) {
						Toast.makeText(CreateLdlDetail.this, e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		sfz.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() == 18) {
					String sbString = s.toString().substring(16, 17);
					String sbString1 = s.toString().substring(6, 14);
					if (Integer.parseInt(sbString) % 2 == 0) {
						xb.setText("女");
					} else {
						xb.setText("男");
					}
					csrq.setText(sbString1.subSequence(0, 4) + "-"
							+ sbString1.substring(4, 6) + "-"
							+ sbString1.substring(6));
				} else {
					xb.setText("");
					csrq.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void setButtonClisk(final EditText b, final String[] whcdvalue,
			final String title) {
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CreateLdlDetail.this)
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
	private boolean check(){
		if(sfz.getText().toString().equals("")){
			this.commonUtil.shortToast("身份证不能为空");
			return false;
		} else
		if(name.getText().toString().equals("")){
			this.commonUtil.shortToast("姓名不能为空");
			return false;
		} else
			if(whcd.getText().toString().equals("")){
				this.commonUtil.shortToast("文化程度不能为空");
				return false;
			} 
			else
				if(hkxz.getText().toString().equals("")){
					this.commonUtil.shortToast("户口性质不能为空");
					return false;
				} 
		return true;
	}

}
