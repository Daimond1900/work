package com.yifeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.yifeng.ChifCloud12345update.MainActivity;
import com.yifeng.util.CommonUtil;
import com.yifeng.util.DataConvert;
import com.yifeng.util.FormFile;
import com.yifeng.util.UploadImage;

public class NotifyDAL extends BaseDAL {

	public NotifyDAL(Context context) {
		super(context);
	}
/**
 * 催办通知
 * @param map
 * @return
 */
	public String doQury(Map<String, String> map) {
		this.setUrl(this.getIpconfig() + "android/wsform/doQueryPressingNotice");
		return this.doGet(map);
	}

}
