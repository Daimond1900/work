package com.yifeng.hnqzt.ui.system;

import com.yifeng.hnqzt.R;
import com.yifeng.hnqzt.ui.BaseActivity;
import com.yifeng.hnqzt.ui.LoginActivity;
import com.yifeng.hnqzt.util.AutoUpdate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * comments:系统设置-系统帮助
 * @author Administrator
 * Date: 2012-9-2
 */
public class HelpActivity extends BaseActivity
{
	private TextView introduceTxt;
	private Button backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_help);
		
		introduceTxt = (TextView) findViewById(R.id.introduceTxt);
		
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				HelpActivity.this.finish();
			}
		});
		AutoUpdate autoUpdate=new AutoUpdate(this);
		String text = "\n1.如何获取最新版本？";
		text += "\n\t\t当提示有更新是，请及时更新，";
		text += "\n2.版本信息：就业云--求职版 A"+autoUpdate.versionName+"版本";
		text += "\n3.功能简介：";
		text += "\n\t\t本软件提供最全最新最准确的企业职位招聘信息，为企业提供人才招聘、培训等全方位的人力资源服务，帮助个人求职者与企业搭建最佳的人才招募和人才培养渠道。";
		text += "\n4.使用说明：";
		text += "\n\t\t下载安装本软件后，即可使用，若要获取更多操作权限，需注册登录。";
		text += "\n\n\t\t感谢你的使用，祝你一切顺利！";
		introduceTxt.setText(text);
		
	}
	
}
