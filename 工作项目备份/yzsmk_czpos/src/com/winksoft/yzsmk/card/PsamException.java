package com.winksoft.yzsmk.card;


public class PsamException extends CardException {
	private static final long serialVersionUID = -7349506709562073889L;
	public PsamException(int errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
}

