package com.oleg.diplomfilemanager.fragments;

import java.io.File;
import java.util.ArrayList;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.adapters.SystemOverviewAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class SystemOverviewFragment extends ListFragment{

	private final String SD_CARD = Environment.getExternalStorageDirectory()
			.getPath();
	private final String DISP_DIR = "displayed_directory";
	private String currentDir = SD_CARD;
	private SystemOverviewAdapter overviewAdapter;

	FileManagment fileManagment = new FileManagment(this);
	ArrayList<FileInfoItem> fileInfoItems;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateList(getArguments().getString(DISP_DIR));
		getActivity().setTitle(getCurrentDir());
		
	}

	public void back() {
		if (fileInfoItems.get(0).getFullPath()
				.equals(new File(currentDir).getParent())) {
			updateList(fileInfoItems.get(0).getFullPath());
			setCurrentDir(currentDir, true);
			getActivity().setTitle(getCurrentDir());
		} else
			return;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Toast.makeText(getActivity(), "" + position,
		// Toast.LENGTH_SHORT).show();
		super.onListItemClick(l, v, position, id);
		if (fileInfoItems.get(position).isCollection()) {
			updateList(setCurrentDir(fileInfoItems.get(position).getFullPath(),
					false));
			getActivity().setTitle(getCurrentDir());
		} else
			fileManagment.openFile(fileInfoItems.get(position));
	}

	private String setCurrentDir(String string, boolean getParent) {
		if (getParent) {
			return currentDir = new File(string).getParent();
		}
		return currentDir = string;
	}

	public void updateList(String path) {
		
		fileInfoItems = fileManagment.getList(path);
		overviewAdapter = new SystemOverviewAdapter(
				getActivity(), fileInfoItems);
		setListAdapter(overviewAdapter);
	}
}
