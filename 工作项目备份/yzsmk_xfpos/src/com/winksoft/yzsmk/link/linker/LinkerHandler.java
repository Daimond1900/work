package com.winksoft.yzsmk.link.linker;

import android.os.Handler;
import android.os.Message;

public class LinkerHandler extends Handler {
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (m_callback != null){
			switch(msg.what){
				case LinkerMessageType.MESSAGE_RECEIVED: 
					m_callback.onDataReceived((byte[])msg.obj);
					break;
				case LinkerMessageType.SESSION_OPENED:
					m_callback.onConnected();
				case LinkerMessageType.SESSION_CLOSED:
					m_callback.onDisconnected();
					break;
				case LinkerMessageType.EXCEPTION_CAUGHT:
					m_callback.onError((Exception)msg.obj);
					break;
				default:
					break;
			}
		}
		
	}
	private ILinkerCallback m_callback;
	public LinkerHandler(ILinkerCallback callback){
		super();
		m_callback = callback;
	}
	
}
