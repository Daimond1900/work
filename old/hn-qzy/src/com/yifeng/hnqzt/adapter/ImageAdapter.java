/*
 * Copyright (C) 2011 Patrik ?kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yifeng.hnqzt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yifeng.hnqzt.R;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private static final int[] ids = {R.drawable.banner, R.drawable.banner2, R.drawable.banner3};

	public ImageAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ids.length;  //返回很大的值使得getView中的position不断增大来实现循环。
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
		final int id=position;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.advertisement_item, null);
			ImageView img =(ImageView) convertView.findViewById(R.id.imgView);
			img.setImageResource(ids[position]);
			img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					   System.out.println("第 ==="+id+" ===张图!");
					}
		     }); 
		}
		
		return convertView;
	}


}
