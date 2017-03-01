package com.winksoft.yzsmk.ftp;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winksoft.yzsmk.czpos.R;

public class MyBaseAdapter1 extends BaseAdapter {
	
	private List<Map<String, Object>> list1;
	private int source;	 
	private LayoutInflater inflater;	 
	
	public MyBaseAdapter1(Context context,List<Map<String, Object>> list , int source){
		this.list1 = list;
		this.source = source;
 
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list1.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list1.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	//ȡ�ô����Ŀ��view����
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 * arg0	��ǰ��Ŀ��Ҫ�󶨵��������Ŀ�е�����ֵ
	 * 
	 */
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		TextView tvcontent = null;
		TextView tvtype = null;
		TextView tvinsertdate = null;
		
 
		if(arg1 == null){
			 
			arg1 = inflater.inflate(source, null);
			
			tvcontent = (TextView)arg1.findViewById(R.id.tvcontent);
			tvtype = (TextView)arg1.findViewById(R.id.tvtype);
			tvinsertdate = (TextView)arg1.findViewById(R.id.tvinsertdate);
			
			ViewCache cache = new ViewCache();
			
			cache.tvcontent = tvcontent;
			cache.tvtype = tvtype;
			cache.tvinsertdate = tvinsertdate;
			
			 
			arg1.setTag(cache);
		}
		else{
			ViewCache cache = (ViewCache)arg1.getTag();
			tvcontent = cache.tvcontent;
			tvtype = cache.tvtype;
			tvinsertdate = cache.tvinsertdate;
		}
		Map map = list1.get(arg0);
		tvcontent.setText(map.get("content").toString());
		tvtype.setText(map.get("type").toString());
		tvinsertdate.setText(map.get("insertdate").toString());
		
		return arg1;
	}
	
	private final class ViewCache{
		public TextView tvcontent;
		public TextView tvtype;
		public TextView tvinsertdate;
	}

}
