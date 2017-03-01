package com.yifeng.qzt.ui.system;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hngly.ui.BaseActivity;
import com.yifeng.hngly.ui.R;

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
		PackageInfo info=null;
		try {
			info = this.getPackageManager().getPackageInfo(

					this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String text = "\n1.如何获取最新版本？";
		text += "\n\t当提示有更新是，请及时更新，";
		text += "\n2.版本信息：就业云--基层版A" +  (info==null?"1.0":info.versionName)+
				"版本";
		text += "\n3.功能简介：";
		text += "\n\t本软件主要提供给基层工作人员使用，提供劳动力信息登记、查询、修改。农村零转移家庭的信息查看、增加、修改。";
		text += "\n4.使用说明：";
		text += "\n\t下载安装本软件后，登陆使用。";
		text += "\n\n\n\t\t感谢你的使用，祝你一切顺利！";
		introduceTxt.setText(text);
		
	}
	
}
