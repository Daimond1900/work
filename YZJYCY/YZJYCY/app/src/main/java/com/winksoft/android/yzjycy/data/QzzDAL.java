package com.winksoft.android.yzjycy.data;

import android.content.Context;
import android.util.Log;

import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.winksoft.android.yzjycy.util.FileUtils;
import com.winksoft.android.yzjycy.util.FormFile;
import com.yifeng.nox.android.http.*;
import com.yifeng.nox.android.http.http.AjaxCallBack;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 用户操作工具类
 *
 * @author wujiahong 2012-10-19
 */
public class QzzDAL extends com.yifeng.nox.android.http.BaseDAL {
    UserSession session;
    User user;

    public QzzDAL(Context context) {
        super(context);
        session = new UserSession(context);
        user = session.getUser();
    }

    /**
     * 查询简历
     *
     * @param userid
     * @param flag
     * @param callBack
     */
    public void queryResumeDetail(String userid, String flag, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userid);
        map.put("flag", flag);
        post(Constants.IP + "android/person/queryResumeDetail", callBack, map);
    }


    /**
     * 添加修改简历
     */
    public void postResume(Map<String, String> map, List<com.yifeng.nox.android.http.entity.FormFile> list, AjaxCallBack<?> callBack) {
        upload(Constants.IP + "android/person/modifyResume", callBack, map, list);
    }


    /***
     * 获取婚姻状况
     * @return
     */
    public List<Map<String, String>> getMarriages() {
        List<Map<String, String>> marriages = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "");
        map.put("value", "选择");
        marriages.add(map);

        map = new HashMap<String, String>();
        map.put("id", "未婚");
        map.put("value", "未婚");
        marriages.add(map);

        map = new HashMap<String, String>();
        map.put("id", "已婚");
        map.put("value", "已婚");
        marriages.add(map);
        return marriages;
    }

    /***
     * 获取政治面貌
     * @return
     */
    public List<Map<String, String>> getPolitics() {
        List<Map<String, String>> politics = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "");
        map.put("value", "选择");
        politics.add(map);

        map = new HashMap<String, String>();
        map.put("id", "党员");
        map.put("value", "党员");
        politics.add(map);

        map = new HashMap<String, String>();
        map.put("id", "团员");
        map.put("value", "团员");
        politics.add(map);

        map = new HashMap<String, String>();
        map.put("id", "无");
        map.put("value", "无");
        politics.add(map);

        return politics;

    }

    /***
     * 获取学历水平
     * @return
     */
    public List<Map<String, String>> getEducationals() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "");
        map.put("value", "选择");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "博士");
        map.put("value", "博士");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "硕士");
        map.put("value", "硕士");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "大学");
        map.put("value", "大学");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "大专");
        map.put("value", "大专");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "中专中技");
        map.put("value", "中专中技");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "技校");
        map.put("value", "技校");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "高中");
        map.put("value", "高中");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "职高");
        map.put("value", "职高");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "初中");
        map.put("value", "初中");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "小学");
        map.put("value", "小学");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "文盲或半文盲");
        map.put("value", "文盲或半文盲");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "不限");
        map.put("value", "不限");
        list.add(map);

        return list;
    }

    /***
     * 获取工作性质
     * @return
     */
    public List<Map<String, String>> getJobs() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "");
        map.put("value", "选择");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "全职");
        map.put("value", "全职");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "兼职");
        map.put("value", "兼职");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "实习");
        map.put("value", "实习");
        list.add(map);

        map = new HashMap<String, String>();
        map.put("id", "不限");
        map.put("value", "不限");
        list.add(map);

        return list;
    }

    /***
     * 获取区域
     * @return
     */
    public List<Map<String, String>> getAreas(boolean flag) {
        List<Map<String, String>> maps = DataConvert.toAreaList(user.getCities());
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (flag) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "");
            map.put("name", "选择");
            list.add(map);
        }
        if (maps.size() > 0) {
            for (int i = 0; i < maps.size(); i++) {
                Map<String, String> m = maps.get(i);
                list.add(m);
            }
        }
        return list;
    }

    /***
     * 获取统计数据
     */
    public void queryQzCount(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userid);
        post(Constants.IP + "android/sending/countSending", callBack, map);
    }

    /***
     * 录用通知
     */
    public void queryLytzList(String userId, int pageNum, String status, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("id", userId);
        map.put("state", status);
        post(Constants.IP + "android/sending/listSending", callBack, map);
    }

    /***
     * 取消投递
     */
    public void doRevokeResume(String sending_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sending_id", sending_id);
        post(Constants.IP + "android/sending/revokeSending", callBack, map);
    }



}
