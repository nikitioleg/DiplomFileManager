package com.oleg.diplomfilemanager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;

import com.oleg.diplomfilemanager.dialogs.CopyProgressDialog;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import com.oleg.diplomfilemanager.fragments.YandexDiskOverviewFragment;

public class LongFileOperatoin extends Thread {

	private ProgressDialog progress;
	private int operationId;
	private String currentPath;
	private ListFragment listFragment;
	private FileInfoItem fileInfoItem;

	public LongFileOperatoin(int operationId, FileInfoItem fileInfoItem,
			ListFragment listFragment) {
		this.operationId = operationId;
		this.currentPath = FileManagment.getInstance().getCurrentDir();
		this.listFragment = listFragment;
		this.fileInfoItem = fileInfoItem;
		progress = new ProgressDialog(listFragment.getActivity());
		progress.setMessage("Удаление файла... ");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
	}

	@Override
	public void run() {
		switch (operationId) {
		case Constants.DELETE:
			try {
				listFragment.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progress.show();

					}
				});
				int storageID = FileManagment.getInstance().getCurrentStorage();
				switch (storageID) {
				case Constants.SD_CARD_STORAGE:
					FileManagment.getInstance().delete(
							fileInfoItem.getFullPath());
					break;
				case Constants.YANDEX_DISK_STORAGE:
					YandexDiskManagment.getInstance().deleteYandexDiskFile(
							fileInfoItem);
					break;
				}

			} finally {
				int storageID = FileManagment.getInstance().getCurrentStorage();
				switch (storageID) {
				case Constants.SD_CARD_STORAGE:
					updateFileList();
					break;
				case Constants.YANDEX_DISK_STORAGE:
					((YandexDiskOverviewFragment) listFragment)
							.reloadContent(currentPath);
					progress.dismiss();
					break;
				}
			}

			break;

		case Constants.PASTE:
			try {
				int storageID = FileManagment.getInstance().getCurrentStorage();
				switch (storageID) {
				case Constants.SD_CARD_STORAGE:

					String inFullPath = getCopyFile();
					if (inFullPath == null)
						break;
					CopyProgressDialog copyProgressDialog = CopyProgressDialog
							.getInstance(
									((SystemOverviewFragment) listFragment),
									inFullPath);
					copyProgressDialog.show(listFragment.getFragmentManager(),
							"copy_progress_dialog");
					FileManagment.getInstance().copy(inFullPath,
							currentPath + getCopyFileName(inFullPath),
							copyProgressDialog);
					if (!isCopy()) {
						FileManagment.getInstance().delete(inFullPath);
					}
					copyProgressDialog.dismiss();

					break;
				case Constants.YANDEX_DISK_STORAGE:
					String targetFile = listFragment
							.getActivity()
							.getPreferences(
									listFragment.getActivity().MODE_PRIVATE)
							.getString(Constants.PREFERENCES_KEY_CUT, null);
					YandexDiskManagment.getInstance().moveYandexDiskFile(
							targetFile, getYndexDiskCutFile(targetFile));
					break;
				}
			} finally {
				int storageID = FileManagment.getInstance().getCurrentStorage();
				switch (storageID) {
				case Constants.SD_CARD_STORAGE:
					updateFileList();
					break;
				case Constants.YANDEX_DISK_STORAGE:
					((YandexDiskOverviewFragment) listFragment)
							.reloadContent(currentPath);
					break;
				}
				delCopyPref();
			}
			break;

		}
	}

	private String getYndexDiskCutFile(String string) {
		String temp = currentPath;
		int i = string.lastIndexOf("/");
		if (currentPath.equalsIgnoreCase("/"))
			i++;
		temp += string.substring(i);
		return temp;
	}

	private void updateFileList() {
		((ListFragment) listFragment).getActivity().runOnUiThread(
				new Runnable() {

					@Override
					public void run() {
						progress.dismiss();
						((SystemOverviewFragment) listFragment)
								.updateList(FileManagment.getInstance()
										.getList(currentPath));
					}
				});
	}

	private boolean isCopy() {
		String temp;
		SharedPreferences preferences = listFragment.getActivity()
				.getPreferences(listFragment.getActivity().MODE_PRIVATE);
		temp = preferences.getString(Constants.PREFERENCES_KEY_COPY, null);
		if (temp == null)
			return false;
		else {
			return true;
		}

	}

	private String getCopyFile() {
		String temp;
		SharedPreferences preferences = listFragment.getActivity()
				.getPreferences(listFragment.getActivity().MODE_PRIVATE);
		temp = preferences.getString(Constants.PREFERENCES_KEY_COPY, null);
		if (temp == null)
			temp = preferences.getString(Constants.PREFERENCES_KEY_CUT, null);
		return temp;
	}

	private void delCopyPref() {
		SharedPreferences preferences = listFragment.getActivity()
				.getPreferences(listFragment.getActivity().MODE_PRIVATE);
		preferences.edit().putString(Constants.PREFERENCES_KEY_CUT, null)
				.putString(Constants.PREFERENCES_KEY_COPY, null).commit();
	}

	public String getCopyFileName(String inFullPath) {
		return inFullPath.substring(inFullPath.lastIndexOf("/"));
	}

}
