package com.winksoft.android.yzjycy.ui.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.JobDAL;
import com.winksoft.android.yzjycy.util.Constants;

import com.winksoft.android.yzjycy.BaseActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 我的求职
 * 
 * @author wujiahong 2012-09-21
 */
public class QztMyJobActivity extends BaseActivity {
	private ListView listView, listView1;
	private List<Map<String, Object>> list, list1;
	private CommonAdapter adapter;
	private Button back_btn;
	private ImageView userHead;
	private TextView userName, jobCountLab;
	private JobDAL jobDal;
	private ProgressDialog progressDialog;
	private Map<String, String> returnMap;
	private Bitmap headBamp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.qzt_my_work);

		jobDal = new JobDAL(this);

		userHead = (ImageView) findViewById(R.id.userHead);

		userName = (TextView) findViewById(R.id.userName);
		// userName.setText(user.getTitle().equals("")?user.getLoginName():user.getTitle());
		userName.setText(user.getLoginName());

		jobCountLab = (TextView) findViewById(R.id.jCountLab);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String count = list.get(arg2).get("count").toString();
					if (count.equals("0")) {
						commonUtil.shortToast("没有查到数据!");
					} else {
						Intent bl = (Intent) list.get(arg2).get("intent");
						startActivity(bl);
					}

					// Intent bl=(Intent)list.get(arg2).get("intent");
					// startActivity(bl);

				} catch (Exception e) {
					e.printStackTrace();
					commonUtil.shortToast("未响应!");
				}

			}
		});

		/*
		 * listView1=(ListView)findViewById(R.id.listview1);
		 * listView1.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * position, long arg3) { switch (position) { case 0: Intent intent0 =
		 * new Intent(MyJobActivity.this,JobCommonActivity.class);
		 * intent0.putExtra("title", "收藏的职位"); intent0.putExtra("flag", "3");
		 * startActivity(intent0); break; case 1: Intent intent1 = new
		 * Intent(MyJobActivity.this,AttentionActivity.class);
		 * intent1.putExtra("title", "关注的公司"); startActivity(intent1); break;
		 * default: break; }
		 * 
		 * } });
		 */

		loadMenu();

		adapter = new CommonAdapter(this, list, R.layout.qzt_my_work_menu_item,
				new String[] { "title", "count", "icon" }, new int[] { R.id.title, R.id.count, R.id.icon }, listView);
		listView.setAdapter(adapter);

		/*
		 * adapter=new CommonAdapter(this, list1, R.layout.my_work_menu_item,
		 * new String[]{"title","count","icon"}, new
		 * int[]{R.id.title,R.id.count,R.id.icon}, listView1);
		 * listView1.setAdapter(adapter);
		 */

		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QztMyJobActivity.this.finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 加载数据
		// progressDialog=commonUtil.showProgressDialog("正在加载数据....");
		new Thread(myRunnable).start();
	}

	private void loadMenu() {
		list = new ArrayList<Map<String, Object>>();
		list1 = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "1");
		map.put("title", "录用通知");
		map.put("count", "0");
		map.put("icon", R.drawable.qzt_campus_icon_listmeanu_talk);
		Intent bl = new Intent(QztMyJobActivity.this, QztNoticeActivity.class);
		bl.putExtra("title", "录用通知");
		map.put("intent", bl);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "2");
		map.put("title", "面试邀请函");
		map.put("count", "0");
		map.put("icon", R.drawable.qzt_campus_apply_icon);
		Intent intent1 = new Intent(QztMyJobActivity.this, QztInvitedActivity.class);
		intent1.putExtra("title", "面试邀请函");
		map.put("intent", intent1);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "3");
		map.put("title", "等待公司确认");
		map.put("count", "0");
		map.put("icon", R.drawable.qzt_campus_viewed_company_icon);
		Intent intent2 = new Intent(QztMyJobActivity.this, QztJobCommonActivity.class);
		// intent2.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent2.putExtra("title", "等待公司确认");
		intent2.putExtra("type", "0");
		map.put("intent", intent2);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("id", "4");
		map.put("title", "简历投递失败");
		map.put("count", "0");
		map.put("icon", R.drawable.qzt_campus_other_app_icon);
		Intent intent3 = new Intent(QztMyJobActivity.this, QztJobCommonActivity.class);
		intent3.putExtra("title", "简历投递失败");
		intent3.putExtra("type", "1");
		map.put("intent", intent3);
		list.add(map);

		/*
		 * map=new HashMap<String,Object>(); map.put("id", "5");
		 * map.put("title", "收藏的职位"); map.put("count", "2");
		 * map.put("icon",R.drawable.campus_icon_listmeanu_favorite);
		 * list1.add(map);
		 * 
		 * map=new HashMap<String,Object>(); map.put("id", "6");
		 * map.put("title", "关注的公司"); map.put("count", "6");
		 * map.put("icon",R.drawable.campus_change_background_icon);
		 * list1.add(map);
		 */
	}

	Runnable myRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				returnMap = jobDal.doGetCount(user.getUserId());
				// returnMap=jobDal.doGetCount("4028808a49e50db90149e519c0370001");
				myHandler.sendEmptyMessage(1);

				/**
				 * 获取头像 待修改
				 */
				if (user.getPic() != null && !user.getPic().equals("")) {
					ImageLoader.getInstance().displayImage(Constants.IP + "images/" + user.getPic().replace("\\", "/"),
							userHead);
				}
				// if(!user.getPic().equals("")){//从远程加载图片
				// headBamp=CommonUtil.getURLBitmap(Constants.ip+"images/"+user.getPic().replace("\\",
				// "/"));
				// myHandler.sendEmptyMessage(2);
				// }

			} catch (Exception e) {
				e.printStackTrace();
				myHandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				fillData();
			}
			// if(msg.what==2){
			// //获取头象
			// if(headBamp!=null){
			// userHead.setImageBitmap(headBamp);
			// }
			// }
			// if(progressDialog!=null)
			// progressDialog.dismiss();
		};
	};

	/***
	 * 填充数据
	 */
	private void fillData() {
		if (returnMap != null) {
			if (returnMap.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
				int sum = 0;
				String count = "0";
				for (int i = 0; i < list.size(); i++) {
					switch (i) {
					case 0:
						count = returnMap.get("employ").equals("") ? "0" : returnMap.get("employ");// 试用通知
						sum += Integer.parseInt(count);
						break;
					case 1:
						count = returnMap.get("interview").equals("") ? "0" : returnMap.get("interview");// 面试通知
						sum += Integer.parseInt(count);
						break;
					case 2:
						count = returnMap.get("preview").equals("") ? "0" : returnMap.get("preview");// 等待公司确认
						sum += Integer.parseInt(count);
						break;
					case 3:
						count = returnMap.get("fail").equals("") ? "0" : returnMap.get("fail");// 投递简历失败
						sum += Integer.parseInt(count);
						break;
					default:
						count = "0";
						break;
					}
					list.get(i).put("count", count);
				}
				jobCountLab.setText(Html.fromHtml("您共投递<font color='red' size='24px'>" + sum + "</font>份简历"));
				adapter.notifyDataSetChanged();
			} else {
				commonUtil.shortToast("网络异常，请稍后再试!");
			}

		} else {
			commonUtil.shortToast("系统异常，请稍后再试!");
		}
	}
	
	
}
