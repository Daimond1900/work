package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yifeng.adapter.HomeAdapter;
/**
 * 联系电话
 * @author Administrator
 *
 */
public class PhoneListActivity extends BaseActivity {
	ListView listview;
	HomeAdapter adapter;
	LinearLayout top;
	Button   back;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lxdh);
		listview = (ListView) findViewById(R.id.listview);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhoneListActivity.this.finish();
			}
		});
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		adapter = new HomeAdapter(this, list, R.layout.lxdh_item,
				new String[] { "content","phone" }, new int[] { R.id.content,R.id.phone },getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 
				
//				Intent intent = new Intent(PhoneListActivity.this,
//						PolicyActivty.class);
//				intent.putExtra("name", (String)list.get(position).get("content"));
				Intent phoneIntent = new Intent(
						"android.intent.action.CALL", Uri
								.parse("tel:"+
										list.get(position).get("phone")));
				startActivity(phoneIntent);
//				switch (position) {
//				case 0:
////					Intent intent = new Intent(ReadyToDoActivity.this,
////							HotLinkActivity.class);
////					startActivity(intent);
//
//					break;
//
//				default:
//					break;
//				}

			}
		});
		initBottom();
		setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
		addData();
		adapter.notifyDataSetChanged();
	}

	void addData() {
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("content", "江都区");
		map.put("phone", "86299131");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "仪征市");
		map.put("phone", "83418444");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "高邮市");
		map.put("phone", "84611910");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "宝应县");
		map.put("phone", "88282714");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "广陵区");
		map.put("phone", "87345675");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "维扬区");
		map.put("phone", "87636050");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "邗江区");
		map.put("phone", "87860247");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市经济开发区管委会");
		map.put("phone", "87962217");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市化工园管委会");
		map.put("phone", "89185821");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "新城西区管委会");
		map.put("phone", "82931023");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "蜀岗-瘦西湖管委会");
		map.put("phone", "87937015");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市发改委");
		map.put("phone", "87961635");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市经信委");
		map.put("phone", "87867755");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市建设局");
		map.put("phone", "87329110");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市房管局");
		map.put("phone", "87347652");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市科技局");
		map.put("phone", "87938531");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市地震局");
		map.put("phone", "87347772");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市教育局");
		map.put("phone", "87361100");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市公安局");
		map.put("phone", "87031199");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市监察局");
		map.put("phone", "87865356");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市民政局");
		map.put("phone", "87340875");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市司法局");
		map.put("phone", "   12348");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市财政局");
		map.put("phone", "87863540");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市人社局");
		map.put("phone", "   12333");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市国土局");
		map.put("phone", "   12336");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市物价局");
		map.put("phone", "   12358");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市统计局");
		map.put("phone", "87863606");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市粮食局");
		map.put("phone", "87342803");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市工商局");
		map.put("phone", "   12315");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市质监局");
		map.put("phone", "   12365");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市食品药品监督局");
		map.put("phone", "87782890");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市安监局");
		map.put("phone", "87963500");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市环保局");
		map.put("phone", "   12369");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市规划局");
		map.put("phone", "87366135");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市城管局");
		map.put("phone", "   12319");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市交通局");
		map.put("phone", "   96196");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市农工办  ");
		map.put("phone", "87347722");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市农委");
		map.put("phone", "87341522");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市水利局");
		map.put("phone", "87340437");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市农开局");
		map.put("phone", "87333214");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市农机局");
		map.put("phone", "80988203");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市供销社");
		map.put("phone", "87342124");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市商务局");
		map.put("phone", "87859332");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市旅游局");
		map.put("phone", "87340828");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市园林局");
		map.put("phone", "87346051");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市文广新局");
		map.put("phone", "87347420");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市卫生局");
		map.put("phone", "87959782");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市体育局");
		map.put("phone", "87338173");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市人口计生委");
		map.put("phone", "   12356");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市民宗局");
		map.put("phone", "87347638");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市民防局");
		map.put("phone", "82990070");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市外办");
		map.put("phone", "87782306");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市侨办");
		map.put("phone", "87863622");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市政府法制办");
		map.put("phone", "80986605");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市级机关事务管理局");
		map.put("phone", "87881868");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市国资委");
		map.put("phone", "87993800");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市国税局");
		map.put("phone", "   12366");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市地税局");
		map.put("phone", "   12366");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市气象局");
		map.put("phone", "87870213");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市盐务局");
		map.put("phone", "87021997");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市检验检疫局");
		map.put("phone", "87865526");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市公积金管理中心");
		map.put("phone", " 9688988");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市公安局交巡警支队");
		map.put("phone", "87032013");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市残联");
		map.put("phone", "87956183");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "移动");
		map.put("phone", "   10086");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "联通");
		map.put("phone", "   10010");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "电信");
		map.put("phone", "   10000");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市供电公司");
		map.put("phone", "   95598");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市邮政局");
		map.put("phone", "   11185");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市广电集团");
		map.put("phone","   96296");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市报业集团");
		map.put("phone", "   96496");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市自来水公司");
		map.put("phone", "87877777");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市长途汽车站");
		map.put("phone", "87963658");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市公交公司");
		map.put("phone", "87341937");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市绿化队");
		map.put("phone", "15052508883");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "火车站");
		map.put("phone", "85546001");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "市信访局");
		map.put("phone", "87861651");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "汽车东站");
		map.put("phone", "80975207");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "江都交通局");
		map.put("phone", "86553848");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "江都运管所");
		map.put("phone", "86843153");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "高邮交通局");
		map.put("phone", "84613409");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "高邮运管所");
		map.put("phone", "84610077");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "仪征交通局");
		map.put("phone", "83441032");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "仪征运管所");
		map.put("phone", "83420525");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "邗江交通局");
		map.put("phone", "87862376");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "邗江运管处");
		map.put("phone", "87770619");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "宝应交通局");
		map.put("phone", "88244160");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("content", "宝应交管所");
		map.put("phone", "88222027");
		list.add(map);
	}
	 

}