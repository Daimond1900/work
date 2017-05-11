package com.yifeng.ChifCloud12345update;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yifeng.adapter.ReadyToDoAdapter;
import com.yifeng.data.ReadyToDoDal;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
/**
 * 政策动态列表
 * @author Administrator
 *
 */
public class PolicyActivty extends BaseActivity {
	private ListView listview;
	private ReadyToDoAdapter adapter;
	private LinearLayout top;
	private Button back;
	private String name;
	private List<Map<String, String>> list;
	private ReadyToDoDal readyToDo;
	private Thread thread;
	private ProgressDialog progressDialog ;
	private Map<String,String> param;
	private TextView dt_title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zcdt);
		readyToDo=new ReadyToDoDal(this);
		list = new ArrayList<Map<String, String>>();
		dt_title=(TextView)findViewById(R.id.dt_title);
	    listview = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PolicyActivty.this.finish();
			}
		});
		Intent intent=getIntent();
		name=intent.getStringExtra("name");
		param=new HashMap<String,String>();
		param.put("policy_id",intent.getStringExtra("id"));
		dt_title.setText(name+"政策动态");
		
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		startThread();
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu1, R.drawable.bottom_menu1_);
	}
	private void showwait(){
		 progressDialog = ProgressDialog.show(this, null, "正在处理请稍后...", true);
	}
	/**
	 * 启动线程
	 */
	private void startThread(){
		showwait();
		thread=new Thread(mRunnable);
		thread.start();
	}
		/**
	     * 加载数据线程
	     */
	   private Runnable mRunnable=new Runnable(){
	     	public void run(){
	     		try {
	 				Thread.sleep(100);
	 				mHandler.sendMessage(mHandler.obtainMessage());
	 			} catch (InterruptedException e) {
	 				//System.out.println("Error-"+e.getMessage());
	 			}
	     	}
	     };
	     
	     Handler mHandler = new Handler() {
	    	 public void handleMessage(Message msg){
	    		 super.handleMessage(msg);
	    		loadData();
	    		progressDialog.dismiss();
	    		//thread.stop();
	    		
	    	 }
	     };
   private void loadData() {
	   list=readyToDo.doQuery(param);
	   if(list.get(0).get("state").equals(String.valueOf(ConstantUtil.SERVER_ERROR))){
       	 this.commonUtil.showToast(getString(R.string.server_error_msg));
		}
       else if(list.get(0).get("state").equals(String.valueOf(ConstantUtil.KEY_ERROR))){
      	 this.commonUtil.showToast(getString(R.string.login_time_out));
		}
       else if(list.get(0).get("state").equals(String.valueOf(ConstantUtil.INNER_ERROR))){
      	 this.commonUtil.showToast(getString(R.string.inner_error_msg));
		}
      
	   else if(list.get(0).get("state").equals(String.valueOf(ConstantUtil.IS_EMPTY))){
			 this.commonUtil.showToast(getString(R.string.empty_error_msg));
       }
	   else{
		    List<Map<String,String>> vlist=new ArrayList<Map<String,String>>();
		    for(int i=0;i<list.size();i++){
		    	Map<String,String> map=list.get(i);
		    	map.put("SUB_DATE", DateUtil.getDate(map.get("SUB_DATE")));
		    	vlist.add(map);
		    }
		    adapter = new ReadyToDoAdapter(this, vlist, R.layout.zcdt_item,
					new String[] {  "SUB_CONTENT", "SUB_DATE", "SUB_TITLE" },
					new int[] {   R.id.content, R.id.time,R.id.title});
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
	   }
	}
}