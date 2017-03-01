package com.yifeng.hnqzt.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseWebView;

public class ResumeInfoActivity extends BaseWebView{
	private String fixUrl="";
	private Button back_btn;
	private TextView title;
	private LinearLayout buttom_menu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruitment_webview);
        buttom_menu=(LinearLayout)findViewById(R.id.buttom_menu);
        Intent bl=this.getIntent();
        
        if(bl.getStringExtra("info")!=null){
        	buttom_menu.setVisibility(View.GONE);
        }
        
    	fixUrl="file:///android_asset/html/resume.html";
    	
    	this.setUrl(fixUrl);
    	this.doInitView();
    	
    	MyOnclick onclick=new MyOnclick();
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);
		
		title=(TextView)findViewById(R.id.title);
		title.setText("简历预览");
    }
    private class MyOnclick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				ResumeInfoActivity.this.finish();
				break;
			
			default:
				break;
			}
		}

	
	}
}
