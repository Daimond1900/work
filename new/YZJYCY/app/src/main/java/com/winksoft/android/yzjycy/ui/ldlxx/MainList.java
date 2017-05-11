package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainList extends BaseListActivity {
	private static final String TAG = "MainList";
	private Button searchbut, back, save;
	private EditText code;
	private YFBaseAdapter adapter;
	String[] items = { "基本信息", "就业状况登记", "培训意愿登记", "资源减少登记", "求职意愿登记" };
	private Dialog proDialog;
	private CommonUtil commonUtil;
	private JcDAL jcDAL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcy_ldl_main_list);
		jcDAL = new JcDAL(this);
		commonUtil = new CommonUtil(this);
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

		intiListview(false,true);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
				loadDate();
			}
		});
	}

	void showDialog(final String code, final String name) {

		new AlertDialog.Builder(this).setTitle("请选择").setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// "基本信息", "就业状况登记", "培训意愿登记", "资源减少登记", "求职意愿登记"
				if (which == 0) { // "基本信息"
					Intent intent = new Intent(MainList.this, LdlDetail.class);
					intent.putExtra("code", code);
					intent.putExtra("name", name);
					startActivity(intent);
				} else if (which == 1) { // "就业状况登记"
					Intent intent = new Intent(MainList.this, JyzkDetail.class);
					intent.putExtra("code", code);
					intent.putExtra("name", name);
					startActivity(intent);
				} else if (which == 2) { // "培训意愿登记"
					Intent intent = new Intent(MainList.this, PxyyDetail.class);
					intent.putExtra("code", code);
					intent.putExtra("name", name);
					startActivity(intent);
				} else if (which == 3) { // "资源减少登记"
					Intent intent = new Intent(MainList.this, ZyjsDetail.class);
					intent.putExtra("code", code);
					intent.putExtra("name", name);
					startActivity(intent);
				} else if (which == 4) { // "求职意愿登记"
					Intent intent = new Intent(MainList.this, QzyyDetail.class);
					intent.putExtra("code", code);
					intent.putExtra("name", name);
					startActivity(intent);
				}
			}
		}).show();
	}



	@Override
	protected BaseAdapter setAdapter() {
		Log.d(TAG, "setAdapter: SURPERDATA "+ SURPERDATA);
		adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.jcy_ldl_main_list_item,
				new String[] { "aac003", "aac002", "aac006", "state"},
				new int[] { R.id.contact_name, R.id.code, R.id.birthday, R.id.state}, getResources()) {
			@Override
			protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

				ImageView tx = (ImageView) view.findViewById(R.id.contacts_head);
				String flag = data.get(position).get("aac004") != null &&
						!"".equals(data.get(position).get("aac004")) ? (String) data.get(position).get("aac004") :"";
				if("1".equals(flag)){
					tx.setImageResource(R.drawable.jcy_ic_launcher);
				}else{
					tx.setImageResource(R.drawable.jcy_ic_launcher1);
				}
			}
		};
		return adapter;
	}

	@Override
	protected void loadDate() {
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
			@Override
			public void onStart() {
				super.onStart();
				onListviewStart();
				proDialog = CustomeProgressDialog.createLoadingDialog(
						MainList.this, "加载中,请稍后...");
				proDialog.show();
			}

			@Override
			public void onSuccess(Object arg0) {
				super.onSuccess(arg0);
				postResult((String) arg0);
				if (proDialog != null)
					proDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				if (proDialog != null)
					proDialog.dismiss();
				onListviewonFailure();
			}
		};
		jcDAL.doLdlQuery(SUPERPAGENUM, code.getText().toString(), "", callBack);
	}

	/**
	 * @param json
	 */
	private void postResult(String json) {
		Log.d(TAG, "postResult: json " + json);
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (("true").equals(map.get("success"))) {
				STRINGLIST = DataConvert.toConvertStringList(json, "labours");
				onListviewSuccess();
				return;
			} else {
				commonUtil.shortToast("没有记录");
				onListviewNoResult();
			}
		} else {
			onListviewNoResult();
			commonUtil.shortToast("系统繁忙，请稍后再试!");
		}
	}
	@Override
	protected void formatData() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			String state = tm.get("aac016") !=null && !"".equals(tm.get("aac016")) ? tm.get("aac016"):"";
			tm.put("state",ParseData.JYZT.get(state));
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
}
