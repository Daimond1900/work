package com.yifeng.hnqzt.ui.policy;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.ui.BaseWebView;

/**
 * comments:政策法规-详情
 * @author Administrator
 * Date: 2012-9-3
 */
public class PolicyDetailActivity extends BaseWebView
{
	private TextView titleTxt;
	private Button backBtn;
	private String fixUrl = "";
	private LinearLayout buttom_menu;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_detail);
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		buttom_menu = (LinearLayout) findViewById(R.id.buttom_menu);
		buttom_menu.setVisibility(View.GONE);
		
		Bundle qz = this.getIntent().getExtras();
		titleTxt.setText(qz.getString("title"));
		fixUrl=this.getIntent().getStringExtra("url")==null?"":this.getIntent().getStringExtra("url");
		this.setUrl(fixUrl);
		this.doInitView();
		
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PolicyDetailActivity.this.finish();
			}
		});
		
	}
	
}