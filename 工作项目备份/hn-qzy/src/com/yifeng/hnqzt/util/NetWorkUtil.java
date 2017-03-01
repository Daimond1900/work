package com.yifeng.hnqzt.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class NetWorkUtil {
	private TelephonyManager tm;
	private CdmaCellLocation location = null;
	private String longitude="0",latitude="0",pos="";
	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getPos() {
		return pos;
	}

	private Activity activity;
	public NetWorkUtil(Activity activity){
		this.activity=activity;
		tm = (TelephonyManager) this.activity.getSystemService(Context.TELEPHONY_SERVICE);
		
	}
	
	/**
	 * 加载所在位置数据
	 */
	public void loadData(){
		int type = tm.getNetworkType();
		//在中国，移动的2G是EGDE，联通的2G为GPRS，电信的2G为CDMA，电信的3G为EVDO 
		//String OperatorName = tm.getNetworkOperatorName(); 
		Location loc = null;
		ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
		//中国电信为CTC
		//NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
		//NETWORK_TYPE_CDMA电信2G是CDMA
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type ==TelephonyManager.NETWORK_TYPE_1xRTT)
		{
			location = (CdmaCellLocation) tm.getCellLocation();
			int cellIDs = location.getBaseStationId();
			int networkID = location.getNetworkId();
			StringBuilder nsb = new StringBuilder();
			nsb.append(location.getSystemId());
            CellIDInfo info = new CellIDInfo();
            info.cellId = cellIDs;
            info.locationAreaCode = networkID; //ok
            info.mobileNetworkCode = nsb.toString();
            info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
            info.radioType = "cdma";
            CellID.add(info);
		}
		//移动2G卡 + CMCC + 2 
		//type = NETWORK_TYPE_EDGE
		else if(type == TelephonyManager.NETWORK_TYPE_EDGE)
		{
			GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
			int cellIDs = location.getCid();  
			int lac = location.getLac(); 
			CellIDInfo info = new CellIDInfo();
            info.cellId = cellIDs;
            info.locationAreaCode = lac;
            info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);   
            info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
            info.radioType = "gsm";
            CellID.add(info);
		}
		//联通的2G经过测试 China Unicom   1 NETWORK_TYPE_GPRS
		else if(type == TelephonyManager.NETWORK_TYPE_GPRS)
		{
			GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
			int cellIDs = location.getCid();  
			int lac = location.getLac(); 
			CellIDInfo info = new CellIDInfo();
            info.cellId = cellIDs;
            info.locationAreaCode = lac;
            //经过测试，获取联通数据以下两行必须去掉，否则会出现错误，错误类型为JSON Parsing Error
            //info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);   
            //info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
            info.radioType = "gsm";
            CellID.add(info);
		}
		else
		{
			//textMsg.setText("Current Not Support This Type.");
		}
		
		loc = callGear(CellID);
		
		if(loc != null){
			try {
				pos = getLocation(loc);//获得所在地与邮编
			} catch (Exception e) {
				e.printStackTrace();
			}
			StringBuffer bf=new StringBuffer();
			
			longitude = Double.toString(loc.getLongitude());//经度
			latitude = Double.toString(loc.getLatitude());//纬度
			if(!pos.equals("") && !pos.equals(null)){
				bf.append(pos);
				
			}else {
				//address_info.setText("网络故障,无法获取地理位置!");
				//textMsg.setText(desc);
			}
			if(!longitude.equals("") && !longitude.equals(null)){
				bf.append("==="+longitude+"===");
			}else {
				//coord_x.setText("网络故障,无法获取当前经度!");
				//textMsg.setText("0");
			}
			if(!latitude.equals("") && !latitude.equals(null)){
				
				bf.append("==="+latitude+"===");
			}else {
				//coord_y.setText("网络故障,无法获取当前纬度!");
				//textMsg.setText("0");
			}
			//textMsg.setText(bf.toString());
			
		} else {
			//coord_x.setText("网络故障,无法获取当前经度!");
			//coord_y.setText("网络故障,无法获取当前纬度!");
			//address_info.setText("网络故障,无法获取地理位置!");
			//textMsg.setText("");
		}
	}
	
	private Location callGear(ArrayList<CellIDInfo> cellID) {
    	if (cellID == null) return null;
    	DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code", cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code", cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode)) 
				holder.put("address_language", "zh_CN");
			else
				holder.put("address_language", "en_US");
			JSONObject data,current_data;
			JSONArray array = new JSONArray();
			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("location_area_code", cellID.get(0).locationAreaCode);
			current_data.put("mobile_country_code", cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code", cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			array.put(current_data);
			if (cellID.size() > 2) {
				for (int i = 1; i < cellID.size(); i++) {
					data = new JSONObject();
					data.put("cell_id", cellID.get(i).cellId);
					data.put("location_area_code", cellID.get(i).locationAreaCode);
					data.put("mobile_country_code", cellID.get(i).mobileCountryCode);
					data.put("mobile_network_code", cellID.get(i).mobileNetworkCode);
					data.put("age", 0);
					array.put(data);
				}
			}
			holder.put("cell_towers", array);
			StringEntity se = new StringEntity(holder.toString());
			Log.e("Location send", holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);
			HttpEntity entity = resp.getEntity();

			BufferedReader br = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				Log.e("Locaiton receive", result);
				sb.append(result);
				result = br.readLine();
			}
			if(sb.length() <= 1)
				return null;
			data = new JSONObject(sb.toString());
			data = (JSONObject) data.get("location");

			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			loc.setTime(GetUTCTime());
			return loc;
		} catch (JSONException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * 获取地理位置
	 * 
	 * @throws Exception
	 */
	private String getLocation(Location itude) throws Exception {
		String resultString = "";
		/** 这里采用get方法，直接将参数加到URL上 */
		String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.getLatitude(), itude.getLongitude());
		Log.i("URL", urlString);
		/** 新建HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** 采用GET方法 */
		HttpGet get = new HttpGet(urlString);
		try {
			/** 发起GET请求并获得返回数据 */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			/** 解析JSON数据，获得物理地址 */
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString("address");
				}
			}
		} catch (Exception e) {
			throw new Exception("获取物理位置出现错误:" + e.getMessage());
		} finally {
			get.abort();
			client = null;
		}
		return resultString;
	}
    
    public long GetUTCTime() { 
        Calendar cal = Calendar.getInstance(Locale.CHINA); 
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET); 
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET); 
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset)); 
        return cal.getTimeInMillis();
    }
	
	public class CellIDInfo {
			public int cellId;
			public String mobileCountryCode;
			public String mobileNetworkCode;
			public int locationAreaCode;
			public String radioType;
			public CellIDInfo(){}
	}
}