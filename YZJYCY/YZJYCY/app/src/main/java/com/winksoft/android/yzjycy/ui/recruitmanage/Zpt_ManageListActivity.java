package com.winksoft.android.yzjycy.ui.recruitmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.ManageDAL;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

/**
 * ClassName:ManageListActivity
 * Description：招聘管理-列表
 *
 * @author Administrator
 *         Date：2012-10-23
 */
public class Zpt_ManageListActivity extends BaseListActivity {
    private static final String TAG = "ZptManageList";
    private EditText searchEdt;
    private Button backBtn, refreshBtn, searchBtn;
    private Button ysh_tabBtn, wsh_tabBtn;
    private YFBaseAdapter adapter;
    private ManageDAL manageDAL;

    private QyDAL qyDAL;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    private boolean isLoading = true;// 标志正在加载数据
    private int flag = 0;//0默认已审核, 1未审核
    Dialog proDialog;
    private CommonUtil commonUtil;
    private int f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_manage_list);
        f = 0;
        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
        searchEdt = (EditText) findViewById(R.id.searchEdt);
        MyClick myClick = new MyClick();
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(myClick);
        refreshBtn = (Button) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(myClick);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(myClick);
        ysh_tabBtn = (Button) findViewById(R.id.ysh_tabBtn);
        ysh_tabBtn.setOnClickListener(myClick);

        wsh_tabBtn = (Button) findViewById(R.id.wsh_tabBtn);
        wsh_tabBtn.setOnClickListener(myClick);

        intiListview(false,true);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Map<String, Object> map = SURPERDATA.get(arg2 -1);
                    Intent intent = new Intent();
                    intent.setClass(Zpt_ManageListActivity.this, Zpt_PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("s_post", map.get("aca112").toString());// 岗位
                    bundle.putString("s_gwsm", map.get("acb216").toString());    //岗位说明
                    bundle.putString("s_zpnx", map.get("acb211").toString());    //招聘男性
                    bundle.putString("s_zpvx", map.get("acb212").toString());    //招聘女性
                    bundle.putString("s_jz", map.get("acb213").toString());//兼招
                    bundle.putString("s_ygz", map.get("acc034").toString());    //月工资
                    bundle.putString("s_wage", map.get("acc214") == null ? "" : map.get("acc214").toString());// 待遇
                    bundle.putString("s_require", map.get("aae013") == null ? "" : map.get("aae013").toString());// 要求
                    bundle.putString("s_start_date", map.get("aae030").toString());// 起始时间
                    bundle.putString("s_end_date", map.get("aae031").toString());// 有效期
                    bundle.putString("s_id", map.get("acb210").toString());// 招聘岗位的id
                    bundle.putString("s_name", map.get("name").toString());// 用工区域
                    if (flag == 0) {//已发布不可修改
                        bundle.putString("s_sh", "0");
                    } else {//未发布
                        bundle.putString("s_sh", map.get("acb208").toString());// 是否审核
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 按钮切换
     * @param btn
     */
    private void switchMenu(Button btn) {
        if (btn.getId() == R.id.ysh_tabBtn) {
            ysh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
            wsh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
            flag = 0;//已审核
            SUPERPAGENUM = 0;
            loadDate();
        } else if (btn.getId() == R.id.wsh_tabBtn) {
            ysh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button_));
            wsh_tabBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.zpt_register_tab_button));
            flag = 1;//未审核
            SUPERPAGENUM = 0;
            loadDate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        f += 1;
        if(f == 1){
            return;
        }
        SUPERPAGENUM = 0;
        loadDate();
        f = 1;
    }

    private class MyClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    Zpt_ManageListActivity.this.finish();
                    break;
                case R.id.refreshBtn:
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                case R.id.searchBtn:
                    SUPERPAGENUM = 0;
                    loadDate();
                    break;
                case R.id.ysh_tabBtn:
                    switchMenu(ysh_tabBtn);
                    break;
                case R.id.wsh_tabBtn:
                    switchMenu(wsh_tabBtn);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected BaseAdapter setAdapter() {


        if (flag == 1) {//未审核显示图标
            adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_manage_list_item,
                    new String[]{"itemJobs", "itemAudit", "itemWage", "itemDate"},
                    new int[]{R.id.item_jobsTxt, R.id.item_auditImg, R.id.item_wageTxt, R.id.item_dateTxt}, getResources()) {
                @Override
                protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

                }
            };
        } else {//已审核不显示图标
            adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_manage_list_item,
                    new String[]{"itemJobs", "itemWage", "itemDate"},
                    new int[]{R.id.item_jobsTxt, R.id.item_wageTxt, R.id.item_dateTxt}, getResources()){
                @Override
                protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

                }
            };
        }
        return adapter;
    }

    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();

                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        Zpt_ManageListActivity.this, "加载中,请稍后...");
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
                onListviewonFailure();
            }
        };
        qyDAL.doZpglQuery(SUPERPAGENUM,commonUtil.doConvertEmpty(searchEdt.getText().toString()), flag + "",callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
//                showToast(commonUtil.getMapValue(map, "msg"), false);
                STRINGLIST = DataConvert.toConvertStringList(json, "recruitments");
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
                commonUtil.shortToast(commonUtil.getMapValue(map, "msg"));
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("查询失败!");
        }
    }

    @Override
    protected void myNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void formatData() {
        Log.d(TAG, "formatData: STRINGLIST: " + STRINGLIST);
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("itemJobs", tm.get("acb216"));// 岗位
            String auditTag = tm.get("acb208").toString();// 是否审核：0.未审核；1.已审核
            Bitmap auditStr = BitmapFactory.decodeResource(getResources(), R.mipmap.zpt_wsh);
            if ("1".equals(auditTag)) {
                auditStr = BitmapFactory.decodeResource(getResources(), R.mipmap.zpt_wsh);
            } else {
                auditStr = BitmapFactory.decodeResource(getResources(), R.mipmap.zpt_ysh);
            }
            otm.put("itemAudit", auditStr);

            tm.put("itemWage", "待遇：" + tm.get("acc034") == null ? "" : tm.get("acc034"));// 待遇
            tm.put("itemDate", tm.get("aae030")); // 发布时间

            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
            Log.d(TAG, "SURPERDATA: " + SURPERDATA);
        }
    }
}
