package com.winksoft.android.yzjycy.data;

import android.content.Context;
import android.util.Log;

import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
import com.yifeng.nox.android.http.http.AjaxCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 用户操作工具类
 *
 * @author wujiahong 2012-10-19
 */
public class JyDAL extends com.yifeng.nox.android.http.BaseDAL {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private static final String TAG = "XwzxDAL";
    private User user;
    private UserSession userSession;

    public JyDAL(Context context) {
        super(context);
        userSession = new UserSession(context);
        user = userSession.getUser();
    }


    /**
     * 招聘信息列表
     * ACA112	岗位名称
     * ACA111	职业（工种）
     * ACB239	工作性质
     * AAB001	单位编号
     */
    public void queryZpxx(String gwmc, String zygz, String gzsx, String dwbh, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aca112", gwmc);
        map.put("aca111", zygz);
        map.put("acb239", gzsx);
        map.put("aab001", dwbh);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/listDataCb21", callBack, map);
    }

    /**
     * 新闻列表 刷新展示的数据
     */
    public void queryXwRef(AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        post(Constants.IP + "android/news/listIndex", callBack, SignHashMap);
    }

    /**
     * 首页招聘会列表
     */
    public void queryZph(String pagesize, String aab301, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pagesize", pagesize);
        map.put("aab301", aab301);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "/android/cb33/listData", callBack, map);
    }

    /**
     * 按照资讯类型查询 资讯列表
     */
    public void queryZxByTypeId(String typeid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("typeid", typeid);
        String secret_value = DateUtil.getStrCurrentDate();
        Log.d(TAG, "queryZxByTypeId: secret_value++++++++" + secret_value);
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/news/listInfo", callBack, map);
    }

    public String queryTypeidByTypename(String typename) {
        String typeid = "";
        switch (typename) {
            case "就业":
                typeid = "1";
                break;
            case "创业":
                typeid = "2";
                break;
            case "工资指导价":
                typeid = "3";
                break;
            case "通知公告":
                typeid = "4";
                break;
            case "政策法规":
                typeid = "5";
                break;
            default:
                break;
        }
        return typeid;
    }


    /**
     * 政策法规列表
     */
    public void queryZcfg(int pageNum, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("type", "2");
        post(Constants.IP + "android/news/listNews", callBack, map);
    }

    /**
     * 就业新闻列表
     */
    public void queryJyxw(String keyWord, int pageNum, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("title", keyWord);
        map.put("type", "1");
        post(Constants.IP + "android/news/listNews", callBack, map);
    }


//    /**
//     * 招聘信息列表
//     */
//    public void queryZpxx(String pageNum, String aab004, String acb202, String acb216, AjaxCallBack<?> callBack) {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("page", pageNum + "");
//        map.put("aab004", aab004); //单位名称
//        map.put("acb202", acb202); //县市编号
//        map.put("acb216", acb216); //工种说明
//        post(Constants.IP + "android/corecruitment/listRecruitInfo", callBack, map);
//    }

    /**
     * 招聘信息、招聘企业未登录加载地点、首页就业电话加载地址
     *
     * @return
     */
    public void doUnLoginAreas(AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        post(Constants.IP + "android/person/listCity", callBack, map);
    }


    /***
     * 职位详细
     * @param positionId
     * @return
     */
    public void getPosition(String positionId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("acb210", positionId);//职位编号
        post(Constants.IP + "android/corecruitment/queryRecruitmentDetail", callBack, map);
    }

    /***
     * 投递简历
     * @param positionId
     * @param userid
     * @param companyId
     * @param remark
     * @return
     */
    public void doPostDelivery(String positionId, String userid, String companyId, String remark, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("acb210", positionId);//岗位编号
        map.put("id", userid);//用户名
        map.put("aab001", companyId);//公司编号
        map.put("remark", remark);//描述
        post(Constants.IP + "android/sending/sendResume", callBack, map);
    }

    /***
     * 招聘信息里面的---公司详细
     * @return
     */
    public void getEnterPrise(String companyId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aab001", companyId);
        post(Constants.IP + "android/corecruitment/queryCompanyDetail", callBack, map);
    }

    /***
     * 查询公司所有职位
     * @param companyId
     * @return
     */
    public void doQueryPosition(String companyId, int pageNum, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aab001", companyId);
        map.put("page", pageNum + "");
        post(Constants.IP + "android/corecruitment/listRecruitInfo", callBack, map);
    }

    /**
     * 培训信息列表
     */
    public void queryPxxx(String selectStr, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("class_name", selectStr);        // key 未定
//        post(Constants.IP + "android/news/listNews", callBack, map);
//        post("http://192.168.66.244:8080/yzjycy/android/attendance/listTrainingCourse.do", callBack, map);
//        post("http://192.168.1.105:8888/s", callBack, map);
//        post("http://192.168.66.122:8888/s", callBack, map);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        post(Constants.IP + "android/attendance/listTrainingCourse.do", callBack, SignHashMap);
    }

    /***
     * 培训信息详细
     * @return
     */
    public void doPxxxDetailsQuery(String class_id, String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("class_id", class_id);
        map.put("userid", userid);
        post("http://192.168.66.244:8080/yzjycy/android/attendance/viewTrainingCourse", callBack, map);
    }

    /**
     * 验证个人信息
     */
    public void doVerifyUserinfor(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        post("http://192.168.66.244:8080/yzjycy/android/attendance/verifyPersonalInfo.do", callBack, map);
    }

    /**
     * 报名
     */
    public void doBm(String userid, String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("class_id", class_id);
        post("http://192.168.66.244:8080/yzjycy/android/attendance/enrollTrainingCourse.do", callBack, map);
    }


    /**
     * 完善个人信息
     */
    public void doCompletePersonalInfo(String userid, String name, String idcard, int sex, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("name", name);
        map.put("idcard", idcard);
        map.put("sex", sex + "");
        post("http://192.168.66.244:8080/yzjycy/android/attendance/completePersonalInfo.do", callBack, map);
    }

    /**
     * 考勤信息列表
     */
    public void doKqInfoQuery(String keyword, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("keyword", keyword);
        map.put("userid", user.getUserId());
        post("http://192.168.66.244:8080/yzjycy/android/attendance/enrolledTrainingList.do", callBack, map);
    }

    /**
     * 考勤信息详情
     */
    public void doKqInfoDetails(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());    // key 未定
        map.put("class_id", class_id);        // key 未定
        post("http://192.168.66.244:8080/yzjycy/android/attendance/viewCheckOutIn.do", callBack, map);
    }


    /**
     * 考勤信息统计
     */
    public void doKqRecordQuery(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());    // key 未定
        map.put("class_id", class_id);        // key 未定
        post("http://192.168.66.244:8080/yzjycy/android/attendance/hisCheckOutInInfo.do", callBack, map);
    }

    /**
     * 是否可以考勤
     */
    public void isKqInfo(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());
        map.put("class_id", class_id);
        post("http://192.168.66.244:8080/yzjycy/android/attendance/verifyCheckInTime.do", callBack, map);
    }

    /**
     * 是否可以签退
     */
    public void isQtInfo(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());
        map.put("class_id", class_id);
        post("http://192.168.66.244:8080/yzjycy/android/attendance/verifyCheckOutTime.do", callBack, map);
    }

    /**
     * 考勤
     */
    public void postResume(Map<String, String> map, List<com.yifeng.nox.android.http.entity.FormFile> list, AjaxCallBack<?> callBack) {
        upload("http://192.168.66.244:8080/yzjycy/android/attendance/doCheckOutIn.do", callBack, map, list);
    }


}
