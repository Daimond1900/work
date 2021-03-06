package com.winksoft.android.yzjycy.ui.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.data.QztEnterpriseDal;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztPositionView;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 地理位置 列表
 */
public class QztPositionList extends BaseActivity implements OnScrollListener{
	private ListView listView;
	private CommonAdapter adapter;
	private int pageNum = 0,lastItem = 0;
	private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
	private QztEnterpriseDal enterDal;
	private String companyId="";
	private Button back_btn;
	private TextView companyName;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_position_list);
		enterDal=new QztEnterpriseDal(this);
		companyName=(TextView)findViewById(R.id.companyName);
		
		companyId=this.getIntent().getStringExtra("companyId")==null?"":this.getIntent().getStringExtra("companyId");
		companyName.setText(this.getIntent().getStringExtra("companyName")==null?"":this.getIntent().getStringExtra("companyName"));
		
		listView=(ListView)findViewById(R.id.listview);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			   try{
					 Intent bl=new Intent(QztPositionList.this,QztPositionView.class);
					 bl.putExtra("positionId", list.get(arg2).get("acb210").toString());
					 bl.putExtra("companyId", list.get(arg2).get("aab001").toString());
					 bl.putExtra("isClick", true);//进到职位详细信息 公司名称不可点
					 
					 startActivity(bl);
				}catch(Exception e){
					e.printStackTrace();
					commonUtil.shortToast("未响应!");
				}
			}
		});
		
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QztPositionList.this.finish();
			}
		});
		
		loadData();
		
	}
   
   
   private void loadData()
	{
		if (pageNum == 0)
		{
			list.clear();
			listView.removeFooterView(commonUtil.loadingLayout);
			listView.addFooterView(commonUtil.addFootBar());
			adapter=new CommonAdapter(this, list, R.layout.qzt_position_list_item, new String[]{"positionDate","positionMoney","positionName","positionAddress"}, 
					new int[]{R.id.positionDate,R.id.positionMoney,R.id.positionName,R.id.positionAddress}, listView);
			listView.setAdapter(adapter);

		}
		new Thread(recRunnable).start();
	}
   
   /**
	 * 加载信息
	 */
	private Runnable recRunnable = new Runnable()
	{
		public void run()
		{
			try
			{
				Thread.sleep(500);
				returnList = enterDal.doQueryPosition(companyId,pageNum);
				recHandler.sendEmptyMessage(100);

			} catch (Exception e)
			{
				Log.v("数据加载线出错:", e.getMessage());
				e.printStackTrace();
				recHandler.sendEmptyMessage(-1);
			}
		}
	};
	Handler recHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 100)
			{
				addData();
			}
		}
	};
	
	/**
	 * 更新列表
	 */
	private void addData()
	{
		
		pageNum++;
		
		String state = (String) returnList.get(0).get("state");

		if (state.equals(String.valueOf(Constants.LOGIN_SUCCESS)))
		    {
				if (returnList.size() < 10)
					listView.removeFooterView(commonUtil.loadingLayout);
				for (int i = 0; i < returnList.size(); i++)
				{
					Map<String, Object> map = returnList.get(i);
					//月工资
					String money = map.get("acc034") == null ? "" : map.get("acc034").toString();

					if (money.equals(""))
					{
						money = "面议";
					}
					//招聘总人数
					int peopleCount=Integer.parseInt(map.get("acb211").toString())+Integer.parseInt(map.get("acb212").toString())+Integer.parseInt(map.get("acb213").toString());
					map.put("positionName", map.get("acb216")+"("+peopleCount+")");
					map.put("positionDate", "发布:"+map.get("aae030"));//起始日期
					map.put("positionMoney", "月薪:"+money);//求职数
					map.put("positionAddress", map.get("aae006"));//地址
					list.add(map);
				}
			} 
		   else{
				this.commonUtil.showListAddDataState(listView, state);
			}

		
		adapter.count = list.size();
		adapter.notifyDataSetChanged();

	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount){
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (lastItem == adapter.count
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			loadData();
		}
	}
    
    
}
