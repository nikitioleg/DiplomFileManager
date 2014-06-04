package com.oleg.diplomfilemanager.fragments;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.LongFileOperatoin;
import com.oleg.diplomfilemanager.R;
import com.oleg.diplomfilemanager.adapters.OverviewAdapter;
import com.oleg.diplomfilemanager.dialogs.DeleteDialog;
import com.oleg.diplomfilemanager.dialogs.NewFileDirDialog;
import com.oleg.diplomfilemanager.dialogs.RenameDialog;
import com.oleg.diplomfilemanager.dialogs.SearchDialog;
import com.oleg.diplomfilemanager.dialogs.UploadYandexDiskDialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class SystemOverviewFragment extends ListFragment {

	private OverviewAdapter overviewAdapter;
	private FileManagment fileManagment;
	ArrayList<FileInfoItem> currentDirFileInfoItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fileManagment = FileManagment.getInstance();
		String string = getArguments().getString(Constants.DISP_DIR);
		updateList(fileManagment.getList(string));
		getActivity().setTitle(getArguments().getString(Constants.DISP_DIR));
		registerForContextMenu(getListView());
		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {
		Log.d("myLog", "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d("myLog", "onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d("myLog", "onPause");
		super.onPause();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (fileManagment.getCurrentStorage()) {
		case Constants.SD_CARD_STORAGE:
			if (currentDirFileInfoItems.get(position).isCollection()) {

				fileManagment.setCurrentDir(currentDirFileInfoItems.get(
						position).getFullPath());
				SystemOverviewFragment fragment = new SystemOverviewFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.DISP_DIR,
						fileManagment.getCurrentDir());
				fragment.setArguments(bundle);
				getFragmentManager().beginTransaction()
						.replace(R.id.container, fragment).addToBackStack(null)
						.commit();
			} else
				FileManagment.getInstance().openFile(
						currentDirFileInfoItems.get(position), getActivity());
			break;
		case Constants.YANDEX_DISK_STORAGE:
			// if (currentDirFileInfoItems.get(position).isCollection()) {
			// fileManagment.setCurrentDir(currentDirFileInfoItems.get(
			// position).getFullPath());
			//
			// getLoaderManager().destroyLoader(Constants.YANDEX_DISK_LOADER);
			// getLoaderManager().initLoader(Constants.YANDEX_DISK_LOADER,
			// null, LoadersControl.getInstance());
			// } else {
			// YandexDiskDownloadDialog.getInstance(
			// currentDirFileInfoItems.get(position),
			// this).show(getChildFragmentManager(), null);
			// }
			break;
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		SharedPreferences preferences = getActivity().getPreferences(
				getActivity().MODE_PRIVATE);
		String temp1 = preferences.getString(Constants.PREFERENCES_KEY_COPY,
				null);
		String temp2 = preferences.getString(Constants.PREFERENCES_KEY_CUT,
				null);
		if (temp1 != null) {
			menu.add(0, Constants.PASTE, 1, "Вставить");
		}
		if (temp2 != null) {
			menu.add(0, Constants.PASTE, 1, "Вставить");
		}
		// menu.add(0, Constants.PHONE_STORAGE_SETTINGS, 6, "Настройки");
		menu.add(0, Constants.PHONE_STORAGE_CREATE_FILE_DIR, 2, "Создать");
		menu.add(0, Constants.PHONE_STORAGE_HOME, 4, "Домой");
		menu.add(0, Constants.REFRESH, 5, "Обновить");
		menu.add(0, Constants.PHONE_STORAGE_SEARCH, 3, "Поиск");

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constants.PASTE:
			LongFileOperatoin operatoin = new LongFileOperatoin(
					Constants.PASTE, null, this);
			operatoin.start();
			break;
		case Constants.REFRESH:
			updateList(fileManagment.getList(fileManagment.getCurrentDir()));
			break;
		case Constants.PHONE_STORAGE_CREATE_FILE_DIR:
			NewFileDirDialog.getInstance(this, fileManagment.getCurrentDir())
					.show(getFragmentManager(), "create_file_or_dir");
			break;
		case Constants.PHONE_STORAGE_SEARCH:
			SearchDialog.getInstanse(this).show(getFragmentManager(), "search");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, Constants.PHONE_STORAGE_COPY, 2, "Копировать");
		menu.add(0, Constants.CUT, 1, "Вырезать");
		menu.add(0, Constants.DELETE, 3, "Удалить");
		menu.add(0, Constants.SEND_YANDEX_DISK, 5, "Отправить на Яндекс.Диск");
		menu.add(0, Constants.RENAME, 4, "Переименовать");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		SharedPreferences preferences = getActivity().getPreferences(
				getActivity().MODE_PRIVATE);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case Constants.CUT:
			preferences
					.edit()
					.putString(
							Constants.PREFERENCES_KEY_CUT,
							currentDirFileInfoItems.get(info.position)
									.getFullPath())
					.putString(Constants.PREFERENCES_KEY_COPY, null).commit();
			;
			break;

		case Constants.PHONE_STORAGE_COPY:
			preferences
					.edit()
					.putString(
							Constants.PREFERENCES_KEY_COPY,
							currentDirFileInfoItems.get(info.position)
									.getFullPath())
					.putString(Constants.PREFERENCES_KEY_CUT, null).commit();
			break;
		case Constants.DELETE:
			DeleteDialog.getInstance(
					currentDirFileInfoItems.get(info.position), this).show(
					getFragmentManager(), "delete");
			break;
		case Constants.RENAME:
			RenameDialog.getInstance(
					currentDirFileInfoItems.get(info.position), true, false,
					this, fileManagment.getCurrentDir()).show(
					getFragmentManager(), "rename");
			break;
		case Constants.SEND_YANDEX_DISK:
			UploadYandexDiskDialog.getInstance(
					currentDirFileInfoItems.get(info.position), this).show(
					getFragmentManager(), null);
			break;

		}
		return super.onContextItemSelected(item);
	}

	// public void back() {
	// if (currentDirFileInfoItems.get(0).getFullPath()
	// .equals(new File(currentDir).getParent())) {
	// updateList(fileManagment.getList(currentDirFileInfoItems.get(0)
	// .getFullPath()));
	// setCurrentDir(currentDir, true);
	// getActivity().setTitle(getCurrentDir());
	// } else
	// return;
	// }

	public void updateList(ArrayList<FileInfoItem> items) {
		setCurrentDirFileInfoItems(items);
		overviewAdapter = new OverviewAdapter(getActivity(), items);
		setListAdapter(overviewAdapter);
		// overviewAdapter.setData(items);
		// ((BaseAdapter) getListView().getAdapter()).notifyDataSetChanged();

	}

	public void setCurrentDirFileInfoItems(ArrayList<FileInfoItem> arrayList) {
		currentDirFileInfoItems = arrayList;
	}

	private ArrayList<FileInfoItem> getCurrentDirFileInfoItems() {
		return currentDirFileInfoItems;
	}

}
