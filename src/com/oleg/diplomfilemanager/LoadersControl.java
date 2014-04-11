package com.oleg.diplomfilemanager;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

public class LoadersControl implements
		LoaderManager.LoaderCallbacks<ArrayList<FileInfoItem>> {

	private static LoadersControl instance;
	private Context context;
	private SystemOverviewFragment systemOverview;

	public LoadersControl(Context context, SystemOverviewFragment systemOverview) {
		this.context = context;
		this.systemOverview = systemOverview;
	}
	
	public static void initInstance(Context context, SystemOverviewFragment systemOverview) {
		if (instance == null) {
			instance = new LoadersControl(context,systemOverview);
		}
	}

	public static synchronized LoadersControl getInstance() {
		return instance;
	}
	
	@Override
	public Loader<ArrayList<FileInfoItem>> onCreateLoader(int id, Bundle args) {
		Log.d("myLog", "onCreateLoader");
		if (id == Constants.YANDEX_DISK_LOADER) {
			return new YandexDiskLoader(context, getCredentials(),
					systemOverview.getCurrentDir());
		} else {
			return null;
		}

	}

	private Credentials getCredentials() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String username = preferences.getString(Constants.USERNAME, null);
		String token = preferences.getString(Constants.TOKEN, null);
		return new Credentials(username, token);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<FileInfoItem>> loader,
			ArrayList<FileInfoItem> data) {
		
		Log.d("myLog", "onLoadFinished");
		loader.reset();
		systemOverview.setCurrentDirFileInfoItems(data);
		systemOverview.updateList(data);// обновить коллекцию с которой работает
										// обработчик нажатий
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<FileInfoItem>> loader) {
		Log.d("myLog", "onLoaderReset");
	}

}
