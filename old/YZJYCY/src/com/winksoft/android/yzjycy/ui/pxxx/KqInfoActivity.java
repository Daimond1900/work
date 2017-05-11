package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseListActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.MenuActivity;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.util.ReJson;
import com.yifeng.nox.android.json.DataConvert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class KqInfoActivity extends BaseListActivity implements OnClickListener {
	private final String TAG = "KqInfoActivity";
	private Button back, searchbut;
	private MyBaseAdapter adapter;
	private EditText code; // 搜索
	private String keyWord = ""; // 查询的关键字
	pxDAL dal;
	private String listJson = "";
	List<Map<String, String>> listInfo = new ArrayList<Map<String,String>>(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kq_activity);

		dal = new pxDAL(this, new Handler());
		searchbut = (Button) findViewById(R.id.searchbut);
		searchbut.setOnClickListener(this);
		code = (EditText) findViewById(R.id.code); // 搜索
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(this);
//		listJson = getIntent().getStringExtra("kqCountStr");
//		listInfo = DataConvert.toConvertStringList(listJson, "enrolledTraningList");
//		listInfo = new ReJson(this, new Handler()).setValue(listJson, "enrolledTraningList").getList();
		intiListview();

		// listview.setOnItemClickListener
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(KqInfoActivity.this, KqInfoDetailsActivity.class);
				/*
				 * ** 标记，显示 报名 取消报名按钮（1：已经报名的，0：没有报名的） 1. 标题 2. 培训机构 3. 培训地点 4.
				 * 联系人 5. 联系电话 6. 培训时间 7. 截至报名时间 8. 课程信息 （大文本）
				 */
				// String jzsj = SURPERDATA.get(arg2 -
				// 1).get("jzsj").toString();
				// String title = SURPERDATA.get(arg2 -
				// 1).get("jzsj").toString();

				String class_id = SURPERDATA.get(arg2 - 1).get("class_id").toString();
				intent.putExtra("class_id", class_id);
				startActivity(intent);
			}
		});
	}

	// 未完
	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA, R.layout.kq_list_item,
				new String[] {"class_id", "class_name"},
				/*
				 * contact_name:标题 nrjj： 内容简介 jzrq：截至日期 contacts_head：图片
				 * ,R.id.contacts_head
				 */
				new int[] { R.id.classid, R.id.classname}) {
			@Override
			protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
				/** 图片加载，截至日期的格式化 */
				// ((TextView)view.findViewById(R.id.jzrq)).setText(Html.fromHtml("<font>"
				// + "2019-2-22" + "</font>"));
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
		return dal.doKqInfoQuery(keyWord,user.getUserId());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchbut:
			keyWord = code.getText().toString().trim();
			SUPERPAGENUM = 0;
			intiListview();
			break;
		case R.id.back:
			KqInfoActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
