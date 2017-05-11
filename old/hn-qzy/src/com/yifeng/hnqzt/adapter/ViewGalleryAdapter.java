package com.yifeng.hnqzt.adapter;

import java.util.List;
import java.util.Map;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.util.AsyncImageLoader;
import com.yifeng.hnqzt.util.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;


/***
 * 创业平台
 * @author wujiahong
 * 2012-10-25
 */
public class ViewGalleryAdapter extends SimpleAdapter {
    private AsyncImageLoader asyncImageLoader;
	public int count = 10,ret=0,tag=0,shareindex=0;
	private Activity context;
	private List<Map<String, Object>> data;
	// private AsyncImageLoader asyncImageLoader;
	private int[] colors = new int[] { 0x30000000, 0x30A0A0A0 };
	public boolean isImg = false,isHtml=false;
	private Gallery listView;
	public Handler chandler;
		public ViewGalleryAdapter(Activity context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Gallery listView) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.listView = listView;
		
		asyncImageLoader=new AsyncImageLoader(context);
		
        //mImageAsynLoader = new AsynImageLoader(new Handler());
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		final int index = position;
		if(isHtml){
		    TextView content=(TextView)view.findViewById(R.id.content);
		    content.setText(Html.fromHtml(data.get(position).get("content")+""));
		}
         
		return view;
	}
	
	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	
	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,String textRepresentation) {
			//当url时执行
			if (data != null && (view instanceof ImageView) && (data instanceof String)) {
				String imageUrl = (String) data;//下载地址
				ImageView imageView = (ImageView) view;
				imageView.setTag(imageUrl);
				String tag = imageUrl;
				view.setTag(imageUrl);
				Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						if(!imageUrl.equals("")||imageUrl!=null){
							try{
								ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
								if (imageViewByTag != null) {
									if(imageDrawable!=null){
									imageViewByTag.setImageDrawable(imageDrawable);
									ViewGalleryAdapter.this.notifyDataSetChanged();
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				},tag);
				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.nopic);
				} else {
					imageView.setImageDrawable(cachedImage);
				}

				return true;
			}else if (data != null & (view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}

	

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
