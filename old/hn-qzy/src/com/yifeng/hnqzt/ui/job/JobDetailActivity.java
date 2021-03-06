package com.yifeng.hnqzt.ui.job;

import java.util.Map;
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
import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.data.JobDAL;
import com.yifeng.hnqzt.ui.BaseWebView;
import com.yifeng.hnqzt.ui.mapabc.MapInfoActivity;
import com.yifeng.hnqzt.util.WebViewJsUtil;

/**
 * comments:我的求职-详情
 * @author Administrator 
 * Date: 2012-9-3
 */
public class JobDetailActivity extends BaseWebView
{
	private TextView titleTxt;
	private String fixUrl = "";
	private Button backBtn;
	private Button commitBtn;
	private WebViewJsUtil webViewJsUtil;
	private String type="",sending_id="";
	private JobDAL jobDal;
	private Map<String,String> returnMap;
	private final int REMOVE_MSG=11312;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_detail);
		jobDal=new JobDAL(this);
		
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		String title = this.getIntent().getStringExtra("title");
		titleTxt.setText(title);
		
		fixUrl=this.getIntent().getStringExtra("url")==null?"":this.getIntent().getStringExtra("url");
		
		
		
		this.setUrl(fixUrl);
		this.doInitView();
		
		setClient();
		
		//取消投递
		commitBtn = (Button) findViewById(R.id.commitBtn);
		commitBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			   commitBtn.setEnabled(false);
			   commitBtn.setText("正在处理,请稍候.....");
			   new Thread(new Runnable() {
				
				@Override
				public void run() {
					try{
					 Thread.sleep(200);
					 returnMap=jobDal.doRevokeResume(sending_id);
					 mhandler.sendEmptyMessage(REMOVE_MSG);
					}catch(Exception e){e.printStackTrace();mhandler.sendEmptyMessage(-1);}
				}
			}).start();
			}
		});
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				JobDetailActivity.this.finish();
			}
		});
		
		//等待公司确认的简历可以取消
		sending_id=this.getIntent().getStringExtra("sending_id")==null?"":this.getIntent().getStringExtra("sending_id");
		
		if(this.getIntent().getStringExtra("type")!=null){
			type=this.getIntent().getStringExtra("type");
			if(type.equals("0")){
				commitBtn.setText("取消投递");
			}else{
				commitBtn.setVisibility(View.GONE);
			}
		}else{
			commitBtn.setVisibility(View.GONE);
		}
		

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
		   
		    /*
		    webView.addJavascriptInterface(new Object(){
		     	 public void goToMap(final String companyId,final String companyName,final String companyAddress,final String telNo,final String longitude,final String latitude){
		     		 Handler mHandler=new Handler();
		     		 mHandler.post(new Runnable(){  
		                  public void run() {
		                	  goToMapActivity(companyId,companyName,companyAddress,telNo,longitude,latitude);
		                  } 
		              });
		     	 }
		     }, "android");
		     */
		    
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
			Intent map=new Intent(JobDetailActivity.this,MapInfoActivity.class);
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
		  if(msg.what==REMOVE_MSG){
			  if(returnMap!=null){
				  if(returnMap.get("success")!=null&&returnMap.get("success").equals("true")){
					  commonUtil.shortToast("取消投递简成功!");
					  //Intent bl=new Intent(JobDetailActivity.this,JobCommonActivity.class);
					  //bl.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					  //bl.putExtra("title", "等待公司确认");
					  ///bl.putExtra("type", "0");
					  //startActivity(bl);
					  setResult(2, null);
					  JobDetailActivity.this.finish();
					  
				  }else{
					  commitBtn.setText("操作失败,请重试...");
				  }
				  commitBtn.setEnabled(true);
				  
			  }
		  }
		  if(msg.what==-1){
			  commitBtn.setText("操作失败,请重试...");
			  commitBtn.setEnabled(true);
		  }
	  };
  };

}
