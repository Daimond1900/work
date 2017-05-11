package com.winksoft.android.yzjycy.ui.bminfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.data.QzzDAL;
import com.winksoft.android.yzjycy.data.RegisterDAL;
import com.winksoft.android.yzjycy.ui.resume.QztPersonalResumeActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.yifeng.nox.android.http.http.AjaxCallBack;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName:PersonalResumeActivity
 * Description：报名信息-个人详情的处理
 *
 * @author Administrator
 *         Date：2012-10-26
 */
public class ZptPersonalResumeActivity extends BaseActivity {
    private final String TAG = "ZptPersonalResume";
    private String postUrl = "";
    private TextView head_tipTxt, userName, birth_place, id_cardno, graduation_school, major, major_desp, job_unit, job_post, work_year, oneself_desp,
            family_address, contact_phone, email_add, mobile_num, qq_number, edu_training, work_experience, request_jobpost, other_request, marriage_state,
            politics_status, educational_history, work_area, job_nature, graduation_date, sr;
    private Button leftBtn, rightBtn, backBtn;
    private LinearLayout buttons;
    private ProgressDialog progressDialog;
    private RegisterDAL registerDAL;
    private Map<String, String> returnMap;
    private ImageView head;
    private Bitmap headBamp;
    private Map<String, String> leftParams, rightParams;
    private String leftResult, rightResult;
    private Intent qz;
    private String flag = "";// 0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用
    private String reasonStr = "", detailStr = "";
    private Dialog proDialog;
    private CommonUtil commonUtil;
    private QzzDAL qzzDAL;
    private QyDAL qyDAL;
    private ImageLoader im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_register_personal_resume);

        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(this));
        registerDAL = new RegisterDAL(this);
        qzzDAL = new QzzDAL(this);
        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
        qz = this.getIntent();
        initPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onQueryJL();
    }

    private void showProg(String Msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(Msg);
        progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
        progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
        progressDialog.show();
    }

    /**
     * 初始化界面
     */
    private void initPage() {
        head = (ImageView) findViewById(R.id.head);

        MyClick click = new MyClick();
        leftBtn = (Button) findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(click);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(click);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(click);

        buttons = (LinearLayout) findViewById(R.id.buttons);

        flag = qz.getStringExtra("c_tag");

		/* -------状态（0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用）------- */
        if ("0".equals(flag)) {
            leftBtn.setText("通知面试");
        } else if ("1".equals(flag)) {
            buttons.setVisibility(View.GONE);
        } else if ("2".equals(flag)) {
            leftBtn.setText("录   用");
        } else {
            buttons.setVisibility(View.GONE);
        }

        head_tipTxt = (TextView) findViewById(R.id.head_tipTxt);// 是否有头像提示
        userName = (TextView) findViewById(R.id.userName);// 姓名
        birth_place = (TextView) findViewById(R.id.birth_place);// 籍 贯
        work_year = (TextView) findViewById(R.id.work_year);// 工龄
        contact_phone = (TextView) findViewById(R.id.contact_phone);// 联系电话
        email_add = (TextView) findViewById(R.id.email_add);// emial
        qq_number = (TextView) findViewById(R.id.qq_number);// QQ号码
        family_address = (TextView) findViewById(R.id.family_address);// 家庭住址
//		mobile_num = (TextView) findViewById(R.id.mobile_num);// 手机号码
        id_cardno = (TextView) findViewById(R.id.id_cardno);// 身份证号码
        oneself_desp = (TextView) findViewById(R.id.oneself_desp);// 自我描述
        edu_training = (TextView) findViewById(R.id.edu_training);// 教育培训
        graduation_date = (TextView) findViewById(R.id.graduation_date);// 毕业时间
        graduation_school = (TextView) findViewById(R.id.graduation_school);// 毕业学校
        major = (TextView) findViewById(R.id.major);// 所学专业
        major_desp = (TextView) findViewById(R.id.major_desp);// 专业描述
        job_unit = (TextView) findViewById(R.id.job_unit);// 在职单位
        job_post = (TextView) findViewById(R.id.job_post);// 在职岗位
        work_experience = (TextView) findViewById(R.id.work_experience);// 工作经验
        request_jobpost = (TextView) findViewById(R.id.request_jobpost);// 求职岗位
        other_request = (TextView) findViewById(R.id.other_request);// 其它
        marriage_state = (TextView) findViewById(R.id.marriage_state);// 婚姻状况
        politics_status = (TextView) findViewById(R.id.politics_status);// 政治面貌
        educational_history = (TextView) findViewById(R.id.educational_history);// 学历水平
        work_area = (TextView) findViewById(R.id.work_area);// 工作地区
        job_nature = (TextView) findViewById(R.id.job_nature);// 工作性质
        sr = (TextView) findViewById(R.id.sr);// 出生年月

    }

    /**
     * 查询简历
     */
    private void onQueryJL() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {

            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ZptPersonalResumeActivity.this, "正在查询中,请稍后...");
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
                // setLoadingMsg("");
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        qzzDAL.queryResumeDetail(qz.getStringExtra("id"), "1", callBack);
    }

    /**
     * 查询简历结果处理
     *
     * @param json
     */
    private void queryResult(String json) {
        Map<String, String> map = com.yifeng.nox.android.json.DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else if (map.get("success").equals("true")) {
                Map<String, String> map1 = com.yifeng.nox.android.json.DataConvert.toConvertStringMap(json,
                        "resume");
                if (map1 != null) {
                    returnMap = map1;
                    initData();
                } else {
                    showProg("系统提示，查询失败,请重试或联系后台管理员!");
                }
            }
        } else {
            showProg("系统提示，查询失败,请重试或联系后台管理员!");
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (returnMap != null) {
            userName.setText(commonUtil.getMapValue(returnMap, "xm"));//姓名
            birth_place.setText(commonUtil.getMapValue(returnMap, "jg"));//籍贯
            id_cardno.setText(commonUtil.getMapValue(returnMap, "sfzh"));//身份证号码
            graduation_school.setText(commonUtil.getMapValue(returnMap, "byxx"));//毕业学校
            major.setText(commonUtil.getMapValue(returnMap, "zy"));//所学专业
            major_desp.setText(commonUtil.getMapValue(returnMap, "zyms"));//专业描述
            job_unit.setText(commonUtil.getMapValue(returnMap, "zzdw"));//在职单位
            job_post.setText(commonUtil.getMapValue(returnMap, "zzzw"));//在职岗位
            work_year.setText(commonUtil.getMapValue(returnMap, "gl"));//工龄
            oneself_desp.setText(commonUtil.getMapValue(returnMap, "zwms"));//自我描述
            family_address.setText(commonUtil.getMapValue(returnMap, "jtzz"));//家庭地址
            contact_phone.setText(commonUtil.getMapValue(returnMap, "lxdh"));//联系电话
            email_add.setText(commonUtil.getMapValue(returnMap, "email"));//电子邮箱
            qq_number.setText(commonUtil.getMapValue(returnMap, "qq"));//QQ号码
            edu_training.setText(commonUtil.getMapValue(returnMap, "jypxjl"));//教育培训
            work_experience.setText(commonUtil.getMapValue(returnMap, "gzjl"));//工作经历
            request_jobpost.setText(commonUtil.getMapValue(returnMap, "qzgw"));//求职岗位
            other_request.setText(commonUtil.getMapValue(returnMap, "qtyq"));//其它要求
            graduation_date.setText(commonUtil.getMapValue(returnMap, "bysj"));//毕业时间
            marriage_state.setText(commonUtil.getMapValue(returnMap, "hyzk"));//婚姻状况
            politics_status.setText(commonUtil.getMapValue(returnMap, "zzmm"));//政治面貌
            educational_history.setText(commonUtil.getMapValue(returnMap, "xl"));//学历水平
            work_area.setText(commonUtil.getMapValue(returnMap, "gzdq"));//工作区域
            job_nature.setText(commonUtil.getMapValue(returnMap, "gzxz"));//工作性质
            sr.setText(commonUtil.getMapValue(returnMap, "sr"));//生日
            String imgUrl = returnMap.get("pic_addr");
            if (imgUrl != null && !imgUrl.equals("")) {
                im.displayImage(Constants.IP + "images/" + imgUrl.replace("\\", "/"), head);
                head_tipTxt.setVisibility(View.GONE);
            }
        } else {
            dialogUtil.shortToast("信息加载失败!");

        }
    }

    private class MyClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    ZptPersonalResumeActivity.this.finish();
                    break;
                case R.id.leftBtn:
                    showAgreeDialog("提示");
                    break;
                case R.id.rightBtn:
                /* -------状态（0：投递后初始状态；1：被单位拒绝；2：初审通过等待面试；3：录用）------- */
                    if ("0".equals(flag)) {
                        showRefuseDialog("提示", "简历审核未通过！");
                    } else if ("2".equals(flag)) {
                        showRefuseDialog("提示", "面试未通过！");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /*
     * 录用、通知面试
     */
    private void doLeft() {
        leftParams = new HashMap<String, String>();
        leftParams.put("aab001", user.getUserId());// 公司名称
        leftParams.put("sending_id", qz.getStringExtra("c_id"));
        leftParams.put("remark", detailStr);// 输入的具体信息

        if ("0".equals(flag)) {
            postUrl = "android/sending/passTrial";
        } else if ("2".equals(flag)) {
            postUrl = "android/sending/passInterview";
        }
        onDoLeft();
    }

    /**
     * 同意 通知
     */
    private void onDoLeft() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ZptPersonalResumeActivity.this, "提交中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doLeftResult((String) arg0);

                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                // setLoadingMsg("");
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        qyDAL.doUpLeft(leftParams, postUrl, callBack);
    }

    /**
     * 同意 通知 结果处理
     *
     * @param json
     */
    private void doLeftResult(String json) {
        Map<String, String> map = com.yifeng.nox.android.json.DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else if (map.get("success").equals("true")) {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            showProg("系统提示，查询失败,请重试或联系后台管理员!");
        }
    }


    /*
       * 拒绝
       */
    private void doRight() {
        rightParams = new HashMap<>();
        rightParams.put("sending_id", qz.getStringExtra("c_id"));// sending_id
        rightParams.put("aab001", user.getUserId());// 公司名称
        rightParams.put("remark", reasonStr);// 输入的拒绝原因
        postUrl = "android/sending/rejectSending";
        Log.d(TAG, "doRight: " + rightParams);
        onDoRight();
    }

    /*
     * 拒绝
	 */
    private void onDoRight() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ZptPersonalResumeActivity.this, "提交中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doRightResult((String) arg0);
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
        qyDAL.doUpRight(rightParams, postUrl, callBack);
    }

    /**
     * 拒绝 结果处理
     *
     * @param json
     */
    private void doRightResult(String json) {
        Map<String, String> map = com.yifeng.nox.android.json.DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else if (map.get("success").equals("true")) {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            showProg("系统提示，提交失败,请重试或联系后台管理员!");
        }
    }
    /**
     * 可输入对话框-供通知面试和录用时使用
     *
     * @param title
     */
    private void showAgreeDialog(String title) {
        final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
        builder.setContentView(R.layout.zpt_register_input_dialog_agree);
        TextView titleTxt = (TextView) builder.findViewById(R.id.titleTxt);
        titleTxt.setText(title);
        final EditText contentEdt = (EditText) builder.findViewById(R.id.contentEdt);
        final TextView countTxt = (TextView) builder.findViewById(R.id.countTxt);
        contentEdt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            int num = 150;// 限制的最大字数

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                countTxt.setText("" + number + "/150");
                selectionStart = contentEdt.getSelectionStart();
                selectionEnd = contentEdt.getSelectionEnd();
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    contentEdt.setText(s + "/150");
                    contentEdt.setSelection(tempSelection);// 设置光标在最后
                }
            }
        });

        Button confirmBtn = (Button) builder.findViewById(R.id.confirmBtn);
        Button cancelBtn = (Button) builder.findViewById(R.id.cancelBtn);
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                detailStr = contentEdt.getText().toString();
                doLeft();
                builder.dismiss();
                ZptPersonalResumeActivity.this.finish();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setCanceledOnTouchOutside(true);
        builder.show();
    }

    /**
     * 可输入对话框-供拒绝时使用
     *
     * @param title
     */
    private void showRefuseDialog(String title, final String content1) {
        final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
        builder.setContentView(R.layout.zpt_register_input_dialog_refuse);
        TextView titleTxt = (TextView) builder.findViewById(R.id.titleTxt);
        titleTxt.setText(title);
        TextView contentTxt = (TextView) builder.findViewById(R.id.contentTxt);
        contentTxt.setText(content1);
        final EditText contentEdt = (EditText) builder.findViewById(R.id.contentEdt);
        final TextView countTxt = (TextView) builder.findViewById(R.id.countTxt);
        contentEdt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            int num = 150;// 限制的最大字数

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                countTxt.setText("" + number + "/150");
                selectionStart = contentEdt.getSelectionStart();
                selectionEnd = contentEdt.getSelectionEnd();
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    contentEdt.setText(s + "/150");
                    contentEdt.setSelection(tempSelection);// 设置光标在最后
                }
            }
        });

        Button confirmBtn = (Button) builder.findViewById(R.id.confirmBtn);
        Button cancelBtn = (Button) builder.findViewById(R.id.cancelBtn);
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputStr = contentEdt.getText().toString();
                if (inputStr.equals("")) {
                    reasonStr = content1;
                } else {
                    reasonStr = contentEdt.getText().toString();
                }
                doRight();
                builder.dismiss();
                ZptPersonalResumeActivity.this.finish();
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setCanceledOnTouchOutside(true);
        builder.show();
    }

    /**
     * 确认提示对话框
     *
     * @return
     */
    private void showMsg(String title, String msg) {
        final Dialog builder = new Dialog(ZptPersonalResumeActivity.this, R.style.dialog);
        builder.setContentView(R.layout.zpt_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText(title);
        pMsg.setText(msg);
        Button btn = (Button) builder.findViewById(R.id.pBtn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
//				Intent intent = new Intent(PersonalResumeActivity.this, RegisterCountListActivity.class);
//				startActivity(intent);
//				PersonalResumeActivity.this.finish();
            }
        });
        // builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        builder.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                ZptPersonalResumeActivity.this.finish();
            }
        });
        builder.show();
    }

}
