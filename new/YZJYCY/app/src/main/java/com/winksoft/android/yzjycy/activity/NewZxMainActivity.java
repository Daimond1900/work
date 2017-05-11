package com.winksoft.android.yzjycy.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.lynnchurch.horizontalscrollmenu.BaseAdapter;
import com.winksoft.lynnchurch.horizontalscrollmenu.HorizontalScrollMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewZxMainActivity extends BaseActivity {
    private HorizontalScrollMenu hsm_container;
    private List<Map<String, Object>> SURPERDATA;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        initData();
        initView();
    }

    private void initData() {
        SURPERDATA = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);

        map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);

        map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);

        map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);

        map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);
        map = new HashMap<>();
        map.put("t_title", "韩媒又炒“限韩令”:北京电影节难展映韩国电影");
        map.put("t_from", "扬州发布");
        map.put("t_date", "2017-09-21 12:02:07");
        SURPERDATA.add(map);

    }

    public void initView() {
        hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
        hsm_container.setSwiped(false);
        menuAdapter = new MenuAdapter();
        menuAdapter.setIndex(this.getIntent().getIntExtra("index",0));
        hsm_container.setAdapter(menuAdapter);
    }

    class MenuAdapter extends BaseAdapter {
        private int index;
        String[] names = new String[]
                {"就业新闻", "创业新闻", "市场公告", "政策法规", "工资指导价"};

        public void setIndex(int index) {
            this.index = index;
            if(index == 0){
                return;
            }
            changeNames();
        }

        private void changeNames() {
            switch (index) {
                case 1:
                    names = new String[]
                            {"创业新闻", "就业新闻", "市场公告", "政策法规", "工资指导价"};
                    break;
                case 2:
                    names = new String[]
                            {"市场公告", "就业新闻", "创业新闻", "政策法规", "工资指导价"};
                    break;
                case 3:
                    names = new String[]
                            {"政策法规", "就业新闻", "创业新闻", "市场公告", "工资指导价"};
                    break;
                case 4:
                    names = new String[]
                            {"工资指导价", "就业新闻", "创业新闻", "市场公告", "政策法规"};
                    break;
                default:
                    break;
            }
        }


        @Override
        public List<String> getMenuItems() {
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (String str : names) {
                View v = LayoutInflater.from(NewZxMainActivity.this).inflate(
                        R.layout.new_content_view, null);
                ListView listView = (ListView) v.findViewById(R.id.listview);
                SimpleAdapter adapter = new SimpleAdapter(NewZxMainActivity.this, SURPERDATA, R.layout.qzt_training_list_item, new String[]{"t_title",
                        "t_from", "t_date"}, new int[]{
                        R.id.training_title, R.id.training_from,
                        R.id.training_date});
                listView.setAdapter(adapter);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {
//			// TODO Auto-generated method stub
//			Toast.makeText(NewZxMainActivity.this,
//					"内容页：" + (position + 1) + " 访问状态：" + visitStatus,
//					Toast.LENGTH_SHORT).show();
        }

    }
}
