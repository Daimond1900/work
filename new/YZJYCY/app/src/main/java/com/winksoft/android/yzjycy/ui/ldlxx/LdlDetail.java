package com.winksoft.android.yzjycy.ui.ldlxx;

import java.util.HashMap;
import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.JcDAL;
import com.winksoft.android.yzjycy.data.ManpowerDetailDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 基本信息
 * 
 * @author user_ygl
 * 
 */
public class LdlDetail extends BaseActivity {
	private static final String TAG = "LdlDetail";
	private Button back;
	private TextView sfz, xb, csrq, whcd, hkxz, mz, zzmm, jyzt;
	private TextView name, lxdh, jtzz;
	private ManpowerDetailDAL dal;
	private String code;
	private JcDAL jcDAL;
	private Dialog proDialog;
	private CommonUtil commonUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcy_ldl_main_detail);
		commonUtil = new CommonUtil(this);
		jcDAL = new JcDAL(this);
		code = getIntent().getStringExtra("code");
		back = (Button) findViewById(R.id.back);
		sfz = (TextView) findViewById(R.id.sfz);
		name = (TextView) findViewById(R.id.name); // 姓名
		xb = (TextView) findViewById(R.id.xb);
		csrq = (TextView) findViewById(R.id.csrq);
		lxdh = (TextView) findViewById(R.id.lxdh);
		whcd = (TextView) findViewById(R.id.whcd);
		hkxz = (TextView) findViewById(R.id.hkxz);
		mz = (TextView) findViewById(R.id.mz);
		zzmm = (TextView) findViewById(R.id.zzmm);
		jtzz = (TextView) findViewById(R.id.jtzz);
		jyzt = (TextView) findViewById(R.id.jyzt);
		sfz.setFocusable(false);
		sfz.setBackgroundColor(0x00000000);
		back.requestFocus();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LdlDetail.this.finish();
			}
		});
		if (code != null && !code.equals(""))
			doPost();
	}

	private void doPost() {
		AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
			@Override
			public void onStart() {
				super.onStart();
				proDialog = CustomeProgressDialog.createLoadingDialog(
						LdlDetail.this, "加载中,请稍后...");
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
		jcDAL.doJbxxQuery(code, callBack);
	}

	/**
	 * @param json
	 */
	private void postResult(String json) {
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (("true").equals(map.get("success"))) {
				setData(DataConvert.toConvertStringMap(json, "detail"));
				return;
			} else {
				commonUtil.shortToast("没有记录");
			}
		} else {
			commonUtil.shortToast("系统繁忙，请稍后再试!");
		}
	}

	private void setData(Map<String,String> resultMap) {
		if (resultMap != null) {
			sfz.setText(commonUtil.getMapValue(resultMap, "aac002"));//身份证
			xb.setText(ParseData.NANVN.get(commonUtil.getMapValue(resultMap, "aac004")));//性别
			csrq.setText(commonUtil.getMapValue(resultMap, "aac006"));// 出生年月
			whcd.setText(ParseData.WHCD.get(commonUtil.getMapValue(resultMap, "aac011")));
			hkxz.setText(ParseData.HKZT.get(commonUtil.getMapValue(resultMap, "aac009")));
			mz.setText(ParseData.MAPMZ.get(commonUtil.getMapValue(resultMap, "aac005")));
			zzmm.setText(ParseData.ZZMM.get(commonUtil.getMapValue(resultMap, "aac024")));
			jyzt.setText(ParseData.JYZT.get(commonUtil.getMapValue(resultMap, "aac016")));    //就业状态
			name.setText(commonUtil.getMapValue(resultMap, "aac003"));
			lxdh.setText(commonUtil.getMapValue(resultMap, "aae005"));    //联系电话
			jtzz.setText(commonUtil.getMapValue(resultMap, "aac026"));        // 家庭住址
		}
	}

}
