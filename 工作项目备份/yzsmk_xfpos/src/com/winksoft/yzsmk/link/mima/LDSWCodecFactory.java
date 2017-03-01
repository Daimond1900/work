package com.winksoft.yzsmk.link.mima;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class LDSWCodecFactory implements ProtocolCodecFactory  {


		LDSWDecoder decoder;
		LDSWEncoder encoder;
		public LDSWCodecFactory(LDSWDecoder decoder, LDSWEncoder encoder){
			this.decoder = decoder;
			this.encoder = encoder;
		}
		
		@Override
		public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			return decoder;
		}
		
		@Override
		public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
			// TODO Auto-generated method stub
			return (ProtocolEncoder) encoder;
		}

}
