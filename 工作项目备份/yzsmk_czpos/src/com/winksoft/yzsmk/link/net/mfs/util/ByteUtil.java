package com.winksoft.yzsmk.link.net.mfs.util;
  
/** 
*  
* <ul> 
* <li>�ļ����: com.born.util.ByteUtil.java</li> 
* <li>�ļ�����: byteת������</li> 
* <li>��Ȩ����: ��Ȩ����(C)2001-2006</li> 
* <li>�� ˾: bran</li> 
 * <li>����ժҪ:</li> 
 * <li>����˵��:</li> 
 * <li>������ڣ�2011-7-18</li> 
 * <li>�޸ļ�¼0����</li> 
 * </ul> 
 *  
 * @version 1.0 
 * @author ������ 
 */  
public class ByteUtil {  
   /** 
    * ת��shortΪbyte 
    *  
    * @param b 
    * @param s 
    *            ��Ҫת����short 
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
   //Ĭ�ϴ��ģʽ0x1234 -> 12 34
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
    * ת��intΪbyte���� 
    *  
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
 
   /** 
    * ͨ��byte����ȡ��int 
    *  
    * @param bb 
    * @param index 
    *            �ڼ�λ��ʼ 
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
    * ת��long��Ϊbyte���� 
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
    * ͨ��byte����ȡ��long 
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
	 * �ֽ�ת��Ϊ����
	 * 
	 * @param b �ֽڣ�����4���ֽڣ�
	 * @param index ��ʼλ��
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

} 

