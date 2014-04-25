package com.oleg.diplomfilemanager.fragments;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.adapters.SearchResultAdapter;
import com.oleg.diplomfilemanager.dialogs.DeleteDialog;
import com.oleg.diplomfilemanager.dialogs.RenameDialog;
import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SearchResultFragment extends ListFragment {

	private ArrayList<FileInfoItem> matches;
	private SearchResultAdapter resultAdapter;
	private static SystemOverviewFragment systemOverviewFragment;
	private int menuPos;

	public static SearchResultFragment getInstance(
			SystemOverviewFragment systemFragment,
			ArrayList<FileInfoItem> matchList) {
		systemOverviewFragment = systemFragment;
		Bundle args = new Bundle();
		args.putParcelableArrayList("matches", matchList);
		SearchResultFragment resultFragment = new SearchResultFragment();
		resultFragment.setArguments(args);
		return resultFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		matches = bundle.getParcelableArrayList("matches");
		getActivity().setTitle("Результатов поиска - " + matches.size());
		resultAdapter = new SearchResultAdapter(matches, getActivity());
		setListAdapter(resultAdapter);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, Constants.PHONE_STORAGE_DELETE, 1, "Удалить");
		menu.add(0, Constants.PHONE_STORAGE_PROPERTIES, 2, "Свойства");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case Constants.PHONE_STORAGE_DELETE:
			DeleteDialog.getInstance(matches.get(info.position),
					systemOverviewFragment).show(getFragmentManager(),
					"delete");
			updateMatchList();
			break;
		case Constants.PHONE_STORAGE_PROPERTIES:

			break;

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (matches.get(position).isCollection()) {
			Bundle bundle = new Bundle();
			bundle.putString(Constants.DISP_DIR, matches.get(position)
					.getFullPath());
			SystemOverviewFragment fragment = new SystemOverviewFragment();
			fragment.setArguments(bundle);
			getFragmentManager().beginTransaction()
					.replace(R.id.container, fragment).addToBackStack(null)
					.commit();
		} else {
			FileManagment.getInstance().openFile(matches.get(position),
					getActivity());
		}
		super.onListItemClick(l, v, position, id);
	}

	public void updateMatchList() {
		matches.remove(menuPos);
		resultAdapter.setData(matches);
		resultAdapter.notifyDataSetChanged();
	}

}
