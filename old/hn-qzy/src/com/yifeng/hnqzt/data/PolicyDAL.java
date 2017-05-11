package com.yifeng.hnqzt.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yifeng.hnqzt.util.DataConvert;

import android.content.Context;

/**
 * comments:政策法规
 * @author WuJiaHong
 * Date: 2012-9-20
 */
public class PolicyDAL extends BaseDAL
{

	/**
	 * @param context
	 */
	public PolicyDAL(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	/***
	 * 政治法规列表
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, Object>> doQuery(int pageNum,String keyWord,int type)
	{
		
		this.setUrl(this.getIpconfig()+"android/managementcloud/listNews");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("state", type+""));
		params.add(new BasicNameValuePair("page", pageNum+""));
		params.add(new BasicNameValuePair("title",keyWord));//关键字
		String json=this.doPost(params);
		return DataConvert.toConvertObjectList(json, "news");
		/*
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("state", "1");
		map.put("id", "1");
		map.put("pTitle", "关于调整扬州市区失业保险金标准上下限的通知");
		map.put("pFrom", "扬人社〔2012〕198号 \t各区人力资源和社会保障局、财政局： 为保障失业人员的基本生活，根据《江苏省失业保险规定》、《关于调整扬州市最低工资标准的通知》（扬人社〔2012〕192号）等文件精神，经研究，决定调整市区失业保险金标准上下限，现将有关事项通知如下：");
		map.put("pDate", "2012-05-30\t15:19:23");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("state", "1");
		map.put("id", "2");
		map.put("pTitle", "省政府关于加快完善就业服务体系促进社会就业更加充分的实施意见");
		map.put("pFrom", "各市、县（市、区）人民政府，省各委办厅局，省各直属单位： 为深入贯彻党的十七届五中全会精神和胡锦涛总书记对江苏工作“六个注重”最新要求，根据省委、省政府关于大力推进民生幸福工程的整体部署，现就加快完善就业服务体系、促进社会就业更加充分提出如下实施意见：");
		map.put("pDate", "2011-11-24\t11:35:24");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("state", "1");
		map.put("id", "3");
		map.put("pTitle", "关于做好促进重点群体就业工作的通知");
		map.put("pFrom", "为认真贯彻落实市委、市政府统筹城乡就业政策，突出工作重点，加大三类重点群体就业促进工作，努力把扬州打造成为充分就业城市。根据市政府办公室《关于印发扬州市充分就业城市建设行动计划的通知》，现就我市做好促进重点群体就业工作的有关事项通知如下：");
		map.put("pDate", "2011-08-15\t21:45:37");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("state", "1");
		map.put("id", "4");
		map.put("pTitle", "江苏省失业保险规定");
		map.put("pFrom", "《江苏省失业保险规定》已于2011年4月28日经省人民政府第66次常务会议讨论通过，现予发布，自2011年7月1日起施行。");
		map.put("pDate", "2011-05-09\t18:20:45");
		list.add(map);
		return list;*/
	}

}
