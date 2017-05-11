package com.yifeng.hngly.ui.ldlxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.yifeng.hngly.adapter.MyBaseAdapter;
import com.yifeng.hngly.data.ManpowerDetailDAL;
import com.yifeng.hngly.ui.BaseListActivity;
import com.yifeng.hngly.ui.R;
import com.yifeng.hngly.util.DataConvert;

public class MainList extends BaseListActivity {
	private Button searchbut, back, save;
	private EditText code;
	private TextView edt_total;
	private MyBaseAdapter adapter;
	ManpowerDetailDAL dal;
	String[] items = { "基本信息", "就业状况登记", "培训意愿登记", "资源减少登记", "求职意愿登记" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldl_main_list);
		dal = new ManpowerDetailDAL(this, new Handler());
		// name = (EditText) findViewById(R.id.name);
		edt_total = (TextView) findViewById(R.id.edt_total);
		code = (EditText) findViewById(R.id.code);
		searchbut = (Button) findViewById(R.id.searchbut);
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainList.this.finish();

			}
		});
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainList.this, CreateLdlDetail.class);
				startActivity(intent);
			}
		});
		intiListview();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0 || arg2 == (SURPERDATA.size() + 1))
					return;
				showDialog((String) SURPERDATA.get(arg2 - 1).get("aac002"),
						(String) SURPERDATA.get(arg2 - 1).get("aac003"));
			}
		});
		searchbut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EMTYTEXT = "未找到指定人员。";
				searchbut.setEnabled(false);
				SUPERPAGENUM = 0;
				doSetListView();
			}
		});
	}

	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA,
				R.layout.ldl_main_list_item, new String[] { "aac003", "aac002",
						"aac006", "aac016", "contacts_head" }, new int[] {
						R.id.contact_name, R.id.code, R.id.birthday,
						R.id.state, R.id.contacts_head }) {
			@Override
			protected void iniview(View view, int position,
					List<? extends Map<String, ?>> data) {
			}
		};
		adapter.setViewBinder();
		listview.setAdapter(adapter);

	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {

				if (ts.equals("aac004")) {

					if (tm.get(ts).equals("1")) {
						otm.put("contacts_head", BitmapFactory.decodeResource(
								res, R.drawable.ic_launcher));
					} else {
						otm.put("contacts_head", BitmapFactory.decodeResource(
								res, R.drawable.ic_launcher1));

					}

				}
				if (ts.equals("aac016")) {
					otm.put(ts, ParseData.JYZT.get(tm.get(ts)));
					continue;
				}

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

	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	@Override
	protected List<Map<String, String>> setDataMethod() {
		String json = "";
		if (code.getText().toString().trim().endsWith("x")) {
			if (this.commonUtil
					.isNumeric(code
							.getText()
							.toString()
							.trim()
							.substring(
									0,
									code.getText().toString().trim().length() - 1)))
				json = dal.doGetAll(code.getText().toString(), "",
						SUPERPAGENUM, this.user.getOrg());
		}
		if (this.commonUtil.isNumeric(code.getText().toString().trim()))
			json = dal.doGetAll(code.getText().toString(), "", SUPERPAGENUM,
					user.getDepartmentid());
		else
			json = dal.doGetAll("", code.getText().toString().trim(),
					SUPERPAGENUM, user.getDepartmentid());

		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("total") != null) {
			handler.sendEmptyMessage(Integer.valueOf(map.get("total")));
		} else {
			handler.sendEmptyMessage(-1);
		}
		if (map.get("success") != null && map.get("success").equals("true")) {
			list = DataConvert.toArrayList(map.get("labours"));
			return list;
		}
		return null;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == -1) {
				edt_total.setText("共0条记录");
			} else {
				edt_total.setText("共" + msg.what + "条记录");
			}
		};
	};

	void showDialog(final String code, final String name) {

		new AlertDialog.Builder(this).setTitle("请选择")
				.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// "基本信息", "就业状况登记", "培训意愿登记", "资源减少登记", "求职意愿登记"
						if (which == 0) {
							Intent intent = new Intent(MainList.this,
									LdlDetail.class);
							intent.putExtra("code", code);
							intent.putExtra("name", name);
							startActivity(intent);
						} else if (which == 1) {
							Intent intent = new Intent(MainList.this,
									JyzkDetail.class);
							intent.putExtra("code", code);
							intent.putExtra("name", name);
							startActivity(intent);
						} else if (which == 2) {
							Intent intent = new Intent(MainList.this,
									PxyyDetail.class);
							intent.putExtra("code", code);
							intent.putExtra("name", name);
							startActivity(intent);
						} else if (which == 3) {
							Intent intent = new Intent(MainList.this,
									ZyjsDetail.class);
							intent.putExtra("code", code);
							intent.putExtra("name", name);
							startActivity(intent);
						} else if (which == 4) {
							Intent intent = new Intent(MainList.this,
									QzyyDetail.class);
							intent.putExtra("code", code);
							intent.putExtra("name", name);
							startActivity(intent);
						}

					}
				}).show();

	}

}
