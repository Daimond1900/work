package com.winksoft.android.yzjycy.data;

import android.content.Context;

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
public class JcDAL extends com.yifeng.nox.android.http.BaseDAL {
    private User user;
    private UserSession userSession;

    public JcDAL(Context context) {
        super(context);
        userSession = new UserSession(context);
        user = userSession.getUser();
    }

    /**
     * 劳动力列表
     */
    public void doLdlQuery(int pageNum,String idcardno, String name, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", idcardno);
        map.put("aac003", name + "");
        map.put("page", pageNum + "");
        map.put("aae011", user.getUserId());//
        map.put("aae017", user.getDepartment_id());//deptId
        post(Constants.IP + "android/root/listLabourInfo", callBack, map);
    }
    /**
     * 基本信息
     */
    public void doJbxxQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", code);
        post(Constants.IP + "android/root/queryLabourInfoDetail", callBack, map);
    }


    /**
     * jyzt
     */
    public void doJyztQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", code);
        post(Constants.IP + "android/root/queryEmploymentStatus", callBack, map);
    }

    /**
     * pxyy
     */
    public void doPzyyQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", code);
        post(Constants.IP + "android/root/queryTrainingAspiration", callBack, map);
    }

    /**
     * zyjs
     */
    public void doZyjsQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", code);
        post(Constants.IP + "android/root/queryResourceReduction", callBack, map);
    }
    /**
     * qzyy
     */
    public void doQzyyQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac002", code);
        post(Constants.IP + "android/root/queryJobAspiration", callBack, map);
    }
    /**
     * 零转移列表
     */
    public void doLzyQuery(int page,String cun, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page+"");
        map.put("aae017", user.getDepartment_id());//deptId
        map.put("hzsfz", cun);
        map.put("aae011", user.getUserId());//
        post(Constants.IP + "android/root/listZeroTransfer", callBack, map);
    }

    /**
     * 零转移详情
     */
    public void doLzyDetailQuery(String code, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("hzsfz", code);
        post(Constants.IP + "android/root/queryZeroTransferDetail", callBack, map);
    }

}
