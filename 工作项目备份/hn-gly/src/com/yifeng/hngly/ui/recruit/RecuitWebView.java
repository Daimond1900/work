package com.yifeng.hngly.ui.recruit;
import com.yifeng.hngly.ui.BaseWebView;
import com.yifeng.hngly.ui.R;
import com.yifeng.hngly.util.ConstantUtil;
import com.yifeng.qzt.ui.mapabc.MapInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class RecuitWebView extends BaseWebView {
	private String fixUrl="";
	private Button back_btn;
	private TextView title;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruitment_webview);
        title=(TextView)findViewById(R.id.title);
        
        String mtitle=this.getIntent().getStringExtra("title")==null?"详细信息":this.getIntent().getStringExtra("title");
        title.setText(mtitle);
        
    	//fixUrl="file:///android_asset/html/info.html";
        fixUrl = ConstantUtil.ip + this.getIntent().getStringExtra("url");
      
        this.setUrl(fixUrl);
    	this.doInitView();
    	
    	MyOnclick onclick=new MyOnclick();
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);
		
		
		webView.addJavascriptInterface(new Object(){
			//查看地图
	    	public void toMap(){
	     		 Handler mHandler=new Handler();
	     		 mHandler.post(new Runnable(){  
	                  public void run() {
	                	Intent bl=new Intent(RecuitWebView.this,MapInfoActivity.class);
	                	startActivity(bl);
	                  } 
	              });
	     	 }
		},"android");
		
    }
    
    
    
    private class MyOnclick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					
					RecuitWebView.this.finish();
				}
				break;
			default:
				break;
			}
		}

	
	}
}
