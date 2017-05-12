package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseListActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.ui.ldlxx.MainList;
import com.winksoft.android.yzjycy.ui.ldlxx.ParseData;
import com.winksoft.android.yzjycy.ui.training.QztTrainDeatilActivity;
import com.winksoft.android.yzjycy.ui.training.QztTrainingActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PxxxActivity extends BaseListActivity implements OnClickListener {
	private final String TAG = "PxxxActivity";
	private Button back, searchbut;
	private MyBaseAdapter adapter;
	private EditText code; // 搜索
	private String keyWord = ""; // 查询的关键字
	pxDAL dal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pxxx_list);

		dal = new pxDAL(this, new Handler());
		searchbut = (Button) findViewById(R.id.searchbut);
		searchbut.setOnClickListener(this);
		code = (EditText) findViewById(R.id.code); // 搜索
		back = (Button) findViewById(R.id.back);
		back.requestFocus();
		back.setOnClickListener(this);

		intiListview();

		// listview.setOnItemClickListener
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				/*
				 * class_name 班级名称 training_agent_name 培训机构名称 training_type 培训类型
				 * training_job_type 培训工种 training_level 培训等级 opening_time
				 * 开班日期/截至日期
				 */try {
					Intent intent = new Intent(PxxxActivity.this, PxDetailsActivity.class);		//跳转至详情界面
					String class_id = SURPERDATA.get(arg2 - 1).get("class_id").toString();
					String class_name = SURPERDATA.get(arg2 - 1).get("class_name").toString();
					String training_agent_name = SURPERDATA.get(arg2 - 1).get("training_agent_name").toString();
					String training_type = SURPERDATA.get(arg2 - 1).get("training_type").toString();
					String training_job_type = SURPERDATA.get(arg2 - 1).get("training_job_type").toString();
					String training_level = SURPERDATA.get(arg2 - 1).get("training_level").toString();
					String opening_time = SURPERDATA.get(arg2 - 1).get("opening_time").toString();

					intent.putExtra("class_id", class_id);
					intent.putExtra("class_name", class_name);
					intent.putExtra("training_agent_name", training_agent_name);
					intent.putExtra("training_type", training_type);
					intent.putExtra("training_job_type", training_job_type);
					intent.putExtra("training_level", training_level);
					intent.putExtra("opening_time", opening_time);
					startActivity(intent);
				} catch (Exception e) {
					commonUtil.shortToast("未响应!");
				}
			}
		});
	}
	
	// 未完
	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA, R.layout.pxxx_list_item,
				new String[] { "class_name", "training_agent_name", "training_type", "training_job_type",
						"training_level", "opening_time", "enrolled_cnt" },
				/*
				 * contact_name:标题 nrjj： 内容简介 jzrq：截至日期 contacts_head：图片
				 * ,R.id.contacts_head
				 */
				new int[] { R.id.bjmc, R.id.pxjg, R.id.pxlx, R.id.pxgz, R.id.pxdj, R.id.jzrq, R.id.rs }) {
			@Override
			protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
				/** 图片加载，截至日期的格式化 */
				ImageView rs_imag = (ImageView) view.findViewById(R.id.rs_imag);
				TextView rs = (TextView) view.findViewById(R.id.rs);
				Map<String, ?> map = data.get(position);
				if ("0".equals(map.get("enrolled_cnt").toString().trim())) {
					rs.setVisibility(View.GONE);
					rs_imag.setVisibility(View.GONE);
				} else {
					rs.setVisibility(View.VISIBLE);
					rs_imag.setVisibility(View.VISIBLE);
				}
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
		return dal.doPxInfoQuery(keyWord);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchbut:
			keyWord = code.getText().toString().trim(); // code.getText().toString().trim();
			SUPERPAGENUM = 0;
			intiListview();
			break;
		case R.id.back:
			PxxxActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
