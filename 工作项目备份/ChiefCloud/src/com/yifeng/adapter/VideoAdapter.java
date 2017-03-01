package com.yifeng.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.R;
import com.yifeng.util.AsyncImageLoader;
import com.yifeng.util.AsyncImageLoader.ImageCallback;
import com.yifeng.util.CommonUtil;

/**
 * 在线视频适配器
 * 
 * @author WuJiaHong
 *
 */
public class VideoAdapter extends SimpleAdapter {
	public int count = 10;
	private Activity context;
	private List<Map<String, Object>> data;
	private AsyncImageLoader asyncImageLoader;
	private int[] colors = new int[] { 0xefefefef, 0x30A0A0A0 };
	private ListView listView;
	private CommonUtil commonUtil;

	public VideoAdapter(Activity context, List<Map<String, Object>> data, int resource, String[] from, int[] to,
			ListView listView) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		commonUtil = new CommonUtil(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		View view = super.getView(position, convertView, parent);

		/*
		 * int colorPos = position % this.colors.length;
		 * view.setBackgroundColor(this.colors[colorPos]);
		 */

		// 播放视频
		ImageView play = (ImageView) view.findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String video_url = "";
				if (data.get(index).get("URL") != null) {
					video_url = (String) data.get(index).get("URL");
				}
				if (!video_url.equals("")) {
					// CommonUtil commonUtil =new CommonUtil(context);
					// if(commonUtil.isWifi()!=1){
					Intent intent = new Intent(Intent.ACTION_VIEW);
//					String type = "video/* ";
					Uri uri = Uri.parse(video_url);
//					intent.setDataAndType(uri, type);
					intent.setData(uri);
					context.startActivity(intent);
					// }else{
					// commonUtil.shortToast("必须是3G网才可以播放!");
					// }
				} else {
					commonUtil.shortToast("无效的视频路径!");
				}
			}
		});

		// 查看详细
		TextView look_info = (TextView) view.findViewById(R.id.content);
		String content = "";
		if (data.get(position).get("CONTENT") != null) {
			content = (String) data.get(position).get("CONTENT");
			if (content.length() > 60) {
				content = content.substring(0, 60) + "...";
			}
		}
		look_info.setText(Html.fromHtml(content));

		/*
		 * look_info.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent bl=new
		 * Intent(context,VideoInfo.class); bl.putExtra("title_name", "视频详细");
		 * bl.putExtra("url", "http://www.baidu.com");
		 * context.startActivity(bl);
		 * 
		 * } });
		 */

		return view;
	}

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if (data != null && (view instanceof ImageView) && (data instanceof String)) {

				String imageUrl = (String) data;
				ImageView imageView = (ImageView) view;
				imageView.setTag(imageUrl);

				view.setTag(imageUrl);

				Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
						ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(imageDrawable);
							VideoAdapter.this.notifyDataSetChanged();
						}
					}
				});
				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.icon);
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
}
