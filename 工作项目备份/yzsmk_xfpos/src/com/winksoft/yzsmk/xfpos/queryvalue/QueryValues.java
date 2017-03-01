package com.winksoft.yzsmk.xfpos.queryvalue;

import java.util.Map;

import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.utils.SettingDataUtil;
import com.winksoft.yzsmk.xfpos.base.BaseActivity;

/**
 * 数据提供类 terminalNum 终端号 ip ip地址 port 端口号 username 用户名 password 密码 jobNumber 工号
 * 无数据时，get出来的结果为 "";
 * 
 * @author wg
 */
public class QueryValues extends BaseActivity {
	private Map<String, String> valuesMap;
	private SqliteUtil sqliteUtil;
	private String terminalNum;
	private String ip;
	private String port;
	private String username;
	private String password;
	private String jobNumber;
	private String remotePath,desfireIP,desfirePort;

	public QueryValues(int i) {
		sqliteUtil = SqliteUtil.getInstance(this);
		jobNumber = sqliteUtil.doQueryJobNumber();
		if (i == 1) {
			valuesMap = SettingDataUtil.getPaySettingInfo("/ftpSettingInfo.txt");
		}

		if (!valuesMap.isEmpty()) {
			terminalNum = valuesMap.get("terminalNum");
			ip = valuesMap.get("ip");
			port = valuesMap.get("port");
			username = valuesMap.get("username");
			password = valuesMap.get("password");
			remotePath = valuesMap.get("remotePath");
			desfireIP = valuesMap.get("desfireIp");
			desfirePort = valuesMap.get("desfirePort");
		}
	}

	public String getPSAMNoByType(String type, String cName) {
		return sqliteUtil.daQueryPSAMNo(type, cName);
	}
	public String getDesfireIP() {
		return desfireIP;
	}
	public String getDesfirePort() {
		return desfirePort;
	}
	public String getIp() {
		return ip;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getTerminalNum() {
		return terminalNum;
	}

	public String getUsername() {
		return username;
	}
}
