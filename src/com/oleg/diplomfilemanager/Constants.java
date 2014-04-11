package com.oleg.diplomfilemanager;

import android.os.Environment;

public class Constants {

	public static final String SD_CARD = Environment
			.getExternalStorageDirectory().getPath();
	public static final String ROOT = "/";
	public static final String STORAGE_ID = "sterage_id";
	public static final int YANDEX_DISK_STORAGE = 1;
	public static final int SD_CARD_STORAGE = 2;
	public static final int INTEGRAL_STORAGE = 3;
	public static final int YANDEX_DISK_LOADER = 4;
	public static final String DISP_DIR = "displayed_directory";
	public static String USERNAME = "auth.username";
	public static String TOKEN = "auth.token";
	public static final String CLIENT_ID = "36c90d0661b644998b7d36afcee167c5";
	public static final String CLIENT_SECRET = "45693810a541466e9dbb559194c0eed2";
	public static final String ACCOUNT_TYPE = "com.yandex";
	public static final String AUTH_URL = "https://oauth.yandex.ru/authorize?response_type=token&client_id="
			+ CLIENT_ID;

}
