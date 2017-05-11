package com.winksoft.android.yzjycy.ui.pxxx;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.bigkoo.pickerview.TimePickerView;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.IDCard;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BmInfoSureActivity extends BaseActivity implements OnClickListener {
    private EditText et_zsxm, et_sfz, et_sjh;
    private TextView et_csrq;
    private int xb = 0; // 性别
    Dialog proDialog;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private String zsxm = "", sfz = "";
    private String flag = "";
    private String realname, sex, idcard, sjhm, csrq,pSjhm,pCsrq,pXb;
    private TimePickerView pvCustomTime;
    private RadioGroup xbRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bminfo_activity);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        flag = getIntent().getStringExtra("flag") != null && !"".equals(getIntent().getStringExtra("flag")) ? getIntent().getStringExtra("flag") : "";
        realname = getIntent().getStringExtra("lr") != null && !"".equals(getIntent().getStringExtra("lr")) ? getIntent().getStringExtra("lr") : "";
        sex = getIntent().getStringExtra("xb") != null && !"".equals(getIntent().getStringExtra("xb")) ? getIntent().getStringExtra("xb") : "";
        idcard = getIntent().getStringExtra("sfz") != null && !"".equals(getIntent().getStringExtra("sfz")) ? getIntent().getStringExtra("sfz") : "";
        sjhm = getIntent().getStringExtra("sjhm") != null && !"".equals(getIntent().getStringExtra("sjhm")) ? getIntent().getStringExtra("sjhm") : "";
        csrq = getIntent().getStringExtra("csrq") != null && !"".equals(getIntent().getStringExtra("csrq")) ? getIntent().getStringExtra("csrq") : "";
        initView();
    }

    private void initView() {
        initQsrqCustomTimePicker();
        TextView title = (TextView) findViewById(R.id.title);
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        Button  btn_baom = (Button) findViewById(R.id.btn_baom);
        btn_baom.setOnClickListener(this);
        xbRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        xbRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.man) {
                    xb = 1;
                } else {
                    xb = 2;
                }
            }
        });
        et_zsxm = (EditText) findViewById(R.id.et_zsxm);
        et_sfz = (EditText) findViewById(R.id.et_sfz);
        et_sjh = (EditText) findViewById(R.id.et_sjh);  // 手机号
        et_csrq = (TextView) findViewById(R.id.et_csrq);// 出生日期
        et_csrq.setOnClickListener(this);

        if ("1".equals(flag)) {
            title.setText("完善个人信息");
            btn_baom.setText("提交完善信息");
            et_zsxm.setText(realname);
            et_sfz.setText(idcard);
            et_sjh.setText(sjhm);       // 手机号
            et_csrq.setText(csrq);      // 出生日期
            if ("1".equals(sex)) {
                ((RadioButton) xbRadioGroup.getChildAt(0)).setChecked(true);
            } else if ("2".equals(sex)) {
                ((RadioButton) xbRadioGroup.getChildAt(1)).setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.btn_baom:
                if ("".equals(et_zsxm.getText().toString().trim())) {
                    commonUtil.shortToast("真实姓名不能为空！");
                    return;
                }
                if ("".equals(et_sfz.getText().toString().trim())) {
                    commonUtil.shortToast("身份证不能为空！");
                    return;
                }
                if ("".equals(et_sjh.getText().toString().trim())) {
                    commonUtil.shortToast("手机号码不能为空！");
                    return;
                }
                if ("".equals(et_csrq.getText().toString().trim())) {
                    commonUtil.shortToast("出生日期不能为空！");
                    return;
                }
                try {
                    if (!"".equals(IDCard.IDCardValidate(et_sfz.getText().toString().trim()))) {
                        commonUtil.shortToast("身份证输入有误！");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (et_sjh.getText().toString().trim().length() != 11) {
                    commonUtil.shortToast("手机号码位数不对！");
                    return;
                }
                if (xb == 0) {
                    xb = 1;
                }
                zsxm = et_zsxm.getText().toString().trim();
                sfz = et_sfz.getText().toString().trim();
                pSjhm = et_sjh.getText().toString().trim();
                pCsrq = et_csrq.getText().toString().trim();
                doCompletePersonalInfo();

                break;
            case R.id.et_csrq:
                if (pvCustomTime != null) {
                    pvCustomTime.show(); //弹出自定义时间选择器
                }
                break;
            default:
                break;
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
                et_csrq.setText(DateUtil.getSjxzqTime(date));
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


    private void doCompletePersonalInfo() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        BmInfoSureActivity.this, "信息提交中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doCompletePersonalInfoResult((String) arg0);
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
        xwzxDAL.doCompletePersonalInfo(user.getUserId(), zsxm, sfz, xb,pSjhm,pCsrq, callBack);
    }

    /**
     * @param json : success result
     */
    private void doCompletePersonalInfoResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                BmInfoSureActivity.this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试！");
        }
    }
}
