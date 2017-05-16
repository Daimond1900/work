package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

/**
 * Created by 1900 on 2017/5/12.
 */

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

import java.util.List;
import java.util.Map;

/**
 * 语言能力 列表
 * Created by 1900 on 2017/4/25.
 */
public class ModifyYynlInfo extends BaseActivity implements View.OnClickListener{
    private MyListView1 list;
    private XwzxDAL xwzxDAL;
    private Dialog proDialog;
    private List<Map<String, String>> listResultvalue;
    private ImageView wsj_img;
    private TextView wsj_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xgyynl);
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
                        ModifyYynlInfo.this, "加载中,请稍后...");
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
        xwzxDAL.queryYynl(user.getUserId(), callBack);
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
            SimpleAdapter adapter = new SimpleAdapter(this, listResult, R.layout.xgyynl_list_item,
                    new String[]{"aac038", "aac175"},
                    new int[]{R.id.yz, R.id.sp});
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
                // 语言编号
                String yzid = listResultvalue.get(position).get("yyid") != null && !"".equals(listResultvalue.get(position).get("yyid")) ? listResultvalue.get(position).get("yyid") : "";
                // 外语种类
                String wyzl = listResultvalue.get(position).get("wyzl") != null && !"".equals(listResultvalue.get(position).get("wyzl")) ? listResultvalue.get(position).get("wyzl") : "";
                // 语种代码
                String yzdm = listResultvalue.get(position).get("aac038") != null && !"".equals(listResultvalue.get(position).get("aac038")) ? listResultvalue.get(position).get("aac038") : "";
                // 外语水平
                String yysp = listResultvalue.get(position).get("aac175") != null && !"".equals(listResultvalue.get(position).get("aac175")) ? listResultvalue.get(position).get("aac175") : "";

                Intent intent = new Intent(ModifyYynlInfo.this, ModifyYynlInfoDetail.class);
                intent.putExtra("yzid",yzid);
                intent.putExtra("wyzl",wyzl);
                intent.putExtra("yzdm",yzdm);
                intent.putExtra("yysp",yysp);
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
                Intent intent = new Intent(this, ModifyYynlInfoDetail.class);
                intent.putExtra("flag",0); //新增
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
