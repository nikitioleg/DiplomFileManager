package com.oleg.diplomfilemanager;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.oleg.diplomfilemanager.dialogs.UploadYandexDiskDialog;
import com.oleg.diplomfilemanager.dialogs.YandexDiskDownloadDialog;
import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ProgressListener;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.WebdavException;

public class YandexDiskManagment {

	private static final String TAG = "YandexDiskManagment";
	private Context context;
	private static YandexDiskManagment instance;

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

	public void downloadYandexDiskFile(FileInfoItem fileInfoItem,
			YandexDiskDownloadDialog yandexDiskDownloadDialog) {
		TransportClient client = null;
		File result = new File("/storage/sdcard0/Download" + "/"
				+ fileInfoItem.getDisplayName());
		try {
			client = TransportClient.getInstance(context, getCredentials());
			client.downloadFile(fileInfoItem.getFullPath(), result,
					yandexDiskDownloadDialog);
		} catch (IOException ex) {
			Log.d(TAG, "loadFile", ex);
		} catch (WebdavException ex) {
			Log.d(TAG, "loadFile", ex);
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
	}

	public void uploadFileToYandexDisk(FileInfoItem fileInfoItem,
			UploadYandexDiskDialog uploadYandexDiskDialog) {
		TransportClient client = null;
		try {
			client = TransportClient.getInstance(context, getCredentials());
			client.uploadFile(fileInfoItem.getFullPath(), "/"
					, uploadYandexDiskDialog);
		} catch (IOException ex) {
			Log.d(TAG, "uploadItem", ex);
		} catch (WebdavException ex) {
			Log.d(TAG, "uploadItem", ex);
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}
		return;
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
		return;
	}

	public void moveYandexDiskFile(String srcPath, String dstPath) {
		TransportClient client = null;
		try {
			client = TransportClient.getInstance(context, getCredentials());
			client.move(srcPath, dstPath);
		} catch (IOException ex) {
			Log.d(TAG, "renameMoveItem", ex);
		} catch (WebdavException ex) {
			Log.d(TAG, "renameMoveItem", ex);

		} finally {
			TransportClient.shutdown(client);
		}
		return;
	}

}
