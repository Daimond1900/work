package com.winksoft.android.yzjycy;

import java.io.File;
import java.util.Map;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.winksoft.android.yzjycy.db.MainManage;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyApp extends Application {

	public static MyApp mDemoApp;

	// 百度MapAPI的管理类
	public BMapManager mBMapMan = null;
	public String mStrKey = "povre0eCq8rlK4hHMHrFkMD9hHkLoT1V";
	static boolean m_bKeyRight = true; // 授权Key正确，验证通过
	public static Map<String, Long> map;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("BMapApiApp", "onCreate");
		mDemoApp = this;
		new MainManage(this).initMenus();
		initEngineManager(this);
		AppContext.init(this);
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
		.showImageForEmptyUri(R.drawable.nodatapic) 
		.showImageOnFail(R.drawable.nodatapic) 
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
		.discCache(new UnlimitedDiscCache(cacheDir))
		.defaultDisplayImageOptions(defaultOptions)
		.discCacheSize(50 * 1024 * 1024)
		.discCacheFileCount(100)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
	}

	public void initEngineManager(Context context) {
		if (mBMapMan == null) {
			mBMapMan = new BMapManager(context);
		}

		if (!mBMapMan.init(mStrKey, new MyGeneralListener())) {
			Toast.makeText(getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static MyApp getInstance() {
		return mDemoApp;
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
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
				Toast.makeText(mDemoApp,
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(mDemoApp,
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				// Toast.makeText(BMapApiApp.getInstance().getApplicationContext(),
				// "请在 App.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
				m_bKeyRight = false;
			}
		}
	}

}
