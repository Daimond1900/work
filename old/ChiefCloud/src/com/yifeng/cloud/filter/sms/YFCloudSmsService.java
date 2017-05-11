package com.yifeng.cloud.filter.sms;
import com.yifeng.util.ConstantUtil;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
/**
 * 短信挂起服务
 * 开机启动和登陆成功后执行服务
 * @author WuJiaHong
 * 2012-06-20
 */
public class YFCloudSmsService extends Service{
	private MySmsManager mSMSManager;// 短信管理器
	private Handler mHandler;
	private final String YFTAG="YFCloudSmsService服务";
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.v(YFTAG, "启动YFCloudSmsService....");
		super.onCreate();
	}
	
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	Log.v(YFTAG, "销毁YFCloudSmsService...");
    	
    	stopService();
    	
    	super.onDestroy();
    }
    
    private void stopService(){
    	
    	if(mSMSManager!=null){
			 //反注册
			 mSMSManager.unRegister();
			
			 //终止子线程
			 mSMSManager.destory();
		}
    	ConstantUtil.YFCloudSmsServiceDoing=false;//关闭监听状态
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
    	//开始执行
    	Log.v(YFTAG, "开始执行YFCloudSmsService...");
        initLogic();
    	ConstantUtil.YFCloudSmsServiceDoing=true;//开启监听状态
    	
    	return super.onStartCommand(intent, flags, startId);
    }
    
    public void initLogic()
	{
		
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
			}
			
		};
	
		mSMSManager = new MySmsManager(mHandler, this);
		
		// 注册短信拦截和数据库监听
		mSMSManager.register(mHandler, new IProcessObser() {
			
			@Override
			public void onProcessSms(SmsInfo info) {
				// TODO Auto-generated method stub
				if(info!=null){
					
					//发送广播
				    Intent bl=new Intent();
				    bl.setAction(ConstantUtil.SMS_FILTER_ACTION);
					bl.putExtra("sender", info.address);
					bl.putExtra("body",info.body);
					bl.putExtra("date",info.date);
					sendBroadcast(bl);
					
					Log.v(YFTAG, "已过滤了一条来自"+info.address+"的短信并发送广播.....");
				}
			}
		});
		
	}
    

}
