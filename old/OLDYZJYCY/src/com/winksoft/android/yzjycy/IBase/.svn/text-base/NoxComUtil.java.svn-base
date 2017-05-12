package com.winksoft.android.yzjycy.IBase;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.yifeng.nox.android.util.Constants;
import com.yifeng.nox.android.util.ThemeUtils;

public class NoxComUtil {
	private static NoxComUtil instance;

	public static NoxComUtil getinstance() {
		if (instance == null)
			instance = new NoxComUtil();
		return instance;
	}

	/***
	 * 弹出确认框
	 * 
	 * @param context
	 * @param title
	 * @param Msg
	 * @param ICD
	 */
	public void showConfirmDialog(Context context, String title, String Msg,
			final IConfirmDialog ICD) {
		if (ICD == null)
			return;

		try {
			final Dialog builder = new Dialog(context,
					ThemeUtils
							.getResourceId(context, "dialog", Constants.STYLE));
			builder.setContentView(ThemeUtils.getResourceId(context,
					"confirm_dialog", Constants.LAYOUT));
			TextView ptitle = (TextView) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "pTitle"));
			TextView pMsg = (TextView) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "pMsg"));
			ptitle.setText(title);
			pMsg.setText(Html.fromHtml(Msg));
			final Button confirm_btn = (Button) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "confirm_btn"));
			Button cancel_btn = (Button) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "cancel_btn"));
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					builder.dismiss();
					ICD.onConfirm();
				}
			});
			cancel_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					builder.dismiss();
					ICD.onCancel();
				}
			});
			builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog

			builder.show();

		} catch (Exception e) {
			Toast.makeText(context, "初始化失败!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	/***
	 * 弹出单个确认按钮
	 * 
	 * @param context
	 * @param title
	 * @param Msg
	 * @param ICD
	 */
	public void showHintDialog(Context context, String title, String Msg,
			final IConfirmDialog ICD) {
		if (ICD == null)
			return;

		try {
			final Dialog builder = new Dialog(context,
					ThemeUtils
							.getResourceId(context, "dialog", Constants.STYLE));
			builder.setContentView(ThemeUtils.getResourceId(context,
					"confirm_dialog", Constants.LAYOUT));
			TextView ptitle = (TextView) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "pTitle"));
			TextView pMsg = (TextView) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "pMsg"));
			ptitle.setText(title);
			pMsg.setText(Html.fromHtml(Msg));
			final Button confirm_btn = (Button) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "confirm_btn"));
			Button cancel_btn = (Button) builder.findViewById(ThemeUtils
					.findResClassId(context, Constants.R_ID, "cancel_btn"));
			cancel_btn.setVisibility(View.GONE);
			confirm_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					builder.dismiss();
					ICD.onConfirm();
				}
			});
			builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
			builder.setCancelable(false);
			builder.show();

		} catch (Exception e) {
			Toast.makeText(context, "初始化失败!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}
}
