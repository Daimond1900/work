package com.winksoft.android.yzjycy.newyzjycy.pxkq.me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.ui.pxxx.KqInfoDetailsActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的培训
 */
public class WdpxFragment extends Fragment implements View.OnClickListener {
    private TextView tx_dl;
    private View view;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;

    int temp_data_count_page = 0;//临时存放当前加载页对应的pos

    XwzxDAL xwzxDAL;
    Dialog proDialog;

    private int sc = 0;
    private SpringView springView;
    private boolean isBotom = false;
    private boolean isTen = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.fragment_wdpx, null);
        xwzxDAL = new XwzxDAL(getActivity());
        tx_dl = (TextView) view.findViewById(R.id.tx_dl);
        tx_dl.setOnClickListener(this);

        listview = (MyListView1) view.findViewById(R.id.id_lv);


        yfbaseAdapter = new YFBaseAdapter(getActivity(), mapList, R.layout.kq_list_item,
                new String[]{"class_id", "class_name"},
                new int[]{R.id.classid, R.id.classname}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
            }
        };
        listview.setAdapter(yfbaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == mapList.size()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), KqInfoDetailsActivity.class);
                String class_id = mapList.get(arg2).get("class_id").toString();
                intent.putExtra("class_id", class_id);
                startActivity(intent);
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
                        if(!isBotom) {
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

    @Override
    public void onResume() {
        super.onResume();
        if (!Constants.iflogin) {
            tx_dl.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
            view.findViewById(R.id.kb).setVisibility(View.GONE);
            view.findViewById(R.id.wlyc).setVisibility(View.GONE);
            isBotom = true;
            springView.setEnable(true);
            springView.setGive(SpringView.Give.TOP);
            springView.getFooterView().setVisibility(View.GONE);
        } else if (Constants.iflogin && sc == 0) {
            tx_dl.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            loadDate();
            sc++;
        } else {
            tx_dl.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
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
        isTen = true;
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


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
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
        xwzxDAL.doKqInfoQuery(temp_data_count_page, "", callBack);
    }

    /**
     * @param json  返回结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "enrolledTraningList"));
                listview.setVisibility(View.VISIBLE);
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_dl:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            default:
                break;
        }
    }
}
