package com.winksoft.yzsmk.czpos.czrecord;

import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.utils.ListConversion;
import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.base.BaseActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CzRecord extends BaseActivity {

	private final int COUNT = 500; // 控制查询记录条数

	private List<Map<String, Object>> czQuery;	
	private ListView lt1;
	private SqliteUtil sqliteUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.czrecord_list);
		setTitle("充值记录");
		sqliteUtil = SqliteUtil.getInstance(this);
		initView();
	}

	/*
	 * 页面加载完成执行查询数据库 
	 */
	public void initView() {
		czQuery = sqliteUtil.doQuery("select * from czinfos order by insertdate desc limit " + COUNT,
				new String[] { "CitizenCardNo","TxnAmt", "BusinessNo" });

		if(czQuery!=null && czQuery.size()>0){
			for (Map<String, Object> map : czQuery) {
				String money = "";
				try{
					int parseInt = Integer.parseInt(map.get("TxnAmt").toString(), 16);
					money = String.format("%,.2f 元",parseInt/100.00);
				}catch (Exception e) {
				}
				map.put("money", money); //交易金额		
			}
		}
		
		lt1 = (ListView) this.findViewById(R.id.lt1);
		
		SimpleAdapter adapter = new SimpleAdapter(this, czQuery, R.layout.czrecord_list_item,
				new String[] { "CitizenCardNo", "money", "BusinessNo"},
				new int[] { R.id.CitizenCardNo, R.id.money, R.id.BusinessNo});
		lt1.setAdapter(adapter);
	}
}
