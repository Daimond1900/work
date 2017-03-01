package com.yifeng.util;

import java.util.*;
import org.cmbr.fcl.vo.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author 吴家宏 2011-06-02 BOX平台及手机端常用数据结果集转换工具类
 *         主要对map,list,json,CommonVO,VOList数据进行转换
 */
public class DataConvert {
	/**
	 * 根据JSON格式字各符串转换成List<Map<String,String>>集合
	 * 
	 * @param array
	 *            state状态 0：表示没有数据，1：表示加载成功，-1:表示返回error字符,2:表示解析失败json格式不正确
	 * @return list
	 */
	public static List<Map<String, String>> toArrayList(String array) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		if (array == null) {
			map.put("state", String.valueOf(ConstantUtil.DATA_NULL));
			list.add(map);
			return list;
		} else if (array.equals("")) {
			map.put("state", String.valueOf(ConstantUtil.IS_EMPTY));
			list.add(map);
			return list;
		} else if (StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", array)
				.equals("error")) {
			map.put("state", String.valueOf(ConstantUtil.SERVER_ERROR));
			list.add(map);
			return list;
		} else if (array.equals("FAIL")) {// key失败
			map.put("state", String.valueOf(ConstantUtil.KEY_ERROR));
			list.add(map);
			return list;
		} else {
			try {
				JSONArray jdata = new JSONArray(array);
				if (jdata.length() == 0) {
					map.put("state", String.valueOf(ConstantUtil.IS_EMPTY));
					list.add(map);
				} else {
					boolean ifadd=true;
					for (int i = 0; i < jdata.length(); i++) {
						JSONObject obj = jdata.getJSONObject(i);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("state",
								String.valueOf(ConstantUtil.LOGIN_SUCCESS));
						for (Iterator iterator = obj.keys(); iterator.hasNext();) {
							String key = (String) iterator.next();
							if (key.equals("COUNTS")) {
								if (((String) obj.getString(key)).equals("0")){
									ifadd=false;
									break;
								}
							}
							if (obj.get(key) instanceof String) {
								map1.put(key, obj.getString(key));
							} else if (obj.get(key) instanceof Integer) {
								map1.put(key, String.valueOf(obj.getInt(key)));
							} else if (obj.get(key) instanceof Long) {
								map1.put(key, String.valueOf(obj.getLong(key)));
							} else if (obj.get(key) instanceof Double) {
								map1.put(key,
										StringHelper.doConvert(obj.get(key)));
							} else {
								map1.put(key, obj.get(key).toString());
							}
						}
						if(ifadd){
						list.add(map1);
						}else{
							ifadd=true;
						}
					}
				}
			} catch (Exception e) {
				map.put("state", String.valueOf(ConstantUtil.INNER_ERROR));
				list.add(map);
			}
			if(list.size()==0){
				map.put("state", String.valueOf(ConstantUtil.IS_EMPTY));
				list.add(map);
			}
			return list;
		}
	}

	/**
	 * 根据JSON格式字各符串转换成Map<String,String>集合
	 * 
	 * @param json
	 *            state状态 0：表示没有数据，1：表示加载成功，-1:表示返回error字符,2:表示解析失败json格式不正确
	 * @return map
	 */
	public static Map<String, String> toMap(String json) {
		Map<String, String> map = new HashMap<String, String>();
		if (json == null) {
			map.put("state", String.valueOf(ConstantUtil.DATA_NULL));
			return map;
		} else if (json.equals("")) {
			map.put("state", String.valueOf(ConstantUtil.IS_EMPTY));
			return map;
		} else if (StringHelper.eregi_replace("(\r\n|\r|\n|\n\r)", "", json)
				.equals("error")) {
			map.put("state", String.valueOf(ConstantUtil.SERVER_ERROR));
			return map;
		} else if (json.equals("FAIL")) {// key失败
			map.put("state", String.valueOf(ConstantUtil.KEY_ERROR));
			return map;
		} else {
			try {
				JSONObject jdata = new JSONObject(json);
				if (jdata.length() == 0) {
					map.put("state", String.valueOf(ConstantUtil.IS_EMPTY));
				} else {
					map.put("state", String.valueOf(ConstantUtil.LOGIN_SUCCESS));
					for (Iterator iterator = jdata.keys(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if (jdata.get(key) instanceof String) {
							map.put(key, jdata.getString(key));
						} else if (jdata.get(key) instanceof Integer) {
							map.put(key, String.valueOf(jdata.getInt(key)));
						} else if (jdata.get(key) instanceof Long) {
							map.put(key, String.valueOf(jdata.getLong(key)));
						} else if (jdata.get(key) instanceof Double) {
							map.put(key, StringHelper.doConvert(jdata.get(key)));
						} else {
							map.put(key, jdata.get(key).toString());
						}

					}
				}
			} catch (Exception e) {
				map.put("state", String.valueOf(ConstantUtil.INNER_ERROR));
			}
		}
		return map;
	}

	/***
	 * 根据给定的List<Map<String,String>> 转换成json格式数据
	 * 
	 * @param list
	 * @return JSONArray
	 */
	public static JSONArray toJSONArray(List<Map<String, String>> list) {
		JSONArray jsonarray = new JSONArray();
		try {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				JSONObject json = new JSONObject();
				Map<String, String> map = (Map<String, String>) iter.next();
				for (Iterator iterator = map.keySet().iterator(); iterator
						.hasNext();) {
					String key = (String) iterator.next();
					json.put(key, map.get(key));
				}
				jsonarray.put(json);
			}
			return jsonarray;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 根据给定的map转换成JSONObject数据
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject toJsonObject(Map<String, String> map) {
		try {
			JSONObject json = new JSONObject();
			for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				json.put(key, map.get(key));
			}
			return json;
		} catch (Exception e) {
		}
		return null;
	}

	/***
	 * 根据指定的json格式的字符串转换成CommonVO
	 * 
	 * @param json
	 * @return vo
	 */
	public static CommonVO toCommonVO(String json) {
		CommonVO vo = new CommonVO();
		try {
			JSONObject obj = new JSONObject(json);
			for (Iterator iter = obj.keys(); iter.hasNext();) {
				String key = (String) iter.next();
				vo.setData(key, obj.get(key));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vo;
	}

	/***
	 * 根据指定的json格式的字符串转换成VOList
	 * 
	 * @param json
	 * @return
	 */
	public static VOList toVOList(String json) {
		VOList list = new VOList();
		try {
			JSONArray data = new JSONArray(json);
			for (int i = 0; i < data.length(); i++) {
				JSONObject cvo = data.getJSONObject(i);
				CommonVO vo = new CommonVO();
				for (Iterator iter = cvo.keys(); iter.hasNext();) {
					String key = (String) iter.next();
					vo.setData(key, cvo.get(key));
				}
				list.add(vo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
}
