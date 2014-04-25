package com.oleg.diplomfilemanager;

import android.app.Application;

public class MyApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		FileManagment.initInstance(this);
		FileManagment.getInstance()
				.setCerrentStorage(Constants.SD_CARD_STORAGE);
		YandexDiskManagment.initInstance(getApplicationContext());
	}

}
