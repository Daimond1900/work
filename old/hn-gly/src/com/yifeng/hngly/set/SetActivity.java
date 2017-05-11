package com.yifeng.hngly.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yifeng.hngly.ui.BaseActivity;
import com.yifeng.hngly.ui.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 通许录
 * 
 * @author ZK
 */
public class SetActivity extends BaseActivity {
	private GridView toolbarGrid;
	private List<Map<String, Object>> menus;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);

		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		// toolbarGrid.setSelector(R.drawable.select_btns);
		// toolbarGrid.setBackgroundResource(R.drawable.tabbg);// 设置背景
		toolbarGrid.setNumColumns(3);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		toolbarGrid.setVerticalSpacing(15);// 垂直间隔
		toolbarGrid.setHorizontalSpacing(10);// 水平间隔
		
		loadMenuData();
		SimpleAdapter simperAdapter = new SimpleAdapter(this, menus,
				R.layout.set_menu, new String[] { "itemImage","itemText" },
				new int[] { R.id.item_imageg,R.id.item_textg});
		
		toolbarGrid.setAdapter(simperAdapter);// 设置菜单Adapter*/
		
		/** 监听菜单选项 **/
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			     Intent bl=(Intent)menus.get(arg2).get("intent");
			     startActivity(bl);
			}
		});

	}
	
	 private void loadMenuData(){
			menus=new ArrayList<Map<String,Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", "修改密码");
			map.put("itemImage", R.drawable.xgmm);
			map.put("tag", "mpjh");
			Intent  swap=new Intent(SetActivity.this,MyPassword.class);
			map.put("intent", swap);
			menus.add(map);
		}
}
