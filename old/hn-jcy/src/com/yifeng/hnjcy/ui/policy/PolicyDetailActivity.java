package com.yifeng.hnjcy.ui.policy;

import com.yifeng.hnjcy.data.PolicyDAL;
import com.yifeng.hnjcy.ui.BaseWebView;
import com.yifeng.hnjcy.ui.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 政策法规详细
 * 
 * @author ZK
 */
public class PolicyDetailActivity extends BaseWebView {
	private PolicyDAL dal;
	private Button back;
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_detail);

		String id = getIntent().getStringExtra("id");
		textView = (TextView) findViewById(R.id.top_title);
		textView.setText(getIntent().getStringExtra("title_name"));
		
		dal = new PolicyDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyDetailActivity.this.finish();
			}
		});

		this.setUrl(dal.doGetPolicyDetail(id));
		this.doInitView();
	}
}
