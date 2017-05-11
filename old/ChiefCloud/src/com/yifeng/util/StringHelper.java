package com.yifeng.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cmbr.fcl.security.CipherTool;
import org.cmbr.fcl.security.Digest;

/**
 * 
 * @author 吴家宏版本1.1
 *
 */
public class StringHelper {
	/**
	 * 自定义字符串替换函数
	 * 
	 * @param strFrom
	 * @param strTo
	 * @param strTarget
	 * @return
	 */
	public static String eregi_replace(String strFrom, String strTo, String strTarget) {
		String strPattern = "(?i)" + strFrom;
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strTarget);
		if (m.find()) {
			return strTarget.replaceAll(strFrom, strTo);
		} else {
			return strTarget;
		}
	}

	/**
	 * 解析替换XML回来前面带空格的数据
	 * 
	 * @param strFrom
	 * @param strTo
	 * @param strTarget
	 * @return
	 */
	public static String eregi_replaceXml(String strFrom, String strTo, String strTarget) {
		String strPattern = "(?i)" + strFrom;
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strTarget);
		String str = "";
		if (m.find()) {
			str = strTarget.replaceAll(strFrom, strTo);
			if (str != null || !str.equals("")) {
				int index = str.indexOf("<?xml");// 找到XML前面的第一个索引
				str = str.substring(index);
			}
			return str;
		} else {
			str = strTarget;
			if (str != null || !str.equals("")) {
				int index1 = str.indexOf("<?xml");// 找到XML前面的第一个索引
				str = str.substring(index1);
			}
			return str;
		}
	}

	/**
	 * MD5密码加密
	 * 
	 * @param pwd
	 * @return
	 */
	public static String handlePwd(String pwd) {
		try {
			Digest dg = new Digest(Digest.ALGORITHM_MD5);
			String md5Pwd = CipherTool.byteToString(dg.getDigest(pwd), false);
			return md5Pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pwd;
	}

	/**
	 * 清除掉所有特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str) {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String nstr = "";
		try {
			if (!str.equals("")) {
				String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/\\?~！@#￥%……& amp;*（）――+|{}【】‘；：”“’。，、？]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(str);
				nstr = m.replaceAll("").trim();
				nstr = nstr.replace("\\", "");
				return nstr;
			} else {
				return nstr;
			}
		} catch (Exception e) {
			return nstr;
		}
	}

	/***
	 * 去掉HTML标记
	 * 
	 * @param str
	 * @return
	 */
	public static String removeHtml(String str) {
		String nstr = "";
		if (!str.equals("")) {
			Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(str);
			nstr = matcher.replaceAll("");
		}
		return nstr;

	}

	/**
	 * 检测特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkString(String str) {
		if (!str.equals("")) {
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）――+|{}【】‘；：”“’。，、？]";
			Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
		} else {
			return false;
		}
	}

	/**
	 * 判断是否只能输入数字及字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkNumOrEn(String str) {
		if (!str.equals("")) {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
		} else {
			return false;
		}
	}

	/**
	 * 验证邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkEmail(String str) {
		if (!str.equals("")) {
			Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
		} else {
			return false;
		}
	}

	public static String doConvert(Object obj) {
		try {
			DecimalFormat df = new DecimalFormat("#0.00");
			String str = df.format(obj);
			return str;
		} catch (Exception e) {
			return "0";
		}

	}

	/**
	 * 时间转换
	 * 
	 * @param d
	 * @return
	 */
	public static String doDateFormat(String d) {
		String result = d.substring(0, 4) + "-" + d.substring(4, 6) + "-" + d.substring(6, 8) + " " + d.substring(8, 10)
				+ ":" + d.substring(10, 12) + ":" + d.substring(12, 14);
		return result;
	}
}
