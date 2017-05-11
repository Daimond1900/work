package com.yifeng.ChifCloud12345update;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.Button;

public class HotLinkActivity extends Activity implements OnFocusChangeListener {
	WebView webview;
	Button   back, myjb, drdp, ndzj, ldps;

	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rxtb);
		webview = (WebView) findViewById(R.id.webview);
		webview.setBackgroundColor(0); 
		myjb = (Button) findViewById(R.id.myjb);
		drdp = (Button) findViewById(R.id.drdp);
		ndzj = (Button) findViewById(R.id.ndzj);
		ldps = (Button) findViewById(R.id.ldps);
		myjb.setOnFocusChangeListener(this);
		drdp.setOnFocusChangeListener(this);
		ndzj.setOnFocusChangeListener(this);
		ldps.setOnFocusChangeListener(this);
		myjb.requestFocus();
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HotLinkActivity.this.finish();
			}
		});
		
		
		loadData("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title>12345简报</title>"
				+ "</head>"
				+ "<style type=\"text/css\">"
				+ ".STYLE1 {"
				+ "	font-size: 18px;"
				+ "	font-family: \"幼圆\";"
				+ "	font-weight: bold;"
				+ "}"
				+ ".STYLE2 {font-family: \"幼圆\"; padding-left:4px; padding-right:4px; text-indent:2em; line-height:25px;}"
				+ ".STYLE3 {font-family: \"幼圆\";}"
				+ "</style>"

				+ "<body>"
				+ "<p align=\"center\" class=\"STYLE1\">12345简报</p>"
				+ "<p align=\"center\" class=\"STYLE3\">2011-11-3 07:00</p>"
				+ "<p class=\"STYLE2\">自11月2日正式开通以来，12345政府服务热线累计接听电话3698件，平均每小时接话量13件，来电主要集中分为求助类、咨询类、投诉类、举报类、议类，工作人员现场解答率达68.3&#37;，电子工单交办率为23.5&#37;，有效电话办结率达92&#37;。</p>"
				+ "<p class=\"STYLE2\">为进一步推进服务型政府建设，畅通群众诉求渠道，营造良好的经济社会发展环境，更好地为民便民利民，市政府决定在原有12345市长公开电话的基础上，整合各地各部门为民服务电话，形成覆盖全市、资源共享、系统相连、协调互助、便捷高效、保障有力的政府公共服务热线体系，实现群众拨打一个电话或发一个帖子，即可得到及时、规范的回应和服务。目前，初步纳入12345政府服务热线的成员单位有83个。市民拨打12345后，由人工接听受理平台统一接听接收，对咨询类问题，依据知识库标准答案立即答复来电人；对不能立即答复的诉求事项，按照规定流程分类处理；不能当天答复的，收到电子工单次日起在规定的工作日内办结和答复诉求人；对超过办理期限又未说明理由的，将发出《催办通知书》。同时，市政府办公室和市监察局对市政府服务热线办理情况进行督查，并开展综合考评，对拖延耽搁、敷衍了事、推诿扯皮的承接单位，将追究相关人员责任。</p>"
				+ "</body>" + "</html>");
	}

	void loadData(String data) {

		webview.loadData(
				data, "text/html", "UTF-8");

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.myjb:
			
			loadData("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>12345简报</title>"
					+ "</head>"
					+ "<style type=\"text/css\">"
					+ ".STYLE1 {"
					+ "	font-size: 18px;"
					+ "	font-family: \"幼圆\";"
					+ "	font-weight: bold;"
					+ "}"
					+ ".STYLE2 {font-family: \"幼圆\"; padding-left:4px; padding-right:4px; text-indent:2em; line-height:25px;}"
					+ ".STYLE3 {font-family: \"幼圆\";}"
					+ "</style>"

					+ "<body>"
					+ "<p align=\"center\" class=\"STYLE1\">12345简报</p>"
					+ "<p align=\"center\" class=\"STYLE3\">2011-11-3 07:00</p>"
					+ "<p class=\"STYLE2\">自11月2日正式开通以来，12345政府服务热线累计接听电话3698件，平均每小时接话量13件，来电主要集中分为求助类、咨询类、投诉类、举报类、议类，工作人员现场解答率达68.3&#37;，电子工单交办率为23.5&#37;，有效电话办结率达92&#37;。</p>"
					+ "<p class=\"STYLE2\">为进一步推进服务型政府建设，畅通群众诉求渠道，营造良好的经济社会发展环境，更好地为民便民利民，市政府决定在原有12345市长公开电话的基础上，整合各地各部门为民服务电话，形成覆盖全市、资源共享、系统相连、协调互助、便捷高效、保障有力的政府公共服务热线体系，实现群众拨打一个电话或发一个帖子，即可得到及时、规范的回应和服务。目前，初步纳入12345政府服务热线的成员单位有83个。市民拨打12345后，由人工接听受理平台统一接听接收，对咨询类问题，依据知识库标准答案立即答复来电人；对不能立即答复的诉求事项，按照规定流程分类处理；不能当天答复的，收到电子工单次日起在规定的工作日内办结和答复诉求人；对超过办理期限又未说明理由的，将发出《催办通知书》。同时，市政府办公室和市监察局对市政府服务热线办理情况进行督查，并开展综合考评，对拖延耽搁、敷衍了事、推诿扯皮的承接单位，将追究相关人员责任。</p>"
					+ "</body>" + "</html>");
			break;
		case R.id.drdp:
			
			loadData("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>当日点评</title>"
					+ "</head>"
					+ "<style type=\"text/css\">"
					+ ".STYLE1 {"
					+ "	font-size: 18px;"
					+ "	font-family: \"幼圆\";"
					+ "	font-weight: bold;"
					+ "}"
					+ ".STYLE2 {font-family: \"幼圆\"; padding-left:4px; padding-right:4px; text-indent:2em; line-height:25px;}"
					+ ".STYLE3 {font-family: \"幼圆\";}"
					+ "</style>"

					+ "<body>"
					+ "<p align=\"center\" class=\"STYLE1\">当日点评</p>"
					+ "<p align=\"center\" class=\"STYLE3\">"+df.format(new Date())+"</p>"
					+ "<p class=\"STYLE2\">政府服务热线累计接听电话3698件，平均每小时接话量13件，来电主要集中分为求助类、咨询类、投诉类、举报类、议类，工作人员现场解答率达68.3&#37;，电子工单交办率为23.5&#37;，有效电话办结率达92&#37;。</p>"
					+ "<p class=\"STYLE2\">为进一步推进服务型政府建设，畅通群众诉求渠道，营造良好的经济社会发展环境，更好地为民便民利民，市政府决定在原有12345市长公开电话的基础上，整合各地各部门为民服务电话，形成覆盖全市、资源共享、系统相连、协调互助、便捷高效、保障有力的政府公共服务热线体系，实现群众拨打一个电话或发一个帖子，即可得到及时、规范的回应和服务。目前，初步纳入12345政府服务热线的成员单位有83个。市民拨打12345后，由人工接听受理平台统一接听接收，对咨询类问题，依据知识库标准答案立即答复来电人；对不能立即答复的诉求事项，按照规定流程分类处理；不能当天答复的，收到电子工单次日起在规定的工作日内办结和答复诉求人；对超过办理期限又未说明理由的，将发出《催办通知书》。同时，市政府办公室和市监察局对市政府服务热线办理情况进行督查，并开展综合考评，对拖延耽搁、敷衍了事、推诿扯皮的承接单位，将追究相关人员责任。</p>"
					+ "</body>" + "</html>");
			break;
		case R.id.ndzj:
			
			loadData("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>年度总结</title>"
					+ "</head>"
					+ "<style type=\"text/css\">"
					+ ".STYLE1 {"
					+ "	font-size: 18px;"
					+ "	font-family: \"幼圆\";"
					+ "	font-weight: bold;"
					+ "}"
					+ ".STYLE2 {font-family: \"幼圆\"; padding-left:4px; padding-right:4px; text-indent:2em; line-height:25px;}"
					+ ".STYLE3 {font-family: \"幼圆\";}"
					+ "</style>"

					+ "<body>"
					+ "<p align=\"center\" class=\"STYLE1\">年度总结</p>"
					+ "<p align=\"center\" class=\"STYLE3\">"+df.format(new Date())+"</p>"
					+ "<p class=\"STYLE2\">热线累计接听电话3698件，平均每小时接话量13件，来电主要集中分为求助类、咨询类、投诉类、举报类、议类，工作人员现场解答率达68.3&#37;，电子工单交办率为23.5&#37;，有效电话办结率达92&#37;。</p>"
					+ "<p class=\"STYLE2\">为进一步推进服务型政府建设，畅通群众诉求渠道，营造良好的经济社会发展环境，更好地为民便民利民，市政府决定在原有12345市长公开电话的基础上，整合各地各部门为民服务电话，形成覆盖全市、资源共享、系统相连、协调互助、便捷高效、保障有力的政府公共服务热线体系，实现群众拨打一个电话或发一个帖子，即可得到及时、规范的回应和服务。目前，初步纳入12345政府服务热线的成员单位有83个。市民拨打12345后，由人工接听受理平台统一接听接收，对咨询类问题，依据知识库标准答案立即答复来电人；对不能立即答复的诉求事项，按照规定流程分类处理；不能当天答复的，收到电子工单次日起在规定的工作日内办结和答复诉求人；对超过办理期限又未说明理由的，将发出《催办通知书》。同时，市政府办公室和市监察局对市政府服务热线办理情况进行督查，并开展综合考评，对拖延耽搁、敷衍了事、推诿扯皮的承接单位，将追究相关人员责任。</p>"
					+ "</body>" + "</html>");
			break;
		case R.id.ldps:
			
			loadData("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>领导批示</title>"
					+ "</head>"
					+ "<style type=\"text/css\">"
					+ ".STYLE1 {"
					+ "	font-size: 18px;"
					+ "	font-family: \"幼圆\";"
					+ "	font-weight: bold;"
					+ "}"
					+ ".STYLE2 {font-family: \"幼圆\"; padding-left:4px; padding-right:4px; text-indent:2em; line-height:25px;}"
					+ ".STYLE3 {font-family: \"幼圆\";}"
					+ "</style>"

					+ "<body>"
					+ "<p align=\"center\" class=\"STYLE1\">领导批示</p>"
					+ "<p align=\"center\" class=\"STYLE3\">"+df.format(new Date())+"</p>"
					+ "<p class=\"STYLE2\">助类、咨询类、投诉类、举报类、议类，工作人员现场解答率达68.3&#37;，电子工单交办率为23.5&#37;，有效电话办结率达92&#37;。</p>"
					+ "<p class=\"STYLE2\">为进一步推进服务型政府建设，畅通群众诉求渠道，营造良好的经济社会发展环境，更好地为民便民利民，市政府决定在原有12345市长公开电话的基础上，整合各地各部门为民服务电话，形成覆盖全市、资源共享、系统相连、协调互助、便捷高效、保障有力的政府公共服务热线体系，实现群众拨打一个电话或发一个帖子，即可得到及时、规范的回应和服务。目前，初步纳入12345政府服务热线的成员单位有83个。市民拨打12345后，由人工接听受理平台统一接听接收，对咨询类问题，依据知识库标准答案立即答复来电人；对不能立即答复的诉求事项，按照规定流程分类处理；不能当天答复的，收到电子工单次日起在规定的工作日内办结和答复诉求人；对超过办理期限又未说明理由的，将发出《催办通知书》。同时，市政府办公室和市监察局对市政府服务热线办理情况进行督查，并开展综合考评，对拖延耽搁、敷衍了事、推诿扯皮的承接单位，将追究相关人员责任。</p>"
					+ "</body>" + "</html>");
			break;

		default:
			break;
		}

	}

}
