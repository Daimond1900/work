package com.yifeng.hngly.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 返回json
 * 
 * @author ygl
 * 
 */
public class ReJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	private boolean success;
	private Handler handler;
	private List<Map<String, String>> list;
	private int size;
	private Context c;
	private Map<String, String> map;

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	private Map<String, String> other;

	public Map<String, String> getOther() {
		return other;
	}

	public void setOther(Map<String, String> other) {
		this.other = other;
	}

	public int getSize() {
		return size;
	}

	public ReJson(Context context, Handler handler) {
		this.c = context;
		this.handler = handler;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	/**
	 * 
	 * @param returnString
	 *            第一个为json 第二个为json内容获取key 第三个参数为 第二个类型 默认为list 后面可取得列表中所需字段
	 * @return
	 */
	public ReJson setValue(String... returnString) {
		String valueType = "list";
		String listKey = "list";
		String json = returnString[0];

		if (json.equals(HttpPostGetUtil.SERVICEERROR)) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(c, "服务端异常", Toast.LENGTH_SHORT).show();

				}
			});
			success = false;
			return this;
		}
		if (json.equals(HttpPostGetUtil.TIMEOUTERROR)) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(c, "连接超时,	待网络稳定后重新尝试连接。", Toast.LENGTH_SHORT)
							.show();

				}
			});
			success = false;
			return this;
		}
		if (json.equals(HttpPostGetUtil.NONETWORKERROR)) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(c, "确认网络正常后重新尝试访问。", Toast.LENGTH_SHORT)
							.show();

				}
			});
			success = false;
			return this;
		}
		if (json.equals(HttpPostGetUtil.SERVERNOTFOUND)) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(c, "服务端异常。", Toast.LENGTH_SHORT)
							.show();

				}
			});
			success = false;
			return this;
		}
		if (returnString.length > 1) {
			listKey = returnString[1];
		}
		String params[] = null;
		if (returnString.length > 2) {
			valueType = returnString[2];
		}

		if (returnString.length > 3) {
			params = new String[returnString.length - 3];
			for (int i = 3; i < returnString.length; i++) {
				params[i - 3] = returnString[i];
			}
			other = new HashMap<String, String>();
		}

		Map<String, String> map = DataConvert.toMap(json);

		String s = map.get("success");
		msg = map.get("msg");
		try {
			if (s.equals("true"))
				success = true;
			else if (s.equals("false"))
				success = false;
			if (returnString.length == 1) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
					}
				});
				return this;
			}
			if (success) {
				if (valueType.equals("map")) {
					if (map.get(listKey) == null)
						setMap(null);
					else {
						setMap(DataConvert.toMap(map.get(listKey)));

						String state = getMap().get("state");

						if (state.equals("-1")) {
							success = false;
							handler.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(c, "返回error字符",
											Toast.LENGTH_SHORT).show();

								}
							});
						} else if (state.equals("-2")) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(c, "key失败",
											Toast.LENGTH_SHORT).show();
								}
							});
							success = false;
						} else if (state.equals("2")) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(c, "解析失败json格式不正确",
											Toast.LENGTH_SHORT).show();
								}
							});
							success = false;
						}
					}
				} else {
					if (map.get(listKey) == null && msg != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(c, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
						success = false;
						size = 0;
						list = new ArrayList<Map<String, String>>();
						return this;
					}
					list = DataConvert.toArrayList(map.get(listKey));
					size = list.size();
					String state = list.get(0).get("state");
					if (state.equals("0")) {
//						list = new ArrayList<Map<String,String>>;
						list.clear();
						size = 0;
						success = false;
					} else if (state.equals("-1")) {
						success = false;
						handler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(c, "返回error字符",
										Toast.LENGTH_SHORT).show();

							}
						});
					} else if (state.equals("-2")) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(c, "key失败", Toast.LENGTH_SHORT)
										.show();
							}
						});
						success = false;
					} else if (state.equals("2")) {
						success = false;
						handler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(c, "解析失败json格式不正确",
										Toast.LENGTH_SHORT).show();
							}
						});

					}
				}
				if (params != null)
					for (String param : params) {
						other.put(param, map.get(param));
					}

			} else {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
					}
				});
				list = new ArrayList<Map<String, String>>();
				size = 0;
			}
		} catch (Exception e) {
			success = false;
			size = 0;
			list = new ArrayList<Map<String, String>>();
			msg = "数据解析失败";
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
		return this;
	}

}
