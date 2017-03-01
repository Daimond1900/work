package com.yifeng.hnqzt.util;
/**
 * 行程路线实体类
 * @author 吴家宏
 * 2012-10-26
 */
public class RouteLine {
	private String route;
	/*value 表示持续时间（以秒为单位）。
	text 包含持续时间的可人工读取的表示形式。*/
	private String h_value;
	private String h_text;
	/*
	 * value 表示距离（以米为单位）
       text 包含距离的可人工读取的表示形式，以起点所用的单位以及请求中指定的语言显示。
	 */
	private String r_value;
	private String r_text;

	public String getH_value() {
		return h_value;
	}

	public void setH_value(String hValue) {
		h_value = hValue;
	}

	public String getH_text() {
		return h_text;
	}

	public void setH_text(String hText) {
		h_text = hText;
	}

	public String getR_value() {
		return r_value;
	}

	public void setR_value(String rValue) {
		r_value = rValue;
	}

	public String getR_text() {
		return r_text;
	}

	public void setR_text(String rText) {
		r_text = rText;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
	

}
