package com.winksoft.yzsmk.link.linker;

import java.util.HashMap;

import android.content.Context;

public interface ILinker {
	int connect(HashMap<String, String> param);
	int connect(HashMap<String, String> param, LinkerHandler handler);
	int sendData(byte[] data);
	int setHandler(LinkerHandler handler);
	int disconnect();
	
}
