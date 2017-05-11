package com.winksoft.android.yzjycy.ui.pxxx;

import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PxxxActivity extends BaseListActivity implements OnClickListener {
    private Button back, searchbut;
    private EditText code; // 搜索
    private String keyWord = ""; // 查询的关键字
    private Dialog proDialog;
    private YFBaseAdapter adapter;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pxxx_list);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        searchbut = (Button) findViewById(R.id.searchbut);
        searchbut.setOnClickListener(this);
        code = (EditText) findViewById(R.id.code); // 搜索
        back = (Button) findViewById(R.id.back);
        back.requestFocus();
        back.setOnClickListener(this);
        intiListview(false, true);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(PxxxActivity.this, PxDetailsActivity.class);        //跳转至详情界面
                    String class_id = SURPERDATA.get(arg2 - 1).get("class_id").toString();
                    String class_name = SURPERDATA.get(arg2 - 1).get("class_name").toString();
                    String training_agent_name = SURPERDATA.get(arg2 - 1).get("training_agent_name").toString();
                    String training_type = SURPERDATA.get(arg2 - 1).get("training_type").toString();
                    String training_job_type = SURPERDATA.get(arg2 - 1).get("training_job_type").toString();
                    String training_level = SURPERDATA.get(arg2 - 1).get("training_level").toString();
                    String opening_time = SURPERDATA.get(arg2 - 1).get("opening_time").toString();

                    intent.putExtra("class_id", class_id);
                    intent.putExtra("class_name", class_name);
                    intent.putExtra("training_agent_name", training_agent_name);
                    intent.putExtra("training_type", training_type);
                    intent.putExtra("training_job_type", training_job_type);
                    intent.putExtra("training_level", training_level);
                    intent.putExtra("opening_time", opening_time);
                    startActivity(intent);
                } catch (Exception e) {
                    commonUtil.shortToast("未响应!");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbut:    // 查询按钮
                keyWord = code.getText().toString().trim();
                SUPERPAGENUM = 0;
                loadDate();     // 加载数据
                break;
            case R.id.back:
                PxxxActivity.this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 加载数据
     */
    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        PxxxActivity.this, "加载中,请稍后...");
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
        xwzxDAL.queryPxxx(SUPERPAGENUM,keyWord, callBack);
    }

    /**
     * 处理结果
     *
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "attendance");
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
            }
        } else {
            onListviewNoResult();
        }
    }

    /**
     * 适配器
     *
     * @return
     */
    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.pxxx_list_item,
                new String[]{"class_name", "training_agent_name", "training_type", "training_job_type",
                        "training_level", "opening_time", "enrolled_cnt"},
                new int[]{R.id.bjmc, R.id.pxjg, R.id.pxlx, R.id.pxgz, R.id.pxdj, R.id.jzrq, R.id.rs}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {
                ImageView rs_imag = (ImageView) view.findViewById(R.id.rs_imag);
                TextView rs = (TextView) view.findViewById(R.id.rs);
                Map<String, ?> map = data.get(position);
                if ("0".equals(map.get("enrolled_cnt").toString().trim())) { /*隐藏报名小图标*/
                    rs.setVisibility(View.GONE);
                    rs_imag.setVisibility(View.GONE);
                } else {    /*显示报名小图标*/
                    rs.setVisibility(View.VISIBLE);
                    rs_imag.setVisibility(View.VISIBLE);
                }
            }
        };
        return adapter;
    }

    @Override
    protected void myNotifyDataSetChanged() {
        searchbut.setEnabled(true);
        adapter.notifyDataSetChanged();
    }
}
