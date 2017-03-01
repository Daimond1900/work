package com.yifeng.adapter;

/**
 * 工单已派
 * */
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.yifeng.ChifCloud12345update.R;
import com.yifeng.ChifCloud12345update.YiFlowActivity;


public class GdzpAdapter extends SimpleAdapter{
	public int count = 8;
	Context context;
	List<Map<String, Object>> data;
	Resources res;

	public GdzpAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		final String transaction_id = (String) data.get(position).get("TRANSACTION_ID"); //流程ID 
		final Button flow = (Button) view.findViewById(R.id.my_lc);//流程
		flow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						YiFlowActivity.class);
				intent.putExtra("transaction_id", transaction_id);
				intent.putExtra("sin", "y");
				context.startActivity(intent);
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
