package com.yifeng.ChifCloud12345update;
import java.util.Collections;
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

import com.yifeng.adapter.MyDepartmentComparator;
import com.yifeng.adapter.ReadyToDoAdapter;
import com.yifeng.data.ReadyToDoDal;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DataConvert;

/**
 * 超时工单所管辖部门
 * 
 * @author Administrator
 */
public class OrverTimeDeptActivity extends BaseActivity {
	private ListView listview;
	private ReadyToDoAdapter adapter;
	private LinearLayout top;
	private Button back;
	private ReadyToDoDal todoDal;
	private List<Map<String, String>> list;
	private ProgressDialog progressDialog ;
	private Thread thread;
	private TextView title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldps);
		todoDal=new ReadyToDoDal(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ldps);
		listview = (ListView) findViewById(R.id.listview);
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		title=(TextView)findViewById(R.id.title);
		title.setText("超时工单");
		startThread();
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(OrverTimeDeptActivity.this,
						OverTimeTaskActivity.class);
				intent.putExtra("ORG_NAME",
						(String) list.get(position).get("ORG_NAME"));
				intent.putExtra("ORG_ID",
						(String) list.get(position).get("ORG_ID"));
				intent.putExtra("JY_COUNT",
						(String) list.get(position).get("JY_COUNT"));
				intent.putExtra("TS_COUNT",
						(String) list.get(position).get("TS_COUNT"));
				intent.putExtra("ZX_COUNT",
						(String) list.get(position).get("ZX_COUNT"));
				intent.putExtra("QZ_COUNT",
						(String) list.get(position).get("QZ_COUNT"));
				intent.putExtra("JB_COUNT",
						(String) list.get(position).get("JB_COUNT"));
				startActivity(intent);
			}
		});
		
		back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrverTimeDeptActivity.this.finish();
			}
		});
	
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
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
	    		addData();
	    		progressDialog.dismiss();
	    		//thread.stop();
	    		
	    	 }
	     };
	void addData() {
		   Map<String,String> map=new HashMap<String,String>();
		   map.put("user_id", user.getUserId());
		   map.put("type", "2");
		   map.put("role_id", user.getRole_type());
		   try {
				String json = todoDal.doMyDept(map);
				Map<String, String> m = DataConvert.toMap(json);
				if(m.get("success") !=null && m.get("success").equals("false")){
					commonUtil.shortToast(m.get("msg"));
					return;
				}
				if (m.get("state")
						.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
					this.commonUtil.showToast(getString(R.string.server_error_msg));
				}
				else if (m.get("state")
						.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
					this.commonUtil.showToast(getString(R.string.inner_error_msg));

				} else if (m.get("state")
						.equals(String.valueOf(ConstantUtil.LOGIN_FAIL))) {
					this.commonUtil.shortToast("没有更多数据。");
				}
				else if (m.get("state")
						.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
					list = DataConvert.toArrayList(m.get("orgs"));
					if(list!=null && list.size()>0){
						MyDepartmentComparator comparator = new MyDepartmentComparator();
						Collections.sort(list, comparator);
						adapter = new ReadyToDoAdapter(this, list, R.layout.dbsx_item,
								new String[] { "ORG_NAME", "COUNTS" }, new int[] {
										R.id.content, R.id.count });
						listview.setAdapter(adapter);
					}else{
						this.commonUtil.shortToast("没有更多数据。");
					}
				}

			} catch (Exception e) {
				this.commonUtil.showToast(getString(R.string.fail_msg));
			}
	}
}