package com.winksoft.android.yzjycy.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.fragment.BusinessFragment;
import com.winksoft.android.yzjycy.fragment.HomeFragment;
import com.winksoft.android.yzjycy.fragment.JobFragment;
import com.winksoft.android.yzjycy.fragment.MineFragment;
import com.yifeng.nox.android.ui.DialogUtil;
import com.yifeng.nox.android.util.ActivityStackControl;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private Class<?>[] mFragments = new Class[]{
            HomeFragment.class,
            JobFragment.class,
            BusinessFragment.class,
            HomeFragment.class, // 社保
            MineFragment.class};
    private int[] mTabSelectors = new int[]{
            R.drawable.main_bottom_tab_home,
            R.drawable.main_bottom_tab_jy,
            R.drawable.main_bottom_tab_cy,  // 改为咨询 待确认
            R.drawable.main_bottom_tab_home,  // 改为社保 待确认
            R.drawable.main_bottom_tab_mine};
    private String[] mTabSpecs = new String[]{"首页", "就业", "资讯","社保","我的"};

    DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStackControl.add(this);
        dialogUtil = new DialogUtil(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        addTab();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addTab() {
        for (int i = 0; i < mTabSelectors.length; i++) {
            View tabIndicator = getLayoutInflater().inflate(
                    R.layout.tab_indicator, null);
            ImageView imageView = (ImageView) tabIndicator.findViewById(R.id.imageView1);
            imageView.setImageResource(mTabSelectors[i]);
            TextView text_item = (TextView) tabIndicator.findViewById(R.id.text_item);
            text_item.setText(mTabSpecs[i]);
            mTabHost.addTab(mTabHost.newTabSpec(mTabSpecs[i]).setIndicator(tabIndicator), mFragments[i], null);
        }
    }

    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //若当前不在主页，则先返回主页
                if (mTabHost.getCurrentTab() != 0) {
                    mTabHost.setCurrentTab(0);
                    return false;
                }

                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(MainActivity.this, "再按一下返回键退出程序！",
                            Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                    return false;
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
