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
 * 教育经历 修改
 * Created by 1900 on 2017/4/25.
 */

public class ModifyJyjlInfoDetail extends BaseActivity implements View.OnClickListener {
    private TextView qsrq, jsrq, byyx, xxbz;
    private Spinner sxzy, zcdj, hdzc;
    private String jyidStr, sxzyId, sxzyStr, zcdjStr, zcdjId, hdzcStr, hdzcId;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private TimePickerView pvCustomTime, pvCustomTime1;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyjyjlinfodetail);

        flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {  //新增
            ((TextView) findViewById(R.id.textView2)).setText("新增教育经历");
        }

        initView();
    }

    private void postBc() {
        String qsrqStr = qsrq.getText().toString().trim();
        String jsrqStr = jsrq.getText().toString().trim();
        String byyxStr = byyx.getText().toString().trim();
        String xxbzStr = xxbz.getText().toString().trim();

        if (TextUtils.isEmpty(qsrqStr)) {
            commonUtil.shortToast("起始日期不能为空！");
            return;
        }
        if (TextUtils.isEmpty(jsrqStr)) {
            commonUtil.shortToast("结束日期不能为空！");
            return;
        }
        if (TextUtils.isEmpty(byyxStr)) {
            commonUtil.shortToast("毕业院校不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sxzyId)) {
            commonUtil.shortToast("所学专业不能为空！");
            return;
        }
        if (DateUtil.getSubtractDate(qsrqStr, jsrqStr) <= 0) {
            commonUtil.shortToast("日期填写有误！");
            return;
        }
        if (flag == 0) {
            postDataXz(user.getUserId(), qsrqStr, jsrqStr, byyxStr, sxzyId, zcdjId, hdzcId, xxbzStr);
        } else {
            postData(jyidStr, qsrqStr, jsrqStr, byyxStr, sxzyId, zcdjId, hdzcId, xxbzStr);
        }
    }

    private void postDataXz(String userid, String qsrqStr, String jsrqStr, String byyxStr, String sxzyStr, String zcdjStr, String hdzcStr, String xxbzStr) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJyjlInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doXzJyjl(userid, qsrqStr, jsrqStr, byyxStr, sxzyStr, zcdjStr, hdzcStr, xxbzStr,
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

    private void postData(String jyidStr, String qsrqStr, String jsrqStr, String byyxStr, String sxzyStr, String zcdjStr, String hdzcStr, String xxbzStr) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJyjlInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyJyjl(jyidStr, qsrqStr, jsrqStr, byyxStr, sxzyStr, zcdjStr, hdzcStr, xxbzStr,
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

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        loadSpnerDate("AAC014");      // 获得职称
        loadSpnerDate("AAC183");      // 所学专业
        loadSpnerDate("AAC015");      // 职称等级

        initQsrqCustomTimePicker();
        initJsrqCustomTimePicker();
        // 教育编号
        jyidStr = getIntent().getStringExtra("jyid") != null ? getIntent().getStringExtra("jyid") : "";
        // 起始日期
        String qsrqStr = getIntent().getStringExtra("qsrq") != null ? getIntent().getStringExtra("qsrq") : "";
        // 结束日期
        String jsrqStr = getIntent().getStringExtra("jsrq") != null ? getIntent().getStringExtra("jsrq") : "";
        // 毕业院校
        String byyxStr = getIntent().getStringExtra("byyx") != null ? getIntent().getStringExtra("byyx") : "";
        // 所学专业
        sxzyStr = getIntent().getStringExtra("sxzy") != null ? getIntent().getStringExtra("sxzy") : "";
        // 学习备注
        String xxbzStr = getIntent().getStringExtra("xxbz") != null ? getIntent().getStringExtra("xxbz") : "";
        // 获得职称
        hdzcStr = getIntent().getStringExtra("hdzc") != null ? getIntent().getStringExtra("hdzc") : "";
        // 职称等级
        zcdjStr = getIntent().getStringExtra("zcdj") != null ? getIntent().getStringExtra("zcdj") : "";

        qsrq = (TextView) findViewById(R.id.qsrq);
        jsrq = (TextView) findViewById(R.id.jsrq);

        qsrq.setOnClickListener(this);
        jsrq.setOnClickListener(this);

        byyx = (TextView) findViewById(R.id.byyx);
        sxzy = (Spinner) findViewById(R.id.sxzy);
        xxbz = (TextView) findViewById(R.id.xxbz);
        hdzc = (Spinner) findViewById(R.id.hdzc);
        zcdj = (Spinner) findViewById(R.id.zcdj);

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
        byyx.setText(byyxStr);
        xxbz.setText(xxbzStr);
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
     * 删除
     */
    private void postSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJyjlInfoDetail.this, "正在删除中,请稍后...");
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
        xwzxDAL.doScJyjl(jyidStr, callBack);
    }

    /**
     * 删除结果
     */
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
                    case "AAC183":
                        initSxzy(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC015":
                        initZcdj(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC014":
                        initHdzc(DataConvert.toConvertStringList(json, "table"));
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void initHdzc(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hdzc.setPrompt("请选择职称");
            hdzc.setAdapter(adapter);
            hdzc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    hdzcId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (hdzcStr.equals(list.get(i).get("zdmc"))) {
                    hdzc.setSelection(i);
                }
            }
        }
    }

    /**
     * 所学专业
     */
    private void initSxzy(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sxzy.setPrompt("请选择所学专业");
            sxzy.setAdapter(adapter);
            sxzy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    sxzyId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
//            sxzySpnerList = list;
            for (int i = 0; i < list.size(); i++) {
                if (sxzyStr.equals(list.get(i).get("zdmc"))) {
                    sxzy.setSelection(i);
                }
            }
        }
    }

    /**
     * 职称等级
     */
    private void initZcdj(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zcdj.setPrompt("请选择职称等级");
            zcdj.setAdapter(adapter);
            zcdj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    zcdjId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (zcdjStr.equals(list.get(i).get("zdmc"))) {
                    zcdj.setSelection(i);
                }
            }
        }
    }


}
