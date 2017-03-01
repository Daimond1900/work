package com.yifeng.ChifCloud12345.service;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.baidu.android.pushservice.PushConstants;
import com.yifeng.ChifCloud12345update.ImportantEventActivity;
import com.yifeng.ChifCloud12345update.LoginActivity;
import com.yifeng.ChifCloud12345update.MainActivity;
import com.yifeng.ChifCloud12345update.MainPageActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.data.AlertDAL;
import com.yifeng.entity.User;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ActivityStackControlUtil;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DataConvert;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	AlertDialog.Builder builder;
	private NotificationManager nm;
	private AlertDAL alertDal;
	private int isAlert = 0;
	
	    
	

	/**
	 * 
	 * 
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d(TAG, ">>> Receive intent: \r\n" + intent);
		loadSource(context,intent);
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// 获取消息内容
			String json = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			if(json!=null){
				Map<String,String> map  = DataConvert.toMap(json);
				if(map.get("state").equals("1")){
					String title = map.get("title");
					String content = map.get("content");
					if(content.trim().length() > 15){
						content = content.substring(0,15)+"...";
					}
					doSendMsg(context, title,content);
				}else{
					doSendMsg(context,"","");
				}
			}else{
 				doSendMsg(context,"","");
			}
			ConstantUtil.STATE = false;
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// 处理绑定等方法的返回数据
			// PushManager.startWork()的返回�?通过PushConstants.METHOD_BIND得到
			// 获取方法
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			// 方法返回错误码�?若绑定返回错误（�?），则应用将不能正常接收消息�?
			// 绑定失败的原因有多种，如网络原因，或access token过期�?
			// 请不要在出错时进行简单的startWork调用，这有可能导致死循环�?
			// 可以通过限制重试次数，或者在其他时机重新调用来解决�?
			final int errorCode = intent
					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
							PushConstants.ERROR_SUCCESS);
			// 返回内容
			final String content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));

			// 用户在此自定义处理消�?以下代码为demo界面展示�?
			Log.d(TAG, "onMessage: method : " + method);
			Log.d(TAG, "onMessage: result : " + errorCode);
			Log.d(TAG, "onMessage: content : " + content);

			Intent responseIntent = null;
			responseIntent = new Intent(Utils.ACTION_RESPONSE);
			responseIntent.putExtra(Utils.RESPONSE_METHOD, method);
			responseIntent.putExtra(Utils.RESPONSE_ERRCODE, errorCode);
			responseIntent.putExtra(Utils.RESPONSE_CONTENT, content);
			// responseIntent.setClass(context, MainActivity.class);
			responseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			handleIntent(responseIntent);
			// context.startActivity(responseIntent);

			// 可�?。�?知用户点击事件处�?
		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			// Log.d(TAG, "intent=" + intent.toUri(0));
			Intent aIntent = new Intent();
			aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			aIntent.setClass(context, MainActivity.class);
			String title = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, title);
			String content = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
			aIntent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, content);
			
			context.startActivity(aIntent);
		}
	}

	public static String ZYTX = "0";
	public static String CSGD = "0";
	public static String LDPS = "0";
	public static String GQSK = "0";
	public static String SPXW = "0";

	/***
	 * 发送通知提醒
	 * 
	 * @param id
	 */ 
	private void doSendMsg(Context context,String title,String content) {
		String service = Context.NOTIFICATION_SERVICE;
		nm = (NotificationManager) context.getSystemService(service); // get
		Notification n = new Notification();
		n.icon = R.drawable.sys_alert;// 通知图标
		n.tickerText = "12345热线重要提醒!";// 状态栏显示的通知文本提示
		n.when = System.currentTimeMillis();// 通知产生的时间，会在通知信息里显示
		// 如果想在Ongoing列表项中显示通知，加上这一句就OK ，如果在通知栏里就把下面一句话去掉
		n.flags = Notification.FLAG_AUTO_CANCEL;
		// 添加声音
		 n.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;;
		Intent intent = null;
		User user = LoginBiz.loadUser(context);
		alertDal = new AlertDAL(context);
		Map<String, String> param = new HashMap<String, String>();
		if (user == null) {
			intent = new Intent(context, LoginActivity.class);// 如果用户还没登陆先到登陆界面
		} else {
			// 点击该通知后要跳转的 TabMainActivity
//			intent = new Intent(context, MainPageActivity.class);
			SharedPreferences shareds = context.getSharedPreferences("PWD", 0);
			SharedPreferences shared = context.getSharedPreferences("loginstate", 0);
			
			if(!shareds.getString("loginPwd", "").equals("")){
				intent = new Intent(context,MainPageActivity.class);
			}else if(shared.getString("loginName", "").equals("") &&
					shareds.getString("loginPwd", "").equals("")){
				intent = new Intent(context,LoginActivity.class);
			}else{
				intent = new Intent(context,MainPageActivity.class);
			}
			intent.putExtra("tag", "0");
			intent.putExtra("isRead", "0");
			param.put("user_id", user.getUserId());
			param.put("department_id", user.getOrg_id());
			param.put("role_id", user.getRole_type());

			Map<String, String> map = alertDal.doAlert(param);
			if (map.get("state").equals("1")) {// 数据加载成功
				/***
				 * zytx：重要提醒 ； ldps：领导批示; csgd:超时工单
				 */
				ZYTX = map.get("zytx") == null ? "0" : map.get("zytx");
				CSGD = map.get("overtime") == null ? "0" : map.get("overtime");
				SPXW = map.get("spxw") == null ? "0" : map.get("spxw");
				GQSK = map.get("gqsp") == null ? "0" : map.get("gqsp");
				String str = "";
				if (ZYTX != null && !ZYTX.equals("0")) {
					str += "重要提醒:" + ZYTX;
				}
				if (CSGD != null && !CSGD.equals("0"))
					str += "超时工单:" + CSGD;
				if (LDPS != null && !LDPS.equals("0"))
					str += "领导批示:" + LDPS;
				if (!str.equals("")) {
					// if (ZYTX!=null&&!ZYTX.equals("0")){
					// n.defaults |=Notification.DEFAULT_VIBRATE;
					// long[] vibrate = {0,50,100,150};
					// n.vibrate = vibrate;
					// }
					PendingIntent pi = PendingIntent.getActivity(context, 0,
							intent, 0);
					n.setLatestEventInfo(context, title, content, pi);
					nm.notify(2, n);
				}
				// }

			}else{
				PendingIntent pi = PendingIntent.getActivity(context, 0,
						intent, 0);
				n.setLatestEventInfo(context, title, content, pi);
				nm.notify(2, n);
			}
		}
		// System.out.println("正在运行：SendNotificationService===每格一秒执行一次:"+id);
		// Log.v("12345定时提醒：", "====================每格一秒执行一次:"+id);
	}
	
	
	
	public void loadSource(Context context,Intent intent){
		Map<String, String> param = new HashMap<String, String>();
		User user = LoginBiz.loadUser(context);
		alertDal = new AlertDAL(context);
		intent = new Intent(context,MainPageActivity.class);
		intent.putExtra("tag", "0");
		intent.putExtra("isRead", "0");
		param.put("user_id", user.getUserId());
		param.put("department_id", user.getOrg_id());
		param.put("role_id", user.getRole_type());
		Map<String, String> map = alertDal.doAlert(param);
		if (map.get("state").equals("1")) {// 数据加载成功
			/***
			 * zytx：重要提醒 ； ldps：领导批示; csgd:超时工单
			 */
			ZYTX = map.get("zytx") == null ? "0" : map.get("zytx");
			CSGD = map.get("overtime") == null ? "0" : map.get("overtime");
			SPXW = map.get("spxw") == null ? "0" : map.get("spxw");
			GQSK = map.get("gqsp") == null ? "0" : map.get("gqsp");
		}
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (Utils.ACTION_RESPONSE.equals(action)) {
			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);
			if (PushConstants.METHOD_BIND.equals(method)) {
				int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent
							.getStringExtra(Utils.RESPONSE_CONTENT);
					String appid = "";
					String channelid = "";
					String userid = "";
					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");

						Log.i(TAG, "appid: " + appid);
						Log.i(TAG, "channel_id: " + channelid);
						Log.i(TAG, "user_id: " + userid);
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}
				} else {
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}
			}
		}
	}
}
