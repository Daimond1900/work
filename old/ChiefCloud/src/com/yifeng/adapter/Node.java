package com.yifeng.adapter;


import java.util.ArrayList;
import java.util.List;

public class Node {
	public Node parent;//ç¶èç?
    public List<Node> children = new ArrayList<Node>();//å­èç?
    public String text;//èç¹æ¾ç¤ºçæå­?
    public String value;//èç¹çå?
    public int icon = -1;//æ¯å¦æ¾ç¤ºå°å¾æ ?-1è¡¨ç¤ºéèå¾æ 
    public boolean isExpanded = true;//æ¯å¦å¤äºå±å¼ç¶æ?
    
    /**
     * Nodeæé?å½æ°
     * @param text èç¹æ¾ç¤ºçæå­?
     * @param value èç¹çå?
     */
    public Node(String text,String value){
    	this.text = text;
    	this.value = value;
    }
    
    /**
     * è®¾ç½®ç¶èç?
     * @param node
     */
    public void setParent(Node node){
    	this.parent = node;
    }
    /**
     * è·å¾ç¶èç?
     * @return
     */
    public Node getParent(){
    	return this.parent;
    }
    /**
     * è®¾ç½®èç¹ææ¬
     * @param text
     */
    public void setText(String text){
    	this.text = text;
    }
    /**
     * è·å¾èç¹ææ¬
     * @return
     */
    public String getText(){
    	return this.text;
    }
    /**
     * è®¾ç½®èç¹å?
     * @param value
     */
    public void setValue(String value){
    	this.value = value;
    }
    /**
     * è·å¾èç¹å?
     * @return
     */
    public String getValue(){
    	return this.value;
    }
    /**
     * è®¾ç½®èç¹å¾æ æä»¶
     * @param icon
     */
    public void setIcon(int icon){
    	this.icon = icon;
    }
    /**
     * è·å¾å¾æ æä»¶
     * @return
     */
    public int getIcon(){
    	return icon;
    }
    /**
     * æ¯å¦æ ¹èç?
     * @return
     */
    public boolean isRoot(){
    	return parent==null?true:false;
    }
    /**
     * è·å¾å­èç?
     * @return
     */
    public List<Node> getChildren(){
    	return this.children;
    }
    /**
     * æ·»å å­èç?
     * @param node
     */
    public void add(Node node){
    	if(!children.contains(node)){
    		children.add(node);
    	}
    }
    /**
     * æ¸é¤æ?å­èç?
     */
    public void clear(){
    	children.clear();
    }
    /**
     * å é¤ä¸?¸ªå­èç?
     * @param node
     */
    public void remove(Node node){
    	if(!children.contains(node)){
    		children.remove(node);
    	}
    }
    /**
     * å é¤æå®ä½ç½®çå­èç¹
     * @param location
     */
    public void remove(int location){
    	children.remove(location);
    }
    /**
     * è·å¾èç¹ççº§æ?æ ¹èç¹ä¸º0
     * @return
     */
    public int getLevel(){
    	return parent==null?0:parent.getLevel()+1;
    }
    /**
     * æ¯å¦å¶èç?å³æ²¡æå­èç¹çèç?
     * @return
     */
    public boolean isLeaf(){
    	return children.size()<1?true:false;
    }
    /**
    * å½åèç¹æ¯å¦å¤äºå±å¼ç¶æ? 
    * @return
    */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * è®¾ç½®èç¹å±å¼ç¶æ?
     * @return
     */
    public void setExpanded(boolean isExpanded){
    	 this.isExpanded =  isExpanded;
    }
 
}
