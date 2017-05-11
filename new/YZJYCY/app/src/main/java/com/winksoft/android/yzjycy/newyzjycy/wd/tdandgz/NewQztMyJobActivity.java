package com.winksoft.android.yzjycy.newyzjycy.wd.tdandgz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
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
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.QztEnterpriseView;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.QztPositionView;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我投递的职位   1
 * 我收藏的职位   3
 * Created by 1900 on 2017/4/12.
 */

public class NewQztMyJobActivity extends BaseActivity {
    private User user;
    private String flag = "";
    MyListView1 listview;
    List<Map<String, Object>> mapList = new ArrayList<>();
    YFBaseAdapter yfbaseAdapter;
    private int a = 0;

    int temp_data_count_page = 0;//临时存放当前加载页对应的pos

    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private SpringView springView;
    private boolean isBotom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newqztmyjobactivity);
        TextView headerstr = (TextView) findViewById(R.id.headerstr);
        commonUtil = new CommonUtil(this);
        xwzxDAL = new XwzxDAL(this);
        UserSession userSession = new UserSession(this);
        user = userSession.getUser();
        // flag 用于判断 是我的报名单位 1 or 报名岗位 2
        flag = this.getIntent().getStringExtra("flag") == null ? "" : this.getIntent().getStringExtra("flag");
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

        if ("2".equals(flag)) {
            headerstr.setText("我投递的职位");
        } else if ("1".equals(flag)) {
            headerstr.setText("我投递的职位");
        } else if ("3".equals(flag)) {
            headerstr.setText("我收藏的职位");
        }

        if ("2".equals(flag)) {   // 报名单位

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    try {
                        if (arg2 == mapList.size()) {
                            return;
                        }
                        Intent intent = new Intent(NewQztMyJobActivity.this, QztEnterpriseView.class);
                        intent.putExtra("title", "企业信息");
                        intent.putExtra("companyId", mapList.get(arg2).get("aab001").toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        commonUtil.shortToast("未响应!");
                    }
                }
            });
        } else if ("1".equals(flag)) { // 报名岗位

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    try {
                        if (arg2 == mapList.size()) {
                            return;
                        }
                        String positionId = mapList.get(arg2).get("acb200").toString();// 职位编号
                        Intent intent = new Intent(NewQztMyJobActivity.this, QztPositionView.class);
                        intent.putExtra("title", "职位详情");
                        intent.putExtra("positionId", positionId);
                        intent.putExtra("flag", "1");
                        intent.putExtra("companyNameStr", mapList.get(arg2).get("aab004").toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        commonUtil.shortToast("未响应!");
                    }
                }
            });
        } else if ("3".equals(flag)) { // 关注岗位

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    try {
                        if (arg2 == mapList.size()) {
                            return;
                        }
                        String positionId = mapList.get(arg2).get("acb200").toString();// 职位编号
                        Intent intent = new Intent(NewQztMyJobActivity.this, QztPositionView.class);
                        intent.putExtra("title", "职位详情");
                        intent.putExtra("positionId", positionId);
                        intent.putExtra("flag", "3");
                        intent.putExtra("companyNameStr", mapList.get(arg2).get("aab004").toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        commonUtil.shortToast("未响应!");
                    }
                }
            });
        }
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewQztMyJobActivity.this.finish();
            }
        });

        findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
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
                }, 2000);
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
                }, 2000);
            }
        });
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
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
        temp_data_count_page++;
        loadDate();
        yfbaseAdapter.notifyDataSetChanged();
    }

    //*************************************************************************

    private void formatData(List<Map<String, String>> STRINGLIST) {
        if (STRINGLIST != null && STRINGLIST.size() > 0) {
            if ("1".equals(flag)) {
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    tm.put("work", tm.get("aca112"));   // 职位名称
                    // 投递状态
                    String money = tm.get("hfzt") == null ? "" : tm.get("hfzt");
                    switch (money) {
                        case "3":
                            money = "未阅";
                            break;
                        case "4":
                            money = "已阅";
                            break;
                        case "5":
                            money = "接收";
                            break;
                        case "6":
                            money = "拒绝";
                            break;
                        case "7":
                            money = "退档";
                            break;
                        case "8":
                            money = "成交";
                            break;
                        case "11":
                            money = "系统退回";
                            break;
                        default:
                            money = "等待回复";
                            break;
                    }
                    tm.put("money", money);

                    // 公司地址
                    tm.put("company", tm.get("aab004") == null ? "" : tm.get("aab004"));

                    // 投递日期日期
                    tm.put("date", tm.get("tdrq") == null ? "" : tm.get("tdrq"));
                    for (String ts : tm.keySet()) {
                        otm.put(ts, tm.get(ts));
                    }
                    mapList.add(otm);
                }
            } else if ("2".equals(flag)) {
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    tm.put("work", tm.get("aca112"));   // 职位名称
                    // 投递状态
                    String money = tm.get("hfzt") == null ? "" : tm.get("hfzt");
                    switch (money) {
                        case "3":
                            money = "未阅";
                            break;
                        case "4":
                            money = "已阅";
                            break;
                        case "5":
                            money = "接收";
                            break;
                        case "6":
                            money = "拒绝";
                            break;
                        case "7":
                            money = "退档";
                            break;
                        case "8":
                            money = "成交";
                            break;
                        case "11":
                            money = "系统退回";
                            break;
                        default:
                            money = "等待回复";
                            break;

                    }
                    tm.put("money", money);
                    // 公司地址
                    tm.put("company", tm.get("acb202") == null ? "" : tm.get("acb202"));
                    // 投递日期日期
                    tm.put("date", tm.get("tdrq") == null ? "" : tm.get("tdrq"));
                    for (String ts : tm.keySet()) {
                        otm.put(ts, tm.get(ts));
                    }
                    mapList.add(otm);
                }
            } else if ("3".equals(flag)) {
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    tm.put("work", tm.get("aca112"));   // 职位名称
                    // 投递状态
                    String money = tm.get("acb241") == null ? "" : tm.get("acb241");
                    if ("".equals(money)) {
                        money = "0";
                    }
                    tm.put("money", "标准工资:" + money);
                    // 公司地址
                    tm.put("company", tm.get("aab004") == null ? "" : tm.get("aab004"));
                    // 投递日期日期
                    tm.put("date", tm.get("gzrq") == null ? "" : tm.get("gzrq"));
                    for (String ts : tm.keySet()) {
                        otm.put(ts, tm.get(ts));
                    }
                    mapList.add(otm);
                }
            }
        }

        if (STRINGLIST != null && STRINGLIST.size() < 10 && STRINGLIST.size() > 0) {
            isBotom = true;
            springView.setEnable(true);
            springView.setGive(SpringView.Give.TOP);
            springView.getFooterView().setVisibility(View.GONE);
        } else if (STRINGLIST != null && STRINGLIST.size() == 0) {
            isBotom = true;
            springView.setEnable(true);
            springView.setGive(SpringView.Give.TOP);
            springView.getFooterView().setVisibility(View.GONE);
            findViewById(R.id.islayout).setVisibility(View.GONE);
            findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            findViewById(R.id.kb).setVisibility(View.VISIBLE);
            commonUtil.shortToast("暂无数据");
        }
    }


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        NewQztMyJobActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        if ("1".equals(flag)) {
            xwzxDAL.queryBmZw(temp_data_count_page, user.getUserId(), callBack);
        } else if ("2".equals(flag)) {
            xwzxDAL.queryBmdw(user.getUserId(), callBack);
        } else if ("3".equals(flag)) {
            xwzxDAL.queryGzzw(temp_data_count_page, user.getUserId(), callBack);
        }
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                formatData(DataConvert.toConvertStringList(json, "table"));
                listview.setVisibility(View.VISIBLE);
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统繁忙!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (a != 0) {
            refreshData();
            a = 0;
        }
        a++;
    }
}

