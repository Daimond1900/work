package com.yifeng.hnjcy.contacts.tool;

import android.net.Uri;
import android.os.Environment;

public class SystemConstant
{	//public static final String	cKey							= "G6O5q0gU6esASH5";
	public static final String	cKey							= "E50a67bC1394D2F8";																	// 加密用的Key
	public static final String	PWD								= "57530b4a35494e3651f72cb83bdb0a67";
	public static String		RESTART_LOGIN_ACTION			= "com.dossysoft.guagua.restart";
	public static String		SERVICE_CLOSE			= "com.yifeng.close";
	public static String		RESTART_REGISTER_ACTION			= "com.dossysoft.guagua.register";
	public static final String	TOTAL_MSG_NUM					= "TOTAL_MSG_NUM";
	public static final String	TOTAL_FREE_MSG_NUM				= "TOTAL_FREE_MSG_NUM";
	public static final String	RECORD_MONTH					= "RECORD_MONTH";
	public static final String	MSG_NUM_IN_MONTH				= "MSG_NUM_IN_MONTH";
	public static final String	FREE_MSG_NUM_IN_MONTH			= "FREE_MSG_NUM_IN_MONTH";

	public static final String	MSG_TYPE_CMD					= "dossy.cmd";
	public static final String	GROUP_NAME						= "friend";
	public static final String	MSG_TYPE_CMD_REQUEST_LOCATION	= "dossy.location.request";
	public static final String	MSG_TYPE_CMD_ACCEPT_LOCATION	= "dossy.location.accept";
	public static final String	MSG_TYPE_CMD_REJECT_LOCATION	= "dossy.location.reject";
	public static final String	MSG_TYPE_CMD_LOCATION			= "dossy.location.location";
	public static final String	MSG_TYPE_CMD_LOCATION_STOP		= "dossy.location.stop";
	public static final String	MSG_TYPE_CMD_PING_REQ			= "ping_req";
	public static final String	MSG_TYPE_CMD_PING_RESP			= "ping_resp";
	public static final String	ZTL_TZ							= "ZTL_TZ";
	public static final String	ZTL_YL							= "ZTL_YL";
	public static final String	TC_TZ							= "TC_TZ";
	public static final String	TC_YL							= "TC_YL";
	public static final String	TX_ZD							= "TX_ZD";
	public static final String	TX_SY							= "TX_SY";
	public static final String	PRO								= "PRO";
	public static final String	VERSION							= "VERSION";
	public static final String	IS_NEW_VERSION					= "NEW_VERSION";
	public static final String	DEFULT_TYPE_RING				= "2";
	public static final String	SEARCH_SERVICE					= "SEARCH_SERVICE";
	public static final String	LOCAL_ADDR						= "LOCAL_ADDR";
	public static final String	STATUS_BAR_NOTIFY				= "STATUS_BAR_NOTIFY";
	public static final String	STATUS_BAR_EXPECT				= "STATUS_BAR_EXPECT";
	public static final String	REMIND_JUMP_INFO				= "REMIND_JUMP_INFO";
	public static final String	REMIND_REMIND_SHOCK				= "REMIND_REMIND_SHOCK";
	public static final String	REMIND_REMIND_AUDIO				= "REMIND_REMIND_AUDIO";
	public static final String	REMIND_RING_SHOCK_FREQUENCY		= "RING_SHOCK_FREQUENCY";
	public static final String	DEFULT_RING_SHOCK_FREQUENCY		= "1";
	public static final String	RING_NAME						= "RING_NAME";
	public static final String	RING_PATH						= "RING_PATH";
	public static final String	RING_TYPE						= "RING_TYPE";
	public static final String	SKIN_PACKAGE_NAME				= "themePackage";
	public static final String	FRIEND_ADDRESS_LISTS			= "FRIEND_ADDRESS_LISTS";
	public final static String	GUAGUA							= "hnjcy";
	public static final String	BUBBLE_COLOR					= "BUBBLE_COLOR";
	public static final String	BUBBLE_SHAPE					= "BUBBLE_SHAPE";
	public static final String	FONT_SIZE						= "FONT_SIZE";
	public static final String	IMAGE_RESOLUTION				= "IMAGE_RESOLUTION";
	public static final String	TO_WEB							= "TO_WEB";
	public static final String	USER_HEAD						= "USER_HEAD";
	public static final String	APP_RINGS						= "APP_RINGS";
	public static final String	HEAD_IMAGE						= "head";
	public static final String	NAME							= "name";
	public static final String	SIGNATURE						= "signature";
	public static final String	TYPE_RINGS						= "type_rings";
	public static final String	MMS								= "彩信";
	public static final int		ISERROR							= -1;																					// 代表下载文件出错
	public static final int		SEUCESS							= 0;																					// 代表下载文件成功
	public static final int		FILEISEXIT						= 1;

	public static final int		MESSAGE_TYPE_ALL				= 0;
	public static final int		MESSAGE_TYPE_INBOX				= 1;
	public static final int		MESSAGE_TYPE_SENT				= 2;
	public static final int		MESSAGE_TYPE_DRAFT				= 3;
	public static final int		MESSAGE_TYPE_OUTBOX				= 4;
	public static final int		MESSAGE_TYPE_FAILED				= 5;																					// for
	public static final int		MESSAGE_TYPE_QUEUED				= 6;																					// for
	public static final int		MESSAGE_STATUS_RECEIVE			= -1;																					// 发送/接收中
	public static final int		MESSAGE_STATUS_COMPLETE			= 0;																					// 发送/接收成功
	public static final int		MESSAGE_STATUS_PENDING			= 64;																					// 发送/接收中
	public static final int		MESSAGE_STATUS_FAILED			= 128;																					// 发送/接收失败

	public static final int		PROTOCOL_SMS					= 0;
	public static final int		PROTOCOL_MMS					= 1;
	public static final int		PROTOCOL_OUR					= 2;
	public static final int		UNREAD							= 0;
	public static final int		READ							= 1;

	static final String			ATTACH_URL_FORMAT				= "[file/download";
	public static final String	PROTOCOL_OUR_HEAD				= "YF:";

	public static final int		MEDIARECORDER_PATH_NULL			= 1;
	public static final int		MEDIARECORDER_START_FETTLE		= 2;
	public static final int		MEDIARECORDER_START_DATA		= 3;
	public static final int		MEDIARECORDER_START				= 21;
	public static final int		MEDIARECORDER_STOP				= 22;
	public static final int		MEDIARECORDER_CLEAR				= 23;
	public static final int		MEDIARECORDER_SUCCESS_FILEPATH	= 5;
	public static final int		MEDIARECORDER_TIMEDATA			= 6;

	public static final String	SQL_CREATE_LOCATION_TABLE		= "CREATE TABLE IF NOT EXISTS tbl_location (id INTEGER PRIMARY KEY, status INTEGER)";
	public static final String	COL_NAME_ID						= "id";
	public static final String	COL_NAME_STATUS					= "status";
	public static final String	TBL_NAME						= "tbl_location";
	public static final int		STATUS_NOT_INIT					= 0;
	public static final int		STATUS_LOADING					= 1;
	public static final int		STATUS_OK						= 2;
	public static final int		STATUS_UPDATED					= 3;
	public static final Uri		CANONICAL_ADDRESS_URI			= Uri
																		.parse("content://mms-sms/canonical-addresses");
	public static final Uri		MMSSMS_FULL_CONVERSATION_URI	= Uri
																		.parse("content://mms-sms/conversations");
	public static final Uri		CONVERSATION_URI				= MMSSMS_FULL_CONVERSATION_URI
																		.buildUpon()
																		.appendQueryParameter(
																				"simple",
																				"true")
																		.build();
	public static final Uri		CONTENT_URI_SMS					= Uri
																		.parse("content://sms");
	public static final Uri		CONTENT_URI_MMS					= Uri
																		.parse("content://mms");														// mmssms.db中pdu表的uri
	public static final Uri		CONTENT_URI_PART				= Uri
																		.parse("content://mms/part");													// mmssms.db中part表的uri
	public static final String	TBL_MSG							= "tbl_msg";
	public static final String	COLNAME_TBL_MSG_ID				= "id";
	public static final String	COLNAME_TBL_MSG_PATH			= "path";
	public static final String	COLNAME_TBL_MSG_TYPE			= "type";
	public static final String	COLNAME_TBL_MSG_SIZE			= "size";
	public static final String	COLNAME_TBL_MSG_NAME			= "name";
	public static final String	COLNAME_TBL_MSG_TRANSFER_STATUS	= "transfer_status";
	public static final String	SQL_CREATE_MSG_TABLE			= "CREATE TABLE IF NOT EXISTS tbl_msg (id INTEGER PRIMARY KEY AUTOINCREMENT, path VARCHAR(100) "
																		+ ", name VARCHAR(50), type INTEGER, size INTEGER, transfer_status INTEGER)";
	public static final int		SELECT_PICTURE					= 1;																					// 选择图片
	public static final int		SELECT_TYPE_CAMERA				= 2;																					// 調用相機拍照
	public static final int		SELECT_TYPE_VIDEO				= 3;
	public static final String	ATTACHPATH						= Environment
																		.getExternalStorageDirectory()
																		+ "/"
																		+ SystemConstant.GUAGUA
																		+ "/download/";
	public static final String	IMAGE_CAPTURE					= Environment
																		.getExternalStorageDirectory()
																		+ "/"
																		+ SystemConstant.GUAGUA
																		+ "/";
	public static final String	SERVER_ADDRESS					= "file/upload";														// 调用系统录像
//	public static final String	SERVER_ADDRESS1					= "http://221.229.34.146:8421/yzdxjyy/";
	//	public static final String	SERVER_ADDRESS					= "http://192.168.0.202:8088/guaguaUpDown/upload";														// 调用系统录像
	public static final long	INTERVAL_FIVE_MINUTES			= 5 * 60 * 1000;
	public static final String	REPEAT_ALARM					= "REPEAT_ALARM";
	public static final String	AUTO_UPDATE_DAY					= "auto_update_day";
	public static final String	XML_UPDATE_DAY					= "xml_update_day";
	public static final String	XML_VERSION_TEXT				= "xmlversion";
	public static final int		CHUCK_SIZE						= 102400;																				// (100K)
	public static final String	TRY_LOGIN						= "TRY_LOGIN";
	public static final long	TEN_M							= 1024 * 1024 * 10;
}
