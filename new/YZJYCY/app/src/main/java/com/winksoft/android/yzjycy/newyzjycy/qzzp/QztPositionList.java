package com.winksoft.android.yzjycy.newyzjycy.qzzp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.winksoft.android.yzjycy.BaseActivity;
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
 * 该公司所有职位 列表 招聘信息 列表
 */
public class QztPositionList extends BaseActivity {
    private String companyId = "", zphId = "", flag = "";
    private SpringView springView;
    private Boolean isflage = true;
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;
    int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private boolean isBottom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_position_list);
        commonUtil = new CommonUtil(this);
        xwzxDAL = new XwzxDAL(this);
        TextView companyName = (TextView) findViewById(R.id.companyName);
        TextView headerstr = (TextView) findViewById(R.id.headerstr);
        zphId = this.getIntent().getStringExtra("zphId") == null ? "" : this.getIntent().getStringExtra("zphId");


        flag = this.getIntent().getStringExtra("flag") == null ? "" : this.getIntent().getStringExtra("flag");


        if (!"".equals(zphId)) {
            if ("1".equals(flag)) {
                headerstr.setText("招聘单位");
            } else {
                headerstr.setText("招聘信息");
            }
            isflage = false;
        }
        companyId = this.getIntent().getStringExtra("companyId") == null ? "" : this.getIntent().getStringExtra("companyId");
        companyName.setText(this.getIntent().getStringExtra("companyName") == null ? "" : this.getIntent().getStringExtra("companyName"));

        loadDate();
        listview = (MyListView1) findViewById(R.id.id_lv);
        yfbaseAdapter = new YFBaseAdapter(this, mapList, R.layout.qzt_recruitment_list_item,
                new String[]{"work", "money", "company", "date"},
                new int[]{R.id.workTxt, R.id.moneyTxt, R.id.companyTxt, R.id.dateTxt}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
            }
        };
        listview.setAdapter(yfbaseAdapter);

        if (!"1".equals(flag)) {
            listview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    try {
                        if (arg2 == mapList.size()) {
                            return;
                        }
                        String positionId = mapList.get(arg2).get("acb200").toString();// 职位编号
                        Intent intent = new Intent(QztPositionList.this, QztPositionView.class);
                        intent.putExtra("title", "职位详情");
                        intent.putExtra("positionId", positionId);
                        intent.putExtra("isClick", isflage);//进到职位详细信息 公司名称不可点
                        intent.putExtra("companyNameStr", mapList.get(arg2).get("aab004").toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonUtil.shortToast("未响应!");
                    }
                }
            });
        } else {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    try {
                        if (arg2 == mapList.size()) {
                            return;
                        }
                        Intent intent = new Intent(QztPositionList.this, QztEnterpriseView.class);
                        intent.putExtra("title", "企业信息");
                        intent.putExtra("zphId", zphId);
                        intent.putExtra("companyId", mapList.get(arg2).get("aab001").toString());
                        intent.putExtra("companyName", mapList.get(arg2).get("aab004").toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        commonUtil.shortToast("未响应!");
                    }
                }
            });
        }
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                QztPositionList.this.finish();
            }
        });

        springView = (SpringView) findViewById(R.id.springview);
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
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
    }


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionList.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                isBottom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };

        if ("1".equals(flag)) {
            xwzxDAL.queryZphZpdw(temp_data_count_page, zphId, callBack);
        } else if ("2".equals(flag)) {
            xwzxDAL.queryZphZpxx(temp_data_count_page, zphId, companyId, callBack);
        } else {
            xwzxDAL.queryZpxx(temp_data_count_page, "", "", "", "", companyId, zphId, callBack);
        }
    }

    private void postResult(String json) {

        Log.d("qzt", "postResult: 返回结果  = " + json);

        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "table"));
                yfbaseAdapter.notifyDataSetChanged();
                listview.setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
            }
        }
    }

    //模拟刷新数据
    private void refreshData() {
        mapList.clear();
        temp_data_count_page = 0;//回到第一页
        loadDate();
        isBottom = false;
    }


    //模拟加载数据
    private void loadMore() {
        temp_data_count_page++;
        loadDate();
        yfbaseAdapter.notifyDataSetChanged();
    }

    private void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null) {
            if (STRINGLIST.size() < 10 && STRINGLIST.size() >= 0) {
                isBottom = true;
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
            }
            if (STRINGLIST.size() == 0) {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
            if (STRINGLIST.size() > 0) {
                findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                if (!"1".equals(flag)) {
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
                } else {
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
            }
        } else {
            findViewById(R.id.islayout).setVisibility(View.GONE);
            findViewById(R.id.kb).setVisibility(View.VISIBLE);
            findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
        }
    }
}
