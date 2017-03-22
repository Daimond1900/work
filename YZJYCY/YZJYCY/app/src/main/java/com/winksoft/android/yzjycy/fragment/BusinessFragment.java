package com.winksoft.android.yzjycy.fragment;

import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.MenuActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.banner.Banner;
import com.winksoft.banner.BannerConfig;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class BusinessFragment extends Fragment implements OnClickListener {
	private View layout;
	private Banner banner;
	private Object[] images = new Object[] { R.drawable.main_banner1, R.drawable.main_banner2 };
	private View cy_cyfu, cy_cyxmzj, cy_cydx, cy_swfzcyq, cy_cyfhjd, cy_cyzdzj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (layout != null) {
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_business, container, false);
		initView();
		return layout;
	}

	private void initView() {
		initBanner();
		cy_cyfu = layout.findViewById(R.id.cy_cyfu);
		cy_cyxmzj = layout.findViewById(R.id.cy_cyxmzj);
		cy_cydx = layout.findViewById(R.id.cy_cydx);
		cy_swfzcyq = layout.findViewById(R.id.cy_swfzcyq);
		cy_cyfhjd = layout.findViewById(R.id.cy_cyfhjd);
		cy_cyzdzj = layout.findViewById(R.id.cy_cyzdzj);

		cy_cyfu.setOnClickListener(this);
		cy_cyxmzj.setOnClickListener(this);
		cy_cydx.setOnClickListener(this);
		cy_swfzcyq.setOnClickListener(this);
		cy_cyfhjd.setOnClickListener(this);
		cy_cyzdzj.setOnClickListener(this);
	}

	private void initBanner() {
		banner = (Banner) layout.findViewById(R.id.businessbanner);
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		banner.setDelayTime(4000);
		banner.setImages(images);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getContext(), CommonPageView.class);
		switch (v.getId()) {
		case R.id.cy_cyfu: // 创业服务
			intent.putExtra("url", Constants.IP + "wap/serviceguide/listServiceGuide");
			intent.putExtra("title", "服务指南");
			startActivity(intent);
			break;
		case R.id.cy_cyxmzj: // 创业项目征集
			intent.putExtra("url", Constants.IP + "wap/wapuser/viewProject?isAndroid=true");
			intent.putExtra("title", "创业项目征集");
			startActivity(intent);
			break;
		case R.id.cy_cydx: // 创业典型
			intent.putExtra("url", Constants.IP + "wap/typical/listTypical?isAndroid=true");
			intent.putExtra("title", "创业典型");
			startActivity(intent);
			break;
		case R.id.cy_swfzcyq: // 15分钟创业圈
			intent.putExtra("url", Constants.IP + "wap/servicearea/roundGuide?isAndroid=true&openId=");
			intent.putExtra("title", "创业服务圈");
			startActivity(intent);
			break;
		case R.id.cy_cyfhjd: // 创业孵化基地
			intent.putExtra("url", Constants.IP + "wap/incubators/listIncubators?isAndroid=true");
			intent.putExtra("title", "创业孵化基地");
			startActivity(intent);
			break;
		case R.id.cy_cyzdzj: // 创业指导专家
			intent.putExtra("url", Constants.IP + "wap/experts/listExperts?isAndroid=true");
			intent.putExtra("title", "创业指导专家");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
