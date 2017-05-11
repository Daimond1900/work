package com.yifeng.ChifCloud12345update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yifeng.ChifCloud12345.attention.AttentionActivity;
import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.FormDAL;
import com.yifeng.mythread.GenericTask;
import com.yifeng.mythread.HttpException;
import com.yifeng.mythread.TaskAdapter;
import com.yifeng.mythread.TaskListener;
import com.yifeng.mythread.TaskParams;
import com.yifeng.mythread.TaskResult;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.ListViewUtil;

/**
 * 热线待办
 * 
 * @author wujiahong
 * 
 */
public class MyWorkActivity extends BaseActivity {
	FormDAL dal;
	ListView listview;
	HomeAdapter adapter;
	LinearLayout top;
	ListViewUtil util;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wdsw);
		dal = new FormDAL(this);
		doLoadData(params);
		listview = (ListView) findViewById(R.id.listview);
		util = new ListViewUtil(this, listview);
		top = (LinearLayout) findViewById(R.id.top);
		top.requestFocus();
		adapter = new HomeAdapter(this, list, R.layout.home_item1, new String[] { "vimg", "row_name", "counts" },
				new int[] { R.id.vimg, R.id.row_name, R.id.counts }, getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int itemid = Integer.parseInt(list.get(position).get("id").toString());
				switch (itemid) {

				case 1:
					// 重要提醒
					Intent intent = new Intent(MyWorkActivity.this, ImportantEventActivity.class);
					startActivity(intent);

					break;

				case 2:
					// 待办事宜

					intent = new Intent(MyWorkActivity.this, CommentActivity.class);
					startActivity(intent);
					break;
				case 3:
					// 超时工单
					intent = new Intent(MyWorkActivity.this, OrverTimeDeptActivity.class);// OverTimeTaskActivity
					startActivity(intent);
					break;
				case 5:
					// 已经办事宜
					intent = new Intent(MyWorkActivity.this, TakenActivity.class);
					startActivity(intent);
					break;
				/**
				 * 去除催办通知 ----2016/11/23 修改
				 */
			/*	case 6:// 催办通知
					intent = new Intent(MyWorkActivity.this, NotifyActivity.class);
					startActivity(intent);
					break;*/
				/*------------------------------------------*/
				case 9:
					intent = new Intent(MyWorkActivity.this, PhoneListActivity.class);
					startActivity(intent);

					break;
				case 10:
					// 回访不满意工单
					intent = new Intent(MyWorkActivity.this, NotJobAreaActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});

		addData();
		this.initBottom();
		this.setFocus(this.bt_bottom_menu2, R.drawable.bottom_menu2_);
	}

	void addData() {
		Resources res = getResources();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.sy_zdsj));
		map.put("id", "1");
		map.put("row_name", "重要提醒");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.dbsy));
		map.put("id", "2");
		map.put("row_name", "待办事宜");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.csgd));
		map.put("id", "3");
		map.put("row_name", "超时工单");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.ybsy));
		map.put("id", "5");
		map.put("row_name", "已办事宜");
		list.add(map);
		
		/**
		 * 2016/11/23删除 催办通知
		 */
		/*map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.cbtz));
		map.put("id", "6");
		map.put("row_name", "催办通知");
		list.add(map);*/

		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.lxdh));
		map.put("id", "9");
		map.put("row_name", "联系电话");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("vimg", BitmapFactory.decodeResource(res, R.drawable.bmygd));
		map.put("id", "10");
		map.put("row_name", "回访不满意工单");
		list.add(map);

	}

	private class LoadDataTask extends GenericTask {
		private String msg = "";

		public String getMsg() {
			return msg;
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				addLeaderData();
			} catch (HttpException e) {
				return TaskResult.FAILED;
			}
			return TaskResult.OK;
		}
	}

	Map<String, String> returnMap = null;

	void addLeaderData() throws HttpException {

		returnMap = dal.doCountMass(user.getUserId(), user.getRole_type(), user.getOrg_id());

		// adapter1.notifyDataSetChanged();
	}

	private void doLoadData(TaskParams params) {

		if (loadDataTask != null && loadDataTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {

			loadDataTask = new LoadDataTask();
			loadDataTask.setListener(loadTaskListener);
			loadDataTask.execute(params);
		}
	}

	private GenericTask loadDataTask;
	private TaskListener loadTaskListener = new TaskAdapter() {

		@Override
		public void onPreExecute(GenericTask task) {

		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {
			// updateProgress((String) param);
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			String state = returnMap.get("state");
			if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
				//

				if (returnMap != null) {
					for (Map<String, Object> map : list) {

						if (map.get("row_name").equals("待办事宜")) {
							map.put("counts", returnMap.get("undealed"));
						} else if (map.get("row_name").equals("超时工单")) {
							map.put("counts", returnMap.get("overtime"));
						} else if (map.get("row_name").equals("已办事宜")) {
							map.put("counts", returnMap.get("dealed"));
						} else if (map.get("row_name").equals("回访不满意工单")) {
							map.put("counts", returnMap.get("unsatisfied"));
						} else
							map.put("counts", "");

					}
				}
				adapter.notifyDataSetChanged();
			} else
				util.showListAddDataState(state);

			// onLoginSuccess();
			// if (result == TaskResult.FAILED) {
			// MainPageActivity.this.commonUtil
			// .showToast(((LoadDataTask) task).getMsg());
			// }
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "mywork";
		}
	};
	TaskParams params = new TaskParams();
}