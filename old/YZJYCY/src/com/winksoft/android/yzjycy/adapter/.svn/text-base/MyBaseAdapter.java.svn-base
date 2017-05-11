package com.winksoft.android.yzjycy.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
/**
 * 
 * @author user_ygl
 *
 */
public abstract class MyBaseAdapter extends SimpleAdapter{
	private List<? extends Map<String, ?>> data;
	/**
	 * 
	 * @param context  
	 * @param data
	 * @param resource  xm文件
	 * @param from   对应的id
	 * @param to    对应的data数据
	 */
	public MyBaseAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.data=data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		iniview(view, position, data);
		return view;
	} 
	@Override
	public int getCount() {
		return data.size();
	}
	protected abstract void iniview(View view,int position,List<? extends Map<String, ?>> data);

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
