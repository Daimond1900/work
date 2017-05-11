package com.yifeng.hnjcy.ui.resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import com.yifeng.cloud.entity.User;
import com.yifeng.hnjcy.adapter.AsyncAdapter;
import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseListActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.UserSession;

/**
* @Description: 简历代维
* @author ZhuZhiChen   
* @date 2014-9-23 
 */
public class ResumeListActivity extends BaseListActivity implements OnClickListener{
	private ResumeDAL dal;
	AsyncAdapter adapter;
	String s_top_title;
	private Button back,btn_add;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_main);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ResumeListActivity.this.finish();
			}
		});
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try{
					Intent intent = new Intent(ResumeListActivity.this, ResumeDetailActivity.class);
					intent.putExtra("id", SURPERDATA.get(position-1).get("id").toString());
					startActivity(intent);
				}catch (Exception e) {
					
				}
			}
		});
		UserSession session = new UserSession(this);
		user = session.getUser();
		dal = new ResumeDAL(this, new Handler());
	}

	@Override
	protected void setAdapter() {
		adapter = new AsyncAdapter(this, SURPERDATA, R.layout.resume_main_item,
				new String[] { "xm", "xb", "sfzh",
						"qzgw","is_o" }, new int[] { R.id.r_name,
						R.id.r_sex, R.id.r_num, R.id.r_qzgw ,R.id.r_open },
				getResources(), listview);
		adapter.setViewBinder();
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
			otm.put("is_o", otm.get("is_open").equals("Y")? R.drawable.zx:R.drawable.ys);
			SURPERDATA.add(otm);
		}
		System.out.println();

	}

	@Override
	protected void myNotifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	@Override
	protected List<Map<String, String>> setDataMethod() {
		return dal.doQueryResumeList(SUPERPAGENUM,user.getUserId(),"");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			Intent intent = new Intent(ResumeListActivity.this,AddResumeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
}
