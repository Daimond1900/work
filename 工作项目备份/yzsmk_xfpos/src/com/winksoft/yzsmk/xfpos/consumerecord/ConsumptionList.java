package com.winksoft.yzsmk.xfpos.consumerecord;

import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.utils.ListConversion;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ConsumptionList extends BaseActivity {

	private final int COUNT = 500; // 控制查询记录条数

	private List<Map<String, Object>> xfinfosQuery;	// xfinfos表  CPU类别：查询结果
	private List<Map<String, Object>> desfireQuery; // desfire表 Desfire类别：查询结果
	private ListView lt1;
	private SqliteUtil sqliteUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumption_list);
		setTitle("交易记录");
		sqliteUtil = SqliteUtil.getInstance(this);
		initView();
	}

	/*
	 * 页面加载完成执行查询数据库 
	 */
	public void initView() {
		/* 查询xfinfos表  ----insertdate:交易日期 -----body_87：交易金额 */
		xfinfosQuery = sqliteUtil.doQuery("select * from xfinfos order by insertdate desc limit " + COUNT,
				new String[] { "jobNum","insertdate", "body_79" });
		/* 格式化数据 */
		if(xfinfosQuery!=null && xfinfosQuery.size()>0){
			/*xfinfos: 表名  ||  0 ：交易记录格式化数据类型 */
			xfinfosQuery = ListConversion.formatDate(xfinfosQuery, "xfinfos", 0);
		}
		
		//***************************************************************** 
		
		/* 查询desfire表  ----insertdate:交易日期 -----consume_money：交易金额 */
		desfireQuery = sqliteUtil.doQuery("select * from disfire order by insertdate desc limit " + COUNT,
				new String[] {"jobNum", "insertdate", "consume_money" });
		/* 格式化数据 */
		if(desfireQuery!=null && desfireQuery.size()>0){
			desfireQuery = ListConversion.formatDate(desfireQuery, "disfire", 0);
		}
		
		/* 合并数据 */
		List<Map<String,Object>> twoList2oneList = ListConversion.twoList2oneList(xfinfosQuery, desfireQuery, COUNT);
		
		/* 对合并数据中 money格式化*/
		for (Map<String, Object> map : twoList2oneList) {
			String money = "";
			try{
				int parseInt = Integer.parseInt(map.get("money").toString(), 16);
				money = String.format("%,.2f 元",parseInt/100.00);
			}catch (Exception e) {
			}
			map.put("money", money); //交易金额		
		}
		
		lt1 = (ListView) this.findViewById(R.id.lt1);
		
		SimpleAdapter adapter = new SimpleAdapter(this, twoList2oneList, R.layout.consumption_list_item,
				new String[] { "jobNum", "time", "money", "category" },
				new int[] { R.id.jobNumber, R.id.time, R.id.money, R.id.category });
		lt1.setAdapter(adapter);
	}
}
