package com.winksoft.android.yzjycy.newyzjycy.xwzx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.DialogUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 政策资讯 行业资讯 行业资讯 通知公告
 */
public class AllXwFm extends Fragment {

    private static final String TAG = "AllXwFm";

    private String typeid;
    private ImageLoader im;
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private View view;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;
    int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    Dialog proDialog;
    private SpringView springView;
    private boolean isBotom = false;
    private CommonUtil commonUtil;
    private DialogUtil dialogUtil;
    private boolean isTen = false;
    private int lazy_id = 0;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && lazy_id == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadDate();
                }
            }, 25);
            lazy_id = 1;
        }
    }


    @Override
    public void setArguments(Bundle args) {
        typeid = args.getString("typeid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.allxw_fragment, container, false);
        commonUtil = new CommonUtil(getActivity());
        dialogUtil = new DialogUtil(getActivity());
        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(getActivity()));
        xwzxDAL = new XwzxDAL(getActivity());
        listview = (MyListView1) view.findViewById(R.id.id_lv);
        yfbaseAdapter = new YFBaseAdapter(getActivity(), mapList, R.layout.xwzx_yt_list_item,
                new String[]{"header", "typename", "hits", "createtime"},
                new int[]{R.id.header, R.id.xw_type, R.id.xw_hit, R.id.createtime}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
                ImageView xw_img = (ImageView) view.findViewById(R.id.xw_img);
                String imgUrl = (String) data.get(position).get("picpath");
                if (imgUrl != null && !"".equals(imgUrl)) {
                    xw_img.setVisibility(View.VISIBLE);
                    im.displayImage(Constants.IP + imgUrl, xw_img);
                } else {
                    xw_img.setVisibility(View.GONE);
                }
            }
        };
        listview.setAdapter(yfbaseAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == mapList.size()) {
                    return;
                }

                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    String id = mapList.get(arg2).get("id").toString();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("typeid", typeid);
                    String secret_value = DateUtil.getStrCurrentDate();
                    map.put("Date", secret_value);
                    HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
                    Intent intent = new Intent(getActivity(), CommonPageView.class);
                    intent.putExtra("url", Constants.IP + "android/news/viewInfo?id=" + id + "&&typeid=" + typeid + "&&appSign=" + SignHashMap.get("appSign"));
                    intent.putExtra("title", "详情");
                    startActivity(intent);
                }
            }
        });
         /*异常情况刷新*/
        view.findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        springView = (SpringView) view.findViewById(R.id.springview);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        springView.onFinishFreshAndLoad();
                    }
                }, 25);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isBotom) {
                            loadMore();
                        }
                        springView.onFinishFreshAndLoad();
                    }
                }, 25);
            }
        });
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
        return view;
    }


    //模拟刷新数据
    private void refreshData() {
        mapList.clear();
        temp_data_count_page = 0;//回到第一页
        loadDate();
        isBotom = false;
       /* yfbaseAdapter.notifyDataSetChanged();*/
    }


    //模拟加载数据
    private void loadMore() {
        isTen = true;
        temp_data_count_page++;
        loadDate();
        yfbaseAdapter.notifyDataSetChanged();
    }


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        getActivity(), "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                view.findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.queryZxByTypeId(temp_data_count_page, typeid, callBack);
    }

    /**
     * @param json 返回结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "table"));
                listview.setVisibility(View.VISIBLE);
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }
        }
    }


    private void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null) {
            if (STRINGLIST.size() < 10 && STRINGLIST.size() >= 0) {
                isBotom = true;
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
            }
            if (STRINGLIST.size() == 0 && !isTen) {
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
            if (STRINGLIST.size() > 0) {
                view.findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.GONE);
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    for (String ts : tm.keySet()) {
                        otm.put(ts, tm.get(ts));
                    }
                    mapList.add(otm);
                }
            }
        } else {
            view.findViewById(R.id.islayout).setVisibility(View.GONE);
            view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
            view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 将layout从父组件中移除
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
    }
}
