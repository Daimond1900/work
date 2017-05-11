package com.yifeng.adapter;

import java.util.Comparator;
import java.util.Map;

public class MyDepartmentComparator implements Comparator<Map<String, String>> {

	@Override
	public int compare(Map<String, String> object1, Map<String, String> object2) {

		String count1 = object1.get("COUNTS");
		String count2 = object2.get("COUNTS");
		if (count1 == null && count2 == null)
			return 0;
		else{
			count1=count1.trim();
			count2=count2.trim();
			return  Integer.parseInt(count2)-Integer.parseInt(count1);
		}
	}

}
