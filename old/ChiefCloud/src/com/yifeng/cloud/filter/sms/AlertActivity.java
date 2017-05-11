package com.yifeng.cloud.filter.sms;
import com.yifeng.ChifCloud12345update.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/***
 * 短信提醒界面
 * @author WuJiaHong
 * 2012-08-24
 */
public class AlertActivity extends Activity{
	private Button close_btn;
	private TextView content;
	private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	setContentView(R.layout.sms_alert);
    	
    	Intent bl=this.getIntent();
    	String sender=bl.getStringExtra("sender")==null?"":bl.getStringExtra("sender");//电话号码
    	String body=bl.getStringExtra("body")==null?"":bl.getStringExtra("body");//短信内容
    	
    	content=(TextView)findViewById(R.id.content);
    	content.setText(Html.fromHtml(body));
    	
    	close_btn=(Button)findViewById(R.id.close_btn);
    	close_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertActivity.this.finish();
			}
		});
    	
    	//播放提醒声音
    	doPlayRing();
    }
    /***
	 * 播放提醒声音
	 */
	private void doPlayRing(){
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(AlertActivity.this, R.raw.ring1);
		//mMediaPlayer.setLooping(true);//true为重复播放,false为只播放一次
		try{
		 if (mMediaPlayer != null){ 
		        mMediaPlayer.stop(); 
		 } 
		 /*MediaPlayer取得播放资源与stop()之后 要准备Playback的状态前几定要使用MediaPlayer.prepare()*/
	      mMediaPlayer.prepare(); 
	      /*开始或反复播放*/
	      mMediaPlayer.start();
	     }catch(Exception e){
	    	 e.printStackTrace();
	    	 Log.v("Alarm会议提醒出错", e.getMessage());
	     } 
	}
	/**
	 * 关闭提醒
	 */
	private void colosePlayer(){
		if(mMediaPlayer!=null){
			mMediaPlayer.stop();
		}
	}
    /**
	    *返回
	*/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				 /* 关闭Activity */
				AlertActivity.this.finish();
				colosePlayer();
				return true;
			}
			return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		colosePlayer();
		
		super.onDestroy();
		
	}
    
}
