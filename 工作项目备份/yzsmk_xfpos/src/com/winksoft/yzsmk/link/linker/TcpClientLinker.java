package com.winksoft.yzsmk.link.linker;

import java.net.InetSocketAddress;
import java.util.HashMap;

import com.winksoft.yzsmk.link.mima.LDSWClientHandler;
import com.winksoft.yzsmk.link.mima.LDSWCodecFactory;
import com.winksoft.yzsmk.link.mima.LDSWDecoder;
import com.winksoft.yzsmk.link.mima.LDSWEncoder;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.content.Context;
import android.os.Handler;


public class TcpClientLinker extends DefaultLinker {
	private static ILinker m_instance = null;
	private IoSession session = null;
	private NioSocketConnector connector = null;   
	private int PORT = 4402;
	private String CONNIP = "192.168.66.82";
	
	public static ILinker getLinker(Context context) {
		m_context = context;
		if (m_instance == null){
			m_instance = new TcpClientLinker();
		}
		return m_instance;
	}
	
	

	@Override
	public int connect(HashMap<String, String> param) {
		try
		{
			Object obj = param.get("ip");
			if (obj != null){
				CONNIP = (String)obj;
			}
			
			obj = param.get("port");
			if (obj != null){
				PORT =  Integer.parseInt((String)obj);
			}
		}catch(Exception e)
		{
			if (m_handler!= null){
				m_handler.obtainMessage(LinkerMessageType.EXCEPTION_CAUGHT, e).sendToTarget();
			}
			return -2;
		}
		
		// 创建TCP/IP的连接
		connector = new NioSocketConnector();
		// 创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		// 设置日志过滤器
		chain.addLast("logger", new LoggingFilter());

		// 设置编码解码过滤器
		LDSWCodecFactory factory = new LDSWCodecFactory(new LDSWDecoder(), 	new LDSWEncoder());
		chain.addLast("myChain", new ProtocolCodecFilter(factory));

		// 设置处理的类
		connector.setHandler(new LDSWClientHandler(m_handler));
		// 设置时间
		connector.setConnectTimeoutMillis(10000);
		// 开始连接服务器
		InetSocketAddress inetSocketAddress = new InetSocketAddress(CONNIP,	PORT);
		ConnectFuture cf = connector.connect(inetSocketAddress);
		// 等待连接结束
		cf.awaitUninterruptibly();
		try{
			session = cf.getSession();
		}catch (Exception e){
			if (m_handler!= null){
				m_handler.obtainMessage(LinkerMessageType.EXCEPTION_CAUGHT, e).sendToTarget();
			}
			return -1;
		} 
		return 0;
	}


	@Override
	public int connect(HashMap<String, String> param, LinkerHandler handler) {
		m_handler = handler;
		
		return connect(param);
	}


	@Override
	public int sendData(byte[] data) {
		if (session != null){
			session.write(data);
			return 0;
		}else 
			return -1;
	}


	@Override
	public int disconnect() {
		if (session != null){
			session.close(true);
			session = null;
			connector = null;	
		}
		return super.disconnect();
	}

}
