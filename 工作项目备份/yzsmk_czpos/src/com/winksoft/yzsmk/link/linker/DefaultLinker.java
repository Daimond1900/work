package com.winksoft.yzsmk.link.linker;

import java.util.HashMap;

import android.content.Context;

public class DefaultLinker implements ILinker{
	protected static LinkerHandler m_handler;
	protected static Context m_context;


	@Override
	public int connect(HashMap<String, String> param) {
		// TODO 自动生成的方法存根
		return 0;
	}
	
	@Override
	public int connect(HashMap<String, String> param, LinkerHandler handler) {
		m_handler = handler;
		return 0;
	}

	@Override
	public int sendData(byte[] data) {
		// TODO 自动生成的方法存根
		return 0;
	}

	
	@Override
	public int setHandler(LinkerHandler handler) {
		m_handler = handler;
		return 0;
	}

	@Override
	public int disconnect() {
		m_handler = null;
		return 0;
	}

}
