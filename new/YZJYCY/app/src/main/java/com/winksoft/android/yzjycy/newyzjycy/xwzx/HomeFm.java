package com.winksoft.android.yzjycy.newyzjycy.xwzx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.winksoft.android.yzjycy.newyzjycy.zph.ZphActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.DialogUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.winksoft.banner.Banner;
import com.winksoft.banner.BannerConfig;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页新闻
 */
public class HomeFm extends Fragment {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private Banner banner;
    private Object[] images = new Object[]{R.drawable.main_banner1,
            R.drawable.main_banner2};
    private YFBaseAdapter yfbaseAdapter;
    private ImageLoader im;
    private boolean isScjz = true; // 加载首页默认显示的10条数据
    private List<String> zphIds = new ArrayList<>();
    private List<Object> imgUrls = new ArrayList<>();
    private View view;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    Dialog proDialog;
    private boolean isBottom = false;
    private String imgUrl;
    private SpringView springView;
    private CommonUtil commonUtil;
    public DialogUtil dialogUtil;

    private int lazy_id = 0;

    private boolean isTen = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && lazy_id == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadBannerImg();
                    loadDate();
                }
            }, 25);
            lazy_id = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        xwzxDAL = new XwzxDAL(getActivity());
        commonUtil = new CommonUtil(getActivity());
        dialogUtil = new DialogUtil(getActivity());
        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(getActivity()));
        /*加载Banner招聘会图片*/

        listview = (MyListView1) view.findViewById(R.id.id_lv);

        yfbaseAdapter = new YFBaseAdapter(getActivity(), mapList, R.layout.xwzx_yt_list_item,
                new String[]{"header", "typename", "hits", "createtime"},
                new int[]{R.id.header, R.id.xw_type, R.id.xw_hit, R.id.createtime}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
                ImageView xw_img = (ImageView) view.findViewById(R.id.xw_img);
                imgUrl = (String) data.get(position).get("picpath");
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
                Log.d("home", "onItemClick: " + arg2);
                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    String id = mapList.get(arg2).get("id").toString();
                    String typeid = mapList.get(arg2).get("typeid").toString();
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
                        if (!isBottom) {
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

    private void loadBannerImg() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postLoadBannerImgResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        String pageSize = "3";
        String dqId = "";
        xwzxDAL.queryZph(pageSize, dqId, callBack);        // 查询首页的Banner
    }

    /**
     * @param json 返回结果
     */
    private void postLoadBannerImgResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                List<Map<String, String>> tables = DataConvert.toConvertStringList(json, "table");// 待定
                imgUrls = new ArrayList<>();
                String url;
                for (Map<String, String> table : tables) {
                    if (table.get("picpath") == null || "".equals(table.get("picpath"))) {
                        url = Constants.IP + "attached/recruit/main_banner1.png";
                    } else {
//                        url = Constants.IP + table.get("picpath").replace("\\", "/");
                        Log.d("heool", "postLoadBannerImgResult: 图片地址" + table.get("picpath").replace("\\", "/"));
                        url = table.get("picpath").replace("\\", "/");
//                        tpdz.substring(2);
//                        url = "http://222.189.216.110:8010" + tpdz.substring(2);
                    }
                    String zphId = table.get("acb330") != null && !"".equals(table.get("acb330")) ? table.get("acb330") : "";
                    zphIds.add(zphId);
                    imgUrls.add(url);
                }
                banner.setImages(imgUrls); // 加载banner图片 默认的
            } else {
                banner.setImages(images); // 加载banner图片 默认的
            }
        } else {
            banner.setImages(images); // 加载banner图片 默认的
        }
    }

    private void initView() {
        banner = (Banner) view.findViewById(R.id.banner1);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setDelayTime(4000);
        // 首页招聘会图片点击事件
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                if (zphIds != null && zphIds.size() > 0) {
                    Intent intent = new Intent(getActivity(), ZphActivity.class);
                    intent.putExtra("id", zphIds.get(position - 1) != null && !"".equals(zphIds.get(position - 1)) ? zphIds.get(position - 1) : "");
                    intent.putExtra("zphtp", imgUrls.get(position - 1) != null && !"".equals(imgUrls.get(position - 1)) ? imgUrls.get(position - 1).toString() : "");
                    startActivity(intent);
                }
            }
        });
    }


    //模拟刷新数据
    private void refreshData() {
        mapList.clear();
        temp_data_count_page = 0;//回到第一页
        isScjz = true;
        loadBannerImg();
        loadDate();
        isBottom = false;
    }


    //模拟加载数据
    private void loadMore() {
        isTen = true;
        loadDate();
        temp_data_count_page++;
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
                banner.setVisibility(View.VISIBLE);
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
                //网络异常时调用
                banner.setVisibility(View.GONE);
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                isBottom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        if (isScjz) {
            xwzxDAL.queryXwMr(callBack);        // 首页默认10条数据
            isScjz = false;
        } else {
            xwzxDAL.queryXwRef(temp_data_count_page, callBack);       // 首页刷新后显示的数据
        }
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
                banner.setVisibility(View.GONE);
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
        }
    }

    private void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null) {
            if (STRINGLIST.size() < 10 && STRINGLIST.size() >= 0) {
                isBottom = true;
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
