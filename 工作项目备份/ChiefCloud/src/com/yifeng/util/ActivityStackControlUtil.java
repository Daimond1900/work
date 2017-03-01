package com.yifeng.util;
import java.util.*;

import android.app.Activity;
/**
 * activity管理
 * @author DELL
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
   
   public static void finishProgram(){
	   for (Activity activity:activityList) {
		activity.finish();
	   }
	   android.os.Process.killProcess(android.os.Process.myPid());
   }
   
   public static Activity getActivity(){
	   return activityList.get(0);
   }
}
