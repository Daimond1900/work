
package com.yifeng.hngly.adapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yifeng.hngly.util.CommonUtil;
/**
 * 首页图片和滚动字幕实现
 * @author WuJiaHong
 *
 */
public class MainPicAdapter  extends BaseAdapter {
	private Activity context;
	private  CommonUtil  comutil;
	public   ImageView banner;
	public   WebView webView;
	 
	private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	
   public MainPicAdapter (Activity context) {
		this.context=context;
		comutil=new CommonUtil(context);
	}

	  @Override
	    public int getItemViewType(int position) {
	        return position;
	    }

	    @Override
	    public int getViewTypeCount() {
	        return list.size();
	    }

	    @Override
	    public int getCount() {
	        return list.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return position;
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	    
	    /**
         * 添加每项数据源
         * @param map
         */
        public void addItemView(Map<String,Object> map){
        	list.add(map);
        }
        /**
         * 获取每项数据源
         * @param ponsiton
         * @return
         */
        public Map<String,Object> getItemView(int ponsiton){
        	return list.get(ponsiton);
        }
        /**
         * 设置数据源
         * @param data
         */
        public void setData(List<Map<String,Object>> data){
        	for (int i = 0; i < data.size(); i++) {
				addItemView(data.get(i));
			}
        }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	   if (convertView == null) {
		 convertView=(View)this.getItemView(position).get("layout");
		 //Button v11_btn=(Button) convertView.findViewById(R.id.v11_btn);	
		 //if(this.getItemView(position).get("itemId").equals("view1")){//版面1
			  //banner=(ImageView)convertView.findViewById(R.id.banner);
			 
		 //}
		
		}
	    return convertView;
	}
}
