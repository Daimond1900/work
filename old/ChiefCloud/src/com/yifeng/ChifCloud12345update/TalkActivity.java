package com.yifeng.ChifCloud12345update;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.TalkDAL;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
/**
 * 经验交流
 * @author Administrator
 *
 */
public class TalkActivity extends BaseActivity implements OnScrollListener{
	private TalkDAL talkdal;
	private ListView listview;
	private Button back;
	private HomeAdapter adapter;
	private ArrayList<Map<String, Object>> list = new ArrayList <Map<String, Object>> ();
	private ProgressBar progressBar;
    private LinearLayout loadingLayout;
    private int pageNum = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jyjl);
        talkdal=new TalkDAL(this);
    	listview = (ListView) findViewById(R.id.listview);
    	addFootBar();
    	listview.setOnScrollListener(this);
    	
    	adapter = new HomeAdapter(this, list, R.layout.jyjl_item, new String[] {
				"SUBJECT","CONTENT","ADDTIME"  }, new int[] {R.id.name, R.id.content,R.id.addtime },getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
		 
		 new Thread(gorupRunnable).start();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu1, R.drawable.bottom_menu1_);
    }
	void addData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageNum + "");
		pageNum++;
		List<Map<String, String>> returnList = talkdal.doQury(map);
		if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.SERVER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.server_error_msg));
		}

		else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.INNER_ERROR))) {
			this.commonUtil.showToast(getString(R.string.inner_error_msg));

		}
		else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.IS_EMPTY))) {
			removeFootBar();
			this.commonUtil.shortToast("无数据");
		}

		else if (returnList.get(0).get("state")
				.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			if (returnList.size() < 10)
				removeFootBar();
	
			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				tmap.put("ADDTIME",DateUtil.getDate((String)tmap.get("ADDTIME")));
				list.add(tmap);
			}

		}
		adapter.count=list.size();
		adapter.notifyDataSetChanged();
	}
   

	private void removeFootBar() {

		listview.removeFooterView(loadingLayout);

	}

	private void addFootBar() {
		LinearLayout searchLayout = new LinearLayout(this);
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);
		progressBar = new ProgressBar(this);
		progressBar.setPadding(0, 0, 15, 0);
		searchLayout.addView(progressBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		TextView textView = new TextView(this);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		searchLayout.addView(textView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		searchLayout.setGravity(Gravity.CENTER);
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		loadingLayout.setGravity(Gravity.CENTER);
		listview.addFooterView(loadingLayout);
	}

	int lastItem = 0;

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
				Thread.sleep(1000);
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
}