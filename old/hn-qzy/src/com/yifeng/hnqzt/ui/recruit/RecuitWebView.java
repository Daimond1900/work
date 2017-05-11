package com.yifeng.hnqzt.ui.recruit;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseWebView;
import com.yifeng.hnqzt.ui.mapabc.MapInfoActivity;
import com.yifeng.hnqzt.util.ConstantUtil;

public class RecuitWebView extends BaseWebView {
	private String fixUrl="";
	private Button back_btn,deliveryBtn,attentionBtn,contactBtn;
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
		deliveryBtn = (Button) findViewById(R.id.deliveryBtn);//投递简历
		deliveryBtn.setOnClickListener(onclick);
		attentionBtn = (Button) findViewById(R.id.attentionBtn);//关注岗位
		attentionBtn.setOnClickListener(onclick);
		contactBtn = (Button) findViewById(R.id.contactBtn);
		contactBtn.setOnClickListener(onclick);//联系HR
		
		
		webView.addJavascriptInterface(new Object(){
			//查看地图
	    	public void toMap(){//js方法
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
			case R.id.deliveryBtn:
				commonUtil.shortToast("简历投递成功！");
				break;
			case R.id.attentionBtn:
				commonUtil.shortToast("关注岗位成功！");
				break;
			case R.id.contactBtn:
				commonUtil.shortToast("联系HR开发中！");
				break;
				
			
			default:
				break;
			}
		}

	
	}
}
