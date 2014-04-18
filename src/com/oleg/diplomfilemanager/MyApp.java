package com.oleg.diplomfilemanager;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		FileManagment.initInstance(this);
		FileManagment.getInstance()
				.setCerrentStorage(Constants.SD_CARD_STORAGE);
	}

}
