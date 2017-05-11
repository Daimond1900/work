package com.yifeng.microalloy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.BaseActivity;
import com.yifeng.ChifCloud12345update.R;
import com.yifeng.adapter.AppsAdapter;
import com.yifeng.data.InteractiveDAL;
import com.yifeng.face.SmileyParser;
import com.yifeng.manager.LoginBiz;

/**
 * 编辑界面
 * 
 * @author Administrator
 * 
 */
public class DeclearActivity extends BaseActivity {
	ListView listview;
	TextView top_title;
	LinearLayout top;
	Button back, submit, paizhao;
	AppsAdapter adapter;
	GridView gridView;
	EditText edittext, title;
	ProgressDialog dlg;
	String id;
	ImageView img;
	LinearLayout imgLayout;
	String fileUrl;// 图片地址
	String fileUrl1;// 图片地址

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fbyj);

		submit = (Button) findViewById(R.id.submit);
		paizhao = (Button) findViewById(R.id.paizhao);
		adapter = new AppsAdapter(this);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(adapter);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeclearActivity.this.finish();
			}
		});
		id = getIntent().getStringExtra("id");
		img = (ImageView) findViewById(R.id.img);
		imgLayout = (LinearLayout) findViewById(R.id.imglayout);
		edittext = (EditText) findViewById(R.id.content);
		title = (EditText) findViewById(R.id.title);
		top_title = (TextView) findViewById(R.id.top_title);
		if (id != null) {
			title.setVisibility(View.GONE);
			top_title.setText("回复");
		}
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SmileyParser.init(DeclearActivity.this);
				SmileyParser parser = SmileyParser.getInstance();
				String text = edittext.getText().toString() + "[" + arg2 + "]";
				edittext.setText(parser.addSmileySpans(text));
			}
		});
		this.initBottom();
		this.setFocus(this.bt_bottom_menu3, R.drawable.bottom_menu3_);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendMessage(handler.obtainMessage());
				dlg = new ProgressDialog(DeclearActivity.this);
				dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dlg.setTitle("发送");
				dlg.setMessage("数据发送中，请稍候...");
				dlg.setIndeterminate(false);
				dlg.setCancelable(false);
				dlg.setButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int i) {
						dialog.cancel();
					}
				});
				dlg.show();
			}
		});
		paizhao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				doPickPhotoAction();

			}
		});

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					String stitle = title.getText().toString();
					String sContent = edittext.getText().toString();
					InteractiveDAL dal = new InteractiveDAL(
							DeclearActivity.this);
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user.getUserId());
					if (stitle.equals(""))
						stitle = " ";
					map.put("subject", stitle.replaceAll("\t|\r|\n", ""));
					map.put("key", " " + LoginBiz.loadUser(DeclearActivity.this).getKey());
					map.put("content", " " + sContent.replaceAll("\t|\r|\n", ""));
					if (id == null) {
						dal.sendMessage2(map, fileUrl,bitmap);
						sendEndHandler.sendMessage(sendEndHandler
								.obtainMessage());
					} else {
						map.put("id", id);
						dal.doReply(map, fileUrl,bitmap);
						DeclearActivity.this.setResult(RESULT_OK);
						DeclearActivity.this.finish();
						dlg.cancel();
					}

				}
			}).run();
		}

	};

	Handler sendEndHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(DeclearActivity.this,
							InteractiveActivty.class);
					startActivity(intent);
					dlg.cancel();
					DeclearActivity.this.finish();
				}
			}).run();
		}

	};

	private void doPickPhotoAction() {
		Context context = this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.take_photo); // 拍照
		choices[1] = getString(R.string.pick_photo); // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle(R.string.attachToContact);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							doTakePhoto();// 用户点击了从照相机获取
							break;

						}
						case 1:
							doPickPhotoFromGallery();// 从相册中去获取
							break;
						}
					}

				});
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	// Environment.getExternalStorageDirectory() + "/Camera");
	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
//	String imgName;

	private String getPhotoFileName() {
//		long temp = System.currentTimeMillis();
		String temp="camera_tmp";
		String	imgName = "img_" + temp + ".jpg";
		return imgName;
	}

	private Uri imageFilePath;

	private void doTakePhoto() {
		// PHOTO_DIR.mkdirs();
		// fileUrl1= getPhotoFileName();
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT,
		// Uri.fromFile(new File(PHOTO_DIR, fileUrl1)));
		// startActivityForResult(intent, 20);

		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// ContentValues values = new ContentValues(3);
		// values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
		// values.put(MediaStore.Images.Media.DESCRIPTION,
		// "this is description");
		// values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		// imageFilePath = this.getContentResolver().insert(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
		// startActivityForResult(intent, 20);

		// TODO Auto-generated method stub
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 存储卡可用 将照片存储在 sdcard
		if (isHasSdcard()) {

			/**
			 * 拍照
			 */
			String filePath = android.os.Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/";
			File photoPath = new File(filePath);
			if (!photoPath.exists()) {
				photoPath.mkdirs();
			}
			File PHOTO_DIR = new File(filePath, getPhotoFileName());

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PHOTO_DIR));
		}
		startActivityForResult(intent, 2);

	}

	/**
	 * 检查存储卡是否插入
	 * 
	 * @return
	 */
	public boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public String IMAGE_CAPTURE_NAME = "cameraTmp.png"; // 照片名称

	public static final String IMAGE_UNSPECIFIED = "image/*";

	/**
	 * 选取本地图片
	 * */
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, 20);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == NONE)
			return;

//		if (data == null)
//			return;

		if (requestCode == 20) {
			ContentResolver resolver = getContentResolver();

			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			fileUrl = cursor.getString(1);

 
				Bitmap img1 =this.getImage( fileUrl);
				img.setImageBitmap(img1);
				imgLayout.setVisibility(View.VISIBLE);
			 

		} else if (requestCode == 2) {

			if (isHasSdcard()) {
				 
				fileUrl = android.os.Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + getPhotoFileName();
				Bitmap img1 =this.getImage( fileUrl);
				img.setImageBitmap(img1);
				imgLayout.setVisibility(View.VISIBLE);

			} else {

				// 存储卡不可用直接返回缩略图
				Bundle extras = data.getExtras();
				bitmap = (Bitmap) extras.get("data");
			}

		}

	}

	/*
	 * 获得图片 图片高度 最大maxH
	 * 
	 * @param imagePath
	 */
	Bitmap bitmap;
	int maxH = 200;//

	public Bitmap getImage(String imagePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		bitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回bm为空
		// 计算缩放比
		int be = (int) (options.outHeight / (float) maxH);
		int ys = options.outHeight % maxH;// 求余数
		float fe = ys / (float) maxH;
		if (fe >= 0.5)
			be = be + 1;
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;

		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		return bitmap;
	}

}