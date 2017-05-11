package com.yifeng.util;

import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.os.Handler; 
import android.os.Message; 
 
public class ImageDownloadThread extends Thread { 
    //单例类 
    private ImageDownloadThread() {} 
    private static ImageDownloadThread imageDownloadThread = null; 
    public static ImageDownloadThread getInstance() { 
        if (imageDownloadThread == null) { 
            imageDownloadThread = new ImageDownloadThread(); 
            imageDownloadThread.start();//创建后立刻运行 
        } 
        return imageDownloadThread; 
    } 
     
    //缓存下载图片 
    private Map<String, String> cache = new HashMap<String, String>();//KEY:图片URL；VALUE:下载后的图片路径 
    public boolean isDownload(String imageUrl) { 
        return cache.containsKey(imageUrl); 
    } 
    public Bitmap downloadWithCache(ImageDownloadItem item) { 
        if (cache.containsKey(item.imageUrl)) { 
            Bitmap bitmap = BitmapFactory.decodeFile(cache.get(item.imageUrl)); 
            return bitmap; 
        } else { 
            addDownloadItem(item); 
        } 
        return null; 
    } 
    public void downloadWithoutCache(ImageDownloadItem item) { 
        addDownloadItem(item); 
    } 
 
    //下载队列 
    private List<ImageDownloadItem> queue = new ArrayList<ImageDownloadItem>(); 
    private synchronized void addDownloadItem(ImageDownloadItem item) { 
        queue.add(item); 
        this.notify();//添加了下载项就激活本线程 
    } 
 
    @Override 
    public void run() { 
        while(true) { 
            while(queue.size() > 0) { 
                ImageDownloadItem item = queue.remove(0); 
                String imagePath = downloadImage(item.imageUrl); 
                //缓存图片路径 
                cache.put(item.imageUrl, imagePath); 
 
                if (item.callback != null) {//需要执行回调来显示图片 
                    item.imagePath = imagePath; 
 
                    //交由UI线程处理 
                    Message msg = handler.obtainMessage(); 
                    msg.obj = item; 
                    handler.sendMessage(msg); 
                } 
            } 
            try { 
                synchronized(this) { 
                    this.wait();//没有下载项时等待 
                } 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
     
    private String downloadImage(String imageUrl) { 
        //TODO 
        //不提供该方法代码 
        //下载部分应该有专门下载文件的类（如：FileDownloadUtil.download(imageUrl)） 
        return ""; 
    } 
 
    private Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            ImageDownloadItem item = (ImageDownloadItem)msg.obj; 
            Bitmap bitmap = BitmapFactory.decodeFile(item.imagePath); 
            item.callback.update(bitmap, item.imageUrl); 
        } 
    }; 
 
    public static class ImageDownloadItem { 
        public String imageUrl;//需要下载的图片URL 
        public String imagePath;//下载的后图片路径 
        public ImageDownloadCallback callback;//回调方法 
    } 
     
    public static interface ImageDownloadCallback { 
        //策略模式，由子类实现 
        public void update(Bitmap bitmap, String imageUrl); 
    } 

} 
 


