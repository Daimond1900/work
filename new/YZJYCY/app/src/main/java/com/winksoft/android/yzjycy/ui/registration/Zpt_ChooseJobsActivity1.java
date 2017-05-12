package com.winksoft.android.yzjycy.ui.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.data.RecruitmentDAL;
import com.winksoft.android.yzjycy.ui.recruitInfor.QztRecuitInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.ListViewUtil;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * ClassName:ChooseJobsActivity1 Description：招聘登记-选择招聘岗位（一级列表）
 *
 * @author Administrator Date：2012-10-19
 */
public class Zpt_ChooseJobsActivity1 extends BaseListActivity {
    private static final String TAG = "Zpt_ChooseJobs";
    private Button backBtn;
    private ListViewUtil util;
    private YFBaseAdapter adapter;
    private QyDAL qyDAL;
    private int pageNum = 0, lastItem = 0;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    private boolean isLoading = true;// 标志正在加载数据
    Dialog proDialog;
    private CommonUtil commonUtil;
    private Map<String,String> postMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zpt_recruitment_choose_jobs_list);

        qyDAL = new QyDAL(this);

        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Zpt_ChooseJobsActivity1.this.finish();
            }
        });
        doSubmitData();
        intiListview(false, true);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (SURPERDATA.size() > 0) {
                        String postId = SURPERDATA.get(arg2 - 1).get("aca111").toString();
                        String post_id = postId.substring(0, 1);
                        Intent intent = new Intent(Zpt_ChooseJobsActivity1.this, Zpt_ChooseJobsActivity2.class);
                        intent.putExtra("jobsId", post_id);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doSubmitData() {
        postMap = new HashMap<>();
        postMap.put("page", SUPERPAGENUM + "");
        postMap.put("aca11a", "1" + "");
        postMap.put("aca111", "");
    }

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_recruitment_choose_jobs_list_item1_1,
                new String[]{"itemPost"}, new int[]{R.id.item_postTxt}, getResources()) {
            @Override
            protected void iniview(View view, int position,
                                   List<? extends Map<String, ?>> data) {
            }
        };
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
                        Zpt_ChooseJobsActivity1.this, "加载中,请稍后...");
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
        qyDAL.doZwgwQuery(postMap, callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        Log.d(TAG, "map : "+ map);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "jobs");
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
                commonUtil.shortToast("查询失败!");
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("查询失败!");
        }
    }


    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("itemPost", tm.get("aca112") == null ? "" : tm.get("aca112"));
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }

}