package com.yifeng.adapter;


import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.ChifCloud12345update.R;

public class MyAdapterd extends BaseAdapter{
	private List<Node> allNodes;
	private List<Node> allNodesCache;
	private Context context;
	private LayoutInflater lif;
	private MyAdapterd oThis = this;
	private int expanderIcon=-1;
	private int collsapsedIcon=-1;

	public MyAdapterd(Context convertView,Node rootNode){
		allNodes = new ArrayList<Node>();
		allNodesCache= new ArrayList<Node>();
		this.context=convertView;
		this.lif = (LayoutInflater) convertView.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		addAllnode(rootNode);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allNodes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return allNodes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/****View显示�?****/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = this.lif.inflate(R.layout.user_adapter, null);
			//得到页面控件
			holder = new ViewHolder();
			holder.imageView = (ImageView)convertView.findViewById(R.id.image2);
			holder.tView = (TextView)convertView.findViewById(R.id.text2);
			convertView.setTag(holder);
		}else{
				holder = (ViewHolder)convertView.getTag();
		}
		//将节点的值赋给他�?
			Node node = allNodes.get(position);
			if(node != null){
					holder.tView.setText(node.getText());
				if(!node.isLeaf()){
					holder.imageView.setVisibility(View.VISIBLE);
					if(node.isExpanded){
						holder.imageView.setImageResource(R.drawable.oa_user_tree_ec);
					}else{
						holder.imageView.setImageResource(R.drawable.oa_user_tree_ex);
					}
				}else{
					holder.imageView.setVisibility(View.GONE);
					
				}
			}
			convertView.setPadding(20*node.getLevel(), 5, 5, 5);
		//Log.i("level", allNodes.get(position).getLevel()+"");
		return convertView;
	}
	
	//根据传入的根节点  得到当前�?��节点
	public void addAllnode(Node node){
		allNodes.add(node);
		allNodesCache.add(node);
		if(node.isLeaf())return;
		for(int i=0;i<node.getChildren().size();i++){
			addAllnode(node.getChildren().get(i));
		}
	}
	//当用户点击某项LIST的时�? 控制节点收缩
	public void ExpandOrCollapse(int position){
		Node n = allNodes.get(position);
		Log.i("我们点击的是", n.text+" isExpanded:"+n.isExpanded());
		if(n != null){
			if(!n.isLeaf()){
//				n.setExpanded(!n.isExpanded());
//				filterNode();
				if(!n.isExpanded){
					for(int i =0;i<n.getChildren().size();i++){
						//allNodes.add(n.getChildren().get(i));
						//存在�?��数据显示顺序的问�? 要求显示在他的父节点�?
						//点击的节点位�?
						allNodes.add(position+1+i, n.getChildren().get(i));
					}
				}else{
					List<Node> rNode=getChildren(n);
					for(int i =0;i<rNode.size();i++){
						allNodes.remove(rNode.get(i));
					}
				}	
				n.setExpanded(!n.isExpanded);
				this.notifyDataSetChanged();
			}
		}
	}
	
	List<Node> getChildren(Node n){
		List<Node> list=new ArrayList<Node>();
		for(Node tn: n.getChildren()){
//			System.out.println(tn.getText()+"=========="+tn.isLeaf());
			if(!tn.isLeaf()){
				list.addAll(getChildren(tn));
			}
			tn.setExpanded(false);
			list.add(tn);
		}
		return list;
	}
	
	//设置默认打开时展�?���?
	public void ExpanderLevel(int level){
		allNodes.clear();
		for(int i =0;i<allNodesCache.size();i++){
			//得到每一个节�?
			Node n = allNodesCache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level){
					n.setExpanded(true);
				}else{
					n.setExpanded(false);
				}
				allNodes.add(n);
			}else{
				n.setExpanded(false);
			}
		}
		oThis.notifyDataSetChanged();
	}
	//设置图标
	public void setIcon(int expandedIcon,int collsapsedIcon){
		this.expanderIcon = expandedIcon;
		this.collsapsedIcon= collsapsedIcon;
	}
	public class ViewHolder{
		ImageView imageView;//图标
		TextView tView;//文本〉�?�?
	}
}