package com.winksoft.android.yzjycy.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.entity.MenuItem;
import android.content.Context;
import android.util.Log;

public class MainManage {
	private Context context;

	public MainManage(Context context) {
		this.context = context;
	}

	public static Map<String, Object> maps;
	/** 已选菜单 */
	public static List<MenuItem> defaultUserMenus;
	/** 就业菜单 */
	public static List<MenuItem> jyMenus;
	/** 培训信息 */
	public static List<MenuItem> pxMenus;
	/** 创业菜单 */
	public static List<MenuItem> cyMenus;
	/** 资讯菜单 */
	public static List<MenuItem> zxMenus;

	static {
		defaultUserMenus = new ArrayList<MenuItem>();
		jyMenus = new ArrayList<MenuItem>();
		cyMenus = new ArrayList<MenuItem>();
		zxMenus = new ArrayList<MenuItem>();
		pxMenus = new ArrayList<MenuItem>();		// 新加的培训信息

		jyMenus.add(new MenuItem(1, "招聘信息", 0, 1, "jy_zpxx", 1));
		jyMenus.add(new MenuItem(2, "求职信息", 0, 0, "jy_qzxx", 1));
		jyMenus.add(new MenuItem(3, "我的简历", 0, 0, "jy_wdjl", 1));
		jyMenus.add(new MenuItem(4, "我的求职", 0, 0, "jy_wdqz", 1));
		jyMenus.add(new MenuItem(5, "企业信息", 0, 0, "jy_qyxx", 1));
		jyMenus.add(new MenuItem(6, "招聘发布", 0, 0, "jy_zpfb", 1));
		jyMenus.add(new MenuItem(7, "招聘管理", 0, 0, "jy_zpgl", 1));
		jyMenus.add(new MenuItem(8, "报名信息", 0, 0, "jy_bmxx", 1));
		jyMenus.add(new MenuItem(9, "录用查询", 0, 0, "jy_lycx", 1));
		jyMenus.add(new MenuItem(10, "劳动力信息", 0, 0, "jy_ldlxx", 1));
		jyMenus.add(new MenuItem(11, "零转移", 0, 0, "jy_lzy", 1));
		jyMenus.add(new MenuItem(12, "每日一新", 0, 0, "jy_mryx", 1));
		jyMenus.add(new MenuItem(13, "通讯录", 0, 0, "jy_txl", 1));
		jyMenus.add(new MenuItem(14, "就业热线", 0, 1, "jy_jyrx", 1));

		cyMenus.add(new MenuItem(15, "创业政策", 0, 0, "cy_kbxx", 2));
		cyMenus.add(new MenuItem(16, "服务指南", 0, 0, "cy_zn", 2));
		cyMenus.add(new MenuItem(17, "创业培训", 0, 0, "cy_pxkc", 2));
		cyMenus.add(new MenuItem(18, "创业咨询", 0, 1, "cy_pxjg", 2));
		cyMenus.add(new MenuItem(19, "创业项目库", 0, 0, "cy_xmk", 2));
		cyMenus.add(new MenuItem(20, "项目征集", 0, 0, "cy_xmzj", 2));
		cyMenus.add(new MenuItem(21, "创业典型", 0, 0, "cy_cydx", 2));
		cyMenus.add(new MenuItem(22, "创业服务圈", 0, 0, "cy_fw", 2));
		cyMenus.add(new MenuItem(23, "孵化基地", 0, 1, "cy_fhjd", 2));
		cyMenus.add(new MenuItem(24, "指导专家", 0, 1, "cy_zdzj", 2));

		zxMenus.add(new MenuItem(25, "政策法规", 0, 1, "zx_zcfg", 3));
		zxMenus.add(new MenuItem(26, "就业新闻", 0, 1, "zx_jyxw", 3));
		/*zxMenus.add(new MenuItem(27, "培训信息", 0, 0, "zx_pxxx", 3));*/
		
		pxMenus.add(new MenuItem(28, "培训信息", 0, 0, "zx_pxxx", 4));
		pxMenus.add(new MenuItem(29, "考勤信息", 0, 0, "jy_bmxx", 4));
	}

	/**
	 * 清除所有的菜单
	 */
	public void deleteAllChannel() {
		DBUtil.getInstance(context).clearFeedTable();
	}

	/**
	 * 初始化插入数据
	 */
	public void initMenus() {
		List<MenuItem> items = getAllChannel();
		if (items.isEmpty()) {
			initDefaultChannel();
		}
	}

	/**
	 * 获取首页菜单
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getMainMenu() {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> cacheList = DBUtil.getInstance(context).listCache(SQLHelper.SELECTED + "= ?",
				new String[] { "1" }, SQLHelper.ORDERID + " asc", "0,7");
		if (cacheList != null && !cacheList.isEmpty()) {
			for (int i = 0; i < cacheList.size(); i++) {
				Map<String, String> map = cacheList.get(i);
				Map<String, Object> map1 = new HashMap<String, Object>();
				for (String key : map.keySet()) {
					map1.put(key, map.get(key));
				}
				map1.put(SQLHelper.COUNT, "0");
				mapList.add(map1);
			}
		}
		return mapList;
	}

	private List<Map<String, Object>> defaultList() {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < defaultUserMenus.size(); i++) {
			MenuItem navigate = defaultUserMenus.get(i);
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put(SQLHelper.ID, navigate.getId());
			map1.put(SQLHelper.NAME, navigate.getName());
			map1.put(SQLHelper.RESNAME, navigate.getResName());
			map1.put(SQLHelper.GROUPID, navigate.getGroupId());
			map1.put(SQLHelper.COUNT, "0");
			mapList.add(map1);
		}
		return mapList;
	}

	/**
	 * 获取用户菜单
	 * 
	 * @return
	 */
	public List<MenuItem> getUserChannel() {
		List<Map<String, String>> cacheList = DBUtil.getInstance(context).listCache(SQLHelper.SELECTED + "= ?",
				new String[] { "1" }, SQLHelper.ORDERID + " asc", null);
		List<MenuItem> list = new ArrayList<MenuItem>();
		if (cacheList != null && !cacheList.isEmpty()) {
			for (int i = 0; i < cacheList.size(); i++) {
				MenuItem item = new MenuItem();
				item.setId(Integer.valueOf(cacheList.get(i).get(SQLHelper.ID)));
				item.setName(cacheList.get(i).get(SQLHelper.NAME));
				item.setOrderId(Integer.valueOf(cacheList.get(i).get(SQLHelper.ORDERID)));
				item.setSelected(Integer.valueOf(cacheList.get(i).get(SQLHelper.SELECTED)));
				item.setResName(cacheList.get(i).get(SQLHelper.RESNAME));
				item.setGroupId(Integer.valueOf(cacheList.get(i).get(SQLHelper.GROUPID)));
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 获取全部菜单
	 * 
	 * @return
	 */
	public List<MenuItem> getAllChannel() {
		List<Map<String, String>> cacheList = DBUtil.getInstance(context).listCache(null, null, null, null);
		List<MenuItem> list = new ArrayList<MenuItem>();
		if (cacheList != null && !cacheList.isEmpty()) {
			for (int i = 0; i < cacheList.size(); i++) {
				MenuItem item = new MenuItem();
				item.setId(Integer.valueOf(cacheList.get(i).get(SQLHelper.ID)));
				item.setName(cacheList.get(i).get(SQLHelper.NAME));
				item.setOrderId(Integer.valueOf(cacheList.get(i).get(SQLHelper.ORDERID)));
				item.setSelected(Integer.valueOf(cacheList.get(i).get(SQLHelper.SELECTED)));
				item.setResName(cacheList.get(i).get(SQLHelper.RESNAME));
				item.setGroupId(Integer.valueOf(cacheList.get(i).get(SQLHelper.GROUPID)));
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 保存用户菜单到数据库
	 * 
	 * @param userList
	 */
	public void saveChannel(List<MenuItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			MenuItem channelItem = (MenuItem) userList.get(i);
			DBUtil.getInstance(context).addCache(channelItem);
		}
	}

	/**
	 * 初始化数据库内的菜单数据
	 */
	private synchronized void initDefaultChannel() {
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		// saveChannel(defaultUserMenus);
		saveChannel(jyMenus);
		saveChannel(cyMenus);
		saveChannel(zxMenus);
		saveChannel(pxMenus);
	}
}