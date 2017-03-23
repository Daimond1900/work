package com.winksoft.nox.android.view;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.winksoft.android.yzjycy.R;
import com.yifeng.nox.android.http.FinalBitmap;

public abstract class YFBaseAdapter extends SimpleAdapter {
	FinalBitmap fb;
	private List<? extends Map<String, ?>> data;
	private Context context;
	private Bitmap defaultBmp, loadingBmp;
	Resources res;

	/**
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 *            xm文件
	 * @param from
	 *            对应的id
	 * @param to
	 *            对应的data数据
	 */
	public YFBaseAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		fb = FinalBitmap.create(context);
		this.res = res;
		defaultBmp = BitmapFactory.decodeResource(res, R.drawable.noneimg);
		loadingBmp = BitmapFactory.decodeResource(res, R.drawable.nopic);
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

	protected abstract void iniview(View view, int position,
			List<? extends Map<String, ?>> data);

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
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
				iv.setImageResource(bm);
				return true;
			}
			if (data != null && (view instanceof ImageView)
					& (data instanceof String)) {
				ImageView iv = (ImageView) view;
//				String bm = Constants.IP + (String) data;
//				fb.display(iv, bm, loadingBmp, defaultBmp);
				iv.setImageResource(getResource(data.toString()));
				return true;
			}
			if (data != null && (view instanceof TextView)) {
				((TextView) view).setText(Html.fromHtml((String) data));
				return true;
			}
			return false;
		}
	}

	/**
	 * 获取图片名称获取图片的资源id的方法
	 * 
	 * @param imageName
	 * @return
	 */
	public int getResource(String imageName) {
		int resId = context.getResources().getIdentifier(imageName, "drawable",
				context.getPackageName());
		return resId;
	}
}
