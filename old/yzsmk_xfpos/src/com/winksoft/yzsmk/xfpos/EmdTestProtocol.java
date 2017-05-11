package com.winksoft.yzsmk.xfpos;

public class EmdTestProtocol {
	public  static final byte CMD_DISPLAY = (byte) 0x04;
	public  static final byte CMD_CLEAR = (byte) 0x05;
	public  static final byte CMD_INPUT = (byte) 0x06;
	public  static final byte CMD_INPUTOK = (byte) 0x03;
	public  static final byte CMD_SELECT = (byte) 0x02;
	public  static final byte CMD_MENU = (byte) 0x01;

	public  static final byte INPUT_NUMBER = (byte) 0x00;
	public  static final byte INPUT_HEX = (byte) 0x01;
	public  static final byte INPUT_AMOUNT = (byte) 0x02;
	public  static final byte INPUT_PASSWORD = (byte) 0x03;
	public  static final byte INPUT_ASCII = (byte) 0x04;
	
	
	public  static final byte KEY_UP = (byte) 0x26;
	public  static final byte KEY_DOWN = (byte) 0x27;
	public  static final byte KEY_OK = (byte) 0x13;
	public  static final byte KEY_CANCEL = (byte) 0x03;

	
}
