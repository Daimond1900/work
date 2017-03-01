package com.yifeng.ChifCloud12345update;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.manager.LoginBiz;
/**
 * 详细页
 * @author WuJiaHong
 *
 */
public class WebViewForMyWorkActivty extends BaseWebView{
	private TextView title;
	private Button back;
	private String url, title_name, data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		title = (TextView) findViewById(R.id.title);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent intent = getIntent();
		title_name = intent.getStringExtra("title_name");
		url = intent.getStringExtra("url")+"&mobile_no="+LoginBiz.loadUser(this).getMobile_no();
		data = intent.getStringExtra("data");
		title.setText(title_name);
		
		this.setUrl(url);
		this.doInitView();
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

}
