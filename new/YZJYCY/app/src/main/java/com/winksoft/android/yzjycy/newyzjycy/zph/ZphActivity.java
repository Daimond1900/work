package com.winksoft.android.yzjycy.newyzjycy.zph;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newyzjycy.qzzp.QztPositionList;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

import java.util.Map;

/**
 * 招聘会
 * Created by 1900 on 2017/4/11.
 */

public class ZphActivity extends BaseActivity implements View.OnClickListener {
    private TextView zphmc, zphzt, zbdw, cbdw, jxcs, dz, dwsm, zwgs, kssj, jssj, lxr, lxdh, dzyx, positionDesc;
    private String zphId = "";
    private CommonUtil commonUtil;
    private Dialog proDialog;
    private XwzxDAL xwzxDAL;
    private String zphmcStr = "";
    private ImageView zphTp;
    private ImageLoader im;
    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zph_activity);
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        imgUrl = getIntent().getStringExtra("zphtp");
        im = ImageLoader.getInstance();
        im.init(ImageLoaderConfiguration.createDefault(this));
        zphId = getIntent().getStringExtra("id") == null && "".equals(getIntent().getStringExtra("id")) ? "" : getIntent().getStringExtra("id");
        initView();
        loadDate();
          /*异常情况刷新*/
        findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDate();
            }
        });
    }

    private void initView() {
        zphTp = (ImageView) findViewById(R.id.zphTp);
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        Button zpxx = (Button) findViewById(R.id.zpxx);
        zpxx.setOnClickListener(this);
        Button zpdw = (Button) findViewById(R.id.zpdw);
        zpdw.setOnClickListener(this);
        zphmc = (TextView) findViewById(R.id.zphmc);
        zphzt = (TextView) findViewById(R.id.zphzt);
        zbdw = (TextView) findViewById(R.id.zbdw);
        cbdw = (TextView) findViewById(R.id.cbdw);
        jxcs = (TextView) findViewById(R.id.jxcs);
        dz = (TextView) findViewById(R.id.dz);
        dwsm = (TextView) findViewById(R.id.dwsm);
        zwgs = (TextView) findViewById(R.id.zwgs);
        kssj = (TextView) findViewById(R.id.kssj);
        jssj = (TextView) findViewById(R.id.jssj);
        lxr = (TextView) findViewById(R.id.lxr);
        lxdh = (TextView) findViewById(R.id.lxdh);
        dzyx = (TextView) findViewById(R.id.dzyx);
        positionDesc = (TextView) findViewById(R.id.positionDesc);
    }

    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        ZphActivity.this, "加载中,请稍后...");
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
        xwzxDAL.queryZphInfo(zphId, callBack);       // 首页刷新后显示的数据
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                formatData(DataConvert.toConvertStringMap(json, "row"));
            } else {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
            }
        }
    }

    private void formatData(Map<String, String> positionInfo) {
        if (positionInfo != null) {
            zphmcStr = commonUtil.getMapValue(positionInfo, "acb331");
            zphmc.setText(zphmcStr);
            zphzt.setText(commonUtil.getMapValue(positionInfo, "acb332"));
            zbdw.setText(commonUtil.getMapValue(positionInfo, "aab045"));
            cbdw.setText(commonUtil.getMapValue(positionInfo, "acb338"));
            jxcs.setText(commonUtil.getMapValue(positionInfo, "aab301"));
            dz.setText(commonUtil.getMapValue(positionInfo, "acb336"));
            dwsm.setText(commonUtil.getMapValue(positionInfo, "acb339"));
            zwgs.setText(commonUtil.getMapValue(positionInfo, "acb311"));
            kssj.setText(commonUtil.getMapValue(positionInfo, "aae030"));
            jssj.setText(commonUtil.getMapValue(positionInfo, "aae031"));
            lxr.setText(commonUtil.getMapValue(positionInfo, "aae004"));
            lxdh.setText(commonUtil.getMapValue(positionInfo, "aae005"));
            dzyx.setText(commonUtil.getMapValue(positionInfo, "aae159"));
            positionDesc.setText(commonUtil.getMapValue(positionInfo, "acb337"));
            im.displayImage(imgUrl, zphTp);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.zpxx: // 招聘信息
                i = new Intent(this, QztPositionList.class);
                Log.d("zphid", "onClick: zphId = " + zphId);
                i.putExtra("zphId", zphId);
                i.putExtra("flag", "2");
                i.putExtra("sfkd","1");
                i.putExtra("companyName", zphmcStr);
                startActivity(i);
                break;
            case R.id.zpdw: // 招聘单位
                i = new Intent(this, QztPositionList.class);
                i.putExtra("flag", "1");
                i.putExtra("zphId", zphId);
                i.putExtra("companyName", zphmcStr);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
