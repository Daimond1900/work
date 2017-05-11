package com.winksoft.yzsmk.czpos.cz;

import com.winksoft.yzsmk.czpos.R;
import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.ibase.IKeybord;
import com.winksoft.yzsmk.widget.Keyboard;
import com.winksoft.yzsmk.widget.MoneyEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class CzActivity extends BaseActivity implements IKeybord {
	private Keyboard mKeyboard;
	private MoneyEditText mEditMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cz);
		setTitle(getString(R.string.app_name));
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
 
		mKeyboard  = (Keyboard)this.findViewById(R.id.keyboard);
	    mEditMoney = (MoneyEditText)this.findViewById(R.id.enter_pay);
	    mKeyboard.setFocusEditText(mEditMoney,this);
	}

	@Override
	public void onConfirm() {
		Intent intent = new Intent(this,CzByCreditCardActivity.class);
		intent.putExtra("money", mEditMoney.getMoney());
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onCancel() {
		this.finish();
	}
}

