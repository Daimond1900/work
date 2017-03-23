package com.winksoft.android.yzjycy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.entity.MenuItem;

@SuppressLint({ "InflateParams", "ViewHolder" })
public abstract class OtherAdapter extends BaseAdapter {

	private Context context;
	public List<MenuItem> channelList;
	private TextView item_text;
	/** 是否可见 在移动动画完毕之前不可见，动画完毕后可见 */
	boolean isVisible = true;
	/** 要删除的position */
	public int remove_position = -1;
	/** 是否是用户菜单 */
	private boolean isUser = false;
	
	private LayoutInflater inflater;

	private boolean isShowDelete = false;// 根据这个变量来判断是否显示图标

	public OtherAdapter(Context context, List<MenuItem> channelList,
			boolean isUser) {
		this.context = context;
		this.channelList = channelList;
		this.isUser = isUser;
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
	
	
	protected abstract void addView(View view, int position);

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view = inflater.inflate(R.layout.mygridview_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		ImageView ivPlus = (ImageView) view.findViewById(R.id.ivPlus);
		ivPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addView(view,position);
			}
		});
		ImageView img = (ImageView) view.findViewById(R.id.img);
		ImageView ivSelected = (ImageView) view.findViewById(R.id.ivSelected);
		MenuItem channel = getItem(position);
		
		img.setImageResource(getResource(channel.getResName()));
		
		if(isShowDelete){
			view.setBackgroundResource(R.drawable.subscribe_item_bg);
			if(channel.selected == 1){
				ivSelected.setVisibility(View.VISIBLE);
				ivPlus.setVisibility(View.GONE);
			}else{
				ivSelected.setVisibility(View.GONE);
				ivPlus.setVisibility(View.VISIBLE);
			}
		}else{
			view.setBackgroundResource(0);
			ivPlus.setVisibility(View.GONE);
		}
		item_text.setText(channel.getName());

		if (!isVisible && (position == -1 + channelList.size())) {
//			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if (remove_position == position) {
//			item_text.setText("");
		}
		return view;
	}

	/** 获取菜单列表 */
	public List<MenuItem> getChannnelLst() {
		return channelList;
	}

	/** 添加菜单列表 */
	public void setItem(int position) {
		getItem(position).setSelected(0);
		notifyDataSetChanged();
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** 删除菜单列表 */
	public void remove() {
		getItem(remove_position).setSelected(1);
		remove_position = -1;
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

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
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
