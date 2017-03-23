package com.winksoft.android.yzjycy.newentity;

/*需修改*/
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户信息
 * 
 */
public class UserSession {
	private Context context;
	public static final String USERINFO = "USERINFO";

	public UserSession(Context context) {
		this.context = context;
	}
	public void setMaps(String companyName,String companyAddress,String telNo){
		SharedPreferences settings = this.context.getSharedPreferences("maps", 0);
		settings.edit().putString("companyName",companyName)
		.putString("companyAddress", companyAddress).putString("telNo", telNo).commit();
	}
	
	
	public String[] getMaps(){
		SharedPreferences settings = this.context.getSharedPreferences("maps", 0);
		return new String[]{settings.getString("companyName", ""),
				settings.getString("companyAddress", "")
				,settings.getString("telNo", "")};
	}
	/**
	 * 昨时存用户
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		SharedPreferences settings = this.context.getSharedPreferences(USERINFO, 0);
		settings.edit()
				// .putString("isuse", user.get())
				.putString("logintype", user.getLogintype())
				.putString("isuse", user.getIsuse())
				.putString("loginname", user.getLoginName())
				.putString("password", user.getPassWord())
				.putString("userid", user.getUserId())  //
				.putString("realname", user.getRealName())//
				.putString("sex", user.getSex())
				.putString("phone", user.getPhone())
				.putString("is_open_phone", user.getIs_open_phone())
				.putString("address", user.getAddress())
				.putString("weixinno", user.getWeixinNo())
				.putString("is_open_weixin", user.getIs_open_weixin())
				.putString("is_open_weibono", user.getIs_open_weibono())
				.putString("email", user.getEmail())
				.putString("is_open_mail", user.getIs_open_mail())
				.putString("idcard", user.getIdCard())
				.putString("pic", user.getPic())
				.putString("usertype", user.getUserType())
				.putString("specialty", user.getSpecialty())
				.putString("education", user.getEducation())
				.putString("resume", user.getResume())
				.putString("qqNo", user.getQqNo())
				.putString("is_open_qq", user.getIs_open_qq())
				.putString("nkname", user.getNkName())
				.putString("regtime", user.getRegTime())
				.putString("department_id", user.getDepartment_id())
				.putString("area_id", user.getArea_id())
				.putString("orgid", user.getOrgid())
				.putString("roleid", user.getRoleid())
				.putString("department_name", user.getDepartment_name())
				.putString("cities", user.getCities())
				.putString("zbdw", user.getZbdw())
				.putString("aae004", user.getAAE004())
				.putString("aae005", user.getAAE005())
				.putString("aae006", user.getAAE006())
				.putString("lng", user.getLNG())
				.putString("lat", user.getLAT())
//				.putString("isJl", user.getIsJl())	//用于判断是否有简历的
				.commit();
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public User getUser() {
		User user = new User();
		SharedPreferences userinfo = context.getSharedPreferences(USERINFO, 0);
		// user.setKey(userinfo.getString("key", ""));
		user.setLogintype(userinfo.getString("logintype", ""));
		user.setIsuse(userinfo.getString("isuse", ""));
		user.setLoginName(userinfo.getString("loginname", ""));
		user.setPassWord(userinfo.getString("password", ""));
		user.setUserId(userinfo.getString("userid", ""));
		user.setRealName(userinfo.getString("realname", ""));
		user.setSex(userinfo.getString("sex", ""));
		user.setPhone(userinfo.getString("phone", ""));
		user.setIs_open_phone(userinfo.getString("is_open_phone", ""));
		user.setAddress(userinfo.getString("address", ""));
		user.setWeixinNo(userinfo.getString("weixinno", ""));
		user.setIs_open_weixin(userinfo.getString("is_open_weixin", ""));
		user.setIs_open_weibono(userinfo.getString("is_open_weibono", ""));
		user.setEmail(userinfo.getString("email", ""));
		user.setIs_open_mail(userinfo.getString("is_open_mail", ""));
		user.setIdCard(userinfo.getString("idcard", ""));
		user.setUserType(userinfo.getString("usertype", ""));
		user.setSpecialty(userinfo.getString("specialty", ""));
		user.setEducation(userinfo.getString("education", ""));
		user.setResume(userinfo.getString("resume", ""));
		user.setQqNo(userinfo.getString("qqNo", ""));
		user.setIs_open_qq(userinfo.getString("is_open_qq", ""));
		user.setNkName(userinfo.getString("nkname", ""));
		user.setRegTime(userinfo.getString("regtime", ""));
		user.setDepartment_id(userinfo.getString("department_id", ""));
		user.setArea_id(userinfo.getString("area_id", ""));
		user.setOrgid(userinfo.getString("orgid", ""));
		user.setRoleid(userinfo.getString("roleid", ""));
		user.setDepartment_name(userinfo.getString("department_name", ""));
		user.setCities(userinfo.getString("cities", ""));
		user.setZbdw(userinfo.getString("zbdw", ""));
		user.setAAE004(userinfo.getString("aae004", ""));
		user.setAAE005(userinfo.getString("aae005", ""));
		user.setAAE006(userinfo.getString("aae006", ""));
		user.setLNG(userinfo.getString("lng", ""));
		user.setLAT(userinfo.getString("lat", ""));
//		user.setIsJl(userinfo.getString("isJl", ""));	//判断是否有简历
		return user;
	}
}
