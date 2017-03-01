package com.yifeng.hnjcy.util;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.hnjcy.ui.AppContext;
import com.yifeng.hnjcy.ui.R;

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
		
		Drawable d=AppContext.get().getResources().getDrawable(R.anim.publicloading);
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
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			Toast.makeText(context, "服务器未响应", Toast.LENGTH_SHORT).show();
			 
			 removeFootBar();
		}

		else if (state
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			Toast.makeText(context, "数据解析失败", Toast.LENGTH_SHORT).show();
			 removeFootBar();

		} else if (state.equals(String.valueOf(ConstantUtil.IS_EMPTY))) {
			 removeFootBar();
			 
			 Toast.makeText(context,ALERTTEXT.equals("") ?"无更多数据":ALERTTEXT, Toast.LENGTH_SHORT).show();
			 ALERTTEXT="";
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
