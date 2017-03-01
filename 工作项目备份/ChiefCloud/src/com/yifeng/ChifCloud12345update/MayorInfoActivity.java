package com.yifeng.ChifCloud12345update;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class MayorInfoActivity extends BaseWebView{
	private Button back;
	private TextView title;
	private String title_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jysz);
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
		url = intent.getStringExtra("url");
		title=(TextView)findViewById(R.id.title);
		title.setText(title_name);
		this.setUrl(url);
		this.doInitView();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu5, R.drawable.bottom_menu5_);
	}
}