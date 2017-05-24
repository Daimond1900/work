package com.winksoft.android.yzjycy.newyzjycy.wd;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.LoginDal;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.newyzjycy.pxkq.me.WdpxFragmentActivity;
import com.winksoft.android.yzjycy.newyzjycy.wd.clearcache.DataCleanManager;
import com.winksoft.android.yzjycy.newyzjycy.wd.jl.NewWdjlActivity;
import com.winksoft.android.yzjycy.newyzjycy.wd.khfk.WdfkListActivity;
import com.winksoft.android.yzjycy.newyzjycy.wd.tdandgz.NewQztMyJobActivity;
import com.winksoft.android.yzjycy.newyzjycy.wd.wjdc.WjdcListActivity;
import com.winksoft.android.yzjycy.ui.bminfo.Zpt_RegisterCountListActivity;
import com.winksoft.android.yzjycy.ui.enterprise.ZptEnterpriseActivity;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.ui.pxxx.BmInfoSureActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DialogUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Map;

/**
 * 我的
 */
public class MineFragment extends Fragment implements OnClickListener {

    private View mViewNotLogined;
    private View mViewLogined;
    private View layout;
    private TextView tv_name, tv_phone;
    private View layoutCompany, layoutPersonal, layout_mine_xgmm;
    private LoginDal loginDal;
    private Button exitBtn;
    private User user;
    private String sex;
    private String sjhm;
    private String csrq;
    private String newRealname, newSfz;
    private TextView tv_cache_size;
    private ProgressBar publicloading;
    Dialog proDialog;
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

        loginDal = new LoginDal(getActivity());
        layout = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return layout;
    }


    private void onLoadUser() {
        if (Constants.iflogin) {
            user = new UserSession(getContext()).getUser();
            tv_phone.setText(user.getLoginName());
            exitBtn.setVisibility(View.VISIBLE);
            mViewLogined.setVisibility(View.VISIBLE);
            mViewNotLogined.setVisibility(View.GONE);
            layout_mine_xgmm.setVisibility(View.VISIBLE);

            if (user.getRoleid().equals("4028c2e948ba2b750148ba2e76960000")) {
                tv_name.setText(newRealname);
                layoutCompany.setVisibility(View.GONE);
                layoutPersonal.setVisibility(View.VISIBLE);
            } else if (user.getRoleid().equals("4028c2e941fd35f60141fdac66280006")) {
                tv_name.setText(user.getZbdw());
                layoutCompany.setVisibility(View.VISIBLE);
                layoutPersonal.setVisibility(View.GONE);
            }
        } else {
            tv_name.setText("");
            tv_phone.setText("");

            exitBtn.setVisibility(View.GONE);

            mViewLogined.setVisibility(View.GONE);
            mViewNotLogined.setVisibility(View.VISIBLE);

            layoutCompany.setVisibility(View.GONE);
            layoutPersonal.setVisibility(View.GONE);
            layout_mine_xgmm.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoginInfo();
        try {
            Log.d("ttt", "onClick:  清除缓存前 本APP内部cache大小 = " + DataCleanManager.getCacheSize(getActivity().getCacheDir()));
            Log.d("ttt", "onClick:  清除缓存前 file 大小 = " + DataCleanManager.getCacheSize(getActivity().getFilesDir()));
            Log.d("ttt", "onClick:  清除缓存前 外部cache 大小 = " + DataCleanManager.getCacheSize(getActivity().getExternalCacheDir()));
            Log.d("ttt", "-------------------------------------------------------");
            tv_cache_size.setText(DataCleanManager.getCacheSize(getActivity().getCacheDir()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLoginInfo() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(getActivity(),
                false) {

            @Override
            public void onStart() {
                super.onStart();
                // setLoadingMsg("正在登录中,请稍后...");
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                loginResult((String) arg0);
            }

            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
            }
        };
        user = new UserSession(getContext()).getUser();
        loginDal.login(user.getLoginName(), user.getPassWord(), callBack);
    }

    private void loginResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if ("true".equals(map.get("success"))) {
                Map<String, String> map1 = DataConvert.toConvertStringMap(json,
                        "user");
                if (map1 != null) {
                    newRealname = getMapValue(map1, "realname");
                    newSfz = getMapValue(map1, "idcard");
                    sex = getMapValue(map1, "aac004");
                    sjhm = getMapValue(map1, "acb501"); // 手机号
                    csrq = getMapValue(map1, "aac006");// 出生日期

                    onLoadUser();
                }
            }
        }
    }


    private void initView() {
        publicloading = (ProgressBar) layout.findViewById(R.id.publicloading);
        tv_cache_size = (TextView) layout.findViewById(R.id.tv_cache_size);

        Button loginBtn = (Button) layout.findViewById(R.id.personal_login_button);
        loginBtn.setOnClickListener(this);
        exitBtn = (Button) layout.findViewById(R.id.btn_exit);
        exitBtn.setOnClickListener(this);

        mViewNotLogined = layout.findViewById(R.id.layout_not_logined);
        mViewLogined = layout.findViewById(R.id.layout_logined);

        layoutPersonal = layout.findViewById(R.id.layoutPersonal);
        layoutCompany = layout.findViewById(R.id.layoutCompany);

        tv_name = (TextView) layout.findViewById(R.id.tv_name);
        tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
        ImageView imageView = (ImageView) layout.findViewById(R.id.user_icon);
        imageView.setOnClickListener(this);
        layout_mine_xgmm = layout.findViewById(R.id.layout_mine_xgmm);
        layout_mine_xgmm.setOnClickListener(this);

        layout.findViewById(R.id.layout_mine_update).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_clear).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_help).setOnClickListener(this);

        // 初始化个人
        layout.findViewById(R.id.layout_mine_wdjl).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_wdqz).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_mstz).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_wdpx).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_gzgw).setOnClickListener(this);
        layout.findViewById(R.id.layout_khfk).setOnClickListener(this);         //客户反馈
        layout.findViewById(R.id.layout_wjdc).setOnClickListener(this);         //问卷调查


        // 初始化企业
        layout.findViewById(R.id.layout_mine_qyxx).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_bmgl).setOnClickListener(this);
        layout.findViewById(R.id.layout_mine_lygl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent1;
        switch (v.getId()) {
            case R.id.btn_exit:
                Constants.iflogin = false;
                onLoadUser();
                break;
            case R.id.user_icon: // 点击用户头像

                break;
            case R.id.layout_mine_qyxx: // 企业信息
                intent1 = new Intent(getContext(), ZptEnterpriseActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_mine_bmgl: // 报名信息
                intent1 = new Intent(getContext(), Zpt_RegisterCountListActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_mine_lygl: // 录用管理
                intent1 = new Intent(getContext(), Zpt_EmployCountListActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_mine_gzgw: //我关注的
                intent1 = new Intent(getContext(), NewQztMyJobActivity.class);
                intent1.putExtra("flag", "3");
                startActivity(intent1);
                break;
            case R.id.layout_mine_wdjl: // 我的简历
                intent1 = new Intent(getContext(), NewWdjlActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_mine_wdqz: // 我的求职	// 完善个人信息
                intent1 = new Intent(getContext(), BmInfoSureActivity.class);
                intent1.putExtra("flag", "1");
                intent1.putExtra("lr", newRealname);
                intent1.putExtra("sfz", newSfz);
                intent1.putExtra("xb", sex);
                intent1.putExtra("sjhm", sjhm);
                intent1.putExtra("csrq", csrq);
                startActivity(intent1);
                break;
            case R.id.layout_mine_mstz: // 面试邀请	// new 报名单位表
                intent1 = new Intent(getContext(), NewQztMyJobActivity.class);
                intent1.putExtra("flag", "1");
                startActivity(intent1);
                break;
            case R.id.layout_mine_wdpx: // 我的培训
                intent1 = new Intent(getContext(), WdpxFragmentActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_mine_xgmm: // 修改密码
                intent1 = new Intent(getContext(), AccountActivity.class);
                intent1.putExtra("title", "面试邀请函");
                startActivity(intent1);
                break;

            case R.id.personal_login_button: // 登录
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.layout_khfk: // 客户反馈
                startActivity(new Intent(getContext(), WdfkListActivity.class));
                break;
            case R.id.layout_wjdc: // 问卷调查
                startActivity(new Intent(getContext(), WjdcListActivity.class));
                break;
            case R.id.layout_mine_clear: //清除缓存

                doQchc();

                break;
            case R.id.layout_mine_help: //使用说明
                if (!commonUtil.checkNetWork()) {/*dialogUtil.shortToast("请设置网络连接!");*/
                    dialogUtil.alertNetError();
                } else {
                    Intent intent = new Intent(getActivity(), CommonPageView.class);
                    intent.putExtra("url", Constants.IP+"android/person/help");
                    intent.putExtra("title", "使用说明");
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 清除缓存的提示框
     */
    public void doQchc() {
        final Dialog builder = new Dialog(getActivity(), R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("清除缓存");
        pMsg.setText("确定需要清除缓存吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                DataCleanManager.cleanInternalCache(getActivity());
                try {
                    tv_cache_size.setText(DataCleanManager.getCacheSize(getActivity().getCacheDir()));
                } catch (Exception e) {

                }
                builder.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 将layout从父组件中移除
        ViewGroup parent = (ViewGroup) layout.getParent();
        parent.removeView(layout);
    }

    private String getMapValue(Map<String, String> map, String key) {
        return map.get(key) == null ? "" : map.get(key);
    }

}
