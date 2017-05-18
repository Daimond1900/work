package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基本信息
 * Created by 1900 on 2017/4/20.
 */
public class ModifyPersonJcInfo extends BaseActivity implements View.OnClickListener {
    private EditText xm, byyx, sg, tz, zwjs, jtzz, gddh, sjhm, dzyj, zyms, jkzk, sl, yzbm;
    private TextView csrq;
    private TextView byrq;
    private Spinner mz, zzmm, sxzy, jsjsp, xb, jyzt, szdq, hyzk;
    private Spinner xl;
    private XwzxDAL xwzxDAL;
    private String xlId, zzmmId, mzId, sxzyId, jsjspId, xbId, jyztId, sfz, szdqId, hyzkStr;
    private Dialog proDialog;
    private List<Map<String, String>> xbs;
    private List<Map<String, String>> hyzks;
    private TimePickerView pvCustomTime, pvCustomTime1;
    private String xueliStr, mzStr, zzmmStr, sxzyStr, jsjspStr, szdqStr;
    /*--------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xggrjcxx);
        initView();
    }

    /**
     * 先初始化它
     */
    private void initCsrqCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                csrq.setText(DateUtil.getSjxzqTime(date));
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
    private void initByrqCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 31);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 0, 1);
        //时间选择器 ，自定义布局
        pvCustomTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                byrq.setText(DateUtil.getSjxzqTime(date));
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
                postBcInfo();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBc:
                doBcTs();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.csrq:
                if (pvCustomTime != null) {
                    pvCustomTime.show(); //弹出自定义时间选择器
                }
                break;
            case R.id.byrq:
                if (pvCustomTime1 != null) {
                    pvCustomTime1.show(); //弹出自定义时间选择器
                }
                break;
            default:
                break;
        }
    }

    private void postBcInfo() {
        String xmStr = xm.getText().toString().trim();  //姓名
        String sgStr = sg.getText().toString().trim();
        String tzStr = tz.getText().toString().trim();
        String slStr = sl.getText().toString().trim();
        String zymsStr = zyms.getText().toString().trim();
        String jkzkStr = jkzk.getText().toString().trim();
        String byyxStr = byyx.getText().toString().trim();
        String byrqStr = byrq.getText().toString().trim();
        String zwjsStr = zwjs.getText().toString().trim();
        String jtzzStr = jtzz.getText().toString().trim();

        String yzbmStr = yzbm.getText().toString().trim();
        String gddhStr = gddh.getText().toString().trim();
        String sjhmStr = sjhm.getText().toString().trim();
        String dzyjStr = dzyj.getText().toString().trim();
        String csrqStr = csrq.getText().toString().trim();

        if (TextUtils.isEmpty(xmStr)) {
            commonUtil.shortToast("姓名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(jkzkStr)) {
            commonUtil.shortToast("健康状况不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sjhmStr)) {
            commonUtil.shortToast("手机号码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(csrqStr)) {
            commonUtil.shortToast("出生日期不能为空！");
            return;
        }
        postData(
                user.getUserId(), xmStr, xbId, sfz, xlId, mzId, zzmmId, sxzyId, zymsStr, csrqStr, hyzkStr, jkzkStr
                , byyxStr, sgStr, tzStr, slStr, jsjspId, byrqStr, jyztId, zwjsStr, jtzzStr, yzbmStr, szdqId, gddhStr, sjhmStr
                , dzyjStr
        );
    }


    /**
     * 提交基本信息数据
     */
    private void postData(String useId, String xm, String xbId, String sfz, String xlId,
                          String mzId, String zzmmId, String sxzyId, String zymsStr, String csrq, String hyzkStr
            , String jkzkStr, String byyxStr, String sgStr, String tzStr, String slStr, String jsjspId, String byrqStr
            , String jyztId, String zwjsStr, String jtzzStr, String yzbmStr, String szdqStr, String gddhStr, String sjhmStr
            , String dzyjStr
    ) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyPersonJcInfo.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyJbxx(
                useId, xm, xbId, sfz, xlId, mzId, zzmmId, sxzyId, zymsStr, csrq, hyzkStr, jkzkStr
                , byyxStr, sgStr, tzStr, slStr, jsjspId, byrqStr, jyztId, zwjsStr, jtzzStr, yzbmStr, szdqStr
                , gddhStr, sjhmStr
                , dzyjStr,
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
        xbs = xwzxDAL.queryXB();  // 性别
        List<Map<String, String>> jyzts = xwzxDAL.queryJYZT();
        hyzks = xwzxDAL.queryHyzk();  // 就业状态
        xb = (Spinner) findViewById(R.id.xb);           // 性别
        jyzt = (Spinner) findViewById(R.id.jyzt);       // 就业状态
        hyzk = (Spinner) findViewById(R.id.hyzk);       // 婚姻状态
        xl = (Spinner) findViewById(R.id.xl);           // 学历
        mz = (Spinner) findViewById(R.id.mz);           // 民族
        zzmm = (Spinner) findViewById(R.id.zzmm);       // 政策面貌
        sxzy = (Spinner) findViewById(R.id.sxzy);       // 所学专业
        jsjsp = (Spinner) findViewById(R.id.jsjsp);     // 计算机水平
        initXb(xbs);
        initJyzt(jyzts);
        initHyzk(hyzks);


        jkzk = (EditText) findViewById(R.id.jkzk);      //
        sl = (EditText) findViewById(R.id.sl);      //
        zyms = (EditText) findViewById(R.id.zyms);      //
        xm = (EditText) findViewById(R.id.xm);      //  姓名

        csrq = (TextView) findViewById(R.id.csrq);
        csrq.setOnClickListener(this);
        initCsrqCustomTimePicker();

        byyx = (EditText) findViewById(R.id.byyx);
        sg = (EditText) findViewById(R.id.sg);
        tz = (EditText) findViewById(R.id.tz);

        byrq = (TextView) findViewById(R.id.byrq);
        byrq.setOnClickListener(this);
        initByrqCustomTimePicker();


        zwjs = (EditText) findViewById(R.id.zwjs);
        jtzz = (EditText) findViewById(R.id.jtzz);
        gddh = (EditText) findViewById(R.id.gddh);
        sjhm = (EditText) findViewById(R.id.sjhm);
        dzyj = (EditText) findViewById(R.id.dzyj);
        TextView title = (TextView) findViewById(R.id.title);
        szdq = (Spinner) findViewById(R.id.szdq);
        yzbm = (EditText) findViewById(R.id.yzbm);
        Button btBc = (Button) findViewById(R.id.btBc);
        Button back = (Button) findViewById(R.id.back);
        btBc.setOnClickListener(this);
        back.setOnClickListener(this);

        int pdbt = getIntent().getIntExtra("pdbt", 0);
        if (pdbt == 1) {
            title.setText("修改基本信息");
        } else if (pdbt == 0) {
            title.setText("新增基本信息");
        }
        loadData(); /*加载数据*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadSpnerDate("AAB301");    //所在区域
                loadSpnerDate("AAC183");
                loadSpnerDate("AAC011");
                loadSpnerDate("AAC005");
                loadSpnerDate("AAC024");
                loadSpnerDate("AAC179");
            }
        }, 1000);
    }


    /**
     * 加载基本信息数据
     */
    private void loadData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyPersonJcInfo.this, "正在查询中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                queryResult((String) arg0);
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
        xwzxDAL.doJLgejbxx(callBack);
    }

    private void queryResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                setPage(DataConvert.toConvertStringMap(json, "row"));
            } else {
                commonUtil.shortToast("系统繁忙，查询失败！");
            }
        }
    }

    /**
     * 设置页面数据
     *
     * @param map   页面数据
     */
    private void setPage(Map<String, String> map) {
        xm.setText(commonUtil.getMapValue(map, "aac003"));
        sg.setText(commonUtil.getMapValue(map, "aac034"));
        tz.setText(commonUtil.getMapValue(map, "aac035"));
        csrq.setText(commonUtil.getMapValue(map, "aac006"));
        byyx.setText(commonUtil.getMapValue(map, "byyx"));
        byrq.setText(commonUtil.getMapValue(map, "bysj"));
        zwjs.setText(commonUtil.getMapValue(map, "zwjs"));
        jtzz.setText(commonUtil.getMapValue(map, "aac026"));
        gddh.setText(commonUtil.getMapValue(map, "aae005"));
        sjhm.setText(commonUtil.getMapValue(map, "acb501"));
        dzyj.setText(commonUtil.getMapValue(map, "aae159"));
        zyms.setText(commonUtil.getMapValue(map, "zyms"));
        jkzk.setText(commonUtil.getMapValue(map, "jkzk"));
        sl.setText(commonUtil.getMapValue(map, "aac036"));
        yzbm.setText(commonUtil.getMapValue(map, "aae007"));
        sfz = commonUtil.getMapValue(map, "aac002");

        xueliStr = commonUtil.getMapValue(map, "aac011");
        szdqStr = commonUtil.getMapValue(map, "aab301");
        mzStr = commonUtil.getMapValue(map, "aac005");
        zzmmStr = commonUtil.getMapValue(map, "aac024");
        sxzyStr = commonUtil.getMapValue(map, "aac183");
        jsjspStr = commonUtil.getMapValue(map, "aac179");

        for (int i = 0; i < xbs.size(); i++) {
            if (commonUtil.getMapValue(map, "aac004").equals(xbs.get(i).get("zdmc"))) {
                xb.setSelection(i);
            }
        }

        for (int i = 0; i < hyzks.size(); i++) {
            if (commonUtil.getMapValue(map, "hyzk").equals(hyzks.get(i).get("zdmc"))) {
                hyzk.setSelection(i);
            }
        }
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
                    case "AAC011":
                        initXueLi(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC005":
                        initMz(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC024":
                        initZzmm(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC183":
                        initSxzy(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC179":
                        initJsjsp(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAB301":
                        initSzdq(DataConvert.toConvertStringList(json, "table"));
                        break;
                }
            }
        }
    }


    /**
     * 就业状态下拉框
     *
     * @param xbs 就业状态
     */
    private void initJyzt(final List<Map<String, String>> xbs) {
        if (xbs != null && xbs.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, xbs, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jyzt.setPrompt("请选择就业状态");
            jyzt.setAdapter(adapter);
            jyzt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    jyztId = xbs.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    /**
     * 婚姻状况下拉框
     *
     * @param xbs   婚姻状况
     */
    private void initHyzk(final List<Map<String, String>> xbs) {
        if (xbs != null && xbs.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, xbs, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            hyzk.setPrompt("请选择婚姻状态");
            hyzk.setAdapter(adapter);
            hyzk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    hyzkStr = xbs.get(arg2).get("zdmc");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    /**
     * 区域下拉框
     *
     * @param xbs   区域
     */
    private void initSzdq(final List<Map<String, String>> xbs) {
        if (xbs != null && xbs.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, xbs, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            szdq.setPrompt("请选择所在区域");
            szdq.setAdapter(adapter);
            szdq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    szdqId = xbs.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            for (int i = 0; i < xbs.size(); i++) {
                if (szdqStr.equals("江苏省扬州市" + xbs.get(i).get("zdmc"))) {
                    szdq.setSelection(i);
                }
            }
        }
    }


    /**
     * 性别下拉框
     * @param xbs   性别
     */
    private void initXb(final List<Map<String, String>> xbs) {
        if (xbs != null && xbs.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, xbs, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            xb.setPrompt("请选择性别");
            xb.setAdapter(adapter);
            xb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    xbId = xbs.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    /**
     * 学历
     */
    private void initXueLi(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            xl.setPrompt("请选择学历水平");
            xl.setAdapter(adapter);
            xl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    xlId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (xueliStr.equals(list.get(i).get("zdmc"))) {
                    xl.setSelection(i);
                }
            }
        }
    }

    /**
     * 民族
     */
    private void initMz(final List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mz.setPrompt("请选择民族");
            mz.setAdapter(adapter);
            mz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    mzId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (mzStr.equals(list.get(i).get("zdmc"))) {
                    mz.setSelection(i);
                }
            }
        }
    }

    /**
     * 政治面貌
     */
    private void initZzmm(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zzmm.setPrompt("请选择政治面貌");
            zzmm.setAdapter(adapter);
            zzmm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    zzmmId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (zzmmStr.equals(list.get(i).get("zdmc"))) {
                    zzmm.setSelection(i);
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
            for (int i = 0; i < list.size(); i++) {
                if (sxzyStr.equals(list.get(i).get("zdmc"))) {
                    sxzy.setSelection(i);
                }
            }
        }
    }

    /**
     * 计算机水平
     */
    private void initJsjsp(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jsjsp.setPrompt("请选择政治面貌");
            jsjsp.setAdapter(adapter);
            jsjsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    jsjspId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (jsjspStr.equals(list.get(i).get("zdmc"))) {
                    jsjsp.setSelection(i);
                }
            }
        }
    }


}
