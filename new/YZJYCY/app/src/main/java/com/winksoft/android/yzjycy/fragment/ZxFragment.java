package com.winksoft.android.yzjycy.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.winksoft.android.widget.MyGridView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.RecuitDal;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.ZpdwFragment;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.ZpxxFragment;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.banner.Banner;
import com.winksoft.in.srain.cube.views.ptr.PtrClassicFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.winksoft.android.yzjycy.fragment.HomeFragment.MyReceiver;

public class ZxFragment extends Fragment {
	private CommonUtil commonUtil;
	private RecuitDal reciotDal;
	private String c_name = "", area_id = "", c_remark = "";
	private View layout;
	private Banner banner;
	private Object[] images = new Object[] { R.drawable.main_banner1, R.drawable.main_banner2 };
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> zpList;
	private MyGridView mainGv;
	SimpleAdapter adapter;
	private ListView job_zp_list;
	private List<Map<String, Object>> zpReturnlist = new ArrayList<Map<String, Object>>();
	private CommonAdapter commAdapter;
	private int pageNum = 0;
	private boolean isLoading = true;// 标志正在加载数据
	private PtrClassicFrameLayout mPtrFrame;
	private ScrollView mScrollView;
	Resources resources;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private ImageView ivBottomLine;
	private TextView tvTabNew, tvTabHot,tvTabNew1, tvTabHot2,tvTabNew3;

	private int currIndex = 0;
	private int bottomLineWidth;
	private int offset = 0;
	private int position_one;
	public final static int num = 5 ;
	Fragment zpxxFragment;
	Fragment zpdwFragment;
	Fragment scggFragment;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (layout != null) {
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_zx, container, false);
		resources = getResources();
		InitWidth(layout);
		InitTextView(layout);
		InitViewPager(layout);
		TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
		tvTabHot.setTextColor(resources.getColor(R.color.black));
		animation.setFillAfter(true);
		animation.setDuration(300);
		ivBottomLine.startAnimation(animation);
		initView();
		return layout;
	}

	private void initView() {

	}




	private void InitTextView(View parentView) {
		tvTabNew = (TextView) parentView.findViewById(R.id.tv_tab_1);
		tvTabHot = (TextView) parentView.findViewById(R.id.tv_tab_2);
		tvTabNew1 = (TextView) parentView.findViewById(R.id.tv_tab_3);

		tvTabNew.setOnClickListener(new MyOnClickListener(0));
		tvTabHot.setOnClickListener(new MyOnClickListener(1));
		tvTabNew1.setOnClickListener(new MyOnClickListener(2));

	}

	private void InitViewPager(View parentView) {
		mPager = (ViewPager) parentView.findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();

		zpxxFragment = new ZpxxFragment();
		zpdwFragment = new ZpdwFragment();

		scggFragment = new ZpdwFragment();

		fragmentsList.add(zpxxFragment);
		fragmentsList.add(zpdwFragment);
		fragmentsList.add(scggFragment);



		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setCurrentItem(0);

	}

	private void InitWidth(View parentView) {
		ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((screenW / num - bottomLineWidth) / 2);
		int avg = (int) (screenW / num);
		position_one = avg + offset;


	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
				case 0:
					if (currIndex == 1) {
						animation = new TranslateAnimation(position_one, offset, 0, 0);
						tvTabHot.setTextColor(resources.getColor(R.color.black));
					}
					tvTabNew.setTextColor(resources.getColor(R.color.blue));
					break;
				case 1:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, position_one, 0, 0);
						tvTabNew.setTextColor(resources.getColor(R.color.black));
					}
					tvTabHot.setTextColor(resources.getColor(R.color.blue));
					break;
				case 2:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, position_one, 0, 0);
						tvTabHot.setTextColor(resources.getColor(R.color.black));
					}
					tvTabNew1.setTextColor(resources.getColor(R.color.blue));
					break;
				case 3:
					if (currIndex == 0) {
						animation = new TranslateAnimation(offset, position_one, 0, 0);
						tvTabNew1.setTextColor(resources.getColor(R.color.black));
					}
					tvTabHot2.setTextColor(resources.getColor(R.color.blue));
					break;
				case 4:
					if (currIndex == 1) {
						animation = new TranslateAnimation(offset, position_one, 0, 0);
						tvTabHot2.setTextColor(resources.getColor(R.color.black));
					}
					tvTabNew3.setTextColor(resources.getColor(R.color.blue));
					break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
