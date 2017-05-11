package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;
import com.winksoft.android.yzjycy.data.ManpowerDetailDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.ReJson;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 就业状况
 *
 * @author user_ygl
 */
public class JyzkDetail extends BaseActivity {
    private TextView sfz, name, jbqc, jyzt, mqjycy, cyhy, jyxs, sffxcyry, jydqlx, jydq, jyfs, wgnx, jnwgsj, wgygz,
            sfqdldht, sfjgbm, swzyryjyyx;
    private String intentCode, intentName;
    private ManpowerDetailDAL dal;
    private Button save, back;
    private String finalString;
    private boolean cansave = true;
    private Dialog proDialog;
    private CommonUtil commonUtil;
    private JcDAL jcDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcy_ldl_jyzk_detail);
        commonUtil = new CommonUtil(this);
        jcDAL = new JcDAL(this);
        intentCode = getIntent().getStringExtra("code");
        intentName = getIntent().getStringExtra("name");
        dal = new ManpowerDetailDAL(this, new Handler());
        back = (Button) findViewById(R.id.back);
        back.requestFocus();
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JyzkDetail.this.finish();
            }
        });
//,,,,,,,,,,,,,,,
        sfz = (TextView) findViewById(R.id.sfz);
        name = (TextView) findViewById(R.id.name);
        jbqc = (TextView) findViewById(R.id.jbqc);
        jyzt = (TextView) findViewById(R.id.jyzt);
        mqjycy = (TextView) findViewById(R.id.mqjycy);
        cyhy = (TextView) findViewById(R.id.cyhy);
        jyxs = (TextView) findViewById(R.id.jyxs);
        sffxcyry = (TextView) findViewById(R.id.sffxcyry);
        jydqlx = (TextView) findViewById(R.id.jydqlx);
        jydq = (TextView) findViewById(R.id.jydq);
        jyfs = (TextView) findViewById(R.id.jyfs);
        wgnx = (TextView) findViewById(R.id.wgnx);
        jnwgsj = (TextView) findViewById(R.id.jnwgsj);
        wgygz = (TextView) findViewById(R.id.wgygz);
        sfqdldht = (TextView) findViewById(R.id.sfqdldht);
        sfjgbm = (TextView) findViewById(R.id.sfjgbm);
        swzyryjyyx = (TextView) findViewById(R.id.swzyryjyyx);
        sfz.setFocusable(false);
        sfz.setBackgroundColor(0x00000000);
        name.setFocusable(false);
        name.setBackgroundColor(0x00000000);

        if (intentCode != null && !intentCode.equals(""))
            doPost();

    }

    private void doPost() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        JyzkDetail.this, "加载中,请稍后...");
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
            }
        };
        jcDAL.doJyztQuery(intentCode, callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                setData(DataConvert.toConvertStringMap(json, "detail"));
                return;
            } else {
                commonUtil.shortToast("没有记录");
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }

    private void setData(Map<String,String> resultMap) {
        if (resultMap != null) {
            jbqc.setText(ParseData.NCJBQK.get(commonUtil.getMapValue(resultMap, "acc100")));
            jyzt.setText(ParseData.JYZT1.get(commonUtil.getMapValue(resultMap, "acc926")));
            mqjycy.setText(ParseData.MQJYCY.get(commonUtil.getMapValue(resultMap, "aab054")));
            cyhy.setText(ParseData.CYHY.get(commonUtil.getMapValue(resultMap, "aab022")));
            jyxs.setText(ParseData.JYXS.get(commonUtil.getMapValue(resultMap, "acc90b")));
            sffxcyry.setText(ParseData.TYSF.get(commonUtil.getMapValue(resultMap, "acc90h")));
            jydqlx.setText(ParseData.JYDQXS.get(commonUtil.getMapValue(resultMap, "acc901")));
            jydq.setText(ParseData.JYDQ.get(commonUtil.getMapValue(resultMap, "acc902")));
            jyfs.setText(ParseData.JYFS.get(commonUtil.getMapValue(resultMap, "acc423")));
            wgnx.setText(commonUtil.getMapValue(resultMap, "acc903"));
            jnwgsj.setText(commonUtil.getMapValue(resultMap, "acb214"));
            wgygz.setText(commonUtil.getMapValue(resultMap, "acc034"));
            sfqdldht.setText(ParseData.TYSF.get(commonUtil.getMapValue(resultMap, "acc22b")));
            sfjgbm.setText(ParseData.TYSF.get(commonUtil.getMapValue(resultMap, "acc906")));
            swzyryjyyx.setText(ParseData.SWZYRYJYYX.get(commonUtil.getMapValue(resultMap, "acc905")));
        }
    }
}
