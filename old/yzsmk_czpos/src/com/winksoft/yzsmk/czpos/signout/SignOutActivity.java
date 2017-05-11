package com.winksoft.yzsmk.czpos.signout;

import java.text.SimpleDateFormat;
import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.czpos.queryvalue.QueryValues;
import com.winksoft.yzsmk.ftp.BusinessMode;
import com.winksoft.yzsmk.solab.iso8583.IsoMessage;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignOutActivity extends BaseActivity implements OnClickListener {
	private BusinessMode bm;
	private static IsoMessage m = null;
	private Button btUpLoad;
	private QueryValues qv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signout);
		setTitle("充值上传");

		btUpLoad = (Button) findViewById(R.id.btUpLoad);
		btUpLoad.setOnClickListener(this);
		qv = new QueryValues();
		ftpStart();		// FTP上传
	}

	/**************************************************************************************/
	private void ftpStart() {
		String hostName = qv.getIp();					//IP
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(qv.getPort()); //端口
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		String userName = qv.getUsername();		//用户名
		String password = qv.getPassword();		//密码

		boolean mode = true;
		String runtime = "09:00";
		String remotePath = qv.getRemotePath(); // 远程目录
		Context cxt = this.getApplicationContext();
		if (bm == null) {
			bm = BusinessMode.getInstance();
		}
		SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm");
		String strDt = tempDate.format(new java.util.Date()).toString();
		bm.setParams(hostName, serverPort, userName, password, strDt, remotePath, msghandler, cxt);
	}

	Handler msghandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			Bundle bundle = msg.getData();
			String strMsg = "";
			if (bundle != null) {
				strMsg = bundle.getString("MSG");
			}
			switch (msg.what) {
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btUpLoad:
			if (!bm.bRunning) {
				bm.start();
			}
			break;
		default:
			break;
		}
	}
}
