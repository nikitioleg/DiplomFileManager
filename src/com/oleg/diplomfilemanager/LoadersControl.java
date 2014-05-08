package com.oleg.diplomfilemanager;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import com.oleg.diplomfilemanager.fragments.YandexDiskOverviewFragment;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class LoadersControl implements
		LoaderManager.LoaderCallbacks<ArrayList<FileInfoItem>> {

	//private static LoadersControl instance;
	private Context context;
	private YandexDiskOverviewFragment yandexDiskOverviewFragment;

	public LoadersControl(Context context,
			YandexDiskOverviewFragment yandexDiskOverviewFragment) {
		this.context = context;
		this.yandexDiskOverviewFragment = yandexDiskOverviewFragment;
	}

	// public static void initInstance(Context context,
	// YandexDiskOverviewFragment yandexDiskOverviewFragment) {
	// if (instance == null) {
	// instance = new LoadersControl(context, yandexDiskOverviewFragment);
	// }
	// }
	//
	// public static synchronized LoadersControl getInstance() {
	// return instance;
	// }

	private Credentials getCredentials() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String username = preferences.getString(Constants.USERNAME, null);
		String token = preferences.getString(Constants.TOKEN, null);
		return new Credentials(username, token);
	}

	@Override
	public Loader<ArrayList<FileInfoItem>> onCreateLoader(int id, Bundle args) {
		Log.d("myLog", "onCreateLoader");
		if (id == Constants.YANDEX_DISK_LOADER) {
			return new YandexDiskLoader(context, getCredentials(),
					yandexDiskOverviewFragment.getArguments().getString(
							Constants.DISP_DIR));
		} else {
			return null;
		}

	}

	@Override
	public void onLoadFinished(Loader<ArrayList<FileInfoItem>> loader,
			ArrayList<FileInfoItem> data) {

		Log.d("myLog", "onLoadFinished");
		loader.reset();
		yandexDiskOverviewFragment.updateList(data);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<FileInfoItem>> loader) {
		Log.d("myLog", "onLoaderReset");
	}

}
