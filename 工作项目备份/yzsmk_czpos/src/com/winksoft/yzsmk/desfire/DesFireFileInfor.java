package com.winksoft.yzsmk.desfire;

import com.winksoft.yzsmk.utils.ByteUtils;

public class DesFireFileInfor {
	// 消费文件号
	public static byte decreaseFile;	// 消费文件号
	public static byte[] uid;	//
	public static byte[] sessionKey;	//
	//(01)充值信息文件 Value loading file   42字节 实际占用64
	public static byte[] file01_Valid_date;// = new byte[4];						//有效日期    BCD
	public static byte[] file01_Accumulative_loaded_value;// = new byte[4];			//累计充值金额     LSB UB
	public static byte[] file01_Latest_loading_authorization_code;// = new byte[7];  //最近充值授权码   BCD
	public static byte[] file01_Loading_counter;// = new byte[2]; 					//充值计数器       MSB UB
	public static byte[] file01_Latest_value_loading_agent;// = new byte[2];  		//最近充值代理商   BCD
	public static byte[] file01_Latest_loading_date_and_time;// = new byte[4];  	//最近充值时间     UTC  1970-1-1
	public static byte[] file01_RFU;// = new byte[2];	//  u8 RFU[2];
	public static byte[] file01_Card_Balance_after_latest_loading;// = new byte[4];  //最近充值后金额   LSB UB
	public static byte[] file01_Latest_loading_terminal_code;// = new byte[4];  	//最近充值终端号   BCD
	public static byte[] file01_Latest_loading_operator_code;// = new byte[4];  	//最近充值操作员号 BCD
	public static byte[] file01_Latest_Balance;// = new byte[4];   					//最近充值金额     LSB UB
	public static byte[] file01_Check_Code;  //校验

	//(02)钱包交易过程记录文件 Purse Linkage File
	public static byte[] file02_Transaction_Sequence_Number;// = new byte[4];  //钱包交易次数  MSB UB
	public static byte[] file02_Transaction_date_and_time;// = new byte[4];  //交日期时间    MSB UB  UTC
	public static byte[] file02_Application_ID;// = new byte[3];			 	 //交易应用ID    MSB UB
	public static byte[] file02_Sub_Application_ID;		 	 //子应用编号
	public static byte[] file02_RFU;// = new byte[3];					  	//保留
	public static byte[] file02_Check_Code;					  //校验


	//(03)钱包累计交易次数文件 Sequence Number File
	//(04)交易记录文件 Transaction records file
	  
	public static byte[] file04_City_code;// = new byte[2];	//城市代码
	public static byte[] file04_Terminal_code;// = new byte[4];	//终端代码
	public static byte[] file04_Transaction_date_and_time;// = new byte[4];	//交易日期时间(UTC time)
	public static byte[] file04_Transaction_type;	//交易类型
	public static byte[] file04_Transaction_value;// = new byte[3];	//交易金额
	public static byte[] file04_RFU;	//保留
	public static byte[] file04_Check_Code;	//校验
	
	//(05)黑名单标识文件 Blacklist Label file
	public static byte[] file05_Blacklist_label;	//黑名单标志 00-----正常卡  04----黑名单卡	  
	public static byte[] file05_Transaction_Sequence_Number;// = new byte[4];	//交易序列号  MSB UB	  
	public static byte[] file05_Date_and_time;// = new byte[4];	//交易日期时间(UTC time)	  
	public static byte[] file05_Company_ID;// = new byte[4];	//公司代码  BCD	  
	public static byte[] file05_Device_ID;// = new byte[4];	//设备号    BCD	  
	public static byte[] file05_RFU;	//保留	  
	public static byte[] file05_Check_Code;	//校验
	
	
	//(0D)持卡人信息文件 Card holder file
	public static byte[] file0D_Certificate_type;  //证件类型 UB
	public static byte[] file0D_Certificate_code;//[10];  //证件编号 BCD
	public static byte[] file0D_Reservation;//[5];  //保留     UB
	public static byte[] file0D_name;//[10];  //姓名
	public static byte[] file0D_sex;   //性别  31男  32 女
	public static byte[] file0D_date_of_birth;//[4];  //出生年月 BCD
	public static byte[] file0D_nation;  //民族

	
	//(0E)售卡信息文件 Card sale file
	public static byte[] file0E_Startup_date;//[4];  				//启用日期      BCD 
	public static byte[] file0E_Card_sale_operator_code;//[4];  	//售卡操作员号  BCD
	public static byte[] file0E_Value_loading_agent;//[2];  		//售卡充值代理商BCD
	public static byte[] file0E_Deposit_or_cost;  				//押金或者工本（元）
	public static byte[] file0E_overdraft_limit;//[4];  			//透支限额      MSB
	public static byte[] file0E_Station_equipment_code;//[4];		//售卡设备号    BCD
	public static byte[] file0E_Card_Type;  					//卡种
	public static byte[] file0E_RFU;//[3]; 						//保留
	public static byte[] file0E_Check_Code;  					//校验
	
	//扬州市民卡文件结构 
		// (0F)发卡方数据文件 Card issuer data file
	public static byte[] file0F_Version_number;//[2]; 			//版本                 MSB
	public static byte[] file0F_city_code;//[2];					//城市代码      BCD  
	public static byte[] file0F_Card_issuer_code;//[2];			//发卡方代码 BCD
	public static byte[] file0F_Card_internal_number;//[4]; 		//卡内号            MSB
	public static byte[] file0F_RFU; 						 	//保留
	public static byte[] file0F_Card_authentication_code;//[4];  	//卡认证码       MSB
	public static byte[] file0F_Issuing_date;//[4];				//发行日期       BCD
	public static byte[] file0F_Issuing_operator_code;//[2];  	//发卡操作员编号BCD
	public static byte[] file0F_Issuing_equipment_code;//[2];  	//发卡设备编号      BCD
	public static byte[] file0F_Card_engraved_number;//[8];   	//卡面号                       BCD
	public static byte[] file0F_Check_Code;  					//校验 
	
	public DesFireFileInfor(){
		decreaseFile = 0;
		uid = null;
		sessionKey = null;
		file01_Valid_date=null;// new byte[4];						//有效日期    BCD
		file01_Accumulative_loaded_value=null;// new byte[4];		//累计充值金额     LSB UB
		file01_Latest_loading_authorization_code=null;// new byte[7];  //最近充值授权码   BCD
		file01_Loading_counter=null;// new byte[2]; 				//充值计数器       MSB UB
		file01_Latest_value_loading_agent=null;// new byte[2];  		//最近充值代理商   BCD
		file01_Latest_loading_date_and_time=null;// new byte[4];  	//最近充值时间     UTC  1970-1-1
		file01_RFU=null;// new byte[2];	//  u8 RFU[2];
		file01_Card_Balance_after_latest_loading=null;// new byte[4];  //最近充值后金额   LSB UB
		file01_Latest_loading_terminal_code=null;// new byte[4];  //最近充值终端号   BCD
		file01_Latest_loading_operator_code=null;// new byte[4];  //最近充值操作员号 BCD
		file01_Latest_Balance=null;// new byte[4];   //最近充值金额     LSB UB
		file01_Check_Code=null;  //校验
		//(02)钱包交易过程记录文件 Purse Linkage File
		file02_Transaction_Sequence_Number=null;// new byte[4];  //钱包交易次数  MSB UB
		file02_Transaction_date_and_time=null;// new byte[4];  //交日期时间    MSB UB  UTC
		file02_Application_ID=null;// new byte[3];			 	 //交易应用ID    MSB UB
		file02_Sub_Application_ID=null;		 	 //子应用编号
		file02_RFU=null;// new byte[3];					  	//保留
		file02_Check_Code=null;					  //校验
		//(03)钱包累计交易次数文件 Sequence Number File
		//(04)交易记录文件 Transaction records file
		file04_City_code=null;// new byte[2];	//城市代码
		file04_Terminal_code=null;// new byte[4];	//终端代码
		file04_Transaction_date_and_time=null;// new byte[4];	//交易日期时间(UTC time)
		file04_Transaction_type=null;	//交易类型
		file04_Transaction_value=null;// new byte[3];	//交易金额
		file04_RFU=null;	//保留
		file04_Check_Code=null;	//校验
		//(05)黑名单标识文件 Blacklist Label file
		file05_Blacklist_label=null;	//黑名单标志 00-----正常卡  04----黑名单卡	  
		file05_Transaction_Sequence_Number=null;// new byte[4];	//交易序列号  MSB UB	  
		file05_Date_and_time=null;// new byte[4];	//交易日期时间(UTC time)	  
		file05_Company_ID=null;// new byte[4];	//公司代码  BCD	  
		file05_Device_ID=null;// new byte[4];	//设备号    BCD	  
		file05_RFU=null;	//保留	  
		file05_Check_Code=null;	//校验
		//(0D)持卡人信息文件 Card holder file
		file0D_Certificate_type=null;  //证件类型 UB
		file0D_Certificate_code=null;//[10];  //证件编号 BCD
		file0D_Reservation=null;//[5];  //保留     UB
		file0D_name=null;//[10];  //姓名
		file0D_sex=null;   //性别  31男  32 女
		file0D_date_of_birth=null;//[4];  //出生年月 BCD
		file0D_nation=null;  //民族
		//(0E)售卡信息文件 Card sale file
		file0E_Startup_date=null;//[4];  				//启用日期      BCD 
		file0E_Card_sale_operator_code=null;//[4];  	//售卡操作员号  BCD
		file0E_Value_loading_agent=null;//[2];  		//售卡充值代理商BCD
		file0E_Deposit_or_cost=null;  				//押金或者工本（元）
		file0E_overdraft_limit=null;//[4];  			//透支限额      MSB
		file0E_Station_equipment_code=null;//[4];		//售卡设备号    BCD
		file0E_Card_Type=null;  					//卡种
		file0E_RFU=null;//[3]; 						//保留
		file0E_Check_Code=null;  					//校验
		//扬州市民卡文件结构 
			// (0F)发卡方数据文件 Card issuer data file
		file0F_Version_number=null;//[2]; 			//版本                 MSB
		file0F_city_code=null;//[2];					//城市代码      BCD  
		file0F_Card_issuer_code=null;//[2];			//发卡方代码 BCD
		file0F_Card_internal_number=null;//[4]; 		//卡内号            MSB
		file0F_RFU=null; 						 	//保留
		file0F_Card_authentication_code=null;//[4];  	//卡认证码       MSB
		file0F_Issuing_date=null;//[4];				//发行日期       BCD
		file0F_Issuing_operator_code=null;//[2];  	//发卡操作员编号BCD
		file0F_Issuing_equipment_code=null;//[2];  	//发卡设备编号      BCD
		file0F_Card_engraved_number=null;//[8];   	//卡面号                       BCD
		file0F_Check_Code=null;  	
	}
	
	public boolean setFile01(byte[] file01){
		boolean returnflag = false;
		if ((file01.length == 42)&&((ByteUtils.genLRC(file01, 0, file01.length)^(byte)0x33) == 0)){
			int prt=0;
			file01_Valid_date = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];						//有效日期    BCD
			file01_Accumulative_loaded_value = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];		//累计充值金额     LSB UB
			file01_Latest_loading_authorization_code = ByteUtils.getBytes(file01,prt,7);prt += 7;// = new byte[7];  //最近充值授权码   BCD
			file01_Loading_counter = ByteUtils.getBytes(file01,prt,2);prt += 2;// = new byte[2]; 				//充值计数器       MSB UB
			file01_Latest_value_loading_agent = ByteUtils.getBytes(file01,prt,2);prt += 2;// = new byte[2];  		//最近充值代理商   BCD
			file01_Latest_loading_date_and_time = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];  	//最近充值时间     UTC  1970-1-1
			file01_RFU = ByteUtils.getBytes(file01,prt,2);prt += 2;// = new byte[2];	//  u8 RFU[2];
			file01_Card_Balance_after_latest_loading = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];  //最近充值后金额   LSB UB
			file01_Latest_loading_terminal_code = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];  //最近充值终端号   BCD
			file01_Latest_loading_operator_code = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];  //最近充值操作员号 BCD
			file01_Latest_Balance = ByteUtils.getBytes(file01,prt,4);prt += 4;// = new byte[4];   //最近充值金额     LSB UB
			file01_Check_Code = ByteUtils.getBytes(file01,prt,1);prt += 1;  //校验
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setFile02(byte[] file02){
		boolean returnflag = false;
		if ((file02.length == 16)&&((ByteUtils.genLRC(file02, 0, file02.length)^(byte)0x33) == 0)){
			int prt=0;
			file02_Transaction_Sequence_Number = ByteUtils.getBytes(file02,prt,4);prt += 4;// = new byte[4];  //钱包交易次数  MSB UB
			file02_Transaction_date_and_time = ByteUtils.getBytes(file02,prt,4);prt += 4;// = new byte[4];  //交日期时间    MSB UB  UTC
			file02_Application_ID = ByteUtils.getBytes(file02,prt,3);prt += 3;// = new byte[3];			 	 //交易应用ID    MSB UB
			file02_Sub_Application_ID = ByteUtils.getBytes(file02,prt,1);prt += 1;		 	 //子应用编号
			file02_RFU = ByteUtils.getBytes(file02,prt,3);prt += 3;// = new byte[3];					  	//保留
			file02_Check_Code = ByteUtils.getBytes(file02,prt,1);prt += 1;					  //校验
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setFile04(byte[] file04){
		boolean returnflag = false;
		if ((file04.length == 16)&&((ByteUtils.genLRC(file04, 0, file04.length)^(byte)0x33) == 0)){
			int prt=0;
			file04_City_code = ByteUtils.getBytes(file04,prt,2);prt += 2;// = new byte[2];	//城市代码
			file04_Terminal_code = ByteUtils.getBytes(file04,prt,4);prt += 4;// = new byte[4];	//终端代码
			file04_Transaction_date_and_time = ByteUtils.getBytes(file04,prt,4);prt += 4;// = new byte[4];	//交易日期时间(UTC time)
			file04_Transaction_type = ByteUtils.getBytes(file04,prt,1);prt += 1;	//交易类型
			file04_Transaction_value = ByteUtils.getBytes(file04,prt,3);prt += 3;// = new byte[3];	//交易金额
			file04_RFU = ByteUtils.getBytes(file04,prt,1);prt += 1;	//保留
			file04_Check_Code = ByteUtils.getBytes(file04,prt,1);prt += 1;	//校验
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setFile05(byte[] file05){
		boolean returnflag = false;
		if ((file05.length == 32)&&((ByteUtils.genLRC(file05, 0, file05.length)^(byte)0x33) == 0)){
			int prt=0;
			file05_Blacklist_label = ByteUtils.getBytes(file05,prt,1);prt += 1;	//黑名单标志 00-----正常卡  04----黑名单卡	  
			file05_Transaction_Sequence_Number = ByteUtils.getBytes(file05,prt,4);prt += 4;// = new byte[4];	//交易序列号  MSB UB	  
			file05_Date_and_time = ByteUtils.getBytes(file05,prt,4);prt += 4;// = new byte[4];	//交易日期时间(UTC time)	  
			file05_Company_ID = ByteUtils.getBytes(file05,prt,4);prt += 4;// = new byte[4];	//公司代码  BCD	  
			file05_Device_ID = ByteUtils.getBytes(file05,prt,4);prt += 4;// = new byte[4];	//设备号    BCD	  
			file05_RFU = ByteUtils.getBytes(file05,prt,4);prt += 4;	//保留	  
			file05_Check_Code = ByteUtils.getBytes(file05,prt,1);prt += 1;	//校验
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setFile0D(byte[] file0D){
		boolean returnflag = false;
		if (file0D.length == 16){
			int prt=0;
			file0D_Certificate_type = ByteUtils.getBytes(file0D,prt,1);prt += 1;  //证件类型 UB
			file0D_Certificate_code = ByteUtils.getBytes(file0D,prt,10);prt += 10;//[10];  //证件编号 BCD
			file0D_Reservation = ByteUtils.getBytes(file0D,prt,5);prt += 5;//[5];  //保留     UB
			file0D_name = ByteUtils.getBytes(file0D,prt,10);prt += 10;//[10];  //姓名
			file0D_sex = ByteUtils.getBytes(file0D,prt,1);prt += 1;   //性别  31男  32 女
			file0D_date_of_birth = ByteUtils.getBytes(file0D,prt,4);prt += 4;//[4];  //出生年月 BCD
			file0D_nation = ByteUtils.getBytes(file0D,prt,1);prt += 1;  //民族
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setFile0E(byte[] file0E){
		boolean returnflag = false;
		if ((file0E.length == 24)&&((ByteUtils.genLRC(file0E, 0, file0E.length)^(byte)0x33) == 0)){
			int prt=0;
			file0E_Startup_date = ByteUtils.getBytes(file0E,prt,4);prt += 4;//[4];  				//启用日期      BCD 
			file0E_Card_sale_operator_code = ByteUtils.getBytes(file0E,prt,4);prt += 4;//[4];  	//售卡操作员号  BCD
			file0E_Value_loading_agent = ByteUtils.getBytes(file0E,prt,2);prt += 2;//[2];  		//售卡充值代理商BCD
			file0E_Deposit_or_cost = ByteUtils.getBytes(file0E,prt,1);prt += 1;;  				//押金或者工本（元）
			file0E_overdraft_limit = ByteUtils.getBytes(file0E,prt,4);prt += 4;//[4];  			//透支限额      MSB
			file0E_Station_equipment_code = ByteUtils.getBytes(file0E,prt,4);prt += 4;//[4];		//售卡设备号    BCD
			file0E_Card_Type = ByteUtils.getBytes(file0E,prt,1);prt += 1;  					//卡种
			file0E_RFU = ByteUtils.getBytes(file0E,prt,3);prt += 3;//[3]; 						//保留
			file0E_Check_Code = ByteUtils.getBytes(file0E,prt,1);prt += 1;  					//校验
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setFile0F(byte[] file0F){
		boolean returnflag = false;
		if ((file0F.length == 32)&&((ByteUtils.genLRC(file0F, 0, file0F.length)^(byte)0x33) == 0)){
			int prt=0;
			file0F_Version_number = ByteUtils.getBytes(file0F,prt,2);prt += 2;//[2]; 			//版本                 MSB
			file0F_city_code = ByteUtils.getBytes(file0F,prt,2);prt += 2;//[2];					//城市代码      BCD  
			file0F_Card_issuer_code = ByteUtils.getBytes(file0F,prt,2);prt += 2;//[2];			//发卡方代码 BCD
			file0F_Card_internal_number = ByteUtils.getBytes(file0F,prt,4);prt += 4;//[4]; 		//卡内号            MSB
			file0F_RFU = ByteUtils.getBytes(file0F,prt,1);prt += 1; 						 	//保留
			file0F_Card_authentication_code = ByteUtils.getBytes(file0F,prt,4);prt += 4;//[4];  	//卡认证码       MSB
			file0F_Issuing_date = ByteUtils.getBytes(file0F,prt,4);prt += 4;//[4];				//发行日期       BCD
			file0F_Issuing_operator_code = ByteUtils.getBytes(file0F,prt,2);prt += 2;//[2];  	//发卡操作员编号BCD
			file0F_Issuing_equipment_code = ByteUtils.getBytes(file0F,prt,2);prt += 2;//[2];  	//发卡设备编号      BCD
			file0F_Card_engraved_number = ByteUtils.getBytes(file0F,prt,8);prt += 8;//[8];   	//卡面号                       BCD
			file0F_Check_Code = ByteUtils.getBytes(file0F,prt,1);prt += 1;  					//校验 
			returnflag = true;
		}
		return returnflag;
	}
	public boolean setDecreaseFile(){
		boolean returnflag = false;
		if (file0E_Card_Type != null){
			if (file0E_Card_Type[0] == 0x65){
				decreaseFile = 0x00;
			}
			else{
				decreaseFile = 0x06;				
			}
				
		}
		return returnflag;
	}
	
	public boolean setUID(byte[] buid){
		boolean returnflag = false;
//		if (buid.length == 7){
			uid = buid;
//		}
		return returnflag;
	}
}
