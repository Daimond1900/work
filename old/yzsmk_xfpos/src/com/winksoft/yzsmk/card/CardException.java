package com.winksoft.yzsmk.card;

public class CardException extends Exception {
	
	private int errorCode;
	
	public int getErrorCode() {
		return errorCode;
	}

	public CardException(int errorCode, String errorMessage){
		super(errorMessage);
		this.errorCode = errorCode;
	}
	
}