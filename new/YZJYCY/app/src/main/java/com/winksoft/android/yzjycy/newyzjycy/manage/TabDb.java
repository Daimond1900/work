package com.winksoft.android.yzjycy.newyzjycy.manage;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.newyzjycy.wd.MineFragment;
import com.winksoft.android.yzjycy.newyzjycy.shebao.SheBaoFm;

/**
 * 首页 头部 底部 的 管理类
 * Created by 1900 on 2017/4/18.
 */
public class TabDb {
    /**
     * 获得底部所有项
     */
    public static String[] getTabsTxt() {
        return new String[]{"首页", "求职招聘", "社保查询", "培训报名", "我的"};
    }

    /**
     * 获得所有碎片
     */
    public static Class[] getFramgent() {
        return new Class[]{XwFragmentMain.class, ZpFragmentMain.class, SheBaoFm.class, PxFragmentMain.class, MineFragment.class};
    }

    /**
     * 获得所有点击前的图片
     */
    public static int[] getTabsImg() {
        return new int[]{R.mipmap.home, R.mipmap.jy, R.mipmap.sb, R.mipmap.px, R.mipmap.wd};
    }

    /**
     * 获得所有点击后的图片
     */
    public static int[] getTabsImgLight() {
        return new int[]{R.mipmap.home_, R.mipmap.jy_, R.mipmap.sb_, R.mipmap.px_, R.mipmap.wd_};
    }
}
