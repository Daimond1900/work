package com.winksoft.yzsmk.cpu;

import com.winksoft.yzsmk.utils.ByteUtils;

/*   需要设置的信息
 * 
 * 	public static byte[] init_keyIndex;		//[1];  //  密钥索引号
 *	public static byte[] init_terminalNumber;	//[6];  //  终端机编号
 */

public class Cpu_creditForLoadInfor {
	// ---其它全局变量
	
	// 圈存初始化 --->IC
	  public static byte[] init_keyIndex;		//[1];  //  密钥索引号
	  public static byte[] init_transAmount;	//[4];  //  交易金额
	  public static byte[] init_terminalNumber;	//[6];  //  终端机编号
    // 圈存初始化卡片返回 <---IC
	  public static byte[] initR_balance;				//[4];  //  电子钱包余额
	  public static byte[] initR_onlineTransNumber;		//[2];  //  联机交易序号
	  public static byte[] initR_keyVersion;			//[1];  //  密钥版本号（DLK）
	  public static byte[] initR_algorithmIdentifies;	//[1];  //  算法标识（DLK）
	  public static byte[] initR_pseudoRandom;			//[4];  //  伪随机数（卡片）
	  public static byte[] initR_mac1;					//[4];  //  MAC1
  	// 圈存  --->IC
	  public static byte[] credit_transData;			//[4];  //  交易日期（主机）
	  public static byte[] credit_transTime;			//[3];  //  交易时间（主机）
	  public static byte[] credit_mac2;					//[4];  //  MAC2
	// 圈存卡片返回 <---IC
	  public static byte[] creditR_TAC;					//[4];  //  TAC
	// 圈存请求上送平台数据  ---->
	  public static String request_TRANSCODE;		// 1	交易码	TRANSCODE	4	是	字符串	5312
	  public static String request_CITIZEN_CARD_NO;	// 2	市民卡号	CITIZEN_CARD_NO	19	是	字符串	
	  public static String request_APP_FLAG;		// 3	应用标识	APP_FLAG	2	是	字符串	20
	  public static String request_ACCEPT_CUS_NO;	// 4	受理方代码	ACCEPT_CUS_NO	12	是	字符串	商户号
	  public static String request_TERM_NO;			// 5	终端号	TERM_NO	20	是	字符串	
	  public static String request_SAM_NO;			// 6	SAM卡号	SAM_NO	12	是	字符串	
	  public static String request_OPR_NO;			// 7	操作员号	OPR_NO	24	是	字符串	
	  public static String request_TERM_SEQ;		// 8	终端流水号	TERM_SEQ	16	是	字符串	
	  public static String request_CRD_NO;			// 9	卡内号	CRD_NO	16	是	字符串	卡号后16位
	  public static String request_CRD_PHYS_TP;		// 10	卡物理类型	CRD_PHYS_TP	1	是	字符串	1-cpu
	  public static String request_ISS_CITY_CD;		// 11	发卡城市代码	ISS_CITY_CD	4	是	字符串	卡内读取
	  public static String request_TXN_CITY_CD;		// 12	交易城市代码	TXN_CITY_CD	4	是	字符串	3120
	  public static String request_CRD_CITY_CD;		// 13	城市代码	CRD_CITY_CD	4	是	字符串	3120
	  public static String request_CURR_COUNT;		// 14	卡计数器	CURR_COUNT	4	是	字符串	Hex 
	  public static String request_CRD_BAL_BEF;		// 15	交易前余额	CRD_BAL_BEF	8	是	字符串	
	  public static String request_CRD_BAL_AFT;		// 16	交易后余额	CRD_BAL_AFT	8	是	字符串	
	  public static String request_TXN_AMT;			// 17	交易金额	TXN_AMT	8	是	字符串	Hex，单位为分
	  public static String request_TXN_DT;			// 18	交易时间	TXN_DT	14	是	字符串	精确到秒，19，14位
	  public static String request_CRD_TXN_TYPE;	// 19	卡交易类型	CRD_TXN_TYPE	2	是	字符串	02-充值
	  public static String request_DIV_FACTOR;		// 20	分散因子	DIV_FACTOR	16	是	字符串	Hex
	  public static String request_RAND_NU;			// 21	伪随机数	RAND_NU	8	是	字符串	
	  public static String request_TXN_MAC1;		// 22	交易MAC1	TXN_MAC1	8	是	字符串	
	  public static String request_RAND_NUM_FILL;	// 23	伪随机数交易序号及补位	RAND_NUM_FILL	16	是	字符串	随机数+卡交易序号+80+00
	  public static String request_CARD_TYPE;		// 24	卡型	CARD_TYPE	1	是	字符串	
	  public static String request_REAL_TXN_AMT;	// 25	实际充值金额	REAL_TXN_AMT	8	是	字符串	
	  public static String request_LAST_TXN_CNT;	// 26	最后消费交易计数器	LAST_TXN_CNT	4	是	字符串	Hex，卡上读取。使用消费初始化命令取值-1
	  public static String request_LAST_TXN_DT;		// 27	最后交易时间	LAST_TXN_DT	14	是	字符串	卡上读取最后一笔交易时间
	  public static String request_SAM_SEQ;			// 28	SAM流水号	SAM_SEQ	16	是	字符串	
	// 圈存请求平台返回数据 <----
	  public static String requestR_TRANSCODE;		// 1	交易码	TRANSCODE	4	是	字符串	5312
	  public static String requestR_RTN_CODE;		// 2	返回码	RTN_CODE	6	是	字符串	成功:000000 其它：失败
	  public static String requestR_RTN_MSG;		// 3	返回信息	RTN_MSG	30	是	字符串	
	  public static String requestR_TXN_MAC2;		// 4	交易MAC2	TXN_MAC2	8	否	字符串	16进制，失败不返回
	  public static String requestR_CT_SEQ;			// 5	中心流水号	CT_SEQ	24	否	字符串	失败不返回
	  public static String requestR_BUSINESS_NO;	// 6	业务流水号	BUSINESS_NO	10	否	字符串	失败不返回
	// 圈存确认上送平台数据 ---->
	  public static String confirm_TRANSCODE;		// 1	交易码	TRANSCODE	4	是	字符串	5311
	  public static String confirm_CITIZEN_CARD_NO;	// 2	市民卡号	CITIZEN_CARD_NO	20	是	字符串	
	  public static String confirm_CT_SEQ;			// 3	中心流水号	CT_SEQ	24	是	字符串	
	  public static String confirm_BUSINESS_NO;		// 4	业务流水号	BUSINESS_NO	10	是	字符串	需要冲正的业务流水号
	  public static String confirm_CRD_BAL_AFT;		// 5	交易后余额	CRD_BAL_AFT	8	是	字符串	Hex，卡面读取，充值失败时返回交易前金额
	  public static String confirm_CURR_COUNT;		// 6	卡计数器	CURR_COUNT	4	是	字符串	Hex，卡面获取，充值计数器
	  public static String confirm_APP_FLAG;		// 7	应用标识	APP_FLAG	2	是	字符串	20
	  public static String confirm_TXN_AMT;			// 8	交易金额	TXN_AMT	8	是	字符串	Hex
	  public static String confirm_TXN_DT;			// 9	交易时间	TXN_DT	14	是	字符串	
	// 圈存确认平台返回数据  <----
	  public static String confirmR_TRANSCODE;		// 1	交易码	TRANSCODE	4	是	字符串	5311
	  public static String confirmR_RTN_CODE;		// 2	返回码	RTN_CODE	6	是	字符串	成功:000000 其它：失败
	  public static String confirmR_RTN_MSG;		// 3	返回信息	RTN_MSG	30	是	字符串	
	  public static String confirmR_BUSINESS_NO;		// 4	业务流水号	BUSINESS_NO	10	否	字符串	成功时返回。
	  
	  public Cpu_creditForLoadInfor(){
		  init_keyIndex = new byte[]{(byte)(0x01)};	// 后台指定
	  
	  
		  request_TRANSCODE = "5312";		// 1	交易码	TRANSCODE	4	是	字符串	5312
		  request_APP_FLAG = "20";		// 3	应用标识	APP_FLAG	2	是	字符串	20
		  request_ACCEPT_CUS_NO = "10002";//"00010002";	// 4	受理方代码	ACCEPT_CUS_NO	12	是	字符串	商户号
//		  request_TERM_NO = "000100021";			// 5	终端号	TERM_NO	20	是	字符串	
		  request_TERM_NO = "1000211";//"0001000211";			// 5	终端号	TERM_NO	20	是	字符串	
		 ///////???????????????????????
//		  public static String request_SAM_NO;			// 6	SAM卡号	SAM_NO	12	是	字符串	
		  
		  //***********************************
//		  request_SAM_NO = "1000211";//"000001000211";			// 6	SAM卡号	SAM_NO	12	是	字符串	
//		  
//		  request_SAM_NO = ByteUtils.printBytes(Cpu_psamInfor.file15_psamSerialNo);
//		  request_SAM_NO = ByteUtils.printBytes(Cpu_psamInfor.file16_terminalNum);
		  //--------------------------------------------------------
		  
		  request_OPR_NO = "100021101";//"000100021101";			// 7	操作员号	OPR_NO	24	是	字符串	
		  request_CRD_PHYS_TP = "1";		// 10	卡物理类型	CRD_PHYS_TP	1	是	字符串	1-cpu
		  request_TXN_CITY_CD = "3120";		// 12	交易城市代码	TXN_CITY_CD	4	是	字符串	3120
		  request_CRD_CITY_CD = "3120";		// 13	城市代码	CRD_CITY_CD	4	是	字符串	3120
		  request_CRD_TXN_TYPE = "02";	// 19	卡交易类型	CRD_TXN_TYPE	2	是	字符串	02-充值	  
		  ///////???????????????
//		  public static String request_SAM_SEQ;			// 28	SAM流水号	SAM_SEQ	16	是	字符串	
		  request_SAM_SEQ = "0";
	  
		  
		  
		  
		// 圈存确认上送平台数据 ---->
		  confirm_TRANSCODE = "5311";		// 1	交易码	TRANSCODE	4	是	字符串	5311
		  confirm_APP_FLAG = "20";		// 7	应用标识	APP_FLAG	2	是	字符串	20
	  }
	  

	  public void setRequest_TERM_SEQ(int termSeq){ 
		  byte[] tmp = ByteUtils.unsignedInt(termSeq);
		  request_TERM_SEQ = ByteUtils.printBytes(tmp);		// 8	终端流水号	TERM_SEQ	16	是	字符串	
	  }
	  
	  public boolean setInit_AmountOpratian(int balance,int circleAmount) {
		  Cpu_creditForLoadInfor.init_transAmount = ByteUtils.unsignedInt(circleAmount);
		  String tmp = ByteUtils.printBytes(Cpu_creditForLoadInfor.init_transAmount);
		  for (int i=0;i<tmp.length();i++){
			  if (tmp.charAt(0) == '0'){
				  tmp =tmp.substring(1,tmp.length());
			  }
			  else
				  break;
		  }
		  request_TXN_AMT = tmp;			// 17	交易金额	TXN_AMT	8	是	字符串	Hex，单位为分
		  request_REAL_TXN_AMT = tmp;	// 25	实际充值金额	REAL_TXN_AMT	8	是	字符串	

		  confirm_TXN_AMT = tmp;			// 8	交易金额	TXN_AMT	8	是	字符串	Hex
		  
		  byte[] tmpBalance = ByteUtils.unsignedInt(balance);
		  tmp = ByteUtils.printBytes(tmpBalance);
		  for (int i=0;i<tmp.length();i++){
			  if (tmp.charAt(0) == '0'){
				  tmp =tmp.substring(1,tmp.length());
			  }
			  else
				  break;
		  }
		  request_CRD_BAL_BEF = tmp;		// 15	交易前余额	CRD_BAL_BEF	8	是	字符串	
		  
		  byte[] tmpAfterBalance = ByteUtils.unsignedInt(balance + circleAmount);
		  tmp = ByteUtils.printBytes(tmpAfterBalance);
		  for (int i=0;i<tmp.length();i++){
			  if (tmp.charAt(0) == '0'){
				  tmp =tmp.substring(1,tmp.length());
			  }
			  else
				  break;
		  }
		  request_CRD_BAL_AFT = tmp;		// 16	交易后余额	CRD_BAL_AFT	8	是	字符串	
		  

		  confirm_CRD_BAL_AFT = tmp;		// 5	交易后余额	CRD_BAL_AFT	8	是	字符串	Hex，卡面读取，充值失败时返回交易前金额
		  return true;
	  }
	  
	  public boolean setRequest_cardNumber(byte[] cardNumber) {
		  if (cardNumber.length != 10)
			  return false;
		  String tmp = ByteUtils.printBytes(cardNumber);
		  for (int i=0;i<tmp.length();i++){
			  if (tmp.charAt(0) == '0'){
				  tmp =tmp.substring(1,tmp.length());
			  }
			  else
				  break;
		  }
		  request_CITIZEN_CARD_NO = tmp;	// 2	市民卡号	CITIZEN_CARD_NO	19	是	字符串	
		  confirm_CITIZEN_CARD_NO = tmp;	// 2	市民卡号	CITIZEN_CARD_NO	20	是	字符串	
//		  request_CRD_NO = tmp;				// 9	卡内号	CRD_NO	16	是	字符串	卡号后16位		2016-12-01
		  if (tmp.length() >= 16){
			  tmp =tmp.substring(tmp.length() - 16,tmp.length());
		  }
//		  request_CRD_NO = tmp;			// 9	卡内号	CRD_NO	16	是	字符串	卡号后16位
		  request_DIV_FACTOR = tmp;		// 20	分散因子	DIV_FACTOR	16	是	字符串	Hex
		  request_CRD_NO = tmp;				// 9	卡内号	CRD_NO	16	是	字符串	卡号后16位
		  return true;
	  }
	  
	  public boolean setRequest_ISS_CITY_CD(byte[] issCityCD) {
		  if (issCityCD.length != 2)
			  return false;
		  request_ISS_CITY_CD = ByteUtils.printBytes(issCityCD);		// 11	发卡城市代码	ISS_CITY_CD	4	是	字符串	卡内读取
		  return true;
	  }
	  
	  public boolean setRequest_TXN_DT(byte[] txnDT) {
		  if (txnDT.length != 7)
			  return false;
		  request_TXN_DT = ByteUtils.printBytes(txnDT);		// 18	交易时间	TXN_DT	14	是	字符串	精确到秒，19，14位
		  confirm_TXN_DT = request_TXN_DT;			// 9	交易时间	TXN_DT	14	是	字符串	
		  return true;
	  }
	  
	  public boolean setCredit_transTimes(byte[] transData,byte[]transTime) {
		  if ((transData.length != 4)||(transTime.length != 3))
			  return false;
		  credit_transData = transData;			//[4];  //  交易日期（主机）
		  credit_transTime = transTime;			//[3];  //  交易时间（主机）
		  return true;
	  }
	  
	  public boolean setCredit_mac2(byte[] mac2) {
		  if (mac2.length != 4)
			  return false;
		  credit_mac2 = mac2;					//[4];  //  MAC2
		  return true;
	  }

	  public boolean setCredit_response(byte[] response) {
		  if (response.length != 4)
			  return false;
		  creditR_TAC = response;					//[4];  //  TAC
		  return true;
	  }
	  
	  public boolean setInit_response(byte[] response) {
		  if (response.length != 16)
			  return false;
		  int prt=0;
		  initR_balance = ByteUtils.getBytes(response,prt,4);prt += 4;				//[4];  //  电子钱包余额		
		  initR_onlineTransNumber = ByteUtils.getBytes(response,prt,2);prt += 2;	//[2];  //  联机交易序号
		  initR_keyVersion = ByteUtils.getBytes(response,prt,1);prt += 1;			//[1];  //  密钥版本号（DLK）
		  initR_algorithmIdentifies = ByteUtils.getBytes(response,prt,1);prt += 1;	//[1];  //  算法标识（DLK）
		  initR_pseudoRandom = ByteUtils.getBytes(response,prt,4);prt += 4;			//[4];  //  伪随机数（卡片）
		  initR_mac1 = ByteUtils.getBytes(response,prt,4);prt += 4;					//[4];  //  MAC1
		  

		  request_CURR_COUNT = ByteUtils.printBytes(initR_onlineTransNumber);		// 14	卡计数器	CURR_COUNT	4	是	字符串	Hex 
		  request_RAND_NU = ByteUtils.printBytes(initR_pseudoRandom);				// 21	伪随机数	RAND_NU	8	是	字符串	
		  request_TXN_MAC1 = ByteUtils.printBytes(initR_mac1);						// 22	交易MAC1	TXN_MAC1	8	是	字符串	
		  request_RAND_NUM_FILL = request_RAND_NU+request_CURR_COUNT+"8000";		// 23	伪随机数交易序号及补位	RAND_NUM_FILL	16	是	字符串	随机数+卡交易序号+80+00

		  
		  confirm_CRD_BAL_AFT = ByteUtils.printBytes(initR_balance);				// 交易后余额	
		  confirm_CURR_COUNT = request_CURR_COUNT;
//		  /////test/////////////////////////////////
//		  confirm_CURR_COUNT = request_CURR_COUNT;
//		  int tmp = (initR_onlineTransNumber[0]<<8) + initR_onlineTransNumber[1];
//		  tmp++;
//		  byte[] temp = new byte[]{(byte)((tmp>>8)&0xff),(byte)(tmp&0xff)};
//		  confirm_CURR_COUNT = ByteUtils.printBytes(temp);		// 6	卡计数器	CURR_COUNT	4	是	字符串	Hex，卡面获取，充值计数器
		  return true;
	  }
	  
	  public boolean setInit_response(byte[] response,int test) {
		  if (response.length != 16)
			  return false;
		  int prt=0;
		  initR_balance = ByteUtils.getBytes(response,prt,4);prt += 4;				//[4];  //  电子钱包余额		
		  initR_onlineTransNumber = ByteUtils.getBytes(response,prt,2);prt += 2;	//[2];  //  联机交易序号
		  
		  confirm_CRD_BAL_AFT = ByteUtils.printBytes(initR_balance);				// 交易后余额
		  confirm_CURR_COUNT = ByteUtils.printBytes(initR_onlineTransNumber);		// 14	卡计数器	CURR_COUNT	4	是	字符串	Hex 
		  return true;
	  }
	  
	  public boolean setResponse_CARD_TYPE(byte[] cardType) {
		  if (cardType.length != 1)
			  return false;
		  String tmp = ByteUtils.printBytes(cardType);
		  request_CARD_TYPE = tmp.substring(1,tmp.length());;		// 24	卡型	CARD_TYPE	1	是	字符串	
		  return true;
	  }
	  
	  public boolean setInit_consumptionResponse(byte[] response) {
		  if (response.length != 15)
			  return false;
		  int prt=0;
		  byte[] balance = ByteUtils.getBytes(response,prt,4);prt += 4;				//[4];  //  电子钱包余额		
		  byte[] offlineTransNumber = ByteUtils.getBytes(response,prt,2);prt += 2;	//[2];  //  联机交易序号
		  request_LAST_TXN_CNT = ByteUtils.printBytes(offlineTransNumber);			// 26	最后消费交易计数器	LAST_TXN_CNT	4	是	字符串	Hex，卡上读取。使用消费初始化命令取值-1
		  return true;
	  }
	  
	  public boolean setRead_recode(byte[] recode) {
		  if (recode.length != 23)
			  return false;
		  int prt=0;
		  byte[] transSerial = ByteUtils.getBytes(recode,prt,2);prt += 2;				//[2];  //  EP联机或脱机交易序号		
		  byte[] overdraftLimit = ByteUtils.getBytes(recode,prt,3);prt += 3;	//[3];  //  透支限额
		  byte[] transAmount = ByteUtils.getBytes(recode,prt,4);prt += 4;			//[4];  //  交易金额
		  byte[] transType = ByteUtils.getBytes(recode,prt,1);prt += 1;	//[1];  //  交易类型标识
		  byte[] termNumber = ByteUtils.getBytes(recode,prt,6);prt += 6;			//[6];  //  终端机编号
		  byte[] transData = ByteUtils.getBytes(recode,prt,4);prt += 4;					//[4];  //  交易日期（终端）
		  byte[] transTime = ByteUtils.getBytes(recode,prt,3);prt += 3;					//[4];  //  交易时间（终端）
		 
		  String tmp = ByteUtils.printBytes(transData);
		  tmp += ByteUtils.printBytes(transTime);
		  request_LAST_TXN_DT = tmp;		// 27	最后交易时间	LAST_TXN_DT	14	是	字符串	卡上读取最后一笔交易时间
		  return true;
	  }
	  
	  public boolean setrequest_SAM_NO(){
		  if (Cpu_psamInfor.file16_terminalNum == null)
			  return false;

		  init_terminalNumber = Cpu_psamInfor.file16_terminalNum;//new byte[]{(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x02,(byte)0x11};
//		  System.arraycopy(key_62.getBytes(), 0, keyPIn, 0, keyPIn.length);
		  request_SAM_NO = ByteUtils.printBytes(Cpu_psamInfor.file16_terminalNum);
//		  request_SAM_NO = "00100000002A";//"000001000211";	
//		  request_SAM_NO = "413100640148";//"000001000211";	// 6	SAM卡号	SAM_NO	12	是	字符串	
//		  request_SAM_NO = "000001000211";//"000001000211";			// 6	SAM卡号	SAM_NO	12	是	字符串	
		  return true;
	  }
}
