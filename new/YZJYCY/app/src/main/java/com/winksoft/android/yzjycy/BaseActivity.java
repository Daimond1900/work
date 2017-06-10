package com.winksoft.android.yzjycy;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.ActivityStackControlUtil;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.DialogUtil;

/***
 * 所有管理类
 *
 * @author wujiahong 2012-10-11
 */
public class BaseActivity extends AppCompatActivity {
    public CommonUtil commonUtil;
    public Resources res;
    public Handler BASEHANDLER;
    public User user;
    public DialogUtil dialogUtil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 全屏显示 使应用程序全屏运行，不使用title bar **/
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        commonUtil = new CommonUtil(this);
        dialogUtil = new DialogUtil(this);
        UserSession session = new UserSession(this);
        user = session.getUser();

        ActivityStackControlUtil.add(this);
        res = getResources();
        if (!commonUtil.checkNetWork()) {
            /*dialogUtil.shortToast("请设置网络连接!");
			Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			startActivity(intent);*/
            dialogUtil.alertNetError();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (BASEHANDLER != null) BASEHANDLER.sendMessage(BASEHANDLER.obtainMessage());
            else {
                System.gc();  //提醒系统及时回收

                this.finish();
                ActivityStackControlUtil.remove(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
////		MenuInflater inflater = getMenuInflater();
////		inflater.inflate(R.menu.jcy_main, menu);
////		return true;
//	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.recomd:
//			Uri smsToUri = Uri.parse("smsto:");
//			Intent mIntent = new Intent(Intent.ACTION_SENDTO,
//					smsToUri);
//			mIntent.putExtra("sms_body", getString(R.string.app_name) + "下载地址:"
//					+ Constants.downapk);
//			startActivity(mIntent);
//			break;
////		case R.id.editPwd:
////			Intent intent = new Intent(this,MyPassword.class);
////			startActivity(intent);
////			break;
//		case R.id.exit:
//			this.commonUtil.exit();
//			break;
//		}
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackControlUtil.remove(this);
    }
}
