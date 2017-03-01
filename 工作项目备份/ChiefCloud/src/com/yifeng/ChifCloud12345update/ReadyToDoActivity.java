package com.yifeng.ChifCloud12345update;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.yifeng.adapter.ReadyToDoAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.util.ConstantUtil;
/**
 * 政策动态
 * @author wujiahong
 *
 */
public class ReadyToDoActivity extends BaseActivity {
	private ListView listview;
	private ReadyToDoAdapter adapter;
	private LinearLayout top;
	private Button back;
	private List<Map<String, String>> list;
	private FormDAL readyToDo;
	private Thread thread;
	private ProgressDialog progressDialog ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbsx);
		readyToDo=new FormDAL(this);
		
		listview = (ListView) findViewById(R.id.listview);
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		list= new ArrayList<Map<String, String>>();
		
		
		
		startThread();
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
//				Intent intent = new Intent(ReadyToDoActivity.this,
//						PolicyActivty.class);
//				intent.putExtra("id", (String)list.get(position).get("ID"));
//				intent.putExtra("name", (String)list.get(position).get("POLICY_NAME"));
//			    startActivity(intent);
				Intent intent = new Intent(ReadyToDoActivity.this,
						ForWebViewActivty.class);
				intent.putExtra("title_name", "政策法规");
				intent.putExtra("url","android/policy/doGetPolicyDetail"+"?key="+user.getKey()+"&address="+URLEncoder.encode(list.get(position).get("ADDRESS")));
				startActivity(intent);
			}
		});
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu1, R.drawable.bottom_menu1_);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
	   list=readyToDo.doGetPolicyList();
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
		    adapter = new ReadyToDoAdapter(this, list, R.layout.dilog_item,
					new String[] { "TITLE" }, new int[] { R.id.content });
			adapter.setViewBinder();
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
	   }
	}
}