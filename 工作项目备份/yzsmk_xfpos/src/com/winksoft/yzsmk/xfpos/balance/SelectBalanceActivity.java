package com.winksoft.yzsmk.xfpos.balance;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.winksoft.yzsmk.desfire.DesFireFileInfor;
import com.winksoft.yzsmk.desfire.PsamInfor;
import com.winksoft.yzsmk.desfire.desfire_tradingInfor;
import com.winksoft.yzsmk.utils.ByteUtils;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.TicketResult;
import com.winksoft.yzsmk.xfpos.YFApp;
import com.winksoft.yzsmk.xfpos.YzsmkConst;
import com.winksoft.yzsmk.xfpos.R.drawable;
import com.winksoft.yzsmk.xfpos.R.id;
import com.winksoft.yzsmk.xfpos.R.layout;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description: 余额查询
 */
public class SelectBalanceActivity extends BaseActivity {

	public final byte[] CITY_CODE = new byte[] { (byte) 0x22, (byte) 0x50 };
	private ImageView imgResult;
	private TextView txCardType,txBalance,tvResult;
	private CheckTicketTask task; // 检票任务
	private String test_balance; // 余额


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectbalance);
		setTitle("余额查询");
		this.tvResult = (TextView) super.findViewById(R.id.tvResult);
		this.txCardType = (TextView) super.findViewById(R.id.txCardType);
		this.txBalance = (TextView) super.findViewById(R.id.txBalance);
		this.imgResult = (ImageView) super.findViewById(R.id.imgResult);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(task == null){
			task = new CheckTicketTask();
			task.execute();
		}
	}
	
	/** 检票任务 */
	private class CheckTicketTask extends AsyncTask<Void, Object, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			while (!isCancelled()) {
				try { Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				byte[] atr = null;
				byte[] cpuID=null;
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
							// CPU 卡 的执行任务
							re = cpuYuanlinCheck1(cpuID);
						} else if (atr[0] == YzsmkConst.CARD_DESFIRE) { // DESFIRE卡片处理
							cpuID = ByteUtils.getBytes(atr, 2, atr.length - 2);
							// Desfire 卡 的执行任务
							re = desfireYanlinCheck1(cpuID);
						} else if (atr[0] == YzsmkConst.CARD_TIMEOUT) {
							re = TicketResult.E_FINDTIMEOUT;
						} else {
							re = TicketResult.E_FIND;
						}
					}

					this.publishProgress(YzsmkConst.P_UPDATE_INFO,re);
					try { Thread.sleep(3000);} catch (InterruptedException e) {}
				}catch (CardException e ){
					this.publishProgress(YzsmkConst.P_ERROR, e);
					try { Thread.sleep(1500);} catch (InterruptedException e1) {}
				}
				catch (Exception e) {
					this.publishProgress(YzsmkConst.P_ERROR, e);
					try { Thread.sleep(1500);} catch (InterruptedException e1) {}
				}

				this.publishProgress(YzsmkConst.P_TICKET_END);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			int step = (Integer) values[0];
			switch (step) {
			case YzsmkConst.P_TICKET_BEGIN:
//				tvResult.setText("开始检票...");
				reset();
				break;
			case YzsmkConst.P_FIND_CARD:
				tvResult.setText("正在寻卡...");
				break;
			case YzsmkConst.P_READ_INFO:
				tvResult.setText("正在读取信息...");
				break;
			case YzsmkConst.P_UPDATE_INFO:
				TicketResult result = (TicketResult)values[1];
				if(result.equals(TicketResult.OK)){
					txBalance.setText(test_balance + "元");
					tvResult.setText("查询成功");
					imgResult.setImageResource(R.drawable.jptg);
				}else{
					tvResult.setText("查询失败");
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
	
	/** 检票结果 
	 *  return ok 通过检票
	 * */
	@SuppressLint("SimpleDateFormat")
	private TicketResult desfireYanlinCheck1(byte[] cpuID)
			throws Exception {
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
			}

		};

		// 1、所有参数初始化
		DesFireFileInfor desFireFileInfor = new DesFireFileInfor();
		PsamInfor psamInfor = new PsamInfor();
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
		tag.selectApplication(new byte[] { (byte) 0x01, (byte) 0x00,
				(byte) 0x00 }); // 2)选择目录
		tmp = tag.readData((byte) 0x01, 0x00, 0x2A); // 3)读取 01文件
		Log.i("desfire---4", tmp.toString());
		desFireFileInfor.setFile01(tmp);
		tmp = tag.readData((byte) 0x05, 0x00, 0x20); // 4)读取 05文件
		Log.i("desfire---5", tmp.toString());
		desFireFileInfor.setFile05(tmp);
		tmp = tag.readData((byte) 0x0E, 0x00, 0x18); // 5)读取 0e文件
		Log.i("desfire---6", tmp.toString());
		desFireFileInfor.setFile0E(tmp);
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
		DesFireFileInfor.sessionKey = tag.authenticate((byte) 0x01,
				subKeyBuf.array()); // 双向认证
									// 并获得会话密钥
		// 5 、读取余额
		desFireFileInfor.setDecreaseFile(); // 判断并设置消费文件 00 或 06
		int balance = tag.getValue(DesFireFileInfor.decreaseFile,
				DesFireFileInfor.sessionKey);

		// ---------------test----------------------------------------
		test_balance = ByteUtils.getMoney(balance);

		return TicketResult.OK;
	}
	
	/** 对desfire卡的校验*/
	@SuppressLint("SimpleDateFormat")
	private boolean isDesfireValid(desfire_tradingInfor tradingInfor, int balance) {
		// 2、判断城市代码是否正确
		if ((DesFireFileInfor.file0F_city_code[0] != CITY_CODE[0])
				|| (DesFireFileInfor.file0F_city_code[1] != CITY_CODE[1]))
			return false;
		// 3、获取保存时间，并判断卡是否在有效期内-----------------------------------------
		tradingInfor.setRecordTime(System.currentTimeMillis()); // 保存交易记录时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		byte[] time = ByteUtils.hexToByte(str); // str 转 HEX
		tradingInfor.setCrade_date(time);// 保存年月日 时分秒
		int nowtime = ByteUtils.getInt(ByteUtils.getBytes(time, 0, 4), 0);
		int statetime = ByteUtils.getInt(DesFireFileInfor.file0E_Startup_date,
				0);
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

	
	// ///////////////////////////////////////////////////////////////////////////
	
	private TicketResult cpuYuanlinCheck1(byte[] cpuID)
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
		tmp = YFApp.getApp().iService.psamResetEx(1, 2); // psam卡复位 YFApp.getApp().iService.psamResetEx(2, 2);
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
		tmp = tag.selectFileByAID(new byte[] { (byte) 0xA0, (byte) 0x00,
				(byte) 0x00, (byte) 0x06, (byte) 0x32, (byte) 0x01,
				(byte) 0x01, (byte) 0x05 }); // 选择AID 电子钱包 AID
		tmp = tag.getBinaryFile(true, (byte) 0x15, 0, (byte) 0x1E); // 读取 0x15文件
		cpu_cardInfor.setFile15(tmp); // 保存CPU卡 0x15文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x16, 0, (byte) 0x37); // 读取 0x16文件
		cpu_cardInfor.setFile16(tmp); // 保存CPU卡 0x16文件信息
		tmp = tag.getBinaryFile(true, (byte) 0x17, 0, (byte) 0x3C); // 读取 0x17文件
		cpu_cardInfor.setFile17(tmp); // 保存CPU卡 0x17文件信息

		// 4、读取余额
		int balance = tag.getBalance();

		// ---------------test----------------------------------------
		test_balance = ByteUtils.getMoney(balance);

		return TicketResult.OK;
	}
	
	@SuppressLint("SimpleDateFormat")
	private boolean isCpuValid(Cpu_tradingInfor cpu_tradingInfor, int balance) {
		// 2、获取并保存当前时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		byte[] time = ByteUtils.hexToByte(str); // str 转 HEX
		Cpu_tradingInfor.setCpu_termiTradeDate(ByteUtils.getBytes(time, 0, 4));// 保存年月日
																				// ？？？？？？
		Cpu_tradingInfor.setCpu_termiTradeTime(ByteUtils.getBytes(time, 4, 3));// 保存时分秒
		int nowtime = ByteUtils.getInt(Cpu_tradingInfor.cpu_termiTradeDate, 0,
				true);
		int statetime = ByteUtils.getInt(Cpu_cardInfor.file15_appStartDate, 0,
				true);
		int endtime = ByteUtils.getInt(Cpu_cardInfor.file15_appValidDate, 0,
				true);

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
	
	@Override
    protected void onPause() {
		stopTask();
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
//    	stopTask();
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
		txCardType.setText("");
		txBalance.setText("");
		tvResult.setText("");
		imgResult.setImageResource(R.drawable.jpz);
	}
}
