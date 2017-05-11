package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.winksoft.android.yzjycy.util.DataConvert;

public class ParseData {
	public static boolean IFINIT = false;

	public enum MZ {
		汉族, 蒙古族, 回族, 藏族, 维吾尔族, 苗族, 彝族, 壮族, 布依族, 朝鲜族, 满族, 侗族, 瑶族, 白族, 土家族, 哈尼族, 哈萨克族, 傣族, 黎族, 傈傈族, 佤族, 畲族, 高山族, 拉祜族, 水族, 东乡族, 纳西族, 景颇族, 柯尔克孜族, 土族, 达翰尔族, 仫佬族, 羌族, 布朗族, 撒拉族, 毛南族, 仡佬族, 锡伯族, 阿昌族, 普米族, 塔吉克族, 怒族, 乌孜别克族, 俄罗斯族, 鄂温克族, 德昂族, 保安族, 裕固族, 京族, 塔塔尔族, 独龙族, 鄂伦春族, 赫哲族, 门巴族, 珞巴族, 基诺族, 外籍人士
	}

	// 民族
	public static Map<String, String> MAPMZ;
	// 就业状态
	public static Map<String, String> JYZT;
	// 户口状态
	public static Map<String, String> HKZT;
	// 文化程度
	public static Map<String, String> WHCD;
	// 政治面貌
	public static Map<String, String> ZZMM;
	// 农村基本情况
	public static Map<String, String> NCJBQK;
	// 就业状态里的就业状态
	public static Map<String, String> JYZT1;
	// 目前就业产业
	public static Map<String, String> MQJYCY;
	// 从业行业
	public static Map<String, String> CYHY;
	// 就业形式
	public static Map<String, String> JYXS;
	// 就业地区类型
	public static Map<String, String> JYDQXS;
	// 就业地区
	public static Map<String, String> JYDQ;
	// 就业方式
	public static Map<String, String> JYFS;
	// 尚未转移人员就业意向
	public static Map<String, String> SWZYRYJYYX;
	// 国家职业资格
	public static Map<String, String> GJZYZG;
	// 减少原因
	public static Map<String, String> JSYY;
	// 通用是否
	public static Map<String, String> TYSF;
	// 男女
	public static Map<String, String> NANVN;
	// 培训意愿
	public static Map<String, String> PXYY;
	static {
		JYZT = new LinkedHashMap<String, String>();
		// 01就业 03 退休 05 失业 06 无业 10 迁出 12 死亡
		JYZT.put("01", "就业");
		JYZT.put("03", "退休");
		JYZT.put("05", "失业");
		JYZT.put("06", "无业");
		JYZT.put("10", "迁出");
		JYZT.put("12", "死亡");
		// 1 非农业 2 农业 3 居民户
		HKZT = new LinkedHashMap<String, String>();
		HKZT.put("1", "非农业");
		HKZT.put("2", "农业");
		HKZT.put("3", "居民户");
		// 00 不限 11博士 12硕士 21 大学 31大专 40 中专中技 50 技校 61 高中 62 职高 70 初中 80 小学

		// 90文盲或半文盲
		WHCD = new LinkedHashMap<String, String>();
		WHCD.put("00", "不限");
		WHCD.put("11", "博士");
		WHCD.put("12", "硕士");
		WHCD.put("21", "大学");
		WHCD.put("31", "大专");
		WHCD.put("40", "中专中技");
		WHCD.put("50", "技校 ");
		WHCD.put("61", "高中 ");
		WHCD.put("62", "职高 ");
		WHCD.put("70", "初中 ");
		WHCD.put("80", "小学 ");
		WHCD.put("90", "文盲或半文盲 ");
		// 01 中共党员 02 中共预备党员 03 共青团员 04 民革会员 05 民盟会员 06 民建会员 07 民进会员
		// 08 农工党党员 09致公党党员 10 九三学社社员 11 台盟盟员 12 无党派民主人士 13 群众
		ZZMM = new LinkedHashMap<String, String>();
		ZZMM.put("01", "中共党员");
		ZZMM.put("02", "中共预备党员");
		ZZMM.put("03", "共青团员");
		ZZMM.put("04", "民革会员");
		ZZMM.put("05", "民盟会员");
		ZZMM.put("06", "民建会员");
		ZZMM.put("07", "民进会员");
		ZZMM.put("08", "农工党党员");
		ZZMM.put("09", "致公党党员 ");
		ZZMM.put("10", "九三学社社员");
		ZZMM.put("11", "台盟盟员");
		ZZMM.put("12", "无党派民主人士");
		ZZMM.put("13", "群众");
		MAPMZ = new LinkedHashMap<String, String>();
		for (MZ s : MZ.values()) {
			String key = "";
			if (s.ordinal() <= 8) {
				key = "0" + (s.ordinal() + 1) + "";
			} else if (s.ordinal() == 56) {
				key = "90";
			} else {
				key = s.ordinal() + 1 + "";
			}
			MAPMZ.put(key, s.name());
		}
		NCJBQK = new LinkedHashMap<String, String>();
		NCJBQK.put("1", "纯农户");
		NCJBQK.put("2", "被征地农民");
		NCJBQK.put("3", "享受农村低保户");
		NCJBQK.put("4", "贫困农户");
		NCJBQK.put("5", "一般农户");
		NCJBQK.put("6", "当年新增劳动力");
		NCJBQK.put("7", "残疾人");
		NCJBQK.put("8", "退伍军人");
		NCJBQK.put("9", "其他");
		JYZT1 = new LinkedHashMap<String, String>();
		JYZT1.put("1", "劳务输出");
		JYZT1.put("2", "就地转移");
		JYZT1.put("3", "返乡创业");
		JYZT1.put("4", "失业");
		JYZT1.put("5", "务农");
		MQJYCY = new LinkedHashMap<String, String>();
		MQJYCY.put("01", "第一产业");
		MQJYCY.put("02", "第二产业");
		MQJYCY.put("03", "第三产业");
		CYHY = new LinkedHashMap<String, String>();
		CYHY.put("010000", "农林牧渔业");
		CYHY.put("020000", "采掘业");
		CYHY.put("030000", "制造业");
		CYHY.put("040000", "电力、煤气及水生产和供应");
		CYHY.put("050000", "建筑业");
		CYHY.put("060000", "交通运输、仓储及邮电通信业");
		CYHY.put("070000", "信息传输、计算机服务和软件业");
		CYHY.put("080000", "批发零售业");
		CYHY.put("090000", "住宿和餐饮业");
		CYHY.put("100000", "金融业");
		CYHY.put("110000", "房地产业");
		CYHY.put("120000", "租赁和商务服务业");
		CYHY.put("130000", "科学研究、技术服务和地质勘查业");
		CYHY.put("140000", "水利、环境和公共设施管理业");
		CYHY.put("150000", "居民服务和其它服务业");
		CYHY.put("160000", "教育");
		CYHY.put("170000", "卫生、社会保障和公共设施管理");
		CYHY.put("180000", "文化、体育和娱乐业");
		CYHY.put("190000", "公共管理和社会组织");
		CYHY.put("200000", "国际组织");
		CYHY.put("900000", "其他");
		JYXS = new LinkedHashMap<String, String>();
		JYXS.put("1", "在单位务工");
		JYXS.put("2", "自谋职业");
		JYXS.put("3", "自主创业");
		JYDQXS = new LinkedHashMap<String, String>();
		JYDQXS.put("1", "县内");
		JYDQXS.put("2", "县外市内");
		JYDQXS.put("3", "市外省内");
		JYDQXS.put("4", "省外境内");
		JYDQXS.put("5", "境外");
		JYDQ = new LinkedHashMap<String, String>();
		JYDQ.put("11", "北京");
		JYDQ.put("12", "天津");
		JYDQ.put("13", "东三省");
		JYDQ.put("14", "上海");
		JYDQ.put("15", "浙江");
		JYDQ.put("16", "山东");
		JYDQ.put("17", "广东");
		JYDQ.put("18", "苏南");
		JYDQ.put("19", "苏中");
		JYDQ.put("20", "苏北");
		JYDQ.put("21", "西部地区");
		JYDQ.put("22", "其他");
		JYFS = new LinkedHashMap<String, String>();
		JYFS.put("1", "托亲戚朋友");
		JYFS.put("2", "政府或公共职介机构组织");
		JYFS.put("3", "通过民办职业介绍机构");
		JYFS.put("4", "企业直接来招工");
		JYFS.put("5", "自发");
		JYFS.put("6", "其他");
		SWZYRYJYYX = new LinkedHashMap<String, String>();
		SWZYRYJYYX.put("1", "务农");
		SWZYRYJYYX.put("2", "就近转移");
		SWZYRYJYYX.put("3", "异地输出");
		GJZYZG = new LinkedHashMap<String, String>();
		GJZYZG.put("1", "职业资格一级(高级技师)");
		GJZYZG.put("2", "职业资格二级(技师)");
		GJZYZG.put("3", "职业资格三级(高级)");
		GJZYZG.put("4", "职业资格四级(中级)");
		GJZYZG.put("5", "职业资格五级(初级)");
		JSYY = new LinkedHashMap<String, String>();
		JSYY.put("1", "退休");
		JSYY.put("2", "死亡");
		JSYY.put("3", "户口迁出");
		JSYY.put("4", "农村转移城镇");
		JSYY.put("5", "新增系统自动减少");
		JSYY.put("6", "其他");

		TYSF = new LinkedHashMap<String, String>();
		TYSF.put("0", "否");
		TYSF.put("1", "是");
		NANVN = new LinkedHashMap<String, String>();
		NANVN.put("1", "男");
		NANVN.put("2", "女");
		PXYY = new LinkedHashMap<String, String>();
		//
	}

	public static String getKeyByValue(Map<String, String> map, Object value) {
		String keys = "";
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String obj = entry.getValue();
			if (obj != null && obj.equals(value)) {
				keys = (String) entry.getKey();
			}
		}
		return keys;
	}

	public static String[] getMapAllValue(Map<String, String> map) {
		String values[] = new String[map.size()];
		int i = 0;
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String obj = entry.getValue();
			values[i] = obj;
			i++;
		}

		return values;

	}

	public static void initDate(Map<String, String> map) {
		if (map != null && map.size() != 0) {
			if (map.get("aac005") == null)
				return;
			MAPMZ = pasreDate(map.get("aac005"));
			ZZMM = pasreDate(map.get("aac024"));
			HKZT = pasreDate(map.get("aac009"));
			WHCD = pasreDate(map.get("aac011"));
			NCJBQK = pasreDate(map.get("acc100"));
			JYZT1 = pasreDate(map.get("acc926"));
			MQJYCY = pasreDate(map.get("aab054"));
			CYHY = pasreDate(map.get("aab022"));
			JYXS = pasreDate(map.get("acc90b"));
			JYDQXS = pasreDate(map.get("acc901"));
			SWZYRYJYYX = pasreDate(map.get("acc905"));
			JYDQ = pasreDate(map.get("acc902"));
			JYFS = pasreDate(map.get("acc423"));
			GJZYZG = pasreDate(map.get("acc904"));
			PXYY = map;
			JSYY = pasreDate(map.get("acc912"));
		}
	}

	public static Map<String, String> pasreDate(String value) {
		Map<String, String> m = new LinkedHashMap<String, String>();
		if (value != null) {
			// List<Map<String, String>> list = DataConvert.toArrayList(value);
			//
			// for (Map<String, String> tm : list) {
			// m.put(tm.get("aaa102"), tm.get("aaa103"));
			// }
			m = DataConvert.toMap(value);

		}
		if (m != null && m.size() > 1) {
			m.remove("state");
			return m;
		} else
			return new LinkedHashMap<String, String>();
	}

}
