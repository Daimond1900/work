package com.winksoft.yzsmk.ftp;

import java.util.List;
import java.util.Map;

import android.util.Log;

public class XFFilesInfo {
	
	private static String tag = "XFFilesInfo";
	private List<Map<String, Object>> mapList1;
	public XFFilesInfo(List<Map<String, Object>> mapList) 
	{
		mapList1 = mapList;
	}
	 
	//文件头
	public String getHead()
	{
	    String ret = "";	
		int tranCount = mapList1.size();
		String head_1 = "1001"; //0	4	n	文件类型	1001
		String head_2 = padRight(tranCount+"",10);//4	10	n	交易记录数 	交易记录数
		String head_3 = padRight(316+"",8); //14	8	n	记录长度	单条记录长度
		String head_4 = padRight("",24);//22	24	ans	保留域	预留区域 空格
		//String head_5 = "\n";//46	1	s	回车符	回车符’\n’
	    ret = head_1 + head_2 + head_3 + head_4;
	    return ret;
	}
	
	public String getBody()
	{
		String ret = "";
		return ret;
	} 
	
	public String getBody(int index)
	{
		String ret = "";
		if(mapList1!=null)
		{
			try
			{
		        Map map = mapList1.get(index);
		        String body_0  = padRight(map.get("body_0").toString(),12); //0	12	n	本地流水号	本地自定义流水号	M
		        String body_12 = padRight(map.get("body_12").toString(),20);//12	20	an	卡片序列号	卡主账号，不足位数以空格填充	M
		        String body_32 = padRight(map.get("body_32").toString(),19);//32	19	n	主账号	向左对齐，不足19位时不足部分补空格（右补空格）。-乘客卡号	M
		        String body_51 = padRight(map.get("body_51").toString(),16);//51	16	H	卡内号	卡主账号后16位	M
		        String body_67 = padRight(map.get("body_67").toString(),3);//67	3	n	卡种	01 普通卡；02 学生卡；03老年卡；05  拥军卡；06 离休卡；07 优抚卡；08 残疾人卡；09  敬老卡；10  异形卡；11 纪念卡	M
		        String body_70 = padRight(map.get("body_70").toString(),1);//70	1	n	记录类型	0：正常交易 1：黑卡交易	M
		        String body_71 = padRight(map.get("body_71").toString(),8);//71	8	H	交易前余额	消费前卡余额	M
		        String body_79 = padRight(map.get("body_79").toString(),8);//79	8	H	*交易金额	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
		        String body_87 = padRight(map.get("body_87").toString(),8);//87	8	H	交易余额	用8个可见的16进制字符（0～9，A～F）表示；电子钱包的消费，本域后补两个F。  若无法填写用缺省值空格填充。  此字段表示卡片消费后余额	M
		        String body_95 = padRight(map.get("body_95").toString(),2);//95	2	n	*交易类型标识	交易性质  06-表示电子钱包脱机消费；09-复合消费的类型。	M
		        String body_97 = padRight("",8);//97	8	n	采集点编号	运营系统的公司采集点序号。	0
		        String body_105 = padRight(map.get("body_105").toString(),12);//105	12	n	终端机编号	POS机编号	M
		        String body_117 = padRight(map.get("body_117").toString(),12);//117	12	n	*PSAM卡号	PSAM卡终端机编号	M
		        String body_129 = padRight(map.get("body_129").toString(),9);//129	9	n	*PSAM卡流水号	PSAM卡交易唯一流水号	M
		        String body_138 = padRight(map.get("body_138").toString(),1);//138	1	n	锁卡交易标志	0为正常交易；1为锁卡交易。统一为0，但发现黑名单卡要进行锁卡。	M
		        String body_139 = padRight(map.get("body_139").toString(),8);//139	8	H	终端交易序号	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指：POS交易流水号	M
		        String body_147 = padRight(map.get("body_147").toString(),8);//147	8	n	*终端交易日期	格式为YYYYMMDD	M
		        String body_155 = padRight(map.get("body_155").toString(),6);//155	6	n	*终端交易时间	格式为hhmmss	M
		        String body_161 = padRight(map.get("body_161").toString(),8);//161	8	H	*交易验证代码（TAC）	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
		        String body_169 = padRight(map.get("body_169").toString(),2);//169	2	H	消费密钥版本号	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
		        String body_171 = padRight(map.get("body_171").toString(),2);//171	2	H	消费密钥索引	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
		        String body_173 = padRight(map.get("body_173").toString(),4);//173	4	H	卡片脱机交易序列号	用4个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指卡消费计数器。	M
		        String body_177 = padRight(map.get("body_177").toString(),16);//177	16	n	发卡机构标识	卡主账号前八位	M
		        String body_193 = padRight(map.get("body_193").toString(),4);//193	4	n	*城市代码（交易地）	扬州城市代码：3120	M
		        String body_197 = padRight(map.get("body_197").toString(),4);//197	4	n	*城市代码（卡属地）	卡片读取	M
		        String body_201 = padRight(map.get("body_201").toString(),8);//201	8	n	运营单位代码	商户编号	M
		        String body_209 = padRight("",20);//209	20	ans	所属线路	线路编号(公交必备)	0
		        String body_229 = padRight("",8);//229	8	n	交易柜台编号	公交为：公交车辆编号	M
		        String body_237 = padRight("",8);//237	8	n	司机卡号	司机卡面号(公交必备)	0
		        String body_245 = padRight("",14);//245	14	n	司机上班时间	上班时间:YYYYMMDDhhmmss(公交必备)	0
		        String body_259 = "07";//259	2	  n	行业代码	01-公交，02-出租，03-地铁，04-有轨电车，05-公共自行车，06-轮渡，07-小额消费，08-停车场	M
		        String body_261 = padRight("",1);//261	1	n	消费类型	0 普通 2 本地赠送钱包 8换乘(公交必备)	0
		        String body_262 = padRight("",14);//262	14	n	采集时间	YYYYMMDDhhmmss必须是北京时间(公交必备)	0
		        String body_276 = "0";//276	1	n	测试标志	0为正式数据；1为测试数据	M
		        String body_277 = padRight("",8);//277	8	H	伪随机数	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	O
		        String body_285 = padRight("",30);//285	30	ans	保留使用		O
		        //String body_315 = "";//315	1	s	回车符	回车符’\n’	M
		        ret = body_0 + body_12+ body_32 + body_51 + body_67 + body_70 + body_71 + body_79 + body_87 + body_95 + body_97 + body_105 + body_117 + body_129 + body_138 + body_139 + body_147 + body_155 + body_161 + body_169 + body_171 + body_173 + body_177 + body_193 + body_197 + body_201 + body_209 + body_229 + body_237 + body_245 + body_259 + body_261 + body_262 + body_276 + body_277 + body_285 ;
			}
			catch(Exception e)
			{
				Log.d(tag,"getBody err:" +  e.getMessage() + "," + e.getStackTrace());
				ret = "getBody err:" +  e.getMessage() + "," + e.getStackTrace();
			}
		}
		return ret;
	} 
	public String getyydwcode()
	{
		 String ret ="123456789000";
		 Map map = mapList1.get(0);
		 if(!map.get("body_201").toString().equals(""))
		 {
			 ret = map.get("body_201").toString();
		 }
		 ret = padLeftZero(ret,12);
		return ret;
	
	}
	public String padRight(String src, int len)
	{
		return CommonUtil.padRight(src, len);
	}
	
	public String padLeftZero(String src, int len)
	{
		return CommonUtil.padLeftZero(src, len);
	}
}

