package com.winksoft.android.yzjycy;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.winksoft.android.yzjycy.entity.User;
import com.winksoft.android.yzjycy.util.GpsUtil;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.ui.BaseActivity;
import com.yifeng.nox.android.util.CommonUtil;
import com.yifeng.nox.android.util.FileUtils;

/***
 * 公共详细界面
 */
@SuppressLint("JavascriptInterface")
public class CommonPageView extends BaseActivity {
	@SetView(id = R.id.backBtn, click = "onViewClick")Button backBtn;
	@SetView(id = R.id.textBtn, click = "onViewClick")Button textBtn;
	@SetView(id = R.id.topTitle)TextView topTitle;
	@SetView(id=R.id.webview)WebView webView;
	protected ProgressDialog progressDialog = null;
	@SetView(id = R.id.publicloading)LinearLayout publicloading;
	private String fxUrl = "";
	private String title = "";
	private GpsUtil gpsUtil;
	//拍照
	FileUtils fileUtil;
	private String fileName = "";
	private String strCaptureFilePath;
	final int TAKE_PHOTO=1001,TAKE_PHOTO1=1002,START_AUTO2=1003;
	private String picUrl="";
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_detail);
		textBtn.setVisibility(View.VISIBLE);
		textBtn.setText("关闭");
		
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
		
		//文件下载调用内置浏览器
		webView.setDownloadListener(new MyWebViewDownLoadListener()); 
		
		//定位
		try{
		gpsUtil=new GpsUtil(this);
		GpsUtil.mHandler=this.mHandler;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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

				CommonPageView.this.setProgress(progress * 100);
				if (progress == 100) {
					publicloading.setVisibility(View.GONE);
				}
				
			  }
			    
			 //不显示js alert弹框
			 @Override
			 public boolean onJsAlert(WebView view, String url, String message,
			    	JsResult result) {
			    // TODO Auto-generated method stub
				showDoigMsg("系统提示", message);
				result.confirm();
			    return true;
				 //return super.onJsAlert(view, url, message, result);
			 }
		});
    	
    	webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				webView.loadUrl("file:///android_asset/error.html");
				
				
			}
			
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//super.shouldOverrideUrlLoading(view, url);
				
				if(url.contains("tel:")){//调用拔打电话   
					try{
					int i=url.indexOf(":");
					String tel="";
					if(i>0){
						tel=url.substring(i+1);
					}
					if(!tel.equals(""))
					dialogUtil.showCallDialog("系统提示", "您是否要拔打"+tel+"电话吗?",tel);
					}catch(Exception e){e.printStackTrace();}
		          
		        }else if(url.contains("qq.com")){
		        	goToOAth2(url);
		        }else{
		        	
		        	
		        	if(url.indexOf("?")>0){
		        	   url=url+"&isAndroid=true";
		        	}else{
		        	   url=url+"?isAndroid=true";
		        	}
		        	
		        	url=url.replace("#", "");//把#号设置为空，一般浏览器URL有#号会认为结束、后面参数找不到。
		        	
		        	//System.out.println("执行url==="+url);
		        	
		        	webView.loadUrl(url);  
		        	
		        } 
				  return true;    
				
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
	
	private void goToOAth2(String url){
		Intent v=new Intent(this,OAuthWebView.class);
		v.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		v.putExtra("url",url);
		v.putExtra("title","OAuth2.0认证");
		//startActivity(v);
		startActivityForResult(v,START_AUTO2);
	}
	
	private class MyWebViewDownLoadListener implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {        	
        	//调用内置浏览器下载
        	System.out.println("url="+url+"\n userAgent="+userAgent+"contentDisposition=\n" +
        			""+contentDisposition+"\n mimetype="+mimetype+"\n contentLength="+contentLength);
            
        	Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);        	 
        }
    }
	
    Handler mHandler=new Handler(){
    	  public void handleMessage(android.os.Message msg) {
    		  if(msg.what==GpsUtil.SEND_GPS_MSG){
    			  //定位成功，传给html界面
    			  //webView.loadUrl("javascript:onLocationByAndroid('"+GpsUtil.address+"','"+GpsUtil.longitude+"','"+GpsUtil.latitude+"')");
    			  if(gpsUtil!=null)
    			  gpsUtil.stopLocation();//定位成功即时关掉避免重复定位
    		  }
    		  if(msg.what==111){
    			  
    			  topTitle.setText(msg.getData().getString("title"));
    		  }
    	  };
    };
	
    private final class MyAndroidJsApi {
	     //java调用Html里的js方法， 在onload调用此方法实现如： javascript:android.hiddenTitle
		 //--------------------------------------------------------------------------------
	     //隐藏标题
		 /*public void onInitHtml() {
	    	  // 调用html的JS中的方法
	          webView.loadUrl("javascript:onInitHtml()");
	      }*/
		 
	      //获取经纬度度坐标
	      public void onLocationByAndroid() {
	    	  webView.loadUrl("javascript:onLocationByAndroid('"+GpsUtil.address+"','"+GpsUtil.longitude+"','"+GpsUtil.latitude+"')");
	      }
	      
	      //获取网络状态
	      /*public void onGetNetworkInfo() {
	    	  webView.loadUrl("javascript:onGetNetworkInfo('"+getNetworkInfo()+"')");
	      }*/
	      
	      //获取网络状态
	      public String  onGetNetworkInfo() {
	    	  return getNetworkInfo();
	      }
	      
	      //拍照回调路径方法
	      /*public void onGetPhotoUrl() {
	    	  webView.loadUrl("javascript:onGetPhotoUrl('"+picUrl+"')");
	      }*/
	      
	      
	      
	      //网页js直接调用java代码
	      //--------------------------------------------------------------------------------
	      
	      //关闭本页面
	      public void onFinishPage(){
	    	    if(gpsUtil!=null)
				gpsUtil.stopLocation();
			    //为了关掉视频加了这一句
			    webView.loadUrl("about:blank");
			    
			    finish();;
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
	      
	      //显示-标题回主界面按钮
	      public void showHomeBtn(boolean flag) {
	    	  if(flag)
	    		  textBtn.setVisibility(View.VISIBLE);
	    	  else
	    		  textBtn.setVisibility(View.GONE);
	      }
	      
	      //拍照
	      public String onTakePhoto(){
	    	  takePhoto();
	    	  return picUrl;
	      };
	      
	      //从相册获取照片
	      public String onPhotoFromGallery(){
	    	  getPhotoFromGallery();
	    	  return picUrl;
	      };
	      
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
	    	  
	    	  
	    	  CacheData.setUser(CommonPageView.this, user);
	      }
	     
	  }
	  
	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
		switch (v.getId()) {
		case R.id.backBtn:
			finishPage();
			break;
		case R.id.textBtn:
			if(gpsUtil!=null)
			 gpsUtil.stopLocation();
		    //为了关掉视频加了这一句
		    webView.loadUrl("about:blank");
		    
		    finish();
			break;
		default:
			break;
		}
	}
	
	private void finishPage(){
		if (webView.canGoBack()) {  
			webView.goBack();  
		}else{
		    if(gpsUtil!=null)
				gpsUtil.stopLocation();
		    //为了关掉视频加了这一句
		    webView.loadUrl("about:blank");
		    
		    finish();

		 }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {  
			 webView.goBack();
			 return true;  
			}else{
				if(keyCode == KeyEvent.KEYCODE_BACK){
				  if(gpsUtil!=null)
					  gpsUtil.stopLocation();
				  //为了关掉视频加了这一句
				  webView.loadUrl("about:blank");
				  finish();
			     }

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
    
    
    /***
	 * 拍照
	 * @param btnId
	 */
	private void takePhoto() {
		 
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//startActivityForResult(intent, TAKE_PHOTO);
		
		// 如果存储卡可用 ,将照片存储在 sdcard
		if (fileUtil.checkSDCard()) {
			String filePath = fileUtil.getSDPATH()+"/";
			File photoPath = new File(filePath);
			if (!photoPath.exists()) {
				photoPath.mkdirs();
			}
			File PHOTO_DIR = new File(filePath, fileName);
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PHOTO_DIR));
		    startActivityForResult(intent, TAKE_PHOTO);
		}
		else{
			showToast("sdcard不可用!", false);
		}
	}
	
	/**
	 * 选取本地图片
	 * */
	private void getPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, TAKE_PHOTO1);
	}
    
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==0)return;
		
	    try{
			   if(requestCode == TAKE_PHOTO){//拍照
				     if (fileUtil.checkSDCard()) {
						try{
							Bitmap bmp=CommonUtil.getSmallBitmap(strCaptureFilePath, 400,600);
							//上传图片
							picUrl="拍照上传成功!";
							webView.loadUrl("javascript:onGetPhotoUrl('"+picUrl+"')");
						}catch(Exception e){
							System.out.println("图片获取失败!SDCard不可用!");
							
						}

					} else {
						
						Bundle extras = data.getExtras();
						Bitmap bmp= (Bitmap) extras.get("data");
						//上传图片
						picUrl="拍照上传成功!";
						webView.loadUrl("javascript:onGetPhotoUrl('"+picUrl+"')");
						
					}
				}
			   if (requestCode ==TAKE_PHOTO1) {
					Uri uri = data.getData();
					ContentResolver cr = this.getContentResolver();
					Cursor cursor = cr.query(uri, null, null, null, null);
					cursor.moveToFirst();
					String fileUrl = cursor.getString(1);
					Bitmap bmp=CommonUtil.getSmallBitmap(fileUrl,400,600);
					//上传图片
					picUrl="从本地取图片上传成功!";
					webView.loadUrl("javascript:onGetPhotoUrl('"+picUrl+"')");

				}
			   
			   //弹出新页面回来
			   if (requestCode ==START_AUTO2) {
				   Bundle b = data.getExtras();
				   boolean isTrue=b.getBoolean("isLoginSuccess");
				   if(isTrue){
				    //如果登录成功直接关掉该页
				    finish();
				  }
				  
			   }
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("图片获取失败!");
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
	
	/***
	 * 获取网络状态
	 * @return
	 */
	 private  String getNetworkInfo()
	    {
	    	ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

	        //mobile 3G Data Network
	        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	       
	        //wifi
	        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	        
	        //3G/2G
	        if(mobile==State.CONNECTED||mobile==State.CONNECTING)
	            return "3g";
	        
	        //wifi
	        if(wifi==State.CONNECTED||wifi==State.CONNECTING)
	            return "wifi";
	        
	        return "";
	          
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
