package com.yifeng.ChifCloud12345.service;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.yifeng.ChifCloud12345update.LoginActivity;
import com.yifeng.ChifCloud12345update.MainPageActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.data.AlertDAL;
import com.yifeng.entity.User;
import com.yifeng.manager.LoginBiz;

public class SendNotificationService extends Service {
	private NotificationManager nm;
	private AlertDAL alertDal;
    private int isAlert=0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		System.out.println("开始启动：SendNotificationService===");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		System.out.println("关闭了：SendNotificationService===");
		notifhandler.removeCallbacks(notifRunnable);//结束线程
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		//显示正在运行 
		
	    systemdoing();
		
		//执行Service方法重要提醒
		//User users=LoginBiz.loadUser(this);
		notifhandler.post(notifRunnable);//启动线程
		 //if(users.getRoleId().equals("5")){//施工人员才提醒
		   //notifhandler.post(notifRunnable);//启动线程
		  //}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
    //创建一个Handler对像
    Handler notifhandler=new Handler();
    
    Runnable notifRunnable=new Runnable() {
   	    int id=0;
   	   
		@Override
		public void run() {
			 id++;
			 doSendMsg(id);
			 //在线程内部执行Handler的postDelayed或者是post方法
			 notifhandler.postDelayed(notifRunnable,30*60*1000);//1分种执行队列一次
		}
	};
	
   /***
    * 发送通知提醒
    * @param id
    */
	private void doSendMsg(int id){
		String service = Context.NOTIFICATION_SERVICE;
		nm = (NotificationManager) getSystemService(service); // get system service
		Notification n = new Notification();

		n.icon=R.drawable.sys_alert;//通知图标
		n.tickerText = "12345热线重要提醒!";//状态栏显示的通知文本提示 
		
		n.when = System.currentTimeMillis();// 通知产生的时间，会在通知信息里显示 
		
		//如果想在Ongoing列表项中显示通知，加上这一句就OK  ，如果在通知栏里就把下面一句话去掉
		//n.flags=Notification.FLAG_ONGOING_EVENT;
		 //添加声音  
		 n.defaults |=Notification.DEFAULT_SOUND; 
		 
		 Intent intent=null;
		 User user=LoginBiz.loadUser(this);
		 alertDal=new AlertDAL(this);
		 
		 Map<String,String> param=new HashMap<String,String>();
		 if(user==null){
			 intent= new Intent(this, LoginActivity.class);//如果用户还没登陆先到登陆界面
		 }else{
			 //点击该通知后要跳转的 TabMainActivity
		 intent=new Intent(this,MainPageActivity.class);
	     intent.putExtra("tag", "0");
		 intent.putExtra("isRead", "0");
	     param.put("user_id", user.getUserId());
	     param.put("department_id", user.getOrg_id());
	     param.put("role_id", user.getRole_type());
	     
		 Map<String,String> map=alertDal.doAlert(param);
		 if(map.get("state").equals("1")){//数据加载成功
//			 if(map.get("zytx").equals("0")){
//				 //如果为0的时候关闭线程，关闭消息
//				 notifhandler.removeCallbacks(notifRunnable);
//				 nm.cancel(2);
//				
//			 }else{
				 //如果不为0的时候才发送消息
				 /***
				  * zytx：重要提醒 ；  ldps：领导批示;  csgd:超时工单
				  */
				 ZYTX=map.get("zytx")==null?"0":map.get("zytx");
				 CSGD=map.get("csgd")==null?"0":map.get("csgd");
				 LDPS= map.get("ldps")==null?"0":map.get("ldps");
				 SPXW=map.get("csgd")==null?"0":map.get("spxw");
				 GQSK= map.get("ldps")==null?"0":map.get("gqsk");
				 String str="";
				 if (ZYTX!=null&&!ZYTX.equals("0")){
					 str+="重要提醒:"+ZYTX;
				 }
				 if (CSGD!=null&&!CSGD.equals("0"))
					 str+="超时工单:"+CSGD;
				 if (LDPS!=null&&!LDPS.equals("0"))
					 str+="领导批示:"+LDPS;
				 if (!str.equals("")){
//					 if (ZYTX!=null&&!ZYTX.equals("0")){
//						 n.defaults |=Notification.DEFAULT_VIBRATE;
//						 long[] vibrate = {0,50,100,150};
//						 n.vibrate = vibrate;
//					 }
					 PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
					 n.setLatestEventInfo(this, "12345数据提醒",str, pi);
					 nm.notify(2, n);
				 }
//			 }
		
		   }
		 }	 
		//System.out.println("正在运行：SendNotificationService===每格一秒执行一次:"+id);
		 Log.v("12345定时提醒：", "====================每格一秒执行一次:"+id);
	} 
	
	
	public static String ZYTX="0";
	public static String CSGD="0";
	public static String LDPS="0";
	public static String GQSK="0";
	public static String SPXW="0";

	/***
	 * 正在运行程序
	 */
	private void systemdoing(){
		String service = Context.NOTIFICATION_SERVICE;
	    NotificationManager main = (NotificationManager) getSystemService(service);
		Notification n = new Notification();
		n.icon=R.drawable.icon;//通知图标
		n.tickerText = "12345热线!";//状态栏显示的通知文本提示 
		n.when = System.currentTimeMillis();// 通知产生的时间，会在通知信息里显示 
		//如果想在Ongoing列表项中显示通知，加上这一句就OK  ，如果在通知栏里就把下面一句话去掉
		n.flags=Notification.FLAG_ONGOING_EVENT;
		//添加声音  
		//n.defaults |=Notification.DEFAULT_SOUND; 
		Intent intent=new Intent(this,MainPageActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
	    n.setLatestEventInfo(this, "12345服务热线","12345服务热线正在运行", pi);
	    main.notify(1, n);//1代表排在第一个当服务停掉可以清
	}
}
