package com.winksoft.android.yzjycy.newyzjycy.wd.khfk;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Map;

/**
 * 客户反馈详情
 * Created by 1900 on 2017/5/3.
 */

public class KhfkInfoActivity extends BaseActivity {
    private TextView fkzt, fknr, cjsj, clzt, xm, sjhm, dzyj, hfsj, hfjl, hfr, fklx;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private String typeid = "";
    private View myscroll;
    private ImageView wsj_img;
    private TextView wsj_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khfk_info_detail);
        initView();
        loadData();
    }

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);

        myscroll = findViewById(R.id.myscroll);
        wsj_img = (ImageView) findViewById(R.id.wsj_img);
        wsj_tv = (TextView) findViewById(R.id.wsj_tv);

        typeid = getIntent().getStringExtra("id") != null && !"".equals(getIntent().getStringExtra("id")) ? getIntent().getStringExtra("id") : "";

        fkzt = (TextView) findViewById(R.id.fkzt);
        fknr = (TextView) findViewById(R.id.fknr);
        cjsj = (TextView) findViewById(R.id.cjsj);
        clzt = (TextView) findViewById(R.id.clzt);
        xm = (TextView) findViewById(R.id.xm);
        sjhm = (TextView) findViewById(R.id.sjhm);
        dzyj = (TextView) findViewById(R.id.dzyj);
        hfsj = (TextView) findViewById(R.id.hfsj);
        hfjl = (TextView) findViewById(R.id.hfjl);
        hfr = (TextView) findViewById(R.id.hfr);
        fklx = (TextView) findViewById(R.id.fklx);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KhfkInfoActivity.this.finish();
            }
        });
    }

    private void loadData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        KhfkInfoActivity.this, "加载中,请稍后...");
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
                myscroll.setVisibility(View.GONE);
                wsj_img.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_tv.setText("服务器繁忙，稍后再试！");
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.queryWdFkInfoDetail(typeid, callBack);
    }

    /**
     * @param json 结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                setPage(DataConvert.toConvertStringMap(json, "row"));
                myscroll.setVisibility(View.VISIBLE);
                wsj_img.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.GONE);
            } else {
                myscroll.setVisibility(View.GONE);
                wsj_img.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_tv.setText("暂无数据！");
            }
        } else {
            myscroll.setVisibility(View.GONE);
            wsj_img.setVisibility(View.VISIBLE);
            wsj_tv.setVisibility(View.VISIBLE);
            wsj_tv.setText("暂无数据！");
        }
    }

    private void setPage(Map<String, String> json) {
        if (json != null && json.size() > 0) {
            fkzt.setText(commonUtil.getMapValue(json,"header"));
            fknr.setText(commonUtil.getMapValue(json,"content"));
            cjsj.setText(commonUtil.getMapValue(json,"createtime"));
            clzt.setText(commonUtil.getMapValue(json,"status"));
            xm.setText(commonUtil.getMapValue(json,"reporter_name"));
            sjhm.setText(commonUtil.getMapValue(json,"tel"));
            dzyj.setText(commonUtil.getMapValue(json,"email"));
            hfsj.setText(commonUtil.getMapValue(json,"replaytime"));
            hfjl.setText(commonUtil.getMapValue(json,"replayrmk"));
            hfr.setText(commonUtil.getMapValue(json,"replayuser"));
            fklx.setText(commonUtil.getMapValue(json,"type"));
        }
    }
}
