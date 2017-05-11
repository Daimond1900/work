package com.yifeng.hnqzt.ui.ambitus;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.adapter.CommonAdapter;
import com.yifeng.hnqzt.ui.BaseActivity;

import java.util.*;
/***
 * 周边信息
 * @author wujiahong
 *
 */
public class AmbitusInformation extends BaseActivity implements OnClickListener{
    private CommonAdapter adapter;
	private ListView listView;
	private List<Map<String, Object>> data;
	private Button backBtn,screen_btn;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ambitus_list);
		listView=(ListView)findViewById(R.id.listview);
		backBtn=(Button)findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		screen_btn=(Button)findViewById(R.id.screen_btn);
		screen_btn.setOnClickListener(this);
		
		loadData();
	}
	private void loadData(){
		data=new ArrayList<Map<String,Object>>();
		for (int i = 4; i < 15; i++) {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("distance", "(2012-09-27)-距我"+i+"000米");
			map.put("duty", "电子高薪聘工程师");
			map.put("address", "扬州市维扬路130号");
			map.put("price", i+"000元");
			data.add(map);
		}
		adapter=new CommonAdapter(this, data, R.layout.ambitus_list_item, new String[]{"distance","duty","address","price"}, 
				new int[]{R.id.distance,R.id.duty,R.id.address,R.id.price}, listView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					Intent bl=new Intent(AmbitusInformation.this,AmbitusDetail.class);
					startActivity(bl);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			AmbitusInformation.this.finish();
			break;
		case R.id.screen_btn:
			Intent bl=new Intent(AmbitusInformation.this,Search.class);
			startActivity(bl);
			break;
		default:
			break;
		}
	}
	
}
