package com.oleg.diplomfilemanager;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileManagment {

	private final String LOG_TAG = "myLogs";
	private final int KB = 1024;
	private final int MB = KB * KB;
	private final int GB = MB * KB;
	private final String ROOT = Environment.getExternalStorageDirectory()
			.getPath();

	private Context context;
	private FileInfoItem.Builder builder;

	public FileManagment(Context context) {
		this.context = context;
	}

	public String getMIME(File file) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		return fileNameMap.getContentTypeFor(file.getAbsolutePath());
	}

	public String getLastModif(File file) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return dateFormat.format(file.lastModified());
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
		if (!currentFile.equals(ROOT)){
			builder.setFullPath(new File(currentFile).getParent());
			builder.setDisplayName(" ... ");
			filesInfoList.add(builder.build());
		}
		for (int i = 0; i < temp.length; i++) {
			builder.setFullPath(temp[i].getAbsolutePath());
			builder.setDisplayName(temp[i].getName());
			builder.setContentLength(getFileSizeOrFilesInside(temp[i]));
			builder.setLastModified(getLastModif(temp[i]));
			builder.setContentType(getMIME(temp[i]));
			builder.addCollection(!temp[i].isFile());
			builder.addReadable(temp[i].canRead());
			builder.addWritable(temp[i].canWrite());
			builder.setVisible(!temp[i].isHidden());
			builder.setPublicUrl(getURL(temp[i]));
			filesInfoList.add(builder.build());
		}

		return filesInfoList;
	}

	private String getURL(File file) {
		return file.toURI().toString();
	}

}
