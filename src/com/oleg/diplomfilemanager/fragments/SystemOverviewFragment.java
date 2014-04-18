package com.oleg.diplomfilemanager.fragments;

import java.io.File;
import java.util.ArrayList;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.LoadersControl;
import com.oleg.diplomfilemanager.LongFileOperatoin;
import com.oleg.diplomfilemanager.adapters.SystemOverviewAdapter;

import dialogs.DeleteDialog;
import dialogs.NewFileDirDialog;
import dialogs.RenameDialog;
import dialogs.SearchDialog;
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
		// overviewAdapter = new SystemOverviewAdapter(getActivity(),
		// getCurrentDirFileInfoItems());
		// setListAdapter(overviewAdapter);
		updateList(fileManagment.getList(getArguments().getString(
				Constants.DISP_DIR)));
		getActivity().setTitle(getCurrentDir());
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
				updateList(fileManagment.getList(setCurrentDir(
						currentDirFileInfoItems.get(position).getFullPath(),
						false)));
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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		SharedPreferences preferences = getActivity().getPreferences(
				getActivity().MODE_PRIVATE);
		String temp = preferences.getString(Constants.PREFERENCES_KEY_COPY,
				null);
		String temp1 = preferences.getString(Constants.PREFERENCES_KEY_CUT,
				null);
		// if (!(temp==null)&&!(temp1==null)){
		// TODO програмно добавлять
		menu.add(0, Constants.PHONE_STORAGE_PASTE, 1, "Вставить");
		// }
		menu.add(0, Constants.PHONE_STORAGE_SETTINGS, 6, "Настройки");
		menu.add(0, Constants.PHONE_STORAGE_CREATE_FILE_DIR, 2, "Создать");
		menu.add(0, Constants.PHONE_STORAGE_HOME, 4, "Домой");
		menu.add(0, Constants.PHONE_STORAGE_REFRESH, 5, "Обновить");
		menu.add(0, Constants.PHONE_STORAGE_SEARCH, 3, "Поиск");

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constants.PHONE_STORAGE_PASTE:
			LongFileOperatoin operatoin = new LongFileOperatoin(
					Constants.PHONE_STORAGE_PASTE, null, currentDir, this);
			operatoin.start();
			break;
		case Constants.PHONE_STORAGE_REFRESH:
			updateList(fileManagment.getList(currentDir));
			break;
		case Constants.PHONE_STORAGE_CREATE_FILE_DIR:
			NewFileDirDialog.getInstance(this,currentDir).show(getFragmentManager(), "create_file_or_dir");
			break;
		case Constants.PHONE_STORAGE_SEARCH:
			SearchDialog.getInstanse(currentDir).show(getFragmentManager(), "search");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, Constants.PHONE_STORAGE_COPY, 2, "Копировать");
		menu.add(0, Constants.PHONE_STORAGE_CUT, 1, "Вырезать");
		menu.add(0, Constants.PHONE_STORAGE_DELETE, 3, "Удалить");
		// menu.add(0, Constants.PHONE_STORAGE_PASTE, 3, "Вставить");
		menu.add(0, Constants.PHONE_STORAGE_PROPERTIES, 5, "Свойства");
		menu.add(0, Constants.PHONE_STORAGE_RENAME, 4, "Переименовать");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		SharedPreferences preferences = getActivity().getPreferences(
				getActivity().MODE_PRIVATE);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case Constants.PHONE_STORAGE_CUT:
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
		case Constants.PHONE_STORAGE_DELETE:
			DeleteDialog.getInstance(
					currentDirFileInfoItems.get(info.position), currentDir,
					this).show(getFragmentManager(), "delete");
			break;
		case Constants.PHONE_STORAGE_RENAME:
			RenameDialog.getInstance(
					currentDirFileInfoItems.get(info.position), true, false, this,
					currentDir).show(getFragmentManager(), "rename");
			break;
		case Constants.PHONE_STORAGE_PROPERTIES:

			break;

		}
		return super.onContextItemSelected(item);
	}

	public void back() {
		if (currentDirFileInfoItems.get(0).getFullPath()
				.equals(new File(currentDir).getParent())) {
			updateList(fileManagment.getList(currentDirFileInfoItems.get(0)
					.getFullPath()));
			setCurrentDir(currentDir, true);
			getActivity().setTitle(getCurrentDir());
		} else
			return;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public String setCurrentDir(String string, boolean getParent) {
		if (getParent) {
			return currentDir = new File(string).getParent();
		}
		return currentDir = string;
	}

	public void updateList(ArrayList<FileInfoItem> items) {
		setCurrentDirFileInfoItems(items);
		overviewAdapter = new SystemOverviewAdapter(getActivity(), items);
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
