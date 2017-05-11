package com.winksoft.android.yzjycy.fragment;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.activity.NewZxMainActivity;
import com.winksoft.android.yzjycy.adapter.GalleryAdapter;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.banner.Banner;
import com.winksoft.banner.BannerConfig;
import com.winksoft.nox.android.ui.BaseListActivity1;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseListActivity1 implements OnClickListener {
    private static final String TAG = "HomeFragment";
    private Banner banner;
    private Object[] images = new Object[]{R.drawable.main_banner1,
            R.drawable.main_banner2};
    private View layout;
    private Button btZxMore;
    Dialog proDialog;
    private YFBaseAdapter adapter;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return layout;
        }
        layout = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initHeader();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(getActivity(), mDatas);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), NewZxMainActivity.class);
                        intent.putExtra("index",0);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), NewZxMainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), NewZxMainActivity.class);
                        intent.putExtra("index",2);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), NewZxMainActivity.class);
                        intent.putExtra("index",3);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), NewZxMainActivity.class);
                        intent.putExtra("index",4);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        xwzxDAL = new XwzxDAL(getActivity());
        commonUtil = new CommonUtil(getActivity());
        intiListview(true, false, layout);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent = new Intent(getActivity(), KqInfoDetailsActivity.class);
//				String class_id = SURPERDATA.get(arg2).get("class_id").toString();
//				intent.putExtra("class_id", class_id);
//				startActivity(intent);
            }
        });
        return layout;
    }

    private void initHeader() {
        mDatas = new ArrayList<>(Arrays.asList("就业新闻", "创业新闻", "市场公告", "政策法规", "工资指导价"));
    }

    private void initView() {
        initBanner();
    }


    private void initBanner() {
        banner = (Banner) layout.findViewById(R.id.banner1);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setDelayTime(4000);
        banner.setImages(images);
    }


    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(getActivity(), SURPERDATA, R.layout.syxwmr_list_item,
                new String[]{"header", "createtime"},
                new int[]{R.id.header, R.id.createtime}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
            }
        };
        return adapter;
    }

    @Override
    protected void myNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                Log.d(TAG, "onStart");
                super.onStart();
                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        getActivity(), "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                Log.d(TAG, "onSuccess");
                super.onSuccess(arg0);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                Log.d(TAG, "onFailure");
                super.onFailure(t, strMsg);
                if (proDialog != null)
                    proDialog.dismiss();
                onListviewonFailure();
            }
        };
        xwzxDAL.queryXwMr(callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Log.d(TAG, "postResult: json" + json);
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "postResult: map" + map);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "table");
                Log.d(TAG, "STRINGLIST: STRINGLIST" + STRINGLIST);
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
            }
        } else {
            onListviewNoResult();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 将layout从父组件中移除
        ViewGroup parent = (ViewGroup) layout.getParent();
        parent.removeView(layout);
    }

    /**
     * 判断
     *
     * @param iflogin
     * @param intent
     */
    private void ifLogin(boolean iflogin, Intent intent) {
        if (iflogin) {
            startActivity(intent);
        } else {
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }



}
