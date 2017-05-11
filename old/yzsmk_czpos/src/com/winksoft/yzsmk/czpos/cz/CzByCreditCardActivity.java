package com.winksoft.yzsmk.czpos.cz;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import com.winksoft.yzsmk.card.CpuException;
import com.winksoft.yzsmk.card.CpuJBD;
import com.winksoft.yzsmk.card.PsamException;
import com.winksoft.yzsmk.card.PsamJsb;
import com.winksoft.yzsmk.cpu.Cpu_cardInfor;
import com.winksoft.yzsmk.cpu.Cpu_creditForLoadInfor;
import com.winksoft.yzsmk.cpu.Cpu_psamInfor;
import com.winksoft.yzsmk.cpu.Cpu_tradingInfor;
import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.TicketResult;
import com.winksoft.yzsmk.czpos.YFApp;
import com.winksoft.yzsmk.czpos.YzsmkConst;
import com.winksoft.yzsmk.czpos.main.MainActivity;
import com.winksoft.yzsmk.czpos.queryvalue.QueryValues;
import com.winksoft.yzsmk.ftp.BusinessMode;
import com.winksoft.yzsmk.utils.ByteUtils;
import com.yifengcom.yfpos.service.IService;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CzByCreditCardActivity extends MainActivity {
	
	private final String TAG = "CzByCreditCardActivity";
	
	private int serialNumber = 1;

	IService service2 = YFApp.getApp().iService;
	private CheckTicketTask2 task2; // 检票任务
	private byte test_cardType = 0; // 0 desfire卡 1 Cpu卡
	private String test_balance; // 余额
	private byte test_result = 0;// 执行结果 0失败 1成功
	private String test_afterBalance; // 消费后余额
	public static int localNo = 0; // CPU 卡消费流水号
	byte[] cpuID = null;
	public BusinessMode bm;
	public QueryValues qv;
	private TextView tvMoney, afterBalance, txBalance, tvResult;
	private ImageView imgResult;
	private Button btnPrint;
	private long money;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_for_load);
		setTitle("充值");
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
		qv = new QueryValues();
		if (service2 == null)
			service2 = YFApp.getApp().iService;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (task2 == null) {
			task2 = new CheckTicketTask2();
			task2.execute();
		}
	}

	// 打包交易明细
	private OnClickListener onPacket = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Package();
		}
	};

	private void Package() {
		Map map = new HashMap<String, Object>();

		String tmp = "00";

		if (Cpu_creditForLoadInfor.request_ACCEPT_CUS_NO != null) {
			map.put("CorpId", Cpu_creditForLoadInfor.request_ACCEPT_CUS_NO + "");
			System.out.println("CorpId: " + map.get("CorpId"));
		} else
			System.out.println("null: CorpId");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		tmp = formatter.format(curDate);
		map.put("SettDate", tmp);
		System.out.println("SettDate: " + map.get("SettDate"));

		if (Cpu_creditForLoadInfor.request_CITIZEN_CARD_NO != null) {
			map.put("CitizenCardNo", Cpu_creditForLoadInfor.request_CITIZEN_CARD_NO + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("CitizenCardNo: " + map.get("CitizenCardNo"));
		} else
			System.out.println("null: CitizenCardNo");

		if (Cpu_creditForLoadInfor.request_CRD_BAL_BEF != null) {
			map.put("CrdBalBef", Cpu_creditForLoadInfor.request_CRD_BAL_BEF + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("CrdBalBef: " + map.get("CrdBalBef"));
		} else
			System.out.println("null: CrdBalBef");

		if (Cpu_creditForLoadInfor.request_CRD_BAL_AFT != null) {
			map.put("CrdBalAft", Cpu_creditForLoadInfor.request_CRD_BAL_AFT + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("CrdBalAft: " + map.get("CrdBalAft"));
		} else
			System.out.println("null: CrdBalAft");

		if (Cpu_creditForLoadInfor.request_TXN_AMT != null) {
			map.put("TxnAmt", Cpu_creditForLoadInfor.request_TXN_AMT + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("TxnAmt: " + map.get("TxnAmt"));
		} else
			System.out.println("null: TxnAmt");

		if (Cpu_creditForLoadInfor.request_TXN_DT != null) {
			map.put("TxnDt", Cpu_creditForLoadInfor.request_TXN_DT + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("TxnDt: " + map.get("TxnDt"));
		} else
			System.out.println("null: TxnDt");

		if (Cpu_creditForLoadInfor.request_SAM_NO != null) {
			map.put("SamNo", Cpu_creditForLoadInfor.request_SAM_NO + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("SamNo: " + map.get("SamNo"));
		} else
			System.out.println("null: SamNo");

		if (Cpu_creditForLoadInfor.request_TERM_NO != null) {
			map.put("AccpetCusNo", Cpu_creditForLoadInfor.request_TERM_NO + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("AccpetCusNo: " + map.get("AccpetCusNo"));
		} else
			System.out.println("null: AccpetCusNo");

		if (Cpu_creditForLoadInfor.request_OPR_NO != null) {
			map.put("OprNo", Cpu_creditForLoadInfor.request_OPR_NO + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("OprNo: " + map.get("OprNo"));
		} else
			System.out.println("null: OprNo");

		if (Cpu_creditForLoadInfor.creditR_TAC != null) {
			tmp = ByteUtils.printBytes(Cpu_creditForLoadInfor.creditR_TAC);
			map.put("TAC", tmp + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("TAC: " + map.get("TAC"));
		} else
			System.out.println("null: TAC");

		if (Cpu_creditForLoadInfor.requestR_BUSINESS_NO != null) {
			map.put("BusinessNo", Cpu_creditForLoadInfor.requestR_BUSINESS_NO + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("BusinessNo: " + map.get("BusinessNo"));
		} else
			System.out.println("null: BusinessNo");

		if (Cpu_creditForLoadInfor.confirm_CURR_COUNT != null) {
			map.put("CurrCount", Cpu_creditForLoadInfor.confirm_CURR_COUNT + "");// ByteUtils.printBytes(Cpu_tradingInfor.cpu_recordStatus)
			System.out.println("CurrCount: " + map.get("CurrCount"));
		} else
			System.out.println("null: CurrCount");

		bm.saveToczDb(map, this.getApplicationContext());//
	}

	private class CheckTicketTask2 extends AsyncTask<Void, Object, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			byte[] atr = null;
			TicketResult re = TicketResult.FAIL;
			try {
				this.publishProgress(YzsmkConst.P_TICKET_BEGIN);
				// 1开始寻找卡片
				this.publishProgress(YzsmkConst.P_FIND_CARD);
				atr = service2.rfidOpenEx(60);
				this.publishProgress(YzsmkConst.P_READ_INFO);
				// 1 判断卡片类型,
				if (atr != null) {
					if (atr[0] == YzsmkConst.CARD_CPU) { // CPU卡片处理
						cpuID = ByteUtils.getBytes(atr, 2, atr.length - 2);
						// 1 ：０.01元    充值金额
						re = cpuCreditForLoad(cpuID, 1);  // 充值只有CPU
					} else if (atr[0] == YzsmkConst.CARD_DESFIRE) { // DESFIRE卡片处理
						cpuID = ByteUtils.getBytes(atr, 2, atr.length - 2);
						// re= desfireCreditForLoad(cpuID,1);
					} else if (atr[0] == YzsmkConst.CARD_TIMEOUT) {
						re = TicketResult.E_FINDTIMEOUT;
					} else {
						re = TicketResult.E_FIND;
					}
				}
				service2.rfidCloseEx(); // 关闭场强
				this.publishProgress(YzsmkConst.TEST_CARD_TYPE);
				this.publishProgress(YzsmkConst.P_UPDATE_INFO, re);
			} catch (Exception e) {
				this.publishProgress(YzsmkConst.P_ERROR, e);
			}
			this.publishProgress(YzsmkConst.P_TICKET_END);
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			int step = (Integer) values[0];
			switch (step) {
			case YzsmkConst.P_TICKET_BEGIN:
				tvResult.setText(TicketResult.CHECKING.getMesssage());
				// tvStatus.setText("开始检票...");
				break;
			case YzsmkConst.P_FIND_CARD:
				tvResult.setText(TicketResult.CHECKING.getMesssage());
				tvResult.setText("正在寻卡...");
				break;
			case YzsmkConst.P_READ_INFO:
				tvResult.setText(TicketResult.CHECKING.getMesssage());
				tvResult.setText("正在读取信息...");
				break;
			case YzsmkConst.P_UPDATE_INFO:
				TicketResult result = (TicketResult) values[1];
				// tvStatus.setText("检票完成");
				if (result.equals(TicketResult.OK)) {
					txBalance.setText(test_balance + "元");
					afterBalance.setText(test_afterBalance + "元");
					tvResult.setText("充值成功");
					imgResult.setImageResource(R.drawable.jptg);
					// doPrint();
				} else {
					tvResult.setText("充值失败");
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

	/**
	 * 停止读卡线程
	 */
	private void stopTask() {
		if (task2 != null && task2.getStatus() == AsyncTask.Status.RUNNING) {
			task2.cancel(true);
			task2 = null;
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnPrint) {
			setBtn(false);
			// doPrint();
		}
	}

	private void setBtn(boolean flag) {
		btnPrint.setVisibility(flag ? View.VISIBLE : View.GONE);
	}

	// ///////////////////////////////////////////////////////////////////////////
	private TicketResult cpuCreditForLoad(byte[] cpuID, int consumptioAmount)
			throws CpuException, RemoteException, PsamException {
		CpuJBD tag = new CpuJBD() {
			@Override
			public byte[] transmit(byte[] send) throws CpuException {
				try {
					byte[] result = service2.rfidApduEx(send, 4);
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
					byte[] result = service2.psamApduEx(1, send, 4);// service1.psamApduEx(2,
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

		// -----------test-----------
		test_cardType = 1; // CPU卡
		// --------------------

		byte[] tmp;
		String s;
		// 1、所有参数初始化
		Cpu_cardInfor cpu_cardInfor = new Cpu_cardInfor();
		Cpu_psamInfor cpu_psamInfor = new Cpu_psamInfor();
		Cpu_creditForLoadInfor cpu_creditForLoadInfor = new Cpu_creditForLoadInfor();
		// cpu_creditForLoadInfor.setInit_transAmount(ByteUtils.unsignedInt(consumptioAmount));

		tmp = psam.selectFileById(new byte[] { (byte) 0x3f, (byte) 0x00 }); // 选择
																			// 0x3f00
																			// 主目录
		tmp = psam.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x0e); // 读取PSAM卡
																		// 0x15文件
		cpu_psamInfor.setFile15(tmp); // 保存PSAM卡 0x15文件信息
		tmp = psam.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x06); // 读取PSAM卡
																		// 0x16文件
		cpu_psamInfor.setFile16(tmp); // 保存PSAM卡 0x16文件信息

		// 2、读取CPU卡文件
		tmp = tag.selectFileByAID(new byte[] { (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x32,
				(byte) 0x01, (byte) 0x01, (byte) 0x05 }); // 选择AID 电子钱包 AID
		tmp = tag.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x1E); // 读取 0x15文件
		cpu_cardInfor.setFile15(tmp); // 保存CPU卡 0x15文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x37); // 读取 0x16文件
		cpu_cardInfor.setFile16(tmp); // 保存CPU卡 0x16文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x17, 0, (byte) 0x3C); // 读取 0x17文件
		cpu_cardInfor.setFile17(tmp); // 保存CPU卡 0x17文件信息

		cpu_creditForLoadInfor.setrequest_SAM_NO();

		cpu_creditForLoadInfor.setRequest_TERM_SEQ(serialNumber++);
		cpu_creditForLoadInfor.setRequest_cardNumber(Cpu_cardInfor.file15_appSerialNo);
		cpu_creditForLoadInfor.setRequest_ISS_CITY_CD(Cpu_cardInfor.file17_cityCode);
		cpu_creditForLoadInfor.setResponse_CARD_TYPE(Cpu_cardInfor.file17_cardType);

		// 3、读取余额
		int balance = tag.getBalance();
		test_balance = ByteUtils.getMoney(balance);
		cpu_creditForLoadInfor.setInit_AmountOpratian(balance, consumptioAmount);

		// 4、判断CPU卡是否有效
		if (isCpuValid(cpu_creditForLoadInfor) == false)
			; // 不是有效CUP有卡返回 或发送异常
		// 5、保存相关信息
		// saveCpuTradingInfor(cpu_tradingInfor, balance, consumptioAmount);

		// 6、消费初始化圈存 (只为取最后消费交易计数器)
		ByteBuffer debitInitBuf = ByteBuffer.allocate(11);
		debitInitBuf.put(cpu_creditForLoadInfor.init_keyIndex) // 密钥索引 后台加密机指定
				.put(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }) // 本次交易金额
																						// 充值的金额
				.put(cpu_creditForLoadInfor.init_terminalNumber); // 终端机编号
																	// 后台指定的终端机编号
		tmp = tag.debitInit(debitInitBuf.array(), (byte) 0x01);
		cpu_creditForLoadInfor.setInit_consumptionResponse(tmp);

		// 7、读取交易记录 (只为取最后消费交易计数器)
		// for (int i = 1;i<=0x0a;i++){
		tmp = tag.getRecordFile(1, (byte) 0x18);
		cpu_creditForLoadInfor.setRead_recode(tmp);
		// }
		// 8、初始化圈存
		ByteBuffer creditInitBuf = ByteBuffer.allocate(11);
		creditInitBuf.put(cpu_creditForLoadInfor.init_keyIndex) // 密钥索引 后台加密机指定
				.put(cpu_creditForLoadInfor.init_transAmount) // 本次交易金额 充值的金额
				.put(cpu_creditForLoadInfor.init_terminalNumber); // 终端机编号
																	// 后台指定的终端机编号
		tmp = tag.debitInit(creditInitBuf.array(), (byte) 0x00);
		cpu_creditForLoadInfor.setInit_response(tmp); // 保存CPU卡 0x17文件信息
		// 9、圈存请求（连接后台）
		// JSONObject temp =packageRequest();
		String temp = packageRequest();

		/*
		 * String testtmp
		 * =" {\"TRANSCODE\":\"5312\",\"RTN_CODE\":\"000000\",\"RTN_MSG\":\"充值成功\",\"TXN_MAC2\":\"123456ab\",\"CT_SEQ\":\"1\",\"BUSINESS_NO\":\"2\"}"
		 * ; try { Map<String, Object> map = parseRequest(testtmp); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * System.out.println(Cpu_creditForLoadInfor.requestR_TRANSCODE);
		 * 
		 * System.out.println(Cpu_creditForLoadInfor.requestR_RTN_CODE);
		 * System.out.println(Cpu_creditForLoadInfor.requestR_RTN_MSG);
		 * System.out.println(Cpu_creditForLoadInfor.requestR_TXN_MAC2);
		 * System.out.println(Cpu_creditForLoadInfor.requestR_CT_SEQ);
		 * System.out.println(Cpu_creditForLoadInfor.requestR_BUSINESS_NO);
		 */

		try {
			Map<String, Object> map = parseRequest(temp, cpu_creditForLoadInfor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!Cpu_creditForLoadInfor.requestR_RTN_CODE.equals("000000"))
			return TicketResult.FAIL;
		// 8、圈存
		ByteBuffer creditBuf = ByteBuffer.allocate(11);
		creditBuf.put(cpu_creditForLoadInfor.credit_transData) // 交易日期（主机） 4
				.put(cpu_creditForLoadInfor.credit_transTime) // 交易时间（主机） 3
				.put(cpu_creditForLoadInfor.credit_mac2); // MAC2 4
		tmp = tag.circleDeposit(creditBuf.array());
		cpu_creditForLoadInfor.setCredit_response(tmp); // 保存圈存返回的 TAC

		// 9、初始化圈存 （和之前圈存不同，本次只为获取联机交易序号）
		ByteBuffer creditInitBufG = ByteBuffer.allocate(11);
		creditInitBufG.put(cpu_creditForLoadInfor.init_keyIndex) // 密钥索引 后台加密机指定
				.put(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }) // 本次交易金额
																						// 充值的金额
				.put(cpu_creditForLoadInfor.init_terminalNumber); // 终端机编号
																	// 后台指定的终端机编号
		tmp = tag.debitInit(creditInitBufG.array(), (byte) 0x00);
		cpu_creditForLoadInfor.setInit_response(tmp, 1); //

		// 9、圈存确认(连接后台)
		String confirmResponse = packageConfirm();
		try {
			Map<String, Object> map = parseConfirm(confirmResponse, cpu_creditForLoadInfor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// saveCpuRemainTradingInfor(cpu_tradingInfor);

		// --------------------test-----------------------------------
		int afterBalance = tag.getBalance();
		test_afterBalance = ByteUtils.getMoney(afterBalance);
		test_result = 1;// 执行成功
		// -------------------------------------------------------
		// Package(cpu_tradingInfor); // 消费后打包数据
		return TicketResult.OK;
	}

	@SuppressLint("SimpleDateFormat")
	private boolean isCpuValid(Cpu_creditForLoadInfor cpu_creditForLoadInfor) {
		// 2、获取并保存当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		byte[] time = ByteUtils.hexToByte(str); // str 转 HEX
		Cpu_tradingInfor.setCpu_termiTradeDate(ByteUtils.getBytes(time, 0, 4));// 保存年月日
																				// ？？？？？？
		Cpu_tradingInfor.setCpu_termiTradeTime(ByteUtils.getBytes(time, 4, 3));// 保存时分秒
		int nowtime = ByteUtils.getInt(Cpu_tradingInfor.cpu_termiTradeDate, 0, true);
		int statetime = ByteUtils.getInt(Cpu_cardInfor.file15_appStartDate, 0, true);
		int endtime = ByteUtils.getInt(Cpu_cardInfor.file15_appValidDate, 0, true);

		if (nowtime < statetime) // 判断是否启用
			return false;
		if (nowtime > endtime) // 判断是否过期
			return false;

		byte[] tmp = new byte[7];
		System.arraycopy(Cpu_tradingInfor.cpu_termiTradeDate, 0, tmp, 0, Cpu_tradingInfor.cpu_termiTradeDate.length);
		System.arraycopy(Cpu_tradingInfor.cpu_termiTradeTime, 0, tmp, Cpu_tradingInfor.cpu_termiTradeDate.length,
				Cpu_tradingInfor.cpu_termiTradeTime.length);
		cpu_creditForLoadInfor.setRequest_TXN_DT(tmp); // 保存充值的时间
		cpu_creditForLoadInfor.setCredit_transTimes(Cpu_tradingInfor.cpu_termiTradeDate,
				Cpu_tradingInfor.cpu_termiTradeTime);
		// 3、判断发卡机构应用版本号
		if (Cpu_cardInfor.file15_isuAppVer[0] == 0) // 判断发卡机构应用版本,作为启动状态判断位
			return false;

		// 4、 verfy 1 互联互通判断
		if (Cpu_cardInfor.file17_interflowType[0] == 0) // verfy 1 互联互通判断
			return false;

		return true;
	}

	// private JSONObject packageRequest(){
	private String packageRequest() {
		JSONObject temp = new JSONObject(); // 组发送后台的jons数据
		try {
			temp.put("TRANSCODE", Cpu_creditForLoadInfor.request_TRANSCODE)
					.put("CITIZEN_CARD_NO", Cpu_creditForLoadInfor.request_CITIZEN_CARD_NO)
					.put("APP_FLAG", Cpu_creditForLoadInfor.request_APP_FLAG)
					.put("ACCEPT_CUS_NO", Cpu_creditForLoadInfor.request_ACCEPT_CUS_NO)
					.put("TERM_NO", Cpu_creditForLoadInfor.request_TERM_NO)
					.put("SAM_NO", Cpu_creditForLoadInfor.request_SAM_NO)
					.put("OPR_NO", Cpu_creditForLoadInfor.request_OPR_NO)
					.put("TERM_SEQ", Cpu_creditForLoadInfor.request_TERM_SEQ)
					.put("CRD_NO", Cpu_creditForLoadInfor.request_CRD_NO)
					.put("CRD_PHYS_TP", Cpu_creditForLoadInfor.request_CRD_PHYS_TP)
					.put("ISS_CITY_CD", Cpu_creditForLoadInfor.request_ISS_CITY_CD)
					.put("TXN_CITY_CD", Cpu_creditForLoadInfor.request_TXN_CITY_CD)
					.put("CRD_CITY_CD", Cpu_creditForLoadInfor.request_CRD_CITY_CD)
					.put("CURR_COUNT", Cpu_creditForLoadInfor.request_CURR_COUNT)
					.put("CRD_BAL_BEF", Cpu_creditForLoadInfor.request_CRD_BAL_BEF)
					.put("CRD_BAL_AFT", Cpu_creditForLoadInfor.request_CRD_BAL_AFT)
					.put("TXN_AMT", Cpu_creditForLoadInfor.request_TXN_AMT)
					.put("TXN_DT", Cpu_creditForLoadInfor.request_TXN_DT)
					.put("CRD_TXN_TYPE", Cpu_creditForLoadInfor.request_CRD_TXN_TYPE)
					.put("DIV_FACTOR", Cpu_creditForLoadInfor.request_DIV_FACTOR)
					.put("RAND_NU", Cpu_creditForLoadInfor.request_RAND_NU)
					.put("TXN_MAC1", Cpu_creditForLoadInfor.request_TXN_MAC1)
					.put("RAND_NUM_FILL", Cpu_creditForLoadInfor.request_RAND_NUM_FILL)
					.put("CARD_TYPE", Cpu_creditForLoadInfor.request_CARD_TYPE)
					.put("REAL_TXN_AMT", Cpu_creditForLoadInfor.request_REAL_TXN_AMT)
					.put("LAST_TXN_CNT", Cpu_creditForLoadInfor.request_LAST_TXN_CNT)
					.put("LAST_TXN_DT", Cpu_creditForLoadInfor.request_LAST_TXN_DT)
					.put("SAM_SEQ", Cpu_creditForLoadInfor.request_SAM_SEQ);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			System.out.println(temp.toString());
			return (requestSend(temp.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.toString();
	}

	private String requestSend(String sendData) throws Exception {
		// URL url = new URL(qv.getIpNum());
		URL url = new URL("http://10.100.30.12:8085/bill/pbp/api/gateway.do");// ("http://222.189.209.114:8082/bill/pbp/api/gateway.do");
		// 创建HttpURLConnection
		URLConnection rulConnection = url.openConnection();// 此处的urlConnection对象实际上是根据URL的
		// 请求协议(此处是http)生成的URLConnection类
		// 的子类HttpURLConnection,故此处最好将其转化
		// 为HttpURLConnection类型的对象,以便用到
		// HttpURLConnection更多的API.如下:
		HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
		// 设置HttpURLConnection参数
		httpUrlConnection.setConnectTimeout(30000);
		httpUrlConnection.setReadTimeout(30000);
		// 设定请求的方法为"POST"，默认是GET
		httpUrlConnection.setRequestMethod("POST");
		// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
		// http正文内，因此需要设为true, 默认情况下是false;
		httpUrlConnection.setDoOutput(true);
		// 设置是否从httpUrlConnection读入，默认情况下是true;
		httpUrlConnection.setDoInput(true);
		// Post 请求不能使用缓存
		httpUrlConnection.setUseCaches(false);
		// 设定传送的内容类型是可序列化的java对象
		// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
		httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
		// 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
		httpUrlConnection.connect();
		// URLConnection建立连接
		// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
		// 所以在开发中不调用上述的connect()也可以)。
		OutputStream outStrm = httpUrlConnection.getOutputStream();
		// HttpURLConnection发送请求
		// // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
		// ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
		// // 向对象输出流写出数据，这些数据将存到内存缓冲区中
		// objOutputStrm.writeObject(sendData);
		// // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
		// objOutputStrm.flush();
		// // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
		// // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
		outStrm.write(sendData.getBytes());
		outStrm.close();
		// HttpURLConneciton获取响应
		// 调用HttpURLConnection连接对象的getInputStream()函数,
		InputStream inStrm = httpUrlConnection.getInputStream();
		Scanner scanner = new Scanner(inStrm, "UTF-8");
		String text = scanner.useDelimiter("\\A").next();
		System.out.println(text);
		scanner.close();
		// String result = IOUtils.toString(inStrm, "UTF-8");
		// String tmp = inStrm.toString();
		return text;
		// 设置POST参数
		// OutputStream os = httpUrlConnection.getOutputStream();
		// String param = new String();
		// param = "CorpID=" + CorpID +
		// "&LoginName=" + LoginName+
		// "&send_no=" + phoneNumber +
		// "&msg=" + java.net.URLEncoder.encode(msg,"GBK"); ;
		// os.write(param.getBytes());
		// 超时设置，防止 网络异常的情况下，可能会导致程序僵死而不继续往下执行
		// HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
	}

	private Map<String, Object> parseRequest(String str, Cpu_creditForLoadInfor cpu_creditForLoadInfor)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObj = new JSONObject(str); // json数据

		map.put("TRANSCODE", jsonObj.getString("TRANSCODE"));
		map.put("RTN_CODE", jsonObj.getString("RTN_CODE"));
		map.put("RTN_MSG", jsonObj.getString("RTN_MSG"));
		map.put("TXN_MAC2", jsonObj.getString("TXN_MAC2"));
		map.put("CT_SEQ", jsonObj.getString("CT_SEQ"));
		map.put("BUSINESS_NO", jsonObj.getString("BUSINESS_NO"));
		map.put("SYS_TIME", jsonObj.getString("SYS_TIME"));

		Cpu_creditForLoadInfor.requestR_TRANSCODE = (String) map.get("TRANSCODE"); // 1
																					// 交易码
																					// TRANSCODE
																					// 4
																					// 是
																					// 字符串
																					// 5312
		Cpu_creditForLoadInfor.requestR_RTN_CODE = (String) map.get("RTN_CODE"); // 2
																					// 返回码
																					// RTN_CODE
																					// 6
																					// 是
																					// 字符串
																					// 成功:000000
																					// 其它：失败
		Cpu_creditForLoadInfor.requestR_RTN_MSG = (String) map.get("RTN_MSG"); // 3
																				// 返回信息
																				// RTN_MSG
																				// 30
																				// 是
																				// 字符串
		Cpu_creditForLoadInfor.requestR_TXN_MAC2 = (String) map.get("TXN_MAC2"); // 4
																					// 交易MAC2
																					// TXN_MAC2
																					// 8
																					// 否
																					// 字符串
																					// 16进制，失败不返回
		Cpu_creditForLoadInfor.requestR_CT_SEQ = (String) map.get("CT_SEQ"); // 5
																				// 中心流水号
																				// CT_SEQ
																				// 24
																				// 否
																				// 字符串
																				// 失败不返回
		Cpu_creditForLoadInfor.requestR_BUSINESS_NO = (String) map.get("BUSINESS_NO"); // 6
																						// 业务流水号
																						// BUSINESS_NO
																						// 10
																						// 否
																						// 字符串
																						// 失败不返回

		Cpu_creditForLoadInfor.confirm_CT_SEQ = Cpu_creditForLoadInfor.requestR_CT_SEQ; // 3
																						// 中心流水号
																						// CT_SEQ
																						// 24
																						// 是
																						// 字符串
		Cpu_creditForLoadInfor.confirm_BUSINESS_NO = Cpu_creditForLoadInfor.requestR_BUSINESS_NO; // 4
																									// 业务流水号
																									// BUSINESS_NO
																									// 10
																									// 是
																									// 字符串
																									// 需要冲正的业务流水号

		cpu_creditForLoadInfor.setCredit_mac2(ByteUtils.hexToByte(Cpu_creditForLoadInfor.requestR_TXN_MAC2));
		// if (map.get("SYS_TIME") != null){
		// byte[] reponseSystime = ByteUtils.hexToByte((String)
		// map.get("SYS_TIME"));
		// cpu_creditForLoadInfor.setCredit_transTimes(ByteUtils.getBytes(reponseSystime,0,4),ByteUtils.getBytes(reponseSystime,4,3));
		// }
		return map;
	}

	// private JSONObject packageConfirm(){
	private String packageConfirm() {
		JSONObject temp = new JSONObject(); // 组发送后台的jons数据
		try {
			temp.put("TRANSCODE", Cpu_creditForLoadInfor.confirm_TRANSCODE)
					.put("CITIZEN_CARD_NO", Cpu_creditForLoadInfor.confirm_CITIZEN_CARD_NO)
					.put("CT_SEQ", Cpu_creditForLoadInfor.confirm_CT_SEQ)
					.put("BUSINESS_NO", Cpu_creditForLoadInfor.confirm_BUSINESS_NO) // 冲正时才带这个字段
					.put("CRD_BAL_AFT", Cpu_creditForLoadInfor.confirm_CRD_BAL_AFT)
					.put("CURR_COUNT", Cpu_creditForLoadInfor.confirm_CURR_COUNT)
					.put("APP_FLAG", Cpu_creditForLoadInfor.confirm_APP_FLAG)
					.put("TXN_AMT", Cpu_creditForLoadInfor.confirm_TXN_AMT)
					.put("TXN_DT", Cpu_creditForLoadInfor.confirm_TXN_DT);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			System.out.println(temp.toString());
			return (requestSend(temp.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.toString();
	}

	private Map<String, Object> parseConfirm(String str, Cpu_creditForLoadInfor cpu_creditForLoadInfor)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObj = new JSONObject(str); // json数据

		map.put("TRANSCODE", jsonObj.getString("TRANSCODE"));
		map.put("RTN_CODE", jsonObj.getString("RTN_CODE"));
		map.put("RTN_MSG", jsonObj.getString("RTN_MSG"));
		map.put("BUSINESS_NO", jsonObj.getString("BUSINESS_NO"));

		Cpu_creditForLoadInfor.confirmR_TRANSCODE = (String) map.get("TRANSCODE"); // 1
																					// 交易码
																					// TRANSCODE
																					// 4
																					// 是
																					// 字符串
																					// 5311
		Cpu_creditForLoadInfor.confirmR_RTN_CODE = (String) map.get("RTN_CODE"); // 2
																					// 返回码
																					// RTN_CODE
																					// 6
																					// 是
																					// 字符串
																					// 成功:000000
																					// 其它：失败
		Cpu_creditForLoadInfor.confirmR_RTN_MSG = (String) map.get("RTN_MSG"); // 3
																				// 返回信息
																				// RTN_MSG
																				// 30
																				// 是
																				// 字符串
		Cpu_creditForLoadInfor.confirmR_BUSINESS_NO = (String) map.get("BUSINESS_NO"); // 4
																						// 业务流水号
																						// BUSINESS_NO
																						// 10
																						// 否
																						// 字符串
																						// 成功时返回。

		return map;
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

	@Override
	protected void onPause() {
		stopTask();
		super.onPause();
	}

	private void cancel() {
		try {
			YFApp.getApp().iService.cancel();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// stopTask();
		super.onDestroy();
	}

}
