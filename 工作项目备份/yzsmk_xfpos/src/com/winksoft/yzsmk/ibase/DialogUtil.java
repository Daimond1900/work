package com.winksoft.yzsmk.ibase;

import com.winksoft.yzsmk.ibase.IDialog.AddEmployeeListener;
import com.winksoft.yzsmk.ibase.IDialog.PwdListener;
import com.winksoft.yzsmk.xfpos.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogUtil {
	private static DialogUtil instance;
	private Dialog builder;
	private DialogUtil(){}

	public static DialogUtil getinstance() {
		if (instance == null)
			instance = new DialogUtil();
		return instance;
	}

	/**
	 * 输入密码
	 * @param context
	 * @param dialog
	 */
	public void showPwdDialog(Context context,final PwdListener dialog) {
		try {
			builder = new Dialog(context,R.style.dialog);
			builder.setContentView(R.layout.pwd_dialog);
			final EditText etPwd = (EditText) builder.findViewById(R.id.etPwd);
			Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.onConfirm(etPwd.getText().toString().trim());
				}
			});
			builder.setCanceledOnTouchOutside(true);
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加员工信息
	 * @param context
	 * @param dialog
	 */
	public void showAddDialog(Context context,String title,final AddEmployeeListener dialog) {
		try {
			builder = new Dialog(context,R.style.dialog);
			builder.setContentView(R.layout.activity_signin);
			
			TextView tvTitle = (TextView) builder.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
			final EditText etNo = (EditText) builder.findViewById(R.id.etNo);
			final EditText etPwd = (EditText) builder.findViewById(R.id.etPwd);
			
			Button btSignin = (Button) builder.findViewById(R.id.btSignin);
			btSignin.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.onConfirm(etNo.getText().toString().trim(),etPwd.getText().toString().trim());
				}
			});
			builder.setCanceledOnTouchOutside(true);
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeDialog(){
		if(builder != null && builder.isShowing()){
			builder.dismiss();
			builder = null;
		}
	}
}
