package com.winksoft.yzsmk.card;

import java.nio.ByteBuffer;
import android.util.Log;
import com.winksoft.yzsmk.utils.DesUtils;

public class DesfireCard {
	// DESFIRE指令，调用读卡器组包
	byte[] FSessionKey = new byte[16];

	// 选择卡片应用目录
	// 函数功能：选择AID指定的卡片应用目录
	// 输入参数：AID 应用目录ID例如:"000001"
	// 输出参数：选择结果
	public int rats(byte param) throws DesfireException {
		byte[] sendBuf;
		sendBuf = new byte[] { CMD_RATS, param };

		byte[] tmp = transmit(sendBuf);
		if (tmp != null)
			return (tmp[0] & 0xff);
		else
			return 0;
	}

	public int selectApplication(byte[] AID) throws DesfireException {
		ByteBuffer buff = ByteBuffer.allocate(AID.length + 1);
		buff.put(CMD_SELECT_APPLICATION).put(AID);

		byte[] tmp = transmit(buff.array());
		if (tmp != null)
			return (tmp[0] & 0xff);
		else
			return 0;
	}

	public byte[] transmit(byte send[]) throws DesfireException {
		return null;
	}

	// DESFIRE卡片双向认证
	// 函数功能：PCD与PCII进行双向认证，可以采用DES或3DES算法，并产生SessionKey(8Byte或16byte)
	// 输入参数：KeyNo 认证所使用的密钥索引（PCD与PCII在密钥机制上需要保持一致)
	// bTriple 使用3Des还是Des算法， true 3Des, false Des
	// pDesKey 密钥，3DES时密钥长度16字节
	// 输出参数：sSessionKey，会话密钥（PCD与PCII之间数据传输的会话密钥，如MAC的计算)
	public byte[] authenticate(byte KeyNo, byte[] pdesKey) throws Exception {
		byte[] RNDB = new byte[8];
		byte[] RNDA = new byte[8];
		byte[] RNDC = new byte[8];
		byte[] RNDB1 = new byte[8];
		byte[] RNDA1 = new byte[8];
		byte[] RNDC1 = new byte[8];
		byte[] RNDB_PLAIN;
		byte[] RNDA_PLAIN = new byte[] { (byte) 0x11, (byte) 0x22, (byte) 0x33,
				(byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88 };

		byte[] RNDA_PLAIN1 = new byte[8];
		byte[] RNDA_B = new byte[16];
		int i;
		// 1、取IC卡随机数
		byte[] sendBuf = new byte[] { CMD_AUTHENTICATE, KeyNo };
		Log.i("desfire---8","123");
		byte[] recvBuf = transmit(sendBuf);
		if (recvBuf[0] != (byte) 0xAF) {
			throw new DesfireException(recvBuf[0], "认证失败");
		}
		Log.i("desfire---9","123");
		System.arraycopy(recvBuf, 1, RNDB, 0, 8);
		// 2 3DES解密运算RNDB得到RNDB_PLAIN，整体左移8位转换成RNDB1
		RNDB_PLAIN = DesUtils.decrypt(RNDB, pdesKey);
		for (i = 0; i < 8; i++)
			RNDB1[i] = RNDB_PLAIN[(i + 1) % 8];

		Log.i("desfire---91","123");
		// 3 3DES解密运算RNDA得到RNDA_PLAIN
		RNDA1 = DesUtils.decrypt(RNDA_PLAIN, pdesKey);

		Log.i("desfire---92","123");
		// 4 RNDB1 ^ RNDA1 得到 RNDC1
		for (i = 0; i < 8; i++) {
			RNDC[i] = (byte) (RNDA1[i] ^ RNDB1[i]);
		}
		RNDC1 = DesUtils.decrypt(RNDC, pdesKey);

		Log.i("desfire---93","123");
		// 5 向卡片PICC发送认证请求
		System.arraycopy(RNDA1, 0, RNDA_B, 0, 8);
		System.arraycopy(RNDC1, 0, RNDA_B, 8, 8);
		ByteBuffer buff = ByteBuffer.allocate(17);
		buff.put(CMD_ADDITIONAL_FRAME).put(RNDA_B);

		Log.i("desfire---94","123");
		for (int times = 0; times < 2; times++) {
			Log.i("desfire---944","123");
			recvBuf = transmit(buff.array());
			if (recvBuf[0] == 0x00)
				break;
		}
		Log.i("desfire---95","123");
//		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0x00) {
			throw new DesfireException(recvBuf[0], "认证失败"
					+ getErrorMsg(recvBuf[0] & 0xFF));
		}

		Log.i("desfire---96","123");
		// 6
		// 解密RNDA’，或得RNDA"_plain,整体各移8位转换得RNDA_PLAIN，验证RNDA_PLAIN与原RNDA_PLAIN是否相同
		System.arraycopy(recvBuf, 1, RNDA, 0, 8);
		RNDA_PLAIN1 = DesUtils.decrypt(RNDA, pdesKey);
		for (i = 0; i < 8; i++) {
			if (RNDA_PLAIN[(i + 1) % 8] != RNDA_PLAIN1[i]) {
				throw new DesfireException(recvBuf[0], "认证失败 MAC ERROR"
						+ getErrorMsg(recvBuf[0] & 0xFF));
			}
		}

		Log.i("desfire---10","123");
		// 6 将RNDA与RNDB组成会话密钥
		byte[] FSessionKey = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0 };
		System.arraycopy(RNDA_PLAIN, 0, FSessionKey, 0, 4);
		System.arraycopy(RNDB_PLAIN, 0, FSessionKey, 4, 4);
		System.arraycopy(RNDA_PLAIN, 4, FSessionKey, 8, 4);
		System.arraycopy(RNDB_PLAIN, 4, FSessionKey, 12, 4);
		return FSessionKey;
	}

	// public byte[] authenticate(byte KeyNo, byte[] pdesKey)
	// throws DesfireException {
	// byte[] RNDB = new byte[8];
	// byte[] RNDA = new byte[8];
	// byte[] RNDB1 = new byte[8];
	// byte[] RNDA_PLAIN1 = new byte[8];
	// byte[] RNDB_PLAIN;
	// byte[] RNDA_PLAIN = new byte[8];
	// byte[] RNDA_B = new byte[16];
	// int i;
	// TripleDes en = TripleDes.getInstance();
	// byte[] sendBuf = new byte[] { CMD_AUTHENTICATE, KeyNo };
	//
	// // 1 向卡片PICC发送认证请求
	// byte[] recvBuf = transmit(sendBuf);
	//
	// if (recvBuf[0] != (byte) 0xAF) {
	// throw new DesfireException(recvBuf[0], "认证失败");
	// }
	//
	// // 2 3DES解密运算RNDB得到RNDB_PLAIN，整体左移8位转换成RNDB’
	// System.arraycopy(recvBuf, 1, RNDB, 0, 8);
	// RNDB_PLAIN = en.tripleDes(RNDB, pdesKey, true);
	// for (i = 0; i < 8; i++)
	// RNDB1[i] = RNDB_PLAIN[(i + 1) % 8];
	//
	// // 3 产生RNDA_PLAIN
	// Random random = new Random();
	// random.nextBytes(RNDA_PLAIN);
	//
	// // 4 将RNDA_PLAIN与RNDB‘连接，采用3DES-CBC解密运算De(RNDA+RNDB")，结果送PICC
	// System.arraycopy(RNDA_PLAIN, 0, RNDA_B, 0, 8);
	// System.arraycopy(RNDB1, 0, RNDA_B, 8, 8);
	//
	// RNDA_B = en.DES_CBC(RNDA_B, null, pdesKey, true, false);
	// // OutputDebugString(pchar("RNDA,RNDB1:" + CharToHexChar(@RNDA_B[0], 16,
	// // false)));
	// ByteBuffer buff = ByteBuffer.allocate(17);
	// buff.put(CMD_ADDITIONAL_FRAME).put(RNDA_B);
	//
	// recvBuf = transmit(sendBuf);
	// if (recvBuf[0] != 0x00) {
	// throw new DesfireException(recvBuf[0], "认证失败"
	// + getErrorMsg(recvBuf[0] & 0xFF));
	// }
	//
	// // 5
	// // 解密RNDA’，或得RNDA"_plain,整体各移8位转换得RNDA_PLAIN，验证RNDA_PLAIN与原RNDA_PLAIN是否相同
	// System.arraycopy(recvBuf, 1, RNDA, 0, 8);
	// RNDA_PLAIN1 = en.tripleDes(RNDA, pdesKey, false);
	// for (i = 0; i < 8; i++) {
	// if (RNDA_PLAIN[(i + 1) % 8] != RNDA_PLAIN1[i]) {
	// throw new DesfireException(recvBuf[0], "认证失败 MAC ERROR"
	// + getErrorMsg(recvBuf[0] & 0xFF));
	// }
	// }
	//
	// // 6 将RNDA与RNDB组成会话密钥
	// byte[] FSessionKey = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	// 0, 0, 0, 0 };
	// System.arraycopy(RNDA_PLAIN, 0, FSessionKey, 0, 4);
	// System.arraycopy(RNDB_PLAIN, 0, FSessionKey, 4, 4);
	// System.arraycopy(RNDA_PLAIN, 4, FSessionKey, 8, 4);
	// System.arraycopy(RNDB_PLAIN, 4, FSessionKey, 12, 4);
	// return FSessionKey;
	// }

	// DESFIRE卡片双向认证
	// 函数功能：PCD与PCII进行双向认证，可以采用DES或3DES算法，并产生SessionKey(8Byte或16byte)
	// 输入参数：KeyNo 认证所使用的密钥索引（PCD与PCII在密钥机制上需要保持一致)
	// bTriple 使用3Des还是Des算法， true 3Des, false Des
	// pDesKey 密钥，3DES时密钥长度16字节
	// 输出参数：sRNDB，会话密钥（PCD与PCII之间数据传输的会话密钥，如MAC的计算)
	// 返回值 ：0认证成功，其它失败
	public byte[] auth1(byte keyNo) throws DesfireException {
		byte[] sendBuf, recvBuf;
		// 1 向卡片PICC发送认证请求
		sendBuf = new byte[] { CMD_AUTHENTICATE, keyNo };
		recvBuf = transmit(sendBuf);
		if (recvBuf[0] != 0xAF) {
			throw new DesfireException(recvBuf[0], "Auth1: "
					+ getErrorMsg(recvBuf[0] & 0xFF));
		}
		byte[] RNDB = new byte[8];
		System.arraycopy(recvBuf, 1, RNDB, 0, 8);
		return RNDB;
	}

	// DESFIRE卡片双向认证
	// 函数功能：PCD与PCII进行双向认证，可以采用DES或3DES算法，并产生SessionKey(8Byte或16byte)
	// 输入参数：KeyNo 认证所使用的密钥索引（PCD与PCII在密钥机制上需要保持一致)
	// bTriple 使用3Des还是Des算法， true 3Des, false Des
	// pDesKey 密钥，3DES时密钥长度16字节
	// 输出参数：sSessionKey，会话密钥（PCD与PCII之间数据传输的会话密钥，如MAC的计算)
	// 返回值 ：0认证成功，其它失败
	public byte[] auth2(byte[] RNDAB) throws DesfireException {

		byte[] recvBuf;
		ByteBuffer buff = ByteBuffer.allocate(17);
		buff.put(CMD_ADDITIONAL_FRAME).put(RNDAB);

		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0x00) {
			throw new DesfireException(recvBuf[0], "认证失败2  "
					+ getErrorMsg(recvBuf[0] & 0xFF));
		}
		byte[] RNDA = new byte[8];
		System.arraycopy(recvBuf, 1, RNDA, 0, 8);
		return RNDA;
	}

	// 读取文件
	// 函数说明：读取指令文件内容，在读取文件前首先要选择相应的目录，并通过相应的认证
	// 输入参数：FileNo文件ID号
	// offSet读取偏移量
	// len读取长度
	// 输出参数：data 读取到的数据，以十六进制字符串型式返回
	// 返回值 ：0表示成功，其它表示失败
	public byte[] readData(byte FileNo, int offset, int len)
			throws DesfireException {
		byte[] sendBuf, recvBuf;
		sendBuf = new byte[] { CMD_READ_DATA, FileNo, (byte) (offset & 0xff),
				(byte) ((offset >> 8) & 0xff), (byte) ((offset >> 16) & 0xff),
				(byte) (len & 0xff), (byte) ((len >> 8) & 0xff),
				(byte) ((len >> 16) & 0xff), };

		recvBuf = transmit(sendBuf);
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "ReadData "
					+ getErrorMsg(recvBuf[0]));
		}
		byte[] data = new byte[recvBuf.length - 1];
		System.arraycopy(recvBuf, 1, data, 0, recvBuf.length - 1);
		return data;
	}

	// 读取文件
	// 函数说明：读取指令值文件，在读取文件前首先要选择相应的目录，并通过相应的认证
	// 输入参数：FileNo文件ID号
	// 输出参数：data 读取到的数据，以十六进制字符串型式返回
	// 返回值 ：0表示成功，其它表示失败
	public int getValue(byte fileNo, byte[] key) throws DesfireException {
		byte[] sendBuf, recvBuf;
		sendBuf = new byte[] { CMD_GET_VALUE, fileNo };
		recvBuf = transmit(sendBuf);
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "getValue "
					+ getErrorMsg(recvBuf[0]));
		}

		// 验证MAC的正确性
		byte[] mac = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		byte[] mac1 = new byte[8];
		System.arraycopy(recvBuf, 1, mac, 0, 4);
		try {
			mac1 = DesUtils.encrypt(mac, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < 4; i++) {
			if (mac1[i] != recvBuf[i + 4 + 1])
				break;
		}

		// return ((recvBuf[1] & 0XFF) << 24) | ((recvBuf[2] & 0XFF) << 16)
		// | ((recvBuf[3] & 0XFF) << 8) | (recvBuf[4] & 0XFF);
		return ((recvBuf[4] & 0XFF) << 24) | ((recvBuf[3] & 0XFF) << 16) // 倒叙返回
				| ((recvBuf[2] & 0XFF) << 8) | (recvBuf[1] & 0XFF);
	}

	public void credit(byte fileNo, int amount, byte[] MAC)
			throws DesfireException {
		byte[] recvBuf;
		ByteBuffer buff = ByteBuffer.allocate(10);
		// CREDIT
		buff.put(CMD_CREDIT).put(fileNo).put((byte) (amount & 0xff))
				.put((byte) ((amount >> 8) & 0xff))
				.put((byte) ((amount >> 16) & 0xff))
				.put((byte) ((amount >> 32) & 0xff)).put(MAC);

		recvBuf = transmit(buff.array());

		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "credit "
					+ getErrorMsg(recvBuf[0]));
		}
	}

	// 扣除指定值
	// 函数功能：操作值文件，减少值文件的数值
	// 输入参数：FileNo值文件ID号
	// amount减少值
	// 输出参数：无
	// 返回参数：0表示成功其它失败
	public void debit(byte fileNo, int amount, byte[] key)
			throws DesfireException {
		byte[] recvBuf;
		ByteBuffer buff = ByteBuffer.allocate(10);
		byte[] a = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		a[0] = (byte) (amount & 0xff);
		a[1] = (byte) ((amount >> 8) & 0xff);
		a[2] = (byte) ((amount >> 16) & 0xff);
		a[3] = (byte) ((amount >> 24) & 0xff);

		byte[] MAC = null;

		try {
			MAC = DesUtils.encrypt(a, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// CREDIT
		buff.put(CMD_DEBIT).put(fileNo).put(a, 0, 4).put(MAC, 0, 4);

		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "debit "
					+ getErrorMsg(recvBuf[0] & 0xFF));
		}
	}

	public void commitTranscation() throws DesfireException {
		byte[] sendBuf, recvBuf;
		sendBuf = new byte[] { CMD_COMMIT_TRANSCATION };
		recvBuf = transmit(sendBuf);
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "commitTranscation"
					+ getErrorMsg(recvBuf[0] & 0xff));
		}
	}

	public void abortTranscation() throws DesfireException {
		byte[] sendBuf, recvBuf;
		sendBuf = new byte[] { CMD_ABORT_TRANSCATION };
		recvBuf = transmit(sendBuf);
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "abortTranscation "
					+ getErrorMsg(recvBuf[0] & 0xff));
		}
	}

	public void writeRecord(byte fileNo, int offset, int len, byte[] data)
			throws DesfireException {
		byte[] recvBuf;
		ByteBuffer buff = ByteBuffer.allocate(8 + data.length);
		buff.put(CMD_WRITE_RECORD).put(fileNo).put((byte) (offset & 0xff))
				.put((byte) ((offset >> 8) & 0xff))
				.put((byte) ((offset >> 16) & 0xff)).put((byte) (len & 0xff))
				.put((byte) ((len >> 8) & 0xff))
				.put((byte) ((len >> 16) & 0xff)).put(data);

		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "writeRecord"
					+ getErrorMsg(recvBuf[0] & 0xff));
		}
	}

	public void writeData(byte fileNo, int offset, int len, byte[] data)
			throws DesfireException {
		byte[] recvBuf;
		ByteBuffer buff = ByteBuffer.allocate(8 + data.length);
		buff.put(CMD_WRITE_DATA).put(fileNo).put((byte) (offset & 0xff))
				.put((byte) ((offset >> 8) & 0xff))
				.put((byte) ((offset >> 16) & 0xff)).put((byte) (len & 0xff))
				.put((byte) ((len >> 8) & 0xff))
				.put((byte) ((len >> 16) & 0xff)).put(data);

		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "writeData"
					+ getErrorMsg(recvBuf[0] & 0xff));
		}
	}

	// 读取记录文件
	// 函数功能: 一次读取所有交易记录文件
	// 输入参数：fileNo文件ID号
	// offset读取的记录内容的偏移地址
	// len 读取的记录内容的长度
	// 输出参数: data返回被读取的数据，记录以十六进制字符串表示，多条记录间使用0D0A换行
	// 返 回 值: 0表示成功，其它表示失败
	public byte[] readRecords(byte fileNo, byte index) throws DesfireException {
		byte[] recvBuf;
		int len = 1;
		int offset = index & 0xff;
		ByteBuffer buff = ByteBuffer.allocate(8);
		buff.put(CMD_READ_RECORDS).put(fileNo).put((byte) (offset & 0xff))
				.put((byte) ((offset >> 8) & 0xff))
				.put((byte) ((offset >> 16) & 0xff)).put((byte) (len & 0xff))
				.put((byte) ((len >> 8) & 0xff))
				.put((byte) ((len >> 16) & 0xff));
		recvBuf = transmit(buff.array());
		if (recvBuf[0] != 0) {
			throw new DesfireException(recvBuf[0], "readRecords "
					+ getErrorMsg(recvBuf[0] & 0xff));
		}
		byte[] data = new byte[recvBuf.length - 1];
		System.arraycopy(recvBuf, 1, data, 0, recvBuf.length - 1);
		return data;
	}

	public String getErrorMsg(int ErrorNo) {
		switch (ErrorNo) {
		case 0x00:
			return "SUCCESSFUL";
		case 0x0C:
			return "NO_CHANGES";
		case 0x0E:
			return "OUT_OF_EEPROM_ERROR";
		case 0x1C:
			return "ILLEGAL_COMMAND_CODE";
		case 0x1E:
			return "INTEGRITY_ERROR";
		case 0x40:
			return "NO_SUCH_KEY";
		case 0x7E:
			return "LENGTH_ERROR";
		case 0x9D:
			return "PERMISSION_DENIED";
		case 0x9E:
			return "PARAMETER_ERROR";
		case 0xA0:
			return "APPLICATION_NOT_FOUND";
		case 0xA1:
			return "APPL_INTEGRITY_ERROR";
		case 0xAE:
			return "AUTHENTICATION_ERROR";
		case 0xAF:
			return "ADDITIONAL_FRAME";
		case 0xBE:
			return "BOUNDARY_ERROR";
		case 0xC1:
			return "PICC_INTEGRITY_ERROR";
		case 0xCA:
			return "COMMAND_ABORTED";
		case 0xCD:
			return "PICC_DISABLED_ERROR";
		case 0xCE:
			return "COUNT_ERROR";
		case 0xDE:
			return "DUPLICATE_ERROR";
		case 0xEE:
			return "EEPROM_ERROR";
		case 0xF0:
			return "FILE_NOT_FOUND";
		case 0xF1:
			return "FILE_INTEGRITY_ERROR";
		default:
			return "OTHER_ERROR";
		}
	}

	public static int FRAME_LEN = 512;
	public static byte CMD_REQA = (byte) 0x26;
	public static byte CMD_WAKEUUP = (byte) 0x52;
	public static byte CMD_ANTI_AND_SELECT1 = (byte) 0x93;
	public static byte CMD_ANTI_AND_SELECT2 = (byte) 0x95;
	public static byte CMD_RATS = (byte) 0xE0;
	public static byte CMD_PPS = (byte) 0xDF;
	public static byte CMD_AUTHENTICATE = (byte) 0x0A;
	public static byte CMD_CHG_KEY_SETTINGS = (byte) 0x54;
	public static byte CMD_GET_KEY_SETTINGS = (byte) 0x45;
	public static byte CMD_CHG_KEY = (byte) 0xC4;
	public static byte CMD_GET_KEY_VERSION = (byte) 0x64;
	public static byte CMD_DELETE_APPLICATION = (byte) 0xDA;
	public static byte CMD_GET_APPLICATIONIDS = (byte) 0x6A;
	public static byte CMD_SELECT_APPLICATION = (byte) 0x5A;
	public static byte CMD_FORMAT_PICC = (byte) 0xFC;
	public static byte CMD_GET_VERSION = (byte) 0x60;
	public static byte CMD_GET_FILEIDS = (byte) 0x6F;
	public static byte CMD_GET_FILESETTINGS = (byte) 0xF5;
	public static byte CMD_CHG_FILESETTINGS = (byte) 0x5F;
	public static byte CMD_CRE_STD_DATA_FILE = (byte) 0xCD;
	public static byte CMD_CRE_BACKUP_DATA_FILE = (byte) 0xCB;
	public static byte CMD_CRE_VALUE_FILE = (byte) 0xCC;
	public static byte CMD_CRE_LINEAR_RECORD_FILE = (byte) 0xC1;
	public static byte CMD_CRE_CYCLIC_RECORD_FILE = (byte) 0xC0;
	public static byte CMD_DELETE_FILE = (byte) 0xDE;
	public static byte CMD_READ_DATA = (byte) 0xBD;
	public static byte CMD_WRITE_DATA = (byte) 0x3D;
	public static byte CMD_GET_VALUE = (byte) 0x6C;
	public static byte CMD_CREDIT = (byte) 0x0C;
	public static byte CMD_DEBIT = (byte) 0xDC;
	public static byte CMD_LIMITED_CREDIT = (byte) 0x1C;
	public static byte CMD_WRITE_RECORD = (byte) 0x3B;
	public static byte CMD_READ_RECORDS = (byte) 0xBB;
	public static byte CMD_CLEAR_RECORD_FILE = (byte) 0xEB;
	public static byte CMD_COMMIT_TRANSCATION = (byte) 0xC7;
	public static byte CMD_ABORT_TRANSCATION = (byte) 0xA7;
	public static byte CMD_ADDITIONAL_FRAME = (byte) 0xAF;

	public static byte E_AUTH_RNDA = (byte) 0x55; // RNDA 错误
	public static byte E_MAC = (byte) 0x56;
}
