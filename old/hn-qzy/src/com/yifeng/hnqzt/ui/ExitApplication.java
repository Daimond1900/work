package com.yifeng.hnqzt.ui;

import com.yifeng.hnqzt.entity.User;

import android.app.Application;

public class ExitApplication extends Application{
	public  static ExitApplication instance;
	private static User user;
	
	//单例模式
	public static  ExitApplication getInstance()
	{
	    if (instance == null)
	      instance = new ExitApplication();
	    return instance;
	 }
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		user=new User();
		super.onCreate();
		instance = this;
	}
	
	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		ExitApplication.user = user;
	}
	
}
