package com.winksoft.android.yzjycy.activity;

import java.util.Map;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.LoginDal;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.yifeng.nox.android.afinal.annotation.view.SetView;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;
import com.yifeng.nox.android.ui.BaseLogin;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 用户登录
 */
public class LoginActivity extends BaseLogin {
	protected static final String TAG = "LoginActivity";
	@SetView(id=R.id.topTitle) TextView topTitle;
	@SetView(id = R.id.backBtn, click = "onViewClick") Button backBtn;
	@SetView(id = R.id.tv_quick_sign_up, click = "onViewClick") TextView sign_up ;
	@SetView(id = R.id.registerBtn, click = "onViewClick") Button registerBtn ;
	private LoginDal loginDal;
	Dialog proDialog;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		topTitle.setText("用户登录");
		loginDal = new LoginDal(this);
		user = new User();

		// 参数说明:
		// loginNameId 用户名控件ID必须
		// loginPwdId 密码控件ID 必须
		// loginBtnId 登录按钮ID 必须
		// quitBtnId 退出按钮Id 可无0代表没有
		// loginMessageId 登录进度文本ID 可无0代表没有
		// keepPasswordId 记住密码check控件ID 可无 0代表没
		this.initView(R.id.loginName, R.id.loginPwd, R.id.loginBtn,
				0, R.id.loginMsg, R.id.rememberPwd);
	}
	
	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
		switch (v.getId()) {
		case R.id.backBtn:
			LoginActivity.this.finish();
			break;
		case R.id.tv_quick_sign_up:
			
			break;
		case R.id.registerBtn:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onLogin() {

		user.setLoginName(this.getName());
		user.setPassWord(this.getPwd());

		boolean flag = this.checkLogin(true);

		if (flag) {
			AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(context,
					false) {

				@Override
				public void onStart() {
					super.onStart();
					// setLoadingMsg("正在登录中,请稍后...");
					proDialog = CustomeProgressDialog.createLoadingDialog(
							LoginActivity.this, "正在登录中,请稍后...");
					proDialog.show();
				}

				@Override
				public void onSuccess(Object arg0) {
					super.onSuccess(arg0);
					loginResult((String) arg0);

					if (proDialog != null)
						proDialog.dismiss();
				}

				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					showDoigMsg("系统提示", "连接服务器失败！请检查网络是否连接正常");
					// setLoadingMsg("");
					if (proDialog != null)
						proDialog.dismiss();
				}
			};
			loginDal.login(this.getName(), this.getPwd(), callBack);
		}
	}

	private void loginResult(String json) {
		Log.d(TAG, "登录结果的: json == " + json);
		Map<String, String> map = DataConvert.toMap(json);
		if (map != null) {
			if (map.get("success").equals("false")) {
				showToast(map.get("msg"), false);
			} else if (map.get("success").equals("true")) {
				Map<String, String> map1 = DataConvert.toConvertStringMap(json,
						"user");
				if (map1 != null) {
					// 记住密码
					this.remeberPwd(this.getName(), this.getPwd());
 					User user = new User();
 					/*解析数据，存入User*/
 					user.setLoginName(this.getName());				// 用户名	
 					user.setPassWord(this.getPwd());
 					user.setLogintype(getMapValue(map1,"logintype"));//密码
 					user.setIsuse(getMapValue(map1,"isuse"));		//禁用状态
 					user.setUserId(getMapValue(map1,"userid"));		
 					user.setRealName(getMapValue(map1,"realname"));	//真实姓名
 					user.setSex(getMapValue(map1,"AAC004"));
 					user.setPhone(getMapValue(map1,"acb501"));		// 手机号
					user.setCsrq(getMapValue(map1,"aac006"));		// 出生日期
 					user.setIs_open_phone(getMapValue(map1,"is_open_phone"));
 					user.setAddress(getMapValue(map1,"address"));
 					user.setWeixinNo(getMapValue(map1,"weixinno"));
 					user.setIs_open_weixin(getMapValue(map1,"is_open_weixin"));
 					user.setIs_open_weibono(getMapValue(map1,"is_open_weibono"));
 					user.setEmail(getMapValue(map1,"email"));
 					user.setIs_open_mail(getMapValue(map1,"is_open_mail"));
 					user.setIdCard(getMapValue(map1,"idcard"));
 					user.setUserType(getMapValue(map1,"usertype"));
 					user.setSpecialty(getMapValue(map1,"specialty"));
 					user.setEducation(getMapValue(map1,"education"));
 					user.setResume(getMapValue(map1,"resume"));
 					user.setQqNo(getMapValue(map1,"qqNo"));
 					user.setIs_open_qq(getMapValue(map1,"is_open_qq"));
 					user.setNkName(getMapValue(map1,"nkname"));
 					user.setRegTime(getMapValue(map1,"regtime"));
 					user.setDepartment_id(getMapValue(map1,"department_id"));
 					user.setArea_id(getMapValue(map1,"area_id"));
 					user.setOrgid(getMapValue(map1,"orgid"));
 					user.setRoleid(getMapValue(map1,"roleid"));
 					user.setDepartment_name(getMapValue(map1,"department_name"));
 					user.setCities(map.get("cities")!=null ? map.get("cities") : "");	// cities 层
 					user.setZbdw(getMapValue(map1,"zbdw"));
 					user.setAAE004(getMapValue(map1,"aae004"));
 					user.setAAE005(getMapValue(map1,"aae005"));
 					user.setAAE006(getMapValue(map1,"aae006"));
 					user.setLNG(getMapValue(map1,"lng"));
 					user.setLAT(getMapValue(map1,"lat"));
					// 登录状态
					Constants.iflogin = true;
					// 临时保存用户信息到本地
					UserSession session = new UserSession(context);
					session.setUser(user);
					this.finish();
					
				} else {
					showDoigMsg("系统提示", "登录失败,请重试或联系后台管理员!");
				}
			}
		} else {
			showDoigMsg("系统提示", "登录失败,请重试或联系后台管理员!");
		}

	}
	
	private String getMapValue(Map<String,String> map,String key){
		return map.get(key) == null ? "": map.get(key);
	}

	@Override
	public void onCancel() {
		
	}
}
