package com.yifeng.hnzpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yifeng.hnzpt.R;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private static final int[] ids = { R.drawable.banner2, R.drawable.banner2, R.drawable.banner2 };

	public ImageAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ids.length; // 返回很大的值使得getView中的position不断增大来实现循环。
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int id = position;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.advertisement_item, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.imgView);
			img.setImageResource(ids[position]);
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("第 ===" + id + " ===张图!");
				}
			});
		}

		return convertView;
	}

}
