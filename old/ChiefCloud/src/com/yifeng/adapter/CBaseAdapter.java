package com.yifeng.adapter;
/**
 * 自定义适配器
 * @author 吴家宏
 * 2011年09月01日
 *
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
public class CBaseAdapter extends BaseAdapter {
	protected Context context; 
	protected List<Map<String, String>> list;
	public CBaseAdapter(Context context) {
		this.context=context;
		list = new ArrayList<Map<String, String>>();
	}

	public int getCount() {
		return list.size();
	}
	
	public Map<String,String> getItem(int position) {
		if(position<0)
			position=0;
		return list.get(position);
	}
	
	public long getItemId(int position) {
		if(position<0)
			position = 0;
		return position;
	};
	
	
    
    /**
	 *  添加数据
	 * @param shop
	 */
	public void addData(Map<String, String> map){
		list.add(map);
	}
	
	/**
	 * 添加数据
	 * @param list
	 */
	public void setData(List<Map<String,String>> list){
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				addData(list.get(i));
			}
		}
	}
	
	/**
	 * 获取数据集
	 * @param list
	 */
	public List<Map<String,String>> getData(){
		return this.list;
	}
	
	/**
	 * 清空
	 */
	public void removeData(){
		this.list.clear();
	}
	 /***
	  * 返回视图
	  */
	protected int[] colors = new int[] {  0x30000000, 0x30A0A0A0  }; 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
