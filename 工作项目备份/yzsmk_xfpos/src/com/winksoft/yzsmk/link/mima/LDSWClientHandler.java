package com.winksoft.yzsmk.link.mima;
import com.winksoft.yzsmk.link.linker.LinkerHandler;
import com.winksoft.yzsmk.link.linker.LinkerMessageType;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LDSWClientHandler extends IoHandlerAdapter{
	

	private LinkerHandler handler=null;
	public LDSWClientHandler(LinkerHandler handler){
		//if (handler == null)
		super();
		this.handler = handler;
	}
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		Log.i(this.getClass().toString(), "messageReceived");
		byte[] tmp = (byte[])message;
		//Message msg = Message.obtain(handler, LinkerMessageType.MESSAGE_RECEIVED, tmp);
		//handler.sendMessage(msg);
		handler.obtainMessage(LinkerMessageType.MESSAGE_RECEIVED, tmp).sendToTarget();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Log.i(this.getClass().toString(), "sessionClosed");
		Message msg= Message.obtain();
		msg.what = LinkerMessageType.SESSION_CLOSED;
		handler.sendMessage(msg);
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		Log.e("ClientSessionHandler", cause.getMessage());
	    session.close(true);
		handler.obtainMessage(LinkerMessageType.EXCEPTION_CAUGHT).sendToTarget();	
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		Log.i(this.getClass().toString(), "messageSent");
		
		handler.obtainMessage(LinkerMessageType.MESSAGE_SENT).sendToTarget();
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		handler.obtainMessage(LinkerMessageType.SESSION_OPENED).sendToTarget();
	}

	
}
