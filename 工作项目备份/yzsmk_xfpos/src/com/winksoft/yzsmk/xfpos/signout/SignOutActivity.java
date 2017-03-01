package com.winksoft.yzsmk.xfpos.signout;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.desfire.Consumption;
import com.winksoft.yzsmk.ftp.BusinessMode;
import com.winksoft.yzsmk.link.PosLinker;
import com.winksoft.yzsmk.link.TransInforDesfire;
import com.winksoft.yzsmk.solab.iso8583.IsoMessage;
import com.winksoft.yzsmk.solab.iso8583.MessageFactory;
import com.winksoft.yzsmk.solab.iso8583.parse.ConfigParser;
import com.winksoft.yzsmk.solab.iso8583.yzsmk.PosFuntion;
import com.winksoft.yzsmk.solab.iso8583.yzsmk.PosTradeType;
import com.winksoft.yzsmk.utils.ListConversion;
import com.winksoft.yzsmk.utils.SettingDataUtil;
import com.winksoft.yzsmk.xfpos.CallBackListener;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.YFApp;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;
import com.winksoft.yzsmk.xfpos.queryvalue.QueryValues;
import com.yifengcom.yfpos.model.PrintType;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SignOutActivity extends BaseActivity implements OnClickListener {
	private SqliteUtil sqliteUtil;
	private TextView tvUserName, tvPayDate, tvDayPayDate, tvPSAM1, tvPSAM2, tvPayMoney, tvPayCount;
	private BusinessMode bm;
	private QueryValues qv;
	private List<Map<String, Object>> twoList2oneList;
	private Button btnPrint, btDayPay;
	private boolean ftpSetParmFlage;
	private TransInforDesfire transInforDesfire;
	private static MessageFactory mfact = null;
	private PosFuntion fun;
	private PosLinker link;
	private static IsoMessage m = null;
	private Map<String,String> settingMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signout);
		setTitle("签退");
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvPayDate = (TextView) findViewById(R.id.tvPayDate);
		tvDayPayDate = (TextView) findViewById(R.id.tvDayPayDate);
		tvPSAM1 = (TextView) findViewById(R.id.tvPSAM1);
		tvPSAM2 = (TextView) findViewById(R.id.tvPSAM2);
		tvPayMoney = (TextView) findViewById(R.id.tvPayMoney);
		tvPayCount = (TextView) findViewById(R.id.tvPayCount);
		btnPrint = (Button) super.findViewById(R.id.dayPayPrint);
		btnPrint.setOnClickListener(this);
		btDayPay = (Button) super.findViewById(R.id.dayPay);
		btDayPay.setOnClickListener(this);
		/**************************************************************************************/
		transInforDesfire = new TransInforDesfire();
		InputStream is = SignOutActivity.this.getResources().openRawResource(R.raw.j8583);
		try {
			mfact = ConfigParser.createFromStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Create a new message
		System.out.println("NEW MESSAGE");

		///////////////
		fun = new PosFuntion(SignOutActivity.this);
		link = new PosLinker(SignOutActivity.this);

		/**************************************************************************************/
		twoList2oneList = new ArrayList<Map<String, Object>>();

		qv = new QueryValues(1);

		sqliteUtil = SqliteUtil.getInstance(this);
		init();
		// if(!ftpStart()){
		// showToast("设置有误");
		// }
		settingMap = new HashMap<String,String>();
//		settingMap = SettingDataUtil.getPaySettingInfo("/j8583SettingInfo.txt");
//		settingMap.remove("terminalNum");
//		settingMap.remove("username");
//		settingMap.remove("password");
		settingMap.put("ip", qv.getDesfireIP());
		settingMap.put("port", qv.getDesfirePort());
		
		
		ftpSetParmFlage = ftpStart(); // 用于上传FTP的返回结果
	}

	private void setBtn(boolean flag, Button bt) {
		bt.setVisibility(flag ? View.VISIBLE : View.GONE);
	}

	public void init() {
		// 1. insertdate:交易时间
		// 2. getCurrentTime():日结时间
		// 3. body_117:PSAM卡号
		// 4. body_79:交易金额
		// 5. queryList.size:交易笔数

		List<Map<String, Object>> xfinfosList = sqliteUtil.doQuery(
				"select * from xfinfos where state = '0' order by insertdate ",
				new String[] { "jobNum", "insertdate", "body_117", "body_79" });

		List<Map<String, Object>> disfireList = sqliteUtil.doQuery(
				"select * from disfire where state = '0' order by insertdate ",
				new String[] { "jobNum", "insertdate", "care_class", "consume_money" });

		xfinfosList = ListConversion.formatDate(xfinfosList, "xfinfos", 1);

		disfireList = ListConversion.formatDate(disfireList, "disfire", 1);

		if (xfinfosList.size() > 0 || disfireList.size() > 0) {
			twoList2oneList = ListConversion.twoList2oneList(xfinfosList, disfireList,
					xfinfosList.size() + disfireList.size());
			int money = 0;
			for (Map<String, Object> map : twoList2oneList) {
				money += Integer.parseInt(map.get("money").toString(), 16);
			}
			String moneyFormat = "";
			try {
				moneyFormat = String.format("%,.2f 元", money / 100.00);
			} catch (Exception e) {
			}

			String PSAM1 = "";
			String PSAM2 = "";
			for (Map<String, Object> map : twoList2oneList) {
				if (!"".equals(map.get("PSAM1").toString()) && !PSAM1.equals(map.get("PSAM1").toString())) {
					PSAM1 = map.get("PSAM1").toString();
				}
				if (!"".equals(map.get("PSAM2").toString()) && !PSAM2.equals(map.get("PSAM2").toString())) {
					PSAM2 = map.get("PSAM2").toString();
				}
				if (!"".equals(PSAM1) && !"".equals(PSAM2)) {
					break;
				}
			}

			tvPayDate.setText((String) (twoList2oneList.get(0).get("time")));
			tvDayPayDate.setText(getCurrentTime());
			tvPayMoney.setText(moneyFormat); // 交易总和
			tvPayCount.setText(twoList2oneList.size() + ""); // 交易笔数
			tvUserName.setText((String) (twoList2oneList.get(0).get("jobNum")));
			tvPSAM1.setText(PSAM1);
			tvPSAM2.setText(PSAM2);
		}
	}

	private boolean printReceipt() {
		boolean flage = false;
		if (twoList2oneList.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", "消费日结报表");
			map.put("body_1", tvPayDate.getText().toString());
			map.put("body_2", tvDayPayDate.getText().toString());
			map.put("body_3", tvUserName.getText().toString());
			map.put("body_4", tvPSAM1.getText().toString());
			map.put("body_5", tvPSAM2.getText().toString());
			map.put("body_6", tvPayMoney.getText().toString());
			map.put("body_7", tvPayCount.getText().toString());
			try {
				YFApp.getApp().iService.onPrint(getPrintBody(map), mCallBack);
				flage = true;
			} catch (RemoteException e) {
				e.printStackTrace();
				showToast("打印异常");
				setBtn(true, btnPrint);
			} catch (Exception e) {
				e.printStackTrace();
				showToast("打印异常");
				setBtn(true, btnPrint);
			}
		}
		return flage;
	}

	/**
	 * 签退
	 * 
	 * @param jobNumber
	 *            工号
	 */
	private void signOut(String jobNumber) {
		int update = sqliteUtil.doUpdate(
				"update signin set state = ?,signout_date = ?,transaction_date= ?,daypay_date=?,psam1=?,psam2=?,money=?,pay_count=? where state = ? and sign_no = ?",
				new String[] { "1", getCurrentTime(), tvPayDate.getText().toString(), tvDayPayDate.getText().toString(),
						tvPSAM1.getText().toString(), tvPSAM2.getText().toString(), tvPayMoney.getText().toString(),
						tvPayCount.getText().toString(), "0", jobNumber });
		if (update == 1) {
			setBtn(false, btDayPay);
			// this.finish();
		}
	}

	protected CallBackListener mCallBack = new CallBackListener() {
		@Override
		public void onError(final int errorCode, final String errorMessage) throws RemoteException {

			runOnUiThread(new Runnable() {
				public void run() {
					new AlertDialog.Builder(SignOutActivity.this).setTitle("提示")
							.setMessage("操作失败，返回码:" + errorCode + " 信息:" + errorMessage).setPositiveButton("确定", null)
							.show();
				}
			});
		}

		@Override
		public void onTimeout() throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					setBtn(true, btnPrint);
					new AlertDialog.Builder(SignOutActivity.this).setTitle("错误").setMessage("打印超时")
							.setPositiveButton("确定", null).show();
				}
			});
		}

		@Override
		public void onResultSuccess(final int ntype) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					if (ntype != 0) {
						setBtn(true, btnPrint);
					}
					showToast(PrintType.convert(ntype).getMesssage());
				}
			});
		}

		@Override
		public void onTradeCancel() throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					setBtn(true, btnPrint);
					new AlertDialog.Builder(SignOutActivity.this).setTitle("错误").setMessage("取消打印")
							.setPositiveButton("确定", null).show();
				}
			});
		}
	};

	private boolean ftpStart() {
		// String hostName = "192.168.66.123";
		// int serverPort = 2121;
		// String userName = "test";
		// String password = "123456";
		String hostName = qv.getIp();
		int serverPort = 0;
		try {
			serverPort = Integer.parseInt(qv.getPort());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		String userName = qv.getUsername();
		String password = qv.getPassword();
		String remotePath = qv.getRemotePath();
		
		boolean mode = true;
		String runtime = "09:00";
//		String remotePath = "/pbp/consume_import"; // 消费业务为:/XF/,充值业务为:/CZ/
		Context cxt = this.getApplicationContext();
		if (bm == null) {
			bm = BusinessMode.getInstance();
		}
		SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm");
		String strDt = tempDate.format(new java.util.Date()).toString();
		boolean setParams = bm.setParams(hostName, serverPort, userName, password, strDt, remotePath, msghandler, cxt);
		return setParams;
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
				// editText1.setText(strMsg);
				// editText1.invalidate();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void desfireSign() {
		transInforDesfire.setTradeType(PosTradeType.TYPE_SIGN_IN_U);
		m = mfact.newMessage(TransInforDesfire.tradeType);
		boolean fs = fun.setPackeFunction(m, mfact, PosTradeType.TYPE_SIGN_IN_U);
		link.linker(settingMap);
	}

	private void desfireLiquTrade() {
		transInforDesfire.setTradeType(PosTradeType.TYPE_OFF_LINEREPORT_U);
		m = mfact.newMessage(TransInforDesfire.tradeType);
		fun.setPackeFunction(m, mfact, PosTradeType.TYPE_OFF_LINEREPORT_U);
		link.linker(settingMap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dayPayPrint:
			setBtn(false, btnPrint);
			printReceipt();
			break;
		case R.id.dayPay:
			if (twoList2oneList.size() > 0) { // 有没有消费记录
				/*判断FTP上传是否成功*/
				if (ftpSetParmFlage && bm != null) { // ftp上传配置是否有错
					if (!bm.bRunning) {
						bm.start();
						// desfire发送签到信息给中心
//						desfireSign();
//						// desfire上传
//						try {
//							desfireLiquTrade();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						signOut(qv.getJobNumber());
//						printReceipt();
					}
				} else {
					showToast("参数设置有误");
				}
			} else {
				showToast("请先消费");
			}
			break;
		default:
			break;
		}
	}
}
