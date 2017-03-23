package com.winksoft.android.yzjycy.ui.pxxx;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.resume.QztAddResumeActivity;
import com.winksoft.android.yzjycy.ui.resume.QztPersonalResumeActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;
import com.yifeng.nox.android.afinal.annotation.view.SetView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KqInfoSureActivity extends BaseActivity implements OnClickListener {
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
	private String longitude = ""; // 经度
	private String latitude = ""; // 纬
	// private String sj = ""; // 签到时间 private Map<String,String>
	private Map<String, String> postParams, postResult;
	private ProgressDialog progressDialog;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	pxDAL dal;
	private boolean isPost;
	private String class_id = "";
	private String isCheck = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kqinfo_activity);
		fileUtil = new FileUtils();
		fileName = DateUtil.getStrCurrentDate() + ".jpg";
		dal = new pxDAL(this, new Handler());
		isCheck = this.getIntent().getStringExtra("ischeck");
		initView();
		locationInfo();
	}

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

	private void locationInfo() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			// 优先使用gps
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			// 没有可用的位置提供器
			Toast.makeText(KqInfoSureActivity.this, "请开启定位功能后，完成考勤！", Toast.LENGTH_LONG).show();
			Intent i = new Intent();
			i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(i);
			return;
		}

		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			// 显示当前设备的位置信息
			showLocation(location);
		} else {
			commonUtil.shortToast("无法获得当前位置");
		}

		// 更新当前位置
		locationManager.requestLocationUpdates(provider, 5 * 1000, 1, locationListener);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			// 关闭程序时将监听器移除
			locationManager.removeUpdates(locationListener);
		}

	}

	LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			showLocation(location);
		}
	};

	/**
	 * 显示当前设备的位置信息
	 * 
	 * @param location
	 */
	private void showLocation(Location location) {
		// String currentLocation = "当前的经度是：" + location.getLongitude() + ",\n"
		// + "当前的纬度是：" + location.getLatitude();
		longitude = location.getLongitude() + "";
		latitude = location.getLatitude() + "";
	}

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
			// sj = sdf.format(new Date()); // 当前时间
			isPost = true;
			postData();
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
			// this.dialogUtil.shortToast("当前sdcard不可用!");
			commonUtil.shortToast("当前sdcard不可用!");
		}
	}

	/**
	 * 图片裁剪
	 * 
	 * @param file
	 */
	private void cutPhoto(String path) {
		File file = new File(path);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 9);// 裁剪框比例
		// intent.putExtra("aspectY", 3);
		// intent.putExtra("outputX", 450);// 输出图片大小
		// intent.putExtra("outputY", 150);
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
			// ContentResolver resolver = getContentResolver();
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
			// BitmapDrawable dbm = new BitmapDrawable(bmp);
			// logoBtn.setBackgroundDrawable(dbm);
			kqImageBtn.setImageBitmap(bmp);
			logo_tipTxt.setVisibility(View.GONE);

		}
	}

	/***
	 * 提交数据
	 */
	private void postData() {
		postParams = new HashMap<String, String>();
		postParams.put("chk_lng", longitude == null && "".equals(longitude) ? "" : longitude);
		postParams.put("chk_lat", latitude == null && "".equals(latitude) ? "" : latitude);
		// postParams.put("sj", sj == null && "".equals(sj) ? "" : sj);
		postParams.put("userid", user.getUserId() == null && "".equals(user.getUserId()) ? "" : user.getUserId());
		postParams.put("class_id", class_id == null && "".equals(class_id) ? "" : class_id);
		postParams.put("chk_type", isCheck);	// 签到
		progressDialog = commonUtil.showProgressDialog("正在提交信息,请稍等...");
		new Thread(runnable).start();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				if (isPost) {
					byte[] pic = null;
					FormFile[] files = null;
					if (postBitmap != null) {
						pic = CommonUtil.Bitmap2Bytes(postBitmap);
						// data = FileUtils.readFile(fileUrl);
						FormFile f1 = new FormFile(fileName, pic, "img", "image/pjpeg");
						files = new FormFile[] { f1 };
					}
					postResult = dal.doPostKq(postParams, files);
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(-1);
			}
		}
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (postResult.get("state").equals(String.valueOf(1))) {
					if (postResult.get("success").equals("true")) {
						commonUtil.shortToast(postResult.get("msg"));
						KqInfoSureActivity.this.finish();
					} else {
						commonUtil.shortToast(postResult.get("msg"));
					}
				} else {
					commonUtil.shortToast(postResult.get("msg"));
				}
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		};
	};
}
