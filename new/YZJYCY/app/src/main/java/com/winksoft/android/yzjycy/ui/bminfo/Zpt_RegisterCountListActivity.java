package com.winksoft.android.yzjycy.ui.bminfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.winksoft.android.yzjycy.CustomeProgressDialog;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.adapter.CommonAdapter;
import com.winksoft.android.yzjycy.data.QyDAL;
import com.winksoft.android.yzjycy.data.RegisterDAL;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.ui.zcfg.QztZCFGListActivity;
import com.winksoft.android.yzjycy.util.CommonUtil;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.ListViewUtil;
import com.winksoft.nox.android.ui.BaseListActivity;
import com.winksoft.nox.android.view.YFBaseAdapter;
import com.yifeng.nox.android.http.http.AjaxCallBack;
import com.yifeng.nox.android.json.DataConvert;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * ClassName:RegisterCountListActivity Description：报名信息-统计页面
 * @author Administrator Date：2012-10-19
 */
public class Zpt_RegisterCountListActivity extends BaseListActivity {
	private Button backBtn;
	private YFBaseAdapter adapter;
	private QyDAL qyDAL;
    private Dialog proDialog;
    private CommonUtil commonUtil;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zpt_register_count_list);
        qyDAL = new QyDAL(this);
        commonUtil = new CommonUtil(this);
		MyClick myClick = new MyClick();
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(myClick);
        intiListview(false,true);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String postId = SURPERDATA.get(arg2-1).get("acb210").toString();
					Intent intent = new Intent(Zpt_RegisterCountListActivity.this, Zpt_RegisterPostListActivity.class);
					intent.putExtra("post_id", postId);// 岗位编号
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    @Override
    protected BaseAdapter setAdapter() {
        adapter = new YFBaseAdapter(this, SURPERDATA, R.layout.zpt_register_count_list_item,
                new String[] { "itemPost", "itemNumber", }, new int[] { R.id.item_postTxt, R.id.item_numberTxt },
                getResources()) {
            @Override
            protected void iniview(View view, int position, List<? extends Map<String, ?>> data) {

            }
        };
        return adapter;
    }

    @Override
    protected void loadDate() {
        AjaxCallBack<Object> callBack = new AjaxCallBack<Object>(this, false) {
            @Override
            public void onStart() {
                super.onStart();
                onListviewStart();
                proDialog = CustomeProgressDialog.createLoadingDialog(
                        Zpt_RegisterCountListActivity.this, "加载中,请稍后...");
                proDialog.show();
            }

            @Override
            public void onSuccess(Object arg0) {
                super.onSuccess(arg0);
                postResult((String) arg0);
                if (proDialog != null)
                    proDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                if (proDialog != null)
                    proDialog.dismiss();
                onListviewonFailure();
            }
        };
        qyDAL.doBmxxQuery(SUPERPAGENUM,"","", callBack);
    }
    /**
     * @param json
     */
    private void postResult(String json) {
        Map<String, String> map = DataConvert.toMap(json);
        if (map != null) {
            if (("true").equals(map.get("success"))) {
                STRINGLIST = DataConvert.toConvertStringList(json, "jobs");
                onListviewSuccess();
                return;
            } else {
                onListviewNoResult();
                commonUtil.shortToast(commonUtil.getMapValue(map, "msg"));
            }
        } else {
            onListviewNoResult();
            commonUtil.shortToast("系统繁忙，请稍后再试!");
        }
    }
    @Override
    protected void formatData() {
        for (Map<String, String> tm : STRINGLIST) {
            Map<String, Object> otm = new HashMap<String, Object>();
            tm.put("itemPost", tm.get("acb216"));// 岗位名称
            tm.put("itemNumber", tm.get("num"));// 人数
            for (String ts : tm.keySet()) {
                otm.put(ts, tm.get(ts));
            }
            SURPERDATA.add(otm);
        }
    }
    @Override
    protected void myNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backBtn:
				Zpt_RegisterCountListActivity.this.finish();
				break;
			default:
				break;
			}
		}

	}
}
