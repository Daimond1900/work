package com.yifeng.hnzpt.ui.mapabc;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.yifeng.hnzpt.ui.AppContext;


public class BMapApiApp extends Application {
	
	static BMapApiApp mDemoApp;
	
	//百度MapAPI的管理类
	public BMapManager mBMapMan = null;
	
	// 授权Key
	// TODO: 请输入您的Key,
	// 申请地址：http://dev.baidu.com/wiki/static/imap/key/
	public String mStrKey = "rdCPeGEGjecyMYF4UBIgMPhr";
	boolean m_bKeyRight = true;	// 授权Key正确，验证通过
	 

	@Override
    public void onCreate() {
		Log.v("BMapApiApp", "onCreate");
		mDemoApp = this;
		initEngineManager(this);
		AppContext.init(this);
		super.onCreate();
	}
	public void initEngineManager(Context context) {
        if (mBMapMan == null) {
        	mBMapMan = new BMapManager(context);
        }

        if (!mBMapMan.init(mStrKey,new MyGeneralListener())) {
            Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	
	public static BMapApiApp getInstance() {
		return mDemoApp;
	}
	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
//                Toast.makeText(BMapApiApp.getInstance().getApplicationContext(), 
//                        "请在 App.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                BMapApiApp.getInstance().m_bKeyRight = false;
            }
        }
    }

}
