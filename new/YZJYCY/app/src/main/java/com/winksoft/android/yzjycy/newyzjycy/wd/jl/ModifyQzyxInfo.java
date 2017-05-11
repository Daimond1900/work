package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 求职意向 列表
 * Created by 1900 on 2017/4/25.
 */
public class ModifyQzyxInfo extends BaseActivity implements View.OnClickListener {
    private MyListView1 list;
    private XwzxDAL xwzxDAL;
    private Dialog proDialog;
    private List<Map<String, String>> listResultvalue = new ArrayList<>();
    private ImageView wsj_img;
    private TextView wsj_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xgqzyx);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyQzyxInfo.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                loadDataResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.queryQzyx(user.getUserId(), callBack);
    }

    private void loadDataResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("true")) {
                list.setVisibility(View.VISIBLE);
                setPage(DataConvert.toConvertStringList(json, "table"));
                wsj_img.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.GONE);
            } else {
                wsj_tv.setText("暂无记录，赶紧完善它吧！");
                wsj_img.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }
        } else {
            wsj_tv.setText("服务器繁忙，请稍后再试！");
            wsj_img.setVisibility(View.VISIBLE);
            wsj_tv.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
    }

    private void setPage(List<Map<String, String>> listResult) {
        if (list != null && listResult.size() > 0) {
            listResultvalue = listResult;
            SimpleAdapter adapter = new SimpleAdapter(this, listResult, R.layout.xgqzyx_list_item,
                    new String[]{"acb242", "aca111", "aab301", "aab019", "acc201", "aab020", "acb22a"},
                    new int[]{R.id.yxyq, R.id.gwmc, R.id.gzdd, R.id.dwlx, R.id.gzxz, R.id.jjlx, R.id.gwms});
            list.setAdapter(adapter);
        }
    }


    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        wsj_img = (ImageView) findViewById(R.id.wsj_img);
        wsj_tv = (TextView) findViewById(R.id.wsj_tv);
        list = (MyListView1) findViewById(R.id.id_lv);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 求职编号
                String qzid = listResultvalue.get(position).get("qzid") != null && !"".equals(listResultvalue.get(position).get("qzid")) ? listResultvalue.get(position).get("qzid") : "";
                // 单位类型
                String dwlx = listResultvalue.get(position).get("aab019") != null && !"".equals(listResultvalue.get(position).get("aab019")) ? listResultvalue.get(position).get("aab019") : "";
                // 经济类型
                String jjlx = listResultvalue.get(position).get("aab020") != null && !"".equals(listResultvalue.get(position).get("aab020")) ? listResultvalue.get(position).get("aab020") : "";
                // 工作地点
                String gzdd = listResultvalue.get(position).get("aab301") != null && !"".equals(listResultvalue.get(position).get("aab301")) ? listResultvalue.get(position).get("aab301") : "";
                // 工作性质
                String gzxz = listResultvalue.get(position).get("acc201") != null && !"".equals(listResultvalue.get(position).get("acc201")) ? listResultvalue.get(position).get("acc201") : "";
                // 岗位名称
                String gwmc = listResultvalue.get(position).get("aca111") != null && !"".equals(listResultvalue.get(position).get("aca111")) ? listResultvalue.get(position).get("aca111") : "";
                //岗位描述
                String gwms = listResultvalue.get(position).get("acb22a") != null && !"".equals(listResultvalue.get(position).get("acb22a")) ? listResultvalue.get(position).get("acb22a") : "";
                //月薪要求
                String yxyq = listResultvalue.get(position).get("acb242") != null && !"".equals(listResultvalue.get(position).get("acb242")) ? listResultvalue.get(position).get("acb242") : "";

                Intent intent = new Intent(ModifyQzyxInfo.this, ModifyQzyxInfoDetail.class);
                intent.putExtra("qzid", qzid);
                intent.putExtra("dwlx", dwlx);
                intent.putExtra("jjlx", jjlx);
                intent.putExtra("gzdd", gzdd);
                intent.putExtra("gzxz", gzxz);
                intent.putExtra("gwmc", gwmc);
                intent.putExtra("gwms", gwms);
                intent.putExtra("yxyq", yxyq);
                startActivity(intent);
            }
        });

        Button xz = (Button) findViewById(R.id.xz);
        Button back = (Button) findViewById(R.id.back);
        xz.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xz:
                //新增
                Intent intent = new Intent(this, ModifyQzyxInfoDetail.class);
                intent.putExtra("flag", 0); //新增
                startActivity(intent);
                break;
            case R.id.back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
