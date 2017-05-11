package com.winksoft.android.yzjycy.fragment;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.activity.LoginActivity;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.ui.bminfo.Zpt_RegisterCountListActivity;
import com.winksoft.android.yzjycy.ui.enterprise.ZptEnterpriseActivity;
import com.winksoft.android.yzjycy.ui.hireQuery.Zpt_EmployCountListActivity;
import com.winksoft.android.yzjycy.ui.job.QztInvitedActivity;
import com.winksoft.android.yzjycy.ui.job.QztMyJobActivity;
import com.winksoft.android.yzjycy.ui.resume.QztPersonalResumeActivity;
import com.winksoft.android.yzjycy.ui.system.AccountActivity;
import com.winksoft.android.yzjycy.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineFragment extends Fragment implements OnClickListener {

	private View mViewNotLogined;
	private View mViewLogined;
	private View layout;
	private ImageView imageView;
	private RelativeLayout layout_not_logined, layout_logined;
	private TextView tv_name, tv_phone;
	private View layoutCompany, layoutPersonal, layout_mine_xgmm;

	private Button loginBtn, exitBtn;
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (layout != null) {
			// initLogin();
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_mine, container, false);
		initView();
		return layout;
	}

	private void onLoadUser() {
		if (Constants.iflogin) {
			user = new UserSession(getContext()).getUser();
			tv_phone.setText(user.getLoginName());
			exitBtn.setVisibility(View.VISIBLE);
			mViewLogined.setVisibility(View.VISIBLE);
			mViewNotLogined.setVisibility(View.GONE);
			layout_mine_xgmm.setVisibility(View.VISIBLE);

			if (user.getRoleid().equals("4028c2e948ba2b750148ba2e76960000")) {
				tv_name.setText(user.getRealName());
				layoutCompany.setVisibility(View.GONE);
				layoutPersonal.setVisibility(View.VISIBLE);
			} else if (user.getRoleid().equals("4028c2e941fd35f60141fdac66280006")) {
				tv_name.setText(user.getZbdw());
				layoutCompany.setVisibility(View.VISIBLE);
				layoutPersonal.setVisibility(View.GONE);
			}
		} else {
			tv_name.setText("");
			tv_phone.setText("");

			exitBtn.setVisibility(View.GONE);

			mViewLogined.setVisibility(View.GONE);
			mViewNotLogined.setVisibility(View.VISIBLE);

			layoutCompany.setVisibility(View.GONE);
			layoutPersonal.setVisibility(View.GONE);
			layout_mine_xgmm.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		onLoadUser();
	}

	private void initView() {
		loginBtn = (Button) layout.findViewById(R.id.personal_login_button);
		loginBtn.setOnClickListener(this);
		exitBtn = (Button) layout.findViewById(R.id.btn_exit);
		exitBtn.setOnClickListener(this);

		mViewNotLogined = layout.findViewById(R.id.layout_not_logined);
		mViewLogined = layout.findViewById(R.id.layout_logined);

		layoutPersonal = layout.findViewById(R.id.layoutPersonal);
		layoutCompany = layout.findViewById(R.id.layoutCompany);

		tv_name = (TextView) layout.findViewById(R.id.tv_name);
		tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
		imageView = (ImageView) layout.findViewById(R.id.user_icon);
		imageView.setOnClickListener(this);
		layout_mine_xgmm = layout.findViewById(R.id.layout_mine_xgmm);
		layout_mine_xgmm.setOnClickListener(this);

		layout.findViewById(R.id.layout_mine_update).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_clear).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_help).setOnClickListener(this);

		// 初始化个人
		layout.findViewById(R.id.layout_mine_wdjl).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_wdqz).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_mstz).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_wdpx).setOnClickListener(this);

		// 初始化企业
		layout.findViewById(R.id.layout_mine_qyxx).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_bmgl).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_lygl).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent1 = null;
		switch (v.getId()) {
		case R.id.btn_exit:
			Constants.iflogin = false;
			onLoadUser();
			break;
		case R.id.user_icon: // 点击用户头像

			break;
		case R.id.layout_mine_qyxx: // 企业信息
			intent1 = new Intent(getContext(), ZptEnterpriseActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_mine_bmgl: // 报名信息
			intent1 = new Intent(getContext(), Zpt_RegisterCountListActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_mine_lygl: // 录用管理
			intent1 = new Intent(getContext(), Zpt_EmployCountListActivity.class);
			startActivity(intent1);
			break;

		case R.id.layout_mine_wdjl: // 我的简历
			intent1 = new Intent(getContext(), QztPersonalResumeActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_mine_wdqz: // 我的求职
			intent1 = new Intent(getContext(), QztMyJobActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_mine_mstz: // 面试邀请
			intent1 = new Intent(getContext(), QztInvitedActivity.class);
			intent1.putExtra("title", "面试邀请函");
			startActivity(intent1);
			break;
		case R.id.layout_mine_xgmm: // 修改密码
			intent1 = new Intent(getContext(), AccountActivity.class);
			intent1.putExtra("title", "面试邀请函");
			startActivity(intent1);
			break;

		case R.id.personal_login_button: // 登录
			startActivity(new Intent(getContext(), LoginActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

}
