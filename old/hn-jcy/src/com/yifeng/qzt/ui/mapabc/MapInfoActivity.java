package com.yifeng.qzt.ui.mapabc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.yifeng.hnjcy.ui.R;

/***
 * 地图显示公司详细信息
 * 
 * @author wujiahong 2012-10-22
 */
public class MapInfoActivity extends Activity {
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point;
	private MyLocationOverlay mLocationOverlay;
	public static final int FIRST_LOCATION = 1002;
	private Button back_btn;
	private View mPopView;

	private String longitude = "1192679470";// 经度
	private String latitude = "32248012";// 纬度

	private String companyName = "", companyAddress = "", telNo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.setMapMode(MAP_MODE_VECTOR);//设置地图为矢量模式

		super.onCreate(savedInstanceState);

		// BMapManager的注册和初始化
		
		setContentView(R.layout.mapabc_view);
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MapInfoActivity.this.finish();
			}
		});

		Intent bl = this.getIntent();

		longitude = bl.getStringExtra("longitude") == null ? "1192679470" : bl
				.getStringExtra("longitude");// 经度
		latitude = bl.getStringExtra("latitude") == null ? "32248012" : bl
				.getStringExtra("latitude");// 纬度

		companyName = bl.getStringExtra("companyName");
		companyAddress = bl.getStringExtra("companyAddress");
		telNo = bl.getStringExtra("telNo");

		mMapView = (MapView) findViewById(R.id.main_mapView);
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		mMapController = mMapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		/*
		 * point = new GeoPoint((int) (32.38801132515847 * 1E6), (int)
		 * (119.457947015380 * 1E6)); //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		 */

		// point=new
		// GeoPoint(StringHelper.getLatitude(latitude),StringHelper.getLongitude(longitude));

		int lat = (int) (Double.parseDouble(latitude) * 1E6);
		int lng = (int) (Double.parseDouble(longitude) * 1E6);

		point = new GeoPoint(lat, lng);

		mLocationOverlay = new MyLocationOverlay(mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
		mMapController.setCenter(point); // 设置地图中心点
		mMapController.setZoom(12);

		// 添加自己的位置
		/*
		 * mMapView.getOverlays().add(mLocationOverlay);
		 * mLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
		 * handler.sendMessage(Message.obtain(handler, FIRST_LOCATION)); } });
		 */

		// 添加冒泡框
		addPopView();

	}

	// 添加冒泡框
	private void addPopView() {

		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);// 得到需要标在地图上的资源

		ItemizedOverlay<OverlayItem> lay = new ItemizedOverlay<OverlayItem>(
				marker, mMapView);

		OverlayItem item = new OverlayItem(point, "", "");
		item.setAnchor(OverlayItem.ALING_CENTER);
		lay.addItem(item);
		mMapView.getOverlays().add(lay);

		if (mPopView != null)
			mMapView.removeView(mPopView);

		mPopView = View.inflate(this, R.layout.popup, null);
		mMapView.addView(mPopView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER));
		TextView title = (TextView) mPopView.findViewById(R.id.PoiName);
		title.setText(companyName);

		TextView conTextView = (TextView) mPopView
				.findViewById(R.id.PoiAddress);
		conTextView.setText(Html.fromHtml(companyAddress + "<br/>电话:" + telNo));

		ImageButton rightimg = (ImageButton) mPopView
				.findViewById(R.id.ImageButtonRight);
		rightimg.setVisibility(View.GONE);
	}

	 @Override
	    protected void onPause() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	    	 */
	        mMapView.onPause();
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	    	 */
	        mMapView.onResume();
	        super.onResume();
	    }
	    
	    @Override
	    protected void onDestroy() {
	    	/**
	    	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
	    	 */
	        mMapView.destroy();
	        super.onDestroy();
	    }
	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	mMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	mMapView.onRestoreInstanceState(savedInstanceState);
	    }
}
