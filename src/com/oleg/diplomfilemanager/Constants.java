package com.oleg.diplomfilemanager;

import android.os.Environment;

public class Constants {

	public static final String SD_CARD = Environment
			.getExternalStorageDirectory().getPath();
	public static final String ROOT = "/";
	public static final String STORAGE = "/storage";
	public static final String STORAGE_ID = "sterage_id";
	public static final int YANDEX_DISK_STORAGE = 1;
	public static final int SD_CARD_STORAGE = 2;
	public static final int INTEGRAL_STORAGE = 3;
	public static final int YANDEX_DISK_LOADER = 4;
	public static final int CUT = 100;
	public static final int PHONE_STORAGE_COPY = 101;
	public static final int PASTE = 102;
	public static final int DELETE = 103;
	public static final int RENAME = 104;
	public static final int PHONE_STORAGE_PROPERTIES = 105;
	public static final int PHONE_STORAGE_SETTINGS = 106;
	public static final int PHONE_STORAGE_CREATE_FILE_DIR = 107;
	public static final int PHONE_STORAGE_HOME = 108;
	public static final int REFRESH = 109;
	public static final int PHONE_STORAGE_SEARCH = 110;
	public static final int PHONE_STORAGE_CREATE_FILE = 111;
	public static final int DOWNLOAD = 111;
	public static final int MOVE = 112;
	public static final int SEND_YANDEX_DISK = 113;
	public static final String PREFERENCES_KEY_CUT = "cut";
	public static final String PREFERENCES_KEY_COPY = "copy";
	public static final String DISP_DIR = "displayed_directory";
	public static final String USERNAME = "auth.username";
	public static final String TOKEN = "auth.token";
	public static final String CLIENT_ID = "36c90d0661b644998b7d36afcee167c5";
	public static final String CLIENT_SECRET = "45693810a541466e9dbb559194c0eed2";
	public static final String ACCOUNT_TYPE = "com.yandex";
	public static final String AUTH_URL = "https://oauth.yandex.ru/authorize?response_type=token&client_id="
			+ CLIENT_ID;
	public static final int PROGRESS = 1024 * 1024;
	
	
}
