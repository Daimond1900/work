package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.List;
import java.util.Map;

/**
 * 简历预览
 * Created by 1900 on 2017/4/13.
 */

public class NewWdjlActivity extends BaseActivity implements View.OnClickListener {
    private TextView xm, xb, xl, mz, zzmm, sxzy, csrq, hyzk, byyx, sg, tz, jsjsp, byrq, zwjs, jtzz, gddh, sjhm, dzyj;
    private MyListView1 jyjl, pxjl, yynl, jszc, gzjl, qzyx;
    private Button btXiuGai, xz;
    XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    Dialog proDialog;
    private List<Map<String, String>> gzjlList, jyjlList, pxjlList, yynlList, jszcList, qzyxList;
    private Map<String, String> jcxxList;
    private ImageView wsj_img , head;
    private TextView wsj_tv;
    private ScrollView islayout;
    private ImageLoader im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wdjl);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        im = ImageLoader.getInstance();
        initView();
        im.init(ImageLoaderConfiguration.createDefault(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDate();
    }

    private void initView() {
        head = (ImageView) findViewById(R.id.head);/*头像*/
        xz = (Button) findViewById(R.id.xz);
        xz.setOnClickListener(this);

        wsj_img = (ImageView) findViewById(R.id.wsj_img);
        wsj_tv = (TextView) findViewById(R.id.wsj_tv);
        islayout = (ScrollView) findViewById(R.id.islayout);
        btXiuGai = (Button) findViewById(R.id.btX);
        btXiuGai.setOnClickListener(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        xm = (TextView) findViewById(R.id.xm);
        xb = (TextView) findViewById(R.id.xb);
        xl = (TextView) findViewById(R.id.xl);
        mz = (TextView) findViewById(R.id.mz);
        zzmm = (TextView) findViewById(R.id.zzmm);
        sxzy = (TextView) findViewById(R.id.sxzy);
        csrq = (TextView) findViewById(R.id.csrq);
        hyzk = (TextView) findViewById(R.id.hyzk);
        byyx = (TextView) findViewById(R.id.byyx);
        sg = (TextView) findViewById(R.id.sg);
        tz = (TextView) findViewById(R.id.tz);
        jsjsp = (TextView) findViewById(R.id.jsjsp);
        byrq = (TextView) findViewById(R.id.byrq);
        zwjs = (TextView) findViewById(R.id.zwjs);
        jtzz = (TextView) findViewById(R.id.jtzz);
        gddh = (TextView) findViewById(R.id.gddh);
        sjhm = (TextView) findViewById(R.id.sjhm);
        dzyj = (TextView) findViewById(R.id.dzyj);

        jyjl = (MyListView1) findViewById(R.id.jyjlListview);
        pxjl = (MyListView1) findViewById(R.id.pxjlListview);
        jszc = (MyListView1) findViewById(R.id.jszcListview);
        gzjl = (MyListView1) findViewById(R.id.gzjlListview);
        qzyx = (MyListView1) findViewById(R.id.qzyxListview);
        yynl = (MyListView1) findViewById(R.id.yynlListview);
    }


    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        NewWdjlActivity.this, "加载中,请稍后...");
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
                xz.setVisibility(View.GONE);
                islayout.setVisibility(View.GONE);
                wsj_img.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.VISIBLE);
                wsj_tv.setText("暂无数据，下拉刷新！");
                wsj_img.setVisibility(View.GONE);
                wsj_tv.setVisibility(View.GONE);
                if (proDialog != null)
                    proDialog.dismiss();
            }
        };
        xwzxDAL.doJlcx(user.getUserId(), callBack);
    }

    /**
     * @param json 成功返回的json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {

                String isYouJl = commonUtil.getMapValue(DataConvert.toConvertStringMap(json, "row"), "resume");
                if ("0".equals(isYouJl)) { // 无简历
                    xz.setVisibility(View.VISIBLE);
                    wsj_img.setVisibility(View.VISIBLE);
                    wsj_tv.setVisibility(View.VISIBLE);
                    wsj_tv.setText("你还没有简历呢！赶快去新增吧！");
                    btXiuGai.setVisibility(View.GONE);
                    islayout.setVisibility(View.GONE);
                } else if ("1".equals(isYouJl)) {
                    xz.setVisibility(View.GONE);
                    wsj_img.setVisibility(View.GONE);
                    wsj_tv.setVisibility(View.GONE);
                    btXiuGai.setVisibility(View.VISIBLE);
                    islayout.setVisibility(View.VISIBLE);
                    jcxxList = DataConvert.toConvertStringMap(json, "jcxx");//  基础信息
                    formatInfo(1);
                    qzyxList = DataConvert.toConvertStringList(json, "qzyx"); // 求职意向
                    formatInfo(2);
                    gzjlList = DataConvert.toConvertStringList(json, "gzjl");
                    formatInfo(3);
                    jszcList = DataConvert.toConvertStringList(json, "jszc");
                    formatInfo(4);
                    jyjlList = DataConvert.toConvertStringList(json, "jyjl");
                    formatInfo(5);
                    pxjlList = DataConvert.toConvertStringList(json, "pxjl");
                    formatInfo(6);
                    yynlList = DataConvert.toConvertStringList(json, "yynl");
                    formatInfo(7);
                }
            }
        }
    }

    private void formatInfo(final int i) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                formatData(i);
            }
        }, 1000);
    }

    private void formatData(int i) {
        /*-------------------基础信息------------------------------*/
        if (i == 1) { //
            if (jcxxList != null) {
                xm.setText(commonUtil.getMapValue(jcxxList, "aac003"));//姓名
                xb.setText(commonUtil.getMapValue(jcxxList, "aac004"));//性别
                xl.setText(commonUtil.getMapValue(jcxxList, "aac011"));//学历
                mz.setText(commonUtil.getMapValue(jcxxList, "aac005")); //民族
                zzmm.setText(commonUtil.getMapValue(jcxxList, "aac024"));//政治面貌
                sxzy.setText(commonUtil.getMapValue(jcxxList, "aac183"));//所学专业
                csrq.setText(commonUtil.getMapValue(jcxxList, "aac006"));//出生日期
                hyzk.setText(commonUtil.getMapValue(jcxxList, "hyzk"));//婚姻状况
                byyx.setText(commonUtil.getMapValue(jcxxList, "byyx"));//毕业院校
                sg.setText(commonUtil.getMapValue(jcxxList, "aac034"));//身高
                tz.setText(commonUtil.getMapValue(jcxxList, "aac035"));//体重
                jsjsp.setText(commonUtil.getMapValue(jcxxList, "aac179"));//计算机水平
                byrq.setText(commonUtil.getMapValue(jcxxList, "bysj"));//毕业日期
                zwjs.setText(commonUtil.getMapValue(jcxxList, "zwjs"));//自我介绍
                jtzz.setText(commonUtil.getMapValue(jcxxList, "aac026"));//家庭住址
                gddh.setText(commonUtil.getMapValue(jcxxList, "aae005"));//固定电话
                sjhm.setText(commonUtil.getMapValue(jcxxList, "acb501"));//手机号码
                dzyj.setText(commonUtil.getMapValue(jcxxList, "aae159"));//电子信箱

                /*----------------------------------------------------------------------*/
                /*头像的获取*/
                String headUrl = commonUtil.getMapValue(jcxxList, "head");/*头像地址*/
                if(headUrl !=null && !"".equals(headUrl)){
                    im.displayImage(Constants.IP+headUrl,head);
                }
                /*----------------------------------------------------------------------*/
            }
        }
        /*-------------------求职意向------------------------------*/
        if (i == 2) { //
            // 职位描述
            SimpleAdapter adapter = new SimpleAdapter(this, qzyxList, R.layout.qzyx_list_item,
                    new String[]{"acb242", "aca111", "aab301", "aab019", "acc201", "aab020"},
                    new int[]{R.id.yxyq, R.id.gwmc, R.id.gzdd, R.id.dwlx, R.id.gzxz, R.id.jjlx});
            qzyx.setAdapter(adapter);
        }
         /*-------------------工作经历 ------------------------------*/
        if (i == 3) {
            if (gzjlList != null && gzjlList.size() > 0) {

                SimpleAdapter adapter = new SimpleAdapter(this, gzjlList, R.layout.gzjl_list_item,
                        new String[]{"qsrq", "jsrq", "aab004", "pxnr", "gzbz"},
                        new int[]{R.id.qssj, R.id.jssj, R.id.dwmc, R.id.gzbz});
                gzjl.setAdapter(adapter);
            }
        }
            /*-------------------技术特长 ------------------------------*/
        if (i == 4) {
            if (jszcList != null && jszcList.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, jszcList, R.layout.jszc_list_item,
                        new String[]{"aca111", "aac015", "csnx", "bfrq"},
                        new int[]{R.id.jsmc, R.id.jsdj, R.id.csnx, R.id.bfrq});
                jszc.setAdapter(adapter);
            }
        }
     /*-------------------教育经历 ------------------------------*/
        if (i == 5) {
            if (jyjlList != null && jyjlList.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, jyjlList, R.layout.jyjl_list_item,
                        new String[]{"qsrq", "jsrq", "byyx", "aac183"},
                        new int[]{R.id.qssj, R.id.jssj, R.id.byxx, R.id.sxzy});
                jyjl.setAdapter(adapter);
            }
        }
         /*-------------------培训经历 ------------------------------*/
        if (i == 6) {
            if (pxjlList != null && pxjlList.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, pxjlList, R.layout.pxjg_list_item,
                        new String[]{"qsrq", "jsrq", "aab004", "pxnr", "pxjg"},
                        new int[]{R.id.qssj, R.id.jssj, R.id.byxx, R.id.sxzy, R.id.pxjg});
                pxjl.setAdapter(adapter);
            }
        }
        /*-------------------语言能力------------------------------*/
        if (i == 7) {
            if (yynlList != null && yynlList.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, yynlList, R.layout.yynl_list_item,
                        new String[]{"wyzl", "aac175"},
                        new int[]{R.id.yz, R.id.sp});
                yynl.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.btX:
                Intent intent = new Intent(this, ModifyJLActivity.class);
                intent.putExtra("pdbt", 1);
                startActivity(intent);
                break;
            case R.id.xz:
                Intent intent1 = new Intent(this, ModifyJLActivity.class);
                intent1.putExtra("pdbt", 0);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
