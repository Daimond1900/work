package com.winksoft.android.yzjycy.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.winksoft.android.yzjycy.AppContext;

import android.content.SharedPreferences;

/**
 * Http请求
 * 
 * @author 吴家宏 2011年03月19日
 * 
 */
public class HttpPostGetUtil {
	private static HttpClient httpClient;
	private static HttpParams httpParams;
	private static Map<String, String> cookie;
	private static final String CHARSET = HTTP.UTF_8;
	private static final String COOKIES = "COOKIES";
	public static String SERVICEERROR = "SERVICEERROR";
	public static String NONETWORKERROR = "NONETWORKERROR";
	public static String SERVERNOTFOUND = "SERVERNOTFOUND";
	public static String JSONERROR = "JSONERROR";
	public static String ERROR = "ERROR";
	public static String TIMEOUTERROR = "TIMEOUTERROR";

	public static String doGetLogin(String url, Map params) {

		/* 建立HTTPGet对象 */

		String paramStr = "";
		if (params != null) {
			Iterator iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				paramStr += paramStr = "&" + key + "=" + val;
			}

			if (!paramStr.equals("")) {
				paramStr = paramStr.replaceFirst("&", "?");
				url += paramStr;
			}
		}

		String strResult = "error";

		try {
			HttpGet httpRequest = new HttpGet(url);

			/* 设置OA Cookie */
			setGetCookie(httpRequest);

			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				// 获取 oa用到的cookie
				getCookie(httpResponse);

				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

			} else {
				strResult = "error";
			}
		} catch (ClientProtocolException e) {
			strResult = "error";
		} catch (IOException e) {
			strResult = "error";
		} catch (Exception e) {
			strResult = "error";
		}

		// Log.v("strResult", strResult);

		return strResult;
	}

	/*
	 * Get请求 参数请求路径
	 */
	public static String doGet(String url, Map params) {

		/* 建立HTTPGet对象 */

		String paramStr = "";
		if (params != null) {
			Iterator iter = params.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				paramStr += paramStr = "&" + key + "=" + val;
			}

			if (!paramStr.equals("")) {
				paramStr = paramStr.replaceFirst("&", "?");
				url += paramStr;
			}
		}

		String strResult = "error";

		try {
			HttpGet httpRequest = new HttpGet(url);

			/* 设置OA Cookie */
			setGetCookie(httpRequest);

			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

			} else {
				strResult = "error";
			}
		} catch (ClientProtocolException e) {
			strResult = "error";
		} catch (IOException e) {
			strResult = "error";
		} catch (Exception e) {
			strResult = "error";
		}

		// Log.v("strResult", strResult);

		return strResult;
	}

	/**
	 * 取得cookie头信息
	 * 
	 * @param httpResponse
	 * @return
	 */
	private static void getCookie(HttpResponse httpResponse) {
		if (httpResponse != null) {
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				// 只取cookie
				if (headers[i].getName().equals("Set-Cookie")) {
					String str = headers[i].getValue();
					setCookies(str);
					break;
				}
			}
		}
	}

	private static void setCookies(String cookie) {
		SharedPreferences cookies = AppContext.get().getSharedPreferences(
				COOKIES, 0);
		cookies.edit().putString("COOKIES", cookie).commit();
	}

	/**
	 * 设置提交Post 方法cookie到服务器
	 * 
	 * @param httpRequest
	 */
	public static void setPostCookie(HttpPost httpRequest) {
		SharedPreferences cookies = AppContext.get().getSharedPreferences(
				COOKIES, 0);
		String str = cookies.getString("COOKIES", "");
		if (!str.equals("")) {
			httpRequest.setHeader("Cookie", str);
		}
	}

	/**
	 * 设置提交 Get 方法 cookie到服务器
	 * 
	 * @param httpRequest
	 */
	public static void setGetCookie(HttpGet httpRequest) {
		SharedPreferences cookies = AppContext.get().getSharedPreferences(
				COOKIES, 0);
		String str = cookies.getString("COOKIES", "");
		if (!str.equals("")) {
			httpRequest.setHeader("Cookie", str);
		}
	}

	/**
	 * POST请求服务器
	 * 
	 * @param list提交参数
	 * @param strTo
	 * @param strTarget
	 * @return
	 */
	public static String doPost(String url, List<NameValuePair> params) {

		/* 建立HTTPPost对象 */
		String strResult = "error";
		try {
			HttpPost httpRequest = new HttpPost(url);

			/* 设置Cookie */
			setPostCookie(httpRequest);

			/*
			 * if(params!=null){ String myCookie=""; for (Iterator iterator =
			 * params.iterator(); iterator.hasNext();) { BasicNameValuePair
			 * nameValuePair = (BasicNameValuePair) iterator.next();
			 * if(nameValuePair.getName().equals("Cookie")){ myCookie=
			 * nameValuePair.getValue(); } } if(!myCookie.equals("")){
			 * httpRequest.setHeader("SPRING_SECURITY_REMEMBER_ME_COOKIE",
			 * myCookie); } }
			 */

			/* 添加请求参数到请求对象 */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

			} else {
				System.out.println("服务器返回状态:"
						+ httpResponse.getStatusLine().getStatusCode());
				strResult = "error";
			}
		} catch (ClientProtocolException e) {
			strResult = "error";
		} catch (IOException e) {
			e.printStackTrace();
			strResult = "error";
		} catch (Exception e) {
			strResult = "error";
		}

		// Log.v("strResult", strResult);

		return strResult;
	}

	public static String doPost1(String url, List<NameValuePair> params) {

		/* 建立HTTPPost对象 */
		String strResult = "error";
		try {
			HttpPost httpRequest = new HttpPost(url);

			/* 设置Cookie */
			setPostCookie(httpRequest);

			/*
			 * if(params!=null){ String myCookie=""; for (Iterator iterator =
			 * params.iterator(); iterator.hasNext();) { BasicNameValuePair
			 * nameValuePair = (BasicNameValuePair) iterator.next();
			 * if(nameValuePair.getName().equals("Cookie")){ myCookie=
			 * nameValuePair.getValue(); } } if(!myCookie.equals("")){
			 * httpRequest.setHeader("SPRING_SECURITY_REMEMBER_ME_COOKIE",
			 * myCookie); } }
			 */

			/* 添加请求参数到请求对象 */
			httpRequest
					.setEntity(new UrlEncodedFormEntity(params, "iso-8859-1"));
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = httpClient.execute(httpRequest);

			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				// 获取 oa用到的cookie
				getCookie(httpResponse);

				/* 读返回数据 */
				strResult = EntityUtils.toString(httpResponse.getEntity());

			} else {
				System.out.println("服务器返回状态:"
						+ httpResponse.getStatusLine().getStatusCode());
				strResult = "error";
			}
		} catch (ClientProtocolException e) {
			strResult = "error";
		} catch (IOException e) {
			e.printStackTrace();
			strResult = "error";
		} catch (Exception e) {
			strResult = "error";
		}

		// Log.v("strResult", strResult);

		return strResult;
	}

	/*
	 * 连接远程服务器设置
	 */
	public static HttpClient getHttpClient() {

		// 为了实现单例，如果是第一次才需创建
		if (httpClient == null) {

			// 初始化
			cookie = new HashMap<String, String>();

			// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）

			httpParams = new BasicHttpParams();

			// 设置一些基本参数

			// 设置请求版本 HttpProtocolParams.setVersion(httpParams,
			// HttpVersion.HTTP_1_1);
			// 设置编码 HttpProtocolParams.setContentCharset(httpParams, CHARSET);

			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小

			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(httpParams, 1000);

			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);

			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

			/* 以及 Socket 缓存大小 */
			HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

			// 设置重定向，缺省为 true

			HttpClientParams.setRedirecting(httpParams, true);

			// 检测代理设置
			/**
			 * String proxyHost = Proxy.getHost(context); int proxyPort =
			 * Proxy.getPort(context);
			 * 
			 * if (!TextUtils.isEmpty(proxyHost)) { HttpHost proxy = new
			 * HttpHost(proxyHost, proxyPort);
			 * httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy); }
			 */
			// HttpHost proxy = new HttpHost("132.234.6.85", 809);
			// httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

			// 设置 user agent

			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";

			HttpProtocolParams.setUserAgent(httpParams, userAgent);

			// 创建一个 HttpClient 实例

			// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

			// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParams, schReg);

			httpClient = new DefaultHttpClient(conMgr, httpParams);

		}

		return httpClient;
	}

}
