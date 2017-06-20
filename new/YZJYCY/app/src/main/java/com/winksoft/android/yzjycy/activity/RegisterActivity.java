package com.winksoft.android.yzjycy.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.winksoft.android.widget.TimeButton;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.LoginDal;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends BaseActivity {

    @SetView(id = R.id.topTitle)
    TextView topTitle;
    @SetView(id = R.id.edit_uid)
    TextView edit_uid;
    @SetView(id = R.id.edit_code)
    TextView edit_code;
    @SetView(id = R.id.edit_realname)
    TextView edit_realname; /*真实姓名*/
    @SetView(id = R.id.edit_psw)
    TextView edit_psw;
    @SetView(id = R.id.edit_psw_val)
    TextView edit_psw_val;
    @SetView(id = R.id.radioGroup)
    RadioGroup radioGroup;


    @SetView(id = R.id.backBtn, click = "onViewClick")
    Button backBtn;
    @SetView(id = R.id.btn_code, click = "onViewClick")
    TimeButton btn_code;
    @SetView(id = R.id.btn_sign_up, click = "onViewClick")
    Button btn_sign_up;

    Dialog proDialog;
    private LoginDal loginDal;

    private String phone = "", uuid = "", loginType = "0", pwd = "", confirmPwd = "",
            code = "", edit_realname_str = "";

    private final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btn_code.onCreate(savedInstanceState);
        topTitle.setText("注册");
        loginDal = new LoginDal(this);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioPersonal) {
                    loginType = "0";
                } else {
                    loginType = "0";    // 暂时改为 0 ： 个人用户  1 ：企业用户
                }
            }
        });
    }

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.backBtn:
                RegisterActivity.this.finish();
                break;
            case R.id.btn_sign_up:
                phone = edit_uid.getText().toString();
                code = edit_code.getText().toString();
                pwd = edit_psw.getText().toString().trim();
                confirmPwd = edit_psw_val.getText().toString().trim();

                edit_realname_str = edit_realname.getText().toString().trim();
                if (!phoneValidate(phone)) {
                    return;
                }

                if (edit_realname_str.equals("")) {
                    showToast("请输入真实姓名", false);
                    return;
                }

                if (code.equals("")) {
                    showToast("请输入验证码", false);
                    return;
                }

                if (pwd.equals("")) {
                    showToast("请输入确认密码", false);
                    return;
                }
                if (pwd.length() < 6) {
                    showToast("密码最小六位", false);
                    return;
                }
                if (confirmPwd.equals("")) {
                    showToast("请输入确认密码", false);
                    return;
                }
                if (!pwd.equals(confirmPwd)) {
                    showToast("两次密码不一致,请重新输入", false);
                    return;
                }
                registerUser();
                break;
            case R.id.btn_code:
                phone = edit_uid.getText().toString().trim();
                if (!phoneValidate(phone)) {
                    return;
                }
                getCode();

                break;
            default:
                break;
        }
    }

    /**
     * 判断是否输入手机号
     *
     * @param phone
     * @return
     */
    private boolean phoneValidate(String phone) {
        if (phone.equals("")) {
            showToast("请输入手机号码", false);
            return false;
        }
        if (phone.length() != 11) {
            showToast("请输入11位手机号码", false);
            return false;
        }
        return true;
    }


    /**
     * 获取验证码
     */
    public void getCode() {
        uuid = UUID.randomUUID().toString();
        AjaxCallBack<Object> ajaxCallBack = new AjaxCallBack<Object>(context,
                false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        RegisterActivity.this, "获取验证码,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                closeDialog();
                try {
                    Map<String, String> map = DataConvert.toMap((String) arg0);
                    Log.d(TAG, "onSuccess: 成功 = " + map);
                    if (map != null && map.get("msg") != null) {
                        showToast(map.get("msg"), false);
                        if (map.get("success").equals("true")) {
                            btn_code.start();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onFailure: 出现异常");
                    showToast("获取失败！", false);
                }
                btn_code.setEnabled(true);
            }

            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
                Log.d(TAG, "onFailure: 404 不通");
                closeDialog();
                btn_code.setEnabled(true);
                showToast("获取失败！", false);
            }
        };
        loginDal.getCode(edit_uid.getText().toString(), uuid, ajaxCallBack);
    }

    /**
     * 注册
     */
    public void registerUser() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(context,
                false) {

            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        RegisterActivity.this, "数据提交,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                closeDialog();
                registerResult((String) arg0);
            }

            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
                closeDialog();
                showDoigMsg("系统提示", "注册失败,请重试或联系后台管理员...");
            }
        };

        Log.d(TAG, "registerUser: 注册的真实姓名 = " + edit_realname_str);
        loginDal.registerUser(phone, code, pwd, loginType, uuid, edit_realname_str, callBack);
    }

    private void registerResult(String json) {
        Log.d(TAG, "registerUser: 注册的返回结果 = " + json);
        try {
            Map<String, String> map = DataConvert.toMap(json);
            if (map != null && map.get("msg") != null) {
                showToast(map.get("msg"), false);
                if (map.get("success").equals("true")) {
                    this.finish();
                }
            } else {
                showToast("数据解析异常", false);
            }
        } catch (Exception e) {
            showToast("数据解析异常", false);
        }
    }

    private void closeDialog() {
        if (proDialog != null && proDialog.isShowing())
            proDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        btn_code.onDestroy();
        super.onDestroy();
    }
}
