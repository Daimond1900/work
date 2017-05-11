package com.winksoft.android.yzjycy.ui.hireQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.ui.bminfo.ZptPersonalResumeActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * ClassName:ManageListActivity
 * Description：录用查询
 *
 * @author Administrator
 *         Date：2012-10-23
 */
public class ZptEmployListActivity extends BaseListActivity {
    private static final String TAG = "ZptEmployList";
    private Button backBtn;
    private YFBaseAdapter adapter;
    private QyDAL qyDAL;
    private Intent qz;
    private Dialog proDialog;
    private CommonUtil commonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_employ_list);

        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
        qz = this.getIntent();

        MyClick myClick = new MyClick();
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(myClick);
        intiListview(false, true);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(ZptEmployListActivity.this, ZptPersonalResumeActivity.class);
                    intent.putExtra("c_yhm", SURPERDATA.get(arg2 - 1).get("itemName").toString());
                    intent.putExtra("c_id", SURPERDATA.get(arg2 - 1).get("sending_id").toString());
                    intent.putExtra("id", SURPERDATA.get(arg2 - 1).get("id").toString());
                    intent.putExtra("c_tag", "3");// 录用
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class MyClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    ZptEmployListActivity.this.finish();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_employ_list_item,
                new String[]{"itemName", "itemSex", "itemContact", "itemDate"},
                new int[]{R.id.item_nameTxt, R.id.item_sexTxt, R.id.item_contactTxt, R.id.item_dateTxt}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
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
                        ZptEmployListActivity.this, "加载中,请稍后...");
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
        qyDAL.doLyxxQuery(SUPERPAGENUM, "3", qz.getStringExtra("post_id"), callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
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
            Log.d(TAG, "formatData: STRINGLIST" + STRINGLIST);
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("itemName", tm.get("xm") != null && !"".equals(tm.get("xm")) ? tm.get("xm") : "");// 姓名
            tm.put("itemSex", tm.get("xb") != null && !"".equals(tm.get("xb")) ? tm.get("xb") : "");// 性别
            tm.put("itemContact", tm.get("lxdh") != null && !"".equals(tm.get("lxdh")) ? tm.get("lxdh") : "");// 联系电话
            String date = tm.get("send_time") != null && !"".equals(tm.get("send_time")) ? tm.get("send_time").toString() : "";
            Log.d(TAG, "formatData: date" + date);
            String date1 = date.substring(0, 10);
            tm.put("itemDate", date1); // 报名时间
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
}
