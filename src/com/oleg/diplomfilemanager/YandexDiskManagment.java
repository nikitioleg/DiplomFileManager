package com.oleg.diplomfilemanager;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.WebdavException;

public class YandexDiskManagment {

	private static final String TAG = "YandexDiskManagment";
	private Context context;
	private static YandexDiskManagment instance;
	//private SharedPreferences preferences;
		
	public YandexDiskManagment(Context context) {
		this.context = context;
	}
	
	public static void initInstance(Context context) {
		if (instance == null) {
			instance = new YandexDiskManagment(context);
		}
	}

	public static synchronized YandexDiskManagment getInstance() {
		return instance;
	}

	private Credentials getCredentials() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String username = preferences.getString(Constants.USERNAME, null);
		String token = preferences.getString(Constants.TOKEN, null);
		return new Credentials(username, token);
	}

	public void openYandexDiskFile() {

	}

	public void deleteYandexDiskFile(FileInfoItem fileInfoItem) {
		TransportClient client = null;
		try {
			client = TransportClient.getInstance(context, getCredentials());
			client.delete(fileInfoItem.getFullPath());
		} catch (IOException ex) {
			Log.d(TAG, "deleteItem", ex);
			
		} catch (WebdavException ex) {
			Log.d(TAG, "deleteItem", ex);

		} finally {
			TransportClient.shutdown(client);
		}
		return ;
	}

	public void renameYandexDiskFile() {

	}

}
