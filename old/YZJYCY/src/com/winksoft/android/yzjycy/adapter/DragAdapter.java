package com.winksoft.android.yzjycy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.entity.MenuItem;

@SuppressLint({ "InflateParams", "ViewHolder" })
public abstract class DragAdapter extends BaseAdapter {
	/** TAG*/
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 列表数据是否改变 */
	private boolean isListChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 可以拖动的列表（即用户选择的频道列表） */
	public  List<MenuItem> channelList;
	/** TextView 频道内容 */
	private TextView item_text;
	/** 要删除的position */
	public int remove_position = -1;
	/** 是否是用户频道 */
//	private boolean isUser = false;
	
	private boolean isShowDelete = false;// 根据这个变量来判断是否显示图标
	
	private LayoutInflater inflater;
	
	public DragAdapter(Context context,  List<MenuItem> channelList,boolean isUser) {
		this.context = context;
		this.channelList = channelList;
//		this.isUser = isUser;
		inflater = LayoutInflater.from(context);
	}
	
	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public MenuItem getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	protected abstract void delView(View view, int position);
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view = inflater.inflate(R.layout.mygridview_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		ImageView ivDel = (ImageView) view.findViewById(R.id.ivDel);
		ivDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				delView(view,position);
			}
		});
		ImageView img = (ImageView) view.findViewById(R.id.img);
		MenuItem channel = getItem(position);
		img.setImageResource(getResource(channel.getResName()));
		if(isShowDelete){
			view.setBackgroundResource(R.drawable.subscribe_item_bg);
			ivDel.setVisibility(View.VISIBLE);
		}else{
			view.setBackgroundResource(0);
			ivDel.setVisibility(View.GONE);
		}
		item_text.setText(channel.getName());

		if (isChanged && (position == holdPosition) && !isItemShow) {
//			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
//			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if(remove_position == position){
//			item_text.setText("");
		}
		return view;
	}

	/** 添加频道列表 */
	public void addItem(MenuItem channel) {
		channelList.add(channel);
		isListChanged = true;
		notifyDataSetChanged();
	}

	/** 拖动变更频道排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		MenuItem dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		isListChanged = true;
		notifyDataSetChanged();
	}
	
	/** 获取菜单列表 */
	public List<MenuItem> getChannnelLst() {
		return channelList;
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** 删除频道列表 */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		isListChanged = true;
		notifyDataSetChanged();
	}
	
	/** 设置菜单列表 */
	public void setListDate(List<MenuItem> list) {
		channelList = list;
	}
	
	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/** 排序是否发生改变 */
	public boolean isListChanged() {
		return isListChanged;
	}
	
	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
	
	/**
	 * 获取图片名称获取图片的资源id的方法
	 * @param imageName
	 * @return
	 */
	public int getResource(String imageName) {
		int resId = context.getResources().getIdentifier(imageName, "drawable",
				context.getPackageName());
		return resId;
	}
}