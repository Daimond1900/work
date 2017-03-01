package com.yifeng.hngly.ui.Statistical;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yifeng.hngly.ui.BaseWebView;
import com.yifeng.hngly.ui.R;

public class StatisticalActivity extends BaseWebView implements OnClickListener{
	
	private TextView title;
	private Button backBtn,btn_start,btn_end,search_btn;
	private String fixUrl="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistical_detail);
		
		Bundle qz = this.getIntent().getExtras();
		title = (TextView)findViewById(R.id.top_titleTxt);
		title.setText(qz.getString("title"));
		fixUrl=qz.getString("url");
		

		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		btn_end = (Button) findViewById(R.id.btn_end);
		btn_end.setOnClickListener(this);
		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		
		Map<String, String> map = getFirstDay();
		btn_start.setText(map.get("first"));
		btn_end.setText(map.get("now"));
		loadView(map.get("first"),map.get("now"));
	}
	
	private void loadView(String start_time,String end_time){
		if(!fixUrl.equals("")){
			String url = fixUrl + "?department="+user.getDepartmentid()+"&start_date="+start_time+"&end_date="+end_time;
			this.setUrl(url);
			this.doInitView();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			commonUtil.getTime(btn_start);
			break;
		case R.id.btn_end:
			commonUtil.getTime(btn_end);
			break;
		case R.id.search_btn:
			String s_date = btn_start.getText().toString();
			String e_date = btn_end.getText().toString();
			if(s_date.equals("")){
				commonUtil.shortToast("请选择开始日期！");
				return;
			}
			if(e_date.equals("")){
				commonUtil.shortToast("请选择结束日期！");
				return;
			}
			if(Integer.parseInt(s_date.replace("-", "")) > Integer.parseInt(e_date.replace("-", ""))){
				commonUtil.shortToast("结束日期不能大于开始日期！");
				return;
			}
			loadView(s_date,e_date);
			break;
		case R.id.backBtn:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 当月第一天
	 * 
	 * @return
	 */
	private Map<String, String> getFirstDay() {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		map.put("now", df.format(theDate));
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		// StringBuffer str = new StringBuffer().append(day_first);
		map.put("first", day_first);
		return map;
	}
	
}
