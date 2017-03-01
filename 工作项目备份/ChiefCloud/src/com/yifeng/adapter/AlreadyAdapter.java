package com.yifeng.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
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
import com.yifeng.ChifCloud12345update.YiFlowActivity;

public class AlreadyAdapter extends SimpleAdapter{
	public int count = 8;
	Context context;
	List<Map<String, Object>> data;
	Resources res;

	public AlreadyAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
	}
	
	public Handler jobHandler; // 签收handler
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		final Button job = (Button) view.findViewById(R.id.b_jb);//工作交办
		final String transaction_id = (String) data.get(position).get("TRANSACTION_ID"); //流程ID 
		final String form_id = (String) data.get(position).get("FORM_ID");// 工单id
		final Button flow = (Button) view.findViewById(R.id.my_lc);//流程
		
		final String ASSIGN_ID = (String) data.get(position).get("DISPATCH_ID");// 领导批示id
		final String flag =  (String) data.get(position).get("FLAG");
		if(flag.equals("2")){
			job.setVisibility(view.GONE);
		}
		
		flow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						YiFlowActivity.class);
				intent.putExtra("transaction_id",  transaction_id== null ? "" : transaction_id);
				intent.putExtra("sin", "banjie");
				intent.putExtra("form_id", form_id);
				context.startActivity(intent);
			}
		});
		job.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg;
				msg = new Message();
				Bundle data = new Bundle();
				data.putString("form_id", form_id);
				data.putString("transaction_id", transaction_id== null ? "" : transaction_id);
				data.putString("dispatch_id", ASSIGN_ID == null ? "" : ASSIGN_ID);
				data.putString("flag", flag);
				msg.setData(data);
				jobHandler.sendMessage(msg);
				//job.setEnabled(false);
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
