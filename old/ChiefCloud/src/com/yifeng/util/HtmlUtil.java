package com.yifeng.util;
/***
 * HTMl设置
 * @author WuJiaHong
 *
 */
public class HtmlUtil {
	
	/**
	 * 字体设置
	 * @param str
	 * @param color
	 * @return
	 */
    public static String doFont(String str,String color){
    	String nstr="";
    	if(str!=null){
    	    nstr="<font color='"+color+"'>"+str+"</font>";
    	}
    	return nstr;
    }
    /***
     * 字体设置
     * @param str
     * @param color
     * @param size
     * @return
     */
    public static String doFont(String str,String color,int size){
    	String nstr="";
    	if(str!=null){
    	    nstr="<font color='"+color+"',size='"+size+" px'>"+str+"</font>";
    	}
    	return nstr;
    }
}
