package com.winksoft.android.yzjycy.ui.resume;

import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QzzDAL;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * comment:个人简历管理
 *
 * @author:ZhangYan Date:2012-8-28
 */
public class QztPersonalResumeActivity extends BaseActivity {
    private TextView userName, birth_place, id_cardno, graduation_school, major, major_desp, job_unit, job_post, work_year, oneself_desp,
            family_address, contact_phone, email_add, mobile_num, qq_number, edu_training, work_experience, request_jobpost, other_request, marriage_state,
            politics_status, educational_history, work_area, job_nature, graduation_date, sr, isApprove, updateTime;
    private Button add_btn, back_btn, editBtn;
    private TextView tip_info;
    private TableLayout infoTable;
    private ProgressDialog progressDialog;
    private QzzDAL qzzDal;
    private Map<String, String> info;
    private boolean isAdd = true;
    private ImageView head;
    private final String flage = "0";    //
    Dialog proDialog;
    private ImageLoader im;
    private static final String TAG = "QztPersonalActiviy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_personal_resume);
        qzzDal = new QzzDAL(this);
        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(QztPersonalResumeActivity.this));
        initPage();
    }

    /**
     * 根据具体信息，显示不同的按钮
     */
    private void setUserPage() {
        if ("".equals(user.getIdCard())) {      // 通过身份证号判断是否存在简历
            infoTable.setVisibility(View.GONE);
            tip_info.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.GONE);
            isAdd = true;
            add_btn.setText("新增个人简历");
        } else {
            isAdd = false;
            add_btn.setText("修改个人资料");
            onQueryJL();                    // 查询简历信息
        }
    }

    /**
     * 初始化界面
     */
    private void initPage() {
        ButtonClickListener click = new ButtonClickListener();
        head = (ImageView) findViewById(R.id.head);
        tip_info = (TextView) findViewById(R.id.tip_info);
        infoTable = (TableLayout) findViewById(R.id.infoTable);
        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(click);
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(click);
        editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(click);
        userName = (TextView) findViewById(R.id.userName);//姓名
        birth_place = (TextView) findViewById(R.id.birth_place);//籍     贯
        work_year = (TextView) findViewById(R.id.work_year);//工龄
        contact_phone = (TextView) findViewById(R.id.contact_phone);//联系电话
        email_add = (TextView) findViewById(R.id.email_add);//emial
        qq_number = (TextView) findViewById(R.id.qq_number);//QQ号码
        family_address = (TextView) findViewById(R.id.family_address);//家庭住址
//		mobile_num = (TextView)findViewById(R.id.mobile_num);//手机号码
        id_cardno = (TextView) findViewById(R.id.id_cardno);//身份证号码
        oneself_desp = (TextView) findViewById(R.id.oneself_desp);//自我描述
        edu_training = (TextView) findViewById(R.id.edu_training);//教育培训
        graduation_date = (TextView) findViewById(R.id.graduation_date);//毕业时间
        graduation_school = (TextView) findViewById(R.id.graduation_school);//毕业学校
        major = (TextView) findViewById(R.id.major);//所学专业
        major_desp = (TextView) findViewById(R.id.major_desp);//专业描述
        job_unit = (TextView) findViewById(R.id.job_unit);//在职单位
        job_post = (TextView) findViewById(R.id.job_post);//在职岗位
        work_experience = (TextView) findViewById(R.id.work_experience);//工作经验
        request_jobpost = (TextView) findViewById(R.id.request_jobpost);//求职岗位
        other_request = (TextView) findViewById(R.id.other_request);//其它
        marriage_state = (TextView) findViewById(R.id.marriage_state);//婚姻状况
        politics_status = (TextView) findViewById(R.id.politics_status);//政治面貌
        educational_history = (TextView) findViewById(R.id.educational_history);//学历水平
        work_area = (TextView) findViewById(R.id.work_area);//工作地区
        job_nature = (TextView) findViewById(R.id.job_nature);//工作性质
        sr = (TextView) findViewById(R.id.sr);//出生年月
        isApprove = (TextView) findViewById(R.id.isApprove);//是否审核
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userName.setText(commonUtil.getMapValue(info, "xm"));//姓名
        birth_place.setText(commonUtil.getMapValue(info, "jg"));//籍贯
        id_cardno.setText(commonUtil.getMapValue(info, "sfzh"));//身份证号码
        graduation_school.setText(commonUtil.getMapValue(info, "byxx"));//毕业学校
        major.setText(commonUtil.getMapValue(info, "zy"));//所学专业
        major_desp.setText(commonUtil.getMapValue(info, "zyms"));//专业描述
        job_unit.setText(commonUtil.getMapValue(info, "zzdw"));//在职单位
        job_post.setText(commonUtil.getMapValue(info, "zzzw"));//在职岗位
        work_year.setText(commonUtil.getMapValue(info, "gl"));//工龄
        oneself_desp.setText(commonUtil.getMapValue(info, "zwms"));//自我描述
        family_address.setText(commonUtil.getMapValue(info, "jtzz"));//家庭地址
        contact_phone.setText(commonUtil.getMapValue(info, "lxdh"));//联系电话
        email_add.setText(commonUtil.getMapValue(info, "email"));//电子邮箱
//		   	  mobile_num.setText(info.get("sj"));//手机号码
        qq_number.setText(commonUtil.getMapValue(info, "qq"));//QQ号码
        edu_training.setText(commonUtil.getMapValue(info, "jypxjl"));//教育培训
        work_experience.setText(commonUtil.getMapValue(info, "gzjl"));//工作经历
        request_jobpost.setText(commonUtil.getMapValue(info, "qzgw"));//求职岗位
        other_request.setText(commonUtil.getMapValue(info, "qtyq"));//其它要求
        graduation_date.setText(commonUtil.getMapValue(info, "bysj"));//毕业时间
        marriage_state.setText(commonUtil.getMapValue(info, "hyzk"));//婚姻状况
        politics_status.setText(commonUtil.getMapValue(info, "zzmm"));//政治面貌
        educational_history.setText(commonUtil.getMapValue(info, "xl"));//学历水平
        work_area.setText(commonUtil.getMapValue(info, "gzdq"));//工作区域
        job_nature.setText(commonUtil.getMapValue(info, "gzxz"));//工作性质
        sr.setText(commonUtil.getMapValue(info, "sr"));//生日
        String approve = info.get("is_open") == null ? "N" : info.get("is_open");
        isApprove.setText(approve.equals("N") ? "未审核" : "已审核");
        /**
         * 头像获取  待修改
         */
        String imgUrl = info.get("pic_addr");
        if (imgUrl != null && !imgUrl.equals("")) {
            im.displayImage(Constants.IP + "images/" + imgUrl.replace("\\", "/"), head);
        }
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
                        QztPersonalResumeActivity.this, "正在查询中,请稍后...");
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
        qzzDal.queryResumeDetail(user.getUserId(), "1", callBack);
    }

    /**
     * 查询简历结果处理
     * @param json
     */
    private void queryResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
            } else if (map.get("success").equals("true")) {
                Map<String, String> map1 = DataConvert.toConvertStringMap(json,
                        "resume");
                Log.d(TAG, "queryResult: map1" + map1);
                if (map1 != null) {
                    info = map1;
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
     * 显示进度等待条
     * @param Msg
     */
    private void showProg(String Msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(Msg);
        progressDialog.setIndeterminate(true);//设置进度条是否为不明确
        progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserPage();
    }


    /**
     * 按钮点击事件
     */
    private class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_btn:  // 新增简历按钮
                    Intent addIntent = new Intent(QztPersonalResumeActivity.this, QztAddResumeActivity.class);
                    addIntent.putExtra("userId", user.getUserId());
                    addIntent.putExtra("isAdd", isAdd);
                    startActivity(addIntent);
                    break;
                case R.id.editBtn:  // 修改简历按钮
                    Intent editBtn = new Intent(QztPersonalResumeActivity.this, QztAddResumeActivity.class);
                    editBtn.putExtra("userId", user.getUserId());
                    editBtn.putExtra("isAdd", isAdd);
                    startActivity(editBtn);
                    break;
                case R.id.back_btn:
                    QztPersonalResumeActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    }
}
