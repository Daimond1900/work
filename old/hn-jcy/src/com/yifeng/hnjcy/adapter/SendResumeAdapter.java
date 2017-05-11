package com.yifeng.hnjcy.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yifeng.hnjcy.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SendResumeAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	Context context;
	private LayoutInflater inflater = null;
	public static HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public SendResumeAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		init();
	}

	public void init() {
		state.clear();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			if (m.get("checked") != null) {
				state.put(i, true);
			}
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		CacheView cacheView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.send_resume_item, null);
			cacheView = new CacheView();
			cacheView.r_name = (TextView) convertView.findViewById(R.id.r_name);
			cacheView.r_sex = (TextView) convertView.findViewById(R.id.r_sex);
			cacheView.r_num = (TextView) convertView.findViewById(R.id.r_num);
			cacheView.r_qzgw = (TextView) convertView.findViewById(R.id.r_qzgw);
//			cacheView.r_qzqy = (TextView) convertView.findViewById(R.id.r_qzqy);
			cacheView.checkBox = (CheckBox) convertView
					.findViewById(R.id.btn_check);
			convertView.setTag(cacheView);
		} else {
			cacheView = (CacheView) convertView.getTag();
		}
		cacheView.r_name.setText(list.get(index).get("xm").toString());
		cacheView.r_sex.setText(list.get(index).get("xb").toString());
		cacheView.r_num.setText(list.get(index).get("sfzh").toString());
		cacheView.r_qzgw.setText(list.get(index).get("qzgw").toString());
//		cacheView.r_qzqy.setText(list.get(index).get("gzdq").toString());
		cacheView.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							state.put(index, isChecked);
						} else {
							state.remove(index);
						}
					}
				});
		cacheView.checkBox.setChecked((state.get(index) == null ? false
				: true));
		return convertView;
	}

	public class CacheView {
		public TextView r_name;
		public TextView r_sex;
		public TextView r_num;
		public TextView r_qzgw;
		public TextView r_qzqy;
		public CheckBox checkBox;
	}

}
