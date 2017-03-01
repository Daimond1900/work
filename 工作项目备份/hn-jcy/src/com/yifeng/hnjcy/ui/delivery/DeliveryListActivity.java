package com.yifeng.hnjcy.ui.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import com.yifeng.cloud.entity.User;
import com.yifeng.hnjcy.adapter.AsyncAdapter;
import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseListActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.UserSession;

/**
* @Description: 投递记录
* @author ZhuZhiChen   
* @date 2014-9-23 
 */
public class DeliveryListActivity extends BaseListActivity {
	private ResumeDAL dal;
	AsyncAdapter adapter;
	String s_top_title;
	private Button back;
	User user;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_list);
		title = (TextView)findViewById(R.id.top_title);
		title.setText("投递记录");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeliveryListActivity.this.finish();
			}
		});
		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent detail = new Intent(DeliveryListActivity.this,
						DeliveryDetailActivity.class);
				detail.putExtra("id",
						SURPERDATA.get(position - 1).get("sending_id")
								.toString());
				detail.putExtra("title_name", "投递简历详情");
				startActivity(detail);
			}
		});
		UserSession session = new UserSession(this);
		user = session.getUser();
		dal = new ResumeDAL(this, new Handler());
	}

	@Override
	protected void setAdapter() {
		adapter = new AsyncAdapter(this, SURPERDATA, R.layout.send_main_item,
				new String[] { "xm", "acb216", "aab004","sta","send_time" }, new int[] { R.id.r_name,
						R.id.r_tdgw, R.id.r_tddw, R.id.r_tdzt,R.id.r_time },
				getResources(), listview);
		listview.setAdapter(adapter);
	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
			for (String ts : tm.keySet()) {
				otm.put(ts,
						tm.get(ts).replaceAll("\r\t", "")
								.replaceAll("\r\n", ""));
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
		return dal.doQuerySendList(SUPERPAGENUM,user.getUserId());
	}
}
