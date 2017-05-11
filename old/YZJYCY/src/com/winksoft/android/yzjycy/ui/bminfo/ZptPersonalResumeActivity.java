package com.winksoft.android.yzjycy.ui.bminfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.RegisterDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName:PersonalResumeActivity 
 * Description：报名信息-个人详情的处理
 * @author Administrator 
 * Date：2012-10-26
 */
public class ZptPersonalResumeActivity extends BaseActivity {
	private final String TAG = "ZptPersonalResumeActivity"; 
			
	private TextView head_tipTxt, userName, birth_place, id_cardno, graduation_school, major, major_desp, job_unit, job_post, work_year, oneself_desp,
			family_address, contact_phone, email_add, mobile_num, qq_number, edu_training, work_experience, request_jobpost, other_request, marriage_state,
			politics_status, educational_history, work_area, job_nature, graduation_date, sr;
	private Button leftBtn, rightBtn, backBtn;
	private LinearLayout buttons;
	private ProgressDialog progressDialog;
	private RegisterDAL registerDAL;
	private Map<String, String> returnMap;
	private ImageView head;
	private Bitmap headBamp;
	private List<NameValuePair> leftParams, rightParams;
	private String leftResult, rightResult;
	private Intent qz;
	private String flag = "";// 0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用
	private String reasonStr = "", detailStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_register_personal_resume);

		registerDAL = new RegisterDAL(this);

		qz = this.getIntent();

		initPage();

		showProg("正在加载信息,请稍等...");
		new Thread(loadRunnable).start();

	}

	private void showProg(String Msg) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(Msg);
		progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		progressDialog.show();
	}

	/**
	 * 初始化界面
	 */
	private void initPage() {
		head = (ImageView) findViewById(R.id.head);

		MyClick click = new MyClick();
		leftBtn = (Button) findViewById(R.id.leftBtn);
		leftBtn.setOnClickListener(click);
		rightBtn = (Button) findViewById(R.id.rightBtn);
		rightBtn.setOnClickListener(click);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(click);

		buttons = (LinearLayout) findViewById(R.id.buttons);

		flag = qz.getStringExtra("c_tag");

		/* -------状态（0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用）------- */
		if ("0".equals(flag)) {
			leftBtn.setText("通知面试");
		} else if ("1".equals(flag)) {
			buttons.setVisibility(View.GONE);
		} else if ("2".equals(flag)) {
			leftBtn.setText("录   用");
		} else {
			buttons.setVisibility(View.GONE);
		}

		head_tipTxt = (TextView) findViewById(R.id.head_tipTxt);// 是否有头像提示
		userName = (TextView) findViewById(R.id.userName);// 姓名
		birth_place = (TextView) findViewById(R.id.birth_place);// 籍 贯
		work_year = (TextView) findViewById(R.id.work_year);// 工龄
		contact_phone = (TextView) findViewById(R.id.contact_phone);// 联系电话
		email_add = (TextView) findViewById(R.id.email_add);// emial
		qq_number = (TextView) findViewById(R.id.qq_number);// QQ号码
		family_address = (TextView) findViewById(R.id.family_address);// 家庭住址
//		mobile_num = (TextView) findViewById(R.id.mobile_num);// 手机号码
		id_cardno = (TextView) findViewById(R.id.id_cardno);// 身份证号码
		oneself_desp = (TextView) findViewById(R.id.oneself_desp);// 自我描述
		edu_training = (TextView) findViewById(R.id.edu_training);// 教育培训
		graduation_date = (TextView) findViewById(R.id.graduation_date);// 毕业时间
		graduation_school = (TextView) findViewById(R.id.graduation_school);// 毕业学校
		major = (TextView) findViewById(R.id.major);// 所学专业
		major_desp = (TextView) findViewById(R.id.major_desp);// 专业描述
		job_unit = (TextView) findViewById(R.id.job_unit);// 在职单位
		job_post = (TextView) findViewById(R.id.job_post);// 在职岗位
		work_experience = (TextView) findViewById(R.id.work_experience);// 工作经验
		request_jobpost = (TextView) findViewById(R.id.request_jobpost);// 求职岗位
		other_request = (TextView) findViewById(R.id.other_request);// 其它
		marriage_state = (TextView) findViewById(R.id.marriage_state);// 婚姻状况
		politics_status = (TextView) findViewById(R.id.politics_status);// 政治面貌
		educational_history = (TextView) findViewById(R.id.educational_history);// 学历水平
		work_area = (TextView) findViewById(R.id.work_area);// 工作地区
		job_nature = (TextView) findViewById(R.id.job_nature);// 工作性质
		sr = (TextView) findViewById(R.id.sr);// 出生年月

	}

	Runnable loadRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(300);
				returnMap = registerDAL.queryResumeDetail(qz.getStringExtra("id"),"1");
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
			switch (msg.what) {
			case 1:
				initData();
				break;
			case 2:
				// 获取头象
//				if (headBamp != null) {
//					head.setImageBitmap(headBamp);
//					head_tipTxt.setVisibility(View.GONE);
//				}
				
				break;
			case 3:
				Map<String, String> rightMap = new HashMap<String, String>();
				rightMap = DataConvert.toMap(rightResult);
				Boolean rightSuccess = Boolean.parseBoolean(rightMap.get("success"));
				String rightMsg = rightMap.get("msg").toString();
				if (rightSuccess == true) {
					showMsg("提示", rightMsg);
				} else {
					dialogUtil.showMsg("提示", rightMsg);
				}
				break;
			case 4:
				Map<String, String> leftMap = new HashMap<String, String>();
				leftMap = DataConvert.toMap(leftResult);
				Boolean leftSuccess = Boolean.parseBoolean(leftMap.get("success"));
				String leftMsg = leftMap.get("msg").toString();
				if (leftSuccess == true) {
					showMsg("提示", leftMsg);
				} else {
					dialogUtil.showMsg("提示", leftMsg);
				}
				break;
			default:
				break;
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		};
	};

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (returnMap.get("state").equals(String.valueOf(Constants.LOGIN_SUCCESS))) {
			userName.setText(returnMap.get("xm"));// 姓名
			birth_place.setText(returnMap.get("jg"));// 籍贯
			id_cardno.setText(returnMap.get("sfzh"));// 身份证号码
			graduation_school.setText(returnMap.get("byxx"));// 毕业学校
			major.setText(returnMap.get("zy"));// 所学专业
			major_desp.setText(returnMap.get("zyms"));// 专业描述
			job_unit.setText(returnMap.get("zzdw"));// 在职单位
			job_post.setText(returnMap.get("zzzw"));// 在职岗位
			work_year.setText(returnMap.get("gl"));// 工龄
			oneself_desp.setText(returnMap.get("zwms"));// 自我描述
			family_address.setText(returnMap.get("jtzz"));// 家庭地址
			contact_phone.setText(returnMap.get("lxdh"));// 联系电话
			email_add.setText(returnMap.get("email"));// 电子邮箱
//			mobile_num.setText(returnMap.get("sj"));// 手机号码
			qq_number.setText(returnMap.get("qq"));// QQ号码
			edu_training.setText(returnMap.get("jypxjl"));// 教育培训
			work_experience.setText(returnMap.get("gzjl"));// 工作经历
			request_jobpost.setText(returnMap.get("qzgw"));// 求职岗位
			other_request.setText(returnMap.get("qtyq"));// 其它要求
			graduation_date.setText(returnMap.get("bysj"));// 毕业时间
			marriage_state.setText(returnMap.get("hyzk"));// 婚姻状况
			politics_status.setText(returnMap.get("zzmm"));// 政治面貌
			educational_history.setText(returnMap.get("xl"));// 学历水平
			work_area.setText(returnMap.get("gzdq"));// 工作区域
			job_nature.setText(returnMap.get("gzxz"));// 工作性质
			sr.setText(returnMap.get("sr"));// 生日
			
//			 String imgUrl = returnMap.get("pic_addr");
//		      if(imgUrl !=null && !imgUrl.equals("")){//从远程加载图片
//		    	  headBamp=CommonUtil.getURLBitmap(Constants.ip+"images/"+imgUrl.replace("\\", "/"));
//		    	  myHandler.sendEmptyMessage(2);
//		      }
			
			if(returnMap.get("pic_addr")!=null && !returnMap.get("pic_addr").equals("")){
//				head.setBackgroundDrawable(null);
				ImageLoader.getInstance().displayImage(Constants.IP +"images/"+ returnMap.get("pic_addr").replace("\\", "/"),head);
				head_tipTxt.setText("");
			}
		} else {
			dialogUtil.shortToast("信息加载失败!");

		}
	}

	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				ZptPersonalResumeActivity.this.finish();
				break;
			case R.id.leftBtn:
				showAgreeDialog("提示");
				break;
			case R.id.rightBtn:
				/* -------状态（0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用）------- */
				if ("0".equals(flag)) {
					showRefuseDialog("提示", "简历审核未通过！");
				} else if ("2".equals(flag)) {
					showRefuseDialog("提示", "面试未通过！");
				}
				break;
			default:
				break;
			}
		}
	}
	
	/*
	 * 录用、通知面试
	 */
	private void doLeft() {
		leftParams = new ArrayList<NameValuePair>();
		leftParams.add(new BasicNameValuePair("aab001", user.getUserId()));// 公司名称
		leftParams.add(new BasicNameValuePair("sending_id", qz.getStringExtra("c_id")));
		leftParams.add(new BasicNameValuePair("remark", detailStr));// 输入的具体信息
		//System.out.println("------------------->reason:" + detailStr);
		Log.d(TAG, "doLeft： "+"参数是： "+leftParams.toString());
		showProg("正在处理,请稍等...");
		new Thread(leftRunnable).start();
	}
	/*
	 * 录用、通知面试
	 */
	private Runnable leftRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				/* -------状态（0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用）------- */
				if ("0".equals(flag)) {
					leftResult = registerDAL.doUpLeft(leftParams, "android/sending/passTrial");
					Log.d(TAG, "doLeft： "+"接口是： "+"android/sending/passTrial");
				} else if ("2".equals(flag)) {
					leftResult = registerDAL.doUpLeft(leftParams, "android/sending/passInterview");
					Log.d(TAG, "doLeft： "+"接口是： "+"android/sending/passInterview");
				}
				//System.out.println("------------------->leftParams:" + leftParams);
				myHandler.sendEmptyMessage(4);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	
	/*
	 * 拒绝
	 */
	private void doRight() {
		rightParams = new ArrayList<NameValuePair>();
		rightParams.add(new BasicNameValuePair("sending_id", qz.getStringExtra("c_id")));// sending_id
		rightParams.add(new BasicNameValuePair("aab001", user.getUserId()));// 公司名称
		rightParams.add(new BasicNameValuePair("remark", reasonStr));// 输入的拒绝原因
		Log.d(TAG, "doRight： "+"参数是： "+rightParams.toString());
		showProg("正在处理,请稍等...");
		new Thread(rightRunnable).start();
	}

	private Runnable rightRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				rightResult = registerDAL.doUpRight(rightParams, "android/sending/rejectSending");
				//System.out.println("------------------->rightParams:" + rightParams);
				Log.d(TAG, "doRight： "+"接口是： "+"android/sending/rejectSending");
				myHandler.sendEmptyMessage(3);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	

	/**
	 * 可输入对话框-供通知面试和录用时使用
	 * @param title
	 * @param msg
	 * @param inputContnet
	 */
	private void showAgreeDialog(String title) {
		final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
		builder.setContentView(R.layout.zpt_register_input_dialog_agree);
		TextView titleTxt = (TextView) builder.findViewById(R.id.titleTxt);
		titleTxt.setText(title);
		final EditText contentEdt = (EditText) builder.findViewById(R.id.contentEdt);
		final TextView countTxt = (TextView) builder.findViewById(R.id.countTxt);
		contentEdt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			int num = 150;// 限制的最大字数

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();
				countTxt.setText("" + number + "/150");
				selectionStart = contentEdt.getSelectionStart();
				selectionEnd = contentEdt.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					contentEdt.setText(s + "/150");
					contentEdt.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});

		Button confirmBtn = (Button) builder.findViewById(R.id.confirmBtn);
		Button cancelBtn = (Button) builder.findViewById(R.id.cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				detailStr = contentEdt.getText().toString();
				//System.out.println("-------------------->detailStr：" + detailStr);
				doLeft();
				builder.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}

	/**
	 * 可输入对话框-供拒绝时使用
	 * @param title
	 * @param msg
	 * @param inputContnet
	 */
	private void showRefuseDialog(String title, final String content1) {
		final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
		builder.setContentView(R.layout.zpt_register_input_dialog_refuse);
		TextView titleTxt = (TextView) builder.findViewById(R.id.titleTxt);
		titleTxt.setText(title);
		TextView contentTxt = (TextView) builder.findViewById(R.id.contentTxt);
		contentTxt.setText(content1);
		final EditText contentEdt = (EditText) builder.findViewById(R.id.contentEdt);
		final TextView countTxt = (TextView) builder.findViewById(R.id.countTxt);
		contentEdt.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			int num = 150;// 限制的最大字数

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();
				countTxt.setText("" + number + "/150");
				selectionStart = contentEdt.getSelectionStart();
				selectionEnd = contentEdt.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					contentEdt.setText(s + "/150");
					contentEdt.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});

		Button confirmBtn = (Button) builder.findViewById(R.id.confirmBtn);
		Button cancelBtn = (Button) builder.findViewById(R.id.cancelBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String inputStr = contentEdt.getText().toString();
				if (inputStr.equals("")) {
					reasonStr = content1;
				} else {
					reasonStr = contentEdt.getText().toString();
				}
				doRight();
				builder.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		builder.setCanceledOnTouchOutside(true);
		builder.show();
	}

	/**
	 * 确认提示对话框
	 * 
	 * @param context
	 * @return
	 */
	private void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
		builder.setContentView(R.layout.zpt_dialog);
		TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
		TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
		ptitle.setText(title);
		pMsg.setText(msg);
		Button btn = (Button) builder.findViewById(R.id.pBtn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
//				Intent intent = new Intent(PersonalResumeActivity.this, RegisterCountListActivity.class);
//				startActivity(intent);
//				PersonalResumeActivity.this.finish();
			}
		});
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		builder.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				ZptPersonalResumeActivity.this.finish();
			}
		});
		builder.show();
	}

}
