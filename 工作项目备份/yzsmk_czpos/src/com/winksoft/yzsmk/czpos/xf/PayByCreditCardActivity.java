package com.winksoft.yzsmk.czpos.xf;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.winksoft.yzsmk.card.CardException;
import com.winksoft.yzsmk.card.CpuException;
import com.winksoft.yzsmk.card.CpuJBD;
import com.winksoft.yzsmk.card.DesfireCard;
import com.winksoft.yzsmk.card.DesfireException;
import com.winksoft.yzsmk.card.PsamException;
import com.winksoft.yzsmk.card.PsamJsb;
import com.winksoft.yzsmk.cpu.Cpu_cardInfor;
import com.winksoft.yzsmk.cpu.Cpu_psamInfor;
import com.winksoft.yzsmk.cpu.Cpu_tradingInfor;
import com.winksoft.yzsmk.czpos.CallBackListener;
import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.TicketResult;
import com.winksoft.yzsmk.czpos.YFApp;
import com.winksoft.yzsmk.czpos.YzsmkConst;
import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.czpos.model.CardType;
import com.winksoft.yzsmk.desfire.DesFireFileInfor;
import com.winksoft.yzsmk.desfire.PsamInfor;
import com.winksoft.yzsmk.desfire.desfire_tradingInfor;
import com.winksoft.yzsmk.ftp.BusinessMode;
import com.winksoft.yzsmk.utils.ByteUtils;
import com.yifengcom.yfpos.model.PrintType;
import com.yifengcom.yfpos.print.Print;
import com.yifengcom.yfpos.print.PrintPackage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PayByCreditCardActivity extends BaseActivity implements OnClickListener {
	public final byte[] CITY_CODE = new byte[] { (byte) 0x22, (byte) 0x50 };
	private TextView tvMoney, afterBalance, txBalance, tvResult;
	private ImageView imgResult;
	private Button btnPrint;
	private CheckTicketTask task; // 检票任务
	private String test_balance; // 余额
	private String test_afterBalance; // 消费后余额
	public static int localNo = 0; // CPU 卡消费流水号
	BusinessMode bm;
	private long money;
	private double douMoney;
	private String cardCategory = ""; // 卡片类别
	private String transactionDate = ""; // 交易日期
	private byte[] cardType; // 卡片类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paybycard);
		setTitle("消费");
		bm = BusinessMode.getInstance();
		money = this.getIntent().getLongExtra("money", 0);
		this.btnPrint = (Button) super.findViewById(R.id.btnPrint);
		this.btnPrint.setOnClickListener(this);
		this.tvResult = (TextView) super.findViewById(R.id.tvResult);
		this.imgResult = (ImageView) super.findViewById(R.id.imgResult);
		this.tvMoney = (TextView) super.findViewById(R.id.tvMoney);
		this.txBalance = (TextView) super.findViewById(R.id.txBalancepay);
		this.afterBalance = (TextView) super.findViewById(R.id.afterBalance);
		tvMoney.setText(String.format("%,.2f 元", money / 100.00));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (task == null) {
			task = new CheckTicketTask();
			task.execute();
		}
	}

	private void Package() {
		Map<String, String> map = new HashMap<String, String>();

		if (Cpu_tradingInfor.cpu_localNo != null) {
			String tmp = ByteUtils.printBytes(Cpu_tradingInfor.cpu_localNo);
			tmp = tmp.substring(2, tmp.length());
			map.put("body_0", tmp);
			System.out.println("body_0: " + map.get("body_0"));
		} else
			System.out.println("null:body_0");
		if (Cpu_tradingInfor.cpu_cardSerialNo != null) {
			String tmp = ByteUtils.printBytes(Cpu_tradingInfor.cpu_cardSerialNo);
			tmp = tmp.substring(1, tmp.length());
			map.put("body_12", tmp);
			System.out.println("body_12: " + map.get("body_12"));
			map.put("body_32", tmp);
			System.out.println("body_32: " + map.get("body_32"));
			tmp = tmp.substring(3, tmp.length());
			map.put("body_51", tmp);
			System.out.println("body_51: " + map.get("body_51"));
		} else
			System.out.println("null:body_12");
		if (Cpu_tradingInfor.cpu_cardType != null) {
			map.put("body_67", ByteUtils.printBytes(Cpu_tradingInfor.cpu_cardType));
			System.out.println("body_67: " + map.get("body_67"));
		} else
			System.out.println("null: body_67");
		int cpu_recordStatus = Cpu_tradingInfor.cpu_recordStatus[0];
		if (Cpu_tradingInfor.cpu_recordStatus != null) {
			map.put("body_70", cpu_recordStatus + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("body_70: " + map.get("body_70"));
		} else
			System.out.println("null: body_70");
		if (Cpu_tradingInfor.cpu_beforeBalance != null) {
			map.put("body_71", ByteUtils.printBytes(Cpu_tradingInfor.cpu_beforeBalance));
			System.out.println("body_71: " + map.get("body_71"));
		} else
			System.out.println("null: body_71");
		if (Cpu_tradingInfor.cpu_trandAmount != null) {
			map.put("body_79", ByteUtils.printBytes(Cpu_tradingInfor.cpu_trandAmount));
			System.out.println("body_79: " + map.get("body_79"));
		} else
			System.out.println("null: body_79");
		if (Cpu_tradingInfor.cpu_afterBalance != null) {
			map.put("body_87", ByteUtils.printBytes(Cpu_tradingInfor.cpu_afterBalance));
			System.out.println("body_87: " + map.get("body_87"));
		} else
			System.out.println("null: body_87");
		if (Cpu_tradingInfor.cpu_tradeType != null) {
			map.put("body_95", ByteUtils.printBytes(Cpu_tradingInfor.cpu_tradeType));
			System.out.println("body_95: " + map.get("body_95"));
		} else
			System.out.println("null: body_95");
		String cpu_terminalNum = ByteUtils.printBytes(Cpu_tradingInfor.cpu_terminalNum);
		while (cpu_terminalNum.substring(0, 1).equals("0")) {
			cpu_terminalNum = cpu_terminalNum.substring(1, cpu_terminalNum.length());
		}
		if (Cpu_tradingInfor.cpu_terminalNum != null) {
			map.put("body_105", cpu_terminalNum);// ByteUtils.printBytes(Cpu_tradingInfor.cpu_terminalNum)
			System.out.println("body_105: " + map.get("body_105"));
		} else
			System.out.println("null: body_105");

		if (Cpu_tradingInfor.cpu_psamCardNum != null) {
			map.put("body_117", ByteUtils.printBytes(Cpu_tradingInfor.cpu_psamCardNum));
			System.out.println("body_117: " + map.get("body_117"));
		} else
			System.out.println("null: body_117");

		String cpu_psamSerialNO = ByteUtils.printBytes(Cpu_tradingInfor.cpu_psamSerialNO);
		
		while (cpu_psamSerialNO.substring(0, 1).equals("0")) {
			cpu_psamSerialNO = cpu_psamSerialNO.substring(1, cpu_psamSerialNO.length());
		}
		if (cpu_psamSerialNO.length() > 6) {
			cpu_psamSerialNO = cpu_psamSerialNO.substring(cpu_psamSerialNO.length() - 6, cpu_psamSerialNO.length());
		}
		cpu_psamSerialNO = Integer.parseInt(cpu_psamSerialNO, 16) + "";

		if (Cpu_tradingInfor.cpu_psamSerialNO != null) {
			map.put("body_129", cpu_psamSerialNO);// ByteUtils.printBytes(Cpu_tradingInfor.cpu_psamSerialNO)
			System.out.println("body_129: " + map.get("body_129"));
		} else
			System.out.println("null: body_129");

		int cpu_lockCardFlag = Cpu_tradingInfor.cpu_lockCardFlag[0];
		
		if (Cpu_tradingInfor.cpu_lockCardFlag != null) {
			map.put("body_138", cpu_lockCardFlag + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_lockCardFlag)
			System.out.println("body_138: " + map.get("body_138"));
		} else
			System.out.println("null: body_138");

		if (Cpu_tradingInfor.cpu_termiTradeNo != null) {
			String tmp = ByteUtils.printBytes(Cpu_tradingInfor.cpu_termiTradeNo);
			tmp = tmp.substring(2, tmp.length());
			map.put("body_139", tmp);// "0000"+
			System.out.println("body_139: " + map.get("body_139"));
		} else
			System.out.println("null: body_139");

		if (Cpu_tradingInfor.cpu_termiTradeDate != null) {
			map.put("body_147", ByteUtils.printBytes(Cpu_tradingInfor.cpu_termiTradeDate));
			System.out.println("body_147: " + map.get("body_147"));
		} else
			System.out.println("null: body_147");
		if (Cpu_tradingInfor.cpu_termiTradeTime != null) {
			map.put("body_155", ByteUtils.printBytes(Cpu_tradingInfor.cpu_termiTradeTime));
			System.out.println("body_155: " + map.get("body_155"));
		} else
			System.out.println("null: body_155");

		if (Cpu_tradingInfor.cpu_tradeTAC != null) {
			map.put("body_161", ByteUtils.printBytes(Cpu_tradingInfor.cpu_tradeTAC));
			System.out.println("body_161: " + map.get("body_161"));
		} else
			System.out.println("null: body_161");
		if (Cpu_tradingInfor.cpu_debitSKeyVer != null) {
			map.put("body_169", ByteUtils.printBytes(Cpu_tradingInfor.cpu_debitSKeyVer));
			System.out.println("body_169: " + map.get("body_169"));
		} else
			System.out.println("null: body_169");
		if (Cpu_tradingInfor.cpu_debitSKeyIndex != null) {
			map.put("body_171", ByteUtils.printBytes(Cpu_tradingInfor.cpu_debitSKeyIndex));
			System.out.println("body_171: " + map.get("body_171"));
		} else
			System.out.println("null: body_171");
		if (Cpu_tradingInfor.cpu_cardOffSerialNo != null) {
			map.put("body_173", ByteUtils.printBytes(Cpu_tradingInfor.cpu_cardOffSerialNo));
			System.out.println("body_173: " + map.get("body_173"));
		} else
			System.out.println("null: body_173");
		if (Cpu_tradingInfor.cpu_IssuingCompany != null) {
			map.put("body_177", ByteUtils.printBytes(Cpu_tradingInfor.cpu_IssuingCompany));
			System.out.println("body_177: " + map.get("body_177"));
		} else
			System.out.println("null: body_177");

		if (Cpu_tradingInfor.cpu_cityCode_L != null) {
			map.put("body_193", ByteUtils.printBytes(Cpu_tradingInfor.cpu_cityCode_L));
			System.out.println("body_193: " + map.get("body_193"));
		} else
			System.out.println("null: body_193");
		if (Cpu_tradingInfor.cpu_cityCode_C != null) {
			map.put("body_197", ByteUtils.printBytes(Cpu_tradingInfor.cpu_cityCode_C));
			System.out.println("body_197: " + map.get("body_197"));
		} else
			System.out.println("null: body_197");

		String cpu_operaCompany = ByteUtils.printBytes(Cpu_tradingInfor.cpu_operaCompany);
		while (cpu_operaCompany.substring(0, 1).equals("0")) {
			cpu_operaCompany = cpu_operaCompany.substring(1, cpu_operaCompany.length());
		}
		if (Cpu_tradingInfor.cpu_operaCompany != null) {
			map.put("body_201", cpu_operaCompany);// ByteUtils.printBytes(Cpu_tradingInfor.cpu_operaCompany)
			System.out.println("body_201: " + map.get("body_201"));
		} else
			System.out.println("null: body_201");

		if (Cpu_tradingInfor.cpu_tradeCounterID != null) {
			map.put("body_229", ByteUtils.printBytes(Cpu_tradingInfor.cpu_tradeCounterID));
			System.out.println("body_229: " + map.get("body_229"));
		} else
			System.out.println("null: body_229");
		if (Cpu_tradingInfor.cpu_professionCode != null) {
			map.put("body_259", ByteUtils.printBytes(Cpu_tradingInfor.cpu_professionCode));
			System.out.println("body_259: " + map.get("body_259"));
		} else
			System.out.println("null: body_259");
		if (Cpu_tradingInfor.cpu_debitType != null) {
			System.out.println("body_261: " + map.get("body_261"));
		} else
			System.out.println("null: body_261");
		if (Cpu_tradingInfor.cpu_testFlag != null) {
			System.out.println("body_276: " + map.get("body_276"));
		} else
			System.out.println("null: body_276");

		if (Cpu_tradingInfor.cpu_randNum != null) {
			System.out.println("body_277: " + map.get("body_277"));
		} else
			System.out.println("null: body_277");
		bm.saveToxfDb(map, this.getApplicationContext());//

	}

	private void Package_desfire() {
		Map map = new HashMap<String, Object>();

		if (desfire_tradingInfor.crade_num != null) {
			map.put("crade_num", ByteUtils.printBytes(desfire_tradingInfor.crade_num));
			System.out.println("crade_num: " + map.get("crade_num"));
		} else
			System.out.println("null: crade_num");
		if (desfire_tradingInfor.city_code != null) {
			map.put("city_code", ByteUtils.printBytes(desfire_tradingInfor.city_code));
			System.out.println("city_code: " + map.get("city_code"));
		} else
			System.out.println("null: city_code");
		if (desfire_tradingInfor.passenger_num != null) {
			map.put("passenger_num", ByteUtils.printBytes(desfire_tradingInfor.passenger_num));
			System.out.println("passenger_num: " + map.get("passenger_num"));
		} else
			System.out.println("null: passenger_num");
		if (desfire_tradingInfor.care_class != null) {
			map.put("care_class", ByteUtils.printBytes(desfire_tradingInfor.care_class));
			System.out.println("care_class: " + map.get("care_class"));
		} else
			System.out.println("null: care_class");
		if (desfire_tradingInfor.care_type != null) {
			map.put("care_type", ByteUtils.printBytes(desfire_tradingInfor.care_type));
			System.out.println("care_type: " + map.get("care_type"));
		} else
			System.out.println("null: care_type");
		if (desfire_tradingInfor.crade_class != null) {
			map.put("crade_class", ByteUtils.printBytes(desfire_tradingInfor.crade_class));
			System.out.println("crade_class: " + map.get("crade_class"));
		} else
			System.out.println("null: crade_class");
		if (desfire_tradingInfor.crade_date != null) {
			map.put("crade_date", ByteUtils.printBytes(desfire_tradingInfor.crade_date));
			System.out.println("crade_date: " + map.get("crade_date"));
		} else
			System.out.println("null: crade_date");
		if (desfire_tradingInfor.consume_money != null) {
			map.put("consume_money", ByteUtils.printBytes(desfire_tradingInfor.consume_money));
			System.out.println("consume_money: " + map.get("consume_money"));
		} else
			System.out.println("null: consume_money");
		if (desfire_tradingInfor.consume_after_balance != null) {
			map.put("consume_after_balance", ByteUtils.printBytes(desfire_tradingInfor.consume_after_balance));
			System.out.println("consume_after_balance: " + map.get("consume_after_balance"));
		} else
			System.out.println("null: consume_after_balance");
		if (desfire_tradingInfor.authorization_num != null) {
			map.put("authorization_num", ByteUtils.printBytes(desfire_tradingInfor.authorization_num));
			System.out.println("authorization_num: " + map.get("authorization_num"));
		} else
			System.out.println("null: authorization_num");
		if (desfire_tradingInfor.recharge_money != null) {
			map.put("recharge_money", ByteUtils.printBytes(desfire_tradingInfor.recharge_money));
			System.out.println("recharge_money: " + map.get("recharge_money"));
		} else
			System.out.println("null: recharge_money");
		if (desfire_tradingInfor.recharge_date != null) {
			map.put("recharge_date", ByteUtils.printBytes(desfire_tradingInfor.recharge_date));
			System.out.println("recharge_date: " + map.get("recharge_date"));
		} else
			System.out.println("null: recharge_date");
		if (desfire_tradingInfor.crade_auth != null) {
			map.put("crade_auth", ByteUtils.printBytes(desfire_tradingInfor.crade_auth));
			System.out.println("crade_auth: " + map.get("crade_auth"));
		} else
			System.out.println("null: crade_auth");
		if (desfire_tradingInfor.care_num != null) {
			map.put("care_num", ByteUtils.printBytes(desfire_tradingInfor.care_num));
			System.out.println("care_num: " + map.get("care_num"));
		} else
			System.out.println("null: care_num");
		byte[] tmp = desfire_tradingInfor.getConsume_crade_val_mac_record();
		if (tmp != null) {
			map.put("consume_crade_val", ByteUtils.printBytes(tmp));
			System.out.println("consume_crade_val: " + map.get("consume_crade_val"));
		} else
			System.out.println("null: consume_crade_val");
		
		bm.saveToxfDb_desfire(map, this.getApplicationContext());// 保存数据
	}

	private class CheckTicketTask extends AsyncTask<Void, Object, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			byte[] atr = null;
			byte[] cpuID = null;
			TicketResult re = TicketResult.FAIL;
			try {
				this.publishProgress(YzsmkConst.P_TICKET_BEGIN);

				// 1开始寻找卡片
				this.publishProgress(YzsmkConst.P_FIND_CARD);
				atr = YFApp.getApp().iService.rfidOpenEx(60);

				this.publishProgress(YzsmkConst.P_READ_INFO);
				// 1 判断卡片类型,
				if (atr != null) {
					if (atr[0] == YzsmkConst.CARD_CPU) { // CPU卡片处理
						cpuID = ByteUtils.getBytes(atr, 2, atr.length - 2);
						// cpu 设置交易金额
						re = cpuYuanlinCheck1(cpuID, (int) money);
					} else if (atr[0] == YzsmkConst.CARD_DESFIRE) { // DESFIRE卡片处理
						cpuID = ByteUtils.getBytes(atr, 2, atr.length - 2);
						// disfire 设置交易金额
						re = desfireYanlinCheck1(cpuID, (int) money);
					} else if (atr[0] == YzsmkConst.CARD_TIMEOUT) {
						re = TicketResult.E_FINDTIMEOUT;
					} else {
						re = TicketResult.E_FIND;
					}
				}
				this.publishProgress(YzsmkConst.P_UPDATE_INFO, re);
			} catch (CardException e) {
				this.publishProgress(YzsmkConst.P_ERROR, e);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
				}
			} catch (Exception e) {
				this.publishProgress(YzsmkConst.P_ERROR, e);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			int step = (Integer) values[0];
			switch (step) {
			case YzsmkConst.P_TICKET_BEGIN:
				// tvResult.setText("开始检票...");
				break;
			case YzsmkConst.P_FIND_CARD:
				tvResult.setText("正在寻卡...");
				break;
			case YzsmkConst.P_READ_INFO:
				tvResult.setText("正在读取信息...");
				break;
			case YzsmkConst.P_UPDATE_INFO:
				TicketResult result = (TicketResult) values[1];
				if (result.equals(TicketResult.OK)) {
					txBalance.setText(test_balance + "元");
					afterBalance.setText(test_afterBalance + "元");
					tvResult.setText("交易成功");
					imgResult.setImageResource(R.drawable.jptg);
					// 跳转到成功页面
					// Intent intent = new Intent(
					// PayByCreditCardActivity.this,
					// SuccessPayActivity.class);
					// startActivity(intent);
					// PayByCreditCardActivity.this.finish();
					doPrint();
				} else {
					tvResult.setText("交易失败");
					imgResult.setImageResource(R.drawable.jpsb);
				}
				break;
			case YzsmkConst.P_ERROR:
				closeRFID();
				String msg = values[1].toString();
				tvResult.setText(TicketResult.FAIL.getMesssage());
				imgResult.setImageResource(R.drawable.jpsb);
				break;
			default:
				break;
			}
		}
	}

	public void doPrint() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", "市民卡消费凭证");
		map.put("body_1", "消费（" + cardCategory + "）"); // 交易类型
		map.put("body_2", transactionDate); // 交易日期
		map.put("body_3", tvMoney.getText().toString()); // 交易金额
		map.put("body_4", txBalance.getText().toString()); // 消费前金额
		map.put("body_5", afterBalance.getText().toString()); // 消费后余额
		map.put("body_6", ""); // 网点编号
//		map.put("body_7", new QueryValues(1).getJobNumber()); // 操作员号
		map.put("body_8", cardType != null ? CardType.convert(cardType[0]).getMesssage() : ""); // 卡片类型
		try {
			YFApp.getApp().iService.onPrint(getPrintBody(map), mCallBack);
		} catch (RemoteException e) {
			e.printStackTrace();
			showToast("打印异常");
			setBtn(true);
		} catch (Exception e) {
			e.printStackTrace();
			showToast("打印异常");
			setBtn(true);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private TicketResult desfireYanlinCheck1(byte[] cpuID, int consumptioAmount) throws Exception {
		// 读取基础信息
		// 读取园林应用信息
		DesfireCard tag = new DesfireCard() {

			@Override
			public byte[] transmit(byte[] send) throws DesfireException {
				try {
					byte[] result = YFApp.getApp().iService.rfidApduEx(send, 2);
					byte[] data = new byte[result.length - 2];
					if (result[1] == 0x00)
						System.arraycopy(result, 2, data, 0, result.length - 2);
					else
						throw new DesfireException(0, "执行命令失败：" + data[1]);
					return data;
				} catch (RemoteException e) {

					Log.i("desfire---71555", "PSAM");
					throw new DesfireException(0, e.getMessage());
				}
			}

		};

		PsamJsb psam = new PsamJsb() {
			@Override
			public byte[] transmit(byte[] send) throws PsamException {
				try {
					byte[] result = YFApp.getApp().iService.psamApduEx(1, send, 2);
					byte[] data = new byte[result.length - 2];
					if (result[1] == 0x00)
						System.arraycopy(result, 2, data, 0, result.length - 2);
					else
						throw new PsamException(0, "执行命令失败：" + data[1]);
					return data;
				} catch (RemoteException e) {
					throw new PsamException(0, e.getMessage());
				}
				// return null;
			}

		};

		cardCategory = "旧";

		// 1、所有参数初始化
		DesFireFileInfor desFireFileInfor = new DesFireFileInfor();
		PsamInfor psamInfor = new PsamInfor();
		desfire_tradingInfor tradingInfor = new desfire_tradingInfor();
		// IsAvailableInfor isAvailableInfor = new IsAvailableInfor();
		desFireFileInfor.setUID(cpuID);
		// 2、PSAM卡操作
		byte tmp[];
		String s;
		tmp = YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位
		s = ByteUtils.getString(tmp, 0, tmp.length);
		Log.i("desfire---1", s.toString());
		tmp = psam.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x0e); // 读取PSAM卡
																		// 0x15文件
		Log.i("desfire---2", tmp.toString());
		psamInfor.setFile15(tmp); // 保存PSAM卡 0x15文件信息
		tmp = psam.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x06); // 读取PSAM卡
																		// 0x16文件
		Log.i("desfire---3", tmp.toString());
		psamInfor.setFile16(tmp); // 保存PSAM卡 0x16文件信息
		// tmp= YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位
		// 3、读取desFire卡文件信息
		tag.rats((byte) 0x50); // 1)rtas 卡复位
		tag.selectApplication(new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00 }); // 2)选择目录
		tmp = tag.readData((byte) 0x01, 0x00, 0x2A); // 3)读取 01文件
		Log.i("desfire---4", tmp.toString());
		desFireFileInfor.setFile01(tmp);
		tmp = tag.readData((byte) 0x05, 0x00, 0x20); // 4)读取 05文件
		Log.i("desfire---5", tmp.toString());
		desFireFileInfor.setFile05(tmp);
		tmp = tag.readData((byte) 0x0E, 0x00, 0x18); // 5)读取 0e文件
		Log.i("desfire---6", tmp.toString());
		desFireFileInfor.setFile0E(tmp);
		cardType = desFireFileInfor.file0E_Card_Type;
		tmp = tag.readData((byte) 0x0F, 0x00, 0x20); // 6)读取 0F文件
		Log.i("desfire---7", tmp.toString());
		desFireFileInfor.setFile0F(tmp);
		String testT = ByteUtils.byteToHex(tmp);
		Log.i("desfire---71", testT);
		// 4、通过PSAM卡产生子密钥，并通过双向认证后产生 会话密钥
		tmp = YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位
		psamInfor.getSubKey(psam);
		ByteBuffer subKeyBuf = ByteBuffer.allocate(16);
		subKeyBuf.put(PsamInfor.subKey) // PSAM卡返回 6字节密钥
				.put(PsamInfor.subKey) // PSAM卡返回 6字节密钥
				.put(ByteUtils.getBytes(DesFireFileInfor.uid, 1, 4)); // IC卡UID1--UID5
		DesFireFileInfor.sessionKey = tag.authenticate((byte) 0x01, subKeyBuf.array()); // 双向认证
																						// 并获得会话密钥
		// 5 、读取余额
		desFireFileInfor.setDecreaseFile(); // 判断并设置消费文件 00 或 06
		int balance = tag.getValue(DesFireFileInfor.decreaseFile, DesFireFileInfor.sessionKey);

		// ---------------test----------------------------------------
		test_balance = ByteUtils.getMoney(balance);
		// 查询余额截至
		// -------------------------------------------------------

		// 6、判断desfire卡是否有效
		if (isDesfireValid(tradingInfor, balance, consumptioAmount))
			;
		// 7、保存相关信息
		saveDesfireTradingInfor(desFireFileInfor, tradingInfor, balance, consumptioAmount);
		// 8、获取消费前交易次数
		int transactionNumber = tag.getValue((byte) 0x03, DesFireFileInfor.sessionKey); // 消费前
																						// 交易计数器
		tradingInfor.setConsume_crade_val(transactionNumber); // 交易前交易计数器
		// 9、消费
		tag.debit(DesFireFileInfor.decreaseFile, consumptioAmount, DesFireFileInfor.sessionKey);
		// 10、向IC卡写交易记录文件
		ByteBuffer writeFile = ByteBuffer.allocate(16);
		writeFile.put(DesFireFileInfor.file0F_city_code)
				// 城市代码 2
				.put(PsamInfor.file16_terid, 0, 2)
				// terid 0-1
				.put(PsamInfor.file16_terid, 4, 2)
				// terid 4-5
				.put(tradingInfor.recordTime)
				// 交易UTC 时间 4字节 ???????
				.put((byte) 0x00)
				// 消费类型
				.put((byte) (consumptioAmount & 0xff))
				// 消费金额
				.put((byte) ((consumptioAmount >> 8) & 0xff)).put((byte) ((consumptioAmount >> 16) & 0xff))
				.put((byte) 0x00); // 保留字节
		writeFile.put((byte) ByteUtils.genLRC(writeFile.array(), 0, 15)); // LRC
		tag.writeRecord((byte) 0x04, 0, 0x10, writeFile.array());
		// 11、向IC卡交易计数器写减次
		tag.debit((byte) 0x03, 1, DesFireFileInfor.sessionKey);
		// 12IC卡提交
		tag.commitTranscation();
		// 13、送PSAM卡计数TAC
		int afterBalance = tag.getValue(DesFireFileInfor.decreaseFile, DesFireFileInfor.sessionKey);
		if (afterBalance + consumptioAmount != balance)
			;
		tradingInfor.setConsume_after_balance(afterBalance);
		tmp = YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位
		tmp = psam.selectFileById(new byte[] { (byte) 0x10, (byte) 0x05 }); // 选择0000目录
		ByteBuffer TransactioRecords = ByteBuffer.allocate(32);
		TransactioRecords.put(desfire_tradingInfor.passenger_num, 4, 4) // 发行流水号（卡面号）后
																// 4Byte
				.put((byte) 0x00) // 补金额最高字节
				.put(desfire_tradingInfor.consume_after_balance) // [3];//消费后卡内余额 3
				.put(desfire_tradingInfor.consume_money) // [3];//消费金额
				.put(tradingInfor.getConsume_crade_val_mac()) // 消费后交易计数器 处理后的
																// 两字节 2
				.put(tradingInfor.getCrade_num_hex()) // POS 交易流水号 3 字节 HEX
				.put(desfire_tradingInfor.crade_date, 0, 6) // 交易日期时间 7 取6字节
				.put((byte) 0x00) // 终端ID 前面补个00
				.put(desfire_tradingInfor.TerminalInfo_ID) // 终端ID，终端自己的参数 4
				.put(PsamInfor.file15_uid, 6, 4) // PSAM卡号 4Byte 前面补0
				.put((byte) 0x80); // 填充字
		byte[] sDe = new byte[8];
		// sDe = ByteUtils.getBytes(TransactioRecords.array(), 0, 4); // 长度 不是
		// 8吗？？？ 可能是钱4字节参与了运算后面补了4字节0
		System.arraycopy(TransactioRecords.array(), 0, sDe, 0, 4);
		tmp = psam.deliveryKey(sDe, (byte) 0x24, (byte) 0x01); // 密钥初始化
		ByteBuffer TransactioRecords_mac = ByteBuffer.allocate(40);
		TransactioRecords_mac.put((byte) 0x00).put((byte) 0x00).put((byte) 0x00).put((byte) 0x00).put((byte) 0x00)
				.put((byte) 0x00).put((byte) 0x00).put((byte) 0x00).put(TransactioRecords.array()); // 32字节交易数据
		tmp = psam.cipher(TransactioRecords.array(), (byte) 0x05, (byte) 0x00);
		tradingInfor.setCrade_auth(tmp);
		// 保存操作员ID 4字节

		// ------------------test-------------------------------------
		test_afterBalance = ByteUtils.getMoney(afterBalance);
		// -------------------------------------------------------
		Package_desfire();
		return TicketResult.OK;
	}

	@SuppressLint("SimpleDateFormat")
	private boolean isDesfireValid(desfire_tradingInfor tradingInfor, int balance, int consumptioAmount) {
		// 1、判断余额是否大于消费金额
		if (balance < consumptioAmount)
			return false;
		// 2、判断城市代码是否正确
		if ((DesFireFileInfor.file0F_city_code[0] != CITY_CODE[0])
				|| (DesFireFileInfor.file0F_city_code[1] != CITY_CODE[1]))
			return false;
		// 3、获取保存时间，并判断卡是否在有效期内-----------------------------------------
		tradingInfor.setRecordTime(System.currentTimeMillis()); // 保存交易记录时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		transactionDate = getTime(curDate);
		byte[] time = ByteUtils.hexToByte(str); // str 转 HEX
		tradingInfor.setCrade_date(time);// 保存年月日 时分秒
		int nowtime = ByteUtils.getInt(ByteUtils.getBytes(time, 0, 4), 0);
		int statetime = ByteUtils.getInt(DesFireFileInfor.file0E_Startup_date, 0);
		int endtime = ByteUtils.getInt(DesFireFileInfor.file01_Valid_date, 0);
		if (nowtime < statetime) // 判断是否启用
			return false;
		if (nowtime > endtime) // 判断是否过期
			return false;
		// ---------------------------------------------------------------------
		// 4、判断黑名单
		if (DesFireFileInfor.file05_Blacklist_label[0] == 0x04)
			return false;
		// 5、卡号判断暂时不作 	
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private void saveDesfireTradingInfor(DesFireFileInfor desFireFileInfor, desfire_tradingInfor tradingInfor, int balance,
			int consumptioAmount) {
		tradingInfor.setAuthorization_num(DesFireFileInfor.file01_Latest_loading_authorization_code);// 最近充值授权码
		tradingInfor.setRecharge_money(ByteUtils.getBytes(ByteUtils.byte_H2L(DesFireFileInfor.file01_Latest_Balance), 1, 3));//最近充值金额 
		// ----------------充值日期  ------------------------
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		long dataTime = (long)ByteUtils.getInt(DesFireFileInfor.file01_Latest_loading_date_and_time, 0)*1000;
		Date curDate = new Date(dataTime);//获取当前时间
		String str = formatter.format(curDate);
		byte[] time = ByteUtils.hexToByte(str);	   // str 转  HEX 
		tradingInfor.setRecharge_date(time);
		//------------------------------------------------
		tradingInfor.setCare_type(DesFireFileInfor.file0E_Card_Type);// 卡种 实际1字节  内存2字节
		tradingInfor.setCity_code(DesFireFileInfor.file0F_city_code);// 城市代码 2
		tradingInfor.setPassenger_num(DesFireFileInfor.file0F_Card_internal_number);// 卡内号8
		tradingInfor.setCare_num(DesFireFileInfor.file0F_Card_engraved_number);// 卡面号8
		tradingInfor.setConsume_befor_balance(ByteUtils.getBytes(ByteUtils.putInt(balance), 1, 3));// 保存消费前余额
		tradingInfor.setConsume_money(consumptioAmount);//消费金额
		tradingInfor.setCare_class(new byte[]{(byte)0x04});	// 卡类型 1
		//
		tradingInfor.setCrade_class(new byte[]{(byte)0x00});	// 卡消费类型	  
		
		tradingInfor.setPassenger_num(DesFireFileInfor.file0F_Card_engraved_number); // 卡面号8字节
		// 流水号的管理
		tradingInfor.setCrade_num(new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01});  //POS 交易流水号 4  ???
	// ???
	}

	private TicketResult cpuYuanlinCheck1(byte[] cpuID, int consumptioAmount)
			throws CpuException, RemoteException, PsamException {
		CpuJBD tag = new CpuJBD() {
			@Override
			public byte[] transmit(byte[] send) throws CpuException {
				try {
					byte[] result = YFApp.getApp().iService.rfidApduEx(send, 4);
					byte[] data = new byte[result.length - 2];
					if (data[1] == 0x00)
						System.arraycopy(result, 2, data, 0, result.length - 2);
					else
						throw new CpuException(0, "执行命令失败：" + data[1]);
					return data;
				} catch (RemoteException e) {
					throw new CpuException(0, e.getMessage());
				}
			}

		};

		PsamJsb psam = new PsamJsb() {
			@Override
			public byte[] transmit(byte[] send) throws PsamException {
				try {
					byte[] result = YFApp.getApp().iService.psamApduEx(1, send, 4);// YFApp.getApp().iService.psamApduEx(2,
					// send, 2);
					byte[] data = new byte[result.length - 2];
					if (result[1] == 0x00)
						System.arraycopy(result, 2, data, 0, result.length - 2);
					else
						throw new PsamException(0, "执行命令失败：" + data[1]);
					return data;
				} catch (RemoteException e) {
					throw new PsamException(0, e.getMessage());
				}
			}
		};

		cardCategory = "新";
		// 1、所有参数初始化
		Cpu_cardInfor cpu_cardInfor = new Cpu_cardInfor();
		Cpu_psamInfor cpu_psamInfor = new Cpu_psamInfor();
		Cpu_tradingInfor cpu_tradingInfor = new Cpu_tradingInfor();
		// Cpu_isAvailableInfor cpu_isAvailableInfor = new
		// Cpu_isAvailableInfor();
		// desFireFileInfor.setUID(cpuID);
		// 2、读取PSAM卡文件并进入8011目录
		byte[] tmp;
		String s;
		tmp = YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位
															// YFApp.getApp().iService.psamResetEx(2,
															// 2);
		s = ByteUtils.getString(tmp, 0, tmp.length);
		Log.i("1234567890", s);
		tmp = psam.selectFileById(new byte[] { (byte) 0x3f, (byte) 0x00 }); // 选择
																			// 0x3f00
																			// 主目录
		tmp = psam.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x0e); // 读取PSAM卡
																		// 0x15文件
		cpu_psamInfor.setFile15(tmp); // 保存PSAM卡 0x15文件信息
		tmp = psam.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x06); // 读取PSAM卡
																		// 0x16文件
		cpu_psamInfor.setFile16(tmp); // 保存PSAM卡 0x16文件信息
		tmp = psam.selectFileById(new byte[] { (byte) 0x80, (byte) 0x11 }); // 选择
																			// 0x8011
																			// 主目录
		tmp = psam.getBinaryFile(true, (byte) 0x17, 0, (byte) 0x19); // 读取PSAM卡
																		// 0x17文件
		cpu_psamInfor.setFile17(tmp); // 保存PSAM卡 0x17文件信息

		// 3、读取CPU卡文件
		tmp = tag.selectFileByAID(new byte[] { (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x32,
				(byte) 0x01, (byte) 0x01, (byte) 0x05 }); // 选择AID 电子钱包 AID
		tmp = tag.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x1E); // 读取 0x15文件
		cpu_cardInfor.setFile15(tmp); // 保存CPU卡 0x15文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x37); // 读取 0x16文件
		cpu_cardInfor.setFile16(tmp); // 保存CPU卡 0x16文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x17, 0, (byte) 0x3C); // 读取 0x17文件
		cpu_cardInfor.setFile17(tmp); // 保存CPU卡 0x17文件信息
		cardType = cpu_cardInfor.file17_cardType;
		// 4、读取余额
		int balance = tag.getBalance();

		// ---------------test----------------------------------------
		test_balance = ByteUtils.getMoney(balance);
		// 查询余额截止
		// -------------------------------------------------------

		// 5、判断CPU卡是否有效
		if (isCpuValid(cpu_tradingInfor, balance, consumptioAmount) == false)
			; // 不是有效CUP有卡返回 或发送异常
		// 6、保存相关信息
		saveCpuTradingInfor(cpu_tradingInfor, balance, consumptioAmount);
		// 7、初始化消费
		ByteBuffer debitInitBuf = ByteBuffer.allocate(11);
		debitInitBuf.put(Cpu_psamInfor.file17_debitSkeyIndex) // 密钥索引
																// SAM卡00B0970001指令返回
				.put(ByteUtils.unsignedInt(consumptioAmount)) // 本次交易金额
				.put(Cpu_psamInfor.file16_terminalNum); // 终端机编号
														// SAM卡00B0960006指令返回
		tmp = tag.debitInit(debitInitBuf.array(), (byte) 0x01);
		cpu_cardInfor.setdebitInit(tmp); // 保存CPU卡 0x17文件信息
		// 8、计算MAC1
		ByteBuffer mac1Buf = ByteBuffer.allocate(36);
		mac1Buf.put(Cpu_cardInfor.debitInit_randNum)
				// 随机数 用户卡8050指令返回
				.put(Cpu_cardInfor.debitInit_cardOffSerialNo)
				// CPU卡脱机交易序号
				// 用户卡8050指令返回
				.put(ByteUtils.unsignedInt(consumptioAmount))
				// 本次交易金额
				.put((byte) 0x06)
				// 消费交易 固定值 06
				.put(Cpu_tradingInfor.cpu_termiTradeDate)
				// 交易日期及时间 终端系统日期与时间
				.put(Cpu_tradingInfor.cpu_termiTradeTime).put(Cpu_cardInfor.debitInit_skeyVer) // 密钥版本
																								// 用户卡8050指令返回
				.put(Cpu_cardInfor.debitInit_arithFlag) // 算法标识 用户卡8050指令返回
				.put(Cpu_cardInfor.file15_appSerialNo, 2, 8) // 卡应用序列号
				.put(Cpu_cardInfor.file15_isuCompyFlag); // 取15文件的前8个字节（发卡机构代码）
		tmp = psam.iniForPurchase(mac1Buf.array());
		cpu_psamInfor.setMac1(tmp);
		// 9、发送扣款指令
		ByteBuffer deductionsBuf = ByteBuffer.allocate(15);
		deductionsBuf.put(Cpu_psamInfor.mac1_termiOffSerialno)
				// 终端脱机交易序号
				// SAM卡8070指令返回
				.put(Cpu_tradingInfor.cpu_termiTradeDate)
				// 交易日期及时间 终端系统日期与时间
				// 与8070指令时间一致
				.put(Cpu_tradingInfor.cpu_termiTradeTime).put(Cpu_psamInfor.mac1_mac1);// MAC1
																						// SAM卡8070指令返回
		tmp = tag.deductions(deductionsBuf.array());
		cpu_cardInfor.setMac2(tmp);
		// 10、验证MAC2
		tmp = psam.creditForPurchase(Cpu_cardInfor.mac2_MAC2);
		// 11、保存剩余交易信息
		saveCpuRemainTradingInfor(cpu_tradingInfor);

		// --------------------test-----------------------------------
		int afterBalance = tag.getBalance();
		test_afterBalance = ByteUtils.getMoney(afterBalance);
		// -------------------------------------------------------
		// Package(cpu_tradingInfor); // 消费后打包数据
		Package();
		return TicketResult.OK;
	}

	@SuppressLint("SimpleDateFormat")
	private boolean isCpuValid(Cpu_tradingInfor cpu_tradingInfor, int balance, int consumptioAmount) {

		// 1、判断余额是否大于消费金额
		if (balance < consumptioAmount)
			return false;
		// 2、获取并保存当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		transactionDate = getTime(curDate);
		byte[] time = ByteUtils.hexToByte(str); // str 转 HEX
		Cpu_tradingInfor.setCpu_termiTradeDate(ByteUtils.getBytes(time, 0, 4));// 保存年月日
		Cpu_tradingInfor.setCpu_termiTradeTime(ByteUtils.getBytes(time, 4, 3));// 保存时分秒
		int nowtime = ByteUtils.getInt(Cpu_tradingInfor.cpu_termiTradeDate, 0, true);
		int statetime = ByteUtils.getInt(Cpu_cardInfor.file15_appStartDate, 0, true);
		int endtime = ByteUtils.getInt(Cpu_cardInfor.file15_appValidDate, 0, true);

		if (nowtime < statetime) // 判断是否启用
			return false;
		if (nowtime > endtime) // 判断是否过期
			return false;

		// 3、判断发卡机构应用版本号
		if (Cpu_cardInfor.file15_isuAppVer[0] == 0) // 判断发卡机构应用版本,作为启动状态判断位
			return false;

		// 4、 verfy 1 互联互通判断
		if (Cpu_cardInfor.file17_interflowType[0] == 0) // verfy 1 互联互通判断
			return false;

		return true;
	}

	private void saveCpuTradingInfor(Cpu_tradingInfor cpu_tradingInfor, int balance, int consumptioAmount) {
		Cpu_tradingInfor.setCpu_operaCompany(new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x08 });// 商户编号
																												// 取后8字节
		Cpu_tradingInfor.setCpu_terminalNum(new byte[] { (byte) 0x00, (byte) 0x09, (byte) 0x99, (byte) 0x99 });// POS机号
																												// 4字节
		// HEX
		Cpu_tradingInfor.setCpu_cardSerialNo(Cpu_cardInfor.file15_appSerialNo);// 应用序列号
		Cpu_tradingInfor.setCpu_IssuingCompany(Cpu_cardInfor.file15_isuCompyFlag);// 发卡机构代
		Cpu_tradingInfor.setCpu_cardType(Cpu_cardInfor.file17_cardType);// 卡种类型
		Cpu_tradingInfor.setCpu_cityCode_C(Cpu_cardInfor.file17_cityCode);// 城市代码
		Cpu_tradingInfor.setCpu_cityCode_L(new byte[] { (byte) 0x31, (byte) 0x20 });// 扬州本地城市代码
		Cpu_tradingInfor.setCpu_recordStatus(new byte[] { (byte) 0x00 });// 记录类型
		Cpu_tradingInfor.setCpu_debitType(new byte[] { (byte) 0x00 });// 消费类型
		Cpu_tradingInfor.setCpu_professionCode(new byte[] { (byte) 0x07 });// 行业代码
		Cpu_tradingInfor.setCpu_beforeBalance(ByteUtils.unsignedInt(balance));// 交易前余额
		Cpu_tradingInfor.setCpu_trandAmount(ByteUtils.unsignedInt(consumptioAmount));// 交易金额
		Cpu_tradingInfor.setCpu_afterBalance(ByteUtils.unsignedInt(balance - consumptioAmount));// 交易余额

		Cpu_tradingInfor.setCpu_debitSKeyIndex(Cpu_psamInfor.file17_debitSkeyIndex);// 1
																					// 建设部消费密钥索引号
		Cpu_tradingInfor.setCpu_psamCardNum(Cpu_psamInfor.file16_terminalNum);// 终端机编号
																				// SAM卡00B0960006指令返回

		Cpu_tradingInfor.setCpu_tradeType(new byte[] { (byte) 0x06 }); // 消费交易
																		// 固定值
																		// 06
																		// ?????
																		// 是否或有复合
																		// 消费
																		// 0x09

		Cpu_tradingInfor.setCpu_lockCardFlag(new byte[] { (byte) 0x00 }); // 锁卡交易标志
																			// 0为正常交易；1为锁卡交易。
		Cpu_tradingInfor.setCpu_testFlag(new byte[] { (byte) 0x00 }); // 测试标志
																		// 0为正式数据；1为测试数据

		// 10000010
		// 00099999
		Cpu_tradingInfor.setCpu_tradeCounterID(new byte[] { (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x10 }); // 后续作为参数设置
	}

	private void saveCpuRemainTradingInfor(Cpu_tradingInfor cpu_tradingInfor) {
		// 本地流水号
		PayByCreditCardActivity.localNo++;
		byte[] data = new byte[6];
		data = ByteUtils.unsignedInt(PayByCreditCardActivity.localNo);
		
		Cpu_tradingInfor.setCpu_localNo(data);
		
		Cpu_tradingInfor.setCpu_termiTradeNo(data);

		Cpu_tradingInfor.setCpu_debitSKeyVer(Cpu_cardInfor.debitInit_skeyVer); // 密钥版本
		
		Cpu_tradingInfor.setCpu_cardOffSerialNo(Cpu_cardInfor.debitInit_cardOffSerialNo); // cpu卡脱机交易序号
		
		Cpu_tradingInfor.setCpu_randNum(Cpu_cardInfor.debitInit_randNum); // 随机数

		Cpu_tradingInfor.setCpu_psamSerialNO(Cpu_psamInfor.mac1_termiOffSerialno); // 随机数

		Cpu_tradingInfor.setCpu_tradeTAC(Cpu_cardInfor.mac2_TAC); // 随机数
	}

	@Override
	protected void onPause() {
		stopTask();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// stopTask();
		super.onDestroy();
	}

	/**
	 * 取消操作
	 */
	private void cancel() {
		try {
			YFApp.getApp().iService.cancel();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭RFID
	 */
	private void closeRFID() {
		try {
			YFApp.getApp().iService.rfidCloseEx();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止读卡线程
	 */
	private void stopTask() {
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
			task = null;
			reset();
		}
		closeRFID();
		cancel();
	}
	
	public void reset() {
		txBalance.setText(test_balance);
		afterBalance.setText(test_afterBalance);
		tvResult.setText("");
		imgResult.setImageResource(R.drawable.jpz);
	}

	protected CallBackListener mCallBack = new CallBackListener() {
		@Override
		public void onError(final int errorCode, final String errorMessage) throws RemoteException {

			runOnUiThread(new Runnable() {
				public void run() {
					new AlertDialog.Builder(PayByCreditCardActivity.this).setTitle("提示")
							.setMessage("操作失败，返回码:" + errorCode + " 信息:" + errorMessage).setPositiveButton("确定", null)
							.show();
				}
			});

		}

		@Override
		public void onTimeout() throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					setBtn(true);
					new AlertDialog.Builder(PayByCreditCardActivity.this).setTitle("错误").setMessage("打印超时")
							.setPositiveButton("确定", null).show();
				}
			});
		}

		@Override
		public void onResultSuccess(final int ntype) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					if (ntype != 0) {
						setBtn(true);
					}
					showToast(PrintType.convert(ntype).getMesssage());
				}
			});
		}

		@Override
		public void onTradeCancel() throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					setBtn(true);
					new AlertDialog.Builder(PayByCreditCardActivity.this).setTitle("错误").setMessage("取消打印")
							.setPositiveButton("确定", null).show();

				}
			});
		}
	};

	protected byte[] getPrintBody(Map<String, String> map) {
		byte[] sendbuf = new byte[1024];
		try {
			Print printinfor = new Print();
			printinfor.PRINT_clear();

			String print = getString(map, "title");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_48X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "--------------------------------";
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易类型:" + getString(map, "body_1");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易日期:" + getString(map, "body_2");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "交易金额:" + getString(map, "body_3");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_48X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "消费前金额:" + getString(map, "body_4");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "消费后金额:" + getString(map, "body_5");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "网点编号:" + getString(map, "body_6");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "操作员号:" + getString(map, "body_7");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			print = "卡片类型:" + getString(map, "body_8");
			printinfor.PRINT_Add_character((byte) 0, Print.PNT_24X24, print.getBytes("gb2312"),
					(short) (print.getBytes("gb2312").length));

			printinfor.PRINT_Add_setp((short) (100));

			short sendLen = printinfor.PRINT_packages(sendbuf);
			byte[] sendbuf1 = new byte[sendLen];

			System.arraycopy(sendbuf, 0, sendbuf1, 0, sendLen);

			PrintPackage package1 = new PrintPackage(sendbuf1);
			if (package1 != null) {
				return package1.getPackData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnPrint) {
			setBtn(false);
			doPrint();
		}
	}

	private void setBtn(boolean flag) {
		btnPrint.setVisibility(flag ? View.VISIBLE : View.GONE);
	}
}
