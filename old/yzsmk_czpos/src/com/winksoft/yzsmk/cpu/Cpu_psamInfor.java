package com.winksoft.yzsmk.cpu;

import com.winksoft.yzsmk.utils.ByteUtils;

public class Cpu_psamInfor {
	// 字节 数据元 长度
	public static byte[] file15_psamSerialNo;// [10]; //1－10 PSAM 序列号 10
	public static byte[] file15_psamVerNum; // 11 PSAM 版本号 1
	public static byte[] file15_psamSkeyType; // 12 密钥卡类型 1
	public static byte[] file15_cardDefine;// [2]; //13－14 发卡方自定义 FCI 数据 2

	public static byte[] file16_terminalNum;// [6]; //终端机编号 6字节

	// 字节 数据元 长度
	public static byte[] file17_debitSkeyIndex; // 1 建设部消费密钥索引号 1
	public static byte[] file17_appSenderFlag;// [8]; //2-9 应用发行者标识 8
	public static byte[] file17_appReciverFlag;// [8]; //10－17 应用接收者标识 8
	public static byte[] file17_appStartData;// [4]; //18-21 应用启用日期 4
	public static byte[] file17_appValidDate;// [4]; //22-25 应用有效日期 4

	// mac1计算
	public static byte[] mac1_termiOffSerialno;// [4]; //终端脱机交易序号
	public static byte[] mac1_mac1;// [4]; //将密钥版本对应的消费主密钥进行分散产生消费子密钥，使用子密钥产生MAC1。
	
	public Cpu_psamInfor(){
		// 字节 数据元 长度
		file15_psamSerialNo=null;// [10]; //1－10 PSAM 序列号 10
		file15_psamVerNum=null; // 11 PSAM 版本号 1
		file15_psamSkeyType=null; // 12 密钥卡类型 1
		file15_cardDefine=null;// [2]; //13－14 发卡方自定义 FCI 数据 2

		file16_terminalNum=null;// [6]; //终端机编号 6字节

		// 字节 数据元 长度
		file17_debitSkeyIndex=null; // 1 建设部消费密钥索引号 1
		file17_appSenderFlag=null;// [8]; //2-9 应用发行者标识 8
		file17_appReciverFlag=null;// [8]; //10－17 应用接收者标识 8
		file17_appStartData=null;// [4]; //18-21 应用启用日期 4
		file17_appValidDate=null;// [4]; //22-25 应用有效日期 4

		// mac1计算
		mac1_termiOffSerialno=null;// [4]; //终端脱机交易序号
		mac1_mac1=null;// [4]; //将密钥版本对应的消费主密钥进行分散产生消费子密钥，使用子密钥产生MAC1。
	}
	
	public boolean setFile15(byte[] file15){
		boolean returnflag = false;
		if (file15.length == 14){
			int prt=0;
			file15_psamSerialNo = ByteUtils.getBytes(file15,prt,10);prt += 10;//1-10	PSAM序列号	10	cn
			file15_psamVerNum = ByteUtils.getBytes(file15,prt,1);prt += 1;//11	PSAM版本号	1	b
			file15_psamSkeyType = ByteUtils.getBytes(file15,prt,1);prt += 1;//12	密钥卡类型	1	b
			file15_cardDefine = ByteUtils.getBytes(file15,prt,2);prt += 2;	//[2];//13-14	发卡方自定义FCI数据	2	b
			returnflag = true;
		}
		return returnflag;	
	}
	
	public boolean setFile16(byte[] file16){
		boolean returnflag = false;
		if (file16.length == 6){
			file16_terminalNum = ByteUtils.getBytes(file16,0,6);// [6]; //终端机编号 6字节
			returnflag = true;
		}
		return returnflag;	
	}
	
	public boolean setFile17(byte[] file17){
		boolean returnflag = false;
		if (file17.length == 25){
			int prt=0;
			file17_debitSkeyIndex = ByteUtils.getBytes(file17,prt,1);prt += 1; // 1 建设部消费密钥索引号 1
			file17_appSenderFlag = ByteUtils.getBytes(file17,prt,8);prt += 8;// [8]; //2-9 应用发行者标识 8
			file17_appReciverFlag = ByteUtils.getBytes(file17,prt,8);prt += 8;// [8]; //10－17 应用接收者标识 8
			file17_appStartData = ByteUtils.getBytes(file17,prt,4);prt += 4;// [4]; //18-21 应用启用日期 4
			file17_appValidDate = ByteUtils.getBytes(file17,prt,4);prt += 4;// [4]; //22-25 应用有效日期 4
			returnflag = true;
		}
		return returnflag;	
	}

	public boolean setMac1(byte[] mac1){
		boolean returnflag = false;
		if (mac1.length == 8){
			int prt=0;
			mac1_termiOffSerialno = ByteUtils.getBytes(mac1,prt,4);prt += 4;// [4]; //终端脱机交易序号
			 mac1_mac1 = ByteUtils.getBytes(mac1,prt,4);prt += 4;// [4]; //将密钥版本对应的消费主密钥进行分散产生消费子密钥，使用子密钥产生MAC1。
			returnflag = true;
		}
		return returnflag;	
	}
}
