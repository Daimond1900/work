package com.yifeng.hnjcy.ui;

import android.content.Context;


public class AppContext
{
	private static Context	c;

	public static void init(Context ctx)
	{
		c = ctx;
	}

	public static Context get()
	{
		return c;
	}
}