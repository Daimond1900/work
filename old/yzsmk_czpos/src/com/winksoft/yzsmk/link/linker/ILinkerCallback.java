package com.winksoft.yzsmk.link.linker;

public abstract interface ILinkerCallback {
	void onConnected();
	void onError(Exception e);
	void onDisconnected();
	void onDataReceived(byte[] data);
}
