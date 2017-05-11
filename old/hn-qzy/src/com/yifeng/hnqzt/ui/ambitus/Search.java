package com.yifeng.hnqzt.ui.ambitus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.StringHelper;

import java.util.*;

public class Search extends BaseActivity implements OnClickListener,OnSeekBarChangeListener{
	private Button backBtn,screen_btn,reset_btn;
	private TableRow duty_item,price_item;
	private SeekBar mSeekBar; 
	private TextView ambitus_lab;
	private CommonAdapter adpater;
	private List<Map<String,Object>> areaData,priceData;
	private ListView listView;
	private TextView areaLab,priceLab;
	private String lng="0",lat="0",areaName="",positionName="",price="",slng="",slat="";
	private int limit=0;
	private EditText ponsition;
	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ambitus_search);
		
		ponsition=(EditText)findViewById(R.id.ponsition);
		
		
		price_item=(TableRow)findViewById(R.id.price_item);
		price_item.setOnClickListener(this);
		
		mSeekBar=(SeekBar)findViewById(R.id.seek);
		mSeekBar.setOnSeekBarChangeListener(this);
		
		ambitus_lab=(TextView)findViewById(R.id.ambitus_lab);
		
		backBtn=(Button)findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		
		screen_btn=(Button)findViewById(R.id.screen_btn);
		screen_btn.setOnClickListener(this);
		
		reset_btn=(Button)findViewById(R.id.reset_btn);
		reset_btn.setOnClickListener(this);
		
		priceLab=(TextView)findViewById(R.id.priceLab);
		
		Intent bl=this.getIntent();
		mSeekBar.setProgress(bl.getIntExtra("limit", 3000));
		ambitus_lab.setText(bl.getIntExtra("limit", 3000)+"米|");
		ponsition.setText(bl.getStringExtra("positionName")==null?"":bl.getStringExtra("positionName"));
		//areaLab.setText(bl.getStringExtra("areaName").equals("")?"我的位置":bl.getStringExtra("areaName"));
		//priceLab.setText(bl.getStringExtra("price").equals("")?"全部":bl.getStringExtra("price"));
		priceLab.setText(bl.getStringExtra("prices").equals("")?"全部":bl.getStringExtra("prices"));
		lng=bl.getIntExtra("lng", 0)+"";
		lat=bl.getIntExtra("lat", 0)+"";
		
		slng=bl.getIntExtra("slng", 0)+"";
		slat=bl.getIntExtra("slat", 0)+"";
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			Search.this.finish();
			break;
		case R.id.screen_btn:
			
			positionName=ponsition.getText().toString();
			limit=mSeekBar.getProgress();
			
			Intent bl=new Intent();
			bl.putExtra("lng", StringHelper.getLongitude(lng));
			bl.putExtra("lat", StringHelper.getLatitude(lat));
			bl.putExtra("positionName", ponsition.getText().toString());
			bl.putExtra("price", price);
			bl.putExtra("prices", priceLab.getText().toString());
			bl.putExtra("limit", limit);
			setResult(Activity.RESULT_OK, bl);
			Search.this.finish();
			break;
		case R.id.price_item:
			showMsg("请选薪资",1);
			break;
		case R.id.reset_btn:
			limit=3000;
			mSeekBar.setProgress(limit);
			
			//areaLab.setText("我的位置");
			lng=slng;
			lat=slat;
			
			ponsition.setText("");//职位
			positionName="";
			
			priceLab.setText("全部");//薪资
			price="";
			break;
		default:
			break;
		}
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		ambitus_lab.setText(progress+"米内|");
		
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.v("滑条", "开始移动");
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.v("滑条", "停止移动");
	}
	
	/***
	 * 设置
	 * @param type
	 */
	private void setListViewData(int type){
		if(type==0){
			areaData=new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("lng", slng);
			map.put("lat", slat);
			map.put("title", "我的位置");
			areaData.add(map);
			
		    map=new HashMap<String, Object>();
			map.put("lng", "119440206");
			map.put("lat", "32400357");
			map.put("title", "扬州市");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119438222");
			map.put("lat", "32400655");
			map.put("title", "广陵区");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119404833");
			map.put("lat", "32382808");
			map.put("title", "邗江区");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "11940439677");
			map.put("lat", "3235115935");
			map.put("title", "开发区");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "11939529872");
			map.put("lat", "32424653");
			map.put("title", "维扬区");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119365431");
			map.put("lat", "33247197");
			map.put("title", "宝应县");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119191014");
			map.put("lat", "32278116");
			map.put("title", "仪征市");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119467666");
			map.put("lat", "32786743");
			map.put("title", "高邮市");
			areaData.add(map);
			
			map=new HashMap<String, Object>();
			map.put("lng", "119577121");
			map.put("lat", "32440142");
			map.put("title", "江都市");
			areaData.add(map);
		   
		  adpater=new CommonAdapter(this, areaData, R.layout.template_spinner_item, new String[]{"title"}, new int[]{R.id.item_lab}, listView);
		  listView.setAdapter(adpater);
		  
		}
		if(type==1){
	      priceData=new ArrayList<Map<String,Object>>();
		  for(int i=1;i<10;i++){
				   Map<String,Object> map=new HashMap<String, Object>();
				   map.put("id", i+"000");
				   map.put("title",i+"000-"+(i+3)+"000");
				   priceData.add(map);
		  }
		  adpater=new CommonAdapter(this, priceData, R.layout.template_spinner_item, new String[]{"title"}, new int[]{R.id.item_lab}, listView);
		  listView.setAdapter(adpater); 
		}
	}
	
	/***
	 * 选择框
	 * @param title
	 * @param msg
	 * @param type
	 */
	public void showMsg(String title,final int type) {
		final Dialog builder = new Dialog(this, R.style.dialog);
		builder.setContentView(R.layout.template_spinner);
		
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		ptitle.setText(title);
		
		listView = (ListView) builder.findViewById(R.id.listview);
		setListViewData(type);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			    if(type==0){
			    	areaLab.setText(areaData.get(arg2).get("title").toString());
			    	areaName=areaData.get(arg2).get("title").toString();
			    	lng=areaData.get(arg2).get("lng").toString();
			    	lat=areaData.get(arg2).get("lat").toString();
			    }
			    if(type==1){
			    	priceLab.setText(priceData.get(arg2).get("title").toString());
			    	price=priceData.get(arg2).get("id").toString();
			    }
				builder.dismiss();
			}
			
		});
	    builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}
}
