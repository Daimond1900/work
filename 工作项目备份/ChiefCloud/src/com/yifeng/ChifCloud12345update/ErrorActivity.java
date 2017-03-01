package com.yifeng.ChifCloud12345update;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.yifeng.util.ActivityStackControlUtil;

public class ErrorActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.error);
    	ActivityStackControlUtil.add(this);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
   		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
   			ActivityStackControlUtil.finishProgram();//退出
   			return true;
   		}
   		return super.onKeyDown(keyCode, event);
   	}
}
