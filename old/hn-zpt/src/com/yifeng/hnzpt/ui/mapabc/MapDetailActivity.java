package com.yifeng.hnzpt.ui.mapabc;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.ui.UserSession;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName:MapDetailActivity Description：企业信息-编辑信息中公司地理位置
 * 
 * @author wujiahong Date：2012-10-25
 */
public class MapDetailActivity extends BaseActivity {
	private TextView textMsg;
	private Button backBtn, sendBtn;
	private String longitude = "";// 经度
	private String latitude = "";// 纬度
	private GeoPoint point;
	private MapController mMapController;
	private MapView mMapView;
	private View mPopView;
	private GeoPoint mpoint;
	UserSession userSession;
	ItemizedOverlay<OverlayItem> lay;

	private String companyName = "", companyAddress = "", telNo = "";
	private int selfLongitude=110804172;//经度
    private int selfLatitude=19549153;//纬度

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		userSession = new UserSession(this);
		super.onCreate(arg0);
		setContentView(R.layout.map_select);
		textMsg = (TextView) findViewById(R.id.showMsg);

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MapDetailActivity.this.finish();
			}
		});
		sendBtn = (Button) findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent bl = new Intent();
				bl.putExtra("longitude", longitude);
				bl.putExtra("latitude", latitude);
				setResult(Activity.RESULT_OK, bl);
				MapDetailActivity.this.finish();
			}
		});

		mMapView = (MapView) findViewById(R.id.main_mapView);
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		mMapController = mMapView.getController();// 得到mMapView的控制权,可以用它控制和驱动平移和缩放

		Intent bl = this.getIntent();

		companyName = bl.getStringExtra("companyName");
		companyAddress = bl.getStringExtra("companyAddress");
		telNo = bl.getStringExtra("telNo");

		// Intent bl = this.getIntent();
		//
		// longitude = bl.getStringExtra("longitude").equals("") ? "1192679470"
		// : bl
		// .getStringExtra("longitude");// 经度
		// latitude = bl.getStringExtra("latitude").equals("")? "32248012" : bl
		// .getStringExtra("latitude");// 纬度
		//
		// String companyName = bl.getStringExtra("companyName") == null ?
		// "32248012" : bl
		// .getStringExtra("companyName");// 纬度
		// System.out.println(longitude+"=============="+companyName);
		//
		// int lat = (int)(Double.parseDouble(latitude) * 1E6);
		// int lng = (int)(Double.parseDouble(longitude) * 1E6);
		//
		// point = new GeoPoint(lat,lng);

//		point = new GeoPoint((int) (32.38801132515847 * 1E6),
//				(int) (119.457947015380 * 1E6)); // 用给定的经纬度构造一个GeoPoint，单位是微度
//
//		mMapController.setCenter(point); // 设置地图中心点
//		mMapController.setZoom(14);
		// addPopView();

		point = new GeoPoint(selfLatitude,selfLongitude);  //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);  //默认设置地图中心点
		mMapController.setZoom(14);
		
		initLogic();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int actionType = ev.getAction();
		switch (actionType) {
		case MotionEvent.ACTION_UP:
			Projection proj = mMapView.getProjection();
			GeoPoint loc = proj.fromPixels((int) ev.getX(), (int) ev.getY());
			longitude = Double.toString(loc.getLongitudeE6() / 1000000.0000);
			latitude = Double.toString(loc.getLatitudeE6() / 1000000.0000);
			// longitude =loc.getLongitudeE6()+"";
			// latitude = loc.getLatitudeE6()+"";
			textMsg.setText(Html.fromHtml("您点位置为：纬度" + latitude + ",经度"
					+ longitude));
			mpoint = new GeoPoint((int) (Double.parseDouble(latitude) * 1E6),
					(int) (Double.parseDouble(longitude) * 1E6));
			addPopView();
			mMapController.setCenter(mpoint);
			mMapController.animateTo(mpoint);
		}

		return false;
	}

	// /定位公司位置
	private void addPopView() {

		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);// 得到需要标在地图上的资源
		if (lay != null) {
			mMapView.getOverlays().remove(lay);
			mMapView.refresh();
		}

		lay = new ItemizedOverlay<OverlayItem>(marker, mMapView);

		OverlayItem item = new OverlayItem(mpoint, "", "");
		item.setAnchor(OverlayItem.ALING_CENTER);
		lay.addItem(item);
		mMapView.getOverlays().add(lay);

		if (mPopView != null)
			mMapView.removeView(mPopView);

		// 添加冒泡框
		mPopView = View.inflate(this, R.layout.popup, null);
		mMapView.addView(mPopView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, mpoint,
				MapView.LayoutParams.BOTTOM_CENTER));
		TextView title = (TextView) mPopView.findViewById(R.id.PoiName);
		title.setText(companyName);

		TextView conTextView = (TextView) mPopView
				.findViewById(R.id.PoiAddress);
		conTextView.setText(Html.fromHtml(companyAddress + "<br/>电话:" + telNo));
		//
		LinearLayout LinearLayoutPoi = (LinearLayout) mPopView
				.findViewById(R.id.LinearLayoutPoi);
		LinearLayoutPoi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// /mPopView.setVisibility(View.GONE);

			}
		});
		ImageButton rightimg = (ImageButton) mPopView
				.findViewById(R.id.ImageButtonRight);
		rightimg.setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.destroy();
		super.onDestroy();
	}

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private MyLocationOverlay mLocationOverlay = null; // 定位图层
	boolean flag = true;

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			if (flag) {// 第一次才执行
				flag = false;
				selfLongitude = (int) (location.getLongitude() * 1E6);
				selfLatitude = (int) (location.getLatitude() * 1E6);
				mHandler.sendEmptyMessage(1);
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
	
	Handler mHandler =new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			 if(msg.what==1){
					point = new GeoPoint(selfLatitude,selfLongitude); // 用给定的经纬度构造一个GeoPoint，单位是微度
					mMapController.setCenter(point); // 设置地图中心点
					mMapController.setZoom(14);
			 }
		};  
	  };

	private void initLogic() {

		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
}
