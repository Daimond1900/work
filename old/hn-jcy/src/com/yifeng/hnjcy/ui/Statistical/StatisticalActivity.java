package com.yifeng.hnjcy.ui.Statistical;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hnjcy.ui.BaseWebView;
import com.yifeng.hnjcy.ui.R;

public class StatisticalActivity extends BaseWebView{
	
	private TextView title;
	private Button backBtn;
	private String fixUrl="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_detail);
		
		Bundle qz = this.getIntent().getExtras();
		title = (TextView)findViewById(R.id.top_titleTxt);
		title.setText(qz.getString("title"));
		fixUrl=qz.getString("url");
		

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				StatisticalActivity.this.finish();
			}
		});
		
		this.setUrl(fixUrl);
		this.doInitView();
	}
	
}
