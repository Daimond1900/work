package com.winksoft.android.yzjycy.util;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * comment:弹出框类
 * @author:ZhangYan
 * Date:2012-7-24
 */
public class DialogUtil {
	private Activity context;

	public DialogUtil(Activity context) {
		this.context = context;
	}
	
	/**
	 * 自定义退出程序
	 */
	public void doAdvanceExit() {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.qzt_confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(context.getString(R.string.alert_dialog_finish_title));
		pMsg.setText(context.getString(R.string.alert_dialog_finish));
		final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				confirm_btn.setEnabled(false);
				sysExit();
			}
		});
		
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
		builder.show();

	}
	
	
	/***
	 * 拨打电话对话框
	 * @param title
	 * @param msg
	 * @param telNo
	 */
	public void showCallDialog(String title,String msg,final String telNo) {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.qzt_confirm_dialog); 
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		final Button confirm_btn = (Button) builder.findViewById(R.id.qzt_confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.qzt_cancel_btn);
		confirm_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!telNo.equals("")){
					Intent callTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telNo));  
					context.startActivity(callTel);
				}else{
					Toast.makeText(context, "无效的电话号码", Toast.LENGTH_SHORT).show();
				}
				builder.dismiss();
			}
		});
		
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
		builder.show();

	}
	
	
	//退出
    public void sysExit(){
    	Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);
		
		ActivityStackControlUtil.finishProgram();//清除所有activity
		
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
    	
    }
	
	/**
	 * 系统自带弹出框
	 */
	public void doExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setIcon(R.drawable.qzt_ic_launcher);
		builder.setTitle(R.string.alert_dialog_finish_title);
		builder.setMessage(R.string.alert_dialog_finish);
		builder.setPositiveButton(R.string.alert_dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//CommonUtil.doLogOut();
						//exit();
					}
				});
		builder.setNegativeButton(R.string.alert_dialog_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		builder.show();
	}
	
	/**
	 * 
	 * @param 信息提示长时间显示
	 */
	public void showToast(String context){
		Toast.makeText(this.context, context, Toast.LENGTH_LONG).show();
	}
	/**
	 * 
	 * @param 信息提示短时间显示
	 */
	public void shortToast(String context){
		Toast.makeText(this.context, context, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @return
	 */
	public void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}
	
	
	
	/***
	 * 自定义确定框
	 * @param title
	 * @param Msg
	 * @param mHandler
	 * @param resultMsg 返回消息包 由调用者处理
	 */
	public void showMsg(String title, String msg,final Handler mHandler,final int resultMsg) {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.zpt_confirm_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(Html.fromHtml(msg));
		Button btn = (Button) builder.findViewById(R.id.confirm_btn);
		Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mHandler!=null)
				 mHandler.sendEmptyMessage(resultMsg);
				else
				 Toast.makeText(context, "Handler 没有实例化!", Toast.LENGTH_LONG).show();
				
				builder.dismiss();
			}
		});
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}
	
	/**
	 * 提示对话框
	 * 
	 * @param context
	 * @return
	 */
	public void showMsgs(String title, String msg) {
		final Dialog builder = new Dialog(context, R.style.dialog);
		builder.setContentView(R.layout.qzt_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
				sysExit();
			}
		});
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}
	
	
	/**
	 * 网络连接异常
	 * @param context
	 */
	public void alertNetError() {
		final Dialog dialog = new Dialog(context,R.style.dialog);
		dialog.setContentView(R.layout.qzt_network_set);
		final Button update = (Button)dialog.findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));//网络设置
			}
		});
		final Button close = (Button)dialog.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//context.finish();//退出
			}
		});
		dialog.show();
	}
}
