package com.winksoft.yzsmk.link.net.mfs.util;

import android.annotation.SuppressLint;

public class YFConvert {
	public static byte[]  getSubBuffer(byte[] buffer, int srcPos, int length){
		byte[] tmp = new byte[length];
		System.arraycopy(buffer, srcPos, tmp, 0, length);
		return tmp;
	}
	
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv+" ");
		}
		return stringBuilder.toString();
	}
	
	@SuppressLint("DefaultLocale")
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            
        }
        return d;
    }
	
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    public static byte[] hexArrayToBytes(byte[] arr, int start, int len) {
        if (arr == null) {
            return null;
        }
        int length = len / 2;
        byte d[] = new byte[length];
        int pos=0;
        for (int i = 0; i < length; i++) {
            pos = i * 2 +start;
            d[i] = (byte) (charToByte((char)arr[pos]) << 4 | charToByte((char)arr[pos + 1]));
        }
        return d;
    }
    
  
    public static byte getByte(byte[] b, int index) {  
        return (byte) (b[index + 0] & 0xff);  
    }  
    
    
    
    /** 
     * ת��shortΪbyte 
     *  
     * @param b 
     * @param s 
     *            ��Ҫת����short 
     * @param index 

     */  
    public static void putShort(byte b[], short s, int index) {  
        b[index + 0] = (byte) (s >> 8);  
        b[index + 1] = (byte) (s >> 0);  
    }  
  
    /** 
     * ͨ��byte����ȡ��short 
     *  
     * @param b 
     * @param index 
     *            �ڼ�λ��ʼȡ 
     * @return 
     */  
    public static short getShort(byte[] b, int index) {  
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));  
    }  
  
    /** 
     * ת��intΪbyte���� 
     *  
     * @param bb 
     * @param x 
     * @param index 
     */  
    public static void putInt(byte[] bb, int x, int index) {  
        bb[index + 0] = (byte) (x >> 24);  
        bb[index + 1] = (byte) (x >> 16);  
        bb[index + 2] = (byte) (x >> 8);  
        bb[index + 3] = (byte) (x >> 0);  
    }  
  
    /** 
     * ͨ��byte����ȡ��int 
     *  
     * @param bb 
     * @param index 
     *            �ڼ�λ��ʼ 
     * @return 
     */  
    public static int getInt(byte[] bb, int index) {  
        return (int) ((((bb[index + 0]) << 24)  
                | ((bb[index + 1]) << 16)  
                | ((bb[index + 2]) << 8) | bb[index + 3] & 0xff));  
    }  
  
    /** 
     * ת��long��Ϊbyte���� 
     *  
     * @param bb 
     * @param x 
     * @param index 
     */  
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
     * ͨ��byte����ȡ��long 
     *  
     * @param bb 
     * @param index 
     * @return 
     */  
    public static long getLong(byte[] bb, int index) {  
         return ((((long) bb[index + 0] & 0xff) << 56)  
                 | (((long) bb[index + 1] & 0xff) << 48)  
                 | (((long) bb[index + 2] & 0xff) << 40)  
                 | (((long) bb[index + 3] & 0xff) << 32)  
                 | (((long) bb[index + 4] & 0xff) << 24)  
                 | (((long) bb[index + 5] & 0xff) << 16)  
                 | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));  
     }  
}
