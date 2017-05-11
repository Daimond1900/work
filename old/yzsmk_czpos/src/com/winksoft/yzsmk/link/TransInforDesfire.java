package com.winksoft.yzsmk.link;

public class TransInforDesfire {
	public static byte[] batch_no_9;	//[3] 批次号    	9
	public static byte[] trace_no_11;	//[3] 流水号		11
	public static byte[] key_31;		//[8] KEY		31
	public static byte[] version_32;		//[11] 应用版本号	32
	public static byte[] terminal_no_41;//[8] 终端号		41	
	public static byte[] merchant_no_42;//[15] 商户号		42
	public static String merchant_name_59;//[变长] 商户名称	59
	public static byte[] pin_key_62;	//[8] PIN密钥	62(前8)
	public static byte[] mac_key_62;	//[8] MAC密钥	62(后8)
	public static byte[] opration_no;	//[4] 操作员号               60
	
	public static byte[] trans_random;	//[4]随机数
	public static byte[] package_no;	//[1]包序号
	public static int tradeType;		// 交易类型
	
	public TransInforDesfire(){
		batch_no_9 = new byte[]{(byte)0x00,(byte)0x00,(byte)0x01};
		trace_no_11 = new byte[]{(byte)0x00,(byte)0x00,(byte)0x01};
		key_31 = new byte[]{(byte)0x32,(byte)0x32,(byte)0x38,(byte)0x35,
				            (byte)0x36,(byte)0x30,(byte)0x30,(byte)0x30};
		version_32 = new byte[]{(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,
	            			    (byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,
	            			    (byte)0x30,(byte)0x30};
		terminal_no_41 = null;
		merchant_no_42 = new byte[]{(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x39,
			    					(byte)0x39,(byte)0x39,(byte)0x39,(byte)0x39,
			    					(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
			    					(byte)0x00,(byte)0x00,(byte)0x00}; 	// 00099999
		merchant_name_59 = null;
		pin_key_62 = new byte[]{(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,
			    			    (byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30};
		mac_key_62 = new byte[]{(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,
			    			    (byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30};
		opration_no = new byte[]{(byte)0x32,(byte)0x38,(byte)0x35,(byte)0x33};
		trans_random = null;
		package_no = null;
	}
	
	public boolean setBatch_no_9(byte[] batch_no){
		boolean returnflag = false;
		if (batch_no.length == 3){
			batch_no_9 = batch_no;//[3] 批次号    	9
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setTrace_no_11(byte[] trace_no){
		boolean returnflag = false;
		if (trace_no.length == 3){
			trace_no_11 = trace_no; //[3] 流水号		11
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setKey_31(byte[] key){
		boolean returnflag = false;
		if (key.length == 8){
			key_31 = key; //[8] KEY		31
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setVersion_32(byte[] version){
		boolean returnflag = false;
		if (version.length == 11){
			version_32 = version; //[11] 应用版本号	32
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setTerminal_no_41(byte[] terminal_no){
		boolean returnflag = false;
		if (terminal_no.length == 8){
			terminal_no_41 = terminal_no; //[8] 终端号		41	
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setMerchant_no_42(byte[] merchant_no){
		boolean returnflag = false;
		if (merchant_no.length == 15){
			merchant_no_42 = merchant_no; //[15] 商户号		42
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setMerchant_name_59(String merchant_name){
		merchant_name_59 = merchant_name; //[变长] 商户名称	59
		return true;
	}
	
	public boolean setPin_key_62(byte[] pin_key){
		boolean returnflag = false;
		if (pin_key.length == 8){
			pin_key_62 = pin_key; //[8] PIN密钥	62(前8)
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setMac_key_62(byte[] mac_key){
		boolean returnflag = false;
		if (mac_key.length == 8){
			mac_key_62 = mac_key; //[8] MAC密钥	62(后8)
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setOpration_no(byte[] opration){
		boolean returnflag = false;
		if (opration.length == 4){
			opration_no = opration; //[4] 操作员号               60
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setTrans_random(byte[] trans_random_n){
		boolean returnflag = false;
		if (trans_random_n.length == 4){
			trans_random = trans_random_n; //[4]随机数
			returnflag = true;
		}
		return returnflag;
	}

	public boolean setPackage_no(byte[] package_no_n){
		boolean returnflag = false;
		if (package_no_n.length == 1){
			package_no = package_no_n; //[1]包序号
			returnflag = true;
		}
		return returnflag;
	}
	
	public void setTradeType(int trade_type){
			tradeType = trade_type;		// 交易类型
	}
}
