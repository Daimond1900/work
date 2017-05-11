package com.yifeng.hnqzt.ui;

import java.util.HashMap;
import java.util.Map;

import com.yifeng.hnqzt.data.UserDAL;
import com.yifeng.hnqzt.entity.User;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.StringHelper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/**
 * 实现自动登录服务
 * @author wujiahong
 * 2012-10-17
 */
public class AutoLoginService  extends Service{
	private UserDAL userDAL;
	private User loginUser;//登录用户
	private User locationUser;//保存在本地的用户
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.v("自动登录：", "启动:AutoLoginService .....");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		 
		
        super.onDestroy();
		Log.v("自动登录:","关闭：AutoLoginService.....");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		userDAL = new UserDAL(this);
		
		UserSession user=new UserSession(this);
		locationUser=user.getUser();
		
		new Thread(autoRunnable).start();
		Log.v("自动登录：", "AutoLoginService 自动登录线程开始.....");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private Runnable autoRunnable=new Runnable() {
		@Override
		public void run() {
			try{
				
				 Thread.sleep(100);
				 loginUser=userDAL.loadUser(locationUser.getUserName(),locationUser.getUserPwd());
				 handler.sendEmptyMessage(1);
		    
		    }catch(Exception e){
		    	e.printStackTrace();
		    	Log.v("自动登录失败：", e.getMessage());
		    	
		    	handler.sendEmptyMessage(-1);
		    }
		}
	};
	
	
  Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if(msg.what==1){
			  if(loginUser.getState()==ConstantUtil.LOGIN_SUCCESS){
					ConstantUtil.isLogin=true;//更新登录状态
			  }
			}
			//操作完成后不管成功或失败关闭服务
			AutoLoginService.this.stopSelf();
		}

	};
	

}
