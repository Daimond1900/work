package com.winksoft.yzsmk.solab.iso8583.yzsmk;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.com.senter.sdkdefault.mediator.impl.v;

import com.winksoft.yzsmk.cpu.Cpu_psamInfor;
import com.winksoft.yzsmk.desfire.Consumption;
import com.winksoft.yzsmk.desfire.desfire_tradingInfor;
import com.winksoft.yzsmk.link.PosLinker;
import com.winksoft.yzsmk.link.TransInforDesfire;
import com.winksoft.yzsmk.solab.iso8583.IsoMessage;
import com.winksoft.yzsmk.solab.iso8583.IsoType;
import com.winksoft.yzsmk.solab.iso8583.MessageFactory;
import com.winksoft.yzsmk.solab.iso8583.parse.ConfigParser;
//import com.yifeng.mpos.R;
//import com.winksoft.yzsmk.yfposdemo.activity.TerminalActivity;
//import com.winksoft.yzsmk.yfposdemo12.R;
import com.winksoft.yzsmk.utils.ByteUtils;

//import com.yifeng.mpos.MainActivity.socketThread;

//import com.yifeng.mpos.MainActivity.socketThread;

public class PosFuntion {
	Context m_context;

	public static byte[] sendBuf = new byte[1024];
	private static int sendBufLen = 0;

	public static byte[] getSendBuf() {
		final byte[] data = new byte[sendBufLen];
		for (int i=0;i<sendBufLen;i++)
			data[i] = sendBuf[i];
		return data;
	}

	public static void setSendBuf(byte[] sendBuf) {
		PosFuntion.sendBuf[0] = (byte)((sendBuf.length>>8)&0xff);
		PosFuntion.sendBuf[1] = (byte)(sendBuf.length&0xff);
		for (int i=0;i<sendBuf.length;i++)
			PosFuntion.sendBuf[i+2] = sendBuf[i];
		sendBufLen = sendBuf.length+2;
	}

	public PosFuntion(Context context) {
		m_context = context;
	}

	public void getFunction() {

	}

	public boolean someFunction(IsoMessage m,int tradeType) {
		try {
			if (tradeType == PosTradeType.TYPE_SIGN_IN_U){
				m.setValue(3, 920000,
						IsoType.NUMERIC, 6);	// 处理码
			}else if (tradeType == PosTradeType.TYPE_OFF_LINEREPORT_U){
				m.setValue(3, 960000,
						IsoType.NUMERIC, 6);	// 处理码
			}else if (tradeType == PosTradeType.TYPE_ROCREPORT_U){
				m.setValue(3, 180000,
						IsoType.NUMERIC, 6);	// 处理码
			}else if (tradeType == PosTradeType.TYPE_LIQUIDATE_CIC_U){
				m.setValue(3, 960000,
						IsoType.NUMERIC, 6);	// 处理码
			}
			m.setValue(11, ByteUtils.byteToInt_long(TransInforDesfire.trace_no_11),
						IsoType.NUMERIC, 6); // 交易流水号 从STM32获取
			if (tradeType == PosTradeType.TYPE_SIGN_IN_U){
				m.setValue(32, ByteUtils.printBytes(TransInforDesfire.version_32), IsoType.BINARY, 8); 
			}
			m.setValue(41, ByteUtils.printBytes(TransInforDesfire.terminal_no_41), IsoType.BINARY, 8); // 从
																			// STM32获取
			m.setValue(42, ByteUtils.printBytes(TransInforDesfire.merchant_no_42), IsoType.BINARY, 15); // 从STM32获取

			m.setValue(60,ByteUtils.printBytes(TransInforDesfire.opration_no), IsoType.LLBCD, 4); // ????

		} catch (Exception e) {
			e.printStackTrace();
			// return false;
		}
		return false;
	}
	
	public byte receiveParse(PosLinker link,IsoMessage m,TransInforDesfire transInforDesfire) throws UnsupportedEncodingException {
		
		if (m.getType() != TransInforDesfire.tradeType+(byte)0x10){
			return 0;
		}
		if (m.getObjectValue(39).equals("00") == false){
			return 0;
		}else{
			if (m.getType() == 0){
				;
			}
				
		}
		
		// 检测批次号是否正确
		// 检测流水号是否正确
		// 检测应用版本号是否正确
		// 检测终端号、商户号是否正确
		switch (m.getType()){
		case PosTradeType.TYPE_SIGN_IN_D://	签到
			link.sendPackage((byte)0x83,null);// 发送 83应答包
			byte[] value59 = m.getObjectValue(59);
			String merchantName = ByteUtils.printBytes(value59);			
			String key_62 = m.getObjectValue(62);
			byte[] keyPIn = new byte[8];
			byte[] macPIn = new byte[8];
			System.arraycopy(key_62.getBytes(), 0, keyPIn, 0, keyPIn.length);
			System.arraycopy(key_62.getBytes(), 8, macPIn, 0, macPIn.length);
			transInforDesfire.setMerchant_name_59(merchantName);// 保存59域
			transInforDesfire.setPin_key_62(keyPIn);// 保存62域中的PIN和MAC密钥
			transInforDesfire.setMac_key_62(macPIn);
			
			link.close();	// 挂断
			break;
			
		case PosTradeType.TYPE_OFF_LINEREPORT_D: // 离线上送
			byte finishFlag = 0;	// 0 未完成上送，继续
			// 有是否还有未上送数据
			if (finishFlag == 0){
				
				transInforDesfire.setTradeType(PosTradeType.TYPE_OFF_LINEREPORT_U);
				Consumption.m = Consumption.mfact.newMessage(TransInforDesfire.tradeType);
				this.setPackeFunction(Consumption.m,Consumption.mfact,PosTradeType.TYPE_OFF_LINEREPORT_U);
//				link.linker();
			}else{
				desfire_tradingInfor.setUpdata_mode((byte)0x01);
				transInforDesfire.setTradeType(PosTradeType.TYPE_LIQUIDATE_CIC_U);
				Consumption.m = Consumption.mfact.newMessage(TransInforDesfire.tradeType);
				this.setPackeFunction(Consumption.m,Consumption.mfact,PosTradeType.TYPE_LIQUIDATE_CIC_U);
			}
			link.sendPackage((byte)0x87,PosFuntion.sendBuf);
			break;

		case PosTradeType.TYPE_ROCREPORT_D: // 批上送
			byte finishFlag1 = 0;	// 0 未完成上送，继续
			// 有是否还有未上送数据
			if (finishFlag1 == 0){

				Consumption.m = Consumption.mfact.newMessage(TransInforDesfire.tradeType);
				
				transInforDesfire.setTradeType(PosTradeType.TYPE_ROCREPORT_U);
				Consumption.m = Consumption.mfact.newMessage(TransInforDesfire.tradeType);
				this.setPackeFunction(Consumption.m,Consumption.mfact,PosTradeType.TYPE_ROCREPORT_U);
//				link.linker();
			}else{
				desfire_tradingInfor.setUpdata_mode((byte)0x02);
				transInforDesfire.setTradeType(PosTradeType.TYPE_LIQUIDATE_CIC_U);
				Consumption.m = Consumption.mfact.newMessage(TransInforDesfire.tradeType);
				this.setPackeFunction(Consumption.m,Consumption.mfact,PosTradeType.TYPE_LIQUIDATE_CIC_U);
			}
			link.sendPackage((byte)0x87,PosFuntion.sendBuf);
			break;

		case PosTradeType.TYPE_LIQUIDATE_CIC_D: // 结算
			link.close();	// 挂断
			// 清除数据库记录
			break;
		}
		
		
//		byte[] bitmap = new byte[8];
//	//	System.out.println(m.getIsoHeader().getBytes("gb2312").toString());
//		System.out.println("TYPE: " + Integer.toHexString(m.getType()));
//		for (int i = 2; i < 64; i++) {
//			if (m.hasField(i)) {
//				System.out.println("F " + i + "(" + m.getField(i).getType() + "): " + m.getObjectValue(i) + " -> '"
//					+ m.getField(i).toString() + "'");
//			}
//		}
		return 1;
	}
	
	
	public boolean setPackeFunction(IsoMessage m,MessageFactory<IsoMessage> mfact,int tradeType) {
		
//		m = mfact.newMessage(TransInforDesfire.tradeType);
		m.setBinary(true);
		mfact.setUseBinaryBitmap(true);
		mfact.setUseBinaryMessages(true);
		this.someFunction(m, TransInforDesfire.tradeType);
		
		switch (tradeType){
			case PosTradeType.TYPE_SIGN_IN_U:
				try {
					print(m);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PosFuntion.setSendBuf(m.writeData());
				break;
			case PosTradeType.TYPE_OFF_LINEREPORT_U:	
				m.setValue(9, ByteUtils.byteToInt_long(TransInforDesfire.batch_no_9),IsoType.NUMERIC, 6); // 交易批次号 从STM32获取
				// 模拟数据  总条数和实际上送的条数
//				byte[] tmp = TradingInfor.getConsumeInforPackage(17,1);
				byte[] tmp = desfire_tradingInfor.getConsumeInforPackage(0);
				byte[] tmp1 = desfire_tradingInfor.getConsumeRecord(0);
				m.setValue(45,ByteUtils.printBytes(tmp), IsoType.LLBIN, tmp.length);
				m.setValue(63,ByteUtils.printBytes(tmp1), IsoType.LLLBIN, tmp1.length);
				
				byte[] tmp2 = {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07};
				m.setValue(64, ByteUtils.printBytes(tmp2), IsoType.BINARY, 8); 
				

				try {
					print(m);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				PosFuntion.setSendBuf(m.writeData());
				break;
			case PosTradeType.TYPE_ROCREPORT_U:
				m.setValue(9, ByteUtils.byteToInt_long(TransInforDesfire.batch_no_9),IsoType.NUMERIC, 6); // 交易批次号 从STM32获取
				byte[] tmp8 = {(byte)0x01,(byte)0x00};		// 常量代表是  消费
				m.setValue(18, ByteUtils.printBytes(tmp8),IsoType.NUMERIC, 4); // 交易批次号 从STM32获取
				// 模拟数据  总条数和实际上送的条数
//				byte[] tmp3 = {(byte)0x00,(byte)0x00,(byte)0x17,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
//				byte[] tmp4 = {(byte)0x00,(byte)0x00,(byte)0x81,(byte)0x51,(byte)0x22,(byte)0x50,(byte)0x03,(byte)0x94,(byte)0x08,(byte)0x89,(byte)0x04,(byte)0x65,(byte)0x00,(byte)0x20,(byte)0x16,(byte)0x09,(byte)0x18,(byte)0x17,(byte)0x28,(byte)0x18,(byte)0x02,(byte)0x08,(byte)0x50,(byte)0x02,(byte)0x0c,(byte)0x15,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x0b,(byte)0x70,(byte)0x20,(byte)0x16,(byte)0x09,(byte)0x18,(byte)0x17,(byte)0x23,(byte)0x16,(byte)0x31,(byte)0xe6,(byte)0xf2,(byte)0x8a,(byte)0x96,(byte)0x16,(byte)0x90,(byte)0x07,(byte)0x60,(byte)0x03,(byte)0x31,(byte)0x61,(byte)0x00,(byte)0x03,(byte)0xdc};
				
				byte[] tmp3 = desfire_tradingInfor.getConsumeInforPackage(1);
				byte[] tmp4 = desfire_tradingInfor.getConsumeRecord(1);
				m.setValue(45,ByteUtils.printBytes(tmp3), IsoType.LLBIN, tmp3.length);
				m.setValue(63,ByteUtils.printBytes(tmp4), IsoType.LLLBIN, tmp4.length);
				
				byte[] tmp5 = {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07};
				m.setValue(64, ByteUtils.printBytes(tmp5), IsoType.BINARY, 8); 
				

				try {
					print(m);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				PosFuntion.setSendBuf(m.writeData());
				break;	
			case PosTradeType.TYPE_LIQUIDATE_CIC_U:
				m.setValue(9, ByteUtils.byteToInt_long(TransInforDesfire.batch_no_9),IsoType.NUMERIC, 6); // 交易批次号 从STM32获取
				byte[] tmp11 = {(byte)0x01,(byte)0x00};		// 常量代表是  消费
				m.setValue(18, ByteUtils.printBytes(tmp11),IsoType.NUMERIC, 4); // 交易批次号 从STM32获取
				
				byte[] tmp12 = desfire_tradingInfor.getConsume_Lauq_conut();
				m.setValue(27, ByteUtils.byteToInt_long(tmp12),IsoType.NUMERIC, 2); // 交易批次号 从STM32获取
				
				byte[] tmp13 = desfire_tradingInfor.getConsume_Lauq();
				m.setValue(45,ByteUtils.printBytes(tmp13), IsoType.LLBIN, tmp13.length);
				
				byte[] tmp14 = {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07};
				m.setValue(64, ByteUtils.printBytes(tmp14), IsoType.BINARY, 8); 
				PosFuntion.setSendBuf(m.writeData());
				break;	
		}
		return false;
	}
	
	
	
	
	
	
	public void print(IsoMessage m) throws UnsupportedEncodingException {
		byte[] bitmap = new byte[8];
//		System.out.println(m.getIsoHeader().getBytes("gb2312").toString());
		System.out.println("TYPE: " + Integer.toHexString(m.getType()));
		for (int i = 2; i <= 64; i++) {
			if (m.hasField(i)) {
				System.out.println("F " + i + "(" + m.getField(i).getType() + "): " + m.getObjectValue(i) + " -> '"
					+ m.getField(i).toString() + "'");
			}
		}
	}
}
