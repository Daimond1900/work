package com.winksoft.android.yzjycy;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winksoft.android.yzjycy.entity.User;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.ui.BaseActivity;
import com.yifeng.nox.android.ui.BaseWebView;
import com.yifeng.nox.android.util.CommonUtil;
import com.yifeng.nox.android.util.FileUtils;

/***
 * OAuth2.0
 * @author 验证界面
 * 2014-8-20
 */
@SuppressLint("JavascriptInterface")
public class OAuthWebView extends BaseActivity {
	@SetView(id = R.id.backBtn, click = "onViewClick")Button backBtn;
	@SetView(id = R.id.topTitle)TextView topTitle;
	@SetView(id=R.id.webview)WebView webView;
	protected ProgressDialog progressDialog = null;
	@SetView(id = R.id.publicloading)LinearLayout publicloading;
	private String fxUrl = "";
	private String title = "";
	//拍照
	FileUtils fileUtil;
	private String fileName = "";
	private String strCaptureFilePath;
	final int TAKE_PHOTO=1001,TAKE_PHOTO1=1002;
	private String picUrl="";
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_detail);
		
		Intent bl = this.getIntent();
		fxUrl = bl.getStringExtra("url") == null ? "" : bl
				.getStringExtra("url");
		title = bl.getStringExtra("title") == null ? "" : bl
				.getStringExtra("title");
		topTitle.setText(title);
		if (fxUrl.equals("")) {
			showToast("访问接口错误!", false);
			return;
		}
		
		
		WebSettings set=webView.getSettings();
		set.setJavaScriptEnabled(true);
		//设置缓存
		set.setCacheMode(WebSettings.LOAD_DEFAULT);
	    setPageCacheCapacity(set);
	    //双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置
	    webView.getSettings().setUseWideViewPort(true);
	    //提高渲染的优先级
	  	//webView.getSettings().setRenderPriority(RenderPriority.HIGH); 
	  	//把图片加载放在最后来加载渲染
	    //webView.getSettings().setBlockNetworkImage(true); 
	    webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
	    
	    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	    
		webView.loadUrl(fxUrl);
		
		
		
		
		
		//拍照
		fileUtil=new FileUtils();
		fileName="yzcyfwpt.jpg";
		strCaptureFilePath=fileUtil.getSDPATH()+"/"+fileName;
				
		//设置字体大小
    	//setFontSize();
    	
    	
    	webView.addJavascriptInterface(new MyAndroidJsApi(), "yifeng");
    	
    	
    	webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				
				if(publicloading.getVisibility()==View.GONE){
                	showProg();
                }

				OAuthWebView.this.setProgress(progress * 100);
				if (progress == 100) {
					publicloading.setVisibility(View.GONE);
				}
				
			  }
		});
    	
    	webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				webView.loadUrl("file:///android_asset/error.html");
				
				
			}
			
		});
        
	}
	
	protected void showProg() {
		publicloading.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		user = CacheData.getUser(this);
		
	};
	
	
	
    Handler mHandler=new Handler(){
    	  public void handleMessage(Message msg) {
    		 
    		  if(msg.what==111){
    			  
    			  topTitle.setText(msg.getData().getString("title"));
    		  }
    	  };
    };
    
    private boolean isLoginSuccess=false;//标志网页登录是否成功、如果成功为true;
	
    private final class MyAndroidJsApi {
	     //java调用Html里的js方法， 在onload调用此方法实现如： javascript:android.hiddenTitle
		 //--------------------------------------------------------------------------------
	    
	      
	      //获取网络状态
	      /*public void onGetNetworkInfo() {
	    	  webView.loadUrl("javascript:onGetNetworkInfo('"+getNetworkInfo()+"')");
	      }*/
	      
	     
	      
	      
	      //网页js直接调用java代码
	      //--------------------------------------------------------------------------------
    	  
    	  //关闭本页面
	      public void onFinishPage(){
	    	    setBackPage();
	      };
	      
    	  //网页调用赋值给登录状态
    	  public void onLoginState(boolean flag){
	    	  isLoginSuccess=flag; 
	      };
	      
	      //拔打电话
	      public void onCall(final String telNo){
	    	  dialogUtil.showCallDialog("系统提示", "您是否确定拔打"+telNo, telNo); 
	      };
	      
	      //设置标题文字
	      public void onSetAndroidTitle(final String title) {
	    	  Message msg=new Message();
        	  msg.what=111;
        	  Bundle b=new Bundle();
        	  b.putString("title", title);
        	  msg.setData(b);
        	  mHandler.sendMessage(msg);
        	
	      }
	      
	      
	      //获取登录为型
	      public String getLoginType(){
	    	 return "android";
	      };
	      
	      //获取登录名
	      public String getLoginName(){
	    	  return user.getUserId();
	      }
	      
	      //获取登录密码
	      public String getLoginPwd(){
	    	  return user.getUserPwd();
	      }
	      
	      /**
	       * 登录
	       * @param data
	       */
	      public void onNewLogin(String data){
	    	  //TODO:完成登录逻辑
	    	  
	    	  User user = new User();
	    	  
	    	  
	    	  CacheData.setUser(OAuthWebView.this, user);
	      }
	     
	  }
	  
	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
		switch (v.getId()) {
		case R.id.backBtn:
			setBackPage();
			break;
		default:
			break;
		}
	}
	private void setBackPage(){
		Intent res=new Intent();
		res.putExtra("isLoginSuccess", isLoginSuccess);
		setResult(Activity.RESULT_OK, res);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) ) {  
			   setBackPage();
			}
		return super.onKeyDown(keyCode, event);
	}
	
	private WebSettings settings;  
	/** 
     * 根据字体大小刷新UI 
     */  
    private void setFontSize() {
    	 //得到字体大小
    	 settings = webView.getSettings();
    	 int width=getWidthPixels(this);
    	 if(width<480){
    		 settings.setTextSize(WebSettings.TextSize.SMALLER);
		 }else if(width>=480&&width<800){
			 settings.setTextSize(WebSettings.TextSize.NORMAL);
		 }else if(width>=800&&width<1024){
			  settings.setTextSize(WebSettings.TextSize.LARGER);
		  }else{
			  settings.setTextSize(WebSettings.TextSize.LARGER);
		  }
    
    }  
    
    /**
	 * 显示手机屏幕分辨率
	 */
	public static  int getWidthPixels(Activity mContext){
		DisplayMetrics dm=new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		return dm.widthPixels;
	}
	
	
	 
	 /**
	     * 这个函数是用来处理 当进行goBack的时候 使用前一个页面的缓存 避免每次都从新载入
	     * @param webSettings webView的settings
	     */
	    protected void setPageCacheCapacity(WebSettings webSettings) {
	        try {
	            Class<?> c = Class.forName("android.webkit.WebSettingsClassic");

	            Method tt = c.getMethod("setPageCacheCapacity", new Class[] { int.class });

	            tt.invoke(webSettings, 5);

	        } catch (ClassNotFoundException e) {
	            System.out.println("No such class: " + e);
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
	    }
	 
}
