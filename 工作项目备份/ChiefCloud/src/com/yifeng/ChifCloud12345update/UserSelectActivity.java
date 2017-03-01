package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cmbr.web.support.LoginContext;


import com.yifeng.adapter.MyAdapterd;
import com.yifeng.adapter.Node;
import com.yifeng.adapter.SelectAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.data.OAfindUserData;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.ListViewUtil;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


public class UserSelectActivity  extends BaseActivity implements OnClickListener,OnScrollListener {
	private RadioGroup radioGroup;
	private SelectAdapter adapter;
	private String departments;
	private OAfindUserData findUserData;
	private List<Map<String, String>> list;
	private ListView listview, leftlistView;
	private String userid = "", username = "";
	private Thread thread;
	Button cancel, submit;
	EditText editText1;
	String org_id, form_id, target_user_id,transaction_id,dispatch_id,flag;
	private Map<String, String> map = new HashMap<String, String>();
	FormDAL dal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_user);
		dal = new FormDAL(this);
		editText1 = (EditText) findViewById(R.id.editText1);
		
		adapter = new SelectAdapter(this);
		findUserData = new OAfindUserData(this);

		listview = (ListView) findViewById(R.id.listview);
		list = new ArrayList<Map<String, String>>();

		thread = new Thread(questRunnable);
		thread.start();
		listview.setAdapter(adapter);

		 

		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		cancel = (Button) findViewById(R.id.cancle);
		cancel.setOnClickListener(this);
		
		transaction_id= getIntent().getStringExtra("transaction_id");
		dispatch_id= getIntent().getStringExtra("dispatch_id");
		form_id = getIntent().getStringExtra("form_id");
		flag  = getIntent().getStringExtra("flag");
	}
	
	private Runnable questRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				questHandler.sendMessage(questHandler.obtainMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	Handler questHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loadData();
			//thread.stop();
		}
	};
	
	/**
	 * 加载远程数据
	 */
	public void loadData() {
		String depId = ""; 

		list = findUserData.selectAppUser(depId,user.getUserId());
	
			if (list.get(0).get("state")
					.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {

				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("selected", "0");	// 未选中
					adapter.addData(list.get(i));
				}
			}
		departments = findUserData.listDepartment(user.getOrg_id());
		getDepa(departments, null);
		showLeft();
	}


	/***
	 * 获取选中用户
	 */
	private boolean addAssign() {
		userid="";
		username="";
		for (int i = 0; i < adapter.getData().size(); i++) {
			if (adapter.getItem(i).get("selected").equals("1")) {
				userid += adapter.getItem(i).get("USERID") + ",";
				username += adapter.getItem(i).get("FULLNAME") + ",";
			}
		}
		if (!userid.equals("")) {
			map.put("form_id", form_id);
			map.put("user_id", user.getUserId());
			map.put("transaction_id", transaction_id);
			map.put("dispatch_id", dispatch_id);
			map.put("target_user_id", userid);
			map.put("dispatch_comment", editText1.getText().toString());
			map.put("flag", flag);
			
			String state =  dal.doAddDispatch(map,"android/dispatch/doAddDispatch");
			if (state.equals("SUCCESS")){
				return true;
			}
			else{
				return false;
			}
		} else
			Toast.makeText(this, "先选择处理人。", Toast.LENGTH_SHORT).show();
		return false;
		
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.submit:
			try {
				if (addAssign()) {
					Intent intent = new Intent();
					setResult(Activity.RESULT_OK, intent);
					finish();
				}

			} catch (Exception e) {
				Intent intent = new Intent();
				setResult(3, intent);
				finish();
			} finally {

			}
			break;
		case R.id.cancle:
			UserSelectActivity.this.finish();
			break;
		}
	}
String deptId="";
	void showLeft() {
		leftlistView = (ListView) findViewById(R.id.listviewId);
		MyAdapterd adapterd = new MyAdapterd(this, rootNode);
		adapterd.ExpanderLevel(0);
		leftlistView.setAdapter(adapterd);
		leftlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				((MyAdapterd) parent.getAdapter()).ExpandOrCollapse(position);
				Node snode=(Node) ((MyAdapterd) parent.getAdapter()).getItem(position);
				GetUserByDept task=new GetUserByDept();
				deptId=snode.getValue();
				task.execute(snode.getValue(),"0");
				//UserSelectActivity.this.commonUtil.showToast(snode.getText()+"====="+snode.getValue());
			}
		});
		footbar = new ListViewUtil(this, listview);
	}
	Node rootNode;
	Node normalNode;
	void getDepa(String depats, Node node) {
		if(depats==null) return;
		List<Map<String, String>> dlist = DataConvert.toArrayList(depats);
		for (Map<String, String> m : dlist) {
			normalNode=new Node(m.get("text"), m.get("id"));
			if (node == null)
				rootNode = normalNode;
			else{
				node.add(normalNode);
				normalNode.setParent(node);
			}
			if (m.get("leaf") == null) {
				getDepa(m.get("children"),normalNode);
			}
		}
	}
	
	class GetUserByDept extends AsyncTask<String, Integer, Void> {
		ProgressDialog pd;
		long totalSize;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(UserSelectActivity.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("加载人员中...");
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			
			List<Map<String,String>> tList=new  ArrayList<Map<String, String>>();
			tList.clear();
			String depId = arg0[0];
			list = findUserData.selectAppUser(depId,user.getUserId());
			if (list.get(0).get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).put("selected", "0");	// 未选中
						tList.add(list.get(i));
					}
			}
			adapter.setData(tList);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			pd.setProgress((int) (progress[0]));
		}

		@Override
		protected void onPostExecute(Void v) {
			pd.dismiss();
			adapter.notifyDataSetChanged();
		}
	}

	int lastItem = 0;
	ListViewUtil footbar;
	@Override
	public void onScroll(AbsListView v, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView v, int state) {

	}

	private void doSetListView() {
		 
	}

}
