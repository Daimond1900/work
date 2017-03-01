package com.winksoft.yzsmk.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class CommonUtil {
	
	private static String tag = "CommonUtils";
	
	/*
	 * 右补空格
	 * */
	public static String padRight(String src, int len)
	{
		String ret = src;
		if(src.length()<len)
		{
			for(int i=0 ;i<len-src.length();i++)
			{
				ret = ret + " ";
			}
		}
		return ret;
	}
	
	/*左补空格,第一个补0*/
	public static String padLeft(String src, int len)
	{
		String ret = src;
		if(src.length()<len)
		{
			for(int i=0 ;i<len-src.length();i++)
			{
				if(i==0)
				{
					ret = "0" + ret;
				}
				else
				{
				    ret =  " " + ret ;
				}
			}
		}
		return ret;
	}
	
	/*左补0*/
	public static String padLeftZero(String src, int len)
	{
		String ret = src;
		if(src.length()<len)
		{
			for(int i=0 ;i<len-src.length();i++)
			{
				ret = "0" + ret;
			}
		}
		return ret;
	}
}

