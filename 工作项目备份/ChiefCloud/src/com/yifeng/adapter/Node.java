package com.yifeng.adapter;


import java.util.ArrayList;
import java.util.List;

public class Node {
	public Node parent;//父节�?
    public List<Node> children = new ArrayList<Node>();//子节�?
    public String text;//节点显示的文�?
    public String value;//节点的�?
    public int icon = -1;//是否显示小图�?-1表示隐藏图标
    public boolean isExpanded = true;//是否处于展开状�?
    
    /**
     * Node构�?函数
     * @param text 节点显示的文�?
     * @param value 节点的�?
     */
    public Node(String text,String value){
    	this.text = text;
    	this.value = value;
    }
    
    /**
     * 设置父节�?
     * @param node
     */
    public void setParent(Node node){
    	this.parent = node;
    }
    /**
     * 获得父节�?
     * @return
     */
    public Node getParent(){
    	return this.parent;
    }
    /**
     * 设置节点文本
     * @param text
     */
    public void setText(String text){
    	this.text = text;
    }
    /**
     * 获得节点文本
     * @return
     */
    public String getText(){
    	return this.text;
    }
    /**
     * 设置节点�?
     * @param value
     */
    public void setValue(String value){
    	this.value = value;
    }
    /**
     * 获得节点�?
     * @return
     */
    public String getValue(){
    	return this.value;
    }
    /**
     * 设置节点图标文件
     * @param icon
     */
    public void setIcon(int icon){
    	this.icon = icon;
    }
    /**
     * 获得图标文件
     * @return
     */
    public int getIcon(){
    	return icon;
    }
    /**
     * 是否根节�?
     * @return
     */
    public boolean isRoot(){
    	return parent==null?true:false;
    }
    /**
     * 获得子节�?
     * @return
     */
    public List<Node> getChildren(){
    	return this.children;
    }
    /**
     * 添加子节�?
     * @param node
     */
    public void add(Node node){
    	if(!children.contains(node)){
    		children.add(node);
    	}
    }
    /**
     * 清除�?��子节�?
     */
    public void clear(){
    	children.clear();
    }
    /**
     * 删除�?��子节�?
     * @param node
     */
    public void remove(Node node){
    	if(!children.contains(node)){
    		children.remove(node);
    	}
    }
    /**
     * 删除指定位置的子节点
     * @param location
     */
    public void remove(int location){
    	children.remove(location);
    }
    /**
     * 获得节点的级�?根节点为0
     * @return
     */
    public int getLevel(){
    	return parent==null?0:parent.getLevel()+1;
    }
    /**
     * 是否叶节�?即没有子节点的节�?
     * @return
     */
    public boolean isLeaf(){
    	return children.size()<1?true:false;
    }
    /**
    * 当前节点是否处于展开状�? 
    * @return
    */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * 设置节点展开状�?
     * @return
     */
    public void setExpanded(boolean isExpanded){
    	 this.isExpanded =  isExpanded;
    }
 
}
