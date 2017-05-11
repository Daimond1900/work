package com.winksoft.android.yzjycy.newyzjycy.wd.khfk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
 * 反馈列表
 * Created by 1900 on 2017/5/3.
 */

public class WdfkListActivity extends BaseActivity implements View.OnClickListener {
    private MyListView1 listview;
    private List<Map<String, Object>> mapList = new ArrayList<>();
    private YFBaseAdapter yfbaseAdapter;
    private int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    Dialog proDialog;
    private SpringView springView;
    private boolean isBotom = false;
    private ImageView wsj_img;
    private TextView wsj_tv;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wdfk_list);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        wsj_img = (ImageView) findViewById(R.id.wsj_img);
        wsj_tv = (TextView) findViewById(R.id.wsj_tv);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button xz = (Button) findViewById(R.id.xz);
        xz.setOnClickListener(this);

        loadDate();
        listview = (MyListView1) findViewById(R.id.id_lv);
        yfbaseAdapter = new YFBaseAdapter(this, mapList, R.layout.wdfk_list_item,
                new String[]{"header", "createtime", "type", "status"},
                new int[]{R.id.fkbt, R.id.cjsj, R.id.fklx, R.id.clzt}, getResources()) {
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
                String fkid = mapList.get(arg2) != null ? mapList.get(arg2).get("id") != null ? mapList.get(arg2).get("id").toString() : "" : ""; //反馈id

                Intent intent = new Intent(WdfkListActivity.this, KhfkInfoActivity.class); // 我得反馈详情
                intent.putExtra("id", fkid);
                startActivity(intent);
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
    }


    //模拟加载数据
    private void loadMore() {
        temp_data_count_page++;
        loadDate();
        yfbaseAdapter.notifyDataSetChanged();
    }


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        WdfkListActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                listview.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_tv.setText("服务器繁忙，请稍后再试！");
                wsj_img.setVisibility(View.VISIBLE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.queryWdFkInfo(user.getUserId(), temp_data_count_page, callBack);
    }

    /**
     * @param json 结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                formatData(DataConvert.toConvertStringList(json, "table"));
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                listview.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_img.setVisibility(View.VISIBLE);
            }
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
            if (STRINGLIST.size() == 0) {
                listview.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_img.setVisibility(View.VISIBLE);
            }
            if (STRINGLIST.size() > 0) {
                listview.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.GONE);
                wsj_img.setVisibility(View.GONE);
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
            wsj_tv.setVisibility(View.VISIBLE);
            wsj_img.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.xz:
                startActivity(new Intent(this, KhfkActivity.class));
                break;
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
