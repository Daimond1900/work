package com.winksoft.android.yzjycy.adapter;

import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.FinalBitmap;

/**
 * 公用适配器
 */
public class CommonAdapter extends SimpleAdapter{
	private Activity activity;
	private List<? extends Map<String, ?>> list;
	private  Bitmap defaultBmp=null,loadingBmp=null;
	FinalBitmap fb;
	private ListView listView;
	public int count = 10,ret=0,tag=0,shareindex=0;
	Resources res;
	public boolean isHtml = false;
	

	public CommonAdapter(Activity activity,int resource, String[] from, int[] to,
			List<? extends Map<String, ?>> list) {
		super(activity, list, resource, from, to);
		this.list = list;
		this.activity = activity;
		fb=FinalBitmap.create(activity);
		//默认图片
		Resources res=activity.getResources();
		defaultBmp=BitmapFactory.decodeResource(res, R.drawable.noneimg);
		loadingBmp=BitmapFactory.decodeResource(res, R.drawable.nopic);
	}
	
	public CommonAdapter(Activity context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, ListView listView) {
		super(context, data, resource, from, to);
		this.activity = context;
		this.list = data;
		this.listView = listView;
		
//		asyncImageLoader=new AsyncImageLoader(context);
		
        //mImageAsynLoader = new AsynImageLoader(new Handler());
		
	}
	
	public CommonAdapter(Activity context, List<Map<String, Object>> data, int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.activity = context;
		this.list = data;
		this.res = res;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		//setOnclick(view, new Intent(this.activity,CommonDetail.class),position);
		if (isHtml) {
			TextView itemTag = (TextView) view.findViewById(R.id.item_tagTxt);
			String tag = list.get(position).get("sta").toString().trim();
			if ("0".equals(tag)) {
				itemTag.setText(Html.fromHtml("<font color = red>未处理</font>"));
			} else if ("1".equals(tag)) {
				itemTag.setText(Html.fromHtml("<font color = #4DA0D4>已拒绝</font>"));
			} else if ("2".equals(tag)) {
				itemTag.setText(Html.fromHtml("<font color = #4DA0D4>待面试</font>"));
			} else {
				itemTag.setText(Html.fromHtml("<font color = #4DA0D4>已录用</font>"));
			}
		}

		return view;
	}
	
	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,String textRepresentation) {
			if (data != null && (view instanceof ImageView)
					& (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			if (data != null && (view instanceof ImageView)
					& (data instanceof Integer)) {
				ImageView iv = (ImageView) view;
				Integer bm = (Integer) data;
				//iv.setBackgroundResource(bm);
				iv.setImageResource(bm);
				return true;
			}
			if (data != null && (view instanceof ImageView)& (data instanceof String)) {
				/*ImageView iv = (ImageView) view;
				String bm = (String) data;
				FinalBitmap fb=FinalBitmap.create(activity);*/
				//fb.display(iv, bm);
				//默认图片
				String imageUrl = Constants.IP+ (String) data;
				ImageView imageView = (ImageView) view;
				fb.display(imageView, imageUrl,loadingBmp,defaultBmp);
				
//			    fb.display(iv, bm, loadingBmp,defaultBmp);
				
				return true;
			}
			  if (data != null && (view instanceof TextView)){
				((TextView) view).setText(Html.fromHtml((String) data) );
		
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
		this.list = data;
		notifyDataSetChanged();
	}
	
	
	

}
