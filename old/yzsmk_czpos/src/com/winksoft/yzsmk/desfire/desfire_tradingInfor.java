package com.winksoft.yzsmk.desfire;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.utils.ByteUtils;

public class desfire_tradingInfor {
	public static byte[] crade_num;// [4];//POS 交易流水号 4
	public static byte[] city_code;// [2];//城市代码
	public static byte[] passenger_num;// [8];//乘客卡号 8	
	public static byte[] care_class;// 卡类型 1
	public static byte[] care_type;// [2]; //卡种 2
	public static byte[] crade_class;// 交易类型 1
	public static byte[] crade_date;// [7];//交易日期时间 7
	public static byte[] consume_money;// [3];//消费金额 3
	public static byte[] consume_after_balance;// [3];//消费后卡内余额 3
	public static byte[] authorization_num;// [7];//充值授权号 7
	public static byte[] recharge_money;// [3];//充值金额 3
	public static byte[] recharge_date;// [7];//充值日期 7
	public static byte[] crade_auth;// [4];//交易认证码 4+4
	public static byte[] care_num;// [8];//卡面号 8
	public static byte[] consume_crade_val;// [3];//消费后交易计数器 3
	
	public static byte[] operator_num;// [2];//操作员 2
	public static byte[] consume_befor_balance;// [3]; //消费前余额 3
	public static byte[] flag;// [3]; //隔天纪录 标志 3
	final public static int DEBITCNTMAX = 1000000;
	public static byte[] TerminalInfo_ID;
	public static byte[] recordTime;	// 向卡写交易记录时的时间
	
	// 结算、离线上送、批上送
	public static int	consume_total_number;		// 消费总条数  		3byte
	public static int	consume_package_number;		// 本报上传消费条数	2byte
	public static byte[] consume_infor_package;		// 消费上传条数信息	
	public static byte[] consume_record;			// 消费上传记录信息
	public static int   consume_amount;				// 消费金额		8byte   ASCCC
	public static byte  consume_Lauq_conut;			// 结算次数
	final static int UPDATA_COUNT = 2;				// 单次上传条数
	public static int updata_count;					// 单次上传条数
	public static byte updata_mode;					// 0离线上送  1批上送
	//--------------------------------
	public desfire_tradingInfor() {
		crade_num = null;// [4]=null;//POS 交易流水号 4
		city_code = null;// [2]=null;//城市代码
		passenger_num = null;// [8]=null;//乘客卡号 8
		care_class = null;// 卡类型 1
		care_type = null;// [2]; //卡种 2
		crade_class = null;// 交易类型 1
		crade_date = null;// [7]=null;//交易日期时间 7
		consume_money = null;// [3]=null;//消费金额 3
		consume_after_balance = null;// [3]=null;//消费后卡内余额 3
		authorization_num = null;// [7]=null;//充值授权号 7
		recharge_money = null;// [3]=null;//充值金额 3
		recharge_date = null;// [7]=null;//充值日期 7
		crade_auth = null;// [8]=null;//交易认证码 4+4
		care_num = null;// [8]=null;//卡面号 8
		consume_crade_val = null;// [3]=null;//消费后交易计数器 3
		operator_num = null;// [2]=null;//操作员 2
		consume_befor_balance = null;// [3]; //消费前余额 3
		flag = null;// [3]; //隔天纪录 标志 3
		TerminalInfo_ID = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01};
		recordTime = null;	//
	}
	
	
	public boolean setCrade_num(byte[] bCrade_num){
		boolean returnflag = false;
		if (bCrade_num.length == 4){
			crade_num = bCrade_num;// [4];//POS 交易流水号 4
			returnflag = true;
		}
		return returnflag;
	}
	
	public byte[] getCrade_num_hex(){
		int tmp=0;
		byte tmpBuf[] = new byte[3];
		tmp = ByteUtils.bcd_toInt(crade_num);
		tmpBuf[0] = (byte)((tmp>>16)&0xff);
		tmpBuf[1] = (byte)((tmp>>8)&0xff);
		tmpBuf[2] = (byte)(tmp&0xff);
		return tmpBuf;
	}
	
	public boolean setCity_code(byte[] bcity_code){
		boolean returnflag = false;
		if (bcity_code.length == 2){
			city_code = bcity_code;// [2];//城市代码
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setPassenger_num(byte[] bpassenger_num){
		boolean returnflag = false;
		if (bpassenger_num.length == 8){
			passenger_num = bpassenger_num;// [8];//乘客卡号 8	
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setCare_class(byte[] bcare_class){
		boolean returnflag = false;
		if (bcare_class.length == 1){
			care_class = bcare_class;// 卡类型 1	
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setCare_type(byte[] bcare_type){
		boolean returnflag = false;
		if (bcare_type.length == 1){
			care_type = bcare_type;// [2]; //卡种 2
			returnflag = true;
		}
//		else if (bcare_type.length == 1){
//			byte[] tmp = new byte[2];
//			tmp[0] = 0;
//			care_type = bcare_type;// [2]; //卡种 2
//			returnflag = true;
//		}
		return returnflag;
	}

	public boolean setCrade_class(byte[] bcrade_class){
		boolean returnflag = false;
		if (bcrade_class.length == 1){
			crade_class = bcrade_class;// 交易类型 1
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setCrade_date(byte[] bcrade_date){
		boolean returnflag = false;
		if (bcrade_date.length == 7){
			crade_date = bcrade_date;// [7];//交易日期时间 7
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setConsume_money(int consumptioAmount){

		byte[] tmpBalance = new byte[]{(byte)((consumptioAmount>>16)&0xff),(byte)((consumptioAmount>>8)&0xff),(byte)(consumptioAmount&0xff)};	// 3字节金额
		consume_money = tmpBalance;// [3];//消费金额 3
		return true;
	}

	public boolean setConsume_after_balance(int bconsume_after_balance){
		byte[] tmpBalance = new byte[]{(byte)((bconsume_after_balance>>16)&0xff),(byte)((bconsume_after_balance>>8)&0xff),(byte)(bconsume_after_balance&0xff)};	// 3字节金额
		consume_after_balance = tmpBalance;// [3];//消费后卡内余额 3
		return true;
	}

	public boolean setAuthorization_num(byte[] bauthorization_num){
		boolean returnflag = false;
		if (bauthorization_num.length == 7){
			authorization_num = bauthorization_num;// [7];//充值授权号 7
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setRecharge_money(byte[] brecharge_money){
		boolean returnflag = false;
		if (brecharge_money.length == 3){
			recharge_money = brecharge_money;// [3];//充值金额 3
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setRecharge_date(byte[] brecharge_date){
		boolean returnflag = false;
		if (brecharge_date.length == 7){
			recharge_date = brecharge_date;// [7];//充值日期 7
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setCrade_auth(byte[] bcrade_auth){
		boolean returnflag = false;
		if (bcrade_auth.length == 4){
			crade_auth = bcrade_auth;// [8];//交易认证码 4+4
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setCare_num(byte[] bcare_num){
		boolean returnflag = false;
		if (bcare_num.length == 8){
			care_num = bcare_num;// [8];//卡面号 8
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setConsume_crade_val(int transactionNumber){
		
		byte[] tmpBalance = new byte[]{(byte)((transactionNumber>>16)&0xff),(byte)((transactionNumber>>8)&0xff),(byte)(transactionNumber&0xff)};	// 3
		consume_crade_val = tmpBalance;// [3];//消费金额 3
		return true;
	}
	
	public static byte[] getConsume_crade_val_mac_record(){
		int tmp=0;
		byte[] tmpBuf = new byte[3];
		
		tmp = consume_crade_val[0]<<16 + consume_crade_val[1]<<8 + consume_crade_val[0]; 
		tmp = DEBITCNTMAX - tmp;
		tmpBuf[0] = (byte)((tmp>>16)&0xff);
		tmpBuf[1] = (byte)((tmp>>8)&0xff);
		tmpBuf[2] = (byte)(tmp&0xff);
		return tmpBuf;
	}    
	
	public byte[] getConsume_crade_val_mac(){
		int tmp=0;
		byte[] tmpBuf = new byte[2];
		
		tmp = consume_crade_val[0]<<16 + consume_crade_val[1]<<8 + consume_crade_val[0]; 
		tmp = DEBITCNTMAX - tmp;
		tmpBuf[0] = (byte)((tmp>>8)&0xff);
		tmpBuf[1] = (byte)(tmp&0xff);
		return tmpBuf;
	}    
	
	public boolean setOperator_num(byte[] boperator_num){
		boolean returnflag = false;
		if (boperator_num.length == 2){
			operator_num = boperator_num;// [2];//操作员 2
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setConsume_befor_balance(byte[] bconsume_befor_balance){
		boolean returnflag = false;
		if (bconsume_befor_balance.length == 3){
			consume_befor_balance = bconsume_befor_balance;// [3]; //消费前余额 3
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setFlag_befor_balance(byte[] bflag){
		boolean returnflag = false;
		if (bflag.length == 3){
			flag = bflag;// [3]; //隔天纪录 标志 3
			returnflag = true;
		}
		return returnflag;
	}

	public void setRecordTime(long time){
		long tmp;
		tmp = time / 1000;	// 换成秒为秒单位
//		tmp -= 8*3600;		// 时区
		recordTime = new byte[]{(byte)((tmp>>24)&0xff),(byte)((tmp>>16)&0xff),(byte)((tmp>>8)&0xff),(byte)(tmp&0xff)};
		
//		int tt = ByteUtils.getInt(recordTime,0);
		
		// ----------------充值日期  ------------------------
//				SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddhhmmss");
//				long dataTime = (long)ByteUtils.getInt(recordTime, 0)*1000;
//				Date curDate = new Date(dataTime);//获取当前时间
//				String str = formatter.format(curDate);
//				byte[] time1 = ByteUtils.hexToByte(str);	   // str 转  HEX
			//	tradingInfor.setRecharge_date(time);
				//------------------------------------------------
	}
	
//	public static byte[] getDesfireInfor(){
//		byte[] tmp = new byte[3];
//		
////		Map<String, Object> map = new HashMap<String, Object>();
//		
//		Map<String, Object> map =  SqliteUtil.getInstance(null).querySumMoneyAndSumCount();
//		
//		int tt = (Integer) map.get("sumcount");
//		
//		return tmp;
//	}
	
	public static int[] getDesfireCount(){
		int[] tmp = new int[2];
		Map<String, Object> map =  SqliteUtil.getInstance(null).querySumMoneyAndSumCount();
		
		String  count = (String) map.get("sumcount"); 
		int i = Integer.valueOf(count).intValue(); 
		tmp[0] = i;

		tmp[1] =  SqliteUtil.getInstance(null).queryCountByState(1);
		
		if (tmp[1] > UPDATA_COUNT){
			tmp[1] = UPDATA_COUNT;
		}
		updata_count = tmp[1];
		return tmp;
	}
	
	public static byte[] getConsumeInforPackage(int state){
		byte[] tmp = {0,0,0,0,0,0,0,0,0,0};
		int consumeTotalNumber,consumePackageNumber;
		
		//--------获取总条数，和本次上传的条数--------------
		Map<String, Object> map =  SqliteUtil.getInstance(null).querySumMoneyAndSumCount();
		String  count = (String) map.get("sumcount"); 
		consumeTotalNumber = Integer.valueOf(count).intValue(); 
		consumePackageNumber  =  SqliteUtil.getInstance(null).queryCountByState(state);
		
		if (consumePackageNumber > UPDATA_COUNT){
			consumePackageNumber = UPDATA_COUNT;
		}
		updata_count = consumePackageNumber;
		if (updata_count == 0){
			return null;
		}
		
		
		//--------将总条数和本次上传条数转换成上送报文格式---------------
		byte[] tmp1 = ByteUtils.intToBCD(consumeTotalNumber,3);
		byte[] tmp2 = ByteUtils.intToBCD(consumePackageNumber,2);
		System.arraycopy(tmp1, 0, tmp, 0,tmp1.length);
		System.arraycopy(tmp2, 0, tmp, tmp1.length,tmp2.length);
		return tmp;
	}
	
	
//	public static byte[] getConsumeInforPackage(int consumeTotalNumber,int consumePackageNumber){
//		byte[] tmp = {0,0,0,0,0,0,0,0,0,0};
//
//		byte[] tmp1 = ByteUtils.intToBCD(consumeTotalNumber,3);
//		byte[] tmp2 = ByteUtils.intToBCD(consumePackageNumber,2);
//		
//		System.arraycopy(tmp1, 0, tmp, 0,tmp1.length);
//		System.arraycopy(tmp2, 0, tmp, tmp1.length,tmp2.length);
//		return tmp;
//	}
	
	public static byte[] getConsumeRecord(int state){
//		byte[] tmp1 = {(byte)0x00,(byte)0x00,(byte)0x81,(byte)0x51,(byte)0x22,(byte)0x50,(byte)0x03,(byte)0x94,(byte)0x08,(byte)0x89,(byte)0x04,(byte)0x65,(byte)0x00,(byte)0x20,(byte)0x16,(byte)0x09,(byte)0x18,(byte)0x17,(byte)0x28,(byte)0x18,(byte)0x02,(byte)0x08,(byte)0x50,(byte)0x02,(byte)0x0c,(byte)0x15,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x0b,(byte)0x70,(byte)0x20,(byte)0x16,(byte)0x09,(byte)0x18,(byte)0x17,(byte)0x23,(byte)0x16,(byte)0x31,(byte)0xe6,(byte)0xf2,(byte)0x8a,(byte)0x96,(byte)0x16,(byte)0x90,(byte)0x07,(byte)0x60,(byte)0x03,(byte)0x31,(byte)0x61,(byte)0x00,(byte)0x03,(byte)0xdc};
		byte[] tmpbuf = new byte[2048];
		byte[] tmp = null;
////////////////////////
		List<Map<String, Object>> list =  SqliteUtil.getInstance(null).queryDesfirebyState(state, SqliteUtil.getInstance(null).queryCountByState(state));
		
		int count=0;
		int ptr = 0;

		if (list.size() == 0){
			return null;
		}
		if (list.size() > updata_count){
			count = updata_count;
		}else {
			count = list.size();
		}
		for (int i = 0; i < count; i++) {
			Map<String, Object> m = list.get(i);
			
//			String indexs = (String)m.get("id");
			String indexs = m.get("id").toString();
			int index = Integer.valueOf(indexs).intValue(); 
			
			tmp = ByteUtils.hexToByte(m.get("crade_num").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("city_code").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			// -------后4字节----------
			tmp = ByteUtils.hexToByte(m.get("passenger_num").toString());
			System.arraycopy(tmp, 4, tmpbuf, ptr,tmp.length-4);
			ptr += tmp.length-4; 
			
			tmp = ByteUtils.hexToByte(m.get("care_class").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("care_type").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("crade_class").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("crade_date").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
	
			tmp = ByteUtils.hexToByte(m.get("consume_money").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("consume_after_balance").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
	
			tmp = ByteUtils.hexToByte(m.get("authorization_num").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("recharge_money").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("recharge_date").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 

			tmp = ByteUtils.hexToByte(m.get("crade_auth").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 

			tmp = ByteUtils.hexToByte(m.get("care_num").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			tmp = ByteUtils.hexToByte(m.get("consume_crade_val").toString());
			System.arraycopy(tmp, 0, tmpbuf, ptr,tmp.length);
			ptr += tmp.length; 
			
			SqliteUtil.getInstance(null).updateState(new int[]{index},state+1);    ////////////////////////
		}
		byte[] rtmp = new byte[ptr];
		System.arraycopy(tmpbuf, 0, rtmp, 0,ptr);
		return rtmp;
	}
	
	public static void setUpdata_mode(byte updataMode){
		updata_mode = updataMode;
	}
	
	public static byte[] getConsume_Lauq_conut(){
		byte[] tmp = new byte[1];
		
		tmp[0] = updata_mode;
		return tmp;
	}
	
//	public static byte[] getConsume_Lauq(int consumeAmount,int consumeToale){
//		byte[] tmp = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//
//		byte[] tmp1 = ByteUtils.intToBCD(consumeAmount,4);
//		byte[] tmp3 = ByteUtils.HexToAsc(tmp1,tmp1.length);
//		byte[] tmp2 = ByteUtils.intToBCD(consumeToale,2);
//		
//		System.arraycopy(tmp3, 0, tmp, 0,tmp3.length);
//		System.arraycopy(tmp2, 0, tmp, tmp3.length,tmp2.length);
//		return tmp;
//	}
	public static byte[] getConsume_Lauq(){
		byte[] tmp = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int consumeAmount,consumeToale;

		Map<String, Object> map =  SqliteUtil.getInstance(null).querySumMoneyAndSumCount();
		String  count = (String) map.get("sumcount"); 
		String  amount = (String) map.get("summoney"); 
		consumeToale = Integer.valueOf(count).intValue(); 
		consumeAmount = Integer.valueOf(amount).intValue(); 
		
		byte[] tmp1 = ByteUtils.intToBCD(consumeAmount,4);
		byte[] tmp3 = ByteUtils.HexToAsc(tmp1,tmp1.length);
		byte[] tmp2 = ByteUtils.intToBCD(consumeToale,2);
		
		System.arraycopy(tmp3, 0, tmp, 0,tmp3.length);
		System.arraycopy(tmp2, 0, tmp, tmp3.length,tmp2.length);
		return tmp;
	}	
}
