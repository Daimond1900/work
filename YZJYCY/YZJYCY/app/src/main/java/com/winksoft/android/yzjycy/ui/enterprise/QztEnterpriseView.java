package com.winksoft.android.yzjycy.ui.enterprise;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QztEnterpriseDal;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.ui.training.QztTrainingActivity;
import com.winksoft.android.yzjycy.ui.zptmapabc.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 公司详细信息
 * @author wujiahong
 * 2012-10-18
 */
public class QztEnterpriseView extends BaseActivity {
	private static final String TAG = "QztEnterpriseView";
	private TableRow positionItem,companyAddressItem;
	private Button back_btn;
	private TextView companyName,qualityName,tradeName,telNumber
	,companyDesc,companyAddress,txtfaxes,txtemail,txtcontact,txturl;
	private XwzxDAL xwzxDAL;
	private ProgressDialog  progressDialog;
	private Map<String,String> enterInfo;
	private String companyId="";
	private Dialog proDialog;
	private String longitude="1192679470";//经度
    private String latitude="32248012";//纬度
    
    private String  companyNames="", address="",telNo="",faxes="",email="",contact="",url="";
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qzt_enterprise_info);
		xwzxDAL=new XwzxDAL(this);
		
		initPage();
		
		//加载远程数据
//    	new Thread(loadRunnanle).start();
		onPostData();
	}

	private void onPostData() {
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
			@Override
			public void onStart() {
				super.onStart();
				proDialog = CustomeProgressDialog.createLoadingDialog(
						QztEnterpriseView.this, "加载中,请稍后...");
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
		xwzxDAL.getEnterPrise(companyId, callBack);
	}

	private void postResult(String json) {
		Map<String, String> map = DataConvert.toMap(json);
		Log.d(TAG, "postResult: postResult: " + map);
		if (map != null) {
			if (("true").equals(map.get("success"))) {
				enterInfo = DataConvert.toConvertStringMap(json, "company");
				setPageData();
				return;
			} else {
				commonUtil.shortToast(map.get("msg"));
			}
		} else {
			commonUtil.shortToast("查询失败!");
		}
		setPageData();
	}

	/**
	 * 初始化界面
	 */
	private void initPage(){
		
		companyId=this.getIntent().getStringExtra("companyId")==null?"":this.getIntent().getStringExtra("companyId");
		
		MyOnclick onclick=new MyOnclick();
		
		//点击到该公司所有职位
		positionItem=(TableRow)findViewById(R.id.positionItem);
		positionItem.setOnClickListener(onclick);
		
		//点击到公司对应的地图展现
		companyAddressItem=(TableRow)findViewById(R.id.companyAddressItem);
		companyAddressItem.setOnClickListener(onclick);
		
		back_btn=(Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(onclick);
		
		companyName=(TextView)findViewById(R.id.companyName);//公司名称;
		qualityName=(TextView)findViewById(R.id.qualityName);//公司性质
		tradeName=(TextView)findViewById(R.id.tradeName);//行业
		telNumber=(TextView)findViewById(R.id.telNumber);//联系电话
		txtfaxes = (TextView)findViewById(R.id.faxes);	//传真
		txtemail = (TextView)findViewById(R.id.email);	//邮箱
		companyDesc=(TextView)findViewById(R.id.companyDesc);//公司简介
		companyAddress=(TextView)findViewById(R.id.companyAddress);//公司地址
		txtcontact = (TextView)findViewById(R.id.contact);	//联系人
		txturl = (TextView)findViewById(R.id.url);	//网址
	}
	
   private class MyOnclick implements OnClickListener{

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.positionItem:
			Intent posotionList=new Intent(QztEnterpriseView.this,QztPositionList.class);
			posotionList.putExtra("companyName", companyName.getText().toString());
			posotionList.putExtra("companyId",companyId);
			startActivity(posotionList);
			break;
		case R.id.companyAddressItem:
			if(!longitude.equals("")&&!latitude.equals("")){
				Intent map=new Intent(QztEnterpriseView.this,ZptMapInfoActivity.class);
				map.putExtra("companyName", companyNames);
				map.putExtra("companyAddress", address);
				map.putExtra("telNo", telNo);
				map.putExtra("longitude", longitude);//经度
				map.putExtra("latitude", latitude);//纬度
				startActivity(map);
			}else{
				commonUtil.shortToast(companyNames+",还没标注地理位置.");
			}
			break;
		case R.id.back_btn:
			QztEnterpriseView.this.finish();
			break;
		default:
			break;
		}
	}
	}




	
	/***
	 * 加载数据
ACB200	招聘编号
AAB004	单位名称
AAB022	行业类型   枚举
AAB019	单位类型   枚举
AAE006	单位地址
AAE004	单位联系人
AAE005	单位联系人电话
ACB206	单位简介
AAE014	传真
AAE015	电子信箱
ACB202	用工区域
ACB203	工作地点
ACB204	地点详细
AAE030	招聘起始日期
AAE031	招聘终止日期
	 */
   private void setPageData(){
   	     if(enterInfo != null){
   	    	        companyNames=enterInfo.get("aab004")==null?"":enterInfo.get("aab004");
   	    	    	companyName.setText(companyNames);//公司名称;
   	    			qualityName.setText(enterInfo.get("aab019"));//公司性质
   	    			tradeName.setText(enterInfo.get("aab022"));//行业
   	    			
   	    			
   	    			
   	    			telNo=enterInfo.get("aae005")==null?"":enterInfo.get("aae005");
   	    			telNumber.setText(telNo);//联系电话
   	    			
   	    			faxes = enterInfo.get("aae014")==null?"":enterInfo.get("aae014");
   	    		    txtfaxes.setText(faxes);	//传真
   	    		    
   	    		    
   	    		    email = enterInfo.get("aae015")==null?"":enterInfo.get("aae015");
   	    		    txtemail.setText(email);	//邮箱
   	    		    
   	    		    contact = enterInfo.get("aae004")==null?"":enterInfo.get("aae004");
   	    			txtcontact.setText(contact);//联系人
   	    			
   	    			url = enterInfo.get("aae016")==null?"":enterInfo.get("aae016");
   	    			txturl.setText(url);	//网址
   	    		    
   	    			//公司简介
   	    			String desc=enterInfo.get("dwjj")==null?"":enterInfo.get("dwjj");
   	    			companyDesc.setText(Html.fromHtml(desc.equals("")?"暂无简介":desc));
   	    			
   	    		    //如果公司地址为空就显示公司名字
   	    			address=enterInfo.get("aae006")==null?"":enterInfo.get("aae006");
   	    			companyAddress.setText(address.equals("")?companyNames:address+"	(地图定位)");
   	    			
   	    			longitude=enterInfo.get("lng");//经度
	    	    	latitude=enterInfo.get("lat");//纬度
   	     }else{
   	    	commonUtil.shortToast("信息加载失败!");
   	     }
   }




}
