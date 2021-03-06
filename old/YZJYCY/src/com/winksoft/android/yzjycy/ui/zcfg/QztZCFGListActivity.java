package com.winksoft.android.yzjycy.ui.zcfg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseListActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.TrainingDAL;
import com.winksoft.android.yzjycy.util.Constants;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * comments:政策法规
 */
public class QztZCFGListActivity extends BaseListActivity implements
		OnClickListener {
	private TextView titleTxt;
	private Button backBtn, refreshBtn;
	private TrainingDAL trainingDAL;
	private MyBaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_zcfg_list);

		trainingDAL = new TrainingDAL(this);

		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText("政策法规");

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
//		refreshBtn = (Button) findViewById(R.id.refreshBtn);
//		refreshBtn.setOnClickListener(this);

		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					Intent intent = new Intent(QztZCFGListActivity.this,
							QztZCFGDeatilActivity.class);
					// String
					// articleid=list.get(arg2).get("articleid").toString();
					String news_id = SURPERDATA.get(arg2 - 1).get("news_id")
							.toString();
					Bundle bundle = new Bundle();
					bundle.putString("title", "政策法规详情");
					bundle.putString("url", Constants.IP
							+ "android/news/queryNewsDetail?news_id=" + news_id);
					intent.putExtras(bundle);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					commonUtil.shortToast("未响应!");
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			QztZCFGListActivity.this.finish();
			break;
//		case R.id.refreshBtn:
//			SUPERPAGENUM = 0;
//			intiListview();
//			break;
		case R.id.searchBtn:
			SUPERPAGENUM = 0;
			intiListview();
			break;
		default:
			break;
		}
	}

	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA, R.layout.qzt_zcfg_list_item,
				new String[] { "t_title", "t_from", "t_date" },
				new int[] { R.id.training_title, R.id.training_from,
						R.id.training_date }) {

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

			// tm.put("t_title", tm.get("title"));
			// tm.put("t_from", "信息来源:" + tm.get("author"));
			// tm.put("t_date", "发布时间:" + tm.get("pubtime"));
			// tm.put("readCount", "阅读(" + tm.get("hits") + ")");// 阅读量

			tm.put("t_title", tm.get("title"));
			tm.put("news_id", tm.get("news_id"));
			tm.put("t_from", "发布单位:" + tm.get("create_by") == null ? ""
					: "发布单位:" + tm.get("create_by"));
			tm.put("t_date",tm.get("create_time"));
			tm.put("readCount", tm.get("hits"));

			for (String ts : tm.keySet()) {
				otm.put(ts, tm.get(ts));
			}

			SURPERDATA.add(otm);
		}

	}

	@Override
	protected void myNotifyDataSetChanged() {
		adapter.notifyDataSetChanged();

	}

	@Override
	protected List<Map<String, String>> setDataMethod() {

		return trainingDAL.doQuery_ZCFG(SUPERPAGENUM);
	}

}
