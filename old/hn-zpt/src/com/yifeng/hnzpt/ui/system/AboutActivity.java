package com.yifeng.hnzpt.ui.system;

import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.ui.BaseActivity;
import com.yifeng.hnzpt.util.AutoUpdate;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * ClassName:AboutActivity 
 * Description：系统设置-关于我们
 * @author Administrator 
 * Date：2012-10-16
 */
public class AboutActivity extends BaseActivity {
	private TextView introduceTxt;
	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_about);

		introduceTxt = (TextView) findViewById(R.id.introduceTxt);
		AutoUpdate autoUpdate=new AutoUpdate(this);
		String text = "\n1.版本号：A"+autoUpdate.versionName;
		text += "\n\t此版本适用于Android 2.1及以上操作系统；为使本软件达到最优性能，推荐使用中国电信3G网络。";
		text += "\n\n2.主办单位：海南省就业局";
		text += "\n\n3.版权所有：中国电信海南分公司";
		text += "\n\n4.技术支持：中国电信海南分公司";

		introduceTxt.setText(text);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
			}
		});
	}

}