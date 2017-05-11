package com.winksoft.android.yzjycy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.widget.MyGridView;
import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.activity.MenuActivity;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.winksoft.android.yzjycy.db.MainManage;
import com.winksoft.android.yzjycy.ui.bminfo.Zpt_RegisterCountListActivity;
import com.winksoft.android.yzjycy.ui.enterprise.ZptEnterpriseActivity;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.ui.job.QztMyJobActivity;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztRecuitInfoActivity;
import com.winksoft.android.yzjycy.ui.recruitmanage.Zpt_ManageListActivity;
import com.winksoft.android.yzjycy.ui.registration.Zpt_RecruitmentActivity;
import com.winksoft.android.yzjycy.ui.resume.QztPersonalResumeActivity;
import com.winksoft.android.yzjycy.ui.training.QztTrainingActivity;
import com.winksoft.android.yzjycy.ui.zcfg.QztZCFGListActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.banner.Banner;
import com.winksoft.banner.BannerConfig;
import com.winksoft.nox.android.view.YFBaseAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment implements OnItemClickListener,OnClickListener{
	private Banner banner;
	private Object[] images = new Object[] { R.drawable.main_banner1,
			R.drawable.main_banner2 };
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private MyGridView mainGv;
	private UserDAL userDal;
	YFBaseAdapter adapter;
	MainManage manage;
	private MyReceiver receiver;
	private View layout;
	private Button btZpMore,btQzMore;
	private ImageView ivCyfw,ivCydx;
	private View layout_special3;
	private View layout_special4;
	
	private int secondCount = 7200; // 倒计时2小时
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout != null) {
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_home, container, false);
		initView();
		initTimer();
		displayWidth = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth();
		userDal=new UserDAL(getContext());
		new Thread(runnable).start();
		return layout;
	}
	
	private void initView(){
		manage = new MainManage(getContext());
		mainGv = (MyGridView) layout.findViewById(R.id.mainGridView);
		mainGv.setOnItemClickListener(this);
		btZpMore = (Button) layout.findViewById(R.id.btZpMore);		// 招聘更多  
		btZpMore.setOnClickListener(this);
		btQzMore = (Button) layout.findViewById(R.id.btQzMore);		// 求职更多  
		btQzMore.setOnClickListener(this);
		ivCyfw = (ImageView) layout.findViewById(R.id.ivCyfw);		// 创业服务
		ivCyfw.setOnClickListener(this);
		ivCydx = (ImageView) layout.findViewById(R.id.ivCydx);		// 创业典型
		ivCydx.setOnClickListener(this);
		layout_special3 = layout.findViewById(R.id.layout_special3);// 15分钟服务圈
		layout_special3.setOnClickListener(this);
		layout_special4 = layout.findViewById(R.id.layout_special4);// 服务专家
		layout_special4.setOnClickListener(this);
		
		initBanner();
		initGrid();
		// 注册广播接收者
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(
				Constants.BROADCAST_FILTER.REFRESH_MENUS);
		getContext().registerReceiver(receiver, filter);
	}
	 
	
	private void initBanner() {
		banner = (Banner) layout.findViewById(R.id.banner1);
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		banner.setDelayTime(4000);
		banner.setImages(images);
	}

	private void initGrid() {
		if(list != null)
			list.clear();
		list = manage.getMainMenu();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resName", "more");
		map.put("name", "全部");
		map.put("count", "0");
		map.put("id", "-99");
		list.add(map);
		adapter = new YFBaseAdapter(getContext(), list, R.layout.mygridview_item1,
				new String[] { "resName", "name" }, new int[] {
						R.id.img, R.id.text_item },
				getResources()) {
			@Override
			protected void iniview(View view, int position,
					List<? extends Map<String, ?>> data) {
			
		}};
		adapter.setViewBinder();
		mainGv.setAdapter(adapter);
	}

		/**
		 * 判断
		 * @param iflogin
		 * @param intent
		 */
		private void ifLogin(boolean iflogin,Intent intent){
			if(iflogin){
				startActivity(intent);
			}else{
				Intent loginIntent = new Intent(getContext(),LoginActivity.class);
				startActivity(loginIntent);
			}
		}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		try{
			if(parent.getId() == R.id.mainGridView){
				int itemId = Integer.valueOf(list.get(position).get("id").toString());
				Intent intent = new Intent(getContext(), CommonPageView.class);
				Intent intent1 = null;
				switch (itemId) {
				case 1: //招聘信息
					intent1 = new Intent(getContext(), QztRecuitInfoActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 2:	//求职信息
					
					break;
				case 3:	//我的简历
					intent1 = new Intent(getContext(), QztPersonalResumeActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 4:	//我的求职
					intent1 = new Intent(getContext(), QztMyJobActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 5:	//企业信息
					intent1 = new Intent(getContext(), ZptEnterpriseActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 6:	//招聘发布
					intent1 = new Intent(getContext(), Zpt_RecruitmentActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 7:	//招聘管理
					intent1 = new Intent(getContext(), Zpt_ManageListActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 8:	//报名信息
					intent1 = new Intent(getContext(), Zpt_RegisterCountListActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 9:	//录用查询
					intent1 = new Intent(getContext(), Zpt_EmployCountListActivity.class);
					ifLogin(Constants.iflogin,intent1);
					break;
				case 10: //劳动力信息
					break;
				case 11: //零转移
					break;
				case 12: //每日一新
					break;
				case 13: //通讯录
					break;
				case 14: //就业热线
					if(Constants.iflogin){
						showItem();
					}else{
						intent1 = new Intent(getContext(),LoginActivity.class);
						startActivity(intent1);
					}
					break;
				case 15: //创业政策
					intent.putExtra("url", Constants.IP+"wap/entreppolicy/listEntrepPolicy");
					intent.putExtra("title", "创业政策");
					startActivity(intent);
					break;
				case 16: //服务指南
					intent.putExtra("url", Constants.IP+"wap/serviceguide/listServiceGuide");
					intent.putExtra("title", "服务指南");
					startActivity(intent);
					break;
				case 17: //创业培训
					intent.putExtra("url", Constants.IP+"wap/maintrain/mainTrain.jsp");
					intent.putExtra("title", "创业培训");
					startActivity(intent);
					break;
				case 18: //创业咨询
					intent.putExtra("url", Constants.IP+"wap/nomalquestion/listNomalQuestion");
					intent.putExtra("title", "创业咨询");
					startActivity(intent);
					break;
				case 19: //项目库
					intent.putExtra("url", Constants.IP+"wap/library/listLibrary");
					intent.putExtra("title", "创业项目库");
					startActivity(intent);
					break;
				case 20://项目征集
					intent.putExtra("url", Constants.IP+"wap/wapuser/viewProject?isAndroid=true");
					intent.putExtra("title", "创业项目征集");
					startActivity(intent);
					break;
				case 21: //创业典型
					intent.putExtra("url", Constants.IP+"wap/typical/listTypical?isAndroid=true");
					intent.putExtra("title", "创业典型");
					startActivity(intent);
					break;
				case 22: //15分钟创业服务圈
					intent.putExtra("url", Constants.IP+"wap/servicearea/roundGuide?isAndroid=true&openId=");
					intent.putExtra("title", "15分钟创业服务圈");
					startActivity(intent); 
					break;
				case 23: //孵化基地
					intent.putExtra("url", Constants.IP+"wap/incubators/listIncubators?isAndroid=true");
					intent.putExtra("title", "创业孵化基地");
					startActivity(intent);
					break;
				case 24: //指导专家
					intent.putExtra("url", Constants.IP+"wap/experts/listExperts?isAndroid=true");
					intent.putExtra("title", "创业指导专家");
					startActivity(intent);
					break;
				case 25: //政策法规
					intent1 = new Intent(getContext(), QztZCFGListActivity.class);
					startActivity(intent1);
					break;
				case 26: //就业新闻
					intent1 = new Intent(getContext(), QztTrainingActivity.class);
					startActivity(intent1);
					break;
				case 27: //培训信息
					intent1 = new Intent(getContext(), QztTrainingActivity.class);
					startActivity(intent1);
					break;
				case -99: //全部
					intent1 = new Intent(getContext(), MenuActivity.class);
					startActivity(intent1);
					break;
				default:
					break;
				}
				
			}
		}catch (Exception e) {
		}
	}
	
	/**
	 * 倒计时
	 */
	private void initTimer() {
		final TextView tvHour = (TextView) layout.findViewById(R.id.tv_hour);
		final TextView tvMinute = (TextView) layout
				.findViewById(R.id.tv_minute);
		final TextView tvSecond = (TextView) layout
				.findViewById(R.id.tv_second);
		tvHour.post(new Runnable() {

			@Override
			public void run() {
				if (secondCount > 0) {

					secondCount--;
					int h = secondCount / 3600;
					int m = secondCount / 60 % 60;
					int s = secondCount % 60;
					StringBuffer hour = new StringBuffer();
					StringBuffer minute = new StringBuffer();
					StringBuffer second = new StringBuffer();
					if (h < 10) {
						hour.append(0);
					}
					if (m < 10) {
						minute.append(0);
					}
					if (s < 10) {
						second.append(0);
					}
					hour.append(h);
					minute.append(m);
					second.append(s);
					tvHour.setText(hour);
					tvMinute.setText(minute);
					tvSecond.setText(second);
					tvHour.postDelayed(this, 1000);
				}
			}
		});
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getContext().unregisterReceiver(receiver);
	}
	
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			initGrid();
		}
	}
	
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				subList = userDal.doGetAreas1();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private int displayWidth;
	CommonAdapter itemAdapter;
	private List<Map<String, Object>> subList;
	
	public void showItem() {
		if (subList == null) {
			subList = new ArrayList<Map<String, Object>>();
		}
		int maxWidth = (int) (displayWidth * 0.8);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.qzt_main_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.qzt_dialog_view);
		TextView ptitle = (TextView) layout.findViewById(R.id.qzt_top_title);
		ptitle.setText("就业热线");
		ListView listView = (ListView) layout.findViewById(R.id.qzt_list_view);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					final String name = subList.get(arg2).get("name").toString().trim();
					final String tell = subList.get(arg2).get("phone").toString().trim();
					if (tell.equals("")) {
						return;
					}

					final Dialog builder = new Dialog(getContext(), R.style.dialog);
					builder.setContentView(R.layout.qzt_confirm_dialog);
					TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
					TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
					ptitle.setText(getContext().getString(R.string.alert_dialog_finish_title));
					pMsg.setText("确定要拔打" + name + "就业热线" + tell + "吗？");
					final Button confirm_btn = (Button) builder.findViewById(R.id.qzt_confirm_btn);
					Button cancel_btn = (Button) builder.findViewById(R.id.qzt_cancel_btn);
					confirm_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tell));
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
		itemAdapter = new CommonAdapter(this.getActivity(), subList, R.layout.qzt_main_dialog_item,
				new String[] { "name", "phone" }, new int[] { R.id.row_name, R.id.row_num }, listView);
		itemAdapter.setViewBinder();
		listView.setAdapter(itemAdapter);

		final Dialog builder = new Dialog(getContext(), R.style.dialog);
		builder.setContentView(layout, new LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}

	
	/**
	 * 页面上的按钮点击事件响应
	 * */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getContext(), CommonPageView.class);
		Intent intent1 = null;
		switch (v.getId()) {
		case R.id.btZpMore:				// 招聘信息	更多 
			intent1 = new Intent(getContext(), QztRecuitInfoActivity.class);
			ifLogin(Constants.iflogin,intent1);
			break;
		case R.id.btQzMore:				// 求职信息	更多
			/**
			 * 待完成
			 */
			break;
		case R.id.ivCydx:				// 创业典型
			intent.putExtra("url", Constants.IP+"wap/typical/listTypical?isAndroid=true");
			intent.putExtra("title", "创业典型");
			startActivity(intent);
			break;
		case R.id.layout_special3:		// 15分钟创业圈
			intent.putExtra("url", Constants.IP+"wap/servicearea/roundGuide?isAndroid=true&openId=");
			intent.putExtra("title", "15分钟创业服务圈");
			startActivity(intent);
			break;
		case R.id.layout_special4:		// 服务专家
			intent.putExtra("url", Constants.IP+"wap/experts/listExperts?isAndroid=true");
			intent.putExtra("title", "创业指导专家");
			startActivity(intent);
			break;
		case R.id.ivCyfw:				// 创业服务
			intent.putExtra("url", Constants.IP+"wap/serviceguide/listServiceGuide");
			intent.putExtra("title", "服务指南");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
