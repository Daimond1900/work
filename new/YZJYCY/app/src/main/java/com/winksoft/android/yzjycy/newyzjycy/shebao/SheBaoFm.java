package com.winksoft.android.yzjycy.newyzjycy.shebao;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.DialogUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * 社保
 */
public class SheBaoFm extends Fragment implements View.OnClickListener {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private View layout;
    private boolean isBindInfo = false; //判断是否帐号绑定
    Dialog proDialog;
    XwzxDAL xwzxDAL;
    private User user;
    private CommonUtil commonUtil;
    private DialogUtil dialogUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (layout != null) {
            // initLogin();
            // 防止多次new出片段对象，造成图片错乱问题
            return layout;
        }
        commonUtil = new CommonUtil(getActivity());
        dialogUtil = new DialogUtil(getActivity());
        UserSession userSession = new UserSession(getActivity());
        user = userSession.getUser();
        layout = inflater.inflate(R.layout.fragment_shebao, container, false);
        xwzxDAL = new XwzxDAL(getActivity());
        initView();
        return layout;
    }

    private void initView() {
        View layout_shebao_ylj = layout.findViewById(R.id.layout_shebao_ylj);
        View layout_shebao_yl = layout.findViewById(R.id.layout_shebao_yl);
        View layout_shebao_sy = layout.findViewById(R.id.layout_shebao_sy);
        layout_shebao_ylj.setOnClickListener(this);
        layout_shebao_yl.setOnClickListener(this);
        layout_shebao_sy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layout_shebao_ylj:    //养老

                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {


                    if (Constants.iflogin) if (isBindInfo) {
                        StringBuilder url = new StringBuilder(Constants.IP + "android/socialsec/pensionPayView");
                        queryInfo(url, "养老金查询");
                    } else { // 跳转信息绑定页面
                        intent = new Intent(getActivity(), SheBaoInfoSure.class);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.layout_shebao_yl: // 医疗
                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    if (Constants.iflogin) {
                        if (isBindInfo) {
                            StringBuilder url = new StringBuilder(Constants.IP + "android/socialsec/medicareQuery");
                            queryInfo(url, "医疗保险查询");
                        } else {
                            intent = new Intent(getActivity(), SheBaoInfoSure.class);
                            startActivity(intent);
                        }
                    } else {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.layout_shebao_sy: // 失业
                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    if (Constants.iflogin) {
                        if (isBindInfo) {
                            StringBuilder url = new StringBuilder(Constants.IP + "android/socialsec/loseWork");
                            queryInfo(url, "失业保险查询");
                        } else {
                            intent = new Intent(getActivity(), SheBaoInfoSure.class);
                            startActivity(intent);
                        }
                    } else {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private void queryInfo(StringBuilder url, String title) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", user.getUserId());
        map.put("datetime", DateUtil.getStrCurrentDateTime());
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);

        url.append("?id=").append(user.getUserId())
                .append("&&datetime=").append(map.get("datetime"))
                .append("&&appSign=").append(SignHashMap.get("appSign"));
        Intent i = new Intent(getActivity(), CommonPageView.class);
        i.putExtra("url", url.toString());
        i.putExtra("title", title);
        startActivity(i);

    }

    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(), false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        getActivity(), "加载中,请稍后...");
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
        xwzxDAL.checkUserInfo(callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                isBindInfo = true;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 将layout从父组件中移除
        ViewGroup parent = (ViewGroup) layout.getParent();
        parent.removeView(layout);
    }
}
