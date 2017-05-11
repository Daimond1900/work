package com.winksoft.android.yzjycy.ui.enterprise;

import java.util.Map;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.newyzjycy.map.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.BaseActivity;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
	private static final String TAG = "ZptEnterpriseActivity";
	private TextView logo_tipTxt, gsTxt, xzTxt, hyTxt, lxrTxt, dhTxt, czTxt, yxTxt, wzTxt, jjTxt, dzTxt;
	private Button backBtn, editBtn;
	private ImageView logoBtn;
	private ProgressDialog progressDialog;
	private QyDAL qyDAL;
	private Map<String, String> resultsMap;
	private TableLayout addressTab;
	private String longitude = "1192679470";// 经度
	private String latitude = "32248012";// 纬度
	private String companyNames = "", address = "", telNo = "";
	UserSession userSession;
	private Dialog proDialog;
	private ImageLoader im;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_enterprise_info);
		userSession = new UserSession(this);
		qyDAL = new QyDAL(this);
		im = ImageLoader.getInstance();
		im.init(ImageLoaderConfiguration.createDefault(this));
		initView();
	}

	private void onPostData() {
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
			@Override
			public void onStart() {
				super.onStart();
				proDialog = CustomeProgressDialog.createLoadingDialog(
						ZptEnterpriseActivity.this, "加载中,请稍后...");
				proDialog.show();
			}

			@Override
			public void onSuccess(Object arg0) {
				super.onSuccess(arg0);
				postResult((String) arg0);
				if (proDialog != null)
					proDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				if (proDialog != null)
					proDialog.dismiss();
			}
		};
		qyDAL.doPostQuery(user.getUserId(), callBack);
	}

	private void postResult(String json) {
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (("true").equals(map.get("success"))) {
				resultsMap = DataConvert.toConvertStringMap(json, "company");
//				Log.d(TAG, "resultsMap: " + resultsMap);
				setPageData();
				return;
			} else {
				commonUtil.shortToast("查询失败!");
			}
		} else {
			commonUtil.shortToast("查询失败!");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		onPostData();// 加载远程数据
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


	/***
	 * 加载数据
	 */
	private void setPageData() {
		if (resultsMap != null) {
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
			if(resultsMap.get("logo_url")!=null && !resultsMap.get("logo_url").equals("")){
				logoBtn.setBackgroundDrawable(null);
				im.displayImage(Constants.IP +"images/"+ resultsMap.get("logo_url").replace("\\", "/"),logoBtn);
			}else {
				logoBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_company_logo));
			}
		} else {
			dialogUtil.shortToast(resultsMap.get("msg"));
		}
	}

}
