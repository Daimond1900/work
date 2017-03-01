package com.yifeng.ChifCloud12345.attention;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.ChifCloud12345update.WebViewForMyWorkActivty;
import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.data.ReadyToDoDal;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 我的关注
 * 
 * @author Administrator
 * 
 */

public class AttentionActivity extends BaseActivity implements OnScrollListener {
	private ListView listview;
	private HomeAdapter adapter;
	private LinearLayout top;
	private Button back;
	private ProgressBar progressBar;
    private LinearLayout loadingLayout;
    private int lastItem = 0;
    private String type_id="10031109341234120160";
    private int pageNum = 0;
    private ReadyToDoDal readyDal;
    private TextView title;
    private ListViewUtil util;
	String JY_COUNT,TS_COUNT,ZX_COUNT,QZ_COUNT,JB_COUNT;
    /***
     *
	10031109341234120160	咨询
	11052520593959390002	建议
	10082511353735370003	投诉
	11030818023423400050	求助
	11101309094894800003	举报
     */
    private Map<String,String> param;
	private ArrayList<Map<String, Object>> list= new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csgd);
		listview = (ListView) findViewById(R.id.listview);
		param=new HashMap<String, String>();
		dal = new FormDAL(this);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AttentionActivity.this.finish();
			}
		});
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		util=new ListViewUtil(this, listview);
		title=(TextView)findViewById(R.id.title);
		title.setText("我的关注");
		Intent intent = getIntent();
		JY_COUNT= intent.getStringExtra("JY_COUNT");
		TS_COUNT= intent.getStringExtra("TS_COUNT");
		ZX_COUNT= intent.getStringExtra("ZX_COUNT");
		QZ_COUNT= intent.getStringExtra("QZ_COUNT");
		JB_COUNT= intent.getStringExtra("JB_COUNT");
    	listview.setOnScrollListener(this);
    	
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AttentionActivity.this,
						WebViewForMyWorkActivty.class);
				intent.putExtra("title_name", "我的关注明细");
				intent.putExtra("url","android/form/doQueryFormDetail?key="+user.getKey()+"&form_id="+list.get(position).get("FORM_ID"));
				startActivity(intent);
			}
		});

		//加载信息
		doSetListView();
		
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
		this.initTaskMenu();
	}

	 private void doSetListView(){    	
	    	if(pageNum==0){
	    		list.clear();
				util.addFootBar();
				adapter = new HomeAdapter(this, list, R.layout.ldps_gdlb_item,
						new String[] { "CONTENT_TEXT", "ADDTIME", "TITLE", "icon1",
								"icon2", "icon3", "icon4" }, new int[] {
								R.id.content, R.id.time, R.id.title, R.id.icon1,
								R.id.icon2, R.id.icon3, R.id.icon4 },getResources());

				adapter.setViewBinder();
				adapter.need2=rushHandler;
				adapter.assignHandler=assignHandler;
				listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
	    	}
	    	/**参数**/
	    	param.put("u_id", user.getUserId());
	    	param.put("form_type", type_id);
	    	param.put("page", pageNum + "");
			new Thread(gorupRunnable).start();
	    }
		Handler assignHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String form_id = msg.getData().getString("form_id");

				List<Map<String, String>> tlist = dal.doQueryMyAssignByForm(LoginBiz
						.loadUser(AttentionActivity.this).getUserId(), form_id);
	String tstr="";
	boolean ifShow=false;
				for(Map<String,String> m:tlist){
					String ASSIGN_COMMENT= m.get("ASSIGN_COMMENT")==null?"":m.get("ASSIGN_COMMENT");
					String REPLY_COMMENT= m.get("REPLY_COMMENT")==null?"":m.get("REPLY_COMMENT");
					
					tstr+=m.get("ASSIGN_NAME")+"于"+m.get("ASSIGN_TIME")+"批示："+ASSIGN_COMMENT+'\n';
					if (m.get("ASSIGN_STATUS") .equals("2"))
					tstr+=m.get("TARGET_NAME")+"于"+m.get("UPDATE_TIME")+"回复："+REPLY_COMMENT+'\n';
					else 
						tstr+=m.get("TARGET_NAME")+"还未回复。\n";
					if(m.get("ASSIGN_TIME")!=null)ifShow=true;
				}
				if(ifShow)
				AttentionActivity.this.commonUtil.showMsg("回复内容", tstr);
				else 
					AttentionActivity.this.commonUtil.showMsg("提示", "非本人指派不能看到详情。");
			}

		};
	    @Override
		public void onScroll(AbsListView v, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

		@Override
		public void onScrollStateChanged(AbsListView v, int state) {

			if (lastItem == adapter.count
					&& state == OnScrollListener.SCROLL_STATE_IDLE) {
				new Thread(gorupRunnable).start();
			}
		}

		/**
		 * 加载信息详细
		 */
		private Runnable gorupRunnable = new Runnable() {
			public void run() {
				try {
					Thread.sleep(100);
					gorupHandler.sendMessage(gorupHandler.obtainMessage());
				} catch (InterruptedException e) {
				}
			}
		};
		Handler gorupHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				addData();

			}
		};
		FormDAL dal;
		void addData() {

			Resources res = getResources();

			List<Map<String, String>> returnList = dal.doQueryMyConcern(LoginBiz.loadUser(this).getUserId(), type_id);
			pageNum++;
			String state=returnList.get(0).get("state");
			  if (state
					.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
				// Resources res = getResources();
				if (returnList.size() < 10)
					util.removeFootBar();

				for (int i = 0; i < returnList.size(); i++) {
					Map  tmap = returnList.get(i);
					String role_type = (String) tmap.get("role_type_str");

					if (role_type != null) {
						tmap.put("icon1",
								((role_type.indexOf("10") > -1) ? 
										BitmapFactory.decodeResource(res, R.drawable.icon1)
										:BitmapFactory.decodeResource(res,R.drawable.none)));
						tmap.put("icon2",
								((role_type.indexOf("20") > -1) ?
										BitmapFactory.decodeResource(res, R.drawable.icon2)
										:BitmapFactory.decodeResource(res,R.drawable.none)));
						tmap.put("icon3",
								((role_type.indexOf("30") > -1) ? 
										BitmapFactory.decodeResource(res, R.drawable.icon3)
										: BitmapFactory.decodeResource(res,
												R.drawable.none)));
						tmap.put("icon4",
								((role_type.indexOf("40") > -1) ? BitmapFactory
										.decodeResource(res, R.drawable.icon4)
										: BitmapFactory.decodeResource(res,
												R.drawable.none)));
					}else {
						
						tmap.put( "icon1",
							 BitmapFactory.decodeResource(res,
												R.drawable.none));
						tmap.put( "icon2",
							 BitmapFactory.decodeResource(res,
												R.drawable.none));
						tmap.put(
								"icon3",
							 BitmapFactory.decodeResource(res,
												R.drawable.none));
						tmap.put(
								"icon4",
							 BitmapFactory.decodeResource(res,
												R.drawable.none));
					}

					tmap.put("ADDTIME",
							DateUtil.getDate((String) tmap.get("CREATE_DATE")));

					list.add(tmap);
				}

			}else {
				
				util.showListAddDataState(state);
			}
			adapter.count = list.size();
			adapter.notifyDataSetChanged();

		}
		private Button bt_task_menu1, bt_task_menu2, bt_task_menu3, bt_task_menu4,
				bt_task_menu5;
		
		private void initTaskMenu() {
			bt_task_menu1 = (Button) findViewById(R.id.bt_task_menu1);//咨询
			bt_task_menu2 = (Button) findViewById(R.id.bt_task_menu2);//建议
			bt_task_menu3 = (Button) findViewById(R.id.bt_task_menu3);//投诉
			bt_task_menu4 = (Button) findViewById(R.id.bt_task_menu4);//求助
			bt_task_menu5 = (Button) findViewById(R.id.bt_task_menu5);//举报
			
			bt_task_menu1.setText(ZX_COUNT);
			bt_task_menu2.setText(JY_COUNT);
			bt_task_menu3.setText(TS_COUNT);
			bt_task_menu4.setText(QZ_COUNT);
			bt_task_menu5.setText(JB_COUNT);
			bt_task_menu1.requestFocus();
			bt_task_menu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == true) {
						type_id="10031109341234120160";//咨询
						pageNum=0;
//						util.removeFootBar();
						doSetListView();
					}
				}
			});
			bt_task_menu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == true) {
						type_id="11052520593959390002";//建议
						pageNum=0;
//						util.removeFootBar();
						doSetListView();
						
					}

				}
			});
			bt_task_menu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == true) {
					  type_id="10082511353735370003";//投诉
					  pageNum=0;
//					  util.removeFootBar();
					  doSetListView();
					   
					}

				}
			});
			bt_task_menu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == true) {
						type_id="11030818023423400050";	//求助
						 pageNum=0;
//						 util.removeFootBar();
						doSetListView();
					}
				}
			});
			bt_task_menu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == true) {
						pageNum=0;
						type_id="11101309094894800003";//举报
						doSetListView();
					}
				}
			});
		}

		Handler rushHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				pageNum=0;
				doSetListView();
				
			}
			
			
			
			
		};
}