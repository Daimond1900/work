package com.winksoft.yzsmk.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import com.winksoft.yzsmk.cpu.Cpu_cardInfor;
import com.winksoft.yzsmk.cpu.Cpu_tradingInfor;
import com.winksoft.yzsmk.link.TransInforDesfire;

public class ByteUtils {

	private static final char[] HEX={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	
	/**
	 * 字节转换成HEX 字符串
	 * @param data
	 * @return
	 */
	public static String byteToHex(byte data){
		return String.valueOf(HEX[(data & 0xF0) >> 4])+ String.valueOf(HEX[data & 0x0F]);
	}
	/**
	 * 字节转换成HEX 字符串
	 * @param raw
	 * @return
	 */
	public static String byteToHex(byte... raw) {
		if(raw!=null) {
			return byteToHex(raw,0,raw.length);
		} else {
			return null;
		}
	}
	
	/**
	 * 字节转换成HEX 字符串
	 * @param raw
	 * @param offset
	 * @param count
	 * @return
	 */
	public static String byteToHex(byte[] raw,int offset,int count) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		
		offset = offset>(raw.length-1) ? 0 : offset;
		count = count> raw.length ? raw.length : count;
		int end = offset + count;
		
		for(int i=offset;i<end; i++){
		    byte b = raw[i];
			hex.append(HEX[(b & 0xF0) >> 4]).append(HEX[b & 0x0F]);
		}
		return hex.toString();
	}
	
	/**
	 * 打印 内容
	 * @param raw
	 * @param offset
	 * @param count
	 * @return
	 */
	public static String printBytes(byte[] raw,int offset,int count) {
		if (raw == null) {
			return " waring data is null!";
		}
		final StringBuilder hex = new StringBuilder();
		int len = raw.length;
		
		offset = offset>(len-1) ? len-1 : offset;
		
		int end = offset + count;
		end = end >len ? len : end;
		for(int i=offset;i<end; i++){
		    byte b = raw[i];
			hex.append(HEX[(b & 0xF0) >> 4]).append(HEX[b & 0x0F]).append(" ");
		}
		return hex.toString();
	}
	
	public static String printBytes1(byte[] raw,int offset,int count) {
		if (raw == null) {
			return " waring data is null!";
		}
		final StringBuilder hex = new StringBuilder();
		int len = raw.length;
		
		offset = offset>(len-1) ? len-1 : offset;
		
		int end = offset + count;
		end = end >len ? len : end;
		for(int i=offset;i<end; i++){
		    byte b = raw[i];
			hex.append(HEX[(b & 0xF0) >> 4]).append(HEX[b & 0x0F]).append("");
		}
		return hex.toString();
	}
	
	public static String printBytes(byte[] raw){
		if(raw!=null) {
			return printBytes1(raw,0,raw.length);
		} else {
			return null;
		}
	}
	public static String printBytes1(byte[] raw){
		if(raw!=null) {
			return printBytes(raw,0,raw.length);
		} else {
			return null;
		}
	}
	public static String printByte(byte b) {
		return String.valueOf(HEX[(b & 0xF0) >> 4]) + String.valueOf(HEX[b & 0x0F]);
	}
	
	/**
	 * Hex 字符串转换成字节数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByte(String hex){
		if(hex==null || "".equals(hex))
			return null;
		
		hex = hex.replaceAll(" ", "");
		
		int len = hex.length()/2;
		byte[] data = new byte[len];
		int offset=0;
		int at=0;
		int at1=0;
		for(int i=0;i<len;i++){
			offset  = i*2;
			at = asciiToHex(hex.charAt(offset));
			at1 = asciiToHex(hex.charAt(offset+1)) & 0xFF;
			data[i] = (byte)( at<<4 |at1);
		}
		return data;
	}
	
	private static int asciiToHex(int asc) {
		if(asc<=57) {
			return asc-48;
		} else if(asc<=70) {
			return asc-55;
		} else {
			return asc-87;
		}
	}
	
	/**
	 * BCD 转字符串
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 */
	public static String bcdToString(byte[] data,int offset,int len){
		StringBuilder sb = new StringBuilder(len *2 );
		int end = offset + len;
		for(int i=offset;i<end;i++){
			sb.append((data[i] & 0xF0)>>4).append(data[i] & 0x0F);
 		}
		return sb.toString();
	}
	
	/**
	 * 右靠BCD 
	 * @param value
	 * @return
	 */
	public static byte[] toAlignRightBcd(String value){
		byte[] buf = new byte[Math.round((float)(value.length()/(2.0)))];
		int charpos = 0;
		int bufpos = 0;
		if (value.length() % 2 == 1) {
			buf[0] = (byte)(value.charAt(0) - 48);
			charpos = 1;
			bufpos = 1;
		}
		while (charpos < value.length()) {
			buf[bufpos] = (byte)(((value.charAt(charpos) - 48) << 4)
					| (value.charAt(charpos + 1) - 48));
			charpos += 2;
			bufpos++;
		}
		
		return buf;
	}
	
	/**
	 * 左靠BCD 
	 * @param value
	 * @return
	 */
	public static byte[] toAlignLeftBcd(String value) {
		byte[] buf = new byte[Math.round((float)(value.length()/(2.0)))];
	 
		int len = value.length() / 2;
		for(int i=0; i<len ;i++) {
			buf[i] = (byte)(((value.charAt(i*2) - 48) << 4) | (value.charAt(i*2 + 1) - 48));
		}
		
		if (value.length() % 2 == 1) {
			buf[len] =(byte)( (value.charAt(value.length()-1)  - 48) << 4);
		}
		return buf;
	}
	
	/**
	 * 2位byte 转换成 int   高前低后
	 * @param bit1
	 * @param bit2
	 * @return
	 */
	public static int byteToInt_long(byte[] data) {
		int tmp = 0;
		if (data.length > 4){
			return 0;
		}
		else{
			for (byte i=0;i<data.length;i++){
				tmp += (data[i] & 0xff)<<((data.length-1-i)*8);
			}
			return tmp;
		}
	}
	
	/**
	 * 2位byte 转换成 int   高前低后
	 * @param bit1
	 * @param bit2
	 * @return
	 */
	public static int byteToInt(byte bit1,byte bit2) {
		return ((bit1 & 0xFF)<< 8) | (bit2 & 0xFF);
	}
	
	/**
	 * 低位优先(little-endian)
	 * @param bit1
	 * @param bit2
	 * @return
	 */
	public static int byteToIntLE(byte bit1,byte bit2) {
		return (bit1 & 0xFF) | ((bit2 & 0xFF) << 8);
	}
	
	public static int byteToInt(byte value) {
		return value & 0xFF;
	}
	
	/**
	 * 获取 2字节int  高前低后 高位优先(big-endian)
	 * @param value
	 * @return
	 */
	public static byte[] unsignedShort(int value) {
		byte[] data = new byte[2];
		data[0] = (byte)(value>>8);
		data[1] = (byte)(value);
		return data;
	}
	
	/**
	 * 低位优先(little-endian)
	 * @param value
	 * @return
	 */
	public static byte[] unsignedShortLE(int value) {
		byte[] data = new byte[2];
		data[0] = (byte) value;
		data[1] = (byte)(value>>8);
		return data;
	}
	
	
	public static byte[] unsignedInt(long value) {
		byte[] data = new byte[4];
		data[0] = (byte)(value >> 24);
		data[1] = (byte)(value >> 16);
		data[2] = (byte)(value >> 8);
		data[3] = (byte)(value);
		return data;
	}
	
	public static byte[] unsignedIntLE(long value) {
		byte[] data = new byte[4];
		data[0] = (byte)(value);
		data[1] = (byte)(value >> 8);
		data[2] = (byte)(value >> 16);
		data[3] = (byte)(value >> 24);
		return data;
	}
	
	
	/**
	 * 获取 LRC
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 */
	public static byte genLRC(byte[] data,int offset,int len) {
		byte lrc = 0;
		for(int i=offset,end=offset+len; i<end; i++) {
			lrc ^=data[i];
		}
		return lrc;
	}
	
	public static byte checkSumCal(byte[] data,int offset,int len) {
		int sum = 0;
		byte checkSum = 0;
		for(int i=offset,end=offset+len; i<end; i++) {
			sum +=data[i];
		}
		checkSum = (byte)sum;
		sum = (byte)0xff - (byte)checkSum+1;
		return (byte)sum;
	}
	
	 /** 
    * 转换short为byte
    * @param b 
    * @param s 
    *            需要转换的short 
    * @param index 
    */  
   public static void putShort(byte b[], short s, int index, boolean bigMode) {  
	   if (bigMode){
		   b[index + 0] = (byte) (s >> 8);  
	       b[index + 1] = (byte) (s >> 0);  
	   }
	   else{
	       b[index + 1] = (byte) (s >> 8);  
	       b[index + 0] = (byte) (s >> 0);  
	   }
   }
   
   //默认大端模式0x1234 -> 12 34
   public static void putShort(byte b[], short s, int index) {  
		   b[index + 0] = (byte) (s >> 8);  
	       b[index + 1] = (byte) (s >> 0);  
   }  
	 
   /** 
    * 通过byte数组取到short 
    * @param b 
    * @param index 
    *            第几位开始取 
    * @return 
    */  
   public static short getShort(byte[] b, int index, boolean bigMode) {  
	   if (bigMode){
		   return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
	   }else{
		   return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	   }
   }
   
   public static short getShort(byte[] b, int index) {  
	   return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
   } 
   
   
 
   /** 
    * 转换int为byte数组 
    * @param bb 
    * @param x 
    * @param index 
    */  
   public static void putInt(byte[] bb, int x, int index, boolean bigMode) { 
	   if (bigMode){
	       bb[index + 0] = (byte) (x >> 24);  
	       bb[index + 1] = (byte) (x >> 16);  
	       bb[index + 2] = (byte) (x >> 8);  
	       bb[index + 3] = (byte) (x >> 0);
	   }else{
		   bb[index + 3] = (byte) (x >> 24);  
	       bb[index + 2] = (byte) (x >> 16);  
	       bb[index + 1] = (byte) (x >> 8);  
	       bb[index + 0] = (byte) (x >> 0);
	   }
   }  
   
   public static void putInt(byte[] bb, int x, int index) { 
	       bb[index + 0] = (byte) (x >> 24);  
	       bb[index + 1] = (byte) (x >> 16);  
	       bb[index + 2] = (byte) (x >> 8);  
	       bb[index + 3] = (byte) (x >> 0);
   }  
   public static byte[] putInt(int x) { 
	   byte[] tmp = new byte[4];
	   tmp[0] = (byte) (x >> 24);  
	   tmp[1] = (byte) (x >> 16);  
	   tmp[2] = (byte) (x >> 8);  
	   tmp[3] = (byte) (x >> 0);
	return tmp;   
   }  
 
   /** 
    * 通过byte数组取到int 
    *  
    * @param bb 
    * @param index 
    *            第几位开始 
    * @return 
    */  
   public static int getInt(byte[] bb, int index, boolean bigMode) {  
	   if (bigMode){
	       return (int) ((((bb[index + 0] & 0xff) << 24)  
	               | ((bb[index + 1] & 0xff) << 16)  
	               | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
	   }else{
		   return (int) ((((bb[index + 3] & 0xff) << 24)  
	               | ((bb[index + 2] & 0xff) << 16)  
	               | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
	   }
   }  
   
   public static int getInt(byte[] bb, int index) {  
	       return (int) ((((bb[index + 0] & 0xff) << 24)  
	               | ((bb[index + 1] & 0xff) << 16)  
	               | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
   }
 
   /** 
    * 转换long型为byte数组 
    *  
    * @param bb 
    * @param x 
    * @param index 
    */  
   public static void putLong(byte[] bb, long x, int index, boolean bigMode) { 
	   if (bigMode){
		   bb[index + 0] = (byte) (x >> 56);  
		   bb[index + 1] = (byte) (x >> 48);  
		   bb[index + 2] = (byte) (x >> 40);  
	       bb[index + 3] = (byte) (x >> 32);  
	       bb[index + 4] = (byte) (x >> 24);  
	       bb[index + 5] = (byte) (x >> 16);  
	       bb[index + 6] = (byte) (x >> 8);  
	       bb[index + 7] = (byte) (x >> 0);
	   }else{
		   bb[index + 7] = (byte) (x >> 56);  
		   bb[index + 6] = (byte) (x >> 48);  
		   bb[index + 5] = (byte) (x >> 40);  
	       bb[index + 4] = (byte) (x >> 32);  
	       bb[index + 3] = (byte) (x >> 24);  
	       bb[index + 2] = (byte) (x >> 16);  
	       bb[index + 1] = (byte) (x >> 8);  
	       bb[index + 0] = (byte) (x >> 0);
	   }
   }  
   
   public static void putLong(byte[] bb, long x, int index) { 
		   bb[index + 0] = (byte) (x >> 56);  
		   bb[index + 1] = (byte) (x >> 48);  
		   bb[index + 2] = (byte) (x >> 40);  
	       bb[index + 3] = (byte) (x >> 32);  
	       bb[index + 4] = (byte) (x >> 24);  
	       bb[index + 5] = (byte) (x >> 16);  
	       bb[index + 6] = (byte) (x >> 8);  
	       bb[index + 7] = (byte) (x >> 0);
   }  
   /** 
    * 通过byte数组取到long 
    *  
    * @param bb 
    * @param index 
    * @return 
    */  
   public static long getLong(byte[] bb, int index, boolean bigMode) {  
	   if (bigMode)
       		return ((((long) bb[index + 0] & 0xff) << 56)  
                | (((long) bb[index + 1] & 0xff) << 48)  
                | (((long) bb[index + 2] & 0xff) << 40)  
                | (((long) bb[index + 3] & 0xff) << 32)  
                | (((long) bb[index + 4] & 0xff) << 24)  
                | (((long) bb[index + 5] & 0xff) << 16)  
                | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
	   else
		   return ((((long) bb[index + 7] & 0xff) << 56)  
	                | (((long) bb[index + 6] & 0xff) << 48)  
	                | (((long) bb[index + 5] & 0xff) << 40)  
	                | (((long) bb[index + 4] & 0xff) << 32)  
	                | (((long) bb[index + 3] & 0xff) << 24)  
	                | (((long) bb[index + 2] & 0xff) << 16)  
	                | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
    }  
   
   public static long getLong(byte[] bb, int index) {  
       		return ((((long) bb[index + 0] & 0xff) << 56)  
                | (((long) bb[index + 1] & 0xff) << 48)  
                | (((long) bb[index + 2] & 0xff) << 40)  
                | (((long) bb[index + 3] & 0xff) << 32)  
                | (((long) bb[index + 4] & 0xff) << 24)  
                | (((long) bb[index + 5] & 0xff) << 16)  
                | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));

    }  

	/**
	 * 字节转换为浮点
	 * 
	 * @param b 字节（至少4个字节）
	 * @param index 开始位置
	 * @return
	 */
	public static float getFloat(byte[] b, int index, boolean bigMode) {  
	    int l;     
	    if (bigMode){
		    l = b[index + 0];                                
		    l &= 0xff;                                       
		    l |= ((long) b[index + 1] << 8);                 
		    l &= 0xffff;                                     
		    l |= ((long) b[index + 2] << 16);                
		    l &= 0xffffff;                                   
		    l |= ((long) b[index + 3] << 24);
	    }else{
	    	l = b[index + 3];                                
		    l &= 0xff;                                       
		    l |= ((long) b[index + 2] << 8);                 
		    l &= 0xffff;                                     
		    l |= ((long) b[index + 1] << 16);                
		    l &= 0xffffff;                                   
		    l |= ((long) b[index + 0] << 24);
	    }
	    return Float.intBitsToFloat(l);                  
	}
	
	public static float getFloat(byte[] b, int index) {  
	    int l;     
		    l = b[index + 0];                                
		    l &= 0xff;                                       
		    l |= ((long) b[index + 1] << 8);                 
		    l &= 0xffff;                                     
		    l |= ((long) b[index + 2] << 16);                
		    l &= 0xffffff;                                   
		    l |= ((long) b[index + 3] << 24);

	    return Float.intBitsToFloat(l);                  
	}
	public static String getString(byte[] data, int srcPos , int length){
		if (length<=0) return "";
		byte[] n = new byte[length];
		System.arraycopy(data, srcPos, n, 0, length);
		try {
			return new String(data, "GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			return "Convert to GB2312 Error";
		}
	}
	
	public static byte[] getBytes(byte[] data, int srcPos , int length)
	{
		byte[] tmp = new byte[length];
		System.arraycopy(data, srcPos, tmp, 0, length);
		return tmp;
	}
	
	public static byte[] byte_H2L(byte[] data){
		byte[] tmp = new byte[data.length];	
		for(int i=0;i<data.length;i++){
			tmp[i] = data[data.length - i - 1];
		}
		return tmp;
	}

	public static int bcd_to8Int(byte BcdData){
		int result;
		
		result = BcdData & 0x0F;
		result += ((BcdData >> 4) & 0x0F) *10;
		return(result);
	}
	
	public static int bcd_toInt(byte[] data_ptr){
		int tmp = 0;
		for (int i=0;i<data_ptr.length;i++){
			tmp = tmp*100 + bcd_to8Int(data_ptr[i]);
		}
		return tmp;
	}
	
	public static String getMoney(int money) {
		String filesize = String.valueOf(money);
		float size = Float.parseFloat(filesize) / (100);
		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		return decimalFormat.format(size);// format 返回的是字符串
	}
	
	public static byte[] byteCopy(byte src,byte[] drs,int doffset){
		if (doffset <= drs.length){
			ByteBuffer tmp = ByteBuffer.allocate(drs.length+1);
			if (doffset == 0){
				tmp.put(src)
				   .put(drs);
			}else{
				tmp.put(drs,0,doffset)
				   .put(src)
				   .put(drs,doffset,drs.length-doffset);
			}
			return tmp.array();
				
		}
		else
			return drs;
	}
	
	public static byte[] byteCopy(byte[] src,int soffset,byte[] drs,int doffset,int length){
		if (doffset <= drs.length){
			ByteBuffer tmp = ByteBuffer.allocate(drs.length+length);
			if (doffset == 0){
				tmp.put(src,soffset,length)
				   .put(drs);
			}else{
				tmp.put(drs,0,doffset)
				   .put(src,soffset,length)
				   .put(drs,doffset,drs.length-doffset);
			}
			return tmp.array();
				
		}
		else
			return drs;
	}
	
	public static byte[] intToBCD(long vlaue,int len){
		byte[] tmp = new byte[len];
		byte i = (byte) (vlaue%10);
		byte j = (byte)((vlaue/10)%10);
		byte k = (byte)((vlaue/100)%10);
		byte m = (byte)((vlaue/1000)%10);
		byte n = (byte)((vlaue/10000)%10);
		byte o = (byte)((vlaue/100000)%10);
		byte p = (byte)((vlaue/1000000)%10);
		byte q = (byte)((vlaue/10000000)%10);
		
		if (len == 2){
			tmp[0] = (byte)(m*16 + k);
			tmp[1] = (byte)(j*16 + i);
		}else if (len == 3){
			tmp[0] = (byte)(o*16 + n);
			tmp[1] = (byte)(m*16 + k);
			tmp[2] = (byte)(j*16 + i);
		}else if (len == 4){
			tmp[0] = (byte)(q*16 + p);
			tmp[1] = (byte)(o*16 + n);
			tmp[2] = (byte)(m*16 + k);
			tmp[3] = (byte)(j*16 + i);
		}
		return tmp;
	}
	
	public static byte Num2Char(byte Oct)
	{
		Oct = (byte)(Oct & 0x0F);
		if (Oct > 9)
			return (byte)( Oct + 0x37);
		else
			return (byte)( Oct + 0x30);
	}
	
	public static byte[]  HexToAsc(byte[] hex_buf, int hex_len)
	{
		byte[] tmp = new byte[hex_len*2];
		
		if (hex_len == 0)
			return null;
	    for (int i=0; i<hex_len; i++)
	    {
	    	tmp[i*2] = Num2Char((byte)(hex_buf[i] >> 4));
	    	tmp[i*2+1] = Num2Char((byte)(hex_buf[i]));
	    }
	    return tmp;
	}
}
