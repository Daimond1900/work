package com.winksoft.android.yzjycy.ui.zerotransfer;

import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.ZeroTransferDAL;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.util.ReJson;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author user_ygl
 */
public class TansferDetail extends BaseActivity {

    private EditText sfz, jbrq, jbr, jbjg, zt;
    private TextView hzxm, ldls, lxdh;
    private TextView bz;
    private Button back;
    private Button save;
    private String finalString;
    private ZeroTransferDAL dal;
    private String intentCode;
    private String id;
    private boolean ifmodify;
    private String sfzStr,hzxmStr,  ldlsStr, lxdhStr, bzStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jcy_lzy_main_edit);
        intentCode = getIntent().getStringExtra("code");
        id = getIntent().getStringExtra("id");
        ifmodify = getIntent().getBooleanExtra("ifmodify", false);
        //,,  , , ;
        sfzStr = sfz.getText().toString();
        hzxmStr = hzxm.getText().toString();
        lxdhStr = lxdh.getText().toString();
        ldlsStr = ldls.getText().toString();
        bzStr = bz.getText().toString();

        initView();

        dal = new ZeroTransferDAL(this, new Handler());
        if (ifmodify)
            BASEHANDLER = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (finalString != null && !finalString.equals(getAllString())) {
                        new AlertDialog.Builder(TansferDetail.this)
                                .setTitle("提示")
                                .setMessage("信息有变更，是否保存？")
                                .setPositiveButton("取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                System.gc(); // 提醒系统及时回收
                                                TansferDetail.this.finish();
                                                ActivityStackControlUtil
                                                        .remove(TansferDetail.this);
                                            }
                                        })
                                .setNegativeButton("保存",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                            }
                                        }).show();
                    } else {
                        System.gc(); // 提醒系统及时回收
                        TansferDetail.this.finish();
                        ActivityStackControlUtil.remove(TansferDetail.this);
                    }
                }
            };
    }

    private void initView() {
        back = (Button) findViewById(R.id.back);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Boolean>() {
                    // sfzStr,hzxmStr,  ldlsStr, lxdhStr, bzStr;
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        ReJson json = dal.modifyZeroTransferDetail(id, sfzStr,
                                hzxmStr,
                                ldlsStr,
                                lxdhStr,
                                bzStr,
                                TansferDetail.this.user.getUserId(),
                                TansferDetail.this.user.getDepartment_name(),
                                TansferDetail.this.user.getOrgid(),
        /*需要修改*/
                                "");    //TansferDetail.this.user.getZzs051()
                        return json.isSuccess();
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        save.setEnabled(true);
                        progressDialog.dismiss();
                        if (result) {
//							Toast.makeText(TansferDetail.this, "修改成功。",
//									Toast.LENGTH_SHORT).show();
                            TansferDetail.this.finish();
                        } else {
//							Toast.makeText(TansferDetail.this, "修改失败。",
//									Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        save.setEnabled(false);
                        progressDialog = TansferDetail.this.commonUtil.showProgressDialog(save.getText() + "零转移信息中...");

                    }

                    ProgressDialog progressDialog;

                }.execute();
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TansferDetail.this.finish();

            }
        });

        sfz = (EditText) findViewById(R.id.sfz);
        hzxm = (TextView) findViewById(R.id.hzxm);
        ldls = (TextView) findViewById(R.id.ldls);
        lxdh = (TextView) findViewById(R.id.lxdh);
        bz = (TextView) findViewById(R.id.bz);
        jbrq = (EditText) findViewById(R.id.jbrq);
        jbr = (EditText) findViewById(R.id.jbr);
        jbjg = (EditText) findViewById(R.id.jbjg);
        zt = (EditText) findViewById(R.id.zt);

        new AsyncTask<Void, Void, Map<String, String>>() {

            @Override
            protected Map<String, String> doInBackground(Void... params) {
                System.out.println(intentCode);
                ReJson json = dal.queryZeroTransferDetail(intentCode);
                if (json.isSuccess())
                    return json.getMap();
                else
                    return null;

            }

            @Override
            protected void onPostExecute(Map<String, String> result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                // System.out.println(result);
                if (result == null) {
                    sfz.setText(intentCode);
                    save.setText("添加");

                } else {
                    save.setText("修改");
                    sfz.setText(result.get("hzsfz"));
                    // hzxm,ldls,lxdh,bz,jbrq,jbr,jbjg,zt;
                    hzxm.setText(result.get("hzxm"));
                    ldls.setText(result.get("ldls"));
                    lxdh.setText(result.get("lxdh"));
                    bz.setText(result.get("bz"));
                    jbrq.setText(result.get("aae036"));
                    jbr.setText(result.get("aae019"));
                    jbjg.setText(result.get("aae020"));

                    if (result.get("state1").equals("0")) {
                        zt.setText("未审核");
                    } else if (result.get("state1").equals("1")) {
                        zt.setText("已审核");
                    } else if (result.get("state1").equals("2")) {
                        zt.setText("已注销");
                    }

                }

                finalString = getAllString();
                setEdittext(sfz);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = TansferDetail.this.commonUtil.showProgressDialog("数据加载中...");

            }

            ProgressDialog progressDialog;

        }.execute();

    }

    private String getAllString() {
        return sfz.getText().toString() + "," + hzxm.getText().toString() + ","
                + ldls.getText().toString() + "," + lxdh.getText().toString()
                + "," + bz.getText().toString();
    }

    private void setEdittext(EditText... edittext) {

        for (EditText et : edittext) {
            if (ifmodify)
                et.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if (!finalString.equals(getAllString()))
                            save.setVisibility(View.VISIBLE);
                        else
                            save.setVisibility(View.GONE);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            else et.setEnabled(false);
        }
    }
}
