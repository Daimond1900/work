package com.yifeng.hnzpt.adapter;

import java.util.List;
import java.util.Map;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.util.AsyncImageLoader;
import com.yifeng.hnzpt.util.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @comment:
 * @author:ZhangYan
 * @Date:2012-10-22
 */
public class PolicyAdapter extends SimpleAdapter {
	public int count = 10;
	private Context context;
	private List<Map<String, Object>> data;
	private AsyncImageLoader asyncImageLoader;
	private boolean isImg = false;
	private ListView listView;
	public boolean other = false;

	public PolicyAdapter(Activity activitys, List<Map<String, Object>> datas, int resource, String[] from, int[] to, ListView listViews) {
		super(activitys, datas, resource, from, to);
		context = activitys;
		data = datas;
		listView = listViews;
		asyncImageLoader = new AsyncImageLoader(activitys);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView policy_title = (TextView) view.findViewById(R.id.policy_title);
		policy_title.setText(data.get(position).get("policy_title").toString());

		TextView policy_content = (TextView) view.findViewById(R.id.policy_content);
		policy_content.setText(data.get(position).get("policy_content").toString());

		TextView policy_time = (TextView) view.findViewById(R.id.policy_time);
		policy_time.setText(data.get(position).get("policy_time").toString());
		return view;
	}

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			// 当url时执行
			if (data != null && (view instanceof ImageView) && (data instanceof String)) {
				String imageUrl = (String) data;// 下载地址
				ImageView imageView = (ImageView) view;
				imageView.setTag(imageUrl);
				String tag = imageUrl;
				view.setTag(imageUrl);
				Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
						ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(imageDrawable);
							PolicyAdapter.this.notifyDataSetChanged();
						}
					}
				}, tag);
				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.noneimg);
				} else {
					imageView.setImageDrawable(cachedImage);
				}

				return true;
			} else if (data != null & (view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}

	public boolean isImg() {
		return isImg;
	}

	public void setImg(boolean isImg) {
		this.isImg = isImg;
	}

}