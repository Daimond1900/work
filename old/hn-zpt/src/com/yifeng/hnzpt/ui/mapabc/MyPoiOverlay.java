package com.yifeng.hnzpt.ui.mapabc;
//package com.yifeng.zpt.ui.mapabc;
//
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.PoiOverlay;
//import com.yifeng.zpt.R;
//
//public class MyPoiOverlay extends PoiOverlay {
//	private Context context;
//	private Drawable drawable;
//	private int number = 0;
//	private List<PoiOverlay> poiItem;
//	private LayoutInflater mInflater;
//	private int height;
//
//	public MyPoiOverlay(Context context, Drawable drawable, List<PoiItem> poiItem) {
//		super(drawable, poiItem);
//		this.context = context;
//		this.poiItem = poiItem;
//		mInflater = LayoutInflater.from(context);
//		height = drawable.getIntrinsicHeight();
//	}
//
//
//	@Override
//	protected View getPopupView(final PoiItem item) {
//		View view = mInflater.inflate(R.layout.popup, null);
//		TextView nameTextView = (TextView) view.findViewById(R.id.PoiName);
//		TextView addressTextView = (TextView) view.findViewById(R.id.PoiAddress);
//		nameTextView.setText(item.getTitle());
//		String address = item.getSnippet();
//		if (address == null || address.length() == 0) {
//			address = "中国";
//		}
//		addressTextView.setText(address);
//		LinearLayout layout = (LinearLayout) view.findViewById(R.id.LinearLayoutPopup);
//		layout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 实现onClick事件
//			}
//		});
//
//		return view;
//	}
//
//
//	@Override
//	protected boolean onTap(int index) {
//		number = index;
//		return super.onTap(index);
//	}
//
//}