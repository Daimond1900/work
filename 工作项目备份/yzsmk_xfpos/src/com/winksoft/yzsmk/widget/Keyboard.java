package com.winksoft.yzsmk.widget;

import com.winksoft.yzsmk.ibase.IKeybord;
import com.winksoft.yzsmk.xfpos.R;
import com.winksoft.yzsmk.xfpos.xf.PayByCreditCardActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Keyboard extends LinearLayout implements  View.OnClickListener,
											   View.OnLongClickListener{

	private EditText mEditText;
	IKeybord iKeybord;
	
	public Keyboard(Context context) {
		super(context);
	}

	public Keyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected void onFinishInflate(){
		super.onFinishInflate();
		//查找button 绑定事件
		int layout = getChildCount();
		int clen = 0;
		ViewGroup view;
		for(int i=0;i<layout;i++){
			view = (ViewGroup)this.getChildAt(i);
			clen = view.getChildCount();
			for(int j=0;j<clen;j++){
				((ViewGroup)this.getChildAt(i)).getChildAt(j).setOnClickListener(this);
			}
		}
	}

	@Override
	public boolean onLongClick(View view) {
		if(view.getId()==R.id.btn_keyboard_del){
			//长按清空
			if(mEditText!=null)
				mEditText.setText("");
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		if(mEditText==null)
			return;
		
		int id = view.getId();
		int cursor = mEditText.getSelectionStart();
		switch (id) {
		case R.id.btn_keyboard_del:
			if(cursor>=1)
				mEditText.getText().delete(cursor-1,cursor);
			break;
		case R.id.btDetermine:
			if(iKeybord != null)
				iKeybord.onConfirm();
			break;
		case R.id.btCancel:
			if(iKeybord != null)
				iKeybord.onCancel();
			break;
		default:   //number
			if (view instanceof Button) {
				 String text = ((Button) view).getText().toString();
				 mEditText.getText().insert(cursor, text);
			}
			break;
		}
	}
	
	public void setFocusEditText(EditText mEditText,IKeybord iKeybord){
		this.mEditText = mEditText;
		this.iKeybord = iKeybord;
	}
}
