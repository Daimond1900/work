package com.yifeng.hngly.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yifeng.hngly.asyncload.AsyncImageLoader;
import com.yifeng.hngly.asyncload.AsyncImageLoader.ImageCallback;
import com.yifeng.hngly.ui.R;

public class AsyncAdapter extends SimpleAdapter {

	public int count = 8;
	private int[] colors = new int[] { 0xe7e7e7e7, 0xe7e7e7e7 };

	Context context;
	List<Map<String, Object>> data;
	Resources res;

	ListView listView;

	public AsyncAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res,
			ListView listView) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
	}

	public int selectedPosition = -1;
	public int chockPosition = -2;
	public boolean need = false;// 是否需要按钮功能
	public Handler need2;// 我的关注是否需要按钮功能
	public boolean need1 = false;// 文字转换
	public boolean need3 = false;// 图片显示
	public boolean need4 = false;// 我的关注逻辑标示
	public Handler jbHandler; // 交办
	public Handler assignHandler; // 交办回复内容
	public Handler gzHandler; // 关注
	public boolean nywy = false; // 你云我云关注功能

	public Handler noHandler; // 关注
	public boolean nocare = false; // 取消关注

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		return view;
	}

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public AsyncImageLoader asyncImageLoader;

	// int ima=1;
	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if (data != null && (view instanceof ImageView)
					&& (data instanceof String)) {

				// ima++;
				String imageUrl = (String) data;
				String tag = imageUrl;
				ImageView imageView = (ImageView) view;
				// imageView.setTag(imageUrl);
				view.setTag(tag);
				Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
						new ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {
								ImageView imageViewByTag = (ImageView) listView
										.findViewWithTag(imageUrl);
								if (imageViewByTag != null) {
									imageViewByTag
											.setImageDrawable(imageDrawable);
									AsyncAdapter.this.notifyDataSetChanged();
								}
							}
						}, tag);
				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.noneimg);
				} else {
					imageView.setImageDrawable(cachedImage);
				}

				return true;
			} else if (data != null && (view instanceof ImageView)
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
