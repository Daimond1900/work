package com.yifeng.ChifCloud12345update;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class PaiDanActivity extends TabActivity {
	Button   back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdtab);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PaiDanActivity.this.finish();
			}
		});
		
		final TabHost tabHost = getTabHost();
		tabHost.setup();  
        final TabWidget tabWidget = tabHost.getTabWidget();  

	
		tabHost.addTab(tabHost.newTabSpec("weipai").setIndicator("δ��(7)")
				.setContent(new Intent(this, WeiPaiActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("yipai").setIndicator("����(6)")
				.setContent(new Intent(this, YiPaiActivity.class)));
		tabHost.setCurrentTab(0);
		
		int height = 50;
		
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			/**
			 * ���ø߶ȡ���ȣ����������������Ϊfill_parent���ڴ˶���ûЧ��
			 */
			tabWidget.getChildAt(i).getLayoutParams().height = height;

			/**
			 * ����tab�б������ֵ���ɫ����ȻĬ��Ϊ��ɫ
			 */
			final TextView tv = (TextView) tabWidget.getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.white));
			View v = tabWidget.getChildAt(i);
			if (tabHost.getCurrentTab() == i) {
				v.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.menubg));
			} else {
				v.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.navbgfocus));
			}
		}
		
		/**
		 * �����tabѡ���ʱ�򣬸��ĵ�ǰ�ı���
		 */
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < tabWidget.getChildCount(); i++) {
					View v = tabWidget.getChildAt(i);
					if (tabHost.getCurrentTab() == i) {
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.menubg));
					} else {
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.navbgfocus));
					}
				}
				if (tabId != null && tabId.equals("yipai")) {
					YiPaiActivity yp=(YiPaiActivity)PaiDanActivity.this.getCurrentActivity();
					yp.pageNum = 0;
					yp.lastItem = 0;
					yp.doSetListView();
					
				}else{
					WeiPaiActivity wp=(WeiPaiActivity)PaiDanActivity.this.getCurrentActivity();
					wp.pageNum = 0;
					wp.lastItem = 0;
					wp.doSetListView();
				}

			}
		});
	}
}
