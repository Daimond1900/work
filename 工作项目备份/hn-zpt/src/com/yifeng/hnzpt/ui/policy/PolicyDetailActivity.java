package com.yifeng.hnzpt.ui.policy;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.data.PolicyDAL;
import com.yifeng.hnzpt.ui.BaseWebView;

import android.os.Bundle;
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
	private PolicyDAL policyDAL;
	private Button backBtn;
	private TextView titleTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_detail);

		String id = getIntent().getStringExtra("id");
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(getIntent().getStringExtra("c_title"));

		policyDAL = new PolicyDAL(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyDetailActivity.this.finish();
			}
		});

		this.setUrl(policyDAL.doGetPolicyDetail(id));
		this.doInitView();
	}
}
