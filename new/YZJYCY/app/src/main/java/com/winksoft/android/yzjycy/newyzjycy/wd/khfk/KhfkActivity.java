package com.winksoft.android.yzjycy.newyzjycy.wd.khfk;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.yifeng.nox.android.http.entity.FormFile;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建反馈
 * Created by 1900 on 2017/5/3.
 */

public class KhfkActivity extends BaseActivity implements View.OnClickListener {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private EditText khmc, sjhm, dzyj, fkzt, fknr;
    private Spinner fklx;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private String fklxId = "";
    private Map<String, String> postParams;
    private Dialog proDialog;
    private List<FormFile> listForm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_khfk);
        initView();
    }

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        List<Map<String, String>> queryFklx = xwzxDAL.queryFklx();
        khmc = (EditText) findViewById(R.id.khmc);
        sjhm = (EditText) findViewById(R.id.sjhm);
        dzyj = (EditText) findViewById(R.id.dzyj);
        fkzt = (EditText) findViewById(R.id.fkzt);
        fknr = (EditText) findViewById(R.id.fknr);

        fklx = (Spinner) findViewById(R.id.fklx);
        initFklx(queryFklx);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        Button bc = (Button) findViewById(R.id.bc);
        bc.setOnClickListener(this);

    }

    /**
     * 反馈类型下拉框
     *
     * @param queryFklx 反馈类型下拉框
     */
    private void initFklx(final List<Map<String, String>> queryFklx) {
        if (queryFklx != null && queryFklx.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, queryFklx, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fklx.setPrompt("请选择反馈类型");
            fklx.setAdapter(adapter);
            fklx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    fklxId = queryFklx.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.bc:

                String khmc1 = khmc.getText().toString().trim();
                String sjhm1 = sjhm.getText().toString().trim();
                String fkzt1 = fkzt.getText().toString().trim();
                String fknr1 = fknr.getText().toString().trim();
                String dzyj1 = dzyj.getText().toString().trim();

                if (TextUtils.isEmpty(khmc1)) {
                    commonUtil.shortToast("客户名称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(sjhm1)) {
                    commonUtil.shortToast("手机号码不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(fkzt1)) {
                    commonUtil.shortToast("反馈主题不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(fknr1)) {
                    commonUtil.shortToast("反馈内容不能为空！");
                    return;
                }
                postData(fkzt1, fknr1, fklxId, khmc1, sjhm1, dzyj1);
                onPostData();
                break;
        }
    }

    private void postData(String fkzt1, String fknr1, String fklxId, String khmc1, String sjhm1, String dzyj1) {
        postParams = new HashMap<>();
        postParams.put("userid", user.getUserId() == null && "".equals(user.getUserId()) ? "" : user.getUserId());
        postParams.put("type", fklxId == null || "".equals(fklxId) ? "" : fklxId);
        postParams.put("tel", sjhm1 == null || "".equals(sjhm1) ? "" : sjhm1);

        String secret_value = DateUtil.getStrCurrentDate();
        postParams.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(postParams, secret_key);
        postParams.put("appSign", (String) SignHashMap.get("appSign"));
        postParams.put("header", fkzt1 == null || "".equals(fkzt1) ? "" : fkzt1);
        postParams.put("content", fknr1 == null || "".equals(fknr1) ? "" : fknr1);
        postParams.put("reporter_name", khmc1 == null || "".equals(khmc1) ? "" : khmc1);
        postParams.put("email", dzyj1 == null || "".equals(dzyj1) ? "" : dzyj1);
    }

    /**
     * 提交数据 postParams listForm
     */
    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        KhfkActivity.this, "正在提交,请稍后...");
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
        xwzxDAL.xzWdfkResume(postParams, listForm, callBack);
    }

    /**
     * @param json 结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(commonUtil.getMapValue(map, "msg"));
            } else if ("true".equals(map.get("success"))) {
                commonUtil.shortToast(commonUtil.getMapValue(map, "msg"));
                this.finish();
            }
        } else {
            commonUtil.shortToast("系统正忙,请稍后再试!");
        }
    }


}
