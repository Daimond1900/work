package com.winksoft.android.yzjycy.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.winksoft.android.yzjycy.R;
import com.yifeng.nox.android.ui.BaseLoading;
import android.view.animation.Animation.AnimationListener;

public class LoadingActivity extends BaseLoading{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=View.inflate(this, R.layout.loading, null);
		setContentView(view);
		
		// 渐变展示启动屏
		AlphaAnimation ap = new AlphaAnimation(0.5f, 1.0f);
		ap.setDuration(2000);
		view.startAnimation(ap);
		ap.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//带有自动更新闪屏,参数说明:successToPage 跳至成功界面,failToPage 跳至失败界面,downApkUrl 下载apk地址 ,versionUrl 检测版号txt路径,isShowMsg 是否显示进度文字
//		        initPage(LoginActivity.class, null, Constants.downapk, Constants.downtxt, false);
				initPage(MainActivity.class, 1000);
			}
		});
		
		//不带有自动更新闪屏
	    //参数说明:goToPage跳至目标界面,sleep缓冲毫秒数
        //this.initPage(LoginActivity.class, 2200);
	}
	
}
