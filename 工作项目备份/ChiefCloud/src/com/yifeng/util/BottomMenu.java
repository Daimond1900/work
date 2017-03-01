package com.yifeng.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.yifeng.ChifCloud12345update.HomeActivity;
import com.yifeng.ChifCloud12345update.MainPageActivity;
import com.yifeng.ChifCloud12345update.MayorActivity;
import com.yifeng.ChifCloud12345update.MyWorkActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.microalloy.InteractiveActivty;

public class BottomMenu extends Activity {
	public Button bt_bottom_menu1, bt_bottom_menu2, bt_bottom_menu3, bt_bottom_menu4,
			bt_bottom_menu5;
	public Context context;

	public void initBottom() {

		bt_bottom_menu1 = (Button) findViewById(R.id.bt_bottom_menu1);
		bt_bottom_menu2 = (Button) findViewById(R.id.bt_bottom_menu2);
		bt_bottom_menu3 = (Button) findViewById(R.id.bt_bottom_menu3);
		bt_bottom_menu4 = (Button) findViewById(R.id.bt_bottom_menu4);
		bt_bottom_menu5 = (Button) findViewById(R.id.bt_bottom_menu5);
		bt_bottom_menu1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Intent intent = new Intent(context,
							HomeActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		bt_bottom_menu2.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Intent intent = new Intent(context,
						MyWorkActivity.class);
				startActivity(intent);
				finish();
			}
		});
		bt_bottom_menu3.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Intent intent = new Intent(context,
							InteractiveActivty.class);
					startActivity(intent);
					finish();
				}
			}
		});
		bt_bottom_menu4.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Intent intent = new Intent(context,
							MainPageActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		bt_bottom_menu5.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					Intent intent = new Intent(context,
							MayorActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});

	}
	public void setFocus( Button menu,int drawable){
		menu.setBackgroundResource(drawable);
		menu.setFocusable(false);
	}
	/**
	 * 退出
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doExit();
			return false;
		}
		return false;
	}

	public void doExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出");
		builder.setMessage("确定退出？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(startMain);
				System.exit(0);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();

	}
}
