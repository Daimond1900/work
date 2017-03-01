package com.yifeng.hnzpt.ui.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yifeng.hnzpt.R;
import com.yifeng.hnzpt.adapter.MyBaseAdapter;
import com.yifeng.hnzpt.data.TrainingDAL;
import com.yifeng.hnzpt.ui.BaseListActivity;
import com.yifeng.hnzpt.util.ConstantUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ClassName:TrainingListActivity 
 * Description：培训信息-列表
 * @author Administrator 
 * Date：2012-10-24
 */
public class TrainingListActivity extends BaseListActivity implements
OnClickListener 
{
	private TextView titleTxt;
	private EditText inputEdt;
	private Button backBtn, searchBtn;
	private TrainingDAL trainingDAL;
	private MyBaseAdapter adapter;
	private String keyWord = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_list);

		trainingDAL = new TrainingDAL(this);

		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText("就业新闻");
		inputEdt = (EditText) findViewById(R.id.searchEdt);

		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);

	 
		intiListview();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
					Intent intent = new Intent(TrainingListActivity.this,TrainDeatilActivity.class);
					//String articleid=list.get(arg2).get("articleid").toString();
					String news_id=SURPERDATA.get(arg2-1).get("news_id").toString();
					
					Bundle bundle = new Bundle();
					bundle.putString("title", "就业信息详情");
					bundle.putString("url", ConstantUtil.ip+"android/news/queryNewsDetail?news_id="+news_id);
					intent.putExtras(bundle); 
					startActivity(intent);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		 
	}
 

	 
 

	 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backBtn:
			this.finish();
			break;
		case R.id.searchBtn:
			keyWord = commonUtil.doConvertEmpty(inputEdt.getText().toString());
			SUPERPAGENUM = 0;
			intiListview();
			break;
		default:
			break;
		}
	}

 

	@Override
	protected void setAdapter() {
		adapter = new MyBaseAdapter(this, SURPERDATA,
				R.layout.training_list_item, new String[] { "t_title",
						"t_from", "t_date" }, new int[] {
						R.id.training_title, R.id.training_from,
						R.id.training_date }){

							@Override
							protected void iniview(View view, int position,
									List<? extends Map<String, ?>> data) {
							}};
		adapter.setViewBinder();
		listview.setAdapter(adapter);

	}

	@Override
	protected void formatDate() {
		for (Map<String, String> tm : STRINGLIST) {
			Map<String, Object> otm = new HashMap<String, Object>();
		 
//			tm.put("t_title", tm.get("title"));
//			tm.put("t_from", "信息来源:" + tm.get("author"));
//			tm.put("t_date", "发布时间:" + tm.get("pubtime"));
//			tm.put("readCount", "阅读(" + tm.get("hits") + ")");// 阅读量
			tm.put("t_title",tm.get("title"));
			tm.put("news_id",tm.get("news_id"));
			tm.put("t_from", "发布单位:"+tm.get("create_by")==null?"":"发布单位:"+tm.get("create_by"));
			tm.put("t_date","发布时间:"+tm.get("create_time"));
			tm.put("readCount", tm.get("hits"));
			for (String ts : tm.keySet()) {
				otm.put(ts, tm.get(ts));
			}

			SURPERDATA.add(otm);
		}

	}

	@Override
	protected void myNotifyDataSetChanged() {
		adapter.notifyDataSetChanged();

	}

	@Override
	protected List<Map<String, String>> setDataMethod() {
		 
		return  trainingDAL.doQuery(keyWord, SUPERPAGENUM);
	}

}
