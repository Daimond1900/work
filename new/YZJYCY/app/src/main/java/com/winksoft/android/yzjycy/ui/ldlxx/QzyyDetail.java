package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.data.ManpowerDetailDAL;
import com.winksoft.android.yzjycy.BaseActivity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 求职意愿
 *
 * @author user_ygl
 */
public class QzyyDetail extends BaseActivity {
    TextView sfz, name, qzgz, qzdq, yxgzjw, djsj;
    private String intentCode, intentName;
    private ManpowerDetailDAL dal;
    private String finalString;
    Button save, back;
    private Dialog proDialog;
    private CommonUtil commonUtil;
    private JcDAL jcDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcy_ldl_qzyy_detail);
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
                QzyyDetail.this.finish();
            }
        });

        sfz = (TextView) findViewById(R.id.sfz);
        name = (TextView) findViewById(R.id.name);
        qzgz = (TextView) findViewById(R.id.qzgz);
        qzdq = (TextView) findViewById(R.id.qzdq);
        yxgzjw = (TextView) findViewById(R.id.yxgzjw);
        djsj = (TextView) findViewById(R.id.djsj);
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
                        QzyyDetail.this, "加载中,请稍后...");
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
        jcDAL.doQzyyQuery(intentCode, callBack);
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

    private void setData(Map<String, String> resultMap) {
        if (resultMap != null) {
            sfz.setText(commonUtil.getMapValue(resultMap, "aac002"));
            name.setText(commonUtil.getMapValue(resultMap, "aac003"));
            qzgz.setText(commonUtil.getMapValue(resultMap, "aca112"));
            qzdq.setText(ParseData.JYDQXS.get(commonUtil.getMapValue(resultMap, "acc901")));
            yxgzjw.setText(commonUtil.getMapValue(resultMap, "acc034"));
            djsj.setText(commonUtil.getMapValue(resultMap, "aae036"));
        }
    }


}
