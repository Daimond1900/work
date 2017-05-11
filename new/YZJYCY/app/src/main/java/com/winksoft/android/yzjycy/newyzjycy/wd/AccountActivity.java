package com.winksoft.android.yzjycy.newyzjycy.wd;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Map;

/**
 * 修改密码
 */
public class AccountActivity extends BaseActivity {
    private EditText old_passwordEdt, new_passwordEdt, confirm_passwordEdt;
    private String oldPasswd = "", newPasswd = "", confirmPasswd = "";
    private XwzxDAL xwzxDAL;
    private Dialog proDialog;
    private CommonUtil commonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_system_account);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        old_passwordEdt = (EditText) findViewById(R.id.old_passwordEdt);
        new_passwordEdt = (EditText) findViewById(R.id.new_passwordEdt);
        confirm_passwordEdt = (EditText) findViewById(R.id.confirm_passwordEdt);

        Button backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AccountActivity.this.finish();
            }
        });

        Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd();
            }
        });
    }

    /***
     * 验证修改密码
     */
    private void checkPwd() {
        oldPasswd = old_passwordEdt.getText().toString();
        newPasswd = new_passwordEdt.getText().toString();
        confirmPasswd = confirm_passwordEdt.getText().toString();
        if ("".equals(oldPasswd)) {
            commonUtil.shortToast("旧密码不能为空!");
        } else if (!user.getPassWord().equals(oldPasswd)) {
            commonUtil.shortToast("旧密码不正确!");
        } else if (newPasswd.equals("")) {
            commonUtil.shortToast("新密码不能为空!");
        } else if (newPasswd.length() < 6) {
            commonUtil.shortToast("新密码不能小于6位!");
        } else if (!newPasswd.equals(confirmPasswd)) {
            commonUtil.shortToast("新密码与确认密码不一致!");
        } else {
            loadDate();

        }
    }

    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        AccountActivity.this, "正在处理,请稍等...");
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
        xwzxDAL.doUpdatePwd(user.getUserId(), user.getLoginName(), oldPasswd, confirmPasswd, callBack);
    }

    /**
     * @param json 修改密码结果
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                user.setPassWord(newPasswd);
                UserSession session = new UserSession(AccountActivity.this);// 更新本地用户信息
                session.setUser(user);
                Constants.iflogin = false;
                commonUtil.shortToast("修改密码成功，请重新登陆！");
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统忙,请稍后再试!");
        }
    }
}
