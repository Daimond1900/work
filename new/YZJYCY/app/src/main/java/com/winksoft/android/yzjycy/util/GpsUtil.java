package com.winksoft.android.yzjycy.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by 1900 on 2017/4/13.
 */

public class GpsUtil {
    private final static boolean DEBUG = true;
    private final static String TAG = "GpsUtil";
    private static GpsUtil mInstance;
    private BDLocation mLocation = null;
    private MLocation mBaseLocation = new MLocation();
    public static Handler mHandler;
    public static int SEND_GPS_MSG=1001888;
    public static GpsUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GpsUtil(context);
        }
        return mInstance;
    }

    Context mContext;
    String mProvider;
    public BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;

    public GpsUtil(Context context) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        initParams();
        mLocationClient.registerLocationListener(myListener);
    }

    public void startMonitor() {
        if (DEBUG) Log.d(TAG, "start monitor location");
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        } else {
            Log.d("LocSDK3", "locClient is null or not started");
        }
    }

    public void stopMonitor() {
        if (DEBUG) Log.d(TAG, "stop monitor location");
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public BDLocation getLocation() {
        if (DEBUG) Log.d(TAG, "get location");
        return mLocation;
    }

    public MLocation getBaseLocation() {
        if (DEBUG) Log.d(TAG, "get location");
        return mBaseLocation;
    }

    private void initParams() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //option.setPriority(LocationClientOption.NetWorkFirst);
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位
//        option.setPoiNumber(5);    //最多返回POI个数
//        option.setPoiDistance(1000); //poi查询距离
//        option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            mLocation = location;
            mBaseLocation.latitude = mLocation.getLatitude();
            mBaseLocation.longitude = mLocation.getLongitude();
            mBaseLocation.province = mLocation.getProvince();
            mBaseLocation.city = mLocation.getCity();
            mBaseLocation.district = mLocation.getDistrict();
            mBaseLocation.street = mLocation.getStreet();
            mBaseLocation.streetNumber = mLocation.getStreetNumber();

            StringBuilder currentPosition = new StringBuilder();
            currentPosition
                    .append("纬度：")
                    .append(location.getLatitude())
                    .append("\n");
            currentPosition
                    .append("经度：")
                    .append(location.getLongitude())
                    .append("\n");
            currentPosition
                    .append("国家：")
                    .append(location.getCountry())
                    .append("\n");
            currentPosition
                    .append("省：")
                    .append(location.getProvince())
                    .append("\n");
            currentPosition
                    .append("市：")
                    .append(location.getCity())
                    .append("\n");
            currentPosition
                    .append("区：")
                    .append(location.getDistrict())
                    .append("\n");
            currentPosition
                    .append("街道：")
                    .append(location.getStreet())
                    .append("\n");
            currentPosition
                    .append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
            Log.d(TAG, "onReceiveLocation: 位置信息 = " + currentPosition.toString());
            if (DEBUG) Log.d(TAG, "" + currentPosition);
            if (mHandler != null) {
                Message msg = new Message();
                msg.what = SEND_GPS_MSG;
                Bundle b = new Bundle();
//                b.putString("address", address);
                b.putDouble("latitude", mLocation.getLatitude());
                b.putDouble("longitude", mLocation.getLongitude());
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    public class MLocation {
        public double latitude;
        public double longitude;
        public String province; //省
        public String city; //市
        public String district; //区
        public String street; //街道
        public String streetNumber;//门牌号号码
    }
}
