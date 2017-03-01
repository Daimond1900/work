package com.winksoft.yzsmk.desfire;

import java.nio.ByteBuffer;

import android.util.Log;

import com.winksoft.yzsmk.card.DesfireCard;
import com.winksoft.yzsmk.card.PsamException;
import com.winksoft.yzsmk.card.PsamJsb;
import com.winksoft.yzsmk.link.TransInforDesfire;
import com.winksoft.yzsmk.utils.ByteUtils;

public class PsamInfor extends PsamJsb{
	public static byte[] file15_uid;//[10];//1-10	PSAM序列号	10	cn
	public static byte[] file15_ver;//11	PSAM版本号	1	b
	public static byte[] file15_type;//12	密钥卡类型	1	b
	public static byte[] file15_fci;//[2];//13-14	发卡方自定义FCI数据	2	b
	
	public static byte[] file16_terid;	// [6] 6字节
	public static byte[] subKey;//[6];// 子密钥	2	b
	
	public PsamInfor(){
		file15_uid=null;//[10];//1-10	PSAM序列号	10	cn
		file15_ver=null;//11	PSAM版本号	1	b
		file15_type=null;//12	密钥卡类型	1	b
		file15_fci=null;//[2];//13-14	发卡方自定义FCI数据	2	b		
		subKey=null;//[6];// 子密钥	2	b
		file16_terid = null;//
	}
	
	public boolean setFile15(byte[] file15){
		boolean returnflag = false;
		if (file15.length == 14){
			int prt=0;
			file15_uid = ByteUtils.getBytes(file15,prt,10);prt += 10;//1-10	PSAM序列号	10	cn
			file15_ver = ByteUtils.getBytes(file15,prt,1);prt += 1;//11	PSAM版本号	1	b
			file15_type = ByteUtils.getBytes(file15,prt,1);prt += 1;//12	密钥卡类型	1	b
			file15_fci = ByteUtils.getBytes(file15,prt,2);prt += 2;	//[2];//13-14	发卡方自定义FCI数据	2	b
			returnflag = true;
		}
			
		return returnflag;	
	}
	
	public boolean setFile16(byte[] terid){
		boolean returnflag = false;
		if (terid.length == 6){
			file16_terid = ByteUtils.getBytes(terid,0,6);// 6 字节 终端ID
			
			ByteBuffer writeFile = ByteBuffer.allocate(8);
			writeFile.put((byte)0x00) 	// 前面填充两字节 00	
				.put((byte)0x00)					
				.put(file16_terid,0,file16_terid.length);
			TransInforDesfire.terminal_no_41 = writeFile.array();
			returnflag = true;
		}
		return returnflag;	
	}
	
	public boolean getSubKey(PsamJsb psam) throws PsamException{
		ByteBuffer inputBuf = ByteBuffer.allocate(13);
		inputBuf.put(DesFireFileInfor.file0F_city_code)	// 城市代码 2
			.put(ByteUtils.getBytes(DesFireFileInfor.uid,3,4))						// cx//卡内唯一代码 SN3-SN6		
			.put(ByteUtils.getBytes(DesFireFileInfor.file0F_Card_internal_number,2,2))						// 卡内号 后2个字节
			.put(DesFireFileInfor.file0F_Card_authentication_code)						// 卡认证码 4字节
			.put((byte)0x11);						// 0x11
		
		byte[] tmp;
		Log.i("desfire---711", "PSAM");
		tmp = psam.selectFileById(new byte[]{(byte)0x10, (byte)0x05});
		Log.i("desfire---712", "PSAM");
		
		subKey = psam.calculateKey((byte)0x01, inputBuf.array());
		Log.i("desfire---713", "PSAM");
		return false;
	}
}
