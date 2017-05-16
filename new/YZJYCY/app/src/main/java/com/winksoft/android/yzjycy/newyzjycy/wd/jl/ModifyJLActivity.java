package com.winksoft.android.yzjycy.newyzjycy.wd.jl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;

/**
 * 修改简历 主界面
 * Created by 1900 on 2017/4/18.
 */
public class ModifyJLActivity extends BaseActivity implements View.OnClickListener {
    private int pdBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyjl_activity);
        initView();
    }

    private void initView() {
        pdBt = getIntent().getIntExtra("pdbt", 0); /*判断标题 没有取到值为0 0：新增*/

        TextView xg_title = (TextView) findViewById(R.id.xg_title);

        if (pdBt == 1) {
            xg_title.setText("修改简历");
        } else {
            xg_title.setText("新增简历");
        }
        View layout_grjcxx = findViewById(R.id.layout_grjcxx);
        View layout_jyjl = findViewById(R.id.layout_jyjl);
        View layout_pxjl = findViewById(R.id.layout_pxjl);
        View layout_yynl = findViewById(R.id.layout_yynl);
        View layout_jszc = findViewById(R.id.layout_jszc);
        View layout_gzjl = findViewById(R.id.layout_gzjl);
        View layout_qzyx = findViewById(R.id.layout_qzyx);
        layout_grjcxx.setOnClickListener(this);
        layout_jyjl.setOnClickListener(this);
        layout_pxjl.setOnClickListener(this);
        layout_yynl.setOnClickListener(this);
        layout_jszc.setOnClickListener(this);
        layout_gzjl.setOnClickListener(this);
        layout_qzyx.setOnClickListener(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.layout_grjcxx:    // 基本信息
                intent = new Intent(this, ModifyPersonJcInfo.class);
                intent.putExtra("pdbt", pdBt);
                startActivity(intent);
                break;
            case R.id.layout_jyjl:    // 教育经历
                intent = new Intent(this, ModifyJyjlInfo.class);
                intent.putExtra("pdbt", pdBt);
                startActivity(intent);
                break;
            case R.id.layout_pxjl:  // 培训经历
                intent = new Intent(this, ModifyPxjlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_yynl:  // 语言能力
                intent = new Intent(this, ModifyYynlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_jszc:  // 技术专长
                intent = new Intent(this, ModifyJszcInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_gzjl:  // 工作经历
                intent = new Intent(this, ModifyGzjlInfo.class);
                startActivity(intent);
                break;
            case R.id.layout_qzyx:  // 求职意向
                intent = new Intent(this, ModifyQzyxInfo.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
