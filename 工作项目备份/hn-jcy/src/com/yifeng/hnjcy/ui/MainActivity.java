package com.yifeng.hnjcy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.yifeng.hnjcy.adapter.CommonAdapter;
import com.yifeng.hnjcy.adapter.MainPicAdapter;
import com.yifeng.hnjcy.ui.delivery.DeliveryListActivity;
import com.yifeng.hnjcy.ui.ldlxx.MainList;
import com.yifeng.hnjcy.ui.recruit.RecuitInfoActivity;
import com.yifeng.hnjcy.ui.resume.ResumeListActivity;
import com.yifeng.hnjcy.ui.zcdt.ZCFGListActivity;
import com.yifeng.hnjcy.ui.zerotransfer.TransferList;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.hnjcy.util.DataConvert;
import com.yifeng.qzt.ui.enterprise.EnterpriseList;
import com.yifeng.qzt.ui.training.TrainingActivity;
import com.yifeng.qzt.widget.CircleFlowIndicator;
import com.yifeng.qzt.widget.ViewFlow;

/**
 * 系统首界面
 * 
 */
public class MainActivity extends BaseActivity {

	private Button  zpxx, zcfg, zpqy, pxxx, rx,jldw,tdjl,ldlxx, lzy;
	private ImageView banner;
	private TextView loginuser,txt_companyName;
	private MainPicAdapter mainPicadapter;
	private ViewFlow viewFlow;
	private LinearLayout l_zpxx,l_zpqy, l_zcfg, l_pxxx,l_rx,l_jldw,l_tdjl,l_ldlxx, l_lzy;
	CommonAdapter itemAdapter;
	private int displayWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		displayWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		mainPicadapter = new MainPicAdapter(this);
		setBanner();
		intButton();
		BASEHANDLER = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				MainActivity.this.commonUtil.doExit();
			}

		};
	}

	/**
	 * 首页广告图片显示
	 */
	private void setBanner() {

		LayoutInflater inflater = LayoutInflater.from(this);

		View layout = (View) inflater.inflate(R.layout.main_pic, null);
		// banner = (ImageView) findViewById(R.id.banner);
		banner = (ImageView) layout.findViewById(R.id.banner);
		banner.setImageResource(R.drawable.main_2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("layout", layout);

		map.put("itemId", "view1");
		mainPicadapter.addItemView(map);

		map = new HashMap<String, Object>();
		View layout1 = (View) inflater.inflate(R.layout.main_pic, null);
		banner = (ImageView) layout1.findViewById(R.id.banner);
		banner.setImageResource(R.drawable.main_1);
		map.put("layout", layout1);
		map.put("itemId", "view2");
		mainPicadapter.addItemView(map);

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(mainPicadapter);

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
	}

	/**
	 * 初始化button
	 */
	private void intButton() {
		loginuser = (TextView) findViewById(R.id.loginuser);
		txt_companyName = (TextView) findViewById(R.id.txt_companyName);
		txt_companyName.setText(user.getZbdw().equals("")?getString(R.string.copyright):"主办："+user.getZbdw());
		loginuser.setText(this.user.getOperatorname() + ",欢迎您使用!");
		l_ldlxx = (LinearLayout) findViewById(R.id.l_ldlxx);
		l_lzy = (LinearLayout) findViewById(R.id.l_lzy);
		l_zpxx = (LinearLayout) findViewById(R.id.l_zpxx);
		l_zpqy = (LinearLayout)findViewById(R.id.l_zpqy);
		l_zcfg = (LinearLayout) findViewById(R.id.l_zcfg);
		l_pxxx = (LinearLayout) findViewById(R.id.l_pxxx);
		l_rx = (LinearLayout) findViewById(R.id.l_rx);
		l_jldw = (LinearLayout) findViewById(R.id.l_jldw);
		l_tdjl = (LinearLayout) findViewById(R.id.l_tdjl);
		
		ldlxx = (Button) findViewById(R.id.ldlxx);
		lzy = (Button) findViewById(R.id.lzy);
		zpxx = (Button) findViewById(R.id.zpxx);
		zpqy = (Button)findViewById(R.id.zpqy);
		zcfg = (Button) findViewById(R.id.zcfg);
		pxxx = (Button) findViewById(R.id.pxxx);
		rx = (Button) findViewById(R.id.rx);
		jldw = (Button) findViewById(R.id.jldw);
		tdjl = (Button) findViewById(R.id.tdjl);
		
		rx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showItem();
			}
		});

		zcfg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent zcfg_intent = new Intent(MainActivity.this,
						ZCFGListActivity.class);
				zcfg_intent.putExtra("top_title", "政策法规");
				zcfg_intent.putExtra("state", "1");
				startActivity(zcfg_intent);

			}
		});
		
		setIntent(ldlxx, MainList.class);
		setIntent(lzy, TransferList.class);
		setIntent(zpxx, RecuitInfoActivity.class);
		setIntent(zpqy,EnterpriseList.class);
		setIntent(pxxx, TrainingActivity.class);
		setIntent(jldw, ResumeListActivity.class);
		setIntent(tdjl, DeliveryListActivity.class);
		
		setOnTouch(ldlxx,l_ldlxx);
		setOnTouch(lzy,l_lzy);
		setOnTouch(jldw,l_tdjl);
		setOnTouch(jldw,l_jldw);
		setOnTouch(zpxx,l_zpxx);
		setOnTouch(zpqy, l_zpqy);
		setOnTouch(zcfg,l_zcfg);
		setOnTouch(pxxx,l_pxxx);
		setOnTouch(rx,l_rx);
	}
	
private List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
	
	private void showItem(){
		subList.clear();
		subList = DataConvert.toObjectArrayList(user.getGroup());
		int maxWidth = (int) (displayWidth * 0.8);
		LayoutInflater inflater = LayoutInflater.from(this);  
        View v = inflater.inflate(R.layout.main_dialog, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        TextView ptitle = (TextView) layout.findViewById(R.id.top_title);
        ptitle.setText("就业热线");
        ListView listView = (ListView) layout.findViewById(R.id.list_view);
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					final String name = subList.get(arg2).get("name").toString().trim();
					final String tell = subList.get(arg2).get("hotline").toString().trim();
					if(tell.equals("")){
						return;
					}
					
					final Dialog builder = new Dialog(MainActivity.this,
							R.style.dialog);
					builder.setContentView(R.layout.confirm_dialog);
					TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
					TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
					ptitle.setText(MainActivity.this
							.getString(R.string.alert_dialog_finish_title));
					pMsg.setText("确定要拔打"+name+"就业热线"+tell+"吗？");
					final Button confirm_btn = (Button) builder
							.findViewById(R.id.confirm_btn);
					Button cancel_btn = (Button) builder
							.findViewById(R.id.cancel_btn);
					confirm_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:"+tell));
							startActivity(intent);
							builder.dismiss();
						}
					});
	
					cancel_btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							builder.dismiss();
						}
					});
					builder.setCanceledOnTouchOutside(true);
					builder.show();
					 
				}catch (Exception e) {
					
				}
				
			}
		});
        itemAdapter = new CommonAdapter(this,subList, R.layout.main_dialog_item, new String[] {
		 "name","hotline" }, new int[] {  R.id.row_name,R.id.row_num }, listView);
        itemAdapter.setViewBinder();
        listView.setAdapter(itemAdapter);
        
		final Dialog builder = new Dialog(this,R.style.dialog);
		builder.setContentView(layout,
				new LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		builder.setCancelable(true);
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}

	private void setIntent(Button button, final Class<?> mclass) {

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, mclass);
				startActivity(intent);
			}
		});

	}

	private void setOnTouch(Button button, final LinearLayout layout) {

		button.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					layout.setBackgroundResource( R.drawable.maian_bottom);
				} else if (MotionEvent.ACTION_UP == event.getAction()) {
					layout.setBackgroundResource(R.color.trans);
				}
				return false;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ConstantUtil.ISEJOB) {// 如果是管理版进来，直接返回;
				this.finish();
			} else {
				this.commonUtil.doExit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
