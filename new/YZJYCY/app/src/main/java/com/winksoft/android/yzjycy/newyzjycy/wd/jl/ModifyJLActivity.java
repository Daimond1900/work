package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.MyListView1;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.List;
import java.util.Map;

/**
 * 修改简历 主界面
 * Created by 1900 on 2017/4/18.
 */

public class ModifyJLActivity extends BaseActivity implements View.OnClickListener {
    private int pdBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyjl_activity);
        initView();
    }

    private void initView() {
        pdBt = getIntent().getIntExtra("pdbt", 0); /*判断标题 没有取到值为0 0：新增*/

        TextView xg_title = (TextView) findViewById(R.id.xg_title);

        if (pdBt == 1) {
            xg_title.setText("修改简历");
        } else {
            xg_title.setText("新增简历");
        }
        View layout_grjcxx = findViewById(R.id.layout_grjcxx);
        View layout_jyjl = findViewById(R.id.layout_jyjl);
        View layout_pxjl = findViewById(R.id.layout_pxjl);
        View layout_yynl = findViewById(R.id.layout_yynl);
        View layout_jszc = findViewById(R.id.layout_jszc);
        View layout_gzjl = findViewById(R.id.layout_gzjl);
        View layout_qzyx = findViewById(R.id.layout_qzyx);
        layout_grjcxx.setOnClickListener(this);
        layout_jyjl.setOnClickListener(this);
        layout_pxjl.setOnClickListener(this);
        layout_yynl.setOnClickListener(this);
        layout_jszc.setOnClickListener(this);
        layout_gzjl.setOnClickListener(this);
        layout_qzyx.setOnClickListener(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.layout_grjcxx:    // 基本信息
                intent = new Intent(this, ModifyPersonJcInfo.class);
                intent.putExtra("pdbt", pdBt);
                startActivity(intent);
                break;
            case R.id.layout_jyjl:    // 教育经历
                intent = new Intent(this, ModifyJyjlInfo.class);
                intent.putExtra("pdbt", pdBt);
                startActivity(intent);
                break;
            case R.id.layout_pxjl:  // 培训经历
                intent = new Intent(this, ModifyPxjlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_yynl:  // 语言能力
                intent = new Intent(this, ModifyYynlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_jszc:  // 技术专长
                intent = new Intent(this, ModifyJszcInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_gzjl:  // 工作经历
                intent = new Intent(this, ModifyGzjlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_qzyx:  // 求职意向
                intent = new Intent(this, ModifyQzyxInfo.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 语言能力 列表
     * Created by 1900 on 2017/4/25.
     */
    public static class ModifyYynlInfo extends BaseActivity implements View.OnClickListener{
        private MyListView1 list;
        private XwzxDAL xwzxDAL;
        private Dialog proDialog;
        private List<Map<String, String>> listResultvalue;
        private ImageView wsj_img;
        private TextView wsj_tv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.xgyynl);
            initView();
        }
        @Override
        protected void onResume() {
            super.onResume();
            loadData();
        }
        private void loadData() {
            AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
                @Override
                public void onStart() {
                    super.onStart();
                    proDialog = CustomeProgressDialog.createLoadingDialog(
                            ModifyYynlInfo.this, "加载中,请稍后...");
                    proDialog.show();
                }

                @Override
                public void onSuccess(Object arg0) {
                    super.onSuccess(arg0);
                    loadDataResult((String) arg0);
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
            xwzxDAL.queryYynl(user.getUserId(), callBack);
        }

        private void loadDataResult(String json) {
            Map<String, String> map = DataConvert.toMap(json);
            if (map != null) {
                if (map.get("success").equals("true")) {
                    list.setVisibility(View.VISIBLE);
                    setPage(DataConvert.toConvertStringList(json, "table"));
                    wsj_img.setVisibility(View.GONE);
                    wsj_tv.setVisibility(View.GONE);
                } else {
                    wsj_tv.setText("暂无记录，赶紧完善它吧！");
                    wsj_img.setVisibility(View.VISIBLE);
                    wsj_tv.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }
            } else {
                wsj_tv.setText("服务器繁忙，请稍后再试！");
                wsj_img.setVisibility(View.VISIBLE);
                wsj_tv.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }
        }

        private void setPage(List<Map<String, String>> listResult) {
            if (list != null && listResult.size() > 0) {
                listResultvalue = listResult;
                SimpleAdapter adapter = new SimpleAdapter(this, listResult, R.layout.xgyynl_list_item,
                        new String[]{"aac038", "aac175"},
                        new int[]{R.id.yz, R.id.sp});
                list.setAdapter(adapter);
            }
        }


        private void initView() {
            xwzxDAL = new XwzxDAL(this);
            commonUtil = new CommonUtil(this);
            wsj_img = (ImageView) findViewById(R.id.wsj_img);
            wsj_tv = (TextView) findViewById(R.id.wsj_tv);
            list = (MyListView1) findViewById(R.id.id_lv);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 语言编号
                    String yzid = listResultvalue.get(position).get("yyid") != null && !"".equals(listResultvalue.get(position).get("yyid")) ? listResultvalue.get(position).get("yyid") : "";
                    // 外语种类
                    String wyzl = listResultvalue.get(position).get("wyzl") != null && !"".equals(listResultvalue.get(position).get("wyzl")) ? listResultvalue.get(position).get("wyzl") : "";
                    // 语种代码
                    String yzdm = listResultvalue.get(position).get("aac038") != null && !"".equals(listResultvalue.get(position).get("aac038")) ? listResultvalue.get(position).get("aac038") : "";
                    // 外语水平
                    String yysp = listResultvalue.get(position).get("aac175") != null && !"".equals(listResultvalue.get(position).get("aac175")) ? listResultvalue.get(position).get("aac175") : "";

                    Intent intent = new Intent(ModifyYynlInfo.this, ModifyYynlInfoDetail.class);
                    intent.putExtra("yzid",yzid);
                    intent.putExtra("wyzl",wyzl);
                    intent.putExtra("yzdm",yzdm);
                    intent.putExtra("yysp",yysp);
                    startActivity(intent);


                }
            });

            Button xz = (Button) findViewById(R.id.xz);
            Button back = (Button) findViewById(R.id.back);
            xz.setOnClickListener(this);
            back.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.xz:
                    //新增
                    Intent intent = new Intent(this, ModifyYynlInfoDetail.class);
                    intent.putExtra("flag",0); //新增
                    startActivity(intent);
                    break;
                case R.id.back:
                    this.finish();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 语言能力 修改
     */
    public static class ModifyYynlInfoDetail extends BaseActivity implements View.OnClickListener {

        private Spinner yyzl, yysp;
        private String yzidStr;
        private String yyspStr;
        private String yyspId;
        private String yyzlId;
        private String yzdmStr;
        private XwzxDAL xwzxDAL;
        private CommonUtil commonUtil;
        private Dialog proDialog;
        private int flag;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.modifyyynlinfodetail);

            flag = getIntent().getIntExtra("flag", -1);
            if (flag == 0) {  //新增
                ((TextView) findViewById(R.id.textView2)).setText("新增教育经历");
            }

            initView();
        }

        private void initView() {
            xwzxDAL = new XwzxDAL(this);
            commonUtil = new CommonUtil(this);
            loadSpnerDate("AAC038");      // 语种代码
            loadSpnerDate("AAC175");      // 外语水平

            // 语言编号
            yzidStr = getIntent().getStringExtra("yzid") != null ? getIntent().getStringExtra("yzid") : "";
            // 语种代码
            yzdmStr = getIntent().getStringExtra("yzdm") != null ? getIntent().getStringExtra("yzdm") : "";
            // 外语水平
            yyspStr = getIntent().getStringExtra("yysp") != null ? getIntent().getStringExtra("yysp") : "";

            yyzl = (Spinner) findViewById(R.id.yyzl);
            yysp = (Spinner) findViewById(R.id.yysp);
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
                        case "AAC175":
                            initYysp(DataConvert.toConvertStringList(json, "table"));
                            break;
                        case "AAC038":
                            initYzdm(DataConvert.toConvertStringList(json, "table"));
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        private void initYzdm(final List<Map<String, String>> list) {
            if (list != null && list.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yyzl.setPrompt("请选择语言种类");
                yyzl.setAdapter(adapter);
                yyzl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        yyzlId = list.get(arg2).get("zddm");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                for (int i = 0; i < list.size(); i++) {
                    if (yzdmStr.equals(list.get(i).get("zdmc"))) {
                        yyzl.setSelection(i);
                    }
                }
            }
        }

        private void initYysp(final List<Map<String, String>> list) {
            if (list != null && list.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_spinner_item, new String[]{"zdmc"}, new int[]{android.R.id.text1});
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                yysp.setPrompt("请选择语言水平");
                yysp.setAdapter(adapter);
                yysp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        yyspId = list.get(arg2).get("zddm");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                for (int i = 0; i < list.size(); i++) {
                    if (yyspStr.equals(list.get(i).get("zdmc"))) {
                        yysp.setSelection(i);
                    }
                }
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bc:
                    postBc();
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

        private void postBc() {

            if (flag == 0) {
                postDataXz(user.getUserId(), yyzlId, yyspId);
            } else {
                postData(yzidStr, yyzlId, yyspId);
            }
        }

        private void postData(String yzidStr, String yyzlId, String yyspId) {
            AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
                @Override
                public void onStart() {
                    super.onStart();
                    proDialog = CustomeProgressDialog.createLoadingDialog(
                            ModifyYynlInfoDetail.this, "正在查询中,请稍后...");
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
            xwzxDAL.doModifyYynl(yzidStr,yyzlId, yyspId,
                    callBack);
        }

        private void postResult(String json) {
            Map<String, String> map = DataConvert.toMap(json);
            if (map != null) {
                if (("true").equals(map.get("success"))) {
                    commonUtil.shortToast(map.get("msg"));
                } else {
                    commonUtil.shortToast(map.get("msg"));
                }
            } else {
                commonUtil.shortToast("服务器繁忙，请稍后再试!");
            }
        }

        private void postDataXz(String userId, String yyzlId, String yyspId) {
            AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
                @Override
                public void onStart() {
                    super.onStart();
                    proDialog = CustomeProgressDialog.createLoadingDialog(
                            ModifyYynlInfoDetail.this, "正在查询中,请稍后...");
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
            xwzxDAL.doXzYynl(userId, yyzlId, yyspId,
                    callBack);
        }

        private void postDataXzResult(String json) {
            Map<String, String> map = DataConvert.toMap(json);
            if (map != null) {
                if (("true").equals(map.get("success"))) {
                    commonUtil.shortToast(map.get("msg"));
                } else {
                    commonUtil.shortToast(map.get("msg"));
                }
            } else {
                commonUtil.shortToast("服务器繁忙，请稍后再试!");
            }
        }

        private void postSc() {
            AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
                @Override
                public void onStart() {
                    super.onStart();
                    proDialog = CustomeProgressDialog.createLoadingDialog(
                            ModifyYynlInfoDetail.this, "正在删除中,请稍后...");
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
            xwzxDAL.doScyynl(yzidStr, callBack);
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


    }
}
