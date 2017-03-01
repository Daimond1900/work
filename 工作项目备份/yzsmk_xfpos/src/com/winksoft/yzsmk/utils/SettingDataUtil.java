package com.winksoft.yzsmk.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

public class SettingDataUtil {
	public static boolean saveSetting(String filename,Map<String,String> map) {
		boolean flage = false;

		BufferedWriter bw = null;

		if (Config.BLIST_DIR != null) {
			try {
				File file = new File(Config.BLIST_DIR + filename);
				if (!file.exists()) {
					file.createNewFile();
				}

				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				bw.write("terminalNum=" + map.get("terminalNum")); //
				bw.newLine();
				bw.write("ip=" + map.get("ip"));
				bw.newLine();
				bw.write("port=" + map.get("port"));
				bw.newLine();
				bw.write("username=" + map.get("username"));
				bw.newLine();
				bw.write("password=" + map.get("password"));
				bw.newLine();
				bw.write("remotePath=" + map.get("remotePath"));
				bw.newLine();
				bw.write("desfireIp=" + map.get("desfireIp"));
				bw.newLine();
				bw.write("desfirePort=" + map.get("desfirePort"));
				flage = true;
			} catch (Exception e) {
				e.printStackTrace();
				flage = false;
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return flage;
	}
	
	public static Map<String, String> getPaySettingInfo(String filename) {

		FileInputStream fis = null;
		Map<String, String> map = new HashMap<String, String>();

		if (Config.BLIST_DIR != null) {
			File file = new File(Config.BLIST_DIR + filename);

			if (file != null) {
				try {
					fis = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					String str = null;
					while ((str = br.readLine()) != null) {
						String[] split = str.split("=");
						map.put(split[0], split[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return map;
	}
}
