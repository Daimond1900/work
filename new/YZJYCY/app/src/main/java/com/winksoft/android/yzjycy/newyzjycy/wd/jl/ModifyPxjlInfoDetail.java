package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 培训经历 修改
 * Created by 1900 on 2017/4/25.
 */

public class ModifyPxjlInfoDetail extends BaseActivity implements View.OnClickListener {
    private int flag;
    private EditText pxnr, pxdw, pxjg, pxbz;
    private TextView qsrq, jsrq;
    private String pxid;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private TimePickerView pvCustomTime, pvCustomTime1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypxjlinfodetail);

        flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {  //新增
            ((TextView) findViewById(R.id.textView2)).setText("新增培训经历");
        }

        initView();
    }

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);

        initQsrqCustomTimePicker();
        initJsrqCustomTimePicker();

        // 培训编号
        pxid = getIntent().getStringExtra("pxid") != null ? getIntent().getStringExtra("pxid") : "";
        // 起始日期
        String qsrqStr = getIntent().getStringExtra("qsrq") != null ? getIntent().getStringExtra("qsrq") : "";
        // 结束日期
        String jsrqStr = getIntent().getStringExtra("jsrq") != null ? getIntent().getStringExtra("jsrq") : "";
        // 培训单位名称
        String pxdwStr = getIntent().getStringExtra("pxdwmc") != null ? getIntent().getStringExtra("pxdwmc") : "";
        // 培训内容
        String pxnrStr = getIntent().getStringExtra("pxnr") != null ? getIntent().getStringExtra("pxnr") : "";
        // 培训备注
        String pxbzStr = getIntent().getStringExtra("pxbz") != null ? getIntent().getStringExtra("pxbz") : "";
        // 培训结果
        String pxjgStr = getIntent().getStringExtra("pxjg") != null ? getIntent().getStringExtra("pxjg") : "";


        qsrq = (TextView) findViewById(R.id.qsrq);
        jsrq = (TextView) findViewById(R.id.jsrq);

        qsrq.setOnClickListener(this);
        jsrq.setOnClickListener(this);

        pxnr = (EditText) findViewById(R.id.pxnr);
        pxdw = (EditText) findViewById(R.id.pxdw);
        pxjg = (EditText) findViewById(R.id.pxjg);
        pxbz = (EditText) findViewById(R.id.pxbz);

        Button back = (Button) findViewById(R.id.back);
        Button bc = (Button) findViewById(R.id.bc);
        back.setOnClickListener(this);
        bc.setOnClickListener(this);
        Button sc = (Button) findViewById(R.id.sc);
        sc.setOnClickListener(this);

        if (flag == 0) {
            sc.setVisibility(View.GONE);
        } else {
            sc.setVisibility(View.VISIBLE);
        }
        qsrq.setText(qsrqStr);
        jsrq.setText(jsrqStr);
        pxnr.setText(pxnrStr);
        pxdw.setText(pxdwStr);
        pxjg.setText(pxjgStr);
        pxbz.setText(pxbzStr);
    }


    private void postBc() {
        String qsrq1 = qsrq.getText().toString().trim();
        String jsrq1 = jsrq.getText().toString().trim();
        String pxdw1 = pxdw.getText().toString().trim();
        String pxjg1 = pxjg.getText().toString().trim();
        String pxbz1 = pxbz.getText().toString().trim();
        String pxnr1 = pxnr.getText().toString().trim();
        if(DateUtil.getSubtractDate(qsrq1,jsrq1) <= 0){
            commonUtil.shortToast("日期填写有误！");
            return;
        }
        if (TextUtils.isEmpty(qsrq1)) {
            commonUtil.shortToast("起始时间不能为空！");
            return;
        }
        if (TextUtils.isEmpty(jsrq1)) {
            commonUtil.shortToast("结束时间不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pxdw1)) {
            commonUtil.shortToast("培训单位不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pxnr1)) {
            commonUtil.shortToast("培训内容不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pxjg1)) {
            commonUtil.shortToast("培训结果不能为空！");
            return;
        }

        if (flag == 0) {
            postDataXz(user.getUserId(), qsrq1, jsrq1, pxdw1,  pxnr1,  pxjg1, pxbz1);
        } else {
            postData(pxid,qsrq1, jsrq1, pxdw1,  pxnr1,  pxjg1, pxbz1);
        }
    }


    private void postDataXz(String userid, String qsrqStr, String jsrqStr, String pxdwStr, String pxnrStr, String pxjgStr, String pxbzStr) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyPxjlInfoDetail.this, "正在查询中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postDataXzResult((String) arg0);
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
        xwzxDAL.doXzPxjl(userid, qsrqStr, jsrqStr, pxdwStr,  pxnrStr,  pxjgStr, pxbzStr,
                callBack);
    }

    private void postDataXzResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }

    private void postData(String pxid, String qsrqStr, String jsrqStr, String pxdwStr, String pxnrStr, String pxjgStr, String pxbzStr) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyPxjlInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyPxjl(pxid, qsrqStr, jsrqStr, pxdwStr,  pxnrStr,  pxjgStr, pxbzStr,
                callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bc:

                doBcTs();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.qsrq:
                if (pvCustomTime != null) {
                    pvCustomTime.show(); //弹出自定义时间选择器
                }
                break;
            case R.id.jsrq:
                if (pvCustomTime1 != null) {
                    pvCustomTime1.show(); //弹出自定义时间选择器
                }
                break;
            case R.id.sc:
                doSc();
                break;
            default:
                break;
        }
    }

    /**
     * 保存的提示框
     */
    public void doBcTs() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("保存记录");
        pMsg.setText("确定要保存这条记录吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                postBc();
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
     * 删除的提示框
     */
    public void doSc() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("删除");
        pMsg.setText("确定删除此条记录吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                postSc();
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
     * delete
     */
    private void postSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyPxjlInfoDetail.this, "正在删除中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postScResult((String) arg0);
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
        xwzxDAL.doScPxjl(pxid,callBack);
    }

    private void postScResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }

    /**
     * 先初始化它
     */
    private void initQsrqCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                qsrq.setText(DateUtil.getSjxzqTime(date));
            }
        }).setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDividerColor(Color.BLACK)//设置分割线的颜色
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
    }

    /**
     * 先初始化它
     */
    private void initJsrqCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                jsrq.setText(DateUtil.getSjxzqTime(date));
            }
        }).setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDividerColor(Color.BLACK)//设置分割线的颜色
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
    }

}
