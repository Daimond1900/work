package com.winksoft.android.yzjycy.newyzjycy.pxkq.me;

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
import com.winksoft.android.yzjycy.newyzjycy.wd.khfk.KhfkActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤记录 列表
 * Created by 1900 on 2017/5/4.
 */

public class KqjlInfoActivity extends BaseActivity implements View.OnClickListener {
    private MyListView1 listview;
    private List<Map<String, Object>> mapList = new ArrayList<>();
    private YFBaseAdapter yfbaseAdapter;
    private int temp_data_count_page = 0;//临时存放当前加载页对应的pos
    XwzxDAL xwzxDAL;
    Dialog proDialog;
    private SpringView springView;
    private boolean isBotom = false;
    private String class_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kqjl_list);
        xwzxDAL = new XwzxDAL(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        class_id = getIntent().getStringExtra("class_id") != null && !"".equals(getIntent().getStringExtra("class_id")) ? getIntent().getStringExtra("class_id") : "";

        loadDate();
        listview = (MyListView1) findViewById(R.id.id_lv);
        yfbaseAdapter = new YFBaseAdapter(this, mapList, R.layout.kqxq_list_item,
                new String[]{"check_time", "course_name"},
                new int[]{R.id.time, R.id.tv_kcmc}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
                Map<String, ?> map = data.get(position);
                if ("1".equals(map.get("check_type") == null ? "" : map.get("check_type"))) {    //签到
                    TextView ztinfo = (TextView) view.findViewById(R.id.ztinfo);
                    ztinfo.setText("签到");
                } else if ("2".equals(map.get("check_type") == null ? "" : map.get("check_type"))) { //签退
                    TextView ztinfo = (TextView) view.findViewById(R.id.ztinfo);
                    ztinfo.setText("签退");
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

                String pic_url = mapList.get(arg2) != null ? mapList.get(arg2).get("pic_url") != null ? mapList.get(arg2).get("pic_url").toString() : "" : "";
                String chk_lng = mapList.get(arg2) != null ? mapList.get(arg2).get("chk_lng") != null ? mapList.get(arg2).get("chk_lng").toString() : "" : "";
                String chk_lat = mapList.get(arg2) != null ? mapList.get(arg2).get("chk_lat") != null ? mapList.get(arg2).get("chk_lat").toString() : "" : "";

                Intent intent = new Intent(KqjlInfoActivity.this, KqjlQuery.class);
                intent.putExtra("pic_url", pic_url);
                intent.putExtra("chk_lng", chk_lng);
                intent.putExtra("chk_lat", chk_lat);
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
                        KqjlInfoActivity.this, "加载中,请稍后...");
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
                isBotom = true;
                springView.setEnable(true);
                springView.setGive(SpringView.Give.TOP);
                springView.getFooterView().setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.doKqRecordQuery(class_id, temp_data_count_page, callBack);
    }

    /**
     * @param json 结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                springView.setGive(SpringView.Give.BOTH);
                formatData(DataConvert.toConvertStringList(json, "checkOutInInfo"));
                listview.setVisibility(View.VISIBLE);
                yfbaseAdapter.notifyDataSetChanged();
            } else {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
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
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
            if (STRINGLIST.size() > 0) {
                findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                for (Map<String, String> tm : STRINGLIST) {
                    Map<String, Object> otm = new HashMap<>();
                    for (String ts : tm.keySet()) {
                        otm.put(ts, tm.get(ts));
                    }
                    mapList.add(otm);
                }
            }
        } else {
            findViewById(R.id.islayout).setVisibility(View.GONE);
            findViewById(R.id.kb).setVisibility(View.VISIBLE);
            findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
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

}
