package com.winksoft.yzsmk.xfpos;

public class YzsmkConst {
	public final static byte CARD_DESFIRE = 0x0D;
	public final static byte CARD_CPU = 0x0A;
	public final static byte CARD_Mifire = 0xC;
	public final static byte CARD_IDCARD = 0x0B;
	public final static byte CARD_FAIL = 0X00;      //寻卡失败  
	public final static byte CARD_TIMEOUT = 0X02;   //寻卡超时
	
	
	public final static int P_TICKET_BEGIN = 0; //开始检票
	public final static int P_FIND_CARD = 1;   	//正在寻卡
	public final static int P_READ_INFO = 2;   	//正在读取卡片信息
	public final static int P_UPDATE_INFO =3;  	//更新信息
	public final static int P_TICKET_END = 4;   //检票结束
	public final static int P_ERROR =9;   	//
	
	
	public final static int E_FIND_CARD =  100; // 寻卡失败
	public final static int E_CARD_TYPE = 101; 	//卡类型错误
	public final static int E_CARD_OWNER = 102; //发卡方错误
	
	public final static int CHECK_RESULT_OK 		= 0;  //检票通过
	public final static int CHECK_RESULT_FAIL 		= 1;  //检票失败
	public final static int CHECK_RESULT_OVERDATE 	= 3;  //已过期 
	public final static int CHECK_RESULT_ERROR 		= 2;  //刷卡错误
	public final static int CHECK_RESULT_UNAUTH 	= 4;  //非法卡

	
	
	public final static int TEST_CARD_TYPE =  81; // 卡类型
	public final static int TEST_BALANCE =  82; // 消费前余额
	public final static int TEST_RESULT = 83; // 结果
}
