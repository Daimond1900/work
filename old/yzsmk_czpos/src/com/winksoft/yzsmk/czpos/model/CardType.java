package com.winksoft.yzsmk.czpos.model;


public enum CardType {
	
	ORDINARY((byte)0x01,"普通卡"),
	STUDENT((byte)0x02,"学生卡"),
	ELDERLY((byte)0x03,"老人卡"),
	TEST((byte)0x04,"测试卡"),
	SOLDIER((byte)0x05,"军人卡"),
	UNKNOWN((byte)0x06,"未知");
	
	private final byte value;
	private final String messsage;
	
	CardType(byte value,String messsage) {
		this.value = value;
		this.messsage = messsage;
	}

	public byte getValue() {
		return value;
	}

	public String getMesssage() {
		return messsage;
	}
	
	
	public static  CardType convert(byte type) {
		for(CardType trxType : CardType.values()) {
			if(trxType.value == type) {
				return trxType;
			}
		}
		return CardType.UNKNOWN;
		
	}
	
}
