package com.winksoft.yzsmk.czpos;

public enum TicketResult{
	OK(0, "检票通过"),
	CHECKING(1, "检票中..."),
	FAIL(2, "检票失败"),
	E_OVERDATE(3, "已过期"),
	E_UNAUTH(4, "未授权的卡片"),
	E_BLACK(5, "黑名单卡片"),
	E_ERROR(6, "刷卡异常"),
	E_FIND(7, "寻卡失败"),
	E_FINDTIMEOUT(8, "寻卡超时"),
	E_SAVE(9, "保存图像文件失败"),
	E_BANK(10, "发卡方错误"),
	E_FILE(11, "文件错误"),
	E_CARDTYPE(12, "卡类型错误");
	
	private int value;
	private String message;
	TicketResult(int value, String message)
	{
		this.value = value;
		this.message = message;
	}
	
	public int getValue() {
		return this.value;
	}

	public String getMesssage() {
		return this.message;
	}
	
	
	public static  TicketResult convert(int type) {
		for(TicketResult cr : TicketResult.values()) {
			if(cr.value == (byte)type) {
				return cr;
			}
		}
		return TicketResult.FAIL;
		
	}
}