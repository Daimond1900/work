package com.yifeng.hnjcy.asyncload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import com.yifeng.hnjcy.contacts.tool.SystemConstant;
import com.yifeng.hnjcy.ui.AppContext;
import com.yifeng.hnjcy.util.FileUtils;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public synchronized Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback, final String tag) {

		if (imageCache.containsKey(imageUrl)) {
			if (imageCache.get(imageUrl) == null)
				return null;
			Drawable softReference = imageCache.get(imageUrl).get();
			if (softReference != null) {
				Drawable drawable = softReference;
				if (drawable != null) {
					return drawable;
				}
			} else
				imageCache.remove(imageUrl);
			// return null;
		} else {
			imageCache.put(imageUrl, null);
		}
		Drawable d = findByNetUrl(imageUrl);
		if (d != null) {
			imageCache.put(imageUrl, new SoftReference<Drawable>(d));
			return d;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, tag);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				if (drawable != null) {
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
					System.gc(); // 提醒系统及时回收
				}
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		InputStream i = null;
		Drawable d = findByNetUrl(url);// 先到SDCard中查询一下
		if (d == null) {
			try {
				HttpGet httpGet = new HttpGet(url);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = null;
				response = httpclient.execute(httpGet);
				i = response.getEntity().getContent();
				String fileName = System.currentTimeMillis() + ".jpg";
				insert(fileName, url, i);
				File file = new File(SystemConstant.IMAGE_CAPTURE + "head/"
						+ fileName);
				if (file.exists()) {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 2;
					Bitmap tbm = BitmapFactory.decodeFile(
							SystemConstant.IMAGE_CAPTURE + "head/" + fileName,
							opts);
					if (tbm != null)
						d = new BitmapDrawable(tbm);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (i != null)
					try {
						i.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return d;
	}

	public static Bitmap loadImageFromUrl2Bitmap(String url,
			ReturnImage returnImage) {
		URL m;
		InputStream i = null;
		Bitmap d = findByNetUrl2Bitmap(url, returnImage);
		if (d == null) {
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
				String fileName = System.currentTimeMillis() + ".jpg";
				insert(fileName, url, i);

				File file = new File(SystemConstant.IMAGE_CAPTURE + "head/"
						+ fileName);
				if (file.exists()) {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 2;
					Bitmap tbm = BitmapFactory.decodeFile(
							SystemConstant.IMAGE_CAPTURE + "head/" + fileName,
							opts);
					d = tbm;

				}
				if (returnImage != null) {
					returnImage.setBitmap(d);
					returnImage.setPath(SystemConstant.IMAGE_CAPTURE + "head/"
							+ fileName);
				}
				if (i != null)
					i.close();

			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	private static Drawable findByNetUrl(String url) {
		String uri = "";
		Drawable d = null;
		// Bitmap d = null;
		SqliteUtil util = new SqliteUtil(AppContext.get());

		String columnName[] = { "LOCAL_URL" };
		String selectArray[] = { url };
		List<Map<String, Object>> list = util.doQuery("USER_HEAD_IMG",
				columnName, "NET_URL=?", selectArray, null, null, " id desc");

		for (Map<String, Object> m : list) {
			uri = (String) m.get("LOCAL_URL");
			break;
		}

		if (uri != null && !uri.equals("")) {

			// File f = new File(uri);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			Bitmap tbm = BitmapFactory.decodeFile(uri, opts);
			if (tbm != null)
				d = new BitmapDrawable(tbm);
		}
		return d;
	}

	public static synchronized Bitmap findByNetUrl2Bitmap(String url,
			ReturnImage returnImage) {
		String uri = "";
		// Drawable d = null;
		Bitmap d = null;
		SqliteUtil util = new SqliteUtil(AppContext.get());

		String columnName[] = { "LOCAL_URL" };
		String selectArray[] = { url };
		List<Map<String, Object>> list = util.doQuery("USER_HEAD_IMG",
				columnName, "NET_URL=?", selectArray, null, null, " id desc");

		for (Map<String, Object> m : list) {
			uri = (String) m.get("LOCAL_URL");
			break;
		}

		if (uri != null && !uri.equals("")) {

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			Bitmap tbm = BitmapFactory.decodeFile(uri, opts);
			d = tbm;
			if (returnImage != null) {
				returnImage.setPath(uri);
				returnImage.setBitmap(d);
			}
		}
		return d;
	}

	public static synchronized String findByNetUrl2Path(String url) {
		String uri = "";
		// Drawable d = null;
		Bitmap d = null;
		SqliteUtil util = new SqliteUtil(AppContext.get());

		String columnName[] = { "LOCAL_URL" };
		String selectArray[] = { url };
		List<Map<String, Object>> list = util.doQuery("USER_HEAD_IMG",
				columnName, "NET_URL=?", selectArray, null, null, " id desc");

		for (Map<String, Object> m : list) {
			uri = (String) m.get("LOCAL_URL");
			break;
		}

		return uri;
	}

	private static void insert(String fileName, String net_url, InputStream i) {
		File dir = new File(SystemConstant.IMAGE_CAPTURE + "head/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileUtils fUtile = new FileUtils();
		fUtile.write2SDFromInput(SystemConstant.GUAGUA + "/head/", fileName, i);

		SqliteUtil util = new SqliteUtil(AppContext.get());
		Map<String, String> columnNameValue = new HashMap<String, String>();
		columnNameValue.put("LOCAL_URL", SystemConstant.IMAGE_CAPTURE + "head/"
				+ fileName);
		columnNameValue.put("NET_URL", net_url);
		util.insert("USER_HEAD_IMG", columnNameValue);
	}

	/**
	 * 
	 * 释放缓存中所有的Bitmap对象，并将缓存清空
	 */

	public void releaseBitmapCache() {

		if (imageCache != null) {

			for (Entry<String, SoftReference<Drawable>> entry : imageCache
					.entrySet()) {

				if (entry.getValue() == null)
					continue;
				Drawable bitmap = entry.getValue().get();

				if (bitmap != null) {
					BitmapDrawable bd = (BitmapDrawable) bitmap;
					Bitmap bm = bd.getBitmap();
					if (bm != null && bm.isRecycled())
						bm.recycle();
					bm = null;
					// bitmap.recycle();// 释放bitmap对象

					bitmap = null;
					System.gc();
				}

			}

			imageCache.clear();

		}

	}

}