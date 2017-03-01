package com.yifeng.hnqzt.ui.venture;

import java.util.HashMap;
import java.util.Map;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.ViewAdapter;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.widget.CircleFlowIndicator;
import com.yifeng.hnqzt.widget.ViewFlow;

/**
 * comments:创业项目
 * @author
 * Date: 2012-9-3
 */
public class VentureActivity extends BaseActivity
{
	private TextView titleTxt;
	private Button backBtn;
	private ViewFlow viewFlow;
	private ViewAdapter viewdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venture);
		viewdapter = new ViewAdapter(this);
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(this.getIntent().getStringExtra("title"));

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				VentureActivity.this.finish();
			}
		});
		loadView();
	}

	private void loadView()
	{

		LayoutInflater inflater = LayoutInflater.from(this);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < 20; i++) {
			map = new HashMap<String, Object>();
			
			View layout = (View) inflater.inflate(R.layout.venture_view1, null);
			TextView title=(TextView)layout.findViewById(R.id.title);
 			title.setText("花木园艺场"+i);
			
 			map.put("layout", layout);
			map.put("itemId", "view"+i);
			viewdapter.addItemView(map);
		}
		
		viewFlow = (ViewFlow) findViewById(R.id.viewflow1);
		//viewFlow.isSendHandler=true;
		//viewFlow.sendHandler=mhandler;
		viewFlow.setAdapter(viewdapter);
		

		//CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic1);
		//viewFlow.setFlowIndicator(indic);
	}
	
	Handler mhandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.getData()!=null){
  		     int index=msg.getData().getInt("currentIndex")+1;
    	     commonUtil.shortToast("总条数=="+viewdapter.getCount()+"当前条数:"+index);	
    	     
    	     if(index==viewdapter.getCount()){
    	    	//新加载 数据
    	    	 Map<String, Object> map = new HashMap<String, Object>();
    	    	 
    	    	 for (int i = 0; i < 5; i++) {

     	 			map = new HashMap<String, Object>();
     	 			
    	 			View layout = (View) View.inflate(VentureActivity.this,R.layout.venture_view1, null);
    	 			TextView title=(TextView)layout.findViewById(R.id.title);
    	 			title.setText("花木园艺场"+i);
    	 			
    	 			map.put("itemId", "view"+i);
    	 			map.put("layout", layout);
    	 			viewdapter.addItemView(map);
    	 			
    	 		}
    	    	 viewdapter.notifyDataSetChanged();
    	     }
    		}
		};
	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}

}

