package com.yifeng.hnjcy.ui.resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.yifeng.cloud.entity.User;
import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.DataConvert;
import com.yifeng.hnjcy.util.UserSession;

public class AddResumeActivity extends BaseActivity implements OnClickListener {
	private Spinner sp_hyzk, sp_xl, sp_gzxz;
	private EditText edt_user_name, edt_full_name, edt_num,
			edt_jg, edt_lldh, edt_qzgw;
	private Button back, submitBtn, resetBtn;
	ResumeDAL dal;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_resume);
		dal = new ResumeDAL(this, new Handler());
		user = new UserSession(this).getUser();
		
		initViews();
		initSpinner();
	}

	private void initViews() {
		sp_hyzk = (Spinner) findViewById(R.id.sp_hyzk);
		sp_xl = (Spinner) findViewById(R.id.sp_xl);
		sp_gzxz = (Spinner) findViewById(R.id.sp_gzxz);
		edt_user_name = (EditText) findViewById(R.id.edt_user_name);
		edt_full_name = (EditText) findViewById(R.id.edt_full_name);
		edt_num = (EditText) findViewById(R.id.edt_num);
		edt_jg = (EditText) findViewById(R.id.edt_jg);
		edt_lldh = (EditText) findViewById(R.id.edt_lldh);
		edt_qzgw = (EditText) findViewById(R.id.edt_qzgw);
		
		submitBtn= (Button)findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		resetBtn= (Button)findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(this);
		back= (Button)findViewById(R.id.back);
		back.setOnClickListener(this);
	}
	
	private String user_name,full_name,num,jg,lxdh,qzgw,hyzk,xl,gzxz;
	private ProgressDialog progressDialog;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String resultJson;
	
	private void postData(){
		user_name =  edt_user_name.getText().toString().trim();
		full_name =	edt_full_name.getText().toString().trim();
		num =	edt_num.getText().toString().trim();
		jg = edt_jg.getText().toString().trim();
		lxdh =	edt_lldh.getText().toString().trim();
		qzgw =	edt_qzgw.getText().toString().trim();
		if(user_name.equals("")){
			commonUtil.shortToast("请输入用户名!");
			return;
		}
		if(full_name.equals("")){
			commonUtil.shortToast("请输入姓名!");
			return;
		}
		if(num.equals("")){
			commonUtil.shortToast("请输入身份证号!");
			return;
		}
		if(lxdh.equals("")){
			commonUtil.shortToast("请输入联系电话!");
			return;
		}
		if(qzgw.equals("")){
			commonUtil.shortToast("请输入求职岗位!");
			return;
		}
		params.clear();
		params.add(new BasicNameValuePair("yhm", user_name));
		params.add(new BasicNameValuePair("xm", full_name ));
		params.add(new BasicNameValuePair("lxdh", lxdh));
		params.add(new BasicNameValuePair("jg", jg));
		params.add(new BasicNameValuePair("hyzk", hyzk));
		params.add(new BasicNameValuePair("sfzh", num));
		params.add(new BasicNameValuePair("xl", xl));
		params.add(new BasicNameValuePair("qzgw", qzgw));
		params.add(new BasicNameValuePair("gzxz", gzxz));
		params.add(new BasicNameValuePair("create_by", user.getUserId()));
		params.add(new BasicNameValuePair("department_id", user.getDepartmentid()));
		
		progressDialog=commonUtil.showProgressDialog("正在提交信息,请稍候...");
        new Thread(runnable).start();
	}
	
	Runnable  runnable=new Runnable() {
		@Override
		public void run() {
			try{
				Thread.sleep(100);
				resultJson = dal.doSendResume(params);
				handler.sendEmptyMessage(2);
			}catch (Exception e) {
				resultJson = "";
				handler.sendEmptyMessage(-1);
			}
		}
	};
	
	Handler handler=new Handler(){
		 public void handleMessage(Message msg) {
			 super.handleMessage(msg);
			 if(progressDialog!=null){
				 progressDialog.dismiss();
			 }
			 if(msg.what==2){
				 try{
					Map<String, String> map = DataConvert.toMap(resultJson);
					if (map.get("success") != null){
						commonUtil.showToast(map.get("msg"));
						if(map.get("success").equals("true")){ 
							resetValue();
						}
					}else{
						commonUtil.shortToast("简历新增失败！");
					}
				 }catch (Exception e) {
					 commonUtil.shortToast("简历新增失败！");
				}
			 }
			 if(msg.what == -1){
				 commonUtil.shortToast("简历新增失败！");
			 }
		 };
	};

	private void initSpinner() {
		initGzxz();
		initHyzk();
		initXl();
	}

	/**
	 * 婚姻状况
	 */
	private void initHyzk() {
		String[] mItems = getResources().getStringArray(R.array.resume_hyzks);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_hyzk.setPrompt("请选择婚姻状况");
		sp_hyzk.setAdapter(adapter);
		sp_hyzk.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				hyzk = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * 学历
	 */
	private void initXl() {
		String[] mItems = getResources().getStringArray(R.array.resume_xls);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_xl.setPrompt("请选择学历");
		sp_xl.setAdapter(adapter);
		sp_xl.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				xl = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/**
	 * 工作性质
	 */
	private void initGzxz() {
		String[] mItems = getResources().getStringArray(R.array.resume_gzxzs);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_gzxz.setPrompt("请选择工作性质");
		sp_gzxz.setAdapter(adapter);
		sp_gzxz.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				gzxz = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.submitBtn:
			postData();
			break;
		case R.id.resetBtn:
			resetValue();
			break;
		default:
			break;
		}
	}
	
	private void resetValue(){
		edt_user_name.setText("");
		edt_full_name.setText("");
		edt_num.setText("");
		edt_jg.setText("");
		edt_lldh.setText("");
		edt_qzgw.setText("");
	}
}
