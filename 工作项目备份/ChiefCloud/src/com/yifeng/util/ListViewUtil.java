package com.yifeng.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewUtil {
	
	
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
		progressBar.setPadding(0, 0, 15, 0);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		TextView textView = new TextView(context);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);
		if(loadingLayout!=null)	listview.removeFooterView(loadingLayout);
		loadingLayout = new LinearLayout(context);
		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		loadingLayout.setGravity(Gravity.CENTER);
		listview.addFooterView(loadingLayout);
	}
	
	public void removeFootBar() {
		
		
		listview.removeFooterView(loadingLayout);

	}
	
	public void showListAddDataState(String state){
		
		
		if (state
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
//			Toast.makeText(context, "服务器未响应", Toast.LENGTH_SHORT).show();
			 
			 removeFootBar();
		}

		else if (state
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
			 removeFootBar();

		} else if (state
				.equals(String.valueOf(ConstantUtil.IS_EMPTY))) {
			 removeFootBar();
			 Toast.makeText(context, "无新数据", Toast.LENGTH_SHORT).show();
		} 
		else if(state
				.equals(String.valueOf(ConstantUtil.DATA_NULL))){
			 removeFootBar();
			 Toast.makeText(context, "服务端放回null", Toast.LENGTH_SHORT).show();
		}else if(state
				.equals(String.valueOf(ConstantUtil.KEY_ERROR))){
			 removeFootBar();
			 
			 Toast.makeText(context, "重复登录", Toast.LENGTH_SHORT).show();
		}
	}

}
