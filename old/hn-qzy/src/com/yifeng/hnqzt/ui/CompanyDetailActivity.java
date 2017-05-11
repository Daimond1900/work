package com.yifeng.hnqzt.ui;

import com.yifeng.hnqzt.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * comment:浏览企业招聘信息详细信息
 * 
 * @author:ZhangYan Date:2012-8-30
 */
public class CompanyDetailActivity extends BaseActivity {
	private TextView company_name, job_type, gzsm, zpzrs, ygqy, gzdd, dzxx,
			nlyq, whcd, jsdj, cyls, ygz, fldy, sstg, jszc, hkxz, zpdx, wyyz,
			wysp, jsjsp, lxr, lxfs, yxq;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_detail);

		Intent qz = this.getIntent();
		id = qz.getStringExtra("id");
		initPage();
		if (id.equals("1.")) {
			setData1();
		} else if (id.equals("2.")) {
			setData2();
		} else if (id.equals("3.0")) {
			setData3();
		}

	}

	/**
	 * 页面初始化操作
	 */
	private void initPage() {
		company_name = (TextView) findViewById(R.id.company_name);
		job_type = (TextView) findViewById(R.id.job_type);
		gzsm = (TextView) findViewById(R.id.gzsm);
		zpzrs = (TextView) findViewById(R.id.zpzrs);
		ygqy = (TextView) findViewById(R.id.ygqy);
		gzdd = (TextView) findViewById(R.id.gzdd);
		dzxx = (TextView) findViewById(R.id.dzxx);
		nlyq = (TextView) findViewById(R.id.nlyq);
		whcd = (TextView) findViewById(R.id.whcd);
		jsdj = (TextView) findViewById(R.id.jsdj);
		cyls = (TextView) findViewById(R.id.cyls);
		ygz = (TextView) findViewById(R.id.ygz);
		fldy = (TextView) findViewById(R.id.fldy);
		sstg = (TextView) findViewById(R.id.sstg);
		jszc = (TextView) findViewById(R.id.jszc);
		hkxz = (TextView) findViewById(R.id.hkxz);
		zpdx = (TextView) findViewById(R.id.zpdx);
		wyyz = (TextView) findViewById(R.id.wyyz);
		wysp = (TextView) findViewById(R.id.wysp);
		jsjsp = (TextView) findViewById(R.id.jsjsp);
		lxr = (TextView) findViewById(R.id.lxr);
		lxfs = (TextView) findViewById(R.id.lxfs);
		yxq = (TextView) findViewById(R.id.yxq);
	}

	private void setData1() {
		company_name.setText("扬州恒春电子有限公司");
		job_type.setText("计算机工程技术人员");
		gzsm.setText("软件设计师");
		zpzrs.setText("1 (男1 女0 兼招0)");
		ygqy.setText("本埠");
		gzdd.setText("");
		dzxx.setText("");
		nlyq.setText("最小年龄30  最大年龄45");
		whcd.setText("大专");
		jsdj.setText("");
		cyls.setText("");
		ygz.setText("1000.0");
		fldy.setText("");
		sstg.setText("");
		jszc.setText("");
		hkxz.setText("");
		zpdx.setText("");
		wyyz.setText("");
		wysp.setText("");
		jsjsp.setText("");
		lxr.setText("张女士");
		lxfs.setText("80827726");
		yxq.setText("从20120830至20120930");
	}

	private void setData2() {
		company_name.setText("扬州中科半导体照明有限公司");
		job_type.setText("计算机软件工程师");
		gzsm.setText("软件设计师");
		zpzrs.setText("1 (男0 女0 兼招1)");
		ygqy.setText("本埠");
		gzdd.setText("");
		dzxx.setText("");
		nlyq.setText("最小年龄30  最大年龄45");
		whcd.setText("大学");
		jsdj.setText("");
		cyls.setText("");
		ygz.setText("1000.0");
		fldy.setText("");
		sstg.setText("");
		jszc.setText("");
		hkxz.setText("");
		zpdx.setText("");
		wyyz.setText("");
		wysp.setText("");
		jsjsp.setText("");
		lxr.setText("叶小燕");
		lxfs.setText("80785000");
		yxq.setText("从20120815至20120904");
	}

	private void setData3() {
		company_name.setText("江苏亚威机床股份有限公司");
		job_type.setText("计算机工程技术人员");
		gzsm.setText("计算机软件及应用、数控系统二次开发");
		zpzrs.setText("3 (男0 女0 兼招3)");
		ygqy.setText("本埠");
		gzdd.setText("");
		dzxx.setText("");
		nlyq.setText("最小年龄30  最大年龄45");
		whcd.setText("大专");
		jsdj.setText("");
		cyls.setText("");
		ygz.setText("年薪6-10万");
		fldy.setText("");
		sstg.setText("");
		jszc.setText("");
		hkxz.setText("");
		zpdx.setText("");
		wyyz.setText("");
		wysp.setText("");
		jsjsp.setText("");
		lxr.setText("宋道宏");
		lxfs.setText("13952511428");
		yxq.setText("从20120815至20120904");
	}
}
