package com.winksoft.yzsmk.cpu;

import com.winksoft.yzsmk.utils.ByteUtils;

public class Cpu_cardInfor {
	// 字节 数据元 长度（字节）
	public static byte[] file15_isuCompyFlag;// [8]; //1～8 发卡机构标识 8
	public static byte[] file15_appTypeFlag; // 9 应用类型标识 1
	public static byte[] file15_isuAppVer; // 10 发卡机构应用版本 1
	public static byte[] file15_appSerialNo;// [10]; //11～20 应用序列号 10
	public static byte[] file15_appStartDate;// [4]; //21～24 应用启用日期（YYYYMMDD） 4
	public static byte[] file15_appValidDate;// [4]; //25～28 应用有效日期（YYYYMMDD） 4
	public static byte[] file15_isuSelfDef;// [2]; //29～30 发卡机构自定义FCI数据 2

	// 字节 数据元 长度（字节）
	public static byte[] file16_cardTypeFlag; // 1 卡类型标识 1
	public static byte[] file16_ownFlag; // 2 本行职工标识 2
	public static byte[] file16_cardHoldername;// [20]; //3-22 持卡人姓名 3-22
	public static byte[] file16_cardHolderID;// [32]; //23-54 持卡人证件号码 23-54
	public static byte[] file16_cardHolderType; // 55 持卡人证件类型 55

	// 字节 数据元 长度（字节）
	public static byte[] file17_internatCode;// [4]; //1-4 国际代码 4
	public static byte[] file17_provinCode;// [2]; //5-6 省级代码 2
	public static byte[] file17_cityCode;// [2]; //7-8 城市代码 2
	public static byte[] file17_interflowType;// [2]; //9-10 互通卡种 2
	public static byte[] file17_cardType; // 11 卡种类型 1
	public static byte[] file17_reserved;// [49]; //12-60 预留 49
	
	public static byte[] debitInit_balance;// [4];			//余额 
	public static byte[] debitInit_cardOffSerialNo;// [2];	//cpu卡脱机交易序号 
	public static byte[] debitInit_overdraftLimit;// [3];	//透支限额 
	public static byte[] debitInit_skeyVer;				//密钥版本 
	public static byte[] debitInit_arithFlag;			//算法标识 
	public static byte[] debitInit_randNum;// [4];			//随机数 
	
	public static byte[] mac2_TAC;// [4];		//
	public static byte[] mac2_MAC2;// [4];		//使用用户卡中的消费密钥产生MAC2。 

	public Cpu_cardInfor(){
		// 字节 数据元 长度（字节）
		file15_isuCompyFlag = null;// [8]; //1～8 发卡机构标识 8
		file15_appTypeFlag = null; // 9 应用类型标识 1
		file15_isuAppVer = null; // 10 发卡机构应用版本 1
		file15_appSerialNo = null;// [10]; //11～20 应用序列号 10
		file15_appStartDate = null;// [4]; //21～24 应用启用日期（YYYYMMDD） 4
		file15_appValidDate = null;// [4]; //25～28 应用有效日期（YYYYMMDD） 4
		file15_isuSelfDef = null;// [2]; //29～30 发卡机构自定义FCI数据 2

		// 字节 数据元 长度（字节）
		file16_cardTypeFlag = null; // 1 卡类型标识 1
		file16_ownFlag = null; // 2 本行职工标识 2
		file16_cardHoldername = null;// [20]; //3-22 持卡人姓名 3-22
		file16_cardHolderID = null;// [32]; //23-54 持卡人证件号码 23-54
		file16_cardHolderType = null; // 55 持卡人证件类型 55

		// 字节 数据元 长度（字节）
		file17_internatCode = null;// [4]; //1-4 国际代码 4
		file17_provinCode = null;// [2]; //5-6 省级代码 2
		file17_cityCode = null;// [2]; //7-8 城市代码 2
		file17_interflowType = null;// [2]; //9-10 互通卡种 2
		file17_cardType = null; // 11 卡种类型 1
		file17_reserved = null;// [49]; //12-60 预留 49
		
		
		debitInit_balance = null;// [4];			//余额 
		debitInit_cardOffSerialNo = null;// [2];	//cpu卡脱机交易序号 
		debitInit_overdraftLimit = null;// [3];	//透支限额 
		debitInit_skeyVer = null;				//密钥版本 
		debitInit_arithFlag = null;			//算法标识 
		debitInit_randNum = null;// [4];			//随机数 
		
		mac2_TAC = null;// [4];		//
		mac2_MAC2 = null;// [4];		//使用用户卡中的消费密钥产生MAC2。 
	}
	
	public boolean setFile15(byte[] file15){
		boolean returnflag = false;
		if (file15.length == 30){
			int prt=0;
			file15_isuCompyFlag = ByteUtils.getBytes(file15,prt,8);prt += 8;// [8]; //1～8 发卡机构标识 8
			file15_appTypeFlag = ByteUtils.getBytes(file15,prt,1);prt += 1; // 9 应用类型标识 1
			file15_isuAppVer = ByteUtils.getBytes(file15,prt,1);prt += 1; // 10 发卡机构应用版本 1
			file15_appSerialNo = ByteUtils.getBytes(file15,prt,10);prt += 10;// [10]; //11～20 应用序列号 10
			file15_appStartDate = ByteUtils.getBytes(file15,prt,4);prt += 4;// [4]; //21～24 应用启用日期（YYYYMMDD） 4
			file15_appValidDate = ByteUtils.getBytes(file15,prt,4);prt += 4;// [4]; //25～28 应用有效日期（YYYYMMDD） 4
			file15_isuSelfDef = ByteUtils.getBytes(file15,prt,2);prt += 2;// [2]; //29～30 发卡机构自定义FCI数据 2
			returnflag = true;
		}
		return returnflag;	
	}
	
	public boolean setFile16(byte[] file16){
		boolean returnflag = false;
		if (file16.length == 55){
			int prt=0;
			file16_cardTypeFlag = ByteUtils.getBytes(file16,prt,1);prt += 1; // 1 卡类型标识 1
			file16_ownFlag = ByteUtils.getBytes(file16,prt,1);prt += 1; // 2 本行职工标识 2
			file16_cardHoldername = ByteUtils.getBytes(file16,prt,20);prt += 20;// [20]; //3-22 持卡人姓名 3-22
			file16_cardHolderID = ByteUtils.getBytes(file16,prt,32);prt += 32;// [32]; //23-54 持卡人证件号码 23-54
			file16_cardHolderType = ByteUtils.getBytes(file16,prt,1);prt += 1; // 55 持卡人证件类型 55
			returnflag = true;
		}
		return returnflag;	
	}
	
	public boolean setFile17(byte[] file17){
		boolean returnflag = false;
		if (file17.length == 60){
			int prt=0;
			file17_internatCode = ByteUtils.getBytes(file17,prt,4);prt += 4;// [4]; //1-4 国际代码 4
			file17_provinCode = ByteUtils.getBytes(file17,prt,2);prt += 2;// [2]; //5-6 省级代码 2
			file17_cityCode = ByteUtils.getBytes(file17,prt,2);prt += 2;// [2]; //7-8 城市代码 2
			file17_interflowType = ByteUtils.getBytes(file17,prt,2);prt += 2;// [2]; //9-10 互通卡种 2
			file17_cardType = ByteUtils.getBytes(file17,prt,1);prt += 1; // 11 卡种类型 1
			file17_reserved = ByteUtils.getBytes(file17,prt,49);prt += 49;// [49]; //12-60 预留 49
			returnflag = true;
		}
		return returnflag;	
	}

	public boolean setdebitInit(byte[] debitInit){
		boolean returnflag = false;
		if (debitInit.length == 15){
			int prt=0;
			debitInit_balance = ByteUtils.getBytes(debitInit,prt,4);prt += 4;// [4];			//余额 
			debitInit_cardOffSerialNo = ByteUtils.getBytes(debitInit,prt,2);prt += 2;// [2];	//cpu卡脱机交易序号 
			debitInit_overdraftLimit = ByteUtils.getBytes(debitInit,prt,3);prt += 3;// [3];	//透支限额 
			debitInit_skeyVer = ByteUtils.getBytes(debitInit,prt,1);prt += 1;				//密钥版本 
			debitInit_arithFlag = ByteUtils.getBytes(debitInit,prt,1);prt += 1;			//算法标识 
			debitInit_randNum = ByteUtils.getBytes(debitInit,prt,4);prt += 4;// [4];			//随机数 
			returnflag = true;
		}
		return returnflag;	
	}

	public boolean setMac2(byte[] mac2){
		boolean returnflag = false;
		if (mac2.length == 8){
			int prt=0;
			mac2_TAC = ByteUtils.getBytes(mac2,prt,4);prt += 4;// [4];		//
			mac2_MAC2 = ByteUtils.getBytes(mac2,prt,4);prt += 4;// [4];		//使用用户卡中的消费密钥产生MAC2。 
			returnflag = true;
		}
		return returnflag;	
	}
}
