package com.yifeng.hnqzt.ui.mapabc;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;

/***
 * 根据给定的坐标点进行画
 * 起始位置到终点位置的线
 * 
 * @author 吴家宏
 * 2012-10-26
 */
public class DrawLineOverLay extends Overlay {
	private List<GeoPoint> points;
	private Paint paint;

	/**
	 * 构造函数，使用GeoPoint List构造Polyline
	 * 
	 * @param points
	 */
	public DrawLineOverLay(List<GeoPoint> points) {
		this.points = points;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAlpha(150);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(4);
	}

//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//		if (!shadow) {// 不是绘制shadow层
//			Projection projection = mapView.getProjection();
//			if (points != null && points.size() >= 2) {// 画线
//				Point start = new Point();
//				projection.toPixels(points.get(0), start);// 需要转换坐标
//				for (int i = 1; i < points.size(); i++) {
//					Point end = new Point();
//					projection.toPixels(points.get(i), end);
//					canvas.drawLine(start.x, start.y, end.x, end.y, paint);// 绘制到canvas上即可
//					start = end;
//				}
//			}
//		}
//	}
	
	/**   
     * 解析返回xml中overview_polyline的路线编码   
     *    
      * @param encoded   
     * @return   
     */    
    public static List<GeoPoint> decodePoly(String encoded) {   
    
        List<GeoPoint> poly = new  ArrayList<GeoPoint>();   
         int  index =  0 , len = encoded.length();   
         int  lat =  0 , lng =  0 ;   
    
         while  (index < len) {   
            int  b, shift =  0 , result =  0 ;   
             do  {   
                b = encoded.charAt(index++) - 63 ;   
                result |= (b & 0x1f ) << shift;   
                 shift += 5 ;   
            } while  (b >=  0x20 );   
             int  dlat = ((result &  1 ) !=  0  ? ~(result >>  1 ) : (result >>  1 ));   
             lat += dlat;   
    
             shift = 0 ;   
             result = 0 ;   
            do  {   
                b = encoded.charAt(index++) - 63 ;   
                 result |= (b & 0x1f ) << shift;   
                 shift += 5 ;   
             } while  (b >=  0x20 );   
             int  dlng = ((result &  1 ) !=  0  ? ~(result >>  1 ) : (result >>  1 ));   
             lng += dlng;   
    
             GeoPoint p = new  GeoPoint(( int ) ((( double ) lat / 1E5) * 1E6),   
                  (int ) ((( double ) lng / 1E5) * 1E6));   
            poly.add(p);   
         }   
   
        return  poly;   
     }   
}
