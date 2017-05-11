package com.winksoft.android.yzjycy.ui.bminfo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * ClassName:RegisterPostListActivity Description：报名信息-岗位的报名情况
 *
 * @author Administrator Date：2012-10-22
 */
public class Zpt_RegisterPostListActivity extends BaseListActivity {
    private static final String TAG = "Zpt_RegisterPostList";
    private Button backBtn, qbBtn, ytzBtn, yjjBtn;
    private String flag = "";// Tab标志位：0.全部；1.已拒绝；4.已通知
    private YFBaseAdapter adapter;
    private QyDAL qyDAL;
    private Intent qz;
    private Dialog proDialog;
    private CommonUtil commonUtil;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_register_post_list);

        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
        qz = this.getIntent();

        MyClick myClick = new MyClick();
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(myClick);
        qbBtn = (Button) findViewById(R.id.qbBtn);
        qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
        qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
        qbBtn.setOnClickListener(myClick);
        ytzBtn = (Button) findViewById(R.id.ytzBtn);
        ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
        ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
        ytzBtn.setOnClickListener(myClick);
        yjjBtn = (Button) findViewById(R.id.yjjBtn);
        yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
        yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
        yjjBtn.setOnClickListener(myClick);

        intiListview(true, true);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(Zpt_RegisterPostListActivity.this, ZptPersonalResumeActivity.class);
                    intent.putExtra("c_yhm", SURPERDATA.get(arg2 - 1).get("itemName").toString());
                    intent.putExtra("id", SURPERDATA.get(arg2 - 1).get("id").toString());
                    intent.putExtra("c_id", SURPERDATA.get(arg2 - 1).get("sending_id").toString());
                    intent.putExtra("c_tag", SURPERDATA.get(arg2 - 1).get("sta").toString());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        a += 1;
        if(a == 1){
            return;
        }
        SUPERPAGENUM = 0;
        loadDate();
        a = 1;

    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_register_post_list_item1,
                new String[]{"itemName", "itemSex", "itemTag", "itemContact", "itemDate"},
                new int[]{R.id.item_nameTxt, R.id.item_sexTxt, R.id.item_tagTxt, R.id.item_contactTxt,
                        R.id.item_dateTxt},
                getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
                if (flag.equals("")) {
                    String tag = data.get(position).get("sta").toString().trim();
                    TextView itemTag = (TextView) view.findViewById(R.id.item_tagTxt);
                    if ("0".equals(tag)) {
                        itemTag.setText(Html.fromHtml("<font color = red>未处理</font>"));
                    } else if ("1".equals(tag)) {
                        itemTag.setText(Html.fromHtml("<font color = #4DA0D4>已拒绝</font>"));
                    } else if ("2".equals(tag)) {
                        itemTag.setText(Html.fromHtml("<font color = #4DA0D4>待面试</font>"));
                    } else {
                        itemTag.setText(Html.fromHtml("<font color = #4DA0D4>已录用</font>"));
                    }
                }
            }
        };

        return adapter;
    }

    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        Zpt_RegisterPostListActivity.this, "加载中,请稍后...");
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
                if (proDialog != null)
                    proDialog.dismiss();
                onListviewonFailure();
            }
        };
        qyDAL.doGwBmxxQuery(SUPERPAGENUM, flag, qz.getStringExtra("post_id") + "", callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "formatData: " + map);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "sendings");
                onListviewSuccess();
                return;
            } else {
                commonUtil.shortToast("没有记录");
                onListviewNoResult();
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }

    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {

            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("itemName", tm.get("xm") == null ? "" : tm.get("xm"));// 姓名
            tm.put("itemSex", tm.get("xb") == null ? "" : tm.get("xb"));// 性别
            tm.put("itemContact", tm.get("lxdh") == null ? "" : tm.get("lxdh"));// 联系电话
            String date = tm.get("send_time").toString();
            String date1 = date.substring(0, 16);
            tm.put("itemDate", date1);// 日期
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }

    @Override
    protected void myNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    private class MyClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    Zpt_RegisterPostListActivity.this.finish();
                    break;
                case R.id.qbBtn:
                    flag = "";
                    qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
                    qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
                    ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                case R.id.ytzBtn:
                    flag = "2";
                    qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
                    ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
                    yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                case R.id.yjjBtn:
                    flag = "1";
                    qbBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    qbBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    ytzBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
                    ytzBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font_));
                    yjjBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
                    yjjBtn.setTextColor(getResources().getColor(R.color.register_tab_button_font));
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                default:
                    break;
            }
        }
    }
}
