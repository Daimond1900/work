package com.winksoft.android.yzjycy.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class GpsUtil implements AMapLocationListener{
	private  LocationManagerProxy mAMapLocManager = null;
	public static String address="暂无定位";
	public static Double latitude=0.0,longitude=0.0;
	private Context mContext;
	public static Handler mHandler;
	public static int SEND_GPS_MSG=1001888;
	
	public GpsUtil(Context context){
		this.mContext=context;
		
		mAMapLocManager = LocationManagerProxy.getInstance(mContext);
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 1000*5, 100, this);//1000*5秒
		
		//打开GPS
		//openGPSSettings();
		
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	//自动打开GPS
	private void openGPSSettings() {        
   	 Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
                PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();
        } catch (Exception e) {
                e.printStackTrace();
        }

   }

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
		 
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				
				String cityCode = "";
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
						+ "\n精    度    :" + location.getAccuracy() + "米"
						+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
						+ location.getTime()+ "\n城市编码:"
						+ cityCode + "\n位置描述:" + desc + "\n省:"
						+ location.getProvince() + "\n市:" + location.getCity()
						+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
						.getAdCode());
				System.out.println("自动定位成功:"+str);
				
				address=desc;
				latitude=location.getLatitude();
				longitude=location.getLongitude();
				
				if(mHandler!=null){
					Message msg=new Message();
					msg.what=SEND_GPS_MSG;
					Bundle b=new Bundle();
					b.putString("address", address);
					b.putDouble("latitude", latitude);
					b.putDouble("longitude", longitude);
					msg.setData(b);
					
					mHandler.sendMessage(msg);
				}
		   
			/*
			LatLng startLatlng=new LatLng(geoLat, geoLng);
			LatLng endLatlng=new LatLng(32.39443, 119.433682);
			//根据用户的起点和终点经纬度计算两点间距离，此距离为相对较短的距离，单位米。
			float limit=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
			System.out.println("当前位置到文昌隔距离==="+limit);*/
			
			
		}
		
	}
	
	/**
	 * 销毁定位
	 */
	public   void stopLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
	}
	
	 /***
     * 把时间2012-10-10 23:34:44 改成2012-10-10
     * @param date
     * @return
     */
    public static String formatDate(String date){
    	String nstr="";
    	if(date!=null){
    		if(date.length()>10){
    			nstr=date.substring(0, 10);
    		}else{
    			nstr=date;
    		}
    	}
    	return nstr;
    }
    
   

}
