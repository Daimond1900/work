package com.winksoft.android.yzjycy.newyzjycy.qzzp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招聘信息
 * Created by 1900 on 2017/4/25.
 */
public class ZpxxFragment extends Fragment {
    private EditText edtTxt_keyword;
    private String area_id = "", c_remark = "", gzxzs_id = "";
    private Spinner spn_area, spn_gzxz;
    private List<Map<String, String>> areas, gzxzs;
    private View view;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;
    int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private boolean isBotom = false;
    private SpringView springView;
    private boolean isTen = false;

    private static final String TAG = "ZpxxFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.fragment_zpxx, null);
        xwzxDAL = new XwzxDAL(getActivity());
        commonUtil = new CommonUtil(getActivity());
        edtTxt_keyword = (EditText) view.findViewById(R.id.edtTxt_keyword);
        MyOnclick onclick = new MyOnclick();
        Button search_btn = (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(onclick);
        spn_area = (Spinner) view.findViewById(R.id.spn_area); // 工作区域
        spn_gzxz = (Spinner) view.findViewById(R.id.spn_gzxz); // 工作性质

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onPostAreaData();
                onPostGzxzData();
                loadDate();
            }
        }, 25);

        listview = (MyListView1) view.findViewById(R.id.id_lv);

        yfbaseAdapter = new YFBaseAdapter(getActivity(), mapList, R.layout.qzt_recruitment_list_item,
                new String[]{"work", "money", "company", "date"},
                new int[]{R.id.workTxt, R.id.moneyTxt, R.id.companyTxt, R.id.dateTxt}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
            }
        };
        listview.setAdapter(yfbaseAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (arg2 == mapList.size()) {
                        return;
                    }
                    String positionId = mapList.get(arg2).get("acb200").toString();// 职位编号
                    Intent intent = new Intent(getActivity(), QztPositionView.class);
                    intent.putExtra("title", "职位详情");
                    intent.putExtra("positionId", positionId);
                    intent.putExtra("companyNameStr", mapList.get(arg2).get("aab004").toString());
                    startActivity(intent);
                } catch (Exception e) {
                    commonUtil.shortToast("未响应!");
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
//		 加载地区
    }


    private void onPostAreaData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                areaResult((String) arg0);
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
        xwzxDAL.doGetParams("AAB301", callBack);
    }

    private void areaResult(String json) {
        List<Map<String, String>> maps;
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("zddm", "");
        map.put("zdmc", "区域");
        list.add(map);
        Map<String, String> m = DataConvert.toMap(json);
        if (m != null) {
            if (m.get("success") != null && m.get("success").equals("true")) {
                maps = DataConvert.toConvertStringList(json, "table");
                for (int i = 0; i < maps.size(); i++) {
                    Map<String, String> m1 = maps.get(i);
                    list.add(m1);
                }
            }
        }
        areas = list;
        if (areas.size() > 0) {
            initArea();
        }
    }


    private void onPostGzxzData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                gzxzResult((String) arg0);
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
        xwzxDAL.doGetParams("ACC201", callBack); // 工作性质
    }

    private void gzxzResult(String json) {
        List<Map<String, String>> maps;
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("zddm", "");
        map.put("zdmc", "工作性质");
        list.add(map);
        Map<String, String> m = DataConvert.toMap(json);
        if (m != null) {
            if (m.get("success") != null && m.get("success").equals("true")) {
                maps = DataConvert.toConvertStringList(json, "table");
                for (int i = 0; i < maps.size(); i++) {
                    Map<String, String> m1 = maps.get(i);
                    list.add(m1);
                }
            }
        }
        gzxzs = list;
        if (gzxzs.size() > 0) {
            iniGzxz();
        }
    }


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
                ((TextView) view.findViewById(R.id.sxyc)).setText("暂无数据      ");
            }
            if (STRINGLIST.size() > 0) {
                view.findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.GONE);
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    tm.put("work", tm.get("aca112"));
                    // 月工资
                    String money = tm.get("acb241") == null ? "" : tm.get("acb241");
                    if (money.equals("")) {
                        money = "面议";
                    }
                    tm.put("money", "月薪:" + money);

                    // 公司名称
                    tm.put("company", tm.get("aab004") == null ? "" : tm.get("aab004"));

                    // 起始日期
                    tm.put("date", tm.get("aae397") == null ? "" : tm.get("aae397"));
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
            ((TextView) view.findViewById(R.id.sxyc)).setText("暂无数据      ");
        }
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
                ((TextView) view.findViewById(R.id.sxyc)).setText("服务器繁忙，请稍后再试！");
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.queryZpxx(temp_data_count_page, area_id, c_remark, "", gzxzs_id, "", "", callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "postResult: 返回数据 Map " + map);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "table"));
                yfbaseAdapter.notifyDataSetChanged();
                listview.setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
        }
    }

    private void initArea() {
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), areas, android.R.layout.simple_spinner_item,
                new String[]{"zdmc"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_area.setAdapter(adapter);
        spn_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                area_id = areas.get(arg2).get("zddm");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void iniGzxz() {    // 工作性质
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), gzxzs, android.R.layout.simple_spinner_item,
                new String[]{"zdmc"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_gzxz.setAdapter(adapter);
        spn_gzxz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                gzxzs_id = gzxzs.get(arg2).get("zddm");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    //模拟刷新数据
    private void refreshData() {

        if (areas == null || areas.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPostAreaData();
                }
            }, 1000);

        }
        if (gzxzs == null || gzxzs.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPostGzxzData();
                }
            }, 1000);
        }

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

    private class MyOnclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search_btn:
                    c_remark = commonUtil.doConvertEmpty(edtTxt_keyword.getText().toString());
//                    refreshData();
//                    yfbaseAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isTen = false;
                            refreshData();
                        }
                    }, 25);
                    break;
                default:
                    break;
            }
        }
    }
}
