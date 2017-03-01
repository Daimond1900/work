package com.yifeng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 连接远程服务器资源工具类
 * @author 吴家宏
 *2011年4月11日
 */
public class HttpConnectUtil {
	 /**
	  * 根据给定本地路径读取本地资源图片
	  * @param url
	  * @return
	  */
	public static Bitmap getLoacalBitmap(String url) {
		try {
		FileInputStream fis = new FileInputStream(url);
		return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
		  System.out.println("加载图片资源出错!");
		  e.printStackTrace();
		return null;
		}
		}

	/**
     * 根据HTTP路径获取远程图片即时展现
     * @param uriPic
     * @return
     */
    
    public static Bitmap getURLBitmap(String uriPic)
    {
      URL imageUrl = null;
      Bitmap bitmap = null;
      
      try
      {
        /* new URL对象将网址传入 */
        imageUrl = new URL(uriPic);
      } catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
      try
      {
        /* 取得联机 */
        HttpURLConnection conn = (HttpURLConnection) imageUrl
            .openConnection();
        conn.connect();
        /* 取得回传的InputStream */
        InputStream is = conn.getInputStream();
        /* 将InputStream变成Bitmap */
        bitmap = BitmapFactory.decodeStream(is);
        /* 关闭InputStream */
        is.close();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      return bitmap;
    }
    
}
