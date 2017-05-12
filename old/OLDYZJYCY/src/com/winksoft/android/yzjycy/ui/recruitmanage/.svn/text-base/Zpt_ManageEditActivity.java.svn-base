package com.winksoft.android.yzjycy.ui.recruitmanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.RecruitmentDAL;
import com.winksoft.android.yzjycy.util.DataConvert;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * ClassName:ManageEditActivity Description：招聘管理-修改招聘信息
 * 
 * @author Administrator Date：2012-10-24
 */
public class Zpt_ManageEditActivity extends BaseActivity {
	private final String TAG = "Zpt_ManageEditActivity";
	private TextView postTxt, dateTxt;
	private EditText numberEdt, fldyEdt, requireEdt,gwsmEdt, zpnxEdt, zpvxEdt, jzEdt,
			ygzEdt;
	private Button backBtn, submitBtn, resetBtn;
	private Button daysSpi;
	private String[] spinnerData = { "半个月", "一个月", "两个月" };// flag:0,1,2
	private String numberStr = "", daysStr = "", wageStr = "", requireStr = "";
	private String days = "", number = "", wage = "", require = "", gwsm,zpnx, zpvx,
			jz, ygz,daysFlag="",ygqy="";
	private ProgressDialog progressDialog;
	private RecruitmentDAL recruitmentDAL;
	private Bundle qz;
	private String returnJson;
	private Spinner work_area;
	private List<Map<String,String>> areas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_manage_edit);

		recruitmentDAL = new RecruitmentDAL(this);

		qz = this.getIntent().getExtras();
		
		gwsmEdt = (EditText)findViewById(R.id.gwsmnumberEdt); 
		zpnxEdt = (EditText) findViewById(R.id.zpnxnumberEdt);
		zpvxEdt = (EditText) findViewById(R.id.zpvxnumberEdt);
		jzEdt = (EditText) findViewById(R.id.jznumberEdt);
		ygzEdt = (EditText) findViewById(R.id.ygzEdt);
		work_area = (Spinner)findViewById(R.id.spn_area);//工作地区

		zpnx = qz.getString("s_zpnx");
		zpvx = qz.getString("s_zpvx");
		jz = qz.getString("s_jz");
		ygz = qz.getString("s_ygz");
		gwsm = qz.getString("s_gwsm");
		ygqy = qz.getString("s_name");
		
				
		zpnxEdt.setText(zpnx);
		zpvxEdt.setText(zpvx);
		jzEdt.setText(jz);
		ygzEdt.setText(ygz);
		gwsmEdt.setText(gwsm);

		days = qz.getString("s_days");
		daysStr = qz.getString("s_days");
		// 岗位
		postTxt = (TextView) findViewById(R.id.postTxt);
		postTxt.setText(qz.getString("s_post"));
		// 福利待遇
		fldyEdt = (EditText) findViewById(R.id.fldyEdt);
		wageStr = qz.getString("s_wage");
		if (wageStr.equals(" "))
			wageStr = "";
		fldyEdt.setText(wageStr);
		// 发布日期
		dateTxt = (TextView) findViewById(R.id.dateTxt);
		dateTxt.setText(qz.getString("s_start_date"));
		// 其它要求
		requireEdt = (EditText) findViewById(R.id.requireEdt);
		requireStr = qz.getString("s_require");
		if (requireStr.equals(" "))
			requireStr = "";
		requireEdt.setText(requireStr);

		MyClick myclick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myclick);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(myclick);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(myclick);
		// 有效期限
		daysSpi = (Button) findViewById(R.id.daysSpi);
		daysSpi.setText(qz.getString("s_days"));
		daysSpi.setOnClickListener(myclick);

		initArea();
	}

	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				Zpt_ManageEditActivity.this.finish();
				break;
			case R.id.submitBtn:
				wage = fldyEdt.getText().toString().trim();// 福利待遇
				require = requireEdt.getText().toString().trim();// 具体要求
				gwsm = gwsmEdt.getText().toString();
				zpnx = zpnxEdt.getText().toString().equals("")?"0":zpnxEdt.getText().toString();	
				zpvx = zpvxEdt.getText().toString().equals("")?"0":zpvxEdt.getText().toString();
				jz = jzEdt.getText().toString().equals("")?"0":jzEdt.getText().toString();
				ygz = ygzEdt.getText().toString();
				daysFlag = daysSpi.getText().toString();
				daysFlag = daysFlag.replaceAll("-", "");
				int data = Integer.parseInt(daysFlag);
				
				if(gwsm.equals("")){
					dialogUtil.showToast("工作说明不能为空");
				}else if(gwsm.length() > 50){
					dialogUtil.showToast("工作说明不能大于50个字！");
				}else if(zpnx.equals("0") && zpvx.equals("0")
						 && jz.equals("0")){
					dialogUtil.showToast("招聘男性,招聘女性,兼招三者必须填 写一个！");
				}else if(zpnx.equals("") && zpvx.equals("")
						 && jz.equals("")){
					dialogUtil.showToast("招聘男性,招聘女性,兼招三者不能都等于0！");
				}else if(Integer.parseInt(zpnx.equals("")?"0":zpnx) > 1000){
						dialogUtil.showToast("招聘男性人数不能大于1000！");
				}else if(Integer.parseInt(zpvx.equals("")?"0":zpvx) > 1000){
						dialogUtil.showToast("招聘女性人数不能大于1000！");
				}else if(Integer.parseInt(jz.equals("")?"0":jz) > 1000){
						dialogUtil.showToast("兼职数不能大于1000！");
				}else if(ygz.equals("")){
					dialogUtil.showToast("月工资不能为空,请重新输入！");
				}else if(data < getFormatedTime()){
					dialogUtil.showToast("有效终止日期必须晚于今天！");
				}else if(workArea.equals("")){
					dialogUtil.showToast(getString(R.string.spin_prompt)+"!");
				}else{
					showProg();
					new Thread(uploadRunnable).start();
				}
			
				break;
			case R.id.resetBtn:
				fldyEdt.setText(qz.getString("s_wage"));
				requireEdt.setText(qz.getString("s_require"));
				zpnxEdt.setText(zpnx);
				zpvxEdt.setText(zpvx);
				jzEdt.setText(jz);
				ygzEdt.setText(ygz);
				gwsmEdt.setText(gwsm);
				daysSpi.setText(days);
				break;
			case R.id.daysSpi:
				commonUtil.getTime(daysSpi);
				break;
			default:
				break;
			}
		}
	}
	
	private static int getFormatedTime() {
		   SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		   int data = Integer.parseInt(format.format(new Date()).toString());
	       return data;
	}


	private void showProg() {
		progressDialog = ProgressDialog.show(this, "请稍等...", "正在上传数据...", true);
	}

	private Runnable uploadRunnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				/** 上传的参数 */
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("acb210", qz.getString("id")));
				params.add(new BasicNameValuePair("acc214", wage));// 福利待遇
				params.add(new BasicNameValuePair("aae013", require));// 具体要求
				params.add(new BasicNameValuePair("acb216", gwsm));//
				params.add(new BasicNameValuePair("acb211", zpnx.equals("")?"0":zpnx)); // 招聘男性
				params.add(new BasicNameValuePair("acb212", zpvx.equals("")?"0":zpvx)); // 招聘女性
				params.add(new BasicNameValuePair("acb213", jz.equals("")?"0":jz)); // 兼招
				params.add(new BasicNameValuePair("acc034", ygz.equals("")?"0":ygz)); // 月工资
				params.add(new BasicNameValuePair("aae031", daysFlag));// 期限
				params.add(new BasicNameValuePair("acb202", workArea));// 用工区域
				returnJson = recruitmentDAL.doAdd(params,
						"android/corecruitment/modifyRecruitInfo");// 方法接口
				Log.d(TAG, "招聘信息修改: 参数： "+ params);
				Message msg = new Message();
				msg.what = 0;
				uploadHandler.sendMessage(msg);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	Handler uploadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				doAddRegister();
			}
			progressDialog.dismiss();
		}
	};

	private void doAddRegister() {

		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap = DataConvert.toMap(returnJson);
		Boolean isSuccess = Boolean.parseBoolean(returnMap.get("success"));
		String messageStr = returnMap.get("msg").toString();
		if (isSuccess == true) {
			showMsg("提示", messageStr,isSuccess);
		} else {
			showMsg("提示", messageStr,isSuccess);
		}
	}
	

	private void showMsg(String title, String msg,final boolean success) {
		final Dialog builder = new Dialog(Zpt_ManageEditActivity.this,
				R.style.dialog);
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
				if(success){
					Intent intent = new Intent(Zpt_ManageEditActivity.this,Zpt_ManageListActivity.class);
					intent.putExtra("isEdit", true);
					startActivity(intent);
					
					Zpt_ManageEditActivity.this.finish();
					
				}
				
			}
		});
		builder.show();
	}

	private String workArea = "";
	
	/**
	 * 工作地区
	 */
	private void initArea(){
		areas=recruitmentDAL.getAreas(true,"--"+getString(R.string.spin_prompt)+"--");
		SimpleAdapter adapter=new SimpleAdapter(this, areas,  android.R.layout.simple_spinner_item, new String[]{"name"}, new int[]{android.R.id.text1});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		work_area.setAdapter(adapter);
		work_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				workArea = areas.get(arg2).get("id");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}
		});
		
		for (int i = 0; i < areas.size(); i++) {
			Map<String, String> map = areas.get(i);
			if(map.get("name").equals(ygqy)){
				work_area.setSelection(i);
				break;
			}
		}
		
	}
}
