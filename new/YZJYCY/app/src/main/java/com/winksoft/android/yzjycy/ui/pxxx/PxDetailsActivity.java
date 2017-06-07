package com.winksoft.android.yzjycy.ui.pxxx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newyzjycy.map.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.List;
import java.util.Map;

public class PxDetailsActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "PxDetails";
    private Button back_btn, bm, ybm,ygq;
    private TextView bjmc, pxjg, pxlx, pxgz, pxdj, pxks, kbrq, jsrq;
    private String class_id, is_enroll = "",is_time_ok="";
    private int verifyFlag;
    private String longitude = "";// 经度
    private String latitude = "";// 纬度
    private ListView kblistview;
    private boolean mIsWs = false;
    private Dialog proDialog;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private List<Map<String, String>> listCourseInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pxxq_activity);
        initView(); // 初始化
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        class_id = this.getIntent().getStringExtra("class_id");    // 班级的ID
          /*异常情况刷新*/
        findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载数据
                doVerifyUserinfor();    // 验证用户资料
                doPost();
            }
        });
    }

    /**
     * 报名 已报名 按钮切换
     *
     * @param is_enroll2
     */
    private void isEnroll(String is_enroll2 , String is_time_ok) {
        if ("1".equals(is_enroll2)) { // 已经报过名了
            bm.setVisibility(View.GONE);
            ygq.setVisibility(View.GONE);
            ybm.setVisibility(View.VISIBLE);
        } else {
            if("1".equals(is_time_ok)){ //过期了
                bm.setVisibility(View.GONE);
                ybm.setVisibility(View.GONE);
                ygq.setVisibility(View.VISIBLE);
            }else{
                bm.setVisibility(View.VISIBLE);
                ybm.setVisibility(View.GONE);
                ygq.setVisibility(View.GONE);
            }
        }
    }

    // 初始化
    private void initView() {
        back_btn = (Button) findViewById(R.id.back_btn); // 返回键
        back_btn.setOnClickListener(this);
        bm = (Button) findViewById(R.id.bm); // 报名
        ybm = (Button) findViewById(R.id.ybm); // 已报名
        ygq= (Button) findViewById(R.id.ygq); // 过期
        bm.setOnClickListener(this);
        ybm.setOnClickListener(this);
        ygq.setOnClickListener(this);

        bjmc = (TextView) findViewById(R.id.bjmc);
        pxjg = (TextView) findViewById(R.id.pxjg);
        pxlx = (TextView) findViewById(R.id.pxlx);
        pxgz = (TextView) findViewById(R.id.pxgz);
        pxdj = (TextView) findViewById(R.id.pxdj);
        pxks = (TextView) findViewById(R.id.pxks);
        kbrq = (TextView) findViewById(R.id.kbrq);
        jsrq = (TextView) findViewById(R.id.jsrq);
        kblistview = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_btn: // 返回
                this.finish();
                break;
            case R.id.bm: // 报名
                if (!Constants.iflogin) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    if (verifyFlag == 1 && mIsWs) {
                        doBmts();
                    } else if (verifyFlag == 2) {
                        commonUtil.shortToast("请完善个人信息!");
                        // 跳转到 完整个人信息的界面
                        intent = new Intent(this, BmInfoSureActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.ybm: // 已报名
                commonUtil.shortToast("您已经报过名了!");
                break;
            case R.id.ygq: // 已报名
                commonUtil.shortToast("报名时间已截止!");
                break;
            // case R.id.dz: // 地图
            // if (!longitude.equals("") && !latitude.equals("")) {
            // Intent map = new Intent(PxDetailsActivity.this,
            // ZptMapInfoActivity.class);
            // map.putExtra("companyName", "");
            // map.putExtra("companyAddress", dz.getText().toString());
            // map.putExtra("telNo", "");
            // map.putExtra("longitude", longitude);// 经度
            // map.putExtra("latitude", latitude);// 纬度
            // startActivity(map);
            // } else {
            // dialogUtil.shortToast("还没标注地理位置.");
            // }
            // break;
            default:
                break;
        }
    }

    /**
     * 报名的提示框
     */
    public void doBmts() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("培训报名");
        pMsg.setText("确定要报名吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                doBm();
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
    protected void onResume() {
        super.onResume();
        // 加载数据
        doVerifyUserinfor();    // 验证用户资料
        doPost();                //
    }

    /**
     * 详情查询
     */
    private void doPost() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        PxDetailsActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                findViewById(R.id.islayout).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.GONE);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.doPxxxDetailsQuery(class_id, !Constants.iflogin ? "" : user.getUserId(), callBack);
    }

    /**
     * 查询详情的结果处理
     *
     * @param json
     */
    private void postResult(String json) {
        Log.d(TAG, "postResult:详情界面   = =  "+ json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("false")) {
                commonUtil.shortToast(map.get("msg"));
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
            } else if (map.get("success").equals("true")) {
                Map<String, String> mapClassInfo = DataConvert.toConvertStringMap(json, "classInfo");
                if (mapClassInfo != null) {
                    pxks.setText(commonUtil.getMapValue(mapClassInfo, "training_course_hour"));// 课时
                    jsrq.setText(commonUtil.getMapValue(mapClassInfo, "ending_time"));
                    kbrq.setText(commonUtil.getMapValue(mapClassInfo, "opening_time"));// 开班时间
                    is_enroll = commonUtil.getMapValue(mapClassInfo, "is_enroll"); // 是否投过简历

                    is_time_ok = commonUtil.getMapValue(mapClassInfo, "is_time_ok"); // 是否过时



                    latitude = commonUtil.getMapValue(mapClassInfo, "loc_lat"); // 纬度
                    longitude = commonUtil.getMapValue(mapClassInfo, "loc_lng"); // 经度
                }
                listCourseInfo = DataConvert.toConvertStringList(json, "courseInfo");
                if (listCourseInfo != null && listCourseInfo.size() > 0) {
                    // 课表

                    SimpleAdapter adapter = new SimpleAdapter(this, listCourseInfo, R.layout.listcourseinfo_item,
                            new String[]{"lesson_content", "lesson_teacher", "room_loc", "starting_time", "ending_time"},
                            new int[]{R.id.kcnr, R.id.skr, R.id.dz, R.id.kssj, R.id.jssj});
                    kblistview.setAdapter(adapter);
                    kblistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "onItemClick: position = " + position);
                            Log.d(TAG, "onItemClick:课程点击事件 = " + listCourseInfo.get(position));

                            String room_loc_xy = commonUtil.getMapValue(listCourseInfo.get(position), "room_loc_xy");
                            if ("".equals(room_loc_xy)) {
                                return;
                            }

                            String[] splitLatLong = room_loc_xy.split(",");
                            if (splitLatLong.length != 2) {
                                return;
                            }
                            String lat = splitLatLong[1];
                            String lot = splitLatLong[0];

                            if (lat != null && !"".equals(lat) && lot != null && !"".equals(lot)) {
                                Intent intent = new Intent(PxDetailsActivity.this, ZptMapInfoActivity.class);
                                intent.putExtra("title", "开课地点");
                                intent.putExtra("lat", lat);
                                intent.putExtra("lot", lot);
                                startActivity(intent);
                            }


                          /*  try {
                                if (arg2 == listCourseInfo.size()) {
                                    return;
                                }
                                Log.d(TAG, "onItemClick:课程点击事件 = " + listCourseInfo.get(arg2));
//                                    Log.d(TAG, "onItemClick: SURPERDATA" + mapList + "_________________");
//                                    String positionId = mapList.get(arg2).get("acb200").toString();// 职位编号
//                                    Intent intent = new Intent(getActivity(), QztPositionView.class);
//                                    intent.putExtra("title", "职位详情");
//                                    intent.putExtra("positionId", positionId);
//                                    intent.putExtra("companyNameStr", mapList.get(arg2).get("aab004").toString());
//                                    startActivity(intent);
                            } catch (Exception e) {
                                commonUtil.shortToast("未响应!");
                            }*/
                        }
                    });
                }

            }
        }

        // 给组件赋值
        // 传过来的
        bjmc.setText(this.getIntent().getStringExtra("class_name")); // 课程名
        pxjg.setText(this.getIntent().getStringExtra("training_agent_name")); // 培训机构
        pxlx.setText(this.getIntent().getStringExtra("training_type")); // 课程名
        pxgz.setText(this.getIntent().getStringExtra("training_job_type")); // 培训工种
        pxdj.setText(this.getIntent().getStringExtra("training_level")); // 培训等级

        isEnroll(is_enroll,is_time_ok);
    }

    /**
     * 验证身份信息
     */
    private void doVerifyUserinfor() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doVerifyUserinforResult((String) arg0);
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
        xwzxDAL.doVerifyUserinfor(!Constants.iflogin ? "" : user.getUserId(), callBack);
    }

    /**
     * 验证身份信息的结果处理
     */
    private void doVerifyUserinforResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                verifyFlag = 1; // 信息完整
                mIsWs = true;
                return;
            } else {
                verifyFlag = 2; // 信息不完整
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }

    /**
     * 报名
     */
    private void doBm() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        PxDetailsActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                doBmResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                commonUtil.shortToast("报名失败! 请检查网络");
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.doBm(!Constants.iflogin ? "" : user.getUserId(), class_id, callBack);
    }

    /**
     * 报名结果的处理
     *
     * @param json
     */
    private void doBmResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast("报名成功!");
                isEnroll("1","0");
                return;
            } else {
                commonUtil.shortToast("报名失败!");
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }
}
