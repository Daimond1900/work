package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class KqInfoActivity extends BaseListActivity implements OnClickListener {
	private final String TAG = "KqInfoActivity";
	private Button back, searchbut;
	private EditText code; // 搜索
	private String keyWord = ""; // 查询的关键字
	private String listJson = "";
	List<Map<String, String>> listInfo = new ArrayList<Map<String,String>>();
	Dialog proDialog;
	private YFBaseAdapter adapter;
	private XwzxDAL xwzxDAL;
	private CommonUtil commonUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kq_activity);
		xwzxDAL = new XwzxDAL(this);
		commonUtil = new CommonUtil(this);
		searchbut = (Button) findViewById(R.id.searchbut);
		searchbut.setOnClickListener(this);
		code = (EditText) findViewById(R.id.code); // 搜索
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(this);

		intiListview(false,true);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(KqInfoActivity.this, KqInfoDetailsActivity.class);
				String class_id = SURPERDATA.get(arg2 - 1).get("class_id").toString();
				intent.putExtra("class_id", class_id);
				startActivity(intent);
			}
		});
	}

	@Override
	protected BaseAdapter setAdapter() {
		adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.kq_list_item,
				new String[] {"class_id", "class_name"},
				new int[] { R.id.classid, R.id.classname}, getResources()) {
			@Override
			protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
			}
		};
		return adapter;
	}

	@Override
	protected void myNotifyDataSetChanged() {
		searchbut.setEnabled(true);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void loadDate() {
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
			@Override
			public void onStart() {
				super.onStart();
				onListviewStart();
				proDialog = CustomeProgressDialog.createLoadingDialog(
						KqInfoActivity.this, "加载中,请稍后...");
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
		xwzxDAL.doKqInfoQuery(SUPERPAGENUM,keyWord, callBack);
	}

	/**
	 * @param json
	 */
	private void postResult(String json) {
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (("true").equals(map.get("success"))) {
				STRINGLIST = DataConvert.toConvertStringList(json, "enrolledTraningList");
				onListviewSuccess();
				return;
			} else {
				onListviewNoResult();
			}
		} else {
			onListviewNoResult();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchbut:
			keyWord = code.getText().toString().trim();
			SUPERPAGENUM = 0;
			loadDate();
			break;
		case R.id.back:
			KqInfoActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
