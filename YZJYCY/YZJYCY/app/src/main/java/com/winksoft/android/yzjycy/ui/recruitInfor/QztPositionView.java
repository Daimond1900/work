package com.winksoft.android.yzjycy.ui.recruitInfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.XwzxDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.enterprise.QztEnterpriseView;
import com.winksoft.android.yzjycy.ui.resume.QztAddResumeActivity;
import com.winksoft.android.yzjycy.ui.zptmapabc.ZptMapInfoActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.StringHelper;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;

/***
 * 职位详细
 *
 * @author wujiahong 2012-10-18
 */
public class QztPositionView extends BaseActivity {
    private static final String TAG = "QztPositionView";
    private TableRow company_item, companyAddressItem;
    private Button back_btn, deliveryBtn, contactBtn;
    private TextView positionTitle, companyName, pushDate, peopleCount, monthlyPay, record, area, telNumber,
            positionDesc, companyAddress, gzsm;
    private String telNo = "", positionId = "", companyId = "";
    private ProgressDialog progressDialog;
    private XwzxDAL xwzxDAL;
    private Map<String, String> positionInfo, sendDelivery;
    private String longitude = "1192679470";// 经度
    private String latitude = "32248012";// 纬度
    private String companyNameStr = "", companyAddressStr = "";
    private ListView listView;
    private ArrayList<Map<String, String>> tels;
    Dialog proDialog;
    private CommonUtil commonUtil;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qzt_position_info);
        UserSession session = new UserSession(this);
        user = session.getUser();
        xwzxDAL = new XwzxDAL(this);
        commonUtil = new CommonUtil(this);
        // 初始化界面
        initPage();
        // 加载数据
        onPostData();
    }

    private void onPostData() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "加载中,请稍后...");
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
        xwzxDAL.getPosition(positionId, callBack);
    }

    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
//                showToast(commonUtil.getMapValue(map, "msg"), false);
                positionInfo = DataConvert.toConvertStringMap(json, "recruitment");
                Log.d(TAG, "postResult: positionInfo" + positionInfo);
                setPageData();
                return;
            } else {
                commonUtil.shortToast("查询失败!");
            }
        } else {
            commonUtil.shortToast("查询失败!");
        }
    }

    /**
     * 投递简历
     */
    private void onPostTdjl() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        QztPositionView.this, "简历投递中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                jltdResult((String) arg0);
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
        xwzxDAL.doPostDelivery(positionId, user.getUserId(), companyId, "暂无", callBack);
    }

    private void jltdResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(commonUtil.getMapValue(map,"success"))) {
                commonUtil.shortToast(map.get("msg"));
                this.finish();
            } else {
                commonUtil.shortToast(map.get("msg"));
            }
        } else {
            commonUtil.shortToast("系统繁忙，请稍后！");
        }

    }

    private void initPage() {

        MyOnclick onClick = new MyOnclick();

        positionId = this.getIntent().getStringExtra("positionId") == null ? ""
                : this.getIntent().getStringExtra("positionId");
        companyId = this.getIntent().getStringExtra("companyId") == null ? ""
                : this.getIntent().getStringExtra("companyId");

        // 点击公司查看公司详细
        company_item = (TableRow) findViewById(R.id.company_item);
        if (!this.getIntent().getBooleanExtra("isClick", false)) {// 如果是所有职位进来查看公司详细不可点
            company_item.setOnClickListener(onClick);
        }

        // 点击地址查看地图
        companyAddressItem = (TableRow) findViewById(R.id.companyAddressItem);
        companyAddressItem.setOnClickListener(onClick);

        // 返回按钮
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(onClick);

        // 投递简历
        deliveryBtn = (Button) findViewById(R.id.deliveryBtn);
        deliveryBtn.setOnClickListener(onClick);

        // 联系HR
        contactBtn = (Button) findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(onClick);

        positionTitle = (TextView) findViewById(R.id.positionTitle);// 职位名称
        companyName = (TextView) findViewById(R.id.companyName);// 公司名称
        pushDate = (TextView) findViewById(R.id.pushDate);// 有效期
        gzsm = (TextView) findViewById(R.id.gzsm); // 工作说明
        peopleCount = (TextView) findViewById(R.id.peopleCount);// 招聘人数
        monthlyPay = (TextView) findViewById(R.id.monthlyPay);// 工资
        record = (TextView) findViewById(R.id.record);// 学历要求
        area = (TextView) findViewById(R.id.area);// 用工区域
        telNumber = (TextView) findViewById(R.id.telNumber);// 联系电话
        positionDesc = (TextView) findViewById(R.id.positionDesc);// 职位描述
        companyAddress = (TextView) findViewById(R.id.companyAddress);// 公司地址

    }


    private class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.company_item:
                    Intent bl = new Intent(QztPositionView.this, QztEnterpriseView.class);
                    bl.putExtra("companyId", companyId);
                    startActivity(bl);
                    break;
                case R.id.companyAddressItem:
                    if (!longitude.equals("") && !latitude.equals("")) {
                        Intent map = new Intent(QztPositionView.this, ZptMapInfoActivity.class);
                        map.putExtra("companyName", companyNameStr);
                        map.putExtra("companyAddress", companyAddressStr);
                        map.putExtra("telNo", telNo);
                        map.putExtra("longitude", longitude);// 经度
                        map.putExtra("latitude", latitude);// 纬度

                        startActivity(map);
                    } else {
                        commonUtil.shortToast(companyNameStr + ",还没标注地理位置.");
                    }
                    break;
                case R.id.back_btn:
                    QztPositionView.this.finish();
                    break;
                case R.id.deliveryBtn:
                    // if(Constants.isLogin){
                    if (user.getIdCard().equals("")) {

                        commonUtil.shortToast("您还未完善简历,请先完善简历再投递!");

                        // 跳到新增简历界面
                        Intent addResume = new Intent(QztPositionView.this, QztAddResumeActivity.class);
                        addResume.putExtra("userId", user.getUserId());
                        addResume.putExtra("isAdd", true);
                        startActivity(addResume);
                        QztPositionView.this.finish();

                    } else {
                        onPostTdjl();
                    }
                    // }
                    // else{
                    // commonUtil.shortToast("请先登录");
                    // startActivity(new
                    // Intent(QztPositionView.this,QztLoginActivity.class));
                    // }

				/*
                 * postid=2;
				 * progressDialog=commonUtil.showProgressDialog("正在处理,请稍等....");
				 * new Thread(loadRunnanle).start();
				 */
                    break;
                case R.id.contactBtn:
                    if (telNo.equals("")) {
                        commonUtil.shortToast("没有找到HR联系号码!");
                    } else {
                        if (telNo.length() < 11) {
                            dialogUtil.showCallDialog("系统提示", "您确定要拔打" + telNo + "吗?", telNo);
                        } else {
                            showMsg();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /***
     * 加载数据
     *
     * AAB004 单位名称 ACA112 招聘工种 ACB216 工种说明 ACB21R 招聘总人数 ACC034 月工资 ACC214 福利待遇
     * AAC039 外语水平 枚举表 AAC043 计算机水平 枚举表 AAC011 文化程度 枚举表 AAE013 其他说明及要求 ACB211
     * 男性人数 ACB212 女性人数 ACB213 兼招人数 AAE030 有效起始日期 AAE031 有效终止日期 AAE036 经办日期
     * ACB21O 空位编号 AAE004 单位联系人 AAE005 单位联系人电话 AAE020 经办机构 AAE006 单位地址 ACB202
     * 用工区域 ACB203 工作地点 ACB204 地点详细
     */
    private void setPageData() {
        if (positionInfo != null) {
            companyNameStr = positionInfo.get("aab004") == null ? "" : positionInfo.get("aab004");
            positionTitle.setText(positionInfo.get("aca112") == null ? "" : positionInfo.get("aca112"));// 职位名称
            companyName.setText(companyNameStr);// 公司名称
            gzsm.setText(positionInfo.get("acb216")); // 工作说明
            pushDate.setText(positionInfo.get("aae030") + "至" + positionInfo.get("aae031"));// 有效期
            peopleCount.setText("男性:" + positionInfo.get("acb211") + ";女性:" + positionInfo.get("acb212") + ";兼招:"
                    + positionInfo.get("acb213"));// 招聘人数

            String price = positionInfo.get("acc034") == null ? "" : positionInfo.get("acc034");
            if (price.equals(""))
                price = "面议";
            monthlyPay.setText(price);// 工资

            record.setText(positionInfo.get("aac011"));// 学历要求
            area.setText(positionInfo.get("acb202"));// 用工区域

            telNo = positionInfo.get("aae005") == null ? "" : positionInfo.get("aae005");
            telNumber.setText(telNo);// 联系电话

            // 公司编号
            companyId = positionInfo.get("aab001") == null ? "" : positionInfo.get("aab001");

            // 职位描述
            positionDesc.setText(Html.fromHtml(
                    "福利待遇:" + positionInfo.get("acc214") + "" + "<br/><br/>具体要求:" + positionInfo.get("aae013")));

            // 如果没有公司详细地址就显示公司名称
            companyAddressStr = positionInfo.get("aae006") == null ? "" : positionInfo.get("aae006");
            companyAddress.setText(companyAddressStr.equals("") ? companyNameStr : companyAddressStr + "	(地图定位)");

            longitude = positionInfo.get("lng");
            ;// 经度
            latitude = positionInfo.get("lat");// 纬度

        } else {
            commonUtil.shortToast("信息加载失败!");
        }
    }

    /***
     * 弹出电话号码列表框让用户拨打电话
     *
     */
    public void showMsg() {
        final Dialog builder = new Dialog(this, R.style.dialog);
        builder.setContentView(R.layout.qzt_template_spinner);

        TextView ptitle = (TextView) builder.findViewById(R.id.pTitle);
        ptitle.setText("请选择拨打号码");

        listView = (ListView) builder.findViewById(R.id.listview);
        setTels();

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String telNumber = tels.get(arg2).get("number");
                dialogUtil.showCallDialog("系统提示", "您确定要拔打" + telNumber + "吗?", telNumber);
                builder.dismiss();
            }

        });
        builder.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        builder.show();
    }

    private void setTels() {
        tels = new ArrayList<Map<String, String>>();
        ArrayList<String> list = StringHelper.AnalyzePhoneNumber(telNo);
        Map<String, String> map;
        for (int i = 0; i < list.size(); i++) {
            map = new HashMap<String, String>();
            map.put("id", i + "");
            map.put("number", list.get(i));
            tels.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, tels, R.layout.qzt_template_spinner_item,
                new String[]{"number"}, new int[]{R.id.item_lab});
        listView.setAdapter(adapter);

    }
}
