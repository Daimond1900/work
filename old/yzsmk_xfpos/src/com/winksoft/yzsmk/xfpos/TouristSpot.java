package com.winksoft.yzsmk.xfpos;

import java.util.HashMap;


//景点类
public class TouristSpot {

	public HashMap<String, String> oldCardMap; //老卡类型
	public HashMap<String, String> newCardMap;
	
	private HashMap<String, String> oldCardType;
	private HashMap<String, String> newCardType;
	
	public TouristSpot()
	{
		super();
		oldCardType = new HashMap<String, String>();
		oldCardType.put("32", "100元");
		oldCardType.put("37", "120元");
		oldCardType.put("31", "180元");
		oldCardType.put("36", "200元");
		
		newCardType = new HashMap<String, String>();
		newCardType.put("32", "100元");
		newCardType.put("37", "120元");
		newCardType.put("31", "180元");
		newCardType.put("36", "200元");
		initMap("", "");
	}
	
	//param:  31:32:33
	private HashMap<String, String> initTouristSpot(String param){
		HashMap<String, String> re = new HashMap<String, String>();
		if (param != null){
			String[] sl = param.split(":");
			if (sl != null)
				for (String s : sl){
					re.put(s, s);
				}
		}
		return re;
	}
	
	//市民卡新新旧卡片不一样
	public void initMap(String oldCardType, String newCardType){
		oldCardMap = initTouristSpot(oldCardType);
		newCardMap = initTouristSpot(newCardType);
	}
	
	//判断卡能否在本终端使用
	public boolean isValidCardType(boolean newCard, int cardType){
		return newCard? (newCardMap.get(cardType) != null) : (oldCardMap.get(cardType) != null);
	}
	
	public String getNewCardTypeName(int cardType){
		return newCardType.get(cardType);
	}
	
	public String getOldCardTypeName(int cardType){
		return oldCardType.get(cardType);
	}
}
