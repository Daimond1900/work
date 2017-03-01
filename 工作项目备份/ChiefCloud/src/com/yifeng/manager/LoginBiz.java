package com.yifeng.manager;
import com.yifeng.data.UserDAL;
import com.yifeng.entity.User;
import com.yifeng.util.ConstantUtil;
import com.yifeng.util.StringHelper;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 用户登陆逻辑类
 * @author 吴家宏
 * 
 */
public class LoginBiz extends BaseBiz{
	public static final String USERINFO = "USERINFO";
	public LoginBiz(Context context){
		super(context);
	}
	/**
	 * 登陆方法
	 * 参数User -1服务器异常，0找不到，1加载成功,2数据解析异常
	 * @return
	 */
	public int doLogin(User us){
		
		//登陆执行业务逻辑成功执行下面方法
		 UserDAL dal=new UserDAL(this.getContext());
		 User user=dal.doLogin(us);
		 int ret=0;
		 if(user!=null){
			 if(user.getState()==ConstantUtil.LOGIN_SUCCESS){
				 SharedPreferences settings = this.getContext().getSharedPreferences(USERINFO, 0);
			     settings.edit()
			     .putString("userId",user.getUserId())
				 .putString("userName",user.getUserName())
				 .putString("userPwd", user.getUserPwd())
				 .putString("department",user.getDepartment())
				 .putString("roleid", user.getRoleId())
				 .putString("org_id", user.getOrg_id())
				 .putString("imsi", user.getImsi())
				 .putString("role_type", user.getRole_type())
				 .putString("job", user.getJob())
				 .putString("key", user.getKey())
				 .putString("mobile_no", user.getMobile_no())
			     .commit(); 
		     }
			 ret=user.getState();
		 }
		 
	      
		return ret;
	}
	/**
	 *修改密码
	 * 参数User -1服务器异常，0找不到，1加载成功,2数据解析异常
	 * @return
	 */
	public int editPwd(String userid,String old_pwd,String new_pwd){
		
		//登陆执行业务逻辑成功执行下面方法
		 UserDAL dal=new UserDAL(this.getContext());
		 int ret=dal.doUpdatePwd(userid, StringHelper.handlePwd(old_pwd) ,
					StringHelper.handlePwd(new_pwd));
		 if(ret==1){
				 SharedPreferences settings = this.getContext().getSharedPreferences("PWD", 0);
			     settings.edit()
				 
				 .putString("loginPwd", new_pwd)
			     .commit(); 
		     }
		return ret;
	}
	
	/**
	 * 获取用户登陆信息
	 * @return
	 */
	public static User loadUser(Context context){
		User user =new User();
		SharedPreferences userinfo =context.getSharedPreferences(USERINFO, 0);
	    user.setUserId(userinfo.getString("userId", ""));
	    user.setUserName(userinfo.getString("userName", ""));
	    user.setUserPwd(userinfo.getString("userPwd", ""));
	    user.setDepartment(userinfo.getString("department", ""));
	    user.setRoleId(userinfo.getString("roleid", ""));
	    user.setOrg_id(userinfo.getString("org_id", ""));
	    user.setImsi(userinfo.getString("imsi", ""));
	    user.setJob(userinfo.getString("job", ""));
	    user.setRole_type(userinfo.getString("role_type", ""));
	    user.setKey(userinfo.getString("key", ""));
	    user.setMobile_no(userinfo.getString("mobile_no", ""));
		return user;
	}

}
