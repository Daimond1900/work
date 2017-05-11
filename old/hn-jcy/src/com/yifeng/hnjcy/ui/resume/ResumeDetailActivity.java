package com.yifeng.hnjcy.ui.resume;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.yifeng.hnjcy.data.ResumeDAL;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.DataConvert;
import com.yifeng.hnjcy.util.UserSession;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 *  
* @Description: 简历详情
* @author ZhuZhiChen   
* @date 2014-11-13
 */
public class ResumeDetailActivity extends BaseActivity implements OnClickListener{
	private TextView txt_full_name,txt_xb,txt_num,txt_jg,txt_hyzk,txt_xl,txt_lldh,txt_qzgw,txt_gzxz,txt_sfgk;
	private Button back,btn_add,btn_update;
	private ProgressDialog progressDialog;
	private Map<String, String> resultsMap;
	UserSession userSession;
	ResumeDAL dal;
	private String id;
	private String resultJson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_detail);
		userSession = new UserSession(this);
		dal = new ResumeDAL(this, new Handler());
		id = getIntent().getStringExtra("id");
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showProg("正在加载数据...");// 加载远程数据
		new Thread(loadRunnanle).start();
	}
	
	private void showProg(String content) {
		progressDialog = ProgressDialog.show(this, "系统提示", content, true);
		progressDialog.setIndeterminate(true);//设置进度条是否为不明确
	    progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消 
	}
	
	private void initView() {
		txt_full_name = (TextView) findViewById(R.id.txt_full_name); //姓名
		txt_xb = (TextView) findViewById(R.id.txt_xb); //性别
		txt_num = (TextView) findViewById(R.id.txt_num); //身份证号码
		txt_jg = (TextView) findViewById(R.id.txt_jg); //籍贯
		txt_hyzk = (TextView) findViewById(R.id.txt_hyzk); //婚姻状况 
		txt_xl = (TextView) findViewById(R.id.txt_xl); //学历
		txt_lldh = (TextView) findViewById(R.id.txt_lldh); //联系电话
		txt_qzgw = (TextView) findViewById(R.id.txt_qzgw); //求职岗位
		txt_gzxz = (TextView) findViewById(R.id.txt_gzxz); //工作性质
		txt_sfgk = (TextView) findViewById(R.id.txt_sfgk); //是否公开
		back= (Button)findViewById(R.id.back);
		back.setOnClickListener(this);
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.setOnClickListener(this);
	}
	
	Runnable loadRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				resultsMap = dal.getResumeDetail(id,"");
				loadHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}

		}
	};
	
	Runnable updateRunnanle = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				resultJson = dal.doModifyResumeStatus(id, flag == 0 ? "N": "Y");
				loadHandler.sendEmptyMessage(5);
			} catch (Exception e) {
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}

		}
	};
	
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null && progressDialog.isShowing()){
				progressDialog.dismiss();
				progressDialog = null;
			}
			if (msg.what == 1) {
				setPageData();
			}
			if(msg.what==5){
				 try{
					Map<String, String> map = DataConvert.toMap(resultJson);
					if (map.get("success") != null){
						commonUtil.showToast(map.get("msg"));
						if(map.get("success").equals("true")){
							showProg("正在加载数据...");// 加载远程数据
							new Thread(loadRunnanle).start();
						}
					}else{
						commonUtil.shortToast("状态更新失败！");
					}
				 }catch (Exception e) {
					 commonUtil.shortToast("状态更新失败！");
				}
			}
		};
	};

	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private int flag = -1; //简历公开状态：0公开，1隐藏
	/***
	 * 加载数据
	 */
	private void setPageData() {
		if(resultsMap != null){
			if (resultsMap.get("success") != null && resultsMap.get("success").equals("true")){
				Map<String, String> map = DataConvert.toMap(resultsMap.get("resume"));
				txt_full_name.setText(getValue(map, "xm")); //姓名
				txt_xb.setText(getValue(map, "xb")); //性别
				txt_num.setText(getValue(map, "sfzh")); //身份证号码
				txt_jg.setText(getValue(map, "jg")); //籍贯
				txt_hyzk.setText(getValue(map, "hyzk")); //婚姻状况 
				txt_xl.setText(getValue(map, "xl")); //学历
				txt_lldh.setText(getValue(map, "lxdh")); //联系电话
				txt_qzgw.setText(getValue(map, "qzgw")); //求职岗位
				txt_gzxz.setText(getValue(map, "gzxz")); //工作性质
				
				if(getValue(map, "is_open").equals("Y")){
					txt_sfgk.setText("公开"); 
					btn_update.setText("设为隐藏");
					flag = 0;
				}else{
					txt_sfgk.setText("未公开"); 
					btn_update.setText("设为公开");
					flag = 1;
				}
				
				hashMap.put("id", id);
				for(Entry<String, String> entry: map.entrySet()) {
					hashMap.put(entry.getKey(), entry.getValue());
				}
			} else {
				commonUtil.shortToast(resultsMap.get("msg"));
			}
		}
	}
	
	private String getValue(Map<String, String> map,String key){
		return map.get(key) != null ? map.get(key): "";
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.btn_update:
			if(flag != -1){
				showProg("正在修改状态...");// 加载远程数据
				new Thread(updateRunnanle).start();
			}
			break;
		case R.id.btn_add:
			if(hashMap.size()>0){
				Intent intent = new Intent(ResumeDetailActivity.this,ModifyResumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", hashMap);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
		default:
			break;
		}

	}
	
}
