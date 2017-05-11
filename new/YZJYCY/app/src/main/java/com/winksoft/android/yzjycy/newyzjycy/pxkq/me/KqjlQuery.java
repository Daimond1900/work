package com.winksoft.android.yzjycy.newyzjycy.pxkq.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.Constants;

/**
 * 考勤记录详情
 * Created by 1900 on 2017/5/5.
 */

public class KqjlQuery extends BaseActivity {
    private MapView mapView;
    private boolean isFirstLocate = true;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.kqjlquery);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KqjlQuery.this.finish();
            }
        });
        ImageLoader im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(this));
        ImageView kqjl_im = (ImageView) findViewById(R.id.kqjl_im);

        String imgUrl = getIntent().getStringExtra("pic_url") != null && !"".equals(getIntent().getStringExtra("pic_url")) ? getIntent().getStringExtra("pic_url") : "";

        if (!"".equals(imgUrl)) {
            im.displayImage(Constants.IP + imgUrl, kqjl_im);
            findViewById(R.id.tv_xczp).setVisibility(View.GONE);
        }

        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        double lat;
        double lot;
        try {
            lat = Double.parseDouble(getIntent().getStringExtra("chk_lat"));
            lot = Double.parseDouble(getIntent().getStringExtra("chk_lng"));
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
