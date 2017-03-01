package com.yifeng.microalloy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.adapter.HomeAdapter;
import com.yifeng.data.InteractiveDAL;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.DateUtil;
import com.yifeng.util.ImageDownloadThread.ImageDownloadItem;
import com.yifeng.util.ListViewUtil;

/**
 * 你云我云详情
 * 
 * @author Administrator
 * 
 */
public class DetailActivty extends BaseActivity {
	ListView listview;
	HomeAdapter adapter;
	Button edit, back;
	String id;
	ListViewUtil util;

	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		listview = (ListView) findViewById(R.id.listview);
		util = new ListViewUtil(this, listview);
		util.addFootBar();

		adapter = new HomeAdapter(this, list, R.layout.replay_item,
				new String[] {  "bm_logo", "SUBJECT", "CONTENT", "USERNAME",  
				"ADDTIME", "pic"   }, new int[] { R.id.bm_logo,
						R.id.title, R.id.content, R.id.bm_name, R.id.time,
						R.id.img }, getResources());
		adapter.setViewBinder();
		listview.setAdapter(adapter);
		id = getIntent().getStringExtra("id");
		adapter.notifyDataSetChanged();
		adapter.need1 = true;
		adapter.need3 = true;
		edit = (Button) findViewById(R.id.replay);
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivty.this,
						DeclearActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, 5);
			}
		});

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				if(arg2==0) return false;
				if(!((String) list.get(arg2).get("USER_ID")).equals(DetailActivty.this.user.getUserId())){
					DetailActivty.this.commonUtil.showMsg("提醒", "只能删除自己发布的帖子。");
					return false;
				}
				
				final Dialog builder = new Dialog(DetailActivty.this,
						R.style.dialog);
				builder.setContentView(R.layout.confirm_dialog);
				TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
				TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
				ptitle.setText("确定删除");
				pMsg.setText("是否确定删除？");
				Button confirm_btn = (Button) builder
						.findViewById(R.id.confirm_btn);
				Button cancel_btn = (Button) builder
						.findViewById(R.id.cancel_btn);
				confirm_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
						dal.deleteReply((String) list.get(arg2).get("RETURN_ID"));
						list.remove(arg2);
						adapter.notifyDataSetChanged();
					}
				});

				cancel_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
					}
				});
				builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
				builder.show();

				return false;
			}
		});
		
		new Thread(gorupRunnable).start();
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DetailActivty.this.finish();
			}
		});
		this.initBottom();

		this.setFocus(this.bt_bottom_menu3, R.drawable.bottom_menu3_);
		dal = new InteractiveDAL(this);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			util.addFootBar();
			new Thread(gorupRunnable).start();
			this.commonUtil.shortToast("评论发送成功");
		}
	}

	InteractiveDAL dal;

	void addData() {
		list.clear();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		List<Map<String, String>> returnList = dal.getDetail(map);
		String state = returnList.get(0).get("state");
		if (state.equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))) {
			Resources res = getResources();

			for (int i = 0; i < returnList.size(); i++) {
				Map tmap = returnList.get(i);
				String img_url = (String) tmap.get("IMG_ADD");
				String user_url = (String) tmap.get("PIC_PATH");

				tmap.put("ADDTIME",
						DateUtil.getDate((String) tmap.get("ADDTIME")));
				if (img_url == null) {
					tmap.put("pic",
							BitmapFactory.decodeResource(res, R.drawable.none));
				} else {
					tmap.put("pic", this.commonUtil
							.getURLBitmap(getString(R.string.ipconfig)
									+ img_url));
				}
				if (user_url == null)
					tmap.put("bm_logo", BitmapFactory.decodeResource(res,
							R.drawable.user_head));
				else
					tmap.put("bm_logo", this.commonUtil
							.getURLBitmap(getString(R.string.ipconfig)
									+ user_url));
				list.add(tmap);
			}
		} else
			util.showListAddDataState(state);
		util.removeFootBar();
		adapter.count = list.size();
		adapter.notifyDataSetChanged();
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
    public static void main(String[] args) {
    	ImageDownloadItem item=new ImageDownloadItem();
	}

}