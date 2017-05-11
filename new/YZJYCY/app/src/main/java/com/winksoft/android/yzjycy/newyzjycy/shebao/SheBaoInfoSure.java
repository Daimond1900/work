package com.winksoft.android.yzjycy.newyzjycy.shebao;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.IDCard;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.text.ParseException;
import java.util.Map;

/**
 * 社保信息确认
 * Created by 1900 on 2017/4/7.
 */
public class SheBaoInfoSure extends BaseActivity implements View.OnClickListener {
    private EditText et_phone, et_sbm, et_sfz, et_sbh;
    Dialog proDialog;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private String sbh = "", sfz = "", sbm = "", phone = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shebaoinfo_activity);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        initView();
    }

    private void initView() {
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_sbm = (EditText) findViewById(R.id.et_sbm);
        et_sfz = (EditText) findViewById(R.id.et_sfz);
        et_sbh = (EditText) findViewById(R.id.et_sbh);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.btn_sure:
                if ("".equals(et_sbh.getText().toString().trim())) {
                    commonUtil.shortToast("社保号不能为空！");
                    return;
                }
                if ("".equals(et_sbm.getText().toString().trim())) {
                    commonUtil.shortToast("个人识别码不能为空！");
                    return;
                }
                if ("".equals(et_sfz.getText().toString().trim())) {
                    commonUtil.shortToast("身份证不能为空！");
                    return;
                } else {
                    try {
                        if (!"".equals(IDCard.IDCardValidate(et_sfz.getText().toString().trim()))) {
                            commonUtil.shortToast("身份证输入有误！");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                sfz = et_sfz.getText().toString().trim();
                sbh = et_sbh.getText().toString().trim();
                sbm = et_sbm.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                doCompleteSheBaoInfo();
                break;

            default:
                break;
        }
    }

    private void doCompleteSheBaoInfo() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        SheBaoInfoSure.this, "信息提交中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doCompleteSheBaoInfoResult((String) arg0);
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
        //Id	账号
//     * idcard	身份证号码
//     * sbcard	社保号
//     * idnumber	省个人识别码
//     * phone	联系电话
//     * appSign	签名认证
//     * dateTime	yyyy-MM-dd HH:mm:ss
        xwzxDAL.doCompleteSheBaoInfo(user.getUserId(), sfz, sbh, sbm, phone, callBack);
    }

    /**
     * @param json 结果
     */
    private void doCompleteSheBaoInfoResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast("信息编辑成功!");
                this.finish();
            } else {
                commonUtil.shortToast("信息编辑失败!");
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }
}
