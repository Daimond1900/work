package com.yifeng.manager;

import android.content.Context;

public class BaseBiz {
	private Context context;
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public BaseBiz(Context context){
		this.context=context;
	}
}
