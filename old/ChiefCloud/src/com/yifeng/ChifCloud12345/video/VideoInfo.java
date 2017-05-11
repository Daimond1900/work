package com.yifeng.ChifCloud12345.video;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.data.VideoDal;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
public class VideoInfo extends BaseActivity{
	private Button back,play;
	private TextView video_title,video_type,video_date,video_long,video_content;
	private Map<String,String> map=null;
	private VideoDal viedeoDal;
	private String id="";
	private ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoinfo);
		viedeoDal=new VideoDal(this);
		Intent bl=this.getIntent();
		id=bl.getStringExtra("video_id")==null?"":bl.getStringExtra("video_id");
		
		video_title=(TextView)findViewById(R.id.video_title);
		video_type=(TextView)findViewById(R.id.video_type);
		video_date=(TextView)findViewById(R.id.video_date);
		video_long=(TextView)findViewById(R.id.video_long);
		video_content=(TextView)findViewById(R.id.video_content);
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		play = (Button) findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(map!=null){
						String video_url=map.get("URL");
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video_url)));
				}else{
					commonUtil.shortToast("视频地址错误!");	
				}
			}
		});
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
		
		showProg("正在加载请稍后...");
		new Thread(initRunnable).start();
	}
	
	private void showProg(String Msg) {
		pd = ProgressDialog.show(this, null, Msg, true);
		pd.setIndeterminate(true);//设置进度条是否为不明确
		pd.setCancelable(true);//设置进度条是否可以按退回键取消  
	}
	/**
	 * 加载信息详细
	 */
	private Runnable initRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
				map=viedeoDal.doGetInfo(id);
				Message msg=new Message();
				msg.what=0;
				initHandler.sendMessage(msg);
				
			} catch (Exception e) {
			  e.printStackTrace();
			}
		}
	};
	Handler initHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				initPage();
			}
			pd.dismiss();
		}
	};
	
	private void initPage(){
		if(map!=null){
			if(map.get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
				video_title.setText(map.get("TITLE"));
				video_type.setText(map.get("TYPE"));
				video_date.setText(DateUtil.getDate(map.get("UPLOAD_TIME")));
				video_long.setText(map.get("LENGTH"));
				video_content.setText(map.get("CONTENT"));
			}else{
				commonUtil.shortToast("信息加载失败!");	
		   }
		}else{
			commonUtil.shortToast("数据连接超时,请检网络是否正常!");
		}
		
	}
}