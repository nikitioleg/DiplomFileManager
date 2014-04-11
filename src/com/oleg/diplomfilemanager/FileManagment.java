package com.oleg.diplomfilemanager;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class FileManagment {

	private static FileManagment instance;
	private Context context;
	private SharedPreferences preferences;

	private FileManagment(Context context) {
		this.context = context;
	}

	public static void initInstance(Context context) {
		if (instance == null) {
			instance = new FileManagment(context);
		}
	}

	public static synchronized FileManagment getInstance() {
		return instance;
	}

	private final String LOG_TAG = "myLogs";
	private final int KB = 1024;
	private final int MB = KB * KB;
	private final int GB = MB * KB;

	private FileInfoItem.Builder builder;

	public void setCerrentStorage(int storageID) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		preferences.edit().putInt(Constants.STORAGE_ID, storageID).commit();
	}
	
	public int getCurrentStorage() {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(Constants.STORAGE_ID, -1);
	}

	public String getMIME(File file) {
		// FileNameMap fileNameMap = URLConnection.getFileNameMap();
		// return fileNameMap.getContentTypeFor(file.getAbsolutePath());

		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(getURL(file));
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	public String getLastModif(long lastModified) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return dateFormat.format(lastModified);
	}

	public String getFileSizeOrFilesInside(File file) {

		if (!file.isFile()) {
			if (file.list() == null)
				return "0 items";
			int files = file.list().length;
			if (files == 1)
				return files + " item";
			return files + " items";
		} else {
			double l = (double) file.length();

			if (l > GB)
				return String.format("%.2f Gb", (double) l / GB);
			else if (l < GB && l > MB)
				return String.format("%.2f Mb", (double) l / MB);
			else if (l < MB && l > KB)
				return String.format("%.2f Kb", (double) l / KB);
			else
				return String.format("%.2f Kb", l);
		}

	}

	public String getFileSizeToString(long fileSize) {

		double l = (double) fileSize;

		if (l > GB)
			return String.format("%.2f Gb", (double) l / GB);
		else if (l < GB && l > MB)
			return String.format("%.2f Mb", (double) l / MB);
		else if (l < MB && l > KB)
			return String.format("%.2f Kb", (double) l / KB);
		else
			return String.format("%.2f Kb", l);

	}

	public String getFilePermissions(File file) {
		String permissions = "";
		if (file.isDirectory())
			permissions += "d";
		if (file.canRead())
			permissions += "r";
		if (file.canWrite())
			permissions += "w";
		return permissions;
	}

	public ArrayList<FileInfoItem> getList(String currentFile) {
		File file = new File(currentFile);
		File[] temp = file.listFiles();
		ArrayList<FileInfoItem> filesInfoList = new ArrayList<FileInfoItem>();
		builder = new FileInfoItem.Builder();
		if (!currentFile.equals(Constants.SD_CARD)) {
			builder.setFullPath(new File(currentFile).getParent());
			builder.setDisplayName(" ... ");
			builder.addCollection(true);
			builder.addPreviousFolder(true);
			filesInfoList.add(builder.build());
		}
		for (int i = 0; i < temp.length; i++) {
			builder.setFullPath(temp[i].getAbsolutePath());
			builder.setDisplayName(temp[i].getName());
			builder.setContentLength(getFileSizeOrFilesInside(temp[i]));
			builder.setLastModified(getLastModif(temp[i].lastModified()));
			builder.setContentType(getMIME(temp[i]));
			builder.addCollection(!temp[i].isFile());
			builder.addReadable(temp[i].canRead());
			builder.addWritable(temp[i].canWrite());
			builder.setVisible(!temp[i].isHidden());
			builder.setPublicUrl(getURL(temp[i]));
			builder.addPreviousFolder(false);
			filesInfoList.add(builder.build());
		}

		return filesInfoList;
	}

	private String getURL(File file) {
		return file.toURI().toString();
	}

	public boolean openFile(FileInfoItem fileInfoItem,
			SystemOverviewFragment systemOverviewFragment) {
		Intent open = new Intent();
		open.setAction(android.content.Intent.ACTION_VIEW);
		try {
			open.setDataAndType(
					Uri.fromFile(new File(fileInfoItem.getPublicUrl())),
					fileInfoItem.getContentType());
			Log.d(LOG_TAG, "Open try " + fileInfoItem.getContentType());
			open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			systemOverviewFragment.startActivity(open);
			return true;
		} catch (ActivityNotFoundException e) {
			Log.d(LOG_TAG, "Open " + fileInfoItem.getContentType());// вызывать
																	// открыть
																	// как
			return false;
		}
	}

}
