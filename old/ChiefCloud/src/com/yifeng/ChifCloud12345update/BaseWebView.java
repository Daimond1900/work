package com.yifeng.ChifCloud12345update;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * WebView操作父类
 * @author WuJiaHong
 *  2011-10-09
 */
public class BaseWebView extends BaseActivity{
	protected String url;
	protected boolean flag=false;
	protected WebView webView;
	protected ProgressDialog progressDialog = null;
	private int timeout=0;
	public boolean isOtherUrl=false;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
   protected void showProg(){
	   progressDialog = ProgressDialog.show(this, "请稍等...", "数据加载中...", true);
   }
   protected void doInitView(){
	     showProg();
	     webView=(WebView)findViewById(R.id.webview);
	     webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	   	 webView.getSettings().setSupportZoom(false);
	     webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
	     webView.getSettings().setJavaScriptEnabled(true);
	     webView.loadUrl(this.getUrl());
	     
	     webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
	     webView.setWebChromeClient(new WebChromeClient() {
	      	public void onProgressChanged(WebView view, int progress) {
	              if ( !progressDialog.isShowing() )
	              {
	              	progressDialog.show();
	              }
	             
	              BaseWebView.this.setProgress(progress * 100);
	              Log.v("webView执行时间:==========",String.valueOf(progress));
	              if(progress == 100){
	            	  BaseWebView.this.setTitle(R.string.app_name);
	              	  progressDialog.dismiss();
	              }
	              timeout+=1;
	              Log.v("webView连接操时测试:==========",String.valueOf(timeout));
	          }
	      });
	     webView.setWebViewClient(new WebViewClient(){
	     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	    	 flag=true;
	      }
	     });
	  }
   public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		if(isOtherUrl){
			this.url=url;
		}else{
		    this.url =getString(R.string.ipconfig)+url;
		}
	}
   public boolean onKeyDown(int keyCode, KeyEvent event) {        
	      if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {        
	    	  if(flag){
	    		  this.finish();
	    	  }else{
	    	     webView.goBack();
	    	  }        
	          return true;        
	      }        
	      return super.onKeyDown(keyCode, event);        
	 }     
}
