package com.yifeng.adapter;

import com.yifeng.face.SmileyParser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class AppsAdapter extends BaseAdapter {
	Context context;

	public AppsAdapter(Context context) {
		this.context = context;
	}

	
	public int[] icons=SmileyParser.sIconIds;

	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		ImageView i;

		if (convertView == null) {
			i = new ImageView(context);
			i.setScaleType(ImageView.ScaleType.CENTER  );
			
			i.setLayoutParams(new GridView.LayoutParams(60,60));
		} else {
			i = (ImageView) convertView;
		}

		i.setImageResource(icons[position]);

		return i;
	}

	public final int getCount() {
		return icons.length;
	}

	public final Object getItem(int position) {
		return icons[position];
	}

	public final long getItemId(int position) {
		return position;
	}


}
