package com.yifeng.hnzpt.ui.policy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.PolicyAdapter;
import com.yifeng.hnzpt.data.PolicyDAL;
import com.yifeng.hnzpt.ui.BaseListActivity;
import com.yifeng.hnzpt.util.ActivityStackControlUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ClassName:PolicyListActivity 
 * Description：政策法规-列表
 * @author Administrator 
 * Date：2012-11-5
 */
public class PolicyListActivity extends BaseListActivity {
	public Button cityBtn, provinceBtn, countryBtn;
	private PolicyDAL policyDAL;
	private PolicyAdapter adapter;
	String flag = "7";// 标志位： 市.7; 省.6; 国.5
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_list);

		policyDAL = new PolicyDAL(this);

		MyClick myClick = new MyClick();
		cityBtn = (Button) findViewById(R.id.cityBtn);
		cityBtn.setBackgroundResource(R.drawable.policy_menu_btn1);
		cityBtn.setTextColor(Color.WHITE);
		cityBtn.setOnClickListener(myClick);
		provinceBtn = (Button) findViewById(R.id.provinceBtn);
		provinceBtn.setOnClickListener(myClick);
		countryBtn = (Button) findViewById(R.id.countryBtn);
		countryBtn.setOnClickListener(myClick);

		if (getIntent().getStringExtra("state") != null) {
			flag = getIntent().getStringExtra("state");

			if ("7".equals(flag)) {
				cityBtn.setBackgroundResource(R.drawable.policy_menu_btn1);
				cityBtn.setTextColor(Color.WHITE);
				provinceBtn.setBackgroundResource(R.drawable.policy_menu_btn2_);
				provinceBtn.setTextColor(Color.BLACK);
				countryBtn.setBackgroundResource(R.drawable.policy_menu_btn3_);
				countryBtn.setTextColor(Color.BLACK);
			} else if ("6".equals(flag)) {
				provinceBtn.setBackgroundResource(R.drawable.policy_menu_btn2);
				provinceBtn.setTextColor(Color.WHITE);
				cityBtn.setBackgroundResource(R.drawable.policy_menu_btn1_);
				cityBtn.setTextColor(Color.BLACK);
				countryBtn.setBackgroundResource(R.drawable.policy_menu_btn3_);
				countryBtn.setTextColor(Color.BLACK);
			} else if ("5".equals(flag)) {
				countryBtn.setBackgroundResource(R.drawable.policy_menu_btn3);
				countryBtn.setTextColor(Color.WHITE);
				cityBtn.setBackgroundResource(R.drawable.policy_menu_btn1_);
				cityBtn.setTextColor(Color.BLACK);
				provinceBtn.setBackgroundResource(R.drawable.policy_menu_btn2_);
				provinceBtn.setTextColor(Color.BLACK);
			}
		}

		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					Intent detail = new Intent(PolicyListActivity.this, PolicyDetailActivity.class);
					detail.putExtra("id", SURPERDATA.get(position - 1).get("policy_num").toString());
					detail.putExtra("c_title", "政策法规详情");
					startActivity(detail);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cityBtn:
				Intent intent1 = new Intent(PolicyListActivity.this, PolicyListActivity.class);
				intent1.putExtra("state", "7");
				startActivity(intent1);
				PolicyListActivity.this.finish();
				break;
			case R.id.provinceBtn:
				Intent intent = new Intent(PolicyListActivity.this, PolicyListActivity.class);
				intent.putExtra("state", "6");
				startActivity(intent);
				PolicyListActivity.this.finish();
				break;
			case R.id.countryBtn:
				Intent intent3 = new Intent(PolicyListActivity.this, PolicyListActivity.class);
				intent3.putExtra("state", "5");
				startActivity(intent3);
				PolicyListActivity.this.finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void setAdapter() {
		adapter = new PolicyAdapter(this, SURPERDATA, R.layout.policy_list_item, 
				new String[] { "policy_title", "policy_content", "policy_time", "image_url" },
				new int[] { R.id.policy_title, R.id.policy_content, R.id.policy_time, R.id.img_id }, listview);
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
				Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(tm.get("FBRQ").toString());
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

	@Override
	protected List<Map<String, String>> setDataMethod() {
		return policyDAL.doQueryPolicy(SUPERPAGENUM, flag);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (BASEHANDLER != null) {
				BASEHANDLER.sendMessage(BASEHANDLER.obtainMessage());
			} else {
				System.gc();
				this.finish();
				ActivityStackControlUtil.remove(this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
