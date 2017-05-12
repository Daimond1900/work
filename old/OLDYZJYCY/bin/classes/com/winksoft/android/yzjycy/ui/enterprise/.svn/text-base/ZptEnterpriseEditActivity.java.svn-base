package com.winksoft.android.yzjycy.ui.enterprise;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.EnterpriseDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.zptmapabc.ZptMapDetailActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.StringHelper;
import com.winksoft.android.yzjycy.BaseActivity;


import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * ClassName:EnterpriseActivity 
 * Description：企业信息-修改企业信息
 * @author Administrator 
 * Date：2012-10-17
 */
public class ZptEnterpriseEditActivity extends BaseActivity {
	private TextView logo_tipTxt, gsTxt, xzTxt, hyTxt, jwdTxt;
	private EditText lxrEdt, dhEdt, czEdt, yxEdt, wzEdt, dzEdt, jjEdt;
//	private Button logoBtn, backBtn, saveBtn;
	private Button backBtn, saveBtn;
	private ImageView logoBtn;
	private TableLayout addressTab;
	private ProgressDialog progressDialog;
	private EnterpriseDAL enterpriseDAL;
	private Map<String, String> resultsMap;
	private String longitude = "1192679470";// 经度
	private String latitude = "32248012";// 纬度
	private String companyNames = "", telNo = "";
	private Bitmap logoBamp, postBitmap;
	private FileUtils fileUtil;
	private String fileName = "", fileUrl = "";// 图片地址
	public static final int NONE = 0;
	private Map<String, String> upParams, returnResult;
	private final int SELECTMAP = 120;
	private String q_lxrStr = "", q_dhStr = "", q_czStr = "", q_yxStr = "", q_wzStr = "", q_dzStr = "", q_jjStr;
	FormFile[] files = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_enterprise_edit);

		fileUtil = new FileUtils();

		enterpriseDAL = new EnterpriseDAL(this);

		initView();

		showProg();// 加载远程数据
		new Thread(loadRunnanle).start();
	}

	private void initView() {
		logo_tipTxt = (TextView) findViewById(R.id.logo_tipTxt);// 是否显示编辑公司logo的提示
		gsTxt = (TextView) findViewById(R.id.gsTxt);// 公司
		xzTxt = (TextView) findViewById(R.id.xzTxt);// 性质
		hyTxt = (TextView) findViewById(R.id.hyTxt);// 行业
		lxrEdt = (EditText) findViewById(R.id.lxrEdt);// 联系人
		dhEdt = (EditText) findViewById(R.id.dhEdt);// 电话
		czEdt = (EditText) findViewById(R.id.czEdt);// 传真
		yxEdt = (EditText) findViewById(R.id.yxEdt);// 邮箱
		wzEdt = (EditText) findViewById(R.id.wzEdt);// 网址
		dzEdt = (EditText) findViewById(R.id.dzEdt);// 地址
		jwdTxt = (TextView) findViewById(R.id.jwdTxt);// 经纬度
		jjEdt = (EditText) findViewById(R.id.jjEdt);// 简介

		MyOnClickListener myclick = new MyOnClickListener();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myclick);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(myclick);

		logoBtn = (ImageView) findViewById(R.id.logoBtn);
		logoBtn.setOnClickListener(myclick);

		addressTab = (TableLayout) findViewById(R.id.addressTabs);
		addressTab.setOnClickListener(myclick);

		fileName = DateUtil.getStrCurrentDate() + ".jpg";

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				// 返回按钮
				ZptEnterpriseEditActivity.this.finish();
				break;
			case R.id.saveBtn:
				// 保存按钮
				doSubmitData();
				break;
			case R.id.logoBtn:
				// 图片编辑按钮
				doChoosePhotos();
				break;
			case R.id.addressTabs:
//				// 企业地址按钮
				Intent map = new Intent(ZptEnterpriseEditActivity.this, ZptMapDetailActivity.class);
				map.putExtra("companyName", gsTxt.getText().toString());
				map.putExtra("companyAddress", q_dzStr);
				map.putExtra("telNo", q_dhStr);
				map.putExtra("longitude", longitude);// 经度
				map.putExtra("latitude", latitude);// 纬度
				startActivityForResult(map, SELECTMAP);
				break;
			default:
				break;
			}
		}
	}

	private void showProg() {
		progressDialog = ProgressDialog.show(this, "请稍等...", "正在加载数据...", true);
	}

	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(300);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("aab001", user.getUserId()));
				resultsMap = enterpriseDAL.doPostQuery(param, "android/corecruitment/queryCompanyDetail");
				myHandler.sendEmptyMessage(1);

			} catch (Exception e) {
				e.printStackTrace();
				myHandler.sendEmptyMessage(-1);
			}
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				setPageData();
			} else if (msg.what == 2) {
				// 获取头象
				if (logoBamp != null) {
					// logoImg.setImageBitmap(logoBamp);
					logo_tipTxt.setVisibility(View.GONE);
					BitmapDrawable dbm = new BitmapDrawable(logoBamp);
					logoBtn.setBackgroundDrawable(dbm);
				}

			} else if (msg.what == 3) {
				if (returnResult.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {

					String messageStr = returnResult.get("msg").toString();
					if (returnResult.get("success").equals("true")) {
						showMsg("提示", messageStr);
						//修改成功设置用户信息到本地
						UserSession session=new UserSession(ZptEnterpriseEditActivity.this);
						session.setUser(user);
						
					} else {
						dialogUtil.showMsg("提示", messageStr);
					}
				}
			}

			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

	/***
	 * 加载数据
	 */
	private void setPageData() {
		if (resultsMap.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			/*
			 * 性质：aab019; 行业 ：aab022; 联系人：aae004; 电话：aae005; 传真：aae014;
			 * 邮箱：aae015; 网址：aae016; 简介：dwjj; 地址：aae006;
			 */
			gsTxt.setText(resultsMap.get("aab004")==null ?user.getZbdw():resultsMap.get("aab004"));
			xzTxt.setText(resultsMap.get("aab019") == null ? "" : resultsMap.get("aab019"));// 性质
			hyTxt.setText(resultsMap.get("aab022") == null ? "" : resultsMap.get("aab022"));// 行业
			// 联系人
			q_lxrStr = resultsMap.get("aae004") == null ? "" : resultsMap.get("aae004");
			lxrEdt.setText(q_lxrStr);
			// 电话
			q_dhStr = resultsMap.get("aae005") == null ? "" : resultsMap.get("aae005");
			dhEdt.setText(q_dhStr);
			// 传真
			q_czStr = resultsMap.get("aae014") == null ? "" : resultsMap.get("aae014");
			czEdt.setText(q_czStr);
			// 邮箱
			q_yxStr = resultsMap.get("aae015") == null ? "" : resultsMap.get("aae015");
			yxEdt.setText(q_yxStr);
			// 网址
			q_wzStr = resultsMap.get("aae016") == null ? "" : resultsMap.get("aae016");
			wzEdt.setText(q_wzStr);
			// 如果公司地址为空就显示公司名字
			q_dzStr = resultsMap.get("aae006") == null ? "" : resultsMap.get("aae006");
			dzEdt.setText(q_dzStr.equals("") ? companyNames : q_dzStr);// 地址
			// 简介
			q_jjStr = resultsMap.get("dwjj") == null ? "" : resultsMap.get("dwjj");
			jjEdt.setText(q_jjStr);
			
			longitude = resultsMap.get("lng") == null ? "" : resultsMap.get("lng");// 经度
			latitude = resultsMap.get("lat") == null ? "" : resultsMap.get("lat");// 纬度
			/* 转换经纬度的显示形式 */
			if (!longitude.equals("") && !latitude.equals("")) {
				try {
//					Double jdDob = Double.parseDouble(longitude) / 1E5;
//					Double wdDob = Double.parseDouble(latitude) / 1E5;
//					jwdTxt.setText("经度：" + jdDob + "；纬度：" + wdDob);
					jwdTxt.setText("经度：" + longitude + "；纬度：" + latitude);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
//			// 从远程加载图片
//						logoBamp = CommonUtil.getURLBitmap(Constants.IP +"images/"+ resultsMap.get("logo_url").replace("\\", "/"));
//						myHandler.sendEmptyMessage(2);
			if(resultsMap.get("logo_url")!=null && !resultsMap.get("logo_url").equals("")){
				ImageLoader.getInstance().displayImage(Constants.IP +"images/"+ resultsMap.get("logo_url").replace("\\", "/"),logoBtn);
			}

		} else {
			dialogUtil.shortToast("信息加载失败!");
		}
	}

	private void doSubmitData() {

		byte[] data = null;
		if (postBitmap != null) {
			data = CommonUtil.Bitmap2Bytes(postBitmap);
			FormFile f1 = new FormFile(fileName, data, "img", "image/pjpeg");
			files = new FormFile[] { f1 };
		}

		String lxrStr = lxrEdt.getText().toString();// 联系人
		String dhStr = dhEdt.getText().toString();// 电话
		String czStr = czEdt.getText().toString();// 传真
		String yxStr = yxEdt.getText().toString();// 邮箱
		String wzStr = wzEdt.getText().toString();// 网址
		String dzStr = dzEdt.getText().toString();// 地址
		String jjStr = jjEdt.getText().toString();// 简介
		//if (lxrStr.equals(q_lxrStr) && dhStr.equals(q_dhStr) && czStr.equals(q_czStr) && yxStr.equals(q_yxStr) && wzStr.equals(q_wzStr)
			//	&& dzStr.equals(q_dzStr) && jjStr.equals(q_jjStr) && files == null) {
			//dialogUtil.shortToast("企业的信息没有变更，无需提交！");
		//} else {
			if (lxrStr.equals("")) {
				dialogUtil.shortToast("联系人不能为空，请重新输入！");
				return;
			} else if (dhStr.equals("")) {
				dialogUtil.shortToast("电话不能为空，请重新输入！");
				return;
			}else if(!isPhoneNumberValid(dhStr)){
				dialogUtil.shortToast("电话格式错误！");
			} else if (dzStr.equals("")) {
				dialogUtil.shortToast("地址不能为空，请重新输入！");
			}else {
				if (!yxStr.equals("")) {
					
					//正则表达式表示邮箱号码
					/*Boolean b = false;
					Pattern p = Pattern.compile("http://([w-]+.)+[w-]+(/[w- ./?%&=]*)?", Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(wzStr);
					b = m.matches();*/
					if (!StringHelper.checkEmail(yxStr)) {
						dialogUtil.shortToast("输入的电子邮箱格式不正确!");
					/*} else if (b == false) {
						dialogUtil.shortToast("输入的网址格式不正确!");*/
					} else {
						showProg1();
						new Thread(uploadRunnable).start();
					}
				} else {
					showProg1();
					new Thread(uploadRunnable).start();
				}
				/** ======= 上传的参数  ======= */
				upParams = new HashMap<String, String>();
				upParams.put("aab001", user.getUserId());// 单位编号
				upParams.put("aae004", lxrStr);// 联系人
				upParams.put("aae005", dhStr);// 电话
				upParams.put("aae014", czStr);// 传真
				upParams.put("aae015", yxStr);// 邮箱
				upParams.put("aae016", wzStr);// 网址
				upParams.put("aae006", dzStr);// 地址
				upParams.put("dwjj", jjStr);// 简介
				upParams.put("lng", longitude);// 经度
				upParams.put("lat", latitude);// 纬度
//				upParams.put("logo_url", logo_url);
				
				//设置用户信息到本地
//				user.setLinkPhone(dhStr);		联系方式
//				user.setcompanyManager(lxrStr);	公司法人
//				user.setLinkAddress(dzStr);		联系地址
//				user.setLongitude(longitude);	经度
//				user.setLatitude(latitude);		纬度
				/**
				*	待解决
				*/
				user.setPhone(dhStr);	//联系电话
				new UserSession(this).setUser(user);
			}
		//}
	}
	
	
	/**
	 * 检查字符串是否为电话号码的方法,并返回true or false的判断值
	 */
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
		boolean isValid = false;
		String expression = "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$|([0-9]{11})";
		CharSequence inputStr = phoneNumber;
		/*创建Pattern*/
		Pattern pattern = Pattern.compile(expression);
		/*将Pattern 以参数传入Matcher作Regular expression*/
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())//|| matcher2.matches())
		{
			isValid = true;
		}
		return isValid;
	}
	
	private void showProg1() {
		progressDialog = ProgressDialog.show(this, "请稍等...", "正在上传数据...", true);
	}

	private Runnable uploadRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);

				returnResult = enterpriseDAL.doChange(upParams, files);
				/*
				 * System.out.println("------------------->" + upParams);
				 * System.out.println("------------------->" + files);
				 */
				myHandler.sendEmptyMessage(3);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(ZptEnterpriseEditActivity.this, R.style.dialog);
		builder.setContentView(R.layout.dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
				Intent intent = new Intent(ZptEnterpriseEditActivity.this, ZptEnterpriseActivity.class);
				startActivity(intent);
				ZptEnterpriseEditActivity.this.finish();
			}
		});
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.show();
	}

	/**
	 * 选择是否拍照还是从相册里取相片
	 */
	private void doChoosePhotos() {
		Context context = this;
		final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices = { "拍照", "从相册中选择" };
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);
		final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("请选择操作");
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0: {
					doTakePhoto();// 用户点击了从照相机获取
					break;
				}
				case 1:
					doChoosePhotosFromGallery();// 从相册中去获取
					break;
				}
			}

		});
		builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.create().show();
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
			this.dialogUtil.shortToast("当前sdcard不可用!");
		}
	}

	/**
	 * 选取本地图片
	 */
	private void doChoosePhotosFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 20);
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
//			BitmapDrawable dbm = new BitmapDrawable(bmp);
//			logoBtn.setBackgroundDrawable(dbm);
			logoBtn.setImageBitmap(bmp);
			logo_tipTxt.setVisibility(View.GONE);

		}

		if (requestCode == SELECTMAP) {
			String lng = data.getStringExtra("longitude");
			String lat = data.getStringExtra("latitude");
			if (!lng.equals("") && !lat.equals("")) {
				try {
					int mlng = (int) (Double.parseDouble(lng) * 1E6);
					int mlat = (int) (Double.parseDouble(lat) * 1E6);
//					longitude = String.valueOf(mlng);
//					latitude = String.valueOf(mlat);
					longitude = lng;
					latitude = lat;
					jwdTxt.setText(Html.fromHtml("纬度：" + lat + "；经度：" + lng));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
