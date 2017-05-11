package com.yifeng.hnjcy.ui.delivery;

import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseWebView;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.WebViewJsUtil;
import com.yifeng.qzt.ui.mapabc.MapInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
* @Description: 投递记录详情
* @author ZhuZhiChen   
* @date 2014-9-23 
 */
public class DeliveryDetailActivity extends BaseWebView {
	private ResumeDAL dal;
	private Button back;
	private TextView textView;
	private WebViewJsUtil webViewJsUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_detail);

		String id = getIntent().getStringExtra("id");
		textView = (TextView) findViewById(R.id.top_title);
		textView.setText(getIntent().getStringExtra("title_name"));
		
		dal = new ResumeDAL(this, new Handler());
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeliveryDetailActivity.this.finish();
			}
		});

		this.setUrl(dal.doQuerySendDetail(id));
		this.doInitView();
		setClient();
	}
	
	
	private void setClient(){
		 webView.setWebViewClient(new WebViewClient(){
		     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		    	 //连接失败时，或找不到路径返回首页 
		 		 try {
		 			   commonUtil.shortToast("接口调用失败!");
					  } catch (Exception e) {}
		        	}
		     });
		    
		   //外部js调用java方法解决代码混淆
		    webViewJsUtil=new WebViewJsUtil();
		    webViewJsUtil.jsHandler=this.mhandler;
		    webView.addJavascriptInterface(webViewJsUtil,"android");
	}
	
	/**
	  * 转到地图界面
	  * @param companyId//公司编号
	  * @param companyName//公司名称
	  * @param companyAddress //公司地址
	  * @param telNo 联系电话
	  * @param longitude 经度
	  * @param latitude 纬度
	  */
	private void goToMapActivity(String companyId,String companyName,String companyAddress,String telNo,String longitude,String latitude){
		if(!longitude.equals("")&&!latitude.equals("")){
			Intent map=new Intent(DeliveryDetailActivity.this,MapInfoActivity.class);
			map.putExtra("companyId", companyId);
			map.putExtra("companyName", companyName);
			map.putExtra("companyAddress", companyAddress);
			map.putExtra("telNo", telNo);
			map.putExtra("longitude", longitude);//经度
			map.putExtra("latitude", latitude);//纬度
			startActivity(map);
		}else{
			commonUtil.shortToast(companyName+",还没标注地理位置.");
		}
 }
	
 Handler mhandler=new Handler(){
	  public void handleMessage(Message msg) {
		  super.handleMessage(msg);
		  if(msg.getData()!=null){//从网页过来请求
				 String method=msg.getData().getString("method")==null?"":msg.getData().getString("method");
				 if(method.equals("goToMap")){//查看公司地图
					  String companyId=msg.getData().getString("companyId");
					  String companyName=msg.getData().getString("companyName");
					  String companyAddress=msg.getData().getString("companyAddress");
					  String telNo=msg.getData().getString("telNo");
					  String longitude=msg.getData().getString("longitude");
					  String latitude=msg.getData().getString("latitude");
					  goToMapActivity(companyId,companyName,companyAddress,telNo,longitude,latitude);
				 }
		  }
	  };
 };
}
