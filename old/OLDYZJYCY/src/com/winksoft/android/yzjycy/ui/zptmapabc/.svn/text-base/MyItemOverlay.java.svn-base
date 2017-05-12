package com.winksoft.android.yzjycy.ui.zptmapabc;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;

public class MyItemOverlay extends ItemizedOverlay<OverlayItem>{

	private View mPopView;
	private MapView mMapView;
	private Drawable marker;
	private Activity activity;
	
	public MyItemOverlay(Drawable arg0, MapView arg1,GeoPoint geopint,Activity activity) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		this.mMapView = arg1;
		this.marker = arg0;
		this.activity = activity;
	}
	

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		Projection projection = mapView.getProjection();
		for (int index = size() - 1; index >= 0; index--) {
			// 遍历GeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item
			String title = overLayItem.getTitle();
			// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null);

		}
	}
	
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(activity,"弹出了框", Toast.LENGTH_SHORT).show();
		return super.onTap(arg0, arg1);
	}
	
	@Override
	protected boolean onTap(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(activity,"关闭了框", Toast.LENGTH_SHORT).show();
		return super.onTap(arg0);
	}

}
