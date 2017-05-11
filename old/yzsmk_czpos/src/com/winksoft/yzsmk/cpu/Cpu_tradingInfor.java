package com.winksoft.yzsmk.cpu;

public class Cpu_tradingInfor {
	  //名称定义            //位置	长度	  格式	内容	      说明	条件
	  public static byte[] cpu_localNo;//[4];       //  n	    本地流水号	本地自定义流水号	M
	  public static byte[] cpu_cardSerialNo;//[10];  //  20	  an	卡片序列号	卡主账号，不足位数以空格填充	M
	  //public static byte[] cpu_mainAccountNo[19]; //32	   19	  n	    主账号	    向左对齐，不足19位时不足部分补空格（右补空格）。-乘客卡号	M
	  //public static byte[] cpu_cardAccNo[16];     //51	   16	  H	    卡内号	    卡主账号后16位	M
	  public static byte[] cpu_cardType;      //67	   3	  n	    卡种	01 普通卡；02 学生卡；03老年卡；05  拥军卡；06 离休卡；
	  //07 优抚卡；08 残疾人卡；09  敬老卡；10  异形卡；11 纪念卡	M
	  public static byte[] cpu_recordStatus;      //70	   1	  n	    记录类型	0：正常交易 1：黑卡交易	M
	  public static byte[] cpu_beforeBalance;//[4];  	//71	   8	  H	    交易前余额	消费前卡余额	M
	  public static byte[] cpu_trandAmount;//[4];    	//79	   8	  H	    *交易金额	用8个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。	M
	  public static byte[] cpu_afterBalance;//[4];   	//87	   8	  H	    交易余额	用8个可见的16进制字符（0～9，A～F）表示；
	  //电子钱包的消费，本域后补两个F。
	  //若无法填写用缺省值空格填充。此字段表示卡片消费后余额	M
	  public static byte[] cpu_tradeType;      		//95	   2	  n	    *交易类型标识	交易性质 06-表示电子钱包脱机消费；09-复合消费的类型。	M
	  //public static byte[] cpu_collectNum[8];     	//97	   8	  n	    采集点编号	运营系统的公司采集点序号。	0
	  public static byte[] cpu_terminalNum;//[4];		//实际保存4字节bcd码  105	   12	  n	    终端机编号	POS机编号	M
	  public static byte[] cpu_psamCardNum;//[6]; 		//117	   12	  n	    *PSAM卡号	PSAM卡终端机编号	M
	  public static byte[] cpu_psamSerialNO;//[4];		//129	   9	  n	    *PSAM卡流水号	PSAM卡交易唯一流水号	M
	  public static byte[] cpu_lockCardFlag;    		//138	   1	  n	    锁卡交易标志	0为正常交易；1为锁卡交易。统一为0，但发现黑名单卡要进行锁卡。	M
	  public static byte[] cpu_termiTradeNo;//[4]; 		//139	   8	  H	    终端交易序号	用8个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。指：POS交易流水号	M
	  public static byte[] cpu_termiTradeDate;//[4];		//147	   8	  n	    *终端交易日期	格式为YYYYMMDD	M
	  public static byte[] cpu_termiTradeTime;//[3];		//155	   6	  n	    *终端交易时间	格式为hhmmss	M
	  public static byte[] cpu_tradeTAC;//[4];   		//161	   8	  H	    *交易验证代码（TAC）	用8个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。	M
	  public static byte[] cpu_debitSKeyVer;   		//169	   2	  H	    消费密钥版本号	用2个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。	M
	  public static byte[] cpu_debitSKeyIndex;   		//171	   2	  H	    消费密钥索引	用2个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。	M
	  public static byte[] cpu_cardOffSerialNo;//[2];	//卡片返回的 就是 两个字节//173   4 H	卡片脱机交易序列号	用4个可见的16进制字符（0～9，A～F）表示；
	  //若无法填写用缺省值空格填充。指卡消费计数器。	M
	  public static byte[] cpu_IssuingCompany;//[8]; 	//177	   16	  n	    发卡机构标识	卡主账号前八位	M
	  public static byte[] cpu_cityCode_L;//[2];  		//193	   4	  n	    *城市代码（交易地）	扬州城市代码：3120	M
	  public static byte[] cpu_cityCode_C;//[2];  		//197	   4	  n	    *城市代码（卡属地）	卡片读取	M
	  public static byte[] cpu_operaCompany;//[8]; 		//201	   8	  n	    运营单位代码	商户编号	M
	  //public static byte[] cpu_ownlineID[20];       	//209	   20	  ans	  所属线路	线路编号(公交必备)	0
	  public static byte[] cpu_tradeCounterID;//[8];   	//229	   8	  n	    交易柜台编号	公交为：公交车辆编号	M
	  //public static byte[] cpu_driverCardNo[8];        	//237	   8	  n	    司机卡号	司机卡面号(公交必备)	0
	  //public static byte[] cpu_driverWorkTime[14];     	//245	   14	  n	    司机上班时间	上班时间:YYYYMMDDhhmmss(公交必备)	0
	  public static byte[] cpu_professionCode;    	//259	   2	  n	    行业代码	01-公交，02-出租，03-地铁，04-有轨电车，05-公共自行车，06-轮渡，07-小额消费，08-停车场	M
	  public static byte[] cpu_debitType;        	 	//261	   1	  n	    消费类型	0 普通 2 本地赠送钱包 8换乘(公交必备)	0
	  //public static byte[] cpu_collectTime[14];   	//262	   14	  n	    采集时间	YYYYMMDDhhmmss必须是北京时间(公交必备)	0
	  public static byte[] cpu_testFlag;        		//276	   1	  n	    测试标志	0为正式数据；1为测试数据	M
	  public static byte[] cpu_randNum;//[4];      		//277	   8	  H	    伪随机数	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	O
	  //public static byte[] cpu_reserved[30];   //285	   30	  ans	  保留使用		
	  //public static byte[] cpu_rtnSign;        //315	   1	  s	    回车符	回车符’\n’	M
	  //注：M-必备，O-可选。*为TAC校验必需项。  若无法填写用缺省值空格填充。
	  
	  public Cpu_tradingInfor(){
		  //名称定义            //位置	长度	  格式	内容	      说明	条件
		  cpu_localNo = null;//[4];       //  n	    本地流水号	本地自定义流水号	M
		  cpu_cardSerialNo = null;//[10];  //  20	  an	卡片序列号	卡主账号，不足位数以空格填充	M
		  cpu_cardType = null;      //67	   3	  n	    卡种	01 普通卡；02 学生卡；03老年卡；05  拥军卡；06 离休卡；
		  cpu_recordStatus = null;      //70	   1	  n	    记录类型	0：正常交易 1：黑卡交易	M
		  cpu_beforeBalance = null;//[4];  	//71	   8	  H	    交易前余额	消费前卡余额	M
		  cpu_trandAmount = null;//[4];    	//79	   8	  H	    *交易金额	用8个可见的16进制字符（0～9，A～F）表示；
		  cpu_afterBalance = null;//[4];   	//87	   8	  H	    交易余额	用8个可见的16进制字符（0～9，A～F）表示；
		  //电子钱包的消费，本域后补两个F。
		  //若无法填写用缺省值空格填充。此字段表示卡片消费后余额	M
		  cpu_tradeType = null;      		//95	   2	  n	    *交易类型标识	交易性质 06-表示电子钱包脱机消费；09-复合消费的类型。	M
		  cpu_terminalNum = null;//[4];		//实际保存4字节bcd码  105	   12	  n	    终端机编号	POS机编号	M
		  cpu_psamCardNum = null;//[6]; 		//117	   12	  n	    *PSAM卡号	PSAM卡终端机编号	M
		  cpu_psamSerialNO = null;//[4];		//129	   9	  n	    *PSAM卡流水号	PSAM卡交易唯一流水号	M
		  cpu_lockCardFlag = null;    		//138	   1	  n	    锁卡交易标志	0为正常交易；1为锁卡交易。统一为0，但发现黑名单卡要进行锁卡。	M
		  cpu_termiTradeNo = null;//[4]; 		//139	   8	  H	    终端交易序号	用8个可见的16进制字符（0～9，A～F）表示；
		  //若无法填写用缺省值空格填充。指：POS交易流水号	M
		  cpu_termiTradeDate = null;//[4];		//147	   8	  n	    *终端交易日期	格式为YYYYMMDD	M
		  cpu_termiTradeTime = null;//[3];		//155	   6	  n	    *终端交易时间	格式为hhmmss	M
		  cpu_tradeTAC = null;//[4];   		//161	   8	  H	    *交易验证代码（TAC）	用8个可见的16进制字符（0～9，A～F）表示；
		  //若无法填写用缺省值空格填充。	M
		  cpu_debitSKeyVer = null;   		//169	   2	  H	    消费密钥版本号	用2个可见的16进制字符（0～9，A～F）表示；
		  //若无法填写用缺省值空格填充。	M
		  cpu_debitSKeyIndex = null;   		//171	   2	  H	    消费密钥索引	用2个可见的16进制字符（0～9，A～F）表示；
		  //若无法填写用缺省值空格填充。	M
		  cpu_cardOffSerialNo = null;//[2];	//卡片返回的 就是 两个字节//173   4 H	卡片脱机交易序列号	用4个可见的16进制字符（0～9，A～F）表示；
		  //若无法填写用缺省值空格填充。指卡消费计数器。	M
		  cpu_IssuingCompany = null;//[8]; 	//177	   16	  n	    发卡机构标识	卡主账号前八位	M
		  cpu_cityCode_L = null;//[2];  		//193	   4	  n	    *城市代码（交易地）	扬州城市代码：3120	M
		  cpu_cityCode_C = null;//[2];  		//197	   4	  n	    *城市代码（卡属地）	卡片读取	M
		  cpu_operaCompany = null;//[8]; 		//201	   8	  n	    运营单位代码	商户编号	M
		  cpu_tradeCounterID = null;//[8];   	//229	   8	  n	    交易柜台编号	公交为：公交车辆编号	M
		  cpu_professionCode = null;    	//259	   2	  n	    行业代码	01-公交，02-出租，03-地铁，04-有轨电车，05-公共自行车，06-轮渡，07-小额消费，08-停车场	M
		  cpu_debitType = null;        	 	//261	   1	  n	    消费类型	0 普通 2 本地赠送钱包 8换乘(公交必备)	0
		  cpu_testFlag = null;        		//276	   1	  n	    测试标志	0为正式数据；1为测试数据	M
		  cpu_randNum = null;//[4];      		//277	   8	  H	    伪随机数	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	O
	  }

	public static void setCpu_beforeBalance(byte[] cpu_beforeBalance) {
		Cpu_tradingInfor.cpu_beforeBalance = cpu_beforeBalance;
	}

	public static void setCpu_afterBalance(byte[] cpu_afterBalance) {
		Cpu_tradingInfor.cpu_afterBalance = cpu_afterBalance;
	}

	public static void setCpu_cardOffSerialNo(byte[] cpu_cardOffSerialNo) {
		if(cpu_cardOffSerialNo.length != 2)
			return ;
		Cpu_tradingInfor.cpu_cardOffSerialNo = cpu_cardOffSerialNo;
	}

	public static void setCpu_localNo(byte[] cpu_localNo) {
		if (cpu_localNo.length != 4)
			return ;
		Cpu_tradingInfor.cpu_localNo = cpu_localNo;
	}

	public static void setCpu_cardSerialNo(byte[] cpu_cardSerialNo) {
		if (cpu_cardSerialNo.length != 10)
			return ;
		Cpu_tradingInfor.cpu_cardSerialNo = cpu_cardSerialNo;
	}

	public static void setCpu_cardType(byte[] cpu_cardType) {
		if (cpu_cardType.length != 1)
			return ;
		Cpu_tradingInfor.cpu_cardType = cpu_cardType;
	}

	public static void setCpu_recordStatus(byte[] cpu_recordStatus) {
		if (cpu_recordStatus.length != 1)
			return ;
		Cpu_tradingInfor.cpu_recordStatus = cpu_recordStatus;
	}

	public static void setCpu_trandAmount(byte[] cpu_trandAmount) {
		if (cpu_trandAmount.length != 4)
			return ;
		Cpu_tradingInfor.cpu_trandAmount = cpu_trandAmount;
	}

	public static void setCpu_tradeType(byte[] cpu_tradeType) {
		if (cpu_tradeType.length != 1)
			return ;
		Cpu_tradingInfor.cpu_tradeType = cpu_tradeType;
	}

	public static void setCpu_terminalNum(byte[] cpu_terminalNum) {
		if (cpu_terminalNum.length != 4)
			return ;
		Cpu_tradingInfor.cpu_terminalNum = cpu_terminalNum;
	}

	public static void setCpu_psamCardNum(byte[] cpu_psamCardNum) {
		if (cpu_psamCardNum.length != 6)
			return ;
		Cpu_tradingInfor.cpu_psamCardNum = cpu_psamCardNum;
	}

	public static void setCpu_psamSerialNO(byte[] cpu_psamSerialNO) {
		if (cpu_psamSerialNO.length != 4)
			return ;
		Cpu_tradingInfor.cpu_psamSerialNO = cpu_psamSerialNO;
	}

	public static void setCpu_lockCardFlag(byte[] cpu_lockCardFlag) {
		if (cpu_lockCardFlag.length != 1)
			return ;
		Cpu_tradingInfor.cpu_lockCardFlag = cpu_lockCardFlag;
	}

	public static void setCpu_termiTradeNo(byte[] cpu_termiTradeNo) {
		if (cpu_termiTradeNo.length != 4)
			return ;
		Cpu_tradingInfor.cpu_termiTradeNo = cpu_termiTradeNo;
	}

	public static void setCpu_termiTradeDate(byte[] cpu_termiTradeDate) {
		if (cpu_termiTradeDate.length != 4)
			return ;
		Cpu_tradingInfor.cpu_termiTradeDate = cpu_termiTradeDate;
	}

	public static void setCpu_termiTradeTime(byte[] cpu_termiTradeTime) {
		if (cpu_termiTradeTime.length != 3)
			return ;
		Cpu_tradingInfor.cpu_termiTradeTime = cpu_termiTradeTime;
	}

	public static void setCpu_tradeTAC(byte[] cpu_tradeTAC) {
		if (cpu_tradeTAC.length != 4)
			return ;
		Cpu_tradingInfor.cpu_tradeTAC = cpu_tradeTAC;
	}

	public static void setCpu_debitSKeyVer(byte[] cpu_debitSKeyVer) {
		if (cpu_debitSKeyVer.length != 1)
			return ;
		Cpu_tradingInfor.cpu_debitSKeyVer = cpu_debitSKeyVer;
	}

	public static void setCpu_debitSKeyIndex(byte[] cpu_debitSKeyIndex) {
		if (cpu_debitSKeyIndex.length != 1)
			return ;
		Cpu_tradingInfor.cpu_debitSKeyIndex = cpu_debitSKeyIndex;
	}

	public static void setCpu_IssuingCompany(byte[] cpu_IssuingCompany) {
		if (cpu_IssuingCompany.length != 8)
			return ;
		Cpu_tradingInfor.cpu_IssuingCompany = cpu_IssuingCompany;
	}

	public static void setCpu_cityCode_L(byte[] cpu_cityCode_L) {
		if (cpu_cityCode_L.length != 2)
			return ;
		Cpu_tradingInfor.cpu_cityCode_L = cpu_cityCode_L;
	}

	public static void setCpu_cityCode_C(byte[] cpu_cityCode_C) {
		if (cpu_cityCode_C.length != 2)
			return ;
		Cpu_tradingInfor.cpu_cityCode_C = cpu_cityCode_C;
	}

	public static void setCpu_operaCompany(byte[] cpu_operaCompany) {
		if (cpu_operaCompany.length != 4)
			return ;
		Cpu_tradingInfor.cpu_operaCompany = cpu_operaCompany;
	}

	public static void setCpu_tradeCounterID(byte[] cpu_tradeCounterID) {
		if (cpu_tradeCounterID.length != 4)
			return ;
		Cpu_tradingInfor.cpu_tradeCounterID = cpu_tradeCounterID;
	}

	public static void setCpu_professionCode(byte[] cpu_professionCode) {
		if (cpu_professionCode.length != 1)
			return ;
		Cpu_tradingInfor.cpu_professionCode = cpu_professionCode;
	}

	public static void setCpu_debitType(byte[] cpu_debitType) {
		if (cpu_debitType.length != 1)
			return ;
		Cpu_tradingInfor.cpu_debitType = cpu_debitType;
	}

	public static void setCpu_testFlag(byte[] cpu_testFlag) {
		if (cpu_testFlag.length != 1)
			return ;
		Cpu_tradingInfor.cpu_testFlag = cpu_testFlag;
	}

	public static void setCpu_randNum(byte[] cpu_randNum) {
		if (cpu_randNum.length != 4)
			return ;
		Cpu_tradingInfor.cpu_randNum = cpu_randNum;
	}
}
