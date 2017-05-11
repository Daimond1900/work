package com.winksoft.yzsmk.czpos.queryvalue;

import java.util.Map;

import com.winksoft.yzsmk.czpos.base.BaseActivity;
import com.winksoft.yzsmk.db.SqliteUtil;
import com.winksoft.yzsmk.utils.SettingDataUtil;

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
	private String ipNum, remotePath, accpetCusNo;

	public QueryValues() {
		sqliteUtil = SqliteUtil.getInstance(this);
		jobNumber = sqliteUtil.doQueryJobNumber();
		valuesMap = SettingDataUtil.getPaySettingInfo("/czSettingInfo.txt");

		if (!valuesMap.isEmpty()) {
			terminalNum = valuesMap.get("terminalNum");
			ip = valuesMap.get("ip");
			port = valuesMap.get("port");
			username = valuesMap.get("username");
			password = valuesMap.get("password");
			ipNum = valuesMap.get("ipNum");
			remotePath = valuesMap.get("remotePath");
			accpetCusNo = valuesMap.get("accpetCusNo");
		}
	}

	public String getPSAMNoByType(String type, String cName) {
		return sqliteUtil.daQueryPSAMNo(type, cName);
	}

	public String getIp() {
		return ip;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getAccpetCusNo() {
		return accpetCusNo;
	}

	//
	public String getIpNum() {
		return ipNum;
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
