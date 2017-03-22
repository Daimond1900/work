package com.winksoft.android.yzjycy.ui.zcfg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * comments:政策法规
 */
public class QztZCFGListActivity extends BaseListActivity implements
        OnClickListener {
    private TextView titleTxt;
    private Button backBtn;
    Dialog proDialog;
    private YFBaseAdapter adapter;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private static final String TAG = "QztZCFGListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_zcfg_list);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        titleTxt = (TextView) findViewById(R.id.titleTxt);
        titleTxt.setText("政策法规");
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
        intiListview(true, true);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(QztZCFGListActivity.this,
                            QztZCFGDeatilActivity.class);
                    String news_id = SURPERDATA.get(arg2 - 1).get("news_id")
                            .toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "政策法规详情");
                    bundle.putString("url", Constants.IP
                            + "android/news/queryNewsDetail?news_id=" + news_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("未响应!", false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                QztZCFGListActivity.this.finish();
                break;
            case R.id.searchBtn:
                SUPERPAGENUM = 0;
                intiListview(true, true);
                break;
            default:
                break;
        }
    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.qzt_zcfg_list_item,
                new String[]{"t_title", "t_from", "t_date"},
                new int[]{R.id.training_title, R.id.training_from,
                        R.id.training_date}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

            }
        };
        return adapter;
    }

    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("t_title", tm.get("title"));
            tm.put("news_id", tm.get("news_id"));
            tm.put("t_from", "发布单位:" + tm.get("create_by") == null ? ""
                    : "发布单位:" + tm.get("create_by"));
            tm.put("t_date", tm.get("create_time"));
            tm.put("readCount", tm.get("hits"));

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

    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();

                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztZCFGListActivity.this, "加载中,请稍后...");
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
        xwzxDAL.queryZcfg(SUPERPAGENUM, callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                showToast(commonUtil.getMapValue(map, "msg"), false);
                STRINGLIST = DataConvert.toConvertStringList(json, "news");
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
                commonUtil.shortToast("查询失败!");
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("查询失败!");
        }
    }

}
