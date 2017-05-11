package com.winksoft.android.yzjycy.ui.zerotransfer;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.MyBaseAdapter;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.ui.ldlxx.MainList;
import com.winksoft.android.yzjycy.ui.ldlxx.ParseData;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.IDCard;
import com.winksoft.android.yzjycy.data.ZeroTransferDAL;
import com.winksoft.android.yzjycy.ui.ldlxx.CreateLdlDetail;
import com.winksoft.android.yzjycy.util.ReJson;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TransferList extends BaseListActivity {
    private YFBaseAdapter adapter;
    private ZeroTransferDAL dal;
    private Button back, save, searchbut;
    private EditText code;
    String[] items1 = {"零转移家庭信息", "删除零转移家庭", "注销零转移家庭"};
    String[] items2 = {"零转移家庭信息", "删除零转移家庭"};
    int i = 0;
    private Dialog proDialog;
    private CommonUtil commonUtil;
    private JcDAL jcDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcy_zero_transfer_list);
        jcDAL = new JcDAL(this);
        commonUtil = new CommonUtil(this);
        dal = new ZeroTransferDAL(this, new Handler());
        back = (Button) findViewById(R.id.back);
        searchbut = (Button) findViewById(R.id.searchbut);
        code = (EditText) findViewById(R.id.code);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferList.this.finish();
            }
        });
        searchbut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbut.setEnabled(false);
                SUPERPAGENUM = 0;
                loadDate();
            }
        });
        intiListview(false,true);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 0 || arg2 == (SURPERDATA.size() + 1))
                    return;
                String state1 = (String) SURPERDATA.get(arg2 - 1).get("state1");
                if (state1 == null)
                    return;
                String intentCode = (String) SURPERDATA.get(arg2 - 1).get(
                        "hzsfz");
                String id = (String) SURPERDATA.get(arg2 - 1).get("id");


                if (intentCode != null) {
                    if (state1.equals("状态:正常"))
                        showDialog(intentCode, id, true, items1);
                    else if (state1.equals("状态:已审核"))
                        showDialog(intentCode, id, false, items1);
                    else if (state1.equals("状态:已注销"))
                        showDialog(intentCode, id, false, items2);
                }

            }
        });
    }

    void showDialog(final String code, final String id, final boolean ifmodify,
                    String[] item) {

        Intent intent = new Intent(TransferList.this, TansferDetail.class);
        intent.putExtra("code", code);
        intent.putExtra("id", id);
        intent.putExtra("ifmodify", ifmodify);
        startActivity(intent);

    }

    /*新加的*/
    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.jcy_recruitment_list_item, new String[]{"hzxm",
                "state1", "hzsfz", "aae036"}, new int[]{
                R.id.workTxt, R.id.moneyTxt, R.id.companyTxt,
                R.id.dateTxt}, getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

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
                        TransferList.this, "加载中,请稍后...");
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
        jcDAL.doLzyQuery(SUPERPAGENUM, code.getText().toString(), callBack);
    }

    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "families");
                onListviewSuccess();
                return;
            } else {
                commonUtil.shortToast("没有记录");
                onListviewNoResult();
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }

    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            if (tm.get("state1") != null) {
                if (tm.get("state1").equals("0")) {
                    tm.put("state1", "状态:正常");
                } else if (tm.get("state1").equals("1")) {
                    tm.put("state1", "状态:已审核");
                } else if (tm.get("state1").equals("2")) {
                    tm.put("state1", "状态:已注销");
                }
            }
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }

    @Override
    protected void myNotifyDataSetChanged() {
        searchbut.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

}
