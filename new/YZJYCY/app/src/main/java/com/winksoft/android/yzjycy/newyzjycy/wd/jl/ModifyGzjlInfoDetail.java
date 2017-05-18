package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;


/**
 * 工作经历 修改
 */
public class ModifyGzjlInfoDetail extends BaseActivity implements View.OnClickListener {

    private TextView qsrq, jsrq, dwmc, gzbz;
    private Spinner szzw;
    private String gzidStr, szzwId, szzwStr;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private TimePickerView pvCustomTime, pvCustomTime1;
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifygzjlinfodetail);

        flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {  //新增
            ((TextView) findViewById(R.id.textView2)).setText("新增工作经历");
        }
        initView();
    }

    private void postBc() {
        String qsrqStr = qsrq.getText().toString().trim();
        String jsrqStr = jsrq.getText().toString().trim();
        String dwmcStr1 = dwmc.getText().toString().trim();
        String gzbzStr1 = gzbz.getText().toString().trim();

        if (DateUtil.getSubtractDate(qsrqStr, jsrqStr) <= 0) {
            commonUtil.shortToast("日期填写有误！");
            return;
        }
        if (TextUtils.isEmpty(qsrqStr)) {
            commonUtil.shortToast("起始时间不能为空！");
            return;
        }
        if (TextUtils.isEmpty(jsrqStr)) {
            commonUtil.shortToast("结束时间不能为空！");
            return;
        }
        if (TextUtils.isEmpty(dwmcStr1)) {
            commonUtil.shortToast("单位名称不能为空！");
            return;
        }
        if (flag == 0) {
            postDataXz(user.getUserId(), qsrqStr, jsrqStr, dwmcStr1, szzwId, gzbzStr1);
        } else {
            postData(gzidStr, qsrqStr, jsrqStr, dwmcStr1, szzwId, gzbzStr1);
        }
    }

    private void postDataXz(String userid, String qsrqStr, String jsrqStr, String dwmcStr1, String szzwId, String gzbzStr1) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyGzjlInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doXzGzjl(userid, qsrqStr, jsrqStr, dwmcStr1, szzwId, gzbzStr1,
                callBack);
    }

    private void postDataXzResult(String json) {
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

    private void postData(String gzidStr, String qsrqStr, String jsrqStr, String dwmcStr1, String szzwId, String gzbzStr1) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyGzjlInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyGzjl(gzidStr, qsrqStr, jsrqStr, dwmcStr1, szzwId, gzbzStr1,
                callBack);
    }

    private void postResult(String json) {
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

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        szzw = (Spinner) findViewById(R.id.szzw);   // 所在职位
        qsrq = (TextView) findViewById(R.id.qsrq);
        jsrq = (TextView) findViewById(R.id.jsrq);

        qsrq.setOnClickListener(this);
        jsrq.setOnClickListener(this);


        loadSpnerDate("ACA111");      // 所在职位

        initQsrqCustomTimePicker();
        initJsrqCustomTimePicker();
        // 工作编号
        gzidStr = getIntent().getStringExtra("gzid") != null ? getIntent().getStringExtra("gzid") : "";
        // 起始日期
        String qsrqStr = getIntent().getStringExtra("qsrq") != null ? getIntent().getStringExtra("qsrq") : "";
        // 结束日期
        String jsrqStr = getIntent().getStringExtra("jsrq") != null ? getIntent().getStringExtra("jsrq") : "";
        // 单位名称
        String dwmcStr = getIntent().getStringExtra("dwmc") != null ? getIntent().getStringExtra("dwmc") : "";
        // 所在职位
        szzwStr = getIntent().getStringExtra("szzw") != null ? getIntent().getStringExtra("szzw") : "";
        // 工作备注
        String gzbzStr = getIntent().getStringExtra("gzbz") != null ? getIntent().getStringExtra("gzbz") : "";

        dwmc = (TextView) findViewById(R.id.dwmc);
        dwmc.setText(dwmcStr);

        gzbz = (TextView) findViewById(R.id.gzbz);
        gzbz.setText(gzbzStr);

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

    private void postSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyGzjlInfoDetail.this, "正在删除中,请稍后...");
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
        xwzxDAL.doScGzjl(gzidStr, callBack);
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


    private void loadSpnerDate(final String str) {

        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                querySpnerResult((String) arg0, str);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        };
        xwzxDAL.doGetParams(str, callBack);
    }

    private void querySpnerResult(String json, String str) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("true")) {
                switch (str) {
                    case "ACA111":
                        initSzzw(DataConvert.toConvertStringList(json, "table"));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 所在职位
     */
    private void initSzzw(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            szzw.setPrompt("请选择所在职位");
            szzw.setAdapter(adapter);
            szzw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    szzwId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (szzwStr.equals(list.get(i).get("zdmc"))) {
                    szzw.setSelection(i);
                }
            }
        }
    }
}
