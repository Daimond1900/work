package com.winksoft.android.yzjycy.ui.enterprise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztRecuitInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.data.QztEnterpriseDal;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztPositionView;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 该公司所有职位 列表
 */
public class QztPositionList extends BaseListActivity {
    private YFBaseAdapter adapter;
    private int pageNum = 0, lastItem = 0;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    private XwzxDAL xwzxDAL;
    private String companyId = "";
    private Button back_btn;
    private TextView companyName;
    private CommonUtil commonUtil;
    Dialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_position_list);

        commonUtil = new CommonUtil(this);

        xwzxDAL = new XwzxDAL(this);
        companyName = (TextView) findViewById(R.id.companyName);

        companyId = this.getIntent().getStringExtra("companyId") == null ? "" : this.getIntent().getStringExtra("companyId");
        companyName.setText(this.getIntent().getStringExtra("companyName") == null ? "" : this.getIntent().getStringExtra("companyName"));

        intiListview(true, true);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent bl = new Intent(QztPositionList.this, QztPositionView.class);
                    bl.putExtra("positionId", SURPERDATA.get(arg2-1).get("acb210").toString());
                    bl.putExtra("companyId", SURPERDATA.get(arg2-1).get("aab001").toString());
                    bl.putExtra("isClick", true);//进到职位详细信息 公司名称不可点
                    startActivity(bl);
                } catch (Exception e) {
                    e.printStackTrace();
                    commonUtil.shortToast("未响应!");
                }
            }
        });
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                QztPositionList.this.finish();
            }
        });
    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.qzt_position_list_item, new String[]{"positionDate", "positionMoney", "positionName", "positionAddress"},
                new int[]{R.id.positionDate, R.id.positionMoney, R.id.positionName, R.id.positionAddress}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
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
                        QztPositionList.this, "加载中,请稍后...");
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
        xwzxDAL.doQueryPosition(companyId, pageNum, callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "recruitments");
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

    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            //月工资
            String money = tm.get("acc034") == null ? "" : tm.get("acc034").toString();
            if (money.equals("")) {
                money = "面议";
            }
            //招聘总人数
            int peopleCount = Integer.parseInt(tm.get("acb211").toString()) + Integer.parseInt(tm.get("acb212").toString()) + Integer.parseInt(tm.get("acb213").toString());
            tm.put("positionMoney", "月薪:" + money);
            tm.put("positionName", tm.get("acb216") + "(" + peopleCount + ")");
            tm.put("positionDate", "发布:" + tm.get("aae030"));//起始日期
            tm.put("positionAddress", tm.get("aae006"));//地址
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }
}
