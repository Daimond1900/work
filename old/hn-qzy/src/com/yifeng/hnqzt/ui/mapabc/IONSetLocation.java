package com.yifeng.hnqzt.ui.mapabc;

import android.location.Location;

public interface IONSetLocation extends Runnable{

	public void setLocation(Location location);
	
	public void setAdress(String adress);
}
