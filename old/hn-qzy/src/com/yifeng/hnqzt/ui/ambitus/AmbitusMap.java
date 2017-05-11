package com.yifeng.hnqzt.ui.ambitus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Color;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.data.EnterpriseDal;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.ui.mapabc.BMapApiApp;
import com.yifeng.hnqzt.ui.mapabc.MyItemizedOverlay;
import com.yifeng.hnqzt.ui.mapabc.MapInfoActivity.MyLocationListenner;
import com.yifeng.hnqzt.util.CommonUtil;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.StringHelper;

/***
 * 周边信息
 * @author wujiahong
 * 2012-10-25
 */
public class AmbitusMap extends BaseActivity{
	private Button back_btn,screen_btn;
	private MapView mMapView;
	private GeoPoint point;
	private MapController mMapController;
	private List<OverlayItem> geoList;
	private  float gps_marker_CENTER_X;
	private  float gps_marker_CENTER_Y;
	private  int zoom=3000;
	private  View mPopView;
	private int selfLongitude=110804172,slng=110804172;//经度
    private int selfLatitude=19549153,slat=19549153;//纬度
    private CommonUtil commonUtil;
    private List<Map<String,String>> companys=new ArrayList<Map<String,String>>();
    private EnterpriseDal enterpriseDal;
    private String positionName="",price="",prices="";
    private TextView nav_title;
    private final int SELECT=20;
    private boolean isResult=false;
    private String areaName=""; 
    private LocationListener mLocationListener = null;// onResume时注册此listener，onPause时需要Remove
   
    
	   @Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//this.setMapMode(MAP_MODE_VECTOR);//设置地图为矢量模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
		
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.ambitus_map);
		
		 
		
		commonUtil=new CommonUtil(this);
		enterpriseDal=new EnterpriseDal(this);
		nav_title=(TextView)findViewById(R.id.nav_title);
		
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AmbitusMap.this.finish();
			}
		});
		screen_btn=(Button)findViewById(R.id.screen_btn);
		screen_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent bl=new Intent(AmbitusMap.this,Search.class);
				bl.putExtra("lat", selfLatitude);
				bl.putExtra("lng", selfLongitude);
				bl.putExtra("slat", slat);
				bl.putExtra("slng", slng);
//				bl.putExtra("areaName", areaName.equals("")?"我的位置":areaName);
				bl.putExtra("price", price.equals("")?"全部":price);
				bl.putExtra("prices",prices);
				bl.putExtra("positionName", positionName);
				bl.putExtra("limit", zoom);
				startActivityForResult(bl, SELECT);
			}
		});
		
		mMapView = (MapView) findViewById(R.id.main_mapView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		
		//默认扬州市中心五指山阁
		//经度坐标：119.43357806
		//纬度坐标：32.39442487
		//selfLongitude=(int)(119.43357806*1E6);
		//selfLatitude=(int)(32.39442487*1E6);
		
		
		point = new GeoPoint(selfLatitude,selfLongitude);  //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);  //默认设置地图中心点
		
			
		//添加OverlayItem 点击效果
		mPopView = View.inflate(this, R.layout.popup, null);  
		mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,  
		        MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));  
		mPopView.setVisibility(View.GONE); 
		
	    //绘制附近公司地图
		//drawItemOverlay();
		
		//线程请求数据
		//showProg("正在获取周边招聘信息,请稍等...");
		
		//自己位置
		mLocationOverlay = new MyLocationOverlay(  mMapView);
	    mMapView.getOverlays().add(mLocationOverlay);
		initLogic( );
	
	
	}
		// 定位相关
		LocationClient mLocClient;
		LocationData locData = null;
		public MyLocationListenner myListener = new MyLocationListenner();
		private MyLocationOverlay mLocationOverlay = null; // 定位图层
		boolean flag=true;
		/**
		 * 定位SDK监听函数
		 */
		public class MyLocationListenner implements BDLocationListener {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;

				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				// 如果不显示定位精度圈，将accuracy赋值为0即可
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
			 
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				// 如果不显示定位精度圈，将accuracy赋值为0即可
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
//				// 更新定位数据
//				mLocationOverlay.setData(locData);
//				// 更新图层数据执行刷新后生效
//				mLocationOverlay.setMarker(null);
//				mMapView.refresh();

				 
			    selfLongitude=(int) (location.getLongitude()*1E6);
				selfLatitude=(int) (location.getLatitude()*1E6);
				if(!isResult){
					 if(flag){//第一次才执行
					   flag=false;
						 selfLongitude=(int) (location.getLongitude()*1E6);
						 slng=selfLongitude;
						 
						 selfLatitude=(int) (location.getLatitude()*1E6);
						 slat=selfLatitude;
						 
						 mHandler.sendEmptyMessage(1);
					 }
				 }
				mLocClient.stop();
			}

			public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null) {
					return;
				}
			}
		}
	private void initLogic( ){
		
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
	  /**
     * 绘制圆，该圆随地图状态变化
     * @return 圆对象
     */
    public Graphic drawCircle() {
     	
	   	GeoPoint pt1 = new GeoPoint(selfLatitude,selfLongitude);
	   	
	   	//构建圆
  		Geometry circleGeometry = new Geometry();
  	
  		//设置圆中心点坐标和半径
  		circleGeometry.setCircle(pt1, zoom);
  		//设置样式
  		Symbol circleSymbol = new Symbol();
 		Symbol.Color circleColor = circleSymbol.new Color();
 		circleColor.red = 131;
 		circleColor.green = 182;
 		circleColor.blue = 222;
 		circleColor.alpha = 100;
  		circleSymbol.setSurface(circleColor,1,3);
  		//生成Graphic对象
  		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
  		return circleGraphic;
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
			  		super.onDestroy();
			  	  if (mLocClient != null)
						mLocClient.stop();
					mMapView.destroy();
				
			}
		
/***
 * 定位自己的位置
 * 
 */
 private void drawSelf(){
	    
	 
	        //初始获取自己的位置 :经纬度
			//point = new GeoPoint((int) (32.38801132515847 * 1E6),
		    //	(int) (119.457947015380 * 1E6));  //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
			
		 
			  
			try{
		        point = new GeoPoint(selfLatitude,selfLongitude); //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		        if(point!=null){
					mMapController.setCenter(point);  //设置地图中心点
					GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
					mMapView.getOverlays().clear();
					  mMapView.getOverlays().add(mLocationOverlay);
					// 更新定位数据
					mLocationOverlay.setData(locData);
					// 更新图层数据执行刷新后生效
					mLocationOverlay.setMarker(null);
				     mMapView.getOverlays().add(graphicsOverlay);
				   //添加圆
				        graphicsOverlay.setData(drawCircle());
				        mMapView.refresh();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
 } 
 
 /****
  * 画所有附近公司地图
  */
 private void drawItemOverlay(){
	       try{
		        //标注位置
				Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);//得到需要标在地图上的资源
			 
				
				MyItemizedOverlay myItemizedOverlay=new MyItemizedOverlay(this, mMapView, mPopView, marker, geoList);
				
				myItemizedOverlay.addItem(geoList );
				myItemizedOverlay.setCompany(companys);
				
				mMapView.getOverlays().add(myItemizedOverlay);
				mMapController.setCenter(point);  //设置地图中心点
		  }catch(Exception e){
			  e.printStackTrace();
		  }
 }
 
  
  Handler mHandler =new Handler(){
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		 if(msg.what==1){
			
			drawSelf();
		    
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
					   Thread.sleep(200);
					   companys=enterpriseDal.
							   loadMaps(selfLatitude,selfLongitude,zoom,positionName, prices.equals("全部")?"":prices);
					   mHandler.sendEmptyMessage(2);
					}catch(Exception e){
						e.printStackTrace();
						mHandler.sendEmptyMessage(-1);
					}
				}
			}).start();
			
		 }
		 if(msg.what==2){
			 addOverlayItem();
			 drawItemOverlay();
			 //progressDialog.dismiss();
		 }
		 
		 
	};  
  };
		
  /**
   * 设数据源		
   */
  private void addOverlayItem(){
	     geoList= new ArrayList<OverlayItem>();
	     String state=companys.get(0).get("state");
	     mPopView.setVisibility(View.GONE);
	     if(state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
		     String msg="";
		     for (int i = 0; i < companys.size(); i++) {
		    	    if(i==0)
		    	    msg=companys.get(i).get("msg");
				    
		    	    String lat=companys.get(i).get("lat");//纬度
				    String lng=companys.get(i).get("lng");//经度
				    
				    int lats = (int) (Double.parseDouble(lat) * 1E6);
				    int lngs = (int)(Double.parseDouble(lng) * 1E6);
				    
				   // GeoPoint gPoint=new GeoPoint(StringHelper.getLatitude(lat),StringHelper.getLongitude(lng));
				    GeoPoint gPoint=new GeoPoint(lats,lngs);
				    
				    
				    geoList.add(new OverlayItem(gPoint, companys.get(i).get("aab004"), companys.get(i).get("aae006")));
				    
			 }
		     commonUtil.shortToast(msg);
		     
	     }else{
	    	 commonUtil.shortToast("没有找到周边招聘信息!");
	     }
    }
  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==SELECT){
			if(data!=null){
			  
			  String navStr="";
			 // areaName=data.getStringExtra("areaName");
			  positionName=data.getStringExtra("positionName");
			  int limit=data.getIntExtra("limit", zoom); 
			 
			  price=data.getStringExtra("price");
			  prices=data.getStringExtra("prices");
			  
//			  if(!areaName.equals("")){
				  navStr=areaName;
				  selfLongitude=data.getIntExtra("lng",0);
				  selfLatitude=data.getIntExtra("lat",0);
				  navStr+="("+limit+")米内"+" "+prices;
				  
//			  }
			  if(!positionName.equals("")){
				  navStr+=" "+positionName;
			  }
			  if(!navStr.equals("")){
				  nav_title.setText(navStr);
			  }
			  
			  //如果区域职位和薪资不为空重新加载
//			  if(!positionName.equals("")||!price.equals("")){
				  
				  mMapView.getOverlays().clear();//先清掉地图上所有的标记
				  
				  zoom=limit; 
				  
				  //showProg("正在定位绘制周边信息,请稍等...");
				  isResult=true;
				  mHandler.sendEmptyMessage(1);
				  //new Thread(loadrunnable).start();
				  
//			  }
			  //mMapView.getOverlays().remove(point);
			 
			}
		}
	}
 
	
 
}
