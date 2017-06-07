package com.winksoft.android.yzjycy.newyzjycy.qzzp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
 * 招聘单位
 * Created by 1900 on 2017/4/25.
 */
public class ZpdwFragment extends Fragment {
    private EditText edtTxt_remark;
    private String area_id = "";
    private String c_remark = "";
    private Spinner spn_area;
    private List<Map<String, String>> areas = new ArrayList<>();
    private View view;
    private MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    private YFBaseAdapter yfbaseAdapter;

    int temp_data_count_page = 0;//临时存放当前加载页对应的pos

    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private SpringView springView;
    private boolean isBotom = false;
    private boolean isTen = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            // 防止多次new出片段对象，造成图片错乱问题
            return view;
        }
        view = inflater.inflate(R.layout.fragment_zpdw, null);
        xwzxDAL = new XwzxDAL(getActivity());
        commonUtil = new CommonUtil(getActivity());
        edtTxt_remark = (EditText) view.findViewById(R.id.edtTxt_remark);
        MyOnclick onclick = new MyOnclick();
        Button search_btn = (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(onclick);
        spn_area = (Spinner) view.findViewById(R.id.spn_area); // 工作区域

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onPostAreaData();
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
                    Intent intent = new Intent(getActivity(), QztEnterpriseView.class);
                    intent.putExtra("title", "企业信息");
                    intent.putExtra("companyId", mapList.get(arg2).get("aab001").toString());
                    intent.putExtra("companyName", mapList.get(arg2).get("aab004").toString());
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
            /*异常情况刷新*/
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


    protected void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null) {
            if (STRINGLIST.size() < 10 && STRINGLIST.size() >= 0) {
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
            }
            if (STRINGLIST.size() == 0  && !isTen) {
                view.findViewById(R.id.islayout).setVisibility(View.GONE);
                view.findViewById(R.id.kb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.sxyc)).setText("暂无数据    ");
            }
            if (STRINGLIST.size() > 0) {
                view.findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.kb).setVisibility(View.GONE);
                view.findViewById(R.id.wlyc).setVisibility(View.GONE);
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    tm.put("work", tm.get("aab004"));
                    // 招聘人数
                    String rs = tm.get("acb240_count") == null ? "" : tm.get("acb240_count");
                    if (rs.equals("")) {
                        rs = "不限";
                    }
                    tm.put("money", "招聘人数:" + rs);
                    String zws = tm.get("aca111_count") == null ? "" : tm.get("aca111_count");
                    // 工种
                    tm.put("company", "招聘职位数:" + zws);

                    // 起始日期
                    tm.put("date", tm.get("shrq") == null ? "" : tm.get("shrq"));
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
            ((TextView) view.findViewById(R.id.sxyc)).setText("暂无数据    ");
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
                ((TextView) view.findViewById(R.id.sxyc)).setText("暂无数据，下拉刷新！");
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        String c_name = "";
        xwzxDAL.queryZpdw(temp_data_count_page, area_id, c_remark, c_name, "", callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
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


    private class MyOnclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search_btn:
                    c_remark = commonUtil.doConvertEmpty(edtTxt_remark.getText().toString());
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
