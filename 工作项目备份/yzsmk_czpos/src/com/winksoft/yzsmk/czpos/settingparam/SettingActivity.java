package com.winksoft.yzsmk.czpos.settingparam;

import java.util.HashMap;
import java.util.Map;

import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.utils.SettingDataUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private EditText etTerminalNum, etIP, etPort, etUsername, etPassword, etIpNum, etRemotePath, etAccpetCusNo;
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
		etAccpetCusNo = (EditText) this.findViewById(R.id.etAccpetCusNo); // 授权方代码
		etIpNum = (EditText) this.findViewById(R.id.etIpNum);

		btSettingEnter = (Button) this.findViewById(R.id.btSettingEnter);
		btSettingEnter.setOnClickListener(this);

		Intent intent = getIntent();
		etTerminalNum.setText(intent.getStringExtra("terminalNum"));
		etIP.setText(intent.getStringExtra("ip"));
		etPort.setText(intent.getStringExtra("port"));
		etUsername.setText(intent.getStringExtra("username"));
		etPassword.setText(intent.getStringExtra("password"));
		etIpNum.setText(intent.getStringExtra("ipNum"));
		etRemotePath.setText(intent.getStringExtra("remotePath"));
		etAccpetCusNo.setText(intent.getStringExtra("accpetCusNo")); // 授权方代码
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSettingEnter:
			String terminalNum = etTerminalNum.getText().toString().trim();
			String ip = etIP.getText().toString().trim();
			String port = etPort.getText().toString().trim();
			String username = etUsername.getText().toString().trim();
			String pwd = etPassword.getText().toString().trim();
			String ipNum = etIpNum.getText().toString().trim();
			String remotePath = etRemotePath.getText().toString().trim();
			String accpetCusNo = etAccpetCusNo.getText().toString().trim();// 授权方代码

			if (TextUtils.isEmpty(terminalNum)) {
				showToast("终端号不能为空");
				return;
			}
			if (TextUtils.isEmpty(accpetCusNo)) {
				showToast("授权方代码不能为空");
				return;
			}
			if (TextUtils.isEmpty(remotePath)) {
				showToast("远程目录不能为空");
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
			}
			if (TextUtils.isEmpty(ipNum)) {
				showToast("网址不能为空");
				return;
			}
			
			Map<String, String> mapValue = new HashMap<String, String>();
			mapValue.put("terminalNum", terminalNum);	// 终端号
			mapValue.put("ip", ip); 					// IP
			mapValue.put("port", port); 				// 端口号
			mapValue.put("username", username); 		// 用户名
			mapValue.put("pwd", pwd); 					// 密码
			mapValue.put("ipNum", ipNum); 				// 授权地址
			mapValue.put("remotePath", remotePath); 	// 远程目录
			mapValue.put("accpetCusNo", accpetCusNo); 	// 授权方代码
			
			SettingDataUtil.saveSetting("/czSettingInfo.txt", mapValue);
			Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
