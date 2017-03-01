package com.yifeng.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 文件名称：UploadImage.java
 *
 * 版权信息：Apache License, Version 2.0
 * 
 * 功能描述：实现图片文件上传。
 * 
 * 创建日期：2011-5-10
 *
 * 作者：Huagang Li
 */

/*
 * 修改历史：
 */
public class UploadImage{
    String multipart_form_data = "multipart/form-data";
    String twoHyphens = "--";
    String boundary = "****************fD4fH3gL0hK7aI6";    // 数据分隔符
    String lineEnd = System.getProperty("line.separator");    // The value is "\r\n" in Windows.
    
    /*
     * 上传图片内容，格式请参考HTTP 协议格式。
     * 人人网Photos.upload中的”程序调用“http://wiki.dev.renren.com/wiki/Photos.upload#.E7.A8.8B.E5.BA.8F.E8.B0.83.E7.94.A8
     * 对其格式解释的非常清晰。
     * 格式如下所示：
     * --****************fD4fH3hK7aI6
     * Content-Disposition: form-data; name="upload_file"; filename="apple.jpg"
     * Content-Type: image/jpeg
     * 
     * 这儿是文件的内容，二进制流的形式
     */
    private void addImageContent(FormFile[] files, DataOutputStream output) {
        for(FormFile file : files) {
            StringBuilder split = new StringBuilder();
            split.append(twoHyphens + boundary + lineEnd);
            split.append("Content-Disposition: form-data; name=\"" + file.getFormname() + "\"; filename=\"" + file.getFilname() + "\"" + lineEnd);
            split.append("Content-Type: " + file.getContentType() + lineEnd);
            split.append(lineEnd);
            try {
                // 发送图片数据
                output.writeBytes(split.toString());
                output.write(file.getData(), 0, file.getData().length);
                output.writeBytes(lineEnd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /*
     * 构建表单字段内容，格式请参考HTTP 协议格式（用FireBug可以抓取到相关数据）。(以便上传表单相对应的参数值)
     * 格式如下所示：
     * --****************fD4fH3hK7aI6
     * Content-Disposition: form-data; name="action"
     * // 一空行，必须有
     * upload 
     */
    private void addFormField(Map<String, String> params, DataOutputStream output) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> param : params.entrySet()) {
            sb.append(twoHyphens + boundary + lineEnd);
            sb.append("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
            sb.append(lineEnd);
            sb.append(param.getValue() + lineEnd);
        }
        try {
            output.write(sb.toString().getBytes("gbk"));// 发送表单字段数据
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void uploadFile(String actionUrl, Map<String, String> params, FormFile[] files)
    {
    	FormFile file=files[0];
      String end = "\r\n";
      String twoHyphens = "--";
      String boundary = "*****";
      try
      {
        URL url =new URL(actionUrl);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* 设置DataOutputStream */
        DataOutputStream ds = 
          new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; " +
                      "name=\"file1\";filename=\"" +
                      file.getFilname() +"\"" + end);
        ds.writeBytes(end);   

        /* 取得文件的FileInputStream */
//        FileInputStream fStream = new FileInputStream(uploadFile);
        /* 设置每次写入1024bytes */
        int bufferSize = 1024;
        byte[] buffer =  file.getData();

        try {
            // 发送图片数据
            ds.write(file.getData(), 0, file.getData().length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        ds.flush();

        /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) != -1 )
        {
          b.append( (char)ch );
        }
        /* 将Response显示于Dialog */
//        showDialog("上传成功"+b.toString().trim());
        /* 关闭DataOutputStream */
        ds.close();
      }
      catch(Exception e)
      {
//        showDialog("上传失败"+e);
      }
    }


    
    /**
     * 直接通过 HTTP 协议提交数据到服务器，实现表单提交功能。
     * @param actionUrl 上传路径
     * @param params 请求参数key为参数名，value为参数值
     * @param files 上传文件信息
     * @return 返回请求结果
     */
    public String post(String actionUrl, Map<String, String> params, FormFile[] files ) {
        HttpURLConnection conn = null;
        DataOutputStream output = null;
        BufferedReader input = null;
        try {
            URL url = new URL(actionUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn
            .setRequestProperty(
                    "Accept",
                    "image/gif,   image/x-xbitmap,   image/jpeg,   image/pjpeg,   application/vnd.ms-excel,   application/vnd.ms-powerpoint,   application/msword,   application/x-shockwave-flash,   application/x-quickviewplus,   */*");
            conn.setRequestProperty("Accept-Language", "zh-cn");

//            conn.setConnectTimeout(120000);
            conn.setDoInput(true);        // 允许输入
            conn.setDoOutput(true);        // 允许输出
            conn.setUseCaches(false);    // 不使用Cache
            conn .setRequestProperty("User-Agent",
            "Mozilla/4.0   (compatible;   MSIE   6.0;   Windows   NT   5.1)");
            conn.setRequestProperty("Connection", "Keep-Alive");

//            conn.setRequestMethod("POST");


            conn.setRequestProperty("Content-Type", multipart_form_data + "; boundary=" + boundary);
            
            conn.connect();
            output = new DataOutputStream(conn.getOutputStream());
            if(files!=null)
            addImageContent(files, output);    // 添加图片内容
            
            addFormField(params, output);    // 添加表单字段内容
            
            output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
            output.flush();
            
            int code = conn.getResponseCode();
            if(code != 200) {
                throw new RuntimeException("请求‘" + actionUrl +"’失败！");
            }
            
            input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String oneLine;
            while((oneLine = input.readLine()) != null) {
                response.append(oneLine + lineEnd);
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 统一释放资源
            try {
                if(output != null) {
                    output.close();
                }
                if(input != null) {
                    input.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            if(conn != null) {
                conn.disconnect();
            }
//            progressDialog.dismiss();
//            Intent main=new Intent(handler,LoginActivity.class);
//  	      main.putExtra("doInit", doInit);
//  	      startActivity(main);
//            handler.sendMessage( handler.obtainMessage());
        }
    }
    
//    public static void main(String[] args) {
//        String response = "";
//      
//		String actionUrl = "http://192.168.0.208:80/pda/upload/submit.php?P=1;b11ab482da8d2617aef90a62673476ae";
//		
//		// 读取表单对应的字段名称及其值
//         
//     	 Map<String, String> params =new HashMap<String, String>();
// params.put("SUBJECT", "辅导费");
// params.put("CONTENT", "阿什顿撒的");
//		
//		// 读取图片所对应的表单字段名称及图片路径
////            Properties imageParams = new Properties();
////            imageParams.load(new FileInputStream(new File("config/imageParams.properties")));
////            Set<Map.Entry<Object,Object>> images = imageParams.entrySet();
////            FormFile[] files = new FormFile[images.size()];
////            int i = 0;
////            for(Map.Entry<Object,Object> image : images) {
////            	FormFile file = new FormFile(image.getValue().toString(), image.getKey().toString());
////                files[i++] = file;
////            }
////            FormFile file = new FormFile("images/apple.jpg", "upload_file");
////            Image[] files = new Image[0];
////            files[0] = file;
//		byte[] b= CommonUtil.getBytesFromFile(new File("C:\\V.jpg"));
// 		 FormFile formFile=new FormFile("V.jpg",b,"ATTACHMENT","image/jpg");
// 		 FormFile[] files=new FormFile[]{formFile};
////		response = new UploadImage().post(actionUrl, params, files);
//		System.out.println("返回结果：" + response);
//    }
}
