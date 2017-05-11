package com.winksoft.android.yzjycy.newyzjycy.manage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.fragment.MyFragmentPagerAdapter;
import com.winksoft.android.yzjycy.newyzjycy.xwzx.AllXwFm;
import com.winksoft.android.yzjycy.newyzjycy.xwzx.HomeFm;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;

import java.util.ArrayList;

/**
 * 首页 和 所有资讯 的管理类
 * Created by 1900 on 2017/4/18.
 */

public class XwFragmentMain extends Fragment implements View.OnClickListener {
    private Button btLogin;
    private ViewPager mViewPager;
    private NavigationTabStrip mCenterNavigationTabStrip;
    private View view;
    private ArrayList<Fragment> fragmentsList;
    private CommonUtil commonUtil;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mainmainfragment, container, false);
        initUI();
        setUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Constants.iflogin) {
            btLogin.setText("登录");
        } else {
            btLogin.setText("退出");
        }
    }


    private void initUI() {
        commonUtil = new CommonUtil(getActivity());
        btLogin = (Button) view.findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.vp);
        fragmentsList = new ArrayList<>();

        Fragment fragment1 = new HomeFm();  // 首页

        Fragment fragment2 = new AllXwFm(); // 政策资讯
        Bundle bundle1 = new Bundle();
        bundle1.putString("typeid", "1");
        fragment2.setArguments(bundle1);

        Fragment fragment3 = new AllXwFm(); // 行业资讯
        Bundle bundle2 = new Bundle();
        bundle2.putString("typeid", "2");
        fragment3.setArguments(bundle2);

        Fragment fragment4 = new AllXwFm(); // 通知公告
        Bundle bundle3 = new Bundle();
        bundle3.putString("typeid", "3");
        fragment4.setArguments(bundle3);

        fragmentsList.add(fragment1);
        fragmentsList.add(fragment2);
        fragmentsList.add(fragment3);
        fragmentsList.add(fragment4);

        mCenterNavigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.nts_center);
    }

    private void setUI() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
        mCenterNavigationTabStrip.setViewPager(mViewPager, 0);  //设置默认第一个显示的viewpage
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLogin:
                if (!Constants.iflogin) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                } else {
                    doTc();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 取消收藏的提示框
     */
    public void doTc() {
        final Dialog builder = new Dialog(getActivity(), R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("退出");
        pMsg.setText("确定退出应用吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                Constants.iflogin = false;
                btLogin.setText("登录");
                commonUtil.shortToast("退出成功！");
                builder.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
//        builder.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }
}


