package com.winksoft.android.yzjycy.ui.pxxx;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.GpsUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.yifeng.nox.android.http.entity.FormFile;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KqInfoSureActivity extends BaseActivity implements OnClickListener {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private static final String TAG = "Kq";
    private Button back;
    private Button btn_nokq;
    private Button btn_yeskq;
    private ImageView kqImageBtn;
    private TextView logo_tipTxt;
    private FileUtils fileUtil;
    private String fileName = "", fileUrl = "";// 图片地址
    public static final int NONE = 0;
    private Bitmap postBitmap; // 照片
    private LocationManager locationManager;// 位置管理类
    private String provider;// 位置提供器
    private Double longitude = 0.0; // 经度
    private Double latitude = 0.0; // 纬
    // private String sj = ""; // 签到时间 private Map<String,String>
    private Map<String, String> postParams, postResult;
    private ProgressDialog progressDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    pxDAL dal;
    private boolean isPost;
    private String class_id = "";
    private String isCheck = "" , course_id = "";
    Dialog proDialog;
    private List<FormFile> listForm = new ArrayList<>();
    private CommonUtil commonUtil;
    private XwzxDAL xwzxDAL;
    private GpsUtil gpsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kqinfo_activity);
        fileUtil = new FileUtils();
        fileName = DateUtil.getStrCurrentDate() + ".jpg";
        dal = new pxDAL(this, new Handler());
        isCheck = this.getIntent().getStringExtra("ischeck");
        course_id = this.getIntent().getStringExtra("course_id");
        commonUtil = new CommonUtil(this);
        xwzxDAL = new XwzxDAL(this);
        initView();
        //定位
        try {
//            gpsUtil = GpsUtil.getInstance(this);
//            gpsUtil.startMonitor();
//            latitude = gpsUtil.getBaseLocation().latitude;
//            longitude = gpsUtil.getBaseLocation().longitude;
//            Log.d(TAG, "onCreate: 经纬度信息 " + latitude+"==== : ====" + longitude);

            gpsUtil = GpsUtil.getInstance(this);
            gpsUtil.startMonitor();
            GpsUtil.mHandler = this.mHandler;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        locationInfo();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == GpsUtil.SEND_GPS_MSG) {
                //定位成功，传给html界面
                //webView.loadUrl("javascript:onLocationByAndroid('"+GpsUtil.address+"','"+GpsUtil.longitude+"','"+GpsUtil.latitude+"')");
                latitude = gpsUtil.getBaseLocation().latitude;
                longitude = gpsUtil.getBaseLocation().longitude;
                Log.d(TAG, "handleMessage: 定位信息" + latitude + "=====================" + longitude);
                if (gpsUtil != null)
                    gpsUtil.stopMonitor();//定位成功即时关掉避免重复定位
            }
        }
    };


    private void initView() {
        kqImageBtn = (ImageView) findViewById(R.id.kqImageBtn);
        kqImageBtn.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        btn_nokq = (Button) findViewById(R.id.btn_nokq);
        btn_nokq.setOnClickListener(this);
        btn_yeskq = (Button) findViewById(R.id.btn_yeskq);
        btn_yeskq.setOnClickListener(this);
        logo_tipTxt = (TextView) findViewById(R.id.logo_tipTxt);
        class_id = this.getIntent().getStringExtra("class_id");
    }


//    private void locationInfo() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        // 获取所有可用的位置提供器
//        List<String> providerList = locationManager.getProviders(true);
//        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
//            // 优先使用gps
//            provider = LocationManager.GPS_PROVIDER;
//        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
//            provider = LocationManager.NETWORK_PROVIDER;
//        } else {
//            // 没有可用的位置提供器
//            Toast.makeText(KqInfoSureActivity.this, "请开启定位功能后，完成考勤！", Toast.LENGTH_LONG).show();
//            Intent i = new Intent();
//            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(i);
//            return;
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(provider);
//        if (location != null) {
//            // 显示当前设备的位置信息
//            showLocation(location);
//        } else {
//            commonUtil.shortToast("无法获得当前位置");
//        }
//
//        // 更新当前位置
//        locationManager.requestLocationUpdates(provider, 5 * 1000, 1, locationListener);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsUtil.stopMonitor();
//        if (locationManager != null) {
//            // 关闭程序时将监听器移除
//            locationManager.removeUpdates(locationListener);
//        }

    }

//    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//    };

    /**
     * 显示当前设备的位置信息
     */
//    private void showLocation(Location location) {
//        longitude = location.getLongitude() + "";
//        latitude = location.getLatitude() + "";
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.kqImageBtn: // 添加现场图片
                doTakePhoto();
                break;
            case R.id.btn_nokq: // 取消
                this.finish();
                break;
            case R.id.btn_yeskq: // 确定
                isPost = true;
                postData();
                onPostData();
                break;

            default:
                break;
        }
    }

    /**
     * 拍照
     */
    private void doTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 存储卡可用 将照片存储在 sdcard
        if (fileUtil.checkSDCard()) {
            String filePath = fileUtil.getSDPATH() + "/";
            File photoPath = new File(filePath);
            if (!photoPath.exists()) {
                photoPath.mkdirs();
            }
            File PHOTO_DIR = new File(filePath, fileName);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PHOTO_DIR));

            startActivityForResult(intent, 2);
        } else {
            commonUtil.shortToast("当前sdcard不可用!");
        }
    }

    /**
     * 图片裁剪
     */
    private void cutPhoto(String path) {
        File file = new File(path);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 9);// 裁剪框比例
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 450);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "图片裁剪"), 3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;
        if (requestCode == 20) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            fileUrl = cursor.getString(1);
            try {
                cutPhoto(fileUrl);
            } catch (Exception e) {
                System.out.println("图片获取失败!");
            }
        }
        if (requestCode == 2) {
            if (fileUtil.checkSDCard()) {
                fileUrl = fileUtil.getSDPATH() + "/" + fileName;
                try {
                    cutPhoto(fileUrl);
                } catch (Exception e) {
                    System.out.println("图片获取失败!");
                }
            } else {
                // 存储卡不可用直接返回缩略图
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                postBitmap = bitmap;
                logo_tipTxt.setVisibility(View.GONE);
            }

        }
        if (requestCode == 3) {
            // 剪切过后的图片
            Bitmap bmp = data.getParcelableExtra("data");
            postBitmap = bmp;
            kqImageBtn.setImageBitmap(bmp);
            logo_tipTxt.setVisibility(View.GONE);

        }
    }

    /***
     * 提交数据
     */
    private void postData() {
        if (isPost) {

            postParams = new HashMap<String, String>();
            postParams.put("chk_lng", longitude + "");
            postParams.put("chk_lat", latitude + "");
            Log.d(TAG, "postData: longitude " + longitude + "" + "::::" + "latitude" + latitude + "");
            // postParams.put("sj", sj == null && "".equals(sj) ? "" : sj);
            postParams.put("userid", user.getUserId() == null && "".equals(user.getUserId()) ? "" : user.getUserId());
            postParams.put("class_id", class_id == null && "".equals(class_id) ? "" : class_id);
            postParams.put("chk_type", isCheck);    // 签到
            postParams.put("course_id", course_id);
            String secret_value = DateUtil.getStrCurrentDate();
            postParams.put("Date", secret_value);
            HashMap SignHashMap = ParamSignUtils.sign(postParams, secret_key);
            postParams.put("appSign", (String) SignHashMap.get("appSign"));
            byte[] pic = null;
            FormFile[] files = null;
            byte[] data = null;
            if (postBitmap != null) {
                data = com.yifeng.nox.android.util.CommonUtil.Bitmap2Bytes(postBitmap);
                FormFile f1 = new FormFile(fileName, data, "img", "image/pjpeg");
                listForm.add(f1);
            }
        }
    }

    /**
     * 提交数据 postParams listForm
     */
    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        KqInfoSureActivity.this, "正在提交,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.postResume(postParams, listForm, callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "postResult: map = " + map);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(commonUtil.getMapValue(map, "msg"));
            } else if ("true".equals(map.get("success"))) {
                KqInfoSureActivity.this.finish();
            }
        } else {
            commonUtil.shortToast("系统正忙,请稍后再试!");
        }
    }
}
