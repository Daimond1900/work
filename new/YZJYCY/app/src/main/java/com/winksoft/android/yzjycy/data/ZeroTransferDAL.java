package com.winksoft.android.yzjycy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.data.BaseDAL;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.ReJson;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class ZeroTransferDAL extends BaseDAL {
	private final String TAG = "ZeroTransferDAL";
	User user;
	public ZeroTransferDAL(Context context, Handler handler) {
		super(context, handler);
		user = new UserSession(context).getUser();
	}

	/**
	 * 零转移列表
	 * @param page
	 * @return
	 */
	public ReJson getListById(String page, String deptId,String cun) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("page", page);
//		param.put("aae017", "4028c2e941fd35f60141fdab774a0002");//deptId
		param.put("aae017", user.getDepartment_id());//deptId
		param.put("hzsfz", cun);
//		param.put("aae011", "4028c2e9497d8eaf01497d91ac8a0002");//user.getUserId()
		param.put("aae011", user.getUserId());//
		this.setUrl(this.getIpconfig() + "android/root/listZeroTransfer");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json, "families");
	}
	public ReJson queryZeroTransferDetail(String id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("hzsfz", id);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryZeroTransferDetail");
		String json = this.doGet(param);
		return new ReJson(context, handler).setValue(json, "family", "map");
	}

	/**
	 * 修改零转移家庭 hzsfz 身份证号码 hzxm 户主姓名 ldls 劳动力数 lxdh 联系电话 bz 备注 aae001（经办人代码）
	 * 、aae019（经办人姓名） 、aae017（经办机构代码） 、aae020（经办机构名称）
	 * 
	 * @return
	 */
	public ReJson modifyZeroTransferDetail(String id,String hzsfz, String hzxm,
			String ldls, String lxdh, String bz, String aae001, String aae019,
			String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("id", id));
		pos_params.add(new BasicNameValuePair("hzsfz", hzsfz));
		pos_params.add(new BasicNameValuePair("hzxm", hzxm));
		pos_params.add(new BasicNameValuePair("ldls", ldls));
		pos_params.add(new BasicNameValuePair("lxdh", lxdh));
		pos_params.add(new BasicNameValuePair("bz", bz));
		pos_params.add(new BasicNameValuePair("aae001", aae001));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/modifyZeroTransferDetail");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);

	}

	/**
	 * 注销 hzsfz 户主身份证 aae001（经办人代码）、 aae019（经办人姓名）、aae017（经办机构代码）、
	 * aae020（经办机构名称）
	 * 
	 * @return
	 */
	public ReJson modifyZeroTransferStatus(String id,String hzsfz, String aae001,
			String aae019, String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("id", id));
		pos_params.add(new BasicNameValuePair("hzsfz", hzsfz));
		pos_params.add(new BasicNameValuePair("aae001", aae001));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/modifyZeroTransferStatus");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}

	/**
	 * 删除零转移
	 * 
	 * 家庭成功 hzsfz 户主身份证 aae001（经办人代码）、
	 * aae019（经办人姓名）、aae017（经办机构代码）、aae020（经办机构名称）
	 * 
	 * @return
	 */
	public ReJson deleteZeroTransfer(String id,String hzsfz, String aae001,
			String aae019, String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("id", id));
		pos_params.add(new BasicNameValuePair("hzsfz", hzsfz));
		pos_params.add(new BasicNameValuePair("aae001", aae001));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/deleteZeroTransfer");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}
/**
 * 查询零转移家庭
 * @param hzsfz
 * @return
 */
	public ReJson add_queryZeroTransferDetail(String hzsfz,String aae017) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("hzsfz", hzsfz);
		param.put("aae017", aae017);
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/add_queryZeroTransferDetail");
		String json = this.doGet(param);
//		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
//		pos_params.add(new BasicNameValuePair("hzsfz", hzsfz));
//		this.setUrl(this.getIpconfig()
//				+ "android/rootscloud/add_queryZeroTransferDetail");
//		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json,"person","map");
	}
	
	
	
	/**
	 * 新增零转移家庭
	 * @param hzsfz
	 * @param hzxm
	 * @param ldls
	 * @param lxdh
	 * @param bz
	 * @param aae001
	 * @param aae019
	 * @param aae017
	 * @param aae020
	 * @return
	 */
	public ReJson addZeroTransferDetail(String hzsfz, String hzxm,
			String ldls, String lxdh, String bz, String aae001, String aae019,
			String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("hzsfz", hzsfz));
		pos_params.add(new BasicNameValuePair("hzxm", hzxm));
		pos_params.add(new BasicNameValuePair("ldls", ldls));
		pos_params.add(new BasicNameValuePair("lxdh", lxdh));
		pos_params.add(new BasicNameValuePair("bz", bz));
		pos_params.add(new BasicNameValuePair("aae001", aae001));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/addZeroTransferDetail");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}
}
