package com.yifeng.hnjcy.adapter;

import java.util.List;
import java.util.Map;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

/**
 * 
 *  
 * 
 */
public class HomeAdapter extends SimpleAdapter {
	Context context;
	List<Map<String, Object>> data;
	Resources res;

	public HomeAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
	}

	public boolean need1 = false;// 图片显示
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (need1) {

		}
		
		return view;
	}

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if (data != null && (view instanceof ImageView)
					&& (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}

}
