package com.winksoft.nox.android.ui.util;

import com.winksoft.android.yzjycy.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewUtil {
	//查询状态   SERVER_ERROR=-1服务器异常，LOGIN_FAIL=0找不到，LOGIN_SUCCESS=1加载成功，INNER_ERROR=2数据解析异常
	//ERROR兼容第二种解析。
	public static final int KEY_ERROR=-2;
	public static final int SERVER_ERROR=-1;
	public static final int LOGIN_FAIL=0;
	public static final int LOGIN_FAIL1=-3;
	public static final int LOGIN_SUCCESS=1;
	public static final int INNER_ERROR=2;
	public static final int IS_EMPTY=0;//空值
	public static final int DATA_NULL=4;//返回null
	public static final int EXISTS=4;//存在
	public static final int LOCKED=1;//锁定
	public static final int ERROR=100;//锁定
public	String ALERTTEXT="";
	private Resources res;
	public ListViewUtil(Context context,ListView listview, Resources res) {
		this.context=context;
		this.listview=listview;
		this.res=res;
	}
	
	Context context;
	ProgressBar progressBar;
	LinearLayout loadingLayout;
	ListView listview;
	public void addFootBar() {
		LinearLayout searchLayout = new LinearLayout(context);
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);
		progressBar = new ProgressBar(context);
		
		Drawable d=res.getDrawable(R.anim.publicloading);
		progressBar.setIndeterminateDrawable(d);
		
		progressBar.setPadding(0, 10, 15, 10);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		TextView textView = new TextView(context);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);
		if(loadingLayout!=null)	listview.removeFooterView(loadingLayout);{
		loadingLayout = new LinearLayout(context);
		loadingLayout.setClickable(false);
		}
		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		loadingLayout.setGravity(Gravity.CENTER);
		listview.addFooterView(loadingLayout);
	}
	
	public void removeFootBar() {
		
		listview.removeFooterView(loadingLayout);
		loadingLayout=null;
	}
	
	public void showListAddDataState(String state){
		if(state==null||state.equals(ERROR+"")){
			removeFootBar();
			 
			 Toast.makeText(context,ALERTTEXT.equals("") ?"无更多数据":ALERTTEXT, Toast.LENGTH_SHORT).show();
			 ALERTTEXT="";
		}
		
		if (state
				.equals(String.valueOf(  SERVER_ERROR))) {
			Toast.makeText(context, "服务器未响应", Toast.LENGTH_SHORT).show();
			 
			 removeFootBar();
		}

		else if (state
				.equals(String.valueOf( INNER_ERROR))) {
			Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
			 removeFootBar();

		} else if (state.equals(String.valueOf( IS_EMPTY))) {
			 removeFootBar();
			 
			 Toast.makeText(context,ALERTTEXT.equals("") ?"无更多数据":ALERTTEXT, Toast.LENGTH_SHORT).show();
			 ALERTTEXT="";
		} 
		else if(state
				.equals(String.valueOf( DATA_NULL))){
			 removeFootBar();
			 Toast.makeText(context, "服务端放回null", Toast.LENGTH_SHORT).show();
		}else if(state
				.equals(String.valueOf( KEY_ERROR))){
			 removeFootBar();
			 
			 Toast.makeText(context, "重复登录", Toast.LENGTH_SHORT).show();
		}
	}

}
