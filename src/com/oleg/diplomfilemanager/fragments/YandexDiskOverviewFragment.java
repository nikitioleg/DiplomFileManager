package com.oleg.diplomfilemanager.fragments;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.LoadersControl;
import com.oleg.diplomfilemanager.LongFileOperatoin;
import com.oleg.diplomfilemanager.R;
import com.oleg.diplomfilemanager.adapters.OverviewAdapter;
import com.oleg.diplomfilemanager.dialogs.DeleteDialog;
import com.oleg.diplomfilemanager.dialogs.RenameDialog;
import com.oleg.diplomfilemanager.dialogs.YandexDiskDownloadDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class YandexDiskOverviewFragment extends ListFragment {

	private OverviewAdapter overviewAdapter;
	private ArrayList<FileInfoItem> currentDirFileInfoItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
		setHasOptionsMenu(true);
		getActivity().setTitle(getArguments().getString(Constants.DISP_DIR));
		LoadersControl control = new LoadersControl(getActivity(), this);
		getLoaderManager().initLoader(Constants.YANDEX_DISK_LOADER, null,
				control);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (getCurrentDirFileInfoItems().get(position).isCollection()) {
			FileManagment.getInstance().setCurrentDir(
					getCurrentDirFileInfoItems().get(position).getFullPath());
			getLoaderManager().destroyLoader(Constants.YANDEX_DISK_LOADER);
			YandexDiskOverviewFragment fragment = new YandexDiskOverviewFragment();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.DISP_DIR, getCurrentDirFileInfoItems()
					.get(position).getFullPath());
			fragment.setArguments(bundle);
			getFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).addToBackStack(null)
					.commit();
		} else {
			YandexDiskDownloadDialog.getInstance(
					getCurrentDirFileInfoItems().get(position), this).show(
					getChildFragmentManager(), null);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		SharedPreferences preferences = getActivity().getPreferences(
				getActivity().MODE_PRIVATE);
		String temp1 = preferences.getString(Constants.PREFERENCES_KEY_CUT,
				null);
		if (temp1 != null) {
			menu.add(0, Constants.PASTE, 1, "Вставить");
		}
		menu.add(0, Constants.REFRESH, 2, "Обновить");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, Constants.CUT, 1, "Вырезать");
		menu.add(0, Constants.DELETE, 3, "Удалить");
		//menu.add(0, Constants.PHONE_STORAGE_PROPERTIES, 4, "Свойства");
		menu.add(0, Constants.RENAME, 2, "Переименовать");
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
			reloadContent(FileManagment.getInstance().getCurrentDir());
			break;
		}
		return super.onOptionsItemSelected(item);
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
			break;

		case Constants.DELETE:
			DeleteDialog.getInstance(
					getCurrentDirFileInfoItems().get(info.position), this)
					.show(getFragmentManager(), "delete");
			break;
		case Constants.RENAME:
			RenameDialog.getInstance(
					getCurrentDirFileInfoItems().get(info.position), true,
					false, this, FileManagment.getInstance().getCurrentDir())
					.show(getFragmentManager(), "rename");
			break;
		case Constants.PHONE_STORAGE_PROPERTIES:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void reloadContent(final String reloadDir) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				YandexDiskOverviewFragment fragment = new YandexDiskOverviewFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.DISP_DIR, reloadDir);
				fragment.setArguments(bundle);
				getFragmentManager().popBackStack();
				getFragmentManager().beginTransaction()
						.replace(R.id.container, fragment).addToBackStack(null)
						.commit();
			}
		});

	}

	public void updateList(ArrayList<FileInfoItem> items) {
		setCurrentDirFileInfoItems(items);
		overviewAdapter = new OverviewAdapter(getActivity(), items);
		setListAdapter(overviewAdapter);
	}

	private void setCurrentDirFileInfoItems(ArrayList<FileInfoItem> arrayList) {
		currentDirFileInfoItems = arrayList;
	}

	private ArrayList<FileInfoItem> getCurrentDirFileInfoItems() {
		return currentDirFileInfoItems;
	}

}
