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
 * 工作经历 列表
 * Created by 1900 on 2017/4/25.
 */
public class ModifyGzjlInfo extends BaseActivity implements View.OnClickListener {

    private MyListView1 list;
    private XwzxDAL xwzxDAL;
    private Dialog proDialog;
    private List<Map<String, String>> listResultvalue = new ArrayList<>();
    private ImageView wsj_img;
    private TextView wsj_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xggzjl);
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
                        ModifyGzjlInfo.this, "加载中,请稍后...");
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
        xwzxDAL.queryGzjl(user.getUserId(), callBack);
    }

    private void loadDataResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("true")) {
                List<Map<String, String>> table = DataConvert.toConvertStringList(json, "table");
                if(table != null && table.size()>0){
                    setPage(table);
                    list.setVisibility(View.VISIBLE);
                    wsj_img.setVisibility(View.GONE);
                    wsj_tv.setVisibility(View.GONE);
                }else{
                    wsj_tv.setText("暂无记录，赶紧完善它吧！");
                    wsj_img.setVisibility(View.VISIBLE);
                    wsj_tv.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }
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
            SimpleAdapter adapter = new SimpleAdapter(this, listResult, R.layout.xggzjl_list_item,
                    new String[]{"qsrq", "jsrq", "aab004", "aca111", "gzbz"},
                    new int[]{R.id.qssj, R.id.jssj, R.id.dwmc, R.id.szzw, R.id.gzbz});
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
                // 工作编号
                String gzid = listResultvalue.get(position).get("gzid") != null && !"".equals(listResultvalue.get(position).get("gzid")) ? listResultvalue.get(position).get("gzid") : "";
                // 起始日期
                String qsrq = listResultvalue.get(position).get("qsrq") != null && !"".equals(listResultvalue.get(position).get("qsrq")) ? listResultvalue.get(position).get("qsrq") : "";
                // 结束日期
                String jsrq = listResultvalue.get(position).get("jsrq") != null && !"".equals(listResultvalue.get(position).get("jsrq")) ? listResultvalue.get(position).get("jsrq") : "";
                // 单位名称
                String dwmc = listResultvalue.get(position).get("aab004") != null && !"".equals(listResultvalue.get(position).get("aab004")) ? listResultvalue.get(position).get("aab004") : "";
                // 所在职位
                String szzw = listResultvalue.get(position).get("aca111") != null && !"".equals(listResultvalue.get(position).get("aca111")) ? listResultvalue.get(position).get("aca111") : "";
                // 工作备注
                String gzbz = listResultvalue.get(position).get("gzbz") != null && !"".equals(listResultvalue.get(position).get("gzbz")) ? listResultvalue.get(position).get("gzbz") : "";

                Intent intent = new Intent(ModifyGzjlInfo.this, ModifyGzjlInfoDetail.class);
                intent.putExtra("gzid", gzid);
                intent.putExtra("qsrq", qsrq);
                intent.putExtra("jsrq", jsrq);
                intent.putExtra("dwmc", dwmc);
                intent.putExtra("szzw", szzw);
                intent.putExtra("gzbz", gzbz);
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
                Intent intent = new Intent(this, ModifyGzjlInfoDetail.class);
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
