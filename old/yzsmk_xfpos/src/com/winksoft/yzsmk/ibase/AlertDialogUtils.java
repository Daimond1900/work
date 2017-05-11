package com.winksoft.yzsmk.ibase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogUtils {

	public static void deleteChooseDialog(Activity context,String title,OnClickListener clickListener){
		AlertDialog alertDialog = new AlertDialog.Builder(context)
		.setTitle(title)
		.setPositiveButton("确定", clickListener)
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		alertDialog.show();
	}

	public static void showDialog(Context context, int statSysWarning,
			String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
		.setIcon(statSysWarning)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		alertDialog.show();
	}
}
