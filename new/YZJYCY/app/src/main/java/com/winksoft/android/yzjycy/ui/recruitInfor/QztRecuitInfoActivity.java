package com.winksoft.android.yzjycy.ui.recruitInfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.QztPositionView;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

/***
 * 招聘信息
 *
 * @author wujiahong 2012-10-19
 */
public class QztRecuitInfoActivity extends BaseListActivity {
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private YFBaseAdapter adapter;
    private Button back_btn, refresh_btn, search_btn, zpxi;
    private EditText edtTxt_name, edtTxt_remark;
    private String c_name = "", area_id = "", c_remark = "";
    private boolean isLoading = true;// 标志正在加载数据
    private Spinner spn_area;
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog progressDialog;
    private List<Map<String, String>> areas;
    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_recruitment_information);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        edtTxt_name = (EditText) findViewById(R.id.edtTxt_name);
        edtTxt_remark = (EditText) findViewById(R.id.edtTxt_remark);
        MyOnclick onclick = new MyOnclick();
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(onclick);
        search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(onclick);
        spn_area = (Spinner) findViewById(R.id.spn_area); // 工作区域
        intiListview(true, true);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                try {
                    String positionId = SURPERDATA.get(arg2 - 1).get("acb210").toString();// 职位编号
                    Intent intent = new Intent(QztRecuitInfoActivity.this, QztPositionView.class);
                    intent.putExtra("title", "职位详情");
                    intent.putExtra("positionId", positionId);
                    intent.putExtra("companyId", SURPERDATA.get(arg2 - 1).get("aab001").toString());
                    startActivity(intent);
                } catch (Exception e) {
                    commonUtil.shortToast("未响应!");
                }
            }
        });

//		 加载地区
        onPostAreaData();
    }

    private void onPostAreaData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                onListviewStart();
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
                onListviewonFailure();
            }
        };
        xwzxDAL.doUnLoginAreas(callBack);
    }

    private void areaResult(String json) {
        List<Map<String, String>> maps;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "");
        map.put("name", "全部");
        list.add(map);
        Map<String, String> m = DataConvert.toMap(json);
        if (m != null) {
            if (m.get("success") != null && m.get("success").equals("true")) {
                maps = DataConvert.toConvertStringList(json, "cities");
                for (int i = 0; i < maps.size(); i++) {
                    Map<String, String> m1 = maps.get(i);
                    list.add(m1);
                }
            }
        }
        areas = list;
        if (areas != null && areas.size() > 0) {
            initArea();
        }
    }

    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("work", tm.get("acb216"));
            // 月工资
            String money = tm.get("acc034") == null ? "" : tm.get("acc034").toString();
            if (money.equals("")) {
                money = "面议";
            }
            tm.put("money", "月薪:" + money);

            // 公司名称
            tm.put("company", tm.get("aab004") == null ? "" : tm.get("aab004"));

            // 起始日期
            tm.put("date", tm.get("aae030") == null ? "" : tm.get("aae030"));
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.qzt_recruitment_list_item,
                new String[]{"work", "money", "company", "date"},
                new int[]{R.id.workTxt, R.id.moneyTxt, R.id.companyTxt, R.id.dateTxt}, getResources()) {
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
                        QztRecuitInfoActivity.this, "加载中,请稍后...");
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
        xwzxDAL.queryZpxx(SUPERPAGENUM,"","", "", "", "","",callBack);
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

    private void showDialog(String msg) {
        progressDialog = ProgressDialog.show(this, null, msg, true);
        progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
        progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
    }

    private void initArea() {
        SimpleAdapter adapter = new SimpleAdapter(this, areas, android.R.layout.simple_spinner_item,
                new String[]{"name"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_area.setAdapter(adapter);
        spn_area.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                area_id = areas.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    @Override
    protected void myNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    private class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    QztRecuitInfoActivity.this.finish();
                    break;
                case R.id.search_btn:
                    c_name = commonUtil.doConvertEmpty(edtTxt_name.getText().toString());
                    c_remark = commonUtil.doConvertEmpty(edtTxt_remark.getText().toString());
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                default:
                    break;
            }
        }
    }
}
