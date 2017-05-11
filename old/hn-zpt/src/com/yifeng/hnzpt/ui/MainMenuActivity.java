package com.yifeng.hnzpt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.CommonAdapter;
import com.yifeng.hnzpt.adapter.ImageAdapter;
import com.yifeng.hnzpt.ui.employ.EmployCountListActivity;
import com.yifeng.hnzpt.ui.enterprise.EnterpriseActivity;
import com.yifeng.hnzpt.ui.enterprise.EnterpriseEditActivity;
import com.yifeng.hnzpt.ui.manage.ManageListActivity;
import com.yifeng.hnzpt.ui.recruitment.RecruitmentActivity;
import com.yifeng.hnzpt.ui.register.RegisterCountListActivity;
import com.yifeng.hnzpt.ui.system.AccountActivity;
import com.yifeng.hnzpt.ui.system.SystemSettings;
import com.yifeng.hnzpt.ui.training.TrainingListActivity;
import com.yifeng.hnzpt.ui.zcfg.ZCFGListActivity;
import com.yifeng.hnzpt.util.ConstantUtil;
import com.yifeng.hnzpt.util.DataConvert;
import com.yifeng.hnzpt.widget.CircleFlowIndicator;
import com.yifeng.hnzpt.widget.ViewFlow;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * ClassName:MainMenuActivity 
 * Description：首页-主菜单九宫格页面（GridView实现）
 * @author Administrator 
 * Date：2012-10-15
 */
public class MainMenuActivity extends BaseActivity {
	private TextView userTxt,txt_companyName;
	private ImageAdapter imageadapter;
	private ViewFlow viewFlow;
	private GridView gridView;
	private List<Map<String, Object>> menus;
	private final int ALERT_MSG=12001;
	private int displayWidth;
	CommonAdapter itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		setContentView(R.layout.main_menu);

		initView();
		// 首页广告图片显示
		setBanner();
		
	}

	/* ================================ 首页广告图片显示 ================================ */
	private void setBanner() {
		imageadapter = new ImageAdapter(this);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(imageadapter);

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);

		viewFlow.setSelection(0); // 设置初始位置
		// viewFlow.startAutoFlowTimer(); //启动自动播放
	}

	private void initView() {
		txt_companyName = (TextView) findViewById(R.id.txt_companyName);
		txt_companyName.setText(user.getZbdw().equals("")?getString(R.string.copyright):"主办："+user.getZbdw());

		userTxt = (TextView) findViewById(R.id.userTxt);
		userTxt.setText(user.getTitle() + ",欢迎您使用！");
		
		if (user.getcompanyManager().equals("")||user.getLinkPhone().equals("")||user.getLinkAddress().equals("")||user.getLongitude().equals("")|| user.getLatitude().equals("")) {
			String showMsg="您有以下信息需要完善：<br/>";
			if(user.getcompanyManager().equals(""))
				showMsg+="联系人<br/>";
			if(user.getLinkPhone().equals(""))
				showMsg+="联系电话<br/>";
			if(user.getLinkAddress().equals(""))
				showMsg+="联系地址<br/>";
			if(user.getLongitude().equals("")|| user.getLatitude().equals("")){
				showMsg+="公司所在位置经纬度坐标 <br/>";
			}
			showMsg+="现在要立即修改吗?";
			
			dialogUtil.showMsg("友情提醒", showMsg,mHandler,ALERT_MSG);
		}

		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(R.drawable.edit_text);
		// gridView.setBackgroundResource(R.drawable.tabbg);// 设置背景
		gridView.setNumColumns(3);// 设置每行列数
		gridView.setGravity(Gravity.CENTER);// 位置居中

		loadMenuData();
		SimpleAdapter adapter = new SimpleAdapter(this, menus, R.layout.main_menu_item, 
				new String[] { "itemImage", "itemText" }, 
				new int[] { R.id.item_imageImg, R.id.item_textTxt });
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = (Intent) menus.get(position).get("intent");
				String tag = menus.get(position).get("tag").toString();
				if (intent != null) {
					if (tag.equals("rx")) {
						// 12333热线，弹出对话框询问操作
//						final Dialog builder = new Dialog(MainMenuActivity.this, R.style.dialog);
//						builder.setContentView(R.layout.confirm_dialog);
//						TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
//						TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
//						ptitle.setText(MainMenuActivity.this.getString(R.string.alert_dialog_finish_title));
//						pMsg.setText("确定要拔打就业热线吗？");
//						Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
//						Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
//						confirm_btn.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0898-63260060"));
//								startActivity(intent);
//								builder.dismiss();
//							}
//						});
//
//						cancel_btn.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								builder.dismiss();
//							}
//						});
//						builder.setCanceledOnTouchOutside(true);
//						builder.show();
						
						showItem();

					} else {
						startActivity(intent);
					}
				}
			}
		});
	}
	
	Handler mHandler=new Handler(){
	  public void handleMessage(Message msg) {
		  super.handleMessage(msg);
		  if(msg.what==ALERT_MSG){
			  Intent edit = new Intent(MainMenuActivity.this,EnterpriseEditActivity.class);
			  startActivity(edit);
		  }
	  };	
	};

	private void loadMenuData() {
		menus = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemText", "企业信息");
		map.put("itemImage", R.drawable.qyxx);
		map.put("tag", "qyxx");
		Intent qyxx = new Intent(MainMenuActivity.this, EnterpriseActivity.class);
		map.put("intent", qyxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "招聘登记");
		map.put("itemImage", R.drawable.zpdj);
		map.put("tag", "zpdj");
		Intent zpdj = new Intent(MainMenuActivity.this, RecruitmentActivity.class);
		zpdj.putExtra("flag", "MainMenuActivity");
		map.put("intent", zpdj);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "招聘管理");
		map.put("itemImage", R.drawable.zpgl);
		map.put("tag", "zpgl");
		Intent zpgl = new Intent(MainMenuActivity.this, ManageListActivity.class);
		map.put("intent", zpgl);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "报名信息");
		map.put("itemImage", R.drawable.bmxx);
		map.put("tag", "bmxx");
		Intent bmxx = new Intent(MainMenuActivity.this, RegisterCountListActivity.class);
		map.put("intent", bmxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "录用查询");
		map.put("itemImage", R.drawable.lycx);
		map.put("tag", "lycx");
		//Intent lycx = new Intent(MainMenuActivity.this, EmployListActivity.class);
		Intent lycx = new Intent(MainMenuActivity.this, EmployCountListActivity.class);
		map.put("intent", lycx);
		menus.add(map);
		
		map = new HashMap<String, Object>();
		map.put("itemText", "培训信息");
		map.put("itemImage", R.drawable.pxxx);
		map.put("tag", "pxxx");
		Intent pxxx = null;
		map.put("intent", pxxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "就业新闻");
		map.put("itemImage", R.drawable.jyxw);
		map.put("tag", "wdqz");
		Intent tjxx = new Intent(MainMenuActivity.this, TrainingListActivity.class);
		map.put("intent", tjxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "政策法规");
		map.put("itemImage", R.drawable.zcfg);
		map.put("tag", "zcfg");
//		Intent zcfg = new Intent(MainMenuActivity.this, PolicyListActivity.class);
		Intent zcfg = new Intent(MainMenuActivity.this, ZCFGListActivity.class);
		map.put("intent", zcfg);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "就业热线");
		map.put("itemImage", R.drawable.rx12333);
		map.put("tag", "rx");
		Intent rx = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0898-63260060"));
		map.put("intent", rx);
		menus.add(map);

//		map = new HashMap<String, Object>();
//		map.put("itemText", "系统设置");
//		map.put("itemImage", R.drawable.gridview_xtsz);
//		map.put("tag", "xtsz");
//		Intent xtsz = new Intent(MainMenuActivity.this, SystemSettings.class);
//		map.put("intent", xtsz);
//		menus.add(map);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.recomd:
			Uri smsToUri = Uri.parse("smsto:");
			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
			mIntent.putExtra("sms_body", getString(R.string.app_name) + "下载地址:" + ConstantUtil.downapk);
			startActivity(mIntent);
			break;
		case R.id.setting:
			Intent intent0 = new Intent(MainMenuActivity.this, AccountActivity.class);
			startActivity(intent0);
			break;
		case R.id.exit:
			this.dialogUtil.doAdvanceExit();
			break;
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ConstantUtil.ISEJOB) {
				// 如果是管理版进来，直接返回;
				this.finish();
			} else {
				// 直接退出
				dialogUtil.doAdvanceExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
private List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
	
	private void showItem(){
		subList.clear();
		subList = DataConvert.toObjectArrayList(user.getAreas());
		int maxWidth = (int) (displayWidth * 0.8);
		LayoutInflater inflater = LayoutInflater.from(this);  
        View v = inflater.inflate(R.layout.main_dialog, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        TextView ptitle = (TextView) layout.findViewById(R.id.top_title);
        ptitle.setText("就业热线");
        ListView listView = (ListView) layout.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					final String name = subList.get(arg2).get("name").toString().trim();
					final String tell = subList.get(arg2).get("hotline").toString().trim();
					if(tell.equals("")){
						return;
					}
					
					final Dialog builder = new Dialog(MainMenuActivity.this,
							R.style.dialog);
					builder.setContentView(R.layout.confirm_dialog);
					TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
					TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
					ptitle.setText(MainMenuActivity.this
							.getString(R.string.alert_dialog_finish_title));
					pMsg.setText("确定要拔打"+name+"就业热线"+tell+"吗？");
					final Button confirm_btn = (Button) builder
							.findViewById(R.id.confirm_btn);
					Button cancel_btn = (Button) builder
							.findViewById(R.id.cancel_btn);
					confirm_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:"+tell));
							startActivity(intent);
							builder.dismiss();
						}
					});
	
					cancel_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							builder.dismiss();
						}
					});
					builder.setCanceledOnTouchOutside(true);
					builder.show();
					 
				}catch (Exception e) {
					
				}
				
			}
		});
        itemAdapter = new CommonAdapter(this,subList, R.layout.main_dialog_item, new String[] {
		 "name","hotline" }, new int[] {  R.id.row_name,R.id.row_num }, getResources());
        itemAdapter.setViewBinder();
        listView.setAdapter(itemAdapter);
        
		final Dialog builder = new Dialog(this,R.style.dialog);
		builder.setContentView(layout,
				new LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}
}
