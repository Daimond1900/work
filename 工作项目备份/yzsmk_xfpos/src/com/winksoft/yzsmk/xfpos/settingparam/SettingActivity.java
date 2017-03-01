package com.winksoft.yzsmk.xfpos.settingparam;

import java.util.HashMap;
import java.util.Map;

import com.winksoft.yzsmk.utils.SettingDataUtil;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.R.id;
import com.winksoft.yzsmk.xfpos.R.layout;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private EditText etTerminalNum,etIP, etPort, etUsername, etPassword,etRemotePath,etdesfireIP , etDesfirePort;
	private Button btSettingEnter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle("设置");
		
		etTerminalNum = (EditText) this.findViewById(R.id.etTerminalNum);
		etIP = (EditText) this.findViewById(R.id.etIP);
		etPort = (EditText) this.findViewById(R.id.etPort);
		etUsername = (EditText) this.findViewById(R.id.etUsername);
		etPassword = (EditText) this.findViewById(R.id.etPassword);
		etRemotePath = (EditText) this.findViewById(R.id.etRemotePath);
		
		etdesfireIP = (EditText) this.findViewById(R.id.etdesfireIP);
		etDesfirePort = (EditText) this.findViewById(R.id.etDesfirePort);
		btSettingEnter = (Button) this.findViewById(R.id.btSettingEnter);
		btSettingEnter.setOnClickListener(this);

		Intent intent = getIntent();
		etTerminalNum.setText(intent.getStringExtra("terminalNum"));
		etIP.setText(intent.getStringExtra("ip"));
		etPort.setText(intent.getStringExtra("port"));
		etUsername.setText(intent.getStringExtra("username"));
		etPassword.setText(intent.getStringExtra("password"));
		etRemotePath.setText(intent.getStringExtra("remotePath"));
		
		etdesfireIP.setText(intent.getStringExtra("desfireIp"));
		etDesfirePort.setText(intent.getStringExtra("desfirePort"));
		
	}
	
//	public void test(View v){
//		SettingValues sv = new SettingValues();
//		showToast(sv.getIp());
//		showToast(sv.getPassword());
//		showToast(sv.getPort());
//		showToast(sv.getTerminalNum());
//		showToast(sv.getUsername());
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSettingEnter:
			String terminalNum = etTerminalNum.getText().toString().trim();
			String ip = etIP.getText().toString().trim();
			String port = etPort.getText().toString().trim();
			String username = etUsername.getText().toString().trim();
			String pwd = etPassword.getText().toString().trim();
			String remotePath = etRemotePath.getText().toString().trim();
			String desfireIp = etdesfireIP.getText().toString().trim();
			String desfirePort = etDesfirePort.getText().toString().trim();
			
			if (TextUtils.isEmpty(terminalNum)) {
				showToast("终端号不能为空");
				return;
			}
			if (TextUtils.isEmpty(ip)) {
				showToast("IP不能为空");
				return;
			}
			if (TextUtils.isEmpty(port)) {
				showToast("端口号不能为空");
				return;
			}
			if (TextUtils.isEmpty(username)) {
				showToast("用户名不能为空");
				return;
			}
			if (TextUtils.isEmpty(pwd)) {
				showToast("密码不能为空");
				return;
			}if (TextUtils.isEmpty(remotePath)) {
				showToast("目录地址不能为空");
				return;
			}if (TextUtils.isEmpty(desfireIp)) {
				showToast("desfireIp不能为空");
				return;
			}
			if (TextUtils.isEmpty(desfirePort)) {
				showToast("desfirePort不能为空");
				return;
			}
			Map<String, String> map = new HashMap<String,String>();
			map.put("terminalNum", terminalNum);
			map.put("ip", ip);
			map.put("port", port);
			map.put("username", username);
			map.put("pwd", pwd);
			map.put("remotePath", remotePath);
			map.put("desfireIp", desfireIp);
			map.put("desfirePort", desfirePort);
//			SettingDataUtil.saveSetting("/ftpSettingInfo.txt",terminalNum,ip, port, username, pwd,remotePath,desfireIp,desfirePort);
			SettingDataUtil.saveSetting("/ftpSettingInfo.txt",map);
//			SettingDataUtil.saveSetting("/j8583SettingInfo.txt","12",ip, port, "12", "12");
			Toast.makeText(this, "设置成功", 0).show();
			break;
		default:
			break;
		}
	}
}
