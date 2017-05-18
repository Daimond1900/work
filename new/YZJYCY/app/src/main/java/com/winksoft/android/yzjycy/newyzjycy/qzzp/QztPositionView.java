package com.winksoft.android.yzjycy.newyzjycy.qzzp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.newyzjycy.wd.jl.ModifyJLActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.StringHelper;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 * 职位详细
 *
 * @author wujiahong 2012-10-18
 */
public class QztPositionView extends BaseActivity {
    private static final String TAG = "QztPositionView";
    private View company_item;
    private Button back_btn, deliveryBtn, contactBtn, scBt;
    private TextView txt_zw, companyName, pushDate, peopleCount, monthlyPay, record, area, telNumber,
            positionDesc, companyAddress, gzsm, lxr, sxzy, xl, nl, sgtz, gxrq, cyns, tv_gz;
    private String telNo = "", positionId = "", companyId = "", zwId = "", zwmcpost = "", zphId = "";
    private XwzxDAL xwzxDAL;
    private Map<String, String> positionInfo;
    private String companyNameStr = "";
    private ListView listView;
    private ArrayList<Map<String, String>> tels;
    Dialog proDialog;
    private CommonUtil commonUtil;
    private User user;
    private String flag = "";
    private TableRow tdrqrow, dwhfztrow, grhfztrow, gzrqrow;
    private TextView tdrq, dwhfzt, grhfzt, gzrq;
    private String gz = "";
    private String isgz, is_enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_position_info);
        UserSession session = new UserSession(this);
        user = session.getUser();
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);

        flag = getIntent().getStringExtra("flag");  // 1：报名岗位 3：关注岗位

        Log.d(TAG, "onCreate: flag " + flag);
        // 初始化界面
        initPage();
        // 加载数据

        /*异常情况刷新*/
        findViewById(R.id.sxyc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPostData();
    }

    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                findViewById(R.id.buttom_menu).setVisibility(View.VISIBLE);
                findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                Log.d(TAG, "onFailure: strMsg++++     " + strMsg);
                //网络异常时调用
                findViewById(R.id.buttom_menu).setVisibility(View.GONE);
                findViewById(R.id.scroll).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        int i = -1;
        if ("1".equals(flag)) {
            i = 1;
        } else if ("3".equals(flag)) {
            i = 3;
        }
        Log.d(TAG, "登录是判断 = " + (Constants.iflogin ? user.getUserId() : ""));

        xwzxDAL.getPosition(Constants.iflogin ? user.getUserId() : "", positionId, i, callBack);

    }

    private void postResult(String json) {
        Log.d(TAG, "postResult: json ++++++++newnewnewneewenwenw++++++++++ " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                positionInfo = DataConvert.toConvertStringMap(json, "row");
                Log.d(TAG, "postResult: positionInfo =====" + positionInfo);
                setPageData();
            } else {
                findViewById(R.id.buttom_menu).setVisibility(View.GONE);
                findViewById(R.id.scroll).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
            }
        } else {
            commonUtil.shortToast("查询失败!");
        }
    }


    /**
     * 取消投递简历
     */
    private void onQxTdjl() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "正在取消,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                qxjltdResult((String) arg0);
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
        xwzxDAL.doQxPostDelivery(Constants.iflogin ? user.getUserId() : "", positionId, callBack);
    }

    /**
     * 取消 -- 投递简历 结果
     */
    private void qxjltdResult(String json) {
        Log.d(TAG, "qxjltdResult: 取消投递接口 = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(commonUtil.getMapValue(map, "success"))) {
                commonUtil.shortToast(map.get("msg"));
                deliveryBtn.setText("投递简历");
                if ("1".equals(flag)) {
                    this.finish();
                }
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后！");
        }

    }

    /**
     * 取消收藏
     */
    private void onQxSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "正在取消,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                qxScResult((String) arg0);
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
        xwzxDAL.doQxScZw(Constants.iflogin ? user.getUserId() : "", positionId, callBack);
    }

    /**
     * 取消 -- 投递简历 结果
     */
    private void qxScResult(String json) {
        Log.d(TAG, "qxjltdResult: 取消收藏接口 = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(commonUtil.getMapValue(map, "success"))) {
                commonUtil.shortToast(map.get("msg"));
                scBt.setText("收藏职位");
                if ("3".equals(flag)) {
                    this.finish();
                }
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后！");
        }

    }


    /**
     * 投递简历
     */
    private void onPostTdjl() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "简历投递中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                jltdResult((String) arg0);
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
        xwzxDAL.doNewPostDelivery(Constants.iflogin ? user.getUserId() : "", positionId, companyNameStr, zwmcpost, companyId, zphId, callBack);
    }

    /**
     * 投递简历 结果
     */
    private void jltdResult(String json) {
        Log.d(TAG, "投递简历的结果:  json = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(commonUtil.getMapValue(map, "success"))) {
                commonUtil.shortToast(map.get("msg"));
                deliveryBtn.setText("取消投递");
            } else {
                if (("您还没有简历！").equals(commonUtil.getMapValue(map, "msg"))) {
                    commonUtil.shortToast("请先完善您的简历");
                    Intent intent = new Intent(this, ModifyJLActivity.class);
                    intent.putExtra("pdbt", 0);
                    startActivity(intent);
                }
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后！");
        }

    }

    private void initPage() {
        MyOnclick onClick = new MyOnclick();
        // 投递简历
        deliveryBtn = (Button) findViewById(R.id.deliveryBtn);
        deliveryBtn.setOnClickListener(onClick);

        // 收藏职位
        scBt = (Button) findViewById(R.id.scBt);
        scBt.setOnClickListener(onClick);

        tdrqrow = (TableRow) findViewById(R.id.tdrqrow);
        dwhfztrow = (TableRow) findViewById(R.id.dwhfztrow);
        grhfztrow = (TableRow) findViewById(R.id.grhfztrow);

        gzrqrow = (TableRow) findViewById(R.id.gzrqrow);

        tdrq = (TextView) findViewById(R.id.tdrq);
        dwhfzt = (TextView) findViewById(R.id.dwhfzt);
        grhfzt = (TextView) findViewById(R.id.grhfzt);


        gzrq = (TextView) findViewById(R.id.gzrq);

        if ("1".equals(flag)) {   //报名岗位
            tdrqrow.setVisibility(View.VISIBLE);
            dwhfztrow.setVisibility(View.VISIBLE);
            grhfztrow.setVisibility(View.VISIBLE);
            gzrqrow.setVisibility(View.GONE);
            deliveryBtn.setText("取消投递");
        } else if ("3".equals(flag)) { //关注岗位
            tdrqrow.setVisibility(View.GONE);
            dwhfztrow.setVisibility(View.GONE);
            grhfztrow.setVisibility(View.GONE);
            gzrqrow.setVisibility(View.VISIBLE);
            scBt.setText("取消收藏");
        }


        positionId = this.getIntent().getStringExtra("positionId") == null ? ""
                : this.getIntent().getStringExtra("positionId");    // 职位编号
        companyNameStr = this.getIntent().getStringExtra("companyNameStr") == null ? ""
                : this.getIntent().getStringExtra("companyNameStr");    // 单位名称

        // 点击公司查看公司详细
        company_item = (TableRow) findViewById(R.id.company_item);
        if (!this.getIntent().getBooleanExtra("isClick", false)) {// 如果是所有职位进来查看公司详细不可点
            findViewById(R.id.im_right).setVisibility(View.VISIBLE);
            company_item.setOnClickListener(onClick);
        } else {
            findViewById(R.id.im_right).setVisibility(View.GONE);
//            companyName.setCompoundDrawables(null,null,null,null);  //左 上 右 下

        }

        // 返回按钮
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(onClick);


        // 联系HR
        contactBtn = (Button) findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(onClick);

        tv_gz = (TextView) findViewById(R.id.tv_gz);
        txt_zw = (TextView) findViewById(R.id.txt_zw);// 职位名称
        companyName = (TextView) findViewById(R.id.companyName);// 公司名称
        pushDate = (TextView) findViewById(R.id.pushDate);// 有效期
        gzsm = (TextView) findViewById(R.id.gzsm); // 工作说明
        peopleCount = (TextView) findViewById(R.id.peopleCount);// 招聘人数
        monthlyPay = (TextView) findViewById(R.id.monthlyPay);// 工资
        telNumber = (TextView) findViewById(R.id.telNumber);// 联系电话
        positionDesc = (TextView) findViewById(R.id.positionDesc);// 职位描述
        lxr = (TextView) findViewById(R.id.lxr); //联系人
        sxzy = (TextView) findViewById(R.id.sxzy);
        xl = (TextView) findViewById(R.id.xl);
        nl = (TextView) findViewById(R.id.nl);
        sgtz = (TextView) findViewById(R.id.sgtz);
        gxrq = (TextView) findViewById(R.id.gxrq);
        cyns = (TextView) findViewById(R.id.cyns);
        record = (TextView) findViewById(R.id.record);// 学历要求
        area = (TextView) findViewById(R.id.area);// 用工区域


    }

    private class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.company_item:
                    Intent bl = new Intent(QztPositionView.this, QztEnterpriseView.class);
                    bl.putExtra("companyId", companyId);
                    Log.d(TAG, "onClick: 公司ID = " + companyId);
                    bl.putExtra("companyName", companyNameStr);
                    startActivity(bl);
                    break;
                case R.id.back_btn:
                    QztPositionView.this.finish();
                    break;
                case R.id.deliveryBtn:
                    if (!Constants.iflogin) {
                        commonUtil.shortToast("请先登录");
                        startActivity(new
                                Intent(QztPositionView.this, LoginActivity.class));
                    } else {
//                        if (user.getIdCard().equals("")) {
//                            commonUtil.shortToast("您还未完善简历,请先完善简历再投递!");
//                            // 跳到新增简历界面
//                            Intent addResume = new Intent(QztPositionView.this, QztAddResumeActivity.class);
//                            addResume.putExtra("userId", user.getUserId());
//                            addResume.putExtra("isAdd", true);
//                            startActivity(addResume);
//                            QztPositionView.this.finish();
//
//                        }
//                        else {
                        String flagStr = deliveryBtn.getText().toString().trim();
                        if ("投递简历".equals(flagStr)) {
                            doTdTd();
                        } else if ("取消投递".equals(flagStr)) {
                            doQxTd();
                        }
                    }
                    break;
                case R.id.contactBtn:
                    if (telNo.equals("")) {
                        commonUtil.shortToast("没有找到HR联系号码!");
                    } else {
                        if (telNo.length() < 11) {
                            dialogUtil.showCallDialog("系统提示", "您确定要拔打" + telNo + "吗?", telNo);
                        } else {
                            showMsg();
                        }
                    }
                    break;
                case R.id.scBt: // 收藏职位
                    if (!Constants.iflogin) {
                        commonUtil.shortToast("请先登录");
                        startActivity(new
                                Intent(QztPositionView.this, LoginActivity.class));
                    } else {
                        String flagStr = scBt.getText().toString().trim();
                        if ("收藏职位".equals(flagStr)) {
                            ScgwPost();
                        } else if ("取消收藏".equals(flagStr)) {
                            doScZw();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 投递的提示框
     */
    public void doTdTd() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("投递简历");
        pMsg.setText("确定投递您的简历吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                onPostTdjl();  // 投递的接口
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


    /**
     * 取消投递的提示框
     */
    public void doQxTd() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("取消投递");
        pMsg.setText("确定取消投递记录吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                onQxTdjl();   // 取消投递的接口
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

    /**
     * 取消收藏的提示框
     */
    public void doScZw() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("取消收藏");
        pMsg.setText("确定取消收藏吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                onQxSc();   // 取消收藏的接口
                builder.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
//        builder.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }


    /**
     * 收藏职位
     */
    private void ScgwPost() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "正在处理中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                scResult((String) arg0);
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
        xwzxDAL.doSczw(user.getUserId(), zwId, companyNameStr, zwmcpost, companyId, zphId, gz, callBack);
    }

    /**
     * 收藏职位结果
     */
    private void scResult(String json) {
        Log.d(TAG, "scResult: json = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(commonUtil.getMapValue(map, "success"))) {
                commonUtil.shortToast(map.get("msg"));
                scBt.setText("取消收藏");
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后！");
        }

    }


    /***
     * 加载数据
     * aca112 岗位名称CB21  acb22a 岗位描述CB21
     */
    private void setPageData() {
        if (positionInfo != null) {

            isgz = commonUtil.getMapValue(positionInfo, "is_focus");
            is_enroll = commonUtil.getMapValue(positionInfo, "is_enroll");
            if ("1".equals(is_enroll)) {    //已报名
                deliveryBtn.setText("取消投递");
            } else {
                deliveryBtn.setText("投递简历");
            }
            if ("1".equals(isgz)) {
                scBt.setText("取消收藏");
            } else {
                scBt.setText("收藏职位");
            }
            gz = commonUtil.getMapValue(positionInfo, "aca111");
            tv_gz.setText(gz);
            zphId = commonUtil.getMapValue(positionInfo, "acb330");
            zwId = commonUtil.getMapValue(positionInfo, "acb200");
            zwmcpost = commonUtil.getMapValue(positionInfo, "aca112");
            txt_zw.setText(zwmcpost);// 职位名称 招聘工种
            gzsm.setText(commonUtil.getMapValue(positionInfo, "acb22a")); // 工种说明
            String nannum = "".equals(commonUtil.getMapValue(positionInfo, "acb21d")) ? "0人" : commonUtil.getMapValue(positionInfo, "acb21d")+"人";
            String nvnum = "".equals(commonUtil.getMapValue(positionInfo, "acb21e")) ? "0人" : commonUtil.getMapValue(positionInfo, "acb21e")+"人";

            peopleCount.setText("男性:" + nannum
                    + "; 女性:" + nvnum);// 招聘人数

            pushDate.setText(commonUtil.getMapValue(positionInfo, "aae397") + "~" + commonUtil.getMapValue(positionInfo, "aae398"));// 有效期
            String price = "".equals(commonUtil.getMapValue(positionInfo, "acb241")) ? "0元" : commonUtil.getMapValue(positionInfo, "acb241")+"元";
            if ("".equals(price))
                price = "面议";
            monthlyPay.setText(price);// 工资
            telNo = commonUtil.getMapValue(positionInfo, "aae005");
            telNumber.setText(telNo);// 联系电话
            lxr.setText(commonUtil.getMapValue(positionInfo, "aae004"));
            sxzy.setText(commonUtil.getMapValue(positionInfo, "aac183"));
            xl.setText(commonUtil.getMapValue(positionInfo, "aac011"));// 学历

            String maxnl = "".equals(commonUtil.getMapValue(positionInfo, "acb222")) ? "0岁" : commonUtil.getMapValue(positionInfo, "acb222")+"岁";
            String minnl = "".equals(commonUtil.getMapValue(positionInfo, "acb221")) ? "0岁" : commonUtil.getMapValue(positionInfo, "acb221")+"岁";


            nl.setText(minnl + "~" + maxnl);//年龄

            String sgtz1 = "".equals(commonUtil.getMapValue(positionInfo, "aac034")) ? "0cm" : commonUtil.getMapValue(positionInfo, "aac034")+"cm";
            String sgtz2 = "".equals(commonUtil.getMapValue(positionInfo, "aac035")) ? "0kg" : commonUtil.getMapValue(positionInfo, "aac035")+"kg";
            sgtz.setText("身高:" + sgtz1 + "; 体重:" + sgtz2);

            gxrq.setText(commonUtil.getMapValue(positionInfo, "aae397"));

            String cyns1 = "".equals(commonUtil.getMapValue(positionInfo, "acc217")) ? "0年" : commonUtil.getMapValue(positionInfo, "acc217")+"年";

            cyns.setText(cyns1);
            area.setText(positionInfo.get("aab301"));// 用工区域


            companyId = positionInfo.get("aab001") == null ? "" : positionInfo.get("aab001");// 公司编号

            companyName.setText(companyNameStr);// 公司名称
            // 职位描述
            positionDesc.setText(Html.fromHtml(
                    "      其他待遇: " + positionInfo.get("acb244") + ""
                            + "<br/><br/>      户口所在地（招收区域限制）:" + positionInfo.get("aac010")));

            tdrq.setText(commonUtil.getMapValue(positionInfo, "tdrq"));
            dwhfzt.setText(commonUtil.getMapValue(positionInfo, "hfzt"));
            grhfzt.setText(commonUtil.getMapValue(positionInfo, "hfzt2"));
            gzrq.setText(commonUtil.getMapValue(positionInfo, "gzrq"));
        } else {
            commonUtil.shortToast("信息加载失败!");
        }
    }

    /***
     * 弹出电话号码列表框让用户拨打电话
     */
    public void showMsg() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.qzt_template_spinner);

        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        ptitle.setText("请选择拨打号码");

        listView = (ListView) builder.findViewById(R.id.listview);
        setTels();

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String telNumber = tels.get(arg2).get("number");
                dialogUtil.showCallDialog("系统提示", "您确定要拔打" + telNumber + "吗?", telNumber);
                builder.dismiss();
            }

        });
        builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }

    private void setTels() {
        tels = new ArrayList<Map<String, String>>();
        ArrayList<String> list = StringHelper.AnalyzePhoneNumber(telNo);
        Map<String, String> map;
        for (int i = 0; i < list.size(); i++) {
            map = new HashMap<String, String>();
            map.put("id", i + "");
            map.put("number", list.get(i));
            tels.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, tels, R.layout.qzt_template_spinner_item,
                new String[]{"number"}, new int[]{R.id.item_lab});
        listView.setAdapter(adapter);

    }
}
