package com.yifeng.ChifCloud12345.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.android.common.logging.Log;
import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.MainPageActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.adapter.VideoAdapter;
import com.yifeng.data.VideoDal;
import com.yifeng.util.ActivityStackControlUtil;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 在线视频列表
 * 
 * @author wujiahong
 * 
 */
public class VideoList extends BaseActivity implements OnScrollListener {
	private ListView listview;
	private VideoAdapter adapter;
	private LinearLayout top;
	private Button back;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private TextView top_title;
	private int lastItem = 0;
	private int pageNum = 0;
	private ListViewUtil util;
	private VideoDal viedeoDal;
	private List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
    private String title="",type="0";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_list);
		viedeoDal=new VideoDal(this);
		
	    top_title=(TextView) findViewById(R.id.top_title);
	    Intent bl=this.getIntent();
	    title=bl.getStringExtra("title")==null?"":bl.getStringExtra("title");
	    type=bl.getStringExtra("type")==null?"":bl.getStringExtra("type");
	    
	    top_title.setText(title);
	    
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		listview = (ListView) findViewById(R.id.listview);   
		util = new ListViewUtil(this, listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				  Intent bl=new Intent(VideoList.this,VideoInfo.class);
				  bl.putExtra("video_id",(String)list.get(position).get("VIDEO_ID"));
				  startActivity(bl);
			}
		});

		doSetListView();
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		startActivity(new Intent(VideoList.this,MainPageActivity.class));
	}
	

	private void doSetListView() {
		if (pageNum == 0) {
			list.clear();
			util.addFootBar();
			if(type.equals("1")){//高清
			 adapter = new VideoAdapter(this, list, R.layout.video_gq_item, new String[] {
					"LOGO","TITLE", "UPLOAD_TIME",  
					"CONTENT","LENGTH" }, new int[] {R.id.video_type,R.id.video_title, R.id.time,
					   R.id.count,R.id.long_time},listview);
		    }else{//切片
		     adapter = new VideoAdapter(this, list, R.layout.video_item, new String[] {
							"TITLE", "UPLOAD_TIME",  
							"CONTENT","LENGTH" }, new int[] {R.id.video_title, R.id.time,
							   R.id.count,R.id.long_time},listview);
		    }

		    adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		new Thread(gorupRunnable).start();
	}
	
	/**
	 * 加载信息详细
	 */
	private Runnable gorupRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(200);
				returnList=viedeoDal.doQury(pageNum,type);
				Message msg=new Message();
				msg.what=0;
				gorupHandler.sendMessage(msg);
				
			} catch (Exception e) {
			  e.printStackTrace();
			}
		}
	};
	Handler gorupHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
			 addData();
			}
		}
	};

	private void addData() {
		
		if (pageNum == 0) {
			list.clear();
		}
		pageNum++;
		
		String state = returnList.get(0).get("state");
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				util.removeFootBar();
			for (int i = 0; i < returnList.size(); i++) {
				Map map=returnList.get(i);
				if(map.get("UPLOAD_TIME")!=null){
				 map.put("UPLOAD_TIME", DateUtil.getDate((String)map.get("UPLOAD_TIME")));
				}
				if(map.get("LOGO")!=null){
					 map.put("LOGO",getString(R.string.ipconfig)+map.get("LOGO"));
				}
				if(map.get("LENGTH")!=null){
					 map.put("LENGTH","片长:"+map.get("LENGTH"));
				}
				list.add(map);
			}
		}else{
			util.showListAddDataState(state);
		}
		
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onScroll(AbsListView v, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView v, int state) {

		if (lastItem == adapter.count
				&& state == OnScrollListener.SCROLL_STATE_IDLE) {
			doSetListView();
		}
	}
}