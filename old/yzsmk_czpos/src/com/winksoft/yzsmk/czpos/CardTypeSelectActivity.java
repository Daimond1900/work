/**
 * 
 */
package com.winksoft.yzsmk.czpos;

import java.util.Map.Entry;
import java.util.Set;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.winksoft.yzsmk.czpos.R;



/**
 * @author mcbpc
 *
 */
public class CardTypeSelectActivity extends Activity {
	private TouristSpot spot = new TouristSpot();
	private CheckBox[] cbCardTypeList = new CheckBox[spot.newCardMap.size()];
	private LinearLayout llCardTypeSelect;
	private Button btnOK;
	private Button btnCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.card_type_select);
		llCardTypeSelect = (LinearLayout) this.findViewById(R.id.llCardTypeSelect);
		btnOK = (Button) this.findViewById(R.id.btnConfigOK);
		btnOK.setOnClickListener(onOk);
		btnCancel= (Button) this.findViewById(R.id.btnConfigCancel);
		btnCancel.setOnClickListener(onCancel);
		int m=0;
		
		for(Entry en: spot.newCardMap.entrySet()){
			cbCardTypeList[m] = new CheckBox(this);
			llCardTypeSelect.addView(cbCardTypeList[m]);
			cbCardTypeList[m].setText((CharSequence) en.getValue());
		}
		
		//读取参数
	}
	
	
	//开始服务
	private OnClickListener onOk = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			
		}
	};
		
		
		
	private OnClickListener onCancel = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};
	
}
