package com.yifeng.hnqzt.util;

import java.util.*;

public class Test {
	   /***
	    * 获取婚姻状况
	    * @return
	    */
       private List<Map<String,String>> getMarriages(){
    	   List<Map<String, String>> marriages=new ArrayList<Map<String,String>>();
    	   Map<String,String> map=new HashMap<String,String>();
    	   map.put("id", "-1");
    	   map.put("value", "选择");
    	   marriages.add(map);
    	   
    	   map=new HashMap<String,String>();
    	   map.put("id", "0");
    	   map.put("value", "未婚");
    	   marriages.add(map);
    	   
    	   map=new HashMap<String,String>();
    	   map.put("id", "1");
    	   map.put("value", "已婚");
    	   marriages.add(map);
    	   return marriages; 
       } 
       
       /***
        * 获取政治面貌
        * @return
        */
       private List<Map<String,String>> getPolitics(){
    	   List<Map<String, String>> politics=new ArrayList<Map<String,String>>();
    	   Map<String,String> map=new HashMap<String,String>();
    	   map.put("id", "-1");
    	   map.put("value", "选择");
    	   politics.add(map);
    	   
    	   map=new HashMap<String,String>();
    	   map.put("id", "0");
    	   map.put("value", "党员");
    	   politics.add(map);
    	   
    	   map=new HashMap<String,String>();
    	   map.put("id", "1");
    	   map.put("value", "团员");
    	   politics.add(map);
    	   return politics; 
       } 
       
       /***
        * 获取学历水平
        * @return
        */
       private List<Map<String,String>> getEducationals(){
    	   List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	   Map<String,String> map=new HashMap<String,String>();
    	   map.put("id", "-1");
    	   map.put("value", "选择");
    	   list.add(map);
    	   
    	  
    	   return list; 
       }
       
       /***
        * 获取区域
        * @return
        */
       private List<Map<String,String>> getAreas(){
    	   List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	   Map<String,String> map=new HashMap<String,String>();
    	   map.put("id", "-1");
    	   map.put("value", "选择");
    	   list.add(map);
    	   
    	  
    	   return list; 
       } 
       
       /***
        * 获取工作性质
        * @return
        */
       private List<Map<String,String>> getJobs(){
    	   List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	   Map<String,String> map=new HashMap<String,String>();
    	   map.put("id", "-1");
    	   map.put("value", "选择");
    	   list.add(map);
    	   
    	  
    	   return list; 
       } 
}
