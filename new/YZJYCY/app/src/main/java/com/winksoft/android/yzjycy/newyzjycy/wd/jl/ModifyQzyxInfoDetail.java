package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.List;
import java.util.Map;

/**
 * 求职意向 修改
 */
public class ModifyQzyxInfoDetail extends BaseActivity implements View.OnClickListener {
    private EditText gzms, yxyq;
    private Spinner jjlx, dwlx, gzdd, gzxz, gwmc;
    private XwzxDAL xwzxDAL;
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private int flag;
    private String qzId, dwlxStr, jjlxStr, gzddStr, gzxzStr, gwmcStr;
    private String dwlxId, jjlxId, gzddId, gzxzId, gwmcId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyqzyxinfodetail);

        flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {  //新增
            ((TextView) findViewById(R.id.textView2)).setText("新增求职意向");
        }
        initView();
    }

    private void postBc() {
        String gzms1 = gzms.getText().toString().trim();
        String yxyq1 = yxyq.getText().toString().trim();

        if (flag == 0) {
            postDataXz(user.getUserId(), dwlxId, jjlxId, gzddId, gzxzId, gwmcId, gzms1, yxyq1);
        } else {
            postData(qzId, dwlxId, jjlxId, gzddId, gzxzId, gwmcId, gzms1, yxyq1);
        }
    }

    private void postDataXz(String userid, String dwlxId, String jjlxId, String gzddId, String gzxzId, String gwmcId, String gwms, String yxyq) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyQzyxInfoDetail.this, "正在查询中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postDataXzResult((String) arg0);
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
        xwzxDAL.doXzQzyx(userid, dwlxId, jjlxId, gzddId, gzxzId, gwmcId, gwms, yxyq,
                callBack);
    }

    private void postDataXzResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }

    private void postData(String qzid, String dwlxId, String jjlxId, String gzddId, String gzxzId, String gwmcId, String gwms, String yxyq) {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyQzyxInfoDetail.this, "正在查询中,请稍后...");
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
        xwzxDAL.doModifyQzyx(qzid, dwlxId, jjlxId, gzddId, gzxzId, gwmcId, gwms, yxyq,
                callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }

    private void initView() {
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);

        jjlx = (Spinner) findViewById(R.id.jjlx);
        dwlx = (Spinner) findViewById(R.id.dwlx);
        gzdd = (Spinner) findViewById(R.id.gzdd);
        gzxz = (Spinner) findViewById(R.id.gzxz);
        gwmc = (Spinner) findViewById(R.id.gwmc);

        gzms = (EditText) findViewById(R.id.gwms);
        yxyq = (EditText) findViewById(R.id.yxyq);

        loadSpnerDate("AAB019");      // 单位类型
        loadSpnerDate("AAB020");      // 经济类型
        loadSpnerDate("AAB301");      // 工作地点
        loadSpnerDate("ACC201");      // 工作性质
        loadSpnerDate("ACA111");      // 岗位名称


        // 工作编号
        qzId = getIntent().getStringExtra("qzid") != null ? getIntent().getStringExtra("qzid") : "";
        // 单位类型
        dwlxStr = getIntent().getStringExtra("dwlx") != null ? getIntent().getStringExtra("dwlx") : "";
        // 经济类型
        jjlxStr = getIntent().getStringExtra("jjlx") != null ? getIntent().getStringExtra("jjlx") : "";
        // 工作地点
        gzddStr = getIntent().getStringExtra("gzdd") != null ? getIntent().getStringExtra("gzdd") : "";
        // 工作性质
        gzxzStr = getIntent().getStringExtra("gzxz") != null ? getIntent().getStringExtra("gzxz") : "";
        // 岗位名称
        gwmcStr = getIntent().getStringExtra("gwmc") != null ? getIntent().getStringExtra("gwmc") : "";
        // 岗位描述
        String gwmsStr = getIntent().getStringExtra("gwms") != null ? getIntent().getStringExtra("gwms") : "";
        // 月薪要求
        String yxyqStr = getIntent().getStringExtra("yxyq") != null ? getIntent().getStringExtra("yxyq") : "";


        gzms.setText(gwmsStr);
        yxyq.setText(yxyqStr);


        Button back = (Button) findViewById(R.id.back);
        Button bc = (Button) findViewById(R.id.bc);
        back.setOnClickListener(this);
        bc.setOnClickListener(this);
        Button sc = (Button) findViewById(R.id.sc);
        sc.setOnClickListener(this);

        if (flag == 0) {
            sc.setVisibility(View.GONE);
        } else {
            sc.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bc:
                doBcTs();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.sc:
                doSc();
                break;
            default:
                break;
        }
    }
    /**
     * 保存的提示框
     */
    public void doBcTs() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("保存记录");
        pMsg.setText("确定要保存这条记录吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                postBc();
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

    /**
     * 删除的提示框
     */
    public void doSc() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.zpt_confirm_dialog);
        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        TextView pMsg = (TextView) builder.findViewById(R.id.pMsg);
        ptitle.setText("删除");
        pMsg.setText("确定删除此条记录吗？");
        final Button confirm_btn = (Button) builder.findViewById(R.id.confirm_btn);
        Button cancel_btn = (Button) builder.findViewById(R.id.cancel_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm_btn.setEnabled(false);
                postSc();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
//        builder.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }

    private void postSc() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ModifyQzyxInfoDetail.this, "正在删除中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postScResult((String) arg0);
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
        xwzxDAL.doScQzyx(qzId, callBack);
    }

    private void postScResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("服务器繁忙，请稍后再试!");
        }
    }


    private void loadSpnerDate(final String str) {

        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                querySpnerResult((String) arg0, str);
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
            }
        };
        xwzxDAL.doGetParams(str, callBack);
    }

    private void querySpnerResult(String json, String str) {


        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (map.get("success").equals("true")) {
                switch (str) {
                    case "AAB019":
                        initDwlx(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAB020":
                        initJjlx(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "AAB301":
                        initGzdd(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "ACC201":
                        initGzxz(DataConvert.toConvertStringList(json, "table"));
                        break;
                    case "ACA111":
                        initGwmc(DataConvert.toConvertStringList(json, "table"));
                        break;


                    default:
                        break;
                }
            }
        }
    }

    /**
     * 单位类型
     */
    private void initDwlx(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dwlx.setPrompt("请选择单位类型");
            dwlx.setAdapter(adapter);
            dwlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    dwlxId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (dwlxStr.equals(list.get(i).get("zdmc"))) {
                    dwlx.setSelection(i);
                }
            }
        }
    }

    /**
     * 经济类型
     */
    private void initJjlx(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jjlx.setPrompt("请选择经济类型");
            jjlx.setAdapter(adapter);
            jjlx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    jjlxId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (jjlxStr.equals(list.get(i).get("zdmc"))) {
                    jjlx.setSelection(i);
                }
            }
        }
    }

    /**
     * 工作地点
     */
    private void initGzdd(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gzdd.setPrompt("请选择工作地点");
            gzdd.setAdapter(adapter);
            gzdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gzddId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (gzddStr.equals("江苏省扬州市" + list.get(i).get("zdmc"))) {
                    gzdd.setSelection(i);
                }
            }
        }
    }

    /**
     * 工作性质
     */
    private void initGzxz(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gzxz.setPrompt("请选择工作性质");
            gzxz.setAdapter(adapter);
            gzxz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gzxzId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (gzxzStr.equals(list.get(i).get("zdmc"))) {
                    gzxz.setSelection(i);
                }
            }
        }
    }

    /**
     * 岗位名称
     */
    private void initGwmc(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gwmc.setPrompt("请选择岗位名称");
            gwmc.setAdapter(adapter);
            gwmc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gwmcId = list.get(arg2).get("zddm");
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            for (int i = 0; i < list.size(); i++) {
                if (gwmcStr.equals(list.get(i).get("zdmc"))) {
                    gwmc.setSelection(i);
                }
            }
        }
    }
}
