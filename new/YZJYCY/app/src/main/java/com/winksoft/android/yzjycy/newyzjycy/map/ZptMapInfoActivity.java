package com.winksoft.android.yzjycy.newyzjycy.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.winksoft.android.yzjycy.R;

/**
 * 地图显示
 */
public class ZptMapInfoActivity extends Activity {
    private MapView mapView;
    private boolean isFirstLocate = true;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.zpt_mapabc_view);
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ZptMapInfoActivity.this.finish();
            }
        });
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);/*新加的 卫星图*/
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        baiduMap.setMapType();
        baiduMap.setMyLocationEnabled(true);
        double lat;
        double lot;
        try {
            lat = Double.parseDouble(getIntent().getStringExtra("lat"));
            lot = Double.parseDouble(getIntent().getStringExtra("lot"));
        } catch (Exception e) {
            lat = 0.0;
            lot = 0.0;
        }
        navigateTo(lat, lot);
    }

    private void navigateTo(double lat, double lot) {

        if (isFirstLocate) {
            LatLng ll = new LatLng(lat, lot);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(lat);
        locationBuilder.longitude(lot);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
