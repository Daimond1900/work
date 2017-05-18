package com.winksoft.android.yzjycy.newyzjycy.qzzp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newyzjycy.map.ZptMapInfoActivity;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import java.util.Map;

/**
 * 公司详细信息
 */
public class QztEnterpriseView extends BaseActivity {
    private TextView companyName, qualityName, tradeName, telNumber, companyDesc, companyAddress, txtfaxes, txtemail, txtcontact, txturl;
    private XwzxDAL xwzxDAL;
    private String companyId = "";
    private Dialog proDialog;
    private String longitude = "";//经度
    private String latitude = "";//纬度
    private String companyNameStr;
    private String zphId = "";
    private String companyNames = "";
    private static final String TAG = "QztEnterpriseView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_enterprise_info);
        xwzxDAL = new XwzxDAL(this);

        initPage();

        onPostData();

          /*异常情况刷新*/
        findViewById(R.id.sxyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostData();
            }
        });


    }

    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztEnterpriseView.this, "加载中,请稍后...");
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

        xwzxDAL.getEnterPrise(companyId, callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                setPageData(DataConvert.toConvertStringMap(json, "row"));
            } else {
                findViewById(R.id.islayout).setVisibility(View.GONE);
                findViewById(R.id.wlyc).setVisibility(View.VISIBLE);
                findViewById(R.id.kb).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initPage() {
        zphId = this.getIntent().getStringExtra("zphId") == null ? "" : this.getIntent().getStringExtra("zphId");
        companyId = this.getIntent().getStringExtra("companyId") == null ? "" : this.getIntent().getStringExtra("companyId");
        companyNameStr = this.getIntent().getStringExtra("companyName") == null ? "" : this.getIntent().getStringExtra("companyName");
        MyOnclick onclick = new MyOnclick();

        //点击到该公司所有职位
        TableRow positionItem = (TableRow) findViewById(R.id.positionItem);
        positionItem.setOnClickListener(onclick);

        //点击到公司对应的地图展现
        TableRow companyAddressItem = (TableRow) findViewById(R.id.companyAddressItem);
        companyAddressItem.setOnClickListener(onclick);

        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(onclick);

        companyName = (TextView) findViewById(R.id.companyName);//公司名称;
        qualityName = (TextView) findViewById(R.id.qualityName);//公司性质
        tradeName = (TextView) findViewById(R.id.tradeName);//行业
        telNumber = (TextView) findViewById(R.id.telNumber);//联系电话
        txtfaxes = (TextView) findViewById(R.id.faxes);    //传真
        txtemail = (TextView) findViewById(R.id.email);    //邮箱
        companyDesc = (TextView) findViewById(R.id.companyDesc);//公司简介
        companyAddress = (TextView) findViewById(R.id.companyAddress);//公司地址
        txtcontact = (TextView) findViewById(R.id.contact);    //联系人
        txturl = (TextView) findViewById(R.id.url);    //网址
    }

    private class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.positionItem:
                    Intent posotionList = new Intent(QztEnterpriseView.this, QztPositionList.class);
                    posotionList.putExtra("companyName", companyName.getText().toString());

                    if (!"".equals(zphId)) {
                        posotionList.putExtra("flag", "2");
                        posotionList.putExtra("zphId", zphId);
                    }
                    posotionList.putExtra("companyId", companyId);
                    startActivity(posotionList);
                    break;
                case R.id.companyAddressItem:
                    if (!"".equals(longitude) && !"".equals(latitude)) {
                        Intent map = new Intent(QztEnterpriseView.this, ZptMapInfoActivity.class);
                        map.putExtra("lot", longitude);//经度
                        map.putExtra("lat", latitude);//纬度
                        startActivity(map);
                    } else {
                        commonUtil.shortToast(companyNames + ",还没标注地理位置.");
                    }
                    break;
                case R.id.back_btn:
                    QztEnterpriseView.this.finish();
                    break;
                default:
                    break;
            }
        }
    }


    /***
     * 加载数据
     ACB200	招聘编号
     AAB004	单位名称
     AAB022	行业类型   枚举
     AAB019	单位类型   枚举
     AAE006	单位地址
     AAE004	单位联系人
     AAE005	单位联系人电话
     ACB206	单位简介
     AAE014	传真
     AAE015	电子信箱
     ACB202	用工区域
     ACB203	工作地点
     ACB204	地点详细
     AAE030	招聘起始日期
     AAE031	招聘终止日期
     */
    private void setPageData(Map<String, String> enterInfo) {
        if (enterInfo != null) {

            companyName.setText(companyNameStr);//公司名称;
            qualityName.setText(enterInfo.get("aab019"));//公司性质
            tradeName.setText(enterInfo.get("aab022"));//行业
            String telNo = enterInfo.get("acb501") == null ? "" : enterInfo.get("acb501");
            telNumber.setText(telNo);//联系电话
            String contact = enterInfo.get("aae004") == null ? "" : enterInfo.get("aae004");
            txtcontact.setText(contact);//联系人
            String email = enterInfo.get("aae159") == null ? "" : enterInfo.get("aae159");
            txtemail.setText(email);    //邮箱


            String faxes = enterInfo.get("aae387") == null ? "" : enterInfo.get("aae387");
            txtfaxes.setText(faxes);    //传真

            String url = enterInfo.get("aae392") == null ? "" : enterInfo.get("aae392");
            txturl.setText(url);    //网址

            //公司简介
            String desc = enterInfo.get("aab092") == null ? "" : enterInfo.get("aab092");
            companyDesc.setText(Html.fromHtml("".equals(desc) ? "暂无简介" : desc));

            //如果公司地址为空就显示公司名字
            String address = enterInfo.get("aae006") == null ? "" : enterInfo.get("aae006");
            Log.d(TAG, "setPageData: 公司名称  == " + companyNameStr);
            companyAddress.setText("".equals(address) ? companyNameStr : address + "	(地图定位)");

            String dwzb = enterInfo.get("dwzb") == null ? "" : enterInfo.get("dwzb");
            if (!"".equals(dwzb)) {
                try {
                    String[] dwzbs = dwzb.split(";");
                    longitude = dwzbs[0];//经度
                    latitude = dwzbs[1];//纬度
                }catch (Exception e){
                    longitude = "";
                    latitude = "";
                }
            }
        } else {
            commonUtil.shortToast("信息加载失败!");
        }
    }
}
