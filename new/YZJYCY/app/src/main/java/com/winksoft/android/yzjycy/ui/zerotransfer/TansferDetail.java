package com.winksoft.android.yzjycy.ui.zerotransfer;

import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.data.ZeroTransferDAL;
import com.winksoft.android.yzjycy.ui.ldlxx.LdlDetail;
import com.winksoft.android.yzjycy.ui.ldlxx.ParseData;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;
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
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author user_ygl
 */
public class TansferDetail extends BaseActivity {

    private EditText sfz, jbrq, jbr, jbjg, zt;
    private TextView hzxm, ldls, lxdh;
    private TextView bz;
    private Button back;
    private Button save;
    private String finalString;
    private ZeroTransferDAL dal;
    private String intentCode;
    private String id;
    private boolean ifmodify;
    private String sfzStr,hzxmStr,  ldlsStr, lxdhStr, bzStr;
    private JcDAL jcDAL;
    private Dialog proDialog;
    private CommonUtil commonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcy_lzy_main_edit);
        commonUtil = new CommonUtil(this);
        jcDAL = new JcDAL(this);
        intentCode = getIntent().getStringExtra("code");
        id = getIntent().getStringExtra("id");
        ifmodify = getIntent().getBooleanExtra("ifmodify", false);
        initView();
        if (intentCode != null && !intentCode.equals(""))
            doPost();
    }

    private void initView() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TansferDetail.this.finish();

            }
        });
        sfz = (EditText) findViewById(R.id.sfz);
        hzxm = (TextView) findViewById(R.id.hzxm);
        ldls = (TextView) findViewById(R.id.ldls);
        lxdh = (TextView) findViewById(R.id.lxdh);
        bz = (TextView) findViewById(R.id.bz);
        jbrq = (EditText) findViewById(R.id.jbrq);
        jbr = (EditText) findViewById(R.id.jbr);
        jbjg = (EditText) findViewById(R.id.jbjg);
        zt = (EditText) findViewById(R.id.zt);
    }

    private void doPost() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        TansferDetail.this, "加载中,请稍后...");
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
        jcDAL.doLzyDetailQuery(intentCode, callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                setData(DataConvert.toConvertStringMap(json, "family"));
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
            sfz.setText(intentCode);
            hzxm.setText(commonUtil.getMapValue(resultMap, "hzxm"));
            ldls.setText(commonUtil.getMapValue(resultMap, "ldls"));
            lxdh.setText(commonUtil.getMapValue(resultMap, "lxdh"));
            bz.setText(commonUtil.getMapValue(resultMap, "bz"));
            jbrq.setText(commonUtil.getMapValue(resultMap, "aae036"));
            jbr.setText(commonUtil.getMapValue(resultMap, "aae019"));
            jbjg.setText(commonUtil.getMapValue(resultMap, "aae020"));
            if (commonUtil.getMapValue(resultMap, "state1").equals("0")) {
                zt.setText("未审核");
            } else if (commonUtil.getMapValue(resultMap, "state1").equals("1")) {
                zt.setText("已审核");
            } else if (commonUtil.getMapValue(resultMap, "state1").equals("2")) {
                zt.setText("已注销");
            }
        }
    }
}
