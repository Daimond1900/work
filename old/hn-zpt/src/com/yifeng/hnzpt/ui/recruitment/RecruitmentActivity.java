package com.yifeng.hnzpt.ui.recruitment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.data.RecruitmentDAL;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.ui.MainMenuActivity;
import com.yifeng.hnzpt.util.DataConvert;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * ClassName:RecruitmentActivity
 * Description：招聘登记
 * @author Administrator
 * Date：2012-8-28
 */
public class RecruitmentActivity extends BaseActivity {
	private EditText postEdt, numberEdt, wageEdt, requireEdt,zpnxEdt,zpvxEdt,jzEdt,ygzEdt,gzsmEdt;
	private Button backBtn, chooseBtn, submitBtn, resetBtn;
	private Button daysSpi;
	private String[] spinnerData = { "一个月", "半个月", "两个月" };// flag:0,1,2
	private String days = "一个月";
	private String job = "", wage = "", require = "",zpnx ="",zpvx = "",jz = "",ygz="",gzsm="",daysFlag="";
	private ProgressDialog progressDialog;
	private RecruitmentDAL recruitmentDAL;
	private Intent qz;
	private String tag = "";
	private String returnJson;
	private Spinner work_area;
	private List<Map<String,String>> areas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recruitment);

		recruitmentDAL = new RecruitmentDAL(this);

		qz = this.getIntent();
		tag = qz.getStringExtra("flag");
		
		gzsmEdt = (EditText)findViewById(R.id.gwsmnumberEdt);
		postEdt = (EditText) findViewById(R.id.postEdt);
		if (tag.equals("ChooseJobsActivity4")) {
			postEdt.setText(qz.getStringExtra("post"));
			gzsmEdt.setText(qz.getStringExtra("post"));
		} 
		wageEdt = (EditText) findViewById(R.id.wageEdt);
		requireEdt = (EditText) findViewById(R.id.requireEdt);
		zpnxEdt = (EditText)findViewById(R.id.zpnxnumberEdt);
		zpvxEdt = (EditText)findViewById(R.id.zpvxnumberEdt);
		jzEdt = (EditText)findViewById(R.id.jznumberEdt);
		ygzEdt = (EditText)findViewById(R.id.ygzEdt);
		work_area = (Spinner)findViewById(R.id.spn_area);//工作地区
		
		
		MyClick myclick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myclick);
		chooseBtn = (Button) findViewById(R.id.chooseBtn);
		chooseBtn.setOnClickListener(myclick);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(myclick);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(myclick);

		daysSpi = (Button) findViewById(R.id.daysSpi);
		daysSpi.setOnClickListener(myclick);
		daysSpi.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		initArea();
	}

	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				RecruitmentActivity.this.finish();
				break;
			case R.id.chooseBtn:
				Intent intent = new Intent(RecruitmentActivity.this, ChooseJobsActivity1.class);
				startActivity(intent);
				break;
			case R.id.submitBtn:
				job = postEdt.getText().toString().trim();
				require = requireEdt.getText().toString().trim();
				//System.out.println("----------------------->job:" + job);
				zpnx =  zpnxEdt.getText().toString().trim();
				zpvx = zpvxEdt.getText().toString().trim();
				jz = jzEdt.getText().toString().trim();
				ygz = ygzEdt.getText().toString().trim();
				gzsm = gzsmEdt.getText().toString().trim();
				daysFlag = daysSpi.getText().toString();
				daysFlag = daysFlag.replaceAll("-", "");
				int data = Integer.parseInt(daysFlag);
				daysFlag = data+"";
				if(job.equals("")){
					dialogUtil.showToast("招聘岗位不能为空！");
				}else if(gzsm.equals("")){
					dialogUtil.showToast("工作说明不能为空");
				}else if(gzsm.length() > 50){
					dialogUtil.showToast("工作说明不能大于50个字！");
				}else if (job.equals("")) {
					dialogUtil.shortToast("招聘岗位不能为空，请重新输入!");
				} else if(zpnx.equals("") && zpvx.equals("")
						 && jz.equals("")){
					dialogUtil.showToast("招聘男性,招聘女性,兼招三者必须填 写一个！");
				}else if(zpnx.equals("0") && zpvx.equals("0")
						 && jz.equals("0")){
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
					wage = wageEdt.getText().toString().trim();
					require = requireEdt.getText().toString().trim();
					showProg();
					new Thread(uploadRunnable).start();
				}
				
				break;
			case R.id.resetBtn:
				postEdt.setText("");
				wageEdt.setText("");
				requireEdt.setText("");
				zpnxEdt.setText("");
				zpvxEdt.setText("");
				jzEdt.setText("");
				ygzEdt.setText("");
				gzsmEdt.setText("");
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
				params.add(new BasicNameValuePair("acb200", user.getUserId()));// 单位编号
				params.add(new BasicNameValuePair("aca112", job));// 招聘岗位
				params.add(new BasicNameValuePair("aca111", getIntent().getStringExtra("aca111")));
				params.add(new BasicNameValuePair("acb216", gzsm));
				params.add(new BasicNameValuePair("acc214", wage==null?"":wage));// 福利待遇
				params.add(new BasicNameValuePair("aae013", require==null?"":require));// 具体要求
				params.add(new BasicNameValuePair("acb211",zpnx.equals("")?"0":zpnx));	//招聘男性
				params.add(new BasicNameValuePair("acb212",zpvx.equals("")?"0":zpvx));	//招聘女性
				params.add(new BasicNameValuePair("acb213",jz.equals("")?"0":jz));		//兼职
				params.add(new BasicNameValuePair("acc034",ygz));							//月工资
				params.add(new BasicNameValuePair("aae017", user.getDeptId())); //单位所在地区编号
				params.add(new BasicNameValuePair("acb202", workArea)); //用工区域编号
				
				params.add(new BasicNameValuePair("aae031", daysFlag));// 期限
				returnJson = recruitmentDAL.doAdd(params, "android/corecruitment/createRecruitInfo");// 方法接口
				//System.out.println("----------------------->params:" + params);
				uploadHandler.sendEmptyMessage(1);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	Handler uploadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
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
			showMsg("提示", messageStr);
		} else {
			dialogUtil.showMsg("提示", messageStr);
		}
	}

	private void showMsg(String title, String msg) {
		final Dialog builder = new Dialog(RecruitmentActivity.this, R.style.dialog);
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
				Intent intent = new Intent(RecruitmentActivity.this, MainMenuActivity.class);
				startActivity(intent);
				RecruitmentActivity.this.finish();
			}
		});
		// builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
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
	}

}
