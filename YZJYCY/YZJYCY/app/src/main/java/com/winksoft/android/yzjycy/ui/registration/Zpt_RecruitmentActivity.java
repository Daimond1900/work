package com.winksoft.android.yzjycy.ui.registration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.MenuActivity;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.data.QzzDAL;
import com.winksoft.android.yzjycy.data.RecruitmentDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.yifeng.nox.android.http.http.AjaxCallBack;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * ClassName:RecruitmentActivity
 * Description：招聘登记
 *
 * @author Administrator
 *         Date：2012-8-28
 */
public class Zpt_RecruitmentActivity extends BaseActivity {
    private static final String TAG = "Zpt_Recruitment";
    private EditText postEdt, numberEdt, wageEdt, requireEdt, zpnxEdt, zpvxEdt, jzEdt, ygzEdt, gzsmEdt;
    private Button backBtn, chooseBtn, submitBtn, resetBtn;
    private Button daysSpi;
    private String[] spinnerData = {"一个月", "半个月", "两个月"};// flag:0,1,2
    private String days = "一个月";
    private String job = "", wage = "", require = "", zpnx = "", zpvx = "", jz = "", ygz = "", gzsm = "", daysFlag = "";
    private ProgressDialog progressDialog;
    private QyDAL qyDAL;
    private Intent qz;
    private String tag = "";
    private String returnJson;
    private Spinner work_area;
    private List<Map<String, String>> areas;
    Dialog proDialog;
    private CommonUtil commonUtil;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_recruitment);
        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
        qz = this.getIntent();
        tag = qz.getStringExtra("flag");

        gzsmEdt = (EditText) findViewById(R.id.gwsmnumberEdt);
        postEdt = (EditText) findViewById(R.id.postEdt);
        postEdt.setText(qz.getStringExtra("post"));
        gzsmEdt.setText(qz.getStringExtra("post"));
        wageEdt = (EditText) findViewById(R.id.wageEdt);
        requireEdt = (EditText) findViewById(R.id.requireEdt);
        zpnxEdt = (EditText) findViewById(R.id.zpnxnumberEdt);
        zpvxEdt = (EditText) findViewById(R.id.zpvxnumberEdt);
        jzEdt = (EditText) findViewById(R.id.jznumberEdt);
        ygzEdt = (EditText) findViewById(R.id.ygzEdt);
        work_area = (Spinner) findViewById(R.id.spn_area);//工作地区


        MyClick myclick = new MyClick();
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(myclick);
        chooseBtn = (Button) findViewById(R.id.chooseBtn);
        chooseBtn.setOnClickListener(myclick);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(myclick);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(myclick);

        daysSpi = (Button) findViewById(R.id.daysSpi);
        daysSpi.setOnClickListener(myclick);
        daysSpi.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        initArea();
    }

    private class MyClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    Zpt_RecruitmentActivity.this.finish();
                    break;
                case R.id.chooseBtn:
                    Intent intent = new Intent(Zpt_RecruitmentActivity.this, Zpt_ChooseJobsActivity1.class);
                    startActivity(intent);
                    break;
                case R.id.submitBtn:
                    job = postEdt.getText().toString().trim();
                    require = requireEdt.getText().toString().trim();
                    //System.out.println("----------------------->job:" + job);
                    zpnx = zpnxEdt.getText().toString().trim();
                    zpvx = zpvxEdt.getText().toString().trim();
                    jz = jzEdt.getText().toString().trim();
                    ygz = ygzEdt.getText().toString().trim();
                    gzsm = gzsmEdt.getText().toString().trim();
                    daysFlag = daysSpi.getText().toString();
                    daysFlag = daysFlag.replaceAll("-", "");
                    int data = Integer.parseInt(daysFlag);
                    daysFlag = data + "";
                    if (job.equals("")) {
                        dialogUtil.showToast("招聘岗位不能为空！");
                    } else if (gzsm.equals("")) {
                        dialogUtil.showToast("工作说明不能为空");
                    } else if (gzsm.length() > 50) {
                        dialogUtil.showToast("工作说明不能大于50个字！");
                    } else if (job.equals("")) {
                        dialogUtil.shortToast("招聘岗位不能为空，请重新输入!");
                    } else if (zpnx.equals("") && zpvx.equals("")
                            && jz.equals("")) {
                        dialogUtil.showToast("招聘男性,招聘女性,兼招三者必须填 写一个！");
                    } else if (zpnx.equals("0") && zpvx.equals("0")
                            && jz.equals("0")) {
                        dialogUtil.showToast("招聘男性,招聘女性,兼招三者不能都等于0！");
                    } else if (Integer.parseInt(zpnx.equals("") ? "0" : zpnx) > 1000) {
                        dialogUtil.showToast("招聘男性人数不能大于1000！");
                    } else if (Integer.parseInt(zpvx.equals("") ? "0" : zpvx) > 1000) {
                        dialogUtil.showToast("招聘女性人数不能大于1000！");
                    } else if (Integer.parseInt(jz.equals("") ? "0" : jz) > 1000) {
                        dialogUtil.showToast("兼职数不能大于1000！");
                    } else if (ygz.equals("")) {
                        dialogUtil.showToast("月工资不能为空,请重新输入！");
                    } else if (data < getFormatedTime()) {
                        dialogUtil.showToast("有效终止日期必须晚于今天！");
                    } else if (workArea.equals("")) {
                        dialogUtil.showToast(getString(R.string.spin_prompt) + "!");
                    } else {
                        wage = wageEdt.getText().toString().trim();
                        require = requireEdt.getText().toString().trim();
                        // 提交数据
                        onSubmitData();
                        onPostData();
                    }

                    break;
                case R.id.resetBtn:
                    postEdt.setText("");
                    wageEdt.setText("");
                    requireEdt.setText("");
                    zpnxEdt.setText("");
                    zpvxEdt.setText("");
                    jzEdt.setText("");
                    ygzEdt.setText("");
                    gzsmEdt.setText("");
                    break;
                case R.id.daysSpi:
                    commonUtil.getTime(daysSpi);
                    break;
                default:
                    break;
            }
        }
    }

    private void onSubmitData() {
        map = new HashMap<String, String>();
        map.put("acb200", user.getUserId());// 单位编号
        map.put("aca112", job);// 招聘岗位
        map.put("aca111", getIntent().getStringExtra("aca111"));//工作岗位ID
        map.put("acb216", gzsm);//工作说明
        map.put("acc214", wage == null ? "" : wage);// 福利待遇
        map.put("aae013", require == null ? "" : require);// 具体要求
        map.put("acb211", zpnx.equals("") ? "0" : zpnx);    //招聘男性
        map.put("acb212", zpvx.equals("") ? "0" : zpvx);    //招聘女性
        map.put("acb213", jz.equals("") ? "0" : jz);        //兼职
        map.put("acc034", ygz);                            //月工资
        map.put("aae017", user.getArea_id()); //单位所在地区编号
        map.put("acb202", workArea); //用工区域编号
        map.put("aae031", daysFlag);// 期限
    }

    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        Zpt_RecruitmentActivity.this, "提交中,请稍后...");
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
        qyDAL.doZpF(map, callBack);
    }

    private void postResult(String json) {
        Log.d(TAG, "postResult: json : " + json);
        Map<String, String> map = com.yifeng.nox.android.json.DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
                return;
            } else {
                commonUtil.shortToast("发布失败!");
            }
        } else {
            commonUtil.shortToast("发布失败!");
        }
    }

    private static int getFormatedTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int data = Integer.parseInt(format.format(new Date()).toString());
        return data;
    }

    private void showMsg(String title, String msg) {
        final Dialog builder = new Dialog(Zpt_RecruitmentActivity.this, R.style.dialog);
        builder.setContentView(R.layout.dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText(title);
        pMsg.setText(msg);
        Button btn = (Button) builder.findViewById(R.id.pBtn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
//				Intent intent = new Intent(Zpt_RecruitmentActivity.this, MainMenuActivity.class);
//				startActivity(intent);
//				Zpt_RecruitmentActivity.this.finish();
            }
        });
        // builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }

    private String workArea = "";

    /**
     * 工作地区
     */
    private void initArea() {
        areas = qyDAL.getAreas(true, "--" + getString(R.string.spin_prompt) + "--");
        SimpleAdapter adapter = new SimpleAdapter(this, areas, android.R.layout.simple_spinner_item, new String[]{"name"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        work_area.setAdapter(adapter);
        work_area.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                workArea = areas.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


}
