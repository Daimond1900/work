package com.yifeng.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.yifeng.ChifCloud12345update.R;

/**
 * 
 *  
 * 
 */
public class Assigndapter extends SimpleAdapter {

	public int count = 8;
	private int[] colors = new int[] { 0xe7e7e7e7, 0xe7e7e7e7 };

	Context context;
	List<Map<String, Object>> data;
	Resources res;

	public Assigndapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
	}

	public Handler replayHandler;// 回复handler
	public Handler appleHandler; // 签收handler
	public Handler viewHanler;// 详情handler

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		final Button apply = (Button) view.findViewById(R.id.apply);// 签收
		final Button repaly = (Button) view.findViewById(R.id.repaly);// 回复
		final Button view1 = (Button) view.findViewById(R.id.view);// 详情
		String ASSIGN_STATUS = (String) data.get(position).get("ASSIGN_STATUS");// 0为未签收
																				// 1为签收
		final String ASSIGN_ID = (String) data.get(position).get("ASSIGN_ID");// 领导批示id
		final String FORM_ID = (String) data.get(position).get("FORM_ID");//工单id
		if (ASSIGN_STATUS.equals("0")) {
			apply.setText("签收");
			apply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Message msg;
					msg = new Message();

					Bundle data = new Bundle();
					data.putString("assign_id", ASSIGN_ID);
					msg.setData(data);
					appleHandler.sendMessage(msg);
					apply.setEnabled(false);
					apply.setText("已签");
				}
			});
		} else {
			apply.setText("已签");
			apply.setEnabled(false);
		}

		
		
		repaly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Message msg;
				msg = new Message();

				Bundle data = new Bundle();
				data.putString("assign_id", ASSIGN_ID);
				msg.setData(data);
				replayHandler.sendMessage(msg);
				repaly.setEnabled(false);
			}
		});
		
		view1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg;
				msg = new Message();
				Bundle data = new Bundle();
				data.putString("form_id", FORM_ID);
				msg.setData(data);
				viewHanler.sendMessage(msg);
			}
		});

		return view;
	}

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
