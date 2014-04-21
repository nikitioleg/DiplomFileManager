package com.oleg.diplomfilemanager.fragments;

import java.util.ArrayList;
import com.oleg.diplomfilemanager.adapters.SearchResultAdapter;
import com.oleg.diplomfilemanager.FileInfoItem;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class SearchResultFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		ArrayList<FileInfoItem> matches = bundle
				.getParcelableArrayList("matches");
		getActivity().setTitle("Результатов поиска - " + matches.size());
		SearchResultAdapter resultAdapter = new SearchResultAdapter(matches, getActivity());
		setListAdapter(resultAdapter);
	}
}
