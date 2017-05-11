package com.yifeng.hnqzt.ui.ambitus;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseWebView;
import com.yifeng.hnqzt.ui.job.JobDetailActivity;

public class AmbitusDetail extends BaseWebView{
	private String fixUrl = "";
	private Button backBtn;
	private LinearLayout buttom_menu;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.job_detail);
	
	buttom_menu=(LinearLayout)findViewById(R.id.buttom_menu);
	buttom_menu.setVisibility(View.GONE);
	
	fixUrl = "file:///android_asset/html/notice.html";
	this.setUrl(fixUrl);
	this.doInitView();
	backBtn = (Button) findViewById(R.id.backBtn);
	backBtn.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			AmbitusDetail.this.finish();
		}
	});
}
}
