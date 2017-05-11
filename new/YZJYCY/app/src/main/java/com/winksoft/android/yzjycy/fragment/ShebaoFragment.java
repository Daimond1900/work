package com.winksoft.android.yzjycy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.winksoft.android.yzjycy.CommonPageView;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.util.Constants;

public class ShebaoFragment extends Fragment implements OnClickListener {

    private View layout, layout_shebao_ylj, layout_shebao_yl, layout_shebao_sy;
    private boolean isBindInfo = false; //判断是否帐号绑定
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (layout != null) {
            // initLogin();
            // 防止多次new出片段对象，造成图片错乱问题
            return layout;
        }
        layout = inflater.inflate(R.layout.fragment_shebao, container, false);
        initView();
        return layout;
    }

    private void initView() {
        layout_shebao_ylj = layout.findViewById(R.id.layout_shebao_ylj);
        layout_shebao_yl = layout.findViewById(R.id.layout_shebao_yl);
        layout_shebao_sy = layout.findViewById(R.id.layout_shebao_sy);
        layout_shebao_ylj.setOnClickListener(this);
        layout_shebao_yl.setOnClickListener(this);
        layout_shebao_sy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), CommonPageView.class);;
        switch (v.getId()) {
            case R.id.layout_shebao_ylj:    //养老
                if(isBindInfo){
                    i.putExtra("url", Constants.IP + "");
                    i.putExtra("title", "养老保险查询");
                    startActivity(i);
                }else { // 跳转信息绑定页面

                }
                break;
            case R.id.layout_shebao_yl: // 医疗
                if(isBindInfo){
                    i.putExtra("url", Constants.IP + "");
                    i.putExtra("title", "医疗保险查询");
                    startActivity(i);
                }else {

                }
                break;
            case R.id.layout_shebao_sy: // 失业
                if(isBindInfo){
                    i.putExtra("url", Constants.IP + "");
                    i.putExtra("title", "失业保险查询");
                    startActivity(i);
                }else {

                }
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
