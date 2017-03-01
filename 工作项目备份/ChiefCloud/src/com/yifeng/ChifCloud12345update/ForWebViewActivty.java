package com.yifeng.ChifCloud12345update;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.manager.LoginBiz;
/***
 * 热线点评列表
 * @author WuJiaHong
 *
 */
public class ForWebViewActivty extends BaseWebView {
	WebView webview;
	TextView title;
	Button back;
	String url, title_name, data;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
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
		title=(TextView)findViewById(R.id.title);
		title.setText(title_name);
		initBottom();
		this.setUrl(url);
		this.doInitView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu1, R.drawable.bottom_menu1_);
	}
}
