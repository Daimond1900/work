package com.winksoft.android.yzjycy.ui.recruitInfor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.data.RecuitDal;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.winksoft.android.yzjycy.BaseActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

/***
 * 招聘信息
 * 
 * @author wujiahong 2012-10-19
 */
public class QztRecuitInfoActivity extends BaseActivity implements OnScrollListener {
	private final String TAG = "zpxx";
	private ListView listview;
	private RecuitDal reciotDal;
	private int pageNum = 0, lastItem = 0;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private CommonAdapter adapter;
	private Button back_btn, refresh_btn, search_btn, zpxi, djzw, rmzw, search;
	private EditText edtTxt_name, edtTxt_remark;
	private String c_name = "", area_id = "", c_remark = "";
	private boolean isLoading = true;// 标志正在加载数据
	private Spinner spn_area;
	ArrayAdapter<String> arrayAdapter;
	private ProgressDialog progressDialog;
	private List<Map<String, String>> areas;
	UserDAL userDal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_recruitment_information);
		reciotDal = new RecuitDal(this);
		userDal = new UserDAL(this);

		edtTxt_name = (EditText) findViewById(R.id.edtTxt_name);
		edtTxt_remark = (EditText) findViewById(R.id.edtTxt_remark);

		MyOnclick onclick = new MyOnclick();
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);

		refresh_btn = (Button) findViewById(R.id.refresh_btn);
		refresh_btn.setOnClickListener(onclick);

		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(onclick);

		// 招聘信息
		zpxi = (Button) findViewById(R.id.zpxi);
		zpxi.setOnClickListener(onclick);

		// 推荐职位
		djzw = (Button) findViewById(R.id.djzw);
		djzw.setOnClickListener(onclick);

		// 热门招聘
		rmzw = (Button) findViewById(R.id.rmzw);
		rmzw.setOnClickListener(onclick);

		// 历史搜索
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(onclick);

		spn_area = (Spinner) findViewById(R.id.spn_area); // 工作区域

		listview = (ListView) findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String positionId = list.get(arg2).get("acb210").toString();// 职位编号
					Intent intent = new Intent(QztRecuitInfoActivity.this, QztPositionView.class);
					intent.putExtra("title", "职位详情");
					intent.putExtra("positionId", positionId);
					intent.putExtra("companyId", list.get(arg2).get("aab001").toString());
					startActivity(intent);
				} catch (Exception e) {
					commonUtil.shortToast("未响应!");
				}
			}
		});

		showDialog("正在加载地点，请稍候...");
		new Thread(runnable).start();

		// 数据加载
		doLoadData();

	}

	private void showDialog(String msg) {
		progressDialog = ProgressDialog.show(this, null, msg, true);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
	}

	private void doLoadData() {
		if (pageNum == 0) {
			list.clear();
			listview.removeFooterView(commonUtil.loadingLayout);
			listview.addFooterView(commonUtil.addFootBar());
			adapter = new CommonAdapter(this, list, R.layout.qzt_recruitment_list_item,
					new String[] { "work", "money", "company", "date" },
					new int[] { R.id.workTxt, R.id.moneyTxt, R.id.companyTxt, R.id.dateTxt }, listview);

			listview.setAdapter(adapter);

		}
		if (isLoading) {
			isLoading = false;
			new Thread(recRunnable).start();
		}
	}

	/**
	 * 加载信息
	 */
	private Runnable recRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(500);
				returnList = reciotDal.doPostQuery(pageNum + "", c_name, area_id, c_remark);
				recHandler.sendEmptyMessage(100);

			} catch (Exception e) {
				Log.v("数据加载线出错:", e.getMessage());
				e.printStackTrace();
				recHandler.sendEmptyMessage(-1);
			}
		}
	};
	Handler recHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 100) {
				addData();
			}

			isLoading = true;
		}
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(100);
				areas = userDal.doUnLoginAreas();
				regeditHandler.sendEmptyMessage(5);

			} catch (Exception e) {
				e.printStackTrace();
				regeditHandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler regeditHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 5) {
				if (areas != null && areas.size() > 0) {
					initArea();
				}
			}
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		};
	};

	private void initArea() {
		SimpleAdapter adapter = new SimpleAdapter(this, areas, android.R.layout.simple_spinner_item,
				new String[] { "name" }, new int[] { android.R.id.text1 });
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_area.setAdapter(adapter);
		spn_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				area_id = areas.get(arg2).get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * 更新列表 数据源参数
	 * 
	 * AAB004 单位名称 ACA112 招聘工种 ACB216 工种说明 ACB21R 招聘总人数 ACC034 月工资 ACC214 福利待遇
	 * AAC039 外语水平 枚举表 AAC043 计算机水平 枚举表 AAC011 文化程度 枚举表 AAE013 其他说明及要求 ACB211
	 * 男性人数 ACB212 女性人数 ACB213 兼招人数 AAE030 有效起始日期 AAE031 有效终止日期 AAE036 经办日期
	 * ACB21O 职位编号 AAE004 单位联系人 AAE005 单位联系人电话 AAE020 经办机构 AAE006 单位地址 ACB202
	 * 用工区域 ACB203 工作地点 ACB204 地点详细
	 */
	private void addData() {

		pageNum++;

		String state = (String) returnList.get(0).get("state");

		if (state.equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				listview.removeFooterView(commonUtil.loadingLayout);
			for (int i = 0; i < returnList.size(); i++) {
				Map<String, Object> map = returnList.get(i);

				// 招聘总人数
				// int
				// peopleCount=Integer.parseInt(map.get("acb211").toString())+Integer.parseInt(map.get("acb212").toString())+Integer.parseInt(map.get("acb213").toString());
				map.put("work", map.get("acb216"));

				// 月工资
				String money = map.get("acc034") == null ? "" : map.get("acc034").toString();

				if (money.equals("")) {
					money = "面议";
				}
				map.put("money", "月薪:" + money);

				// 公司名称
				map.put("company", map.get("aab004") == null ? "" : map.get("aab004"));

				// 起始日期
				map.put("date", map.get("aae030") == null ? "" : map.get("aae030"));

				list.add(map);
			}
		} else {
			this.commonUtil.showListAddDataState(listview, state);
		}

		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}

	private class MyOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back_btn:
				QztRecuitInfoActivity.this.finish();
				break;
			case R.id.refresh_btn:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.search_btn:
				pageNum = 0;
				c_name = commonUtil.doConvertEmpty(edtTxt_name.getText().toString());
				c_remark = commonUtil.doConvertEmpty(edtTxt_remark.getText().toString());
				doLoadData();
				break;
			case R.id.zpxi:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.djzw:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.rmzw:
				pageNum = 0;
				doLoadData();
				break;
			case R.id.search:
				pageNum = 0;
				doLoadData();
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int state) {
		if (lastItem == adapter.count && state == OnScrollListener.SCROLL_STATE_IDLE) {
			doLoadData();
		}

	}

}
