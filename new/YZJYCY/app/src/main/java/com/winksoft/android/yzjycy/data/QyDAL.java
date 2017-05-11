package com.winksoft.android.yzjycy.data;

import android.content.Context;

import com.winksoft.android.yzjycy.AppContext;
import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
import com.yifeng.nox.android.http.entity.FormFile;
import com.yifeng.nox.android.http.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 用户操作工具类
 *
 * @author wujiahong 2012-10-19
 */
public class QyDAL extends com.yifeng.nox.android.http.BaseDAL {
    private User user;
    private UserSession userSession;

    public QyDAL(Context context) {
        super(context);
        userSession = new UserSession(context);
        user = userSession.getUser();
    }

    /**
     * 企业信息
     */
    public void doPostQuery(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aab001", userid);
        post(Constants.IP + "android/corecruitment/queryCompanyDetail", callBack, map);
    }

    /**
     * 修改企业信息
     */
    public void doEditInfo(Map<String, String> map, List<FormFile> list, AjaxCallBack<?> callBack) {
        upload(Constants.IP + "android/corecruitment/modifyCompanyInfo", callBack, map, list);
    }

    /**
     * 企业招聘岗位列表
     */
    public void doZwgwQuery(Map<String, String> map, AjaxCallBack<?> callBack) {
        post(Constants.IP + "android/system/listJob", callBack, map);
    }

    /**
     * 招聘发布
     */
    public void doZpF(Map<String, String> map, AjaxCallBack<?> callBack) {
        post(Constants.IP + "android/corecruitment/createRecruitInfo", callBack, map);
    }

    /**
     * 招聘发布 修改
     */
    public void doZpFXg(Map<String, String> map, AjaxCallBack<?> callBack) {
        post(Constants.IP + "android/corecruitment/modifyRecruitInfo", callBack, map);
    }


    /**
     * 获取区域
     *
     * @param flag
     * @param title
     * @return
     */
    public List<Map<String, String>> getAreas(boolean flag, String title) {
        List<Map<String, String>> maps = DataConvert.toArrayList(user.getCities());
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (flag) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "");
            map.put("name", title);
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

    /**
     * 招聘管理
     *
     * @return
     */
    public void doZpglQuery(int pageNum, String p1, String p3, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("acb216", p1);
        map.put("aab001", user.getUserId());
        map.put("acb208", p3);
        post(Constants.IP + "android/corecruitment/listRecruitInfo", callBack, map);
    }

    /**
     * 报名信息查询
     *
     * @return
     */
    public void doBmxxQuery(int pageNum, String flag, String zwId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("acb210", zwId);
        map.put("state", flag);
        map.put("aab001", user.getUserId());
        post(Constants.IP + "android/sending/groupSendingByJob", callBack, map);
    }

    /**
     * 报名信息查询
     *
     * @return
     */
    public void doLyxxQuery(int pageNum, String flag, String zwId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("acb210", zwId);
        map.put("state", flag);
        map.put("aab001", user.getUserId());
        post(Constants.IP + "android/sending/listSendingByJob", callBack, map);
    }



    /**
     * 报名信息查询
     *
     * @return
     */
    public void doGwBmxxQuery(int pageNum, String flag, String zwId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("acb210", zwId);
        map.put("state", flag);
        map.put("aab001", user.getUserId());
        post(Constants.IP + "android/sending/listSendingByJob", callBack, map);
    }


    /**
     * 拒绝
     *
     * @param url
     * @return
     */
    public void doUpRight(Map<String, String> rightParams, String url, AjaxCallBack<?> callBack) {
        post(Constants.IP + url, callBack, rightParams);
    }

    /**
     * 同意
     */
    public void doUpLeft(Map<String, String> LeftParams, String url, AjaxCallBack<?> callBack) {
        post(Constants.IP + url, callBack, LeftParams);
    }
}
