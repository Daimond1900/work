package com.yifeng.hnqzt.ui.mapabc;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKStep;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.StringHelper;
/***
 * 地图显示公司详细信息
 * @author wujiahong
 * 2012-10-22
 */
public class MapInfoActivity extends BaseActivity{
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point,selfPoint;
	public static final int FIRST_LOCATION=1002;
	public static final int LOADROUTE=1115;
	private Button back_btn;
	private  View mPopView;
	private int selfLongitude=119433578;//自己经度 
    private int selfLatitude=32394424;//自己纬度
	private String longitude="110.7932116";//经度
    private String latitude="19.545220";//纬度
	private String  companyName="", companyAddress="",telNo="";
	private Button loadRouteBtn,coloseRouteBtn,refresh_btn,selfBtn;
	private TextView showRoteTxt;
	private RelativeLayout showRouteView;
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	private MyLocationOverlay mLocationOverlay = null; // 定位图层
	private MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapabc_view);
		// 地图初始化 MyLocationMapView{41e75798 V.E...C. ......I. 0,0-0,0 #7f0a0006 app:id/bmapView}
			 
			
		
		MyClick onclick=new MyClick();
		
		showRouteView=(RelativeLayout)findViewById(R.id.showRouteView);
		showRoteTxt=(TextView)findViewById(R.id.showRoteTxt);
		
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);
		
		selfBtn=(Button)findViewById(R.id.selfBtn);
		selfBtn.setOnClickListener(onclick);
		
		loadRouteBtn=(Button)findViewById(R.id.loadRouteBtn);
		loadRouteBtn.setOnClickListener(onclick);
		
		coloseRouteBtn=(Button)findViewById(R.id.coloseRouteBtn);
		coloseRouteBtn.setOnClickListener(onclick);
		
		refresh_btn=(Button) findViewById(R.id.refresh_btn);
		refresh_btn.setOnClickListener(onclick);
		refresh_btn.setVisibility(View.GONE);
		
		Intent bl=this.getIntent();
		
		longitude=bl.getStringExtra("longitude")==null?"110.79321162":bl.getStringExtra("longitude");//经度
		latitude=bl.getStringExtra("latitude")==null?"19.54522009":bl.getStringExtra("latitude");//纬度
		
		
		companyName=bl.getStringExtra("companyName");
		companyAddress=bl.getStringExtra("companyAddress");
		telNo=bl.getStringExtra("telNo");
		
		mMapView = (MapView) findViewById(R.id.main_mapView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		
		int lng=(int)(Double.parseDouble(longitude)*1E6);
		int lat=(int)(Double.parseDouble(latitude)*1E6);
		point=new GeoPoint(lat,lng);
		// 添加定位图层
		mLocationOverlay = new MyLocationOverlay(  mMapView);
		mMapView.getOverlays().add(mLocationOverlay);
		 //point=new GeoPoint(StringHelper.getLatitude(latitude),StringHelper.getLongitude(longitude));
		
		mMapController.setCenter(point);
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
		
		 
		
	    //定位公司位置
		addPopView();
		
		
		//线程定位查找自己的位置
		//showProg("正在获取当前位置,请稍等...");
		//new Thread(loadRunnable).start();
		
		initSearch( );
		
		
	}
	public MyLocationListenner myListener = new MyLocationListenner();

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
			locData.accuracy = 0;
			locData.direction = 0;
//			if (location == null)
//				return;
//			locData.latitude = location.getLatitude();
//			locData.longitude = location.getLongitude();
//			// 如果不显示定位精度圈，将accuracy赋值为0即可
//			locData.accuracy = location.getRadius();
//			locData.direction = location.getDerect();
			// 更新定位数据
			mLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mLocationOverlay.setMarker(null);
			mMapView.refresh();

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
		    selfLongitude=(int) (location.getLongitude()*1E6);
			selfLatitude=(int) (location.getLatitude()*1E6);
			selfPoint = new GeoPoint(selfLatitude,selfLongitude); //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
			SearchButtonProcess(selfPoint);
			selfBtn.setVisibility(View.VISIBLE);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
	private void initSearch( ){
		 // 初始化搜索模块，注册事件监听  
        mSearch = new MKSearch();
        mSearch.init(((BMapApiApp)this.getApplication()).mBMapMan , new MKSearchListener(){

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(MapInfoActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//自驾规划方案
				String resultMsg="<br/>&nbsp;<b><font color='red'>您的线路信息如下:</font></b><br/>";
				int count=res.getNumPlan();
				MKRoute route=res.getPlan(0).getRoute(0);
				int c=route.getNumSteps();
				System.out.println("方案:======="+count+"线路:======="+c);
				for (int i = 0; i < c; i++) {
					MKStep sp=route.getStep(i);
					System.out.println("："+sp.getContent()+"   坐标"+sp.getPoint());
					resultMsg+=(i+1)+":"+sp.getContent();
				}
				
				RouteOverlay routeOverlay = new RouteOverlay(MapInfoActivity.this, mMapView);
				// 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    mMapView.getController().animateTo(res.getStart().pt);
			    
			    //显示线路信息
		    	showRoteTxt.setText(Html.fromHtml(resultMsg));
				
			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(MapInfoActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				TransitOverlay  routeOverlay = new TransitOverlay (MapInfoActivity.this, mMapView);
				
				String resultMsg="<br/>&nbsp;<b><font color='red'>您的线路信息如下:</font></b><br/>";
				
				//返回路线起点
				MKPlanNode start=res.getStart();
				System.out.println("起始点:"+start.name+"坐标:"+start.pt);
				//返回路线终点
				
				MKPlanNode end=res.getEnd();
				System.out.println("终止点:"+end.name+"坐标:"+end.pt);
				//返回方案数目
				int count=res.getNumPlan();
				System.out.println("方案条目:"+count);
				for (int i = 0; i <count; i++) {
					MKTransitRoutePlan routePlan=res.getPlan(i);//取得当前方案
					
			        System.out.println("方案"+(i+1)+"描述:"+routePlan.getContent()+"" +
			        		"\n距离:"+routePlan.getDistance()+"米\n起点坐标:"+routePlan.getStart()+"" +
			        				"\n终点坐标"+routePlan.getEnd());
			        
					int mklineCount=routePlan.getNumLines();//返回方案包含的公交线路段数 
					
			        for (int j = 0; j < mklineCount; j++) { 
			        	MKLine line=routePlan.getLine(j);
			        	System.out.println("公交车线路"+(j+1)+":总共有"+line.getNumViaStops()+"站 "+line.getTitle()+"" +
								"\n距离:"+line.getDistance()+"米\n 经过的所有点"+line.getPoints());
						MKPoiInfo  off=line.getGetOffStop();//获取下车的站点
						System.out.println("终点站:"+off.address+"/"+off.name);
						MKPoiInfo  on=line.getGetOnStop();//获取上车的站点
						System.out.println("起点站:"+on.address+"/"+on.name);
						
						
						resultMsg+="<br/><br/>起点站:"+on.name+" <br/>乘坐"+line.getTitle()+"" +
								"<br/>经过"+line.getNumViaStops()+"站到达"+off.name;
					}
			        
			        
				}
			    
				//显示线路信息
		    	showRoteTxt.setText(Html.fromHtml(resultMsg));
				
				
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    mMapView.getController().animateTo(res.getStart().pt);
				
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(MapInfoActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String resultMsg="<br/>&nbsp;<b><font color='red'>您的线路信息如下:</font></b><br/>";
				//步行方案
				int count=res.getNumPlan();
				MKRoute route=res.getPlan(0).getRoute(0);
				int c=route.getNumSteps();
				System.out.println("方案:======="+count+"线路:======="+c);
				for (int i = 0; i < c-1; i++) {
						MKStep sp=route.getStep(i);
						System.out.println("："+sp.getContent()+"   坐标"+sp.getPoint());
						
						resultMsg+="<font color='red'>"+(i+1)+":</font>&nbsp;"+sp.getContent()+"<br/><br/>";
				}
				
				RouteOverlay routeOverlay = new RouteOverlay(MapInfoActivity.this, mMapView);
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    
			    //显示公司
			    addPopView();
			    mMapView.getOverlays().add(mLocationOverlay);
			    
			    //mMapView.getController().animateTo(res.getStart().pt);
			    
			    //显示线路信息
		    	showRoteTxt.setText(Html.fromHtml(resultMsg));
		    	
				
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}
        	
        });
	}
	
	
	boolean flag =false;
	private void SearchButtonProcess(GeoPoint startPoint) {
		if(flag) return;
		flag=true;
		//工人新村
		//GeoPoint startPoint=new GeoPoint((int)(32.383422*1e6),(int)(119.448143*1e6) );
		//汽车西站
		//GeoPoint endPoint=new GeoPoint((int)(32.373931*1e6), (int)(119.407027*1e6));
		
		int lng=(int)(Double.parseDouble(longitude)*1E6);
		int lat=(int)(Double.parseDouble(latitude)*1E6);
		GeoPoint endPoint=new GeoPoint(lat,lng);
		
		//GeoPoint endPoint =new GeoPoint(StringHelper.getLatitude(latitude),StringHelper.getLongitude(longitude));
		
		MKPlanNode stPointNode = new MKPlanNode();
		stPointNode.pt=startPoint;
		MKPlanNode enPointNode = new MKPlanNode();
		enPointNode.pt=endPoint;
		
		mSearch.walkingSearch("海南", stPointNode, "海南", enPointNode);
		 
	}
	
	
	 
	
	
	
   class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				MapInfoActivity.this.finish();
				break;
			case R.id.loadRouteBtn://显示线路
					showRouteView.setVisibility(View.VISIBLE);
					loadRouteBtn.setVisibility(View.GONE);
				break;
			case R.id.coloseRouteBtn://关闭线路层
				showRouteView.setVisibility(View.GONE);
				loadRouteBtn.setVisibility(View.VISIBLE);
				break;
			case R.id.refresh_btn://刷新
				
				break;
			case R.id.selfBtn:
				  mMapController.animateTo(selfPoint);//移动到我的位置
				
				break;
			default:
				break;
			}
		}
		
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
	//定位公司位置
	private void addPopView(){
		
//		mMapView.getOverlays().add(new MyOverlay());
		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red);//得到需要标在地图上的资源

		ItemizedOverlay<OverlayItem> lay=	new ItemizedOverlay<OverlayItem>(marker,mMapView) ;
		OverlayItem item=	new OverlayItem(point,"","");
		item.setAnchor( OverlayItem.ALING_CENTER);
		lay.addItem(item);
		mMapView.getOverlays() .add(lay);
		
		if(mPopView!=null)
		mMapView.removeView(mPopView);
		
		//添加冒泡框
		mPopView = View.inflate(this, R.layout.popup, null); 
		mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,  
						        MapView.LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.BOTTOM_CENTER));  
		TextView title = (TextView) mPopView.findViewById(R.id.PoiName);			
		title.setText(companyName);		
	
		TextView conTextView = (TextView) mPopView.findViewById(R.id.PoiAddress);
		conTextView.setText(Html.fromHtml(companyAddress+"<br/>电话:"+telNo));
		
		LinearLayout LinearLayoutPoi=(LinearLayout)mPopView.findViewById(R.id.LinearLayoutPoi);
		LinearLayoutPoi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPopView.setVisibility(View.GONE);
			}
		});
		ImageButton rightimg=(ImageButton) mPopView.findViewById(R.id.ImageButtonRight);
		rightimg.setVisibility(View.GONE);
	}
	
	
 
	
 
 

}

