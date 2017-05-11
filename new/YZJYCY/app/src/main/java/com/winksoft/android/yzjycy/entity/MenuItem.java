package com.winksoft.android.yzjycy.entity;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 * */
public class MenuItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/**
	 * 栏目对应ID
	 * */
	public Integer id;
	/**
	 * 栏目对应NAME
	 * */
	public String name;
	/**
	 * 栏目在整体中的排序顺序 rank
	 * */
	public Integer orderId;
	/**
	 * 栏目是否选中
	 * */
	public Integer selected;
	/**
	 * 栏目对应资源名称
	 */
	public String resName;
	
	/**
	 * 栏目对应资源分组编号
	 */
	public Integer groupId;
	
	public MenuItem() {
		
	}

	/**
	 * @param name 栏目名称
	 * @param id id
	 * @param orderId 默认排序编号
	 * @param selected 是否选中
	 * @param groupId 分组id
	 */
	public MenuItem(int id , String name, int orderId, int selected, String resName , int groupId) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
		this.resName = resName;
		this.groupId = Integer.valueOf(groupId);
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	@Override
	public String toString() {
		return "MenuItem [id=" + id + ", name=" + name + ", orderId=" + orderId
				+ ", selected=" + selected + ", resName=" + resName
				+ ", groupId=" + groupId + "]";
	}
	
	 
}