package com.winksoft.yzsmk.card;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

//建设部PSAM卡
public class PsamJsb {
	public final static byte PSAM_CLA_NORMAL        = (byte)0x00;  //
	public final static byte PSAM_CLA_MAC           = (byte)0x04;  //安全模式
	public final static byte PSAM_CLA_SPEC          = (byte)0x80;  //专用指令
	public final static byte PSAM_CLA_SPEC_MAC      = (byte)0x84;  //专用安全指令
	public final static byte PSAM_CLA_DSK           = (byte)0xBF;  //DELIVERY SESSION KEY
	public final static byte PSAM_INS_SELECT_FILE   = (byte)0xA4;  //选择文件
	public final static byte PSAM_INS_READ_BINARY   = (byte)0xB0;  //读取二进制文件
	public final static byte PSAM_INS_GET_RESPONSE  = (byte)0xC0;
	public final static byte PSAM_INS_GET_CHALLENGE = (byte)0x84;  //取随机数
	public final static byte PSAM_INS_GET_MAC1      = (byte)0x70;  //读取校验码
	public final static byte PSAM_INS_VERIFY_MAC2   = (byte)0x72;  //验证MAC2
	public final static byte PSAM_INS_DSK           = (byte)0xDE;  //产生过程密钥
	public final static byte PSAM_INS_DK            = (byte)0x1A;  //产生密钥
	public final static byte PSAM_INS_CIP           = (byte)0xFA;  //安全计算
	public final static byte PSAM_INS_CALCULATE_KEY = (byte)0xFC;  //计算密钥
	public final static byte PSAM_P1_SF_BY_NAME     = (byte)0x04;  //通过文件名选择文件
	public final static byte PSAM_P1_SF_BY_ID       = (byte)0x00;  //通过标示符选择文件
	public final static byte PSAM_P2_SF_CURR        = (byte)0x00;  //选择当前文件
	public final static byte PSAM_P2_SF_NEXT        = (byte)0x02;  //选择下一个文件
	

	public byte[] transmit(byte[] send) throws PsamException
	{
		return null;
	}
	
//	public byte[] reset(void) throws PsamException
//	{
//		byte[] recvBuf = transmit(buff.array());
//		return getValidReturn(recvBuf);
//	}
	
	//返回命令的有效数据，以去除SW1, SW2
	public byte[] getValidReturn(byte[] recvBuf) throws PsamException
	{
		if  ((recvBuf != null) && (recvBuf.length == 1) && (recvBuf[0] == 0x00)){
			return null;
		}
		else if((recvBuf == null) || (recvBuf.length<2) ||(!((recvBuf[recvBuf.length-2] == (byte)0x90) || (recvBuf[recvBuf.length-2] == (byte)0x61))))
		{
			throw new PsamException(0, "返回值错误！");
		}else if (recvBuf[recvBuf.length-2]  == (byte)0x61){
			recvBuf = getResponse(recvBuf[recvBuf.length-1]);
			return recvBuf;
		}else{
		}
		byte[] result = new byte[recvBuf.length-2];
		System.arraycopy(recvBuf, 0, result, 0, recvBuf.length-2);
		return result;
	}
	
	//选择目录
	//data: 用于返回组织后的数据
	//FileName: 选择的文件名
	//Mode: true表示按照文件名，false表示按照文件表识
	public void selectFileByName(String FileName) throws PsamException
	{
		byte[] m = null;
		try {
			m = FileName.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			throw new PsamException(1, "文件名转换错误！");
		}
		
		ByteBuffer buff = ByteBuffer.allocate(5+m.length);
		buff.put((byte)0x00)
			.put((byte)0xA4)
			.put(PSAM_P1_SF_BY_NAME)
			.put(PSAM_P2_SF_CURR)
			.put((byte)m.length)
			.put(m);
		
		byte[] recvBuf = transmit(buff.array());
		
		
		recvBuf = getValidReturn(recvBuf);

	}
	
	public byte[] selectFileById(byte[] id) throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5+id.length);
		buff.put(PSAM_CLA_NORMAL)
			.put(PSAM_INS_SELECT_FILE)
			.put(PSAM_P1_SF_BY_ID)
			.put(PSAM_P2_SF_CURR)
			.put((byte)id.length)
			.put(id);
		
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	

	//读取二进制文件
	//byShortID: 是否通过短文件标识
	//ShortID: 短文件标示
	//OFFSet:  文件偏移地址
	//FileLen: 读取数据长度
	//data: 返回的数据
	public byte[] getBinaryFile(boolean byShort, byte SFI, int offset, byte fileLen) throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5);
		buff.put(PSAM_CLA_NORMAL)
			.put(PSAM_INS_READ_BINARY);
		if (byShort)
			buff.put((byte)(0x80 |(SFI & 0x1F)))  //P1
				.put((byte)0x00)
				.put(fileLen);
		else
			buff.put((byte)((offset >> 8) & 0x7F))
				.put((byte)((offset) & 0xFF))
				.put(fileLen);
		

	  byte[] recvBuf = transmit(buff.array());
	  return getValidReturn(recvBuf);
	}
	  
	//取随机数
	public byte[] GetChallenge() throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.put(PSAM_CLA_NORMAL)
			.put(PSAM_INS_GET_CHALLENGE)
			.put((byte)0x00)
			.put((byte)0x00);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	
	public byte[] getResponse(byte readLen) throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5);
		buff.put(PSAM_CLA_NORMAL)
			.put(PSAM_INS_GET_RESPONSE)
			.put((byte)0x00)
			.put((byte)0x00)
			.put(readLen);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}

	  
	public byte[] iniForPurchase(byte[] MacData) throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5+MacData.length);
		buff.put(PSAM_CLA_SPEC)
			.put(PSAM_INS_GET_MAC1)
			.put((byte)0x00)
			.put((byte)0x00)
			.put((byte)MacData.length)
			.put(MacData);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}

	
	public byte[] creditForPurchase(byte[] Mac) throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5+Mac.length);
		buff.put(PSAM_CLA_SPEC)
			.put(PSAM_INS_VERIFY_MAC2)
			.put((byte)0x00)
			.put((byte)0x00)
			.put((byte)Mac.length)
			.put(Mac);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	 
	public byte[] deliverySessionKey(byte[] sDe, byte keyID)throws PsamException
	{
		if (sDe.length != 16)
			throw new PsamException(0, "sDelivery Session error");
		ByteBuffer buff = ByteBuffer.allocate(5+sDe.length);
		buff.put(PSAM_CLA_DSK)
			.put(PSAM_INS_DSK)
			.put((byte)0x29)
			.put(keyID)
			.put((byte)sDe.length)
			.put(sDe);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}


	public byte[] deliveryKey(byte[] sDe, byte keyType, byte keyID)throws PsamException
	{
		if (sDe.length != 8)
			throw new PsamException(0, "sDelivery error");
		ByteBuffer buff = ByteBuffer.allocate(5+sDe.length);
		buff.put(PSAM_CLA_SPEC)
			.put(PSAM_INS_DK)
			.put(keyType)
			.put(keyID)
			.put((byte)sDe.length)
			.put(sDe);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	

	public byte[] cipher(byte[] ciper, byte P1, byte P2)throws PsamException
	{
		if (ciper.length %8 != 0)
			throw new PsamException(0, "sCiper error");
		ByteBuffer buff = ByteBuffer.allocate(5+ciper.length);
		buff.put(PSAM_CLA_SPEC)
			.put(PSAM_INS_CIP)
			.put(P1)
			.put(P2)
			.put((byte)ciper.length)
			.put(ciper);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	

	public byte[] calculateKey(byte keyID, byte[] data)throws PsamException
	{
		ByteBuffer buff = ByteBuffer.allocate(5+data.length);
		buff.put(PSAM_CLA_SPEC)
			.put(PSAM_INS_CALCULATE_KEY)
			.put((byte)0x01)
			.put(keyID)
			.put((byte)data.length)
			.put(data);
		byte[] recvBuf = transmit(buff.array());
		return getValidReturn(recvBuf);
	}
	
	public String getErrMsg(byte sw1, byte sw2)
	{
		String result = "未知错误";
		if ((sw1 == (byte)0x90) && (sw2 == (byte)0x00))  result = "正确执行 ";
		if ((sw1 == (byte)0x61))  result = "正确执行响应数据长度"+ sw2 +"。可用 Get Response命令取回响应数据。 （仅用于 T=0） ";
		if ((sw1 == (byte)0x62) && (sw2 == (byte)0x81))  result = "回送的数据可能错误 ";
		if ((sw1 == (byte)0x62) && (sw2 == (byte)0x83))  result = "选择文件无效，文件或密钥校验错误 ";
		if ((sw1 == (byte)0x63))  result = "还可再试次数 "+ sw2;
		if ((sw1 == (byte)0x64) && (sw2 == (byte)0x00))  result = "状态标志未改变 ";
		if ((sw1 == (byte)0x65) && (sw2 == (byte)0x81))  result = "写 EEPROM不成功 ";
		if ((sw1 == (byte)0x60) && (sw2 == (byte)0x60))  result = "？？？？？ ";
		if ((sw1 == (byte)0x67) && (sw2 == (byte)0x00))  result = "错误的长度 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x00))  result = "CLA 与线路保护要求不匹配 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x01))  result = "无效的状态 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x81))  result = "命令与文件结构不相容 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x82))  result = "不满足安全状态 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x83))  result = "密钥被锁死 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x85))  result = "使用条件不满足 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x87))  result = "无安全报文 ";
		if ((sw1 == (byte)0x69) && (sw2 == (byte)0x88))  result = "安全报文数据项不正确 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x80))  result = "数据域参数错误 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x81))  result = "功能不支持或卡中无 MF 或卡片已锁定 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x82))  result = "文件未找到 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x83))  result = "记录未找到 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x84))  result = "文件无足够空间 ";
		if ((sw1 == (byte)0x6A) && (sw2 == (byte)0x86))  result = "参数 P1 P2 错误 ";
		if ((sw1 == (byte)0x6B) && (sw2 == (byte)0x00))  result = "在达到Le/Lc字节之前文件结束，偏移量错误 ";
		if ((sw1 == (byte)0x6C))   result = "Le 错误 "+ sw2;
		if ((sw1 == (byte)0x6E) && (sw2 == (byte)0x00))  result = "无效的 CLA ";
		if ((sw1 == (byte)0x6F) && (sw2 == (byte)0x00))  result = "数据无效 ";
		if ((sw1 == (byte)0x93) && (sw2 == (byte)0x02))  result = "MAC错误 ";
		if ((sw1 == (byte)0x93) && (sw2 == (byte)0x03))  result = "应用已被锁定 ";
		if ((sw1 == (byte)0x94) && (sw2 == (byte)0x01))  result = "金额不足 ";
		if ((sw1 == (byte)0x94) && (sw2 == (byte)0x03))  result = "密钥未找到 ";
		if ((sw1 == (byte)0x94) && (sw2 == (byte)0x06))  result = "所需的 MAC不可用";
		return result;
	}
}
