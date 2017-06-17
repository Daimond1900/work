package com.winksoft.android.yzjycy.data;

import android.content.Context;
import android.util.Log;

import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DateUtil;
import com.winksoft.android.yzjycy.util.ParamSignUtils;
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
public class XwzxDAL extends com.yifeng.nox.android.http.BaseDAL {
    private final static String secret_key = "(YZcyjy2017!@#$)";//签名-key
    private static final String TAG = "XwzxDAL";
    private User user;
    private UserSession userSession;

    public XwzxDAL(Context context) {
        super(context);
        userSession = new UserSession(context);
        user = userSession.getUser();
    }

    /**
     * 性别
     */
    public List<Map<String, String>> queryXB() {
        List<Map<String, String>> xbs = new ArrayList<>();
        Map<String, String> xb_map = new HashMap<>();
        xb_map.put("zdmc", "男");
        xb_map.put("zddm", "1");
        xbs.add(xb_map);
        xb_map = new HashMap<>();
        xb_map.put("zdmc", "女");
        xb_map.put("zddm", "2");
        xbs.add(xb_map);
        return xbs;
    }

    public List<Map<String, String>> queryFklx() {
        List<Map<String, String>> fklx = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("zdmc", "问题咨询");
        map1.put("zddm", "1");
        fklx.add(map1);

        map1 = new HashMap<>();
        map1.put("zdmc", "意见反馈 ");
        map1.put("zddm", "2");
        fklx.add(map1);

        map1 = new HashMap<>();
        map1.put("zdmc", "其他问题");
        map1.put("zddm", "10");
        fklx.add(map1);

        return fklx;
    }


    /***
     * 修改密码
     * @return
     */
    public void doUpdatePwd(String id, String name,
                            String old_pwd, String new_pwd, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("name", name);
        map.put("old_pwd", old_pwd);
        map.put("new_pwd", new_pwd);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        post(Constants.IP + "android/system/changePwd", callBack, map);
    }

    /**
     * 查询我的反馈
     */
    public void queryWdFkInfo(String userId, int page, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        map.put("page", page + "");
        post(Constants.IP + "android/person/listFeedBack", callBack, map);
    }

    /**
     * 查询问卷调查列表
     */
    public void queryWjdcInfo(int page, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        map.put("page", page + "");
        post(Constants.IP + "android/attendance/listQuestionnaire", callBack, map);
    }

    /**
     * 查询我的问卷调查列表
     */
    public void queryWdWjdcInfo(String userid, int page, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        map.put("page", page + "");
        post(Constants.IP + "android/person/listQuestionnaire", callBack, map);
    }

    /**
     * 创建我的反馈
     */
    public void xzWdfkResume(Map<String, String> map, List<com.yifeng.nox.android.http.entity.FormFile> list, AjaxCallBack<?> callBack) {
        upload(Constants.IP + "android/person/CreateFeedBack", callBack, map, list);
    }

    /**
     * 查询我的反馈详情
     */
    public void queryWdFkInfoDetail(String id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        post(Constants.IP + "android/person/viewFeedBack", callBack, map);
    }

    /**
     * jyzt
     */
    public List<Map<String, String>> queryJYZT() {
        List<Map<String, String>> xbs = new ArrayList<>();
        Map<String, String> xb_map = new HashMap<>();
        xb_map.put("zdmc", "失业");
        xb_map.put("zddm", "1");
        xbs.add(xb_map);
        xb_map = new HashMap<>();
        xb_map.put("zdmc", "在职");
        xb_map.put("zddm", "2");
        xbs.add(xb_map);
        xb_map = new HashMap<>();
        xb_map.put("zdmc", "其他");
        xb_map.put("zddm", "3");
        xbs.add(xb_map);
        return xbs;
    }

    /**
     * queryHyzk
     */
    public List<Map<String, String>> queryHyzk() {
        List<Map<String, String>> xbs = new ArrayList<>();
        Map<String, String> xb_map = new HashMap<>();
        xb_map.put("zdmc", "已婚");
        xbs.add(xb_map);
        xb_map = new HashMap<>();
        xb_map.put("zdmc", "未婚");
        xbs.add(xb_map);
        return xbs;
    }

    /**
     * 查询培训经历
     */
    public void queryPxjl(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchAPXJLB", callBack, map);
    }

    /**
     * 查询教育经历
     */
    public void queryJyjl(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchAJYJLB", callBack, map);
    }

    /**
     * 查询工作经历
     */
    public void queryGzjl(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchAGZJLB", callBack, map);
    }

    /**
     * 查询求职意向
     */
    public void queryQzyx(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchAQZYXB", callBack, map);
    }

    /**
     * 查询语言能力
     */
    public void queryYynl(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchACYLNL", callBack, map);
    }

    /**
     * 查询技术专长
     */
    public void queryJszc(String userId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchAJSZCB", callBack, map);
    }

    /**
     * 新闻列表 首页默认展示
     */
    public void queryXwMr(AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        post(Constants.IP + "android/news/listNewsIndex", callBack, SignHashMap);
    }

    /**
     * 新闻列表 刷新展示的数据
     */
    public void queryXwRef(int pagesize, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("page", pagesize + "");
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/news/listIndex", callBack, map);
    }

    /**
     * 首页招聘会列表
     */
    public void queryZph(String pagesize, String aab301, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aab301", aab301);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize);// 默认是三
        post(Constants.IP + "android/news/listData", callBack, map);
    }

    /**
     * 招聘会详情
     */
    public void queryZphInfo(String id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/news/dataInfo", callBack, map);
    }

    /**
     * 按照资讯类型查询 资讯列表
     */
    public void queryZxByTypeId(int pagesize, String typeid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("typeid", typeid);
        String secret_value = DateUtil.getStrCurrentDate();
        Log.d(TAG, "queryZxByTypeId: secret_value++++++++" + secret_value);
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize + "");
        post(Constants.IP + "android/news/listInfo", callBack, map);
    }

    public String queryTypeidByTypename(String typename) {
        String typeid = "";
        switch (typename) {
            case "就业新闻":
                typeid = "1";
                break;
            case "创业新闻":
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
     * 招聘信息列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * acb239	工作性质
     * aab001	单位编号
     */
    public void queryZpxx(int pagesize, String aab301, String aca112, String aca111, String acb239, String aab001, String acb330, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aab301", aab301); //县市编号  行政区号
        map.put("aca112", aca112);  //岗位名称
        map.put("aca111", aca111); //工种说明
        map.put("acb239", acb239); //工作性质
        map.put("aab001", aab001); //公司编号
        map.put("acb330", acb330); //招聘会id
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize + ""); //页数
        post(Constants.IP + "android/recruit/listDataCb21", callBack, map);
    }

    /**
     * 招聘会招聘信息列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * acb239	工作性质
     * aab001	单位编号
     */
    public void queryZphZpxx(int pagesize, String acb330, String aac001, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("acb330", acb330); //招聘会id
        map.put("aac001", aac001); //招聘会id
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize + ""); //页数
        post(Constants.IP + "android/recruit/listJob", callBack, map);
    }


    /***
     * 职位详细
     * @param positionId
     * @return
     */
    public void getPosition(String usrId, String positionId, int flag, AjaxCallBack<?> callBack) {
        String url = "";
        Map<String, String> map = new HashMap<String, String>();

        if (flag == 1) { // 报名职位详情接口
            url = "android/person/viewDelivery";
        } else if (flag == 3) { // 关注职位详情接口
            url = "android/person/viewAgzzwb";
        } else {
            url = "android/recruit/viewCb21";
        }

        map.put("id", positionId);//职位编号
        map.put("aac001", usrId);//userId
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + url, callBack, map);
    }

    /**
     * 招聘会单位列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * appSign	签名认证
     */
    public void queryZphZpdw(int pagesize, String acb330, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("acb330", acb330); //招聘会id
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize + ""); //页数
        post(Constants.IP + "android/recruit/listCom", callBack, map);
    }


    /**
     * 招聘单位列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * appSign	签名认证
     */
    public void queryZpdw(int pagesize, String aab301, String aca112, String aca111, String acb330, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aca112", aca112);  //岗位名称
        map.put("aca111", aca111); //工种说明
        map.put("aab301", aab301); //县市编号  行政区号
        map.put("acb330", acb330); //招聘会id
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", pagesize + ""); //页数
        post(Constants.IP + "android/recruit/listDataCb20", callBack, map);
    }


    /**
     * 报名单位列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * appSign	签名认证
     */
    public void queryBmdw(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person//listDeliveryCB20", callBack, map);
    }

    /**
     * 报名职位列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * appSign	签名认证
     */
    public void queryBmZw(int page, String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", page + "");
        post(Constants.IP + "android/person/listDelivery", callBack, map);
    }

    /**
     * 关注职位列表
     * aab301	指该招聘岗位工作地点所在行政区划的代码。
     * aca112	岗位名称
     * aca111	职业（工种）
     * appSign	签名认证
     */
    public void queryGzzw(int page, String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", page + "");
        post(Constants.IP + "android/person/listAgzzwb", callBack, map);
    }

    /**
     * 社保查询，检查用户信息是否完整
     */
    public void checkUserInfo(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userid);

        Log.d("SheBaoFm", "checkUserInfo: 社保ID = " + userid);

        map.put("datetime", DateUtil.getStrCurrentDateTime());
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/socialsec/getUserInfo", callBack, map);
    }

    /**
     * 社保查询，完善个人信息
     * Id	账号
     * idcard	身份证号码
     * sbcard	社保号
     * idnumber	省个人识别码
     * phone	联系电话
     * appSign	签名认证
     * dateTime	yyyy-MM-dd HH:mm:ss
     */
    public void doCompleteSheBaoInfo(String name, String userid, String idcard, String sbcard, String idnumber, String phone, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userid);
        map.put("name", name);
        map.put("idcard", idcard);
        map.put("sbcard", sbcard);
        map.put("idnumber", idnumber);
        map.put("phone", phone);
        map.put("datetime", DateUtil.getStrCurrentDateTime());
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/socialsec/updatePersonal", callBack, map);
    }

    /**
     * 培训信息列表
     */
    public void queryPxxx(int page, String selectStr, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("class_name", selectStr);        // key 未定
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", page + "");
        post(Constants.IP + "android/attendance/listTrainingCourse", callBack, map);
    }

    /***
     * 培训信息详细
     * @return
     */
    public void doPxxxDetailsQuery(String class_id, String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("class_id", class_id);
        map.put("userid", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/viewTrainingCourse", callBack, map);

    }

    /**
     * 验证个人信息
     */
    public void doVerifyUserinfor(String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/verifyPersonalInfo", callBack, map);
    }

    /**
     * 报名 map.put("page", pagesize+""); //页数
     */
    public void doBm(String userid, String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("class_id", class_id);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/enrollTrainingCourse", callBack, map);
    }


    /**
     * 完善个人信息
     */
    public void doCompletePersonalInfo(String userid, String name, String idcard, int sex, String sjh, String csrq, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("name", name);
        map.put("idcard", idcard);
        map.put("sex", sex + "");
        map.put("phone", sjh);     // 手机号
        map.put("birth", csrq);    // 出生日期
        Log.d(TAG, "手机号: " + sjh);
        Log.d(TAG, "出生日期: " + csrq);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/completePersonalInfo", callBack, map);
    }

    /**
     * 考勤信息列表
     */
    public void doKqInfoQuery(int page, String keyword, String userid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        Log.d("cc_pxbm", "doKqInfoQuery: 传值ID = " + userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", page + "");
        post(Constants.IP + "android/attendance/enrolledTrainingList", callBack, map);
    }

    /**
     * 考勤信息详情
     */
    public void doKqInfoDetails(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());    // key 未定
        map.put("class_id", class_id);        // key 未定
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("ftype", "ANDROID");
        post(Constants.IP + "android/attendance/viewCheckOutIn", callBack, map);
    }


    /**
     * 考勤信息统计
     */
    public void doKqRecordQuery(String class_id, int page, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getUserId());    // key 未定
        map.put("class_id", class_id);        // key 未定
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("page", page + "");
        post(Constants.IP + "android/attendance/hisCheckOutInInfo", callBack, map);
    }

    /**
     * 是否可以考勤
     */
    public void isKqInfo(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", user.getUserId());
        map.put("course_id", class_id);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/verifyCheckInTime", callBack, map);
    }

    /**
     * 是否可以签退
     */
    public void isQtInfo(String class_id, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", user.getUserId());
        map.put("course_id", class_id);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/attendance/verifyCheckOutTime", callBack, map);
    }

    /**
     * 考勤
     */
    public void postResume(Map<String, String> map, List<com.yifeng.nox.android.http.entity.FormFile> list, AjaxCallBack<?> callBack) {
        upload(Constants.IP + "android/attendance/doCheckOutIn", callBack, map, list);
    }

    /***
     * 投递简历
     * @param positionId
     * @param userid
     * @return
     */
    public void doNewPostDelivery(String userid, String positionId, String dwmc, String zwmc, String gsid, String zphid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        map.put("acb200", positionId);
        map.put("aab004", dwmc);
        map.put("aca112", zwmc);
        map.put("aac001x", gsid);
        map.put("acb330", zphid);

        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/doDelivery", callBack, map);

        Log.d(TAG, "doNewPostDelivery: 来看参数：" + "aac001:" + userid + "   " +
                "acb200:" + positionId + "   " +
                "aab004:" + dwmc + "   " +
                "aca112:" + zwmc + "   " +
                "aac001x:" + gsid + "   " +
                "acb330:" + zphid + "   " +
                "appSign:" + (String) SignHashMap.get("appSign")


        );
    }

    /***
     * 取消 ---- 投递简历
     * @param positionId
     * @param userid
     * @return
     */
    public void doQxPostDelivery(String userid, String positionId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        map.put("acb200", positionId);

        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/cancelDelivery", callBack, map);
    }

    /***
     * 取消 ----收藏
     * @param positionId
     * @param userid
     * @return
     */
    public void doQxScZw(String userid, String positionId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        map.put("acb200", positionId);

        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/cancelAgzzwb", callBack, map);
    }

    /**
     * 查询参数 区域 | 工作性质
     */
    public void doGetParams(String param, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", user.getUserId());
        map.put("zdlb", param);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/getParams", callBack, map);
    }

    /*** 招聘信息里面的---公司详细
     *
     * @return
     */
    public void getEnterPrise(String companyId, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", companyId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/viewCb20", callBack, map);
    }

    /**
     * 收藏职位
     * aac001	个人账号编号
     * acb200	招聘职位编号
     * aab004	单位名称
     * aca112	职位名称
     * aac001x	单位账号编号
     * acb330	招聘会编号
     * appSign	签名认证
     * http://222.189.216.110:8889/yzjycy/android/recruit/doAgzzwb
     *
     * @return
     */
    public void doSczw(String userId, String zwId, String dwmc, String zwmc, String dwzhId, String zphId, String gz, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        map.put("acb200", zwId);
        map.put("aab004", dwmc);
        map.put("aca112", zwmc);
        map.put("aac001x", dwzhId);
        map.put("acb330", zphId);
        map.put("aca111", gz);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/recruit/doAgzzwb", callBack, map);
    }


    /**
     * 简历查询
     *
     * @return
     */
    public void doJlcx(String userId, AjaxCallBack<?> callBack) {
        Log.d(TAG, "doJlcx: 我得ID： " + userId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/listResume", callBack, map);
    }

    /**
     * 查询简历中个人基本信息
     */
    public void doJLgejbxx(AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", user.getUserId());
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/searchACAC01", callBack, map);
    }

    /**
     * 修改简历中个人基本信息
     */
    public void doModifyJbxx(String userId,
                             String xm,
                             String xb,
                             String sfz,
                             String xl,
                             String mz,
                             String zzmm,
                             String sxzy,
                             String zyms,
                             String csrq,
                             String hyzkStr,
                             String jkzkStr,
                             String byyxStr,
                             String sgStr,
                             String tzStr,
                             String slStr,
                             String jsjspId,
                             String byrqStr,
                             String jyztId,
                             String zwjsStr,
                             String jtzzStr,
                             String yzbmStr,
                             String szdqStr,
                             String gddhStr,
                             String sjhmStr,
                             String dzyjStr,
                             List<com.yifeng.nox.android.http.entity.FormFile> list,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        map.put("aac002", sfz);  //身份证号
        map.put("acb501", sjhmStr);  //手机号码
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aac003", xm);  //姓名
        map.put("aac004", xb);  //性别
        map.put("aac011", xl);  //学历
        map.put("aac005", mz);  //民族
        map.put("aac024", zzmm);  //政治面貌
        map.put("aac183", sxzy);  //所学专业
        map.put("zyms", zyms);  //专业描述
        map.put("aac006", csrq);  //出生日期
        map.put("hyzk", hyzkStr);  //婚姻状况
        map.put("jkzk", jkzkStr);  //健康状况
        map.put("byyx", byyxStr);  //毕业院校
        map.put("aac034", sgStr);  //身高
        map.put("aac035", tzStr);  //体重
        map.put("aac036", slStr);  //视力
        map.put("aac179", jsjspId);  //计算机水平
        map.put("bysj", byrqStr);  //毕业日期
        map.put("jyzt", jyztId);  //就业状态
        map.put("zwjs", zwjsStr);  //自我介绍
        map.put("aac026", jtzzStr);  //家庭住址
        map.put("aae007", yzbmStr);  //邮政编号
        map.put("aab301", szdqStr);  //所在地区
        Log.d(TAG, "doModifyJbxx: szdqStr = " + szdqStr);
        map.put("aae005", gddhStr);  //固定电话
        map.put("aae159", dzyjStr);  //电子信箱
        upload(Constants.IP + "android/person/updateACAC01", callBack, map, list);
//        post(Constants.IP + "android/person/updateACAC01", callBack, map);
    }


    /**
     * doXzJyjl
     * 修改简历中教育经历
     */
    public void doModifyJyjl(String jyidStr,
                             String qsrqStr,
                             String jsrqStr,
                             String byyxStr,
                             String sxzyStr,
                             String zcdjStr,
                             String hdzcStr,
                             String xxbzStr,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("jyid", jyidStr);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("byyx", byyxStr);  //毕业院校
        map.put("aac183", sxzyStr);  //所学专业
        map.put("aac015", zcdjStr);  //职称等级
        map.put("aac014", hdzcStr);  //获得职称
        map.put("xxbz", xxbzStr);  //学习备注
        post(Constants.IP + "android/person/updateAJYJLB", callBack, map);
    }

    /**
     * 新增简历中教育经历
     */
    public void doXzJyjl(String userid,
                         String qsrqStr,
                         String jsrqStr,
                         String byyxStr,
                         String sxzyStr,
                         String zcdjStr,
                         String hdzcStr,
                         String xxbzStr,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("byyx", byyxStr);  //毕业院校
        map.put("aac183", sxzyStr);  //所学专业
        map.put("aac015", zcdjStr);  //职称等级
        map.put("aac014", hdzcStr);  //获得职称
        map.put("xxbz", xxbzStr);  //学习备注
        post(Constants.IP + "android/person/addAJYJLB", callBack, map);
    }

    /**
     * 新增简历中工作经历
     */
    public void doXzGzjl(String userid,
                         String qsrqStr,
                         String jsrqStr,
                         String dwmcStr,
                         String szzwStr,
                         String gzbzStr,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("aab004", dwmcStr);  //单位名称
        map.put("aca111", szzwStr);  //所在职位
        map.put("gzbz", gzbzStr);  //工作备注
        post(Constants.IP + "android/person/addAGZJLB", callBack, map);
    }

    /**
     * 新增简历中求职意向
     * aac001	账号编号
     * aab019	单位类型（不参与签名认证）
     * aab020	经济类型（不参与签名认证）
     * aab301	工作地点（不参与签名认证）
     * acc201	工作性质（不参与签名认证）
     * aca111	岗位名称（不参与签名认证）
     * acb22a	岗位描述（不参与签名认证）
     * acb242	月薪要求（不参与签名认证）
     */
    public void doXzQzyx(String userid,
                         String dwlx,
                         String jjlx,
                         String gzdd,
                         String gzxz,
                         String gwmc,
                         String gwms,
                         String yxyq,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aab019", dwlx);  //单位类型
        map.put("aab020", jjlx);  //经济类型
        map.put("aab301", gzdd);  //工作地点
        map.put("acc201", gzxz);  //工作性质
        map.put("aca111", gwmc);  //岗位名称
        map.put("acb22a", gwms);  //岗位描述
        map.put("acb242", yxyq);  //月薪要求
        post(Constants.IP + "android/person/addAQZYXB", callBack, map);
    }

    /**
     * 新增简历中求职意向
     * aac001	账号编号
     * aab019	单位类型（不参与签名认证）
     * aab020	经济类型（不参与签名认证）
     * aab301	工作地点（不参与签名认证）
     * acc201	工作性质（不参与签名认证）
     * aca111	岗位名称（不参与签名认证）
     * acb22a	岗位描述（不参与签名认证）
     * acb242	月薪要求（不参与签名认证）
     */
    public void doModifyQzyx(String qzid,
                             String dwlx,
                             String jjlx,
                             String gzdd,
                             String gzxz,
                             String gwmc,
                             String gwms,
                             String yxyq,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("qzid", qzid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aab019", dwlx);  //单位类型
        map.put("aab020", jjlx);  //经济类型
        map.put("aab301", gzdd);  //工作地点
        map.put("acc201", gzxz);  //工作性质
        map.put("aca111", gwmc);  //岗位名称
        map.put("acb22a", gwms);  //岗位描述
        map.put("acb242", yxyq);  //月薪要求
        post(Constants.IP + "android/person/updateAQZYXB", callBack, map);
    }

    /**
     * 新增简历中技术专长
     */
    public void doXzJszc(String userId, String jsmcId, String jsdjId, String sj, String nx,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aca111", jsmcId);  //技术专长代码
        map.put("aac015", jsdjId);  //技术专长等级
        map.put("bfrq", sj);  //颁发时间
        map.put("csnx", nx);  //从事年限
        post(Constants.IP + "android/person/addAJSZCB", callBack, map);
    }

    /**
     * 修改简历中工作经历
     */
    public void doModifyGzjl(String gzjlid,
                             String qsrqStr,
                             String jsrqStr,
                             String dwmcStr,
                             String szzwStr,
                             String gzbzStr,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("gzid", gzjlid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("aab004", dwmcStr);  //单位名称
        map.put("aca111", szzwStr);  //所在职位
        map.put("gzbz", gzbzStr);  //工作备注
        post(Constants.IP + "android/person/updateAGZJLB", callBack, map);
    }

    /**
     * 修改简历中技术专长
     */
    public void doModifyJszc(String jsid, String jsmcId, String jsdjId, String sj, String nx,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("jsid", jsid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aca111", jsmcId);  //技术专长代码
        map.put("aac015", jsdjId);  //技术专长等级
        map.put("bfrq", sj);  //颁发时间
        map.put("csnx", nx);  //从事年限
        post(Constants.IP + "android/person/updateAJSZCB", callBack, map);
    }

    /**
     * 删除简历中教育经历c
     */
    public void doScJyjl(String jyid, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("jyid", jyid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteAJYJLB", callBack, map);
    }

    /**
     * 删除简历中工作经历c
     */
    public void doScGzjl(String gzid, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("gzid", gzid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteAGZJLB", callBack, map);
    }

    /**
     * /**
     * 删除简历中求职意向c
     */
    public void doScQzyx(String qzid, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("qzid", qzid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteAQZYXB", callBack, map);
    }

    /**
     * 删除简历中技术专长
     */
    public void doScJszc(String jsid, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("jsid", jsid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteAJSZCB", callBack, map);
    }

    /**
     * 新增简历中培训经历
     */
    public void doXzPxjl(String userid,
                         String qsrqStr,
                         String jsrqStr,
                         String pxdwStr,
                         String pxnrStr,
                         String pxjgStr,
                         String pxbzStr,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("aab004", pxdwStr);  //培训单位名称
        map.put("pxnr", pxnrStr);  //培训内容
        map.put("pxjg", pxjgStr);  //培训结果
        map.put("pxbz", pxbzStr);  //培训备注
        post(Constants.IP + "android/person/addAPXJLB", callBack, map);
    }

    /**
     * 新增简历中培训经历
     */
    public void doModifyPxjl(String pxid,
                             String qsrqStr,
                             String jsrqStr,
                             String pxdwStr,
                             String pxnrStr,
                             String pxjgStr,
                             String pxbzStr,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("pxid", pxid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("qsrq", qsrqStr);  //起始日期
        map.put("jsrq", jsrqStr);  //结束日期
        map.put("aab004", pxdwStr);  //培训单位名称
        map.put("pxnr", pxnrStr);  //培训内容
        map.put("pxjg", pxjgStr);  //培训结果
        map.put("pxbz", pxbzStr);  //培训备注
        post(Constants.IP + "android/person/updateAPXJLB", callBack, map);
    }

    /**
     * 删除简历中培训经历c
     */
    public void doScPxjl(String pxid, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("pxid", pxid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteAPXJLB", callBack, map);
    }

    /**
     * 删除简历中语言能力
     */
    public void doScyynl(String yyid, AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("yyid", yyid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        post(Constants.IP + "android/person/deleteACYLNL", callBack, map);
    }

    /**
     * 新增简历中语言能力
     */
    public void doXzYynl(String userId, String yzdmStr, String yyspId,
                         AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("aac001", userId);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aac038", yzdmStr);  //语种代码
        map.put("aac175", yyspId);  //外语水平
        post(Constants.IP + "android/person/addACYLNL", callBack, map);
    }

    /**
     * 修改简历中语言能力
     */
    public void doModifyYynl(String yyid, String yzdmStr, String yyspId,
                             AjaxCallBack<?> callBack) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("yyid", yyid);
        String secret_value = DateUtil.getStrCurrentDate();
        map.put("Date", secret_value);
        HashMap SignHashMap = ParamSignUtils.sign(map, secret_key);
        map.put("appSign", (String) SignHashMap.get("appSign"));
        map.put("aac038", yzdmStr);  //语种代码
        map.put("aac175", yyspId);  //外语水平
        post(Constants.IP + "android/person/updateACYLNL", callBack, map);
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


}
