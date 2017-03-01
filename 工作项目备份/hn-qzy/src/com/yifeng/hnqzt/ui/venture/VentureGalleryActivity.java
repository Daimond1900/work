package com.yifeng.hnqzt.ui.venture;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.ViewGalleryAdapter;
import com.yifeng.hnqzt.data.VentureDAL;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.util.ConstantUtil;

import java.util.*;

/**
 * comments:创业项目
 * @author WuJiaHong
 * Date: 2012-10-25
 */
public class VentureGalleryActivity extends BaseActivity 
{
	private TextView titleTxt;
	private Button backBtn,listBtn;
	private ViewGalleryAdapter viewdapter;
	private Gallery gallery;
	private List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> returnData=new ArrayList<Map<String,Object>>();
	private TextView pageLab;
	private int pageNum = 0,currentPage=0; 
	private boolean isLoading=false,isFinish=false;//是否正在加载数据  isFinish如果为true所有数据加载完成
    private VentureDAL ventureDal;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venture_gallery);
		ventureDal=new VentureDAL(this);
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(this.getIntent().getStringExtra("title"));
		pageLab=(TextView)findViewById(R.id.pageLab);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				VentureGalleryActivity.this.finish();
			}
		});
		
		listBtn=(Button)findViewById(R.id.listBtn);
		listBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent bl=new Intent(VentureGalleryActivity.this,VentureListActivity.class);
				bl.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(bl);
			}
		});
		
		gallery=(Gallery)findViewById(R.id.gallery);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				pageLab.setText(Html.fromHtml("当前第 <font color='red'>"+(arg2+1)+"</font> 条"));
				
				if(arg2>currentPage){//如果向右滑动重新加载数据
					
					if(!isLoading&&!isFinish){//如果当前线程不在加载才执行，或未加载完成的数据
						loadView();
						isLoading=true;
					}
					
				}
				currentPage=arg2;//分页标志
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					Intent bl=new Intent(VentureGalleryActivity.this,VentureDetailActivity.class);
					bl.putExtra("title", data.get(arg2).get("title")+"");
					bl.putExtra("fromSource", data.get(arg2).get("fromSource")+"");
					bl.putExtra("pubshTime", data.get(arg2).get("pubshTime")+"");
					bl.putExtra("content", data.get(arg2).get("content")+"");
					bl.putExtra("pic_url", data.get(arg2).get("pic_url")+"");
					startActivity(bl);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
		
		loadView();
	}
	
	//请求数据源
	private void loadView(){
		
		//pageLab.setText("正在加载数据,请稍后....");
		
		if (pageNum == 0)
		{
		    data.clear();
		    viewdapter = new ViewGalleryAdapter(this, data, R.layout.venture_view1, 
		    		new String[]{"title","fromSource","pubshTime","content","pic_url"}, 
		    		new int[]{R.id.title,R.id.fromSource,R.id.timeTxt,R.id.content,R.id.pic}, null);
		    viewdapter.isHtml=true;
			gallery.setAdapter(viewdapter);
			viewdapter.setViewBinder();
	    }
		
		new Thread(loadRunnable).start();
	}
	
    /***设置数据源 json如下
	 * author 来源
	 * articleid 主键
	 * content 内容
	 * pic_url 图片地址
	 * pubtime 发布时间
	 * title 标题
	 */
	private void setGalleryView()
	{
		
		pageNum++;
		
		String state = (String) returnData.get(0).get("state");
		
        if(state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
           
        	if(returnData.size()<10){
        	   isFinish=true;//数据加完成
           }
           
           for (int i = 0; i < returnData.size(); i++) {
        	    Map<String,Object> map=returnData.get(i);
        	    map.put("fromSource", "来源："+map.get("author"));
        	    map.put("pubshTime", "时间："+map.get("pubtime"));
			    data.add(map); 
		   }
           
           pageLab.setText(Html.fromHtml("当前第 <font color='red'>"+(currentPage+1)+"</font> 条"));
           
           viewdapter.notifyDataSetChanged();
          
        }else{
          commonUtil.shortToast("没有加载到数据");
        }
		
		
	}
	
	Runnable loadRunnable=new Runnable() {
		
		@Override
		public void run() {
			try{
				Thread.sleep(100);
				returnData=ventureDal.doQuery(pageNum,"");
				mhandler.sendEmptyMessage(1);
			}catch(Exception e){
				e.printStackTrace();
				mhandler.sendEmptyMessage(-1);
			}
		}
	};
	
	Handler mhandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				setGalleryView();
				isLoading=false;
			}
		}
	};

}

