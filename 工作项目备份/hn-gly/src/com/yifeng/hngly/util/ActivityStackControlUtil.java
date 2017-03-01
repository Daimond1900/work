package com.yifeng.hngly.util;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
/**
 * activity管理
 * @author wjh
 *
 */
public class ActivityStackControlUtil {
   private static List<Activity> activityList=new ArrayList<Activity>();
   
   public static void remove(Activity activity){
	   activityList.remove(activity);
   }
   
   public static void add(Activity activity){
	   activityList.add(activity);
   }
   public static List<Activity> getAllActivity(){
	   return activityList;
   }
   
   public static void finishProgram(){
	   try{
		   for (Activity activity:activityList) {
			activity.finish();
		   }
	    android.os.Process.killProcess(android.os.Process.myPid());
	   }catch(Exception e){
		   android.os.Process.killProcess(android.os.Process.myPid());
	   }
	   
   }
}
