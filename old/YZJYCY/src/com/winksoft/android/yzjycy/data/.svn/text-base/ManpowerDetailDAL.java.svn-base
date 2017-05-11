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
import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.ReJson;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class ManpowerDetailDAL extends BaseDAL {
	private final String TAG = "ManpowerDetailDAL";
	User user;
	public ManpowerDetailDAL(Context context, Handler handler) {
		super(context, handler);
		user = new UserSession(context).getUser();
	}

	/**
	 * 获取所有劳动力
	 * 
	 * @param idcardno
	 * @param name
	 * @param page
	 * @return
	 */
	public List<Map<String, String>> doGetAll(String idcardno, String name,
			int page, String deptId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		param.put("aac003", name + "");
		param.put("page", page + "");
//		param.put("aae017", "4028c2e941fd35f60141fdab774a0002"); //deptId
//		param.put("aae011", "4028c2e9497d8eaf01497d91ac8a0002");	//user.getUserId()
		param.put("aae011", user.getUserId());//
		param.put("aae017", user.getDepartment_id());//deptId
		this.setUrl(this.getIpconfig() + "android/root/listLabourInfo");
		String json = this.doGet(param);
		Log.d(TAG, "劳动力列表: "+"接口： "+"android/root/listLabourInfo");
		Log.d(TAG, "劳动力列表: "+"参数： "+param.toString());
		Log.d(TAG, "劳动力列表: "+"返回值： "+json);
		return new ReJson(context, handler).setValue(json, "labours").getList();
	}

	/**
	 * 获取基本信息
	 * 
	 * @param idcardno
	 * @return
	 */
	public Map<String, String> getDetailByCard(String idcardno) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryLabourInfoDetail");
		String json = this.doGet(param);
		
		Log.d(TAG, "获取基本信息: "+"接口： "+"android/root/queryLabourInfoDetail");
		Log.d(TAG, "获取基本信息: "+"参数： "+param.toString());
		Log.d(TAG, "获取基本信息: "+"返回值： "+json);
		
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true")) {
			if (map.get("detail") != null)
				return DataConvert.toMap(map.get("detail"));
			else
				return null;
		}

		else
			return null;
	}

	/**
	 * 获取就业状况
	 * 
	 * @param idcardno
	 * @return
	 */
	public Map<String, String> queryEmploymentStatus(String idcardno) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryEmploymentStatus");
		String json = this.doGet(param);
		
		Log.d(TAG, "获取就业状况: "+"接口： "+"android/root/queryEmploymentStatus");
		Log.d(TAG, "获取就业状况: "+"参数： "+param.toString());
		Log.d(TAG, "获取就业状况: "+"返回值： "+json);
		
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toMap(map.get("detail"));
		else
			return null;
	}

	/**
	 * 求职意愿
	 * 
	 * @param idcardno
	 * @return
	 */
	public Map<String, String> queryJobAspiration(String idcardno) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryJobAspiration");
		String json = this.doGet(param);
		
		Log.d(TAG, "求职意愿: "+"接口： "+"android/root/queryJobAspiration");
		Log.d(TAG, "求职意愿: "+"参数： "+param.toString());
		Log.d(TAG, "求职意愿: "+"返回值： "+json);
		
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toMap(map.get("detail"));
		else
			return null;
	}

	/**
	 * 资源减少
	 * 
	 * @param idcardno
	 * @return
	 */
	public Map<String, String> queryResourceReduction(String idcardno) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryResourceReduction");
		String json = this.doGet(param);
		
		Log.d(TAG, "资源减少: "+"接口： "+"android/root/queryResourceReduction");
		Log.d(TAG, "资源减少: "+"参数： "+param.toString());
		Log.d(TAG, "资源减少: "+"返回值： "+json);
		
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toMap(map.get("detail"));
		else
			return null;
	}

	/**
	 * 培训意愿
	 * 
	 * @param idcardno
	 * @return
	 */
	public Map<String, String> queryTrainingAspiration(String idcardno) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("aac002", idcardno);
		this.setUrl(this.getIpconfig()
				+ "android/root/queryTrainingAspiration");
		String json = this.doGet(param);
		
		Log.d(TAG, "培训意愿: "+"接口： "+"android/root/queryTrainingAspiration");
		Log.d(TAG, "培训意愿: "+"参数： "+param.toString());
		Log.d(TAG, "培训意愿: "+"返回值： "+json);
		
		Map<String, String> map = DataConvert.toMap(json);
		if (map.get("success") != null && map.get("success").equals("true"))
			return DataConvert.toMap(map.get("detail"));
		else
			return null;
	}

	/**
	 * 新增劳动力
	 * 
	 * 身份证号码：idcardno 、姓名：name、联系电话：tel、 民族：nationality、政治面貌：policital、
	 * 户口性质：household、文化程度：diploma、家庭住址：addr、
	 * 
	 * 经办人代码：aae011、经办人姓名：aae019、经办机构代码：aae017、
	 * 经办机构名称：aae020、所在地区代码：aab301、所在地区名称：aac010
	 * 
	 * @return
	 */
	public ReJson inseartNewLdl(String idcardno, String name, String tel,
			String nationality, String policital, String household,
			String diploma, String addr, String aae011, String aae019,
			String aae017, String aae020, String aab301, String aac010) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("name", name));
		pos_params.add(new BasicNameValuePair("tel", tel));
		pos_params.add(new BasicNameValuePair("nationality", nationality));
		pos_params.add(new BasicNameValuePair("policital", policital));
		pos_params.add(new BasicNameValuePair("household", household));
		pos_params.add(new BasicNameValuePair("diploma", diploma));
		pos_params.add(new BasicNameValuePair("addr", addr));

		pos_params.add(new BasicNameValuePair("aae011", aae011));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		pos_params.add(new BasicNameValuePair("aab301", aab301));
		pos_params.add(new BasicNameValuePair("aac010", aac010));

		this.setUrl(this.getIpconfig() + "android/rootscloud/addLabourInfo");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}

	/**
	 * 修改劳动力详情
	 * 
	 * @param idcardno
	 * @param name
	 * @param tel
	 * @param nationality
	 * @param policital
	 * @param household
	 * @param diploma
	 * @param addr
	 * @param aae011
	 * @param aae019
	 * @param aae017
	 * @param aae020
	 * @param aab301
	 * @param aac010
	 * @return
	 */

	public ReJson modifyLabourInfo(String idcardno, String name, String tel,
			String nationality, String policital, String household,
			String diploma, String addr, String aae011, String aae019,
			String aae017, String aae020, String aab301, String aac010) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("name", name));
		pos_params.add(new BasicNameValuePair("tel", tel));
		pos_params.add(new BasicNameValuePair("nationality", nationality));
		pos_params.add(new BasicNameValuePair("policital", policital));
		pos_params.add(new BasicNameValuePair("household", household));
		pos_params.add(new BasicNameValuePair("diploma", diploma));
		pos_params.add(new BasicNameValuePair("addr", addr));
		pos_params.add(new BasicNameValuePair("aae011", aae011));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		pos_params.add(new BasicNameValuePair("aab301", aab301));
		pos_params.add(new BasicNameValuePair("aac010", aac010));

		this.setUrl(this.getIpconfig() + "android/rootscloud/modifyLabourInfo");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}

	/**
	 * 修改就业状态
	 * 
	 * 身份证号码：idcardno、 基本情况：acc100、 就业状态：acc926、 目前就业产业：aab054、 从业行业：aab022、
	 * 就业形势：acc90b、 是否返乡创业人员：acc90h、 就业地区类型：acc901、 acc902 就业地区 acc423 就业方式
	 * acc903 务工年限 acb214 今年务工时间(月) acc034 务工月工资 acc22b 是否签订劳动合同 acc905
	 * 尚未转移人员就业意向 acc906 是否建工部门成建制输出
	 * 
	 * aae011（经办人代码）、 aae019（经办人姓名）、 aae017（经办机构代码）、 aae020（经办机构名称）
	 * 
	 * @return
	 */
	public ReJson modifyEmploymentStatus(String idcardno, String acc100,
			String acc926, String aab054, String aab022, String acc90b,
			String acc90h, String acc901, String acc902, String acc423,
			String acc903, String acb214, String acc034, String acc22b,
			String acc905, String acc906, String aae011, String aae019,
			String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("acc100", acc100));
		pos_params.add(new BasicNameValuePair("acc926", acc926));
		pos_params.add(new BasicNameValuePair("aab054", aab054));
		pos_params.add(new BasicNameValuePair("aab022", aab022));
		pos_params.add(new BasicNameValuePair("acc90b", acc90b));
		pos_params.add(new BasicNameValuePair("acc90h", acc90h));
		pos_params.add(new BasicNameValuePair("acc901", acc901));
		pos_params.add(new BasicNameValuePair("acc902", acc902));
		pos_params.add(new BasicNameValuePair("acc423", acc423));
		pos_params.add(new BasicNameValuePair("acc903", acc903));
		pos_params.add(new BasicNameValuePair("acb214", acb214));
		pos_params.add(new BasicNameValuePair("acc034", acc034));
		pos_params.add(new BasicNameValuePair("acc22b", acc22b));
		pos_params.add(new BasicNameValuePair("acc905", acc905));
		pos_params.add(new BasicNameValuePair("acc906", acc906));
		pos_params.add(new BasicNameValuePair("aae011", aae011));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/addEmploymentStatus");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}

	/**
	 * 修改培训意愿 idcardno 身份证号码 acc907 是否参加过培训 acc908 技能特长 acc904 国家职业资格 acc919
	 * 培训意愿 aae011（经办人代码）、 aae019（经办人姓名）、 aae017（经办机构代码）、 aae020（经办机构名称）
	 * 
	 * @return
	 */
	public ReJson modifyTrainingAspiration(String idcardno, String acc907,
			String acc908, String acc904, String acc919, String aae011,
			String aae019, String aae017, String aae020) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("acc907", acc907));
		pos_params.add(new BasicNameValuePair("acc908", acc908));
		pos_params.add(new BasicNameValuePair("acc904", acc904));
		pos_params.add(new BasicNameValuePair("acc919", acc919));
		pos_params.add(new BasicNameValuePair("aae011", aae011));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/modifyTrainingAspiration");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);

	}

	/**
	 * x 修改资源减少 身份证号码 idcardno acc912 减少原因 aae013 备注说明 acc9a4（减少人代码）、
	 * acc914（减少人姓名）、 acc9a5（减少机构代码）、 acc915（减少机构名称）
	 * 
	 * @return
	 */
	public ReJson modifyResourceReduction(String idcardno, String acc912,
			String aae013, String acc9a4, String acc914, String acc9a5,
			String acc915) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("acc912", acc912));
		pos_params.add(new BasicNameValuePair("aae013", aae013));
		pos_params.add(new BasicNameValuePair("acc9a4", acc9a4));
		pos_params.add(new BasicNameValuePair("acc914", acc914));
		pos_params.add(new BasicNameValuePair("acc9a5", acc9a5));
		pos_params.add(new BasicNameValuePair("acc915", acc915));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/modifyResourceReduction");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}
/**
 * 修改求职意向
 * @param idcardno
 * @param aca112
 * @param acc901
 * @param acc034
 * @param aae011
 * @param aae019
 * @param aae017
 * @param aae020
 * @return
 */
	public ReJson modifyJobAspiration(String idcardno, String aca112,
			String acc901, String acc034, String aae011, String aae019,
			String aae017, String aae020 ) {
		List<NameValuePair> pos_params = new ArrayList<NameValuePair>();
		pos_params.add(new BasicNameValuePair("idcardno", idcardno));
		pos_params.add(new BasicNameValuePair("aca112", aca112));
		pos_params.add(new BasicNameValuePair("acc901", acc901));
		pos_params.add(new BasicNameValuePair("acc034", acc034));
		pos_params.add(new BasicNameValuePair("aae011", aae011));
		pos_params.add(new BasicNameValuePair("aae019", aae019));
		pos_params.add(new BasicNameValuePair("aae017", aae017));
		pos_params.add(new BasicNameValuePair("aae020", aae020));
		this.setUrl(this.getIpconfig()
				+ "android/rootscloud/modifyJobAspiration");
		String json = this.doPost(pos_params);
		return new ReJson(context, handler).setValue(json);
	}

}
