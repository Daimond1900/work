package com.winksoft.yzsmk.ibase;

public interface IDialog {
	public interface PwdListener {
		public void onConfirm(String pwd);
	}
	public interface AddEmployeeListener {
		public void onConfirm(String no,String pwd);
	}
}
