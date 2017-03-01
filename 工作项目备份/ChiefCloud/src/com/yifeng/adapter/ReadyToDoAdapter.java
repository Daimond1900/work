package com.yifeng.adapter;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

/**
 * 
 *  
 * 
 */
public class ReadyToDoAdapter extends SimpleAdapter {

	public int count = 8;
	private int[] colors = new int[] { 0xe7e7e7e7, 0xe7e7e7e7 };

	public ReadyToDoAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);

	}

	public int selectedPosition = -1;
	public int chockPosition = -2;
   @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if (selectedPosition == position) {
			view.setBackgroundColor(Color.BLUE);
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
			if (data != null & (view instanceof ImageView)
					& (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}

}
