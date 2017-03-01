package com.yifeng.qzt.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.yifeng.cloud.entity.User;
import com.yifeng.hngly.data.UserDAL;
import com.yifeng.hngly.ui.LoginActivity;
import com.yifeng.hngly.util.CommonUtil;
import com.yifeng.hngly.util.ConstantUtil;
import com.yifeng.hngly.util.DataConvert;
import com.yifeng.hngly.util.UserSession;

/***
 * 用户认证
 * 
 * @author WuJiaHong 2012-06-18
 */
public class AuthLogin {
	public static final int HANDLER_MSG = 222;

	/** 闪屏登陆标志和登陆界面登陆标志 */
	public static final String LOGINAUTH = "loginAuth";
	public static final String LOADINGAUTH = "loadingAuth";
	private String authMethod = "";// 登陆线程执行完之后handler响应的方法
	public static User user = null;
	private String[] seletcs;
	private List<Map<String, String>> oalist = null;
	private Activity activity;
	private CommonUtil commonUtil;
	private UserDAL userDAL;
	private String userName = "", pwd = "";

	public AuthLogin(Activity activity) {
		this.activity = activity;
		commonUtil = new CommonUtil(activity);
		userDAL = new UserDAL(this.activity, new Handler());
	}

	/***
	 * 登录认证
	 * 
	 * @param userName
	 * @param pwd
	 */
	public void doAuthUser(String userName, String pwd, String authMethod) {
		this.userName = userName;
		this.pwd = pwd;
		this.authMethod = authMethod;

		new Thread(loginRunnable).start(); // 启动登陆线程
	}

	/***
	 * 执行远程云端Runnable
	 */
	private Runnable loginRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Log.v("系统自动登陆：", "自动登陆开始.....");

				Thread.sleep(500);

				Map<String, String> param = new HashMap<String, String>();
				param.put("name", userName);
				param.put("password", pwd);

				user = userDAL.loadUser(param);
				if (user.isSuccess()) {
					Message msg = new Message();
					msg.what = HANDLER_MSG;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = ConstantUtil.LOGIN_FAIL;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.v("自动登陆异常：", e.getMessage());
				Message msg = new Message();
				msg.what = ConstantUtil.LOGIN_FAIL;
				handler.sendMessage(msg);
			}
		}
	};

	public Handler handler ;
	
//	= new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//
//			if (msg.what == HANDLER_MSG) {// 登录线程执行完之后handler响应的方法
//				if (authMethod.equals(LOADINGAUTH)) {
//					loginLoading();// 闪屏登录
//				}
//
//			}
//
//		}
//
//	};

	/***
	 * 登陆线程执行完之后handler响应的方法 闪屏自动登录 线程执行完成后执行
	 */
	private void loginLoading() {
		if (user == null) {
			commonUtil.showToast("系统认证失败!");
			goLoginActivity();
		} else if (user.getState() == ConstantUtil.SERVER_ERROR) {
			commonUtil.showToast("服务器连接超时请重试!或服务器己关闭!");
			goLoginActivity();

		} else if (user.getState() == ConstantUtil.INNER_ERROR) {
			commonUtil.showToast("对不起，系统数据解析异常，请重试!");
			goLoginActivity();

		} else {
			if (!user.isSuccess()) {// 用户名密码不存在!由服务端返回的msg提示
				commonUtil.showToast(user.getMsg());
				goLoginActivity();
			} else if (user.getIsLocked() == ConstantUtil.LOCKED) {
				commonUtil.showToast("账号已被锁定,请联系管理员!");
				return;
			} else {
				setCompany();
			}
		}
	}

	/***
	 * 登陆线程执行完之后handler响应的方法 登录界面 线程执行完成后执行
	 */
	public void loginActivity() {
		if (user == null) {
			commonUtil.showToast("系统认证失败!");
			return;
		} else if (user.getState() == ConstantUtil.SERVER_ERROR) {
			commonUtil.showToast("服务器连接超时请重试!或服务器己关闭!");
			return;

		} else if (user.getState() == ConstantUtil.INNER_ERROR) {
			commonUtil.showToast("对不起，系统数据解析异常，请重试!");
			return;

		} else {
			if (!user.isSuccess()) {// 用户名密码不存在!由服务端返回的msg提示
				commonUtil.showToast(user.getMsg());
				return;
			} else if (user.getIsLocked() == ConstantUtil.LOCKED) {
				commonUtil.showToast("账号已被锁定,请联系管理员!");
				return;
			} else {
				// 选择多公司
				setCompany();
			}

		}
	}

	/**
	 * 公用 云端验证完成之后返回多公司情况 多公司认证选择, 如果有多个公司弹框选择,1个公司直接进入
	 */
	private void setCompany() {

		oalist = DataConvert.toArrayList(user.getGroup());

		seletcs = new String[oalist.size()];

		if (oalist.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			try {
				for (int i = 0; i < oalist.size(); i++) {
					Map<String, String> map = oalist.get(i);
					seletcs[i] = map.get("GROUP_NAME");
				}

				setLoginConfig(0);// 只有一个公司的直接

			} catch (Exception e) {
				e.printStackTrace();
				goLoginActivity();
			}

		} else {
			goLoginActivity();
		}
	}

	/***
	 * 公用 设置用户登陆信息及进入系统
	 * 
	 * @param index
	 */
	private void setLoginConfig(int index) {
		Map<String, String> oa = oalist.get(index);
		user.setGroup_locked(Integer.parseInt(oa.get("GROUP_LOCKED")));
		user.setCard_locked(Integer.parseInt(oa.get("CARD_LOCKED")));

		if (user.getGroup_locked() == ConstantUtil.LOCKED) {
			commonUtil.shortToast("公司被已被锁定!请联系管理员!");
			return;
		} else if (user.getCard_locked() == ConstantUtil.LOCKED) {
			commonUtil.shortToast("账号所在的公司已被锁定!请联系管理员!");
			return;
		}

		user.setOaIp(oa.get("DEPLOYMENT_ADDR"));
		user.setGroup_id(oa.get("GROUP_ID"));
		user.setGroup_name(oa.get("GROUP_NAME"));
		user.setLogo_path(oa.get("LOGO_PATH"));
		user.setUserName(oa.get("REAL_NAME"));
		user.setDepartmentid(oa.get("DEPARTMENTID"));
		user.setCardId(oa.get("CARD_ID"));
		user.setAddress(oa.get("LOCATION"));
		user.setUserEmail(oa.get("EMAIL"));
		user.setCompanyName(oa.get("GROUP_NAME"));
		user.setUserTel(oa.get("TEL_NO"));
		user.setMobileNo(oa.get("MOBILE_NO"));
		user.setPic_path(oa.get("PIC_PATH"));
		user.setDuty(oa.get("DUTY"));
		user.setEhome(oa.get("HOMEPAGE"));
		user.setCard_style(oa.get("TEMPLATE_ID"));

		if (user.getOaGroup() != null) {// 配置oa路径
			Map<String, String> oaparam = setOaConfig(user.getOaGroup());
			if (oaparam != null) {
				user.setOaToken(oaparam.get("token"));
				user.setOaIp(oaparam.get("server"));

			} else {
				user.setOaToken("");
				user.setOaIp("");
			}
		} else {
			user.setOaToken("");
			user.setOaIp("");
		}
	}

	/**
	 * 解析OA路径
	 * 
	 * @param json
	 * @return
	 */
	private Map<String, String> setOaConfig(String json) {
		Map<String, String> map = null;
		if (!json.equals("")) {

			map = DataConvert.toMap(json);

			// 如果解析失败返回空
			if (!map.get("state").equals(
					String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
				map = null;
			}
		}
		return map;
	}

	/**
	 * 公用 登陆成功正常进入系统
	 */

	public void goToSystem() {

		// 记住密码
		// doRememberPwd();

		// 保存到全局Session
		UserSession session = new UserSession(this.activity);
		session.setUser(user);

		ConstantUtil.IFLOGIN = true;// 是否登录标志

		// activity.startService(new Intent(activity,YFCloudSmsService.class));

		// 启动XMPP登陆 服务
		// activity.startService(new Intent(activity, YFService.class));

		// 设置系统初次始用为false
		// doSetIsFirstUse(false);

		commonUtil.doStartActivity();// 进入系统启动项

		// 在标题栏上显示当前运行程序
		commonUtil.systemDoing();

		this.activity.finish();

	}

	/***
	 * 转到登陆界面
	 */
	public void goLoginActivity() {
		Intent login = new Intent(this.activity, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		this.activity.startActivity(login);
		this.activity.finish();
	}

	/**
	 * 记住密码
	 * 
	 */
	public void doRememberPwd() {
		SharedPreferences rmd = this.activity.getSharedPreferences(
				ConstantUtil.PWD, 0);
		rmd.edit().putString("loginName", userName).putString("loginPwd", pwd)
				.commit();
	}

	/**
	 * 记住密码
	 * 
	 */
	public void doRemovePwd() {
		SharedPreferences rmd = this.activity.getSharedPreferences(
				ConstantUtil.PWD, 0);
		rmd.edit().putString("loginName", "").putString("loginPwd", "")
				.commit();
	}

	// 设置系统初次始用情况
	// public void doSetIsFirstUse(boolean flag) {
	// SharedPreferences rmd = this.activity.getSharedPreferences(
	// ConstantUtil.SETUP, 0);
	// rmd.edit().putBoolean("isFirstUse", flag).commit();
	// }
}
