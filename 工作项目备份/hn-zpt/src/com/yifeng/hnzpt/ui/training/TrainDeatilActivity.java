package com.yifeng.hnzpt.ui.training;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.ui.BaseWebView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * ClassName:TrainDeatilActivity 
 * Description：培训信息-详情
 * @author Administrator 
 * Date：2012-10-24
 */
public class TrainDeatilActivity extends BaseWebView {
	private Button backBtn;
	private String fixUrl = "";
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_detail);

		title = (TextView)findViewById(R.id.top_titleTxt);
		Bundle qz = this.getIntent().getExtras();
		title.setText(qz.getString("title"));
		fixUrl = qz.getString("url");
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TrainDeatilActivity.this.finish();
			}
		});

		this.setUrl(fixUrl);
		this.doInitView();

	}

}
