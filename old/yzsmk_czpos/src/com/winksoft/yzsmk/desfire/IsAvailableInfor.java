package com.winksoft.yzsmk.desfire;

public class IsAvailableInfor {
	public static byte[] startup_date;//[4];	//启用日期		BCD 
	public static byte[] valid_data;//[4];	//有效期    BCD
	public static byte[] b_flag;	//黑名单标志
	
	public IsAvailableInfor(){
		startup_date=null;//[4];	//启用日期		BCD 
		valid_data=null;//[4];	//有效期    BCD
		b_flag=null;	//黑名单标志
	}
	
	public boolean setStartup_date(byte[] startup){
		boolean returnflag = false;
		if (startup.length == 4){
			startup_date = startup;//[4];	//启用日期		BCD 
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setValid_data(byte[] valid){
		boolean returnflag = false;
		if (valid.length == 4){
			valid_data = valid;//[4];	//有效期    BCD
			returnflag = true;
		}
		return returnflag;
	}
	
	public boolean setB_flag(byte[] flag){
		boolean returnflag = false;
		if (flag.length == 1){
			b_flag = flag;//黑名单标志
			returnflag = true;
		}
		return returnflag;
	}
}
