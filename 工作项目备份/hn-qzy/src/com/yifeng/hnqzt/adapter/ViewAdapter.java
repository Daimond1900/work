
package com.yifeng.hnqzt.adapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.util.CommonUtil;
/**
 * 创业平台数据显示
 * @author WuJiaHong
 * 2012-10-22
 */
public class ViewAdapter extends BaseAdapter {
	private Activity context;
	private  CommonUtil  comutil;
	private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	public Button news_btn,meeting_btn,notice_btn;
   public ViewAdapter(Activity context) {
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
        
        public void clearData(){
        	list.clear();
        }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	   if (convertView == null) {
		 convertView=(View)this.getItemView(position).get("layout");
		 //TextView title=(TextView)convertView.findViewById(R.id.title);
		 //title.setText(list.get(position).get("title")+"");
		 System.out.println("当前第====="+position+"==条记录");
		}
	    return convertView;
	}
	
	//每一个板面按钮单击事件
	private void setOnclick(final Button btn,final Intent intent){
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 context.startActivity(intent);
			}
		});
	}
	
}
