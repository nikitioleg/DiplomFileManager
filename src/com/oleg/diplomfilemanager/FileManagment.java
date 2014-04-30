package com.oleg.diplomfilemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oleg.diplomfilemanager.dialogs.CopyProgressDialog;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class FileManagment {

	private static FileManagment instance;
	private Context context;
	private SharedPreferences preferences;
	private String currentDir = Constants.SD_CARD;

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

	public String getCurrentDir() {
		return currentDir;
	}

	public String setCurrentDir(String string) {
		return currentDir = string;
	}

	public void setCerrentStorage(int storageID) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt(Constants.STORAGE_ID, storageID).commit();
	}

	public int getCurrentStorage() {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
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

	public boolean createFileOrDir(File file, boolean fileOrDirMark) {
		if (file == null)
			return false;
		if (file.exists()) {
			Toast.makeText(context, "Файл с таким именим существует",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!fileOrDirMark) {
			try {
				file.createNewFile();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.mkdirs();
			return true;
		}
		return false;
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
			filesInfoList.add(getFileItem(builder, temp[i]).build());
		}
		return filesInfoList;
	}

	private FileInfoItem.Builder getFileItem(FileInfoItem.Builder mBuilder,
			File file) {
		mBuilder.setFullPath(file.getAbsolutePath());
		mBuilder.setDisplayName(file.getName());
		mBuilder.setContentLength(getFileSizeOrFilesInside(file));
		mBuilder.setLastModified(getLastModif(file.lastModified()));
		mBuilder.setContentType(getMIME(file));
		mBuilder.addCollection(!file.isFile());
		mBuilder.addReadable(file.canRead());
		mBuilder.addWritable(file.canWrite());
		mBuilder.setVisible(!file.isHidden());
		mBuilder.setPublicUrl(getURL(file));
		mBuilder.addPreviousFolder(false);
		return mBuilder;
	}

	private String getURL(File file) {
		return file.toURI().toString();
	}

	public boolean openFile(FileInfoItem fileInfoItem,
			FragmentActivity fragmentActivity) {
		Intent open = new Intent();
		open.setAction(android.content.Intent.ACTION_VIEW);
		try {
			open.setDataAndType(
					Uri.fromFile(new File(fileInfoItem.getFullPath())),
					fileInfoItem.getContentType());
			Log.d(LOG_TAG, "Open try " + fileInfoItem.getContentType());
			open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			fragmentActivity.startActivity(open);
			return true;
		} catch (ActivityNotFoundException e) {
			Log.d(LOG_TAG, "Open " + fileInfoItem.getContentType());
			// TODO вызывать открыть как

			return false;
		}
	}

	public void copy(String inFullPath, String outFullPath,
			CopyProgressDialog copyProgressDialog) {

		File in = new File(inFullPath);
		File out = new File(outFullPath);

		File[] fileList;
		byte data[] = new byte[2048];
		int publish = 0;
		int step = 0;
		int onePercent = (int) ((in.length()) / 100);
		int progress = 0;
		long downloadedBytes = 0;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		if (in.isDirectory()) {
			createFileOrDir(out, true);
			Log.d("LogTag", "Создано  " + out.getAbsolutePath());
			fileList = in.listFiles();
			for (File file : fileList) {
				copy(file.getAbsolutePath(),
						out.getAbsolutePath() + "/" + file.getName(),
						copyProgressDialog);
			}
		} else {
			try {
				inputStream = new FileInputStream(in);
				outputStream = new FileOutputStream(out);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(
						inputStream);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
						outputStream);
				while ((step = bufferedInputStream.read(data, 0, 2048)) != -1) {
					bufferedOutputStream.write(data, 0, step);
					downloadedBytes += step;
					publish += step;
					if (publish >= onePercent) {
						progress = (int) (downloadedBytes / onePercent);
						copyProgressDialog.updateProgress(inFullPath, progress);

						// publish = 0;
					}
				}
				bufferedOutputStream.flush();
				Log.d("LogTag", "Скачано  " + out.getAbsolutePath());
				bufferedInputStream.close();
				bufferedOutputStream.close();

			} catch (FileNotFoundException e) {
				Log.d(LOG_TAG, "FileNotFoundException");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(LOG_TAG, "IOException");
				e.printStackTrace();
			} finally {
				// if (inputStream != null) {
				// try {
				// inputStream.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// if (outputStream != null) {
				// try {
				// outputStream.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// copyProgressDialog.dismiss();
			}

		}

	}

	public boolean delete(String delFilePath) {
		File delFile = new File(delFilePath);
		File tempFile = delFile;
		if (delFile.isFile() && delFile.canRead() && delFile.exists()) {
			delFile.delete();
			return true;
		} else {
			if (delFile.isDirectory() && delFile.canWrite() && delFile.exists()) {
				File[] files = delFile.listFiles();
				if (files.length == 0) {
					Log.d(LOG_TAG, "" + files.length);
					delFile.delete();
				} else {
					for (File file2 : files) {
						delete(file2.getAbsolutePath());
					}
					tempFile.delete();
				}
				Log.d(LOG_TAG, "" + delFile.getName() + " Удалено");
				return true;
			}
			return false;
		}
	}

	public boolean rename(String string, String newName) {

		// TODO сделать проверку на существование файла

		if (newName.equals("") || newName.equals("Введите имя")) {
			Toast.makeText(context, "Неверное имя файла", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			File oldFile = new File(string);
			File newFile = new File(oldFile.getParent() + "/" + newName);
			return oldFile.renameTo(newFile);
		}
	}

	public ArrayList<FileInfoItem> searchFile(Bundle bundle) {
		ArrayList<FileInfoItem> matchList = new ArrayList<FileInfoItem>();
		File currentDir = new File(getCurrentDir());
		String keyWord = bundle.getString("key_word");
		boolean currentDirSearch = bundle.getBoolean("current_dir", true);
		boolean lowerCase = bundle.getBoolean("lower_case", true);
		boolean subfolder = bundle.getBoolean("subfolder", true);
		String searchFileType = bundle.getString("searchFileType");
		matchList.clear();
		Log.d("myLogs", "Поиск В " + currentDir.getAbsolutePath());
		searchForMatches(currentDir, keyWord, currentDirSearch, lowerCase,
				subfolder, searchFileType, matchList);
		return matchList;
	}

	private void searchForMatches(File currentDir, String keyWord,
			boolean currentDirSearch, boolean lowerCase, boolean subfolder,
			String searchFileType, ArrayList<FileInfoItem> matchList) {
		ArrayList<File> files;
		if (currentDirSearch)
			if (currentDir.listFiles() != null) {
				files = new ArrayList<File>(Arrays.asList(currentDir
						.listFiles()));
			} else {
				return;
			}
		else {
			files = new ArrayList<File>(Arrays.asList(new File(
					Constants.SEARCH_MAIN_DIR).listFiles()));
		}

		Iterator<File> iterator = files.iterator();
		while (iterator.hasNext()) {
			File file = (File) iterator.next();
			Log.d("myLogs", file.getName() + " " + getMIME(file));
			try {
				if (getMIME(file).indexOf(searchFileType) > -1) {
					if (match(file, keyWord, lowerCase)) {
						handleMatch(matchList, file);
					}

				}
			} catch (NullPointerException e) {
				if (searchFileType.equalsIgnoreCase("/"))
					if (match(file, keyWord, lowerCase)) {
						handleMatch(matchList, file);
					}
				if (file.isDirectory() && subfolder) {
					searchForMatches(file, keyWord, true, lowerCase, subfolder,
							searchFileType, matchList);
				}
				Log.d("myLogs", "null searchForMatches");
			}
		}
		return;
	}

	private boolean match(File file, String keyWord, boolean lowerCase) {
		Pattern pattern;
		Matcher matcher;
		boolean match;
		String fileName = file.getName();
		if (lowerCase) {
			String string = Pattern.quote(keyWord.toLowerCase());
			pattern = Pattern.compile(string);
			matcher = pattern.matcher(fileName.toLowerCase());
		} else {
			String string = Pattern.quote(keyWord);
			pattern = Pattern.compile(string);
			matcher = pattern.matcher(fileName);
		}

		match = matcher.find();
		return match;
	}

	private void handleMatch(ArrayList<FileInfoItem> infoItems, File matchedFile) {
		builder = new FileInfoItem.Builder();
		infoItems.add(getFileItem(builder, matchedFile).build());
	}

}
