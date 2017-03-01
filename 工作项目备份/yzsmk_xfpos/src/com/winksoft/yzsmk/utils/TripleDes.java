package com.winksoft.yzsmk.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


public class TripleDes {
	
	public static final int MODE_3DES = 0;
	public static final int MODE_DES = 1;
	private static TripleDes mInstance = null;
	private int mMode = 0;
	private Cipher mCipher = null;
	private SecretKey mSecurekey = null;
	private SecureRandom mSecureRandom = null;
	
	private TripleDes() {
	}
	
	public static TripleDes getInstance() {
		if (mInstance == null) {
			mInstance = new TripleDes();
		}
		return mInstance;
	}
	
	private void init(byte[] key, int mode) {
		try {
			
			mSecureRandom = new SecureRandom();
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			mSecurekey =keyFactory.generateSecret(dks);
			if (mMode == MODE_3DES) {
				mCipher  = Cipher.getInstance("DESede");
			} else {
				mCipher  = Cipher.getInstance("DES");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] getKeyAsBytes(String key) {
	    byte[] keyBytes = new byte[24]; // a Triple DES key is a byte[24] array

	    for (int i = 0; i < key.length() && i < keyBytes.length; i++) 
	        keyBytes[i] = (byte) key.charAt(i);

	    return keyBytes;
	}
	
	public byte[] StagXOR(byte[] init,byte[] input)
	{
		int	  i, j;
		byte[] output = new byte[8];
		for (i = 0; i<8; i++)
			output[i] = init[i];

		for(i=0;i<input.length/8;i++){
			for(j=0;j<8;j++){
				output[j] = (byte)(input[i*8+j] ^ output[j]);
			}
		}
		return output;
	}
	
	public byte[] des(byte[] input, byte[] key, boolean encrypt){
		init(key, MODE_DES);
		byte[] result = null;
		try {
			if (encrypt)
				mCipher.init(Cipher.ENCRYPT_MODE, mSecurekey, mSecureRandom);
			else
				mCipher.init(Cipher.DECRYPT_MODE, mSecurekey, mSecureRandom);
			result = mCipher.doFinal(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] tripleDes(byte[] input, byte[] key, boolean encrypt){
		byte[] newKey = new byte[24];
		System.arraycopy(key, 0, newKey, 0, 16);
		System.arraycopy(key, 0, newKey, 16, 8);
		init(newKey, MODE_3DES);
		byte[] result = null;
		try {
			if (encrypt)
				mCipher.init(Cipher.ENCRYPT_MODE, mSecurekey, mSecureRandom);
			else
				mCipher.init(Cipher.DECRYPT_MODE, mSecurekey, mSecureRandom);
			result = mCipher.doFinal(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] PBOCMac(byte[] input, byte[] Key)
	{
		byte[] src = new byte[8];
		byte[] output = new byte[8];
		int i,j;
		for (i=0; i<8; i++)	src[i] = input[i];
		for (i=0; i<input.length/8;i++)
		{
			for (j=0;j<8;j++)
				src[j] = (byte) (src[j] ^ input[i*8+j]);
			output = des(src, Key, true); //加密
			System.arraycopy(output, 0, src, 0, 8);//保存异或结果           
		}
		return output;
	}

	
	public byte[] DES_CBC(byte[] input, byte[] init, byte[] key, boolean bTriple, boolean encrypt)
	{
	  byte[] initMac = new byte[]{0,0,0,0,0,0,0,0};
	  byte[] temp = new byte[8];
	  byte[] output = new byte[input.length/8*8];
	  int i,j;
	
	  //设置初始向量
	  if (init != null) 
		  System.arraycopy(init[0], 0, initMac, 0, 8);

	  for (i = 0; i<input.length / 8; i++)
	  {
		  byte[] data = new byte[8];
		  System.arraycopy(input, i*8, data, 0, 8);
		  initMac = StagXOR(initMac, data); //异或
		  if (bTriple)
			  temp = tripleDes(initMac, key, encrypt);
		  else
			  temp = des(initMac, key, encrypt);
		  System.arraycopy(temp, 0, output, i*8, 8);
		  System.arraycopy(temp, 0, initMac, 0, 8);
	  }
	  return output;
	}

	
}