package com.winksoft.android.widget;


import com.winksoft.android.yzjycy.R;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class MyAnimationListener implements AnimationListener{
	
	private Context context;
	

	public MyAnimationListener(Context context) {
		super();
		this.context = context;
	}

	private View v;

	private boolean isStop;

	public void setView(View v) {
		this.v = v;
	}

	public void stopAnimation() {
		isStop = true;
	}

	public void startAnimation() {
		isStop = false;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (!isStop) {
			Animation anim = AnimationUtils.loadAnimation(context,
					R.anim.scale_in_static);
			v.startAnimation(anim);
			anim.setAnimationListener(this);
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

}
