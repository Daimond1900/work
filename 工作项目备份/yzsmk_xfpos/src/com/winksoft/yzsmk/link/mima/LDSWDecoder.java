package com.winksoft.yzsmk.link.mima;

import com.winksoft.yzsmk.link.net.mfs.util.StringUtil;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import android.util.Log;

public class LDSWDecoder extends CumulativeProtocolDecoder {
	private final String TAG = this.getClass().getName();

	private MessageDecoderResult isValidPacket(IoSession session, IoBuffer in) {
		if (in.remaining() < 5)
			return MessageDecoderResult.NEED_DATA;
		in.mark();

		// ȡ�����������
		byte[] buffer = new byte[in.remaining()];
		in.get(buffer);
		Log.i(TAG, "read: " + StringUtil.bytesToHexString(buffer));
		in.reset(); // ��ת������

		// if ((buffer[0] == 0x55) && (buffer[1] == (byte)0xAA)){
		// int packLen = (buffer[2] & 0Xff);
		// if (buffer.length < packLen+4)
		// return MessageDecoderResult.NEED_DATA;
		//
		// byte[] tmp = new byte[packLen+4];
		// return MessageDecoderResult.OK;
		// }else{
		if (true) {
//			byte[] tmp = new byte[packLen + 4];
			return MessageDecoderResult.OK;
		} else {
			return MessageDecoderResult.NOT_OK;
		}

	}

	@Override
	/*
	 * @title:
	 * 
	 * @author: mcb
	 * 
	 * @Description: ${�ȼ�⽻�ױ����Ƿ�����}(
	 */
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		MessageDecoderResult result = isValidPacket(session, in);
		if (result == MessageDecoderResult.NOT_OK) {
			// �����ֱ���������
			in.clear();
			return false;
		} else if (result == MessageDecoderResult.NEED_DATA) {
			return false;
		}

		try {
			byte len_h = in.get();
			byte len_l = in.get();
			int packLen = (len_h&0xff)*256+(len_l&0xff);
			byte[] buffer = new byte[packLen];
//			buffer[0] = stx_h;
//			buffer[1] = stx_l;
//			buffer[2] = (byte) packLen;
			in.get(buffer, 0, packLen);
			out.write(buffer);
			return false;
		} catch (Exception e) {
			in.clear();
			e.printStackTrace();
			return false;
		}
	}
}