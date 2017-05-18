package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
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
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *技术专长 修改
 */
public class ModifyJszcInfoDetail extends BaseActivity implements View.OnClickListener {
    private TextView zsbfrq;
    private EditText csnxEd;
    private Spinner jsmc, jsdj;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private TimePickerView pvCustomTime;
    private int flag;
    private String jszcIdStr, jszcdj,jsmcId,jsdjId,jszcdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyjszcinfodetail);

        flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {  //新增
            ((TextView) findViewById(R.id.textView2)).setText("新增技术专长");
        }

        initView();
    }

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        loadSpnerDate("ACA111");      // 技术名称
        loadSpnerDate("AAC015");      // 技术等级名称

        initQsrqCustomTimePicker();

        // 技术编号
        jszcIdStr = getIntent().getStringExtra("jsid") != null ? getIntent().getStringExtra("jsid") : "";
        // 技术专长代码
        jszcdm = getIntent().getStringExtra("jszcdm") != null ? getIntent().getStringExtra("jszcdm") : "";
        // 技术专长等级
        jszcdj = getIntent().getStringExtra("jszcdj") != null ? getIntent().getStringExtra("jszcdj") : "";
        // 颁发时间
        String bfsj = getIntent().getStringExtra("bfsj") != null ? getIntent().getStringExtra("bfsj") : "";
        // 从事年限
        String csnx = getIntent().getStringExtra("csnx") != null ? getIntent().getStringExtra("csnx") : "";

        jsmc = (Spinner) findViewById(R.id.jsmc);
        jsdj = (Spinner) findViewById(R.id.jsdj);
        zsbfrq = (TextView) findViewById(R.id.zsbfrq);
        zsbfrq.setOnClickListener(this);
        csnxEd = (EditText) findViewById(R.id.csnx);
        zsbfrq.setText(bfsj);
        csnxEd.setText(csnx);

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
                    case "ACA111"://技术名称
                        initJsmc(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAC015"://技术等级名称
                        initJsdj(DataConvert.toConvertStringList(json, "table"));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 所学专业
     */
    private void initJsmc(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jsmc.setPrompt("请选择技术名称");
            jsmc.setAdapter(adapter);
            jsmc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    jsmcId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (jszcdm.equals(list.get(i).get("zdmc"))) {
                    jsmc.setSelection(i);
                }
            }
        }
    }

    private void initJsdj(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jsdj.setPrompt("请选择技术等级");
            jsdj.setAdapter(adapter);
            jsdj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    jsdjId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (jszcdj.equals(list.get(i).get("zdmc"))) {
                    jsdj.setSelection(i);
                }
            }
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
                zsbfrq.setText(DateUtil.getSjxzqTime(date));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bc:
                doBcTs();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.zsbfrq:
                if (pvCustomTime != null) {
                    pvCustomTime.show(); //弹出自定义时间选择器
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

    private void postBc() {
        String nx = csnxEd.getText().toString().trim();
        String sj = zsbfrq.getText().toString().trim();


        if (TextUtils.isEmpty(nx)) {
            commonUtil.shortToast("从事年限不能为空！");
            return;
        }
        if (TextUtils.isEmpty(sj)) {
            commonUtil.shortToast("证书颁发日期不能为空！");
            return;
        }

        if(flag == 0){
            postDataXz(user.getUserId(),jsmcId,jsdjId,sj,nx);
        }else {
            postData(jszcIdStr,jsmcId,jsdjId,sj,nx);
        }
    }

    private void postData(String jszcIdStr, String jsmcId, String jsdjId, String sj, String nx) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJszcInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyJszc(jszcIdStr,jsmcId,jsdjId,sj,nx,
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

    private void postDataXz(String userId, String jsmcId, String jsdjId, String sj, String nx) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJszcInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doXzJszc(userId, jsmcId,jsdjId,sj,nx,
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

    private void postSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyJszcInfoDetail.this, "正在删除中,请稍后...");
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
        xwzxDAL.doScJszc(jszcIdStr,callBack);
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

}
