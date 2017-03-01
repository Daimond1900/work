package com.winksoft.yzsmk.link.mima;

import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;


public class LDSWEncoder implements ProtocolEncoder {
	private final String TAG = this.getClass().getName();
	@Override
	public void dispose(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws IOException{
		if (message instanceof byte[]){
			IoBuffer buffer = IoBuffer.allocate(1024);
			buffer.clear();
			byte[] tmp = (byte[])message;
			buffer.put((byte[])tmp);
			buffer.flip();
			out.write(buffer);
		}
	}
}
