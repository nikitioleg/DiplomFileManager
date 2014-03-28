package com.oleg.diplomfilemanager;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.adapters.SystemOverviewAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SystemOverview extends ListFragment {

	private final String ROOT = Environment.getExternalStorageDirectory()
			.getPath();

	FileManagment fileManagment = new FileManagment(getActivity());
	ArrayList<FileInfoItem> fileInfoItems;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateList(ROOT);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Toast.makeText(getActivity(), "" + position,
		// Toast.LENGTH_SHORT).show();
		super.onListItemClick(l, v, position, id);
		updateList(fileInfoItems.get(position).getFullPath());
	}

	private void updateList(String path) {
		fileInfoItems = fileManagment.getList(path);
		SystemOverviewAdapter overviewAdapter = new SystemOverviewAdapter(
				getActivity(), fileInfoItems);
		setListAdapter(overviewAdapter);
	}
}
