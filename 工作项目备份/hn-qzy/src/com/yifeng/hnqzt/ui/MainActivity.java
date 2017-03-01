package com.yifeng.hnqzt.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.adapter.ImageAdapter;
import com.yifeng.hnqzt.data.JobDAL;
import com.yifeng.hnqzt.data.UserDAL;
import com.yifeng.hnqzt.ui.ambitus.AmbitusMap;
import com.yifeng.hnqzt.ui.enterprise.EnterpriseList;
import com.yifeng.hnqzt.ui.job.MyJobActivity;
import com.yifeng.hnqzt.ui.recruit.RecuitInfoActivity;
import com.yifeng.hnqzt.ui.system.SystemSettings;
import com.yifeng.hnqzt.ui.training.TrainingActivity;
import com.yifeng.hnqzt.ui.zcfg.ZCFGListActivity;
import com.yifeng.hnqzt.util.ConstantUtil;
import com.yifeng.hnqzt.util.DataConvert;
import com.yifeng.hnqzt.widget.CircleFlowIndicator;
import com.yifeng.hnqzt.widget.ViewFlow;

/**
 * comment:主界面
 * 
 * @author:吴家宏 Date:2012-8-27
 */
public class MainActivity extends BaseActivity {
	private ImageAdapter imageadapter;
	private ViewFlow viewFlow;
	private GridView grid_view;
	private List<Map<String, Object>> menus;
	private TextView login_msg,txt_companyName;
	private Button loginBtn;
	private Map<String, String> returnMap;
	private JobDAL jobDal;
	private SimpleAdapter simperAdapter;
	private UserDAL userDal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		displayWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		jobDal = new JobDAL(this);
		userDal=new UserDAL(this);
		intitPage();

		// 首页广告图片显示
		setBanner();

		// 如果已登录
		loginView();
		
		new Thread(runnable).start();
	}
	
	/**
	 * 首页广告图片显示
	 */
	private void setBanner() {
		imageadapter = new ImageAdapter(this);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(imageadapter);

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);

		viewFlow.setSelection(0);// 设置初始位置
		// viewFlow.startAutoFlowTimer(); //启动自动播放
	}

	private void intitPage() {
		txt_companyName = (TextView) findViewById(R.id.txt_companyName);
		txt_companyName.setText(user.getZbdw().equals("")?getString(R.string.copyright):"主办："+user.getZbdw());

		login_msg = (TextView) findViewById(R.id.login_msg);
		// login_msgs = (TextView) findViewById(R.id.login_msgs);

		login_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ConstantUtil.isLogin = false;
				for (int i = 0; i < menus.size(); i++) {
					if (menus.get(i).get("tag").equals("wdqz")) {// 我的求职 显示小圆点
						menus.get(i).put("news", "");
						menus.get(i).put("count", "");
					}
				}
				simperAdapter.notifyDataSetChanged();
				login_msg.setVisibility(View.GONE);
				loginBtn.setVisibility(View.VISIBLE);

			}
		});

		loginBtn = (Button) findViewById(R.id.loginBtn);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent login = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(login);
			}
		});

		grid_view = (GridView) findViewById(R.id.grid_view);
		grid_view.setSelector(R.drawable.edit_text);
		// grid_view.setBackgroundResource(R.drawable.tabbg);// 设置背景
		grid_view.setNumColumns(3);// 设置每行列数
		grid_view.setGravity(Gravity.CENTER);// 位置居中
		// grid_view.setVerticalSpacing(10);// 垂直间隔
		// grid_view.setHorizontalSpacing(10);// 水平间隔

		loadMenuData();
		simperAdapter = new SimpleAdapter(this, menus, R.layout.main_menu,
				new String[] { "itemImage", "itemText", "news", "count" },
				new int[] { R.id.item_imageg, R.id.item_textg, R.id.news,
						R.id.count });
		grid_view.setAdapter(simperAdapter);// 设置菜单Adapter*/

		grid_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = (Intent) menus.get(position).get("intent");
				String tag = menus.get(position).get("tag").toString();
				if (intent != null) {

					if (tag.equals("wdjl") || tag.equals("wdqz")) {// 我的简历、我的求职
						if (!ConstantUtil.isLogin) {
							commonUtil.shortToast("请先登录!");
							startActivity(new Intent(MainActivity.this,
									LoginActivity.class));
							return;
						}
					}
					if (tag.equals("rxy")) {
//						dialogUtil.showCallDialog("系统提示", "确定要拔打就业热线吗?",
//								"12333");
						showItem();
						return;
					}

					startActivity(intent);

				}
			}
		});
	}

	private void loadMenuData() {
		menus = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemText", "招聘信息");
		map.put("itemImage", R.drawable.zpxx);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "zpxx");
		Intent zpxx = new Intent(MainActivity.this, RecuitInfoActivity.class);
		// zpxx.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		map.put("intent", zpxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "周边信息");
		map.put("itemImage", R.drawable.zbxx);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "xtbz");
		Intent zbxx = new Intent(MainActivity.this, AmbitusMap.class);
		map.put("intent", zbxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "我的简历");
		map.put("itemImage", R.drawable.wdjl);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "wdjl");
		Intent wdjl = new Intent(MainActivity.this,
				PersonalResumeActivity.class);
		map.put("intent", wdjl);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "招聘企业");
		map.put("itemImage", R.drawable.zpqy);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "zpqy");
		Intent zpqy = new Intent(MainActivity.this, EnterpriseList.class);
		map.put("intent", zpqy);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "我的求职");
		map.put("itemImage", R.drawable.wdqz);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "wdqz");
		Intent wdqz = new Intent(MainActivity.this, MyJobActivity.class);
		map.put("intent", wdqz);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "培训信息");
		map.put("itemImage", R.drawable.pxxx);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "pxxx");
		Intent pxxx = null;
		// pxxx.putExtra("title", "培训信息");
		map.put("intent", pxxx);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "就业新闻");
		map.put("itemImage", R.drawable.jyxx);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "jyxw");
		Intent jyxw = new Intent(MainActivity.this, TrainingActivity.class);
		jyxw.putExtra("title", "就业新闻");
		map.put("intent", jyxw);
		menus.add(map);

		//
		// map = new HashMap<String, Object>();
		// map.put("itemText", "创业平台");
		// map.put("itemImage", R.drawable.cypt);
		// map.put("news", "");
		// map.put("count", "");
		// map.put("tag", "cyxm");
		// Intent cyxm = new
		// Intent(MainActivity.this,VentureGalleryActivity.class);
		// cyxm.putExtra("title", "创业平台");
		// map.put("intent", cyxm);
		// menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "政策法规");
		map.put("itemImage", R.drawable.zcfg);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "zcfg");
		// Intent zcfg = new Intent(MainActivity.this,PolicyActivity.class);
		Intent zcfg = new Intent(MainActivity.this, ZCFGListActivity.class);
		zcfg.putExtra("title", "政策法规");
		map.put("intent", zcfg);
		menus.add(map);

		map = new HashMap<String, Object>();
		map.put("itemText", "就业热线");
		map.put("itemImage", R.drawable.rx12333);
		map.put("news", "");
		map.put("count", "");
		map.put("tag", "rxy");
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:12333"));
		map.put("intent", intent);
		menus.add(map);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.recomd:
			Uri smsToUri = Uri.parse("smsto:");
			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
					smsToUri);
			mIntent.putExtra("sms_body", getString(R.string.app_name) + "下载地址:"
					+ ConstantUtil.downapk);
			startActivity(mIntent);
			break;
		case R.id.setting:
			Intent xtsz = new Intent(MainActivity.this, SystemSettings.class);
			xtsz.putExtra("title", "系统设置");
			startActivity(xtsz);
			break;
		case R.id.exit:
			this.dialogUtil.doAdvanceExit();
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ConstantUtil.ISEJOB) {// 如果是管理版进来，直接返回;
				this.finish();
			} else {// 直接退出
				dialogUtil.doAdvanceExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 登录显示状态
	private void loginView() {
		if (ConstantUtil.isLogin) {// 如果已登录
			login_msg.setVisibility(View.VISIBLE);
			String userName = user.getTitle();
			//
			if (userName.length() > 5) {
				login_msg.setText(Html.fromHtml(userName
						+ "<br/> 欢迎您使用!<a href=''>[注销]</a>"));
			} else {
				login_msg.setText(Html.fromHtml(userName
						+ " 欢迎您使用!<a href=''>[注销]</a>"));
			}

			loginBtn.setVisibility(View.GONE);

			// 只有登录过才显示统计数
			new Thread(mainRunnable).start();

		} else {
			loginBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loginView();
	}

	Runnable mainRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				returnMap = jobDal.doGetCount(user.getUserId());
				mianHandler.sendEmptyMessage(1);

			} catch (Exception e) {
				e.printStackTrace();
				mianHandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler mianHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				try {
					if (returnMap.get("state").equals(
							String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
						for (int i = 0; i < menus.size(); i++) {
							if (menus.get(i).get("tag").equals("wdqz")) {// 我的求职
								int sum;
								int a = returnMap.get("interview").equals("") ? 0
										: Integer.parseInt(returnMap.get(
												"interview").toString());
								int b = returnMap.get("employ").equals("") ? 0
										: Integer.parseInt(returnMap.get(
												"employ").toString());
								int c = returnMap.get("fail").equals("") ? 0
										: Integer.parseInt(returnMap
												.get("fail").toString());
								int d = returnMap.get("preview").equals("") ? 0
										: Integer.parseInt(returnMap.get(
												"preview").toString());
								sum = a + b + c + d;
								menus.get(i).put("news", R.drawable.ts);
								menus.get(i).put("count", sum);
							}
						}
						simperAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {

				}
			}else if(msg.what == 5){
			}else if(msg.what == -1){
			}
		};
	};
	
	
	Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				subList=userDal.doGetAreas1();
				mianHandler.sendEmptyMessage(5);
				
			} catch (Exception e) {
				e.printStackTrace();
				mianHandler.sendEmptyMessage(-1);
			}
		}
	};

	private int displayWidth;
	CommonAdapter itemAdapter;
	private List<Map<String, Object>> subList;

	private void showItem() {
		if(subList == null){
			subList = new ArrayList<Map<String,Object>>();
		}
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
				try {
					final String name = subList.get(arg2).get("name")
							.toString().trim();
					final String tell = subList.get(arg2).get("hotline")
							.toString().trim();
					if (tell.equals("")) {
						return;
					}

					final Dialog builder = new Dialog(MainActivity.this,
							R.style.dialog);
					builder.setContentView(R.layout.confirm_dialog);
					TextView ptitle = (TextView) builder
							.findViewById(R.id.pTitle);
					TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
					ptitle.setText(MainActivity.this
							.getString(R.string.alert_dialog_finish_title));
					pMsg.setText("确定要拔打" + name + "就业热线" + tell + "吗？");
					final Button confirm_btn = (Button) builder
							.findViewById(R.id.confirm_btn);
					Button cancel_btn = (Button) builder
							.findViewById(R.id.cancel_btn);
					confirm_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + tell));
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

				} catch (Exception e) {

				}

			}
		});
		itemAdapter = new CommonAdapter(this, subList,
				R.layout.main_dialog_item, new String[] { "name", "hotline" },
				new int[] { R.id.row_name, R.id.row_num }, listView);
		itemAdapter.setViewBinder();
		listView.setAdapter(itemAdapter);

		final Dialog builder = new Dialog(this, R.style.dialog);
		builder.setContentView(layout, new LinearLayout.LayoutParams(maxWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}
}
