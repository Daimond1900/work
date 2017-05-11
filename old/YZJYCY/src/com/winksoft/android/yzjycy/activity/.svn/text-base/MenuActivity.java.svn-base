package com.winksoft.android.yzjycy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.widget.DragGridView;
import com.winksoft.android.widget.MyGridView;
import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.adapter.DragAdapter;
import com.winksoft.android.yzjycy.adapter.OtherAdapter;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.db.MainManage;
import com.winksoft.android.yzjycy.entity.MenuItem;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.bminfo.Zpt_RegisterCountListActivity;
import com.winksoft.android.yzjycy.ui.enterprise.ZptEnterpriseActivity;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.ui.job.QztMyJobActivity;
import com.winksoft.android.yzjycy.ui.ldlxx.MainList;
import com.winksoft.android.yzjycy.ui.pxxx.KqInfoActivity;
import com.winksoft.android.yzjycy.ui.pxxx.PxxxActivity;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztRecuitInfoActivity;
import com.winksoft.android.yzjycy.ui.recruitmanage.Zpt_ManageListActivity;
import com.winksoft.android.yzjycy.ui.registration.Zpt_RecruitmentActivity;
import com.winksoft.android.yzjycy.ui.resume.QztPersonalResumeActivity;
import com.winksoft.android.yzjycy.ui.training.QztTrainingActivity;
import com.winksoft.android.yzjycy.ui.zcfg.QztZCFGListActivity;
import com.winksoft.android.yzjycy.ui.zerotransfer.TransferList;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	@SetView(id = R.id.topTitle)
	TextView topTitle;
	private Button textBtn, backBtn;
	private MyGridView jyGv, cyGv, zxGv, pxGv;
	private DragGridView mUserGv;
	private List<MenuItem> mUserList;
	private UserDAL userDal;
	private pxDAL dal;
	private List<MenuItem> jyList = new ArrayList<MenuItem>();
	private List<MenuItem> cyList = new ArrayList<MenuItem>();
	private List<MenuItem> zxList = new ArrayList<MenuItem>();
	private List<MenuItem> pxList = new ArrayList<MenuItem>();
	private OtherAdapter jyAdapter, cyAdapter, zxAdapter, pxAdapter;
	private DragAdapter mUserAdapter;
	private boolean isEdit = false; // 是否编辑
	private List<MenuItem> menuItems;
	private MainManage manage;
	private Map<String, String> kqCountMap; // 考勤课数
	private List<Map<String, String>> kqCountStr;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		dal = new pxDAL(this, new Handler());
		UserSession session = new UserSession(this);
		user = session.getUser();

		topTitle.setText("全部分类");
		displayWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		manage = new MainManage(this);
		initView();
		userDal = new UserDAL(this);
		new Thread(runnable).start();
	}

	public void initView() {
		textBtn = (Button) findViewById(R.id.textBtn);
		textBtn.setVisibility(View.VISIBLE);
		textBtn.setText("管理");
		textBtn.setOnClickListener(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		mUserGv = (DragGridView) findViewById(R.id.userGridView);
		jyGv = (MyGridView) findViewById(R.id.jyGridView);
		cyGv = (MyGridView) findViewById(R.id.cyGridView);
		zxGv = (MyGridView) findViewById(R.id.zxGridView);
		pxGv = (MyGridView) findViewById(R.id.pxGridView); // 新加的

		mUserList = manage.getUserChannel();
		menuItems = manage.getAllChannel();
		for (int i = 0; i < menuItems.size(); i++) {
			MenuItem item = menuItems.get(i);
			if (item.getGroupId() == 1) {
				jyList.add(item);
			} else if (item.getGroupId() == 2) {
				cyList.add(item);
			} else if (item.getGroupId() == 3) {
				zxList.add(item);
			} else if (item.getGroupId() == 4) { // 培训
				pxList.add(item);
			}
		}

		mUserAdapter = new DragAdapter(this, mUserList, true) {
			@Override
			protected void delView(View view, int position) {
				setUserView(view, position, mUserGv, mUserAdapter);
			}
		};

		jyAdapter = new OtherAdapter(this, jyList, false) {
			@Override
			protected void addView(View view, int position) {
				setOtherView(view, position, jyGv, jyAdapter);
			}
		};
		cyAdapter = new OtherAdapter(this, cyList, false) {
			@Override
			protected void addView(View view, int position) {
				setOtherView(view, position, cyGv, cyAdapter);
			}
		};
		zxAdapter = new OtherAdapter(this, zxList, false) {
			@Override
			protected void addView(View view, int position) {
				setOtherView(view, position, zxGv, zxAdapter);
			}
		};
		pxAdapter = new OtherAdapter(this, pxList, false) {
			@Override
			protected void addView(View view, int position) {
				setOtherView(view, position, pxGv, pxAdapter);
			}
		};
		mUserGv.setAdapter(mUserAdapter);
		jyGv.setAdapter(jyAdapter);
		cyGv.setAdapter(cyAdapter);
		zxGv.setAdapter(zxAdapter);
		pxGv.setAdapter(pxAdapter); // 培训
		mUserGv.setOnItemClickListener(this);
		jyGv.setOnItemClickListener(this);
		cyGv.setOnItemClickListener(this);
		zxGv.setOnItemClickListener(this);
		pxGv.setOnItemClickListener(this);

	}

	/**
	 * 获取点击的Item的对应View，
	 * 因为点击的Item已经有了自己归属的父容器MyGridView，所有我们要是有一个ImageView来代替Item移动
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器 用于存放我们移动的View
	 */
	private ViewGroup getMoveViewGroup() {
		// window中最顶层的view
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final MenuItem moveChannel,
			final BaseAdapter adapter, final boolean isUser) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// try{
				moveViewGroup.removeView(mMoveView);
				// 判断点击的是DragGrid还是OtherGridView
				if (isUser) {
					int groupId = moveChannel.getGroupId();
					// 获取终点的坐标
					switch (groupId) {
					case 1:
						jyAdapter.setVisible(true);
						jyAdapter.notifyDataSetChanged();
						break;
					case 2:
						cyAdapter.setVisible(true);
						cyAdapter.notifyDataSetChanged();
						break;
					case 3:
						zxAdapter.setVisible(true);
						zxAdapter.notifyDataSetChanged();
						break;
					case 4:
						pxAdapter.setVisible(true);
						pxAdapter.notifyDataSetChanged();
						break;
					default:
						break;
					}
					mUserAdapter.remove();
				} else {
					mUserAdapter.setVisible(true);
					mUserAdapter.notifyDataSetChanged();
					((OtherAdapter) adapter).remove();
				}
				// }catch (Exception e) {
				// }
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		if (isEdit)
			return;

		MenuItem item = (MenuItem) parent.getAdapter().getItem(position);
		int itemId = item.getId();

		Intent intent = new Intent(MenuActivity.this, CommonPageView.class);

		Intent intent1 = null;
		switch (itemId) {
		case 1: // 招聘信息
			intent1 = new Intent(MenuActivity.this, QztRecuitInfoActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 2: // 求职信息

			break;
		case 3: // 我的简历
			intent1 = new Intent(MenuActivity.this, QztPersonalResumeActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 4: // 我的求职
			intent1 = new Intent(MenuActivity.this, QztMyJobActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 5: // 企业信息
			intent1 = new Intent(MenuActivity.this, ZptEnterpriseActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 6: // 招聘发布
			intent1 = new Intent(MenuActivity.this, Zpt_RecruitmentActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 7: // 招聘管理
			intent1 = new Intent(MenuActivity.this, Zpt_ManageListActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 8: // 报名信息
			intent1 = new Intent(MenuActivity.this, Zpt_RegisterCountListActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 9: // 录用查询
			intent1 = new Intent(MenuActivity.this, Zpt_EmployCountListActivity.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 10: // 劳动力信息
			intent1 = new Intent(MenuActivity.this, MainList.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 11: // 零转移
			intent1 = new Intent(MenuActivity.this, TransferList.class);
			ifLogin(Constants.iflogin, intent1);
			break;
		case 12: // 每日一新
			break;
		case 13: // 通讯录

			break;
		case 14: // 就业热线
			if (Constants.iflogin) {
				showItem();
			} else {
				intent1 = new Intent(this, LoginActivity.class);
				startActivity(intent1);
			}
			break;
		case 15: // 创业政策
			intent.putExtra("url", Constants.IP + "wap/entreppolicy/listEntrepPolicy");
			intent.putExtra("title", "创业政策");
			startActivity(intent);
			break;
		case 16: // 服务指南
			intent.putExtra("url", Constants.IP + "wap/serviceguide/listServiceGuide");
			intent.putExtra("title", "服务指南");
			startActivity(intent);
			break;
		case 17: // 创业培训
			intent.putExtra("url", Constants.IP + "wap/maintrain/mainTrain.jsp");
			intent.putExtra("title", "创业培训");
			startActivity(intent);
			break;
		case 18: // 创业咨询
			intent.putExtra("url", Constants.IP + "wap/nomalquestion/listNomalQuestion");
			intent.putExtra("title", "创业咨询");
			startActivity(intent);
			break;
		case 19: // 项目库
			intent.putExtra("url", Constants.IP + "wap/library/listLibrary");
			intent.putExtra("title", "创业项目库");
			startActivity(intent);
			break;
		case 20:// 项目征集
			intent.putExtra("url", Constants.IP + "wap/wapuser/viewProject?isAndroid=true");
			intent.putExtra("title", "创业项目征集");
			startActivity(intent);
			break;
		case 21: // 创业典型
			intent.putExtra("url", Constants.IP + "wap/typical/listTypical?isAndroid=true");
			intent.putExtra("title", "创业典型");
			startActivity(intent);
			break;
		case 22: // 15分钟创业服务圈
			intent.putExtra("url", Constants.IP + "wap/servicearea/roundGuide?isAndroid=true&openId=");
			intent.putExtra("title", "创业服务圈");
			startActivity(intent);
			break;
		case 23: // 孵化基地
			intent.putExtra("url", Constants.IP + "wap/incubators/listIncubators?isAndroid=true");
			intent.putExtra("title", "创业孵化基地");
			startActivity(intent);
			break;
		case 24: // 指导专家
			intent.putExtra("url", Constants.IP + "wap/experts/listExperts?isAndroid=true");
			intent.putExtra("title", "创业指导专家");
			startActivity(intent);
			break;
		case 25: // 政策法规
			intent1 = new Intent(MenuActivity.this, QztZCFGListActivity.class);
			startActivity(intent1);
			break;
		case 26: // 就业新闻
			intent1 = new Intent(MenuActivity.this, QztTrainingActivity.class);
			startActivity(intent1);
			break;
		/*
		 * case 27: // 培训信息 intent1 = new Intent(MenuActivity.this,
		 * QztTrainingActivity.class); startActivity(intent1); break;
		 */
		case 28: // 培训信息
			intent1 = new Intent(MenuActivity.this, PxxxActivity.class);
			startActivity(intent1);
			break;
		case 29: // 考勤信息
			if (Constants.iflogin) {
				new Thread(kqRunnable).start();
			} else {
				intent1 = new Intent(this, LoginActivity.class);
				startActivity(intent1);
			}

			/*
			 * 做判断 报名课数 0：Toast还没有报名 1： 直接
			 */
			// intent1 = new Intent(MenuActivity.this, KqInfoActivity.class);
			// startActivity(intent1);
			break;
		default:
			break;
		}
	}

	private void setUserView(View view, final int position, final DragGridView gv, final DragAdapter adapter) {
		final ImageView moveImageView = getView(view);
		if (moveImageView != null) {
			TextView newTextView = (TextView) view.findViewById(R.id.text_item);
			final int[] startLocation = new int[2];
			newTextView.getLocationInWindow(startLocation);
			final MenuItem channel = adapter.getItem(position);// 获取点击的频道内容
			// 获取分组id
			final int groupId = channel.getGroupId();
			new Handler().postDelayed(new Runnable() {
				public void run() {
					try {
						int[] endLocation = new int[2];
						// 获取终点的坐标
						switch (groupId) {
						case 1:
							jyAdapter.setVisible(false);
							int pos = getPosition(channel, jyAdapter);
							jyAdapter.setItem(pos);
							jyGv.getChildAt(pos).getLocationInWindow(endLocation);
							break;
						case 2:
							cyAdapter.setVisible(false);
							pos = getPosition(channel, cyAdapter);
							cyAdapter.setItem(pos);
							cyGv.getChildAt(pos).getLocationInWindow(endLocation);
							break;
						case 3:
							zxAdapter.setVisible(false);
							pos = getPosition(channel, zxAdapter);
							zxAdapter.setItem(pos);
							zxGv.getChildAt(pos).getLocationInWindow(endLocation);
							break;
						case 4:
							pxAdapter.setVisible(false);
							pos = getPosition(channel, pxAdapter);
							pxAdapter.setItem(pos);
							pxGv.getChildAt(pos).getLocationInWindow(endLocation);
							break;
						default:
							break;
						}
						MoveAnim(moveImageView, startLocation, endLocation, channel, adapter, true);
						adapter.setRemove(position);
					} catch (Exception localException) {
						localException.printStackTrace();
					}
				}
			}, 50L);
		}
	}

	/**
	 * 返回我的应用点击item，所在就业、创业、资讯列表的对应位置
	 * 
	 * @param item
	 * @param adapter
	 * @return
	 */
	private int getPosition(MenuItem item, OtherAdapter adapter) {
		int pos = -1;
		int id = item.getId();
		List<MenuItem> items = adapter.getChannnelLst();
		for (int i = 0; i < items.size(); i++) {
			MenuItem item2 = items.get(i);
			if (id == item2.getId()) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	private void setOtherView(View view, final int position, final MyGridView gv, final OtherAdapter adapter) {
		final ImageView moveImageView = getView(view);
		if (moveImageView != null) {
			TextView newTextView = (TextView) view.findViewById(R.id.text_item);
			final int[] startLocation = new int[2];
			newTextView.getLocationInWindow(startLocation);
			final MenuItem channel = adapter.getItem(position);
			mUserAdapter.setVisible(false);
			// 添加到最后一个
			mUserAdapter.addItem(channel);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					try {
						int[] endLocation = new int[2];
						// 获取终点的坐标
						mUserGv.getChildAt(mUserGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
						MoveAnim(moveImageView, startLocation, endLocation, channel, adapter, false);
						adapter.setRemove(position);
					} catch (Exception localException) {

					}
				}
			}, 50L);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textBtn:
			if (!isEdit) {
				setIsEdit("完成", true);
			} else {
				setIsEdit("管理", false);
				saveChannel();
			}
			break;
		case R.id.backBtn:
			this.finish();
			break;
		default:
			break;
		}
	}

	/*
	 * 点击考勤，检查账户是否报名
	 */
	Runnable kqRunnable = new Runnable() { // 检查考勤（报名培训的课数）
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				kqCountStr = dal.doKqInfoQuery("",Constants.iflogin ? user.getUserId() : "");
				loadHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
//				Map<String, String> map = DataConvert.toMap(kqCountStr);
				if (kqCountStr != null && kqCountStr.size() >0 ) {
						Intent i = new Intent(MenuActivity.this, KqInfoActivity.class);
						startActivity(i);
				}else {
					showToast("您还没有报名呢！", false);
				}
			}
		};
	};

	/**
	 * 管理应用
	 * 
	 * @param title
	 * @param flag
	 */
	private void setIsEdit(String title, boolean flag) {
		isEdit = flag;
		textBtn.setText(title);
		mUserGv.setEdit(flag);
		mUserAdapter.setIsShowDelete(flag);
		jyAdapter.setIsShowDelete(flag);
		cyAdapter.setIsShowDelete(flag);
		zxAdapter.setIsShowDelete(flag);
		pxAdapter.setIsShowDelete(flag);
	}

	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		manage.deleteAllChannel();
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (int i = 0; i < jyList.size(); i++) {
			MenuItem item = jyList.get(i);
			menuItems.add(item);
		}
		for (int i = 0; i < cyList.size(); i++) {
			MenuItem item = cyList.get(i);
			menuItems.add(item);
		}
		for (int i = 0; i < zxList.size(); i++) {
			MenuItem item = zxList.get(i);
			menuItems.add(item);
		}
		for (int i = 0; i < pxList.size(); i++) {
			MenuItem item = pxList.get(i);
			menuItems.add(item);
		}
		for (int i = 0; i < mUserList.size(); i++) {
			MenuItem item = mUserList.get(i);
			for (int j = 0; j < menuItems.size(); j++) {
				MenuItem item1 = menuItems.get(j);
				if (item.getId() == item1.getId()) {
					item1.setOrderId(i + 1);
					break;
				}
			}
		}
		manage.saveChannel(menuItems);

		sendBroadcast(new Intent(Constants.BROADCAST_FILTER.REFRESH_MENUS));
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
		LayoutInflater inflater = LayoutInflater.from(this);
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

					final Dialog builder = new Dialog(MenuActivity.this, R.style.dialog);
					builder.setContentView(R.layout.qzt_confirm_dialog);
					TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
					TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
					ptitle.setText(MenuActivity.this.getString(R.string.alert_dialog_finish_title));
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
		itemAdapter = new CommonAdapter(this, subList, R.layout.qzt_main_dialog_item, new String[] { "name", "phone" },
				new int[] { R.id.row_name, R.id.row_num }, listView);
		itemAdapter.setViewBinder();
		listView.setAdapter(itemAdapter);

		final Dialog builder = new Dialog(this, R.style.dialog);
		builder.setContentView(layout, new LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}

	/**
	 * 判断
	 * 
	 * @param iflogin
	 * @param intent
	 */
	private void ifLogin(boolean iflogin, Intent intent) {
		if (iflogin) {
			startActivity(intent);
		} else {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
		}
	}
}
