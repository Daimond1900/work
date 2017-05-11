package com.winksoft.android.yzjycy.newyzjycy.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.fragment.MyFragmentPagerAdapter;
import com.winksoft.android.yzjycy.newyzjycy.pxkq.all.AllpxFragment;
import com.winksoft.android.yzjycy.newyzjycy.pxkq.me.WdpxFragment;

import java.util.ArrayList;

/**
 * 所有培训、我的培训 的管理类
 * Created by 1900 on 2017/4/18.
 */
public class PxFragmentMain extends Fragment {
    private ViewPager mViewPager;
    private NavigationTabStrip mCenterNavigationTabStrip;
    private View view;
    private ArrayList<Fragment> fragmentsList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mainmainfragment2, container, false);
        initUI();
        setUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initUI() {
        mViewPager = (ViewPager) view.findViewById(R.id.vp);
        fragmentsList = new ArrayList<>();

        Fragment fragment1 = new AllpxFragment();
        Fragment fragment2 = new WdpxFragment();

        fragmentsList.add(fragment1);
        fragmentsList.add(fragment2);

        mCenterNavigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.nts_center);
    }

    private void setUI() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
        mCenterNavigationTabStrip.setViewPager(mViewPager, 0);  //设置默认第一个显示的viewpage
    }
}


