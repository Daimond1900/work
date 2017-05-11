package com.winksoft.yzsmk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.winksoft.yzsmk.czpos.queryvalue.QueryValues;
/**
 * 根据给定的两个list，将其合并为一个list
 * 并且对其进行排序，限制结果list的元素个数（小于等于给定的值）
 * 排序：根据存储在list中的Map（key="time")时间，倒序desc
 * 提供静态方法	twoList2oneList
 * @author weiguo
 * @since 2016/11/24
 */
public class ListConversion	{
	/**
	 * 用于将两个list合并为一个list并且进行排序
	 * @param listOne	需要被合并的list 1
	 * @param listOther	需要被合并的list 2
	 * @param limitSize	限制合并结果list的size
	 */
	public static List<Map<String, Object>> twoList2oneList(List<Map<String, Object>> listOne,
			List<Map<String, Object>> listOther, int limitSize) {
		if(listOne !=null && listOther !=null){
			listOne.addAll(listOther);
			Collections.sort(listOne,new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> m1, Map<String, Object> m2) {
					return str2Date((String)m2.get("time")).compareTo(str2Date((String)m1.get("time")));
				}
			});
		}
		return listOne.size() > limitSize ? listOne.subList(0, limitSize) : listOne;
	}
	/**
	 * String to Date
	 * @param strTime Date类型的String表示（yyyy-MM-dd HH:mm:ss）
	 * @return Date对象  
	 * @exception ParseException 当param为非yyyy-MM-dd HH:mm:ss格式，抛出此异常表示格式化转化错误
	 */
	public static Date str2Date(String strTime){
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = format.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * @param querylist	查询结果
	 * @param tableName	查询的表名
	 * @param queryType	查询的类型：0:交易记录 ； 1：签退
	 * @return
	 */
	public static List<Map<String, Object>> formatDate(List<Map<String, Object>> querylist, String tableName,int queryType) {
		if(querylist!=null && querylist.size()>0){
			QueryValues qv = new QueryValues();
			String strFlage = "";
			switch (tableName) {
			
			/* Cpu卡	xfinfos表的数据处理   查询类型：0  交易记录数据处理 	 查询类型：1 签退*/
			case "xfinfos":	// cpu
				for (Map<String, Object> map : querylist) {
					if(queryType==0){	//交易记录
						map.put("category", "Cpu"); // 类别
					}else if(queryType==1){ //签退
						map.put("PSAM1",qv.getPSAMNoByType(tableName,"body_117"));
						map.put("PSAM2","");
						map.remove("body_117");
					}
					map.put("jobNum",map.get("jobNum")); //工号
					String strTime = ((String) map.get("insertdate")).split("\\.")[0]; // 处理时间数据
					map.remove("insertdate");
					map.put("time", strTime);   // 交易日期
					map.put("money", map.get("body_79")); //交易金额
					map.remove("body_79");
				}
				break;
			/***************************************************************************************/	
			/* Dirfire卡 dirfire表的数据处理   查询类型：0  交易记录数据处理 	 查询类型：1 签退*/
			case "disfire":	// dirfire 
				for (Map<String, Object> map : querylist) {
					if(queryType==0){						//交易记录        
						map.put("category", "Desfire"); 	// 卡类别  
					}else if(queryType==1){					//签退
						map.put("PSAM2",qv.getPSAMNoByType(tableName,"care_class"));
						map.put("PSAM1","");
						map.remove("care_class");
					}
					map.put("jobNum",map.get("jobNum"));  // 工号  
					String strTime = ((String) map.get("insertdate")).split("\\.")[0]; //处理时间数据
					map.remove("insertdate");
					map.put("time", strTime);   // 交易日期
					map.put("money", map.get("consume_money"));	//交易金额
					map.remove("consume_money");
				}
				break;
			default:
				break;
			}
		}
		return querylist;
	}
}
