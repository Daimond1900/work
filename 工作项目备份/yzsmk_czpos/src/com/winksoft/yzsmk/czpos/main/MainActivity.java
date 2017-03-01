package com.winksoft.yzsmk.czpos.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.balance.SelectBalanceActivity;
import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.czpos.cz.CzActivity;
import com.winksoft.yzsmk.czpos.czrecord.CzRecord;
import com.winksoft.yzsmk.czpos.employee.EmployeeActivity;
import com.winksoft.yzsmk.czpos.queryvalue.QueryValues;
import com.winksoft.yzsmk.czpos.settingparam.SettingActivity;
import com.winksoft.yzsmk.czpos.signout.SignOutActivity;
import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.ibase.DialogUtil;
import com.winksoft.yzsmk.ibase.IDialog.AddEmployeeListener;
import com.winksoft.yzsmk.ibase.IDialog.PwdListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener {

	private GridView gridView;
	private SqliteUtil sqliteUtil;
	private LinearLayout lPayment;
	private List<Map<String, Object>> menus;
	SimpleAdapter adapter;
	DialogUtil dialogUtil;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(getString(R.string.app_name));
		dialogUtil = DialogUtil.getinstance();
		sqliteUtil = SqliteUtil.getInstance(this); // 获取sqliteUtil实例
		lPayment = (LinearLayout) findViewById(R.id.lPayment);
		lPayment.setOnClickListener(this);
		initMenu();
	}
	
	/** 初始化GridView数据 */
	private void initMenu() {
		loadMenuData();
		gridView = (GridView) findViewById(R.id.mainGview);
		adapter = new SimpleAdapter(this, menus, R.layout.main_item, new String[] { "itemImage", "itemText" },
				new int[] { R.id.mainImage, R.id.mainText });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int tag = (Integer) menus.get(position).get("id");
				switch (tag) {
				// 余额查询
				case 1:
					Intent balanceintent = new Intent(MainActivity.this, SelectBalanceActivity.class);
					startActivity(balanceintent);
					break;
				// 交易记录
				case 2:
					Intent consumptionList = new Intent(MainActivity.this, CzRecord.class);
					startActivity(consumptionList);
					break;
				// 日结
				case 3:
					showSignDialog();
					break;
				// 月结
				case 4:
					showSignDialog();
					break;
				// 设置
				case 5:
					Intent intent = new Intent(MainActivity.this, SettingActivity.class);
					File file = Environment.getExternalStorageDirectory();
					if (file != null) {
						// Map<String, String> psi =
						// SettingDataUtil.getPaySettingInfo();
						QueryValues qv = new QueryValues();
						intent.putExtra("ipNum", qv.getIpNum());
						intent.putExtra("terminalNum", qv.getTerminalNum());
						intent.putExtra("ip", qv.getIp());
						intent.putExtra("port", qv.getPort());
						intent.putExtra("username", qv.getUsername());
						intent.putExtra("password", qv.getPassword());
						intent.putExtra("remotePath", qv.getRemotePath());
						intent.putExtra("accpetCusNo", qv.getAccpetCusNo());
					}
					startActivity(intent);
					break;
				case 6: // 签到 	
					int isSign = sqliteUtil.doQuery("select count(1) from signin where state = '0'");
					if (isSign == 1) {
						showToast("已签到");
						return;
					}
					showSignDialog();
					break;
				case 7: // 签退
					// 下午修改
//						showSignDialog();
//						break;
					Intent signOutIntent = new Intent(MainActivity.this, SignOutActivity.class);
					startActivity(signOutIntent);
					
					break;
				case 8: // 工号管理
					dialogUtil.showPwdDialog(MainActivity.this, new PwdListener() {
						@Override
						public void onConfirm(String pwd) {
							if (pwd.equals("20060101")) {
								dialogUtil.closeDialog();
								startActivity(EmployeeActivity.class);
							} else if (pwd.equals("")) {
								showToast("请输入密码");
							} else {
								showToast("密码错误");
							}
						}
					});
					break;
				default:
					break;
				}
			}
		});
	}

	private void loadMenuData() {
		menus = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("itemText", "余额查询");
		map.put("itemImage", R.drawable.main_yecx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("id", 2);
		map.put("itemText", "充值记录");
		map.put("itemImage", R.drawable.main_jyjl);
		menus.add(map);

//		map = new HashMap<String, Object>();
//		map.put("id", 6);
//		map.put("itemText", "签到");
//		map.put("itemImage", R.drawable.main_qd);
//		menus.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 7);
		map.put("itemText", "上传");
		map.put("itemImage", R.drawable.main_qt);
		menus.add(map);
		
//		map = new HashMap<String, Object>();
//		map.put("id", 8);
//		map.put("itemText", "工号管理");
//		map.put("itemImage", R.drawable.main_ghcx);
//		menus.add(map);
		map = new HashMap<String, Object>();
		map.put("id", 5);
		map.put("itemText", "设置");
		map.put("itemImage", R.drawable.main_set);
		menus.add(map);
	}

	/**
	 * 判断是否签到 TODO:
	 */
	private void showSignDialog() {
		// 1.查询签到表“sign”是否有state为0的记录
		// 2.已签到直接跳转界面
		// 3.未签到，弹出签到对话框签到    		
		dialogUtil.showAddDialog(MainActivity.this, "签到", new AddEmployeeListener() {
			@Override
			public void onConfirm(String no, String pwd) {
				if (no.isEmpty()) {
					showToast("请输入工号");
					return;
				}
				if (pwd.isEmpty()) {
					showToast("请输入密码");
					return;
				}
				// 去员工表检测工号密码是否匹配，
				// 匹配：插入数据库
				// 不匹配：给出相应提示
				String sql = new StringBuilder().append(" select count(1) from employee ").append(" where UserName = '")
						.append(no).append("' and PassWord = '").append(pwd).append("'").toString();

				int qResult = sqliteUtil.doQuery(sql);
				if (qResult == 0) {
					showToast("工号或密码错误");
					return;
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("sign_no", no);
				map.put("signin_date", getCurrentTime());
				map.put("state", "0");
				//删除
//				sqliteUtil.deleteSignExceed(sqliteUtil.getSignEarliestId());
				int count = sqliteUtil.getCount("signin");
				if(count > 99){
					String id = sqliteUtil.getSignEarliestId();
					sqliteUtil.deleteSignExceed(id);
				}
				long flag = sqliteUtil.insert("signin", map);
				if (flag != -1) {
					showToast("签到成功");
					dialogUtil.closeDialog();
				} else {
					showToast("签到失败");
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lPayment:
			// 下午修改
//			int isSign = sqliteUtil.doQuery("select count(1) from signin where state = '0'");
//			if (isSign == 0) {
//				showSignDialog();
//				return;
//			}
			Intent intent = new Intent(this, CzActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
