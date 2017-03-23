package com.winksoft.android.yzjycy.ui.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.MainActivity;
import com.winksoft.android.yzjycy.data.EnterpriseDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.zptmapabc.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.BaseActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * ClassName:EnterpriseActivity 
 * Description：企业信息
 * @author Administrator 
 * Date：2012-10-17
 */
public class ZptEnterpriseActivity extends BaseActivity {
	private TextView logo_tipTxt, gsTxt, xzTxt, hyTxt, lxrTxt, dhTxt, czTxt, yxTxt, wzTxt, jjTxt, dzTxt;
//	private Button logoBtn, backBtn, editBtn;
	private Button backBtn, editBtn;
	private ImageView logoBtn;
	private ProgressDialog progressDialog;
	private EnterpriseDAL enterpriseDAL;
	private Map<String, String> resultsMap;
	private TableLayout addressTab;
	private String longitude = "1192679470";// 经度
	private String latitude = "32248012";// 纬度
	private String companyNames = "", address = "", telNo = "";
	private Bitmap logoBamp;
	UserSession userSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_enterprise_info);
		userSession = new UserSession(this);
		enterpriseDAL = new EnterpriseDAL(this);

		initView();

		showProg();// 加载远程数据
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Thread(loadRunnanle).start();
	}

	private void initView() {
		logo_tipTxt = (TextView) findViewById(R.id.logo_tipTxt);// 企业logo提示
		gsTxt = (TextView) findViewById(R.id.gsTxt);// 公司
		gsTxt.setText(user.getZbdw());
		/**
		 * 企业名称
		 */
		gsTxt.setText(user.getZbdw());
		xzTxt = (TextView) findViewById(R.id.xzTxt);// 性质
		hyTxt = (TextView) findViewById(R.id.hyTxt);// 行业
		lxrTxt = (TextView) findViewById(R.id.lxrTxt);// 联系人
		dhTxt = (TextView) findViewById(R.id.dhTxt);// 电话
		czTxt = (TextView) findViewById(R.id.czTxt);// 传真
		yxTxt = (TextView) findViewById(R.id.yxTxt);// 邮箱
		wzTxt = (TextView) findViewById(R.id.wzTxt);// 网址
		jjTxt = (TextView) findViewById(R.id.jjTxt);// 简介
		dzTxt = (TextView) findViewById(R.id.dzTxt);// 地址

		MyOnClickListener myclick = new MyOnClickListener();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myclick);
		editBtn = (Button) findViewById(R.id.editBtn);
		editBtn.setOnClickListener(myclick);
		logoBtn = (ImageView) findViewById(R.id.logoBtn);

		addressTab = (TableLayout) findViewById(R.id.addressTab);
		addressTab.setOnClickListener(myclick);

	}

	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
//				Intent intent1 = new Intent(ZptEnterpriseActivity.this,MainActivity.class);
//				startActivity(intent1);
				ZptEnterpriseActivity.this.finish();
				
				break;
			case R.id.editBtn:
				Intent intent2 = new Intent(ZptEnterpriseActivity.this, ZptEnterpriseEditActivity.class);
				userSession.setMaps(gsTxt.getText().toString(), dzTxt.getText().toString(), dhTxt.getText().toString());
				startActivity(intent2);
				break;
			case R.id.addressTab:
				if (!longitude.equals("") && !latitude.equals("")) {
					Intent map = new Intent(ZptEnterpriseActivity.this, ZptMapInfoActivity.class);
					map.putExtra("companyName",gsTxt.getText().toString());
					map.putExtra("companyAddress", dzTxt.getText().toString());
					map.putExtra("telNo", dhTxt.getText().toString());
					map.putExtra("longitude", longitude);// 经度
					map.putExtra("latitude", latitude);// 纬度
					startActivity(map);
				} else {
					dialogUtil.shortToast(user.getZbdw()+ ",还没标注地理位置.");
				}
				break;
			default:
				break;
			}

		}
	}

	private void showProg() {
		progressDialog = ProgressDialog.show(this, "请稍等...", "正在加载数据...", true);
	}

	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(300);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("aab001", user.getUserId()));
				resultsMap = enterpriseDAL.doPostQuery(param, "android/corecruitment/queryCompanyDetail");
				loadHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}

		}
	};

	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				setPageData();
			}

			if (msg.what == 2) {
				// 获取头象
				if (logoBamp != null) {
					//logoBtn.setImageBitmap(logoBamp);
					BitmapDrawable dbm = new BitmapDrawable(logoBamp);
					logoBtn.setBackgroundDrawable(dbm);
					logo_tipTxt.setVisibility(View.GONE);
				}else {
					logoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_company_logo));
				}
			}

			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

	/***
	 * 加载数据
	 */
	private void setPageData() {
		if (resultsMap.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			/*
			 * 性质：aab019; 行业 ：aab022; 联系人：aae004; 电话：aae005; 传真：aae014;
			 * 邮箱：aae015; 网址：aae016; 简介：dwjj; 地址：aae006;
			 */
			gsTxt.setText(resultsMap.get("aab004")==null ?user.getZbdw():resultsMap.get("aab004"));
			xzTxt.setText(resultsMap.get("aab019") == null ? "" : resultsMap.get("aab019"));// 性质
			hyTxt.setText(resultsMap.get("aab022") == null ? "" : resultsMap.get("aab022"));// 行业
			lxrTxt.setText(resultsMap.get("aae004") == null ? "" : resultsMap.get("aae004"));// 联系人
			telNo = resultsMap.get("aae005") == null ? "" : resultsMap.get("aae005");
			dhTxt.setText(telNo);// 电话
			czTxt.setText(resultsMap.get("aae014") == null ? "" : resultsMap.get("aae014"));// 传真
			yxTxt.setText(resultsMap.get("aae015") == null ? "" : resultsMap.get("aae015"));// 邮箱
			wzTxt.setText(resultsMap.get("aae016") == null ? "" : resultsMap.get("aae016"));// 网址
			jjTxt.setText(resultsMap.get("dwjj") == null ? "" : resultsMap.get("dwjj"));// 简介
			// 如果公司地址为空就显示公司名字
			address = resultsMap.get("aae006") == null ? "" : resultsMap.get("aae006");
			dzTxt.setText(address.equals("") ? companyNames : address);

			longitude = resultsMap.get("lng") == null ? "" : resultsMap.get("lng");// 经度
			latitude = resultsMap.get("lat") == null ? "" : resultsMap.get("lat");// 纬度
			
			// 从远程加载图片
//			logoBamp = CommonUtil.getURLBitmap(Constants.IP +"images/"+ resultsMap.get("logo_url").replace("\\", "/"));
			if(resultsMap.get("logo_url")!=null && !resultsMap.get("logo_url").equals("")){
				logoBtn.setBackgroundDrawable(null);
				ImageLoader.getInstance().displayImage(Constants.IP +"images/"+ resultsMap.get("logo_url").replace("\\", "/"),logoBtn);
				
			}
//			loadHandler.sendEmptyMessage(2);
		} else {
			dialogUtil.shortToast(resultsMap.get("msg"));
		}
	}

}
