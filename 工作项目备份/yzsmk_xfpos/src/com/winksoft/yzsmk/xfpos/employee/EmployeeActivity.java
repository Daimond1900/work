package com.winksoft.yzsmk.xfpos.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.ibase.AlertDialogUtils;
import com.winksoft.yzsmk.ibase.DialogUtil;
import com.winksoft.yzsmk.ibase.IDialog.AddEmployeeListener;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;

@SuppressLint("NewApi")
public class EmployeeActivity extends BaseActivity implements OnClickListener{
	
	private Button btnAdd;
	DialogUtil dialogUtil;
	SqliteUtil util;
	private ListView listView;
	SimpleAdapter adapter;
	private List<Map<String, Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_list);
		setTitle("工号管理");
		dialogUtil = DialogUtil.getinstance();
		util = SqliteUtil.getInstance(this);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		onload();
	}
	
	public void onload(){
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String userName = list.get(position).get("UserName").toString();
				AlertDialogUtils.deleteChooseDialog(EmployeeActivity.this,"确定要删除" + userName + "？",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						dialog.dismiss();
						boolean flag = util.doDelete("employee", "UserName=?", new String[]{userName});
						if(flag){
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> map = list.get(i);
								if(map.get("UserName").equals(userName)){
									list.remove(i);
									break;
								}
							}
							adapter.notifyDataSetChanged();
						}
					}
				});
				return true;
			}
		});
		list = util.doQuery("employee", new String[]{"UserName","PassWord"} , null, null, null, null, null);
		adapter = new SimpleAdapter(this, list, R.layout.activity_employee_list_item, new String[]{"UserName","PassWord"}, new int[]{R.id.tvNo,R.id.tvPwd});
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			dialogUtil.showAddDialog(EmployeeActivity.this, "新增用户", new AddEmployeeListener() {
				@Override
				public void onConfirm(String no, String pwd) {
					if(no.isEmpty()){
						showToast("请输入工号");
						return;
					}
					if(pwd.isEmpty()){
						showToast("请输入密码");
						return;
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("UserName", no);
					map.put("PassWord", pwd);
					map.put("CreateTime", getCurrentTime());
					long flag = util.insert("employee", map);
					if(flag != -1){
						showToast("添加成功");
						dialogUtil.closeDialog();
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("UserName", no);
						map1.put("PassWord", pwd);
						list.add(map1);
						adapter.notifyDataSetChanged();
					}else{
						showToast("添加失败");
					}
				}
			});
			break;
		default:
			break;
		}
	}
}
