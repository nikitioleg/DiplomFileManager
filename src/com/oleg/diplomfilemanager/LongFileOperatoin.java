package com.oleg.diplomfilemanager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.app.ListFragment;

import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import dialogs.CopyProgressDialog;

public class LongFileOperatoin extends Thread {

	private ProgressDialog progress;
	private int operationId;
	private String currentPath;
	private SystemOverviewFragment overviewFragment;
	private FileInfoItem fileInfoItem;

	public LongFileOperatoin(int operationId, FileInfoItem fileInfoItem,
			String currentPath, SystemOverviewFragment overviewFragment) {
		this.operationId = operationId;
		this.currentPath = currentPath;
		this.overviewFragment = overviewFragment;
		this.fileInfoItem = fileInfoItem;
		progress = new ProgressDialog(
				((ListFragment) overviewFragment).getActivity());
		progress.setMessage("Удаление файла... ");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setIndeterminate(true);
	}

	@Override
	public void run() {
		switch (operationId) {
		case Constants.PHONE_STORAGE_DELETE:
			try {
				((ListFragment) overviewFragment).getActivity().runOnUiThread(
						new Runnable() {

							@Override
							public void run() {
								progress.show();

							}
						});
				FileManagment.getInstance().delete(fileInfoItem.getFullPath());
			} finally {
				updateList();
			}

			break;

		case Constants.PHONE_STORAGE_PASTE:
			try {
				String inFullPath = getCopyFile();
				if (inFullPath == null)
					break;
				CopyProgressDialog copyProgressDialog = CopyProgressDialog
						.getInstance(overviewFragment,inFullPath);
				copyProgressDialog.show(overviewFragment.getFragmentManager(),
						"copy_progress_dialog");
				FileManagment.getInstance().copy(inFullPath,
						currentPath + getCopyFileName(inFullPath),
						copyProgressDialog);
				if (!isCopy()) {
					FileManagment.getInstance().delete(inFullPath);
				}
				copyProgressDialog.dismiss();
			} finally {
				updateList();
				delCopyPref();
			}
			break;
		}
	}

	private void updateList() {
		((ListFragment) overviewFragment).getActivity().runOnUiThread(
				new Runnable() {

					@Override
					public void run() {
						progress.dismiss();
						overviewFragment.updateList(FileManagment.getInstance()
								.getList(currentPath));
					}
				});
	}

	private boolean isCopy() {
		String temp;
		SharedPreferences preferences = overviewFragment.getActivity()
				.getPreferences(overviewFragment.getActivity().MODE_PRIVATE);
		temp = preferences.getString(Constants.PREFERENCES_KEY_COPY, null);
		if (temp == null)
			return false;
		else {
			return true;
		}

	}

	private String getCopyFile() {
		String temp;
		SharedPreferences preferences = overviewFragment.getActivity()
				.getPreferences(overviewFragment.getActivity().MODE_PRIVATE);
		temp = preferences.getString(Constants.PREFERENCES_KEY_COPY, null);
		if (temp == null)
			temp = preferences.getString(Constants.PREFERENCES_KEY_CUT, null);
		return temp;
	}

	private void delCopyPref() {
		SharedPreferences preferences = overviewFragment.getActivity()
				.getPreferences(overviewFragment.getActivity().MODE_PRIVATE);
		preferences.edit().putString(Constants.PREFERENCES_KEY_CUT, null)
				.putString(Constants.PREFERENCES_KEY_COPY, null).commit();
	}

	public String getCopyFileName(String inFullPath) {
		return inFullPath.substring(inFullPath.lastIndexOf("/"));
	}

}
