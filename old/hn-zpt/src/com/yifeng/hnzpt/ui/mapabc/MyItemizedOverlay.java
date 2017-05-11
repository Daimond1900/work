package com.yifeng.hnzpt.ui.mapabc;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.ui.enterprise.EnterpriseActivity;

/**
 * 在地图上添加自定义标签
 * @author wujiahong 
 * 2012-10-16
 */
public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> implements OnClickListener {
	public List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Activity mContext;
	private View mPopView;
	private MapView mMapView;
	private boolean mBShow;
	private Bitmap gps_marker = null;
	private List<Map<String, String>> companys = new ArrayList<Map<String, String>>();
	private String companyId = "";

	public MyItemizedOverlay(Activity context, MapView mapView, View pView, Drawable marker, List<OverlayItem> geoList) {
		super(marker, mapView);
		this.marker = marker;
		this.mContext = context;
		this.mMapView = mapView;
		this.mPopView = pView;
		this.GeoList = geoList;
		// 画中心圆
		gps_marker = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.drawable.da_marker_red)).getBitmap();
		//populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		
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
	protected OverlayItem createItem(int i) {
		return GeoList.get(i);
	}

	@Override
	public int size() {
		return GeoList.size();
	}

	@Override
	// 处理当点击事件
	protected boolean onTap(int i) {

		if (mPopView != null && mMapView != null) {
			Log.e("", "go");

			TextView title = (TextView) mPopView.findViewById(R.id.PoiName);
			title.setText(GeoList.get(i).getTitle());

			TextView conTextView = (TextView) mPopView.findViewById(R.id.PoiAddress);
			conTextView.setText(Html.fromHtml("详细地址:" + GeoList.get(i).getSnippet() + "<br/>联   系  人:" + companys.get(i).get("aab013") + "" + "<br/>联系电话:"
					+ companys.get(i).get("aae005")));

			companyId = companys.get(i).get("aab001");

			LinearLayout LinearLayoutPoi = (LinearLayout) mPopView.findViewById(R.id.LinearLayoutPoi);
			LinearLayoutPoi.setOnClickListener(this);

			MapView.LayoutParams geoLP = (MapView.LayoutParams) mPopView.getLayoutParams();

			geoLP.point = GeoList.get(i).getPoint();

			mMapView.updateViewLayout(mPopView, geoLP);

			showPopView(true);

		}

		return true;
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		showPopView(false);// 点以外的任意一处隐藏
		return super.onTap(point, mapView);

	}

	private void showPopView(boolean bShow) {
		if (mPopView != null) {
			if (bShow) {
				mPopView.setVisibility(View.VISIBLE);
				mBShow = true;
			} else {
				mPopView.setVisibility(View.GONE);
				mBShow = false;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LinearLayoutPoi:
			goToActivity();
			break;
		default:
			showPopView(false);
			break;
		}
	}

	private void goToActivity() {
		Intent bl = new Intent(this.mContext, EnterpriseActivity.class);
		bl.putExtra("companyId", companyId);
		this.mContext.startActivity(bl);
	}

	/***
	 * 设置数据
	 * @param list
	 */
	public void setCompany(List<Map<String, String>> list) {
		this.companys = list;
	}
}
