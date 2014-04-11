package com.oleg.diplomfilemanager.fragments;

import java.io.File;
import java.util.ArrayList;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.LoadersControl;
import com.oleg.diplomfilemanager.adapters.SystemOverviewAdapter;

import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SystemOverviewFragment extends ListFragment {

	private String currentDir = Constants.SD_CARD;
	private SystemOverviewAdapter overviewAdapter;
	private FileManagment fileManagment;
	ArrayList<FileInfoItem> currentDirFileInfoItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fileManagment = FileManagment.getInstance();
		setCurrentDirFileInfoItems(fileManagment.getList(getArguments()
				.getString(Constants.DISP_DIR)));
		updateList(getCurrentDirFileInfoItems());
		getActivity().setTitle(getCurrentDir());
	}

	public void back() {
		if (currentDirFileInfoItems.get(0).getFullPath()
				.equals(new File(currentDir).getParent())) {
			setCurrentDirFileInfoItems(fileManagment
					.getList(currentDirFileInfoItems.get(0).getFullPath()));
			updateList(getCurrentDirFileInfoItems());
			setCurrentDir(currentDir, true);
			getActivity().setTitle(getCurrentDir());
		} else
			return;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {// лист
																			// файлов
																			// получать
																			// через
																			// Loader
		super.onListItemClick(l, v, position, id);
		switch (fileManagment.getCurrentStorage()) {
		case Constants.SD_CARD_STORAGE:
			if (currentDirFileInfoItems.get(position).isCollection()) {
				setCurrentDirFileInfoItems(fileManagment.getList(setCurrentDir(
						currentDirFileInfoItems.get(position).getFullPath(),
						false)));
				updateList(getCurrentDirFileInfoItems());
				getActivity().setTitle(getCurrentDir());
			} else
				FileManagment.getInstance().openFile(
						currentDirFileInfoItems.get(position), this);
			break;
		case Constants.YANDEX_DISK_STORAGE:
			if (currentDirFileInfoItems.get(position).isCollection()) {
				setCurrentDir(currentDirFileInfoItems.get(position)
						.getFullPath(), false);
				getLoaderManager().destroyLoader(Constants.YANDEX_DISK_LOADER);
				getLoaderManager().initLoader(Constants.YANDEX_DISK_LOADER,
						null, LoadersControl.getInstance());
			} else {
				Toast.makeText(getActivity(), "Тут будет скачивание файла",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}

	public String setCurrentDir(String string, boolean getParent) {
		if (getParent) {
			return currentDir = new File(string).getParent();
		}
		return currentDir = string;
	}

	public void updateList(ArrayList<FileInfoItem> items) {
		// getListView().setAdapter(null);
		overviewAdapter = new SystemOverviewAdapter(getActivity(), items);
		Log.d("myLog", "liat not updated");
		try {
			setListAdapter(overviewAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("myLog", "liat updated");
	}

	public void setCurrentDirFileInfoItems(ArrayList<FileInfoItem> arrayList) {
		currentDirFileInfoItems = arrayList;
	}

	private ArrayList<FileInfoItem> getCurrentDirFileInfoItems() {
		return currentDirFileInfoItems;
	}

	public SystemOverviewAdapter getAdapter() {
		return overviewAdapter;
	}

	public void removeAdapter() {
		overviewAdapter = null;
	}
}
