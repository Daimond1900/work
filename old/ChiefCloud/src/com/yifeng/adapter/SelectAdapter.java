package com.yifeng.adapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.R;
public class SelectAdapter extends BaseAdapter {
	private Context context; 
	private List<Map<String, String>> list;
	public SelectAdapter(Context context) {
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
	
	
	 private int[] colors = new int[] { 0x30FFFFFF, 0x30A0A0A0 }; 
	 
	
	  public View getView(int position, View convertView, ViewGroup parent) {
    	final int index=position;
		View linearLayout=LayoutInflater.from(context).inflate(R.layout.selectuser_item, null);
    	Map<String,String> cvo=list.get(position);
		TextView title=(TextView)linearLayout.findViewById(R.id.username);
		title.setText(cvo.get("FULLNAME")==null?"":(String)cvo.get("FULLNAME"));
		CheckBox check_btn=(CheckBox)linearLayout.findViewById(R.id.question_check);
		check_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				 Map<String,String>map=getItem(index);
			     if(isChecked){
			         map.put("selected", "1");
			       }else{
			    	 map.put("selected", "0");	       
			     }
			}
		});
		
		/*if(position%2==0){
			  linearLayout.setBackgroundDrawable(this.context.getResources().getDrawable(R.drawable.list_item));
	    }*/
		int colorPos = position % colors.length;   
		linearLayout.setBackgroundColor(colors[colorPos]);  
		
		String selected=list.get(index).get("selected")==null?"0":list.get(index).get("selected");
		if(selected.equals("1")){
			check_btn.setChecked(true);
		}
    	return linearLayout;
	  }
    
    
	public void addData(Map<String, String> map){
		list.add(map);
	}
	

	public void setData(List<Map<String,String>> list){
		this.list=list;
	}
	
	
	public List<Map<String,String>> getData(){
		return this.list;
	}
	
	
	public void removeData(){
		this.list.clear();
	}
	
}
