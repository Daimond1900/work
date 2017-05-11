package com.winksoft.android.yzjycy.ui.pxxx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.data.pxDAL;
import com.winksoft.android.yzjycy.newyzjycy.pxkq.me.KqjlInfoActivity;
import com.winksoft.android.yzjycy.newyzjycy.wd.wjdc.WjdcListActivity;
import com.winksoft.android.yzjycy.newyzjycy.map.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的培训详情
 */
public class KqInfoDetailsActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "KqInfoDetail";
    private Button back, bt_kq, bt_qt, bt_kqjl,bt_wjdc;
    pxDAL dal;
    private YFBaseAdapter adapter;
    private String class_id = "", course_id = "";
    private int postId;
    private TextView bjmc, pxjg, pxgz, pxlx, pxdj;
    private Map<String, String> isKq;
    private String isKqStr = "0", isQtStr = "0";
    private String isCheck = "1";
    Dialog proDialog;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private int a = 0;
    private MyListView1 listView;
    private List<Map<String, String>> listCourseInfo;
    YFBaseAdapter yfbaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmkqinfo);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        initView();



           /*异常情况刷新*/
        findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost();
//                loadDate();
            }
        });
    }


    private void initView() {
        bt_kqjl = (Button) findViewById(R.id.bt_kqjl);  //考勤记录按钮
        bt_kqjl.setOnClickListener(this);
        bt_wjdc = (Button) findViewById(R.id.bt_wjdc);  //问卷调查按钮
        bt_wjdc.setOnClickListener(this);

        bjmc = (TextView) findViewById(R.id.bjmc);
        pxjg = (TextView) findViewById(R.id.pxjg);
        pxgz = (TextView) findViewById(R.id.pxgz);
        pxlx = (TextView) findViewById(R.id.pxlx);
        pxdj = (TextView) findViewById(R.id.pxdj);
        dal = new pxDAL(this, new Handler());
        back = (Button) findViewById(R.id.back);
        back.requestFocus();
        back.setOnClickListener(this);
        bt_kq = (Button) findViewById(R.id.bt_kq);
        bt_kq.setOnClickListener(this);
        bt_qt = (Button) findViewById(R.id.bt_qt);
        bt_qt.setOnClickListener(this);
        class_id = this.getIntent().getStringExtra("class_id");
        isKq = new HashMap<String, String>();
        listView = (MyListView1) findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back:
                KqInfoDetailsActivity.this.finish();
                break;
            case R.id.bt_kq: // 考勤
                Log.d(TAG, "onClick: 考勤按钮 = " + isKqStr);
                if ("0".equals(isKqStr) && !"".equals(course_id)) { // "0" --> kq
                    doIsKqInfo();
                } else if ("1".equals(isKqStr) && "0".equals(isQtStr)) {    // "1" --> qt
                    doIsQtInfo();
                } else if ("0".equals(isKqStr) && "".equals(course_id)) {
                    commonUtil.shortToast("考勤时间未到！");
                } else if ("1".equals(isKqStr) && "1".equals(isQtStr)) {    // "1" --> qt
                    commonUtil.shortToast("您已签退！");
                }
                break;
            case R.id.bt_kqjl: // 考勤记录
                intent = new Intent(this, KqjlInfoActivity.class);
                intent.putExtra("class_id", class_id);
                startActivity(intent);
                break;
            case R.id.bt_wjdc: // 问卷调查
                intent = new Intent(this, WjdcListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doPost();
    }


    private void doIsKqInfo() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                isKqInfoResult((String) arg0);
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
        xwzxDAL.isKqInfo(course_id, callBack);
    }

    private void doIsQtInfo() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                isKqInfoResult((String) arg0);
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
        xwzxDAL.isQtInfo(course_id, callBack);
    }

    private void isKqInfoResult(String json) {
        Log.d(TAG, "isKqInfoResult: 判断时候可以考勤 = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("false")) {
                commonUtil.shortToast(map.get("msg"));
            } else if (map.get("success").equals("true")) {
                Intent intent = new Intent(KqInfoDetailsActivity.this, KqInfoSureActivity.class);
                intent.putExtra("class_id", class_id);
                intent.putExtra("course_id", course_id);
                intent.putExtra("ischeck", isCheck);
                startActivity(intent);
            } else {
                commonUtil.shortToast("数据加载失败!");
            }
        }
    }


    private void doPost() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        KqInfoDetailsActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        xwzxDAL.doKqInfoDetails(class_id, callBack);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void postResult(String json) {
        Log.d(TAG, "postResult: 考勤信息详情返回值 = " + json);
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "postResult: map = " + map);
        if (map != null) {
            if (map.get("success").equals("false")) {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
            } else if (map.get("success").equals("true")) {
                Map<String, String> baseInfo = DataConvert.toConvertStringMap(json, "baseInfo");
                if (baseInfo != null) {
                    bjmc.setText(commonUtil.getMapValue(baseInfo, "class_name"));
                    pxjg.setText(commonUtil.getMapValue(baseInfo, "training_agent_name"));
                    pxlx.setText(commonUtil.getMapValue(baseInfo, "training_type"));
                    pxgz.setText(commonUtil.getMapValue(baseInfo, "training_job_type"));
                    pxdj.setText(commonUtil.getMapValue(baseInfo, "training_level"));

                    course_id = commonUtil.getMapValue(baseInfo, "course_id"); // 考勤用的

                    // isCheckIn 是否签到       是否签到（0未签到，1已签到）
                    // isCheckOut 是否签退      是否签退（0未签退，1已签退）

                    isKqStr = commonUtil.getMapValue(baseInfo, "ischeckin"); // 0:未考勤  1:考完勤

                    isQtStr = commonUtil.getMapValue(baseInfo, "ischeckout"); // 0未签退，1已签退


                    if ("1".equals(isKqStr) && "0".equals(isQtStr)) {
                        bt_kq.setText("签退");
                        isCheck = "2";
                    } else if ("0".equals(isKqStr) && !"".equals(course_id)) {
                        bt_kq.setText("考勤");
                        isCheck = "1";
                    } else if ("0".equals(isKqStr) && "".equals(course_id)) {
                        bt_kq.setBackgroundDrawable(getResources().getDrawable(R.drawable.heise_button));
                    } else if ("1".equals(isKqStr) && "1".equals(isQtStr)) {
                        bt_kq.setText("已签退");
                        bt_kq.setBackgroundDrawable(getResources().getDrawable(R.drawable.heise_button));
                    }

                    // 课表
                    listCourseInfo = DataConvert.toConvertStringList(json, "courseInfo");
                    if (listCourseInfo != null && listCourseInfo.size() > 0) {
                        yfbaseAdapter = new YFBaseAdapter(this, listCourseInfo, R.layout.listcourseinfo_item,
                                new String[]{"lesson_content", "lesson_teacher", "room_loc", "starting_time", "ending_time"},
                                new int[]{R.id.kcnr, R.id.skr, R.id.dz, R.id.kssj, R.id.jssj}, getResources()) {
                            @Override
                            protected void iniview(View view, int position,
                                                   List<? extends Map<String, ?>> data) {

                                Log.d(TAG, "iniview: Data课程表 = " + data);
                                Map<String, String> map = (Map<String, String>) data.get(position);
                                String n_id = commonUtil.getMapValue(map, "id"); // 0未签退，1已签退
                                if (course_id == null || n_id == null) {
                                    return;
                                }
                                TextView kqzt = (TextView) view.findViewById(R.id.kqzt);
                                if (!n_id.equals(course_id)) {
                                    kqzt.setVisibility(View.GONE);
                                    return;
                                }
                                if (n_id.equals(course_id) && "0".equals(isKqStr) && !"".equals(course_id)) {
                                    // 提示可以考勤
                                    kqzt.setVisibility(View.VISIBLE);
                                    kqzt.setText("可以签到");
                                }
                                if (n_id.equals(course_id) && "1".equals(isKqStr) && "0".equals(isQtStr)) {
                                    // 提示可以签退
                                    kqzt.setVisibility(View.VISIBLE);
                                    kqzt.setText("可以签退");
                                }
                                if (n_id.equals(course_id) && "1".equals(isKqStr) && "1".equals(isQtStr)) {
                                    // 提示可以签退
                                    kqzt.setVisibility(View.VISIBLE);
                                    kqzt.setText("完成考勤");
                                }
                            }
                        };
                        listView.setAdapter(yfbaseAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                try {
                                    if (arg2 == listCourseInfo.size()) {
                                        return;
                                    }
                                    String room_loc_xy = commonUtil.getMapValue(listCourseInfo.get(arg2), "room_loc_xy");
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
                                        Intent intent = new Intent(KqInfoDetailsActivity.this, ZptMapInfoActivity.class);
                                        intent.putExtra("title", "开课地点");
                                        intent.putExtra("lat", lat);
                                        intent.putExtra("lot", lot);
                                        startActivity(intent);
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
                                }
                            }
                        });
                    }

                } else {
                    commonUtil.shortToast("数据加载失败!");
                }
            }
        }
    }
}
