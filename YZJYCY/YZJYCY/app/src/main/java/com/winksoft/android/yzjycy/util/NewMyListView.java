package com.winksoft.android.yzjycy.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NewMyListView extends ListView{

	public NewMyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 @Override
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	                MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec);
	    }

}
