package com.yifeng.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yifeng.ChifCloud12345update.R;
import com.yifeng.data.FormDAL;
import com.yifeng.face.SmileyParser;
import com.yifeng.manager.LoginBiz;
import com.yifeng.util.AsyncImageLoader;
import com.yifeng.util.AsyncImageLoader.ImageCallback;

/**
 * 
 *  
 * 
 */
public class AsyncAdapter extends SimpleAdapter {

	public int count = 8;
	private int[] colors = new int[] { 0xe7e7e7e7, 0xe7e7e7e7 };

	Context context;
	List<Map<String, Object>> data;
	Resources res;

	ListView listView;

	public AsyncAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to, Resources res,
			ListView listView) {
		super(context, data, resource, from, to);
		this.context = context;
		this.data = data;
		this.res = res;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
	}

	public int selectedPosition = -1;
	public int chockPosition = -2;
	public boolean need = false;// 是否需要按钮功能
	public Handler need2;// 我的关注是否需要按钮功能
	public boolean need1 = false;// 文字转换
	public boolean need3 = false;// 图片显示
	public boolean need4 = false;// 我的关注逻辑标示
	SmileyParser parser;
	public Handler jbHandler; // 交办
	public Handler assignHandler; // 交办回复内容

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		// int colorPos = position % colors.length;
		// view.setBackgroundColor(colors[colorPos]);
		if (need) {
			final FormDAL dal = new FormDAL(context);
			final Button icon1 = (Button) view.findViewById(R.id.b_gz);
			final Button b_jb = (Button) view.findViewById(R.id.b_jb);
			String assign_status = (String) data.get(position).get(
					"ASSIGN_STATUS");
			icon1.setText((data.get(position).get("IS_CONCERNED")).equals("1") ? "取消关注"
					: "关注工单");
			icon1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Map tmap = data.get(position);

					String role_type = LoginBiz.loadUser(context)
							.getRole_type();
					if (icon1.getText().equals("关注工单")) {
						dal.doAddConcern((String) tmap.get("FORM_ID"), LoginBiz
								.loadUser(context).getRole_type(), LoginBiz
								.loadUser(context).getUserId());

						data.get(position).put("IS_CONCERNED", "1");
						if (role_type != null) {
							// 10 市领导 20人大 30政协 40 成员单位领导
							if (role_type.equals("10")) {
								tmap.put("icon1", BitmapFactory.decodeResource(
										res, R.drawable.icon1));
							}
							if (role_type.equals("20")) {
								tmap.put("icon2", BitmapFactory.decodeResource(
										res, R.drawable.icon2));
							}
							if (role_type.equals("30")) {
								tmap.put("icon3", BitmapFactory.decodeResource(
										res, R.drawable.icon3));

							}
							if (role_type.equals("40")) {
								tmap.put("icon4", BitmapFactory.decodeResource(
										res, R.drawable.icon4));

							}
							AsyncAdapter.this.notifyDataSetChanged();
						}

					} else {
						dal.doCancelConcern(
								(String) data.get(position).get("FORM_ID"),
								LoginBiz.loadUser(context).getUserId());
						icon1.setText("关注工单");
					}
				}
			});
			final String FORM_ID = (String) data.get(position).get("FORM_ID");// 工单id
			if (assign_status.equals("-1")) {

				b_jb.setText("工作交办");
				b_jb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Message msg = new Message();
						Bundle data = new Bundle();
						data.putString("form_id", FORM_ID);
						msg.setData(data);
						jbHandler.sendMessage(msg);
					}
				});

			}
			if (assign_status.equals("0")) {
				b_jb.setText("未签收");

				b_jb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Message msg = new Message();
						Bundle data = new Bundle();
						data.putString("form_id", FORM_ID);
						msg.setData(data);
						jbHandler.sendMessage(msg);
					}
				});

			} else if (assign_status.equals("1")) {
				b_jb.setText("已签收");
				b_jb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Message msg = new Message();
						Bundle data = new Bundle();
						data.putString("form_id", FORM_ID);
						msg.setData(data);
						jbHandler.sendMessage(msg);
					}
				});
			} else if (assign_status.equals("2")) {
				b_jb.setText("已回复");
				b_jb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Message msg;
						msg = new Message();
						Bundle data = new Bundle();
						data.putString("form_id", FORM_ID);
						msg.setData(data);
						assignHandler.sendMessage(msg);
					}
				});

			}

		}
		if (selectedPosition == position) {
			view.setBackgroundColor(Color.BLUE);
		} else if (selectedPosition != -1) {
			view.setBackgroundColor(0);

		}
		if (need1) {
			if (context != null) {
				TextView edittext = (TextView) view.findViewById(R.id.content);

				SmileyParser.init(context);
				if (parser == null)
					parser = SmileyParser.getInstance();
				String text = (String) data.get(position).get("CONTENT");

				edittext.setText(parser.addSmileySpans(text));
			}

		}
		if (need3) {

			LinearLayout imglayout = (LinearLayout) view
					.findViewById(R.id.imglayout);
			String IMG_ADD = (String) data.get(position).get("IMG_ADD");
			if (IMG_ADD == null) {
				imglayout.setVisibility(View.GONE);
			} else {
				imglayout.setVisibility(View.VISIBLE);
			}

		}
		// if (chockPosition == position) {
		// RadioButton radio = (RadioButton) view
		// .findViewById(R.id.radioButton);
		// radio.setChecked(true);
		//
		// } else if (chockPosition != -2) {
		// RadioButton radio = (RadioButton) view
		// .findViewById(R.id.radioButton);
		// radio.setChecked(false);
		//
		// }

		if (need2 != null) {
			// 传递handler 完成取消关注
			final FormDAL dal = new FormDAL(context);
			final Button icon1 = (Button) view.findViewById(R.id.b_gz);
			icon1.setText("取消关注");
			icon1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dal.doCancelConcern(
							(String) data.get(position).get("FORM_ID"),
							LoginBiz.loadUser(context).getUserId());
					need2.sendMessage(need2.obtainMessage());
					Toast.makeText(context, "取消关注成功。", Toast.LENGTH_SHORT);
				}
			});
			final String FORM_ID = (String) data.get(position).get("FORM_ID");// 工单id
			final Button b_jb = (Button) view.findViewById(R.id.b_jb);
			String assign_status = (String) data.get(position).get(
					"ASSIGN_STATUS");
			if (assign_status.equals("-1")) {
				b_jb.setVisibility(View.GONE);
			}
			if (assign_status.equals("0")) {
				b_jb.setText("未签收");
				b_jb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

			} else if (assign_status.equals("1")) {
				b_jb.setText("已签收");
				b_jb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
			} else if (assign_status.equals("2")) {
				b_jb.setText("已回复");
				b_jb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Message msg;
						msg = new Message();
						Bundle data = new Bundle();
						data.putString("form_id", FORM_ID);
						msg.setData(data);
						assignHandler.sendMessage(msg);
					}
				});

			}
		}
		return view;
	}

	public void setViewBinder() {
		super.setViewBinder(new MyViewBinder());
	}

	AsyncImageLoader asyncImageLoader;

	public class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if (data != null && (view instanceof ImageView)
					&& (data instanceof String)) {

				String imageUrl = (String) data;
				ImageView imageView = (ImageView) view;
				imageView.setTag(imageUrl);

				view.setTag(imageUrl);

				Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl,
						new ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {
								ImageView imageViewByTag = (ImageView) listView
										.findViewWithTag(imageUrl);
								if (imageViewByTag != null) {
									
									imageViewByTag
											.setImageDrawable(imageDrawable);
									AsyncAdapter.this.notifyDataSetChanged();
								}
							}
						});
				if (cachedImage == null) {
					imageView.setImageResource(R.drawable.user_head);
				} else {
					imageView.setImageDrawable(cachedImage);
				}

				return true;
			} else if (data != null && (view instanceof ImageView)
					&& (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}

}
