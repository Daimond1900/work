package com.winksoft.android.yzjycy.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.util.DataConvert;
import android.content.Context;
import android.os.Handler;

/**
 * @author ZK
 * 通讯录
 */
public class SetDAL extends BaseDAL {

	public SetDAL(Context context, Handler handler) {
		super(context, handler);
	}
	
	/**
	 * 通讯录人员列表
	 */
	public List<Map<String, String>> doQuery() {
		Map<String, String> param = new HashMap<String, String>();
		this.setUrl(this.getIpconfig() + "android/managementcloud/listContact");
		String json = this.doGet(param);
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toArrayList(map.get("news"));
		else
			return null;
	}
}
