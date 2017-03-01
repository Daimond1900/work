package com.yifeng.hnqzt.ui.venture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.ui.BaseWebView;
import com.yifeng.hnqzt.util.ConstantUtil;

/**
 * comments:创业平台-详情
 * @author Administrator
 * Date: 2012-9-1
 */
public class VentureDetailActivity extends BaseActivity
{
	private TextView title,fromSource,timeTxt,content;
	private Button backBtn;
	private String pic_url="";
	private Bitmap bmp;
	private ImageView pic;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venture_detail);
		
		title = (TextView) findViewById(R.id.titleTxt);
		fromSource=(TextView) findViewById(R.id.fromSource);
		timeTxt=(TextView)findViewById(R.id.timeTxt);
		content=(TextView)findViewById(R.id.content);
		pic=(ImageView)findViewById(R.id.pic);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				VentureDetailActivity.this.finish();
			}
		});
		
		initPage();
		
		if(!pic_url.equals("")){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
						bmp=commonUtil.getURLBitmap(pic_url);
						mhand.sendEmptyMessage(2);
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
				}
			}).start();
		}
		
	}
	
	private void initPage(){
		Intent qz = this.getIntent();
		title.setText(qz.getStringExtra("title"));
		fromSource.setText(qz.getStringExtra("fromSource"));
		timeTxt.setText(qz.getStringExtra("pubshTime"));
		content.setText(Html.fromHtml(qz.getStringExtra("content")));
		pic_url=qz.getStringExtra("pic_url");
	}
	Handler mhand=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==2){
				if(bmp!=null){
				 pic.setImageBitmap(bmp);
				}else{
				 pic.setImageDrawable(getResources().getDrawable(R.drawable.nopic));
				}
			}
		};
	};
	
}
