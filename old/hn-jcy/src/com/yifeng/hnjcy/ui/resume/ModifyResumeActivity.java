package com.yifeng.hnjcy.ui.resume;

import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * 
* @Description: 简历编辑
* @author ZhuZhiChen   
* @date 2014-11-13
 */
public class ModifyResumeActivity extends BaseActivity implements OnClickListener {
	private Spinner sp_hyzk, sp_xl, sp_gzxz;
	private EditText edt_full_name, edt_num,edt_jg, edt_lldh, edt_qzgw;
	private Button back, submitBtn ;
	private HashMap<String, String> hashMap;
	ResumeDAL dal;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_modify);
		dal = new ResumeDAL(this, new Handler());
		user = new UserSession(this).getUser();
		Bundle bundle = getIntent().getExtras();  
		hashMap = (HashMap<String, String>) bundle.getSerializable("list");
		initViews();
		initSpinner();
		loadData();
	}
	
	private void loadData(){
		id = getValue(hashMap,"id");
		user_name = getValue(hashMap,"yhm");
		edt_full_name.setText(getValue(hashMap,"xm"));
		edt_num.setText(getValue(hashMap,"sfzh"));
		edt_jg.setText(getValue(hashMap,"jg"));
		edt_lldh.setText(getValue(hashMap,"lxdh"));
		edt_qzgw.setText(getValue(hashMap,"qzgw"));
		for (int i = 0; i < xlItems.length; i++) {
			if(xlItems[i].equals(getValue(hashMap,"xl"))){
				sp_xl.setSelection(i);
			}
		}
		for (int i = 0; i < hyzkItems.length; i++) {
			if(hyzkItems[i].equals(getValue(hashMap,"hyzk"))){
				sp_hyzk.setSelection(i);
			}
		}
		for (int i = 0; i < gzxzItems.length; i++) {
			if(gzxzItems[i].equals(getValue(hashMap,"gzxz"))){
				sp_gzxz.setSelection(i);
			}
		}
	}
	
	private String getValue(Map<String, String> map,String key){
		return map.get(key) != null ? map.get(key): "";
	}
	
	private void initViews() {
		sp_hyzk = (Spinner) findViewById(R.id.sp_hyzk);
		sp_xl = (Spinner) findViewById(R.id.sp_xl);
		sp_gzxz = (Spinner) findViewById(R.id.sp_gzxz);
		edt_full_name = (EditText) findViewById(R.id.edt_full_name);
		edt_num = (EditText) findViewById(R.id.edt_num);
		edt_jg = (EditText) findViewById(R.id.edt_jg);
		edt_lldh = (EditText) findViewById(R.id.edt_lldh);
		edt_qzgw = (EditText) findViewById(R.id.edt_qzgw);
		
		submitBtn= (Button)findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		back= (Button)findViewById(R.id.back);
		back.setOnClickListener(this);
	}
	
	private String user_name,full_name,num,jg,lxdh,qzgw,hyzk,xl,gzxz,id;
	private ProgressDialog progressDialog;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	private String resultJson;
	
	private void postData(){
		full_name =	edt_full_name.getText().toString().trim();
		num =	edt_num.getText().toString().trim();
		jg = edt_jg.getText().toString().trim();
		lxdh =	edt_lldh.getText().toString().trim();
		qzgw =	edt_qzgw.getText().toString().trim();
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
		params.add(new BasicNameValuePair("id", id));
		
		progressDialog=commonUtil.showProgressDialog("正在提交信息,请稍候...");
        new Thread(runnable).start();
	}
	
	Runnable  runnable=new Runnable() {
		@Override
		public void run() {
			try{
				Thread.sleep(200);
				resultJson = dal.doModifyResume(params);
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
							ModifyResumeActivity.this.finish();
						}
					}else{
						commonUtil.shortToast("简历修改失败！");
					}
				 }catch (Exception e) {
					 commonUtil.shortToast("简历修改失败！");
				}
			 }
			 if(msg.what == -1){
				 commonUtil.shortToast("简历修改失败！");
			 }
		 };
	};

	private void initSpinner() {
		initGzxz();
		initHyzk();
		initXl();
	}
	
	private String[] hyzkItems,gzxzItems,xlItems;

	/**
	 * 婚姻状况
	 */
	private void initHyzk() {
		hyzkItems = getResources().getStringArray(R.array.resume_hyzks);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hyzkItems);
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
		xlItems = getResources().getStringArray(R.array.resume_xls);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, xlItems);
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
		gzxzItems = getResources().getStringArray(R.array.resume_gzxzs);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, gzxzItems);
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
		default:
			break;
		}

	}
}