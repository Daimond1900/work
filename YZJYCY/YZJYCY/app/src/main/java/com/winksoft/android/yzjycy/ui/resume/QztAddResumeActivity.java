package com.winksoft.android.yzjycy.ui.resume;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.QzzDAL;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.IDCard;
import com.winksoft.android.yzjycy.util.StringHelper;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.yifeng.nox.android.http.entity.FormFile;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.util.CommonUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * comment:新增个人简历
 *
 * @author:ZhangYan Date:2012-8-28
 */
public class QztAddResumeActivity extends BaseActivity {
    private EditText userName, birth_place, id_cardno, graduation_school, major, major_desp, job_unit, job_post, work_year, oneself_desp,
            family_address, contact_phone, email_add, qq_number, edu_training, work_experience, request_jobpost, other_request;
    private Button back_btn, graduation_date, submitBtn;
    private ImageView head_btn;
    private Spinner marriage_state, politics_status, educational_history, work_area, job_nature;
    private String user_name, borthPlace, idCardNo, gradSchool, maJor, majorDesp, jobUnit, jobPost, workYear, oneselfDesp, familyAddress, contactPhone,
            email, qqNumber, eduTraining, workExp, requestJobPost, other, marState, politicsState, eduState, workArea, jobNature, graduationDate;
    private LinearLayout submit, preview;
    private String userId = "", fileName = "", fileUrl = "";//图片地址
    private UserDAL userDal;
    private QzzDAL qzzDal;
    private List<Map<String, String>> marriages, politics, educationals, areas, jobs;
    private boolean isAdd = true, isPost = false;//isAdd就否是新增还是修改, isPost是否提交
    private Map<String, String> info, postParams;
    private FileUtils fileUtil;
    public static final int NONE = 0;
    private TextView title;
    private Bitmap postBitmap;
    private ImageLoader im;
    Dialog proDialog;
    private List<FormFile> listForm = new ArrayList<>();

    private static final String TAG = "QztAddResumeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_add_resume);
        qzzDal = new QzzDAL(this);
        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(QztAddResumeActivity.this));
        fileUtil = new FileUtils();

        marriages = qzzDal.getMarriages();    // 	婚姻状况
        politics = qzzDal.getPolitics();        //	政治面貌
        educationals = qzzDal.getEducationals();    // 学历
        areas = qzzDal.getAreas(true);        // 	工作区域
        jobs = qzzDal.getJobs();                //	工作性质

        initPage();

        if (!isAdd) {//修改需加载详细数据
            onQueryJL();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 页面初始化
     */
    private void initPage() {
        ButtonClickListener click = new ButtonClickListener();
        userId = this.getIntent().getStringExtra("userId") == null ? user.getUserId() : this.getIntent().getStringExtra("userId");
        isAdd = this.getIntent().getBooleanExtra("isAdd", true);

        title = (TextView) findViewById(R.id.title);

        if (isAdd) {
            title.setText("新增简历");
        } else {
            title.setText("修改简历");
        }

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(click);

        head_btn = (ImageView) findViewById(R.id.head_btn);
        head_btn.setOnClickListener(click);

        userName = (EditText) findViewById(R.id.userName);//姓名
        userName.setText(user.getLoginName());
        birth_place = (EditText) findViewById(R.id.birth_place);//籍     贯
        work_year = (EditText) findViewById(R.id.work_year);//工龄
        contact_phone = (EditText) findViewById(R.id.contact_phone);//联系电话
        email_add = (EditText) findViewById(R.id.email_add);//emial
        qq_number = (EditText) findViewById(R.id.qq_number);//QQ号码
        family_address = (EditText) findViewById(R.id.family_address);//家庭住址
        id_cardno = (EditText) findViewById(R.id.id_cardno);//身份证号码
        oneself_desp = (EditText) findViewById(R.id.oneself_desp);//自我描述
        edu_training = (EditText) findViewById(R.id.edu_training);//教育培训

        graduation_date = (Button) findViewById(R.id.graduation_date);
        graduation_date.setText(DateUtil.getStrCurrentDate());//毕业时间
        graduation_date.setOnClickListener(click);

        graduation_school = (EditText) findViewById(R.id.graduation_school);//毕业学校
        major = (EditText) findViewById(R.id.major);//所学专业
        major_desp = (EditText) findViewById(R.id.major_desp);//专业描述
        job_unit = (EditText) findViewById(R.id.job_unit);//在职单位
        job_post = (EditText) findViewById(R.id.job_post);//在职岗位
        work_experience = (EditText) findViewById(R.id.work_experience);//工作经验
        request_jobpost = (EditText) findViewById(R.id.request_jobpost);//求职岗位
        other_request = (EditText) findViewById(R.id.other_request);//其它

        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(click);


        submit = (LinearLayout) findViewById(R.id.submit);
        submit.setOnClickListener(click);
        preview = (LinearLayout) findViewById(R.id.preview);
        preview.setOnClickListener(click);


        marriage_state = (Spinner) findViewById(R.id.marriage_state);//婚姻状况
        politics_status = (Spinner) findViewById(R.id.politics_status);//政治面貌
        educational_history = (Spinner) findViewById(R.id.educational_history);//学历水平
        work_area = (Spinner) findViewById(R.id.work_area);//工作地区
        job_nature = (Spinner) findViewById(R.id.job_nature);//工作性质
        intitSpinner();
        fileName = DateUtil.getStrCurrentDates() + ".jpg";

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
        qq_number.setText(commonUtil.getMapValue(info, "qq"));//QQ号码
        edu_training.setText(commonUtil.getMapValue(info, "jypxjl"));//教育培训
        work_experience.setText(commonUtil.getMapValue(info, "gzjl"));//工作经历
        request_jobpost.setText(commonUtil.getMapValue(info, "qzgw"));//求职岗位
        other_request.setText(commonUtil.getMapValue(info, "qtyq"));//其它要求
        graduation_date.setText(commonUtil.getMapValue(info, "bysj"));//毕业时间
        String imgUrl = info.get("pic_addr");
        if (imgUrl != null && !imgUrl.equals("")) {
            im.displayImage(Constants.IP + "images/" + imgUrl.replace("\\", "/"), head_btn);
        }
        for (int i = 0; i < marriages.size(); i++) {//婚姻状况
            if (marriages.get(i).get("id").equals(commonUtil.getMapValue(info, "hyzk"))) {
                marriage_state.setSelection(i);
            }
        }

        for (int i = 0; i < politics.size(); i++) {//政治面貌
            if (politics.get(i).get("id").equals(commonUtil.getMapValue(info, "zzmm"))) {
                politics_status.setSelection(i);
            }
        }

        for (int i = 0; i < educationals.size(); i++) {//学历水平
            if (educationals.get(i).get("id").equals(commonUtil.getMapValue(info, "xl"))) {
                educational_history.setSelection(i);
            }
        }

        for (int i = 0; i < areas.size(); i++) {//工作区域
            if (areas.get(i).get("name").equals(commonUtil.getMapValue(info, "gzdq"))) {
                work_area.setSelection(i);
            }
        }

        for (int i = 0; i < jobs.size(); i++) {//工作性质
            if (jobs.get(i).get("id").equals(commonUtil.getMapValue(info, "gzxz"))) {
                job_nature.setSelection(i);
            }
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
                        QztAddResumeActivity.this, "正在查询中,请稍后...");
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
        qzzDal.queryResumeDetail(user.getUserId(), "1", callBack);
    }

    /**
     * 查询简历结果处理
     *
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
                if (map1 != null) {
                    info = map1;
                    initData();
                } else {
                    commonUtil.shortToast("系统正忙,请稍后再试!");
                }
            }
        } else {
            commonUtil.shortToast("系统正忙,请稍后再试!");
        }
    }

    /**
     * 初始化Spinner
     */
    private void intitSpinner() {
        initMarriage();
        initPolitic();
        initEdu();
        initArea();
        initJob();
    }

    /**
     * 婚姻状态
     */
    private void initMarriage() {
        SimpleAdapter adapter = new SimpleAdapter(this, marriages, android.R.layout.simple_spinner_item, new String[]{"value"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marriage_state.setPrompt("请选择婚姻状况");
        marriage_state.setAdapter(adapter);
        marriage_state.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                marState = marriages.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 择政治面貌
     */
    private void initPolitic() {
        SimpleAdapter adapter = new SimpleAdapter(this, politics, android.R.layout.simple_spinner_item, new String[]{"value"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        politics_status.setPrompt("请选择政治面貌");
        politics_status.setAdapter(adapter);
        politics_status.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                politicsState = politics.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 学历水平
     */
    private void initEdu() {
        SimpleAdapter adapter = new SimpleAdapter(this, educationals, android.R.layout.simple_spinner_item, new String[]{"value"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        educational_history.setPrompt("请选择学历水平");
        educational_history.setAdapter(adapter);
        educational_history.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                eduState = educationals.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 工作地区
     */
    private void initArea() {
        SimpleAdapter adapter = new SimpleAdapter(this, areas, android.R.layout.simple_spinner_item, new String[]{"name"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        work_area.setPrompt("请选择工作地区");
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

    /**
     * 工作性质
     */
    private void initJob() {
        SimpleAdapter adapter = new SimpleAdapter(this, jobs, android.R.layout.simple_spinner_item, new String[]{"value"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        job_nature.setPrompt("请选择工作性质");
        job_nature.setAdapter(adapter);
        job_nature.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                jobNature = jobs.get(arg2).get("id");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submitBtn:
                    isPost = true;
                    postData();         // 整理提交的数据
                    onPostData();
                    break;
                case R.id.back_btn:
                    QztAddResumeActivity.this.finish();
                    break;
                case R.id.graduation_date:
                    commonUtil.getTime(graduation_date);
                    break;
                case R.id.head_btn:
                    //选择图片
                    doPickPhotoAction();
                    break;
                case R.id.preview:
                    Intent view = new Intent(QztAddResumeActivity.this, QztResumeInfoActivity.class);
                    view.putExtra("info", "true");
                    startActivity(view);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 整理所要提交的数据
     * 放在   postParams  (Map) 里面
     */
    private void postData() {
        if (isPost) {       // 提交数据
            postParams = new HashMap<String, String>();
            user_name = userName.getText().toString();//姓名
            borthPlace = birth_place.getText().toString();//籍贯
            idCardNo = id_cardno.getText().toString();//身份证号码
            gradSchool = graduation_school.getText().toString();//毕业学校
            maJor = major.getText().toString();//所学专业
            majorDesp = major_desp.getText().toString();//专业描述
            jobUnit = job_unit.getText().toString();//在职单位
            jobPost = job_post.getText().toString();//在职岗位
            workYear = work_year.getText().toString();//工龄
            oneselfDesp = oneself_desp.getText().toString();//自我描述
            familyAddress = family_address.getText().toString();//家庭地址
            contactPhone = contact_phone.getText().toString();//联系电话
            email = email_add.getText().toString();//电子邮箱
            qqNumber = qq_number.getText().toString();//QQ号码
            eduTraining = edu_training.getText().toString();//教育培训
            workExp = work_experience.getText().toString();//工作经验
            requestJobPost = request_jobpost.getText().toString();//求职岗位
            other = other_request.getText().toString();//其它要求
            graduationDate = graduation_date.getText().toString();//毕业时间
            String idCardCheck = "";
            try {
                idCardCheck = IDCard.IDCardValidate(idCardNo);
            } catch (Exception e) {
                idCardCheck = "身份证号码格式不正确";
            }
            if (user_name.equals("")) {
                commonUtil.shortToast("姓名不能为空!");
                return;
            } else if (user_name.length() > 10) {
                commonUtil.shortToast("姓名太长,请输入小于10位的名字.");
                return;
            } else if (borthPlace.equals("")) {
                commonUtil.shortToast("籍贯不能为空!");
                return;
            } else if (contactPhone.equals("")) {
                commonUtil.shortToast("联系电话不能为空!");
                return;
            } else if (marState.equals("")) {
                commonUtil.shortToast("请选择婚姻状况!");
                return;
            } else if (idCardNo.equals("")) {
                commonUtil.shortToast("身份证号码不能为空!");
                return;
            } else if (!idCardCheck.equals("")) {
                commonUtil.shortToast(idCardCheck);
                return;
            } else if (eduState.equals("")) {
                commonUtil.shortToast("请选择学历水平!");
                return;
            } else if (gradSchool.equals("")) {
                commonUtil.shortToast("毕业学校不能为空!");
                return;
            } else if (maJor.equals("")) {
                commonUtil.shortToast("所学专业不能为空!");
                return;
            } else if (requestJobPost.equals("")) {
                commonUtil.shortToast("求职岗位不能为空!");
                return;
            } else if (workArea.equals("")) {
                commonUtil.shortToast("请选择工作区域!");
                return;
            } else {
                if (!email.equals("")) {
                    if (!StringHelper.checkEmail(email)) {
                        commonUtil.shortToast("电子邮箱格式不正确!");
                        return;
                    }

                }
                postParams.put("id", userId);//登录号
                postParams.put("xm", user_name);//姓名
                postParams.put("lxdh", contactPhone);//联系电话
                postParams.put("email", email);//电子邮箱
                postParams.put("qq", qqNumber);// QQ号码
                postParams.put("jtzz", familyAddress);//家庭住址
                postParams.put("jg", borthPlace);//籍贯
                postParams.put("hyzk", marState);//婚姻状态
                postParams.put("sfzh", idCardNo);//身份证号
                postParams.put("zwms", oneselfDesp);//自我描述
                postParams.put("zzmm", politicsState);//政治面貌
                postParams.put("jypxjl", eduTraining);//教育培训
                postParams.put("xl", eduState);//学历
                postParams.put("bysj", graduationDate);//毕业时间
                postParams.put("byxx", gradSchool);//毕业学校
                postParams.put("zy", maJor);//专业
                postParams.put("zyms", majorDesp);//专业描述
                postParams.put("zzdw", jobUnit);//在职单位
                postParams.put("zzzw", jobPost);//在职岗位
                postParams.put("gl", workYear);//工龄
                postParams.put("gzjl", workExp); //工作经验
                postParams.put("qzgw", requestJobPost);//求职岗位
                postParams.put("gzdq", workArea);//工作地区
                postParams.put("gzxz", jobNature);//工作性质
                postParams.put("qtyq", other);//其他要求

                user.setIdCard(idCardNo);

                FormFile files = null;
                byte[] data = null;
                if (postBitmap != null) {
                    data = CommonUtil.Bitmap2Bytes(postBitmap);
                    FormFile f1 = new FormFile(fileName, data, "img", "image/pjpeg");
                    listForm.add(f1);
                }
            }
        }
    }

    /**
     * 提交数据 postParams listForm
     */
    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztAddResumeActivity.this, "正在提交,请稍后...");
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
        qzzDal.postResume(postParams, listForm, callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("false").equals(map.get("success"))) {
                commonUtil.shortToast(commonUtil.getMapValue(map,"msg"));
            } else if ("true".equals(map.get("success"))) {
                UserSession session = new UserSession(QztAddResumeActivity.this);
                String userpic = map.get("pic_add") == null ? "" : map.get("pic_add");
                //如果修改了头象重新设置用户信息
                if (!"".equals(userpic)) {
                    user.setPic(userpic);
                }
                user.setIdCard(idCardNo); //这里用作身份证号
                session.setUser(user);

                if (isAdd) {
                    //跳回查看详细界面
                    Intent info = new Intent(QztAddResumeActivity.this, QztPersonalResumeActivity.class);
                    info.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    info.putExtra("isEdit", true);
                    startActivity(info);
                }
                commonUtil.shortToast(commonUtil.getMapValue(map,"msg"));
                QztAddResumeActivity.this.finish();
            }
        } else {
            commonUtil.shortToast("系统正忙,请稍后再试!");
        }
    }

    /**
     * 选择是否拍照还是从相册里取相片
     */
    private void doPickPhotoAction() {
        Context context = this;
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        String cancel = "返回";
        String[] choices = {"拍照", "从相册中选择"};
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
                android.R.layout.simple_list_item_1, choices);

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                dialogContext);
        builder.setTitle("请选择操作");
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: {
                                doTakePhoto();// 用户点击了从照相机获取
                                break;

                            }
                            case 1:
                                doPickPhotoFromGallery();// 从相册中去获取
                                break;
                        }
                    }

                });
        builder.setNegativeButton(cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        builder.create().show();
    }

    /**
     * 拍照
     */
    private void doTakePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 存储卡可用 将照片存储在 sdcard
        if (fileUtil.checkSDCard()) {
            String filePath = fileUtil.getSDPATH() + "/";
            File photoPath = new File(filePath);
            if (!photoPath.exists()) {
                photoPath.mkdirs();
            }
            File PHOTO_DIR = new File(filePath, fileName);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PHOTO_DIR));

            startActivityForResult(intent, 2);
        } else {
            this.commonUtil.shortToast("当前sdcard不可用!");
        }
    }

    /**
     * 选取本地图片
     */
    private void doPickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 20);
    }

    /***
     * 图片裁剪
     */
    private void cutPhoto(String path) {
        File file = new File(path);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 3);//裁剪框比例
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 80);//输出图片大小
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "图片裁剪"), 3);
        //finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;
        if (requestCode == 20) {
            ContentResolver resolver = getContentResolver();
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            fileUrl = cursor.getString(1);
            try {
                cutPhoto(fileUrl);
            } catch (Exception e) {
                System.out.println("图片获取失败!");
            }
        } else if (requestCode == 2) {

            if (fileUtil.checkSDCard()) {

                fileUrl = fileUtil.getSDPATH() + "/" + fileName;
                try {
                    cutPhoto(fileUrl);
                } catch (Exception e) {
                    System.out.println("图片获取失败!");
                }

            } else {
                // 存储卡不可用直接返回缩略图
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                postBitmap = bitmap;
            }
        } else if (requestCode == 3) {
            //剪切过后的图片
            Bitmap bmp = data.getParcelableExtra("data");
            postBitmap = bmp;
            head_btn.setImageBitmap(bmp);
        }
    }
}
