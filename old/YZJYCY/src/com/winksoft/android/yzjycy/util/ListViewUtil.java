package com.winksoft.android.yzjycy.util;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.Constants;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewUtil {
public	String ALERTTEXT="";
	
	public ListViewUtil(Context context,ListView listview) {
		this.context=context;
		this.listview=listview;
	}
	
	Context context;
	ProgressBar progressBar;
	LinearLayout loadingLayout;
	ListView listview;
	
	public void addFootBar() {
		LinearLayout searchLayout = new LinearLayout(context);
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);
		progressBar = new ProgressBar(context);
		
		Drawable d= context.getResources().getDrawable(R.anim.publicloading);
		progressBar.setIndeterminateDrawable(d);
		
		progressBar.setPadding(0, 0, 15, 0);
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
		
		
		if (state
				.equals(String.valueOf(Constants.SERVER_ERROR))) {
			Toast.makeText(context, "服务器未响应", Toast.LENGTH_SHORT).show();
			 
			 removeFootBar();
		}

		else if (state
				.equals(String.valueOf(Constants.INNER_ERROR))) {
			Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
			 removeFootBar();

		} else if (state.equals(String.valueOf(Constants.IS_EMPTY))) {
			 removeFootBar();
			 
			 Toast.makeText(context,ALERTTEXT.equals("") ?"无更多数据":ALERTTEXT, Toast.LENGTH_SHORT).show();
			 ALERTTEXT="";
		} 
		else if(state
				.equals(String.valueOf(Constants.DATA_NULL))){
			 removeFootBar();
			 Toast.makeText(context, "服务端放回null", Toast.LENGTH_SHORT).show();
		}else if(state
				.equals(String.valueOf(Constants.KEY_ERROR))){
			 removeFootBar();
			 
			 Toast.makeText(context, "重复登录", Toast.LENGTH_SHORT).show();
		}
	}

}
