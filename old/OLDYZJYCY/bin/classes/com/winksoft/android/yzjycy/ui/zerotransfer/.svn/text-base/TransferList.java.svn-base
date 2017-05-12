package com.winksoft.android.yzjycy.ui.zerotransfer;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.util.IDCard;
import com.winksoft.android.yzjycy.data.ZeroTransferDAL;
import com.winksoft.android.yzjycy.ui.ldlxx.CreateLdlDetail;
import com.winksoft.android.yzjycy.util.ReJson;
import com.winksoft.android.yzjycy.BaseListActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TransferList extends BaseListActivity {
	private MyBaseAdapter adapter;
	private ZeroTransferDAL dal;
	private Button back, save, searchbut;
	private EditText code;
	String[] items1 = { "零转移家庭信息", "删除零转移家庭", "注销零转移家庭" };
	String[] items2 = { "零转移家庭信息", "删除零转移家庭" };
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.jcy_zero_transfer_list);
		dal = new ZeroTransferDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		searchbut = (Button) findViewById(R.id.searchbut);
		code = (EditText) findViewById(R.id.code);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TransferList.this.finish();
			}
		});
		searchbut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchbut.setEnabled(false);
				SUPERPAGENUM = 0;
				doSetListView();
			}
		});
		save = (Button) findViewById(R.id.save);

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText inputServer = new EditText(TransferList.this);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						TransferList.this);
				builder.setTitle("添加零转移");
				builder.setMessage("输入要添加的零转移户主的身份证号码。");
				builder.setView(inputServer).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								try {
									Field field = dialog.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);
								} catch (Exception e) {
								}

								// inputServer.getText().toString();
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								try {
									String parseString = IDCard
											.IDCardValidate(inputServer
													.getText().toString());
									if (parseString.equals("")) {
										new AsyncTask<Void, Void, ReJson>() {

											@Override
											protected void onPostExecute(
													ReJson result) {
												progressDialog.dismiss();

												if (result.isSuccess()) {
													if (result.getMap() == null) {
														AlertDialog.Builder builder = new AlertDialog.Builder(
																TransferList.this);
														builder.setTitle("系统提示");
														builder.setMessage("该劳动力不存在,是否添加该劳动力?");
														builder.setNegativeButton(
																"取消", null)
																.setPositiveButton(
																		"确定",
																		new DialogInterface.OnClickListener() {

																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int which) {
																				Intent intent = new Intent(
																						TransferList.this,
																						CreateLdlDetail.class);
																				intent.putExtra(
																						"sfz",
																						inputServer
																								.getText()
																								.toString());
																				startActivity(intent);
																				TransferList.this
																						.finish();
																			}
																		})
																.show();
													} else {

														Intent intent = new Intent(
																TransferList.this,
																ViewDetail.class);

														// sfz.setText(result.get("hzsfz"));
														// hzxm.setText(result.get("hzxm"));
														// ldls.setText(result.get("ldls"));
														// lxdh.setText(result.get("lxdh"));
														// bz.setText(result.get("bz"));
														// jbrq.setText(result.get("aae036"));
														// jbr.setText(result.get("aae019"));
														// jbjg.setText(result.get("aae020"));
														// if
														// (result.get("state1")

														intent.putExtra(
																"id",
																result.getMap()
																		.get("id"));
														intent.putExtra(
																"hzsfz",
																result.getMap()
																		.get("hzsfz"));
														intent.putExtra(
																"hzxm",
																result.getMap()
																		.get("hzxm"));
														intent.putExtra(
																"ldls",
																result.getMap()
																		.get("ldls"));
														intent.putExtra(
																"lxdh",
																result.getMap()
																		.get("lxdh"));
														intent.putExtra(
																"bz",
																result.getMap()
																		.get("bz"));
														intent.putExtra(
																"aae036",
																result.getMap()
																		.get("aae036"));
														intent.putExtra(
																"aae019",
																result.getMap()
																		.get("aae019"));
														intent.putExtra(
																"aae020",
																result.getMap()
																		.get("aae020"));
														intent.putExtra(
																"state1",
																result.getMap()
																		.get("state1"));
														startActivity(intent);
														TransferList.this
																.finish();
													}
												}
											}

											@Override
											protected void onPreExecute() {
												super.onPreExecute();
												progressDialog = TransferList.this.commonUtil
														.showProgressDialog("查询该户主零转移信息中...");

											}

											ProgressDialog progressDialog;

											@Override
											protected ReJson doInBackground(
													Void... params) {
												return dal
														.add_queryZeroTransferDetail(
																inputServer
																		.getText()
																		.toString(),
																TransferList.this.user
																		.getOrgid());

											}

										}.execute();

									} else {
										inputServer.setError(parseString);
										try {
											Field field = dialog
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialog, false);
										} catch (Exception e) {
										}

									}
								} catch (ParseException e) {
									e.printStackTrace();
								}

							}
						});
				builder.show();

			}
		});

		intiListview();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0 || arg2 == (SURPERDATA.size() + 1))
					return;
				String state1 = (String) SURPERDATA.get(arg2 - 1).get("state1");
				if (state1 == null)
					return;
				// Intent intent =new
				// Intent(TransferList.this,TansferDetail.class );
				String intentCode = (String) SURPERDATA.get(arg2 - 1).get(
						"hzsfz");
				String id = (String) SURPERDATA.get(arg2 - 1).get("id");
				// if(tm.get("state1").equals("0")){
				// tm.put("state1", "状态:正常");
				// }else if(tm.get("state1").equals("1")){
				// tm.put("state1", "状态:已审核");
				// }else if(tm.get("state1").equals("2")){
				// tm.put("state1", "状态:已注销");
				// }

				if (intentCode != null) {
					// intent.putExtra("intentCode", (String)
					// SURPERDATA.get(arg2 - 1).get("hzsfz"));
					// startActivity(intent);
					if (state1.equals("状态:正常"))
						showDialog(intentCode, id, true, items1);
					else if (state1.equals("状态:已审核"))
						showDialog(intentCode, id, false, items1);
					else if (state1.equals("状态:已注销"))
						showDialog(intentCode, id, false, items2);
				}

			}
		});

	}

	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA,
				R.layout.jcy_recruitment_list_item, new String[] { "hzxm",
						"state1", "hzsfz", "aae036" }, new int[] {
						R.id.workTxt, R.id.moneyTxt, R.id.companyTxt,
						R.id.dateTxt }) {

			@Override
			protected void iniview(View view, int position,
					List<? extends Map<String, ?>> data) {

			}

		};
		;
		// adapter = new MyBaseAdapter(this, SURPERDATA,
		// R.layout.zero_transfer_item, new String[] { "hzxm", "hzsfz",
		// "aae036", "state1" }, new int[] {
		// R.id.contact_name, R.id.code, R.id.jbrq,
		// R.id.state }) {
		// @Override
		// protected void iniview(View view, int position,
		// List<? extends Map<String, ?>> data) {
		// }
		// };
		adapter.setViewBinder();
		listview.setAdapter(adapter);
	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();

			if (tm.get("state1") != null) {
				if (tm.get("state1").equals("0")) {
					tm.put("state1", "状态:正常");
				} else if (tm.get("state1").equals("1")) {
					tm.put("state1", "状态:已审核");
				} else if (tm.get("state1").equals("2")) {
					tm.put("state1", "状态:已注销");
				}
			}
			// tm.put("hzsfz", "户主身份证:"+tm.get("hzsfz") ) ;
			if (tm.get("aae036") != null && tm.get("aae036").length() > 9)
				tm.put("aae036", tm.get("aae036").substring(0, 10));

			for (String ts : tm.keySet()) {
				otm.put(ts, tm.get(ts));
			}

			SURPERDATA.add(otm);
		}
	}

	@Override
	protected void myNotifyDataSetChanged() {
		searchbut.setEnabled(true);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected List<Map<String, String>> setDataMethod() {
		// return dal.getListById(SUPERPAGENUM + "",
		// new UserSession(this).getUser().getAaa020(),
		// code.getText().toString()).getList();

		return dal.getListById(SUPERPAGENUM + "", this.user.getDepartment_id(),
				code.getText().toString()).getList();
	}

	void showDialog(final String code, final String id, final boolean ifmodify,
			String[] item) {

		Intent intent = new Intent(TransferList.this, TansferDetail.class);
		intent.putExtra("code", code);
		intent.putExtra("id", id);
		intent.putExtra("ifmodify", ifmodify);
		startActivity(intent);

	}

}
