package com.winksoft.android.yzjycy.data;

import android.content.Context;
import android.util.Log;

import com.winksoft.android.yzjycy.newentity.User;
import com.winksoft.android.yzjycy.newentity.UserSession;
import com.winksoft.android.yzjycy.util.Constants;
import com.winksoft.android.yzjycy.util.DataConvert;
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

    public XwzxDAL(Context context) {
        super(context);
    }

    /**
     * 政策法规列表
     */
    public void queryZcfg(int pageNum,AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page",pageNum+"");
        map.put("type","2");
        post(Constants.IP + "android/news/listNews", callBack, map);
    }

    /**
     * 就业新闻列表
     */
    public void queryJyxw(String keyWord,int pageNum, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page",pageNum+"");
        map.put("title",keyWord);
        map.put("type","1");
        post(Constants.IP + "android/news/listNews", callBack, map);
    }

    /**
     * 招聘信息列表
     */
    public void queryZpxx(String pageNum,String aab004,String acb202,String acb216, AjaxCallBack<?> callBack) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", pageNum + "");
        map.put("aab004", aab004); //单位名称
        map.put("acb202", acb202); //县市编号
        map.put("acb216", acb216); //工种说明
        post(Constants.IP + "android/corecruitment/listRecruitInfo", callBack, map);
    }

    /**
     * 招聘信息、招聘企业未登录加载地点
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
    public void getPosition(String positionId,AjaxCallBack<?> callBack){
        Map<String,String> map=new HashMap<String,String>();
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
    public void doPostDelivery(String positionId,String userid,String companyId,String remark,AjaxCallBack<?> callBack){
        Map<String,String> map=new HashMap<String,String>();
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
    public void getEnterPrise(String companyId,AjaxCallBack<?> callBack){
        Map<String,String> map=new HashMap<String,String>();
        map.put("aab001", companyId);
        post(Constants.IP + "android/corecruitment/queryCompanyDetail", callBack, map);
    }

    /***
     * 查询公司所有职位
     * @param companyId
     * @return
     */
    public void doQueryPosition(String companyId,int pageNum,AjaxCallBack<?> callBack){
        Map<String,String> map=new HashMap<String,String>();
        map.put("aab001",companyId);
        map.put("page",pageNum+"");
        post(Constants.IP + "android/corecruitment/listRecruitInfo", callBack, map);
    }

}
