package com.winksoft.android.yzjycy.newyzjycy.pxkq.all;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.ui.pxxx.PxDetailsActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DialogUtil;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有培训信息
 */
public class AllpxFragment extends Fragment {
    private View view;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;
    private SpringView springView;
    private boolean isBotom = false;
    int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private DialogUtil dialogUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.fragment_allpx, null);
        xwzxDAL = new XwzxDAL(getActivity());
        commonUtil = new CommonUtil(getActivity());
        dialogUtil = new DialogUtil(getActivity());
        loadDate();
        listview = (MyListView1) view.findViewById(R.id.id_lv);

        yfbaseAdapter = new YFBaseAdapter(getActivity(), mapList, R.layout.pxxx_list_item,
                new String[]{"class_name", "training_agent_name", "training_type", "training_job_type",
                        "training_level", "opening_time", "enrolled_cnt"},
                new int[]{R.id.bjmc, R.id.pxjg, R.id.pxlx, R.id.pxgz, R.id.pxdj, R.id.jzrq, R.id.rs}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
                ImageView rs_imag = (ImageView) view.findViewById(R.id.rs_imag);
                TextView rs = (TextView) view.findViewById(R.id.rs);
                Map<String, ?> map = data.get(position);
                if ("0".equals(map.get("enrolled_cnt").toString().trim())) { /*隐藏报名小图标*/
                    rs.setVisibility(View.GONE);
                    rs_imag.setVisibility(View.GONE);
                } else {    /*显示报名小图标*/
                    rs.setVisibility(View.VISIBLE);
                    rs_imag.setVisibility(View.VISIBLE);
                }
            }
        };
        listview.setAdapter(yfbaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    try {
                        Intent intent = new Intent(getActivity(), PxDetailsActivity.class);        //跳转至详情界面
                        String class_id = mapList.get(arg2).get("class_id").toString();
                        String class_name = mapList.get(arg2).get("class_name").toString();
                        String training_agent_name = mapList.get(arg2).get("training_agent_name").toString();
                        String training_type = mapList.get(arg2).get("training_type").toString();
                        String training_job_type = mapList.get(arg2).get("training_job_type").toString();
                        String training_level = mapList.get(arg2).get("training_level").toString();
                        String opening_time = mapList.get(arg2).get("opening_time").toString();

                        intent.putExtra("class_id", class_id);
                        intent.putExtra("class_name", class_name);
                        intent.putExtra("training_agent_name", training_agent_name);
                        intent.putExtra("training_type", training_type);
                        intent.putExtra("training_job_type", training_job_type);
                        intent.putExtra("training_level", training_level);
                        intent.putExtra("opening_time", opening_time);
                        startActivity(intent);
                    } catch (Exception e) {
                        commonUtil.shortToast("未响应!");
                    }
                }
            }
        });
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
                listview.setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                listview.setVisibility(View.GONE);
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
        xwzxDAL.queryPxxx(temp_data_count_page, "", callBack);
    }

    /**
     * @param json 处理结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "attendance"));
                listview.setVisibility(View.VISIBLE);
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }
        }
    }


    //模拟刷新数据
    private void refreshData() {
        mapList.clear();
        temp_data_count_page = 0;//回到第一页
        loadDate();
        isBotom = false;
    }


    //模拟加载数据
    private void loadMore() {
        temp_data_count_page++;
        loadDate();
        yfbaseAdapter.notifyDataSetChanged();
    }

    //*************************************************************************

    private void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null) {
            if (STRINGLIST.size() < 10 && STRINGLIST.size() >= 0) {
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
            }
            if (STRINGLIST.size() == 0) {
                listview.setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
            if (STRINGLIST.size() > 0) {
                listview.setVisibility(View.VISIBLE);
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
            listview.setVisibility(View.GONE);
            view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
            view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
        }
    }
}
