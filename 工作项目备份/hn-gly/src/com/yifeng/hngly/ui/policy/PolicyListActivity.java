package com.yifeng.hngly.ui.policy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yifeng.hngly.adapter.AsyncAdapter;
import com.yifeng.hngly.data.PolicyDAL;
import com.yifeng.hngly.ui.BaseListActivity;
import com.yifeng.hngly.ui.R;

/**
 * 报表查询列表
 * 
 * @author ZK
 */
public class PolicyListActivity extends BaseListActivity {
	private PolicyDAL dal;
	private TextView top_title;
	private AsyncAdapter adapter;
	String state = "1";
	String s_top_title;
	private Button back;
	private LinearLayout top_liner1;
	public Button city, province, country;// 政策法规:市内 省内 国内

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy);
		top_title = (TextView) findViewById(R.id.top_title);
		top_liner1 = (LinearLayout) findViewById(R.id.top_liner1);
		intiListview();
		state = getIntent().getStringExtra("state");
	
		s_top_title = getIntent().getStringExtra("top_title");
		top_title.setText(s_top_title);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyListActivity.this.finish();
			}
		});
		city = (Button) findViewById(R.id.city);

		city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 state1="7";
					showTab();
					SUPERPAGENUM=0;
					doSetListView();
			}
		});
		province = (Button) findViewById(R.id.province);
		province.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 state1="6";
				showTab();
				SUPERPAGENUM=0;
				doSetListView();
			}
		});
		country = (Button) findViewById(R.id.country);
		country.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 state1="5";
					showTab();
					SUPERPAGENUM=0;
					doSetListView();
			}
		});
		if (state.equals("1"))
			showTab();
		else {
			top_liner1.setVisibility(View.GONE);
		}
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent detail = new Intent(PolicyListActivity.this,
						PolicyDetailActivity.class);
				detail.putExtra("id",
						SURPERDATA.get(position - 1).get("policy_num")
								.toString());
				detail.putExtra("title_name", "政策法规");
				startActivity(detail);
			}
		});
		dal = new PolicyDAL(this, new Handler());
	}

	@Override
	protected void setAdapter() {
		adapter = new AsyncAdapter(this, SURPERDATA, R.layout.main_policy_item,
				new String[] { "policy_title", "policy_content", "policy_time",
						"image_url" }, new int[] { R.id.policy_title,
						R.id.policy_content, R.id.policy_time, R.id.img_id },
				getResources(), listview);
		listview.setAdapter(adapter);
		adapter.setViewBinder();
	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			otm.put("policy_title", tm.get("BT"));
			otm.put("policy_num", tm.get("ID"));
			otm.put("policy_content", tm.get("NR"));
			otm.put("image_url", tm.get("imageurl"));

			String now;
			try {
				Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.parse(tm.get("FBRQ").toString());
				now = new SimpleDateFormat("MM-dd HH:mm").format(date);
			} catch (Exception e) {
				e.printStackTrace();
				now = tm.get("FBRQ").toString();
			}
			otm.put("policy_time", now);
			SURPERDATA.add(otm);
		}

	}

	@Override
	protected void myNotifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	String state1 = "7";

	@Override
	protected List<Map<String, String>> setDataMethod() {
		if (state.equals("1"))
			return dal.doQueryPolicy(SUPERPAGENUM, state1);
		else
			return dal.doQueryNew(SUPERPAGENUM);
	}

	void showTab() {

		if ("7".equals(state1)) {
			city.setBackgroundResource(R.drawable.contact_left_bar2);
			city.setTextColor(Color.WHITE);
			province.setBackgroundResource(R.drawable.contact_middle_bar1);
			province.setTextColor(Color.BLACK);
			country.setBackgroundResource(R.drawable.contact_right_bar);
			country.setTextColor(Color.BLACK);
		} else if ("6".equals(state1)) {
			province.setBackgroundResource(R.drawable.contact_middle_bar2);
			province.setTextColor(Color.WHITE);
			city.setBackgroundResource(R.drawable.contact_left_bar);
			city.setTextColor(Color.BLACK);
			country.setBackgroundResource(R.drawable.contact_right_bar);
			country.setTextColor(Color.BLACK);
		} else if ("5".equals(state1)) {
			country.setBackgroundResource(R.drawable.contact_right_bar2);
			country.setTextColor(Color.WHITE);
			city.setBackgroundResource(R.drawable.contact_left_bar);
			city.setTextColor(Color.BLACK);
			province.setBackgroundResource(R.drawable.contact_middle_bar1);
			province.setTextColor(Color.BLACK);
		}
	}

}
