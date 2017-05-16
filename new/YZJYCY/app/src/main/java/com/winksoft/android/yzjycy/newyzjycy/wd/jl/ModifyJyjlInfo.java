package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
 * 教育经历 列表
 * Created by 1900 on 2017/4/25.
 */
public class ModifyJyjlInfo extends BaseActivity implements View.OnClickListener {
    private MyListView1 list;
    private XwzxDAL xwzxDAL;
    private Dialog proDialog;
    private List<Map<String, String>> listResultvalue = new ArrayList<>();
    private ImageView wsj_img;
    private TextView wsj_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xgjyjl);
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
                        ModifyJyjlInfo.this, "加载中,请稍后...");
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
        xwzxDAL.queryJyjl(user.getUserId(), callBack);
    }

    private void loadDataResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        Log.d("tttt", "loadDataResult: mapmapmap = "+map);
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
            SimpleAdapter adapter = new SimpleAdapter(this, listResultvalue, R.layout.xgjyjl_list_item,
                    new String[]{"qsrq", "jsrq", "byyx", "aac183", "aac014", "aac015", "xxbz"},
                    new int[]{R.id.qssj, R.id.jssj, R.id.byxx, R.id.sxzy, R.id.hdzc, R.id.zcdj, R.id.xxbz});
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
                // 教育编号
                String jyid = listResultvalue.get(position).get("jyid") != null && !"".equals(listResultvalue.get(position).get("jyid")) ? listResultvalue.get(position).get("jyid") : "";
                // 起始日期
                String qsrq = listResultvalue.get(position).get("qsrq") != null && !"".equals(listResultvalue.get(position).get("qsrq")) ? listResultvalue.get(position).get("qsrq") : "";
                // 结束日期
                String jsrq = listResultvalue.get(position).get("jsrq") != null && !"".equals(listResultvalue.get(position).get("jsrq")) ? listResultvalue.get(position).get("jsrq") : "";
                // 毕业院校
                String byyx = listResultvalue.get(position).get("byyx") != null && !"".equals(listResultvalue.get(position).get("byyx")) ? listResultvalue.get(position).get("byyx") : "";
                // 所学专业
                String sxzy = listResultvalue.get(position).get("aac183") != null && !"".equals(listResultvalue.get(position).get("aac183")) ? listResultvalue.get(position).get("aac183") : "";
                // 学习备注
                String xxbz = listResultvalue.get(position).get("xxbz") != null && !"".equals(listResultvalue.get(position).get("xxbz")) ? listResultvalue.get(position).get("xxbz") : "";
                // 获得职称
                String hdzc = listResultvalue.get(position).get("aac014") != null && !"".equals(listResultvalue.get(position).get("aac014")) ? listResultvalue.get(position).get("aac014") : "";
                // 职称等级
                String zcdj = listResultvalue.get(position).get("aac015") != null && !"".equals(listResultvalue.get(position).get("aac015")) ? listResultvalue.get(position).get("aac015") : "";

                Intent intent = new Intent(ModifyJyjlInfo.this, ModifyJyjlInfoDetail.class);
                intent.putExtra("jyid", jyid);
                intent.putExtra("qsrq", qsrq);
                intent.putExtra("jsrq", jsrq);
                intent.putExtra("byyx", byyx);
                intent.putExtra("sxzy", sxzy);
                intent.putExtra("xxbz", xxbz);
                intent.putExtra("hdzc", hdzc);
                intent.putExtra("zcdj", zcdj);
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
                Intent intent = new Intent(ModifyJyjlInfo.this, ModifyJyjlInfoDetail.class);
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
